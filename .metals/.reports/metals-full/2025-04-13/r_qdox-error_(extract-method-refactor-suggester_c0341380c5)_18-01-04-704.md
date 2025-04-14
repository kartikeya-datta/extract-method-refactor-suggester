error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7985.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7985.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7985.java
text:
```scala
private B@@yte counterLock = new Byte("0"); // ensure counts are updated correctly

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

import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.testelement.property.BooleanProperty;
import org.apache.jmeter.testelement.property.FloatProperty;
import org.apache.jmeter.testelement.property.IntegerProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * This class represents a controller that can control the number of times that
 * it is executed, either by the total number of times the user wants the
 * controller executed (BYNUMBER) or by the percentage of time it is called
 * (BYPERCENT)
 *
 * The current implementation executes the first N samples (BYNUMBER)
 * or the last N% of samples (BYPERCENT).
 */
public class ThroughputController extends GenericController implements Serializable, LoopIterationListener,
        TestStateListener {

    private static final long serialVersionUID = 233L;

    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final int BYNUMBER = 0;

    public static final int BYPERCENT = 1;

    private static final String STYLE = "ThroughputController.style";// $NON-NLS-1$

    private static final String PERTHREAD = "ThroughputController.perThread";// $NON-NLS-1$

    private static final String MAXTHROUGHPUT = "ThroughputController.maxThroughput";// $NON-NLS-1$

    private static final String PERCENTTHROUGHPUT = "ThroughputController.percentThroughput";// $NON-NLS-1$

    private static class MutableInteger{
        private int integer;
        MutableInteger(int value){
            integer=value;
        }
        int incr(){
            return ++integer;
        }
        public int intValue() {
            return integer;
        }
    }

    // These items are shared between threads in a group by the clone() method
    // They are initialised by testStarted() so don't need to be serialised
    private transient MutableInteger globalNumExecutions;

    private transient MutableInteger globalIteration;

    private String counterLock = ""; // ensure counts are updated correctly
    // Need to use something that is serializable, so Object is no use

    /**
     * Number of iterations on which we've chosen to deliver samplers.
     */
    private int numExecutions = 0;

    /**
     * Index of the current iteration. 0-based.
     */
    private int iteration = -1;

    /**
     * Whether to deliver samplers on this iteration.
     */
    private boolean runThisTime;

    public ThroughputController() {
        setStyle(BYNUMBER);
        setPerThread(true);
        setMaxThroughput(1);
        setPercentThroughput(100);
        runThisTime = false;
    }

    public void setStyle(int style) {
        setProperty(new IntegerProperty(STYLE, style));
    }

    public int getStyle() {
        return getPropertyAsInt(STYLE);
    }

    public void setPerThread(boolean perThread) {
        setProperty(new BooleanProperty(PERTHREAD, perThread));
    }

    public boolean isPerThread() {
        return getPropertyAsBoolean(PERTHREAD);
    }

    public void setMaxThroughput(int maxThroughput) {
        setProperty(new IntegerProperty(MAXTHROUGHPUT, maxThroughput));
    }

    public void setMaxThroughput(String maxThroughput) {
        setProperty(new StringProperty(MAXTHROUGHPUT, maxThroughput));
    }

    public String getMaxThroughput() {
        return getPropertyAsString(MAXTHROUGHPUT);
    }

    protected int getMaxThroughputAsInt() {
        JMeterProperty prop = getProperty(MAXTHROUGHPUT);
        int retVal = 1;
        if (prop instanceof IntegerProperty) {
            retVal = ((IntegerProperty) prop).getIntValue();
        } else {
            try {
                retVal = Integer.parseInt(prop.getStringValue());
            } catch (NumberFormatException e) {
            	log.warn("Error parsing "+prop.getStringValue(),e);
            }
        }
        return retVal;
    }

    public void setPercentThroughput(float percentThroughput) {
        setProperty(new FloatProperty(PERCENTTHROUGHPUT, percentThroughput));
    }

    public void setPercentThroughput(String percentThroughput) {
        setProperty(new StringProperty(PERCENTTHROUGHPUT, percentThroughput));
    }

    public String getPercentThroughput() {
        return getPropertyAsString(PERCENTTHROUGHPUT);
    }

    protected float getPercentThroughputAsFloat() {
        JMeterProperty prop = getProperty(PERCENTTHROUGHPUT);
        float retVal = 100;
        if (prop instanceof FloatProperty) {
            retVal = ((FloatProperty) prop).getFloatValue();
        } else {
            try {
                retVal = Float.parseFloat(prop.getStringValue());
            } catch (NumberFormatException e) {
            	log.warn("Error parsing "+prop.getStringValue(),e);
            }
        }
        return retVal;
    }

    private int getExecutions() {
        if (!isPerThread()) {
            synchronized (counterLock) {
                return globalNumExecutions.intValue();
            }
        }
        return numExecutions;
    }

    /**
     * @see org.apache.jmeter.control.Controller#next()
     */
    @Override
    public Sampler next() {
        if (runThisTime) {
            return super.next();
        }
        return null;
    }

    /**
     * Decide whether to return any samplers on this iteration.
     */
    private boolean decide(int executions, int iterations) {
        if (getStyle() == BYNUMBER) {
            return executions < getMaxThroughputAsInt();
        }
        return (100.0 * executions + 50.0) / (iterations + 1) < getPercentThroughputAsFloat();
    }

    /**
     * @see org.apache.jmeter.control.Controller#isDone()
     */
    @Override
    public boolean isDone() {
        if (subControllersAndSamplers.size() == 0) {
            return true;
        } else if (getStyle() == BYNUMBER && getExecutions() >= getMaxThroughputAsInt()
                && current >= getSubControllers().size()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Object clone() {
        ThroughputController clone = (ThroughputController) super.clone();
        clone.numExecutions = numExecutions;
        clone.iteration = iteration;
        clone.runThisTime = false;
        // Ensure global counters and lock are shared across threads in the group
        clone.globalIteration = globalIteration;
        clone.globalNumExecutions = globalNumExecutions;
        clone.counterLock = counterLock;
        return clone;
    }

    @Override
    public void iterationStart(LoopIterationEvent iterEvent) {
        if (!isPerThread()) {
            synchronized (counterLock) {
                globalIteration.incr();
                runThisTime = decide(globalNumExecutions.intValue(), globalIteration.intValue());
                if (runThisTime) {
                    globalNumExecutions.incr();
                }
            }
        } else {
            iteration++;
            runThisTime = decide(numExecutions, iteration);
            if (runThisTime) {
                numExecutions++;
            }
        }
    }

    @Override
    public void testStarted() {
        synchronized (counterLock) {
            globalNumExecutions = new MutableInteger(0);
            globalIteration = new MutableInteger(-1);
        }
    }

    @Override
    public void testStarted(String host) {
        testStarted();
    }

    @Override
    public void testEnded() {
    	// NOOP
    }

    @Override
    public void testEnded(String host) {
    	// NOOP
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7985.java