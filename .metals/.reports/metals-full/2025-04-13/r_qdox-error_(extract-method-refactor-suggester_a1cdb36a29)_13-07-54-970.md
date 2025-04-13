error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5410.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5410.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5410.java
text:
```scala
(@@(InternalIndexShard) indexShard).engine().updateIndexingBufferSize(shardIndexingBufferSize);

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

package org.elasticsearch.indices.memory;

import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.index.service.IndexService;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.index.shard.service.IndexShard;
import org.elasticsearch.index.shard.service.InternalIndexShard;
import org.elasticsearch.indices.IndicesLifecycle;
import org.elasticsearch.indices.IndicesService;
import org.elasticsearch.monitor.jvm.JvmInfo;

/**
 * @author kimchy (shay.banon)
 */
public class IndexingMemoryBufferController extends AbstractComponent {

    private final ByteSizeValue indexingBuffer;

    private final ByteSizeValue minShardIndexBufferSize;

    private final IndicesService indicesService;

    private final Listener listener = new Listener();

    @Inject public IndexingMemoryBufferController(Settings settings, IndicesService indicesService) {
        super(settings);
        this.indicesService = indicesService;

        String indexingBuffer = componentSettings.get("index_buffer_size", "40%");
        if (indexingBuffer.endsWith("%")) {
            double percent = Double.parseDouble(indexingBuffer.substring(0, indexingBuffer.length() - 1));
            this.indexingBuffer = new ByteSizeValue((long) (((double) JvmInfo.jvmInfo().mem().heapMax().bytes()) * (percent / 100)));
        } else {
            this.indexingBuffer = ByteSizeValue.parseBytesSizeValue(indexingBuffer, null);
        }
        this.minShardIndexBufferSize = componentSettings.getAsBytesSize("min_shard_index_buffer_size", new ByteSizeValue(4, ByteSizeUnit.MB));

        logger.debug("using index_buffer_size [{}], with min_shard_index_buffer_size [{}]", this.indexingBuffer, this.minShardIndexBufferSize);

        indicesService.indicesLifecycle().addListener(listener);
    }

    private class Listener extends IndicesLifecycle.Listener {

        @Override public void afterIndexShardCreated(IndexShard indexShard) {
            calcAndSetShardIndexingBuffer("created_shard[" + indexShard.shardId().index().name() + "][" + indexShard.shardId().id() + "]");
        }

        @Override public void afterIndexShardClosed(ShardId shardId, boolean delete) {
            calcAndSetShardIndexingBuffer("removed_shard[" + shardId.index().name() + "][" + shardId.id() + "]");
        }

        private void calcAndSetShardIndexingBuffer(String reason) {
            int shardsCount = countShards();
            if (shardsCount == 0) {
                return;
            }
            ByteSizeValue shardIndexingBufferSize = calcShardIndexingBuffer(shardsCount);
            if (shardIndexingBufferSize == null) {
                return;
            }
            if (shardIndexingBufferSize.bytes() < minShardIndexBufferSize.bytes()) {
                shardIndexingBufferSize = minShardIndexBufferSize;
            }
            logger.debug("recalculating shard indexing buffer (reason={}), total is [{}] with [{}] shards, each shard set to [{}]", reason, indexingBuffer, shardsCount, shardIndexingBufferSize);
            for (IndexService indexService : indicesService) {
                for (IndexShard indexShard : indexService) {
                    ((InternalIndexShard) indexShard).engine().indexingBuffer(shardIndexingBufferSize);
                }
            }
        }

        private ByteSizeValue calcShardIndexingBuffer(int shardsCount) {
            return new ByteSizeValue(indexingBuffer.bytes() / shardsCount);
        }

        private int countShards() {
            int shardsCount = 0;
            for (IndexService indexService : indicesService) {
                for (IndexShard indexShard : indexService) {
                    shardsCount++;
                }
            }
            return shardsCount;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5410.java