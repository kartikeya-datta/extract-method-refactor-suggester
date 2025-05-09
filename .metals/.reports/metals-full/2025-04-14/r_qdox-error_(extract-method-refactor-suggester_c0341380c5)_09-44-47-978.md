error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16166.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16166.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16166.java
text:
```scala
r@@eturn JMeterUtils.getResString("constant_throughput_timer_memo"); //$NON-NLS-1$

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

package org.apache.jmeter.timers;

import java.util.Hashtable;
import java.util.Map;

import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * This class implements a constant throughput timer. A Constant Throughtput
 * Timer paces the samplers under its influence so that the total number of
 * samples per unit of time approaches a given constant as much as possible.
 * 
 * There are two different ways of pacing the requests:
 * - delay each thread according to when it last ran
 * - delay each thread according to when any thread last ran 
 */
public class ConstantThroughputTimer extends AbstractTestElement implements Timer, TestListener, TestBean {
	private static final long serialVersionUID = 3;

    private static class ThroughputInfo{
        final Object MUTEX = new Object();
        long lastScheduledTime = 0;
    }
	private static final Logger log = LoggingManager.getLoggerForClass();

	private static final double MILLISEC_PER_MIN = 60000.0;

	/**
	 * Target time for the start of the next request. The delay provided by the
	 * timer will be calculated so that the next request happens at this time.
	 */
	private long previousTime = 0;

	private String calcMode; // String representing the mode
								// (Locale-specific)

	private int modeInt; // mode as an integer

	/**
	 * Desired throughput, in samples per minute.
	 */
	private double throughput;

    //For calculating throughput across all threads
    private final static ThroughputInfo allThreadsInfo = new ThroughputInfo();
    
    //For holding the ThrougputInfo objects for all ThreadGroups. Keyed by ThreadGroup objects
    private final static Map threadGroupsInfoMap = new Hashtable();
    

	/**
	 * Constructor for a non-configured ConstantThroughputTimer.
	 */
	public ConstantThroughputTimer() {
	}

	/**
	 * Sets the desired throughput.
	 * 
	 * @param throughput
	 *            Desired sampling rate, in samples per minute.
	 */
	public void setThroughput(double throughput) {
		this.throughput = throughput;
	}

	/**
	 * Gets the configured desired throughput.
	 * 
	 * @return the rate at which samples should occur, in samples per minute.
	 */
	public double getThroughput() {
		return throughput;
	}

	public String getCalcMode() {
		return calcMode;
	}

	public void setCalcMode(String mode) {
        this.calcMode = mode;
        // TODO find better way to get modeInt
		this.modeInt = ConstantThroughputTimerBeanInfo.getCalcModeAsInt(calcMode);
	}

	/**
	 * Retrieve the delay to use during test execution.
	 * 
	 * @see org.apache.jmeter.timers.Timer#delay()
	 */
	public long delay() {
		long currentTime = System.currentTimeMillis();

        /* 
         * If previous time is zero, then target will be in the past.
         * This is what we want, so first sample is run without a delay.
        */
        long currentTarget = previousTime  + calculateDelay();
		if (currentTime > currentTarget) {
			// We're behind schedule -- try to catch up:
            previousTime = currentTime;
			return 0;
		}
        previousTime = currentTarget;
		return currentTarget - currentTime;
	}

	/**
	 * @param currentTime
	 * @return new Target time
	 */
    // TODO - is this used?
	protected long calculateCurrentTarget(long currentTime) {
		return currentTime + calculateDelay();
	}

	// Calculate the delay based on the mode
	private long calculateDelay() {
		long delay = 0;
        // N.B. we fetch the throughput each time, as it may vary during a test
		long msPerRequest = (long) (MILLISEC_PER_MIN / getThroughput());
		switch (modeInt) {
		case 1: // Total number of threads
			delay = JMeterContextService.getNumberOfThreads() * msPerRequest;
			break;
            
		case 2: // Active threads in this group
			delay = JMeterContextService.getContext().getThreadGroup().getNumberOfThreads() * msPerRequest;
			break;
            
        case 3: // All threads - alternate calculation
            delay = calculateSharedDelay(allThreadsInfo,msPerRequest);
            break;
            
        case 4: //All threads in this group - alternate calculation
            final org.apache.jmeter.threads.ThreadGroup group = 
                JMeterContextService.getContext().getThreadGroup();
            ThroughputInfo groupInfo;
            synchronized (threadGroupsInfoMap) {
                groupInfo = (ThroughputInfo)threadGroupsInfoMap.get(group);
                if (groupInfo == null) {
                    groupInfo = new ThroughputInfo();
                    threadGroupsInfoMap.put(group, groupInfo);
                }
            }
            delay = calculateSharedDelay(groupInfo,msPerRequest);
            break;
            
		default:
			delay = msPerRequest; // i.e. * 1
			break;
		}
		return delay;
	}

    private long calculateSharedDelay(ThroughputInfo info, long milliSecPerRequest) {
        final long now = System.currentTimeMillis();
        final long calculatedDelay;

        //Synchronize on the info object's MUTEX to ensure
        //Multiple threads don't update the scheduled time simultaneously
        synchronized (info.MUTEX) {
            final long nextRequstTime = info.lastScheduledTime + milliSecPerRequest;
            info.lastScheduledTime = Math.max(now, nextRequstTime);
            calculatedDelay = info.lastScheduledTime - now;
        }
        
        return Math.max(calculatedDelay, 0);
    }

    private synchronized void reset() {
        allThreadsInfo.lastScheduledTime = 0;
        threadGroupsInfoMap.clear();
        previousTime = 0;
    }   

	/**
	 * Provide a description of this timer class.
	 * 
	 * TODO: Is this ever used? I can't remember where. Remove if it isn't --
	 * TODO: or obtain text from bean's displayName or shortDescription.
	 * 
	 * @return the description of this timer class.
	 */
	public String toString() {
		return JMeterUtils.getResString("constant_throughput_timer_memo");
	}

	/**
	 * Get the timer ready to compute delays for a new test.
	 * 
	 * @see org.apache.jmeter.testelement.TestListener#testStarted()
	 */
	public void testStarted()
	{
		log.debug("Test started - reset throughput calculation.");
        reset();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.testelement.TestListener#testEnded()
	 */
	public void testEnded() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.testelement.TestListener#testStarted(java.lang.String)
	 */
	public void testStarted(String host) {
		testStarted();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.testelement.TestListener#testEnded(java.lang.String)
	 */
	public void testEnded(String host) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.testelement.TestListener#testIterationStart(org.apache.jmeter.engine.event.LoopIterationEvent)
	 */
	public void testIterationStart(LoopIterationEvent event) {
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16166.java