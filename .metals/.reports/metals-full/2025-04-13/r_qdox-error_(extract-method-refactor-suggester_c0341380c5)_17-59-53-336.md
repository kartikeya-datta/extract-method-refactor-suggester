error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6201.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6201.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6201.java
text:
```scala
E@@CFServicePublication.DISCOVERY_CONTAINER_ID_PROP, localContainerID);

/*******************************************************************************
 * Copyright (c) 2009 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.internal.osgi.services.discovery;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.discovery.IServiceInfo;
import org.eclipse.ecf.discovery.IServiceProperties;
import org.eclipse.ecf.osgi.services.discovery.ECFServicePublication;
import org.osgi.service.discovery.ServiceEndpointDescription;
import org.osgi.service.discovery.ServicePublication;

public class ServiceEndpointDescriptionImpl implements
		ServiceEndpointDescription {

	private final IServiceInfo serviceInfo;

	public ServiceEndpointDescriptionImpl(ID localContainerID,
			IServiceInfo serviceInfo) {
		this.serviceInfo = serviceInfo;
		// add localContainerID to set of service properties exposed by this
		this.serviceInfo.getServiceProperties().setProperty(
				ECFServicePublication.CONTAINER_ID_PROP, localContainerID);
	}

	public String getEndpointID() {
		return ServicePropertyUtils.getStringProperty(serviceInfo
				.getServiceProperties(),
				ServicePublication.PROP_KEY_ENDPOINT_ID);
	}

	public String getEndpointInterfaceName(String interfaceName) {
		if (interfaceName == null)
			return null;
		String intfNames = serviceInfo.getServiceProperties()
				.getPropertyString(
						ServicePublication.PROP_KEY_ENDPOINT_INTERFACE_NAME);
		if (intfNames == null)
			return null;
		Collection c = ServicePropertyUtils
				.createCollectionFromString(intfNames);
		if (c == null)
			return null;
		// 
		for (Iterator i = c.iterator(); i.hasNext();) {
			String intfName = (String) i.next();
			if (intfName != null && intfName.startsWith(interfaceName)) {
				// return just endpointInterfaceName
				return intfName
						.substring(
								intfName.length()
										+ ServicePropertyUtils.ENDPOINT_INTERFACE_NAME_SEPARATOR
												.length()).trim();
			}
		}
		return null;
	}

	public URL getLocation() {
		String urlExternalForm = ServicePropertyUtils.getStringProperty(
				serviceInfo.getServiceProperties(),
				ServicePublication.PROP_KEY_ENDPOINT_LOCATION);
		if (urlExternalForm == null)
			return null;
		URL url = null;
		try {
			url = new URL(urlExternalForm);
		} catch (MalformedURLException e) {
			// XXX log to error
		}
		return url;
	}

	public Map getProperties() {
		Map result = new HashMap();
		IServiceProperties serviceProperties = serviceInfo
				.getServiceProperties();
		if (serviceProperties != null) {
			for (Enumeration e = serviceProperties.getPropertyNames(); e
					.hasMoreElements();) {
				String propName = (String) e.nextElement();
				Object val = serviceProperties.getProperty(propName);
				result.put(propName, val);
			}
		}
		return result;
	}

	public Object getProperty(String key) {
		IServiceProperties serviceProperties = serviceInfo
				.getServiceProperties();
		if (key == null)
			return null;
		return serviceProperties.getProperty(key);
	}

	public Collection getPropertyKeys() {
		IServiceProperties serviceProperties = serviceInfo
				.getServiceProperties();
		List result = new ArrayList();
		for (Enumeration e = serviceProperties.getPropertyNames(); e
				.hasMoreElements();) {
			String name = (String) e.nextElement();
			result.add(name);
		}
		return result;
	}

	public Collection getProvidedInterfaces() {
		String providedInterfacesStr = serviceInfo.getServiceProperties()
				.getPropertyString(
						ServicePublication.PROP_KEY_SERVICE_INTERFACE_NAME);
		return ServicePropertyUtils
				.createCollectionFromString(providedInterfacesStr);
	}

	public String getVersion(String interfaceName) {
		String intfNames = serviceInfo.getServiceProperties()
				.getPropertyString(
						ServicePublication.PROP_KEY_SERVICE_INTERFACE_VERSION);
		if (intfNames == null)
			return null;
		Collection c = ServicePropertyUtils
				.createCollectionFromString(intfNames);
		if (c == null)
			return null;
		// 
		for (Iterator i = c.iterator(); i.hasNext();) {
			String intfName = (String) i.next();
			if (intfName != null && intfName.startsWith(interfaceName)) {
				// return just version string
				return intfName
						.substring(
								intfName.length()
										+ ServicePropertyUtils.INTERFACE_VERSION_SEPARATOR
												.length()).trim();
			}
		}
		return null;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("ServiceEndpointDescriptionImpl[");
		sb.append("providedinterfaces=").append(getProvidedInterfaces());
		sb.append(";location=").append(getLocation());
		sb.append(";props=").append(getProperties()).append("]");
		return sb.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6201.java