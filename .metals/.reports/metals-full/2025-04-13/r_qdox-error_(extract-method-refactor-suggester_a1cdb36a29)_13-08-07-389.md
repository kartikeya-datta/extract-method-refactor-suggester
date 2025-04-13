error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8638.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8638.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8638.java
text:
```scala
i@@f (!method.equals(""))

package junit.textui;


import java.io.PrintStream;

import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.runner.BaseTestRunner;
import junit.runner.StandardTestSuiteLoader;
import junit.runner.TestSuiteLoader;
import junit.runner.Version;

/**
 * A command line based tool to run tests.
 * <pre>
 * java junit.textui.TestRunner [-wait] TestCaseClass
 * </pre>
 * TestRunner expects the name of a TestCase class as argument.
 * If this class defines a static <code>suite</code> method it 
 * will be invoked and the returned test is run. Otherwise all 
 * the methods starting with "test" having no arguments are run.
 * <p>
 * When the wait command line argument is given TestRunner
 * waits until the users types RETURN.
 * <p>
 * TestRunner prints a trace as the tests are executed followed by a
 * summary at the end. 
 */
public class TestRunner extends BaseTestRunner {
	private ResultPrinter fPrinter;
	
	public static final int SUCCESS_EXIT= 0;
	public static final int FAILURE_EXIT= 1;
	public static final int EXCEPTION_EXIT= 2;

	/**
	 * Constructs a TestRunner.
	 */
	public TestRunner() {
		this(System.out);
	}

	/**
	 * Constructs a TestRunner using the given stream for all the output
	 */
	public TestRunner(PrintStream writer) {
		this(new ResultPrinter(writer));
	}
	
	/**
	 * Constructs a TestRunner using the given ResultPrinter all the output
	 */
	public TestRunner(ResultPrinter printer) {
		fPrinter= printer;
	}
	
	/**
	 * Runs a suite extracted from a TestCase subclass.
	 */
	static public void run(Class testClass) {
		run(new TestSuite(testClass));
	}

	/**
	 * Runs a single test and collects its results.
	 * This method can be used to start a test run
	 * from your program.
	 * <pre>
	 * public static void main (String[] args) {
	 *     test.textui.TestRunner.run(suite());
	 * }
	 * </pre>
	 */
	static public TestResult run(Test test) {
		TestRunner runner= new TestRunner();
		return runner.doRun(test);
	}

	/**
	 * Runs a single test and waits until the user
	 * types RETURN.
	 */
	static public void runAndWait(Test suite) {
		TestRunner aTestRunner= new TestRunner();
		aTestRunner.doRun(suite, true);
	}

	/**
	 * Always use the StandardTestSuiteLoader. Overridden from
	 * BaseTestRunner.
	 */
	public TestSuiteLoader getLoader() {
		return new StandardTestSuiteLoader();
	}

	public void testFailed(int status, Test test, Throwable t) {
	}
	
	public void testStarted(String testName) {
	}
	
	public void testEnded(String testName) {
	}

	/**
	 * Creates the TestResult to be used for the test run.
	 */
	protected TestResult createTestResult() {
		return new TestResult();
	}
	
	public TestResult doRun(Test test) {
		return doRun(test, false);
	}
	
	public TestResult doRun(Test suite, boolean wait) {
		TestResult result= createTestResult();
		result.addListener(fPrinter);
		long startTime= System.currentTimeMillis();
		suite.run(result);
		long endTime= System.currentTimeMillis();
		long runTime= endTime-startTime;
		fPrinter.print(result, runTime);

		pause(wait);
		return result;
	}

	protected void pause(boolean wait) {
		if (!wait) return;
		fPrinter.printWaitPrompt();
		try {
			System.in.read();
		}
		catch(Exception e) {
		}
	}
	
	public static void main(String args[]) {
		TestRunner aTestRunner= new TestRunner();
		try {
			TestResult r= aTestRunner.start(args);
			if (!r.wasSuccessful()) 
				System.exit(FAILURE_EXIT);
			System.exit(SUCCESS_EXIT);
		} catch(Exception e) {
			System.err.println(e.getMessage());
			System.exit(EXCEPTION_EXIT);
		}
	}

	/**
	 * Starts a test run. Analyzes the command line arguments and runs the given
	 * test suite.
	 */
	public TestResult start(String args[]) throws Exception {
		String testCase= "";
		String method= "";
		boolean wait= false;

		for (int i= 0; i < args.length; i++) {
			if (args[i].equals("-wait"))
				wait= true;
			else if (args[i].equals("-c"))
				testCase= extractClassName(args[++i]);
			else if (args[i].equals("-m")) {
				String arg= args[++i];
				int lastIndex= arg.lastIndexOf('.');
				testCase= arg.substring(0, lastIndex);
				method= arg.substring(lastIndex + 1);
			} else if (args[i].equals("-v"))
				System.err.println("JUnit " + Version.id() + " by Kent Beck and Erich Gamma");
			else
				testCase= args[i];
		}

		if (testCase.equals(""))
			throw new Exception("Usage: TestRunner [-wait] testCaseName, where name is the name of the TestCase class");

		try {
			if (method != null) 
				return runSingleMethod(testCase, method, wait);
			Test suite= getTest(testCase);
			return doRun(suite, wait);
		} catch (Exception e) {
			throw new Exception("Could not create and run test suite: " + e);
		}
	}

	protected TestResult runSingleMethod(String testCase, String method, boolean wait) throws Exception {
		Class testClass= loadSuiteClass(testCase);
		Test test= TestSuite.createTest(testClass, method);
		return doRun(test, wait);
	}

	protected void runFailed(String message) {
		System.err.println(message);
		System.exit(FAILURE_EXIT);
	}
	
	public void setPrinter(ResultPrinter printer) {
		fPrinter= printer;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8638.java