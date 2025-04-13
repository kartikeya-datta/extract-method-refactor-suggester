error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5834.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5834.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5834.java
text:
```scala
public static final i@@nt PROP_DIRTY = IWorkbenchPartConstants.PROP_DIRTY;

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

package org.eclipse.ui;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Workbench parts implement or adapt to this interface to participate
 * in the enablement and execution of the <code>Save</code> and
 * <code>Save As</code> actions.
 * 
 * @since 2.1
 * @see org.eclipse.ui.IEditorPart  
 */
public interface ISaveablePart {

	/**
	 * The property id for <code>isDirty</code>.
	 */
	public static final int PROP_DIRTY = WorkbenchPartConstants.PROP_DIRTY;

	/**
	 * Saves the contents of this part.
	 * <p>
	 * If the save is successful, the part should fire a property changed event 
	 * reflecting the new dirty state (<code>PROP_DIRTY</code> property).
	 * </p>
	 * <p>
	 * If the save is cancelled through user action, or for any other reason, the
	 * part should invoke <code>setCancelled</code> on the <code>IProgressMonitor</code>
	 * to inform the caller.
	 * </p>
	 * <p>
	 * This method is long-running; progress and cancellation are provided
	 * by the given progress monitor. 
	 * </p>
	 *
	 * @param monitor the progress monitor
	 */
	public void doSave(IProgressMonitor monitor);
	
	/**
	 * Saves the contents of this part to another object.
	 * <p>
	 * Implementors are expected to open a "Save As" dialog where the user will
	 * be able to select a new name for the contents. After the selection is made,
	 * the contents should be saved to that new name.  During this operation a
	 * <code>IProgressMonitor</code> should be used to indicate progress.
	 * </p>
	 * <p>
	 * If the save is successful, the part fires a property changed event 
	 * reflecting the new dirty state (<code>PROP_DIRTY</code> property).
	 * </p>
	 */
	public void doSaveAs();

	/**
	 * Returns whether the contents of this part have changed since the last save
	 * operation. If this value changes the part must fire a property listener 
	 * event with <code>PROP_DIRTY</code>.
	 * <p>
	 *
	 * @return <code>true</code> if the contents have been modified and need
	 *   saving, and <code>false</code> if they have not changed since the last
	 *   save
	 */
	public boolean isDirty();

	/**
	 * Returns whether the "Save As" operation is supported by this part.
	 *
	 * @return <code>true</code> if "Save As" is supported, and <code>false</code>
	 *  if not supported
	 */
	public boolean isSaveAsAllowed();

	/**
	 * Returns whether the contents of this part should be saved when the part
	 * is closed.
	 *
	 * @return <code>true</code> if the contents of the part should be saved on
	 *   close, and <code>false</code> if the contents are expendable
	 */
	public boolean isSaveOnCloseNeeded();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5834.java