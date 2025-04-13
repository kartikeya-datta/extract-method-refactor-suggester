error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6474.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6474.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6474.java
text:
```scala
i@@f (event.keyCode == SWT.F5 && event.stateMask == 0) {

package org.eclipse.ui.actions;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.ui.internal.IHelpContextIds;
import org.eclipse.ui.internal.WorkbenchMessages;

/**
 * Standard action for refreshing the workspace from the local file system for
 * the selected resources and all of their descendents.
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 */
public class RefreshAction extends WorkspaceAction {

	/**
	 * The id of this action.
	 */
	public static final String ID = PlatformUI.PLUGIN_ID + ".RefreshAction";//$NON-NLS-1$
/**
 * Creates a new action.
 *
 * @param shell the shell for any dialogs
 */
public RefreshAction(Shell shell) {
	super(shell, WorkbenchMessages.getString("RefreshAction.text")); //$NON-NLS-1$
	setToolTipText(WorkbenchMessages.getString("RefreshAction.toolTip")); //$NON-NLS-1$
	setId(ID);
	WorkbenchHelp.setHelp(this, IHelpContextIds.REFRESH_ACTION);
}
/**
 * Checks whether the given project's location has been deleted.
 * If so, prompts the user with whether to delete the project or not.
 */
void checkLocationDeleted(IProject project) throws CoreException {
	if (!project.exists())
		return;
	File location = project.getLocation().toFile();
	if (!location.exists()) {
		String message = WorkbenchMessages.format("RefreshAction.locationDeletedMessage", new Object[] {project.getName(),location.getAbsolutePath()});//$NON-NLS-1$
		
		final MessageDialog dialog = new MessageDialog(
			getShell(),
			WorkbenchMessages.getString("RefreshAction.dialogTitle"), // dialog title //$NON-NLS-1$
			null, // use default window icon
			message,
			MessageDialog.QUESTION,
			new String[] {IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL},
			0); // yes is the default

		// Must prompt user in UI thread (we're in the operation thread here).
		getShell().getDisplay().syncExec(new Runnable() {
			public void run() {
				dialog.open();
			}
		});

		// Do the deletion back in the operation thread
		if (dialog.getReturnCode() == 0) { // yes was chosen
			project.delete(true, true, null);
		}
	}
}
/* (non-Javadoc)
 * Method declared on WorkspaceAction.
 */
String getOperationMessage() {
	return WorkbenchMessages.getString("RefreshAction.progressMessage"); //$NON-NLS-1$
}
/* (non-Javadoc)
 * Method declared on WorkspaceAction.
 */
String getProblemsMessage() {
	return WorkbenchMessages.getString("RefreshAction.problemMessage"); //$NON-NLS-1$
}
/* (non-Javadoc)
 * Method declared on WorkspaceAction.
 */
String getProblemsTitle() {
	return WorkbenchMessages.getString("RefreshAction.problemTitle"); //$NON-NLS-1$
}
/**
 * Returns a list containing the workspace root if the selection would otherwise be empty.
 */
protected List getSelectedResources() {
	List resources = super.getSelectedResources();
	if (resources.isEmpty()) {
		resources = new ArrayList();
		resources.add(ResourcesPlugin.getWorkspace().getRoot());
	}
	return resources;
}
/* (non-Javadoc)
 * Method declared on WorkspaceAction.
 */
void invokeOperation(IResource resource, IProgressMonitor monitor) throws CoreException {
	resource.refreshLocal(IResource.DEPTH_INFINITE,monitor);
	// Check if project's location has been deleted, 
	// as per 1G83UCE: ITPUI:WINNT - Refresh from local doesn't detect new or deleted projects
	if (resource.getType() == IResource.PROJECT) {
		checkLocationDeleted((IProject) resource);
	}
}
/**
 * The <code>RefreshAction</code> implementation of this
 * <code>SelectionListenerAction</code> method ensures that this action is
 * enabled if the selection is empty, but is disabled if any of the selected 
 * elements are not resources.
 */
protected boolean updateSelection(IStructuredSelection s) {
	return (super.updateSelection(s) || s.isEmpty()) && getSelectedNonResources().size() == 0;
}

/**
 * Handle the key release.
 */
public void handleKeyReleased(KeyEvent event) {

	if (event.keyCode == SWT.F5) {
		refreshAll();
	}
}

/**
 * Refreshes the entire workspace.
 */
public void refreshAll() {
	IStructuredSelection currentSelection = getStructuredSelection();
	selectionChanged(StructuredSelection.EMPTY);
	run();
	selectionChanged(currentSelection);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6474.java