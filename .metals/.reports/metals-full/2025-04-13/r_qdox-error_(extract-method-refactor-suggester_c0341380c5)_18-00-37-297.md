error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3273.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3273.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3273.java
text:
```scala
p@@roject.copy(newDescription, IResource.SHALLOW | IResource.FORCE, monitor);

package org.eclipse.ui.actions;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ProjectLocationSelectionDialog;
import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.IHelpContextIds;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * The CopyProjectAction is the action designed to copy projects specifically
 * as they have different semantics from other resources.
 * Note that this action assumes that a single project is selected and being
 * manipulated. This should be disabled for multi select or no selection.
 */
public class CopyProjectAction extends SelectionListenerAction {
	private static String COPY_TOOL_TIP = WorkbenchMessages.getString("CopyProjectAction.toolTip"); //$NON-NLS-1$
	private static String COPY_TITLE = WorkbenchMessages.getString("CopyProjectAction.title"); //$NON-NLS-1$
	private static String COPY_PROGRESS_TITLE = WorkbenchMessages.getString("CopyProjectAction.progressTitle"); //$NON-NLS-1$
	private static String COPY_PROJECT_FAILED_MESSAGE =
		WorkbenchMessages.getString("CopyProjectAction.copyFailedMessage"); //$NON-NLS-1$
	private static String PROBLEMS_TITLE = WorkbenchMessages.getString("CopyProjectAction.copyFailedTitle"); //$NON-NLS-1$

	/**
	 * The id of this action.
	 */
	public static final String ID = PlatformUI.PLUGIN_ID + ".CopyProjectAction";//$NON-NLS-1$

	/**
	 * The shell in which to show any dialogs.
	 */
	protected Shell shell;

	/**
	 * Status containing the errors detected when running the operation or
	 * <code>null</code> if no errors detected.
	 */
	protected IStatus errorStatus;
/**
 * Creates a new project copy action with the default text.
 *
 * @param shell the shell for any dialogs
 * @param text the string used as the text for the action, 
 *   or <code>null</code> if there is no text
 */
public CopyProjectAction(Shell shell) {
	this(shell,COPY_TITLE);
	WorkbenchHelp.setHelp(this, IHelpContextIds.COPY_PROJECT_ACTION);
}
/**
 * Creates a new project copy action with the given text.
 *
 * @param shell the shell for any dialogs
 * @param text the string used as the text for the action, 
 *   or <code>null</code> if there is no text
 */
CopyProjectAction(Shell shell, String name) {
	super(name);
	setToolTipText(COPY_TOOL_TIP);
	setId(CopyProjectAction.ID);
	Assert.isNotNull(shell);
	this.shell = shell;
}
/**
 * Create a new IProjectDescription for the copy using the name and path selected
 * from the dialog.
 * @return IProjectDescription
 * @param project the source project
 * @param projectName the name for the new project
 * @param rootLocation the path the new project will be stored under.
 */
protected IProjectDescription createDescription(
	IProject project,
	String projectName,
	IPath rootLocation)
	throws CoreException {
	//Get a copy of the current description and modify it
	IProjectDescription newDescription = project.getDescription();
	newDescription.setName(projectName);

	//If the location is the default then set the location to null
	if(rootLocation.equals(Platform.getLocation()))
		newDescription.setLocation(null);
	else
		newDescription.setLocation(rootLocation);
		
	return newDescription;
}
/**
 * Opens an error dialog to display the given message.
 * <p>
 * Note that this method must be called from UI thread.
 * </p>
 *
 * @param message the message
 */
void displayError(String message) {
	MessageDialog.openError(this.shell,getErrorsTitle(), message);
}
/**
 * Return the title of the errors dialog.
 * @return java.lang.String
 */
protected String getErrorsTitle() {
	return PROBLEMS_TITLE;
}
/**
 * Get the plugin used by a copy action
 * @return AbstractUIPlugin
 */
protected org.eclipse.ui.plugin.AbstractUIPlugin getPlugin() {
	return (AbstractUIPlugin) Platform.getPlugin(PlatformUI.PLUGIN_ID);
}
/**
 * Copies the project to the new values.
 *
 * @param project the project to copy
 * @param projectName the name of the copy
 * @param newLocation IPath
 * @return <code>true</code> if the copy operation completed, and 
 *   <code>false</code> if it was abandoned part way
 */
boolean performCopy(
	final IProject project,
	final String projectName,
	final IPath newLocation) {
	WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
		public void execute(IProgressMonitor monitor) {

			monitor.beginTask(COPY_PROGRESS_TITLE, 100);
			try {
				if (monitor.isCanceled())
					throw new OperationCanceledException();

				//Get a copy of the current description and modify it
				IProjectDescription newDescription =
					createDescription(project, projectName, newLocation);
				monitor.worked(50);

				project.copy(newDescription, true, monitor);

				monitor.worked(50);

			} catch (CoreException e) {
				recordError(e); // log error
			} finally {
				monitor.done();
			}
		}
	};

	try {
		new ProgressMonitorDialog(shell).run(true, true, op);
	} catch (InterruptedException e) {
		return false;
	} catch (InvocationTargetException e) {
		displayError(WorkbenchMessages.format("CopyProjectAction.internalError", new Object[] {e.getTargetException().getMessage()})); //$NON-NLS-1$
		return false;
	}

	return true;
}
/**
 * Query for a new project name and destination using the parameters in the existing
 * project.
 * @return Object []  or null if the selection is cancelled
 * @param IProject - the project we are going to copy.
 */
protected Object [] queryDestinationParameters(IProject project) {
	ProjectLocationSelectionDialog dialog =
		new ProjectLocationSelectionDialog(shell, project);
	dialog.setTitle(WorkbenchMessages.getString("CopyProjectAction.copyTitle")); //$NON-NLS-1$
	dialog.open();
	return dialog.getResult();
}
/**
 * Records the core exception to be displayed to the user
 * once the action is finished.
 *
 * @param error a <code>CoreException</code>
 */
final void recordError(CoreException error) {
	this.errorStatus = error.getStatus();
}
/**
 * Implementation of method defined on <code>IAction</code>.
 */
public void run() {

	errorStatus = null;
	
	IProject project = (IProject) getSelectedResources().get(0);

	//Get the project name and location in a two element list
	Object[] destinationPaths = queryDestinationParameters(project);
	if (destinationPaths == null)
		return;

	String newName = (String) destinationPaths[0];
	IPath newLocation = new Path((String) destinationPaths[1]);

	boolean completed = performCopy(project, newName, newLocation);

	if (!completed) // ie.- canceled
		return; // not appropriate to show errors

	// If errors occurred, open an Error dialog
	if (errorStatus != null) {
		ErrorDialog.openError(this.shell, getErrorsTitle(), null, errorStatus);
		errorStatus = null; 
	}
}
/**
 * The <code>CopyResourceAction</code> implementation of this
 * <code>SelectionListenerAction</code> method enables this action only if 
 * there is a single selection which is a project.
 */
protected boolean updateSelection(IStructuredSelection selection) {
	if (!super.updateSelection(selection)) {
		return false;
	}
	if (getSelectedNonResources().size() > 0) {
		return false;
	}

	// to enable this command there must be one project selected and nothing else
	List selectedResources = getSelectedResources();
	if (selectedResources.size() != 1)
		return false;
	IResource source = (IResource)selectedResources.get(0);
	if (source instanceof IProject && ((IProject)source).isOpen())
		return true;
	return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3273.java