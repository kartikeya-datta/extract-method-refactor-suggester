error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6847.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6847.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6847.java
text:
```scala
t@@hrow new ElasticsearchIllegalArgumentException("source is forced for fields " +  fieldNamesToHighlight + " but type [" + hitContext.hit().type() + "] has disabled _source");

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

package org.elasticsearch.search.highlight;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.apache.lucene.index.FieldInfo;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.ElasticsearchIllegalArgumentException;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.regex.Regex;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.mapper.DocumentMapper;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.index.mapper.MapperService;
import org.elasticsearch.index.mapper.internal.SourceFieldMapper;
import org.elasticsearch.search.SearchParseElement;
import org.elasticsearch.search.fetch.FetchSubPhase;
import org.elasticsearch.search.internal.InternalSearchHit;
import org.elasticsearch.search.internal.SearchContext;

import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Maps.newHashMap;

/**
 *
 */
public class HighlightPhase extends AbstractComponent implements FetchSubPhase {

    private final Highlighters highlighters;

    @Inject
    public HighlightPhase(Settings settings, Highlighters highlighters) {
        super(settings);
        this.highlighters = highlighters;
    }

    @Override
    public Map<String, ? extends SearchParseElement> parseElements() {
        return ImmutableMap.of("highlight", new HighlighterParseElement());
    }

    @Override
    public boolean hitsExecutionNeeded(SearchContext context) {
        return false;
    }

    @Override
    public void hitsExecute(SearchContext context, InternalSearchHit[] hits) throws ElasticsearchException {
    }

    @Override
    public boolean hitExecutionNeeded(SearchContext context) {
        return context.highlight() != null;
    }

    @Override
    public void hitExecute(SearchContext context, HitContext hitContext) throws ElasticsearchException {
        Map<String, HighlightField> highlightFields = newHashMap();
        for (SearchContextHighlight.Field field : context.highlight().fields()) {
            Set<String> fieldNamesToHighlight;
            if (Regex.isSimpleMatchPattern(field.field())) {
                DocumentMapper documentMapper = context.mapperService().documentMapper(hitContext.hit().type());
                fieldNamesToHighlight = documentMapper.mappers().simpleMatchToFullName(field.field());
            } else {
                fieldNamesToHighlight = ImmutableSet.of(field.field());
            }

            if (field.forceSource()) {
                SourceFieldMapper sourceFieldMapper = context.mapperService().documentMapper(hitContext.hit().type()).sourceMapper();
                if (!sourceFieldMapper.enabled()) {
                    throw new ElasticsearchIllegalArgumentException("source is forced for field [" +  field.field() + "] but type [" + hitContext.hit().type() + "] has disabled _source");
                }
            }

            for (String fieldName : fieldNamesToHighlight) {
                FieldMapper<?> fieldMapper = getMapperForField(fieldName, context, hitContext);
                if (fieldMapper == null) {
                    continue;
                }

                if (field.highlighterType() == null) {
                    boolean useFastVectorHighlighter = fieldMapper.fieldType().storeTermVectors() && fieldMapper.fieldType().storeTermVectorOffsets() && fieldMapper.fieldType().storeTermVectorPositions();
                    if (useFastVectorHighlighter) {
                        field.highlighterType("fvh");
                    } else if (fieldMapper.fieldType().indexOptions() == FieldInfo.IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS) {
                        field.highlighterType("postings");
                    } else {
                        field.highlighterType("plain");
                    }
                }

                Highlighter highlighter = highlighters.get(field.highlighterType());
                if (highlighter == null) {
                    throw new ElasticsearchIllegalArgumentException("unknown highlighter type [" + field.highlighterType() + "] for the field [" + fieldName + "]");
                }

                HighlighterContext.HighlightQuery highlightQuery;
                if (field.highlightQuery() == null) {
                    highlightQuery = new HighlighterContext.HighlightQuery(context.parsedQuery().query(), context.query(), context.queryRewritten());
                } else {
                    highlightQuery = new HighlighterContext.HighlightQuery(field.highlightQuery(), field.highlightQuery(), false);
                }
                HighlighterContext highlighterContext = new HighlighterContext(fieldName, field, fieldMapper, context, hitContext, highlightQuery);
                HighlightField highlightField = highlighter.highlight(highlighterContext);
                if (highlightField != null) {
                    highlightFields.put(highlightField.name(), highlightField);
                }
            }
        }

        hitContext.hit().highlightFields(highlightFields);
    }

    private FieldMapper<?> getMapperForField(String fieldName, SearchContext searchContext, HitContext hitContext) {
        DocumentMapper documentMapper = searchContext.mapperService().documentMapper(hitContext.hit().type());
        FieldMapper<?> mapper = documentMapper.mappers().smartNameFieldMapper(fieldName);
        if (mapper == null) {
            MapperService.SmartNameFieldMappers fullMapper = searchContext.mapperService().smartName(fieldName);
            if (fullMapper == null || !fullMapper.hasDocMapper() || fullMapper.docMapper().type().equals(hitContext.hit().type())) {
                return null;
            }
            mapper = fullMapper.mapper();
        }

        return mapper;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6847.java