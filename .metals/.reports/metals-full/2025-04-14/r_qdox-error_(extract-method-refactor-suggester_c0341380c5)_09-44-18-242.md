error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11587.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11587.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11587.java
text:
```scala
p@@rops.put(SERVICE_EXPORTED_INTERFACES, SERVICE_EXPORTED_INTERFACES_WILDCARD);

/****************************************************************************
 * Copyright (c) 2009 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 *****************************************************************************/
package org.eclipse.ecf.internal.examples.remoteservices.hello.host;

import java.util.Properties;

import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.examples.remoteservices.hello.IHello;
import org.eclipse.ecf.examples.remoteservices.hello.impl.Hello;
import org.eclipse.ecf.osgi.services.distribution.IDistributionConstants;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

public class HelloHostApplication implements IApplication,
		IDistributionConstants {

	private static final String DEFAULT_CONTAINER_TYPE = "ecf.r_osgi.peer";
	public static final String DEFAULT_CONTAINER_ID = null;

	private BundleContext bundleContext;
	private ServiceTracker containerManagerServiceTracker;

	private String containerType = DEFAULT_CONTAINER_TYPE;
	private String containerId = DEFAULT_CONTAINER_ID;

	private final Object appLock = new Object();
	private boolean done = false;

	private ServiceRegistration helloRegistration;

	public Object start(IApplicationContext appContext) throws Exception {
		bundleContext = Activator.getContext();
		// Process Arguments
		processArgs(appContext);
		// Create Container of desired type
		IContainerManager containerManager = getContainerManagerService();
		if (containerId == null || "".equals(containerId))
			containerManager.getContainerFactory().createContainer(
					containerType);
		else
			containerManager.getContainerFactory().createContainer(
					containerType, new Object[] { containerId });

		Properties props = new Properties();
		// add OSGi service property indicating this
		props.put(REMOTE_INTERFACES, REMOTE_INTERFACES_WILDCARD);
		// register remote service
		helloRegistration = bundleContext.registerService(IHello.class
				.getName(), new Hello(), props);
		System.out.println("Host: Hello Service Registered");

		// wait until stopped
		waitForDone();

		return IApplication.EXIT_OK;
	}

	public void stop() {
		if (helloRegistration != null) {
			helloRegistration.unregister();
			helloRegistration = null;
		}
		if (containerManagerServiceTracker != null) {
			containerManagerServiceTracker.close();
			containerManagerServiceTracker = null;
		}
		bundleContext = null;
	}

	private IContainerManager getContainerManagerService() {
		if (containerManagerServiceTracker == null) {
			containerManagerServiceTracker = new ServiceTracker(bundleContext,
					IContainerManager.class.getName(), null);
			containerManagerServiceTracker.open();
		}
		return (IContainerManager) containerManagerServiceTracker.getService();
	}

	private void processArgs(IApplicationContext appContext) {
		String[] originalArgs = (String[]) appContext.getArguments().get(
				"application.args");
		if (originalArgs == null)
			return;
		for (int i = 0; i < originalArgs.length; i++) {
			if (originalArgs[i].equals("-containerType")) {
				containerType = originalArgs[i + 1];
				i++;
			} else if (originalArgs[i].equals("-containerId")) {
				containerId = originalArgs[i + 1];
				i++;
			}
		}
	}

	private void waitForDone() {
		// then just wait here
		synchronized (appLock) {
			while (!done) {
				try {
					appLock.wait();
				} catch (InterruptedException e) {
					// do nothing
				}
			}
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11587.java