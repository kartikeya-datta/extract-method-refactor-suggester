error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4663.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4663.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4663.java
text:
```scala
l@@ogger.debug("[{}][{}] failed to multi percolate", e, request.index(), request.shardId());

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

package org.elasticsearch.action.percolate;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.ExceptionsHelper;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.support.TransportActions;
import org.elasticsearch.action.support.single.shard.SingleShardOperationRequest;
import org.elasticsearch.action.support.single.shard.TransportShardSingleOperationAction;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.block.ClusterBlockException;
import org.elasticsearch.cluster.block.ClusterBlockLevel;
import org.elasticsearch.cluster.routing.ShardIterator;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.StringText;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.percolator.PercolatorService;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class TransportShardMultiPercolateAction extends TransportShardSingleOperationAction<TransportShardMultiPercolateAction.Request, TransportShardMultiPercolateAction.Response> {

    private final PercolatorService percolatorService;

    @Inject
    public TransportShardMultiPercolateAction(Settings settings, ThreadPool threadPool, ClusterService clusterService, TransportService transportService, PercolatorService percolatorService) {
        super(settings, threadPool, clusterService, transportService);
        this.percolatorService = percolatorService;
    }

    @Override
    protected String transportAction() {
        return "mpercolate/shard";
    }

    @Override
    protected String executor() {
        return ThreadPool.Names.PERCOLATE;
    }

    @Override
    protected Request newRequest() {
        return new Request();
    }

    @Override
    protected Response newResponse() {
        return new Response();
    }

    @Override
    protected ClusterBlockException checkGlobalBlock(ClusterState state, Request request) {
        return state.blocks().globalBlockedException(ClusterBlockLevel.READ);
    }

    @Override
    protected ClusterBlockException checkRequestBlock(ClusterState state, Request request) {
        return state.blocks().indexBlockedException(ClusterBlockLevel.READ, request.index());
    }

    @Override
    protected ShardIterator shards(ClusterState state, Request request) throws ElasticSearchException {
        return clusterService.operationRouting().getShards(
                clusterService.state(), request.index(), request.shardId(), request.preference
        );
    }

    @Override
    protected Response shardOperation(Request request, int shardId) throws ElasticSearchException {
        // TODO: Look into combining the shard req's docs into one in memory index.
        Response response = new Response();
        response.items = new ArrayList<Response.Item>(request.items.size());
        for (Request.Item item : request.items) {
            Response.Item responseItem = new Response.Item();
            responseItem.slot = item.slot;
            try {
                responseItem.response = percolatorService.percolate(item.request);
            } catch (Throwable e) {
                logger.trace("[{}][{}] failed to multi percolate", e, request.index(), request.shardId());
                if (TransportActions.isShardNotAvailableException(e)) {
                    throw new ElasticSearchException("", e);
                } else {
                    responseItem.error = new StringText(ExceptionsHelper.detailedMessage(e));
                }
            }
            response.items.add(responseItem);
        }
        return response;
    }


    public static class Request extends SingleShardOperationRequest {

        private int shardId;
        private String preference;
        private List<Item> items;

        public Request() {
        }

        public Request(String concreteIndex, int shardId, String preference) {
            this.index = concreteIndex;
            this.shardId = shardId;
            this.preference = preference;
            this.items = new ArrayList<Item>();
        }

        public int shardId() {
            return shardId;
        }

        public void add(Item item) {
            items.add(item);
        }

        public List<Item> items() {
            return items;
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            super.readFrom(in);
            shardId = in.readVInt();
            preference = in.readOptionalString();
            int size = in.readVInt();
            items = new ArrayList<Item>(size);
            for (int i = 0; i < size; i++) {
                Item item = new Item();
                item.slot = in.readVInt();
                item.request = new PercolateShardRequest(index(), shardId);
                item.request.documentType(in.readString());
                item.request.source(in.readBytesReference());
                item.request.docSource(in.readBytesReference());
                item.request.onlyCount(in.readBoolean());
                items.add(item);
            }
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            super.writeTo(out);
            out.writeVInt(shardId);
            out.writeOptionalString(preference);
            out.writeVInt(items.size());
            for (Item item : items) {
                out.writeVInt(item.slot);
                out.writeString(item.request.documentType());
                out.writeBytesReference(item.request.source());
                out.writeBytesReference(item.request.docSource());
                out.writeBoolean(item.request.onlyCount());
            }
        }

        public static class Item {

            private int slot;
            private PercolateShardRequest request;

            Item() {
            }

            public Item(int slot, PercolateShardRequest request) {
                this.slot = slot;
                this.request = request;
            }

            public int slot() {
                return slot;
            }

            public PercolateShardRequest request() {
                return request;
            }

        }

    }

    public static class Response extends ActionResponse {

        private List<Item> items;

        public List<Item> items() {
            return items;
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            super.writeTo(out);
            out.writeVInt(items.size());
            for (Item item : items) {
                out.writeVInt(item.slot);
                if (item.response != null) {
                    out.writeBoolean(true);
                    item.response.writeTo(out);
                } else {
                    out.writeBoolean(false);
                    out.writeText(item.error);
                }
            }
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            super.readFrom(in);
            int size = in.readVInt();
            items = new ArrayList<Item>(size);
            for (int i = 0; i < size; i++) {
                Item item = new Item();
                item.slot = in.readVInt();
                if (in.readBoolean()) {
                    item.response = new PercolateShardResponse();
                    item.response.readFrom(in);
                } else {
                    item.error = in.readText();
                }
                items.add(item);
            }
        }

        public static class Item {

            private int slot;
            private PercolateShardResponse response;
            private Text error;

            public int slot() {
                return slot;
            }

            public PercolateShardResponse response() {
                return response;
            }

            public Text error() {
                return error;
            }

            public boolean failed() {
                return error != null;
            }
        }

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4663.java