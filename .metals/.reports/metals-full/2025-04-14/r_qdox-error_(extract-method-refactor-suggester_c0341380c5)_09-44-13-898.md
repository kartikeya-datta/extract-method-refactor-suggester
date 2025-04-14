error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8843.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8843.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8843.java
text:
```scala
t@@reeViewer.setSorter(new ViewerSorter());

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
package org.eclipse.ui.internal.misc;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.DrillDownComposite;

/**
 * Workbench-level composite for choosing a container.
 */
public class ContainerSelectionGroup extends Composite {
	// The listener to notify of events
	private Listener listener;

	// Enable user to type in new container name
	private boolean allowNewContainerName = true;

	// show all projects by default
	private boolean showClosedProjects = true;
	
	// Last selection made by user
	private IContainer selectedContainer;
	
	// handle on parts
	private Text containerNameField;
	TreeViewer treeViewer;

	// the message to display at the top of this dialog
	private static final String DEFAULT_MSG_NEW_ALLOWED = WorkbenchMessages.getString("ContainerGroup.message"); //$NON-NLS-1$
	private static final String DEFAULT_MSG_SELECT_ONLY = WorkbenchMessages.getString("ContainerGroup.selectFolder"); //$NON-NLS-1$

	// sizing constants
	private static final int SIZING_SELECTION_PANE_WIDTH = 320;
	private static final int SIZING_SELECTION_PANE_HEIGHT = 300;
/**
 * Creates a new instance of the widget.
 *
 * @param parent The parent widget of the group.
 * @param listener A listener to forward events to. Can be null if
 *	 no listener is required.
 * @param allowNewContainerName Enable the user to type in a new container
 *  name instead of just selecting from the existing ones.
 */
public ContainerSelectionGroup (Composite parent, Listener listener, boolean allowNewContainerName) {
	this(parent, listener, allowNewContainerName, null);
}
/**
 * Creates a new instance of the widget.
 *
 * @param parent The parent widget of the group.
 * @param listener A listener to forward events to.  Can be null if
 *	 no listener is required.
 * @param allowNewContainerName Enable the user to type in a new container
 *  name instead of just selecting from the existing ones.
 * @param message The text to present to the user.
 */
public ContainerSelectionGroup (Composite parent, Listener listener, boolean allowNewContainerName, String message) {
	this(parent, listener, allowNewContainerName, message, true);
}
/**
 * Creates a new instance of the widget.
 *
 * @param parent The parent widget of the group.
 * @param listener A listener to forward events to.  Can be null if
 *	 no listener is required.
 * @param allowNewContainerName Enable the user to type in a new container
 *  name instead of just selecting from the existing ones.
 * @param message The text to present to the user.
 * @param showClosedProjects Whether or not to show closed projects.
 */
public ContainerSelectionGroup (Composite parent, Listener listener, boolean allowNewContainerName, String message, boolean showClosedProjects) {
	this(parent, listener, allowNewContainerName, message, showClosedProjects, SIZING_SELECTION_PANE_HEIGHT);
}
/**
 * Creates a new instance of the widget.
 *
 * @param parent The parent widget of the group.
 * @param listener A listener to forward events to.  Can be null if
 *	 no listener is required.
 * @param allowNewContainerName Enable the user to type in a new container
 *  name instead of just selecting from the existing ones.
 * @param message The text to present to the user.
 * @param showClosedProjects Whether or not to show closed projects.
 * @param heightHint height hint for the drill down composite
 */
public ContainerSelectionGroup (Composite parent, Listener listener, boolean allowNewContainerName, String message, boolean showClosedProjects, int heightHint) {
	super (parent, SWT.NONE);
	this.listener = listener;
	this.allowNewContainerName = allowNewContainerName;
	this.showClosedProjects = showClosedProjects;
	if (message != null)
		createContents(message, heightHint);
	else if (allowNewContainerName)
		createContents(DEFAULT_MSG_NEW_ALLOWED, heightHint);
	else
		createContents(DEFAULT_MSG_SELECT_ONLY, heightHint);
}
/**
 * The container selection has changed in the
 * tree view. Update the container name field
 * value and notify all listeners.
 */
public void containerSelectionChanged(IContainer container) {
	selectedContainer = container;
	
	if (allowNewContainerName) {
		if (container == null)
			containerNameField.setText("");//$NON-NLS-1$
		else
			containerNameField.setText(container.getFullPath().makeRelative().toString());
	}

	// fire an event so the parent can update its controls
	if (listener != null) {
		Event changeEvent = new Event();
		changeEvent.type = SWT.Selection;
		changeEvent.widget = this;
		listener.handleEvent(changeEvent);
	}
}
/**
 * Creates the contents of the composite.
 */
public void createContents(String message) {
	createContents(message, SIZING_SELECTION_PANE_HEIGHT);
}
/**
 * Creates the contents of the composite.
 * 
 * @param heightHint height hint for the drill down composite
 */
public void createContents(String message, int heightHint) {
	GridLayout layout = new GridLayout();
	layout.marginWidth = 0;
	setLayout(layout);
	setLayoutData(new GridData(GridData.FILL_BOTH));

	Label label = new Label(this,SWT.WRAP);
	label.setText(message);
	label.setFont(this.getFont());

	if (allowNewContainerName) {
		containerNameField = new Text(this, SWT.SINGLE | SWT.BORDER);
		containerNameField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		containerNameField.addListener(SWT.Modify, listener);
		containerNameField.setFont(this.getFont());
	}
	else {
		// filler...
		new Label(this, SWT.NONE);
	}

	createTreeViewer(heightHint);
	Dialog.applyDialogFont(this);
}
/**
 * Returns a new drill down viewer for this dialog.
 *
 * @param heightHint height hint for the drill down composite
 * @return a new drill down viewer
 */
protected void createTreeViewer(int heightHint) {
	// Create drill down.
	DrillDownComposite drillDown = new DrillDownComposite(this, SWT.BORDER);
	GridData spec = new GridData(
		GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL |
		GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
	spec.widthHint = SIZING_SELECTION_PANE_WIDTH;
	spec.heightHint = heightHint;
	drillDown.setLayoutData(spec);

	// Create tree viewer inside drill down.
	treeViewer = new TreeViewer(drillDown, SWT.NONE);
	drillDown.setChildTree(treeViewer);
	ContainerContentProvider cp = new ContainerContentProvider();
	cp.showClosedProjects(showClosedProjects);
	treeViewer.setContentProvider(cp);
	treeViewer.setLabelProvider(WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider());
	treeViewer.setSorter(new ViewerSorter() {});
	treeViewer.addSelectionChangedListener(
		new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection)event.getSelection();
				containerSelectionChanged((IContainer) selection.getFirstElement()); // allow null
			}
		});
	treeViewer.addDoubleClickListener(
		new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection) {
					Object item = ((IStructuredSelection)selection).getFirstElement();
					if (treeViewer.getExpandedState(item))
						treeViewer.collapseToLevel(item, 1);
					else
						treeViewer.expandToLevel(item, 1);
				}
			}
		});

	// This has to be done after the viewer has been laid out
	treeViewer.setInput(ResourcesPlugin.getWorkspace());
}
/**
 * Returns the currently entered container name.
 * Null if the field is empty. Note that the
 * container may not exist yet if the user
 * entered a new container name in the field.
 */
public IPath getContainerFullPath() {
	if (allowNewContainerName) {
		String pathName = containerNameField.getText();
		if (pathName == null || pathName.length() < 1)
			return null;
		else
			//The user may not have made this absolute so do it for them
			return (new Path(pathName)).makeAbsolute();
	} else {
		if (selectedContainer == null)
			return null;
		else
			return selectedContainer.getFullPath();
	}
}
/**
 * Gives focus to one of the widgets in the group, as determined by the group.
 */
public void setInitialFocus() {
	if (allowNewContainerName)
		containerNameField.setFocus();
	else
		treeViewer.getTree().setFocus();
}
/**
 * Sets the selected existing container.
 */
public void setSelectedContainer(IContainer container) {
	selectedContainer = container;
	
	//expand to and select the specified container
	List itemsToExpand = new ArrayList();
	IContainer parent = container.getParent();
	while (parent != null) {
		itemsToExpand.add(0,parent);
		parent = parent.getParent();
	}
	treeViewer.setExpandedElements(itemsToExpand.toArray()); 
	treeViewer.setSelection(new StructuredSelection(container),true);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8843.java