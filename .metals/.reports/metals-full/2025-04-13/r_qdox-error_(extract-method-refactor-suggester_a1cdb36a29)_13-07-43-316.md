error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2237.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2237.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2237.java
text:
```scala
public v@@oid handleRequest(final RestRequest request, final RestChannel channel, final Client client) {

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

package org.elasticsearch.rest.action.admin.cluster.node.info;

import com.google.common.collect.Sets;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoRequest;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsFilter;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.*;
import org.elasticsearch.rest.action.support.RestBuilderListener;

import java.util.Set;

import static org.elasticsearch.rest.RestRequest.Method.GET;


/**
 *
 */
public class RestNodesInfoAction extends BaseRestHandler {

    private final SettingsFilter settingsFilter;
    private final static Set<String> ALLOWED_METRICS = Sets.newHashSet("http", "jvm", "network", "os", "plugins", "process", "settings", "thread_pool", "transport");

    @Inject
    public RestNodesInfoAction(Settings settings, Client client, RestController controller,
                               SettingsFilter settingsFilter) {
        super(settings, client);
        controller.registerHandler(GET, "/_nodes", this);
        // this endpoint is used for metrics, not for nodeIds, like /_nodes/fs
        controller.registerHandler(GET, "/_nodes/{nodeId}", this);
        controller.registerHandler(GET, "/_nodes/{nodeId}/{metrics}", this);
        // added this endpoint to be aligned with stats
        controller.registerHandler(GET, "/_nodes/{nodeId}/info/{metrics}", this);

        this.settingsFilter = settingsFilter;
    }

    @Override
    public void handleRequest(final RestRequest request, final RestChannel channel) {
        String[] nodeIds;
        Set<String> metrics;

        // special case like /_nodes/os (in this case os are metrics and not the nodeId)
        // still, /_nodes/_local (or any other node id) should work and be treated as usual
        // this means one must differentiate between allowed metrics and arbitrary node ids in the same place
        if (request.hasParam("nodeId") && !request.hasParam("metrics")) {
            Set<String> metricsOrNodeIds = Strings.splitStringByCommaToSet(request.param("nodeId", "_all"));
            boolean isMetricsOnly = ALLOWED_METRICS.containsAll(metricsOrNodeIds);
            if (isMetricsOnly) {
                nodeIds = new String[]{"_all"};
                metrics = metricsOrNodeIds;
            } else {
                nodeIds = metricsOrNodeIds.toArray(new String[]{});
                metrics = Sets.newHashSet("_all");
            }
        } else {
            nodeIds = Strings.splitStringByCommaToArray(request.param("nodeId", "_all"));
            metrics = Strings.splitStringByCommaToSet(request.param("metrics", "_all"));
        }

        final NodesInfoRequest nodesInfoRequest = new NodesInfoRequest(nodeIds);
        nodesInfoRequest.listenerThreaded(false);

        // shortcut, dont do checks if only all is specified
        if (metrics.size() == 1 && metrics.contains("_all")) {
            nodesInfoRequest.all();
        } else {
            nodesInfoRequest.clear();
            nodesInfoRequest.settings(metrics.contains("settings"));
            nodesInfoRequest.os(metrics.contains("os"));
            nodesInfoRequest.process(metrics.contains("process"));
            nodesInfoRequest.jvm(metrics.contains("jvm"));
            nodesInfoRequest.threadPool(metrics.contains("thread_pool"));
            nodesInfoRequest.network(metrics.contains("network"));
            nodesInfoRequest.transport(metrics.contains("transport"));
            nodesInfoRequest.http(metrics.contains("http"));
            nodesInfoRequest.plugins(metrics.contains("plugins"));
        }

        client.admin().cluster().nodesInfo(nodesInfoRequest, new RestBuilderListener<NodesInfoResponse>(channel) {

            @Override
            public RestResponse buildResponse(NodesInfoResponse response, XContentBuilder builder) throws Exception {
                response.settingsFilter(settingsFilter);
                builder.startObject();
                response.toXContent(builder, request);
                builder.endObject();
                return new BytesRestResponse(RestStatus.OK, builder);
            }
        });
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2237.java