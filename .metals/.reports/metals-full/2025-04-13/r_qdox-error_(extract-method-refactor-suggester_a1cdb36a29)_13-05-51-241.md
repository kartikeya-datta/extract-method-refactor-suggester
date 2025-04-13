error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3859.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3859.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3859.java
text:
```scala
final C@@lusterStateRequest clusterStateRequest = Requests.clusterStateRequest();

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

package org.elasticsearch.rest.action.admin.cluster.state;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.cluster.state.ClusterStateRequest;
import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.block.ClusterBlock;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.cluster.routing.IndexRoutingTable;
import org.elasticsearch.cluster.routing.IndexShardRoutingTable;
import org.elasticsearch.cluster.routing.RoutingNode;
import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.common.collect.ImmutableSet;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsFilter;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.builder.XContentBuilder;
import org.elasticsearch.rest.*;
import org.elasticsearch.rest.action.support.RestActions;
import org.elasticsearch.rest.action.support.RestXContentBuilder;

import java.io.IOException;
import java.util.Map;

/**
 * @author kimchy (shay.banon)
 */
public class RestClusterStateAction extends BaseRestHandler {

    private final SettingsFilter settingsFilter;

    @Inject public RestClusterStateAction(Settings settings, Client client, RestController controller,
                                          SettingsFilter settingsFilter) {
        super(settings, client);
        controller.registerHandler(RestRequest.Method.GET, "/_cluster/state", this);

        this.settingsFilter = settingsFilter;
    }

    @Override public void handleRequest(final RestRequest request, final RestChannel channel) {
        final ClusterStateRequest clusterStateRequest = Requests.clusterState();
        clusterStateRequest.filterNodes(request.paramAsBoolean("filter_nodes", clusterStateRequest.filterNodes()));
        clusterStateRequest.filterRoutingTable(request.paramAsBoolean("filter_routing_table", clusterStateRequest.filterRoutingTable()));
        clusterStateRequest.filterMetaData(request.paramAsBoolean("filter_metadata", clusterStateRequest.filterMetaData()));
        clusterStateRequest.filterBlocks(request.paramAsBoolean("filter_blocks", clusterStateRequest.filterBlocks()));
        clusterStateRequest.filteredIndices(RestActions.splitIndices(request.param("filter_indices", null)));
        client.admin().cluster().state(clusterStateRequest, new ActionListener<ClusterStateResponse>() {
            @Override public void onResponse(ClusterStateResponse response) {
                try {
                    ClusterState state = response.state();
                    XContentBuilder builder = RestXContentBuilder.restContentBuilder(request);
                    builder.startObject();

                    builder.field("cluster_name", response.clusterName().value());

                    if (!clusterStateRequest.filterNodes()) {
                        builder.field("master_node", state.nodes().masterNodeId());
                    }

                    // blocks
                    if (!clusterStateRequest.filterBlocks()) {
                        builder.startObject("blocks");

                        if (!state.blocks().global().isEmpty()) {
                            builder.startObject("global");
                            for (ClusterBlock block : state.blocks().global()) {
                                block.toXContent(builder, request);
                            }
                            builder.endObject();
                        }

                        if (!state.blocks().indices().isEmpty()) {
                            builder.startObject("indices");
                            for (Map.Entry<String, ImmutableSet<ClusterBlock>> entry : state.blocks().indices().entrySet()) {
                                builder.startObject(entry.getKey());
                                for (ClusterBlock block : entry.getValue()) {
                                    block.toXContent(builder, request);
                                }
                                builder.endObject();
                            }
                            builder.endObject();
                        }

                        builder.endObject();
                    }

                    // nodes
                    if (!clusterStateRequest.filterNodes()) {
                        builder.startObject("nodes");
                        for (DiscoveryNode node : state.nodes()) {
                            builder.startObject(node.id());
                            builder.field("name", node.name());
                            builder.field("transport_address", node.address().toString());
                            builder.endObject();
                        }
                        builder.endObject();
                    }

                    // meta data
                    if (!clusterStateRequest.filterMetaData()) {
                        builder.startObject("metadata");

                        builder.startObject("indices");
                        for (IndexMetaData indexMetaData : state.metaData()) {
                            builder.startObject(indexMetaData.index());

                            builder.startObject("settings");
                            Settings settings = settingsFilter.filterSettings(indexMetaData.settings());
                            for (Map.Entry<String, String> entry : settings.getAsMap().entrySet()) {
                                builder.field(entry.getKey(), entry.getValue());
                            }
                            builder.endObject();

                            builder.startObject("mappings");
                            for (Map.Entry<String, String> entry : indexMetaData.mappings().entrySet()) {
                                XContentParser parser = XContentFactory.xContent(entry.getValue()).createParser(entry.getValue());
                                Map<String, Object> mapping = parser.map();
                                if (mapping.size() == 1 && mapping.containsKey(entry.getKey())) {
                                    // the type name is the root value, reduce it
                                    mapping = (Map<String, Object>) mapping.get(entry.getKey());
                                }
                                builder.field(entry.getKey());
                                builder.map(mapping);
                            }
                            builder.endObject();

                            builder.startArray("aliases");
                            for (String alias : indexMetaData.aliases()) {
                                builder.value(alias);
                            }
                            builder.endArray();

                            builder.endObject();
                        }
                        builder.endObject();

                        builder.endObject();
                    }

                    // routing table
                    if (!clusterStateRequest.filterRoutingTable()) {
                        builder.startObject("routing_table");
                        builder.startObject("indices");
                        for (IndexRoutingTable indexRoutingTable : state.routingTable()) {
                            builder.startObject(indexRoutingTable.index());
                            builder.startObject("shards");
                            for (IndexShardRoutingTable indexShardRoutingTable : indexRoutingTable) {
                                builder.startArray(Integer.toString(indexShardRoutingTable.shardId().id()));
                                for (ShardRouting shardRouting : indexShardRoutingTable) {
                                    jsonShardRouting(builder, shardRouting);
                                }
                                builder.endArray();
                            }
                            builder.endObject();
                            builder.endObject();
                        }
                        builder.endObject();
                        builder.endObject();
                    }

                    // routing nodes
                    if (!clusterStateRequest.filterRoutingTable()) {
                        builder.startObject("routing_nodes");
                        builder.startArray("unassigned");
                        for (ShardRouting shardRouting : state.readOnlyRoutingNodes().unassigned()) {
                            jsonShardRouting(builder, shardRouting);
                        }
                        builder.endArray();

                        builder.startObject("nodes");
                        for (RoutingNode routingNode : state.readOnlyRoutingNodes()) {
                            builder.startArray(routingNode.nodeId());
                            for (ShardRouting shardRouting : routingNode) {
                                jsonShardRouting(builder, shardRouting);
                            }
                            builder.endArray();
                        }
                        builder.endObject();

                        builder.endObject();
                    }

                    builder.endObject();
                    channel.sendResponse(new XContentRestResponse(request, RestResponse.Status.OK, builder));
                } catch (Exception e) {
                    onFailure(e);
                }
            }

            private void jsonShardRouting(XContentBuilder builder, ShardRouting shardRouting) throws IOException {
                builder.startObject()
                        .field("state", shardRouting.state())
                        .field("primary", shardRouting.primary())
                        .field("node", shardRouting.currentNodeId())
                        .field("relocating_node", shardRouting.relocatingNodeId())
                        .field("shard", shardRouting.shardId().id())
                        .field("index", shardRouting.shardId().index().name())
                        .endObject();
            }

            @Override public void onFailure(Throwable e) {
                try {
                    channel.sendResponse(new XContentThrowableRestResponse(request, e));
                } catch (IOException e1) {
                    logger.error("Failed to send failure response", e1);
                }
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3859.java