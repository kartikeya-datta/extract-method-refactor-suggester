error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8232.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8232.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8232.java
text:
```scala
synchronized public v@@oid merge(IndexWriter writer, MergeTrigger trigger, boolean newMergesFound) throws CorruptIndexException, IOException {

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

package org.apache.lucene.index;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.metrics.CounterMetric;
import org.elasticsearch.common.metrics.MeanMetric;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.util.concurrent.ConcurrentCollections;
import org.elasticsearch.index.merge.OnGoingMerge;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

// LUCENE MONITOR - Copied from SerialMergeScheduler
public class TrackingSerialMergeScheduler extends MergeScheduler {

    protected final ESLogger logger;

    private final MeanMetric totalMerges = new MeanMetric();
    private final CounterMetric totalMergesNumDocs = new CounterMetric();
    private final CounterMetric totalMergesSizeInBytes = new CounterMetric();
    private final CounterMetric currentMerges = new CounterMetric();
    private final CounterMetric currentMergesNumDocs = new CounterMetric();
    private final CounterMetric currentMergesSizeInBytes = new CounterMetric();

    private final Set<OnGoingMerge> onGoingMerges = ConcurrentCollections.newConcurrentSet();
    private final Set<OnGoingMerge> readOnlyOnGoingMerges = Collections.unmodifiableSet(onGoingMerges);

    private final int maxMergeAtOnce;

    public TrackingSerialMergeScheduler(ESLogger logger, int maxMergeAtOnce) {
        this.logger = logger;
        this.maxMergeAtOnce = maxMergeAtOnce;
    }

    public long totalMerges() {
        return totalMerges.count();
    }

    public long totalMergeTime() {
        return totalMerges.sum();
    }

    public long totalMergeNumDocs() {
        return totalMergesNumDocs.count();
    }

    public long totalMergeSizeInBytes() {
        return totalMergesSizeInBytes.count();
    }

    public long currentMerges() {
        return currentMerges.count();
    }

    public long currentMergesNumDocs() {
        return currentMergesNumDocs.count();
    }

    public long currentMergesSizeInBytes() {
        return currentMergesSizeInBytes.count();
    }

    public Set<OnGoingMerge> onGoingMerges() {
        return readOnlyOnGoingMerges;
    }

    /**
     * Just do the merges in sequence. We do this
     * "synchronized" so that even if the application is using
     * multiple threads, only one merge may run at a time.
     */
    @Override
    synchronized public void merge(IndexWriter writer) throws CorruptIndexException, IOException {
        int cycle = 0;
        while (cycle++ < maxMergeAtOnce) {
            MergePolicy.OneMerge merge = writer.getNextMerge();
            if (merge == null)
                break;

            // different from serial merge, call mergeInit here so we get the correct stats
            // mergeInit can be called several times without side affects (checks on merge.info not being null)
            writer.mergeInit(merge);

            int totalNumDocs = merge.totalNumDocs();
            // don't used #totalBytesSize() since need to be executed under IW lock, might be fixed in future Lucene version
            long totalSizeInBytes = merge.estimatedMergeBytes;
            long time = System.currentTimeMillis();
            currentMerges.inc();
            currentMergesNumDocs.inc(totalNumDocs);
            currentMergesSizeInBytes.inc(totalSizeInBytes);

            OnGoingMerge onGoingMerge = new OnGoingMerge(merge);
            onGoingMerges.add(onGoingMerge);

            // sadly, segment name is not available since mergeInit is called from merge itself...
            if (logger.isTraceEnabled()) {
                logger.trace("merge [{}] starting..., merging [{}] segments, [{}] docs, [{}] size, into [{}] estimated_size", merge.info == null ? "_na_" : merge.info.info.name, merge.segments.size(), totalNumDocs, new ByteSizeValue(totalSizeInBytes), new ByteSizeValue(merge.estimatedMergeBytes));
            }
            try {
                beforeMerge(onGoingMerge);
                writer.merge(merge);
            } finally {
                long took = System.currentTimeMillis() - time;

                onGoingMerges.remove(onGoingMerge);
                afterMerge(onGoingMerge);

                currentMerges.dec();
                currentMergesNumDocs.dec(totalNumDocs);
                currentMergesSizeInBytes.dec(totalSizeInBytes);

                totalMergesNumDocs.inc(totalNumDocs);
                totalMergesSizeInBytes.inc(totalSizeInBytes);
                totalMerges.inc(took);
                if (took > 20000) { // if more than 20 seconds, DEBUG log it
                    logger.debug("merge [{}] done, took [{}]", merge.info == null ? "_na_" : merge.info.info.name, TimeValue.timeValueMillis(took));
                } else if (logger.isTraceEnabled()) {
                    logger.trace("merge [{}] done, took [{}]", merge.info == null ? "_na_" : merge.info.info.name, TimeValue.timeValueMillis(took));
                }
            }
        }
    }

    /**
     * A callback allowing for custom logic before an actual merge starts.
     */
    protected void beforeMerge(OnGoingMerge merge) {

    }

    /**
     * A callback allowing for custom logic before an actual merge starts.
     */
    protected void afterMerge(OnGoingMerge merge) {

    }

    @Override
    public void close() {
    }

    @Override
    public MergeScheduler clone() {
        // Lucene IW makes a clone internally but since we hold on to this instance 
        // the clone will just be the identity.
        return this;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8232.java