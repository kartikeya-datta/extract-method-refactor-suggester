error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13624.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13624.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13624.java
text:
```scala
P@@roxyFactory pf = new ProxyFactory(new Class<?>[] {ifc});

/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.ejb.access;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.rmi.ConnectException;
import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.naming.Context;
import javax.naming.NamingException;

import org.junit.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.jndi.JndiTemplate;
import org.springframework.remoting.RemoteAccessException;

/**
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public class SimpleRemoteSlsbInvokerInterceptorTests {

	private Context mockContext(
			String jndiName, RemoteInterface ejbInstance, int createCount, int lookupCount, int closeCount)
			throws Exception {

		final SlsbHome mockHome = createMock(SlsbHome.class);
		expect(mockHome.create()).andReturn(ejbInstance).times(createCount);
		replay(mockHome);

		final Context mockCtx = createMock(Context.class);

		expect(mockCtx.lookup("java:comp/env/" + jndiName)).andReturn(mockHome).times(lookupCount);
		mockCtx.close();
		expectLastCall().times(closeCount);
		replay(mockCtx);

		return mockCtx;
	}

	private SimpleRemoteSlsbInvokerInterceptor configuredInterceptor(
			final Context mockCtx, String jndiName) throws Exception {

		SimpleRemoteSlsbInvokerInterceptor si = createInterceptor();
		si.setJndiTemplate(new JndiTemplate() {
			protected Context createInitialContext() {
				return mockCtx;
			}
		});
		si.setResourceRef(true);
		si.setJndiName(jndiName);

		return si;
	}

	protected SimpleRemoteSlsbInvokerInterceptor createInterceptor() {
		return new SimpleRemoteSlsbInvokerInterceptor();
	}

	protected Object configuredProxy(SimpleRemoteSlsbInvokerInterceptor si, Class<?> ifc) throws NamingException {
		si.afterPropertiesSet();
		ProxyFactory pf = new ProxyFactory(new Class[] {ifc});
		pf.addAdvice(si);
		return pf.getProxy();
	}


	@Test
	public void testPerformsLookup() throws Exception {
		RemoteInterface ejb = createMock(RemoteInterface.class);
		replay(ejb);

		String jndiName= "foobar";
		Context mockContext = mockContext(jndiName, ejb, 1, 1, 1);

		SimpleRemoteSlsbInvokerInterceptor si = configuredInterceptor(mockContext, jndiName);
		configuredProxy(si, RemoteInterface.class);

		verify(mockContext);
	}

	@Test
	public void testPerformsLookupWithAccessContext() throws Exception {
		RemoteInterface ejb = createMock(RemoteInterface.class);
		expect(ejb.targetMethod()).andReturn(null);
		replay(ejb);

		String jndiName= "foobar";
		Context mockContext = mockContext(jndiName, ejb, 1, 1, 2);

		SimpleRemoteSlsbInvokerInterceptor si = configuredInterceptor(mockContext, jndiName);
		si.setExposeAccessContext(true);
		RemoteInterface target = (RemoteInterface) configuredProxy(si, RemoteInterface.class);
		assertNull(target.targetMethod());

		verify(mockContext);
	}

	@Test
	public void testLookupFailure() throws Exception {
		final NamingException nex = new NamingException();
		final String jndiName = "foobar";
		JndiTemplate jt = new JndiTemplate() {
			public Object lookup(String name) throws NamingException {
				assertTrue(jndiName.equals(name));
				throw nex;
			}
		};

		SimpleRemoteSlsbInvokerInterceptor si = new SimpleRemoteSlsbInvokerInterceptor();
		si.setJndiName("foobar");
		// default resourceRef=false should cause this to fail, as java:/comp/env will not
		// automatically be added
		si.setJndiTemplate(jt);
		try {
			si.afterPropertiesSet();
			fail("Should have failed with naming exception");
		}
		catch (NamingException ex) {
			assertTrue(ex == nex);
		}
	}

	@Test
	public void testInvokesMethodOnEjbInstance() throws Exception {
		doTestInvokesMethodOnEjbInstance(true, true);
	}

	@Test
	public void testInvokesMethodOnEjbInstanceWithLazyLookup() throws Exception {
		doTestInvokesMethodOnEjbInstance(false, true);
	}

	@Test
	public void testInvokesMethodOnEjbInstanceWithLazyLookupAndNoCache() throws Exception {
		doTestInvokesMethodOnEjbInstance(false, false);
	}

	@Test
	public void testInvokesMethodOnEjbInstanceWithNoCache() throws Exception {
		doTestInvokesMethodOnEjbInstance(true, false);
	}

	private void doTestInvokesMethodOnEjbInstance(boolean lookupHomeOnStartup, boolean cacheHome) throws Exception {
		Object retVal = new Object();
		final RemoteInterface ejb = createMock(RemoteInterface.class);
		expect(ejb.targetMethod()).andReturn(retVal).times(2);
		ejb.remove();
		replay(ejb);

		int lookupCount = 1;
		if (!cacheHome) {
			lookupCount++;
			if (lookupHomeOnStartup) {
				lookupCount++;
			}
		}

		final String jndiName= "foobar";
		Context mockContext = mockContext(jndiName, ejb, 2, lookupCount, lookupCount);

		SimpleRemoteSlsbInvokerInterceptor si = configuredInterceptor(mockContext, jndiName);
		si.setLookupHomeOnStartup(lookupHomeOnStartup);
		si.setCacheHome(cacheHome);

		RemoteInterface target = (RemoteInterface) configuredProxy(si, RemoteInterface.class);
		assertTrue(target.targetMethod() == retVal);
		assertTrue(target.targetMethod() == retVal);

		verify(mockContext, ejb);
	}

	@Test
	public void testInvokesMethodOnEjbInstanceWithHomeInterface() throws Exception {
		Object retVal = new Object();
		final RemoteInterface ejb = createMock(RemoteInterface.class);
		expect(ejb.targetMethod()).andReturn(retVal);
		ejb.remove();
		replay(ejb);

		final String jndiName= "foobar";
		Context mockContext = mockContext(jndiName, ejb, 1, 1, 1);

		SimpleRemoteSlsbInvokerInterceptor si = configuredInterceptor(mockContext, jndiName);
		si.setHomeInterface(SlsbHome.class);

		RemoteInterface target = (RemoteInterface) configuredProxy(si, RemoteInterface.class);
		assertTrue(target.targetMethod() == retVal);

		verify(mockContext, ejb);
	}

	@Test
	public void testInvokesMethodOnEjbInstanceWithRemoteException() throws Exception {
		final RemoteInterface ejb = createMock(RemoteInterface.class);
		expect(ejb.targetMethod()).andThrow(new RemoteException());
		ejb.remove();
		replay(ejb);

		final String jndiName= "foobar";
		Context mockContext = mockContext(jndiName, ejb, 1, 1, 1);

		SimpleRemoteSlsbInvokerInterceptor si = configuredInterceptor(mockContext, jndiName);

		RemoteInterface target = (RemoteInterface) configuredProxy(si, RemoteInterface.class);
		try {
			target.targetMethod();
			fail("Should have thrown RemoteException");
		}
		catch (RemoteException ex) {
			// expected
		}

		verify(mockContext, ejb);
	}

	@Test
	public void testInvokesMethodOnEjbInstanceWithConnectExceptionWithRefresh() throws Exception {
		doTestInvokesMethodOnEjbInstanceWithConnectExceptionWithRefresh(true, true);
	}

	@Test
	public void testInvokesMethodOnEjbInstanceWithConnectExceptionWithRefreshAndLazyLookup() throws Exception {
		doTestInvokesMethodOnEjbInstanceWithConnectExceptionWithRefresh(false, true);
	}

	@Test
	public void testInvokesMethodOnEjbInstanceWithConnectExceptionWithRefreshAndLazyLookupAndNoCache() throws Exception {
		doTestInvokesMethodOnEjbInstanceWithConnectExceptionWithRefresh(false, false);
	}

	@Test
	public void testInvokesMethodOnEjbInstanceWithConnectExceptionWithRefreshAndNoCache() throws Exception {
		doTestInvokesMethodOnEjbInstanceWithConnectExceptionWithRefresh(true, false);
	}

	private void doTestInvokesMethodOnEjbInstanceWithConnectExceptionWithRefresh(
			boolean lookupHomeOnStartup, boolean cacheHome) throws Exception {

		final RemoteInterface ejb = createMock(RemoteInterface.class);
		expect(ejb.targetMethod()).andThrow(new ConnectException("")).times(2);
		ejb.remove();
		expectLastCall().times(2);
		replay(ejb);

		int lookupCount = 2;
		if (!cacheHome) {
			lookupCount++;
			if (lookupHomeOnStartup) {
				lookupCount++;
			}
		}

		final String jndiName= "foobar";
		Context mockContext = mockContext(jndiName, ejb, 2, lookupCount, lookupCount);

		SimpleRemoteSlsbInvokerInterceptor si = configuredInterceptor(mockContext, jndiName);
		si.setRefreshHomeOnConnectFailure(true);
		si.setLookupHomeOnStartup(lookupHomeOnStartup);
		si.setCacheHome(cacheHome);

		RemoteInterface target = (RemoteInterface) configuredProxy(si, RemoteInterface.class);
		try {
			target.targetMethod();
			fail("Should have thrown RemoteException");
		}
		catch (ConnectException ex) {
			// expected
		}

		verify(mockContext, ejb);
	}

	@Test
	public void testInvokesMethodOnEjbInstanceWithBusinessInterface() throws Exception {
		Object retVal = new Object();
		final RemoteInterface ejb = createMock(RemoteInterface.class);
		expect(ejb.targetMethod()).andReturn(retVal);
		ejb.remove();
		replay(ejb);

		final String jndiName= "foobar";
		Context mockContext = mockContext(jndiName, ejb, 1, 1, 1);

		SimpleRemoteSlsbInvokerInterceptor si = configuredInterceptor(mockContext, jndiName);

		BusinessInterface target = (BusinessInterface) configuredProxy(si, BusinessInterface.class);
		assertTrue(target.targetMethod() == retVal);

		verify(mockContext, ejb);
	}

	@Test
	public void testInvokesMethodOnEjbInstanceWithBusinessInterfaceWithRemoteException() throws Exception {
		final RemoteInterface ejb = createMock(RemoteInterface.class);
		expect(ejb.targetMethod()).andThrow(new RemoteException());
		ejb.remove();
		replay(ejb);

		final String jndiName= "foobar";
		Context mockContext = mockContext(jndiName, ejb, 1, 1, 1);

		SimpleRemoteSlsbInvokerInterceptor si = configuredInterceptor(mockContext, jndiName);

		BusinessInterface target = (BusinessInterface) configuredProxy(si, BusinessInterface.class);
		try {
			target.targetMethod();
			fail("Should have thrown RemoteAccessException");
		}
		catch (RemoteAccessException ex) {
			// expected
		}

		verify(mockContext, ejb);
	}

	@Test
	public void testApplicationException() throws Exception {
		doTestException(new ApplicationException());
	}

	@Test
	public void testRemoteException() throws Exception {
		doTestException(new RemoteException());
	}

	private void doTestException(Exception expected) throws Exception {
		final RemoteInterface ejb = createMock(RemoteInterface.class);
		expect(ejb.targetMethod()).andThrow(expected);
		ejb.remove();
		replay(ejb);

		final String jndiName= "foobar";
		Context mockContext = mockContext(jndiName, ejb, 1, 1, 1);

		SimpleRemoteSlsbInvokerInterceptor si = configuredInterceptor(mockContext, jndiName);

		RemoteInterface target = (RemoteInterface) configuredProxy(si, RemoteInterface.class);
		try {
			target.targetMethod();
			fail("Should have thrown remote exception");
		}
		catch (Exception thrown) {
			assertTrue(thrown == expected);
		}

		verify(mockContext, ejb);
	}


	/**
	 * Needed so that we can mock create() method.
	 */
	protected interface SlsbHome extends EJBHome {

		EJBObject create() throws RemoteException, CreateException;
	}


	protected interface RemoteInterface extends EJBObject {

		// Also business exception!?
		Object targetMethod() throws RemoteException, ApplicationException;
	}


	protected interface BusinessInterface {

		Object targetMethod() throws ApplicationException;
	}


	@SuppressWarnings("serial")
	protected class ApplicationException extends Exception {

		public ApplicationException() {
			super("appException");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13624.java