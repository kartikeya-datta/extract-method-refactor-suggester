error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14371.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14371.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14371.java
text:
```scala
T@@race.trace(ProviderPlugin.PLUGIN_ID, ECFProviderDebugOptions.SHAREDOBJECTCONTEXT, msg + ":" + container.getID()); //$NON-NLS-1$

/****************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 *****************************************************************************/

/*
 * Created on Dec 6, 2004
 *  
 */
package org.eclipse.ecf.provider.generic;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.core.security.IConnectContext;
import org.eclipse.ecf.core.sharedobject.*;
import org.eclipse.ecf.core.sharedobject.util.IQueueEnqueue;
import org.eclipse.ecf.core.util.Trace;
import org.eclipse.ecf.internal.provider.ECFProviderDebugOptions;
import org.eclipse.ecf.internal.provider.ProviderPlugin;

public class SOContext implements ISharedObjectContext {

	protected SOContainer container = null;
	protected ID sharedObjectID;
	protected ID homeContainerID;
	protected boolean isActive;
	protected Map properties;
	protected IQueueEnqueue queue;

	public SOContext(ID objID, ID homeID, SOContainer cont, Map props, IQueueEnqueue queue) {
		super();
		this.sharedObjectID = objID;
		this.homeContainerID = homeID;
		this.container = cont;
		this.properties = props;
		this.queue = queue;
		isActive = true;
	}

	public boolean isActive() {
		return isActive;
	}

	protected void trace(String msg) {
		Trace.trace(ProviderPlugin.PLUGIN_ID, ECFProviderDebugOptions.DEBUG, msg + ":" + container.getID()); //$NON-NLS-1$
	}

	protected void traceStack(String msg, Throwable e) {
		Trace.catching(ProviderPlugin.PLUGIN_ID, ECFProviderDebugOptions.EXCEPTIONS_CATCHING, SOContext.class, container.getID() + ":" + msg, e); //$NON-NLS-1$
	}

	protected void makeInactive() {
		isActive = false;
	}

	protected boolean isInactive() {
		return !isActive;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContext#getContainerID()
	 */
	public ID getLocalContainerID() {
		return container.getID();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContext#getSharedObjectManager()
	 */
	public ISharedObjectManager getSharedObjectManager() {
		if (isInactive()) {
			return null;
		}
		return container.getSharedObjectManager();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContext#getQueue()
	 */
	public IQueueEnqueue getQueue() {
		if (isInactive()) {
			return null;
		}
		return queue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContext#connect(org.eclipse.ecf.core.identity.ID,
	 *      org.eclipse.ecf.core.security.IConnectContext)
	 */
	public void connect(ID groupID, IConnectContext joinContext) throws ContainerConnectException {
		if (isInactive())
			return;
		container.connect(groupID, joinContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContext#disconnect()
	 */
	public void disconnect() {
		if (isInactive()) {
			trace("leaveGroup() CONTEXT INACTIVE"); //$NON-NLS-1$
			return;
		}
		trace("leaveGroup()"); //$NON-NLS-1$
		container.disconnect();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContext#getConnectedID()
	 */
	public ID getConnectedID() {
		if (isInactive()) {
			return null;
		}
		return container.getConnectedID();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContext#isGroupManager()
	 */
	public boolean isGroupManager() {
		if (isInactive()) {
			return false;
		}
		return container.isGroupManager();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContext#isGroupServer()
	 */
	public boolean isGroupServer() {
		if (isInactive()) {
			return false;
		}
		return container.isGroupManager();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContext#getGroupMembership()
	 */
	public ID[] getGroupMemberIDs() {
		if (isInactive()) {
			return new ID[0];
		}
		return container.getGroupMemberIDs();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContext#sendCreate(org.eclipse.ecf.core.identity.ID,
	 *      org.eclipse.ecf.core.ReplicaSharedObjectDescription)
	 */
	public void sendCreate(ID toContainerID, ReplicaSharedObjectDescription sd) throws IOException {
		if (isInactive()) {
			trace("sendCreate(" + toContainerID + "," + sd + ") CONTEXT INACTIVE"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			return;
		}
		trace("sendCreate(" + toContainerID + "," + sd + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		container.sendCreate(sharedObjectID, toContainerID, sd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContext#sendCreateResponse(org.eclipse.ecf.core.identity.ID,
	 *      java.lang.Throwable, long)
	 */
	public void sendCreateResponse(ID toContainerID, Throwable throwable, long identifier) throws IOException {
		if (isInactive()) {
			trace("sendCreateResponse(" + toContainerID + "," + throwable + "," + identifier + ") CONTEXT INACTIVE"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			return;
		}
		trace("sendCreateResponse(" + toContainerID + "," + throwable + "," + identifier + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		container.sendCreateResponse(toContainerID, sharedObjectID, throwable, identifier);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContext#sendDispose(org.eclipse.ecf.core.identity.ID)
	 */
	public void sendDispose(ID toContainerID) throws IOException {
		if (isInactive()) {
			trace("sendDispose(" + toContainerID + ") CONTEXT INACTIVE"); //$NON-NLS-1$ //$NON-NLS-2$
			return;
		}
		trace("sendDispose(" + toContainerID + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		container.sendDispose(toContainerID, sharedObjectID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContext#sendMessage(org.eclipse.ecf.core.identity.ID,
	 *      java.lang.Object)
	 */
	public void sendMessage(ID toContainerID, Object data) throws IOException {
		if (isInactive()) {
			trace("sendMessage(" + toContainerID + "," + data + ") CONTEXT INACTIVE"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			return;
		}
		trace("sendMessage(" + toContainerID + "," + data + ") CONTEXT ACTIVE"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		container.sendMessage(toContainerID, sharedObjectID, data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContext#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class clazz) {
		return container.getAdapter(clazz);
	}

	public Namespace getConnectNamespace() {
		if (isInactive()) {
			return null;
		}
		return container.getConnectNamespace();
	}

	public Map getLocalContainerProperties() {
		if (isInactive())
			return new HashMap();
		return container.createContainerPropertiesForSharedObject(sharedObjectID);
	}

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14371.java