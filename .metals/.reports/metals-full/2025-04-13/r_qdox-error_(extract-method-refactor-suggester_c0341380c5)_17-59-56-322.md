error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4430.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4430.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4430.java
text:
```scala
d@@urationPanel.add(new JLabel(durationLabel));

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

package org.apache.jmeter.sampler.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.sampler.TestAction;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.layout.VerticalLayout;

/**
 * @version $Revision$
 */
public class TestActionGui extends AbstractSamplerGui {
	// Gui components
	private JComboBox targetBox;

	// private ButtonGroup actionButtons;
	private JRadioButton pauseButton;

	private JRadioButton stopButton;

	private JTextField durationField;

	// State variables
	private int target;

	private int action;

	private int duration;

	// String in the panel
	private static final String targetLabel = JMeterUtils.getResString("test_action_target");

	private static final String threadTarget = JMeterUtils.getResString("test_action_target_thread");

	private static final String testTarget = JMeterUtils.getResString("test_action_target_test");

	private static final String actionLabel = JMeterUtils.getResString("test_action_action");

	private static final String pauseAction = JMeterUtils.getResString("test_action_pause");

	private static final String stopAction = JMeterUtils.getResString("test_action_stop");

	private static final String durationLabel = JMeterUtils.getResString("test_action_duration");

	public TestActionGui() {
		super();
		target = TestAction.THREAD;
		action = TestAction.PAUSE;
		init();
	}

	public String getLabelResource() {
		return "test_action_title";
	}

	public void configure(TestElement element) {
		super.configure(element);
		TestAction ta = (TestAction) element;

		target = ta.getTarget();
		if (target == TestAction.THREAD)
			targetBox.setSelectedItem(threadTarget);
		else
			targetBox.setSelectedItem(testTarget);

		action = ta.getAction();
		if (action == TestAction.PAUSE)
			pauseButton.setSelected(true);
		else
			stopButton.setSelected(true);

		duration = ta.getDuration();
		durationField.setText(Integer.toString(duration));
	}

	/**
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#createTestElement()
	 */
	public TestElement createTestElement() {
		TestAction ta = new TestAction();
		modifyTestElement(ta);
		return ta;
	}

	/**
	 * Modifies a given TestElement to mirror the data in the gui components.
	 * 
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
	 */
	public void modifyTestElement(TestElement element) {
		super.configureTestElement(element);
		TestAction ta = (TestAction) element;
		ta.setAction(action);
		ta.setTarget(target);
		ta.setDuration(duration);
	}

	private void init() {
		setLayout(new VerticalLayout(5, VerticalLayout.LEFT, VerticalLayout.TOP));
		setBorder(makeBorder());
		add(makeTitlePanel());

		// Target
		HorizontalPanel targetPanel = new HorizontalPanel();
		targetPanel.add(new JLabel(targetLabel));
		DefaultComboBoxModel targetModel = new DefaultComboBoxModel();
		targetModel.addElement(threadTarget);
		targetModel.addElement(testTarget);
		targetBox = new JComboBox(targetModel);
		targetBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (((String) targetBox.getSelectedItem()).equals(threadTarget)) {
					target = TestAction.THREAD;
				} else {
					target = TestAction.TEST;
				}
			}
		});
		targetPanel.add(targetBox);
		add(targetPanel);

		// Action
		HorizontalPanel actionPanel = new HorizontalPanel();
		ButtonGroup actionButtons = new ButtonGroup();
		pauseButton = new JRadioButton(pauseAction, true);
		pauseButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (pauseButton.isSelected()) {
					action = TestAction.PAUSE;
					durationField.setEnabled(true);
				}

			}
		});
		stopButton = new JRadioButton(stopAction, false);
		stopButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (stopButton.isSelected()) {
					action = TestAction.STOP;
					durationField.setEnabled(false);
				}
			}
		});
		actionButtons.add(pauseButton);
		actionButtons.add(stopButton);
		actionPanel.add(new JLabel(actionLabel));
		actionPanel.add(pauseButton);
		actionPanel.add(stopButton);
		add(actionPanel);

		// Duration
		HorizontalPanel durationPanel = new HorizontalPanel();
		durationField = new JTextField(5);
		durationField.setText(Integer.toString(duration));
		durationField.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				try {
					duration = Integer.parseInt(durationField.getText());
				} catch (NumberFormatException nfe) {
					duration = 0;
					// alert
					// durationField.grabFocus();
				}
			}

			public void focusGained(FocusEvent e) {
			}
		});
		durationPanel.add(new JLabel("Duration"));
		durationPanel.add(durationField);
		add(durationPanel);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4430.java