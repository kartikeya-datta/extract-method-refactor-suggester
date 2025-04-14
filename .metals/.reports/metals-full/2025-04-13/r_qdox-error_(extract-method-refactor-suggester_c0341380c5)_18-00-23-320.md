error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/204.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/204.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/204.java
text:
```scala
s@@uper("SLP Discovery"); //$NON-NLS-1$

/*******************************************************************************
 * Copyright (c) 2007 Versant Corp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Markus Kuppe (mkuppe <at> versant <dot> com) - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.internal.provider.jslp;

import ch.ethz.iks.slp.ServiceLocationException;
import ch.ethz.iks.slp.ServiceURL;
import java.util.*;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ecf.core.util.Trace;
import org.eclipse.ecf.discovery.IServiceInfo;
import org.eclipse.ecf.provider.jslp.container.JSLPDiscoveryContainer;
import org.eclipse.ecf.provider.jslp.container.JSLPServiceInfo;
import org.osgi.framework.Bundle;

public final class JSLPDiscoveryJob extends Job {

	private final JSLPDiscoveryContainer discoveryContainer;
	private final Map services;

	public JSLPDiscoveryJob(final JSLPDiscoveryContainer container) {
		super(Messages.JSLPDiscoveryJob_TITLE);
		discoveryContainer = container;
		services = Collections.synchronizedMap(new HashMap());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IStatus run(final IProgressMonitor monitor) {
		Assert.isNotNull(monitor);
		try {
			final Map availableServices = Activator.getDefault().getLocator().getServiceURLs();
			final Map removedServices = new HashMap(services);
			for (final Iterator itr = availableServices.entrySet().iterator(); itr.hasNext() && !monitor.isCanceled();) {
				final Map.Entry entry = (Map.Entry) itr.next();
				final ServiceURL url = (ServiceURL) entry.getKey();
				// do we know the service already?
				if (removedServices.containsKey(url)) {
					removedServices.remove(url);
				} else { // we don't know the service, so we need to create the
					final ServicePropertiesAdapter spa = new ServicePropertiesAdapter((List) entry.getValue());
					final String serviceName = spa.getServiceName() == null ? url.toString() : spa.getServiceName();
					final IServiceInfo serviceInfo = new JSLPServiceInfo(serviceName, new ServiceURLAdapter(url), spa.getPriority(), spa.getWeight(), spa);
					services.put(url, serviceInfo);
					discoveryContainer.fireServiceTypeDiscovered(serviceInfo.getServiceID().getServiceTypeID());
					discoveryContainer.fireServiceDiscovered(serviceInfo);
				}
				monitor.worked(1);
			}
			// at this point removedServices only contains stale services
			for (final Iterator itr = removedServices.entrySet().iterator(); itr.hasNext() && !monitor.isCanceled();) {
				final Map.Entry entry = (Map.Entry) itr.next();
				final Object key = entry.getKey();
				final IServiceInfo value = (IServiceInfo) entry.getValue();
				discoveryContainer.fireServiceUndiscovered(value);
				services.remove(key);
				monitor.worked(1);
			}

		} catch (final ServiceLocationException e) {
			// TODO-mkuppe if the advertiser is gone, we run into this exception
			// but we have to let the listeners know about the gone services
			// too
			Trace.catching(Activator.PLUGIN_ID, JSLPDebugOptions.EXCEPTIONS_CATCHING, this.getClass(), "run", e); //$NON-NLS-1$
		}

		// check if the JSLPDiscoveryContainer has been disconnected or disposed
		if (discoveryContainer.getConnectedID() != null) {
			this.schedule(JSLPDiscoveryContainer.REDISCOVER);
		}
		return Status.OK_STATUS;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.Job#shouldRun()
	 */
	public boolean shouldRun() {
		if (Activator.getDefault() != null) {// system went down, no bundle
			final int state = Activator.getDefault().getBundle().getState();
			return (state == Bundle.ACTIVE || state == Bundle.STARTING);
		}
		return false;
	}

	public Collection purgeCache() {
		Set unmodifiableSet = Collections.unmodifiableSet(services.entrySet());
		services.clear();
		return unmodifiableSet;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/204.java