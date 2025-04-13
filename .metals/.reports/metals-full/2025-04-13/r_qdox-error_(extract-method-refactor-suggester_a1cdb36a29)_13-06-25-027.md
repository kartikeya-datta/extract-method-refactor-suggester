error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13740.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13740.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13740.java
text:
```scala
r@@eturn results;

/*******************************************************************************
 * Copyright (c) 2009 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.osgi.services.distribution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.eclipse.ecf.core.ContainerTypeDescription;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.internal.osgi.services.distribution.Activator;
import org.eclipse.ecf.remoteservice.IRemoteServiceContainer;
import org.eclipse.ecf.remoteservice.IRemoteServiceContainerAdapter;
import org.eclipse.ecf.remoteservice.RemoteServiceContainer;
import org.osgi.framework.ServiceReference;

/**
 * Default implementation of IHostContainerFinder.
 * 
 */
public class DefaultHostContainerFinder extends AbstractContainerFinder
		implements IHostContainerFinder {

	public IRemoteServiceContainer[] findHostContainers(
			ServiceReference serviceReference, String[] remoteInterfaces,
			String[] remoteConfigurationType, String[] remoteRequiresIntents) {
		Collection rsContainers = findRemoteContainersSatisfyingRequiredIntents(remoteRequiresIntents);
		List results = new ArrayList();
		for (Iterator i = rsContainers.iterator(); i.hasNext();) {
			IRemoteServiceContainer rsContainer = (IRemoteServiceContainer) i
					.next();
			if (includeContainer(serviceReference, rsContainer))
				results.add(rsContainer);
		}
		return (IRemoteServiceContainer[]) results
				.toArray(new IRemoteServiceContainer[] {});
	}

	protected Collection findRemoteContainersSatisfyingRequiredIntents(
			String[] remoteRequiresIntents) {
		List results = new ArrayList();
		IContainer[] containers = Activator.getDefault().getContainerManager()
				.getAllContainers();
		if (containers == null || containers.length == 0)
			return null;
		for (int i = 0; i < containers.length; i++) {
			// Check to make sure it's a rs container adapter. If it's not go
			// onto next one
			IRemoteServiceContainerAdapter adapter = (IRemoteServiceContainerAdapter) containers[i]
					.getAdapter(IRemoteServiceContainerAdapter.class);
			if (adapter == null)
				continue;
			// Get container type description and intents
			ContainerTypeDescription description = Activator.getDefault()
					.getContainerManager().getContainerTypeDescription(
							containers[i].getID());
			// If it has no description continue
			if (description == null)
				continue;
			List supportedIntents = Arrays.asList(description
					.getSupportedIntents());
			boolean hasIntents = true;
			if (remoteRequiresIntents != null) {
				for (int j = 0; j < remoteRequiresIntents.length; j++) {
					if (!supportedIntents.contains(remoteRequiresIntents[j]))
						hasIntents = false;
				}
			}
			if (hasIntents) {
				trace("findRemoteContainersSatisfyingRequiredIntents",
						"include containerID=" + containers[i].getID());
				results.add(new RemoteServiceContainer(containers[i], adapter));
			} else {
				trace("findRemoteContainersSatisfyingRequiredIntents",
						"exclude containerID=" + containers[i].getID()
								+ " supported intents=" + supportedIntents);
			}
		}
		return results;
	}

	protected boolean includeContainer(ServiceReference serviceReference,
			IRemoteServiceContainer rsContainer) {
		IContainer container = rsContainer.getContainer();
		Object cID = serviceReference
				.getProperty(org.eclipse.ecf.remoteservice.Constants.SERVICE_CONTAINER_ID);
		// If the SERVICE_CONTAINER_ID property is not set, then we'll include
		// it by default
		if (cID == null || !(cID instanceof ID)) {
			trace(
					"includeContainer",
					"serviceReference="
							+ serviceReference
							+ " does not set remote service container id service property.  INCLUDING containerID="
							+ container.getID() + " in remote registration");
			return true;
		}
		// Or if the id is specified and it's the same as the containerID under
		// consideration
		// then it's included
		ID containerID = (ID) cID;
		if (container.getID().equals(containerID)) {
			trace("includeContainer", "serviceReference=" + serviceReference
					+ " has MATCHING container id=" + containerID
					+ ".  INCLUDING rsca=" + container.getID()
					+ " in remote registration");
			return true;
		}
		trace("includeContainer", "serviceReference=" + serviceReference
				+ " has non-matching id=" + containerID + ".  EXCLUDING id="
				+ container.getID() + " in remote registration");
		return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13740.java