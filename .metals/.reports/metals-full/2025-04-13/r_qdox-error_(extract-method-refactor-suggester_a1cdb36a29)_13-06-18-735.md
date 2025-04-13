error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9978.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9978.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9978.java
text:
```scala
b@@uilder.field("prefix_len", prefixLength);

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
package org.elasticsearch.search.suggest.term;
import java.io.IOException;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.suggest.SuggestBuilder.SuggestionBuilder;

/**
 * Defines the actual suggest command. Each command uses the global options
 * unless defined in the suggestion itself. All options are the same as the
 * global options, but are only applicable for this suggestion.
 */
public class TermSuggestionBuilder extends SuggestionBuilder<TermSuggestionBuilder> {

    private String suggestMode;
    private Float accuracy;
    private String sort;
    private String stringDistance;
    private Integer maxEdits;
    private Integer maxInspections;
    private Float maxTermFreq;
    private Integer prefixLength;
    private Integer minWordLength;
    private Float minDocFreq;
    
    /**
     * @param name
     *            The name of this suggestion. This is a required parameter.
     */
    public TermSuggestionBuilder(String name) {
        super(name, "term");
    }

    /**
     * The global suggest mode controls what suggested terms are included or
     * controls for what suggest text tokens, terms should be suggested for.
     * Three possible values can be specified:
     * <ol>
     * <li><code>missing</code> - Only suggest terms in the suggest text that
     * aren't in the index. This is the default.
     * <li><code>popular</code> - Only suggest terms that occur in more docs
     * then the original suggest text term.
     * <li><code>always</code> - Suggest any matching suggest terms based on
     * tokens in the suggest text.
     * </ol>
     */
    public TermSuggestionBuilder suggestMode(String suggestMode) {
        this.suggestMode = suggestMode;
        return this;
    }

    /**
     * s how similar the suggested terms at least need to be compared to the
     * original suggest text tokens. A value between 0 and 1 can be specified.
     * This value will be compared to the string distance result of each
     * candidate spelling correction.
     * <p/>
     * Default is <tt>0.5</tt>
     */
    public TermSuggestionBuilder setAccuracy(float accuracy) {
        this.accuracy = accuracy;
        return this;
    }

    /**
     * Sets how to sort the suggest terms per suggest text token. Two possible
     * values:
     * <ol>
     * <li><code>score</code> - Sort should first be based on score, then
     * document frequency and then the term itself.
     * <li><code>frequency</code> - Sort should first be based on document
     * frequency, then scotr and then the term itself.
     * </ol>
     * <p/>
     * What the score is depends on the suggester being used.
     */
    public TermSuggestionBuilder sort(String sort) {
        this.sort = sort;
        return this;
    }

    /**
     * Sets what string distance implementation to use for comparing how similar
     * suggested terms are. Four possible values can be specified:
     * <ol>
     * <li><code>internal</code> - This is the default and is based on
     * <code>damerau_levenshtein</code>, but highly optimized for comparing
     * string distance for terms inside the index.
     * <li><code>damerau_levenshtein</code> - String distance algorithm based on
     * Damerau-Levenshtein algorithm.
     * <li><code>levenstein</code> - String distance algorithm based on
     * Levenstein edit distance algorithm.
     * <li><code>jarowinkler</code> - String distance algorithm based on
     * Jaro-Winkler algorithm.
     * <li><code>ngram</code> - String distance algorithm based on character
     * n-grams.
     * </ol>
     */
    public TermSuggestionBuilder stringDistance(String stringDistance) {
        this.stringDistance = stringDistance;
        return this;
    }

    /**
     * Sets the maximum edit distance candidate suggestions can have in order to
     * be considered as a suggestion. Can only be a value between 1 and 2. Any
     * other value result in an bad request error being thrown. Defaults to
     * <tt>2</tt>.
     */
    public TermSuggestionBuilder maxEdits(Integer maxEdits) {
        this.maxEdits = maxEdits;
        return this;
    }

    /**
     * A factor that is used to multiply with the size in order to inspect more
     * candidate suggestions. Can improve accuracy at the cost of performance.
     * Defaults to <tt>5</tt>.
     */
    public TermSuggestionBuilder maxInspections(Integer maxInspections) {
        this.maxInspections = maxInspections;
        return this;
    }

    /**
     * Sets a maximum threshold in number of documents a suggest text token can
     * exist in order to be corrected. Can be a relative percentage number (e.g
     * 0.4) or an absolute number to represent document frequencies. If an value
     * higher than 1 is specified then fractional can not be specified. Defaults
     * to <tt>0.01</tt>.
     * <p/>
     * This can be used to exclude high frequency terms from being suggested.
     * High frequency terms are usually spelled correctly on top of this this
     * also improves the suggest performance.
     */
    public TermSuggestionBuilder maxTermFreq(float maxTermFreq) {
        this.maxTermFreq = maxTermFreq;
        return this;
    }

    /**
     * Sets the number of minimal prefix characters that must match in order be
     * a candidate suggestion. Defaults to 1. Increasing this number improves
     * suggest performance. Usually misspellings don't occur in the beginning of
     * terms.
     */
    public TermSuggestionBuilder prefixLength(int prefixLength) {
        this.prefixLength = prefixLength;
        return this;
    }

    /**
     * The minimum length a suggest text term must have in order to be
     * corrected. Defaults to <tt>4</tt>.
     */
    public TermSuggestionBuilder minWordLength(int minWordLength) {
        this.minWordLength = minWordLength;
        return this;
    }

    /**
     * Sets a minimal threshold in number of documents a suggested term should
     * appear in. This can be specified as an absolute number or as a relative
     * percentage of number of documents. This can improve quality by only
     * suggesting high frequency terms. Defaults to 0f and is not enabled. If a
     * value higher than 1 is specified then the number cannot be fractional.
     */
    public TermSuggestionBuilder minDocFreq(float minDocFreq) {
        this.minDocFreq = minDocFreq;
        return this;
    }

    @Override
    public XContentBuilder innerToXContent(XContentBuilder builder, Params params) throws IOException {
        if (suggestMode != null) {
            builder.field("suggest_mode", suggestMode);
        }
        if (accuracy != null) {
            builder.field("accuracy", accuracy);
        }
        if (sort != null) {
            builder.field("sort", sort);
        }
        if (stringDistance != null) {
            builder.field("string_distance", stringDistance);
        }
        if (maxEdits != null) {
            builder.field("max_edits", maxEdits);
        }
        if (maxInspections != null) {
            builder.field("max_inspections", maxInspections);
        }
        if (maxTermFreq != null) {
            builder.field("max_term_freq", maxTermFreq);
        }
        if (prefixLength != null) {
            builder.field("prefix_length", prefixLength);
        }
        if (minWordLength != null) {
            builder.field("min_word_len", minWordLength);
        }
        if (minDocFreq != null) {
            builder.field("min_doc_freq", minDocFreq);
        }
        return builder;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9978.java