error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9905.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9905.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9905.java
text:
```scala
t@@hreadContext.clear();

/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001,2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/)."
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 * "Apache JMeter" must not be used to endorse or promote products
 * derived from this software without prior written permission. For
 * written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 * "Apache JMeter", nor may "Apache" appear in their name, without
 * prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.jmeter.threads;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.jmeter.assertions.Assertion;
import org.apache.jmeter.assertions.AssertionResult;
import org.apache.jmeter.control.Controller;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.processor.PostProcessor;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jmeter.timers.Timer;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.SearchByClass;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;

/**
 * The JMeter interface to the sampling process, allowing JMeter to see the
 * timing, add listeners for sampling events and to stop the sampling process.
 *
 * @author    $Author$
 * @version   $Revision$
 */
public class JMeterThread implements Runnable, java.io.Serializable
{
    transient private static Logger log =
        Hierarchy.getDefaultHierarchy().getLoggerFor("jmeter.engine");
    static Map samplers = new HashMap();
    int initialDelay = 0;
    Controller controller;
    private boolean running;
    HashTree testTree;
    TestCompiler compiler;
    JMeterThreadMonitor monitor;
    String threadName;
    JMeterContext threadContext;
    JMeterVariables threadVars;
    Collection testListeners;
    ListenerNotifier notifier;
    int threadNum = 0;
    long startTime = 0; 
    long endTime = 0;
    private boolean scheduler = false;
    //based on this scheduler is enabled or disabled

    public JMeterThread()
    {
    }

    public JMeterThread(
        HashTree test,
        JMeterThreadMonitor monitor,
        ListenerNotifier note)
    {
        this.monitor = monitor;
        threadVars = new JMeterVariables();
        testTree = test;
        compiler = new TestCompiler(testTree, threadVars);
        controller = (Controller) testTree.getArray()[0];
        SearchByClass threadListenerSearcher =
            new SearchByClass(TestListener.class);
        test.traverse(threadListenerSearcher);
        testListeners = threadListenerSearcher.getSearchResults();
        notifier = note;
    }

    public void setInitialContext(JMeterContext context)
    {
        threadVars.putAll(context.getVariables());
    }


    /**
     * Checks whether the JMeterThread is Scheduled.
     * @author T.Elanjchezhiyan(chezhiyan@siptech.co.in)
     */
    public boolean isScheduled()
    {
        return this.scheduler;
    }

    /**
     * Enable the scheduler for this JMeterThread.
     * @author T.Elanjchezhiyan(chezhiyan@siptech.co.in)
     */
    public void setScheduled(boolean sche)
    {
        this.scheduler = sche;
    }


    /**
     * Set the StartTime for this Thread.
     *
     * @param StartTime the StartTime value.
     * @author T.Elanjchezhiyan(chezhiyan@siptech.co.in)
     */
    public void setStartTime(long stime)
    {
        startTime = stime;
    }

    /**
     * Get the start time value.
     *
     * @return the start time value.
     * @author T.Elanjchezhiyan(chezhiyan@siptech.co.in)
     */
    public long getStartTime()
    {
        return startTime;
    }

    /**
     * Set the EndTime for this Thread.
     *
     * @param EndTime the EndTime value.
     * @author T.Elanjchezhiyan(chezhiyan@siptech.co.in)
     */
    public void setEndTime(long etime)
    {
        endTime = etime;
    }
    
    /**
     * Get the end time value.
     *
     * @return the end time  value.
     * @author T.Elanjchezhiyan(chezhiyan@siptech.co.in)
     */
    public long getEndTime()
    {
        return endTime;
    }


    /**
     * Check the scheduled time is completed.
     *
     * @author T.Elanjchezhiyan(chezhiyan@siptech.co.in)
     */
    public void stopScheduler()
    {
        long delay = System.currentTimeMillis() - endTime;
        if ((delay >= 0))
        {
            running = false;
        }
    }

    /**
     * Start the scheduler with the specified time
     *
     * aAuthor T.Elanjchezhiyan(chezhiyan@siptech.co.in)
     */
    public void startScheduler()
    {
        long delay = (startTime - System.currentTimeMillis());
        if (delay > 0)
        {
            try
            {
                running = true;
                Thread.sleep(delay);
            }
            catch (Exception e)
            {
            }
        }
        else
        {
            running = false;
        }
    }

    public void setThreadName(String threadName)
    {
        this.threadName = threadName;
    }

    public void run()
    {
        try
        {
            threadContext = JMeterContextService.getContext();
            threadContext.setVariables(threadVars);
            threadContext.setThreadNum(getThreadNum());
            testTree.traverse(compiler);
            running = true;
            //listeners = controller.getListeners();
            Sampler entry = null;
            rampUpDelay();
            
            if (scheduler)
            {
                //set the scheduler to start
                startScheduler();
            }

            log.info("Thread " + Thread.currentThread().getName() + " started");
            controller.initialize();
            controller.addIterationListener(new IterationListener());
            threadContext.setSamplingStarted(true);
            while (running)
            {
                Sampler sam;
                while (running && (sam=controller.next())!=null)
                {
                    try
                    {
                        threadContext.setCurrentSampler(sam);
                        SamplePackage pack = compiler.configureSampler(sam);
                        delay(pack.getTimers());                        
                        SampleResult result = pack.getSampler().sample(null);
                        result.setThreadName(threadName);
                        result.setTimeStamp(System.currentTimeMillis());
                        threadContext.setPreviousResult(result);
                        runPostProcessors(pack.getPostProcessors());
                        checkAssertions(pack.getAssertions(), result);
                        notifyListeners(pack.getSampleListeners(), result);
                        compiler.done(pack);
                        if (scheduler)
                        {
                            //checks the scheduler to stop the iteration
                            stopScheduler();
                        }

                    }
                    catch (Exception e)
                    {
                        log.error("", e);
                    }
                }
                if (controller.isDone())
                {
                    running = false;
                }
            }
        }
        finally
        {
            threadContext.setSamplingStarted(false);
            log.info("Thread " + threadName + " is done");
            monitor.threadFinished(this);
        }
    }

    public String getThreadName()
    {
        return threadName;
    }

    public void stop()
    {
        running = false;
        log.info("stopping " + threadName);
    }
    private void checkAssertions(List assertions, SampleResult result)
    {
        Iterator iter = assertions.iterator();
        while (iter.hasNext())
        {
            AssertionResult assertion =
                ((Assertion) iter.next()).getResult(result);
            result.setSuccessful(
                result.isSuccessful()
                    && !(assertion.isError() || assertion.isFailure()));
            result.addAssertionResult(assertion);
        }
    }

    private void runPostProcessors(List extractors)
    {
        ListIterator iter = extractors.listIterator(extractors.size());
        while (iter.hasPrevious())
        {
            PostProcessor ex = (PostProcessor) iter.previous();
            ex.process();
        }
    }

    private void delay(List timers)
    {
        int sum = 0;
        Iterator iter = timers.iterator();
        while (iter.hasNext())
        {
            sum += ((Timer) iter.next()).delay();
        }
        if (sum > 0)
        {
            try
            {
                Thread.sleep(sum);
            }
            catch (InterruptedException e)
            {
                log.error("", e);
            }
        }
    }

    private void notifyTestListeners()
    {
        threadVars.incIteration();
        Iterator iter = testListeners.iterator();
        while (iter.hasNext())
        {
            TestListener listener = (TestListener)iter.next();
            if(listener instanceof TestElement)
            {
                listener.testIterationStart(
                    new LoopIterationEvent(
                        controller,
                        threadVars.getIteration()));
                ((TestElement)listener).recoverRunningVersion();
            }
            else
            {
                listener.testIterationStart(
                    new LoopIterationEvent(
                        controller,
                        threadVars.getIteration()));
            }
        }
    }

    private void notifyListeners(List listeners, SampleResult result)
    {
        SampleEvent event =
            new SampleEvent(
                result,
                controller.getPropertyAsString(TestElement.NAME));
        compiler.sampleOccurred(event);
        notifier.notifyListeners(event, listeners);

    }
    public void setInitialDelay(int delay)
    {
        initialDelay = delay;
    }

    /**
     * Initial delay if ramp-up period is active for this group.
     */
    private void rampUpDelay()
    {
        if (initialDelay > 0)
        {
            try
            {
                Thread.sleep(initialDelay);
            }
            catch (InterruptedException e)
            {}
        }
    }
    
    /**
     * Returns the threadNum.
     */
    public int getThreadNum()
    {
        return threadNum;
    }

    /**
     * Sets the threadNum.
     * @param threadNum the threadNum to set
     */
    public void setThreadNum(int threadNum)
    {
        this.threadNum = threadNum;
    }
    
    private class IterationListener implements LoopIterationListener
    {
        /* (non-Javadoc)
         * @see LoopIterationListener#iterationStart(LoopIterationEvent)
         */
        public void iterationStart(LoopIterationEvent iterEvent)
        {
            notifyTestListeners();
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9905.java