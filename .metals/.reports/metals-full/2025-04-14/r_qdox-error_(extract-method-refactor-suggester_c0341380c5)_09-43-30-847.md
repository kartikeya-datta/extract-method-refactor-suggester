error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/169.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/169.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/169.java
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
package org.eclipse.ecf.internal.examples.loadbalancing.servicehost;

import java.util.Properties;

import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.examples.loadbalancing.IDataProcessor;
import org.eclipse.ecf.osgi.services.distribution.IDistributionConstants;
import org.eclipse.ecf.remoteservice.Constants;
import org.eclipse.ecf.remoteservice.IRemoteServiceContainerAdapter;
import org.eclipse.ecf.remoteservice.IRemoteServiceRegistration;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

public class DataProcessorServiceHostApplication implements IApplication, IDistributionConstants {

	private static final String LB_SVCHOST_CONTAINER_TYPE = "ecf.jms.activemq.tcp.manager.lb.svchost";
	public static final String DEFAULT_QUEUE_ID = "tcp://localhost:61616/exampleQueue";
	private static final String DEFAULT_TOPIC_ID = "tcp://localhost:61616/exampleTopic";

	private BundleContext bundleContext;
	private ServiceTracker containerManagerServiceTracker;

	private String containerType = LB_SVCHOST_CONTAINER_TYPE;
	
	// JMS Queue URI that we will attach to as queue message producer (to issue
	// actual remote method/invocation
	// requests to server consumers). Note that this queueId can be changed by
	// using the -queueId launch parameter...e.g.:
	// -queueId tcp://myjmdnsbrokerdnsname:61616/myQueueName
	private String queueId = DEFAULT_QUEUE_ID;
	// JMS topic URI that we will register remote service registrations on...
	// so that service consumers can lookup/get/use remote services. Note that
	// this
	// topicId can be changed by using the -topicId launch parameter...e.g.
	// -topicId tcp://myjmdnsbrokerdnsname:61616/myTopicName
	private String topicId = DEFAULT_TOPIC_ID;

	// The local service registration when using the osgi remote services
	private ServiceRegistration dataProcessorServiceServiceRegistration;
	
	private boolean useECFRemoteServices = false;
	// The following two member variables are only used if the useECFRemoteServices
	// flag is set to true via command line argument...e.g. -useECFRemoteServices
	// The default is to use OSGi remote services, and so these two members
	// will be null
	
	// Container instance that connects us with the ActiveMQ queue as a message
	// producer and publishes the service on the topicId
	private IContainer container;
	// The service host remote service registration. This is used simply to
	// unregister the service when this application is stopped
	private IRemoteServiceRegistration dataProcessorServiceHostRegistration;

	public Object start(IApplicationContext appContext) throws Exception {
		bundleContext = Activator.getContext();
		// Process Arguments...i.e. set queueId and topicId if specified
		processArgs(appContext);

		// Register and publish as OSGi remote service
		if (useECFRemoteServices) registerECFRemoteService();
		else registerOSGiRemoteService();
		
		// wait for remote service requests until stopped
		waitForDone();

		return IApplication.EXIT_OK;
	}

	private void registerOSGiRemoteService() throws Exception {
		// Setup properties for remote service distribution, as per OSGi 4.2 remote services
		// specification (chap 13 in compendium spec)
		Properties props = new Properties();
		// add OSGi service property indicated export of all interfaces exposed by service (wildcard)
		props.put(IDistributionConstants.SERVICE_EXPORTED_INTERFACES, IDistributionConstants.SERVICE_EXPORTED_INTERFACES_WILDCARD);
		// add OSGi service property specifying config
		props.put(IDistributionConstants.SERVICE_EXPORTED_CONFIGS, containerType);
		// add ECF container arguments
		props.put(IDistributionConstants.SERVICE_EXPORTED_CONTAINER_FACTORY_ARGUMENTS, new String[] { topicId, queueId });
		// This is setting (currently) magical service property that indicates
		// that this service registration is a load balancing service host
		props.put(Constants.SERVICE_REGISTER_PROXY, "true");
		// register remote service
		dataProcessorServiceServiceRegistration = bundleContext.registerService(IDataProcessor.class
				.getName(), new IDataProcessor() {
					public String processData(String data) {
						return null;
					}}, props);
		// tell everyone
		System.out.println("LB Service Host: DataProcessor Registered via OSGi Remote Services topic="+topicId);
	}
	
	private void registerECFRemoteService() throws Exception {
		// Create container of appropriate type, and with the topicId and
		// queueId set
		// upon construction
		container = getContainerManagerService().getContainerFactory()
				.createContainer(containerType,
						new Object[] { topicId, queueId });
		// Get IRemoteServiceContainerAdapter
		IRemoteServiceContainerAdapter remoteServiceAdapter = (IRemoteServiceContainerAdapter) container
				.getAdapter(IRemoteServiceContainerAdapter.class);

		Properties properties = new Properties();
		// This is setting (currently) magical service property that indicates
		// that
		// this service registration is a load balancing service host
		properties.put(Constants.SERVICE_REGISTER_PROXY, "true");
		// Register the remote service with the IDataProcessor interface as it's
		// service registration.
		// Note that the Constants.SERVICE_REGISTER_PROXY allows null to be specified
		// as the registered remote service implementation.
		// This object does not implement the IDataProcessor service interface,
		// but it is not actually used. Rather,
		// the LOAD_BALANCING_SERVICE_PROPERTY set to "true" specifies that for
		// this container the remote service
		// requests are proxied and forwarded to the JMS queue (where they are
		// load balanced among the n servers
		// that are consumers from that queue)
		dataProcessorServiceHostRegistration = remoteServiceAdapter
				.registerRemoteService(new String[] { IDataProcessor.class
						.getName() }, null, properties);

		System.out.println("LB Service Host: DataProcessor Registered via ECF Remote Services topic="+topicId);
	}
	
	public void stop() {
		if (dataProcessorServiceServiceRegistration != null) {
			dataProcessorServiceServiceRegistration.unregister();
			dataProcessorServiceServiceRegistration = null;
		}
		if (dataProcessorServiceHostRegistration != null) {
			dataProcessorServiceHostRegistration.unregister();
			dataProcessorServiceHostRegistration = null;
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
			} else if (originalArgs[i].equals("-topicId")) {
				topicId = originalArgs[i + 1];
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/169.java