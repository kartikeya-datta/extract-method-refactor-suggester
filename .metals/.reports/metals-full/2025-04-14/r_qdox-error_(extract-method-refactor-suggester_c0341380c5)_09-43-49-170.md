error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15743.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15743.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15743.java
text:
```scala
I@@Container[] allContainers = getContainers();

/*******************************************************************************
 * Copyright (c) 2009 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.osgi.services.distribution;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.security.IConnectContext;
import org.eclipse.ecf.discovery.identity.IServiceID;
import org.eclipse.ecf.osgi.services.discovery.IRemoteServiceEndpointDescription;
import org.eclipse.ecf.osgi.services.discovery.RemoteServicePublication;
import org.eclipse.ecf.remoteservice.IRemoteServiceContainer;

/**
 * Default implementation of IProxyContainerFinder.
 * 
 */
public class DefaultProxyContainerFinder extends AbstractContainerFinder
		implements IProxyContainerFinder {

	protected IContainer[] getContainers(IServiceID serviceID,
			IRemoteServiceEndpointDescription endpointDescription) {

		// Get all containers available
		IContainer[] allContainers = getAllContainers();
		// If none then return null
		if (allContainers == null)
			return null;

		List results = new ArrayList();
		for (int i = 0; i < allContainers.length; i++) {
			// Do *not* include containers with same ID as endpoint ID
			ID containerID = allContainers[i].getID();
			if (containerID == null
 containerID
							.equals(endpointDescription.getEndpointAsID()))
				continue;
			// And make sure that the namespaces match
			if (includeContainerWithConnectNamespace(
					allContainers[i],
					(String) endpointDescription
							.getProperty(RemoteServicePublication.ENDPOINT_CONTAINERID_NAMESPACE)))
				results.add(allContainers[i]);
		}
		return (IContainer[]) results.toArray(new IContainer[] {});
	}

	protected IRemoteServiceContainer[] getRemoteServiceContainers(
			IServiceID serviceID,
			IRemoteServiceEndpointDescription endpointDescription) {
		IContainer[] containers = getContainers(serviceID, endpointDescription);
		if (containers == null)
			return null;

		return getRemoteServiceContainers(containers);
	}

	public IRemoteServiceContainer[] findProxyContainers(IServiceID serviceID,
			IRemoteServiceEndpointDescription endpointDescription,
			IProgressMonitor monitor) {
		trace("findProxyContainers", "serviceID=" + serviceID
				+ " endpointDescription=" + endpointDescription);
		// Get remote service containers under consideration
		IRemoteServiceContainer[] rsContainers = getRemoteServiceContainers(
				serviceID, endpointDescription);
		// If none available then return
		if (rsContainers == null) {
			logWarning("findProxyContainers",
					"No remote service containers found");
			return EMPTY_REMOTE_SERVICE_CONTAINER_ARRAY;
		}
		trace("findProxyContainers", "getRemoteServiceContainers.length="
				+ rsContainers.length);

		ID connectTargetID = endpointDescription.getConnectTargetID();
		IRemoteServiceContainer[] connectedContainers = (connectTargetID == null) ? rsContainers
				: connectRemoteServiceContainers(rsContainers, connectTargetID,
						monitor);
		if (connectedContainers == null) {
			logWarning("findProxyContainers",
					"No remote service containers found after connect");
			return EMPTY_REMOTE_SERVICE_CONTAINER_ARRAY;
		}
		trace("findProxyContainers", "connectRemoteServiceContainers.length="
				+ rsContainers.length);
		return connectedContainers;
	}

	protected IConnectContext getConnectContext(
			IRemoteServiceContainer rsContainer, ID connectTargetID) {
		return null;
	}

	protected IRemoteServiceContainer[] connectRemoteServiceContainers(
			IRemoteServiceContainer[] rsContainers, ID connectTargetID,
			IProgressMonitor monitor) {
		List results = new ArrayList();
		for (int i = 0; i < rsContainers.length; i++) {
			IContainer c = rsContainers[i].getContainer();
			try {
				// If the container is not already connected,
				// then connect it to the connectTargetID
				if (c.getConnectedID() == null) {
					connectContainer(c, connectTargetID, getConnectContext(
							rsContainers[i], connectTargetID));
				}
				// If it's connected (either was already connected or was
				// connected via lines above...then add it to result set
				results.add(rsContainers[i]);
			} catch (ContainerConnectException e) {
				logError("connectRemoteServiceContainers",
						"Exception connecting container=" + c.getID()
								+ " to connectTargetID=" + connectTargetID, e);
			}
		}
		return (IRemoteServiceContainer[]) results
				.toArray(new IRemoteServiceContainer[] {});
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15743.java