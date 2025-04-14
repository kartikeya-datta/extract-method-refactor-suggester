error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4334.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4334.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4334.java
text:
```scala
m@@TextArea.setDocument(docModel);

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

package org.apache.jorphan.gui;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.Document;

/**
 * A Helper component that wraps a JTextField with a label into a JPanel (this).
 * This component also has an efficient event handling mechanism for handling
 * the text changing in the Text Field. The registered change listeners are only
 * called when the text has changed.
 * 
 * @author S.Coleman
 * @version $Revision$
 */
public class JLabeledTextArea extends JPanel implements JLabeledField, FocusListener {
	private JLabel mLabel;

	private JTextArea mTextArea;

	// Maybe move to vector if MT problems occur
	private ArrayList mChangeListeners = new ArrayList(3);

	// A temporary cache for the focus listener
	private String oldValue = "";

	/**
	 * Default constructor, The label and the Text field are left empty.
	 */
	public JLabeledTextArea() {
		this("", null);
	}

	/**
	 * Constructs a new component with the label displaying the passed text.
	 * 
	 * @param pLabel
	 *            The text to in the label.
	 */
	public JLabeledTextArea(String pLabel, Document docModel) {
		super();
		mLabel = new JLabel(pLabel);
		if (docModel != null) {
			setDocumentModel(docModel);
		}
		init();
	}

	public List getComponentList() {
		List comps = new LinkedList();
		comps.add(mLabel);
		comps.add(mTextArea);
		return comps;
	}

	public void setDocumentModel(Document docModel) {
		mTextArea.setDocument(docModel);
	}

	/**
	 * Initialises all of the components on this panel.
	 */
	private void init() {
		setLayout(new BorderLayout());

		mTextArea = new JTextArea();
		mTextArea.setRows(4);
		mTextArea.setLineWrap(true);
		mTextArea.setWrapStyleWord(true);
		// Register the handler for focus listening. This handler will
		// only notify the registered when the text changes from when
		// the focus is gained to when it is lost.
		mTextArea.addFocusListener(this);

		// Add the sub components
		this.add(mLabel, BorderLayout.NORTH);
		this.add(new JScrollPane(mTextArea), BorderLayout.CENTER);
	}

	/**
	 * Callback method when the focus to the Text Field component is lost.
	 * 
	 * @param pFocusEvent
	 *            The focus event that occured.
	 */
	public void focusLost(FocusEvent pFocusEvent) {
		// Compare if the value has changed, since we received focus.
		if (!oldValue.equals(mTextArea.getText())) {
			notifyChangeListeners();
		}
	}

	/**
	 * Catch what the value was when focus was gained.
	 */
	public void focusGained(FocusEvent pFocusEvent) {
		oldValue = mTextArea.getText();
	}

	/**
	 * Set the text displayed in the label.
	 * 
	 * @param pLabel
	 *            The new label text.
	 */
	public void setLabel(String pLabel) {
		mLabel.setText(pLabel);
	}

	/**
	 * Set the text displayed in the Text Field.
	 * 
	 * @param pText
	 *            The new text to display in the text field.
	 */
	public void setText(String pText) {
		mTextArea.setText(pText);
	}

	/**
	 * Returns the text in the Text Field.
	 * 
	 * @return The text in the Text Field.
	 */
	public String getText() {
		return mTextArea.getText();
	}

	/**
	 * Returns the text of the label.
	 * 
	 * @return The text of the label.
	 */
	public String getLabel() {
		return mLabel.getText();
	}

	public void setEnabled(boolean enable) {
		super.setEnabled(enable);
		mTextArea.setEnabled(enable);
	}

	/**
	 * Adds a change listener, that will be notified when the text in the text
	 * field is changed. The ChangeEvent that will be passed to registered
	 * listeners will contain this object as the source, allowing the new text
	 * to be extracted using the {@link #getText() getText} method.
	 * 
	 * @param pChangeListener
	 *            The listener to add
	 */
	public void addChangeListener(ChangeListener pChangeListener) {
		mChangeListeners.add(pChangeListener);
	}

	/**
	 * Removes a change listener.
	 * 
	 * @param pChangeListener
	 *            The change listener to remove.
	 */
	public void removeChangeListener(ChangeListener pChangeListener) {
		mChangeListeners.remove(pChangeListener);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4334.java