error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8369.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8369.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8369.java
text:
```scala
l@@ightweightDefinitions[i].setEnabled(false);

package org.eclipse.ui.internal.decorators;

/*******************************************************************************
 * Copyright (c) 2002 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Common Public License v0.5
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v05.html
 *
 * Contributors:
 * IBM - Initial implementation
 ******************************************************************************/

import java.util.*;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;

/*
 * (c) Copyright IBM Corp. 2002.
 * All Rights Reserved.
 */

/**
 * The LightweightDecoratorManager is a decorator manager
 * that encapsulates the behavior for the lightweight decorators.
 */
class LightweightDecoratorManager {

	/**
	 * The runnable is the object used to run the decorations
	 * so that an error in someones decorator will not kill the thread.
	 * It is implemented here to prevent aborting of decoration
	 * i.e. successful decorations will still be applied.
	 */

	private class LightweightRunnable implements ISafeRunnable {
		private Object element;
		private DecorationBuilder decoration;
		private LightweightDecoratorDefinition decorator;

		private void setValues(
			Object object,
			DecorationBuilder builder,
			LightweightDecoratorDefinition definition) {
			element = object;
			decoration = builder;
			decorator = definition;

		}
		/*
		 * @see ISafeRunnable.handleException(Throwable).
		 */
		public void handleException(Throwable exception) {
		}
		/*
		 * @see ISafeRunnable.run
		 */
		public void run() throws Exception {
			decorator.decorate(element, decoration);
		}
	}

	private LightweightRunnable runnable = new LightweightRunnable();

	//The cachedDecorators are a 1-many mapping of type to full decorator.
	private HashMap cachedLightweightDecorators = new HashMap();

	//The lightweight definitionsread from the registry
	private LightweightDecoratorDefinition[] lightweightDefinitions;

	private static final LightweightDecoratorDefinition[] EMPTY_LIGHTWEIGHT_DEF =
		new LightweightDecoratorDefinition[0];

	private OverlayCache overlayCache = new OverlayCache();

	LightweightDecoratorManager(LightweightDecoratorDefinition[] definitions) {
		super();
		lightweightDefinitions = definitions;
	}

	/**
	 * Get the lightweight definitions for the receiver.
	 * @return LightweightDecoratorDefinition[]
	 */
	LightweightDecoratorDefinition[] getDefinitions() {
		return lightweightDefinitions;
	}

	/**
	 * Return the enabled lightweight decorator definitions.
	 * @return LightweightDecoratorDefinition[]
	 */
	LightweightDecoratorDefinition[] enabledDefinitions() {
		ArrayList result = new ArrayList();
		for (int i = 0; i < lightweightDefinitions.length; i++) {
			if (lightweightDefinitions[i].isEnabled())
				result.add(lightweightDefinitions[i]);
		}
		LightweightDecoratorDefinition[] returnArray =
			new LightweightDecoratorDefinition[result.size()];
		result.toArray(returnArray);
		return returnArray;
	}

	/**
	 * Reset any cached values.
	 */
	void reset() {
		cachedLightweightDecorators = new HashMap();
	}

	/**
	* Shutdown the decorator manager by disabling all
	* of the decorators so that dispose() will be called
	* on them.
	*/
	void shutdown() {
		//Disable all fo the enabled decorators 
		//so as to force a dispose of thier decorators
		for (int i = 0; i < lightweightDefinitions.length; i++) {
			if (lightweightDefinitions[i].isEnabled())
				lightweightDefinitions[i].setEnabledWithErrorHandling(false);
		}
		overlayCache.disposeAll();
	}

	/**
	 * Get the LightweightDecoratorDefinition with the supplied id
	 * @return LightweightDecoratorDefinition or <code>null</code> if it is not found
	 * @param decoratorId String
	 */
	LightweightDecoratorDefinition getDecoratorDefinition(String decoratorId) {
		for (int i = 0; i < lightweightDefinitions.length; i++) {
			if (lightweightDefinitions[i].getId().equals(decoratorId))
				return lightweightDefinitions[i];
		}
		return null;
	}

	/**
	* Get the lightweight  registered for elements of this type.
	*/
	LightweightDecoratorDefinition[] getDecoratorsFor(Object element) {

		if (element == null)
			return EMPTY_LIGHTWEIGHT_DEF;

		String className = element.getClass().getName();
		LightweightDecoratorDefinition[] decoratorArray =
			(LightweightDecoratorDefinition[]) cachedLightweightDecorators.get(
				className);
		if (decoratorArray != null) {
			return decoratorArray;
		}

		Collection decorators =
			DecoratorManager.getDecoratorsFor(element, enabledDefinitions());

		if (decorators.size() == 0)
			decoratorArray = EMPTY_LIGHTWEIGHT_DEF;
		else {
			decoratorArray =
				new LightweightDecoratorDefinition[decorators.size()];
			decorators.toArray(decoratorArray);
		}

		cachedLightweightDecorators.put(className, decoratorArray);
		return decoratorArray;
	}

	/**
	* Fill the decoration with all of the results of the 
	* decorators.
	* 
	* @param element The source element
	* @param adapted The adapted value of element or null
	* @param decoration. The DecorationResult we are working on.
	*/

	void getDecorations(
		Object element,
		DecorationBuilder decoration) {

		LightweightDecoratorDefinition[] decorators = getDecoratorsFor(element);

		for (int i = 0; i < decorators.length; i++) {
			if (decorators[i].getEnablement().isEnabledFor(element)) {
				decoration.setCurrentDefinition(decorators[i]);
				decorate(element, decoration, decorators[i]);
			}
		}


	}

	/**
	 * Decorate the element receiver in a SafeRunnable.
	 * @param element. The Object to be decorated
	 * @param decoration. The object building decorations.
	 * @param decorator. The decorator being applied.
	 */
	private void decorate(
		Object element,
		DecorationBuilder decoration,
		LightweightDecoratorDefinition decorator) {

		runnable.setValues(element, decoration, decorator);
		Platform.run(runnable);

	}
	/**
	 * Returns the overlayCache.
	 * @return OverlayCache
	 */
	OverlayCache getOverlayCache() {
		return overlayCache;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8369.java