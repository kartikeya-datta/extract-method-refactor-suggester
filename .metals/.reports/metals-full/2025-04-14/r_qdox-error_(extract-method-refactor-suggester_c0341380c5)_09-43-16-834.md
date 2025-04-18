error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12247.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12247.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12247.java
text:
```scala
.@@getProperty(Constants.SERVICE_NAMESPACE);

/*******************************************************************************
 * Copyright (c) 2009 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.internal.osgi.services.distribution;

import java.util.*;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.core.util.Trace;
import org.eclipse.ecf.remoteservice.*;
import org.eclipse.equinox.concurrent.future.IFuture;
import org.eclipse.equinox.concurrent.future.TimeoutException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.discovery.*;

public class DiscoveredServiceTrackerImpl implements DiscoveredServiceTracker {

	DistributionProviderImpl distributionProvider;

	public DiscoveredServiceTrackerImpl(DistributionProviderImpl dp) {
		this.distributionProvider = dp;
	}

	// IRemoteServiceReference -> ServiceRegistration
	Map serviceRegistrationMap = Collections.synchronizedMap(new HashMap());

	public void serviceChanged(DiscoveredServiceNotification notification) {
		if (notification == null) {
			logError("DiscoveredServiceNotification is null", null);
			return;
		}
		Trace.entering(Activator.PLUGIN_ID, DebugOptions.DEBUG,
				this.getClass(), "serviceChanged", notification);
		int notificationType = notification.getType();
		switch (notificationType) {
		case DiscoveredServiceNotification.AVAILABLE:
			handleDiscoveredServiceAvailable(notification
					.getServiceEndpointDescription());
			break;
		case DiscoveredServiceNotification.UNAVAILABLE:
			handleDiscoveredServiceUnavailable(notification
					.getServiceEndpointDescription());
			break;
		case DiscoveredServiceNotification.MODIFIED:
			handleDiscoveredServiceModified(notification
					.getServiceEndpointDescription());
			break;
		case DiscoveredServiceNotification.MODIFIED_ENDMATCH:
			handleDiscoveredServiceModifiedEndmatch(notification
					.getServiceEndpointDescription());
			break;
		default:
			logError("DiscoveredServiceNotification type=" + notificationType
					+ " not found", null);
			break;
		}
	}

	private IContainerManager getContainerManager() {
		return Activator.getDefault().getContainerManager();
	}

	private void handleDiscoveredServiceModifiedEndmatch(
			ServiceEndpointDescription description) {
		// TODO Auto-generated method stub

	}

	private void handleDiscoveredServiceModified(
			ServiceEndpointDescription description) {
		// TODO Auto-generated method stub

	}

	private void handleDiscoveredServiceUnavailable(
			ServiceEndpointDescription description) {
		// TODO Auto-generated method stub

	}

	private void handleDiscoveredServiceAvailable(
			ServiceEndpointDescription description) {
		Collection providedInterfaces = description.getProvidedInterfaces();
		if (providedInterfaces == null) {
			logError("ServiceEndpointDescription providedInterfaces is null",
					null);
			return;
		}
		// Find RSCAs for the given description
		IRemoteServiceContainerAdapter[] rscas = findRSCAs(description);
		if (rscas == null) {
			logError("No RemoteServiceContainerAdapters found for description "
					+ description, null);
			return;
		}
		for (int i = 0; i < rscas.length; i++) {
			for (Iterator j = providedInterfaces.iterator(); j.hasNext();) {
				String providedInterface = (String) j.next();
				// Use async call to prevent blocking here
				IFuture futureRemoteReferences = rscas[i]
						.asyncGetRemoteServiceReferences(
								createContainerIDsForQuery(description),
								providedInterface,
								getRemoteFilterForQuery(description));
				// And process the future returned in separate thread
				processFutureForRemoteServiceReferences(futureRemoteReferences,
						rscas[i], description, getTimeout(description));
			}
		}
	}

	private String getRemoteFilterForQuery(
			ServiceEndpointDescription description) {
		return null;
	}

	long getTimeout(ServiceEndpointDescription description) {
		// for now return constant of 30s
		return 30000;
	}

	private void processFutureForRemoteServiceReferences(
			final IFuture futureRemoteReferences,
			final IRemoteServiceContainerAdapter rsca,
			final ServiceEndpointDescription description, final long timeout) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					// First get remote service references
					IRemoteServiceReference[] remoteReferences = (IRemoteServiceReference[]) futureRemoteReferences
							.get(timeout);
					IStatus futureStatus = futureRemoteReferences.getStatus();
					if (futureStatus.isOK() && remoteReferences != null
							&& remoteReferences.length > 0) {
						registerRemoteServiceReferences(rsca, remoteReferences,
								description);
					} else {
						logFutureError(futureStatus);
					}
				} catch (InterruptedException e) {
					logError("Retrieval of remote references interrupted", e);
				} catch (OperationCanceledException e) {
					logError("Retrieval of remote references cancelled", e);
				} catch (TimeoutException e) {
					logError("Retrieval of remote references timedout after "
							+ e.getDuration(), e);
				}
			}
		});
		t.start();
	}

	void logFutureError(IStatus futureStatus) {
		logError("Future error: " + futureStatus.getMessage(), futureStatus
				.getException());
	}

	private ServiceRegistration getRemoteServiceRegistration(
			IRemoteServiceReference reference) {
		// XXX TODO...consult serviceRegistrationMap
		return null;
	}

	private ServiceRegistration addRemoteServiceRegistration(
			IRemoteServiceReference ref, ServiceRegistration registration) {
		// XXX TODO...add to serviceRegistrationMap
		return null;
	}

	private void registerRemoteServiceReferences(
			IRemoteServiceContainerAdapter rsca,
			IRemoteServiceReference[] remoteReferences,
			ServiceEndpointDescription description) {
		// First, get IRemoteService instances for remote references
		for (int i = 0; i < remoteReferences.length; i++) {
			// Otherwise we register it.
			IRemoteService remoteService = rsca
					.getRemoteService(remoteReferences[i]);
			if (remoteService == null) {
				logError("remote service is null for remote reference "
						+ remoteReferences[i], null);
				continue;
			}
			String[] clazzes = getClazzesForRemoteServiceReference(
					remoteReferences[i], description);
			if (clazzes == null) {
				logError("no classes specified for remote service reference "
						+ remoteReferences[i], null);
				continue;
			}
			Dictionary properties = getPropertiesForRemoteServiceReference(
					remoteReferences[i], description);
			Object proxy = null;
			try {
				proxy = remoteService.getProxy();
			} catch (ECFException e) {
				logError(
						"Exception creating proxy for remote service reference "
								+ remoteReferences[i], e);
				continue;
			}
			BundleContext bundleContext = Activator.getDefault().getContext();
			// Has to be synchronized on map so that additions do not occur
			// while this is going on
			synchronized (serviceRegistrationMap) {
				// First check to see if remote reference is already registered
				ServiceRegistration reg = getRemoteServiceRegistration(remoteReferences[i]);
				if (reg != null) {
					// log the fact that it's already registered
					logError("remote reference " + remoteReferences
							+ " already registered locally", null);
					continue;
				}
				try {
					// Finally register
					ServiceRegistration registration = bundleContext
							.registerService(clazzes, proxy, properties);
					addRemoteServiceRegistration(remoteReferences[i],
							registration);
				} catch (Exception e) {
					logError("Error registering for remote reference "
							+ remoteReferences[i], e);
					continue;
				}
			}
		}
	}

	private Dictionary getPropertiesForRemoteServiceReference(
			IRemoteServiceReference remoteServiceReference,
			ServiceEndpointDescription description) {
		// TODO Auto-generated method stub
		return null;
	}

	private String[] getClazzesForRemoteServiceReference(
			IRemoteServiceReference iRemoteServiceReference,
			ServiceEndpointDescription description) {
		// TODO Auto-generated method stub
		return null;
	}

	private ID[] createContainerIDsForQuery(
			ServiceEndpointDescription description) {
		// XXX if there is a container id in the ServiceEndpointDescription
		// service properties, then we should retrieve it
		// and return in array here
		return null;
	}

	private IRemoteServiceContainerAdapter[] findRSCAs(
			ServiceEndpointDescription description) {
		IContainerManager containerManager = getContainerManager();
		if (containerManager == null)
			return null;
		IContainer[] containers = containerManager.getAllContainers();
		if (containers == null) {
			// log this?
			return null;
		}
		List results = new ArrayList();
		for (int i = 0; i < containers.length; i++) {
			IRemoteServiceContainerAdapter adapter = (IRemoteServiceContainerAdapter) containers[i]
					.getAdapter(IRemoteServiceContainerAdapter.class);
			if (adapter != null
					&& includeRCSAForDescription(containers[i], adapter,
							description)) {
				results.add(adapter);
			}
		}
		return (IRemoteServiceContainerAdapter[]) results
				.toArray(new IRemoteServiceContainerAdapter[] {});
	}

	private boolean includeRCSAForDescription(IContainer container,
			IRemoteServiceContainerAdapter adapter,
			ServiceEndpointDescription description) {
		String namespaceName = (String) description
				.getProperty(Constants.REMOTESERVICE_NAMESPACE_NAME);
		if (namespaceName != null) {
			Namespace namespace = adapter.getRemoteServiceNamespace();
			if (namespace.getName().equals(namespaceName))
				return true;
		}
		return true;
	}

	private void logError(String string, Throwable t) {
		// XXX TODO
		System.err.println(string);
		if (t != null)
			t.printStackTrace(System.err);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12247.java