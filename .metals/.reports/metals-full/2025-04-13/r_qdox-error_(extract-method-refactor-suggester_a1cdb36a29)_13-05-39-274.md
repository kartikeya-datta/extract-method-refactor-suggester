error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4333.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4333.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4333.java
text:
```scala
private v@@oid init(String[] items, String selected) {

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

package org.apache.jorphan.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author pete
 * 
 * JLabeledRadio will create a set of Radio buttons with a label.
 */
public class JLabeledRadio extends JPanel implements JLabeledField, ActionListener {

	private JLabel mLabel = new JLabel();

	private ButtonGroup bGroup = new ButtonGroup();

	private ArrayList mChangeListeners = new ArrayList(3);

	/**
	 * 
	 */
	public JLabeledRadio() {
		super();
		this.add(mLabel);
	}

	public JLabeledRadio(String label, String[] items) {
		mLabel.setText(label);
		init(items, null);
	}

	public JLabeledRadio(String label, String[] items, String selectedItem) {
		mLabel.setText(label);
		init(items, selectedItem);
	}

	/**
	 * Method is responsible for creating the JRadioButtons and adding them to
	 * the ButtonGroup.
	 * 
	 * @param items
	 */
	public void init(String[] items, String selected) {
		this.add(mLabel);
		for (int idx = 0; idx < items.length; idx++) {
			JRadioButton btn = new JRadioButton(items[idx]);
			btn.setActionCommand(items[idx]);
			btn.addActionListener(this);
			// add the button to the button group
			this.bGroup.add(btn);
			// add the button
			this.add(btn);
			if (selected != null && selected.equals(items[idx])) {
				btn.setSelected(true);
			}
		}
	}

	/**
	 * setItems will set the radio button items. The implementation first
	 * removes the old JRadioButton, then it creates new ones.
	 * 
	 * @param items
	 */
	public void setItems(String[] items) {
		Enumeration en = this.bGroup.getElements();
		while (en.hasMoreElements()) {
			JComponent comp = (JComponent) en.nextElement();
			this.bGroup.remove((JRadioButton) comp);
			this.remove(comp);
		}
		init(items, null);
	}

	/**
	 * The implementation will get the Text value from the selected radio button
	 * in the JButtonGroup.
	 */
	public String getText() {
		return this.bGroup.getSelection().getActionCommand();
	}

	/**
	 * The implementation will iterate through the radio buttons and find the
	 * match. It then sets it to selected and sets all other radion buttons as
	 * not selected.
	 */
	public void setText(String text) {
		Enumeration en = this.bGroup.getElements();
		while (en.hasMoreElements()) {
			JRadioButton jrb = (JRadioButton) en.nextElement();
			if (jrb.getText().equals(text)) {
				this.bGroup.setSelected(jrb.getModel(), true);
			} else {
				this.bGroup.setSelected(jrb.getModel(), false);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jorphan.gui.JLabeledField#setLabel(java.lang.String)
	 */
	public void setLabel(String pLabel) {
		this.mLabel.setText(pLabel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jorphan.gui.JLabeledField#addChangeListener(javax.swing.event.ChangeListener)
	 */
	public void addChangeListener(ChangeListener pChangeListener) {
		this.mChangeListeners.add(pChangeListener);
	}

	/**
	 * Notify all registered change listeners that the text in the text field
	 * has changed.
	 */
	private void notifyChangeListeners() {
		ChangeEvent ce = new ChangeEvent(this);
		for (int index = 0; index < mChangeListeners.size(); index++) {
			((ChangeListener) mChangeListeners.get(index)).stateChanged(ce);
		}
	}

	/**
	 * Method will return all the label and JRadioButtons. ButtonGroup is
	 * excluded from the list.
	 */
	public List getComponentList() {
		List comps = new LinkedList();
		comps.add(mLabel);
		Enumeration en = this.bGroup.getElements();
		while (en.hasMoreElements()) {
			comps.add(en.nextElement());
		}
		return comps;
	}

	/**
	 * When a radio button is clicked, an ActionEvent is triggered.
	 */
	public void actionPerformed(ActionEvent e) {
		this.notifyChangeListeners();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4333.java