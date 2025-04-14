error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15475.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15475.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15475.java
text:
```scala
s@@etLayout(new VerticalLayout(5, VerticalLayout.BOTH, VerticalLayout.TOP));

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

package org.apache.jmeter.control.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.jmeter.control.ThroughputController;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.layout.VerticalLayout;

public class ThroughputControllerGui extends AbstractControllerGui {
	private JComboBox styleBox;

	private int style;

	private JTextField throughput;

	private JCheckBox perthread;

	private boolean isPerThread = true;

	private String BYNUMBER_LABEL = JMeterUtils.getResString("throughput_control_bynumber_label"); // $NON-NLS-1$

	private String BYPERCENT_LABEL = JMeterUtils.getResString("throughput_control_bypercent_label"); // $NON-NLS-1$

	private String THROUGHPUT_LABEL = JMeterUtils.getResString("throughput_control_tplabel"); // $NON-NLS-1$

	private String THROUGHPUT = "Througput Field"; // $NON-NLS-1$

	private String PERTHREAD_LABEL = JMeterUtils.getResString("throughput_control_perthread_label"); // $NON-NLS-1$

	public ThroughputControllerGui() {
		init();
	}

	public TestElement createTestElement() {
		ThroughputController tc = new ThroughputController();
		modifyTestElement(tc);
		return tc;
	}

	/**
	 * Modifies a given TestElement to mirror the data in the gui components.
	 * 
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
	 */
	public void modifyTestElement(TestElement tc) {
		configureTestElement(tc);
		((ThroughputController) tc).setStyle(style);
		((ThroughputController) tc).setPerThread(isPerThread);
		if (style == ThroughputController.BYNUMBER) {
			try {
				((ThroughputController) tc).setMaxThroughput(Integer.parseInt(throughput.getText().trim()));
			} catch (NumberFormatException e) {
				((ThroughputController) tc).setMaxThroughput(throughput.getText());
			}
		} else {
			try {
				((ThroughputController) tc).setPercentThroughput(Float.parseFloat(throughput.getText().trim()));
			} catch (NumberFormatException e) {
				((ThroughputController) tc).setPercentThroughput(throughput.getText());
			}
		}
	}

    /**
     * Implements JMeterGUIComponent.clear
     */
    public void clear() {
        super.clear();
        styleBox.setSelectedIndex(0);
        throughput.setText("1"); // $NON-NLS-1$
        perthread.setSelected(true);
    }

	public void configure(TestElement el) {
		super.configure(el);
		if (((ThroughputController) el).getStyle() == ThroughputController.BYNUMBER) {
			styleBox.getModel().setSelectedItem(BYNUMBER_LABEL);
			throughput.setText(String.valueOf(((ThroughputController) el).getMaxThroughput()));
		} else {
			styleBox.setSelectedItem(BYPERCENT_LABEL);
			throughput.setText(String.valueOf(((ThroughputController) el).getPercentThroughput()));
		}
		perthread.setSelected(((ThroughputController) el).isPerThread());
	}

	public String getLabelResource() {
		return "throughput_control_title";
	}

	private void init() {
		setLayout(new VerticalLayout(5, VerticalLayout.LEFT, VerticalLayout.TOP));
		setBorder(makeBorder());
		add(makeTitlePanel());

		DefaultComboBoxModel styleModel = new DefaultComboBoxModel();
		styleModel.addElement(BYNUMBER_LABEL);
		styleModel.addElement(BYPERCENT_LABEL);
		styleBox = new JComboBox(styleModel);
		styleBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (((String) styleBox.getSelectedItem()).equals(BYNUMBER_LABEL)) {
					style = ThroughputController.BYNUMBER;
				} else {
					style = ThroughputController.BYPERCENT;
				}
			}
		});
		add(styleBox);

		// TYPE FIELD
		JPanel tpPanel = new JPanel();
		JLabel tpLabel = new JLabel(THROUGHPUT_LABEL);
		tpPanel.add(tpLabel);

		// TEXT FIELD
		throughput = new JTextField(5);
		tpPanel.add(throughput);
		throughput.setName(THROUGHPUT);
		throughput.setText("1"); // $NON-NLS-1$
		// throughput.addActionListener(this);
		tpPanel.add(throughput);
		add(tpPanel);

		// PERTHREAD FIELD
		perthread = new JCheckBox(PERTHREAD_LABEL, isPerThread);
		perthread.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					isPerThread = true;
				} else {
					isPerThread = false;
				}
			}
		});
		add(perthread);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15475.java