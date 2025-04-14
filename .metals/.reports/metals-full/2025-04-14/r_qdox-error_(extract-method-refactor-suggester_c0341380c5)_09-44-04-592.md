error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14656.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14656.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14656.java
text:
```scala
final S@@tring targetStr = new String(targetBytes);

/*******************************************************************************
 * Copyright (c) 2009 Markus Alexander Kuppe, Composent, Inc., and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Markus Alexander Kuppe (ecf-dev_eclipse.org <at> lemmster <dot> de) - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.internal.osgi.services.discovery;

import java.net.URI;
import java.util.Arrays;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.discovery.IServiceInfo;
import org.eclipse.ecf.discovery.IServiceProperties;
import org.eclipse.ecf.discovery.ServiceProperties;
import org.eclipse.ecf.discovery.identity.IServiceID;
import org.eclipse.ecf.osgi.services.discovery.RemoteServiceEndpointDescription;
import org.eclipse.ecf.osgi.services.discovery.RemoteServicePublication;

public class RemoteServiceEndpointDescriptionImpl extends
		RemoteServiceEndpointDescription {

	private final ID endpointId;
	private final IServiceID serviceId;
	private ID targetId = null;
	private int hashCode = 7;

	public RemoteServiceEndpointDescriptionImpl(IServiceInfo serviceInfo) {
		super(((ServiceProperties) serviceInfo.getServiceProperties())
				.asProperties());
		this.serviceId = serviceInfo.getServiceID();

		// create the endpoint id
		IServiceProperties serviceProperties = serviceInfo
				.getServiceProperties();
		final byte[] endpointBytes = serviceProperties
				.getPropertyBytes(RemoteServicePublication.ENDPOINT_CONTAINERID);
		if (endpointBytes == null)
			throw new IDCreateException(
					"ServiceEndpointDescription endpointBytes cannot be null");
		final String endpointStr = new String(endpointBytes);
		final String namespaceStr = serviceProperties
				.getPropertyString(RemoteServicePublication.ENDPOINT_CONTAINERID_NAMESPACE);
		if (namespaceStr == null) {
			throw new IDCreateException(
					"ServiceEndpointDescription namespaceStr cannot be null");
		}
		endpointId = IDFactory.getDefault().createID(namespaceStr, endpointStr);

		// create the target id, if it's there
		final byte[] targetBytes = serviceProperties
				.getPropertyBytes(RemoteServicePublication.TARGET_CONTAINERID);
		// If this is null, we're ok with it
		if (targetBytes != null) {
			final String targetStr = new String(endpointBytes);
			String targetNamespaceStr = serviceProperties
					.getPropertyString(RemoteServicePublication.TARGET_CONTAINERID_NAMESPACE);
			if (targetNamespaceStr == null)
				targetNamespaceStr = namespaceStr;
			targetId = IDFactory.getDefault().createID(targetNamespaceStr,
					targetStr);
		}

		// Get location and compute hashCode
		URI serviceLocation = this.serviceId.getLocation();
		long rsId = this.getRemoteServiceId();
		hashCode = 31 * hashCode + (int) (rsId ^ (rsId >>> 32));
		hashCode = 31 * hashCode + serviceLocation.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ecf.osgi.services.discovery.RemoteServiceEndpointDescription
	 * #getECFEndpointID()
	 */
	public ID getEndpointAsID() throws IDCreateException {
		return endpointId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ecf.osgi.services.discovery.RemoteServiceEndpointDescription
	 * #getECFTargetID()
	 */
	public ID getConnectTargetID() {
		return targetId;
	}

	public int hashCode() {
		return hashCode;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RemoteServiceEndpointDescriptionImpl other = (RemoteServiceEndpointDescriptionImpl) obj;
		return this.serviceId.getLocation().equals(
				other.serviceId.getLocation())
				&& getRemoteServiceId() == other.getRemoteServiceId();
	}

	public IServiceID getServiceID() {
		return serviceId;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("ServiceEndpointDescriptionImpl["); //$NON-NLS-1$
		sb.append(";providedInterfaces=").append(getProvidedInterfaces()); //$NON-NLS-1$
		String[] supportedConfigs = getSupportedConfigs();
		sb.append(";supportedConfigTypes").append(
				(supportedConfigs == null) ? "null" : Arrays.asList(
						supportedConfigs).toString());
		String[] serviceIntents = getServiceIntents();
		sb.append(";serviceIntents").append(
				(serviceIntents == null) ? "null" : Arrays.asList(
						serviceIntents).toString());
		sb.append(";location=").append(getLocation()); //$NON-NLS-1$
		sb.append(";remoteServiceId=").append(getRemoteServiceId()); //$NON-NLS-1$
		sb.append(";discoveryServiceID=").append(getServiceID()); //$NON-NLS-1$
		sb.append(";endpointID=").append(getEndpointID()); //$NON-NLS-1$
		sb.append(";endpointAsID=").append(getEndpointAsID()); //$NON-NLS-1$
		sb.append(";connectTargetID=").append(getConnectTargetID()); //$NON-NLS-1$
		sb.append(";remoteServicesFilter=").append(getRemoteServicesFilter()); //$NON-NLS-1$
		sb.append(";props=").append(getProperties()).append("]"); //$NON-NLS-1$ //$NON-NLS-2$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14656.java