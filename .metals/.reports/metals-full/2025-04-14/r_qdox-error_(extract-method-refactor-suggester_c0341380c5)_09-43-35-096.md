error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8231.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8231.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8231.java
text:
```scala
i@@f (used >= 0 && avail >= 0) {

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

package org.elasticsearch.rest.action.cat;

import com.carrotsearch.hppc.ObjectIntOpenHashMap;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.cluster.node.stats.NodeStats;
import org.elasticsearch.action.admin.cluster.node.stats.NodesStatsRequest;
import org.elasticsearch.action.admin.cluster.node.stats.NodesStatsResponse;
import org.elasticsearch.action.admin.cluster.state.ClusterStateRequest;
import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.Table;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.monitor.fs.FsStats;
import org.elasticsearch.rest.RestChannel;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.XContentThrowableRestResponse;
import org.elasticsearch.rest.action.support.RestTable;

import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;

import static org.elasticsearch.rest.RestRequest.Method.GET;


public class RestAllocationAction extends AbstractCatAction {

    @Inject
    public RestAllocationAction(Settings settings, Client client, RestController controller) {
        super(settings, client);
        controller.registerHandler(GET, "/_cat/allocation", this);
        controller.registerHandler(GET, "/_cat/allocation/{nodes}", this);
    }

    @Override
    void documentation(StringBuilder sb) {
        sb.append("/_cat/allocation\n");
    }

    @Override
    public void doRequest(final RestRequest request, final RestChannel channel) {
        final String[] nodes = Strings.splitStringByCommaToArray(request.param("nodes"));
        final ClusterStateRequest clusterStateRequest = new ClusterStateRequest();
        clusterStateRequest.clear().routingTable(true);
        clusterStateRequest.local(request.paramAsBoolean("local", clusterStateRequest.local()));
        clusterStateRequest.masterNodeTimeout(request.paramAsTime("master_timeout", clusterStateRequest.masterNodeTimeout()));

        client.admin().cluster().state(clusterStateRequest, new ActionListener<ClusterStateResponse>() {
            @Override
            public void onResponse(final ClusterStateResponse state) {
                NodesStatsRequest statsRequest = new NodesStatsRequest(nodes);
                statsRequest.clear().fs(true);

                client.admin().cluster().nodesStats(statsRequest, new ActionListener<NodesStatsResponse>() {
                    @Override
                    public void onResponse(NodesStatsResponse stats) {
                        try {
                            Table tab = buildTable(request, state, stats);
                            channel.sendResponse(RestTable.buildResponse(tab, request, channel));
                        } catch (Throwable e) {
                            onFailure(e);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        try {
                            channel.sendResponse(new XContentThrowableRestResponse(request, e));
                        } catch (IOException e1) {
                            logger.error("Failed to send failure response", e1);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable e) {
                try {
                    channel.sendResponse(new XContentThrowableRestResponse(request, e));
                } catch (IOException e1) {
                    logger.error("Failed to send failure response", e1);
                }
            }
        });

    }

    @Override
    Table getTableWithHeader(final RestRequest request) {
        final Table table = new Table();
        table.startHeaders();
        table.addCell("shards", "alias:s;text-align:right;desc:number of shards on node");
        table.addCell("disk.used", "alias:du,diskUsed;text-align:right;desc:disk used (total, not just ES)");
        table.addCell("disk.avail", "alias:da,diskAvail;text-align:right;desc:disk available");
        table.addCell("disk.total", "alias:dt,diskTotal;text-align:right;desc:total capacity of all volumes");
        table.addCell("disk.percent", "alias:dp,diskPercent;text-align:right;desc:percent disk used");
        table.addCell("host", "alias:h;desc:host of node");
        table.addCell("ip", "desc:ip of node");
        table.addCell("node", "alias:n;desc:name of node");
        table.endHeaders();
        return table;
    }

    private Table buildTable(RestRequest request, final ClusterStateResponse state, final NodesStatsResponse stats) {
        final ObjectIntOpenHashMap<String> allocs = new ObjectIntOpenHashMap<String>();

        for (ShardRouting shard : state.getState().routingTable().allShards()) {
            String nodeId = "UNASSIGNED";

            if (shard.assignedToNode()) {
                nodeId = shard.currentNodeId();
            }

            allocs.addTo(nodeId, 1);
        }

        Table table = getTableWithHeader(request);

        for (NodeStats nodeStats : stats.getNodes()) {
            DiscoveryNode node = nodeStats.getNode();

            int shardCount = 0;
            if (allocs.containsKey(node.id())) {
                shardCount = allocs.lget();
            }

            long used = nodeStats.getFs().getTotal().getTotal().bytes() - nodeStats.getFs().getTotal().getAvailable().bytes();
            long avail = nodeStats.getFs().getTotal().getAvailable().bytes();

            short diskPercent = -1;
            if (used >= 0 && avail > 0) {
                diskPercent = (short) (used * 100 / (used + avail));
            }

            table.startRow();
            table.addCell(shardCount);
            table.addCell(used < 0 ? null : new ByteSizeValue(used));
            table.addCell(avail < 0 ? null : new ByteSizeValue(avail));
            table.addCell(nodeStats.getFs().getTotal().getTotal());
            table.addCell(diskPercent < 0 ? null : diskPercent);
            table.addCell(node == null ? null : node.getHostName());
            table.addCell(node == null ? null : node.getHostAddress());
            table.addCell(node == null ? "UNASSIGNED" : node.name());
            table.endRow();
        }

        if (allocs.containsKey("UNASSIGNED")) {
            table.startRow();
            table.addCell(allocs.lget());
            table.addCell(null);
            table.addCell(null);
            table.addCell(null);
            table.addCell(null);
            table.addCell(null);
            table.addCell(null);
            table.addCell("UNASSIGNED");
            table.endRow();
        }

        return table;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8231.java