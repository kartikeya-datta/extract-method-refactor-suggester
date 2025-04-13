error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5032.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5032.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[14,1]

error in qdox parser
file content:
```java
offset: 569
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5032.java
text:
```scala
public class DiscoveryTest extends TestCase {

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

p@@ackage org.eclipse.ecf.tests.discovery;

import java.net.InetAddress;
import java.util.Properties;

import junit.framework.TestCase;

import org.eclipse.ecf.core.ContainerFactory;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.discovery.IDiscoveryContainerAdapter;
import org.eclipse.ecf.discovery.IServiceEvent;
import org.eclipse.ecf.discovery.IServiceListener;
import org.eclipse.ecf.discovery.IServiceTypeListener;
import org.eclipse.ecf.discovery.ServiceInfo;
import org.eclipse.ecf.discovery.ServiceProperties;
import org.eclipse.ecf.discovery.identity.ServiceID;

public class JMDNSDiscoveryTest extends TestCase {
	
	static IContainer container = null;
	static IDiscoveryContainerAdapter discoveryContainer = null;
	static final String TEST_SERVICE_TYPE = "_ecftcp._tcp.local.";
	static final String TEST_PROTOCOL = "ecftcp";
	static final String TEST_HOST = "localhost";
	static final int TEST_PORT = 3282;
	static final String TEST_SERVICE_NAME = System.getProperty("user.name") + "." + TEST_PROTOCOL;
	
	public void testContainerCreate() throws Exception {
		container = ContainerFactory.getDefault().createContainer(
		"ecf.discovery.jmdns");
		assertNotNull(container);
	}
	
	public void testContainerConnect() throws Exception {
		container.connect(null, null);		
	}
	public void testDiscoveryContainerAdapter() throws Exception {
		discoveryContainer = (IDiscoveryContainerAdapter) container
		.getAdapter(IDiscoveryContainerAdapter.class);		
		assertNotNull(discoveryContainer);
	}
	
	public void testAddServiceTypeListener() throws Exception {
		discoveryContainer
		.addServiceTypeListener(new CollabServiceTypeListener());
	}
	
	public void testRegisterServiceType() throws Exception {
		discoveryContainer.registerServiceType(TEST_SERVICE_TYPE);
		System.out.println("registered service type "+TEST_SERVICE_TYPE+" waiting 5s");
		Thread.sleep(5000);
	}
	
	public void testRegisterService() throws Exception {
		Properties props = new Properties();
		String protocol = TEST_PROTOCOL;
		InetAddress host = InetAddress.getByName(TEST_HOST);
		int port = TEST_PORT;
		String svcName = System.getProperty("user.name") + "."
				+ protocol;
		ServiceInfo svcInfo = new ServiceInfo(host, new ServiceID(
				TEST_SERVICE_TYPE, svcName), port,
				0, 0, new ServiceProperties(props));
		discoveryContainer.registerService(svcInfo);
	}
	public final void testDiscovery() throws Exception {
		
		System.out.println("Discovery started.  Waiting 10s for discovered services");
		Thread.sleep(10000);
	}

	class CollabServiceTypeListener implements IServiceTypeListener {
		public void serviceTypeAdded(IServiceEvent event) {
			System.out.println("serviceTypeAdded(" + event + ")");
			ServiceID svcID = event.getServiceInfo().getServiceID();
			discoveryContainer.addServiceListener(svcID.getServiceType(),
					new CollabServiceListener());
			discoveryContainer.registerServiceType(svcID.getServiceType());
		}
	}
	class CollabServiceListener implements IServiceListener {
		public void serviceAdded(IServiceEvent event) {
			System.out.println("serviceAdded(" + event + ")");
			discoveryContainer.requestServiceInfo(event.getServiceInfo()
					.getServiceID(), 3000);
		}
		public void serviceRemoved(IServiceEvent event) {
			System.out.println("serviceRemoved(" + event + ")");
		}
		public void serviceResolved(IServiceEvent event) {
			System.out.println("serviceResolved(" + event + ")");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5032.java