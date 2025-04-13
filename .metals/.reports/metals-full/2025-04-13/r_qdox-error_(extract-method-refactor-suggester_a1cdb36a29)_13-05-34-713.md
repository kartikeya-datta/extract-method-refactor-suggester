error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17909.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17909.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17909.java
text:
```scala
private S@@tring serviceType = "remotesvcs"; //$NON-NLS-1$

/****************************************************************************
 * Copyright (c) 2008 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 *****************************************************************************/

package org.eclipse.ecf.internal.examples.remoteservices.server;

import java.util.Properties;
import org.eclipse.core.runtime.Assert;
import org.eclipse.ecf.core.*;
import org.eclipse.ecf.core.identity.*;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.discovery.*;
import org.eclipse.ecf.discovery.identity.IServiceID;
import org.eclipse.ecf.discovery.identity.ServiceIDFactory;
import org.eclipse.ecf.discovery.service.IDiscoveryService;
import org.eclipse.ecf.examples.remoteservices.common.IRemoteEnvironmentInfo;
import org.eclipse.ecf.remoteservice.Constants;
import org.eclipse.ecf.remoteservice.IRemoteServiceContainerAdapter;
import org.eclipse.ecf.remoteservice.util.DiscoveryProperties;
import org.eclipse.ecf.remoteservice.util.RemoteServiceProperties;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

/**
 *
 */
public class DiscoverableServer implements IApplication {

	public static final String serviceHostContainerTypeArg = "-serviceHostContainerType"; //$NON-NLS-1$
	public static final String serviceHostNamespaceArg = "-serviceHostNamespace"; //$NON-NLS-1$
	public static final String serviceHostIDArg = "-serviceHostID"; //$NON-NLS-1$
	public static final String clientContainerTypeArg = "-clientContainerType"; //$NON-NLS-1$
	public static final String clientConnectTargetArg = "-clientConnectTarget"; //$NON-NLS-1$
	public static final String serviceTypeArg = "-serviceType"; //$NON-NLS-1$

	// Argument variables
	private String serviceHostContainerType = "ecf.generic.server"; //$NON-NLS-1$
	private String serviceHostNamespace = StringID.class.getName();
	private String serviceHostID = "ecftcp://localhost:3285/server"; //$NON-NLS-1$
	private String clientContainerType = "ecf.generic.client"; //$NON-NLS-1$
	private String clientConnectTarget = "ecftcp://localhost:3285/server"; //$NON-NLS-1$
	private String serviceType = Constants.DISCOVERY_SERVICE_TYPE;

	private IContainer serviceHostContainer;

	private IServiceInfo serviceInfo;

	private IDiscoveryService discoveryService;

	private boolean done = false;

	private String getCompleteServiceType() {
		return "_" + serviceType + "._tcp.local."; //$NON-NLS-1$ //$NON-NLS-2$ 
	}

	protected IContainer createServiceHostContainer() throws IDCreateException, ContainerCreateException {
		return ContainerFactory.getDefault().createContainer(serviceHostContainerType, IDFactory.getDefault().createID(serviceHostNamespace, serviceHostID));
	}

	protected Properties createServiceDiscoveryProperties() {
		Properties props = new RemoteServiceProperties(serviceHostContainerType, serviceHostContainer);
		// Add auto registration of remote proxy
		props.put(Constants.AUTOREGISTER_REMOTE_PROXY, "true"); //$NON-NLS-1$
		return props;
	}

	public void start(String[] args) throws Exception {
		initializeFromArguments(args);
		// Create service host container
		serviceHostContainer = createServiceHostContainer();
		// Get adapter from serviceHostContainer
		final IRemoteServiceContainerAdapter containerAdapter = (IRemoteServiceContainerAdapter) serviceHostContainer.getAdapter(IRemoteServiceContainerAdapter.class);
		Assert.isNotNull(containerAdapter);

		final String serviceClassName = IRemoteEnvironmentInfo.class.getName();

		// register IRemoteEnvironmentInfo service
		// Then actually register the remote service implementation, with created props
		containerAdapter.registerRemoteService(new String[] {serviceClassName}, new RemoteEnvironmentInfoImpl(), createServiceDiscoveryProperties());
		System.out.println("Registered remote service " + serviceClassName + " with " + serviceHostContainer + ",ID=" + serviceHostContainer.getID()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		// then register for discovery
		discoveryService = Activator.getDefault().getDiscoveryService(15000);

		Assert.isNotNull(discoveryService);

		final String serviceName = System.getProperty("user.name") + System.currentTimeMillis(); //$NON-NLS-1$
		final IServiceID serviceID = ServiceIDFactory.getDefault().createServiceID(discoveryService.getServicesNamespace(), getCompleteServiceType(), serviceName);
		final Properties serviceProperties = createServicePropertiesForDiscovery(serviceClassName);
		serviceInfo = new ServiceInfo(serviceType, null, 80, serviceID, new ServiceProperties(serviceProperties));
		// register discovery here
		discoveryService.registerService(serviceInfo);
		System.out.println("service published for discovery\n\tserviceName=" + serviceID.getServiceName() + "\n\tserviceTypeID=" + serviceID.getServiceTypeID()); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println("\tserviceProperties=" + serviceProperties); //$NON-NLS-1$

	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	public Object start(IApplicationContext ctxt) throws Exception {
		// Actually start with application args
		start((String[]) ctxt.getArguments().get("application.args")); //$NON-NLS-1$
		// wait on this thread until done
		synchronized (this) {
			while (!done) {
				wait();
			}
		}
		return new Integer(0);
	}

	protected Properties createServicePropertiesForDiscovery(String className) {
		return new DiscoveryProperties(className, clientContainerType, serviceHostNamespace, clientConnectTarget, null, null);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		if (serviceInfo != null) {
			if (discoveryService != null) {
				try {
					discoveryService.unregisterService(serviceInfo);
				} catch (final ECFException e) {
					e.printStackTrace();
				}
				serviceInfo = null;
				final IContainer container = (IContainer) discoveryService.getAdapter(IContainer.class);
				if (container != null) {
					container.disconnect();
				}
				discoveryService = null;
			}
		}
		if (serviceHostContainer != null) {
			serviceHostContainer.disconnect();
			serviceHostContainer = null;
		}
		synchronized (this) {
			done = true;
			notifyAll();
		}

	}

	private void initializeFromArguments(String[] args) throws Exception {
		if (args == null)
			return;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase(serviceHostContainerTypeArg))
				serviceHostContainerType = args[++i];
			else if (args[i].equalsIgnoreCase(serviceHostNamespaceArg))
				serviceHostNamespace = args[++i];
			else if (args[i].equalsIgnoreCase(serviceHostIDArg))
				serviceHostID = args[++i];
			else if (args[i].equalsIgnoreCase(clientContainerTypeArg))
				clientContainerType = args[++i];
			else if (args[i].equalsIgnoreCase(clientConnectTargetArg))
				clientConnectTarget = args[++i];
			else if (args[i].equalsIgnoreCase(serviceTypeArg))
				serviceType = args[++i];
			else {
				usage();
				throw new IllegalArgumentException("Invalid argument"); //$NON-NLS-1$
			}
		}
	}

	/**
	 * 
	 */
	private void usage() {
		System.out.println("usage: eclipse -console [options] -application org.eclipse.ecf.examples.remoteservices.server.remoteServicesServer"); //$NON-NLS-1$
		System.out.println("   options: [" + serviceHostContainerTypeArg + " <typename>] default=ecf.generic.server"); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println("            [" + serviceHostNamespaceArg + " <namespacename>] default=org.eclipse.ecf.identity.StringID"); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println("            [" + serviceHostIDArg + " <hostID>] default=ecftcp://localhost:3285/server"); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println("            [" + clientContainerTypeArg + " <typename>] default=ecf.generic.client"); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println("            [" + clientConnectTargetArg + " <target>] default=ecftcp://localhost:3285/server"); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println("            [" + serviceTypeArg + " <serviceType>] default=remotesvcs"); //$NON-NLS-1$ //$NON-NLS-2$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17909.java