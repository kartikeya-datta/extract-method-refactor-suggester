error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1201.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1201.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1201.java
text:
```scala
a@@rgsPanel.configure((Arguments) el.getProperty(TestPlan.USER_DEFINED_VARIABLES).getObjectValue());

package org.apache.jmeter.control.gui;
import java.awt.BorderLayout;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.gui.AbstractJMeterGuiComponent;
import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.gui.ThreadGroupGui;
import org.apache.jmeter.util.JMeterUtils;

/****************************************
 * Title: JMeter Description: Copyright: Copyright (c) 2000 Company: Apache
 *
 *@author    Michael Stover
 *@created   $Date$
 *@version   1.0
 ***************************************/

public class TestPlanGui extends AbstractJMeterGuiComponent
{
    JCheckBox functionalMode;
    ArgumentsPanel argsPanel;

    /****************************************
     * !ToDo (Constructor description)
     ***************************************/
    public TestPlanGui()
    {
        init();
    }

    /****************************************
     * !ToDo (Method description)
     *
     *@return   !ToDo (Return description)
     ***************************************/
    public JPopupMenu createPopupMenu()
    {
        JPopupMenu pop = new JPopupMenu();
        JMenu addMenu = new JMenu(JMeterUtils.getResString("Add"));
        addMenu.add(MenuFactory.makeMenuItem(new ThreadGroupGui().getStaticLabel(), ThreadGroupGui.class.getName(), "Add"));
        addMenu.add(MenuFactory.makeMenu(MenuFactory.LISTENERS, "Add"));
        addMenu.add(MenuFactory.makeMenu(MenuFactory.CONFIG_ELEMENTS, "Add"));
        addMenu.add(MenuFactory.makeMenu(MenuFactory.ASSERTIONS, "Add"));
        addMenu.add(MenuFactory.makeMenu(MenuFactory.MODIFIERS, "Add"));
        addMenu.add(MenuFactory.makeMenu(MenuFactory.RESPONSE_BASED_MODIFIERS, "Add"));
        addMenu.add(MenuFactory.makeMenu(MenuFactory.TIMERS, "Add"));
        pop.add(addMenu);
        MenuFactory.addFileMenu(pop);
        return pop;
    }

    /****************************************
     * !ToDo (Method description)
     *
     *@return   !ToDo (Return description)
     ***************************************/
    public TestElement createTestElement()
    {
        TestPlan tp = new TestPlan();
        modifyTestElement(tp);
        return tp;
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    public void modifyTestElement(TestElement plan)
    {
        super.configureTestElement(plan);
        if (plan instanceof TestPlan)
        {
            TestPlan tp = (TestPlan) plan;
            tp.setFunctionalMode(functionalMode.isSelected());
            tp.setUserDefinedVariables((Arguments) argsPanel.createTestElement());
        }
    }

    /****************************************
     * !ToDoo (Method description)
     *
     *@return   !ToDo (Return description)
     ***************************************/
    public String getStaticLabel()
    {
        return JMeterUtils.getResString("Test Plan");
    }

    /****************************************
     * !ToDoo (Method description)
     *
     *@return   !ToDo (Return description)
     ***************************************/
    public Collection getMenuCategories()
    {
        return null;
    }

    /****************************************
     * !ToDo (Method description)
     *
     *@param el  !ToDo (Parameter description)
     ***************************************/
    public void configure(TestElement el)
    {
        super.configure(el);
        functionalMode.setSelected(((AbstractTestElement) el).getPropertyAsBoolean(TestPlan.FUNCTIONAL_MODE));
        if (el.getProperty(TestPlan.USER_DEFINED_VARIABLES) != null)
        {
            argsPanel.configure((Arguments) el.getProperty(TestPlan.USER_DEFINED_VARIABLES));
        }
    }

    /****************************************
     * !ToDoo (Method description)
     *
     *@return   !ToDo (Return description)
     ***************************************/
    protected JPanel getVariablePanel()
    {
        argsPanel = new ArgumentsPanel(JMeterUtils.getResString("user_defined_variables"));

        return argsPanel;
    }

    private void init()
    {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
        this.add(getNamePanel(), BorderLayout.NORTH);
        JPanel southPanel = new JPanel(new BorderLayout());
        functionalMode = new JCheckBox(JMeterUtils.getResString("functional_mode"));
        southPanel.add(functionalMode, BorderLayout.NORTH);
        JTextArea explain = new JTextArea(JMeterUtils.getResString("functional_mode_explanation"));
        explain.setColumns(30);
        explain.setRows(10);
        explain.setBackground(this.getBackground());
        southPanel.add(explain, BorderLayout.CENTER);
        add(getVariablePanel(), BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1201.java