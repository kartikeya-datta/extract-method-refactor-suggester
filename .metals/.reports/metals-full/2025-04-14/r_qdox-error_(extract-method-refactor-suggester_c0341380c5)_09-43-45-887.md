error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16051.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16051.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16051.java
text:
```scala
i@@f (reg != null && reg.getExtensionPoint(EXTENSION_POINT) != null) {

/****************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 *****************************************************************************/

package org.eclipse.ecf.server.generic;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URI;
import java.util.*;
import org.eclipse.core.runtime.*;
import org.eclipse.ecf.core.ContainerTypeDescription;
import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.core.identity.*;
import org.eclipse.ecf.core.sharedobject.ISharedObjectContainer;
import org.eclipse.ecf.discovery.*;
import org.eclipse.ecf.discovery.identity.IServiceTypeID;
import org.eclipse.ecf.discovery.identity.ServiceIDFactory;
import org.eclipse.ecf.internal.server.generic.Activator;
import org.eclipse.ecf.provider.generic.*;
import org.eclipse.ecf.server.generic.app.*;

public class ServerManager {

	/**
	 * 
	 */
	private static final String ECF_NAMESPACE_JMDNS = "ecf.namespace.jmdns"; //$NON-NLS-1$

	private static final String GROUP_PROPERTY_NAME = "group"; //$NON-NLS-1$

	private static final String PWREQUIRED_PROPERTY_NAME = "pwrequired"; //$NON-NLS-1$

	private static final String PROTOCOL_PROPERTY_NAME = "protocol"; //$NON-NLS-1$

	private static final String SERVICE_TYPE = "ecfgeneric"; //$NON-NLS-1$

	static TCPServerSOContainerGroup serverGroups[] = null;

	static Map servers = new HashMap();

	public static final String EXTENSION_POINT_NAME = "configuration"; //$NON-NLS-1$

	public static final String EXTENSION_POINT = Activator.PLUGIN_ID + "." //$NON-NLS-1$
			+ EXTENSION_POINT_NAME;

	public static final String CONFIGURATION_ELEMENT = "configuration"; //$NON-NLS-1$
	public static final String CONNECTOR_ELEMENT = "connector"; //$NON-NLS-1$
	public static final String GROUP_ELEMENT = GROUP_PROPERTY_NAME;

	public static final String HOSTNAME_ATTR = "hostname"; //$NON-NLS-1$
	public static final String PORT_ATTR = "port"; //$NON-NLS-1$
	public static final String KEEPALIVE_ATTR = "keepAlive"; //$NON-NLS-1$
	public static final String NAME_ATTR = "name"; //$NON-NLS-1$

	public static final String DISCOVERY_ATTR = "discovery"; //$NON-NLS-1$

	public ServerManager() {
		final IExtensionRegistry reg = Activator.getDefault().getExtensionRegistry();
		try {
			if (reg != null) {
				createServersFromExtensionRegistry(reg);
			} else {
				createServersFromConfigurationFile(Activator.getDefault().getBundle().getEntry("server.xml").openStream()); //$NON-NLS-1$
			}
		} catch (final Exception e) {
			Activator.log("Exception creating servers", e); //$NON-NLS-1$
		}
	}

	public synchronized ISharedObjectContainer getServer(ID id) {
		if (id == null)
			return null;
		return (ISharedObjectContainer) servers.get(id);
	}

	private void createServersFromExtensionRegistry(IExtensionRegistry registry) throws Exception {
		final IExtensionPoint extensionPoint = registry.getExtensionPoint(EXTENSION_POINT);
		if (extensionPoint == null)
			return;
		final IConfigurationElement[] elements = extensionPoint.getConfigurationElements();
		final List connectors = new ArrayList();
		for (int i = 0; i < elements.length; i++) {
			final IConfigurationElement element = elements[i];
			final String portString = element.getAttribute(PORT_ATTR);
			int port = TCPServerSOContainer.DEFAULT_PORT;
			if (portString != null)
				port = Integer.parseInt(portString);
			int keepAlive = TCPServerSOContainer.DEFAULT_KEEPALIVE;
			final String keepAliveString = element.getAttribute(KEEPALIVE_ATTR);
			if (keepAliveString != null)
				keepAlive = Integer.parseInt(keepAliveString);
			boolean discovery = false;
			final String discoveryString = element.getAttribute(DISCOVERY_ATTR);
			if (discoveryString != null)
				discovery = Boolean.valueOf(discoveryString).booleanValue();
			final Connector connector = new Connector(null, element.getAttribute(HOSTNAME_ATTR), port, keepAlive, discovery);
			final IConfigurationElement[] groupElements = element.getChildren(GROUP_ELEMENT);
			for (int j = 0; j < groupElements.length; j++) {
				final String groupName = groupElements[i].getAttribute(NAME_ATTR);
				if (groupName != null)
					connector.addGroup(new NamedGroup(groupName));
			}
			connectors.add(connector);
		}
		createServersFromConnectorList(connectors);
	}

	protected boolean isActive() {
		return (servers.size() > 0);
	}

	public synchronized void closeServers() {
		for (final Iterator i = servers.keySet().iterator(); i.hasNext();) {
			final ID serverID = (ID) i.next();
			final TCPServerSOContainer server = (TCPServerSOContainer) servers.get(serverID);
			if (server != null) {
				try {
					server.dispose();
				} catch (final Exception e) {
					Activator.log("Exception disposing serverID=" + serverID, e); //$NON-NLS-1$
				}
			}
		}
		servers.clear();
		if (serverGroups != null) {
			for (int i = 0; i < serverGroups.length; i++) {
				serverGroups[i].takeOffTheAir();
			}
			serverGroups = null;
		}
	}

	private synchronized void createServersFromConnectorList(List connectors) throws IDCreateException, IOException {
		serverGroups = new TCPServerSOContainerGroup[connectors.size()];
		int j = 0;
		for (final Iterator i = connectors.iterator(); i.hasNext();) {
			final Connector connect = (Connector) i.next();
			serverGroups[j] = createServerGroup(connect);
			final List groups = connect.getGroups();
			for (final Iterator g = groups.iterator(); g.hasNext();) {
				final NamedGroup group = (NamedGroup) g.next();
				final TCPServerSOContainer cont = createServerContainer(group.getIDForGroup(), serverGroups[j], group.getName(), connect.getTimeout());
				if (connect.shouldRegisterForDiscovery())
					registerServerForDiscovery(group, false);
				servers.put(cont.getID(), cont);
				final String msg = "Starting server with id=" + cont.getID().getName(); //$NON-NLS-1$
				System.out.println(msg);
				Activator.log(msg);
			}
			serverGroups[j].putOnTheAir();
			j++;
		}
	}

	private void createServersFromConfigurationFile(InputStream ins) throws Exception {
		final ServerConfigParser scp = new ServerConfigParser();
		final List connectors = scp.load(ins);
		if (connectors != null)
			createServersFromConnectorList(connectors);
	}

	private TCPServerSOContainerGroup createServerGroup(Connector connector) {
		final TCPServerSOContainerGroup group = new TCPServerSOContainerGroup(connector.getHostname(), connector.getPort());
		return group;
	}

	private TCPServerSOContainer createServerContainer(String id, TCPServerSOContainerGroup group, String path, int keepAlive) throws IDCreateException {
		final ID newServerID = IDFactory.getDefault().createStringID(id);
		TCPServerSOContainer container = new TCPServerSOContainer(new SOContainerConfig(newServerID), group, path, keepAlive);
		IContainerManager containerManager = Activator.getDefault().getContainerManager();
		if (containerManager != null) {
			ContainerTypeDescription ctd = containerManager.getContainerFactory().getDescriptionByName("ecf.generic.server"); //$NON-NLS-1$
			containerManager.addContainer(container, ctd);
		}
		return container;
	}

	private void registerServerForDiscovery(NamedGroup group, boolean pwrequired) {
		final IDiscoveryAdvertiser discovery = Activator.getDefault().getDiscovery();
		if (discovery != null) {
			try {
				final String rawGroupName = group.getRawName();
				final Connector connector = group.getConnector();
				final Properties props = new Properties();
				props.put(PROTOCOL_PROPERTY_NAME, TCPServerSOContainer.DEFAULT_PROTOCOL);
				props.put(PWREQUIRED_PROPERTY_NAME, new Boolean(pwrequired).toString());
				props.put(GROUP_PROPERTY_NAME, rawGroupName);
				final Namespace ns = IDFactory.getDefault().getNamespaceByName(ECF_NAMESPACE_JMDNS);
				final IServiceTypeID serviceTypeID = ServiceIDFactory.getDefault().createServiceTypeID(ns, new String[] {SERVICE_TYPE}, IServiceTypeID.DEFAULT_PROTO);
				final InetAddress host = InetAddress.getByName(connector.getHostname());
				URI uri = new URI(TCPServerSOContainer.DEFAULT_PROTOCOL, null, host.getHostAddress(), connector.getPort(), null, null, null);
				final ServiceInfo svcInfo = new ServiceInfo(uri, rawGroupName, serviceTypeID, 0, 0, new ServiceProperties(props));
				discovery.registerService(svcInfo);
			} catch (final Exception e) {
				Activator.log("Discovery registration exception", e); //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16051.java