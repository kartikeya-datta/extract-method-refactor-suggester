error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/447.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/447.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/447.java
text:
```scala
r@@eturn "report_plan";

// $Header$
/*
 * Copyright 2001-2004 The Apache Software Foundation.
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

package org.apache.jmeter.control.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.Collection;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.gui.AbstractJMeterGuiComponent;
import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.ReportPlan;
import org.apache.jmeter.threads.gui.ThreadGroupGui;
import org.apache.jmeter.util.JMeterUtils;

/**
 * JMeter GUI component representing the test plan which will be executed when
 * the test is run.
 * 
 * @version $Revision$ Last Updated: $Date$
 */
public class ReportGui extends AbstractJMeterGuiComponent {

	private JCheckBox serializedMode;

	/** A panel allowing the user to define variables. */
	private ArgumentsPanel argsPanel;

	/** A panel to contain comments on the test plan. */
	private JTextArea commentPanel;

	/**
	 * Create a new TestPlanGui.
	 */
	public ReportGui() {
		init();
	}

	/**
	 * When a user right-clicks on the component in the test tree, or selects
	 * the edit menu when the component is selected, the component will be asked
	 * to return a JPopupMenu that provides all the options available to the
	 * user from this component.
	 * <p>
	 * The TestPlan will return a popup menu allowing you to add ThreadGroups,
	 * Listeners, Configuration Elements, Assertions, PreProcessors,
	 * PostProcessors, and Timers.
	 * 
	 * @return a JPopupMenu appropriate for the component.
	 */
	public JPopupMenu createPopupMenu() {
		JPopupMenu pop = new JPopupMenu();
		JMenu addMenu = new JMenu(JMeterUtils.getResString("Add"));
		addMenu.add(MenuFactory.makeMenuItem(new ThreadGroupGui().getStaticLabel(), ThreadGroupGui.class.getName(),
				"Add"));
		addMenu.add(MenuFactory.makeMenu(MenuFactory.LISTENERS, "Add"));
		addMenu.add(MenuFactory.makeMenu(MenuFactory.CONFIG_ELEMENTS, "Add"));
		pop.add(addMenu);
		MenuFactory.addFileMenu(pop);
		return pop;
	}

	/* Implements JMeterGUIComponent.createTestElement() */
	public TestElement createTestElement() {
		ReportPlan tp = new ReportPlan();
		modifyTestElement(tp);
		return tp;
	}

	/* Implements JMeterGUIComponent.modifyTestElement(TestElement) */
	public void modifyTestElement(TestElement plan) {
		super.configureTestElement(plan);
		if (plan instanceof ReportPlan) {
			ReportPlan tp = (ReportPlan) plan;
			tp.setSerialized(serializedMode.isSelected());
			tp.setUserDefinedVariables((Arguments) argsPanel.createTestElement());
			tp.setProperty(ReportPlan.COMMENTS, commentPanel.getText());
		}
	}

	public String getLabelResource() {
		return "test_plan";
	}

	/**
	 * This is the list of menu categories this gui component will be available
	 * under. This implementation returns null, since the TestPlan appears at
	 * the top level of the tree and cannot be added elsewhere.
	 * 
	 * @return a Collection of Strings, where each element is one of the
	 *         constants defined in MenuFactory
	 */
	public Collection getMenuCategories() {
		return null;
	}

	/**
	 * A newly created component can be initialized with the contents of a Test
	 * Element object by calling this method. The component is responsible for
	 * querying the Test Element object for the relevant information to display
	 * in its GUI.
	 * 
	 * @param el
	 *            the TestElement to configure
	 */
	public void configure(TestElement el) {
		super.configure(el);

		serializedMode.setSelected(((AbstractTestElement) el).getPropertyAsBoolean(ReportPlan.SERIALIZE_THREADGROUPS));

		if (el.getProperty(ReportPlan.USER_DEFINED_VARIABLES) != null) {
			argsPanel.configure((Arguments) el.getProperty(ReportPlan.USER_DEFINED_VARIABLES).getObjectValue());
		}
		commentPanel.setText(el.getPropertyAsString(ReportPlan.COMMENTS));
	}

	/**
	 * Create a panel allowing the user to define variables for the test.
	 * 
	 * @return a panel for user-defined variables
	 */
	private JPanel createVariablePanel() {
		argsPanel = new ArgumentsPanel(JMeterUtils.getResString("user_defined_variables"));

		return argsPanel;
	}

	private Container createCommentPanel() {
		Container panel = makeTitlePanel();
		commentPanel = new JTextArea();
		JLabel label = new JLabel(JMeterUtils.getResString("testplan_comments"));
		label.setLabelFor(commentPanel);
		panel.add(label);
		panel.add(commentPanel);
		return panel;
	}

	/**
	 * Initialize the components and layout of this component.
	 */
	private void init() {
		setLayout(new BorderLayout(10, 10));
		setBorder(makeBorder());

		add(createCommentPanel(), BorderLayout.NORTH);

		add(createVariablePanel(), BorderLayout.CENTER);

		VerticalPanel southPanel = new VerticalPanel();
		serializedMode = new JCheckBox(JMeterUtils.getResString("testplan.serialized"));
		southPanel.add(serializedMode);
		JTextArea explain = new JTextArea(JMeterUtils.getResString("functional_mode_explanation"));
		explain.setEditable(false);
		explain.setBackground(this.getBackground());
		southPanel.add(explain);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/447.java