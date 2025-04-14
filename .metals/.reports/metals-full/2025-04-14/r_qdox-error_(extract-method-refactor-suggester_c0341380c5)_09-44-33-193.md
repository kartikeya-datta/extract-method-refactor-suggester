error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9849.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9849.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9849.java
text:
```scala
r@@eturn new FieldDataBreakerStats(breaker.getMaximum(), breaker.getUsed(), breaker.getOverhead(), breaker.getTrippedCount());

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
package org.elasticsearch.indices.fielddata.breaker;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.common.breaker.MemoryCircuitBreaker;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.node.settings.NodeSettingsService;

/**
 * The InternalCircuitBreakerService handles providing
 * {@link org.elasticsearch.common.breaker.MemoryCircuitBreaker}s
 * that can be used to keep track of memory usage across the node, preventing
 * actions that could cause an {@link OutOfMemoryError} on the node.
 */
public class InternalCircuitBreakerService extends AbstractLifecycleComponent<InternalCircuitBreakerService> implements CircuitBreakerService {

    public static final String CIRCUIT_BREAKER_MAX_BYTES_SETTING = "indices.fielddata.breaker.limit";
    public static final String CIRCUIT_BREAKER_OVERHEAD_SETTING = "indices.fielddata.breaker.overhead";

    public static final double DEFAULT_OVERHEAD_CONSTANT = 1.03;
    private static final String DEFAULT_BREAKER_LIMIT = "60%";

    private volatile MemoryCircuitBreaker breaker;
    private volatile long maxBytes;
    private volatile double overhead;

    @Inject
    public InternalCircuitBreakerService(Settings settings, NodeSettingsService nodeSettingsService) {
        super(settings);
        this.maxBytes = settings.getAsMemory(CIRCUIT_BREAKER_MAX_BYTES_SETTING, DEFAULT_BREAKER_LIMIT).bytes();
        this.overhead = settings.getAsDouble(CIRCUIT_BREAKER_OVERHEAD_SETTING, DEFAULT_OVERHEAD_CONSTANT);

        this.breaker = new MemoryCircuitBreaker(new ByteSizeValue(maxBytes), overhead, null, logger);

        nodeSettingsService.addListener(new ApplySettings());
    }

    class ApplySettings implements NodeSettingsService.Listener {
        @Override
        public void onRefreshSettings(Settings settings) {
            // clear breaker now that settings have changed
            long newMaxByteSizeValue = settings.getAsMemory(CIRCUIT_BREAKER_MAX_BYTES_SETTING, Long.toString(maxBytes)).bytes();
            boolean breakerResetNeeded = false;

            if (newMaxByteSizeValue != maxBytes) {
                logger.info("updating [{}] from [{}]({}) to [{}]({})", CIRCUIT_BREAKER_MAX_BYTES_SETTING,
                        InternalCircuitBreakerService.this.maxBytes, new ByteSizeValue(InternalCircuitBreakerService.this.maxBytes),
                        newMaxByteSizeValue, new ByteSizeValue(newMaxByteSizeValue));
                maxBytes = newMaxByteSizeValue;
                breakerResetNeeded = true;
            }

            double newOverhead = settings.getAsDouble(CIRCUIT_BREAKER_OVERHEAD_SETTING, overhead);
            if (newOverhead != overhead) {
                logger.info("updating [{}] from [{}] to [{}]", CIRCUIT_BREAKER_OVERHEAD_SETTING,
                        overhead, newOverhead);
                overhead = newOverhead;
                breakerResetNeeded = true;
            }

            if (breakerResetNeeded) {
                resetBreaker();
            }
        }
    }

    /**
     * @return a {@link org.elasticsearch.common.breaker.MemoryCircuitBreaker} that can be used for aggregating memory usage
     */
    public MemoryCircuitBreaker getBreaker() {
        return this.breaker;
    }

    /**
     * Reset the breaker, creating a new one and initializing its used value
     * to the actual field data usage, or the existing estimated usage if the
     * actual value is not available. Will not trip the breaker even if the
     * used value is higher than the limit for the breaker.
     */
    public synchronized void resetBreaker() {
        final MemoryCircuitBreaker oldBreaker = this.breaker;
        // discard old breaker by creating a new one and pre-populating from the current breaker
        this.breaker = new MemoryCircuitBreaker(new ByteSizeValue(maxBytes), overhead, oldBreaker, logger);
    }

    @Override
    public FieldDataBreakerStats stats() {
        return new FieldDataBreakerStats(breaker.getMaximum(), breaker.getUsed(), breaker.getOverhead());
    }

    @Override
    protected void doStart() throws ElasticsearchException {
    }

    @Override
    protected void doStop() throws ElasticsearchException {
    }

    @Override
    protected void doClose() throws ElasticsearchException {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9849.java