error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12924.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12924.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12924.java
text:
```scala
u@@ri = DiscoveryTestHelper.createDefaultURI(DiscoveryTestHelper.HOSTNAME);

/*******************************************************************************
 * Copyright (c) 2007 Versant Corp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Markus Kuppe (mkuppe <at> versant <dot> com) - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.tests.discovery;

import java.net.URI;
import java.util.Comparator;

import junit.framework.TestCase;

import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.discovery.IServiceInfo;
import org.eclipse.ecf.discovery.IServiceProperties;
import org.eclipse.ecf.discovery.ServiceInfo;
import org.eclipse.ecf.discovery.ServiceProperties;
import org.eclipse.ecf.discovery.identity.IServiceTypeID;
import org.eclipse.ecf.discovery.identity.ServiceIDFactory;

/**
 * 
 */
public abstract class ServiceInfoTest extends TestCase {

	protected URI uri;
	protected IServiceTypeID serviceTypeID;
	protected int priority;
	protected int weight;
	protected IServiceProperties serviceProperties;
	protected IServiceInfo serviceInfo;
	protected Comparator serviceInfoComparator = new ServiceInfoComparator();

	public ServiceInfoTest(Namespace namespace) {
		uri = DiscoveryTestHelper.createDefaultURI();
		priority = DiscoveryTestHelper.PRIORITY;
		weight = DiscoveryTestHelper.WEIGHT;
		serviceProperties = new ServiceProperties();
		serviceProperties.setProperty("foobar", new String("foobar"));
		serviceProperties.setPropertyBytes("foobar1", new byte[] { 1, 2, 3 });
		try {
			serviceTypeID = ServiceIDFactory.getDefault().createServiceTypeID(namespace, DiscoveryTestHelper.SERVICES, DiscoveryTestHelper.PROTOCOLS);
		} catch (IDCreateException e) {
			fail(e.getMessage());
		}

		serviceInfo = new ServiceInfo(uri, DiscoveryTestHelper.SERVICENAME, serviceTypeID, priority, weight,
				serviceProperties);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		assertNotNull(uri);
		assertNotNull(serviceTypeID);
		assertNotNull(serviceProperties);
		assertNotNull(serviceInfo);
		assertNotNull(serviceInfoComparator);
	}

	/**
	 * Test method for {@link org.eclipse.ecf.discovery.ServiceInfo#getLocation()}.
	 */
	public void testGetLocation() {
		assertEquals(serviceInfo.getServiceID().getLocation(), uri);
	}

	/**
	 * Test method for {@link org.eclipse.ecf.discovery.ServiceInfo#getServiceID()}.
	 */
	public void testGetServiceID() {
		assertNotNull(serviceInfo.getServiceID());
		assertEquals(serviceInfo.getServiceID().getServiceTypeID(), serviceTypeID);
	}

	/**
	 * Test method for {@link org.eclipse.ecf.discovery.ServiceInfo#getPriority()}.
	 */
	public void testGetPriority() {
		assertTrue(serviceInfo.getPriority() == priority);
	}

	/**
	 * Test method for {@link org.eclipse.ecf.discovery.ServiceInfo#getWeight()}.
	 */
	public void testGetWeight() {
		assertTrue(serviceInfo.getWeight() == weight);
	}

	/**
	 * Test method for {@link org.eclipse.ecf.discovery.ServiceInfo#getServiceProperties()}.
	 */
	public void testGetServiceProperties() {
		final IServiceProperties sprops = serviceInfo.getServiceProperties();
		assertEquals(sprops, serviceProperties);
	}

	/**
	 * Test method for {@link java.lang.Object#hashCode()}.
	 */
//	public void testHashCode() {
//		fail("Not yet implemented. How should equality be defined anyway?");
//	}

	/**
	 * Test method for {@link java.lang.Object#equals(java.lang.Object)}.
	 */
//	public void testEquals() {
//		fail("Not yet implemented. How should equality be defined anyway?");
//	}

	/**
	 * Test method for {@link org.eclipse.ecf.discovery.ServiceInfo}.
	 */
	public void testServiceInfo() {
		IServiceInfo si = null;
		try {
			si = getServiceInfo(serviceInfo);
		} catch (final SecurityException e) {
			fail();
		}
		assertTrue(serviceInfoComparator.compare(si, serviceInfo) == 0);
	}

	protected IServiceInfo getServiceInfo(IServiceInfo aServiceInfo) {
		return serviceInfo;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12924.java