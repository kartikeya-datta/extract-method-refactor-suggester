error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5489.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5489.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5489.java
text:
```scala
r@@eturn new ClassesRequest(classes);

package org.junit.runner;

import java.util.Comparator;

import org.junit.internal.requests.ClassRequest;
import org.junit.internal.requests.ClassesRequest;
import org.junit.internal.requests.ErrorReportingRequest;
import org.junit.internal.requests.FilterRequest;
import org.junit.internal.requests.SortingRequest;
import org.junit.runner.manipulation.Filter;

/**
 * <p>A <code>Request</code> is an abstract description of tests to be run. Older versions of 
 * JUnit did not need such a concept--tests to be run were described either by classes containing
 * tests or a tree of {@link  org.junit.Test}s. However, we want to support filtering and sorting,
 * so we need a more abstract specification than the tests themselves and a richer
 * specification than just the classes.</p>
 * 
 * <p>The flow when JUnit runs tests is that a <code>Request</code> specifies some tests to be run ->
 * a {@link org.junit.runner.Runner} is created for each class implied by the <code>Request</code> -> 
 * the {@link org.junit.runner.Runner} returns a detailed {@link org.junit.runner.Description} 
 * which is a tree structure of the tests to be run.</p>
 */
public abstract class Request {
	/**
	 * Create a <code>Request</code> that, when processed, will run a single test.
	 * This is done by filtering out all other tests. This method is used to support rerunning
	 * single tests.
	 * @param clazz the class of the test
	 * @param methodName the name of the test
	 * @return a <code>Request</code> that will cause a single test be run
	 */
	public static Request method(Class<?> clazz, String methodName) {
		Description method= Description.createTestDescription(clazz, methodName);
		return Request.aClass(clazz).filterWith(method);
	}

	/**
	 * Create a <code>Request</code> that, when processed, will run all the tests
	 * in a class. The odd name is necessary because <code>class</code> is a reserved word.
	 * @param clazz the class containing the tests
	 * @return a <code>Request</code> that will cause all tests in the class to be run
	 */
	public static Request aClass(Class<?> clazz) {
		return new ClassRequest(clazz);
	}

	/**
	 * Create a <code>Request</code> that, when processed, will run all the tests
	 * in a set of classes.
	 * @param collectionName a name to identify this suite of tests
	 * @param classes the classes containing the tests
	 * @return a <code>Request</code> that will cause all tests in the classes to be run
	 */
	public static Request classes(String collectionName, Class<?>... classes) {
		return new ClassesRequest(collectionName, classes);
	}

	public static Request errorReport(Class<?> klass, Throwable cause) {
		return new ErrorReportingRequest(klass, cause);
	}

	/**
	 * Returns a {@link Runner} for this Request
	 * @return corresponding {@link Runner} for this Request
	 */
	public abstract Runner getRunner();

	/**
	 * Returns a Request that only contains those tests that should run when
	 * <code>filter</code> is applied
	 * @param filter The {@link Filter} to apply to this Request
	 * @return the filtered Request
	 */
	public Request filterWith(Filter filter) {
		return new FilterRequest(this, filter);
	}

	/**
	 * Returns a Request that only runs contains tests whose {@link Description}
	 * equals <code>desiredDescription</code>
	 * @param desiredDescription {@link Description} of those tests that should be run
	 * @return the filtered Request
	 */
	public Request filterWith(final Description desiredDescription) {
		return filterWith(new Filter() {
			@Override
			public boolean shouldRun(Description description) {
				if (description.isTest())
					return desiredDescription.equals(description);
				
				// explicitly check if any children want to run
				for (Description each : description.getChildren())
					if (shouldRun(each))
						return true;
				return false;					
			}

			@Override
			public String describe() {
				return String.format("Method %s", desiredDescription.getDisplayName());
			}
		});
	}

	/**
	 * Returns a Request whose Tests can be run in a certain order, defined by 
	 * <code>comparator</code>
	 * 
	 * For example, here is code to run a test suite in alphabetical order:
	 * 
	 * <pre>
	private static Comparator<Description> forward() {
		return new Comparator<Description>() {
			public int compare(Description o1, Description o2) {
				return o1.getDisplayName().compareTo(o2.getDisplayName());
			}
		};
	}
	
	public static main() {
		new JUnitCore().run(Request.aClass(AllTests.class).sortWith(forward()));
	}
	 * </pre>
	 * 
	 * @param comparator definition of the order of the tests in this Request
	 * @return a Request with ordered Tests
	 */
	public Request sortWith(Comparator<Description> comparator) {
		return new SortingRequest(this, comparator);
	}

	public static Request classWithoutSuiteMethod(Class<?> newTestClass) {
		return new ClassRequest(newTestClass, false);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5489.java