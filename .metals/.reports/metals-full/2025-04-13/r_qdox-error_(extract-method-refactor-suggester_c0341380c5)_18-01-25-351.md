error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3241.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3241.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3241.java
text:
```scala
L@@ist<Object> textsToHighlight = HighlightUtils.loadFieldValues(fieldMapper, context, hitContext, field.forceSource());

/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.elasticsearch.search.highlight;

import com.google.common.collect.Maps;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.Encoder;
import org.apache.lucene.search.postingshighlight.CustomPassageFormatter;
import org.apache.lucene.search.postingshighlight.CustomPostingsHighlighter;
import org.apache.lucene.search.postingshighlight.Snippet;
import org.apache.lucene.search.postingshighlight.WholeBreakIterator;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.CollectionUtil;
import org.apache.lucene.util.UnicodeUtil;
import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.text.StringText;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.search.fetch.FetchPhaseExecutionException;
import org.elasticsearch.search.fetch.FetchSubPhase;
import org.elasticsearch.search.internal.SearchContext;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.*;

public class PostingsHighlighter implements Highlighter {

    private static final String CACHE_KEY = "highlight-postings";

    @Override
    public String[] names() {
        return new String[]{"postings", "postings-highlighter"};
    }

    @Override
    public HighlightField highlight(HighlighterContext highlighterContext) {

        FieldMapper<?> fieldMapper = highlighterContext.mapper;
        SearchContextHighlight.Field field = highlighterContext.field;
        if (fieldMapper.fieldType().indexOptions() != FieldInfo.IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS) {
            throw new ElasticSearchIllegalArgumentException("the field [" + field.field() + "] should be indexed with positions and offsets in the postings list to be used with postings highlighter");
        }

        SearchContext context = highlighterContext.context;
        FetchSubPhase.HitContext hitContext = highlighterContext.hitContext;

        if (!hitContext.cache().containsKey(CACHE_KEY)) {
            //get the non rewritten query and rewrite it
            Query query;
            try {
                query = rewrite(highlighterContext, hitContext.topLevelReader());
            } catch (IOException e) {
                throw new FetchPhaseExecutionException(context, "Failed to highlight field [" + highlighterContext.fieldName + "]", e);
            }
            SortedSet<Term> queryTerms = extractTerms(query);
            hitContext.cache().put(CACHE_KEY, new HighlighterEntry(queryTerms));
        }

        HighlighterEntry highlighterEntry = (HighlighterEntry) hitContext.cache().get(CACHE_KEY);
        MapperHighlighterEntry mapperHighlighterEntry = highlighterEntry.mappers.get(fieldMapper);

        if (mapperHighlighterEntry == null) {
            Encoder encoder = field.encoder().equals("html") ? HighlightUtils.Encoders.HTML : HighlightUtils.Encoders.DEFAULT;
            CustomPassageFormatter passageFormatter = new CustomPassageFormatter(field.preTags()[0], field.postTags()[0], encoder);
            BytesRef[] filteredQueryTerms = filterTerms(highlighterEntry.queryTerms, fieldMapper.names().indexName(), field.requireFieldMatch());
            mapperHighlighterEntry = new MapperHighlighterEntry(passageFormatter, filteredQueryTerms);
        }

        //we merge back multiple values into a single value using the paragraph separator, unless we have to highlight every single value separately (number_of_fragments=0).
        boolean mergeValues = field.numberOfFragments() != 0;
        List<Snippet> snippets = new ArrayList<Snippet>();
        int numberOfFragments;

        try {
            //we manually load the field values (from source if needed)
            List<Object> textsToHighlight = HighlightUtils.loadFieldValues(fieldMapper, context, hitContext);
            CustomPostingsHighlighter highlighter = new CustomPostingsHighlighter(mapperHighlighterEntry.passageFormatter, textsToHighlight, mergeValues, Integer.MAX_VALUE-1, field.noMatchSize());

             if (field.numberOfFragments() == 0) {
                highlighter.setBreakIterator(new WholeBreakIterator());
                numberOfFragments = 1; //1 per value since we highlight per value
            } else {
                numberOfFragments = field.numberOfFragments();
            }

            //we highlight every value separately calling the highlight method multiple times, only if we need to have back a snippet per value (whole value)
            int values = mergeValues ? 1 : textsToHighlight.size();
            for (int i = 0; i < values; i++) {
                Snippet[] fieldSnippets = highlighter.highlightDoc(fieldMapper.names().indexName(), mapperHighlighterEntry.filteredQueryTerms, hitContext.searcher(), hitContext.docId(), numberOfFragments);
                if (fieldSnippets != null) {
                    for (Snippet fieldSnippet : fieldSnippets) {
                        if (Strings.hasText(fieldSnippet.getText())) {
                            snippets.add(fieldSnippet);
                        }
                    }
                }
            }

        } catch(IOException e) {
            throw new FetchPhaseExecutionException(context, "Failed to highlight field [" + highlighterContext.fieldName + "]", e);
        }

        snippets = filterSnippets(snippets, field.numberOfFragments());

        if (field.scoreOrdered()) {
            //let's sort the snippets by score if needed
            CollectionUtil.introSort(snippets, new Comparator<Snippet>() {
                public int compare(Snippet o1, Snippet o2) {
                    return (int) Math.signum(o2.getScore() - o1.getScore());
                }
            });
        }

        String[] fragments = new String[snippets.size()];
        for (int i = 0; i < fragments.length; i++) {
            fragments[i] = snippets.get(i).getText();
        }

        if (fragments.length > 0) {
            return new HighlightField(highlighterContext.fieldName, StringText.convertFromStringArray(fragments));
        }

        return null;
    }

    private static Query rewrite(HighlighterContext highlighterContext, IndexReader reader) throws IOException {
        //rewrite is expensive: if the query was already rewritten we try not to rewrite
        boolean mustRewrite = !highlighterContext.query.queryRewritten();

        Query original = highlighterContext.query.originalQuery();

        MultiTermQuery originalMultiTermQuery = null;
        MultiTermQuery.RewriteMethod originalRewriteMethod = null;
        if (original instanceof MultiTermQuery) {
            originalMultiTermQuery = (MultiTermQuery) original;
            if (!allowsForTermExtraction(originalMultiTermQuery.getRewriteMethod())) {
                originalRewriteMethod = originalMultiTermQuery.getRewriteMethod();
                originalMultiTermQuery.setRewriteMethod(new MultiTermQuery.TopTermsScoringBooleanQueryRewrite(50));
                //we need to rewrite anyway if it is a multi term query which was rewritten with the wrong rewrite method
                mustRewrite = true;
            }
        }

        if (!mustRewrite) {
            //return the rewritten query
            return highlighterContext.query.query();
        }

        Query query = original;
        for (Query rewrittenQuery = query.rewrite(reader); rewrittenQuery != query;
             rewrittenQuery = query.rewrite(reader)) {
            query = rewrittenQuery;
        }

        if (originalMultiTermQuery != null) {
            if (originalRewriteMethod != null) {
                //set back the original rewrite method after the rewrite is done
                originalMultiTermQuery.setRewriteMethod(originalRewriteMethod);
            }
        }

        return query;
    }

    private static boolean allowsForTermExtraction(MultiTermQuery.RewriteMethod rewriteMethod) {
        return rewriteMethod instanceof TopTermsRewrite || rewriteMethod instanceof ScoringRewrite;
    }

    private static SortedSet<Term> extractTerms(Query query) {
        SortedSet<Term> queryTerms = new TreeSet<Term>();
        query.extractTerms(queryTerms);
        return queryTerms;
    }

    private static BytesRef[] filterTerms(SortedSet<Term> queryTerms, String field, boolean requireFieldMatch) {
        SortedSet<Term> fieldTerms;
        if (requireFieldMatch) {
            Term floor = new Term(field, "");
            Term ceiling = new Term(field, UnicodeUtil.BIG_TERM);
            fieldTerms = queryTerms.subSet(floor, ceiling);
        } else {
            fieldTerms = queryTerms;
        }

        BytesRef terms[] = new BytesRef[fieldTerms.size()];
        int termUpto = 0;
        for(Term term : fieldTerms) {
            terms[termUpto++] = term.bytes();
        }

        return terms;
    }

    private static List<Snippet> filterSnippets(List<Snippet> snippets, int numberOfFragments) {

        //We need to filter the snippets as due to no_match_size we could have
        //either highlighted snippets together non highlighted ones
        //We don't want to mix those up
        List<Snippet> filteredSnippets = new ArrayList<Snippet>(snippets.size());
        for (Snippet snippet : snippets) {
            if (snippet.isHighlighted()) {
                filteredSnippets.add(snippet);
            }
        }

        //if there's at least one highlighted snippet, we return all the highlighted ones
        //otherwise we return the first non highlighted one if available
        if (filteredSnippets.size() == 0) {
            if (snippets.size() > 0) {
                Snippet snippet = snippets.get(0);
                //if we did discrete per value highlighting using whole break iterator (as number_of_fragments was 0)
                //we need to obtain the first sentence of the first value
                if (numberOfFragments == 0) {
                    BreakIterator bi = BreakIterator.getSentenceInstance(Locale.ROOT);
                    String text = snippet.getText();
                    bi.setText(text);
                    int next = bi.next();
                    if (next != BreakIterator.DONE) {
                        String newText = text.substring(0, next).trim();
                        snippet = new Snippet(newText, snippet.getScore(), snippet.isHighlighted());
                    }
                }
                filteredSnippets.add(snippet);
            }
        }

        return filteredSnippets;
    }

    private static class HighlighterEntry {
        final SortedSet<Term> queryTerms;
        Map<FieldMapper<?>, MapperHighlighterEntry> mappers = Maps.newHashMap();

        private HighlighterEntry(SortedSet<Term> queryTerms) {
            this.queryTerms = queryTerms;
        }
    }

    private static class MapperHighlighterEntry {
        final CustomPassageFormatter passageFormatter;
        final BytesRef[] filteredQueryTerms;

        private MapperHighlighterEntry(CustomPassageFormatter passageFormatter, BytesRef[] filteredQueryTerms) {
            this.passageFormatter = passageFormatter;
            this.filteredQueryTerms = filteredQueryTerms;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3241.java