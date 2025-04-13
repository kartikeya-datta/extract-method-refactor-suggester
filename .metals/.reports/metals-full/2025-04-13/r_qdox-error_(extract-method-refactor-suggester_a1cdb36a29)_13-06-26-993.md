error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/728.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/728.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/728.java
text:
```scala
s@@uper.setDone(done);

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

import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.property.BooleanProperty;
import org.apache.jmeter.testelement.property.IntegerProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.StringProperty;

/**
 * Class that implements the Loop Controller.
 */
public class LoopController extends GenericController implements Serializable {

    private static final String LOOPS = "LoopController.loops"; // $NON-NLS-1$

    private static final long serialVersionUID = 7833960784370272300L;

    /*
     * In spite of the name, this is actually used to determine if the loop controller is repeatable.
     *
     * The value is only used in nextIsNull() when the loop end condition has been detected:
     * If forever==true, then it calls resetLoopCount(), otherwise it calls setDone(true).
     *
     * Loop Controllers always set forever=true, so that they will be executed next time
     * the parent invokes them.
     *
     * Thread Group sets the value false, so nextIsNull() sets done, and the Thread Group will not be repeated.
     * However, it's not clear that a Thread Group could ever be repeated.
     */
    private static final String CONTINUE_FOREVER = "LoopController.continue_forever"; // $NON-NLS-1$

    private transient int loopCount = 0;

    // Cache loop value, see Bug 54467
    private Integer nbLoops;

    public LoopController() {
        setContinueForever_private(true);
    }

    public void setLoops(int loops) {
        setProperty(new IntegerProperty(LOOPS, loops));
    }

    public void setLoops(String loopValue) {
        setProperty(new StringProperty(LOOPS, loopValue));
    }

    public int getLoops() {
        try {
            JMeterProperty prop = getProperty(LOOPS);
            return Integer.parseInt(prop.getStringValue());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public String getLoopString() {
        return getPropertyAsString(LOOPS);
    }

    /**
     * Determines whether the loop will return any samples if it is rerun.
     *
     * @param forever
     *            true if the loop must be reset after ending a run
     */
    public void setContinueForever(boolean forever) {
        setContinueForever_private(forever);
    }

    private void setContinueForever_private(boolean forever) {
        setProperty(new BooleanProperty(CONTINUE_FOREVER, forever));
    }

    private boolean getContinueForever() {
        return getPropertyAsBoolean(CONTINUE_FOREVER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Sampler next() {
        if(endOfLoop()) {
            if (!getContinueForever()) {
                setDone(true);
            }
            return null;
        }
        return super.next();
    }

    private boolean endOfLoop() {
        if(nbLoops==null) {
            // Ensure we compute it only once per parent iteration, see Bug 54467
            nbLoops = Integer.valueOf(getLoops());
        }
        final int loops = nbLoops.intValue();
        return (loops > -1) && (loopCount >= loops);
    }

    @Override
    protected void setDone(boolean done) {
        nbLoops = null;
        super.setDone(true);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Sampler nextIsNull() throws NextIsNullException {
        reInitialize();
        if (endOfLoop()) {
            if (!getContinueForever()) {
                setDone(true);
            } else {
                resetLoopCount();
            }
            return null;
        }
        return next();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void triggerEndOfLoop() {
        super.triggerEndOfLoop();
        resetLoopCount();
    }
    
    protected void incrementLoopCount() {
        loopCount++;
    }

    protected void resetLoopCount() {
        loopCount = 0;
        nbLoops = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getIterCount() {
        return loopCount + 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reInitialize() {
        setFirst(true);
        resetCurrent();
        incrementLoopCount();
        recoverRunningVersion();
    }
    
    /**
     * Start next iteration
     */
    public void startNextLoop() {
        reInitialize();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/728.java