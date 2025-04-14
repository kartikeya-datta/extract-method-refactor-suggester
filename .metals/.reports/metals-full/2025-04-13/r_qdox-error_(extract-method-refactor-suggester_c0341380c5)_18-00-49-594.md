error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11217.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11217.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 668
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11217.java
text:
```scala
public static abstract class BaseTestCase {

/*
 * Copyright 2002-2009 the original author or authors.
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

p@@ackage org.springframework.test.context.junit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;

/**
 * <p>
 * JUnit 4 based unit test for verifying that '<i>before</i>' and '<i>after</i>'
 * methods of {@link TestExecutionListener TestExecutionListeners} as well as
 * {@link BeforeTransaction &#064;BeforeTransaction} and
 * {@link AfterTransaction &#064;AfterTransaction} methods can fail a test in a
 * JUnit 4.4 environment, as requested in <a
 * href="http://opensource.atlassian.com/projects/spring/browse/SPR-3960"
 * target="_blank">SPR-3960</a>.
 * </p>
 * <p>
 * Indirectly, this class also verifies that all {@link TestExecutionListener}
 * lifecycle callbacks are called.
 * </p>
 * <p>
 * As of Spring 3.0, this class also tests support for the new
 * {@link TestExecutionListener#beforeTestClass(TestContext) beforeTestClass()}
 * and {@link TestExecutionListener#afterTestClass(TestContext)
 * afterTestClass()} lifecycle callback methods.
 * </p>
 * 
 * @author Sam Brannen
 * @since 2.5
 */
@RunWith(Parameterized.class)
public class FailingBeforeAndAfterMethodsTests {

	protected final Class<?> clazz;


	public FailingBeforeAndAfterMethodsTests(final Class<?> clazz) {
		this.clazz = clazz;
	}

	@Parameters
	public static Collection<Object[]> testData() {
		return Arrays.asList(new Object[][] {//
		//
			{ AlwaysFailingBeforeTestClassTestCase.class },//
			{ AlwaysFailingAfterTestClassTestCase.class },//
			{ AlwaysFailingPrepareTestInstanceTestCase.class },//
			{ AlwaysFailingBeforeTestMethodTestCase.class },//
			{ AlwaysFailingAfterTestMethodTestCase.class },//
			{ FailingBeforeTransactionTestCase.class },//
			{ FailingAfterTransactionTestCase.class } //
		});
	}

	@Test
	public void runTestAndAssertCounters() throws Exception {
		final TrackingRunListener listener = new TrackingRunListener();
		final RunNotifier notifier = new RunNotifier();
		notifier.addListener(listener);

		new SpringJUnit4ClassRunner(this.clazz).run(notifier);
		assertEquals("Verifying number of failures for test class [" + this.clazz + "].", 1,
			listener.getTestFailureCount());
	}


	// -------------------------------------------------------------------

	static class AlwaysFailingBeforeTestClassTestExecutionListener extends AbstractTestExecutionListener {

		@Override
		public void beforeTestClass(TestContext testContext) {
			fail("always failing beforeTestClass()");
		}
	}

	static class AlwaysFailingAfterTestClassTestExecutionListener extends AbstractTestExecutionListener {

		@Override
		public void afterTestClass(TestContext testContext) {
			fail("always failing afterTestClass()");
		}
	}

	static class AlwaysFailingPrepareTestInstanceTestExecutionListener extends AbstractTestExecutionListener {

		@Override
		public void prepareTestInstance(TestContext testContext) throws Exception {
			fail("always failing prepareTestInstance()");
		}
	}

	static class AlwaysFailingBeforeTestMethodTestExecutionListener extends AbstractTestExecutionListener {

		@Override
		public void beforeTestMethod(TestContext testContext) {
			fail("always failing beforeTestMethod()");
		}
	}

	static class AlwaysFailingAfterTestMethodTestExecutionListener extends AbstractTestExecutionListener {

		@Override
		public void afterTestMethod(TestContext testContext) {
			fail("always failing afterTestMethod()");
		}
	}

	@RunWith(SpringJUnit4ClassRunner.class)
	@TestExecutionListeners( {})
	public static class BaseTestCase {

		@Test
		public void testNothing() {
		}
	}

	@TestExecutionListeners(AlwaysFailingBeforeTestClassTestExecutionListener.class)
	public static class AlwaysFailingBeforeTestClassTestCase extends BaseTestCase {
	}

	@TestExecutionListeners(AlwaysFailingAfterTestClassTestExecutionListener.class)
	public static class AlwaysFailingAfterTestClassTestCase extends BaseTestCase {
	}

	@TestExecutionListeners(AlwaysFailingPrepareTestInstanceTestExecutionListener.class)
	public static class AlwaysFailingPrepareTestInstanceTestCase extends BaseTestCase {
	}

	@TestExecutionListeners(AlwaysFailingBeforeTestMethodTestExecutionListener.class)
	public static class AlwaysFailingBeforeTestMethodTestCase extends BaseTestCase {
	}

	@TestExecutionListeners(AlwaysFailingAfterTestMethodTestExecutionListener.class)
	public static class AlwaysFailingAfterTestMethodTestCase extends BaseTestCase {
	}

	@ContextConfiguration("FailingBeforeAndAfterMethodsTests-context.xml")
	public static class FailingBeforeTransactionTestCase extends AbstractTransactionalJUnit4SpringContextTests {

		@Test
		public void testNothing() {
		}

		@BeforeTransaction
		public void beforeTransaction() {
			fail("always failing beforeTransaction()");
		}
	}

	@ContextConfiguration("FailingBeforeAndAfterMethodsTests-context.xml")
	public static class FailingAfterTransactionTestCase extends AbstractTransactionalJUnit4SpringContextTests {

		@Test
		public void testNothing() {
		}

		@AfterTransaction
		public void afterTransaction() {
			fail("always failing afterTransaction()");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11217.java