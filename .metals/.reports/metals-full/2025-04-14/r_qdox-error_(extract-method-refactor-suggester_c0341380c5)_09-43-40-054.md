error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16359.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16359.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16359.java
text:
```scala
t@@hrows ContainerJoinException;

/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/

package org.eclipse.ecf.core;

import java.io.IOException;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.security.IJoinContext;
import org.eclipse.ecf.core.util.IQueueEnqueue;

/**
 * Context reference provided to all ISharedObjects upon initialization.
 * Implementers of this interface provide a runtime context for ISharedObject
 * instances. Upon initialization within a container (see
 * {@link ISharedObject#init(ISharedObjectConfig)}, ISharedObject instances can
 * access an instance of this context by calling
 * {@link ISharedObjectConfig#getContext()}. They then can have access to the
 * functions provided by this context object for use in implementing their
 * behavior.
 * 
 * @see ISharedObject#init
 * @see ISharedObjectConfig#getContext()
 */
public interface ISharedObjectContext extends IAdaptable {

	/**
	 * Get the local container instance's ID
	 * 
	 * @return the ID of the enclosing container
	 */
	public ID getLocalContainerID();

	/**
	 * Get the ISharedObjectManager for this context
	 * 
	 * @return ISharedObjectManager the shared object manager instance for this
	 *         container. Null if none available.
	 */
	public ISharedObjectManager getSharedObjectManager();

	/**
	 * Get the IQueueEnqueue instance associated with this ISharedObject. If the
	 * given container provides a queue for this ISharedObject, this method will
	 * return a IQueueEnqueue reference to the appropriate queue.
	 * 
	 * @return IQueueEnqueue instance if an active queue is associated with this
	 *         ISharedObject. If no active queue is associated with the
	 *         ISharedObject, returns null.
	 */
	public IQueueEnqueue getQueue();

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContainer#joinGroup()
	 */
	public void joinGroup(ID groupID, IJoinContext joinContext)
			throws SharedObjectContainerJoinException;

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContainer#leaveGroup()
	 */
	public void leaveGroup();

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContainer#getGroupID()
	 */
	public ID getGroupID();

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContainer#isGroupManager()
	 */
	public boolean isGroupManager();

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContainer#getGroupMemberIDs()
	 */
	public ID[] getGroupMemberIDs();

	/**
	 * Send message to create a remote instance of an ISharedObject with the
	 * same ID as this instance. This method allows ISharedObject instances
	 * (with a reference to a valid ISharedObjectContext) to send messages to
	 * remote containers asking them to create an instance of a new
	 * ISharedObject. The given ISharedObjectDescription provides the
	 * specification of the new object.
	 * 
	 * @param toContainerID
	 *            the ID of the remote ISharedObjectContainer that is the target
	 *            of the create request. If this parameter is null, the request
	 *            is assumed to be made of <b>all </b> remote containers
	 *            currently in the given group (excepting the local container).
	 * @param sd
	 *            the SharedObjectDescription describing the class, constructor
	 *            and other properties to be associated with the new instance
	 * @throws IOException
	 *             thrown if message cannot be sent by container
	 */
	public void sendCreate(ID toContainerID, SharedObjectDescription sd)
			throws IOException;

	/**
	 * Send create response back to an ISharedObject with the same ID as this
	 * instance. This method allows ISharedObject instances (with a reference to
	 * a valid ISharedObjectContext) to send messages to remote containers
	 * asking them to deliver the create response status back to the
	 * ISharedObject.
	 * 
	 * @param toContainerID
	 *            the ID of the container that is to receive this response
	 * @param throwable
	 *            a throwable associated with the creation. Null means that no
	 *            exception occured
	 * @param identifier
	 *            the identifier used in the original create message (in the
	 *            shared object description)
	 * @exception IOException
	 *                thrown if the create response cannot be sent
	 */
	public void sendCreateResponse(ID toContainerID, Throwable throwable,
			long identifier) throws IOException;

	/**
	 * Send message to dispose of a remote instance of the ISharedObject with
	 * same ID as this instance. This method allows ISharedObject instances to
	 * control the destruction of remote replicas.
	 * 
	 * @param toContainerID
	 *            the ID of the remote ISharedObjectContainer that is the target
	 *            of the dispose request. If this parameter is null, the request
	 *            is assumed to be made of <b>all </b> remote containers
	 *            currently in the given group (excepting the local container).
	 * @throws IOException
	 *             thrown if message cannot be sent by container
	 */
	public void sendDispose(ID toContainerID) throws IOException;

	/**
	 * Send arbitrary message to remote instance of the ISharedObject with same
	 * ID as this instance. This method allows ISharedObject instances to send
	 * arbitrary data to one or more remote replicas of this ISharedObject.
	 * 
	 * @param toContainerID
	 *            the ID of the remote ISharedObjectContainer that is the target
	 *            container for the message request. If this parameter is null,
	 *            the request is assumed to be made of <b>all </b> remote
	 *            containers currently in the given group (excepting the local
	 *            container).
	 * @param data
	 *            arbitrary message object. Must be serializable.
	 * @throws IOException
	 *             thrown if message cannot be sent by container, or if data
	 *             cannot be serialized
	 */
	public void sendMessage(ID toContainerID, Object data) throws IOException;

	/**
	 * Get a reference to a proxy instance that allows the registration and
	 * access to local OSGI-platform-provided services. If this method returns
	 * null, then such services are not available.
	 * 
	 * @return null if OSGI platform services cannot be accessed, a valid
	 *         instance of the given interface if the context allows access to
	 *         such services
	 */
	public IOSGIService getServiceAccess();
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16359.java