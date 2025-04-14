error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4125.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4125.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4125.java
text:
```scala
l@@ist.setSorter(new ViewerSorter());

/******************************************************************************* 
 * Copyright (c) 2000, 2003 IBM Corporation and others. 
 * All rights reserved. This program and the accompanying materials! 
 * are made available under the terms of the Common Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 * 
 * Contributors: 
 *      IBM Corporation - initial API and implementation 
 *  	Sebastian Davids <sdavids@gmx.de> - Fix for bug 19346 - Dialog font should be
 *      activated and used by other components.
************************************************************************/

package org.eclipse.ui.internal.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.ui.internal.IHelpContextIds;
import org.eclipse.ui.internal.WorkbenchMessages;

/**
 * A dialog for perspective creation
 */
public class SelectPerspectiveDialog extends org.eclipse.jface.dialogs.Dialog
	implements ISelectionChangedListener
{
	private TableViewer list;
	private IPerspectiveRegistry perspReg;
	private IPerspectiveDescriptor perspDesc;
	private Button okButton;

	final private static int LIST_WIDTH = 200;
	final private static int LIST_HEIGHT = 200;
/**
 * PerspectiveDialog constructor comment.
 */
public SelectPerspectiveDialog(Shell parentShell, IPerspectiveRegistry perspReg) {
	super(parentShell);
	this.perspReg = perspReg;
}
/**
 * Notifies that the cancel button of this dialog has been pressed.
 */
protected void cancelPressed() {
	perspDesc = null;
	super.cancelPressed();
}
/* (non-Javadoc)
 * Method declared in Window.
 */
protected void configureShell(Shell shell) {
	super.configureShell(shell);
	shell.setText(WorkbenchMessages.getString("SelectPerspective.shellTitle")); //$NON-NLS-1$
	WorkbenchHelp.setHelp(shell, IHelpContextIds.SELECT_PERSPECTIVE_DIALOG);
}
/**
 * Adds buttons to this dialog's button bar.
 * <p>
 * The default implementation of this framework method adds 
 * standard ok and cancel buttons using the <code>createButton</code>
 * framework method. Subclasses may override.
 * </p>
 *
 * @param parent the button bar composite
 */
protected void createButtonsForButtonBar(Composite parent) {
	okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
}
/**
 * Creates and returns the contents of the upper part 
 * of this dialog (above the button bar).
 *
 * @param the parent composite to contain the dialog area
 * @return the dialog area control
 */
protected Control createDialogArea(Composite parent) {
	// Run super.
	Composite composite = (Composite)super.createDialogArea(parent);
	
	// Add perspective list.
	list = new TableViewer(composite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
	list.getTable().setFont(parent.getFont());
	list.setLabelProvider(new PerspLabelProvider());
	list.setContentProvider(new PerspContentProvider());
	list.setSorter(new ViewerSorter() {});
	list.setInput(perspReg);
	list.addSelectionChangedListener(this);
	list.addDoubleClickListener(new IDoubleClickListener() {
		public void doubleClick(DoubleClickEvent event) {
			handleDoubleClickEvent();
		}
	});

	// Set list layout.
	Control ctrl = list.getControl();
	GridData spec = new GridData(GridData.FILL_BOTH);
	spec.widthHint = LIST_WIDTH;
	spec.heightHint = LIST_HEIGHT;
	ctrl.setLayoutData(spec);
	ctrl.setFont(parent.getFont());

	// Return results.
	return composite;
}
/**
 * Returns the current selection.
 */
public IPerspectiveDescriptor getSelection() {
	return perspDesc;
}
/**
 * Handle a double click event on the list
 */
protected void handleDoubleClickEvent() {
	okPressed();
}
/**
 * Notifies that the selection has changed.
 *
 * @param event event object describing the change
 */
public void selectionChanged(SelectionChangedEvent event) {
	updateSelection();
	updateButtons();
}
/**
 * Update the button enablement state.
 */
protected void updateButtons() {
	okButton.setEnabled(getSelection() != null);	
}
/**
 * Update the selection object.
 */
protected void updateSelection() {
	perspDesc = null;
	IStructuredSelection sel = (IStructuredSelection)list.getSelection();
	if (!sel.isEmpty()) {
		Object obj = sel.getFirstElement();
		if (obj instanceof IPerspectiveDescriptor)
			perspDesc = (IPerspectiveDescriptor)obj;
	}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4125.java