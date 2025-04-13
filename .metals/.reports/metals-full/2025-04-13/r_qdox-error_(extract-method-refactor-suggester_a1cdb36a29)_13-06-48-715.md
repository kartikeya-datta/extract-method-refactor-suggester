error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9160.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9160.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[22,1]

error in qdox parser
file content:
```java
offset: 919
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9160.java
text:
```scala
public class FieldQueryBuilder extends BaseQueryBuilder implements BoostableQueryBuilder<FieldQueryBuilder> {

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

p@@ackage org.elasticsearch.index.query;

import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

/**
 * A query that executes the query string against a field. It is a simplified
 * version of {@link QueryStringQueryBuilder} that simply runs against
 * a single field.
 */
public class FieldQueryBuilder extends BaseQueryBuilder {

    public static enum Operator {
        OR,
        AND
    }

    private final String name;

    private final Object query;

    private Operator defaultOperator;

    private String analyzer;

    private Boolean autoGeneratePhraseQueries;

    private Boolean allowLeadingWildcard;

    private Boolean lowercaseExpandedTerms;

    private Boolean enablePositionIncrements;

    private Boolean analyzeWildcard;

    private int fuzzyPrefixLength = -1;
    private float fuzzyMinSim = -1;
    private int fuzzyMaxExpansions = -1;
    private String fuzzyRewrite;

    private float boost = -1;

    private int phraseSlop = -1;

    private boolean extraSet = false;

    private String rewrite;

    private String minimumShouldMatch;

    /**
     * A query that executes the query string against a field. It is a simplified
     * version of {@link QueryStringQueryBuilder} that simply runs against
     * a single field.
     *
     * @param name  The name of the field
     * @param query The query string
     */
    public FieldQueryBuilder(String name, String query) {
        this(name, (Object) query);
    }

    /**
     * A query that executes the query string against a field. It is a simplified
     * version of {@link QueryStringQueryBuilder} that simply runs against
     * a single field.
     *
     * @param name  The name of the field
     * @param query The query string
     */
    public FieldQueryBuilder(String name, int query) {
        this(name, (Object) query);
    }

    /**
     * A query that executes the query string against a field. It is a simplified
     * version of {@link QueryStringQueryBuilder} that simply runs against
     * a single field.
     *
     * @param name  The name of the field
     * @param query The query string
     */
    public FieldQueryBuilder(String name, long query) {
        this(name, (Object) query);
    }

    /**
     * A query that executes the query string against a field. It is a simplified
     * version of {@link QueryStringQueryBuilder} that simply runs against
     * a single field.
     *
     * @param name  The name of the field
     * @param query The query string
     */
    public FieldQueryBuilder(String name, float query) {
        this(name, (Object) query);
    }

    /**
     * A query that executes the query string against a field. It is a simplified
     * version of {@link QueryStringQueryBuilder} that simply runs against
     * a single field.
     *
     * @param name  The name of the field
     * @param query The query string
     */
    public FieldQueryBuilder(String name, double query) {
        this(name, (Object) query);
    }

    /**
     * A query that executes the query string against a field. It is a simplified
     * version of {@link QueryStringQueryBuilder} that simply runs against
     * a single field.
     *
     * @param name  The name of the field
     * @param query The query string
     */
    public FieldQueryBuilder(String name, boolean query) {
        this(name, (Object) query);
    }

    /**
     * A query that executes the query string against a field. It is a simplified
     * version of {@link QueryStringQueryBuilder} that simply runs against
     * a single field.
     *
     * @param name  The name of the field
     * @param query The query string
     */
    public FieldQueryBuilder(String name, Object query) {
        this.name = name;
        this.query = query;
    }

    /**
     * Sets the boost for this query.  Documents matching this query will (in addition to the normal
     * weightings) have their score multiplied by the boost provided.
     */
    public FieldQueryBuilder boost(float boost) {
        this.boost = boost;
        extraSet = true;
        return this;
    }

    /**
     * Sets the boolean operator of the query parser used to parse the query string.
     * <p/>
     * <p>In default mode ({@link FieldQueryBuilder.Operator#OR}) terms without any modifiers
     * are considered optional: for example <code>capital of Hungary</code> is equal to
     * <code>capital OR of OR Hungary</code>.
     * <p/>
     * <p>In {@link FieldQueryBuilder.Operator#AND} mode terms are considered to be in conjunction: the
     * above mentioned query is parsed as <code>capital AND of AND Hungary</code>
     */
    public FieldQueryBuilder defaultOperator(Operator defaultOperator) {
        this.defaultOperator = defaultOperator;
        extraSet = true;
        return this;
    }

    /**
     * The optional analyzer used to analyze the query string. Note, if a field has search analyzer
     * defined for it, then it will be used automatically. Defaults to the smart search analyzer.
     */
    public FieldQueryBuilder analyzer(String analyzer) {
        this.analyzer = analyzer;
        extraSet = true;
        return this;
    }

    /**
     * Set to true if phrase queries will be automatically generated
     * when the analyzer returns more than one term from whitespace
     * delimited text.
     * NOTE: this behavior may not be suitable for all languages.
     * <p/>
     * Set to false if phrase queries should only be generated when
     * surrounded by double quotes.
     */
    public void autoGeneratePhraseQueries(boolean autoGeneratePhraseQueries) {
        this.autoGeneratePhraseQueries = autoGeneratePhraseQueries;
    }

