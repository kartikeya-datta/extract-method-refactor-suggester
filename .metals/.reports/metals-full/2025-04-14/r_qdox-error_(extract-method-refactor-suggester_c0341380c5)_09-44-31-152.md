error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7412.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7412.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7412.java
text:
```scala
final L@@ist<CompletionSuggestion.Entry.Option> options = new ArrayList<>(results.values());

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
package org.elasticsearch.search.suggest.completion;

import com.google.common.collect.Maps;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;
import org.apache.lucene.search.suggest.Lookup;
import org.apache.lucene.util.CharsRef;
import org.apache.lucene.util.CollectionUtil;
import org.apache.lucene.util.UnicodeUtil;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.text.StringText;
import org.elasticsearch.index.mapper.core.CompletionFieldMapper;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestContextParser;
import org.elasticsearch.search.suggest.Suggester;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion.Entry.Option;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class CompletionSuggester extends Suggester<CompletionSuggestionContext> {

    private static final ScoreComparator scoreComparator = new ScoreComparator();


    @Override
    protected Suggest.Suggestion<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>> innerExecute(String name,
            CompletionSuggestionContext suggestionContext, IndexReader indexReader, CharsRef spare) throws IOException {
        if (suggestionContext.mapper() == null || !(suggestionContext.mapper() instanceof CompletionFieldMapper)) {
            throw new ElasticsearchException("Field [" + suggestionContext.getField() + "] is not a completion suggest field");
        }

        CompletionSuggestion completionSuggestion = new CompletionSuggestion(name, suggestionContext.getSize());
        UnicodeUtil.UTF8toUTF16(suggestionContext.getText(), spare);

        CompletionSuggestion.Entry completionSuggestEntry = new CompletionSuggestion.Entry(new StringText(spare.toString()), 0, spare.length());
        completionSuggestion.addTerm(completionSuggestEntry);

        String fieldName = suggestionContext.getField();
        Map<String, CompletionSuggestion.Entry.Option> results = Maps.newHashMapWithExpectedSize(indexReader.leaves().size() * suggestionContext.getSize());
        for (AtomicReaderContext atomicReaderContext : indexReader.leaves()) {
            AtomicReader atomicReader = atomicReaderContext.reader();
            Terms terms = atomicReader.fields().terms(fieldName);
            if (terms instanceof Completion090PostingsFormat.CompletionTerms) {
                final Completion090PostingsFormat.CompletionTerms lookupTerms = (Completion090PostingsFormat.CompletionTerms) terms;
                final Lookup lookup = lookupTerms.getLookup(suggestionContext.mapper(), suggestionContext);
                if (lookup == null) {
                    // we don't have a lookup for this segment.. this might be possible if a merge dropped all
                    // docs from the segment that had a value in this segment.
                    continue;
                }
                List<Lookup.LookupResult> lookupResults = lookup.lookup(spare, false, suggestionContext.getSize());
                for (Lookup.LookupResult res : lookupResults) {

                    final String key = res.key.toString();
                    final float score = res.value;
                    final Option value = results.get(key);
                    if (value == null) {
                        final Option option = new CompletionSuggestion.Entry.Option(new StringText(key), score, res.payload == null ? null
                                : new BytesArray(res.payload));
                        results.put(key, option);
                    } else if (value.getScore() < score) {
                        value.setScore(score);
                        value.setPayload(res.payload == null ? null : new BytesArray(res.payload));
                    }
                }
            }
        }
        final List<CompletionSuggestion.Entry.Option> options = new ArrayList<CompletionSuggestion.Entry.Option>(results.values());
        CollectionUtil.introSort(options, scoreComparator);

        int optionCount = Math.min(suggestionContext.getSize(), options.size());
        for (int i = 0 ; i < optionCount ; i++) {
            completionSuggestEntry.addOption(options.get(i));
        }

        return completionSuggestion;
    }

    @Override
    public String[] names() {
        return new String[] { "completion" };
    }

    @Override
    public SuggestContextParser getContextParser() {
        return new CompletionSuggestParser(this);
    }

    public static class ScoreComparator implements Comparator<CompletionSuggestion.Entry.Option> {
        @Override
        public int compare(Option o1, Option o2) {
            return Float.compare(o2.getScore(), o1.getScore());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7412.java