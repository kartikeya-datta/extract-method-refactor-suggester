error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7515.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7515.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7515.java
text:
```scala
t@@heCondition = new JTextField(""); // This means exit if last sample failed

// $Header$
/*
 * Copyright 2003-2004 The Apache Software Foundation.
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.jmeter.control.WhileController;
import org.apache.jmeter.gui.util.FocusRequester;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;

public class WhileControllerGui extends AbstractControllerGui
	implements ActionListener
{

	  private static final String CONDITION_LABEL = "while_controller_label";

	/**
	 * A field allowing the user to specify the condition (not yet used).
	 */
	private JTextField theCondition;

	/** The name of the condition field component. */
	private static final String CONDITION = "While_Condition";

	/**
	 * Create a new LoopControlPanel as a standalone component.
	 */
	public WhileControllerGui()   {
		init();
	}

	/**
	 * A newly created component can be initialized with the contents of
	 * a Test Element object by calling this method.  The component is
	 * responsible for querying the Test Element object for the
	 * relevant information to display in its GUI.
	 *
	 * @param element the TestElement to configure
	 */
	public void configure(TestElement element) {
		super.configure(element);
		if (element instanceof WhileController) {
			theCondition.setText(((WhileController) element).getCondition());
		}

	}

	/**
	 *  Implements JMeterGUIComponent.createTestElement()
	 */
	public TestElement createTestElement() {
		WhileController controller = new WhileController();
		modifyTestElement(controller);
		return controller;
	}

	/**
	 * Implements JMeterGUIComponent.modifyTestElement(TestElement)
	 */
	public void modifyTestElement(TestElement controller) {
		configureTestElement(controller);
		if (controller instanceof WhileController)
		{
			if (theCondition.getText().length() > 0) {
				((WhileController) controller).setCondition(theCondition.getText());
			} else {
				((WhileController) controller).setCondition("");
			}
		}
	}

	/**
	 * Invoked when an action occurs.  This implementation assumes that the
	 * target component is the infinite loops checkbox.
	 *
	 * @param event the event that has occurred
	 */
	public void actionPerformed(ActionEvent event) {
		new FocusRequester(theCondition);
	}

	public String getLabelResource() {
		return "while_controller_title";
	}

	/**
	 * Initialize the GUI components and layout for this component.
	 */
	private void init()
	{
		setLayout(new BorderLayout(0, 5));
		setBorder(makeBorder());
		add(makeTitlePanel(), BorderLayout.NORTH);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(createConditionPanel(), BorderLayout.NORTH);
		add(mainPanel, BorderLayout.CENTER);

	}


	/**
	 * Create a GUI panel containing the condition.
	 * TODO make use of the field
	 * @return a GUI panel containing the condition components
	 */
	private JPanel createConditionPanel()  {
		JPanel conditionPanel = new JPanel(new BorderLayout(5, 0));

		// Condition LABEL
		JLabel conditionLabel =
		new JLabel(JMeterUtils.getResString( CONDITION_LABEL ));
		conditionPanel.add(conditionLabel, BorderLayout.WEST);

		// TEXT FIELD
		theCondition = new JTextField("N/A");
		theCondition.setName(CONDITION);
		conditionLabel.setLabelFor(theCondition);
		conditionPanel.add(theCondition, BorderLayout.CENTER);
		theCondition.addActionListener(this);

		conditionPanel.add(
			Box.createHorizontalStrut( conditionLabel.getPreferredSize().width
			+ theCondition.getPreferredSize().width)
			, BorderLayout.NORTH);

		return conditionPanel;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7515.java