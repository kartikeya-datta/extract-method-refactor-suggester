error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6865.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6865.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6865.java
text:
```scala
public I@@D[] getGroupMemberIDs();

/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/

package org.eclipse.ecf.core;

import org.eclipse.ecf.core.identity.ID;

/**
 * Core interface that must be implemented by all ECF container instances.
 * Instances are typically
 * created via {@link SharedObjectContainerFactory}  
 */
public interface ISharedObjectContainer {

    /**
     * Return the ISharedObjectContainerConfig for this ISharedObjectContainer.
     * The returned value must always be non-null.
     * 
     * @return ISharedObjectContainerConfig for the given ISharedObjectContainer
     *         instance
     */
    public ISharedObjectContainerConfig getConfig();
    /**
     * Add listener to ISharedObjectContainer. Listener will be notified when
     * container events occur
     * 
     * @param l
     *            the ISharedObjectContainerListener to add
     * @param filter
     * 			  the filter to define types of container events to receive
     */
    public void addListener(ISharedObjectContainerListener l, String filter);
    /**
     * Remove listener from ISharedObjectContainer.
     * 
     * @param l
     *            the ISharedObjectContainerListener to remove
     */
    public void removeListener(ISharedObjectContainerListener l);
    /**
     * Dispose this ISharedObjectContainer instance. The container instance
     * will be made inactive after the completion of this method and will be
     * unavailable for subsequent usage
     * 
     * @param waittime
     */
    public void dispose(long waittime);
    /**
     * Join a container group. The group to join is identified by the first
     * parameter (groupID) using any required authentication provided via the
     * second parameter (loginData). This method provides an implementation
     * independent way for container implementations to connect, authenticate,
     * and communicate with a remote service or group of services. Providers are
     * responsible for implementing this operation in a way appropriate to the
     * given remote service and expected protocol.
     * 
     * @param groupID
     *            the ID of the remote service to join
     * @param loginData
     *            any required login/authentication data to allow this container
     *            to authenticate
     * @exception SharedObjectContainerJoinException
     *                thrown if communication cannot be established with remote
     *                service
     */
    public void joinGroup(ID groupID, Object loginData)
            throws SharedObjectContainerJoinException;
    /**
     * Leave a container group. This operation will disconnect the local
     * container instance from any previously joined group.
     */
    public void leaveGroup();
    /**
     * Get the group id that this container has joined. Return null if no group has
     * previously been joined.
     * 
     * @return ID of the group previously joined
     */
    public ID getGroupID();
    /**
     * Get the current membership of the joined group. This method will
     * accurately report the current group membership of the connected group.
     * 
     * @return ID[] the IDs of the current group membership
     */
    public ID[] getGroupMembership();
    /**
     * @return true if this ISharedObjectContainer instance is in the
     *         'manager' role for the group, false otherwise
     */
    public boolean isGroupManager();
    /**
     * @return true if this ISharedObjectContainer instance is in a server
     *         role for the group, false otherwise
     */
    public boolean isGroupServer();
    
    /**
     * Get SharedObjectManager for this container
     * 
     * @return ISharedObjectManager for this container instance
     */
    public ISharedObjectManager getSharedObjectManager();
    
    /**
     * Returns an object which is an instance of the given class associated with
     * this object.
     * 
     * @param adapter
     *            the adapter class to lookup
     * @return Object a object castable to the given class, or null if this
     *         object does not have an adapter for the given class
     */
    public Object getAdapter(Class adapter);

}
 No newline at end of file
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6865.java