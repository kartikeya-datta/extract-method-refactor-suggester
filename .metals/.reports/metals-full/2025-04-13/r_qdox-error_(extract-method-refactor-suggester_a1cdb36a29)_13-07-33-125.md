error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8819.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8819.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8819.java
text:
```scala
public v@@oid sampleOccurred(SampleEvent e) {

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.jmeter.samplers;

import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements batch reporting for remote testing.
 *
 */
public class StatisticalSampleSender implements SampleSender, Serializable {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final int DEFAULT_NUM_SAMPLE_THRESHOLD = 100;

    private static final long DEFAULT_TIME_THRESHOLD = 60000L;

    private RemoteSampleListener listener;

    private List sampleStore = new ArrayList();

    private Map sampleTable = new HashMap();

    private int numSamplesThreshold;

    private int sampleCount;

    private long timeThreshold;

    private long batchSendTime = -1;

    public StatisticalSampleSender(){
        log.warn("Constructor only intended for use in testing");
    }


    /**
     * Constructor
     *
     * @param listener that the List of sample events will be sent to.
     */
    StatisticalSampleSender(RemoteSampleListener listener) {
        this.listener = listener;
        init();
        log.info("Using batching for this run." + " Thresholds: num="
                + numSamplesThreshold + ", time=" + timeThreshold);
    }

    /**
     * Checks for the Jmeter properties num_sample_threshold and time_threshold,
     * and assigns defaults if not found.
     */
    private void init() {
        this.numSamplesThreshold = JMeterUtils.getPropDefault(
                "num_sample_threshold", DEFAULT_NUM_SAMPLE_THRESHOLD);
        this.timeThreshold = JMeterUtils.getPropDefault("time_threshold",
                DEFAULT_TIME_THRESHOLD);
    }

    /**
     * Checks if any sample events are still present in the sampleStore and
     * sends them to the listener. Informs the listener of the testended.
     */
    public void testEnded() {
        try {
            if (sampleStore.size() != 0) {
                sendBatch();
            }
            listener.testEnded();
        } catch (RemoteException err) {
            log.warn("testEnded()", err);
        }
    }

    /**
     * Checks if any sample events are still present in the sampleStore and
     * sends them to the listener. Informs the listener of the testended.
     *
     * @param host the hostname that the test has ended on.
     */
    public void testEnded(String host) {
        try {
            if (sampleStore.size() != 0) {
                sendBatch();
            }
            listener.testEnded(host);
        } catch (RemoteException err) {
            log.warn("testEnded(hostname)", err);
        }
    }

    /**
     * Stores sample events untill either a time or sample threshold is
     * breached. Both thresholds are reset if one fires. If only one threshold
     * is set it becomes the only value checked against. When a threhold is
     * breached the list of sample events is sent to a listener where the event
     * are fired locally.
     *
     * @param e a Sample Event
     */
    public void SampleOccurred(SampleEvent e) {
        synchronized (sampleStore) {
            // Locate the statistical sample colector
            String key = StatisticalSampleResult.getKey(e);
            StatisticalSampleResult statResult = (StatisticalSampleResult) sampleTable
                    .get(key);
            if (statResult == null) {
                statResult = new StatisticalSampleResult(e.getResult());
                // store the new statistical result collector
                sampleTable.put(key, statResult);
                // add a new wrapper samplevent
                sampleStore
                        .add(new SampleEvent(statResult, e.getThreadGroup()));
            }
            statResult.add(e.getResult());
            sampleCount++;
            if (numSamplesThreshold != -1) {
                if (sampleCount >= numSamplesThreshold) {
                    try {
                        if (log.isDebugEnabled()) {
                            log.debug("Firing sample");
                        }
                        sendBatch();
                    } catch (RemoteException err) {
                        log.warn("sampleOccurred", err);
                    }
                }
            }

            if (timeThreshold != -1) {
                long now = System.currentTimeMillis();
                // Checking for and creating initial timestamp to cheak against
                if (batchSendTime == -1) {
                    this.batchSendTime = now + timeThreshold;
                }

                if (batchSendTime < now) {
                    try {
                        if (log.isDebugEnabled()) {
                            log.debug("Firing time");
                        }
                        sendBatch();
                        this.batchSendTime = now + timeThreshold;
                    } catch (RemoteException err) {
                        log.warn("sampleOccurred", err);
                    }
                }
            }
        }
    }

    private void sendBatch() throws RemoteException {
        if (sampleStore.size() > 0) {
            listener.processBatch(sampleStore);
            sampleStore.clear();
            sampleTable.clear();
            sampleCount = 0;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8819.java