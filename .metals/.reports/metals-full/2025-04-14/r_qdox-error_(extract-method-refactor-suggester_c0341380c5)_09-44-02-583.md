error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5723.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5723.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5723.java
text:
```scala
private S@@tring message = ""; //$NON-NLS-1$

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.internal.WorkbenchMessages;
import java.util.List;
import java.util.ArrayList;

/**
 * The abstract implementation of a selection dialog. It can be primed with
 * initial selections (<code>setInitialSelections</code>), and returns
 * the final selection (via <code>getResult</code>) after completion.
 * <p>
 * Clients may subclass this dialog to inherit its selection facilities.
 * </p>
 */
public abstract class SelectionDialog extends Dialog {
	// the final collection of selected elements, or null if this dialog was canceled
	private Object[] result;

	// a collection of the initially-selected elements
	private List initialSelections = new ArrayList();

	// title of dialog
	private String title;
	
	// message to show user
	private String message;

	static String SELECT_ALL_TITLE = WorkbenchMessages.getString("SelectionDialog.selectLabel"); //$NON-NLS-1$
	static String DESELECT_ALL_TITLE = WorkbenchMessages.getString("SelectionDialog.deselectLabel"); //$NON-NLS-1$
/**
 * Creates a dialog instance.
 * Note that the dialog will have no visual representation (no widgets)
 * until it is told to open.
 *
 * @param parentShell the parent shell
 */
protected SelectionDialog(Shell parentShell) {
	super(parentShell);
}
/* (non-Javadoc)
 * Method declared in Window.
 */
protected void configureShell(Shell shell) {
	super.configureShell(shell);
	if (title != null)
		shell.setText(title);
}
/* (non-Javadoc)
 * Method declared on Dialog.
 */
protected void createButtonsForButtonBar(Composite parent) {
	createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
}
/**
 * Creates the message area for this dialog.
 * <p>
 * This method is provided to allow subclasses to decide where the message
 * will appear on the screen.
 * </p>
 *
 * @param parent the parent composite
 * @return the message label
 */
protected Label createMessageArea(Composite composite) {
	Label label = new Label(composite,SWT.NONE);
	if (message != null) {
		label.setText(message);
	} 
	label.setFont(composite.getFont());
	return label;
}
/**
 * Returns the initial selection in this selection dialog.
 *
 * @deprecated use getInitialElementSelections() instead
 * @return the list of initial selected elements or null 
 */
protected List getInitialSelections() {
	if(initialSelections.isEmpty())
		return null;
	else
		return getInitialElementSelections();
}

/**
 * Returns the list of initial element selections.
 * @return List
 */
protected List getInitialElementSelections(){
	return initialSelections;
}

/**
 * Returns the message for this dialog.
 *
 * @return the message for this dialog
 */
protected String getMessage() {
	return message;
}
/**
 * Returns the ok button.
 *
 * @return the ok button or <code>null</code> if the button is not created
 *  yet.
 */
public Button getOkButton() {
	return getButton(IDialogConstants.OK_ID);
}
/**
 * Returns the list of selections made by the user, or <code>null</code> if
 * the selection was canceled.
 *
 * @return the array of selected elements, or <code>null</code> if Cancel was
 *   pressed
 */
public Object[] getResult() {
	return result;
}
/**
 * Sets the initial selection in this selection dialog to the given elements.
 *
 * @param selectedElements the array of elements to select
 */
public void setInitialSelections(Object[] selectedElements) {
	initialSelections = new ArrayList(selectedElements.length);
	for (int i = 0; i < selectedElements.length; i++) 
		initialSelections.add(selectedElements[i]);
}

/**
 * Sets the initial selection in this selection dialog to the given elements.
 *
 * @param selectedElements the List of elements to select
 */
public void setInitialElementSelections(List selectedElements) {
	initialSelections = selectedElements;
}

/**
 * Sets the message for this dialog.
 *
 * @param message the message
 */
public void setMessage(String message) {
	this.message = message;
}
/**
 * Set the selections made by the user, or <code>null</code> if
 * the selection was canceled.
 *
 * @param the list of selected elements, or <code>null</code> if Cancel was
 *   pressed
 */
protected void setResult(List newResult) {
	if (newResult == null) {
		result = null;
	} else {
		result = new Object[newResult.size()];
		newResult.toArray(result);
	}
}

/**
 * Set the selections made by the user, or <code>null</code> if
 * the selection was canceled.
 * <p>
 * The selections may accessed using <code>getResult</code>.
 * </p>
 *
 * @param Object[] newResult - the new values
 * @since 2.0
 */
protected void setSelectionResult(Object[] newResult) {
	result = newResult;
}

/**
 * Sets the title for this dialog.
 *
 * @param title the title
 */
public void setTitle(String title) {
	this.title = title;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5723.java