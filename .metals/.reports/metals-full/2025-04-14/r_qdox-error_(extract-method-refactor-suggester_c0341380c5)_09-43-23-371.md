error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10405.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10405.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10405.java
text:
```scala
public <@@Request extends ActionRequest, Response extends ActionResponse, RequestBuilder extends ActionRequestBuilder<Request, Response, RequestBuilder>> RequestBuilder prepareExecute(ClusterAction<Request, Response, RequestBuilder> action) {

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

package org.elasticsearch.client.support;

import org.elasticsearch.action.*;
import org.elasticsearch.action.admin.cluster.ClusterAction;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthAction;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequestBuilder;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.cluster.node.hotthreads.NodesHotThreadsAction;
import org.elasticsearch.action.admin.cluster.node.hotthreads.NodesHotThreadsRequest;
import org.elasticsearch.action.admin.cluster.node.hotthreads.NodesHotThreadsRequestBuilder;
import org.elasticsearch.action.admin.cluster.node.hotthreads.NodesHotThreadsResponse;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoAction;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoRequest;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoRequestBuilder;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.elasticsearch.action.admin.cluster.node.restart.NodesRestartAction;
import org.elasticsearch.action.admin.cluster.node.restart.NodesRestartRequest;
import org.elasticsearch.action.admin.cluster.node.restart.NodesRestartRequestBuilder;
import org.elasticsearch.action.admin.cluster.node.restart.NodesRestartResponse;
import org.elasticsearch.action.admin.cluster.node.shutdown.NodesShutdownAction;
import org.elasticsearch.action.admin.cluster.node.shutdown.NodesShutdownRequest;
import org.elasticsearch.action.admin.cluster.node.shutdown.NodesShutdownRequestBuilder;
import org.elasticsearch.action.admin.cluster.node.shutdown.NodesShutdownResponse;
import org.elasticsearch.action.admin.cluster.node.stats.NodesStatsAction;
import org.elasticsearch.action.admin.cluster.node.stats.NodesStatsRequest;
import org.elasticsearch.action.admin.cluster.node.stats.NodesStatsRequestBuilder;
import org.elasticsearch.action.admin.cluster.node.stats.NodesStatsResponse;
import org.elasticsearch.action.admin.cluster.reroute.ClusterRerouteAction;
import org.elasticsearch.action.admin.cluster.reroute.ClusterRerouteRequest;
import org.elasticsearch.action.admin.cluster.reroute.ClusterRerouteRequestBuilder;
import org.elasticsearch.action.admin.cluster.reroute.ClusterRerouteResponse;
import org.elasticsearch.action.admin.cluster.settings.ClusterUpdateSettingsAction;
import org.elasticsearch.action.admin.cluster.settings.ClusterUpdateSettingsRequest;
import org.elasticsearch.action.admin.cluster.settings.ClusterUpdateSettingsRequestBuilder;
import org.elasticsearch.action.admin.cluster.settings.ClusterUpdateSettingsResponse;
import org.elasticsearch.action.admin.cluster.state.ClusterStateAction;
import org.elasticsearch.action.admin.cluster.state.ClusterStateRequest;
import org.elasticsearch.action.admin.cluster.state.ClusterStateRequestBuilder;
import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.client.internal.InternalClusterAdminClient;

/**
 *
 */
public abstract class AbstractClusterAdminClient implements InternalClusterAdminClient {

    @Override
    public <Request extends ActionRequest, Response extends ActionResponse, RequestBuilder extends ActionRequestBuilder<Request, Response>> RequestBuilder prepareExecute(ClusterAction<Request, Response, RequestBuilder> action) {
        return action.newRequestBuilder(this);
    }

    @Override
    public ActionFuture<ClusterHealthResponse> health(final ClusterHealthRequest request) {
        return execute(ClusterHealthAction.INSTANCE, request);
    }

    @Override
    public void health(final ClusterHealthRequest request, final ActionListener<ClusterHealthResponse> listener) {
        execute(ClusterHealthAction.INSTANCE, request, listener);
    }

    @Override
    public ClusterHealthRequestBuilder prepareHealth(String... indices) {
        return new ClusterHealthRequestBuilder(this).setIndices(indices);
    }

    @Override
    public ActionFuture<ClusterStateResponse> state(final ClusterStateRequest request) {
        return execute(ClusterStateAction.INSTANCE, request);
    }

    @Override
    public void state(final ClusterStateRequest request, final ActionListener<ClusterStateResponse> listener) {
        execute(ClusterStateAction.INSTANCE, request, listener);
    }

    @Override
    public ClusterStateRequestBuilder prepareState() {
        return new ClusterStateRequestBuilder(this);
    }

    @Override
    public ActionFuture<ClusterRerouteResponse> reroute(final ClusterRerouteRequest request) {
        return execute(ClusterRerouteAction.INSTANCE, request);
    }

    @Override
    public void reroute(final ClusterRerouteRequest request, final ActionListener<ClusterRerouteResponse> listener) {
        execute(ClusterRerouteAction.INSTANCE, request, listener);
    }

    @Override
    public ClusterRerouteRequestBuilder prepareReroute() {
        return new ClusterRerouteRequestBuilder(this);
    }

    @Override
    public ActionFuture<ClusterUpdateSettingsResponse> updateSettings(final ClusterUpdateSettingsRequest request) {
        return execute(ClusterUpdateSettingsAction.INSTANCE, request);
    }

    @Override
    public void updateSettings(final ClusterUpdateSettingsRequest request, final ActionListener<ClusterUpdateSettingsResponse> listener) {
        execute(ClusterUpdateSettingsAction.INSTANCE, request, listener);
    }

    @Override
    public ClusterUpdateSettingsRequestBuilder prepareUpdateSettings() {
        return new ClusterUpdateSettingsRequestBuilder(this);
    }

    @Override
    public ActionFuture<NodesInfoResponse> nodesInfo(final NodesInfoRequest request) {
        return execute(NodesInfoAction.INSTANCE, request);
    }

    @Override
    public void nodesInfo(final NodesInfoRequest request, final ActionListener<NodesInfoResponse> listener) {
        execute(NodesInfoAction.INSTANCE, request, listener);
    }

    @Override
    public NodesInfoRequestBuilder prepareNodesInfo(String... nodesIds) {
        return new NodesInfoRequestBuilder(this).setNodesIds(nodesIds);
    }

    @Override
    public ActionFuture<NodesStatsResponse> nodesStats(final NodesStatsRequest request) {
        return execute(NodesStatsAction.INSTANCE, request);
    }

    @Override
    public void nodesStats(final NodesStatsRequest request, final ActionListener<NodesStatsResponse> listener) {
        execute(NodesStatsAction.INSTANCE, request, listener);
    }

    @Override
    public NodesStatsRequestBuilder prepareNodesStats(String... nodesIds) {
        return new NodesStatsRequestBuilder(this).setNodesIds(nodesIds);
    }

    @Override
    public ActionFuture<NodesHotThreadsResponse> nodesHotThreads(NodesHotThreadsRequest request) {
        return execute(NodesHotThreadsAction.INSTANCE, request);
    }

    @Override
    public void nodesHotThreads(NodesHotThreadsRequest request, ActionListener<NodesHotThreadsResponse> listener) {
        execute(NodesHotThreadsAction.INSTANCE, request, listener);
    }

    @Override
    public NodesHotThreadsRequestBuilder prepareNodesHotThreads(String... nodesIds) {
        return new NodesHotThreadsRequestBuilder(this).setNodesIds(nodesIds);
    }

    @Override
    public ActionFuture<NodesRestartResponse> nodesRestart(final NodesRestartRequest request) {
        return execute(NodesRestartAction.INSTANCE, request);
    }

    @Override
    public void nodesRestart(final NodesRestartRequest request, final ActionListener<NodesRestartResponse> listener) {
        execute(NodesRestartAction.INSTANCE, request, listener);
    }

    @Override
    public NodesRestartRequestBuilder prepareNodesRestart(String... nodesIds) {
        return new NodesRestartRequestBuilder(this).setNodesIds(nodesIds);
    }

    @Override
    public ActionFuture<NodesShutdownResponse> nodesShutdown(final NodesShutdownRequest request) {
        return execute(NodesShutdownAction.INSTANCE, request);
    }

    @Override
    public void nodesShutdown(final NodesShutdownRequest request, final ActionListener<NodesShutdownResponse> listener) {
        execute(NodesShutdownAction.INSTANCE, request, listener);
    }

    @Override
    public NodesShutdownRequestBuilder prepareNodesShutdown(String... nodesIds) {
        return new NodesShutdownRequestBuilder(this).setNodesIds(nodesIds);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10405.java