error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9032.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9032.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9032.java
text:
```scala
s@@erviceInfo = new ServiceInfo(UPDATE_SITE_SERVICE_TYPE, null, getServicePort(), ServiceIDFactory.getDefault().createServiceID(discovery.getServicesNamespace(), serviceType1, serviceName1), new ServiceProperties(new UpdateSiteProperties(serviceName1, servicePath1).toProperties()));

package org.eclipse.ecf.internal.examples.updatesite;

import java.net.URL;
import org.eclipse.ecf.core.ContainerFactory;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.discovery.*;
import org.eclipse.ecf.discovery.identity.ServiceIDFactory;
import org.eclipse.ecf.discovery.service.IDiscoveryService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

	static final String PLUGIN_ID = "org.eclipse.ecf.examples.updatesite.server"; //$NON-NLS-1$
	static final String UPDATE_SITE_SERVICE_TYPE = Messages.Activator_UPDATE_SITE_SERVICE;
	static final String HTTP_SERVICE_TYPE = "http"; //$NON-NLS-1$

	public static final String DEFAULT_SERVICE_TYPE = "_" + UPDATE_SITE_SERVICE_TYPE + "._" + HTTP_SERVICE_TYPE + "._tcp.local."; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	private static final String DEFAULT_SERVICE_PATH = "/update"; //$NON-NLS-1$
	private static final String DEFAULT_SERVICE_NAME_SUFFIX = " update site"; //$NON-NLS-1$

	// Properties
	private String username;
	protected String serviceType;
	private String serviceName;

	private String servicePath;
	// Update site name and location
	private String updateSiteName;
	private URL updateSiteLocation;

	private static Activator plugin;
	private BundleContext context;

	private ServiceTracker httpServiceTracker;
	private ServiceTracker discoveryTracker;

	private IDiscoveryContainerAdapter discovery;
	private IServiceInfo serviceInfo;

	public static Activator getDefault() {
		return plugin;
	}

	public void setupProperties() throws Exception {
		// Username is org.eclipse.ecf.examples.updatesite.server.username.  Default is System property "user.name"
		username = System.getProperty("username", System.getProperty("user.name")); //$NON-NLS-1$ //$NON-NLS-2$
		serviceType = System.getProperty("serviceType", DEFAULT_SERVICE_TYPE); //$NON-NLS-1$
		serviceName = System.getProperty("serviceName", username + DEFAULT_SERVICE_NAME_SUFFIX); //$NON-NLS-1$
		servicePath = System.getProperty("servicePath", DEFAULT_SERVICE_PATH); //$NON-NLS-1$
		updateSiteName = System.getProperty("updateSiteName", username + DEFAULT_SERVICE_NAME_SUFFIX); //$NON-NLS-1$
		String uSiteLocation = System.getProperty("updateSiteLocation"); //$NON-NLS-1$
		if (!uSiteLocation.endsWith("/")) //$NON-NLS-1$
			uSiteLocation += "/"; //$NON-NLS-1$

		try {
			updateSiteLocation = new URL(uSiteLocation);
		} catch (final Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void setupDiscovery() throws Exception {
		try {
			ContainerFactory.getDefault().getDescriptions();
			Thread.sleep(1000);
			discovery = getDiscoveryService();
			if (discovery == null) {
				final IContainer discoveryContainer = ContainerFactory.getDefault().createContainer("ecf.discovery.jmdns"); //$NON-NLS-1$
				discoveryContainer.connect(null, null);
				discovery = (IDiscoveryContainerAdapter) discoveryContainer.getAdapter(IDiscoveryContainerAdapter.class);
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext ctxt) throws Exception {
		plugin = this;
		context = ctxt;

		// Get/setup properties
		setupProperties();
		// Setup discovery
		setupDiscovery();
		// Setup http server/service
		registerHttpResource(servicePath, "/", updateSiteLocation); //$NON-NLS-1$
		// Register service with discovery
		registerService(serviceType, serviceName, updateSiteName, servicePath);
	}

	private void registerService(String serviceType1, String serviceName1, String updateSiteName1, String servicePath1) throws Exception {
		try {
			serviceInfo = new ServiceInfo(null, getServicePort(), ServiceIDFactory.getDefault().createServiceID(discovery.getServicesNamespace(), serviceType1, serviceName1), new ServiceProperties(new UpdateSiteProperties(serviceName1, servicePath1).toProperties()));
			discovery.registerService(serviceInfo);
		} catch (final Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	private int getServicePort() {
		final String osgiPort = System.getProperty("org.osgi.service.http.port"); //$NON-NLS-1$
		Integer servicePort = new Integer(80);
		if (osgiPort != null) {
			servicePort = Integer.valueOf(osgiPort);
		}
		return servicePort.intValue();
	}

	public void registerHttpResource(String alias, String servicePath1, URL fileSystemLocation) throws Exception {
		try {
			final HttpService httpService = getHttpService();
			if (httpService == null)
				throw new NullPointerException("Http service not found."); //$NON-NLS-1$
			httpService.registerResources(alias, servicePath1, new UpdateSiteContext(httpService.createDefaultHttpContext(), fileSystemLocation));

		} catch (final Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext ctxt) throws Exception {
		if (discovery != null && serviceInfo != null) {
			discovery.unregisterService(serviceInfo);
			discovery = null;
			serviceInfo = null;
		}
		if (httpServiceTracker != null) {
			httpServiceTracker.close();
			httpServiceTracker = null;
		}
		if (discoveryTracker != null) {
			discoveryTracker.close();
			discoveryTracker = null;
		}

		ctxt = null;
		plugin = null;
	}

	public HttpService getHttpService() {
		if (httpServiceTracker == null) {
			httpServiceTracker = new ServiceTracker(context, HttpService.class.getName(), null);
			httpServiceTracker.open();
		}
		return (HttpService) httpServiceTracker.getService();
	}

	public IDiscoveryService getDiscoveryService() {
		if (discoveryTracker == null) {
			discoveryTracker = new ServiceTracker(context, IDiscoveryService.class.getName(), null);
			discoveryTracker.open();
		}
		return (IDiscoveryService) discoveryTracker.getService();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9032.java