error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4823.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4823.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4823.java
text:
```scala
f@@older = new PartTabFolder(page);

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

package org.eclipse.ui.internal;

import org.eclipse.ui.*;
import org.eclipse.ui.help.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.jface.window.*;
import java.util.*;
import java.util.List;

public class DetachedWindow extends Window {

	private PartTabFolder folder;
	private WorkbenchPage page;
	
	//Keep the state of a DetachedWindow when switching perspectives.
	private String title;
	private Rectangle bounds;
/**
 * Create a new FloatingWindow.
 */
public DetachedWindow(WorkbenchPage workbenchPage) {
	super(workbenchPage.getWorkbenchWindow().getShell());
	setShellStyle(/* SWT.CLOSE | SWT.MIN | SWT.MAX | */ SWT.RESIZE);
	this.page = workbenchPage;
	folder = new PartTabFolder();
}
/**
 * Adds a visual part to this window.
 * Supports reparenting.
 */
public void add(ViewPane part, IPartDropListener listener) {
	Shell shell = getShell();
	if (shell != null)
		part.reparent(shell);
	folder.add(part);
	folder.enableDrag(part, listener);
}
public boolean belongsToWorkbenchPage(IWorkbenchPage workbenchPage) {
	return (this.page == workbenchPage);
}
/**
 * Closes this window and disposes its shell.
 */
public boolean close() {
	Shell s = getShell();
	if(s != null) {
		title = s.getText();
		bounds = s.getBounds();
	}
	
	if (folder != null)
		folder.dispose();
	
	return super.close();
}
/**
 * Answer a list of the view panes.
 */
private void collectViewPanes(List result, LayoutPart [] parts) {
	for (int i = 0, length = parts.length; i < length; i++) {
		LayoutPart part = parts[i];
		if (part instanceof ViewPane) {
			result.add(part);
		}
	}
}
/**
 * This method will be called to initialize the given Shell's layout
 */
protected void configureShell(Shell shell) {
	if(title != null) shell.setText(title);
	shell.addListener(SWT.Resize, new Listener() {
		public void handleEvent(Event event) {
			Shell shell = (Shell)event.widget;
			Control[] children = shell.getChildren();
			if (children != null) {
				for (int i = 0, length = children.length; i < length; i++){
					if (children[i] instanceof CTabFolder) {
						children[i].setBounds(shell.getClientArea());
						break;
					}
				}
			}
		}
	});

	WorkbenchHelp.setHelp(shell, IHelpContextIds.DETACHED_WINDOW);
}
/**
 * Override this method to create the widget tree that is used as the window's contents.
 */
protected Control createContents(Composite parent) {
	// Create the tab folder.
	folder.createControl(parent);

	// Reparent each view in the tab folder.
	Vector detachedChildren = new Vector();
	collectViewPanes(detachedChildren, getChildren());
	Enumeration enum = detachedChildren.elements();
	while (enum.hasMoreElements()) {
		LayoutPart part = (LayoutPart)enum.nextElement();
		part.reparent(parent);
	}

	// Return tab folder control.
	return folder.getControl();
}
public LayoutPart [] getChildren() {						
	return folder.getChildren();
}
public WorkbenchPage getWorkbenchPage() {						
	return this.page;
}
/**
 * Close has been pressed.  Close all views.
 */
protected void handleShellCloseEvent() {
//	List views = new ArrayList();
//	collectViewPanes(views, getChildren());
//	Iterator enum = views.iterator();
//	while (enum.hasNext()) {
//		ViewPane child = (ViewPane)enum.next();
//		page.hideView(child.getViewPart());
//	}
//	close();
}
protected void initializeBounds() {
	if(bounds != null)
		getShell().setBounds(bounds);
	else
		super.initializeBounds();
}
/**
 * @see IPersistablePart
 */
public void restoreState(IMemento memento) 
{
	// Read the title.
	title = memento.getString(IWorkbenchConstants.TAG_TITLE);
	
	// Read the bounds.
	Integer bigInt;
	bigInt = memento.getInteger(IWorkbenchConstants.TAG_X);
	int x = bigInt.intValue();
	bigInt = memento.getInteger(IWorkbenchConstants.TAG_Y);
	int y = bigInt.intValue();
	bigInt = memento.getInteger(IWorkbenchConstants.TAG_WIDTH);
	int width = bigInt.intValue();
	bigInt = memento.getInteger(IWorkbenchConstants.TAG_HEIGHT);
	int height = bigInt.intValue();

	// Set the bounds.
	bounds = new Rectangle(x, y, width, height);
	if(getShell() != null) {
		getShell().setText(title);
		getShell().setBounds(bounds);
	}
	
	// Create the folder.
	IMemento childMem = memento.getChild(IWorkbenchConstants.TAG_FOLDER);
	if (childMem != null)
		folder.restoreState(childMem);
}
/**
 * @see IPersistablePart
 */
public void saveState(IMemento memento) 
{
	if(getShell() != null) {
		title = getShell().getText();
		bounds = getShell().getBounds();
	}
	// Save the title.
	memento.putString(IWorkbenchConstants.TAG_TITLE,title);
		
	// Save the bounds.
	memento.putInteger(IWorkbenchConstants.TAG_X, bounds.x);
	memento.putInteger(IWorkbenchConstants.TAG_Y, bounds.y);
	memento.putInteger(IWorkbenchConstants.TAG_WIDTH, bounds.width);
	memento.putInteger(IWorkbenchConstants.TAG_HEIGHT, bounds.height);
	
	// Save the views.	
	IMemento childMem = memento.createChild(IWorkbenchConstants.TAG_FOLDER);
	folder.saveState(childMem);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4823.java