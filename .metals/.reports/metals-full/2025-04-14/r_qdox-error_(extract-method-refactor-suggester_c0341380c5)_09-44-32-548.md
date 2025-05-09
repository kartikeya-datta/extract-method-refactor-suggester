error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9690.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9690.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9690.java
text:
```scala
t@@his.maxBytesPerSec = componentSettings.getAsBytesSize("max_bytes_per_sec", componentSettings.getAsBytesSize("max_size_per_sec", new ByteSizeValue(50, ByteSizeUnit.MB)));

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

package org.elasticsearch.indices.recovery;

import com.google.common.base.Objects;
import org.apache.lucene.store.RateLimiter;
import org.apache.lucene.store.RateLimiter.SimpleRateLimiter;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.util.concurrent.EsExecutors;
import org.elasticsearch.node.settings.NodeSettingsService;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 */
public class RecoverySettings extends AbstractComponent {

    public static final String INDICES_RECOVERY_FILE_CHUNK_SIZE = "indices.recovery.file_chunk_size";
    public static final String INDICES_RECOVERY_TRANSLOG_OPS = "indices.recovery.translog_ops";
    public static final String INDICES_RECOVERY_TRANSLOG_SIZE = "indices.recovery.translog_size";
    public static final String INDICES_RECOVERY_COMPRESS = "indices.recovery.compress";
    public static final String INDICES_RECOVERY_CONCURRENT_STREAMS = "indices.recovery.concurrent_streams";
    public static final String INDICES_RECOVERY_CONCURRENT_SMALL_FILE_STREAMS = "indices.recovery.concurrent_small_file_streams";
    public static final String INDICES_RECOVERY_MAX_BYTES_PER_SEC = "indices.recovery.max_bytes_per_sec";

    public static final long SMALL_FILE_CUTOFF_BYTES = ByteSizeValue.parseBytesSizeValue("5mb").bytes();

    /**
     * Use {@link #INDICES_RECOVERY_MAX_BYTES_PER_SEC} instead
     */
    @Deprecated
    public static final String INDICES_RECOVERY_MAX_SIZE_PER_SEC = "indices.recovery.max_size_per_sec";

    private volatile ByteSizeValue fileChunkSize;

    private volatile boolean compress;
    private volatile int translogOps;
    private volatile ByteSizeValue translogSize;

    private volatile int concurrentStreams;
    private volatile int concurrentSmallFileStreams;
    private final ThreadPoolExecutor concurrentStreamPool;
    private final ThreadPoolExecutor concurrentSmallFileStreamPool;

    private volatile ByteSizeValue maxBytesPerSec;
    private volatile SimpleRateLimiter rateLimiter;

    @Inject
    public RecoverySettings(Settings settings, NodeSettingsService nodeSettingsService) {
        super(settings);

        this.fileChunkSize = componentSettings.getAsBytesSize("file_chunk_size", settings.getAsBytesSize("index.shard.recovery.file_chunk_size", new ByteSizeValue(512, ByteSizeUnit.KB)));
        this.translogOps = componentSettings.getAsInt("translog_ops", settings.getAsInt("index.shard.recovery.translog_ops", 1000));
        this.translogSize = componentSettings.getAsBytesSize("translog_size", settings.getAsBytesSize("index.shard.recovery.translog_size", new ByteSizeValue(512, ByteSizeUnit.KB)));
        this.compress = componentSettings.getAsBoolean("compress", true);

        this.concurrentStreams = componentSettings.getAsInt("concurrent_streams", settings.getAsInt("index.shard.recovery.concurrent_streams", 3));
        this.concurrentStreamPool = EsExecutors.newScaling(0, concurrentStreams, 60, TimeUnit.SECONDS, EsExecutors.daemonThreadFactory(settings, "[recovery_stream]"));
        this.concurrentSmallFileStreams = componentSettings.getAsInt("concurrent_small_file_streams", settings.getAsInt("index.shard.recovery.concurrent_small_file_streams", 2));
        this.concurrentSmallFileStreamPool = EsExecutors.newScaling(0, concurrentSmallFileStreams, 60, TimeUnit.SECONDS, EsExecutors.daemonThreadFactory(settings, "[small_file_recovery_stream]"));

        this.maxBytesPerSec = componentSettings.getAsBytesSize("max_bytes_per_sec", componentSettings.getAsBytesSize("max_size_per_sec", new ByteSizeValue(20, ByteSizeUnit.MB)));
        if (maxBytesPerSec.bytes() <= 0) {
            rateLimiter = null;
        } else {
            rateLimiter = new SimpleRateLimiter(maxBytesPerSec.mbFrac());
        }

        logger.debug("using max_bytes_per_sec[{}], concurrent_streams [{}], file_chunk_size [{}], translog_size [{}], translog_ops [{}], and compress [{}]",
                maxBytesPerSec, concurrentStreams, fileChunkSize, translogSize, translogOps, compress);

        nodeSettingsService.addListener(new ApplySettings());
    }

    public void close() {
        concurrentStreamPool.shutdown();
        try {
            concurrentStreamPool.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // that's fine...
        }
        concurrentStreamPool.shutdownNow();
    }

    public ByteSizeValue fileChunkSize() {
        return fileChunkSize;
    }

    public boolean compress() {
        return compress;
    }

    public int translogOps() {
        return translogOps;
    }

    public ByteSizeValue translogSize() {
        return translogSize;
    }

    public int concurrentStreams() {
        return concurrentStreams;
    }

    public ThreadPoolExecutor concurrentStreamPool() {
        return concurrentStreamPool;
    }

    public ThreadPoolExecutor concurrentSmallFileStreamPool() {
        return concurrentSmallFileStreamPool;
    }

    public RateLimiter rateLimiter() {
        return rateLimiter;
    }

    class ApplySettings implements NodeSettingsService.Listener {
        @Override
        public void onRefreshSettings(Settings settings) {
            ByteSizeValue maxSizePerSec = settings.getAsBytesSize(INDICES_RECOVERY_MAX_BYTES_PER_SEC, settings.getAsBytesSize(INDICES_RECOVERY_MAX_SIZE_PER_SEC, RecoverySettings.this.maxBytesPerSec));
            if (!Objects.equal(maxSizePerSec, RecoverySettings.this.maxBytesPerSec)) {
                logger.info("updating [{}] from [{}] to [{}]", INDICES_RECOVERY_MAX_BYTES_PER_SEC, RecoverySettings.this.maxBytesPerSec, maxSizePerSec);
                RecoverySettings.this.maxBytesPerSec = maxSizePerSec;
                if (maxSizePerSec.bytes() <= 0) {
                    rateLimiter = null;
                } else if (rateLimiter != null) {
                    rateLimiter.setMbPerSec(maxSizePerSec.mbFrac());
                } else {
                    rateLimiter = new SimpleRateLimiter(maxSizePerSec.mbFrac());
                }
            }

            ByteSizeValue fileChunkSize = settings.getAsBytesSize(INDICES_RECOVERY_FILE_CHUNK_SIZE, RecoverySettings.this.fileChunkSize);
            if (!fileChunkSize.equals(RecoverySettings.this.fileChunkSize)) {
                logger.info("updating [indices.recovery.file_chunk_size] from [{}] to [{}]", RecoverySettings.this.fileChunkSize, fileChunkSize);
                RecoverySettings.this.fileChunkSize = fileChunkSize;
            }

            int translogOps = settings.getAsInt(INDICES_RECOVERY_TRANSLOG_OPS, RecoverySettings.this.translogOps);
            if (translogOps != RecoverySettings.this.translogOps) {
                logger.info("updating [indices.recovery.translog_ops] from [{}] to [{}]", RecoverySettings.this.translogOps, translogOps);
                RecoverySettings.this.translogOps = translogOps;
            }

            ByteSizeValue translogSize = settings.getAsBytesSize(INDICES_RECOVERY_TRANSLOG_SIZE, RecoverySettings.this.translogSize);
            if (!translogSize.equals(RecoverySettings.this.translogSize)) {
                logger.info("updating [indices.recovery.translog_size] from [{}] to [{}]", RecoverySettings.this.translogSize, translogSize);
                RecoverySettings.this.translogSize = translogSize;
            }

            boolean compress = settings.getAsBoolean(INDICES_RECOVERY_COMPRESS, RecoverySettings.this.compress);
            if (compress != RecoverySettings.this.compress) {
                logger.info("updating [indices.recovery.compress] from [{}] to [{}]", RecoverySettings.this.compress, compress);
                RecoverySettings.this.compress = compress;
            }

            int concurrentStreams = settings.getAsInt(INDICES_RECOVERY_CONCURRENT_STREAMS, RecoverySettings.this.concurrentStreams);
            if (concurrentStreams != RecoverySettings.this.concurrentStreams) {
                logger.info("updating [indices.recovery.concurrent_streams] from [{}] to [{}]", RecoverySettings.this.concurrentStreams, concurrentStreams);
                RecoverySettings.this.concurrentStreams = concurrentStreams;
                RecoverySettings.this.concurrentStreamPool.setMaximumPoolSize(concurrentStreams);
            }

            int concurrentSmallFileStreams = settings.getAsInt(INDICES_RECOVERY_CONCURRENT_SMALL_FILE_STREAMS, RecoverySettings.this.concurrentSmallFileStreams);
            if (concurrentSmallFileStreams != RecoverySettings.this.concurrentSmallFileStreams) {
                logger.info("updating [indices.recovery.concurrent_small_file_streams] from [{}] to [{}]", RecoverySettings.this.concurrentSmallFileStreams, concurrentSmallFileStreams);
                RecoverySettings.this.concurrentSmallFileStreams = concurrentSmallFileStreams;
                RecoverySettings.this.concurrentSmallFileStreamPool.setMaximumPoolSize(concurrentSmallFileStreams);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9690.java