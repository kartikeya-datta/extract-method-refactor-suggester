error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14701.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14701.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 679
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14701.java
text:
```scala
public final class CustomizableTraceInterceptorTests {

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

p@@ackage org.springframework.aop.interceptor;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.junit.Test;

/**
 * @author Rob Harrop
 * @author Rick Evans
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public class CustomizableTraceInterceptorTests {

	@Test(expected=IllegalArgumentException.class)
	public void testSetEmptyEnterMessage() {
		// Must not be able to set empty enter message
		new CustomizableTraceInterceptor().setEnterMessage("");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSetEnterMessageWithReturnValuePlaceholder() {
		// Must not be able to set enter message with return value placeholder
		new CustomizableTraceInterceptor().setEnterMessage(CustomizableTraceInterceptor.PLACEHOLDER_RETURN_VALUE);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSetEnterMessageWithExceptionPlaceholder() {
		// Must not be able to set enter message with exception placeholder
		new CustomizableTraceInterceptor().setEnterMessage(CustomizableTraceInterceptor.PLACEHOLDER_EXCEPTION);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSetEnterMessageWithInvocationTimePlaceholder() {
		// Must not be able to set enter message with invocation time placeholder
		new CustomizableTraceInterceptor().setEnterMessage(CustomizableTraceInterceptor.PLACEHOLDER_INVOCATION_TIME);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSetEmptyExitMessage() {
		// Must not be able to set empty exit message
		new CustomizableTraceInterceptor().setExitMessage("");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSetExitMessageWithExceptionPlaceholder() {
		// Must not be able to set exit message with exception placeholder
		new CustomizableTraceInterceptor().setExitMessage(CustomizableTraceInterceptor.PLACEHOLDER_EXCEPTION);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSetEmptyExceptionMessage() {
		// Must not be able to set empty exception message
		new CustomizableTraceInterceptor().setExceptionMessage("");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSetExceptionMethodWithReturnValuePlaceholder() {
		// Must not be able to set exception message with return value placeholder
		new CustomizableTraceInterceptor().setExceptionMessage(CustomizableTraceInterceptor.PLACEHOLDER_RETURN_VALUE);
	}

	@Test
	public void testSunnyDayPathLogsCorrectly() throws Throwable {
		Log log = createMock(Log.class);

		MethodInvocation methodInvocation = createMock(MethodInvocation.class);

		Method toString = String.class.getMethod("toString", new Class[]{});

		expect(log.isTraceEnabled()).andReturn(true);
		expect(methodInvocation.getMethod()).andReturn(toString).times(4);
		expect(methodInvocation.getThis()).andReturn(this).times(2);
		log.trace(isA(String.class));
		expect(methodInvocation.proceed()).andReturn(null);
		log.trace(isA(String.class));

		replay(methodInvocation);
		replay(log);

		CustomizableTraceInterceptor interceptor = new StubCustomizableTraceInterceptor(log);
		interceptor.invoke(methodInvocation);

		verify(log);
		verify(methodInvocation);
	}

	@Test
	public void testExceptionPathLogsCorrectly() throws Throwable {
		Log log = createMock(Log.class);

		MethodInvocation methodInvocation = createMock(MethodInvocation.class);

		Method toString = String.class.getMethod("toString", new Class[]{});

		expect(log.isTraceEnabled()).andReturn(true);
		expect(methodInvocation.getMethod()).andReturn(toString).times(4);
		expect(methodInvocation.getThis()).andReturn(this).times(2);
		log.trace(isA(String.class));
		IllegalArgumentException exception = new IllegalArgumentException();
		expect(methodInvocation.proceed()).andThrow(exception);
		log.trace(isA(String.class), eq(exception));

		replay(log);
		replay(methodInvocation);

		CustomizableTraceInterceptor interceptor = new StubCustomizableTraceInterceptor(log);
		try {
			interceptor.invoke(methodInvocation);
			fail("Must have propagated the IllegalArgumentException.");
		}
		catch (IllegalArgumentException expected) {
		}

		verify(log);
		verify(methodInvocation);
	}

	@Test
	public void testSunnyDayPathLogsCorrectlyWithPrettyMuchAllPlaceholdersMatching() throws Throwable {
		Log log = createMock(Log.class);

		MethodInvocation methodInvocation = createMock(MethodInvocation.class);

		Method toString = String.class.getMethod("toString", new Class[0]);
		Object[] arguments = new Object[]{"$ One \\$", new Long(2)};

		expect(log.isTraceEnabled()).andReturn(true);
		expect(methodInvocation.getMethod()).andReturn(toString).times(7);
		expect(methodInvocation.getThis()).andReturn(this).times(2);
		expect(methodInvocation.getArguments()).andReturn(arguments).times(2);
		log.trace(isA(String.class));
		expect(methodInvocation.proceed()).andReturn("Hello!");
		log.trace(isA(String.class));

		replay(methodInvocation);
		replay(log);

		CustomizableTraceInterceptor interceptor = new StubCustomizableTraceInterceptor(log);
		interceptor.setEnterMessage(new StringBuffer()
			.append("Entering the '").append(CustomizableTraceInterceptor.PLACEHOLDER_METHOD_NAME)
			.append("' method of the [").append(CustomizableTraceInterceptor.PLACEHOLDER_TARGET_CLASS_NAME)
			.append("] class with the following args (").append(CustomizableTraceInterceptor.PLACEHOLDER_ARGUMENTS)
			.append(") and arg types (").append(CustomizableTraceInterceptor.PLACEHOLDER_ARGUMENT_TYPES)
			.append(").").toString());
		interceptor.setExitMessage(new StringBuffer()
			.append("Exiting the '").append(CustomizableTraceInterceptor.PLACEHOLDER_METHOD_NAME)
			.append("' method of the [").append(CustomizableTraceInterceptor.PLACEHOLDER_TARGET_CLASS_SHORT_NAME)
			.append("] class with the following args (").append(CustomizableTraceInterceptor.PLACEHOLDER_ARGUMENTS)
			.append(") and arg types (").append(CustomizableTraceInterceptor.PLACEHOLDER_ARGUMENT_TYPES)
			.append("), returning '").append(CustomizableTraceInterceptor.PLACEHOLDER_RETURN_VALUE)
			.append("' and taking '").append(CustomizableTraceInterceptor.PLACEHOLDER_INVOCATION_TIME)
			.append("' this long.").toString());
		interceptor.invoke(methodInvocation);

		verify(log);
		verify(methodInvocation);
	}


	@SuppressWarnings("serial")
	private static class StubCustomizableTraceInterceptor extends CustomizableTraceInterceptor {

		private final Log log;

		public StubCustomizableTraceInterceptor(Log log) {
			super.setUseDynamicLogger(false);
			this.log = log;
		}

		protected Log getLoggerForInvocation(MethodInvocation invocation) {
			return this.log;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14701.java