error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7665.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7665.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7665.java
text:
```scala
r@@eturn (IJavaElementDelta) this.source;

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
package org.eclipse.jdt.core;

import java.util.EventObject;

/**
 * An element changed event describes a change to the structure or contents
 * of a tree of Java elements. The changes to the elements are described by
 * the associated delta object carried by this event.
 * <p>
 * This class is not intended to be instantiated or subclassed by clients.
 * Instances of this class are automatically created by the Java model.
 * </p>
 *
 * @see IElementChangedListener
 * @see IJavaElementDelta
 */
public class ElementChangedEvent extends EventObject {
	
	/**
	 * Event type constant (bit mask) indicating an after-the-fact 
	 * report of creations, deletions, and modifications
	 * to one or more Java element(s) expressed as a hierarchical
	 * java element delta as returned by <code>getDelta()</code>.
	 *
	 * Note: this notification occurs during the corresponding POST_CHANGE
	 * resource change notification, and contains a full delta accounting for
	 * any JavaModel operation  and/or resource change.
	 *
	 * @see IJavaElementDelta
	 * @see org.eclipse.core.resources.IResourceChangeEvent
	 * @see #getDelta()
	 * @since 2.0
	 */
	public static final int POST_CHANGE = 1;

	/**
	 * Event type constant (bit mask) indicating an after-the-fact 
	 * report of creations, deletions, and modifications
	 * to one or more Java element(s) expressed as a hierarchical
	 * java element delta as returned by <code>getDelta</code>.
	 *
	 * Note: this notification occurs during the corresponding PRE_AUTO_BUILD
	 * resource change notification. The delta, which is notified here, only contains
	 * information relative to the previous JavaModel operations (in other words, 
	 * it ignores the possible resources which have changed outside Java operations). 
	 * In particular, it is possible that the JavaModel be inconsistent with respect to
	 * resources, which got modified outside JavaModel operations (it will only be
	 * fully consistent once the POST_CHANGE notification has occurred).
	 * 
	 * @see IJavaElementDelta
	 * @see org.eclipse.core.resources.IResourceChangeEvent
	 * @see #getDelta()
	 * @since 2.0
	 * @deprecated - no longer used, such deltas are now notified during POST_CHANGE
	 */
	public static final int PRE_AUTO_BUILD = 2;

	/**
	 * Event type constant (bit mask) indicating an after-the-fact 
	 * report of creations, deletions, and modifications
	 * to one or more Java element(s) expressed as a hierarchical
	 * java element delta as returned by <code>getDelta</code>.
	 *
	 * Note: this notification occurs as a result of a working copy reconcile
	 * operation.
	 *
	 * @see IJavaElementDelta
	 * @see org.eclipse.core.resources.IResourceChangeEvent
	 * @see #getDelta()
	 * @since 2.0
	 */
	public static final int 	POST_RECONCILE = 4;	
	/*
	 * Event type indicating the nature of this event. 
	 * It can be a combination either:
	 *  - POST_CHANGE
	 *  - PRE_AUTO_BUILD
	 *  - POST_RECONCILE
	 */
	private int type; 
	
	/**
	 * Creates an new element changed event (based on a <code>IJavaElementDelta</code>).
	 *
	 * @param delta the Java element delta.
	 * @param type the type of delta (ADDED, REMOVED, CHANGED) this event contains
	 */
	public ElementChangedEvent(IJavaElementDelta delta, int type) {
		super(delta);
		this.type = type;
	}
	/**
	 * Returns the delta describing the change.
	 *
	 * @return the delta describing the change
	 */
	public IJavaElementDelta getDelta() {
		return (IJavaElementDelta) source;
	}
	
	/**
	 * Returns the type of event being reported.
	 *
	 * @return one of the event type constants
	 * @see #POST_CHANGE
	 * @see #PRE_AUTO_BUILD
	 * @see #POST_RECONCILE
	 * @since 2.0
	 */
	public int getType() {
		return this.type;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7665.java