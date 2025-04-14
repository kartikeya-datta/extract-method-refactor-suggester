error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18018.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18018.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18018.java
text:
```scala
(@@IStatus[]) statuses.toArray(new IStatus[statuses.size()]), errorMessage, null);

/*******************************************************************************
 * Copyright (c) 2010-2011 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.osgi.services.remoteserviceadmin;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ecf.discovery.IDiscoveryAdvertiser;
import org.eclipse.ecf.discovery.IServiceInfo;
import org.eclipse.ecf.internal.osgi.services.remoteserviceadmin.Activator;
import org.osgi.service.remoteserviceadmin.EndpointDescription;

public class EndpointDescriptionAdvertiser implements
		IEndpointDescriptionAdvertiser {

	private EndpointDescriptionLocator endpointDescriptionLocator;

	public EndpointDescriptionAdvertiser(
			EndpointDescriptionLocator endpointDescriptionLocator) {
		this.endpointDescriptionLocator = endpointDescriptionLocator;
	}

	public IStatus advertise(EndpointDescription endpointDescription) {
		return doDiscovery(endpointDescription, true);
	}

	protected IStatus doDiscovery(IDiscoveryAdvertiser discoveryAdvertiser,
			IServiceInfo serviceInfo, boolean advertise) {
		try {
			if (advertise)
				discoveryAdvertiser.registerService(serviceInfo);
			else
				discoveryAdvertiser.unregisterService(serviceInfo);
			return Status.OK_STATUS;
		} catch (Exception e) {
			return createErrorStatus((advertise ? "registerService"
					: "unregisterService")
					+ " with serviceInfo="
					+ serviceInfo
					+ " for discoveryAdvertiser="
					+ discoveryAdvertiser
					+ " failed", e);
		}
	}

	protected IServiceInfoFactory getServiceInfoFactory() {
		return endpointDescriptionLocator.getServiceInfoFactory();
	}

	protected IDiscoveryAdvertiser[] getDiscoveryAdvertisers() {
		return endpointDescriptionLocator.getDiscoveryAdvertisers();
	}

	protected IStatus createErrorStatus(String message) {
		return createErrorStatus(message, null);
	}

	protected IStatus createErrorStatus(String message, Throwable e) {
		return new Status(IStatus.ERROR, Activator.PLUGIN_ID, message, e);
	}

	protected IStatus doDiscovery(EndpointDescription endpointDescription,
			boolean advertise) {
		Assert.isNotNull(endpointDescription);
		String messagePrefix = advertise ? "Advertise" : "Unadvertise";
		List<IStatus> statuses = new ArrayList<IStatus>();
		if (endpointDescription instanceof org.eclipse.ecf.osgi.services.remoteserviceadmin.EndpointDescription) {
			org.eclipse.ecf.osgi.services.remoteserviceadmin.EndpointDescription eed = (org.eclipse.ecf.osgi.services.remoteserviceadmin.EndpointDescription) endpointDescription;
			// First get serviceInfoFactory
			IServiceInfoFactory serviceInfoFactory = getServiceInfoFactory();
			if (serviceInfoFactory == null)
				return createErrorStatus(messagePrefix
						+ " endpointDescription="
						+ endpointDescription
						+ ".  No IServiceInfoFactory is available.  Cannot unpublish endpointDescription="
						+ eed);
			IDiscoveryAdvertiser[] discoveryAdvertisers = getDiscoveryAdvertisers();
			if (discoveryAdvertisers == null
 discoveryAdvertisers.length == 0)
				return createErrorStatus(messagePrefix
						+ " endpointDescription="
						+ endpointDescription
						+ ".  No endpointDescriptionLocator advertisers available.  Cannot unpublish endpointDescription="
						+ eed);
			for (int i = 0; i < discoveryAdvertisers.length; i++) {
				IServiceInfo serviceInfo = (advertise ? serviceInfoFactory
						.createServiceInfoForDiscovery(discoveryAdvertisers[i],
								eed) : serviceInfoFactory
						.removeServiceInfoForUndiscovery(
								discoveryAdvertisers[i], eed));
				if (serviceInfo == null) {
					statuses.add(createErrorStatus(messagePrefix
							+ " endpointDescription="
							+ endpointDescription
							+ ".  Service Info is null.  Cannot publish endpointDescription="
							+ eed));
					continue;
				}
				// Now actually unregister with advertiser
				statuses.add(doDiscovery(discoveryAdvertisers[i], serviceInfo,
						advertise));
			}
		} else {
			statuses.add(createErrorStatus(messagePrefix
					+ " endpointDescription="
					+ endpointDescription
					+ " is not of understood EndpointDescription type.  Not advertising."));
		}
		return createResultStatus(statuses, messagePrefix
				+ " endpointDesription=" + endpointDescription
				+ ".  Problem in unadvertise");
	}

	public IStatus unadvertise(EndpointDescription endpointDescription) {
		return doDiscovery(endpointDescription, false);
	}

	private IStatus createResultStatus(List<IStatus> statuses,
			String errorMessage) {
		List<IStatus> errorStatuses = new ArrayList<IStatus>();
		for (IStatus status : statuses)
			if (!status.isOK())
				errorStatuses.add(status);
		if (errorStatuses.size() > 0)
			return new MultiStatus(Activator.PLUGIN_ID, IStatus.ERROR,
					(IStatus[]) statuses.toArray(), errorMessage, null);
		else
			return Status.OK_STATUS;
	}

	public void close() {
		this.endpointDescriptionLocator = null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18018.java