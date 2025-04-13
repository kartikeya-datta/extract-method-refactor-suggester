error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10181.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10181.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10181.java
text:
```scala
M@@apperQueryParser queryParser = parseContext.queryParser(qpSettings);

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

package org.elasticsearch.index.query;

import org.apache.lucene.queryParser.MapperQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParserSettings;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.lucene.search.Queries;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.analysis.NamedAnalyzer;
import org.elasticsearch.index.query.support.QueryParsers;

import java.io.IOException;

import static org.elasticsearch.common.lucene.search.Queries.fixNegativeQueryIfNeeded;
import static org.elasticsearch.common.lucene.search.Queries.optimizeQuery;

/**
 *
 */
public class FieldQueryParser implements QueryParser {

    public static final String NAME = "field";

    private final boolean defaultAnalyzeWildcard;
    private final boolean defaultAllowLeadingWildcard;

    @Inject
    public FieldQueryParser(Settings settings) {
        this.defaultAnalyzeWildcard = settings.getAsBoolean("indices.query.query_string.analyze_wildcard", QueryParserSettings.DEFAULT_ANALYZE_WILDCARD);
        this.defaultAllowLeadingWildcard = settings.getAsBoolean("indices.query.query_string.allowLeadingWildcard", QueryParserSettings.DEFAULT_ALLOW_LEADING_WILDCARD);
    }

    @Override
    public String[] names() {
        return new String[]{NAME};
    }

    @Override
    public Query parse(QueryParseContext parseContext) throws IOException, QueryParsingException {
        XContentParser parser = parseContext.parser();

        XContentParser.Token token = parser.nextToken();
        if (token != XContentParser.Token.FIELD_NAME) {
            throw new QueryParsingException(parseContext.index(), "[field] query malformed, no field");
        }
        String fieldName = parser.currentName();

        QueryParserSettings qpSettings = new QueryParserSettings();
        qpSettings.defaultField(fieldName);
        qpSettings.analyzeWildcard(defaultAnalyzeWildcard);
        qpSettings.allowLeadingWildcard(defaultAllowLeadingWildcard);

        token = parser.nextToken();
        if (token == XContentParser.Token.START_OBJECT) {
            String currentFieldName = null;
            while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                if (token == XContentParser.Token.FIELD_NAME) {
                    currentFieldName = parser.currentName();
                } else if (token.isValue()) {
                    if ("query".equals(currentFieldName)) {
                        qpSettings.queryString(parser.text());
                    } else if ("boost".equals(currentFieldName)) {
                        qpSettings.boost(parser.floatValue());
                    } else if ("enable_position_increments".equals(currentFieldName) || "enablePositionIncrements".equals(currentFieldName)) {
                        qpSettings.enablePositionIncrements(parser.booleanValue());
                    } else if ("allow_leading_wildcard".equals(currentFieldName) || "allowLeadingWildcard".equals(currentFieldName)) {
                        qpSettings.allowLeadingWildcard(parser.booleanValue());
                    } else if ("auto_generate_phrase_queries".equals(currentFieldName) || "autoGeneratePhraseQueries".equals(currentFieldName)) {
                        qpSettings.autoGeneratePhraseQueries(parser.booleanValue());
                    } else if ("lowercase_expanded_terms".equals(currentFieldName) || "lowercaseExpandedTerms".equals(currentFieldName)) {
                        qpSettings.lowercaseExpandedTerms(parser.booleanValue());
                    } else if ("phrase_slop".equals(currentFieldName) || "phraseSlop".equals(currentFieldName)) {
                        qpSettings.phraseSlop(parser.intValue());
                    } else if ("analyzer".equals(currentFieldName)) {
                        NamedAnalyzer analyzer = parseContext.analysisService().analyzer(parser.text());
                        if (analyzer == null) {
                            throw new QueryParsingException(parseContext.index(), "[query_string] analyzer [" + parser.text() + "] not found");
                        }
                        qpSettings.forcedAnalyzer(analyzer);
                    } else if ("quote_analyzer".equals(currentFieldName) || "quoteAnalyzer".equals(currentFieldName)) {
                        NamedAnalyzer analyzer = parseContext.analysisService().analyzer(parser.text());
                        if (analyzer == null) {
                            throw new QueryParsingException(parseContext.index(), "[query_string] analyzer [" + parser.text() + "] not found");
                        }
                        qpSettings.forcedQuoteAnalyzer(analyzer);
                    } else if ("default_operator".equals(currentFieldName) || "defaultOperator".equals(currentFieldName)) {
                        String op = parser.text();
                        if ("or".equalsIgnoreCase(op)) {
                            qpSettings.defaultOperator(org.apache.lucene.queryParser.QueryParser.Operator.OR);
                        } else if ("and".equalsIgnoreCase(op)) {
                            qpSettings.defaultOperator(org.apache.lucene.queryParser.QueryParser.Operator.AND);
                        } else {
                            throw new QueryParsingException(parseContext.index(), "Query default operator [" + op + "] is not allowed");
                        }
                    } else if ("fuzzy_min_sim".equals(currentFieldName) || "fuzzyMinSim".equals(currentFieldName)) {
                        qpSettings.fuzzyMinSim(parser.floatValue());
                    } else if ("fuzzy_prefix_length".equals(currentFieldName) || "fuzzyPrefixLength".equals(currentFieldName)) {
                        qpSettings.fuzzyPrefixLength(parser.intValue());
                    } else if ("escape".equals(currentFieldName)) {
                        qpSettings.escape(parser.booleanValue());
                    } else if ("analyze_wildcard".equals(currentFieldName) || "analyzeWildcard".equals(currentFieldName)) {
                        qpSettings.analyzeWildcard(parser.booleanValue());
                    } else if ("rewrite".equals(currentFieldName)) {
                        qpSettings.rewriteMethod(QueryParsers.parseRewriteMethod(parser.textOrNull()));
                    } else if ("minimum_should_match".equals(currentFieldName) || "minimumShouldMatch".equals(currentFieldName)) {
                        qpSettings.minimumShouldMatch(parser.textOrNull());
                    } else if ("quote_field_suffix".equals(currentFieldName) || "quoteFieldSuffix".equals(currentFieldName)) {
                        qpSettings.quoteFieldSuffix(parser.textOrNull());
                    } else {
                        throw new QueryParsingException(parseContext.index(), "[field] query does not support [" + currentFieldName + "]");
                    }
                }
            }
            parser.nextToken();
        } else {
            qpSettings.queryString(parser.text());
            // move to the next token
            parser.nextToken();
        }

        qpSettings.defaultAnalyzer(parseContext.mapperService().searchAnalyzer());
        qpSettings.defaultQuoteAnalyzer(parseContext.mapperService().searchQuoteAnalyzer());

        if (qpSettings.queryString() == null) {
            throw new QueryParsingException(parseContext.index(), "No value specified for term query");
        }

        if (qpSettings.escape()) {
            qpSettings.queryString(org.apache.lucene.queryParser.QueryParser.escape(qpSettings.queryString()));
        }

        Query query = parseContext.indexCache().queryParserCache().get(qpSettings);
        if (query != null) {
            return query;
        }

        MapperQueryParser queryParser = parseContext.singleQueryParser(qpSettings);

        try {
            query = queryParser.parse(qpSettings.queryString());
            query.setBoost(qpSettings.boost());
            query = optimizeQuery(fixNegativeQueryIfNeeded(query));
            if (query instanceof BooleanQuery) {
                Queries.applyMinimumShouldMatch((BooleanQuery) query, qpSettings.minimumShouldMatch());
            }
            parseContext.indexCache().queryParserCache().put(qpSettings, query);
            return query;
        } catch (ParseException e) {
            throw new QueryParsingException(parseContext.index(), "Failed to parse query [" + qpSettings.queryString() + "]", e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10181.java