error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16013.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16013.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16013.java
text:
```scala
private O@@bject lock = new Object();

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

package org.apache.jmeter.modifiers;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.processor.PreProcessor;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.BooleanProperty;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class UserParameters extends AbstractTestElement implements Serializable, PreProcessor, LoopIterationListener {
    private static final Logger log = LoggingManager.getLoggerForClass();

    public static final String NAMES = "UserParameters.names";// $NON-NLS-1$

    public static final String THREAD_VALUES = "UserParameters.thread_values";// $NON-NLS-1$

    public static final String PER_ITERATION = "UserParameters.per_iteration";// $NON-NLS-1$

    /*
     * Although the lock appears to be an instance lock, in fact the lock is
     * shared between all threads in a thread group, but different thread groups
     * have different locks - see the clone() method below
     *
     * The lock ensures that all the variables are processed together, which is
     * important for functions such as __CSVRead and _StringFromFile.
     */
    private Integer lock = new Integer(0);

    public CollectionProperty getNames() {
        return (CollectionProperty) getProperty(NAMES);
    }

    public CollectionProperty getThreadLists() {
        return (CollectionProperty) getProperty(THREAD_VALUES);
    }

    /**
     * The list of names of the variables to hold values. This list must come in
     * the same order as the sub lists that are given to
     * {@link #setThreadLists(Collection)}.
     */
    public void setNames(Collection list) {
        setProperty(new CollectionProperty(NAMES, list));
    }

    /**
     * The list of names of the variables to hold values. This list must come in
     * the same order as the sub lists that are given to
     * {@link #setThreadLists(CollectionProperty)}.
     */
    public void setNames(CollectionProperty list) {
        setProperty(list);
    }

    /**
     * The thread list is a list of lists. Each list within the parent list is a
     * collection of values for a simulated user. As many different sets of
     * values can be supplied in this fashion to cause JMeter to set different
     * values to variables for different test threads.
     */
    public void setThreadLists(Collection threadLists) {
        setProperty(new CollectionProperty(THREAD_VALUES, threadLists));
    }

    /**
     * The thread list is a list of lists. Each list within the parent list is a
     * collection of values for a simulated user. As many different sets of
     * values can be supplied in this fashion to cause JMeter to set different
     * values to variables for different test threads.
     */
    public void setThreadLists(CollectionProperty threadLists) {
        setProperty(threadLists);
    }

    private CollectionProperty getValues() {
        CollectionProperty threadValues = (CollectionProperty) getProperty(THREAD_VALUES);
        if (threadValues.size() > 0) {
            return (CollectionProperty) threadValues.get(getThreadContext().getThreadNum() % threadValues.size());
        }
        return new CollectionProperty("noname", new LinkedList());
    }

    public boolean isPerIteration() {
        return getPropertyAsBoolean(PER_ITERATION);
    }

    public void setPerIteration(boolean perIter) {
        setProperty(new BooleanProperty(PER_ITERATION, perIter));
    }

    public void process() {
        if (log.isDebugEnabled()) {
            log.debug(Thread.currentThread().getName() + " process " + isPerIteration());//$NON-NLS-1$
        }
        if (!isPerIteration()) {
            setValues();
        }
    }

    private void setValues() {
        synchronized (lock) {
            if (log.isDebugEnabled()) {
                log.debug(Thread.currentThread().getName() + " Running up named: " + getName());//$NON-NLS-1$
            }
            PropertyIterator namesIter = getNames().iterator();
            PropertyIterator valueIter = getValues().iterator();
            JMeterVariables jmvars = getThreadContext().getVariables();
            while (namesIter.hasNext() && valueIter.hasNext()) {
                String name = namesIter.next().getStringValue();
                String value = valueIter.next().getStringValue();
                if (log.isDebugEnabled()) {
                    log.debug(Thread.currentThread().getName() + " saving variable: " + name + "=" + value);//$NON-NLS-1$
                }
                jmvars.put(name, value);
            }
        }
    }

    /**
     * @see LoopIterationListener#iterationStart(LoopIterationEvent)
     */
    public void iterationStart(LoopIterationEvent event) {
        if (log.isDebugEnabled()) {
            log.debug(Thread.currentThread().getName() + " iteration start " + isPerIteration());//$NON-NLS-1$
        }
        if (isPerIteration()) {
            setValues();
        }
    }

    /*
     * (non-Javadoc) A new instance is created for each thread group, and the
     * clone() method is then called to create copies for each thread in a
     * thread group. This means that the lock object is common to a thread
     * group; separate thread groups have separate locks. If this is not
     * intended, the lock object could be made static.
     *
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        UserParameters up = (UserParameters) super.clone();
        up.lock = lock; // ensure that clones share the same lock object
        return up;
    }

    /*
     * (non-Javadoc)
     *
     * @see AbstractTestElement#mergeIn(TestElement)
     */
    protected void mergeIn(TestElement element) {
        // super.mergeIn(element);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16013.java