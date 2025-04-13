error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4989.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4989.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4989.java
text:
```scala
public P@@artPresentation createDetachedViewPresentation(Composite parent,

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.skins;

import org.eclipse.swt.widgets.Composite;

/**
 * This is a factory for objects that control the appearance of editors and
 * views.
 * 
 * @since 3.0
 */
public abstract class AbstractPresentationFactory {
	
	/**
	 * Creates a skin for a tab folder (a stackable set of views docked
	 * in the workbench window. Must not return null;
	 * 
	 * @param flags any combination of SWT.MIN, SWT.MAX, and SWT.CLOSE
	 * @return a newly created part stack
	 */
	public abstract StackPresentation createViewStack(Composite parent, IStackPresentationSite container, int flags);

	/**
	 * Creates a skin for an editor workbook (a stackable set of editors 
	 * docked in the workbench window). Must not return null.
	 * 
	 * @param flags any combination of SWT.MIN, SWT.MAX, and SWT.CLOSE
	 * @return a newly created part stack
	 */
	public abstract StackPresentation createEditorStack(Composite parent, IStackPresentationSite container, int flags);
	
	/**
	 * Creates a skin for a tab folder (a stackable set of views docked
	 * in the workbench window. Returns null iff this skin does not support
	 * fast views.
	 * 
	 * TODO: document flags
	 * 
	 * @param flags any combination of SWT.MIN, SWT.MAX, and SWT.CLOSE
	 * @return a newly created part stack
	 */
	public PartPresentation createFastViewPresentation(Composite parent, 
			IPartPresentationSite container, IPresentablePart thePart, int flags) {
		return null;
	}
	
	/**
	 * Creates a skin for a detached window. Returns null iff this skin
	 * does not support detached windows.
	 * 
	 * TODO: javadoc
	 * 
	 * @since 3.0
	 */
	public PartPresentation createDetachedWindowPresentation(Composite parent, 
			IPartPresentationSite container, IPresentablePart thePart, int flags) {
		return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4989.java