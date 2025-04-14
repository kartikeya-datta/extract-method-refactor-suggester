error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8190.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8190.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8190.java
text:
```scala
r@@eturn run(Request.classes(classes));

package org.junit.runner;

import java.util.ArrayList;
import java.util.List;

import junit.runner.Version;
import org.junit.internal.JUnitSystem;
import org.junit.internal.RealSystem;
import org.junit.internal.TextListener;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

/**
 * <code>JUnitCore</code> is a facade for running tests. It supports running JUnit 4 tests, 
 * JUnit 3.8.x tests, and mixtures. To run tests from the command line, run 
 * <code>java org.junit.runner.JUnitCore TestClass1 TestClass2 ...</code>.
 * For one-shot test runs, use the static method {@link #runClasses(Class[])}. 
 * If you want to add special listeners,
 * create an instance of {@link org.junit.runner.JUnitCore} first and use it to run the tests.
 * 
 * @see org.junit.runner.Result
 * @see org.junit.runner.notification.RunListener
 * @see org.junit.runner.Request
 */
public class JUnitCore {
	
	private RunNotifier fNotifier;

	/**
	 * Create a new <code>JUnitCore</code> to run tests.
	 */
	public JUnitCore() {
		fNotifier= new RunNotifier();
	}

	/**
	 * Run the tests contained in the classes named in the <code>args</code>.
	 * If all tests run successfully, exit with a status of 0. Otherwise exit with a status of 1.
	 * Write feedback while tests are running and write
	 * stack traces for all failed tests after the tests all complete.
	 * @param args names of classes in which to find tests to run
	 */
	public static void main(String... args) {
		runMainAndExit(new RealSystem(), args);
	}

	/**
	 * Do not use. Testing purposes only.
	 * @param system 
	 */
	public static void runMainAndExit(JUnitSystem system, String... args) {
		Result result= new JUnitCore().runMain(system, args);
		system.exit(result.wasSuccessful() ? 0 : 1);
	}

	/**
	 * Run the tests contained in <code>classes</code>. Write feedback while the tests
	 * are running and write stack traces for all failed tests after all tests complete. This is
	 * similar to {@link #main(String[])}, but intended to be used programmatically.
	 * @param classes Classes in which to find tests
	 * @return a {@link Result} describing the details of the test run and the failed tests.
	 */
	public static Result runClasses(Class<?>... classes) {
		return new JUnitCore().run(classes);
	}
	
	/**
	 * Do not use. Testing purposes only.
	 * @param system 
	 */
	public Result runMain(JUnitSystem system, String... args) {
		system.out().println("JUnit version " + Version.id());
		List<Class<?>> classes= new ArrayList<Class<?>>();
		List<Failure> missingClasses= new ArrayList<Failure>();
		for (String each : args)
			try {
				classes.add(Class.forName(each));
			} catch (ClassNotFoundException e) {
				system.out().println("Could not find class: " + each);
				Description description= Description.createSuiteDescription(each);
				Failure failure= new Failure(description, e);
				missingClasses.add(failure);
			}
		RunListener listener= new TextListener(system);
		addListener(listener);
		Result result= run(classes.toArray(new Class[0]));
		for (Failure each : missingClasses)
			result.getFailures().add(each);
		return result;
	}

	/**
	 * @return the version number of this release
	 */
	public String getVersion() {
		return Version.id();
	}
	
	/**
	 * Run all the tests in <code>classes</code>.
	 * @param classes the classes containing tests
	 * @return a {@link Result} describing the details of the test run and the failed tests.
	 */
	public Result run(Class<?>... classes) {
		return run(Request.classes("All", classes));
	}

	/**
	 * Run all the tests contained in <code>request</code>.
	 * @param request the request describing tests
	 * @return a {@link Result} describing the details of the test run and the failed tests.
	 */
	public Result run(Request request) {
		return run(request.getRunner());
	}

	/**
	 * Run all the tests contained in JUnit 3.8.x <code>test</code>. Here for backward compatibility.
	 * @param test the old-style test
	 * @return a {@link Result} describing the details of the test run and the failed tests.
	 */
	public Result run(junit.framework.Test test) { 
		return run(new JUnit38ClassRunner(test));
	}
	
	/**
	 * Do not use. Testing purposes only.
	 */
	public Result run(Runner runner) {
		Result result= new Result();
		RunListener listener= result.createListener();
		addFirstListener(listener);
		try {
			fNotifier.fireTestRunStarted(runner.getDescription());
			runner.run(fNotifier);
			fNotifier.fireTestRunFinished(result);
		} finally {
			removeListener(listener);
		}
		return result;
	}
	
	private void addFirstListener(RunListener listener) {
		fNotifier.addFirstListener(listener);
	}
	

	/**
	 * Add a listener to be notified as the tests run.
	 * @param listener the listener to add
	 * @see org.junit.runner.notification.RunListener
	 */
	public void addListener(RunListener listener) {
		fNotifier.addListener(listener);
	}

	/**
	 * Remove a listener.
	 * @param listener the listener to remove
	 */
	public void removeListener(RunListener listener) {
		fNotifier.removeListener(listener);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8190.java