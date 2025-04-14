error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9520.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9520.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9520.java
text:
```scala
i@@f (changeId == IWorkbenchPage.CHANGE_RESET || changeId == IWorkbenchPage.CHANGE_EDITOR_AREA_HIDE || changeId == IWorkbenchPage.CHANGE_EDITOR_AREA_SHOW) {

package org.eclipse.ui.internal;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.ui.help.*;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.*;

/**
 * Hides or shows the editor area within the current
 * perspective of the workbench page.
 */
public class ToggleEditorsVisibilityAction extends Action {
	private IWorkbenchWindow workbenchWindow;
/**
 * Creates a new <code>ToggleEditorsVisibilityAction</code>
 */
public ToggleEditorsVisibilityAction(IWorkbenchWindow window) {
	super(WorkbenchMessages.getString("ToggleEditor.hideEditors")); //$NON-NLS-1$
	setToolTipText(WorkbenchMessages.getString("ToggleEditor.toolTip")); //$NON-NLS-1$
	WorkbenchHelp.setHelp(this, IHelpContextIds.TOGGLE_EDITORS_VISIBILITY_ACTION);
	setEnabled(false);
	this.workbenchWindow = window;

	// Once the API on IWorkbenchPage to hide/show
	// the editor area is removed, then switch
	// to using the internal perspective service
	window.addPerspectiveListener(new org.eclipse.ui.IPerspectiveListener() {
		public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
			if (page.isEditorAreaVisible())
				setText(WorkbenchMessages.getString("ToggleEditor.hideEditors")); //$NON-NLS-1$
			else
				setText(WorkbenchMessages.getString("ToggleEditor.showEditors")); //$NON-NLS-1$
		}			
		public void perspectiveChanged(IWorkbenchPage page, IPerspectiveDescriptor perspective, String changeId) {
			if (changeId == page.CHANGE_RESET || changeId == page.CHANGE_EDITOR_AREA_HIDE || changeId == page.CHANGE_EDITOR_AREA_SHOW) {
				if (page.isEditorAreaVisible())
					setText(WorkbenchMessages.getString("ToggleEditor.hideEditors")); //$NON-NLS-1$
				else
					setText(WorkbenchMessages.getString("ToggleEditor.showEditors")); //$NON-NLS-1$
			}
		}			
	});
}
/**
 * Implementation of method defined on <code>IAction</code>.
 */
public void run() {
	boolean visible = workbenchWindow.getActivePage().isEditorAreaVisible();
	if (visible) {
		workbenchWindow.getActivePage().setEditorAreaVisible(false);
		setText(WorkbenchMessages.getString("ToggleEditor.showEditors")); //$NON-NLS-1$
	}
	else {
		workbenchWindow.getActivePage().setEditorAreaVisible(true);
		setText(WorkbenchMessages.getString("ToggleEditor.hideEditors")); //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9520.java