error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6338.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6338.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6338.java
text:
```scala
private static final L@@ogger log = LoggingManager.getLoggerForClass();

/*
 * Copyright 2002-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * This class implements a constant throughput timer. A Constant Throughtput
 * Timer paces the samplers under it's influence so that the total number of
 * samples per unit of time approaches a given constant as much as possible.
 *
 */
public class ConstantThroughputTimer
        extends AbstractTestElement
        implements Timer, TestListener,TestBean
{
	protected static final Logger log = LoggingManager.getLoggerForClass();

    /**
     * Target time for the start of the next request. The delay provided by
     * the timer will be calculated so that the next request happens at this
     * time.
     */
    private long targetTime= 0;

	/**
	 * Desired throughput, in samples per minute.
	 */
	private double throughput;

    /**
     * Constructor for a non-configured ConstantThroughputTimer.
     */
    public ConstantThroughputTimer()
    {
    }

    /**
     * Sets the desired throughput.
     *
     * @param throughput Desired sampling rate, in samples per minute.
     */
    public void setThroughput(double throughput)
    {
       log.info("setting throughput to: " + throughput);
    	this.throughput= throughput;
    }

    /**
     * Gets the configured desired throughput.
     *
     * @return the rate at which samples should occur, in samples per minute.
     */
    public double getThroughput()
    {
       log.info("Getting throughput, which is: " + throughput);
    	return throughput;
    }
    
    /**
     * Retrieve the delay to use during test execution.
     * 
     * @see org.apache.jmeter.timers.Timer#delay()
     */
    public synchronized long delay()
    {
       log.info("in Delay, using throughput, which is " + getThroughput());
        long currentTime = System.currentTimeMillis();
        long currentTarget = targetTime == 0 ? currentTime : targetTime;
        targetTime = currentTarget + (long)( 60000.0 / getThroughput() );
        if (currentTime > currentTarget)
        {
            // We're behind schedule -- try to catch up:
            return 0;
        }
        return currentTarget - currentTime;
    }

    /**
     * Provide a description of this timer class.
     * 
     * TODO: Is this ever used? I can't remember where. Remove if it isn't --
     * TODO: or obtain text from bean's displayName or shortDescription.
     *
     * @return the description of this timer class.
     */
    public String toString()
    {
        return JMeterUtils.getResString("constant_throughput_timer_memo");
    }

    /**
     * Get the timer ready to compute delays for a new test.
     * 
     * @see org.apache.jmeter.testelement.TestListener#testStarted()
     */
    public synchronized void testStarted()//synch to protect targetTime
    {
    	log.debug("Test started - reset throughput calculation.");
    	targetTime= 0;
    }

    /* (non-Javadoc)
     * @see org.apache.jmeter.testelement.TestListener#testEnded()
     */
    public void testEnded()
    {
    }

    /* (non-Javadoc)
     * @see org.apache.jmeter.testelement.TestListener#testStarted(java.lang.String)
     */
    public void testStarted(String host)
    {
    }

    /* (non-Javadoc)
     * @see org.apache.jmeter.testelement.TestListener#testEnded(java.lang.String)
     */
    public void testEnded(String host)
    {
    }

    /* (non-Javadoc)
     * @see org.apache.jmeter.testelement.TestListener#testIterationStart(org.apache.jmeter.engine.event.LoopIterationEvent)
     */
    public void testIterationStart(LoopIterationEvent event)
    {
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6338.java