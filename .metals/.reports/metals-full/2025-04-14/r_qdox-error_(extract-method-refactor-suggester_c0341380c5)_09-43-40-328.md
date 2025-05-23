error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8034.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8034.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8034.java
text:
```scala
f@@iles.put(file.getName(), new StoreFileMetaData(file.getName(), file.length(), checksums.get(file.getName())));

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

package org.elasticsearch.indices.store;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.FailedNodeException;
import org.elasticsearch.action.support.nodes.*;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Streamable;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.env.NodeEnvironment;
import org.elasticsearch.index.service.IndexService;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.index.shard.service.InternalIndexShard;
import org.elasticsearch.index.store.Store;
import org.elasticsearch.index.store.StoreFileMetaData;
import org.elasticsearch.indices.IndicesService;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 *
 */
public class TransportNodesListShardStoreMetaData extends TransportNodesOperationAction<TransportNodesListShardStoreMetaData.Request, TransportNodesListShardStoreMetaData.NodesStoreFilesMetaData, TransportNodesListShardStoreMetaData.NodeRequest, TransportNodesListShardStoreMetaData.NodeStoreFilesMetaData> {

    private final IndicesService indicesService;

    private final NodeEnvironment nodeEnv;

    @Inject
    public TransportNodesListShardStoreMetaData(Settings settings, ClusterName clusterName, ThreadPool threadPool, ClusterService clusterService, TransportService transportService,
                                                IndicesService indicesService, NodeEnvironment nodeEnv) {
        super(settings, clusterName, threadPool, clusterService, transportService);
        this.indicesService = indicesService;
        this.nodeEnv = nodeEnv;
    }

    public ActionFuture<NodesStoreFilesMetaData> list(ShardId shardId, boolean onlyUnallocated, Set<String> nodesIds, @Nullable TimeValue timeout) {
        return execute(new Request(shardId, onlyUnallocated, nodesIds).timeout(timeout));
    }

    @Override
    protected String executor() {
        return ThreadPool.Names.GENERIC;
    }

    @Override
    protected String transportAction() {
        return "/cluster/nodes/indices/shard/store";
    }

    @Override
    protected Request newRequest() {
        return new Request();
    }

    @Override
    protected NodeRequest newNodeRequest() {
        return new NodeRequest();
    }

    @Override
    protected NodeRequest newNodeRequest(String nodeId, Request request) {
        return new NodeRequest(nodeId, request);
    }

    @Override
    protected NodeStoreFilesMetaData newNodeResponse() {
        return new NodeStoreFilesMetaData();
    }

    @Override
    protected NodesStoreFilesMetaData newResponse(Request request, AtomicReferenceArray responses) {
        final List<NodeStoreFilesMetaData> nodeStoreFilesMetaDatas = Lists.newArrayList();
        final List<FailedNodeException> failures = Lists.newArrayList();
        for (int i = 0; i < responses.length(); i++) {
            Object resp = responses.get(i);
            if (resp instanceof NodeStoreFilesMetaData) { // will also filter out null response for unallocated ones
                nodeStoreFilesMetaDatas.add((NodeStoreFilesMetaData) resp);
            } else if (resp instanceof FailedNodeException) {
                failures.add((FailedNodeException) resp);
            }
        }
        return new NodesStoreFilesMetaData(clusterName, nodeStoreFilesMetaDatas.toArray(new NodeStoreFilesMetaData[nodeStoreFilesMetaDatas.size()]),
                failures.toArray(new FailedNodeException[failures.size()]));
    }

    @Override
    protected NodeStoreFilesMetaData nodeOperation(NodeRequest request) throws ElasticSearchException {
        if (request.unallocated) {
            IndexService indexService = indicesService.indexService(request.shardId.index().name());
            if (indexService == null) {
                return new NodeStoreFilesMetaData(clusterService.state().nodes().localNode(), null);
            }
            if (!indexService.hasShard(request.shardId.id())) {
                return new NodeStoreFilesMetaData(clusterService.state().nodes().localNode(), null);
            }
        }
        IndexMetaData metaData = clusterService.state().metaData().index(request.shardId.index().name());
        if (metaData == null) {
            return new NodeStoreFilesMetaData(clusterService.state().nodes().localNode(), null);
        }
        try {
            return new NodeStoreFilesMetaData(clusterService.state().nodes().localNode(), listStoreMetaData(request.shardId));
        } catch (IOException e) {
            throw new ElasticSearchException("Failed to list store metadata for shard [" + request.shardId + "]", e);
        }
    }

    private StoreFilesMetaData listStoreMetaData(ShardId shardId) throws IOException {
        IndexService indexService = indicesService.indexService(shardId.index().name());
        if (indexService != null) {
            InternalIndexShard indexShard = (InternalIndexShard) indexService.shard(shardId.id());
            if (indexShard != null) {
                return new StoreFilesMetaData(true, shardId, indexShard.store().list());
            }
        }
        // try and see if we an list unallocated
        IndexMetaData metaData = clusterService.state().metaData().index(shardId.index().name());
        if (metaData == null) {
            return new StoreFilesMetaData(false, shardId, ImmutableMap.<String, StoreFileMetaData>of());
        }
        String storeType = metaData.settings().get("index.store.type", "fs");
        if (!storeType.contains("fs")) {
            return new StoreFilesMetaData(false, shardId, ImmutableMap.<String, StoreFileMetaData>of());
        }
        File[] shardLocations = nodeEnv.shardLocations(shardId);
        File[] shardIndexLocations = new File[shardLocations.length];
        for (int i = 0; i < shardLocations.length; i++) {
            shardIndexLocations[i] = new File(shardLocations[i], "index");
        }
        boolean exists = false;
        for (File shardIndexLocation : shardIndexLocations) {
            if (shardIndexLocation.exists()) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            return new StoreFilesMetaData(false, shardId, ImmutableMap.<String, StoreFileMetaData>of());
        }

        Map<String, String> checksums = Store.readChecksums(shardIndexLocations);
        if (checksums == null) {
            checksums = ImmutableMap.of();
        }

        Map<String, StoreFileMetaData> files = Maps.newHashMap();
        for (File shardIndexLocation : shardIndexLocations) {
            File[] listedFiles = shardIndexLocation.listFiles();
            if (listedFiles == null) {
                continue;
            }
            for (File file : listedFiles) {
                // BACKWARD CKS SUPPORT
                if (file.getName().endsWith(".cks")) {
                    continue;
                }
                if (Store.isChecksum(file.getName())) {
                    continue;
                }
                files.put(file.getName(), new StoreFileMetaData(file.getName(), file.length(), file.lastModified(), checksums.get(file.getName())));
            }
        }

        return new StoreFilesMetaData(false, shardId, files);
    }

    @Override
    protected boolean accumulateExceptions() {
        return true;
    }

    public static class StoreFilesMetaData implements Iterable<StoreFileMetaData>, Streamable {
        private boolean allocated;
        private ShardId shardId;
        private Map<String, StoreFileMetaData> files;

        StoreFilesMetaData() {
        }

        public StoreFilesMetaData(boolean allocated, ShardId shardId, Map<String, StoreFileMetaData> files) {
            this.allocated = allocated;
            this.shardId = shardId;
            this.files = files;
        }

        public boolean allocated() {
            return allocated;
        }

        public ShardId shardId() {
            return this.shardId;
        }

        public long totalSizeInBytes() {
            long totalSizeInBytes = 0;
            for (StoreFileMetaData file : this) {
                totalSizeInBytes += file.length();
            }
            return totalSizeInBytes;
        }

        @Override
        public Iterator<StoreFileMetaData> iterator() {
            return files.values().iterator();
        }

        public boolean fileExists(String name) {
            return files.containsKey(name);
        }

        public StoreFileMetaData file(String name) {
            return files.get(name);
        }

        public static StoreFilesMetaData readStoreFilesMetaData(StreamInput in) throws IOException {
            StoreFilesMetaData md = new StoreFilesMetaData();
            md.readFrom(in);
            return md;
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            allocated = in.readBoolean();
            shardId = ShardId.readShardId(in);
            int size = in.readVInt();
            files = Maps.newHashMapWithExpectedSize(size);
            for (int i = 0; i < size; i++) {
                StoreFileMetaData md = StoreFileMetaData.readStoreFileMetaData(in);
                files.put(md.name(), md);
            }
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            out.writeBoolean(allocated);
            shardId.writeTo(out);
            out.writeVInt(files.size());
            for (StoreFileMetaData md : files.values()) {
                md.writeTo(out);
            }
        }
    }


    static class Request extends NodesOperationRequest<Request> {

        private ShardId shardId;

        private boolean unallocated;

        public Request() {
        }

        public Request(ShardId shardId, boolean unallocated, Set<String> nodesIds) {
            super(nodesIds.toArray(new String[nodesIds.size()]));
            this.shardId = shardId;
            this.unallocated = unallocated;
        }

        public Request(ShardId shardId, boolean unallocated, String... nodesIds) {
            super(nodesIds);
            this.shardId = shardId;
            this.unallocated = unallocated;
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            super.readFrom(in);
            shardId = ShardId.readShardId(in);
            unallocated = in.readBoolean();
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            super.writeTo(out);
            shardId.writeTo(out);
            out.writeBoolean(unallocated);
        }
    }

    public static class NodesStoreFilesMetaData extends NodesOperationResponse<NodeStoreFilesMetaData> {

        private FailedNodeException[] failures;

        NodesStoreFilesMetaData() {
        }

        public NodesStoreFilesMetaData(ClusterName clusterName, NodeStoreFilesMetaData[] nodes, FailedNodeException[] failures) {
            super(clusterName, nodes);
            this.failures = failures;
        }

        public FailedNodeException[] failures() {
            return failures;
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            super.readFrom(in);
            nodes = new NodeStoreFilesMetaData[in.readVInt()];
            for (int i = 0; i < nodes.length; i++) {
                nodes[i] = NodeStoreFilesMetaData.readListShardStoreNodeOperationResponse(in);
            }
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            super.writeTo(out);
            out.writeVInt(nodes.length);
            for (NodeStoreFilesMetaData response : nodes) {
                response.writeTo(out);
            }
        }
    }


    static class NodeRequest extends NodeOperationRequest {

        private ShardId shardId;

        private boolean unallocated;

        NodeRequest() {
        }

        NodeRequest(String nodeId, TransportNodesListShardStoreMetaData.Request request) {
            super(request, nodeId);
            this.shardId = request.shardId;
            this.unallocated = request.unallocated;
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            super.readFrom(in);
            shardId = ShardId.readShardId(in);
            unallocated = in.readBoolean();
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            super.writeTo(out);
            shardId.writeTo(out);
            out.writeBoolean(unallocated);
        }
    }

    public static class NodeStoreFilesMetaData extends NodeOperationResponse {

        private StoreFilesMetaData storeFilesMetaData;

        NodeStoreFilesMetaData() {
        }

        public NodeStoreFilesMetaData(DiscoveryNode node, StoreFilesMetaData storeFilesMetaData) {
            super(node);
            this.storeFilesMetaData = storeFilesMetaData;
        }

        public StoreFilesMetaData storeFilesMetaData() {
            return storeFilesMetaData;
        }

        public static NodeStoreFilesMetaData readListShardStoreNodeOperationResponse(StreamInput in) throws IOException {
            NodeStoreFilesMetaData resp = new NodeStoreFilesMetaData();
            resp.readFrom(in);
            return resp;
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            super.readFrom(in);
            if (in.readBoolean()) {
                storeFilesMetaData = StoreFilesMetaData.readStoreFilesMetaData(in);
            }
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            super.writeTo(out);
            if (storeFilesMetaData == null) {
                out.writeBoolean(false);
            } else {
                out.writeBoolean(true);
                storeFilesMetaData.writeTo(out);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8034.java