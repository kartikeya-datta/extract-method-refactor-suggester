error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9276.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9276.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9276.java
text:
```scala
T@@imeValue sync = componentSettings.getAsTime("sync", TimeValue.timeValueSeconds(10));

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

package org.elasticsearch.index.gateway.local;

import org.apache.lucene.index.IndexReader;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.stream.InputStreamStreamInput;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.gateway.IndexShardGateway;
import org.elasticsearch.index.gateway.IndexShardGatewayRecoveryException;
import org.elasticsearch.index.gateway.RecoveryStatus;
import org.elasticsearch.index.gateway.SnapshotStatus;
import org.elasticsearch.index.settings.IndexSettings;
import org.elasticsearch.index.shard.AbstractIndexShardComponent;
import org.elasticsearch.index.shard.IndexShardState;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.index.shard.service.IndexShard;
import org.elasticsearch.index.shard.service.InternalIndexShard;
import org.elasticsearch.index.translog.Translog;
import org.elasticsearch.index.translog.TranslogStreams;
import org.elasticsearch.index.translog.fs.FsTranslog;
import org.elasticsearch.threadpool.ThreadPool;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ScheduledFuture;

/**
 * @author kimchy (shay.banon)
 */
public class LocalIndexShardGateway extends AbstractIndexShardComponent implements IndexShardGateway {

    private final InternalIndexShard indexShard;

    private final RecoveryStatus recoveryStatus = new RecoveryStatus();

    private final ScheduledFuture flushScheduler;

    @Inject public LocalIndexShardGateway(ShardId shardId, @IndexSettings Settings indexSettings, ThreadPool threadPool, IndexShard indexShard) {
        super(shardId, indexSettings);
        this.indexShard = (InternalIndexShard) indexShard;

        TimeValue sync = componentSettings.getAsTime("sync", TimeValue.timeValueSeconds(1));
        if (sync.millis() > 0) {
            this.indexShard.translog().syncOnEachOperation(false);
            // we don't need to execute the sync on a different thread, just do it on the scheduler thread
            flushScheduler = threadPool.scheduleWithFixedDelay(new Sync(), sync);
        } else if (sync.millis() == 0) {
            flushScheduler = null;
            this.indexShard.translog().syncOnEachOperation(true);
        } else {
            flushScheduler = null;
        }
    }

    @Override public String toString() {
        return "local";
    }

    @Override public RecoveryStatus recoveryStatus() {
        return recoveryStatus;
    }

    @Override public void recover(RecoveryStatus recoveryStatus) throws IndexShardGatewayRecoveryException {
        recoveryStatus.index().startTime(System.currentTimeMillis());
        long version = -1;
        try {
            if (IndexReader.indexExists(indexShard.store().directory())) {
                version = IndexReader.getCurrentVersion(indexShard.store().directory());
            }
        } catch (IOException e) {
            throw new IndexShardGatewayRecoveryException(shardId(), "Failed to fetch index version after copying it over", e);
        }
        recoveryStatus.index().updateVersion(version);
        recoveryStatus.index().time(System.currentTimeMillis() - recoveryStatus.index().startTime());

        // since we recover from local, just fill the files and size
        try {
            int numberOfFiles = 0;
            long totalSizeInBytes = 0;
            for (String name : indexShard.store().directory().listAll()) {
                numberOfFiles++;
                totalSizeInBytes += indexShard.store().directory().fileLength(name);
            }
            recoveryStatus.index().files(numberOfFiles, totalSizeInBytes, numberOfFiles, totalSizeInBytes);
        } catch (Exception e) {
            // ignore
        }

        recoveryStatus.translog().startTime(System.currentTimeMillis());
        if (version == -1) {
            // no translog files, bail
            indexShard.start("post recovery from gateway, no translog");
            // no index, just start the shard and bail
            recoveryStatus.translog().time(System.currentTimeMillis() - recoveryStatus.index().startTime());
            return;
        }

        // move an existing translog, if exists, to "recovering" state, and start reading from it
        FsTranslog translog = (FsTranslog) indexShard.translog();
        File recoveringTranslogFile = new File(translog.location(), "translog-" + version + ".recovering");
        if (!recoveringTranslogFile.exists()) {
            File translogFile = new File(translog.location(), "translog-" + version);
            if (translogFile.exists()) {
                for (int i = 0; i < 3; i++) {
                    if (translogFile.renameTo(recoveringTranslogFile)) {
                        break;
                    }
                }
            }
        }

        if (!recoveringTranslogFile.exists()) {
            // no translog to recovery from, start and bail
            // no translog files, bail
            indexShard.start("post recovery from gateway, no translog");
            // no index, just start the shard and bail
            recoveryStatus.translog().time(System.currentTimeMillis() - recoveryStatus.index().startTime());
            return;
        }

        // recover from the translog file
        indexShard.performRecoveryPrepareForTranslog();
        try {
            InputStreamStreamInput si = new InputStreamStreamInput(new FileInputStream(recoveringTranslogFile));
            while (true) {
                Translog.Operation operation;
                try {
                    int opSize = si.readInt();
                    operation = TranslogStreams.readTranslogOperation(si);
                } catch (EOFException e) {
                    // ignore, not properly written the last op
                    break;
                } catch (IOException e) {
                    // ignore, not properly written last op
                    break;
                }
                recoveryStatus.translog().addTranslogOperations(1);
                indexShard.performRecoveryOperation(operation);
            }
        } catch (Throwable e) {
            // we failed to recovery, make sure to delete the translog file (and keep the recovering one)
            indexShard.translog().close(true);
            throw new IndexShardGatewayRecoveryException(shardId, "failed to recover shard", e);
        }
        indexShard.performRecoveryFinalization(true);

        recoveringTranslogFile.delete();

        recoveryStatus.translog().time(System.currentTimeMillis() - recoveryStatus.index().startTime());
    }

    @Override public String type() {
        return "local";
    }

    @Override public SnapshotStatus snapshot(Snapshot snapshot) {
        return null;
    }

    @Override public SnapshotStatus lastSnapshotStatus() {
        return null;
    }

    @Override public SnapshotStatus currentSnapshotStatus() {
        return null;
    }

    @Override public boolean requiresSnapshot() {
        return false;
    }

    @Override public boolean requiresSnapshotScheduling() {
        return false;
    }

    @Override public void close(boolean delete) {
        if (flushScheduler != null) {
            flushScheduler.cancel(false);
        }
    }

    @Override public SnapshotLock obtainSnapshotLock() throws Exception {
        return NO_SNAPSHOT_LOCK;
    }

    private class Sync implements Runnable {
        @Override public void run() {
            if (indexShard.state() == IndexShardState.STARTED) {
                indexShard.translog().sync();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9276.java