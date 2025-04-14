error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6788.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6788.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6788.java
text:
```scala
s@@erviceInfo) : factory

/*******************************************************************************
 * Copyright (c) 2010 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.internal.osgi.services.remoteserviceadmin;

import java.util.Arrays;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.ecf.discovery.IServiceEvent;
import org.eclipse.ecf.discovery.IServiceInfo;
import org.eclipse.ecf.discovery.IServiceListener;
import org.eclipse.ecf.discovery.identity.IServiceID;
import org.eclipse.ecf.internal.osgi.services.remoteserviceadmin.Activator.EndpointListenerHolder;
import org.eclipse.ecf.osgi.services.remoteserviceadmin.IEndpointDescriptionFactory;
import org.eclipse.osgi.framework.eventmgr.CopyOnWriteIdentityMap;
import org.eclipse.osgi.framework.eventmgr.EventDispatcher;
import org.eclipse.osgi.framework.eventmgr.EventManager;
import org.eclipse.osgi.framework.eventmgr.ListenerQueue;
import org.osgi.service.remoteserviceadmin.EndpointDescription;
import org.osgi.service.remoteserviceadmin.EndpointListener;

class LocatorServiceListener implements IServiceListener {

	private Object listenerLock = new Object();

	private ListenerQueue queue;
	private EventManager eventManager;

	class EndpointListenerEvent {
		
		private EndpointListenerHolder holder;
		private boolean discovered;

		public EndpointListenerEvent(EndpointListenerHolder holder,
				boolean discovered) {
			this.holder = holder;
			this.discovered = discovered;
		}

		public EndpointListenerHolder getEndpointListenerHolder() {
			return holder;
		}

		public boolean isDiscovered() {
			return discovered;
		}
	}

	public LocatorServiceListener() {
		ThreadGroup eventGroup = new ThreadGroup(
				"EventAdmin EndpointListener Dispatcher"); //$NON-NLS-1$
		eventGroup.setDaemon(true);
		eventManager = new EventManager(
				"EventAdmin EndpointListener Dispatcher", eventGroup); //$NON-NLS-1$
		queue = new ListenerQueue(eventManager);
		CopyOnWriteIdentityMap listeners = new CopyOnWriteIdentityMap();
		listeners.put(this, this);
		queue.queueListeners(listeners.entrySet(), new EventDispatcher() {
			public void dispatchEvent(Object eventListener,
					Object listenerObject, int eventAction, Object eventObject) {
				EndpointListenerHolder endpointListenerHolder = ((EndpointListenerEvent) eventObject).getEndpointListenerHolder();
				final boolean discovered = ((EndpointListenerEvent) eventObject).isDiscovered();
				
				final EndpointListener endpointListener = endpointListenerHolder.getListener();
				final EndpointDescription endpointDescription = endpointListenerHolder
						.getDescription();
				final String matchingFilter = endpointListenerHolder
						.getMatchingFilter();
				
				// run with SafeRunner, so that any exceptions are logged by
				// our logger
				SafeRunner.run(new ISafeRunnable() {
					public void handleException(Throwable exception) {
						logError("Exception notifying EndpointListener ",
								exception);
						Activator a = Activator.getDefault();
						if (a != null)
							a.log(new Status(
									IStatus.ERROR,
									Activator.PLUGIN_ID,
									IStatus.ERROR,
									"Exception in EndpointListener listener="+endpointListener+" description="+endpointDescription+" matchingFilter="+matchingFilter, exception)); //$NON-NLS-1$
					}

					public void run() throws Exception {
						// Call endpointAdded or endpointRemoved
						if (discovered)
							endpointListener.endpointAdded(endpointDescription, matchingFilter);
						else
							endpointListener.endpointRemoved(endpointDescription,
									matchingFilter);
					}
				});
			}
		});
	}

	public void serviceDiscovered(IServiceEvent anEvent) {
		handleService(anEvent.getServiceInfo(), true);
	}

	public void serviceUndiscovered(IServiceEvent anEvent) {
		handleService(anEvent.getServiceInfo(), false);
	}

	private boolean matchServiceID(IServiceID serviceId) {
		if (Arrays.asList(serviceId.getServiceTypeID().getServices()).contains(
				"osgiservices"))
			return true;
		return false;
	}

	void handleService(IServiceInfo serviceInfo, boolean discovered) {
		IServiceID serviceID = serviceInfo.getServiceID();
		if (matchServiceID(serviceID))
			handleOSGiServiceEndpoint(serviceID, serviceInfo, true);
	}

	private void handleOSGiServiceEndpoint(IServiceID serviceId,
			IServiceInfo serviceInfo, boolean discovered) {
		synchronized (listenerLock) {
			EndpointDescription description = createEndpointDescription(
					serviceId, serviceInfo, discovered);
			if (description != null) {
				Activator.EndpointListenerHolder[] endpointListenerHolders = Activator
						.getDefault().getMatchingEndpointListenerHolders(
								description);
				if (endpointListenerHolders != null) {
					for (int i = 0; i < endpointListenerHolders.length; i++) {
						queue.dispatchEventAsynchronous(0,
								new EndpointListenerEvent(
										endpointListenerHolders[i], discovered));
					}
				} else {
					logError("No matching EndpointListeners found for"
							+ (discovered ? "discovered" : "undiscovered")
							+ " serviceInfo=" + serviceInfo);
				}
			}
		}
	}

	private void logError(String message) {
		logError(message, null);
	}

	private void logError(String message, Throwable t) {
		Activator a = Activator.getDefault();
		if (a != null) {
			a.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, message, t));
		}
	}

	private EndpointDescription createEndpointDescription(IServiceID serviceId,
			IServiceInfo serviceInfo, boolean discovered) {
		// Get activator
		Activator activator = Activator.getDefault();
		if (activator == null)
			return null;
		// Get IEndpointDescriptionFactory
		IEndpointDescriptionFactory factory = activator
				.getEndpointDescriptionFactory();
		if (factory == null) {
			logError("No IEndpointDescriptionFactory found, could not create EndpointDescription for "
					+ (discovered ? "discovered" : "undiscovered")
					+ " serviceInfo=" + serviceInfo);
			return null;
		}
		try {
		// Else get endpoint description factory to create EndpointDescription
		// for given serviceID and serviceInfo
		return (discovered) ? factory.createDiscoveredEndpointDescription(
				serviceId, serviceInfo) : factory
				.getUndiscoveredEndpointDescription(serviceId, serviceInfo);
		} catch (Exception e) {
			logError("Exception calling IEndpointDescriptionFactory."+((discovered)?"createDiscoveredEndpointDescription":"getUndiscoveredEndpointDescription"), e);
			return null;
		} catch (NoClassDefFoundError e) {
			logError("NoClassDefFoundError calling IEndpointDescriptionFactory."+((discovered)?"createDiscoveredEndpointDescription":"getUndiscoveredEndpointDescription"), e);
			return null;
		}
	}

	public void close() {
		if (eventManager != null) {
			eventManager.close();
			eventManager = null;
			queue = null;
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6788.java