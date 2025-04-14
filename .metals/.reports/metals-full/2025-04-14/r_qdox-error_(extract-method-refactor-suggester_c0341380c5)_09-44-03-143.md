error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7920.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7920.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7920.java
text:
```scala
T@@hread.sleep(SLEEPTIME);

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

package org.eclipse.ecf.tests.remoteservice.generic;

import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.ecf.remoteservice.IRemoteService;
import org.eclipse.ecf.remoteservice.IRemoteServiceContainerAdapter;
import org.eclipse.ecf.remoteservice.IRemoteServiceListener;
import org.eclipse.ecf.remoteservice.IRemoteServiceProxy;
import org.eclipse.ecf.remoteservice.IRemoteServiceReference;
import org.eclipse.ecf.remoteservice.RemoteServiceHelper;
import org.eclipse.ecf.remoteservice.events.IRemoteServiceEvent;
import org.eclipse.ecf.remoteservice.events.IRemoteServiceRegisteredEvent;
import org.eclipse.ecf.remoteservice.util.tracker.RemoteServiceTracker;
import org.eclipse.ecf.tests.remoteservice.AbstractRemoteServiceTest;
import org.eclipse.ecf.tests.remoteservice.IConcatService;
import org.eclipse.equinox.concurrent.future.IFuture;

/**
 *
 */
public class RemoteServiceProxyTest extends AbstractRemoteServiceTest {

	RemoteServiceTracker remoteServiceTracker;
	
	/* (non-Javadoc)
	 * @see org.eclipse.ecf.tests.remoteservice.AbstractRemoteServiceTest#getClientContainerName()
	 */
	protected String getClientContainerName() {
		return Generic.CONSUMER_CONTAINER_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		setClientCount(2);
		createServerAndClients();
		setupRemoteServiceAdapters();
		connectClients();
		addRemoteServiceListeners();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		cleanUpServerAndClients();
		if (remoteServiceTracker != null) {
			remoteServiceTracker.close();
		}
		super.tearDown();
	}

	protected IRemoteService getRemoteService(IRemoteServiceContainerAdapter adapter, String clazz, String filter) {
		remoteServiceTracker = new RemoteServiceTracker(adapter, null, clazz, null);
		assertNotNull(remoteServiceTracker);
		remoteServiceTracker.open();
		return remoteServiceTracker.getRemoteService();
	}


	public void testRemoteServiceProxy() throws Exception {
		final IRemoteServiceContainerAdapter[] adapters = getRemoteServiceAdapters();
		// client [0]/adapter[0] is the service 'server'
		// client [1]/adapter[1] is the service target (client)
		final Dictionary props = new Hashtable();
		// Register
		adapters[0].registerRemoteService(new String[] {IConcatService.class.getName()}, createService(), props);
		// Give some time for propagation
		sleep(3000);

		final RemoteServiceTracker st = new RemoteServiceTracker(adapters[1], null, IConcatService.class.getName(), null);
		assertNotNull(st);
		st.open();
		IRemoteService rs = st.getRemoteService();
		final IConcatService concatService = (IConcatService) rs.getProxy();
		assertNotNull(concatService);
		// test for proxy implementing IRemoteServiceProxy
		if (concatService instanceof IRemoteServiceProxy) {
			IRemoteService remoteService = ((IRemoteServiceProxy) concatService).getRemoteService();
			assertNotNull(remoteService);
			IRemoteServiceReference remoteServiceReference = ((IRemoteServiceProxy) concatService).getRemoteServiceReference();
			assertNotNull(remoteServiceReference);
			System.out.println("remote service reference found from proxy="+remoteServiceReference);
			System.out.println("remoteserviceproxy call start");
			Object result = RemoteServiceHelper.syncExec(remoteService, "concat" , new Object[] { "IRemoteServiceProxy ","is very cool" }, 20000);
			System.out.println("remoteserviceproxy call end. result=" + result);
		} else {
			System.out.println("proxy call start");
			final String result = concatService.concat("OSGi ", "is cool");
			System.out.println("proxy call end. result=" + result);
		}
		sleep(3000);
		st.close();
		sleep(3000);
	}

	IRemoteService remoteService;
	boolean done;

	public void testServiceListener() throws Exception {
		final IRemoteServiceContainerAdapter[] adapters = getRemoteServiceAdapters();
		done = false;
		final Object lock = new Object();
		adapters[1].addRemoteServiceListener(new IRemoteServiceListener() {
			public void handleServiceEvent(IRemoteServiceEvent event) {
				if (event instanceof IRemoteServiceRegisteredEvent) {
					IRemoteServiceRegisteredEvent e = (IRemoteServiceRegisteredEvent) event;
					IRemoteServiceReference ref = e.getReference();
					remoteService = adapters[1].getRemoteService(ref);
					assertNotNull(remoteService);
					synchronized (lock) {
						done = true;
						lock.notify();
					}
				}
			}
		});

		// Now register service on server (adapters[0]).  This should result in notification on client (adapters[1])
		// in above handleServiceEvent
		adapters[0].registerRemoteService(new String[] { IConcatService.class.getName() }, createService(),
				customizeProperties(null));

		// wait until block above called asynchronously
		int count = 0;
		synchronized (lock) {
			while (!done && count++ < 10) {
				try {
					lock.wait(1000);
				} catch (InterruptedException e) {
					fail();
				}
			}
		}

		assertTrue(done);

		if (remoteService == null) return;
		// We've got the remote service, so we're good to go
		assertTrue(remoteService != null);
		traceCallStart("callAsynchResult");
		final IFuture result = remoteService
				.callAsync(createRemoteConcat("ECF AsynchResults ", "are cool"));
		traceCallEnd("callAsynch");
		assertNotNull(result);
		Thread.sleep(ASYNC_WAITTIME);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7920.java