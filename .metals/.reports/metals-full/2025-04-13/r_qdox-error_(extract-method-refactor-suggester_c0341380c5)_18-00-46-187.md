error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14373.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14373.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14373.java
text:
```scala
T@@race.trace(ProviderPlugin.PLUGIN_ID, ECFProviderDebugOptions.SHAREDOBJECTWRAPPER, msg + ":oID=" + sharedObjectID + ":cID=" + container.getID()); //$NON-NLS-1$ //$NON-NLS-2$

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

package org.eclipse.ecf.provider.generic;

import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.eclipse.ecf.core.events.ContainerConnectedEvent;
import org.eclipse.ecf.core.events.ContainerDisconnectedEvent;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.sharedobject.ISharedObject;
import org.eclipse.ecf.core.sharedobject.SharedObjectInitException;
import org.eclipse.ecf.core.sharedobject.events.*;
import org.eclipse.ecf.core.sharedobject.util.QueueEnqueueImpl;
import org.eclipse.ecf.core.sharedobject.util.SimpleFIFOQueue;
import org.eclipse.ecf.core.util.Event;
import org.eclipse.ecf.core.util.Trace;
import org.eclipse.ecf.internal.provider.ECFProviderDebugOptions;
import org.eclipse.ecf.internal.provider.ProviderPlugin;
import org.eclipse.ecf.provider.generic.gmm.Member;

public class SOWrapper {
	protected ISharedObject sharedObject;
	private SOConfig sharedObjectConfig;
	ID sharedObjectID;
	private SOContainer container;
	private ID containerID;
	private Thread thread;
	SimpleFIFOQueue queue;

	protected SOWrapper(SOContainer.LoadingSharedObject obj, SOContainer cont) {
		sharedObjectID = obj.getID();
		sharedObject = obj;
		container = cont;
		containerID = cont.getID();
		sharedObjectConfig = null;
		thread = null;
		queue = new SimpleFIFOQueue();
	}

	public SOWrapper(SOConfig aConfig, ISharedObject obj, SOContainer cont) {
		sharedObjectConfig = aConfig;
		sharedObjectID = sharedObjectConfig.getSharedObjectID();
		sharedObject = obj;
		container = cont;
		containerID = cont.getID();
		thread = null;
		queue = new SimpleFIFOQueue();
	}

	protected void init() throws SharedObjectInitException {
		debug("init()"); //$NON-NLS-1$
		sharedObjectConfig.makeActive(new QueueEnqueueImpl(queue));
		sharedObject.init(sharedObjectConfig);
	}

	protected ID getObjID() {
		return sharedObjectConfig.getSharedObjectID();
	}

	protected ID getHomeID() {
		return sharedObjectConfig.getHomeContainerID();
	}

	protected SOConfig getConfig() {
		return sharedObjectConfig;
	}

	protected void activated() {
		thread = (Thread) AccessController.doPrivileged(new PrivilegedAction() {
			public Object run() {
				Thread aThread = getThread();
				return aThread;
			}
		});
		// Notify container and listeners
		container.notifySharedObjectActivated(sharedObjectID);
		// Start thread
		thread.start();
		// Send message
		send(new SharedObjectActivatedEvent(containerID, sharedObjectID));
	}

	protected void deactivated() {
		container.notifySharedObjectDeactivated(sharedObjectID);
		send(new SharedObjectDeactivatedEvent(containerID, sharedObjectID));
		destroyed();
	}

	protected void destroyed() {
		if (!queue.isStopped()) {
			if (thread != null)
				queue.enqueue(new DisposeEvent());
			queue.close();
		}
	}

	protected void otherChanged(ID otherID, boolean activated) {
		if (activated && thread != null) {
			send(new SharedObjectActivatedEvent(containerID, otherID));
		} else {
			send(new SharedObjectDeactivatedEvent(containerID, otherID));
		}
	}

	protected void memberChanged(Member m, boolean add) {
		if (thread != null) {
			if (add) {
				send(new ContainerConnectedEvent(containerID, m.getID()));
			} else {
				send(new ContainerDisconnectedEvent(containerID, m.getID()));
			}
		}
	}

	protected Thread getThread() {
		return container.getNewSharedObjectThread(sharedObjectID, new Runnable() {
			public void run() {
				debug("runner(" + sharedObjectID + ")"); //$NON-NLS-1$ //$NON-NLS-2$
				Event evt = null;
				for (;;) {
					if (Thread.currentThread().isInterrupted())
						break;
					evt = (Event) queue.dequeue();
					if (Thread.currentThread().isInterrupted() || evt == null)
						break;
					try {
						if (evt instanceof ProcEvent) {
							SOWrapper.this.svc(((ProcEvent) evt).getEvent());
						} else if (evt instanceof DisposeEvent) {
							SOWrapper.this.doDestroy();
						} else {
							SOWrapper.this.svc(evt);
						}
					} catch (Throwable t) {
						handleRuntimeException(t);
					}
				}
				if (Thread.currentThread().isInterrupted()) {
					debug("runner(" + sharedObjectID //$NON-NLS-1$
							+ ") terminating interrupted"); //$NON-NLS-1$
				} else {
					debug("runner(" + sharedObjectID //$NON-NLS-1$
							+ ") terminating normally"); //$NON-NLS-1$
				}
			}
		});
	}

	private void send(Event evt) {
		queue.enqueue(new ProcEvent(evt));
	}

	public static class ProcEvent implements Event {
		private static final long serialVersionUID = 3257002142513378616L;
		Event theEvent = null;

		public ProcEvent(Event event) {
			theEvent = event;
		}

		Event getEvent() {
			return theEvent;
		}
	}

	protected static class DisposeEvent implements Event {
		private static final long serialVersionUID = 3688503311859135536L;

		DisposeEvent() {
			//
		}
	}

	protected void svc(Event evt) {
		sharedObject.handleEvent(evt);
	}

	protected void doDestroy() {
		sharedObject.dispose(containerID);
		// make config inactive
		sharedObjectConfig.makeInactive();
	}

	protected void deliverSharedObjectMessage(ID fromID, Serializable data) {
		send(new RemoteSharedObjectEvent(getObjID(), fromID, data));
	}

	protected void deliverCreateResponse(ID fromID, ContainerMessage.CreateResponseMessage resp) {
		send(new RemoteSharedObjectCreateResponseEvent(resp.getSharedObjectID(), fromID, resp.getSequence(), resp.getException()));
	}

	public void deliverEvent(Event evt) {
		send(evt);
	}

	protected void destroySelf() {
		debug("destroySelf()"); //$NON-NLS-1$
		send(new DisposeEvent());
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("SharedObjectWrapper[").append(getObjID()).append("]"); //$NON-NLS-1$ //$NON-NLS-2$
		return sb.toString();
	}

	protected void debug(String msg) {
		Trace.trace(ProviderPlugin.PLUGIN_ID, ECFProviderDebugOptions.DEBUG, "SOWrapper:oID=" + sharedObjectID + ":" + msg + ":cID=" + container.getID()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	protected void traceStack(String msg, Throwable e) {
		Trace.catching(ProviderPlugin.PLUGIN_ID, ECFProviderDebugOptions.EXCEPTIONS_CATCHING, SOContainerGMM.class, container.getID() + ":" + msg, e); //$NON-NLS-1$
	}

	protected void handleRuntimeException(Throwable except) {
		except.printStackTrace(System.err);
		traceStack("runner:handleRuntimeException(" + sharedObjectID.getName() + ")", //$NON-NLS-1$ //$NON-NLS-2$
				except);
	}

	protected ISharedObject getSharedObject() {
		return sharedObject;
	}

	public SimpleFIFOQueue getQueue() {
		return queue;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14373.java