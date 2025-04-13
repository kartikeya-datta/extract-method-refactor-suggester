error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8916.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8916.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8916.java
text:
```scala
r@@eturn 30;

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
package org.eclipse.ui.internal.progress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;

import org.eclipse.ui.PlatformUI;

import org.eclipse.ui.internal.misc.Assert;

/**
 * The ProgressAnimationProcessor is the processor for the animation using the
 * system progress.
 */
class ProgressAnimationProcessor implements IAnimationProcessor {
	
	AnimationManager manager;
	
	/**
	 * Create a new instance of the receiver and listen to the animation
	 * manager.
	 * 
	 * @param animationManager
	 */
	ProgressAnimationProcessor(AnimationManager animationManager){
		manager = animationManager;
	}
	
	List items = Collections.synchronizedList(new ArrayList());

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.internal.progress.IAnimationProcessor#startAnimation(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void startAnimationLoop(IProgressMonitor monitor) {

		// Create an off-screen image to draw on, and a GC to draw with.
		// Both are disposed after the animation.
		if (items.size() == 0)
			return;
		if (!PlatformUI.isWorkbenchRunning())
			return;
		
		
		while (manager.isAnimated() && !monitor.isCanceled()) {
			//Do nothing while animation is happening
		}
			
		ProgressAnimationItem[] animationItems = getAnimationItems();
		for (int i = 0; i < animationItems.length; i++) {
			animationItems[i].animationDone();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.internal.progress.IAnimationProcessor#addItem(org.eclipse.ui.internal.progress.AnimationItem)
	 */
	public void addItem(AnimationItem item) {
		Assert.isTrue(item instanceof ProgressAnimationItem);
		items.add(item);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.internal.progress.IAnimationProcessor#hasItems()
	 */
	public boolean hasItems() {
		return items.size() > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.internal.progress.IAnimationProcessor#itemsInactiveRedraw()
	 */
	public void itemsInactiveRedraw() {
		//Nothing to do here as SWT handles redraw

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.internal.progress.IAnimationProcessor#animationStarted(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void animationStarted() {
		AnimationItem [] animationItems = getAnimationItems();
		for (int i = 0; i < animationItems.length; i++) {
			animationItems[i].animationStart();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.internal.progress.IAnimationProcessor#getPreferredWidth()
	 */
	public int getPreferredWidth() {
		return 20;
	}
	
	/**
	 * Get the animation items currently registered for the receiver.
	 * 
	 * @return ProgressAnimationItem[]
	 */
	private ProgressAnimationItem[] getAnimationItems() {
		ProgressAnimationItem[] animationItems = new ProgressAnimationItem[items.size()];
		items.toArray(animationItems);
		return animationItems;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.progress.IAnimationProcessor#animationFinished()
	 */
	public void animationFinished() {
		AnimationItem [] animationItems = getAnimationItems();
		for (int i = 0; i < animationItems.length; i++) {
			animationItems[i].animationDone();
		}

	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.progress.IAnimationProcessor#isProcessorJob(org.eclipse.core.runtime.jobs.Job)
	 */
	public boolean isProcessorJob(Job job) {
		// We have no jobs
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8916.java