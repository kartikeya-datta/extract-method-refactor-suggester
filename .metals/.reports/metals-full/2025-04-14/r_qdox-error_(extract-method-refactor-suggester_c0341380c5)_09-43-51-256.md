error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3707.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3707.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3707.java
text:
```scala
private static final S@@tring INCLUDE_TIMERS = "TransactionController.includeTimers";// $NON-NLS-1$

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

package org.apache.jmeter.control;

import java.io.Serializable;

import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleListener;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.property.BooleanProperty;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.threads.ListenerNotifier;
import org.apache.jmeter.threads.SamplePackage;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * Transaction Controller to measure transaction times
 *
 * There are two different modes for the controller:
 * - generate additional total sample after nested samples (as in JMeter 2.2)
 * - generate parent sampler containing the nested samples
 *
 */
public class TransactionController extends GenericController implements SampleListener, Controller, Serializable {
    private static final long serialVersionUID = 233L;
    
    private static final String TRUE = Boolean.toString(true); // i.e. "true"

    private static final String PARENT = "TransactionController.parent";// $NON-NLS-1$

    private final static String INCLUDE_TIMERS = "TransactionController.includeTimers";// $NON-NLS-1$
    
    private static final Logger log = LoggingManager.getLoggerForClass();

    /**
     * Only used in parent Mode
     */
    private transient TransactionSampler transactionSampler;
    
    /**
     * Only used in NON parent Mode
     */
    private transient ListenerNotifier lnf;

    /**
     * Only used in NON parent Mode
     */
    private transient SampleResult res;
    
    /**
     * Only used in NON parent Mode
     */
    private transient int calls;
    
    /**
     * Only used in NON parent Mode
     */
    private transient int noFailingSamples;

    /**
     * Cumulated pause time to excluse timer and post/pre processor times
     * Only used in NON parent Mode
     */
    private transient long pauseTime;

    /**
     * Previous end time
     * Only used in NON parent Mode
     */
    private transient long prevEndTime;

    /**
     * Creates a Transaction Controller
     */
    public TransactionController() {
        lnf = new ListenerNotifier();
    }

    private Object readResolve(){
        lnf = new ListenerNotifier();
        return this;
    }

    public void setParent(boolean _parent){
        setProperty(new BooleanProperty(PARENT, _parent));
    }

    public boolean isParent(){
        return getPropertyAsBoolean(PARENT);
    }

    /**
     * @see org.apache.jmeter.control.Controller#next()
     */
    @Override
    public Sampler next(){
        if (isParent()){
            return next1();
        }
        return next2();
    }

///////////////// Transaction Controller - parent ////////////////

    private Sampler next1() {
        // Check if transaction is done
        if(transactionSampler != null && transactionSampler.isTransactionDone()) {
            if (log.isDebugEnabled()) {
                log.debug("End of transaction " + getName());
            }
            // This transaction is done
            transactionSampler = null;
            return null;
        }

        // Check if it is the start of a new transaction
        if (isFirst()) // must be the start of the subtree
        {
            if (log.isDebugEnabled()) {
                log.debug("Start of transaction " + getName());
            }
            transactionSampler = new TransactionSampler(this, getName());
        }

        // Sample the children of the transaction
        Sampler subSampler = super.next();
        transactionSampler.setSubSampler(subSampler);
        // If we do not get any sub samplers, the transaction is done
        if (subSampler == null) {
            transactionSampler.setTransactionDone();
        }
        return transactionSampler;
    }

    @Override
    protected Sampler nextIsAController(Controller controller) throws NextIsNullException {
        if (!isParent()) {
            return super.nextIsAController(controller);
        }
        Sampler returnValue;
        Sampler sampler = controller.next();
        if (sampler == null) {
            currentReturnedNull(controller);
            // We need to call the super.next, instead of this.next, which is done in GenericController,
            // because if we call this.next(), it will return the TransactionSampler, and we do not want that.
            // We need to get the next real sampler or controller
            returnValue = super.next();
        } else {
            returnValue = sampler;
        }
        return returnValue;
    }

////////////////////// Transaction Controller - additional sample //////////////////////////////

    private Sampler next2() {
        if (isFirst()) // must be the start of the subtree
        {
            calls = 0;
            noFailingSamples = 0;
            res = new SampleResult();
            res.setSampleLabel(getName());
            // Assume success
            res.setSuccessful(true);
            res.sampleStart();
            prevEndTime = res.getStartTime();//???
            pauseTime = 0;
        }
        boolean isLast = current==super.subControllersAndSamplers.size();
        Sampler returnValue = super.next();
        if (returnValue == null && isLast) // Must be the end of the controller
        {
            if (res != null) {
                res.setIdleTime(pauseTime+res.getIdleTime());
                res.sampleEnd();
                res.setResponseMessage("Number of samples in transaction : " + calls + ", number of failing samples : " + noFailingSamples);
                if(res.isSuccessful()) {
                    res.setResponseCodeOK();
                }
                notifyListeners();
            }
        }
        else {
            // We have sampled one of our children
            calls++;
        }

        return returnValue;
    }

    /**
     * @see org.apache.jmeter.control.GenericController#triggerEndOfLoop()
     */
    @Override
    public void triggerEndOfLoop() {
        if(!isParent()) {
            if (res != null) {
                res.setIdleTime(pauseTime+res.getIdleTime());
                res.sampleEnd();
                res.setSuccessful(TRUE.equals(JMeterContextService.getContext().getVariables().get(JMeterThread.LAST_SAMPLE_OK)));
                res.setResponseMessage("Number of samples in transaction : " + calls + ", number of failing samples : " + noFailingSamples);
                notifyListeners();
            }
        } else {
            transactionSampler.setTransactionDone();
            // This transaction is done
            transactionSampler = null;
        }
        super.triggerEndOfLoop();
    }

    /**
     * Create additional SampleEvent in NON Parent Mode
     */
    protected void notifyListeners() {
        // TODO could these be done earlier (or just once?)
        JMeterContext threadContext = getThreadContext();
        JMeterVariables threadVars = threadContext.getVariables();
        SamplePackage pack = (SamplePackage) threadVars.getObject(JMeterThread.PACKAGE_OBJECT);
        if (pack == null) {
            // If child of TransactionController is a ThroughputController and TPC does
            // not sample its children, then we will have this
            // TODO Should this be at warn level ?
            log.warn("Could not fetch SamplePackage");
        } else {
            SampleEvent event = new SampleEvent(res, threadContext.getThreadGroup().getName(),threadVars, true);
            // We must set res to null now, before sending the event for the transaction,
            // so that we can ignore that event in our sampleOccured method
            res = null;
            // bug 50032 
            if (!getThreadContext().isReinitializingSubControllers()) {
                lnf.notifyListeners(event, pack.getSampleListeners());
            }
        }
    }

    public void sampleOccurred(SampleEvent se) {
        if (!isParent()) {
            // Check if we are still sampling our children
            if(res != null && !se.isTransactionSampleEvent()) {
                SampleResult sampleResult = se.getResult();
                res.setThreadName(sampleResult.getThreadName());
                res.setBytes(res.getBytes() + sampleResult.getBytes());
                if (!isIncludeTimers()) {// Accumulate waiting time for later
                    pauseTime += sampleResult.getEndTime() - sampleResult.getTime() - prevEndTime;
                    prevEndTime = sampleResult.getEndTime();
                }
                if(!sampleResult.isSuccessful()) {
                    res.setSuccessful(false);
                    noFailingSamples++;
                }
                res.setAllThreads(sampleResult.getAllThreads());
                res.setGroupThreads(sampleResult.getGroupThreads());
                res.setLatency(res.getLatency() + sampleResult.getLatency());
            }
        }
    }

    public void sampleStarted(SampleEvent e) {
    }

    public void sampleStopped(SampleEvent e) {
    }

    /**
     * Whether to include timers and pre/post processor time in overall sample.
     * @param includeTimers
     */
    public void setIncludeTimers(boolean includeTimers) {
        setProperty(INCLUDE_TIMERS, includeTimers, true); // default true for compatibility
    }

    /**
     * Whether to include timer and pre/post processor time in overall sample.
     *
     * @return boolean (defaults to true for backwards compatibility)
     */
    public boolean isIncludeTimers() {
        return getPropertyAsBoolean(INCLUDE_TIMERS, true);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3707.java