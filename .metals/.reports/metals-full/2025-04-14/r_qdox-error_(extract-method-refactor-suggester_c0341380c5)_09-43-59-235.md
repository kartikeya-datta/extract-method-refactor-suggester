error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/84.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/84.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/84.java
text:
```scala
t@@extsToHighlight = HighlightUtils.loadFieldValues(mapper, context, hitContext);

/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
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

import com.google.common.collect.Maps;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.util.CollectionUtil;
import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.common.text.StringText;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.search.fetch.FetchPhaseExecutionException;
import org.elasticsearch.search.fetch.FetchSubPhase;
import org.elasticsearch.search.internal.SearchContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class PlainHighlighter implements Highlighter {

    private static final String CACHE_KEY = "highlight-plain";

    @Override
    public String[] names() {
        return new String[] { "plain", "highlighter" };
    }

    public HighlightField highlight(HighlighterContext highlighterContext) {
        SearchContextHighlight.Field field = highlighterContext.field;
        SearchContext context = highlighterContext.context;
        FetchSubPhase.HitContext hitContext = highlighterContext.hitContext;
        FieldMapper<?> mapper = highlighterContext.mapper;

        Encoder encoder = field.encoder().equals("html") ? HighlightUtils.Encoders.HTML : HighlightUtils.Encoders.DEFAULT;

        if (!hitContext.cache().containsKey(CACHE_KEY)) {
            Map<FieldMapper<?>, org.apache.lucene.search.highlight.Highlighter> mappers = Maps.newHashMap();
            hitContext.cache().put(CACHE_KEY, mappers);
        }
        Map<FieldMapper<?>, org.apache.lucene.search.highlight.Highlighter> cache = (Map<FieldMapper<?>, org.apache.lucene.search.highlight.Highlighter>) hitContext.cache().get(CACHE_KEY);

        org.apache.lucene.search.highlight.Highlighter entry = cache.get(mapper);
        if (entry == null) {
            Query query = highlighterContext.query.originalQuery();
            QueryScorer queryScorer = new CustomQueryScorer(query, field.requireFieldMatch() ? mapper.names().indexName() : null);
            queryScorer.setExpandMultiTermQuery(true);
            Fragmenter fragmenter;
            if (field.numberOfFragments() == 0) {
                fragmenter = new NullFragmenter();
            } else if (field.fragmenter() == null) {
                fragmenter = new SimpleSpanFragmenter(queryScorer, field.fragmentCharSize());
            } else if ("simple".equals(field.fragmenter())) {
                fragmenter = new SimpleFragmenter(field.fragmentCharSize());
            } else if ("span".equals(field.fragmenter())) {
                fragmenter = new SimpleSpanFragmenter(queryScorer, field.fragmentCharSize());
            } else {
                throw new ElasticSearchIllegalArgumentException("unknown fragmenter option [" + field.fragmenter() + "] for the field [" + highlighterContext.fieldName + "]");
            }
            Formatter formatter = new SimpleHTMLFormatter(field.preTags()[0], field.postTags()[0]);

            entry = new org.apache.lucene.search.highlight.Highlighter(formatter, encoder, queryScorer);
            entry.setTextFragmenter(fragmenter);
            // always highlight across all data
            entry.setMaxDocCharsToAnalyze(Integer.MAX_VALUE);

            cache.put(mapper, entry);
        }

        // a HACK to make highlighter do highlighting, even though its using the single frag list builder
        int numberOfFragments = field.numberOfFragments() == 0 ? 1 : field.numberOfFragments();
        ArrayList<TextFragment> fragsList = new ArrayList<TextFragment>();
        List<Object> textsToHighlight;

        try {
            textsToHighlight = HighlightUtils.loadFieldValues(mapper, context, hitContext, field.forceSource());

            for (Object textToHighlight : textsToHighlight) {
                String text = textToHighlight.toString();
                Analyzer analyzer = context.mapperService().documentMapper(hitContext.hit().type()).mappers().indexAnalyzer();
                TokenStream tokenStream = analyzer.tokenStream(mapper.names().indexName(), text);
                if (!tokenStream.hasAttribute(CharTermAttribute.class) || !tokenStream.hasAttribute(OffsetAttribute.class)) {
                    // can't perform highlighting if the stream has no terms (binary token stream) or no offsets
                    continue;
                }
                TextFragment[] bestTextFragments = entry.getBestTextFragments(tokenStream, text, false, numberOfFragments);
                for (TextFragment bestTextFragment : bestTextFragments) {
                    if (bestTextFragment != null && bestTextFragment.getScore() > 0) {
                        fragsList.add(bestTextFragment);
                    }
                }
            }
        } catch (Exception e) {
            throw new FetchPhaseExecutionException(context, "Failed to highlight field [" + highlighterContext.fieldName + "]", e);
        }
        if (field.scoreOrdered()) {
            CollectionUtil.introSort(fragsList, new Comparator<TextFragment>() {
                public int compare(TextFragment o1, TextFragment o2) {
                    return Math.round(o2.getScore() - o1.getScore());
                }
            });
        }
        String[] fragments;
        // number_of_fragments is set to 0 but we have a multivalued field
        if (field.numberOfFragments() == 0 && textsToHighlight.size() > 1 && fragsList.size() > 0) {
            fragments = new String[fragsList.size()];
            for (int i = 0; i < fragsList.size(); i++) {
                fragments[i] = fragsList.get(i).toString();
            }
        } else {
            // refine numberOfFragments if needed
            numberOfFragments = fragsList.size() < numberOfFragments ? fragsList.size() : numberOfFragments;
            fragments = new String[numberOfFragments];
            for (int i = 0; i < fragments.length; i++) {
                fragments[i] = fragsList.get(i).toString();
            }
        }

        if (fragments != null && fragments.length > 0) {
            return new HighlightField(highlighterContext.fieldName, StringText.convertFromStringArray(fragments));
        }

        int noMatchSize = highlighterContext.field.noMatchSize();
        if (noMatchSize > 0 && textsToHighlight.size() > 0) {
            // Pull an excerpt from the beginning of the string but make sure to split the string on a term boundary.
            String fieldContents = textsToHighlight.get(0).toString();
            Analyzer analyzer = context.mapperService().documentMapper(hitContext.hit().type()).mappers().indexAnalyzer();
            int end;
            try {
                end = findGoodEndForNoHighlightExcerpt(noMatchSize, analyzer.tokenStream(mapper.names().indexName(), fieldContents));
            } catch (Exception e) {
                throw new FetchPhaseExecutionException(context, "Failed to highlight field [" + highlighterContext.fieldName + "]", e);
            }
            if (end > 0) {
                return new HighlightField(highlighterContext.fieldName, new Text[] { new StringText(fieldContents.substring(0, end)) });
            }
        }
        return null;
    }

    private static int findGoodEndForNoHighlightExcerpt(int noMatchSize, TokenStream tokenStream) throws IOException {
        try {
            if (!tokenStream.hasAttribute(OffsetAttribute.class)) {
                // Can't split on term boundaries without offsets
                return -1;
            }
            int end = -1;
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                OffsetAttribute attr = tokenStream.getAttribute(OffsetAttribute.class);
                if (attr.endOffset() >= noMatchSize) {
                    // Jump to the end of this token if it wouldn't put us past the boundary
                    if (attr.endOffset() == noMatchSize) {
                        end = noMatchSize;
                    }
                    return end;
                }
                end = attr.endOffset();
            }
            // We've exhausted the token stream so we should just highlight everything.
            return end;
        } finally {
            tokenStream.end();
            tokenStream.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/84.java