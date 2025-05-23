error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/954.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/954.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/954.java
text:
```scala
b@@uilder.startObject(indexStatus.index(), XContentBuilder.FieldCaseConversion.NONE);

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

package org.elasticsearch.rest.action.admin.indices.status;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.status.*;
import org.elasticsearch.action.support.broadcast.BroadcastOperationThreading;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsFilter;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.*;
import org.elasticsearch.rest.action.support.RestXContentBuilder;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.rest.RestRequest.Method.*;
import static org.elasticsearch.rest.RestResponse.Status.*;
import static org.elasticsearch.rest.action.support.RestActions.*;

/**
 * @author kimchy (Shay Banon)
 */
public class RestIndicesStatusAction extends BaseRestHandler {

    private final SettingsFilter settingsFilter;

    @Inject public RestIndicesStatusAction(Settings settings, Client client, RestController controller,
                                           SettingsFilter settingsFilter) {
        super(settings, client);
        controller.registerHandler(GET, "/_status", this);
        controller.registerHandler(GET, "/{index}/_status", this);

        this.settingsFilter = settingsFilter;
    }

    @Override public void handleRequest(final RestRequest request, final RestChannel channel) {
        IndicesStatusRequest indicesStatusRequest = new IndicesStatusRequest(splitIndices(request.param("index")));
        // we just send back a response, no need to fork a listener
        indicesStatusRequest.listenerThreaded(false);
        BroadcastOperationThreading operationThreading = BroadcastOperationThreading.fromString(request.param("operation_threading"), BroadcastOperationThreading.SINGLE_THREAD);
        if (operationThreading == BroadcastOperationThreading.NO_THREADS) {
            // since we don't spawn, don't allow no_threads, but change it to a single thread
            operationThreading = BroadcastOperationThreading.SINGLE_THREAD;
        }
        indicesStatusRequest.operationThreading(operationThreading);
        client.admin().indices().status(indicesStatusRequest, new ActionListener<IndicesStatusResponse>() {
            @Override public void onResponse(IndicesStatusResponse response) {
                try {
                    XContentBuilder builder = RestXContentBuilder.restContentBuilder(request);
                    builder.startObject();
                    builder.field("ok", true);

                    buildBroadcastShardsHeader(builder, response);

                    builder.startObject("indices");
                    for (IndexStatus indexStatus : response.indices().values()) {
                        builder.startObject(indexStatus.index());

                        builder.array("aliases", indexStatus.settings().getAsArray("index.aliases"));

                        builder.startObject("settings");
                        Settings settings = settingsFilter.filterSettings(indexStatus.settings());
                        for (Map.Entry<String, String> entry : settings.getAsMap().entrySet()) {
                            builder.field(entry.getKey(), entry.getValue());
                        }
                        builder.endObject();

                        if (indexStatus.storeSize() != null) {
                            builder.field("store_size", indexStatus.storeSize().toString());
                            builder.field("store_size_in_bytes", indexStatus.storeSize().bytes());
                        }
                        if (indexStatus.translogOperations() != -1) {
                            builder.field("translog_operations", indexStatus.translogOperations());
                        }

                        if (indexStatus.docs() != null) {
                            builder.startObject("docs");
                            builder.field("num_docs", indexStatus.docs().numDocs());
                            builder.field("max_doc", indexStatus.docs().maxDoc());
                            builder.field("deleted_docs", indexStatus.docs().deletedDocs());
                            builder.endObject();
                        }

                        builder.startObject("shards");
                        for (IndexShardStatus indexShardStatus : indexStatus) {
                            builder.startArray(Integer.toString(indexShardStatus.shardId().id()));
                            for (ShardStatus shardStatus : indexShardStatus) {
                                builder.startObject();

                                builder.startObject("routing")
                                        .field("state", shardStatus.shardRouting().state())
                                        .field("primary", shardStatus.shardRouting().primary())
                                        .field("node", shardStatus.shardRouting().currentNodeId())
                                        .field("relocating_node", shardStatus.shardRouting().relocatingNodeId())
                                        .field("shard", shardStatus.shardRouting().shardId().id())
                                        .field("index", shardStatus.shardRouting().shardId().index().name())
                                        .endObject();

                                builder.field("state", shardStatus.state());
                                if (shardStatus.storeSize() != null) {
                                    builder.startObject("index");
                                    builder.field("size", shardStatus.storeSize().toString());
                                    builder.field("size_in_bytes", shardStatus.storeSize().bytes());
                                    builder.endObject();
                                }
                                if (shardStatus.translogId() != -1) {
                                    builder.startObject("translog");
                                    builder.field("id", shardStatus.translogId());
                                    builder.field("operations", shardStatus.translogOperations());
                                    builder.endObject();
                                }

                                if (shardStatus.docs() != null) {
                                    builder.startObject("docs");
                                    builder.field("num_docs", shardStatus.docs().numDocs());
                                    builder.field("max_doc", shardStatus.docs().maxDoc());
                                    builder.field("deleted_docs", shardStatus.docs().deletedDocs());
                                    builder.endObject();
                                }

                                if (shardStatus.peerRecoveryStatus() != null) {
                                    PeerRecoveryStatus peerRecoveryStatus = shardStatus.peerRecoveryStatus();
                                    builder.startObject("peer_recovery");
                                    builder.field("stage", peerRecoveryStatus.stage());
                                    builder.field("start_time_in_millis", peerRecoveryStatus.startTime());
                                    builder.field("time", peerRecoveryStatus.time());
                                    builder.field("time_in_millis", peerRecoveryStatus.time().millis());

                                    builder.startObject("index");
                                    builder.field("progress", peerRecoveryStatus.indexRecoveryProgress());
                                    builder.field("size", peerRecoveryStatus.indexSize());
                                    builder.field("size_in_bytes", peerRecoveryStatus.indexSize().bytes());
                                    builder.field("reused_size", peerRecoveryStatus.reusedIndexSize());
                                    builder.field("reused_size_in_bytes", peerRecoveryStatus.reusedIndexSize().bytes());
                                    builder.field("expected_recovered_size", peerRecoveryStatus.expectedRecoveredIndexSize());
                                    builder.field("expected_recovered_size_in_bytes", peerRecoveryStatus.expectedRecoveredIndexSize().bytes());
                                    builder.field("recovered_size", peerRecoveryStatus.recoveredIndexSize());
                                    builder.field("recovered_size_in_bytes", peerRecoveryStatus.recoveredIndexSize().bytes());
                                    builder.endObject();

                                    builder.startObject("translog");
                                    builder.field("recovered", peerRecoveryStatus.recoveredTranslogOperations());
                                    builder.endObject();

                                    builder.endObject();
                                }

                                if (shardStatus.gatewayRecoveryStatus() != null) {
                                    GatewayRecoveryStatus gatewayRecoveryStatus = shardStatus.gatewayRecoveryStatus();
                                    builder.startObject("gateway_recovery");
                                    builder.field("stage", gatewayRecoveryStatus.stage());
                                    builder.field("start_time_in_millis", gatewayRecoveryStatus.startTime());
                                    builder.field("time", gatewayRecoveryStatus.time());
                                    builder.field("time_in_millis", gatewayRecoveryStatus.time().millis());

                                    builder.startObject("index");
                                    builder.field("progress", gatewayRecoveryStatus.indexRecoveryProgress());
                                    builder.field("size", gatewayRecoveryStatus.indexSize());
                                    builder.field("size_in_bytes", gatewayRecoveryStatus.indexSize().bytes());
                                    builder.field("reused_size", gatewayRecoveryStatus.reusedIndexSize());
                                    builder.field("reused_size_in_bytes", gatewayRecoveryStatus.reusedIndexSize().bytes());
                                    builder.field("expected_recovered_size", gatewayRecoveryStatus.expectedRecoveredIndexSize());
                                    builder.field("expected_recovered_size_in_bytes", gatewayRecoveryStatus.expectedRecoveredIndexSize().bytes());
                                    builder.field("recovered_size", gatewayRecoveryStatus.recoveredIndexSize());
                                    builder.field("recovered_size_in_bytes", gatewayRecoveryStatus.recoveredIndexSize().bytes());
                                    builder.endObject();

                                    builder.startObject("translog");
                                    builder.field("recovered", gatewayRecoveryStatus.recoveredTranslogOperations());
                                    builder.endObject();

                                    builder.endObject();
                                }

                                if (shardStatus.gatewaySnapshotStatus() != null) {
                                    GatewaySnapshotStatus gatewaySnapshotStatus = shardStatus.gatewaySnapshotStatus();
                                    builder.startObject("gateway_snapshot");
                                    builder.field("stage", gatewaySnapshotStatus.stage());
                                    builder.field("start_time_in_millis", gatewaySnapshotStatus.startTime());
                                    builder.field("time", gatewaySnapshotStatus.time());
                                    builder.field("time_in_millis", gatewaySnapshotStatus.time().millis());

                                    builder.startObject("index");
                                    builder.field("size", gatewaySnapshotStatus.indexSize());
                                    builder.field("size_in_bytes", gatewaySnapshotStatus.indexSize().bytes());
                                    builder.endObject();

                                    builder.startObject("index");
                                    builder.field("expected_operations", gatewaySnapshotStatus.expectedNumberOfOperations());
                                    builder.endObject();

                                    builder.endObject();
                                }

                                builder.endObject();
                            }
                            builder.endArray();
                        }
                        builder.endObject();

                        builder.endObject();
                    }
                    builder.endObject();

                    builder.endObject();
                    channel.sendResponse(new XContentRestResponse(request, OK, builder));
                } catch (Exception e) {
                    onFailure(e);
                }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/954.java