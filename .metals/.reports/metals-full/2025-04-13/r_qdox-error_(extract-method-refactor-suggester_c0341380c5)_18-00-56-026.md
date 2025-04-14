error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3388.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3388.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3388.java
text:
```scala
r@@eturn BlobStoreIndexShardGateway.buildVirtualBlobs(indexContainer, indexContainer.listBlobs(), null);

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

package org.elasticsearch.index.gateway.blobstore;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.common.blobstore.BlobMetaData;
import org.elasticsearch.common.blobstore.BlobPath;
import org.elasticsearch.common.blobstore.BlobStore;
import org.elasticsearch.common.blobstore.ImmutableBlobContainer;
import org.elasticsearch.common.collect.ImmutableMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.gateway.Gateway;
import org.elasticsearch.gateway.blobstore.BlobStoreGateway;
import org.elasticsearch.gateway.none.NoneGateway;
import org.elasticsearch.index.AbstractIndexComponent;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.gateway.IndexGateway;
import org.elasticsearch.index.settings.IndexSettings;

import java.io.IOException;

/**
 * @author kimchy (shay.banon)
 */
public abstract class BlobStoreIndexGateway extends AbstractIndexComponent implements IndexGateway {

    private final BlobStoreGateway gateway;

    private final BlobStore blobStore;

    private final BlobPath indexPath;

    protected ByteSizeValue chunkSize;

    protected BlobStoreIndexGateway(Index index, @IndexSettings Settings indexSettings, Gateway gateway) {
        super(index, indexSettings);

        if (gateway.type().equals(NoneGateway.TYPE)) {
            logger.warn("index gateway is configured, but no cluster level gateway configured, cluster level metadata will be lost on full shutdown");
        }

        this.gateway = (BlobStoreGateway) gateway;
        this.blobStore = this.gateway.blobStore();

        this.chunkSize = componentSettings.getAsBytesSize("chunk_size", this.gateway.chunkSize());

        this.indexPath = this.gateway.basePath().add("indices").add(index.name());
    }

    public ImmutableMap<String, BlobMetaData> listIndexBlobs(int shardId) throws IOException {
        ImmutableBlobContainer indexContainer = blobStore.immutableBlobContainer(shardIndexPath(shardId));
        return BlobStoreIndexShardGateway.aggregateParts(indexContainer.listBlobs());
    }

    @Override public String toString() {
        return type() + "://" + blobStore + "/" + indexPath;
    }

    public BlobStore blobStore() {
        return blobStore;
    }

    public BlobPath indexPath() {
        return this.indexPath;
    }

    public ByteSizeValue chunkSize() {
        return this.chunkSize;
    }

    public BlobPath shardPath(int shardId) {
        return indexPath.add(Integer.toString(shardId));
    }

    public BlobPath shardIndexPath(int shardId) {
        return shardPath(shardId).add("index");
    }

    public BlobPath shardTranslogPath(int shardId) {
        return shardPath(shardId).add("translog");
    }

    @Override public void close(boolean delete) throws ElasticSearchException {
        if (delete) {
            blobStore.delete(indexPath);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3388.java