error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2819.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2819.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2819.java
text:
```scala
M@@ap availableServices = Activator.getDefault().getLocator().getServiceURLs();

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

	private JSLPDiscoveryContainer discoveryContainer;
	private Map services;

	public JSLPDiscoveryJob(JSLPDiscoveryContainer container) {
		super(Messages.JSLPDiscoveryJob_TITLE);
		discoveryContainer = container;
		services = Collections.synchronizedMap(new HashMap());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IStatus run(IProgressMonitor monitor) {
		Assert.isNotNull(monitor);
		try {
			Map availableServices = Activator.getDefault().getServiceURLs();
			Map removedServices = new HashMap(services);
			for (Iterator itr = availableServices.entrySet().iterator(); itr.hasNext() && !monitor.isCanceled();) {
				Map.Entry entry = (Map.Entry) itr.next();
				ServiceURL url = (ServiceURL) entry.getKey();
				// do we know the service already?
				if (removedServices.containsKey(url)) {
					removedServices.remove(url);
				} else { // we don't know the service, so we need to create the
					// service discovery object
					//TODO-mkuppe do we get meaningful values for ServiceProperties (SLP attributes?), priority and weight from SLP?
					IServiceInfo serviceInfo = new JSLPServiceInfo(new ServiceURLAdapter(url), -1, -1, new ServicePropertiesAdapter((List) entry.getValue()));
					services.put(url, serviceInfo);
					discoveryContainer.fireServiceDiscovered(serviceInfo);
				}
				monitor.worked(1);
			}
			// at this point removedServices only contains stale services
			for (Iterator itr = removedServices.keySet().iterator(); itr.hasNext() && !monitor.isCanceled();) {
				Object key = itr.next();
				discoveryContainer.fireServiceUndiscovered((IServiceInfo) removedServices.get(key));
				services.remove(key);
				monitor.worked(1);
			}

		} catch (ServiceLocationException e) {
			// TODO-mkuppe if the advertiser is gone, we run into this exception
			// but we have to let the listeners know about the gone services
			// too
			Trace.catching(Activator.PLUGIN_ID, JSLPDebugOptions.EXCEPTIONS_CATCHING, this.getClass(), "run", e); //$NON-NLS-1$
		}

		this.schedule(JSLPDiscoveryContainer.REDISCOVER);
		return Status.OK_STATUS;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.Job#shouldRun()
	 */
	public boolean shouldRun() {
		if (Activator.getDefault() != null) {// system went down, no bundle
			int state = Activator.getDefault().getBundle().getState();
			return (state == Bundle.ACTIVE || state == Bundle.STARTING);
		}
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2819.java