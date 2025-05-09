error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2836.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2836.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2836.java
text:
```scala
Q@@uery query = Queries.newMatchAllQuery();

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

import org.apache.lucene.search.Filter;
import org.apache.lucene.search.FilteredQuery;
import org.apache.lucene.search.Query;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.lucene.search.Queries;
import org.elasticsearch.common.lucene.search.XConstantScoreQuery;
import org.elasticsearch.common.lucene.search.XFilteredQuery;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.cache.filter.support.CacheKeyFilter;

import java.io.IOException;

/**
 *
 */
public class FilteredQueryParser implements QueryParser {

    public static final String NAME = "filtered";

    @Inject
    public FilteredQueryParser() {
    }

    @Override
    public String[] names() {
        return new String[]{NAME};
    }

    @Override
    public Query parse(QueryParseContext parseContext) throws IOException, QueryParsingException {
        XContentParser parser = parseContext.parser();

        Query query = Queries.MATCH_ALL_QUERY;
        Filter filter = null;
        boolean filterFound = false;
        float boost = 1.0f;
        boolean cache = false;
        CacheKeyFilter.Key cacheKey = null;

        String currentFieldName = null;
        XContentParser.Token token;
        FilteredQuery.FilterStrategy filterStrategy = XFilteredQuery.CUSTOM_FILTER_STRATEGY;

        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
            } else if (token == XContentParser.Token.START_OBJECT) {
                if ("query".equals(currentFieldName)) {
                    query = parseContext.parseInnerQuery();
                } else if ("filter".equals(currentFieldName)) {
                    filterFound = true;
                    filter = parseContext.parseInnerFilter();
                } else {
                    throw new QueryParsingException(parseContext.index(), "[filtered] query does not support [" + currentFieldName + "]");
                }
            } else if (token.isValue()) {
                if ("strategy".equals(currentFieldName)) {
                    String value = parser.text();
                    if ("query_first".equals(value) || "queryFirst".equals(value)) {
                        filterStrategy = FilteredQuery.QUERY_FIRST_FILTER_STRATEGY;
                    } else if ("random_access_always".equals(value) || "randomAccessAlways".equals(value)) {
                        filterStrategy = XFilteredQuery.ALWAYS_RANDOM_ACCESS_FILTER_STRATEGY;
                    } else if ("leap_frog".equals(value) || "leapFrog".equals(value)) {
                        filterStrategy = FilteredQuery.LEAP_FROG_QUERY_FIRST_STRATEGY;
                    } else if (value.startsWith("random_access_")) {
                        int threshold = Integer.parseInt(value.substring("random_access_".length()));
                        filterStrategy = new XFilteredQuery.CustomRandomAccessFilterStrategy(threshold);
                    } else if (value.startsWith("randomAccess")) {
                        int threshold = Integer.parseInt(value.substring("randomAccess".length()));
                        filterStrategy = new XFilteredQuery.CustomRandomAccessFilterStrategy(threshold);
                    } else if ("leap_frog_query_first".equals(value) || "leapFrogQueryFirst".equals(value)) {
                        filterStrategy = FilteredQuery.LEAP_FROG_QUERY_FIRST_STRATEGY;
                    } else if ("leap_frog_filter_first".equals(value) || "leapFrogFilterFirst".equals(value)) {
                        filterStrategy = FilteredQuery.LEAP_FROG_FILTER_FIRST_STRATEGY;
                    } else {
                        throw new QueryParsingException(parseContext.index(), "[filtered] strategy value not supported [" + value + "]");
                    }
                } else if ("boost".equals(currentFieldName)) {
                    boost = parser.floatValue();
                } else if ("_cache".equals(currentFieldName)) {
                    cache = parser.booleanValue();
                } else if ("_cache_key".equals(currentFieldName) || "_cacheKey".equals(currentFieldName)) {
                    cacheKey = new CacheKeyFilter.Key(parser.text());
                } else {
                    throw new QueryParsingException(parseContext.index(), "[filtered] query does not support [" + currentFieldName + "]");
                }
            }
        }

        // parsed internally, but returned null during parsing...
        if (query == null) {
            return null;
        }

        if (filter == null) {
            if (!filterFound) {
                // we allow for null filter, so it makes compositions on the client side to be simpler
                return query;
            } else {
                // the filter was provided, but returned null, meaning we should discard it, this means no
                // matches for this query...
                return Queries.NO_MATCH_QUERY;
            }
        }
        if (filter == Queries.MATCH_ALL_FILTER) {
            // this is an instance of match all filter, just execute the query
            return query;
        }

        // cache if required
        if (cache) {
            filter = parseContext.cacheFilter(filter, cacheKey);
        }

        // if its a match_all query, use constant_score
        if (Queries.isConstantMatchAllQuery(query)) {
            Query q = new XConstantScoreQuery(filter);
            q.setBoost(boost);
            return q;
        }

        XFilteredQuery filteredQuery = new XFilteredQuery(query, filter, filterStrategy);
        filteredQuery.setBoost(boost);
        return filteredQuery;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2836.java