error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2662.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2662.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2662.java
text:
```scala
a@@ssertNull(Description.TEST_MECHANISM.getMethodName());

package org.junit.tests.experimental.max;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.containsString;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.max.CouldNotReadCoreException;
import org.junit.experimental.max.MaxCore;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.runner.Computer;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.tests.AllTests;

public class MaxStarterTest {
	private MaxCore fMax;

	@Before
	public void createMax() {
		fMax= MaxCore.createFresh();
	}

	@After
	public void forgetMax() {
		fMax.forget();
	}

	public static class TwoTests {
		@Test
		public void succeed() {
		}

		@Test
		public void dontSucceed() {
			fail();
		}
	}

	@Test
	public void twoTestsNotRunComeBackInRandomOrder() {
		Request request= Request.aClass(TwoTests.class);
		List<Description> things= fMax.sortedLeavesForTest(request);
		Description succeed= Description.createTestDescription(TwoTests.class,
				"succeed");
		Description dontSucceed= Description.createTestDescription(
				TwoTests.class, "dontSucceed");
		assertTrue(things.contains(succeed));
		assertTrue(things.contains(dontSucceed));
		assertEquals(2, things.size());
	}

	@Test
	public void preferNewTests() {
		Request one= Request.method(TwoTests.class, "succeed");
		fMax.run(one);
		Request two= Request.aClass(TwoTests.class);
		List<Description> things= fMax.sortedLeavesForTest(two);
		Description dontSucceed= Description.createTestDescription(
				TwoTests.class, "dontSucceed");
		assertEquals(dontSucceed, things.get(0));
		assertEquals(2, things.size());
	}

	// This covers a seemingly-unlikely case, where you had a test that failed
	// on the
	// last run and you also introduced new tests. In such a case it pretty much
	// doesn't matter
	// which order they run, you just want them both to be early in the sequence
	@Test
	public void preferNewTestsOverTestsThatFailed() {
		Request one= Request.method(TwoTests.class, "dontSucceed");
		fMax.run(one);
		Request two= Request.aClass(TwoTests.class);
		List<Description> things= fMax.sortedLeavesForTest(two);
		Description succeed= Description.createTestDescription(TwoTests.class,
				"succeed");
		assertEquals(succeed, things.get(0));
		assertEquals(2, things.size());
	}

	@Test
	public void preferRecentlyFailed() {
		Request request= Request.aClass(TwoTests.class);
		fMax.run(request);
		List<Description> tests= fMax.sortedLeavesForTest(request);
		Description dontSucceed= Description.createTestDescription(
				TwoTests.class, "dontSucceed");
		assertEquals(dontSucceed, tests.get(0));
	}

	@Test
	public void sortTestsInMultipleClasses() {
		Request request= Request.classes(Computer.serial(), TwoTests.class,
				TwoTests.class);
		fMax.run(request);
		List<Description> tests= fMax.sortedLeavesForTest(request);
		Description dontSucceed= Description.createTestDescription(
				TwoTests.class, "dontSucceed");
		assertEquals(dontSucceed, tests.get(0));
		assertEquals(dontSucceed, tests.get(1));
	}

	public static class TwoUnEqualTests {
		@Test
		public void slow() throws InterruptedException {
			Thread.sleep(100);
		}

		@Test
		public void fast() throws InterruptedException {
			Thread.sleep(50);
		}
	}

	@Test
	public void preferFast() {
		Request request= Request.aClass(TwoUnEqualTests.class);
		fMax.run(request);
		Description thing= fMax.sortedLeavesForTest(request).get(1);
		assertEquals(Description.createTestDescription(TwoUnEqualTests.class,
				"slow"), thing);
		// TODO (Nov 18, 2008 2:03:06 PM): flaky?
	}

	@Test
	public void remember() throws CouldNotReadCoreException {
		Request request= Request.aClass(TwoUnEqualTests.class);
		fMax.run(request);
		MaxCore reincarnation= MaxCore.forFolder(fMax.getFolder());
		try {
			Description thing= reincarnation.sortedLeavesForTest(request)
					.get(1);
			assertEquals(Description.createTestDescription(
					TwoUnEqualTests.class, "slow"), thing);
		} finally {
			reincarnation.forget();
		}
	}

	@Test
	public void listenersAreCalledCorrectlyInTheFaceOfFailures()
			throws Exception {
		JUnitCore core= new JUnitCore();
		final List<Failure> failures= new ArrayList<Failure>();
		core.addListener(new RunListener() {
			@Override
			public void testRunFinished(Result result) throws Exception {
				failures.addAll(result.getFailures());
			}
		});
		fMax.run(Request.aClass(TwoTests.class), core);
		assertEquals(1, failures.size());
	}

	@Test
	public void testsAreOnlyIncludedOnceWhenExpandingForSorting()
			throws Exception {
		Result result= fMax.run(Request.aClass(TwoTests.class));
		assertEquals(2, result.getRunCount());
	}

	public static class TwoOldTests extends TestCase {
		public void testOne() {
		}

		public void testTwo() {
		}
	}

	@Test
	public void junit3TestsAreRunOnce() throws Exception {
		Result result= fMax.run(Request.aClass(TwoOldTests.class),
				new JUnitCore());
		assertEquals(2, result.getRunCount());
	}
	
	@Test public void saffSqueezeExample() throws Exception {
		final Description method= Description.createTestDescription(TwoOldTests.class, "testOne");
		Filter filter= Filter.matchDescription(method);
		JUnit38ClassRunner child= new JUnit38ClassRunner(TwoOldTests.class);
		child.filter(filter);
		assertEquals(1, child.testCount());
	}

	@Test
	public void testCountsMatchUp() {
		JUnitCore core= new JUnitCore();
		Request filtered= Request.aClass(AllTests.class).filterWith(
				new Filter() {
					@Override
					public boolean shouldRun(Description description) {
						return !description.toString().contains("Max");
					}

					@Override
					public String describe() {
						return "Avoid infinite recursion";
					}
				});
		int maxCount= fMax.run(filtered, core).getRunCount();
		int coreCount= core.run(filtered).getRunCount();
		assertEquals(coreCount, maxCount);
	}

	@Test
	public void testCountsStandUpToFiltration() {
		// TODO (Nov 18, 2008 4:42:43 PM): DUP above
		Class<AllTests> testClass= AllTests.class;
		assertFilterLeavesTestUnscathed(testClass);
	}

	private void assertFilterLeavesTestUnscathed(Class<?> testClass) {
		Request oneClass= Request.aClass(testClass);
		Request filtered= oneClass.filterWith(new Filter() {
			@Override
			public boolean shouldRun(Description description) {
				return true;
			}

			@Override
			public String describe() {
				return "Everything";
			}
		});

		int filterCount= filtered.getRunner().testCount();
		int coreCount= oneClass.getRunner().testCount();
		assertEquals("Counts match up in " + testClass, coreCount, filterCount);
	}

	// TODO (Nov 18, 2008 2:00:18 PM): move these, which have nothing in
	// particular to do with Max
	@Test
	public void parseClass_whenCantParse() {
		assertNull(Description.TEST_MECHANISM.parseClass());
	}

	@Test
	public void parseMethod_whenCantParse() {
		assertNull(Description.TEST_MECHANISM.parseMethod());
	}

	@Test
	public void buildRunner_whenCantParse() {
		try {
			Description.TEST_MECHANISM.buildRunner();
			// this need not throw an exception in the future, but it does now.
			// as long as it throws an exception, it should have a descriptive
			// toString
		} catch (Throwable t) {
			assertThat(t.toString(), containsString("["
					+ Description.TEST_MECHANISM.toString() + "]"));
		}
	}

	@Test(expected= IllegalArgumentException.class)
	public void createSuiteDescription_whenZeroLength() {
		Description.createSuiteDescription("");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2662.java