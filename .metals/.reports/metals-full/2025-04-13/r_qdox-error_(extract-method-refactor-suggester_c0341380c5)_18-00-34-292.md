error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7070.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7070.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,43]

error in qdox parser
file content:
```java
offset: 43
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7070.java
text:
```scala
@Ignore("was breaking gump") @Test public v@@oid timeoutFailure() throws Exception {

package org.junit.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import junit.framework.JUnit4TestAdapter;
import junit.framework.TestResult;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class TimeoutTest {
	
	static public class FailureWithTimeoutTest {
		@Test(timeout= 1000) public void failure() {
			fail();
		}
	}
	
	@Test public void failureWithTimeout() throws Exception {
		JUnitCore core= new JUnitCore();
		Result result= core.run(FailureWithTimeoutTest.class);
		assertEquals(1, result.getRunCount());
		assertEquals(1, result.getFailureCount());
		assertEquals(AssertionError.class, result.getFailures().get(0).getException().getClass());
	}

	static public class FailureWithTimeoutRunTimeExceptionTest {
		@Test(timeout= 1000) public void failure() {
			throw new NullPointerException();
		}
	}
	
	@Test public void failureWithTimeoutRunTimeException() throws Exception {
		JUnitCore core= new JUnitCore();
		Result result= core.run(FailureWithTimeoutRunTimeExceptionTest.class);
		assertEquals(1, result.getRunCount());
		assertEquals(1, result.getFailureCount());
		assertEquals(NullPointerException.class, result.getFailures().get(0).getException().getClass());
	}

	static public class SuccessWithTimeoutTest {
		@Test(timeout= 1000) public void success() {			
		}
	}
		
	@Test public void successWithTimeout() throws Exception {
		JUnitCore core= new JUnitCore();
		Result result= core.run(SuccessWithTimeoutTest.class);
		assertEquals(1, result.getRunCount());
		assertEquals(0, result.getFailureCount());
	}

	static public class TimeoutFailureTest {
		@Test(timeout= 100) public void success() throws InterruptedException {			
			Thread.sleep(40000);
		}
	}
	
	@Test public void timeoutFailure() throws Exception {
		JUnitCore core= new JUnitCore();
		Result result= core.run(TimeoutFailureTest.class);
		assertEquals(1, result.getRunCount());
		assertEquals(1, result.getFailureCount());
		assertEquals(InterruptedException.class, result.getFailures().get(0).getException().getClass());
	}
	
	static public class InfiniteLoopTest {
		@Test(timeout= 100) public void failure() {
			infiniteLoop();
		}

		private void infiniteLoop() {
			for(;;);
		}
	}
	
	@Test public void infiniteLoop() throws Exception {
		JUnitCore core= new JUnitCore();
		Result result= core.run(InfiniteLoopTest.class);
		assertEquals(1, result.getRunCount());
		assertEquals(1, result.getFailureCount());
		Throwable exception= result.getFailures().get(0).getException();
		assertTrue(exception.getMessage().contains("test timed out after 100 milliseconds"));
	}
	
	static public class ImpatientLoopTest {
		@Test(timeout= 1) public void failure() {
			infiniteLoop();
		}

		private void infiniteLoop() {
			for(;;);
		}
	}
	
	
	@Test public void infiniteLoopRunsForApproximatelyLengthOfTimeout() throws Exception {
		long longTime= runAndTime(InfiniteLoopTest.class);
		long shortTime= runAndTime(ImpatientLoopTest.class);
		long difference= longTime - shortTime;
		assertTrue(String.format("Difference was %sms", difference), difference < 200);
	}

	private long runAndTime(Class<?> clazz) {
		JUnitCore core= new JUnitCore();
		long startTime= System.currentTimeMillis();
		core.run(clazz);
		long totalTime = System.currentTimeMillis() - startTime;
		return totalTime;
	}

	@Ignore("We would like this behavior to work but it may not be possible")
	@Test public void stalledThreadAppearsInStackTrace() throws Exception {
		JUnitCore core= new JUnitCore();
		Result result= core.run(InfiniteLoopTest.class);
		assertEquals(1, result.getRunCount());
		assertEquals(1, result.getFailureCount());
		Throwable exception= result.getFailures().get(0).getException();
		Writer buffer= new StringWriter();
		PrintWriter writer= new PrintWriter(buffer);
		exception.printStackTrace(writer);
		assertTrue(writer.toString().contains("infiniteLoop")); // Make sure we have the stalled frame on the stack somewhere
	}

	@Test public void compatibility() {
		TestResult result= new TestResult();
		new JUnit4TestAdapter(InfiniteLoopTest.class).run(result);
		assertEquals(1, result.errorCount());
	}
	
	static public junit.framework.Test suite() {
		return new JUnit4TestAdapter(TimeoutTest.class);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7070.java