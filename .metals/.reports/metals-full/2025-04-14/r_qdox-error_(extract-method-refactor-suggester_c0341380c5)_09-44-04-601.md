error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8089.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8089.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8089.java
text:
```scala
public v@@oid afterIndexShardClosed(ShardId shardId) {

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

package org.elasticsearch.indices.memory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.engine.EngineClosedException;
import org.elasticsearch.index.engine.FlushNotAllowedEngineException;
import org.elasticsearch.index.service.IndexService;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.index.shard.service.IndexShard;
import org.elasticsearch.index.shard.service.InternalIndexShard;
import org.elasticsearch.index.translog.Translog;
import org.elasticsearch.indices.IndicesLifecycle;
import org.elasticsearch.indices.IndicesService;
import org.elasticsearch.monitor.jvm.JvmInfo;
import org.elasticsearch.threadpool.ThreadPool;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 *
 */
public class IndexingMemoryController extends AbstractLifecycleComponent<IndexingMemoryController> {

    private final ThreadPool threadPool;

    private final IndicesService indicesService;


    private final ByteSizeValue indexingBuffer;

    private final ByteSizeValue minShardIndexBufferSize;
    private final ByteSizeValue maxShardIndexBufferSize;

    private final TimeValue inactiveTime;
    private final TimeValue interval;

    private final Listener listener = new Listener();

    private final Map<ShardId, ShardIndexingStatus> shardsIndicesStatus = Maps.newHashMap();

    private volatile ScheduledFuture scheduler;

    private final Object mutex = new Object();

    @Inject
    public IndexingMemoryController(Settings settings, ThreadPool threadPool, IndicesService indicesService) {
        super(settings);
        this.threadPool = threadPool;
        this.indicesService = indicesService;

        ByteSizeValue indexingBuffer;
        String indexingBufferSetting = componentSettings.get("index_buffer_size", "10%");
        if (indexingBufferSetting.endsWith("%")) {
            double percent = Double.parseDouble(indexingBufferSetting.substring(0, indexingBufferSetting.length() - 1));
            indexingBuffer = new ByteSizeValue((long) (((double) JvmInfo.jvmInfo().mem().heapMax().bytes()) * (percent / 100)));
            ByteSizeValue minIndexingBuffer = componentSettings.getAsBytesSize("min_index_buffer_size", new ByteSizeValue(48, ByteSizeUnit.MB));
            ByteSizeValue maxIndexingBuffer = componentSettings.getAsBytesSize("max_index_buffer_size", null);

            if (indexingBuffer.bytes() < minIndexingBuffer.bytes()) {
                indexingBuffer = minIndexingBuffer;
            }
            if (maxIndexingBuffer != null && indexingBuffer.bytes() > maxIndexingBuffer.bytes()) {
                indexingBuffer = maxIndexingBuffer;
            }
        } else {
            indexingBuffer = ByteSizeValue.parseBytesSizeValue(indexingBufferSetting, null);
        }

        this.indexingBuffer = indexingBuffer;
        this.minShardIndexBufferSize = componentSettings.getAsBytesSize("min_shard_index_buffer_size", new ByteSizeValue(4, ByteSizeUnit.MB));
        // LUCENE MONITOR: Based on this thread, currently (based on Mike), having a large buffer does not make a lot of sense: https://issues.apache.org/jira/browse/LUCENE-2324?focusedCommentId=13005155&page=com.atlassian.jira.plugin.system.issuetabpanels:comment-tabpanel#comment-13005155
        this.maxShardIndexBufferSize = componentSettings.getAsBytesSize("max_shard_index_buffer_size", new ByteSizeValue(512, ByteSizeUnit.MB));

        this.inactiveTime = componentSettings.getAsTime("shard_inactive_time", TimeValue.timeValueMinutes(30));
        // we need to have this relatively small to move a shard from inactive to active fast (enough)
        this.interval = componentSettings.getAsTime("interval", TimeValue.timeValueSeconds(30));

        logger.debug("using index_buffer_size [{}], with min_shard_index_buffer_size [{}], max_shard_index_buffer_size [{}], shard_inactive_time [{}]", this.indexingBuffer, this.minShardIndexBufferSize, this.maxShardIndexBufferSize, this.inactiveTime);

    }

    @Override
    protected void doStart() throws ElasticSearchException {
        indicesService.indicesLifecycle().addListener(listener);
        // its fine to run it on the scheduler thread, no busy work
        this.scheduler = threadPool.scheduleWithFixedDelay(new ShardsIndicesStatusChecker(), interval);
    }

    @Override
    protected void doStop() throws ElasticSearchException {
        indicesService.indicesLifecycle().removeListener(listener);
        if (scheduler != null) {
            scheduler.cancel(false);
            scheduler = null;
        }
    }

    @Override
    protected void doClose() throws ElasticSearchException {
    }

    class ShardsIndicesStatusChecker implements Runnable {
        @Override
        public void run() {
            synchronized (mutex) {
                boolean activeInactiveStatusChanges = false;
                List<IndexShard> activeToInactiveIndexingShards = Lists.newArrayList();
                List<IndexShard> inactiveToActiveIndexingShards = Lists.newArrayList();
                for (IndexService indexService : indicesService) {
                    for (IndexShard indexShard : indexService) {
                        long time = threadPool.estimatedTimeInMillis();
                        Translog translog = ((InternalIndexShard) indexShard).translog();
                        ShardIndexingStatus status = shardsIndicesStatus.get(indexShard.shardId());
                        if (status == null) { // not added yet
                            continue;
                        }
                        // check if it is deemed to be inactive (sam translogId and numberOfOperations over a long period of time)
                        if (status.translogId == translog.currentId() && translog.estimatedNumberOfOperations() == 0) {
                            if (status.time == -1) { // first time
                                status.time = time;
                            }
                            // inactive?
                            if (!status.inactiveIndexing) {
                                // mark it as inactive only if enough time has passed and there are no ongoing merges going on...
                                if ((time - status.time) > inactiveTime.millis() && indexShard.mergeStats().getCurrent() == 0) {
                                    // inactive for this amount of time, mark it
                                    activeToInactiveIndexingShards.add(indexShard);
                                    status.inactiveIndexing = true;
                                    activeInactiveStatusChanges = true;
                                    logger.debug("marking shard [{}][{}] as inactive (inactive_time[{}]) indexing wise, setting size to [{}]", indexShard.shardId().index().name(), indexShard.shardId().id(), inactiveTime, Engine.INACTIVE_SHARD_INDEXING_BUFFER);
                                }
                            }
                        } else {
                            if (status.inactiveIndexing) {
                                inactiveToActiveIndexingShards.add(indexShard);
                                status.inactiveIndexing = false;
                                activeInactiveStatusChanges = true;
                                logger.debug("marking shard [{}][{}] as active indexing wise", indexShard.shardId().index().name(), indexShard.shardId().id());
                            }
                            status.time = -1;
                        }
                        status.translogId = translog.currentId();
                        status.translogNumberOfOperations = translog.estimatedNumberOfOperations();
                    }
                }
                for (IndexShard indexShard : activeToInactiveIndexingShards) {
                    // update inactive indexing buffer size
                    try {
                        ((InternalIndexShard) indexShard).engine().updateIndexingBufferSize(Engine.INACTIVE_SHARD_INDEXING_BUFFER);
                    } catch (EngineClosedException e) {
                        // ignore
                    } catch (FlushNotAllowedEngineException e) {
                        // ignore
                    }
                }
                if (activeInactiveStatusChanges) {
                    calcAndSetShardIndexingBuffer("shards became active/inactive (indexing wise)");
                }
            }
        }
    }

    class Listener extends IndicesLifecycle.Listener {

        @Override
        public void afterIndexShardCreated(IndexShard indexShard) {
            synchronized (mutex) {
                calcAndSetShardIndexingBuffer("created_shard[" + indexShard.shardId().index().name() + "][" + indexShard.shardId().id() + "]");
                shardsIndicesStatus.put(indexShard.shardId(), new ShardIndexingStatus());
            }
        }

        @Override
        public void afterIndexShardClosed(ShardId shardId, boolean delete) {
            synchronized (mutex) {
                calcAndSetShardIndexingBuffer("removed_shard[" + shardId.index().name() + "][" + shardId.id() + "]");
                shardsIndicesStatus.remove(shardId);
            }
        }
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
        if (shardIndexingBufferSize.bytes() > maxShardIndexBufferSize.bytes()) {
            shardIndexingBufferSize = maxShardIndexBufferSize;
        }
        logger.debug("recalculating shard indexing buffer (reason={}), total is [{}] with [{}] active shards, each shard set to [{}]", reason, indexingBuffer, shardsCount, shardIndexingBufferSize);
        for (IndexService indexService : indicesService) {
            for (IndexShard indexShard : indexService) {
                ShardIndexingStatus status = shardsIndicesStatus.get(indexShard.shardId());
                if (status == null || !status.inactiveIndexing) {
                    try {
                        ((InternalIndexShard) indexShard).engine().updateIndexingBufferSize(shardIndexingBufferSize);
                    } catch (EngineClosedException e) {
                        // ignore
                        continue;
                    } catch (FlushNotAllowedEngineException e) {
                        // ignore
                        continue;
                    } catch (Exception e) {
                        logger.warn("failed to set shard [{}][{}] index buffer to [{}]", indexShard.shardId().index().name(), indexShard.shardId().id(), shardIndexingBufferSize);
                    }
                }
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
                ShardIndexingStatus status = shardsIndicesStatus.get(indexShard.shardId());
                if (status == null || !status.inactiveIndexing) {
                    shardsCount++;
                }
            }
        }
        return shardsCount;
    }

    static class ShardIndexingStatus {
        long translogId = -1;
        int translogNumberOfOperations = -1;
        boolean inactiveIndexing = false;
        long time = -1; // contains the first time we saw this shard with no operations done on it
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8089.java