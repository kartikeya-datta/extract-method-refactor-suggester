error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8592.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8592.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8592.java
text:
```scala
s@@crollId = buildScrollId(request.searchType(), queryFetchResults.values(), null);

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

package org.elasticsearch.action.search.type;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.common.collect.ImmutableMap;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.search.SearchShardTarget;
import org.elasticsearch.search.action.SearchServiceListener;
import org.elasticsearch.search.action.SearchServiceTransportAction;
import org.elasticsearch.search.controller.SearchPhaseController;
import org.elasticsearch.search.controller.ShardDoc;
import org.elasticsearch.search.fetch.FetchSearchResultProvider;
import org.elasticsearch.search.internal.InternalSearchRequest;
import org.elasticsearch.search.internal.InternalSearchResponse;
import org.elasticsearch.search.query.QuerySearchResult;
import org.elasticsearch.search.query.QuerySearchResultProvider;
import org.elasticsearch.threadpool.ThreadPool;

import java.util.Map;

import static org.elasticsearch.action.search.type.TransportSearchHelper.*;

/**
 * @author kimchy (shay.banon)
 */
public class TransportSearchCountAction extends TransportSearchTypeAction {

    @Inject public TransportSearchCountAction(Settings settings, ThreadPool threadPool, ClusterService clusterService,
                                              TransportSearchCache transportSearchCache, SearchServiceTransportAction searchService, SearchPhaseController searchPhaseController) {
        super(settings, threadPool, clusterService, transportSearchCache, searchService, searchPhaseController);
    }

    @Override protected void doExecute(SearchRequest searchRequest, ActionListener<SearchResponse> listener) {
        new AsyncAction(searchRequest, listener).start();
    }

    private class AsyncAction extends BaseAsyncAction<QuerySearchResult> {

        private final Map<SearchShardTarget, QuerySearchResultProvider> queryFetchResults = searchCache.obtainQueryResults();

        private AsyncAction(SearchRequest request, ActionListener<SearchResponse> listener) {
            super(request, listener);
        }

        @Override protected String firstPhaseName() {
            return "query";
        }

        @Override protected void sendExecuteFirstPhase(DiscoveryNode node, InternalSearchRequest request, SearchServiceListener<QuerySearchResult> listener) {
            searchService.sendExecuteQuery(node, request, listener);
        }

        @Override protected void processFirstPhaseResult(ShardRouting shard, QuerySearchResult result) {
            queryFetchResults.put(result.shardTarget(), result);
        }

        @Override protected void moveToSecondPhase() throws Exception {
            // no need to sort, since we know we have no hits back
            final InternalSearchResponse internalResponse = searchPhaseController.merge(EMPTY_DOCS, queryFetchResults, ImmutableMap.<SearchShardTarget, FetchSearchResultProvider>of());
            String scrollId = null;
            if (request.scroll() != null) {
                scrollId = buildScrollId(request.searchType(), queryFetchResults.values());
            }
            listener.onResponse(new SearchResponse(internalResponse, scrollId, expectedSuccessfulOps, successulOps.get(), buildTookInMillis(), buildShardFailures()));
            searchCache.releaseQueryResults(queryFetchResults);
        }
    }

    private static ShardDoc[] EMPTY_DOCS = new ShardDoc[0];
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8592.java