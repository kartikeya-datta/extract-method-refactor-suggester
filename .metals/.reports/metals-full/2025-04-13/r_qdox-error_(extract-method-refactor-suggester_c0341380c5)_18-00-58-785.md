error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16254.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16254.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16254.java
text:
```scala
public l@@ong getLookupTimeout() {

/*******************************************************************************
 * Copyright (c) 2009 Markus Alexander Kuppe.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Markus Alexander Kuppe (ecf-dev_eclipse.org <at> lemmster <dot> de) - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.internal.osgi.services.discovery;

import org.eclipse.ecf.discovery.ServiceProperties;

import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.discovery.IServiceProperties;
import org.eclipse.ecf.discovery.identity.IServiceID;
import org.eclipse.ecf.osgi.services.discovery.ECFServicePublication;

import org.eclipse.ecf.core.identity.IDCreateException;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.discovery.IServiceInfo;

import org.eclipse.ecf.osgi.services.discovery.ECFServiceEndpointDescription;

public class ECFServiceEndpointDescriptionImpl extends
		ECFServiceEndpointDescription {

	private static final long DEFAULT_FUTURE_TIMEOUT = new Long(System
			.getProperty("ecf.rs.lookup.timeout", new Long(30000).toString()))
			.longValue();

	private final ID endpointId;
	private final IServiceID serviceId;
	
	public ECFServiceEndpointDescriptionImpl(IServiceInfo serviceInfo) {
		super(((ServiceProperties)serviceInfo.getServiceProperties()).asProperties());
		this.serviceId = serviceInfo.getServiceID();

		// create the endpoint id
		IServiceProperties serviceProperties = serviceInfo.getServiceProperties();
		final byte[] endpointBytes = 
			serviceProperties.getPropertyBytes(ECFServicePublication.PROP_KEY_ENDPOINT_CONTAINERID);
		if (endpointBytes == null)
			throw new IDCreateException(
					"ServiceEndpointDescription endpointBytes cannot be null");
		final String endpointStr = new String(endpointBytes);
		final String namespaceStr = 
			serviceProperties.getPropertyString(ECFServicePublication.PROP_KEY_ENDPOINT_CONTAINERID_NAMESPACE);
		if (namespaceStr == null) {
			throw new IDCreateException(
			"ServiceEndpointDescription namespaceStr cannot be null");
		}
		endpointId = IDFactory.getDefault().createID(namespaceStr,
				endpointStr);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.osgi.services.discovery.ECFServiceEndpointDescription#getECFEndpointID()
	 */
	public ID getECFEndpointID() throws IDCreateException {
		return endpointId;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ecf.osgi.services.discovery.ECFServiceEndpointDescription#getFutureTimeout()
	 */
	public long getFutureTimeout() {
		//TODO get from service properties?
		return DEFAULT_FUTURE_TIMEOUT;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((serviceId == null) ? 0 : serviceId.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ECFServiceEndpointDescriptionImpl other = (ECFServiceEndpointDescriptionImpl) obj;
		if (serviceId == null) {
			if (other.serviceId != null)
				return false;
		} else if (!serviceId.equals(other.serviceId))
			return false;
		return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16254.java