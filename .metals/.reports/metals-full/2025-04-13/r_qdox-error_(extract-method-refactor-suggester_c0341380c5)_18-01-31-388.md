error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3517.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3517.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 709
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3517.java
text:
```scala
private static class MyThrowsHandler extends MethodCounter implements ThrowsAdvice {

/*
 * Copyright 2002-2013 the original author or authors.
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

p@@ackage org.springframework.aop.framework.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;

import javax.transaction.TransactionRolledbackException;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.tests.aop.advice.MethodCounter;


/**
 * @author Rod Johnson
 * @author Chris Beams
 */
public final class ThrowsAdviceInterceptorTests {

	@Test(expected=IllegalArgumentException.class)
	public void testNoHandlerMethods() {
		// should require one handler method at least
		new ThrowsAdviceInterceptor(new Object());
	}

	@Test
	public void testNotInvoked() throws Throwable {
		MyThrowsHandler th = new MyThrowsHandler();
		ThrowsAdviceInterceptor ti = new ThrowsAdviceInterceptor(th);
		Object ret = new Object();
		MethodInvocation mi = mock(MethodInvocation.class);
		given(mi.proceed()).willReturn(ret);
		assertEquals(ret, ti.invoke(mi));
		assertEquals(0, th.getCalls());
	}

	@Test
	public void testNoHandlerMethodForThrowable() throws Throwable {
		MyThrowsHandler th = new MyThrowsHandler();
		ThrowsAdviceInterceptor ti = new ThrowsAdviceInterceptor(th);
		assertEquals(2, ti.getHandlerMethodCount());
		Exception ex = new Exception();
		MethodInvocation mi = mock(MethodInvocation.class);
		given(mi.proceed()).willThrow(ex);
		try {
			ti.invoke(mi);
			fail();
		}
		catch (Exception caught) {
			assertEquals(ex, caught);
		}
		assertEquals(0, th.getCalls());
	}

	@Test
	public void testCorrectHandlerUsed() throws Throwable {
		MyThrowsHandler th = new MyThrowsHandler();
		ThrowsAdviceInterceptor ti = new ThrowsAdviceInterceptor(th);
		FileNotFoundException ex = new FileNotFoundException();
		MethodInvocation mi = mock(MethodInvocation.class);
		given(mi.getMethod()).willReturn(Object.class.getMethod("hashCode", (Class[]) null));
		given(mi.getThis()).willReturn(new Object());
		given(mi.proceed()).willThrow(ex);
		try {
			ti.invoke(mi);
			fail();
		}
		catch (Exception caught) {
			assertEquals(ex, caught);
		}
		assertEquals(1, th.getCalls());
		assertEquals(1, th.getCalls("ioException"));
	}

	@Test
	public void testCorrectHandlerUsedForSubclass() throws Throwable {
		MyThrowsHandler th = new MyThrowsHandler();
		ThrowsAdviceInterceptor ti = new ThrowsAdviceInterceptor(th);
		// Extends RemoteException
		TransactionRolledbackException ex = new TransactionRolledbackException();
		MethodInvocation mi = mock(MethodInvocation.class);
		given(mi.proceed()).willThrow(ex);
		try {
			ti.invoke(mi);
			fail();
		}
		catch (Exception caught) {
			assertEquals(ex, caught);
		}
		assertEquals(1, th.getCalls());
		assertEquals(1, th.getCalls("remoteException"));
	}

	@Test
	public void testHandlerMethodThrowsException() throws Throwable {
		final Throwable t = new Throwable();

		@SuppressWarnings("serial")
		MyThrowsHandler th = new MyThrowsHandler() {
			@Override
			public void afterThrowing(RemoteException ex) throws Throwable {
				super.afterThrowing(ex);
				throw t;
			}
		};

		ThrowsAdviceInterceptor ti = new ThrowsAdviceInterceptor(th);
		// Extends RemoteException
		TransactionRolledbackException ex = new TransactionRolledbackException();
		MethodInvocation mi = mock(MethodInvocation.class);
		given(mi.proceed()).willThrow(ex);
		try {
			ti.invoke(mi);
			fail();
		}
		catch (Throwable caught) {
			assertEquals(t, caught);
		}
		assertEquals(1, th.getCalls());
		assertEquals(1, th.getCalls("remoteException"));
	}

	@SuppressWarnings("serial")
	static class MyThrowsHandler extends MethodCounter implements ThrowsAdvice {
		// Full method signature
		public void afterThrowing(Method m, Object[] args, Object target, IOException ex) {
			count("ioException");
		}
		public void afterThrowing(RemoteException ex) throws Throwable {
			count("remoteException");
		}

		/** Not valid, wrong number of arguments */
		public void afterThrowing(Method m, Exception ex) throws Throwable {
			throw new UnsupportedOperationException("Shouldn't be called");
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3517.java