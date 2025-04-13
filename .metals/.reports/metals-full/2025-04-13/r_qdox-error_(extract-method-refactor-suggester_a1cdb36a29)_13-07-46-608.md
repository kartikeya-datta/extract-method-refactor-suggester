error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8686.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8686.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8686.java
text:
```scala
a@@ssertTrue(remoteServices.size() < remotesLength);

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

import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.remoteservice.Constants;
import org.eclipse.ecf.remoteservice.IRemoteCall;
import org.eclipse.ecf.remoteservice.IRemoteService;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.distribution.DistributionProvider;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public abstract class AbstractServiceRegisterTest extends
		AbstractDistributionTest {

	private static final int REGISTER_WAIT = 60000;

	public void testRegisterServer() throws Exception {
		Properties props = new Properties();
		// *For testing purposes only* -- Set the server container id property, so that the service is not
		// distributed by both the client and server (which are both running in the same process
		// for junit plugin tests)
		IContainer serverContainer = getServer();
		props.put(Constants.SERVICE_CONTAINER_ID, serverContainer.getID());
		
		// Set OSGI property that identifies this service as a service to be remoted
		props.put(OSGI_REMOTE_INTERFACES, new String[] {OSGI_REMOTE_INTERFACES_WILDCARD});
		// Actually register with default service (IConcatService)
		ServiceRegistration registration = registerDefaultService(props);
		// Wait a while
		Thread.sleep(REGISTER_WAIT);
		// Then unregister
		registration.unregister();
		Thread.sleep(REGISTER_WAIT);
	}

	protected ServiceTracker createProxyServiceTracker(String clazz) throws InvalidSyntaxException {
		ServiceTracker st = new ServiceTracker(getContext(),getContext().createFilter("(&("+org.osgi.framework.Constants.OBJECTCLASS+"=" + clazz +")(" + OSGI_REMOTE + "=*))"),new ServiceTrackerCustomizer() {

			public Object addingService(ServiceReference reference) {
				System.out.println("addingService="+reference);
				return getContext().getService(reference);
			}

			public void modifiedService(ServiceReference reference,
					Object service) {
				System.out.println("modifiedService="+reference);
				
			}

			public void removedService(ServiceReference reference,
					Object service) {
				System.out.println("removedService="+reference+",svc="+service);
			}});
		st.open();
		return st;
	}
	
	public void testGetProxy() throws Exception {
		String classname = TestServiceInterface1.class.getName();
		// Setup service tracker for client
		ServiceTracker st = createProxyServiceTracker(classname);
		
		// Server - register service with required OSGI property and some test properties
		Properties props = new Properties();
		// *For testing purposes only* -- Set the server container id property, so that the service is not
		// distributed by both the client and server (which are both running in the same process
		// for junit plugin tests)
		IContainer serverContainer = getServer();
		props.put(Constants.SERVICE_CONTAINER_ID, serverContainer.getID());
		// Set required OSGI property that identifies this service as a service to be remoted
		props.put(OSGI_REMOTE_INTERFACES, new String[] {OSGI_REMOTE_INTERFACES_WILDCARD});
		// Put property foo with value bar into published properties
		String testPropKey = "foo";
		String testPropVal = "bar";
		props.put(testPropKey, testPropVal);
		// Actually register and wait a while
		ServiceRegistration registration = registerService(classname, new TestService1(),props);
		Thread.sleep(REGISTER_WAIT);
		
		// Client - Get service references that are proxies
		ServiceReference [] remoteReferences = st.getServiceReferences();
		assertTrue(remoteReferences != null);
		assertTrue(remoteReferences.length > 0);
		for(int i=0; i < remoteReferences.length; i++) {
			// Get OBJECTCLASS property from first remote reference
			String[] classes = (String []) remoteReferences[i].getProperty(org.osgi.framework.Constants.OBJECTCLASS);
			assertTrue(classes != null);
			// Check object class
			assertTrue(classname.equals(classes[0]));
			// Check the prop
			String prop = (String) remoteReferences[i].getProperty(testPropKey);
			assertTrue(prop != null);
			assertTrue(prop.equals(testPropVal));
		}
		// Now unregister original registration and wait
		registration.unregister();
		st.close();
		Thread.sleep(REGISTER_WAIT);
	}
	
	public void testGetAndUseProxy() throws Exception {
		String classname = TestServiceInterface1.class.getName();
		// Setup service tracker for client
		ServiceTracker st = createProxyServiceTracker(classname);
		
		// Server - register service with required OSGI property and some test properties
		Properties props = new Properties();
		// *For testing purposes only* -- Set the server container id property, so that the service is not
		// distributed by both the client and server (which are both running in the same process
		// for junit plugin tests)
		IContainer serverContainer = getServer();
		props.put(Constants.SERVICE_CONTAINER_ID, serverContainer.getID());
		// Set required OSGI property that identifies this service as a service to be remoted
		props.put(OSGI_REMOTE_INTERFACES, new String[] {OSGI_REMOTE_INTERFACES_WILDCARD});
		// Actually register and wait a while
		ServiceRegistration registration = registerService(classname, new TestService1(),props);
		Thread.sleep(REGISTER_WAIT);
		
		// Client - Get service references from service tracker
		ServiceReference [] remoteReferences = st.getServiceReferences();
		assertTrue(remoteReferences != null);
		assertTrue(remoteReferences.length > 0);
		
		for(int i=0; i < remoteReferences.length; i++) {
			// Get proxy/service
			TestServiceInterface1 proxy = (TestServiceInterface1) getContext().getService(remoteReferences[0]);
			assertNotNull(proxy);
			// Now use proxy
			String result = proxy.doStuff1();
			System.out.println("proxy.doStuff1 result="+result);
			assertTrue(TestServiceInterface1.TEST_SERVICE_STRING1.equals(result));
		}
		
		// Unregister on server and wait
		registration.unregister();
		st.close();
		Thread.sleep(REGISTER_WAIT);
	}

	public void testGetAndUseIRemoteService() throws Exception {
		String classname = TestServiceInterface1.class.getName();
		// Setup service tracker for client
		ServiceTracker st = createProxyServiceTracker(classname);
		
		// Server - register service with required OSGI property and some test properties
		Properties props = new Properties();
		// *For testing purposes only* -- Set the server container id property, so that the service is not
		// distributed by both the client and server (which are both running in the same process
		// for junit plugin tests)
		IContainer serverContainer = getServer();
		props.put(Constants.SERVICE_CONTAINER_ID, serverContainer.getID());
		// Set required OSGI property that identifies this service as a service to be remoted
		props.put(OSGI_REMOTE_INTERFACES, new String[] {OSGI_REMOTE_INTERFACES_WILDCARD});
		// Actually register and wait a while
		ServiceRegistration registration = registerService(classname, new TestService1(),props);
		Thread.sleep(REGISTER_WAIT);
		
		// Client - Get service references from service tracker
		ServiceReference [] remoteReferences = st.getServiceReferences();
		assertTrue(remoteReferences != null);
		assertTrue(remoteReferences.length > 0);
		
		for(int i=0; i < remoteReferences.length; i++) {
			Object o = remoteReferences[i].getProperty(OSGI_REMOTE);
			assertNotNull(o);
			assertTrue(o instanceof IRemoteService);
			IRemoteService rs = (IRemoteService) o;
			// Now call rs methods
			IRemoteCall call = createRemoteCall(TestServiceInterface1.class);
			if (call != null) {
				// Call synchronously
				Object result = rs.callSync(call);
				System.out.println("callSync.doStuff1 result="+result);
				assertNotNull(result);
				assertTrue(result instanceof String);
				assertTrue(TestServiceInterface1.TEST_SERVICE_STRING1.equals(result));
			}
		}
		
		// Unregister on server
		registration.unregister();
		st.close();
		Thread.sleep(REGISTER_WAIT);
	}

	public void testGetExposedServicesFromDistributionProvider() throws Exception {
		String classname = TestServiceInterface1.class.getName();
		// Setup service tracker for distribution provider
		ServiceTracker st = new ServiceTracker(getContext(),DistributionProvider.class.getName(),null);
		st.open();
		DistributionProvider distributionProvider = (DistributionProvider) st.getService();
		assertNotNull(distributionProvider);
		
		// The returned collection should not be null
		Collection exposedServices = distributionProvider.getExposedServices();
		assertNotNull(exposedServices);

		// Server - register service with required OSGI property and some test properties
		Properties props = new Properties();
		// *For testing purposes only* -- Set the server container id property, so that the service is not
		// distributed by both the client and server (which are both running in the same process
		// for junit plugin tests)
		IContainer serverContainer = getServer();
		props.put(Constants.SERVICE_CONTAINER_ID, serverContainer.getID());
		// Set required OSGI property that identifies this service as a service to be remoted
		props.put(OSGI_REMOTE_INTERFACES, new String[] {OSGI_REMOTE_INTERFACES_WILDCARD});
		// Actually register and wait a while
		ServiceRegistration registration = registerService(classname, new TestService1(),props);
		Thread.sleep(REGISTER_WAIT);

		// Client
		exposedServices = distributionProvider.getExposedServices();
		assertNotNull(exposedServices);
		int exposedLength = exposedServices.size();
		assertTrue(exposedLength > 0);
		for(Iterator i=exposedServices.iterator(); i.hasNext(); ) {
			Object o = ((ServiceReference) i.next()).getProperty(OSGI_REMOTE_INTERFACES);
			assertTrue(o != null);
		}

		// Unregister on server
		registration.unregister();
		st.close();
		Thread.sleep(REGISTER_WAIT);
		
		// Check to see that the exposed service went away
		exposedServices= distributionProvider.getExposedServices();
		assertNotNull(exposedServices);
		assertTrue(exposedServices.size() == (exposedLength - 1));

	}

	public void testGetRemoteServicesFromDistributionProvider() throws Exception {
		String classname = TestServiceInterface1.class.getName();
		// Setup service tracker for distribution provider
		ServiceTracker st = new ServiceTracker(getContext(),DistributionProvider.class.getName(),null);
		st.open();
		DistributionProvider distributionProvider = (DistributionProvider) st.getService();
		assertNotNull(distributionProvider);
		
		// The returned collection should not be null
		Collection remoteServices = distributionProvider.getRemoteServices();
		assertNotNull(remoteServices);

		// Server - register service with required OSGI property and some test properties
		Properties props = new Properties();
		// *For testing purposes only* -- Set the server container id property, so that the service is not
		// distributed by both the client and server (which are both running in the same process
		// for junit plugin tests)
		IContainer serverContainer = getServer();
		props.put(Constants.SERVICE_CONTAINER_ID, serverContainer.getID());
		// Set required OSGI property that identifies this service as a service to be remoted
		props.put(OSGI_REMOTE_INTERFACES, new String[] {OSGI_REMOTE_INTERFACES_WILDCARD});
		// Actually register and wait a while
		ServiceRegistration registration = registerService(classname, new TestService1(),props);
		Thread.sleep(REGISTER_WAIT);
		
		// Check that distribution provider (client) has remote services now
		remoteServices = distributionProvider.getRemoteServices();
		assertNotNull(remoteServices);
		int remotesLength = remoteServices.size();
		assertTrue(remotesLength > 0);
		for(Iterator i=remoteServices.iterator(); i.hasNext(); ) {
			Object o = ((ServiceReference) i.next()).getProperty(OSGI_REMOTE);
			assertTrue(o != null);
		}
		// Unregister on server
		registration.unregister();
		st.close();
		Thread.sleep(REGISTER_WAIT);
		
		// Remote services should have gone down by one (because of unregister
		remoteServices= distributionProvider.getRemoteServices();
		assertNotNull(remoteServices);
		assertTrue(remoteServices.size() == (remotesLength - 1));

	}

	protected IRemoteCall createRemoteCall(Class clazz) {
		if (clazz.equals(TestServiceInterface1.class)) {
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
		return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8686.java