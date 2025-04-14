error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8405.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8405.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8405.java
text:
```scala
s@@etToolTipText(WorkbenchMessages.format("SetPagePerspectiveAction.toolTip", new Object[] {persp.getLabel()})); //$NON-NLS-1$

package org.eclipse.ui.internal;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.ui.*;
import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.ui.internal.IHelpContextIds;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.*;

/**
 * Sets the current perspective of the workbench
 * page.
 */
public class SetPagePerspectiveAction extends Action {
	private WorkbenchPage page;
	private IPerspectiveDescriptor persp;
	
	/**
	 *	Create an instance of this class
	 */
	public SetPagePerspectiveAction(IPerspectiveDescriptor perspective, WorkbenchPage workbenchPage) {
		super(WorkbenchMessages.getString("SetPagePerspectiveAction.text")); //$NON-NLS-1$
		setChecked(false);
		persp = perspective;
		page = workbenchPage;
		update(persp);
		WorkbenchHelp.setHelp(this, IHelpContextIds.SWITCH_TO_PERSPECTIVE_ACTION);
	}

	/**
	 * Returns the page this action applies to
	 */
	/* package */ WorkbenchPage getPage() {
		return page;
	}
	
	/**
	 * Returns the perspective this action applies to
	 */
	/* package */ IPerspectiveDescriptor getPerspective() {
		return persp;
	}
	
	/**
	 * Returns whether this action handles the specified
	 * workbench page and perspective.
	 */
	public boolean handles(IPerspectiveDescriptor perspective, WorkbenchPage workbenchPage) {
		return persp == perspective && page == workbenchPage;
	}
	
	/**
	 * Replaces the perspective used
	 */
	public void setPerspective(IPerspectiveDescriptor newPerspective) {
		persp = newPerspective;
	}
	
	/**
	 * The user has invoked this action
	 */
	public void run() {
		page.setPerspective(persp);
		// Force the button into proper checked state
		setChecked(page.getPerspective() == persp);
	}
	
	/**
	 *	Update the action.
	 */
	public void update(IPerspectiveDescriptor newDesc) {
		persp = newDesc;
		setToolTipText(WorkbenchMessages.format("SetPagePerspectiveAction.toolTip", new Object[] {persp.getLabel()}));
		ImageDescriptor image = persp.getImageDescriptor();
		if (image != null) {
			setImageDescriptor(image);
			setHoverImageDescriptor(null);
		} else {
			setImageDescriptor(WorkbenchImages.getImageDescriptor(IWorkbenchGraphicConstants.IMG_CTOOL_DEF_PERSPECTIVE));
			setHoverImageDescriptor(WorkbenchImages.getImageDescriptor(IWorkbenchGraphicConstants.IMG_CTOOL_DEF_PERSPECTIVE_HOVER));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8405.java