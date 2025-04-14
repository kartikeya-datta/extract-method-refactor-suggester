error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14269.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14269.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14269.java
text:
```scala
s@@erviceInfo = new ServiceInfo(Constants.DISCOVERY_SERVICE_TYPE, null, 80, serviceID, new ServiceProperties(new DiscoveryProperties(className, ECF_GENERIC_CLIENT, serviceHostContainer)));

package org.eclipse.ecf.internal.examples.remoteservices.server;

import org.eclipse.core.runtime.Assert;
import org.eclipse.ecf.core.ContainerFactory;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.discovery.IDiscoveryContainerAdapter;
import org.eclipse.ecf.discovery.IServiceInfo;
import org.eclipse.ecf.discovery.ServiceInfo;
import org.eclipse.ecf.discovery.ServiceProperties;
import org.eclipse.ecf.discovery.identity.IServiceID;
import org.eclipse.ecf.discovery.identity.ServiceIDFactory;
import org.eclipse.ecf.discovery.service.IDiscoveryService;
import org.eclipse.ecf.examples.remoteservices.common.IRemoteEnvironmentInfo;
import org.eclipse.ecf.remoteservice.Constants;
import org.eclipse.ecf.remoteservice.IRemoteServiceContainerAdapter;
import org.eclipse.ecf.remoteservice.util.DiscoveryProperties;
import org.eclipse.ecf.remoteservice.util.RemoteServiceProperties;
import org.eclipse.osgi.service.environment.EnvironmentInfo;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator implements BundleActivator {

	private static final String ECF_GENERIC_CLIENT = "ecf.generic.client";

	private static final String ECF_DISCOVERY_JMDNS = "ecf.discovery.jmdns";

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.ecf.examples.remoteservices.server";

	private static final String REMOTE_SERVICE_TYPE = "_" + Constants.DISCOVERY_SERVICE_TYPE + "._tcp.local.";

	private static final String DEFAULT_CONNECT_TARGET = "ecftcp://ecf.eclipse.org:3283/server";

	// The shared instance
	private static Activator plugin;

	private BundleContext context;

	private IContainer serviceHostContainer;

	private IServiceInfo serviceInfo;

	private IDiscoveryContainerAdapter discovery;

	private ServiceTracker environmentInfoTracker;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	public EnvironmentInfo getEnvironmentInfo() {
		if (environmentInfoTracker == null) {
			environmentInfoTracker = new ServiceTracker(context, org.eclipse.osgi.service.environment.EnvironmentInfo.class.getName(), null);
			environmentInfoTracker.open();
		}
		return (EnvironmentInfo) environmentInfoTracker.getService();
	}

	private void registerRemoteService(String className, Object service) {
		try {
			final IRemoteServiceContainerAdapter containerAdapter = (IRemoteServiceContainerAdapter) serviceHostContainer.getAdapter(IRemoteServiceContainerAdapter.class);
			Assert.isNotNull(containerAdapter);
			// register remote service
			containerAdapter.registerRemoteService(new String[] {className}, service, new RemoteServiceProperties(ECF_GENERIC_CLIENT, serviceHostContainer));
			System.out.println("Remote service registered for class " + className);

			// then register for discovery
			final String serviceName = System.getProperty("user.name") + System.currentTimeMillis();
			final IServiceID serviceID = ServiceIDFactory.getDefault().createServiceID(discovery.getServicesNamespace(), REMOTE_SERVICE_TYPE, serviceName);
			serviceInfo = new ServiceInfo(null, 80, serviceID, new ServiceProperties(new DiscoveryProperties(className, ECF_GENERIC_CLIENT, serviceHostContainer)));
			// register discovery here
			discovery.registerService(serviceInfo);
			System.out.println("Service registered for discovery with IServiceID: " + serviceID);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void setupDiscovery() {
		try {
			if (discovery == null) {
				final ServiceTracker serviceTracker = new ServiceTracker(context, IDiscoveryService.class.getName(), null);
				serviceTracker.open();
				// XXX this should not be done in Activator...but we're going do do it anyway
				discovery = (IDiscoveryContainerAdapter) serviceTracker.waitForService(5000);
				serviceTracker.close();
				if (discovery == null) {
					final IContainer discoveryContainer = ContainerFactory.getDefault().createContainer(ECF_DISCOVERY_JMDNS);
					discoveryContainer.connect(null, null);
					discovery = (IDiscoveryContainerAdapter) discoveryContainer.getAdapter(IDiscoveryContainerAdapter.class);
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		plugin = this;
		this.context = context;
		setupDiscovery();
		createAndConnectServiceHostContainer();
		registerRemoteService(IRemoteEnvironmentInfo.class.getName(), new RemoteEnvironmentInfoImpl());
	}

	private void createAndConnectServiceHostContainer() {
		try {
			serviceHostContainer = ContainerFactory.getDefault().createContainer(ECF_GENERIC_CLIENT);
			final ID targetID = IDFactory.getDefault().createID(serviceHostContainer.getConnectNamespace(), DEFAULT_CONNECT_TARGET);
			serviceHostContainer.connect(targetID, null);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		if (serviceInfo != null) {
			if (discovery != null) {
				discovery.unregisterService(serviceInfo);
				serviceInfo = null;
				final IContainer container = (IContainer) discovery.getAdapter(IContainer.class);
				if (container != null) {
					container.disconnect();
				}
				discovery = null;
			}
		}
		if (serviceHostContainer != null) {
			serviceHostContainer.disconnect();
			serviceHostContainer = null;
		}
		if (environmentInfoTracker != null) {
			environmentInfoTracker.close();
			environmentInfoTracker = null;
		}
		this.context = null;
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14269.java