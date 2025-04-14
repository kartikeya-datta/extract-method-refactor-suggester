error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3553.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3553.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3553.java
text:
```scala
l@@ogger.debug("failed to monitor", t);

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

package org.elasticsearch.monitor.jvm;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.common.collect.MapBuilder;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.monitor.dump.DumpGenerator;
import org.elasticsearch.monitor.dump.DumpMonitorService;
import org.elasticsearch.threadpool.ThreadPool;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

import static org.elasticsearch.common.unit.TimeValue.timeValueSeconds;
import static org.elasticsearch.monitor.dump.summary.SummaryDumpContributor.SUMMARY;
import static org.elasticsearch.monitor.dump.thread.ThreadDumpContributor.THREAD_DUMP;
import static org.elasticsearch.monitor.jvm.DeadlockAnalyzer.deadlockAnalyzer;
import static org.elasticsearch.monitor.jvm.JvmStats.GarbageCollector;
import static org.elasticsearch.monitor.jvm.JvmStats.jvmStats;

/**
 *
 */
public class JvmMonitorService extends AbstractLifecycleComponent<JvmMonitorService> {

    private final ThreadPool threadPool;

    private final DumpMonitorService dumpMonitorService;

    private final boolean enabled;

    private final TimeValue interval;

    private final ImmutableMap<String, GcThreshold> gcThresholds;

    private volatile ScheduledFuture scheduledFuture;

    static class GcThreshold {
        public final String name;
        public final long warnThreshold;
        public final long infoThreshold;
        public final long debugThreshold;

        GcThreshold(String name, long warnThreshold, long infoThreshold, long debugThreshold) {
            this.name = name;
            this.warnThreshold = warnThreshold;
            this.infoThreshold = infoThreshold;
            this.debugThreshold = debugThreshold;
        }

        @Override
        public String toString() {
            return "GcThreshold{" +
                    "name='" + name + '\'' +
                    ", warnThreshold=" + warnThreshold +
                    ", infoThreshold=" + infoThreshold +
                    ", debugThreshold=" + debugThreshold +
                    '}';
        }
    }

    @Inject
    public JvmMonitorService(Settings settings, ThreadPool threadPool, DumpMonitorService dumpMonitorService) {
        super(settings);
        this.threadPool = threadPool;
        this.dumpMonitorService = dumpMonitorService;

        this.enabled = componentSettings.getAsBoolean("enabled", true);
        this.interval = componentSettings.getAsTime("interval", timeValueSeconds(1));

        MapBuilder<String, GcThreshold> gcThresholds = MapBuilder.newMapBuilder();
        Map<String, Settings> gcThresholdGroups = componentSettings.getGroups("gc");
        for (Map.Entry<String, Settings> entry : gcThresholdGroups.entrySet()) {
            String name = entry.getKey();
            TimeValue warn = entry.getValue().getAsTime("warn", null);
            TimeValue info = entry.getValue().getAsTime("info", null);
            TimeValue debug = entry.getValue().getAsTime("debug", null);
            if (warn == null || info == null || debug == null) {
                logger.warn("ignoring gc_threshold for [{}], missing warn/info/debug values", name);
            } else {
                gcThresholds.put(name, new GcThreshold(name, warn.millis(), info.millis(), debug.millis()));
            }
        }
        if (!gcThresholds.containsKey(GcNames.YOUNG)) {
            gcThresholds.put(GcNames.YOUNG, new GcThreshold(GcNames.YOUNG, 1000, 700, 400));
        }
        if (!gcThresholds.containsKey(GcNames.OLD)) {
            gcThresholds.put(GcNames.OLD, new GcThreshold(GcNames.OLD, 10000, 5000, 2000));
        }
        if (!gcThresholds.containsKey("default")) {
            gcThresholds.put("default", new GcThreshold("default", 10000, 5000, 2000));
        }

        this.gcThresholds = gcThresholds.immutableMap();

        logger.debug("enabled [{}], last_gc_enabled [{}], interval [{}], gc_threshold [{}]", enabled, JvmStats.isLastGcEnabled(), interval, this.gcThresholds);
    }

    @Override
    protected void doStart() throws ElasticsearchException {
        if (!enabled) {
            return;
        }
        scheduledFuture = threadPool.scheduleWithFixedDelay(new JvmMonitor(), interval);
    }

    @Override
    protected void doStop() throws ElasticsearchException {
        if (!enabled) {
            return;
        }
        scheduledFuture.cancel(true);
    }

    @Override
    protected void doClose() throws ElasticsearchException {
    }

    private class JvmMonitor implements Runnable {

        private JvmStats lastJvmStats = jvmStats();

        private long seq = 0;

        private final Set<DeadlockAnalyzer.Deadlock> lastSeenDeadlocks = new HashSet<DeadlockAnalyzer.Deadlock>();

        public JvmMonitor() {
        }

        @Override
        public void run() {
            try {
//            monitorDeadlock();
                monitorLongGc();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        private synchronized void monitorLongGc() {
            seq++;
            JvmStats currentJvmStats = jvmStats();

            for (int i = 0; i < currentJvmStats.gc().collectors().length; i++) {
                GarbageCollector gc = currentJvmStats.gc().collectors()[i];
                GarbageCollector prevGc = lastJvmStats.gc.collectors[i];

                // no collection has happened
                long collections = gc.collectionCount - prevGc.collectionCount;
                if (collections == 0) {
                    continue;
                }
                long collectionTime = gc.collectionTime - prevGc.collectionTime;
                if (collectionTime == 0) {
                    continue;
                }

                GcThreshold gcThreshold = gcThresholds.get(gc.name());
                if (gcThreshold == null) {
                    gcThreshold = gcThresholds.get("default");
                }

                if (gc.lastGc() != null && prevGc.lastGc() != null) {
                    GarbageCollector.LastGc lastGc = gc.lastGc();
                    if (lastGc.startTime == prevGc.lastGc().startTime()) {
                        // we already handled this one...
                        continue;
                    }
                    // Ignore any duration > 1hr; getLastGcInfo occasionally returns total crap
                    if (lastGc.duration().hoursFrac() > 1) {
                        continue;
                    }
                    if (lastGc.duration().millis() > gcThreshold.warnThreshold) {
                        logger.warn("[last_gc][{}][{}][{}] duration [{}], collections [{}], total [{}]/[{}], reclaimed [{}], leaving [{}][{}]/[{}]",
                                gc.name(), seq, gc.getCollectionCount(), lastGc.duration(), collections, TimeValue.timeValueMillis(collectionTime), gc.collectionTime(), lastGc.reclaimed(), lastGc.afterUsed(), lastGc.max());
                    } else if (lastGc.duration().millis() > gcThreshold.infoThreshold) {
                        logger.info("[last_gc][{}][{}][{}] duration [{}], collections [{}], total [{}]/[{}], reclaimed [{}], leaving [{}]/[{}]",
                                gc.name(), seq, gc.getCollectionCount(), lastGc.duration(), collections, TimeValue.timeValueMillis(collectionTime), gc.collectionTime(), lastGc.reclaimed(), lastGc.afterUsed(), lastGc.max());
                    } else if (lastGc.duration().millis() > gcThreshold.debugThreshold && logger.isDebugEnabled()) {
                        logger.debug("[last_gc][{}][{}][{}] duration [{}], collections [{}], total [{}]/[{}], reclaimed [{}], leaving [{}]/[{}]",
                                gc.name(), seq, gc.getCollectionCount(), lastGc.duration(), collections, TimeValue.timeValueMillis(collectionTime), gc.collectionTime(), lastGc.reclaimed(), lastGc.afterUsed(), lastGc.max());
                    }
                }

                long avgCollectionTime = collectionTime / collections;

                if (avgCollectionTime > gcThreshold.warnThreshold) {
                    logger.warn("[gc][{}][{}][{}] duration [{}], collections [{}]/[{}], total [{}]/[{}], memory [{}]->[{}]/[{}], all_pools {}",
                            gc.name(), seq, gc.collectionCount(), TimeValue.timeValueMillis(collectionTime), collections, TimeValue.timeValueMillis(currentJvmStats.timestamp() - lastJvmStats.timestamp()), TimeValue.timeValueMillis(collectionTime), gc.collectionTime(), lastJvmStats.mem().heapUsed(), currentJvmStats.mem().heapUsed(), JvmInfo.jvmInfo().mem().heapMax(), buildPools(lastJvmStats, currentJvmStats));
                } else if (avgCollectionTime > gcThreshold.infoThreshold) {
                    logger.info("[gc][{}][{}][{}] duration [{}], collections [{}]/[{}], total [{}]/[{}], memory [{}]->[{}]/[{}], all_pools {}",
                            gc.name(), seq, gc.collectionCount(), TimeValue.timeValueMillis(collectionTime), collections, TimeValue.timeValueMillis(currentJvmStats.timestamp() - lastJvmStats.timestamp()), TimeValue.timeValueMillis(collectionTime), gc.collectionTime(), lastJvmStats.mem().heapUsed(), currentJvmStats.mem().heapUsed(), JvmInfo.jvmInfo().mem().heapMax(), buildPools(lastJvmStats, currentJvmStats));
                } else if (avgCollectionTime > gcThreshold.debugThreshold && logger.isDebugEnabled()) {
                    logger.debug("[gc][{}][{}][{}] duration [{}], collections [{}]/[{}], total [{}]/[{}], memory [{}]->[{}]/[{}], all_pools {}",
                            gc.name(), seq, gc.collectionCount(), TimeValue.timeValueMillis(collectionTime), collections, TimeValue.timeValueMillis(currentJvmStats.timestamp() - lastJvmStats.timestamp()), TimeValue.timeValueMillis(collectionTime), gc.collectionTime(), lastJvmStats.mem().heapUsed(), currentJvmStats.mem().heapUsed(), JvmInfo.jvmInfo().mem().heapMax(), buildPools(lastJvmStats, currentJvmStats));
                }
            }
            lastJvmStats = currentJvmStats;
        }

        private String buildPools(JvmStats prev, JvmStats current) {
            StringBuilder sb = new StringBuilder();
            for (JvmStats.MemoryPool currentPool : current.mem()) {
                JvmStats.MemoryPool prevPool = null;
                for (JvmStats.MemoryPool pool : prev.mem()) {
                    if (pool.getName().equals(currentPool.getName())) {
                        prevPool = pool;
                        break;
                    }
                }
                sb.append("{[").append(currentPool.name())
                        .append("] [").append(prevPool == null ? "?" : prevPool.used()).append("]->[").append(currentPool.used()).append("]/[").append(currentPool.getMax()).append("]}");
            }
            return sb.toString();
        }

        private void monitorDeadlock() {
            DeadlockAnalyzer.Deadlock[] deadlocks = deadlockAnalyzer().findDeadlocks();
            if (deadlocks != null && deadlocks.length > 0) {
                ImmutableSet<DeadlockAnalyzer.Deadlock> asSet = new ImmutableSet.Builder<DeadlockAnalyzer.Deadlock>().add(deadlocks).build();
                if (!asSet.equals(lastSeenDeadlocks)) {
                    DumpGenerator.Result genResult = dumpMonitorService.generateDump("deadlock", null, SUMMARY, THREAD_DUMP);
                    StringBuilder sb = new StringBuilder("Detected Deadlock(s)");
                    for (DeadlockAnalyzer.Deadlock deadlock : asSet) {
                        sb.append("\n   ----> ").append(deadlock);
                    }
                    sb.append("\nDump generated [").append(genResult.location()).append("]");
                    logger.error(sb.toString());
                    lastSeenDeadlocks.clear();
                    lastSeenDeadlocks.addAll(asSet);
                }
            } else {
                lastSeenDeadlocks.clear();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3553.java