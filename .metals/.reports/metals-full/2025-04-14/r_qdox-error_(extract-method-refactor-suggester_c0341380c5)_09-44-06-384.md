error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4721.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4721.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4721.java
text:
```scala
a@@ssertThat(result.getInvalidAssumptionCount(), is(1));

package org.junit.tests.experimental;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeNotNull;
import static org.junit.Assume.assumeThat;
import static org.junit.Assume.assumeTrue;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;
import static org.junit.matchers.StringContains.containsString;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assume.AssumptionViolatedException;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;


public class AssumptionTest {
	public static class HasFailingAssumption {
		@Test
		public void assumptionsFail() {
			assumeThat(3, is(4));
			fail();
		}
	}

	@Test
	public void failedAssumptionsMeanIgnored() {
		Result result= JUnitCore.runClasses(HasFailingAssumption.class);
		assertThat(result.getRunCount(), is(0));
		assertThat(result.getFailedAssumptionCount(), is(1));
		assertThat(result.getFailureCount(), is(0));
	}

	@Test
	public void failingAssumptionsPrint() {
		assertThat(testResult(HasFailingAssumption.class).toString(), containsString("invalid assumption"));
		assertThat(testResult(HasFailingAssumption.class).toString(), containsString("INVALID ASSUMPTION 1)"));
	}

	public static class HasPassingAssumption {
		@Test
		public void assumptionsFail() {
			assumeThat(3, is(3));
			fail();
		}
	}

	@Test
	public void passingAssumptionsScootThrough() {
		Result result= JUnitCore.runClasses(HasPassingAssumption.class);
		assertThat(result.getRunCount(), is(1));
		assertThat(result.getIgnoreCount(), is(0));
		assertThat(result.getFailureCount(), is(1));
	}
	
	@Test(expected= AssumptionViolatedException.class)
	public void assumeThatWorks() {
		assumeThat(1, is(2));
	}

	@Test
	public void assumeThatPasses() {
		assumeThat(1, is(1));
		assertCompletesNormally();
	}

	@Test
	public void assumeThatPassesOnStrings() {
		assumeThat("x", is("x"));
		assertCompletesNormally();
	}

	@Test(expected= AssumptionViolatedException.class)
	public void assumeNotNullThrowsException() {
		Object[] objects= { 1, 2, null };
		assumeNotNull(objects);
	}

	@Test
	public void assumeNotNullPasses() {
		Object[] objects= { 1, 2 };
		assumeNotNull(objects);
		assertCompletesNormally();
	}

	@Test
	public void assumeNotNullIncludesParameterList() {
		try {
			Object[] objects= { 1, 2, null };
			assumeNotNull(objects);
		} catch (AssumptionViolatedException e) {
			assertThat(e.getMessage(), containsString("1, 2, null"));
		} catch (Exception e) {
			fail("Should have thrown AssumptionViolatedException");
		}
	}
	@Test
	public void assumeNoExceptionThrows() {
		final Throwable exception= new NullPointerException();
		try {
			assumeNoException(exception);
			fail("Should have thrown exception");
		} catch (AssumptionViolatedException e) {
			assertThat(e.getCause(), is(exception));
		}
	}

	private void assertCompletesNormally() {
	}

	@Test(expected=AssumptionViolatedException.class) public void assumeTrueWorks() {
		Assume.assumeTrue(false);
	}
	
	public static class HasFailingAssumeInBefore {
		@Before public void checkForSomethingThatIsntThere() {
			assumeTrue(false);
		}
		
		@Test public void failing() {
			fail();
		}
	}
	
	@Test public void failingAssumptionInBeforePreventsTestRun() {
		assertThat(testResult(HasFailingAssumeInBefore.class), isSuccessful());
	}
	
	public static class HasFailingAssumeInBeforeClass {
		@BeforeClass public static void checkForSomethingThatIsntThere() {
			assumeTrue(false);
		}
		
		@Test public void failing() {
			fail();
		}
	}
	
	@Test public void failingAssumptionInBeforeClassIgnoresClass() {
		assertThat(testResult(HasFailingAssumeInBeforeClass.class), isSuccessful());
	}
	
	public static class AssumptionFailureInConstructor {
		public AssumptionFailureInConstructor() {
			assumeTrue(false);
		}
		
		@Test public void shouldFail() {
			fail();
		}
	}
	
	@Test public void failingAssumptionInConstructorIgnoresClass() {
		assertThat(testResult(AssumptionFailureInConstructor.class), isSuccessful());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4721.java