error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9678.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9678.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9678.java
text:
```scala
s@@ource = documentMapper.sourceMapper().nativeValue(sourceField);

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
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

package org.elasticsearch.action.get;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldSelector;
import org.apache.lucene.document.Fieldable;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.TransportActions;
import org.elasticsearch.action.support.single.TransportSingleOperationAction;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.block.ClusterBlockLevel;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.lucene.Lucene;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.mapper.*;
import org.elasticsearch.index.service.IndexService;
import org.elasticsearch.index.shard.service.IndexShard;
import org.elasticsearch.indices.IndicesService;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static org.elasticsearch.common.collect.Maps.*;

/**
 * Performs the get operation.
 *
 * @author kimchy (shay.banon)
 */
public class TransportGetAction extends TransportSingleOperationAction<GetRequest, GetResponse> {

    @Inject public TransportGetAction(Settings settings, ClusterService clusterService, TransportService transportService,
                                      IndicesService indicesService, ThreadPool threadPool) {
        super(settings, threadPool, clusterService, transportService, indicesService);
    }

    @Override protected String transportAction() {
        return TransportActions.GET;
    }

    @Override protected String transportShardAction() {
        return "indices/get/shard";
    }

    @Override protected void checkBlock(GetRequest request, ClusterState state) {
        state.blocks().indexBlockedRaiseException(ClusterBlockLevel.READ, request.index());
    }

    @Override protected GetResponse shardOperation(GetRequest request, int shardId) throws ElasticSearchException {
        IndexService indexService = indicesService.indexServiceSafe(request.index());
        IndexShard indexShard = indexService.shardSafe(shardId);

        DocumentMapper docMapper = indexService.mapperService().type(request.type());
        if (docMapper == null) {
            throw new DocumentMapperNotFoundException("No mapper found for type [" + request.type() + "]");
        }

        Engine.Searcher searcher = indexShard.searcher();
        boolean exists = false;
        byte[] source = null;
        Map<String, GetField> fields = null;
        try {
            int docId = Lucene.docId(searcher.reader(), docMapper.uidMapper().term(request.type(), request.id()));
            if (docId != Lucene.NO_DOC) {
                exists = true;
                FieldSelector fieldSelector = buildFieldSelectors(docMapper, request.fields());
                if (fieldSelector != null) {
                    Document doc = searcher.reader().document(docId, fieldSelector);
                    source = extractSource(doc, docMapper);

                    for (Object oField : doc.getFields()) {
                        Fieldable field = (Fieldable) oField;
                        String name = field.name();
                        Object value = null;
                        FieldMappers fieldMappers = docMapper.mappers().indexName(field.name());
                        if (fieldMappers != null) {
                            FieldMapper mapper = fieldMappers.mapper();
                            if (mapper != null) {
                                name = mapper.names().fullName();
                                value = mapper.valueForSearch(field);
                            }
                        }
                        if (value == null) {
                            if (field.isBinary()) {
                                value = field.getBinaryValue();
                            } else {
                                value = field.stringValue();
                            }
                        }

                        if (fields == null) {
                            fields = newHashMapWithExpectedSize(2);
                        }

                        GetField getField = fields.get(name);
                        if (getField == null) {
                            getField = new GetField(name, new ArrayList<Object>(2));
                            fields.put(name, getField);
                        }
                        getField.values().add(value);
                    }
                }
            }
        } catch (IOException e) {
            throw new ElasticSearchException("Failed to get type [" + request.type() + "] and id [" + request.id() + "]", e);
        } finally {
            searcher.release();
        }
        return new GetResponse(request.index(), request.type(), request.id(), exists, source, fields);
    }

    private FieldSelector buildFieldSelectors(DocumentMapper docMapper, String... fields) {
        if (fields == null) {
            return docMapper.sourceMapper().fieldSelector();
        }

        // don't load anything
        if (fields.length == 0) {
            return null;
        }

        FieldMappersFieldSelector fieldSelector = new FieldMappersFieldSelector();
        for (String fieldName : fields) {
            FieldMappers x = docMapper.mappers().smartName(fieldName);
            if (x == null) {
                throw new ElasticSearchException("No mapping for field [" + fieldName + "] in type [" + docMapper.type() + "]");
            }
            fieldSelector.add(x);
        }
        return fieldSelector;
    }

    private byte[] extractSource(Document doc, DocumentMapper documentMapper) {
        byte[] source = null;
        Fieldable sourceField = doc.getFieldable(documentMapper.sourceMapper().names().indexName());
        if (sourceField != null) {
            source = documentMapper.sourceMapper().value(sourceField);
            doc.removeField(documentMapper.sourceMapper().names().indexName());
        }
        return source;
    }

    @Override protected GetRequest newRequest() {
        return new GetRequest();
    }

    @Override protected GetResponse newResponse() {
        return new GetResponse();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9678.java