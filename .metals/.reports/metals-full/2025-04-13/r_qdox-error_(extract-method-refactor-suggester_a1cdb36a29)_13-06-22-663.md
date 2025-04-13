error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9243.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9243.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9243.java
text:
```scala
a@@ppLock.notifyAll();

/****************************************************************************
 * Copyright (c) 2009 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ****************************************************************************/
package org.eclipse.ecf.internal.examples.loadbalancing.server;

import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.examples.loadbalancing.IDataProcessor;
import org.eclipse.ecf.remoteservice.IRemoteServiceContainerAdapter;
import org.eclipse.ecf.remoteservice.IRemoteServiceRegistration;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class DataProcessorServerApplication implements IApplication {

	private static final String LB_SERVER_CONTAINER_TYPE = "ecf.jms.activemq.tcp.lb.server";
	private static final String DEFAULT_QUEUE_ID = "tcp://localhost:61616/exampleQueue";

	private BundleContext bundleContext;
	private ServiceTracker containerManagerServiceTracker;
	private String containerType = LB_SERVER_CONTAINER_TYPE;
	
	// Queue that we will attach to as queue message consumer (receiver of
	// actual remote method/invocation
	// requests). Note that this queueId can be changed by using the -queueId
	// launch parameter...e.g.
	// -queueId tcp://localhost:61616/myQueueName
	private String queueId = DEFAULT_QUEUE_ID;

	// Container instance that connects us with the ActiveMQ queue as a message
	// consumer
	private IContainer container;
	// The IDataProcessor service implementation. This object receives the
	// actual
	// remote method calls, executes arbitrary code, and returns a result.
	private DataProcessorImpl dataProcessorImpl;
	// The remote service registration associated with the dataProcessorImpl
	private IRemoteServiceRegistration dataProcessorRemoteServiceRegistration;

	public Object start(IApplicationContext appContext) throws Exception {
		bundleContext = Activator.getContext();
		// Process Arguments...i.e. set queueId if specified
		processArgs(appContext);

		// Create container and connect to given queueId as message consumer
		container = getContainerManagerService().getContainerFactory()
				.createContainer(containerType,
						new Object[] { queueId });

		// Get remote service container adapter
		IRemoteServiceContainerAdapter remoteServiceContainerAdapter = (IRemoteServiceContainerAdapter) container
				.getAdapter(IRemoteServiceContainerAdapter.class);

		// Create the data processor implementation
		dataProcessorImpl = new DataProcessorImpl(container.getID());

		// Register data processor as remote services (with queue consumer
		// container)
		dataProcessorRemoteServiceRegistration = remoteServiceContainerAdapter
				.registerRemoteService(new String[] { IDataProcessor.class
						.getName() }, dataProcessorImpl, null);

		// Report success of registration
		System.out
				.println("LB Server:  Data Processor Registered queue="+queueId);

		// then just wait for service requests
		waitForDone();
		return IApplication.EXIT_OK;
	}

	public void stop() {
		if (dataProcessorRemoteServiceRegistration != null) {
			dataProcessorRemoteServiceRegistration.unregister();
			dataProcessorRemoteServiceRegistration = null;
		}
		if (dataProcessorImpl != null) {
			dataProcessorImpl.stop();
			dataProcessorImpl = null;
		}
		if (container != null) {
			container.dispose();
			container = null;
			getContainerManagerService().removeAllContainers();
		}
		if (containerManagerServiceTracker != null) {
			containerManagerServiceTracker.close();
			containerManagerServiceTracker = null;
		}
		bundleContext = null;
		synchronized (appLock) {
			done = true;
			notifyAll();
		}
	}

	private void processArgs(IApplicationContext appContext) {
		String[] originalArgs = (String[]) appContext.getArguments().get(
				"application.args");
		if (originalArgs == null)
			return;
		for (int i = 0; i < originalArgs.length; i++) {
			if (originalArgs[i].equals("-queueId")) {
				queueId = originalArgs[i + 1];
				i++;
			} else if (originalArgs[i].equals("-containerType")) {
				containerType = originalArgs[i + 1];
				i++;
			}
		}
	}

	private IContainerManager getContainerManagerService() {
		if (containerManagerServiceTracker == null) {
			containerManagerServiceTracker = new ServiceTracker(bundleContext,
					IContainerManager.class.getName(), null);
			containerManagerServiceTracker.open();
		}
		return (IContainerManager) containerManagerServiceTracker.getService();
	}

	private final Object appLock = new Object();
	private boolean done = false;

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9243.java