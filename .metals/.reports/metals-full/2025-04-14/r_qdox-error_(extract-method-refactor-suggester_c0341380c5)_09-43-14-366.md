error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1271.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1271.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1271.java
text:
```scala
s@@etId("closeAllSaved"); //$NON-NLS-1$

package org.eclipse.ui.internal;

/**********************************************************************
Copyright (c) 2000, 2002 IBM Corp. 
All rights reserved.   This program and the accompanying materials
are made available under the terms of the Common Public License v0.5
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/cpl-v05.html
 
Contributors:
**********************************************************************/
import org.eclipse.ui.*;
import org.eclipse.ui.actions.PartEventAction;
import org.eclipse.ui.help.WorkbenchHelp;

/**
 *	Closes all active editors
 */
public class CloseAllSavedAction extends PartEventAction implements IPageListener, IPropertyListener{
	private IWorkbenchWindow workbench;	
/**
 *	Create an instance of this class
 */
public CloseAllSavedAction(IWorkbenchWindow aWorkbench) {
	super(WorkbenchMessages.getString("CloseAllSavedAction.text")); //$NON-NLS-1$
	this.workbench = aWorkbench;
	setToolTipText(WorkbenchMessages.getString("CloseAllSavedAction.toolTip")); //$NON-NLS-1$
	//Should create a ID in IWorkbenchActionConstants when it becames API?
	setId("closeAllSaved");
	updateState();
	aWorkbench.addPageListener(this);
	WorkbenchHelp.setHelp(this, IHelpContextIds.CLOSE_ALL_SAVED_ACTION);
}
/**
 * Notifies this listener that the given page has been activated.
 *
 * @param page the page that was activated
 * @see IWorkbenchWindow#setActivePage
 */
public void pageActivated(org.eclipse.ui.IWorkbenchPage page) {
	updateState();
}
/**
 * Notifies this listener that the given page has been closed.
 *
 * @param page the page that was closed
 * @see IWorkbenchPage#close
 */
public void pageClosed(org.eclipse.ui.IWorkbenchPage page) {
	updateState();
}
/**
 * Notifies this listener that the given page has been opened.
 *
 * @param page the page that was opened
 * @see IWorkbenchWindow#openPage
 */
public void pageOpened(org.eclipse.ui.IWorkbenchPage page) {}
/**
 * A part has been closed.
 */
public void partClosed(IWorkbenchPart part) {
	if (part instanceof IEditorPart) {
		part.removePropertyListener(this);
		updateState();	
	}
}
/**
 * A part has been opened.
 */
public void partOpened(IWorkbenchPart part) {	
	if (part instanceof IEditorPart) {
		part.addPropertyListener(this);
		updateState();	
	}
}
/**
 * Indicates that a property has changed.
 *
 * @param source the object whose property has changed
 * @param propID the property which has changed.  In most cases this property ID
 * should be defined as a constant on the source class.
 */
public void propertyChanged(Object source, int propID) {
	if (source instanceof IEditorPart) {
		if (propID == IEditorPart.PROP_DIRTY) {
			updateState();
		}
	}
}
/**
 *	The user has invoked this action
 */
public void run() {
	WorkbenchPage page = (WorkbenchPage)workbench.getActivePage();
	if (page != null)
		page.closeAllSavedEditors();
}
/**
 * Enable the action if there at least one editor open.
 */
private void updateState() {
	WorkbenchPage page = (WorkbenchPage)workbench.getActivePage();
	if(page == null) {
		setEnabled(false);
		return;
	}
	IEditorReference editors[] = page.getSortedEditors();
	for (int i = 0; i < editors.length; i++) {
		if(!editors[i].isDirty()) {
			setEnabled(true);
			return;
		}
	}
	setEnabled(false);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1271.java