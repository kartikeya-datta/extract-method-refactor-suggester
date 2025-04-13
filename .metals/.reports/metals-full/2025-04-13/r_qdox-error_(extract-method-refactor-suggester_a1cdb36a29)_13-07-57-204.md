error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5313.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5313.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5313.java
text:
```scala
l@@ogger.warn("[{}] New used memory {} [{}] from field [{}] would be larger than configured breaker: {} [{}], breaking",

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

package org.elasticsearch.common.breaker;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.indices.breaker.BreakerSettings;
import org.elasticsearch.indices.breaker.HierarchyCircuitBreakerService;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Breaker that will check a parent's when incrementing
 */
public class ChildMemoryCircuitBreaker implements CircuitBreaker {

    private final long memoryBytesLimit;
    private final BreakerSettings settings;
    private final double overheadConstant;
    private final AtomicLong used;
    private final AtomicLong trippedCount;
    private final ESLogger logger;
    private final HierarchyCircuitBreakerService parent;
    private final Name name;

    /**
     * Create a circuit breaker that will break if the number of estimated
     * bytes grows above the limit. All estimations will be multiplied by
     * the given overheadConstant. This breaker starts with 0 bytes used.
     * @param settings settings to configure this breaker
     * @param parent parent circuit breaker service to delegate tripped breakers to
     * @param name the name of the breaker
     */
    public ChildMemoryCircuitBreaker(BreakerSettings settings, ESLogger logger,
                                     HierarchyCircuitBreakerService parent, Name name) {
        this(settings, null, logger, parent, name);
    }

    /**
     * Create a circuit breaker that will break if the number of estimated
     * bytes grows above the limit. All estimations will be multiplied by
     * the given overheadConstant. Uses the given oldBreaker to initialize
     * the starting offset.
     * @param settings settings to configure this breaker
     * @param parent parent circuit breaker service to delegate tripped breakers to
     * @param name the name of the breaker
     * @param oldBreaker the previous circuit breaker to inherit the used value from (starting offset)
     */
    public ChildMemoryCircuitBreaker(BreakerSettings settings, ChildMemoryCircuitBreaker oldBreaker,
                                     ESLogger logger, HierarchyCircuitBreakerService parent, Name name) {
        this.name = name;
        this.settings = settings;
        this.memoryBytesLimit = settings.getLimit();
        this.overheadConstant = settings.getOverhead();
        if (oldBreaker == null) {
            this.used = new AtomicLong(0);
            this.trippedCount = new AtomicLong(0);
        } else {
            this.used = oldBreaker.used;
            this.trippedCount = oldBreaker.trippedCount;
        }
        this.logger = logger;
        if (logger.isTraceEnabled()) {
            logger.trace("creating ChildCircuitBreaker with settings {}", this.settings);
        }
        this.parent = parent;
    }

    /**
     * Method used to trip the breaker, delegates to the parent to determine
     * whether to trip the breaker or not
     */
    @Override
    public void circuitBreak(String fieldName, long bytesNeeded) {
        this.trippedCount.incrementAndGet();
        throw new CircuitBreakingException("[" + this.name + "] Data too large, data for [" +
                fieldName + "] would be larger than limit of [" +
                memoryBytesLimit + "/" + new ByteSizeValue(memoryBytesLimit) + "]",
                bytesNeeded, this.memoryBytesLimit);
    }

    /**
     * Add a number of bytes, tripping the circuit breaker if the aggregated
     * estimates are above the limit. Automatically trips the breaker if the
     * memory limit is set to 0. Will never trip the breaker if the limit is
     * set < 0, but can still be used to aggregate estimations.
     * @param bytes number of bytes to add to the breaker
     * @return number of "used" bytes so far
     * @throws CircuitBreakingException
     */
    @Override
    public double addEstimateBytesAndMaybeBreak(long bytes, String label) throws CircuitBreakingException {
        // short-circuit on no data allowed, immediately throwing an exception
        if (memoryBytesLimit == 0) {
            circuitBreak(label, bytes);
        }

        long newUsed;
        // If there is no limit (-1), we can optimize a bit by using
        // .addAndGet() instead of looping (because we don't have to check a
        // limit), which makes the RamAccountingTermsEnum case faster.
        if (this.memoryBytesLimit == -1) {
            newUsed = this.used.addAndGet(bytes);
            if (logger.isTraceEnabled()) {
                logger.trace("[{}] Adding [{}][{}] to used bytes [new used: [{}], limit: [-1b]]",
                        this.name, new ByteSizeValue(bytes), label, new ByteSizeValue(newUsed));
            }
        } else {
            // Otherwise, check the addition and commit the addition, looping if
            // there are conflicts. May result in additional logging, but it's
            // trace logging and shouldn't be counted on for additions.
            long currentUsed;
            do {
                currentUsed = this.used.get();
                newUsed = currentUsed + bytes;
                long newUsedWithOverhead = (long) (newUsed * overheadConstant);
                if (logger.isTraceEnabled()) {
                    logger.trace("[{}] Adding [{}][{}] to used bytes [new used: [{}], limit: {} [{}], estimate: {} [{}]]",
                            this.name,
                            new ByteSizeValue(bytes), label, new ByteSizeValue(newUsed),
                            memoryBytesLimit, new ByteSizeValue(memoryBytesLimit),
                            newUsedWithOverhead, new ByteSizeValue(newUsedWithOverhead));
                }
                if (memoryBytesLimit > 0 && newUsedWithOverhead > memoryBytesLimit) {
                    logger.error("[{}] New used memory {} [{}] from field [{}] would be larger than configured breaker: {} [{}], breaking",
                            this.name,
                            newUsedWithOverhead, new ByteSizeValue(newUsedWithOverhead), label,
                            memoryBytesLimit, new ByteSizeValue(memoryBytesLimit));
                    circuitBreak(label, newUsedWithOverhead);
                }
                // Attempt to set the new used value, but make sure it hasn't changed
                // underneath us, if it has, keep trying until we are able to set it
            } while (!this.used.compareAndSet(currentUsed, newUsed));
        }

        // Additionally, we need to check that we haven't exceeded the parent's limit
        try {
            parent.checkParentLimit(label);
        } catch (CircuitBreakingException e) {
            // If the parent breaker is tripped, this breaker has to be
            // adjusted back down because the allocation is "blocked" but the
            // breaker has already been incremented
            this.used.addAndGet(-bytes);
            throw e;
        }
        return newUsed;
    }

    /**
     * Add an <b>exact</b> number of bytes, not checking for tripping the
     * circuit breaker. This bypasses the overheadConstant multiplication.
     *
     * Also does not check with the parent breaker to see if the parent limit
     * has been exceeded.
     *
     * @param bytes number of bytes to add to the breaker
     * @return number of "used" bytes so far
     */
    @Override
    public long addWithoutBreaking(long bytes) {
        long u = used.addAndGet(bytes);
        if (logger.isTraceEnabled()) {
            logger.trace("[{}] Adjusted breaker by [{}] bytes, now [{}]", this.name, bytes, u);
        }
        assert u >= 0 : "Used bytes: [" + u + "] must be >= 0";
        return u;
    }

    /**
     * @return the number of aggregated "used" bytes so far
     */
    @Override
    public long getUsed() {
        return this.used.get();
    }

    /**
     * @return the number of bytes that can be added before the breaker trips
     */
    @Override
    public long getLimit() {
        return this.memoryBytesLimit;
    }

    /**
     * @return the constant multiplier the breaker uses for aggregations
     */
    @Override
    public double getOverhead() {
        return this.overheadConstant;
    }

    /**
     * @return the number of times the breaker has been tripped
     */
    @Override
    public long getTrippedCount() {
        return this.trippedCount.get();
    }

    /**
     * @return the name of the breaker
     */
    public Name getName() {
        return this.name;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5313.java