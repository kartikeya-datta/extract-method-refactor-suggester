error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5320.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5320.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5320.java
text:
```scala
s@@coreFunction = new CustomScoreQueryParser.ScriptScoreFunction(script, vars, searchScript);

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

package org.elasticsearch.index.query;

import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.elasticsearch.ElasticSearchIllegalStateException;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.lucene.search.function.BoostScoreFunction;
import org.elasticsearch.common.lucene.search.function.FiltersFunctionScoreQuery;
import org.elasticsearch.common.lucene.search.function.ScoreFunction;
import org.elasticsearch.common.trove.list.array.TFloatArrayList;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.script.SearchScript;
import org.elasticsearch.search.internal.SearchContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author kimchy (shay.banon)
 */
public class CustomFiltersScoreQueryParser implements QueryParser {

    public static final String NAME = "custom_filters_score";

    @Inject public CustomFiltersScoreQueryParser() {
    }

    @Override public String[] names() {
        return new String[]{NAME, Strings.toCamelCase(NAME)};
    }

    @Override public Query parse(QueryParseContext parseContext) throws IOException, QueryParsingException {
        XContentParser parser = parseContext.parser();

        Query query = null;
        float boost = 1.0f;
        String scriptLang = null;
        Map<String, Object> vars = null;

        FiltersFunctionScoreQuery.ScoreMode scoreMode = FiltersFunctionScoreQuery.ScoreMode.First;
        ArrayList<Filter> filters = new ArrayList<Filter>();
        ArrayList<String> scripts = new ArrayList<String>();
        TFloatArrayList boosts = new TFloatArrayList();

        String currentFieldName = null;
        XContentParser.Token token;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
            } else if (token == XContentParser.Token.START_OBJECT) {
                if ("query".equals(currentFieldName)) {
                    query = parseContext.parseInnerQuery();
                } else if ("params".equals(currentFieldName)) {
                    vars = parser.map();
                }
            } else if (token == XContentParser.Token.START_ARRAY) {
                if ("filters".equals(currentFieldName)) {
                    while ((token = parser.nextToken()) != XContentParser.Token.END_ARRAY) {
                        String script = null;
                        Filter filter = null;
                        float fboost = Float.NaN;
                        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                            if (token == XContentParser.Token.FIELD_NAME) {
                                currentFieldName = parser.currentName();
                            } else if (token == XContentParser.Token.START_OBJECT) {
                                if ("filter".equals(currentFieldName)) {
                                    filter = parseContext.parseInnerFilter();
                                }
                            } else if (token.isValue()) {
                                if ("script".equals(currentFieldName)) {
                                    script = parser.text();
                                } else if ("boost".equals(currentFieldName)) {
                                    fboost = parser.floatValue();
                                }
                            }
                        }
                        if (script == null && fboost == -1) {
                            throw new QueryParsingException(parseContext.index(), "[custom_filters_score] missing 'script' or 'boost' in filters array element");
                        }
                        if (filter == null) {
                            throw new QueryParsingException(parseContext.index(), "[custom_filters_score] missing 'filter' in filters array element");
                        }
                        filters.add(filter);
                        scripts.add(script);
                        boosts.add(fboost);
                    }
                }
            } else if (token.isValue()) {
                if ("lang".equals(currentFieldName)) {
                    scriptLang = parser.text();
                } else if ("boost".equals(currentFieldName)) {
                    boost = parser.floatValue();
                } else if ("score_mode".equals(currentFieldName) || "scoreMode".equals(currentFieldName)) {
                    String sScoreMode = parser.text();
                    if ("avg".equals(sScoreMode)) {
                        scoreMode = FiltersFunctionScoreQuery.ScoreMode.Avg;
                    } else if ("max".equals(sScoreMode)) {
                        scoreMode = FiltersFunctionScoreQuery.ScoreMode.Max;
                    } else if ("total".equals(sScoreMode)) {
                        scoreMode = FiltersFunctionScoreQuery.ScoreMode.Total;
                    } else if ("first".equals(sScoreMode)) {
                        scoreMode = FiltersFunctionScoreQuery.ScoreMode.First;
                    } else {
                        throw new QueryParsingException(parseContext.index(), "illegal score_mode for nested query [" + sScoreMode + "]");
                    }
                }
            }
        }
        if (query == null) {
            throw new QueryParsingException(parseContext.index(), "[custom_filters_score] requires 'query' field");
        }
        if (filters.isEmpty()) {
            throw new QueryParsingException(parseContext.index(), "[custom_filters_score] requires 'filters' field");
        }

        SearchContext context = SearchContext.current();
        if (context == null) {
            throw new ElasticSearchIllegalStateException("No search context on going...");
        }
        FiltersFunctionScoreQuery.FilterFunction[] filterFunctions = new FiltersFunctionScoreQuery.FilterFunction[filters.size()];
        for (int i = 0; i < filterFunctions.length; i++) {
            ScoreFunction scoreFunction;
            String script = scripts.get(i);
            if (script != null) {
                SearchScript searchScript = context.scriptService().search(context.lookup(), scriptLang, script, vars);
                scoreFunction = new CustomScoreQueryParser.ScriptScoreFunction(searchScript);
            } else {
                scoreFunction = new BoostScoreFunction(boosts.get(i));
            }
            filterFunctions[i] = new FiltersFunctionScoreQuery.FilterFunction(filters.get(i), scoreFunction);
        }
        FiltersFunctionScoreQuery functionScoreQuery = new FiltersFunctionScoreQuery(query, scoreMode, filterFunctions);
        functionScoreQuery.setBoost(boost);
        return functionScoreQuery;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5320.java