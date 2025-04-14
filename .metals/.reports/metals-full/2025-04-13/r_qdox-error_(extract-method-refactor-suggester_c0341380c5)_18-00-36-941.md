error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8394.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8394.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8394.java
text:
```scala
f@@ragments = highlighter.getBestFragments(fieldQuery, reader, docId, mapper.names().indexName(), field.fragmentCharSize(), numberOfFragments);

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

package org.elasticsearch.search.highlight;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.vectorhighlight.*;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.common.collect.ImmutableMap;
import org.elasticsearch.common.io.FastStringReader;
import org.elasticsearch.common.lucene.document.SingleFieldSelector;
import org.elasticsearch.index.mapper.DocumentMapper;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.index.mapper.Uid;
import org.elasticsearch.search.SearchException;
import org.elasticsearch.search.SearchParseElement;
import org.elasticsearch.search.fetch.FetchPhaseExecutionException;
import org.elasticsearch.search.fetch.SearchHitPhase;
import org.elasticsearch.search.highlight.vectorhighlight.SourceScoreOrderFragmentsBuilder;
import org.elasticsearch.search.highlight.vectorhighlight.SourceSimpleFragmentsBuilder;
import org.elasticsearch.search.internal.InternalSearchHit;
import org.elasticsearch.search.internal.SearchContext;
import org.elasticsearch.search.lookup.SearchLookup;

import java.io.IOException;
import java.util.*;

import static org.elasticsearch.common.collect.Maps.*;

/**
 * @author kimchy (shay.banon)
 */
public class HighlightPhase implements SearchHitPhase {

    private static final Encoder DEFAULT_ENCODER = new DefaultEncoder();

    @Override public Map<String, ? extends SearchParseElement> parseElements() {
        return ImmutableMap.of("highlight", new HighlighterParseElement());
    }

    @Override public boolean executionNeeded(SearchContext context) {
        return context.highlight() != null;
    }

    @Override public void execute(SearchContext context, InternalSearchHit hit, Uid uid, IndexReader reader, int docId) throws ElasticSearchException {
        try {
            DocumentMapper documentMapper = context.mapperService().documentMapper(hit.type());

            Map<String, HighlightField> highlightFields = newHashMap();
            for (SearchContextHighlight.Field field : context.highlight().fields()) {
                FieldMapper mapper = documentMapper.mappers().smartNameFieldMapper(field.field());
                if (mapper == null) {
                    throw new SearchException(context.shardTarget(), "No mapping found for [" + field.field() + "]");
                }

                // if we can do highlighting using Term Vectors, use FastVectorHighlighter, otherwise, use the
                // slower plain highlighter
                if (mapper.termVector() != Field.TermVector.WITH_POSITIONS_OFFSETS) {
                    if (!context.queryRewritten()) {
                        try {
                            context.updateRewriteQuery(context.searcher().rewrite(context.query()));
                        } catch (IOException e) {
                            throw new FetchPhaseExecutionException(context, "Failed to highlight field [" + field.field() + "]", e);
                        }
                    }
                    // Don't use the context.query() since it might be rewritten, and we need to pass the non rewritten queries to
                    // let the highlighter handle MultiTerm ones
                    QueryScorer queryScorer = new QueryScorer(context.parsedQuery().query(), null);
                    queryScorer.setExpandMultiTermQuery(true);
                    Fragmenter fragmenter;
                    if (field.numberOfFragments() == 0) {
                        fragmenter = new NullFragmenter();
                    } else {
                        fragmenter = new SimpleSpanFragmenter(queryScorer, field.fragmentCharSize());
                    }
                    Formatter formatter = new SimpleHTMLFormatter(field.preTags()[0], field.postTags()[0]);
                    Highlighter highlighter = new Highlighter(formatter, DEFAULT_ENCODER, queryScorer);
                    highlighter.setTextFragmenter(fragmenter);

                    List<Object> textsToHighlight;
                    if (mapper.stored()) {
                        try {
                            Document doc = reader.document(docId, new SingleFieldSelector(mapper.names().indexName()));
                            textsToHighlight = new ArrayList<Object>(doc.getFields().size());
                            for (Fieldable docField : doc.getFields()) {
                                if (docField.stringValue() != null) {
                                    textsToHighlight.add(docField.stringValue());
                                }
                            }
                        } catch (Exception e) {
                            throw new FetchPhaseExecutionException(context, "Failed to highlight field [" + field.field() + "]", e);
                        }
                    } else {
                        SearchLookup lookup = context.lookup();
                        lookup.setNextReader(reader);
                        lookup.setNextDocId(docId);
                        textsToHighlight = lookup.source().getValues(mapper.names().fullName());
                    }

                    ArrayList<TextFragment> fragsList = new ArrayList<TextFragment>();
                    try {
                        for (Object textToHighlight : textsToHighlight) {
                            String text = textToHighlight.toString();
                            Analyzer analyzer = context.mapperService().documentMapper(hit.type()).mappers().indexAnalyzer();
                            TokenStream tokenStream = analyzer.reusableTokenStream(mapper.names().indexName(), new FastStringReader(text));
                            TextFragment[] bestTextFragments = highlighter.getBestTextFragments(tokenStream, text, false, field.numberOfFragments());
                            Collections.addAll(fragsList, bestTextFragments);
                        }
                    } catch (Exception e) {
                        throw new FetchPhaseExecutionException(context, "Failed to highlight field [" + field.field() + "]", e);
                    }
                    if (field.scoreOrdered()) {
                        Collections.sort(fragsList, new Comparator<TextFragment>() {
                            public int compare(TextFragment o1, TextFragment o2) {
                                return Math.round(o2.getScore() - o1.getScore());
                            }
                        });
                    }
                    int numberOfFragments = fragsList.size() < field.numberOfFragments() ? fragsList.size() : field.numberOfFragments();
                    String[] fragments = new String[numberOfFragments];
                    for (int i = 0; i < fragments.length; i++) {
                        fragments[i] = fragsList.get(i).toString();
                    }
                    HighlightField highlightField = new HighlightField(field.field(), fragments);
                    highlightFields.put(highlightField.name(), highlightField);
                } else {
                    FastVectorHighlighter highlighter = buildHighlighter(context, mapper, field);
                    FieldQuery fieldQuery = buildFieldQuery(highlighter, context.query(), reader, field);

                    String[] fragments;
                    try {
                        // a HACK to make highlighter do highlighting, even though its using the single frag list builder
                        int numberOfFragments = field.numberOfFragments() == 0 ? 1 : field.numberOfFragments();
                        fragments = highlighter.getBestFragments(fieldQuery, context.searcher().getIndexReader(), docId, mapper.names().indexName(), field.fragmentCharSize(), numberOfFragments);
                    } catch (IOException e) {
                        throw new FetchPhaseExecutionException(context, "Failed to highlight field [" + field.field() + "]", e);
                    }
                    HighlightField highlightField = new HighlightField(field.field(), fragments);
                    highlightFields.put(highlightField.name(), highlightField);
                }
            }

            hit.highlightFields(highlightFields);
        } finally {
            CustomFieldQuery.reader.remove();
            CustomFieldQuery.highlightFilters.remove();
        }
    }

    private FieldQuery buildFieldQuery(FastVectorHighlighter highlighter, Query query, IndexReader indexReader, SearchContextHighlight.Field field) {
        CustomFieldQuery.reader.set(indexReader);
        CustomFieldQuery.highlightFilters.set(field.highlightFilter());
        return new CustomFieldQuery(query, highlighter);
    }

    private FastVectorHighlighter buildHighlighter(SearchContext searchContext, FieldMapper fieldMapper, SearchContextHighlight.Field field) {
        FragListBuilder fragListBuilder;
        FragmentsBuilder fragmentsBuilder;
        if (field.numberOfFragments() == 0) {
            fragListBuilder = new SingleFragListBuilder();
            if (fieldMapper.stored()) {
                fragmentsBuilder = new SimpleFragmentsBuilder(field.preTags(), field.postTags());
            } else {
                fragmentsBuilder = new SourceSimpleFragmentsBuilder(fieldMapper, searchContext, field.preTags(), field.postTags());
            }
        } else {
            fragListBuilder = new SimpleFragListBuilder();
            if (field.scoreOrdered()) {
                if (fieldMapper.stored()) {
                    fragmentsBuilder = new ScoreOrderFragmentsBuilder(field.preTags(), field.postTags());
                } else {
                    fragmentsBuilder = new SourceScoreOrderFragmentsBuilder(fieldMapper, searchContext, field.preTags(), field.postTags());
                }
            } else {
                if (fieldMapper.stored()) {
                    fragmentsBuilder = new SimpleFragmentsBuilder(field.preTags(), field.postTags());
                } else {
                    fragmentsBuilder = new SourceSimpleFragmentsBuilder(fieldMapper, searchContext, field.preTags(), field.postTags());
                }
            }
        }

        return new FastVectorHighlighter(true, false, fragListBuilder, fragmentsBuilder);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8394.java