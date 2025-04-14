error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2369.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2369.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2369.java
text:
```scala
a@@ssertEquals(expected, output.toString());


package junit.tests.runner;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.ResultPrinter;
import junit.textui.TestRunner;

public class TextFeedbackTest extends TestCase {
	OutputStream output;
	TestRunner runner;
	
	static class TestResultPrinter extends ResultPrinter {
		TestResultPrinter(PrintStream writer) {
			super(writer);
		}
		
		/* Spoof printing time so the tests are deterministic
		 */
		protected String elapsedTimeAsString(long runTime) {
			return "0";
		}
	}
	
	public static void main(String[] args) {
		TestRunner.run(TextFeedbackTest.class);
	}
	
	public void setUp() {
		output= new ByteArrayOutputStream();
		runner= new TestRunner(new TestResultPrinter(new PrintStream(output)));
	}
	
	public void testEmptySuite() {
		String expected= expected(new String[]{"", "Time: 0", "", "OK (0 tests)", ""});
		runner.doRun(new TestSuite());
		assertEquals(expected, output.toString());
	}

	
	public void testOneTest() {
		String expected= expected(new String[]{".", "Time: 0", "", "OK (1 test)", ""});
		TestSuite suite = new TestSuite();
		suite.addTest(new TestCase() { public void runTest() {}});
		runner.doRun(suite);
		assertEquals(expected, output.toString());
	}
	
	public void testTwoTests() {
		String expected= expected(new String[]{"..", "Time: 0", "", "OK (2 tests)", ""});
		TestSuite suite = new TestSuite();
		suite.addTest(new TestCase() { public void runTest() {}});
		suite.addTest(new TestCase() { public void runTest() {}});
		runner.doRun(suite);
		assertEquals(expected.toString(), output.toString());
	}

	public void testFailure() {
		String expected= expected(new String[]{".F", "Time: 0", "Failures here", "", "FAILURES!!!", "Tests run: 1,  Failures: 1,  Errors: 0", ""});
		ResultPrinter printer= new TestResultPrinter(new PrintStream(output)) {
			public void printFailures(TestResult result) {
				getWriter().println("Failures here");
			}
		};
		runner.setPrinter(printer);
		TestSuite suite = new TestSuite();
		suite.addTest(new TestCase() { public void runTest() {throw new AssertionFailedError();}});
		runner.doRun(suite);
		assertEquals(expected, output.toString());
	}
	
	public void testError() {
		String expected= expected(new String[]{".E", "Time: 0", "Errors here", "", "FAILURES!!!", "Tests run: 1,  Failures: 0,  Errors: 1", ""});
		ResultPrinter printer= new TestResultPrinter(new PrintStream(output)) {
			public void printErrors(TestResult result) {
				getWriter().println("Errors here");
			}
		};
		runner.setPrinter(printer);
		TestSuite suite = new TestSuite();
		suite.addTest(new TestCase() { public void runTest() throws Exception {throw new Exception();}});
		runner.doRun(suite);
		assertEquals(expected, output.toString());
	}
	
	private String expected(String[] lines) {
		OutputStream expected= new ByteArrayOutputStream();
		PrintStream expectedWriter= new PrintStream(expected);
		for (int i= 0; i < lines.length; i++)
			expectedWriter.println(lines[i]);
		return expected.toString(); 
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2369.java