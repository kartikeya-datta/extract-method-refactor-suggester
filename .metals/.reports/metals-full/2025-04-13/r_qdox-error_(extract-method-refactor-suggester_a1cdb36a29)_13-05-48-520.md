error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9824.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9824.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9824.java
text:
```scala
g@@etWriter().println (" (" + result.runCount() + " test" + (result.runCount() == 1 ? "": "s") + ")");

package junit.textui;


import java.lang.reflect.*;
import java.text.NumberFormat;
import java.util.*;
import java.io.PrintStream;

import junit.framework.*;
import junit.runner.*;

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
	PrintStream fWriter= System.out;
	int fColumn= 0;
	
	public static final int SUCCESS_EXIT= 0;
	public static final int FAILURE_EXIT= 1;
	public static final int EXCEPTION_EXIT= 2;

	/**
	 * Constructs a TestRunner.
	 */
	public TestRunner() {
	}
	/**
	 * Constructs a TestRunner using the given stream for all the output
	 */
	public TestRunner(PrintStream writer) {
		this();
		if (writer == null)
			throw new IllegalArgumentException("Writer can't be null");
		fWriter= writer;
	}
	
	/**
	 * Always use the StandardTestSuiteLoader. Overridden from
	 * BaseTestRunner.
	 */
	public TestSuiteLoader getLoader() {
		return new StandardTestSuiteLoader();
	}

	public void testFailed(int status, Test test, Throwable t) {
		switch (status) {
			case TestRunListener.STATUS_ERROR: getWriter().print("E"); break;
			case TestRunListener.STATUS_FAILURE: getWriter().print("F"); break;
		}
	}
	
	public void testStarted(String testName) {
		getWriter().print(".");
		if (fColumn++ >= 40) {
			getWriter().println();
			fColumn= 0;
		}
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
		result.addListener(this);
		long startTime= System.currentTimeMillis();
		suite.run(result);
		long endTime= System.currentTimeMillis();
		long runTime= endTime-startTime;
		getWriter().println();
		getWriter().println("Time: "+elapsedTimeAsString(runTime));
		print(result);

		getWriter().println();

		pause(wait);
		return result;
	}

	protected void pause(boolean wait) {
		if (wait) {
			getWriter().println("<RETURN> to continue");
			try {
				System.in.read();
			}
			catch(Exception e) {
			}
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
	 * Prints failures to the standard output
	 */
	public synchronized void print(TestResult result) {
	    printErrors(result);
	    printFailures(result);
	    printHeader(result);
	}
	/**
	 * Prints the errors to the standard output
	 */
	public void printErrors(TestResult result) {
	    if (result.errorCount() != 0) {
	        if (result.errorCount() == 1)
		        getWriter().println("There was "+result.errorCount()+" error:");
	        else
		        getWriter().println("There were "+result.errorCount()+" errors:");

			int i= 1;
			for (Enumeration e= result.errors(); e.hasMoreElements(); i++) {
			    TestFailure failure= (TestFailure)e.nextElement();
				getWriter().println(i+") "+failure.failedTest());
				getWriter().print(getFilteredTrace(failure.trace()));
		    }
		}
	}
	/**
	 * Prints failures to the standard output
	 */
	public void printFailures(TestResult result) {
		if (result.failureCount() != 0) {
			if (result.failureCount() == 1)
				getWriter().println("There was " + result.failureCount() + " failure:");
			else
				getWriter().println("There were " + result.failureCount() + " failures:");
			int i = 1;
			for (Enumeration e= result.failures(); e.hasMoreElements(); i++) {
				TestFailure failure= (TestFailure) e.nextElement();
				getWriter().print(i + ") " + failure.failedTest());
				getWriter().print(getFilteredTrace(failure.trace()));
			}
		}
	}
	/**
	 * Prints the header of the report
	 */
	public void printHeader(TestResult result) {
		if (result.wasSuccessful()) {
			getWriter().println();
			getWriter().print("OK");
			getWriter().println (" (" + result.runCount() + " tests)");

		} else {
			getWriter().println();
			getWriter().println("FAILURES!!!");
			getWriter().println("Tests run: "+result.runCount()+ 
				         ",  Failures: "+result.failureCount()+
				         ",  Errors: "+result.errorCount());
		}
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
	 * Starts a test run. Analyzes the command line arguments
	 * and runs the given test suite.
	 */
	protected TestResult start(String args[]) throws Exception {
		String testCase= "";
		boolean wait= false;
		
		for (int i= 0; i < args.length; i++) {
			if (args[i].equals("-wait"))
				wait= true;
			else if (args[i].equals("-c")) 
				testCase= extractClassName(args[++i]);
			else if (args[i].equals("-v"))
				System.err.println("JUnit "+Version.id()+" by Kent Beck and Erich Gamma");
			else
				testCase= args[i];
		}
		
		if (testCase.equals("")) 
			throw new Exception("Usage: TestRunner [-wait] testCaseName, where name is the name of the TestCase class");

		try {
			Test suite= getTest(testCase);
			return doRun(suite, wait);
		}
		catch(Exception e) {
			throw new Exception("Could not create and run test suite: "+e);
		}
	}
		
	protected void runFailed(String message) {
		System.err.println(message);
		System.exit(FAILURE_EXIT);
	}
		
	protected PrintStream getWriter() {
		return fWriter;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9824.java