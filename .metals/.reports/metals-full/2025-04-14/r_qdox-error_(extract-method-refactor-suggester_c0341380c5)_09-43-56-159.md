error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16874.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16874.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16874.java
text:
```scala
S@@tring str = endpointDescription.getId();

/*******************************************************************************
 * Copyright (c) 2010 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.osgi.services.remoteserviceadmin;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.discovery.IDiscoveryAdvertiser;
import org.eclipse.ecf.discovery.IServiceInfo;
import org.eclipse.ecf.discovery.IServiceProperties;
import org.eclipse.ecf.discovery.ServiceInfo;
import org.eclipse.ecf.discovery.ServiceProperties;
import org.eclipse.ecf.discovery.identity.IServiceTypeID;
import org.eclipse.ecf.discovery.identity.ServiceIDFactory;

public class ServiceInfoFactory extends AbstractMetadataFactory implements
		IServiceInfoFactory {

	protected Map<ServiceInfoKey, IServiceInfo> serviceInfos = new HashMap();

	protected class ServiceInfoKey {
		private EndpointDescription endpointDescription;
		private Namespace discoveryNamespace;
		private int hashCode = 7;

		public ServiceInfoKey(EndpointDescription endpointDescription,
				Namespace discoveryNamespace) {
			this.endpointDescription = endpointDescription;
			this.discoveryNamespace = discoveryNamespace;
			this.hashCode = 31 * this.hashCode + endpointDescription.hashCode();
			this.hashCode = 31 * this.hashCode + discoveryNamespace.hashCode();
		}

		public boolean equals(Object other) {
			if (other == null)
				return false;
			if (!(other instanceof ServiceInfoKey))
				return false;
			ServiceInfoKey otherKey = (ServiceInfoKey) other;
			return (this.endpointDescription
					.equals(otherKey.endpointDescription) && this.discoveryNamespace
					.equals(otherKey.discoveryNamespace));
		}

		public int hashCode() {
			return hashCode;
		}
	}

	public IServiceInfo createServiceInfoForDiscovery(
			IDiscoveryAdvertiser advertiser,
			EndpointDescription endpointDescription) {
		try {
			Namespace advertiserNamespace = advertiser.getServicesNamespace();
			ServiceInfoKey key = new ServiceInfoKey(endpointDescription,
					advertiserNamespace);
			IServiceInfo existingServiceInfo = null;
			synchronized (serviceInfos) {
				existingServiceInfo = serviceInfos.get(key);
				// If it's already there, then we return null
				if (existingServiceInfo != null)
					return null;
				IServiceTypeID serviceTypeID = createServiceTypeID(
						endpointDescription, advertiser);
				String serviceName = createServiceName(endpointDescription,
						advertiser, serviceTypeID);
				URI uri = createURI(endpointDescription, advertiser,
						serviceTypeID, serviceName);
				IServiceProperties serviceProperties = createServiceProperties(
						endpointDescription, advertiser, serviceTypeID,
						serviceName, uri);
				IServiceInfo newServiceInfo = createServiceInfo(uri,
						serviceName, serviceTypeID, serviceProperties);
				// put into map using key
				serviceInfos.put(key, newServiceInfo);
				return newServiceInfo;
			}
		} catch (Exception e) {
			logError(
					"createServiceInfoForDiscovery",
					"Exception creating service info for endpointDescription="
							+ endpointDescription + ",advertiser=" + advertiser,
					e);
			return null;
		}
	}

	protected IServiceInfo createServiceInfo(URI uri, String serviceName,
			IServiceTypeID serviceTypeID, IServiceProperties serviceProperties) {
		return new ServiceInfo(uri, serviceName, serviceTypeID,
				serviceProperties);
	}

	protected IServiceProperties createServiceProperties(
			EndpointDescription endpointDescription,
			IDiscoveryAdvertiser advertiser, IServiceTypeID serviceTypeID,
			String serviceName, URI uri) {
		ServiceProperties result = new ServiceProperties();
		encodeServiceProperties(endpointDescription, result);
		return result;
	}

	protected URI createURI(EndpointDescription endpointDescription,
			IDiscoveryAdvertiser advertiser, IServiceTypeID serviceTypeID,
			String serviceName) throws URISyntaxException {
		String path = "/" + serviceName;
		String str = endpointDescription.getContainerID().getName();
		URI uri = null;
		while (true) {
			try {
				uri = new URI(str);
				if (uri.getHost() != null) {
					break;
				} else {
					final String rawSchemeSpecificPart = uri
							.getRawSchemeSpecificPart();
					// make sure we break eventually
					if (str.equals(rawSchemeSpecificPart)) {
						uri = null;
						break;
					} else {
						str = rawSchemeSpecificPart;
					}
				}
			} catch (URISyntaxException e) {
				uri = null;
				break;
			}
		}
		String scheme = RemoteConstants.SERVICE_TYPE;
		int port = 32565;
		if (uri != null) {
			port = uri.getPort();
			if (port == -1)
				port = 32565;
		}
		String host = null;
		if (uri != null) {
			host = uri.getHost();
		} else {
			try {
				host = InetAddress.getLocalHost().getHostAddress();
			} catch (Exception e) {
				logInfo("createURI", //$NON-NLS-1$
						"failed to get local host adress, falling back to \'localhost\'.", e); //$NON-NLS-1$
				host = "localhost"; //$NON-NLS-1$
			}
		}
		return new URI(scheme, null, host, port, path, null, null);
	}

	protected String createServiceName(EndpointDescription endpointDescription,
			IDiscoveryAdvertiser advertiser, IServiceTypeID serviceTypeID) {
		// First create unique default name
		String defaultServiceName = createDefaultServiceName(
				endpointDescription, advertiser, serviceTypeID);
		// Look for service name that was explicitly set
		String serviceName = getStringWithDefault(
				endpointDescription.getProperties(),
				RemoteConstants.DISCOVERY_SERVICE_NAME, defaultServiceName);
		return serviceName;
	}

	protected String createDefaultServiceName(
			EndpointDescription endpointDescription,
			IDiscoveryAdvertiser advertiser, IServiceTypeID serviceTypeID) {
		return RemoteConstants.DISCOVERY_DEFAULT_SERVICE_NAME_PREFIX
				+ IDFactory.getDefault().createGUID().getName();
	}

	protected IServiceTypeID createServiceTypeID(
			EndpointDescription endpointDescription,
			IDiscoveryAdvertiser advertiser) {
		Map props = endpointDescription.getProperties();
		String[] scopes = getStringArrayWithDefault(props,
				RemoteConstants.DISCOVERY_SCOPE, IServiceTypeID.DEFAULT_SCOPE);
		String[] protocols = getStringArrayWithDefault(props,
				RemoteConstants.DISCOVERY_PROTOCOLS,
				IServiceTypeID.DEFAULT_SCOPE);
		String namingAuthority = getStringWithDefault(props,
				RemoteConstants.DISCOVERY_NAMING_AUTHORITY,
				IServiceTypeID.DEFAULT_NA);
		return ServiceIDFactory.getDefault().createServiceTypeID(
				advertiser.getServicesNamespace(),
				new String[] { RemoteConstants.SERVICE_TYPE }, scopes,
				protocols, namingAuthority);
	}

	public IServiceInfo removeServiceInfoForUndiscovery(
			IDiscoveryAdvertiser advertiser,
			EndpointDescription endpointDescription) {
		Namespace advertiserNamespace = advertiser.getServicesNamespace();
		ServiceInfoKey key = new ServiceInfoKey(endpointDescription,
				advertiserNamespace);
		synchronized (serviceInfos) {
			return serviceInfos.remove(key);
		}
	}

	public void close() {
		removeAllServiceInfos();
		super.close();
	}

	public boolean removeServiceInfo(IDiscoveryAdvertiser advertiser,
			EndpointDescription endpointDescription) {
		return removeServiceInfoForUndiscovery(advertiser, endpointDescription) != null;
	}

	public void removeAllServiceInfos() {
		synchronized (serviceInfos) {
			serviceInfos.clear();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16874.java