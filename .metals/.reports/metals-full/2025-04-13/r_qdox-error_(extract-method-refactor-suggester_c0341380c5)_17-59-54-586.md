error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3921.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3921.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3921.java
text:
```scala
m@@etaDataBlobContainer.writeBlob(newMetaData, new ByteArrayInputStream(out.underlyingBytes(), 0, out.size()), out.size());

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

package org.elasticsearch.gateway.blobstore;

import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.metadata.MetaData;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.blobstore.BlobContainer;
import org.elasticsearch.common.blobstore.BlobMetaData;
import org.elasticsearch.common.blobstore.BlobPath;
import org.elasticsearch.common.blobstore.BlobStore;
import org.elasticsearch.common.blobstore.ImmutableBlobContainer;
import org.elasticsearch.common.collect.ImmutableMap;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.compress.lzf.LZF;
import org.elasticsearch.common.compress.lzf.LZFOutputStream;
import org.elasticsearch.common.io.FastByteArrayOutputStream;
import org.elasticsearch.common.io.stream.BytesStreamInput;
import org.elasticsearch.common.io.stream.CachedStreamInput;
import org.elasticsearch.common.io.stream.LZFStreamInput;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.gateway.GatewayException;
import org.elasticsearch.gateway.shared.SharedStorageGateway;
import org.elasticsearch.index.gateway.CommitPoint;
import org.elasticsearch.index.gateway.CommitPoints;
import org.elasticsearch.index.gateway.blobstore.BlobStoreIndexGateway;
import org.elasticsearch.threadpool.ThreadPool;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author kimchy (shay.banon)
 */
public abstract class BlobStoreGateway extends SharedStorageGateway {

    private BlobStore blobStore;

    private ByteSizeValue chunkSize;

    private BlobPath basePath;

    private ImmutableBlobContainer metaDataBlobContainer;

    private boolean compress;

    private volatile int currentIndex;

    protected BlobStoreGateway(Settings settings, ThreadPool threadPool, ClusterService clusterService) {
        super(settings, threadPool, clusterService);
    }

    protected void initialize(BlobStore blobStore, ClusterName clusterName, @Nullable ByteSizeValue defaultChunkSize) throws IOException {
        this.blobStore = blobStore;
        this.chunkSize = componentSettings.getAsBytesSize("chunk_size", defaultChunkSize);
        this.basePath = BlobPath.cleanPath().add(clusterName.value());
        this.metaDataBlobContainer = blobStore.immutableBlobContainer(basePath.add("metadata"));
        this.currentIndex = findLatestIndex();
        this.compress = componentSettings.getAsBoolean("compress", true);
        logger.debug("Latest metadata found at index [" + currentIndex + "]");
    }

    @Override public String toString() {
        return type() + "://" + blobStore + "/" + basePath;
    }

    public BlobStore blobStore() {
        return blobStore;
    }

    public BlobPath basePath() {
        return basePath;
    }

    public ByteSizeValue chunkSize() {
        return this.chunkSize;
    }

    @Override public void reset() throws Exception {
        blobStore.delete(BlobPath.cleanPath());
    }

    @Override public MetaData read() throws GatewayException {
        try {
            this.currentIndex = findLatestIndex();
        } catch (IOException e) {
            throw new GatewayException("Failed to find latest metadata to read from", e);
        }
        if (currentIndex == -1)
            return null;
        String metaData = "metadata-" + currentIndex;

        try {
            return readMetaData(metaDataBlobContainer.readBlobFully(metaData));
        } catch (GatewayException e) {
            throw e;
        } catch (Exception e) {
            throw new GatewayException("Failed to read metadata [" + metaData + "] from gateway", e);
        }
    }

    public CommitPoint findCommitPoint(String index, int shardId) throws IOException {
        BlobPath path = BlobStoreIndexGateway.shardPath(basePath, index, shardId);
        ImmutableBlobContainer container = blobStore.immutableBlobContainer(path);
        ImmutableMap<String, BlobMetaData> blobs = container.listBlobs();
        List<CommitPoint> commitPointsList = Lists.newArrayList();
        for (BlobMetaData md : blobs.values()) {
            if (md.length() == 0) { // a commit point that was not flushed yet...
                continue;
            }
            if (md.name().startsWith("commit-")) {
                try {
                    commitPointsList.add(CommitPoints.fromXContent(container.readBlobFully(md.name())));
                } catch (Exception e) {
                    logger.warn("failed to read commit point at path {} with name [{}]", e, path, md.name());
                }
            }
        }
        CommitPoints commitPoints = new CommitPoints(commitPointsList);
        if (commitPoints.commits().isEmpty()) {
            return null;
        }
        return commitPoints.commits().get(0);
    }


    @Override public void write(MetaData metaData) throws GatewayException {
        final String newMetaData = "metadata-" + (currentIndex + 1);
        try {
            FastByteArrayOutputStream out = new FastByteArrayOutputStream();
            OutputStream os = out;
            if (compress) {
                os = new LZFOutputStream(os);
            }
            XContentBuilder builder = XContentFactory.contentBuilder(XContentType.JSON, os);
            builder.startObject();
            MetaData.Builder.toXContent(metaData, builder, ToXContent.EMPTY_PARAMS);
            builder.endObject();
            builder.close();
            metaDataBlobContainer.writeBlob(newMetaData, new ByteArrayInputStream(out.unsafeByteArray(), 0, out.size()), out.size());
        } catch (IOException e) {
            throw new GatewayException("Failed to write metadata [" + newMetaData + "]", e);
        }

        currentIndex++;

        try {
            metaDataBlobContainer.deleteBlobsByFilter(new BlobContainer.BlobNameFilter() {
                @Override public boolean accept(String blobName) {
                    return blobName.startsWith("metadata-") && !newMetaData.equals(blobName);
                }
            });
        } catch (IOException e) {
            logger.debug("Failed to delete old metadata, will do it next time", e);
        }
    }

    private int findLatestIndex() throws IOException {
        ImmutableMap<String, BlobMetaData> blobs = metaDataBlobContainer.listBlobsByPrefix("metadata-");

        int index = -1;
        for (BlobMetaData md : blobs.values()) {
            if (logger.isTraceEnabled()) {
                logger.trace("[findLatestMetadata]: Processing [" + md.name() + "]");
            }
            String name = md.name();
            int fileIndex = Integer.parseInt(name.substring(name.indexOf('-') + 1));
            if (fileIndex >= index) {
                // try and read the meta data
                byte[] data = null;
                try {
                    data = metaDataBlobContainer.readBlobFully(name);
                    readMetaData(data);
                    index = fileIndex;
                } catch (IOException e) {
                    logger.warn("[findLatestMetadata]: failed to read metadata from [{}], data_length [{}] ignoring...", e, name, data == null ? "na" : data.length);
                }
            }
        }

        return index;
    }

    private MetaData readMetaData(byte[] data) throws IOException {
        XContentParser parser = null;
        try {
            if (LZF.isCompressed(data)) {
                BytesStreamInput siBytes = new BytesStreamInput(data);
                LZFStreamInput siLzf = CachedStreamInput.cachedLzf(siBytes);
                parser = XContentFactory.xContent(XContentType.JSON).createParser(siLzf);
            } else {
                parser = XContentFactory.xContent(XContentType.JSON).createParser(data);
            }
            return MetaData.Builder.fromXContent(parser);
        } finally {
            if (parser != null) {
                parser.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3921.java