error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7925.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7925.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7925.java
text:
```scala
i@@f ("cluster".equals(sLevel)) {

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

package org.elasticsearch.rest.action.admin.cluster.health;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.cluster.health.*;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentBuilderString;
import org.elasticsearch.rest.*;
import org.elasticsearch.rest.action.support.RestActions;
import org.elasticsearch.rest.action.support.RestXContentBuilder;

import java.io.IOException;

import static org.elasticsearch.client.Requests.clusterHealthRequest;
import static org.elasticsearch.rest.RestStatus.PRECONDITION_FAILED;

/**
 *
 */
public class RestClusterHealthAction extends BaseRestHandler {

    @Inject
    public RestClusterHealthAction(Settings settings, Client client, RestController controller) {
        super(settings, client);

        controller.registerHandler(RestRequest.Method.GET, "/_cluster/health", this);
        controller.registerHandler(RestRequest.Method.GET, "/_cluster/health/{index}", this);
    }

    @Override
    public void handleRequest(final RestRequest request, final RestChannel channel) {
        ClusterHealthRequest clusterHealthRequest = clusterHealthRequest(RestActions.splitIndices(request.param("index")));
        int level = 0;
        try {
            clusterHealthRequest.masterNodeTimeout(request.paramAsTime("master_timeout", clusterHealthRequest.masterNodeTimeout()));
            clusterHealthRequest.timeout(request.paramAsTime("timeout", clusterHealthRequest.timeout()));
            String waitForStatus = request.param("wait_for_status");
            if (waitForStatus != null) {
                clusterHealthRequest.waitForStatus(ClusterHealthStatus.valueOf(waitForStatus.toUpperCase()));
            }
            clusterHealthRequest.waitForRelocatingShards(request.paramAsInt("wait_for_relocating_shards", clusterHealthRequest.waitForRelocatingShards()));
            clusterHealthRequest.waitForActiveShards(request.paramAsInt("wait_for_active_shards", clusterHealthRequest.waitForActiveShards()));
            clusterHealthRequest.waitForNodes(request.param("wait_for_nodes", clusterHealthRequest.waitForNodes()));
            String sLevel = request.param("level");
            if (sLevel != null) {
                if ("cluster".equals("sLevel")) {
                    level = 0;
                } else if ("indices".equals(sLevel)) {
                    level = 1;
                } else if ("shards".equals(sLevel)) {
                    level = 2;
                }
            }
        } catch (Exception e) {
            try {
                XContentBuilder builder = RestXContentBuilder.restContentBuilder(request);
                channel.sendResponse(new XContentRestResponse(request, PRECONDITION_FAILED, builder.startObject().field("error", e.getMessage()).endObject()));
            } catch (IOException e1) {
                logger.error("Failed to send failure response", e1);
            }
            return;
        }
        final int fLevel = level;
        client.admin().cluster().health(clusterHealthRequest, new ActionListener<ClusterHealthResponse>() {
            @Override
            public void onResponse(ClusterHealthResponse response) {
                try {
                    RestStatus status = RestStatus.OK;
                    // not sure..., we handle the health API, so we are not unavailable
                    // in any case, "/" should be used for
                    //if (response.status() == ClusterHealthStatus.RED) {
                    //    status = RestStatus.SERVICE_UNAVAILABLE;
                    //}
                    XContentBuilder builder = RestXContentBuilder.restContentBuilder(request);
                    builder.startObject();

                    builder.field(Fields.CLUSTER_NAME, response.clusterName());
                    builder.field(Fields.STATUS, response.status().name().toLowerCase());
                    builder.field(Fields.TIMED_OUT, response.timedOut());
                    builder.field(Fields.NUMBER_OF_NODES, response.numberOfNodes());
                    builder.field(Fields.NUMBER_OF_DATA_NODES, response.numberOfDataNodes());
                    builder.field(Fields.ACTIVE_PRIMARY_SHARDS, response.activePrimaryShards());
                    builder.field(Fields.ACTIVE_SHARDS, response.activeShards());
                    builder.field(Fields.RELOCATING_SHARDS, response.relocatingShards());
                    builder.field(Fields.INITIALIZING_SHARDS, response.initializingShards());
                    builder.field(Fields.UNASSIGNED_SHARDS, response.unassignedShards());

                    if (!response.validationFailures().isEmpty()) {
                        builder.startArray(Fields.VALIDATION_FAILURES);
                        for (String validationFailure : response.validationFailures()) {
                            builder.value(validationFailure);
                        }
                        // if we don't print index level information, still print the index validation failures
                        // so we know why the status is red
                        if (fLevel == 0) {
                            for (ClusterIndexHealth indexHealth : response) {
                                builder.startObject(indexHealth.index());

                                if (!indexHealth.validationFailures().isEmpty()) {
                                    builder.startArray(Fields.VALIDATION_FAILURES);
                                    for (String validationFailure : indexHealth.validationFailures()) {
                                        builder.value(validationFailure);
                                    }
                                    builder.endArray();
                                }

                                builder.endObject();
                            }
                        }
                        builder.endArray();
                    }

                    if (fLevel > 0) {
                        builder.startObject(Fields.INDICES);
                        for (ClusterIndexHealth indexHealth : response) {
                            builder.startObject(indexHealth.index(), XContentBuilder.FieldCaseConversion.NONE);

                            builder.field(Fields.STATUS, indexHealth.status().name().toLowerCase());
                            builder.field(Fields.NUMBER_OF_SHARDS, indexHealth.numberOfShards());
                            builder.field(Fields.NUMBER_OF_REPLICAS, indexHealth.numberOfReplicas());
                            builder.field(Fields.ACTIVE_PRIMARY_SHARDS, indexHealth.activePrimaryShards());
                            builder.field(Fields.ACTIVE_SHARDS, indexHealth.activeShards());
                            builder.field(Fields.RELOCATING_SHARDS, indexHealth.relocatingShards());
                            builder.field(Fields.INITIALIZING_SHARDS, indexHealth.initializingShards());
                            builder.field(Fields.UNASSIGNED_SHARDS, indexHealth.unassignedShards());

                            if (!indexHealth.validationFailures().isEmpty()) {
                                builder.startArray(Fields.VALIDATION_FAILURES);
                                for (String validationFailure : indexHealth.validationFailures()) {
                                    builder.value(validationFailure);
                                }
                                builder.endArray();
                            }

                            if (fLevel > 1) {
                                builder.startObject(Fields.SHARDS);

                                for (ClusterShardHealth shardHealth : indexHealth) {
                                    builder.startObject(Integer.toString(shardHealth.id()));

                                    builder.field(Fields.STATUS, shardHealth.status().name().toLowerCase());
                                    builder.field(Fields.PRIMARY_ACTIVE, shardHealth.primaryActive());
                                    builder.field(Fields.ACTIVE_SHARDS, shardHealth.activeShards());
                                    builder.field(Fields.RELOCATING_SHARDS, shardHealth.relocatingShards());
                                    builder.field(Fields.INITIALIZING_SHARDS, shardHealth.initializingShards());
                                    builder.field(Fields.UNASSIGNED_SHARDS, shardHealth.unassignedShards());

                                    builder.endObject();
                                }

                                builder.endObject();
                            }

                            builder.endObject();
                        }
                        builder.endObject();
                    }

                    builder.endObject();

                    channel.sendResponse(new XContentRestResponse(request, status, builder));
                } catch (Exception e) {
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

    static final class Fields {
        static final XContentBuilderString CLUSTER_NAME = new XContentBuilderString("cluster_name");
        static final XContentBuilderString STATUS = new XContentBuilderString("status");
        static final XContentBuilderString TIMED_OUT = new XContentBuilderString("timed_out");
        static final XContentBuilderString NUMBER_OF_SHARDS = new XContentBuilderString("number_of_shards");
        static final XContentBuilderString NUMBER_OF_REPLICAS = new XContentBuilderString("number_of_replicas");
        static final XContentBuilderString NUMBER_OF_NODES = new XContentBuilderString("number_of_nodes");
        static final XContentBuilderString NUMBER_OF_DATA_NODES = new XContentBuilderString("number_of_data_nodes");
        static final XContentBuilderString ACTIVE_PRIMARY_SHARDS = new XContentBuilderString("active_primary_shards");
        static final XContentBuilderString ACTIVE_SHARDS = new XContentBuilderString("active_shards");
        static final XContentBuilderString RELOCATING_SHARDS = new XContentBuilderString("relocating_shards");
        static final XContentBuilderString INITIALIZING_SHARDS = new XContentBuilderString("initializing_shards");
        static final XContentBuilderString UNASSIGNED_SHARDS = new XContentBuilderString("unassigned_shards");
        static final XContentBuilderString VALIDATION_FAILURES = new XContentBuilderString("validation_failures");
        static final XContentBuilderString INDICES = new XContentBuilderString("indices");
        static final XContentBuilderString SHARDS = new XContentBuilderString("shards");
        static final XContentBuilderString PRIMARY_ACTIVE = new XContentBuilderString("primary_active");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7925.java