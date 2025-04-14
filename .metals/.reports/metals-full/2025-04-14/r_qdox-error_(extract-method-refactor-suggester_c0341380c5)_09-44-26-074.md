error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1748.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1748.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1748.java
text:
```scala
t@@his.internalProperties.put(LOCATION, serviceInfo.getLocation());

/*******************************************************************************
 *  Copyright (c)2010 REMAIN B.V. The Netherlands. (http://www.remainsoftware.com).
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 * 
 *  Contributors:
 *    Wim Jongman - initial API and implementation 
 *    Ahmed Aadel - initial API and implementation     
 *******************************************************************************/
package org.eclipse.ecf.provider.zookeeper.core;

import java.net.URI;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.eclipse.core.runtime.Assert;
import org.eclipse.ecf.discovery.IServiceInfo;
import org.eclipse.ecf.discovery.ServiceInfo;
import org.eclipse.ecf.discovery.ServiceProperties;
import org.eclipse.ecf.discovery.identity.IServiceID;
import org.eclipse.ecf.discovery.identity.IServiceTypeID;
import org.eclipse.ecf.discovery.identity.ServiceIDFactory;
import org.eclipse.ecf.provider.zookeeper.core.internal.Configurator;
import org.eclipse.ecf.provider.zookeeper.core.internal.IService;
import org.eclipse.ecf.provider.zookeeper.node.internal.INode;
import org.eclipse.ecf.provider.zookeeper.util.Geo;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

/**
 * Encapsulate a service to be advertised and made visible for discovery. An
 * object of <code>AdvertisedService</code> is build either with an OSGi service
 * reference <code>ServiceReference</code> or <code>ServiceInfo</code> object.<br>
 * 
 */

public class AdvertisedService extends ServiceInfo implements INode, IService {

	private static final long serialVersionUID = 1001026250299416572L;
	private String uuid;
	private Properties internalProperties = new Properties();
	private static Map<String, IServiceInfo> publishedServices = new HashMap<String, IServiceInfo>();
	private ServiceReference serviceReference;

	public AdvertisedService(ServiceReference ref) {
		Assert.isNotNull(ref);
		this.serviceReference = ref;
		this.uuid = UUID.randomUUID().toString();
		String services[] = (String[]) this.serviceReference
				.getProperty(Constants.OBJECTCLASS);
		for (String k : this.serviceReference.getPropertyKeys()) {
			Object value = this.serviceReference.getProperty(k);
			if (value instanceof String
					&& ((String) value).contains("localhost")) {//$NON-NLS-1$
				this.internalProperties.put(k, ((String) value).replace(
						"localhost",//$NON-NLS-1$
						Geo.getHost()));
				continue;
			}
			this.internalProperties
					.put(k, this.serviceReference.getProperty(k));
		}
		IServiceTypeID serviceTypeID = ServiceIDFactory.getDefault()
				.createServiceTypeID(
						ZooDiscoveryContainer.getSingleton()
								.getConnectNamespace(), services,
						IServiceTypeID.DEFAULT_PROTO);
		serviceTypeID = new ZooDiscoveryServiceTypeID(
				(ZooDiscoveryNamespace) ZooDiscoveryContainer.getSingleton()
						.getConnectNamespace(), serviceTypeID,
				this.serviceReference.getProperty(Constants.SERVICE_ID)
						.toString());
		serviceID = new ZooDiscoveryServiceID(ZooDiscoveryContainer
				.getSingleton().getConnectNamespace(), serviceTypeID, Geo
				.getLocation());

		super.properties = new ServiceProperties(this.internalProperties);
		// internal properties
		this.internalProperties.put(Constants.OBJECTCLASS,
				arrayToString(services));
		this.internalProperties.put(LOCATION, Geo.getLocation());
		this.internalProperties.put(WEIGHT, getWeight());
		this.internalProperties.put(PRIORITY, getPriority());
		this.internalProperties.put(NODE_PROPERTY_NAME_PROTOCOLS,
				arrayToString(IServiceTypeID.DEFAULT_PROTO));
		this.internalProperties.put(NODE_PROPERTY_NAME_SCOPE,
				arrayToString(IServiceTypeID.DEFAULT_SCOPE));
		this.internalProperties.put(NODE_PROPERTY_NAME_NA,
				IServiceTypeID.DEFAULT_NA);
		publishedServices.put(serviceTypeID.getInternal(), this);
	}

	public AdvertisedService(IServiceInfo serviceInfo) {
		super(serviceInfo.getLocation(), serviceInfo.getServiceName(),
				serviceInfo.getServiceID().getServiceTypeID(), serviceInfo
						.getPriority(), serviceInfo.getWeight(), serviceInfo
						.getServiceProperties());
		this.uuid = UUID.randomUUID().toString();
		// internal properties

		Enumeration enumm = serviceInfo.getServiceProperties()
				.getPropertyNames();
		while (enumm.hasMoreElements()) {
			String k = (String) enumm.nextElement();
			Object value = serviceInfo.getServiceProperties().getProperty(k);
			byte[] bytes = serviceInfo.getServiceProperties().getPropertyBytes(
					k);
			if (value instanceof String
					&& ((String) value).contains("localhost")) {//$NON-NLS-1$
				this.internalProperties.put(k, ((String) value).replace(
						"localhost",//$NON-NLS-1$
						Geo.getHost()));
				continue;
			}
			if (bytes != null) {
				this.internalProperties.put(INode._BYTES_ + k, new String(bytes));
			} else {
				this.internalProperties.put(k, value);
			}

		}

		this.internalProperties
				.put(NODE_PROPERTY_NAME_PROTOCOLS, arrayToString(getServiceID()
						.getServiceTypeID().getProtocols()));
		this.internalProperties.put(NODE_PROPERTY_NAME_SCOPE,
				arrayToString(getServiceID().getServiceTypeID().getScopes()));
		this.internalProperties.put(NODE_PROPERTY_SERVICES,
				arrayToString(getServiceID().getServiceTypeID().getServices()));
		this.internalProperties.put(NODE_PROPERTY_NAME_NA, getServiceID()
				.getServiceTypeID().getNamingAuthority());
		this.internalProperties.put(LOCATION, Geo.getLocation());
		this.internalProperties.put(WEIGHT, getWeight());
		this.internalProperties.put(PRIORITY, getPriority());
		publishedServices.put(serviceInfo.getServiceID().getServiceTypeID()
				.getInternal(), this);
	}

	public static Map<String, IServiceInfo> getPublishedServices() {
		return Collections.unmodifiableMap(publishedServices);
	}

	public synchronized static IServiceInfo removePublished(String id) {
		return publishedServices.remove(id);
	}

	public Properties getProperties() {
		return this.internalProperties;
	}

	public String getNodeId() {
		return this.uuid;
	}

	public void regenerateNodeId() {
		this.uuid = UUID.randomUUID().toString();
	}

	public URI getLocation() {
		return serviceID.getLocation();
	}

	public IServiceID getServiceID() {
		return serviceID;
	}

	public int compareTo(Object o) {
		Assert.isTrue(o != null && o instanceof IServiceInfo,
				"incompatible types for compare"); //$NON-NLS-1$
		return this.getServiceID().getName().compareTo(
				((IServiceInfo) o).getServiceID().getName());
	}

	/**
	 * @return ServiceReference may be null if this
	 *         <code>AdvertisedService</code> instance is not <code>built</code>
	 *         calling constructor
	 *         <code>AdvertisedService(ServiceReference ref)</code>.
	 */
	public ServiceReference getServiceReference() {
		return this.serviceReference;
	}

	public byte[] getPropertiesAsBytes() {
		return getPropertiesAsString().getBytes();
	}

	public String getPropertiesAsString() {
		String props = "";
		for (Object k : this.getProperties().keySet()) {
			props += k + "=" + this.getProperties().get(k) + "\n";//$NON-NLS-1$//$NON-NLS-2$
		}
		return props;
	}

	public String getPath() {
		return getNodeId() + INode._URI_ + Geo.getHost()
				+ INode._ZOODISCOVERYID_
				+ Configurator.INSTANCE.getID().getName();
	}

	public String getAbsolutePath() {
		return INode.ROOT_SLASH + getPath();
	}

	public boolean isLocalNode() {
		return Geo.isLocal(getAbsolutePath());
	}

	public IService getWrappedService() {
		return this;
	}

	private String arrayToString(String[] arr) {
		String s = "";//$NON-NLS-1$
		for (String c : arr) {
			s += c + " ";//$NON-NLS-1$
		}
		return s;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1748.java