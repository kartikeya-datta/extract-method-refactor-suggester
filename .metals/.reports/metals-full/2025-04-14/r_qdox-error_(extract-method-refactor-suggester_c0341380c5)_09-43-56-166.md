error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6626.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6626.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6626.java
text:
```scala
D@@ocumentMapper documentMapper = context.mapperService().documentMapper(uid.type());

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

package org.elasticsearch.search.fetch;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldSelector;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexReader;
import org.elasticsearch.common.collect.ImmutableMap;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.index.mapper.*;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchParseElement;
import org.elasticsearch.search.SearchPhase;
import org.elasticsearch.search.fetch.explain.ExplainSearchHitPhase;
import org.elasticsearch.search.fetch.matchedfilters.MatchedFiltersSearchHitPhase;
import org.elasticsearch.search.fetch.script.ScriptFieldsSearchHitPhase;
import org.elasticsearch.search.highlight.HighlightPhase;
import org.elasticsearch.search.internal.InternalSearchHit;
import org.elasticsearch.search.internal.InternalSearchHitField;
import org.elasticsearch.search.internal.InternalSearchHits;
import org.elasticsearch.search.internal.SearchContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kimchy (shay.banon)
 */
public class FetchPhase implements SearchPhase {

    private final SearchHitPhase[] hitPhases;

    @Inject public FetchPhase(HighlightPhase highlightPhase, ScriptFieldsSearchHitPhase scriptFieldsPhase,
                              MatchedFiltersSearchHitPhase matchFiltersPhase, ExplainSearchHitPhase explainPhase) {
        this.hitPhases = new SearchHitPhase[]{scriptFieldsPhase, matchFiltersPhase, explainPhase, highlightPhase};
    }

    @Override public Map<String, ? extends SearchParseElement> parseElements() {
        ImmutableMap.Builder<String, SearchParseElement> parseElements = ImmutableMap.builder();
        parseElements.put("fields", new FieldsParseElement());
        for (SearchHitPhase hitPhase : hitPhases) {
            parseElements.putAll(hitPhase.parseElements());
        }
        return parseElements.build();
    }

    @Override public void preProcess(SearchContext context) {
    }

    public void execute(SearchContext context) {
        FieldSelector fieldSelector = buildFieldSelectors(context);

        InternalSearchHit[] hits = new InternalSearchHit[context.docIdsToLoadSize()];
        for (int index = 0; index < context.docIdsToLoadSize(); index++) {
            int docId = context.docIdsToLoad()[context.docIdsToLoadFrom() + index];
            Document doc = loadDocument(context, fieldSelector, docId);
            Uid uid = extractUid(context, doc);

            DocumentMapper documentMapper = context.mapperService().type(uid.type());

            byte[] source = extractSource(doc, documentMapper);

            InternalSearchHit searchHit = new InternalSearchHit(docId, uid.id(), uid.type(), source, null);
            hits[index] = searchHit;

            for (Object oField : doc.getFields()) {
                Fieldable field = (Fieldable) oField;
                String name = field.name();

                // ignore UID, we handled it above
                if (name.equals(UidFieldMapper.NAME)) {
                    continue;
                }

                // ignore source, we handled it above
                if (name.equals(SourceFieldMapper.NAME)) {
                    continue;
                }

                Object value = null;
                FieldMappers fieldMappers = documentMapper.mappers().indexName(field.name());
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

                if (searchHit.fieldsOrNull() == null) {
                    searchHit.fields(new HashMap<String, SearchHitField>(2));
                }

                SearchHitField hitField = searchHit.fields().get(name);
                if (hitField == null) {
                    hitField = new InternalSearchHitField(name, new ArrayList<Object>(2));
                    searchHit.fields().put(name, hitField);
                }
                hitField.values().add(value);
            }

            boolean hitPhaseExecutionRequired = false;
            for (SearchHitPhase hitPhase : hitPhases) {
                if (hitPhase.executionNeeded(context)) {
                    hitPhaseExecutionRequired = true;
                    break;
                }
            }

            if (hitPhaseExecutionRequired) {
                int readerIndex = context.searcher().readerIndex(docId);
                IndexReader subReader = context.searcher().subReaders()[readerIndex];
                int subDoc = docId - context.searcher().docStarts()[readerIndex];
                for (SearchHitPhase hitPhase : hitPhases) {
                    if (hitPhase.executionNeeded(context)) {
                        hitPhase.execute(context, searchHit, uid, subReader, subDoc);
                    }
                }
            }
        }
        context.fetchResult().hits(new InternalSearchHits(hits, context.queryResult().topDocs().totalHits, context.queryResult().topDocs().getMaxScore()));
    }

    private byte[] extractSource(Document doc, DocumentMapper documentMapper) {
        Fieldable sourceField = doc.getFieldable(SourceFieldMapper.NAME);
        if (sourceField != null) {
            return documentMapper.sourceMapper().nativeValue(sourceField);
        }
        return null;
    }

    private Uid extractUid(SearchContext context, Document doc) {
        // TODO we might want to use FieldData here to speed things up, so we don't have to load it at all...
        String sUid = doc.get(UidFieldMapper.NAME);
        if (sUid != null) {
            return Uid.createUid(sUid);
        }
        // no type, nothing to do (should not really happen
        throw new FetchPhaseExecutionException(context, "Failed to load uid from the index");
    }

    private Document loadDocument(SearchContext context, FieldSelector fieldSelector, int docId) {
        try {
            return context.searcher().doc(docId, fieldSelector);
        } catch (IOException e) {
            throw new FetchPhaseExecutionException(context, "Failed to fetch doc id [" + docId + "]", e);
        }
    }

    private FieldSelector buildFieldSelectors(SearchContext context) {
        if (context.hasScriptFields() && !context.hasFieldNames()) {
            // we ask for script fields, and no field names, don't load the source
            return UidFieldSelector.INSTANCE;
        }

        if (!context.hasFieldNames()) {
            return new UidAndSourceFieldSelector();
        }

        if (context.fieldNames().isEmpty()) {
            return UidFieldSelector.INSTANCE;
        }

        // asked for all stored fields, just return null so all of them will be loaded
        // don't load the source field in this case, makes little sense to get it with all stored fields
        if (context.fieldNames().get(0).equals("*")) {
            return AllButSourceFieldSelector.INSTANCE;
        }

        FieldMappersFieldSelector fieldSelector = new FieldMappersFieldSelector();
        for (String fieldName : context.fieldNames()) {
            FieldMappers x = context.mapperService().smartNameFieldMappers(fieldName);
            if (x == null) {
                throw new FetchPhaseExecutionException(context, "No mapping for field [" + fieldName + "]");
            }
            fieldSelector.add(x);
        }
        fieldSelector.add(UidFieldMapper.NAME);
        return fieldSelector;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6626.java