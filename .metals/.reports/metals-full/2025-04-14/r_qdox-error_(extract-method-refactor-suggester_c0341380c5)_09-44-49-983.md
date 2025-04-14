error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10053.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10053.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10053.java
text:
```scala
p@@ublic class DiscoveredEndpointDescriptionFactory extends

/*******************************************************************************
 * Copyright (c) 2010 Composent, Inc. and others. All rights reserved. This
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

import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.discovery.IDiscoveryLocator;
import org.eclipse.ecf.discovery.IServiceInfo;
import org.eclipse.ecf.discovery.IServiceProperties;
import org.eclipse.ecf.discovery.identity.IServiceID;
import org.osgi.service.remoteserviceadmin.EndpointDescription;

public class DefaultDiscoveredEndpointDescriptionFactory extends
		AbstractMetadataFactory implements
		IDiscoveredEndpointDescriptionFactory {

	protected List<DiscoveredEndpointDescription> discoveredEndpointDescriptions = new ArrayList();

	private DiscoveredEndpointDescription findDiscoveredEndpointDescription(
			org.osgi.service.remoteserviceadmin.EndpointDescription endpointDescription) {
		synchronized (discoveredEndpointDescriptions) {
			for (DiscoveredEndpointDescription d : discoveredEndpointDescriptions) {
				org.osgi.service.remoteserviceadmin.EndpointDescription ed = d
						.getEndpointDescription();
				if (ed.equals(endpointDescription))
					return d;
			}
		}
		return null;
	}

	private DiscoveredEndpointDescription findUniscoveredEndpointDescription(
			IDiscoveryLocator locator, IServiceID serviceID) {
		synchronized (discoveredEndpointDescriptions) {
			for (DiscoveredEndpointDescription d : discoveredEndpointDescriptions) {
				Namespace dln = d.getDiscoveryLocatorNamespace();
				IServiceID svcId = d.getServiceID();
				if (dln.getName().equals(
						locator.getServicesNamespace().getName())
						&& svcId.equals(serviceID)) {
					return d;
				}
			}
		}
		return null;
	}

	public DiscoveredEndpointDescription createDiscoveredEndpointDescription(
			IDiscoveryLocator locator, IServiceInfo discoveredServiceInfo) {
		try {
			org.osgi.service.remoteserviceadmin.EndpointDescription endpointDescription = createEndpointDescription(
					locator, discoveredServiceInfo);
			synchronized (discoveredEndpointDescriptions) {
				DiscoveredEndpointDescription ded = findDiscoveredEndpointDescription(endpointDescription);
				if (ded != null)
					return ded;
				else {
					ded = createDiscoveredEndpointDescription(locator,
							discoveredServiceInfo, endpointDescription);
					// put into discoveredEndpointDescriptions
					discoveredEndpointDescriptions.add(ded);
					return ded;
				}
			}
		} catch (Exception e) {
			logError("createDiscoveredEndpointDescription",
					"Exception creating discovered endpoint description", e);
			return null;
		}
	}

	public DiscoveredEndpointDescription getUndiscoveredEndpointDescription(
			IDiscoveryLocator locator, IServiceID serviceID) {
		synchronized (discoveredEndpointDescriptions) {
			DiscoveredEndpointDescription ded = findUniscoveredEndpointDescription(
					locator, serviceID);
			if (ded != null) {
				// remove
				discoveredEndpointDescriptions.remove(ded);
				return ded;
			}
		}
		return null;
	}

	protected org.osgi.service.remoteserviceadmin.EndpointDescription createEndpointDescription(
			IDiscoveryLocator locator, IServiceInfo discoveredServiceInfo) {
		IServiceProperties discoveredServiceProperties = discoveredServiceInfo
				.getServiceProperties();
		return decodeEndpointDescription(discoveredServiceProperties);

	}

	protected DiscoveredEndpointDescription createDiscoveredEndpointDescription(
			IDiscoveryLocator locator,
			IServiceInfo discoveredServiceInfo,
			org.osgi.service.remoteserviceadmin.EndpointDescription endpointDescription) {
		return new DiscoveredEndpointDescription(
				locator.getServicesNamespace(),
				discoveredServiceInfo.getServiceID(), endpointDescription);
	}

	public void close() {
		removeAllEndpointDescriptions();
		super.close();
	}

	public boolean removeEndpointDescription(
			EndpointDescription endpointDescription) {
		synchronized (discoveredEndpointDescriptions) {
			DiscoveredEndpointDescription d = findDiscoveredEndpointDescription(endpointDescription);
			if (d != null) {
				discoveredEndpointDescriptions.remove(d);
				return true;
			}
		}
		return false;
	}

	public void removeAllEndpointDescriptions() {
		synchronized (discoveredEndpointDescriptions) {
			discoveredEndpointDescriptions.clear();
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10053.java