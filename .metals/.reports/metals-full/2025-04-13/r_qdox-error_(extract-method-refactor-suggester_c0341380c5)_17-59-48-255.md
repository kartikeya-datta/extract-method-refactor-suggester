error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4286.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4286.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4286.java
text:
```scala
d@@ecoratorUpdateThread = new Thread(decorationRunnable, "Decoration"); //$NON-NLS-1$

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

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.internal.WorkbenchPlugin;

/**
 * The DecorationScheduler is the class that handles the
 * decoration of elements using a background thread.
 */
public class DecorationScheduler implements IResourceChangeListener {

	// When decorations are computed they are added to this cache via decorated() method
	private Map resultCache = Collections.synchronizedMap(new HashMap());

	// Objects that need an icon and text computed for display to the user
	private List awaitingDecoration = new ArrayList();

	// Objects that are awaiting a label update.
	private List pendingUpdate = new ArrayList();

	private Map awaitingDecorationValues = new HashMap();

	private DecoratorManager decoratorManager;

	private boolean shutdown = false;

	private Thread decoratorUpdateThread;

	//The number of results to batch before the label changed is sent
	private final int NUM_TO_BATCH = 50;

	/**
	 * Return a new instance of the receiver configured for
	 * the supplied DecoratorManager.
	 * @param manager
	 */
	DecorationScheduler(DecoratorManager manager) {
		decoratorManager = manager;

		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	/**
	* Decorate the text for the receiver. If it has already
	* been done then return the result, otherwise queue
	* it for decoration.
	* 
	* @return String
	* @param text
	* @param element
	* @param adaptedElement. The adapted value of element. May be null.
	*/

	public String decorateWithText(
		String text,
		Object element,
		Object adaptedElement) {

		//We do not support decoration of null
		if (element == null)
			return text;

		DecorationResult decoration =
			(DecorationResult) resultCache.get(element);

		if (decoration == null) {
			queueForDecoration(element, adaptedElement);
			return text;
		} else
			return decoration.decorateWithText(text);

	}
	/**
	 * Queue the element and its adapted value if it has not been
	 * already.
	 * @param element
	 * @param adaptedElement. The adapted value of element. May be null.
	 */

	private synchronized void queueForDecoration(
		Object element,
		Object adaptedElement) {

		//Lazily create the thread that calculates the decoration for a resource
		if (decoratorUpdateThread == null) {
			createDecoratorThread();
			decoratorUpdateThread.start();
		}

		if (!awaitingDecorationValues.containsKey(element)) {
			DecorationReference reference =
				new DecorationReference(element, adaptedElement);
			awaitingDecorationValues.put(element, reference);
			awaitingDecoration.add(element);
			//Notify the receiver as the next method is
			//synchronized on the receiver.
			notify();
		}

	}

	/**
	 * Decorate the supplied image, element and its adapted value.
	 * 
	 * @return Image
	 * @param image
	 * @param element
	 * @param adaptedElement. The adapted value of element. May be null.
	 * 
	 */
	public Image decorateWithOverlays(
		Image image,
		Object element,
		Object adaptedElement) {

		//We do not support decoration of null
		if (element == null)
			return image;

		DecorationResult decoration =
			(DecorationResult) resultCache.get(element);

		if (decoration == null) {
			queueForDecoration(element, adaptedElement);
			return image;
		} else
			return decoration.decorateWithOverlays(
				image,
				decoratorManager.getLightweightManager().getOverlayCache());
	}

	/**
	 * Execute a label update using the pending decorations.
	 * @param resources
	 * @param decorationResults
	 */
	public void decorated() {

		//Don't bother if we are shutdown now
		if (!shutdown) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {

					if (pendingUpdate.isEmpty())
						return;
					//Get the elements awaiting update and then
					//clear the list
					Object[] elements =
						pendingUpdate.toArray(new Object[pendingUpdate.size()]);
					pendingUpdate.clear();
					decoratorManager.labelProviderChanged(
						new LabelProviderChangedEvent(
							decoratorManager,
							elements));
					resultCache.clear();
				}
			});

		}
	}

	/**
	 * Shutdown the decoration thread.
	 */
	void shutdown() {
		shutdown = true;
		// Wake the thread up if it is asleep.
		synchronized (this) {
			notifyAll();
		}
		try {
			if (decoratorUpdateThread != null)
				// Wait for the decorator thread to finish before returning.
				decoratorUpdateThread.join();
		} catch (InterruptedException e) {
		}
	}

	/**
	 * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
	 */
	public void resourceChanged(IResourceChangeEvent event) {
		IResourceDelta delta = event.getDelta();
		if (delta != null) {
			try {
				final List changedObjects = new ArrayList();
				delta.accept(new IResourceDeltaVisitor() {
					public boolean visit(IResourceDelta delta)
						throws CoreException {
						IResource resource = delta.getResource();

						if (resource.getType() == IResource.ROOT) {
							// continue with the delta
							return true;
						}

						switch (delta.getKind()) {
							case IResourceDelta.REMOVED :
								// remove the cached decoration for any removed resource
								resultCache.remove(resource);
								break;
							case IResourceDelta.CHANGED :
								// for changed resources remove the result as it will need to 
								//be recalculated.
								resultCache.remove(resource);
						}

						return true;
					}
				});

				changedObjects.clear();
			} catch (CoreException exception) {
				WorkbenchPlugin.getDefault().getLog().log(
					exception.getStatus());
			}
		}
	}

	/**
	 * Get the next resource to be decorated.
	 * @return IResource
	 */
	synchronized DecorationReference next() {
		try {
			if (shutdown)
				return null;

			if (awaitingDecoration.isEmpty()) {
				wait();
			}
			// We were awakened.
			if (shutdown) {
				// The decorator was awakened by the plug-in as it was shutting down.
				return null;
			}
			Object element = awaitingDecoration.remove(0);

			return (DecorationReference) awaitingDecorationValues.remove(
				element);
		} catch (InterruptedException e) {
		}
		return null;
	}

	/**
	 * Create the Thread used for running decoration.
	 */
	private void createDecoratorThread() {
		Runnable decorationRunnable = new Runnable() {
			/* @see Runnable#run()
				*/
			public void run() {
				while (true) {
					// will block if there are no resources to be decorated
					DecorationReference reference = next();
					DecorationBuilder cacheResult = new DecorationBuilder();

					// if next() returned null, we are done and should shut down.
					if (reference == null) {
						return;
					}

					//Don't decorate if there is already a pending result
					if (!resultCache.containsKey(reference.getElement())) {

						//Just build for the resource first
						Object adapted = reference.getAdaptedElement();

						if (adapted != null) {
							decoratorManager
								.getLightweightManager()
								.getDecorations(
								adapted,
								cacheResult);
							if (cacheResult.hasValue()) {
								resultCache.put(
									adapted,
									cacheResult.createResult());

							}
						}

						//Now add in the results for the main object

						decoratorManager
							.getLightweightManager()
							.getDecorations(
							reference.getElement(),
							cacheResult);

						if (cacheResult.hasValue()) {
							resultCache.put(
								reference.getElement(),
								cacheResult.createResult());

							//Add an update for only the original element to 
							//prevent multiple updates and clear the cache.
							pendingUpdate.add(reference.getElement());
							cacheResult.clearContents();
						};
					}

					// notify that decoration is ready
					if (awaitingDecoration.isEmpty()) {
						decorated();
					}
				}
			};
		};

		decoratorUpdateThread = new Thread(decorationRunnable, "Decoration");
		decoratorUpdateThread.setPriority(Thread.MIN_PRIORITY);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4286.java