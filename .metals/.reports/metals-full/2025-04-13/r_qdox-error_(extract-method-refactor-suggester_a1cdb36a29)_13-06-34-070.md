error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/423.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/423.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/423.java
text:
```scala
private I@@nteger lock = new Integer(0);

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
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;

/**
 * @author Administrator
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class UserParameters extends AbstractTestElement implements Serializable, PreProcessor, LoopIterationListener
{

    public static final String NAMES = "UserParameters.names";
    public static final String THREAD_VALUES = "UserParameters.thread_values";
    public static final String PER_ITERATION = "UserParameters.per_iteration";
    private int counter = 0;
    transient private Object lock = new Object();

    public CollectionProperty getNames()
    {
        return (CollectionProperty) getProperty(NAMES);
    }

    public CollectionProperty getThreadLists()
    {
        return (CollectionProperty) getProperty(THREAD_VALUES);
    }

    /**
     * The list of names of the variables to hold values.  This list must come in
     * the same order as the sub lists that are given to setThreadLists(List).
     */
    public void setNames(Collection list)
    {
        setProperty(new CollectionProperty(NAMES, list));
    }

    /**
         * The list of names of the variables to hold values.  This list must come in
         * the same order as the sub lists that are given to setThreadLists(List).
         */
    public void setNames(CollectionProperty list)
    {
        setProperty(list);
    }

    /**
     * The thread list is a list of lists.  Each list within the parent list is a
     * collection of values for a simulated user.  As many different sets of 
     * values can be supplied in this fashion to cause JMeter to set different 
     * values to variables for different test threads.
     */
    public void setThreadLists(Collection threadLists)
    {
        setProperty(new CollectionProperty(THREAD_VALUES, threadLists));
    }

    /**
         * The thread list is a list of lists.  Each list within the parent list is a
         * collection of values for a simulated user.  As many different sets of 
         * values can be supplied in this fashion to cause JMeter to set different 
         * values to variables for different test threads.
         */
    public void setThreadLists(CollectionProperty threadLists)
    {
        setProperty(threadLists);
    }

    private CollectionProperty getValues()
    {
        CollectionProperty threadValues = (CollectionProperty) getProperty(THREAD_VALUES);
        if (threadValues.size() > 0)
        {
            return (CollectionProperty) threadValues.get(JMeterContextService.getContext().getThreadNum() % threadValues.size());
        }
        else
        {
            return new CollectionProperty("noname", new LinkedList());
        }
    }

    public boolean isPerIteration()
    {
        return getPropertyAsBoolean(PER_ITERATION);
    }

    public void setPerIteration(boolean perIter)
    {
        setProperty(new BooleanProperty(PER_ITERATION, perIter));
    }

    public void process()
    {
        if (!isPerIteration())
        {
            setValues();
        }
    }

    private void setValues()
    {
        synchronized (lock)
        {
            log.debug("Running up named: " + getName());
            PropertyIterator namesIter = getNames().iterator();
            PropertyIterator valueIter = getValues().iterator();
            JMeterVariables jmvars = JMeterContextService.getContext().getVariables();
            while (namesIter.hasNext() && valueIter.hasNext())
            {
                String name = namesIter.next().getStringValue();
                String value = valueIter.next().getStringValue();
                log.debug("saving variable: " + name + "=" + value);
                jmvars.put(name, value);
            }
        }
    }

    /**
     * @see org.apache.jmeter.engine.event.LoopIterationListener#iterationStart(LoopIterationEvent)
     */
    public void iterationStart(LoopIterationEvent event)
    {
        if (isPerIteration())
        {
            setValues();
        }
    }

    /* This method doesn't appear to be used anymore.
     * jeremy_a@bigfoot.com  03 May 2003
     * 
     * @see org.apache.jmeter.testelement.ThreadListener#setJMeterVariables(org.apache.jmeter.threads.JMeterVariables)
    public void setJMeterVariables(JMeterVariables jmVars)
    {}
     */

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    public Object clone()
    {
        UserParameters up = (UserParameters) super.clone();
        up.lock = lock;
        return up;
    }

    /* (non-Javadoc)
     * @see org.apache.jmeter.testelement.AbstractTestElement#mergeIn(org.apache.jmeter.testelement.TestElement)
     */
    protected void mergeIn(TestElement element)
    {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/423.java