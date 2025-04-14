error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2391.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2391.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2391.java
text:
```scala
n@@otifier.notifyListeners(event, listeners);

/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
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
import org.apache.jmeter.processor.PostProcessor;
import org.apache.jmeter.processor.PreProcessor;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jmeter.timers.Timer;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.SearchByClass;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;
/****************************************
 * The JMeter interface to the sampling process, allowing JMeter to see the
 * timing, add listeners for sampling events and to stop the sampling process.
 *
 *@author    $Author$
 *@created   $Date$
 *@version   $Revision$
 ***************************************/
public class JMeterThread implements Runnable, java.io.Serializable
{
    transient private static Logger log = Hierarchy.getDefaultHierarchy().getLoggerFor("jmeter.engine");
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
    Collection threadListeners;
    ListenerNotifier notifier;

    /****************************************
     * !ToDo (Constructor description)
     ***************************************/
    public JMeterThread()
    {}
    public JMeterThread(HashTree test, JMeterThreadMonitor monitor, ListenerNotifier note)
    {
        this.monitor = monitor;
        threadVars = new JMeterVariables();
        testTree = test;
        compiler = new TestCompiler(testTree, threadVars);
        controller = (Controller) testTree.getArray()[0];
        SearchByClass threadListenerSearcher = new SearchByClass(ThreadListener.class);
        test.traverse(threadListenerSearcher);
        threadListeners = threadListenerSearcher.getSearchResults();
        notifier = note;
    }

    public void setInitialContext(JMeterContext context)
    {
        threadVars.putAll(context.getVariables());
    }

    public void setThreadName(String threadName)
    {
        this.threadName = threadName;
    }
    /****************************************
     * !ToDo (Method description)
     ***************************************/
    public void run()
    {
        try
        {
            threadContext = JMeterContextService.getContext();
            threadContext.setVariables(threadVars);
            initializeThreadListeners();
            testTree.traverse(compiler);
            running = true;
            //listeners = controller.getListeners();
            Sampler entry = null;
            rampUpDelay();
            log.info("Thread " + Thread.currentThread().getName() + " started");
            while (running)
            {
                while (controller.hasNext() && running)
                {
                    try
                    {
                        if (controller.isNextFirst())
                        {
                            notifyThreadListeners();
                        }
                        SamplePackage pack = compiler.configureSampler(controller.next());
                        runPreProcessors(pack.getPreProcessors());
                        delay(pack.getTimers());
                        SampleResult result = pack.getSampler().sample(null);
                        result.setThreadName(threadName);
                        result.setTimeStamp(System.currentTimeMillis());
                        threadContext.setPreviousResult(result);
                        threadContext.setCurrentSampler(pack.getSampler());
                        checkAssertions(pack.getAssertions(), result);
                        runPostProcessors(pack.getPostProcessors());
                        notifyListeners(pack.getSampleListeners(), result);
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
            log.info("Thread " + threadName + " is done");
            monitor.threadFinished(this);
        }
    }

    public String getThreadName()
    {
        return threadName;
    }

    /****************************************
     * !ToDo (Method description)
     ***************************************/
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
            AssertionResult assertion = ((Assertion) iter.next()).getResult(result);
            result.setSuccessful(result.isSuccessful() && !(assertion.isError() || assertion.isFailure()));
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

    private void runPreProcessors(List preProcessors)
    {
        ListIterator iter = preProcessors.listIterator(preProcessors.size());
        while (iter.hasPrevious())
        {
            PreProcessor ex = (PreProcessor) iter.previous();
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

    private void initializeThreadListeners()
    {
        Iterator iter = threadListeners.iterator();
        while (iter.hasNext())
        {
            ((ThreadListener) iter.next()).setJMeterVariables(threadVars);
        }
    }

    private void notifyThreadListeners()
    {
        threadVars.incIteration();
        Iterator iter = threadListeners.iterator();
        while (iter.hasNext())
        {
            ((ThreadListener) iter.next()).iterationStarted(threadVars.getIteration());
        }
    }

    private void notifyListeners(List listeners, SampleResult result)
    {
        SampleEvent event = new SampleEvent(result, (String) controller.getProperty(TestElement.NAME));
        compiler.sampleOccurred(event);
        notifier.addLast(event, listeners);

    }
    public void setInitialDelay(int delay)
    {
        initialDelay = delay;
    }
    /****************************************
     * Initial delay if ramp-up period is active for this group
     ***************************************/
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2391.java