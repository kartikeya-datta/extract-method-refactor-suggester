error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1422.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1422.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1422.java
text:
```scala
g@@et, request.id(), request.type(), validFields.toArray(Strings.EMPTY_ARRAY), null, false);

/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.index.termvectors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.*;
import org.apache.lucene.index.memory.MemoryIndex;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.termvector.TermVectorRequest;
import org.elasticsearch.action.termvector.TermVectorResponse;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.lucene.uid.Versions;
import org.elasticsearch.common.regex.Regex;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.get.GetField;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.index.mapper.Uid;
import org.elasticsearch.index.mapper.core.StringFieldMapper;
import org.elasticsearch.index.mapper.internal.UidFieldMapper;
import org.elasticsearch.index.settings.IndexSettings;
import org.elasticsearch.index.shard.AbstractIndexShardComponent;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.index.shard.service.IndexShard;

import java.io.IOException;
import java.util.*;

/**
 */

public class ShardTermVectorService extends AbstractIndexShardComponent {

    private IndexShard indexShard;

    @Inject
    public ShardTermVectorService(ShardId shardId, @IndexSettings Settings indexSettings) {
        super(shardId, indexSettings);
    }

    // sadly, to overcome cyclic dep, we need to do this and inject it ourselves...
    public ShardTermVectorService setIndexShard(IndexShard indexShard) {
        this.indexShard = indexShard;
        return this;
    }

    public TermVectorResponse getTermVector(TermVectorRequest request) {
        final Engine.Searcher searcher = indexShard.acquireSearcher("term_vector");
        IndexReader topLevelReader = searcher.reader();
        final TermVectorResponse termVectorResponse = new TermVectorResponse(request.index(), request.type(), request.id());
        final Term uidTerm = new Term(UidFieldMapper.NAME, Uid.createUidAsBytes(request.type(), request.id()));
        try {
            Fields topLevelFields = MultiFields.getFields(topLevelReader);
            Versions.DocIdAndVersion docIdAndVersion = Versions.loadDocIdAndVersion(topLevelReader, uidTerm);
            if (docIdAndVersion != null) {
                /* handle potential wildcards in fields */
                if (request.selectedFields() != null) {
                    handleFieldWildcards(request);
                }
                /* generate term vectors if not available */
                Fields termVectorsByField = docIdAndVersion.context.reader().getTermVectors(docIdAndVersion.docId);
                if (request.selectedFields() != null) {
                    termVectorsByField = generateTermVectorsIfNeeded(termVectorsByField, request, uidTerm, false);
                }
                termVectorResponse.setFields(termVectorsByField, request.selectedFields(), request.getFlags(), topLevelFields);
                termVectorResponse.setExists(true);
                termVectorResponse.setDocVersion(docIdAndVersion.version);
            } else {
                termVectorResponse.setExists(false);
            }
        } catch (Throwable ex) {
            throw new ElasticsearchException("failed to execute term vector request", ex);
        } finally {
            searcher.close();
        }
        return termVectorResponse;
    }

    private void handleFieldWildcards(TermVectorRequest request) {
        Set<String> fieldNames = new HashSet<>();
        for (String pattern : request.selectedFields()) {
            fieldNames.addAll(indexShard.mapperService().simpleMatchToIndexNames(pattern));
        }
        request.selectedFields(fieldNames.toArray(Strings.EMPTY_ARRAY));
    }

    private Fields generateTermVectorsIfNeeded(Fields termVectorsByField, TermVectorRequest request, Term uidTerm, boolean realTime) throws IOException {
        List<String> validFields = new ArrayList<>();
        for (String field : request.selectedFields()) {
            FieldMapper fieldMapper = indexShard.mapperService().smartNameFieldMapper(field);
            if (!(fieldMapper instanceof StringFieldMapper)) {
                continue;
            }
            if (fieldMapper.fieldType().storeTermVectors()) {
                continue;
            }
            // only disallow fields which are not indexed
            if (!fieldMapper.fieldType().indexed()) {
                continue;
            }
            validFields.add(field);
        }
        if (validFields.isEmpty()) {
            return termVectorsByField;
        }

        Engine.GetResult get = indexShard.get(new Engine.Get(realTime, uidTerm));
        Fields generatedTermVectors;
        try {
            if (!get.exists()) {
                return termVectorsByField;
            }
            // TODO: support for fetchSourceContext?
            GetResult getResult = indexShard.getService().get(
                    get, request.id(), request.type(), validFields.toArray(Strings.EMPTY_ARRAY), null);
            generatedTermVectors = generateTermVectors(getResult.getFields().values(), request.offsets());
        } finally {
            get.release();
        }
        if (termVectorsByField == null) {
            return generatedTermVectors;
        } else {
            return mergeFields(request.selectedFields().toArray(Strings.EMPTY_ARRAY), termVectorsByField, generatedTermVectors);
        }
    }

    private Fields generateTermVectors(Collection<GetField> getFields, boolean withOffsets) throws IOException {
        // store document in memory index
        MemoryIndex index = new MemoryIndex(withOffsets);
        for (GetField getField : getFields) {
            String field = getField.getName();
            Analyzer analyzer = indexShard.mapperService().smartNameFieldMapper(field).indexAnalyzer();
            if (analyzer == null) {
                analyzer = indexShard.mapperService().analysisService().defaultIndexAnalyzer();
            }
            for (Object text : getField.getValues()) {
                index.addField(field, text.toString(), analyzer);
            }
        }
        // and read vectors from it
        return MultiFields.getFields(index.createSearcher().getIndexReader());
    }

    private Fields mergeFields(String[] fieldNames, Fields... fieldsObject) throws IOException {
        ParallelFields parallelFields = new ParallelFields();
        for (Fields fieldObject : fieldsObject) {
            assert fieldObject != null;
            for (String fieldName : fieldNames) {
                Terms terms = fieldObject.terms(fieldName);
                if (terms != null) {
                    parallelFields.addField(fieldName, terms);
                }
            }
        }
        return parallelFields;
    }

    // Poached from Lucene ParallelAtomicReader
    private static final class ParallelFields extends Fields {
        final Map<String,Terms> fields = new TreeMap<>();

        ParallelFields() {
        }

        void addField(String fieldName, Terms terms) {
            fields.put(fieldName, terms);
        }

        @Override
        public Iterator<String> iterator() {
            return Collections.unmodifiableSet(fields.keySet()).iterator();
        }

        @Override
        public Terms terms(String field) {
            return fields.get(field);
        }

        @Override
        public int size() {
            return fields.size();
        }
    }

}
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:48)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:97)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:489)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7(Indexer.scala:361)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7$adapted(Indexer.scala:356)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:619)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:617)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1306)
	scala.collection.parallel.ParIterableLike$Foreach.leaf(ParIterableLike.scala:938)
	scala.collection.parallel.Task.$anonfun$tryLeaf$1(Tasks.scala:52)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.util.control.Breaks$$anon$1.catchBreak(Breaks.scala:97)
	scala.collection.parallel.Task.tryLeaf(Tasks.scala:55)
	scala.collection.parallel.Task.tryLeaf$(Tasks.scala:49)
	scala.collection.parallel.ParIterableLike$Foreach.tryLeaf(ParIterableLike.scala:935)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1422.java