error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/49.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/49.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/49.java
text:
```scala
s@@tart(adapter);

/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.springframework.jmx.access;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.BindException;
import java.util.HashMap;
import java.util.Map;

import javax.management.Descriptor;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.junit.Test;
import org.springframework.jmx.AbstractMBeanServerTests;
import org.springframework.jmx.IJmxTestBean;
import org.springframework.jmx.JmxException;
import org.springframework.jmx.JmxTestBean;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.export.assembler.AbstractReflectiveMBeanInfoAssembler;
import org.springframework.tests.Assume;
import org.springframework.tests.TestGroup;

import static org.junit.Assert.*;

/**
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Chris Beams
 */
public class MBeanClientInterceptorTests extends AbstractMBeanServerTests {

	protected static final String OBJECT_NAME = "spring:test=proxy";

	protected JmxTestBean target;

	protected boolean runTests = true;

	@Override
	public void onSetUp() throws Exception {
		target = new JmxTestBean();
		target.setAge(100);
		target.setName("Rob Harrop");

		MBeanExporter adapter = new MBeanExporter();
		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put(OBJECT_NAME, target);
		adapter.setServer(getServer());
		adapter.setBeans(beans);
		adapter.setAssembler(new ProxyTestAssembler());
		adapter.afterPropertiesSet();
	}

	protected MBeanServerConnection getServerConnection() throws Exception {
		return getServer();
	}

	protected IJmxTestBean getProxy() throws Exception {
		MBeanProxyFactoryBean factory = new MBeanProxyFactoryBean();
		factory.setServer(getServerConnection());
		factory.setProxyInterface(IJmxTestBean.class);
		factory.setObjectName(OBJECT_NAME);
		factory.afterPropertiesSet();
		return (IJmxTestBean) factory.getObject();
	}

	@Test
	public void testProxyClassIsDifferent() throws Exception {
		if (!runTests)
			return;
		IJmxTestBean proxy = getProxy();
		assertTrue("The proxy class should be different than the base class", (proxy.getClass() != IJmxTestBean.class));
	}

	@Test
	public void testDifferentProxiesSameClass() throws Exception {
		if (!runTests)
			return;
		IJmxTestBean proxy1 = getProxy();
		IJmxTestBean proxy2 = getProxy();

		assertNotSame("The proxies should NOT be the same", proxy1, proxy2);
		assertSame("The proxy classes should be the same", proxy1.getClass(), proxy2.getClass());
	}

	@Test
	public void testGetAttributeValue() throws Exception {
		if (!runTests)
			return;
		IJmxTestBean proxy1 = getProxy();
		int age = proxy1.getAge();
		assertEquals("The age should be 100", 100, age);
	}

	@Test
	public void testSetAttributeValue() throws Exception {
		if (!runTests)
			return;
		IJmxTestBean proxy = getProxy();
		proxy.setName("Rob Harrop");
		assertEquals("The name of the bean should have been updated", "Rob Harrop", target.getName());
	}

	@Test
	public void testSetAttributeValueWithRuntimeException() throws Exception {
		if (!runTests)
			return;
		IJmxTestBean proxy = getProxy();
		try {
			proxy.setName("Juergen");
			fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException ex) {
			// expected
		}
	}

	@Test
	public void testSetAttributeValueWithCheckedException() throws Exception {
		if (!runTests)
			return;
		IJmxTestBean proxy = getProxy();
		try {
			proxy.setName("Juergen Class");
			fail("Should have thrown ClassNotFoundException");
		} catch (ClassNotFoundException ex) {
			// expected
		}
	}

	@Test
	public void testSetAttributeValueWithIOException() throws Exception {
		if (!runTests)
			return;
		IJmxTestBean proxy = getProxy();
		try {
			proxy.setName("Juergen IO");
			fail("Should have thrown IOException");
		} catch (IOException ex) {
			// expected
		}
	}

	@Test
	public void testSetReadOnlyAttribute() throws Exception {
		if (!runTests)
			return;
		IJmxTestBean proxy = getProxy();
		try {
			proxy.setAge(900);
			fail("Should not be able to write to a read only attribute");
		} catch (InvalidInvocationException ex) {
			// success
		}
	}

	@Test
	public void testInvokeNoArgs() throws Exception {
		if (!runTests)
			return;
		IJmxTestBean proxy = getProxy();
		long result = proxy.myOperation();
		assertEquals("The operation should return 1", 1, result);
	}

	@Test
	public void testInvokeArgs() throws Exception {
		if (!runTests)
			return;
		IJmxTestBean proxy = getProxy();
		int result = proxy.add(1, 2);
		assertEquals("The operation should return 3", 3, result);
	}

	@Test
	public void testInvokeUnexposedMethodWithException() throws Exception {
		if (!runTests)
			return;
		IJmxTestBean bean = getProxy();
		try {
			bean.dontExposeMe();
			fail("Method dontExposeMe should throw an exception");
		} catch (InvalidInvocationException desired) {
			// success
		}
	}

	@Test
	public void testTestLazyConnectionToRemote() throws Exception {
		if (!runTests)
			return;

		Assume.group(TestGroup.JMXMP);

		JMXServiceURL url = new JMXServiceURL("service:jmx:jmxmp://localhost:9876");
		JMXConnectorServer connector = JMXConnectorServerFactory.newJMXConnectorServer(url, null, getServer());

		MBeanProxyFactoryBean factory = new MBeanProxyFactoryBean();
		factory.setServiceUrl(url.toString());
		factory.setProxyInterface(IJmxTestBean.class);
		factory.setObjectName(OBJECT_NAME);
		factory.setConnectOnStartup(false);
		factory.setRefreshOnConnectFailure(true);
		// should skip connection to the server
		factory.afterPropertiesSet();
		IJmxTestBean bean = (IJmxTestBean) factory.getObject();

		// now start the connector
		try {
			connector.start();
		} catch (BindException ex) {
			// couldn't bind to local port 9876 - let's skip the remainder of this test
			System.out.println("Skipping JMX LazyConnectionToRemote test because binding to local port 9876 failed: "
					+ ex.getMessage());
			return;
		}

		// should now be able to access data via the lazy proxy
		try {
			assertEquals("Rob Harrop", bean.getName());
			assertEquals(100, bean.getAge());
		} finally {
			connector.stop();
		}

		try {
			bean.getName();
		} catch (JmxException ex) {
			// expected
		}

		connector = JMXConnectorServerFactory.newJMXConnectorServer(url, null, getServer());
		connector.start();

		// should now be able to access data via the lazy proxy
		try {
			assertEquals("Rob Harrop", bean.getName());
			assertEquals(100, bean.getAge());
		} finally {
			connector.stop();
		}
	}

	/*
	public void testMXBeanAttributeAccess() throws Exception {
		MBeanClientInterceptor interceptor = new MBeanClientInterceptor();
		interceptor.setServer(ManagementFactory.getPlatformMBeanServer());
		interceptor.setObjectName("java.lang:type=Memory");
		interceptor.setManagementInterface(MemoryMXBean.class);
		MemoryMXBean proxy = ProxyFactory.getProxy(MemoryMXBean.class, interceptor);
		assertTrue(proxy.getHeapMemoryUsage().getMax() > 0);
	}

	public void testMXBeanOperationAccess() throws Exception {
		MBeanClientInterceptor interceptor = new MBeanClientInterceptor();
		interceptor.setServer(ManagementFactory.getPlatformMBeanServer());
		interceptor.setObjectName("java.lang:type=Threading");
		ThreadMXBean proxy = ProxyFactory.getProxy(ThreadMXBean.class, interceptor);
		assertTrue(proxy.getThreadInfo(Thread.currentThread().getId()).getStackTrace() != null);
	}

	public void testMXBeanAttributeListAccess() throws Exception {
		MBeanClientInterceptor interceptor = new MBeanClientInterceptor();
		interceptor.setServer(ManagementFactory.getPlatformMBeanServer());
		interceptor.setObjectName("com.sun.management:type=HotSpotDiagnostic");
		HotSpotDiagnosticMXBean proxy = ProxyFactory.getProxy(HotSpotDiagnosticMXBean.class, interceptor);
		assertFalse(proxy.getDiagnosticOptions().isEmpty());
	}
	*/

	private static class ProxyTestAssembler extends AbstractReflectiveMBeanInfoAssembler {

		@Override
		protected boolean includeReadAttribute(Method method, String beanKey) {
			return true;
		}

		@Override
		protected boolean includeWriteAttribute(Method method, String beanKey) {
			if ("setAge".equals(method.getName())) {
				return false;
			}
			return true;
		}

		@Override
		protected boolean includeOperation(Method method, String beanKey) {
			if ("dontExposeMe".equals(method.getName())) {
				return false;
			}
			return true;
		}

		@SuppressWarnings("unused")
		protected String getOperationDescription(Method method) {
			return method.getName();
		}

		@SuppressWarnings("unused")
		protected String getAttributeDescription(PropertyDescriptor propertyDescriptor) {
			return propertyDescriptor.getDisplayName();
		}

		@SuppressWarnings("unused")
		protected void populateAttributeDescriptor(Descriptor descriptor, Method getter, Method setter) {

		}

		@SuppressWarnings("unused")
		protected void populateOperationDescriptor(Descriptor descriptor, Method method) {

		}

		@SuppressWarnings({ "unused", "rawtypes" })
		protected String getDescription(String beanKey, Class beanClass) {
			return "";
		}

		@SuppressWarnings({ "unused", "rawtypes" })
		protected void populateMBeanDescriptor(Descriptor mbeanDescriptor, String beanKey, Class beanClass) {

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/49.java