    /**
     * Should leading wildcards be allowed or not. Defaults to <tt>true</tt>.
     */
    public FieldQueryBuilder allowLeadingWildcard(boolean allowLeadingWildcard) {
        this.allowLeadingWildcard = allowLeadingWildcard;
        extraSet = true;
        return this;
    }

    /**
     * Whether terms of wildcard, prefix, fuzzy and range queries are to be automatically
     * lower-cased or not.  Default is <tt>true</tt>.
     */
    public FieldQueryBuilder lowercaseExpandedTerms(boolean lowercaseExpandedTerms) {
        this.lowercaseExpandedTerms = lowercaseExpandedTerms;
        extraSet = true;
        return this;
    }

    /**
     * Set to <tt>true</tt> to enable position increments in result query. Defaults to
     * <tt>true</tt>.
     * <p/>
     * <p>When set, result phrase and multi-phrase queries will be aware of position increments.
     * Useful when e.g. a StopFilter increases the position increment of the token that follows an omitted token.
     */
    public FieldQueryBuilder enablePositionIncrements(boolean enablePositionIncrements) {
        this.enablePositionIncrements = enablePositionIncrements;
        extraSet = true;
        return this;
    }

    /**
     * Set the minimum similarity for fuzzy queries. Default is 0.5f.
     */
    public FieldQueryBuilder fuzzyMinSim(float fuzzyMinSim) {
        this.fuzzyMinSim = fuzzyMinSim;
        extraSet = true;
        return this;
    }

    /**
     * Set the prefix length for fuzzy queries. Default is 0.
     */
    public FieldQueryBuilder fuzzyPrefixLength(int fuzzyPrefixLength) {
        this.fuzzyPrefixLength = fuzzyPrefixLength;
        extraSet = true;
        return this;
    }

    public FieldQueryBuilder fuzzyMaxExpansions(int fuzzyMaxExpansions) {
        this.fuzzyMaxExpansions = fuzzyMaxExpansions;
        return this;
    }

    public FieldQueryBuilder fuzzyRewrite(String fuzzyRewrite) {
        this.fuzzyRewrite = fuzzyRewrite;
        return this;
    }


    /**
     * Sets the default slop for phrases.  If zero, then exact phrase matches
     * are required. Default value is zero.
     */
    public FieldQueryBuilder phraseSlop(int phraseSlop) {
        this.phraseSlop = phraseSlop;
        extraSet = true;
        return this;
    }

    /**
     * Set to <tt>true</tt> to enable analysis on wildcard and prefix queries.
     */
    public FieldQueryBuilder analyzeWildcard(boolean analyzeWildcard) {
        this.analyzeWildcard = analyzeWildcard;
        extraSet = true;
        return this;
    }

    public FieldQueryBuilder rewrite(String rewrite) {
        this.rewrite = rewrite;
        extraSet = true;
        return this;
    }

    public FieldQueryBuilder minimumShouldMatch(String minimumShouldMatch) {
        this.minimumShouldMatch = minimumShouldMatch;
        return this;
    }

    @Override
    public void doXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject(FieldQueryParser.NAME);
        if (!extraSet) {
            builder.field(name, query);
        } else {
            builder.startObject(name);
            builder.field("query", query);
            if (defaultOperator != null) {
                builder.field("default_operator", defaultOperator.name().toLowerCase());
            }
            if (analyzer != null) {
                builder.field("analyzer", analyzer);
            }
            if (autoGeneratePhraseQueries != null) {
                builder.field("auto_generate_phrase_queries", autoGeneratePhraseQueries);
            }
            if (allowLeadingWildcard != null) {
                builder.field("allow_leading_wildcard", allowLeadingWildcard);
            }
            if (lowercaseExpandedTerms != null) {
                builder.field("lowercase_expanded_terms", lowercaseExpandedTerms);
            }
            if (enablePositionIncrements != null) {
                builder.field("enable_position_increments", enablePositionIncrements);
            }
            if (fuzzyMinSim != -1) {
                builder.field("fuzzy_min_sim", fuzzyMinSim);
            }
            if (boost != -1) {
                builder.field("boost", boost);
            }
            if (fuzzyPrefixLength != -1) {
                builder.field("fuzzy_prefix_length", fuzzyPrefixLength);
            }
            if (fuzzyMaxExpansions != -1) {
                builder.field("fuzzy_max_expansions", fuzzyMaxExpansions);
            }
            if (fuzzyRewrite != null) {
                builder.field("fuzzy_rewrite", fuzzyRewrite);
            }
            if (phraseSlop != -1) {
                builder.field("phrase_slop", phraseSlop);
            }
            if (analyzeWildcard != null) {
                builder.field("analyze_wildcard", analyzeWildcard);
            }
            if (rewrite != null) {
                builder.field("rewrite", rewrite);
            }
            if (minimumShouldMatch != null) {
                builder.field("minimum_should_match", minimumShouldMatch);
            }
            builder.endObject();
        }
        builder.endObject();
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9160.java