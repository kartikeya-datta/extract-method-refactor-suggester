error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3382.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3382.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3382.java
text:
```scala
r@@eturn new ToolBarManager2(SWT.FLAT | SWT.RIGHT);

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.presentations;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.internal.provisional.action.CoolBarManager2;
import org.eclipse.jface.internal.provisional.action.ICoolBarManager2;
import org.eclipse.jface.internal.provisional.action.IToolBarContributionItem;
import org.eclipse.jface.internal.provisional.action.IToolBarManager2;
import org.eclipse.jface.internal.provisional.action.ToolBarContributionItem2;
import org.eclipse.jface.internal.provisional.action.ToolBarManager2;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.internal.provisional.presentations.IActionBarPresentationFactory;

/**
 * The intention of this class is to allow for replacing the implementation of
 * the cool bar and tool bars in the workbench.
 * <p>
 * <strong>EXPERIMENTAL</strong>. This class or interface has been added as
 * part of a work in progress. There is a guarantee neither that this API will
 * work nor that it will remain the same. Please do not use this API without
 * consulting with the Platform/UI team.
 * </p>
 * 
 * @since 3.2
 */
public class DefaultActionBarPresentationFactory implements IActionBarPresentationFactory {

	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.presentations.IActionBarPresentationFactory#createCoolBarManager()
	 */
	public ICoolBarManager2 createCoolBarManager() {
		return new CoolBarManager2(SWT.FLAT);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.presentations.IActionBarPresentationFactory#createCoolBarControl(org.eclipse.jface.action.ICoolBarManager, org.eclipse.swt.widgets.Composite)
	 */
	public Control createCoolBarControl(ICoolBarManager2 coolBarManager,
			Composite parent) {
		return coolBarManager.createControl2(parent);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.presentations.IActionBarPresentationFactory#createToolBarManager()
	 */
	public IToolBarManager2 createToolBarManager() {
		return new ToolBarManager2(SWT.FLAT | SWT.RIGHT | SWT.WRAP);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.presentations.IActionBarPresentationFactory#createToolBarControl(org.eclipse.jface.action.IToolBarManager2, org.eclipse.swt.widgets.Composite)
	 */
	public Control createToolBarControl(IToolBarManager2 toolBarManager,
			Composite parent) {
		return toolBarManager.createControl2(parent);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.presentations.IActionBarPresentationFactory#createViewToolBarManager()
	 */
	public IToolBarManager2 createViewToolBarManager() {
		return new ToolBarManager2(SWT.FLAT | SWT.RIGHT | SWT.WRAP);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.presentations.IActionBarPresentationFactory#createViewToolBarControl(org.eclipse.jface.action.IToolBarManager2, org.eclipse.swt.widgets.Composite)
	 */
	public Control createViewToolBarControl(IToolBarManager2 toolBarManager,
			Composite parent) {
		return toolBarManager.createControl2(parent);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.presentations.IActionBarPresentationFactory#createToolBarContributionItem(org.eclipse.jface.action.IToolBarManager, java.lang.String)
	 */
	public IToolBarContributionItem createToolBarContributionItem(
			IToolBarManager toolBarManager, String id) {
		return new ToolBarContributionItem2(toolBarManager, id);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3382.java