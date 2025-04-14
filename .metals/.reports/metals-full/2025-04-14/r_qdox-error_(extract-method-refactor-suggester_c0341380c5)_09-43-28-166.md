error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3259.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3259.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,24]

error in qdox parser
file content:
```java
offset: 24
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3259.java
text:
```scala
protected static final i@@nt REGISTER_WAIT = Integer.parseInt(System.getProperty("waittime","30000"));

/*******************************************************************************
 * Copyright (c) 2009 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.tests.osgi.services.distribution;

import java.util.Properties;

import org.eclipse.ecf.core.util.Trace;
import org.eclipse.ecf.remoteservice.IRemoteCall;
import org.eclipse.ecf.remoteservice.IRemoteCallListener;
import org.eclipse.ecf.remoteservice.IRemoteService;
import org.eclipse.ecf.remoteservice.events.IRemoteCallCompleteEvent;
import org.eclipse.ecf.remoteservice.events.IRemoteCallEvent;
import org.eclipse.ecf.tests.internal.osgi.services.distribution.Activator;
import org.eclipse.equinox.concurrent.future.IFuture;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

public abstract class AbstractRemoteServiceAccessTest extends
		AbstractDistributionTest {

	protected static final int REGISTER_WAIT = Integer.parseInt(System.getProperty("waittime","15000"));

	private final String classname = TestServiceInterface1.class.getName();
	private ServiceTracker st;
	private ServiceRegistration registration;

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.tests.osgi.services.distribution.AbstractDistributionTest#tearDown()
	 */
	protected void tearDown() throws Exception {
		// Unregister on server
		if (registration != null) {
			registration.unregister();
			registration = null;
		}
		if (st != null) {
			st.close();
			st = null;
		}
		Thread.sleep(REGISTER_WAIT);

		super.tearDown();
	}

	protected void createServiceTrackerAndRegister(final Properties props) throws Exception {
		// Setup service tracker for client
		st = createProxyServiceTracker(classname);

		// Actually register
		registration = registerService(classname,
				new TestService1(), props);

		// Wait
		Thread.sleep(REGISTER_WAIT);
	}

	protected void createServiceTrackerAndRegister() throws Exception {
		createServiceTrackerAndRegister(getServiceProperties());
	}

	protected Properties getServiceProperties() {
		final Properties props = new Properties();
		props.put(SERVICE_EXPORTED_CONFIGS, getServerContainerName());
		props.put(SERVICE_EXPORTED_INTERFACES, SERVICE_EXPORTED_INTERFACES_WILDCARD);
		return props;
	}

	protected IRemoteCall createRemoteCall() {
		return new IRemoteCall() {

			public String getMethod() {
				return "doStuff1";
			}

			public Object[] getParameters() {
				return new Object[] {};
			}

			public long getTimeout() {
				return 30000;
			}

		};
	}


	public void testGetRemoteServiceReference() throws Exception {
		startTest("testGetRemoteServiceReference");
		createServiceTrackerAndRegister();
		
		// Service Consumer - Get (remote) ervice references
		final ServiceReference[] remoteReferences = st.getServiceReferences();
		assertReferencesValidAndFirstHasCorrectType(remoteReferences, classname);
		// Spec requires that the 'service.imported' property be set
		assertTrue(remoteReferences[0].getProperty(SERVICE_IMPORTED) != null);
		endTest("testGetRemoteServiceReference");
	}
	
	public void testGetRemoteServiceReferenceWithExtraProperties() throws Exception {
		startTest("testGetRemoteServiceReferenceWithExtraProperties");
		final String TESTPROP1_VALUE = "baz";
		final String TESTPROP_VALUE = "foobar";
		final String TESTPROP1_NAME = "org.eclipse.ecf.testprop1";
		final String TESTPROP_NAME = "org.eclipse.ecf.testprop";

		final Properties props = getServiceProperties();
		// Add other properties
		props.put(TESTPROP_NAME, TESTPROP_VALUE);
		props.put(TESTPROP1_NAME,TESTPROP1_VALUE);
		createServiceTrackerAndRegister(props);

		// Service Consumer - Get (remote) ervice references
		final ServiceReference[] remoteReferences = st.getServiceReferences();
		assertReferencesValidAndFirstHasCorrectType(remoteReferences, classname);
		// Spec requires that the 'service.imported' property be set
		assertTrue(remoteReferences[0].getProperty(SERVICE_IMPORTED) != null);
		
		final String testProp = (String) remoteReferences[0].getProperty(TESTPROP_NAME);
		final String testProp1 = (String) remoteReferences[0].getProperty(TESTPROP1_NAME);
		assertTrue(TESTPROP_VALUE.equals(testProp));
		assertTrue(TESTPROP1_VALUE.equals(testProp1));
		endTest("testGetRemoteServiceReferenceWithExtraProperties");
	}


	public void testProxy() throws Exception {
		startTest("testProxy");
		createServiceTrackerAndRegister();
		
		// Client - Get service references from service tracker
		final ServiceReference[] remoteReferences = st.getServiceReferences();
		assertReferencesValidAndFirstHasCorrectType(remoteReferences, classname);

		// Get proxy/service
		final TestServiceInterface1 proxy = (TestServiceInterface1) getContext()
				.getService(remoteReferences[0]);
		assertNotNull(proxy);
		// Now use proxy
		final String result = proxy.doStuff1();
		Trace.trace(Activator.PLUGIN_ID, "proxy.doStuff1 result=" + result);
		assertTrue(TestServiceInterface1.TEST_SERVICE_STRING1.equals(result));
		endTest("testProxy");
	}

	public void testCallSyncFromProxy() throws Exception {
		startTest("testCallSyncFromProxy");
		createServiceTrackerAndRegister();
		
		// Client - Get service references from service tracker
		final ServiceReference[] remoteReferences = st.getServiceReferences();
		assertReferencesValidAndFirstHasCorrectType(remoteReferences, classname);

		// Get proxy
		final TestServiceInterface1 proxy = (TestServiceInterface1) getContext()
				.getService(remoteReferences[0]);

		assertProxyValid(proxy);
		
		// Get IRemoteService from proxy
		final IRemoteService remoteService = getRemoteServiceFromProxy(proxy);

		// Call remote service synchronously
		final Object result = remoteService.callSync(createRemoteCall());
		Trace.trace(Activator.PLUGIN_ID, "proxy.doStuff1 result=" + result);
		assertStringResultValid(result, TestServiceInterface1.TEST_SERVICE_STRING1);
		endTest("testCallSyncFromProxy");
	}

	public void testCallSync() throws Exception {
		startTest("testCallSync");
		createServiceTrackerAndRegister();
		
		// Client - Get service references from service tracker
		final ServiceReference[] remoteReferences = st.getServiceReferences();
		assertReferencesValidAndFirstHasCorrectType(remoteReferences, classname);

		final Object o = remoteReferences[0].getProperty(SERVICE_IMPORTED);
		assertNotNull(o);
		assertTrue(o instanceof IRemoteService);
		final IRemoteService rs = (IRemoteService) o;

		// Call synchronously
		final Object result = rs.callSync(createRemoteCall());
		Trace.trace(Activator.PLUGIN_ID, "callSync.doStuff1 result=" + result);
		assertStringResultValid(result, TestServiceInterface1.TEST_SERVICE_STRING1);
		endTest("testCallSync");
	}

	public void testCallAsync() throws Exception {
		startTest("testCallAsync");
		createServiceTrackerAndRegister();
		
		// Client - Get service references from service tracker
		final ServiceReference[] remoteReferences = st.getServiceReferences();
		assertReferencesValid(remoteReferences);

		final Object o = remoteReferences[0].getProperty(SERVICE_IMPORTED);
		assertNotNull(o);
		assertTrue(o instanceof IRemoteService);
		final IRemoteService rs = (IRemoteService) o;
		// Call asynchronously
		rs.callAsync(createRemoteCall(), new IRemoteCallListener() {
			public void handleEvent(final IRemoteCallEvent event) {
				if (event instanceof IRemoteCallCompleteEvent) {
					final Object result = ((IRemoteCallCompleteEvent) event)
							.getResponse();
					Trace.trace(Activator.PLUGIN_ID,
							"callSync.doStuff1 result=" + result);
					assertStringResultValid(result,TestServiceInterface1.TEST_SERVICE_STRING1);
					syncNotify();
				}
			}
		});

		syncWaitForNotify(REGISTER_WAIT);
		endTest("testCallAsync");
	}

	public void testCallFuture() throws Exception {
		startTest("testCallFuture");
		createServiceTrackerAndRegister();
		
		// Client - Get service references from service tracker
		final ServiceReference[] remoteReferences = st.getServiceReferences();
		assertReferencesValid(remoteReferences);

		final Object o = remoteReferences[0].getProperty(SERVICE_IMPORTED);
		assertNotNull(o);
		assertTrue(o instanceof IRemoteService);
		final IRemoteService rs = (IRemoteService) o;
		// Call asynchronously
		final IFuture futureResult = rs.callAsync(createRemoteCall());

		// now get result from futureResult
		final Object result = futureResult.get();
		Trace.trace(Activator.PLUGIN_ID, "callSync.doStuff1 result=" + result);
		assertStringResultValid(result, TestServiceInterface1.TEST_SERVICE_STRING1);
		endTest("testCallFuture");
	}

	public void testFireAsync() throws Exception {
		startTest("testFireAsync");
		createServiceTrackerAndRegister();
		
		// Client - Get service references from service tracker
		final ServiceReference[] remoteReferences = st.getServiceReferences();
		assertReferencesValid(remoteReferences);

		final Object o = remoteReferences[0].getProperty(SERVICE_IMPORTED);
		assertNotNull(o);
		assertTrue(o instanceof IRemoteService);
		final IRemoteService rs = (IRemoteService) o;
		// Call asynchronously
		rs.fireAsync(createRemoteCall());
		Thread.sleep(REGISTER_WAIT);
		endTest("testFireAsync");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3259.java