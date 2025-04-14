error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6352.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6352.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6352.java
text:
```scala
public v@@oid clearGui() {

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

package org.apache.jmeter.gui;

import java.awt.BorderLayout;
import java.util.Collection;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.TreeNode;

import org.apache.jmeter.gui.tree.NamedTreeNode;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.WorkBench;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.util.LocaleChangeEvent;

public class NamePanel extends JPanel implements JMeterGUIComponent {
	/** A text field containing the name. */
	private JTextField nameField = new JTextField(15);

	/** The label for the text field. */
	private JLabel nameLabel;

	/** The node which this component is providing the name for. */
	private TreeNode node;

	/**
	 * Create a new NamePanel with the default name.
	 */
	public NamePanel() {
		setName(getStaticLabel());
		init();
	}

	/**
	 * Initialize the GUI components and layout.
	 */
	private void init() {
		setLayout(new BorderLayout(5, 0));

		nameLabel = new JLabel(JMeterUtils.getResString("name")); // $NON-NLS-1$
		nameLabel.setName("name");
		nameLabel.setLabelFor(nameField);

		nameField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				updateName(nameField.getText());
			}

			public void removeUpdate(DocumentEvent e) {
				updateName(nameField.getText());
			}

			public void changedUpdate(DocumentEvent e) {
				// not for text fields
			}
		});

		add(nameLabel, BorderLayout.WEST);
		add(nameField, BorderLayout.CENTER);
	}

	public void clear() {
		setName(getStaticLabel());
	}

	/**
	 * Get the currently displayed name.
	 * 
	 * @return the current name
	 */
	public String getName() {
		if (nameField != null)
			return nameField.getText();
		else
			return ""; // $NON-NLS-1$
	}

	/**
	 * Set the name displayed in this component.
	 * 
	 * @param name
	 *            the name to display
	 */
	public void setName(String name) {
		super.setName(name);
		nameField.setText(name);
	}

	/**
	 * Get the tree node which this component provides the name for.
	 * 
	 * @return the tree node corresponding to this component
	 */
	protected TreeNode getNode() {
		return node;
	}

	/**
	 * Set the tree node which this component provides the name for.
	 * 
	 * @param node
	 *            the tree node corresponding to this component
	 */
	public void setNode(TreeNode node) {
		this.node = node;
	}

	/* Implements JMeterGUIComponent.configure(TestElement) */
	public void configure(TestElement testElement) {
		setName(testElement.getPropertyAsString(TestElement.NAME));
	}

	/* Implements JMeterGUIComponent.createPopupMenu() */
	public JPopupMenu createPopupMenu() {
		return null;
	}

	/* Implements JMeterGUIComponent.getStaticLabel() */
	public String getStaticLabel() {
		return JMeterUtils.getResString(getLabelResource());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#getLabelResource()
	 */
	public String getLabelResource() {
		return "root"; // $NON-NLS-1$
	}

	/* Implements JMeterGUIComponent.getMenuCategories() */
	public Collection getMenuCategories() {
		return null;
	}

	/* Implements JMeterGUIComponent.createTestElement() */
	public TestElement createTestElement() {
		WorkBench wb = new WorkBench();
		modifyTestElement(wb);
		return wb;
	}

	/* Implements JMeterGUIComponent.modifyTestElement(TestElement) */
	public void modifyTestElement(TestElement wb) {
		wb.setProperty(new StringProperty(TestElement.NAME, getName()));
		wb.setProperty(new StringProperty(TestElement.GUI_CLASS, this.getClass().getName()));
		wb.setProperty(new StringProperty(TestElement.TEST_CLASS, WorkBench.class.getName()));
	}

	/**
	 * Called when the name changes. The tree node which this component names
	 * will be notified of the change.
	 * 
	 * @param newValue
	 *            the new name
	 */
	private void updateName(String newValue) {
		if (getNode() != null) {
            ((NamedTreeNode)getNode()).nameChanged();
		}
	}

	/**
	 * Called when the locale is changed so that the label can be updated. This
	 * method is not currently used.
	 * 
	 * @param event
	 *            the event to be handled
	 */
	public void localeChanged(LocaleChangeEvent event) {
		nameLabel.setText(JMeterUtils.getResString(nameLabel.getName()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#getDocAnchor()
	 */
	public String getDocAnchor() {
		return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6352.java