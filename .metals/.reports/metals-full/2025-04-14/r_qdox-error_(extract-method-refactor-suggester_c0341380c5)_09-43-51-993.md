error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2505.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2505.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2505.java
text:
```scala
s@@napshotIndexCommit.close();

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

package org.elasticsearch.index.snapshots;

import org.elasticsearch.cluster.metadata.SnapshotId;
import org.elasticsearch.cluster.routing.RestoreSource;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.deletionpolicy.SnapshotIndexCommit;
import org.elasticsearch.index.engine.SnapshotFailedEngineException;
import org.elasticsearch.indices.recovery.RecoveryState;
import org.elasticsearch.index.settings.IndexSettings;
import org.elasticsearch.index.shard.AbstractIndexShardComponent;
import org.elasticsearch.index.shard.IndexShardState;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.index.shard.service.IndexShard;
import org.elasticsearch.index.shard.service.InternalIndexShard;
import org.elasticsearch.repositories.RepositoriesService;
import org.elasticsearch.snapshots.RestoreService;


/**
 * Shard level snapshot and restore service
 * <p/>
 * Performs snapshot and restore operations on the shard level.
 */
public class IndexShardSnapshotAndRestoreService extends AbstractIndexShardComponent {

    private final InternalIndexShard indexShard;

    private final RepositoriesService repositoriesService;

    private final RestoreService restoreService;

    @Inject
    public IndexShardSnapshotAndRestoreService(ShardId shardId, @IndexSettings Settings indexSettings, IndexShard indexShard, RepositoriesService repositoriesService, RestoreService restoreService) {
        super(shardId, indexSettings);
        this.indexShard = (InternalIndexShard) indexShard;
        this.repositoriesService = repositoriesService;
        this.restoreService = restoreService;
    }

    /**
     * Creates shard snapshot
     *
     * @param snapshotId     snapshot id
     * @param snapshotStatus snapshot status
     */
    public void snapshot(final SnapshotId snapshotId, final IndexShardSnapshotStatus snapshotStatus) {
        IndexShardRepository indexShardRepository = repositoriesService.indexShardRepository(snapshotId.getRepository());
        if (!indexShard.routingEntry().primary()) {
            throw new IndexShardSnapshotFailedException(shardId, "snapshot should be performed only on primary");
        }
        if (indexShard.routingEntry().relocating()) {
            // do not snapshot when in the process of relocation of primaries so we won't get conflicts
            throw new IndexShardSnapshotFailedException(shardId, "cannot snapshot while relocating");
        }
        if (indexShard.state() == IndexShardState.CREATED || indexShard.state() == IndexShardState.RECOVERING) {
            // shard has just been created, or still recovering
            throw new IndexShardSnapshotFailedException(shardId, "shard didn't fully recover yet");
        }

        try {
            SnapshotIndexCommit snapshotIndexCommit = indexShard.snapshotIndex();
            try {
                indexShardRepository.snapshot(snapshotId, shardId, snapshotIndexCommit, snapshotStatus);
                if (logger.isDebugEnabled()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("snapshot (").append(snapshotId.getSnapshot()).append(") completed to ").append(indexShardRepository).append(", took [").append(TimeValue.timeValueMillis(snapshotStatus.time())).append("]\n");
                    sb.append("    index    : version [").append(snapshotStatus.indexVersion()).append("], number_of_files [").append(snapshotStatus.numberOfFiles()).append("] with total_size [").append(new ByteSizeValue(snapshotStatus.totalSize())).append("]\n");
                    logger.debug(sb.toString());
                }
            } finally {
                snapshotIndexCommit.release();
            }
        } catch (SnapshotFailedEngineException e) {
            throw e;
        } catch (IndexShardSnapshotFailedException e) {
            throw e;
        } catch (Throwable e) {
            throw new IndexShardSnapshotFailedException(shardId, "Failed to snapshot", e);
        }
    }

    /**
     * Restores shard from {@link RestoreSource} associated with this shard in routing table
     *
     * @param recoveryState recovery state
     */
    public void restore(final RecoveryState recoveryState) {
        RestoreSource restoreSource = indexShard.routingEntry().restoreSource();
        if (restoreSource == null) {
            throw new IndexShardRestoreFailedException(shardId, "empty restore source");
        }
        if (logger.isTraceEnabled()) {
            logger.trace("[{}] restoring shard  [{}]", restoreSource.snapshotId(), shardId);
        }
        try {
            IndexShardRepository indexShardRepository = repositoriesService.indexShardRepository(restoreSource.snapshotId().getRepository());
            ShardId snapshotShardId = shardId;
            if (!shardId.getIndex().equals(restoreSource.index())) {
                snapshotShardId = new ShardId(restoreSource.index(), shardId.id());
            }
            indexShardRepository.restore(restoreSource.snapshotId(), shardId, snapshotShardId, recoveryState);
            restoreService.indexShardRestoreCompleted(restoreSource.snapshotId(), shardId);
        } catch (Throwable t) {
            throw new IndexShardRestoreFailedException(shardId, "restore failed", t);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2505.java