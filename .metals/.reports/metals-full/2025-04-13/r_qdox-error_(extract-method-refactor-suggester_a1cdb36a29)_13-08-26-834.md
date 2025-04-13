error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16357.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16357.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16357.java
text:
```scala
i@@f (tg instanceof ThreadGroup && !isRunningVersion())

package org.apache.jmeter.testelement;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.ConfigElement;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.JMeterUtils;

/****************************************
 * Title: JMeter Description: Copyright: Copyright (c) 2000 Company: Apache
 *
 *@author    Michael Stover
 *@created   March 13, 2001
 *@version   1.0
 ***************************************/

public class TestPlan extends AbstractTestElement implements Serializable
{
    /****************************************
     * !ToDo (Field description)
     ***************************************/
    public final static String THREAD_GROUPS = "TestPlan.thread_groups";
    public final static String FUNCTIONAL_MODE = "TestPlan.functional_mode";
    public final static String USER_DEFINED_VARIABLES = "TestPlan.user_defined_variables";

    private List threadGroups = new LinkedList();
    private List configs = new LinkedList();
    private static List itemsCanAdd = new LinkedList();
    private static TestPlan plan;
    private Map userDefinedVariables = new HashMap();

    static {
        // WARNING! This String value must be identical to the String value returned
        // in org.apache.jmeter.threads.ThreadGroup.getClassLabel() method.
        // If it's not you will not be able to add a Thread Group element to a Test Plan.
        itemsCanAdd.add(JMeterUtils.getResString("threadgroup"));
    }

    /****************************************
     * !ToDo (Constructor description)
     ***************************************/
    public TestPlan()
    {
        this("Test Plan");
        setFunctionalMode(false);
    }

    public boolean isFunctionalMode()
    {
        return getPropertyAsBoolean(FUNCTIONAL_MODE);
    }

    public void setUserDefinedVariables(Arguments vars)
    {
        setProperty(USER_DEFINED_VARIABLES, vars);
    }

    public Map getUserDefinedVariables()
    {
        Arguments args = getVariables();
        return args.getArgumentsAsMap();
    }

    private Arguments getVariables()
    {
        Arguments args = (Arguments) getProperty(USER_DEFINED_VARIABLES);
        if (args == null)
        {
            args = new Arguments();
            setUserDefinedVariables(args);
        }
        return args;
    }

    public void setFunctionalMode(boolean funcMode)
    {
        setProperty(FUNCTIONAL_MODE, new Boolean(funcMode));
    }

    /****************************************
     * !ToDo (Constructor description)
     *
     *@param name  !ToDo (Parameter description)
     ***************************************/
    public TestPlan(String name)
    {
        setName(name);
        setProperty(THREAD_GROUPS, threadGroups);
    }

    public void addParameter(String name, String value)
    {
        getVariables().addArgument(name, value);
    }

    /****************************************
     * Description of the Method
     *
     *@param name  Description of Parameter
     *@return      Description of the Returned Value
     ***************************************/
    public static TestPlan createTestPlan(String name)
    {
        if (plan == null)
        {
            if (name == null)
            {
                plan = new TestPlan();
            }
            else
            {
                plan = new TestPlan(name);
            }
            plan.setProperty(TestElement.GUI_CLASS, "org.apache.jmeter.control.gui.TestPlanGui");
        }
        return plan;
    }

    /****************************************
     * !ToDo
     *
     *@param tg  !ToDo
     ***************************************/
    public void addTestElement(TestElement tg)
    {
        super.addTestElement(tg);
        if (tg instanceof ThreadGroup)
        {
            addThreadGroup((ThreadGroup) tg);
        }
    }

    /****************************************
     * !ToDo
     *
     *@param child  !ToDo
     ***************************************/
    public void addJMeterComponent(TestElement child)
    {
        if (child instanceof ThreadGroup)
        {
            addThreadGroup((ThreadGroup) child);
        }
    }

    /****************************************
     * Gets the ThreadGroups attribute of the TestPlan object
     *
     *@return   The ThreadGroups value
     ***************************************/
    public Collection getThreadGroups()
    {
        return threadGroups;
    }

    /****************************************
     * Adds a feature to the ConfigElement attribute of the TestPlan object
     *
     *@param c  The feature to be added to the ConfigElement attribute
     ***************************************/
    public void addConfigElement(ConfigElement c)
    {
        configs.add(c);
    }

    /****************************************
     * Adds a feature to the ThreadGroup attribute of the TestPlan object
     *
     *@param group  The feature to be added to the ThreadGroup attribute
     ***************************************/
    public void addThreadGroup(ThreadGroup group)
    {
        threadGroups.add(group);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16357.java