error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3951.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3951.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3951.java
text:
```scala
t@@his(PlatformUI.getWorkbench().getActiveWorkbenchWindow());

package org.eclipse.ui.internal;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.ui.*;
import org.eclipse.ui.help.*;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.*;

/**
 * Reset the layout within the active perspective.
 */
public class ResetPerspectiveAction extends Action {
	private IWorkbenchWindow window;	
/**
 * This default constructor allows the the action to be called from the welcome page.
 */
public ResetPerspectiveAction() {
	this(((Workbench)PlatformUI.getWorkbench()).getActiveWorkbenchWindow());
}
/**
 *	Create an instance of this class
 */
public ResetPerspectiveAction(IWorkbenchWindow window) {
	super(WorkbenchMessages.getString("ResetPerspective.text")); //$NON-NLS-1$
	setToolTipText(WorkbenchMessages.getString("ResetPerspective.toolTip")); //$NON-NLS-1$
	setEnabled(false);
	WorkbenchHelp.setHelp(this, IHelpContextIds.RESET_PERSPECTIVE_ACTION);
	this.window = window;
}
/**
 *	The user has invoked this action
 */
public void run() {
	IWorkbenchPage page = this.window.getActivePage();
	if (page != null && page.getPerspective() != null) {
		String message = WorkbenchMessages.format("ResetPerspective.message", new Object[] { page.getPerspective().getLabel() }); //$NON-NLS-1$
		String [] buttons= new String[] { 
			IDialogConstants.OK_LABEL,
			IDialogConstants.CANCEL_LABEL
		};
		MessageDialog d= new MessageDialog(
			this.window.getShell(),
			WorkbenchMessages.getString("ResetPerspective.title"), //$NON-NLS-1$
			null,
			message,
			MessageDialog.QUESTION,
			buttons,
			0
		);
		if (d.open() == 0)
			page.resetPerspective();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3951.java