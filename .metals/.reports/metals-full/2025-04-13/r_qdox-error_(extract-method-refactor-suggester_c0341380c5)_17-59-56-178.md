error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7342.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7342.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7342.java
text:
```scala
A@@rrayList<FiltersFunctionScoreQuery.FilterFunction> filterFunctions = new ArrayList<>();

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

package org.elasticsearch.index.query.functionscore;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.lucene.search.Queries;
import org.elasticsearch.common.lucene.search.XConstantScoreQuery;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FiltersFunctionScoreQuery;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.common.lucene.search.function.ScoreFunction;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.query.QueryParseContext;
import org.elasticsearch.index.query.QueryParser;
import org.elasticsearch.index.query.QueryParsingException;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 */
public class FunctionScoreQueryParser implements QueryParser {

    public static final String NAME = "function_score";
    ScoreFunctionParserMapper funtionParserMapper;

    @Inject
    public FunctionScoreQueryParser(ScoreFunctionParserMapper funtionParserMapper) {
        this.funtionParserMapper = funtionParserMapper;
    }

    @Override
    public String[] names() {
        return new String[] { NAME, Strings.toCamelCase(NAME) };
    }
    
    private static final ImmutableMap<String, CombineFunction> combineFunctionsMap;

    static {
        CombineFunction[] values = CombineFunction.values();
        Builder<String, CombineFunction> combineFunctionMapBuilder = ImmutableMap.<String, CombineFunction>builder();
        for (CombineFunction combineFunction : values) {
            combineFunctionMapBuilder.put(combineFunction.getName(), combineFunction);
        }
        combineFunctionsMap = combineFunctionMapBuilder.build();
    }

    @Override
    public Query parse(QueryParseContext parseContext) throws IOException, QueryParsingException {
        XContentParser parser = parseContext.parser();

        Query query = null;
        float boost = 1.0f;

        FiltersFunctionScoreQuery.ScoreMode scoreMode = FiltersFunctionScoreQuery.ScoreMode.Multiply;
        ArrayList<FiltersFunctionScoreQuery.FilterFunction> filterFunctions = new ArrayList<FiltersFunctionScoreQuery.FilterFunction>();
        float maxBoost = Float.MAX_VALUE;

        String currentFieldName = null;
        XContentParser.Token token;
        CombineFunction combineFunction = CombineFunction.MULT;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
            } else if ("query".equals(currentFieldName)) {
                query = parseContext.parseInnerQuery();
            } else if ("filter".equals(currentFieldName)) {
                query = new XConstantScoreQuery(parseContext.parseInnerFilter());
            } else if ("score_mode".equals(currentFieldName) || "scoreMode".equals(currentFieldName)) {
                scoreMode = parseScoreMode(parseContext, parser);
            } else if ("boost_mode".equals(currentFieldName) || "boostMode".equals(currentFieldName)) {
                combineFunction = parseBoostMode(parseContext, parser);
            } else if ("max_boost".equals(currentFieldName) || "maxBoost".equals(currentFieldName)) {
                maxBoost = parser.floatValue();
            } else if ("boost".equals(currentFieldName)) {
                boost = parser.floatValue();
            } else if ("functions".equals(currentFieldName)) {
                currentFieldName = parseFiltersAndFunctions(parseContext, parser, filterFunctions, currentFieldName);
            } else {
                // we tru to parse a score function. If there is no score
                // function for the current field name,
                // funtionParserMapper.get() will throw an Exception.
                filterFunctions.add(new FiltersFunctionScoreQuery.FilterFunction(null, funtionParserMapper.get(parseContext.index(),
                        currentFieldName).parse(parseContext, parser)));
            }
        }
        if (query == null) {
            query = Queries.newMatchAllQuery();
        }
        // if all filter elements returned null, just use the query
        if (filterFunctions.isEmpty()) {
            return query;
        }
        // handle cases where only one score function and no filter was
        // provided. In this case we create a FunctionScoreQuery.
        if (filterFunctions.size() == 1 && filterFunctions.get(0).filter == null) {
            FunctionScoreQuery theQuery = new FunctionScoreQuery(query, filterFunctions.get(0).function);
            if (combineFunction != null) {
                theQuery.setCombineFunction(combineFunction);
            }
            theQuery.setBoost(boost);
            theQuery.setMaxBoost(maxBoost);
            return theQuery;
            // in all other cases we create a FiltersFunctionScoreQuery.
        } else {
            FiltersFunctionScoreQuery functionScoreQuery = new FiltersFunctionScoreQuery(query, scoreMode,
                    filterFunctions.toArray(new FiltersFunctionScoreQuery.FilterFunction[filterFunctions.size()]), maxBoost);
            if (combineFunction != null) {
                functionScoreQuery.setCombineFunction(combineFunction);
            }
            functionScoreQuery.setBoost(boost);
            return functionScoreQuery;
        }
    }

    private String parseFiltersAndFunctions(QueryParseContext parseContext, XContentParser parser,
            ArrayList<FiltersFunctionScoreQuery.FilterFunction> filterFunctions, String currentFieldName) throws IOException {
        XContentParser.Token token;
        while ((token = parser.nextToken()) != XContentParser.Token.END_ARRAY) {
            Filter filter = null;
            ScoreFunction scoreFunction = null;
            if (token != XContentParser.Token.START_OBJECT) {
                throw new QueryParsingException(parseContext.index(), NAME + ": malformed query, expected a "
                        + XContentParser.Token.START_OBJECT + " while parsing functions but got a " + token);
            } else {
                while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                    if (token == XContentParser.Token.FIELD_NAME) {
                        currentFieldName = parser.currentName();
                    } else {
                        if ("filter".equals(currentFieldName)) {
                            filter = parseContext.parseInnerFilter();
                        } else {
                            // do not need to check null here,
                            // funtionParserMapper throws exception if parser
                            // non-existent
                            ScoreFunctionParser functionParser = funtionParserMapper.get(parseContext.index(), currentFieldName);
                            scoreFunction = functionParser.parse(parseContext, parser);
                        }
                    }
                }
            }
            if (filter == null) {
                filter = Queries.MATCH_ALL_FILTER;
            }
            filterFunctions.add(new FiltersFunctionScoreQuery.FilterFunction(filter, scoreFunction));

        }
        return currentFieldName;
    }

    private FiltersFunctionScoreQuery.ScoreMode parseScoreMode(QueryParseContext parseContext, XContentParser parser) throws IOException {
        String scoreMode = parser.text();
        if ("avg".equals(scoreMode)) {
            return FiltersFunctionScoreQuery.ScoreMode.Avg;
        } else if ("max".equals(scoreMode)) {
            return FiltersFunctionScoreQuery.ScoreMode.Max;
        } else if ("min".equals(scoreMode)) {
            return FiltersFunctionScoreQuery.ScoreMode.Min;
        } else if ("sum".equals(scoreMode)) {
            return FiltersFunctionScoreQuery.ScoreMode.Sum;
        } else if ("multiply".equals(scoreMode)) {
            return FiltersFunctionScoreQuery.ScoreMode.Multiply;
        } else if ("first".equals(scoreMode)) {
            return FiltersFunctionScoreQuery.ScoreMode.First;
        } else {
            throw new QueryParsingException(parseContext.index(), NAME + " illegal score_mode [" + scoreMode + "]");
        }
    }

    private CombineFunction parseBoostMode(QueryParseContext parseContext, XContentParser parser) throws IOException {
        String boostMode = parser.text();
        CombineFunction cf = combineFunctionsMap.get(boostMode);
        if (cf == null) {
            throw new QueryParsingException(parseContext.index(), NAME + " illegal boost_mode [" + boostMode + "]");
        }
        return cf;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7342.java