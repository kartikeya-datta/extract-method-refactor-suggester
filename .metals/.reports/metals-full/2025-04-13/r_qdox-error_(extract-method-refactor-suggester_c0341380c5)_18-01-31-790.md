error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8567.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8567.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8567.java
text:
```scala
s@@uper(klass);

package org.junit.runners;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Test.None;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.internal.runners.model.MultipleFailureException;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.statements.ExpectException;
import org.junit.internal.runners.statements.Fail;
import org.junit.internal.runners.statements.FailOnTimeout;
import org.junit.internal.runners.statements.InvokeMethod;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.Sortable;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

/**
 * Implements the JUnit 4 standard test case class model, as defined by the
 * annotations in the org.junit package. Many users will never notice this
 * class: it is now the default test class runner, but it should have exactly
 * the same behavior as the old test class runner ({@code JUnit4ClassRunner}).
 * 
 * BlockJUnit4ClassRunner has advantages for writers of custom JUnit runners
 * that are slight changes to the default behavior, however:
 * 
 * <ul>
 * <li>It has a much simpler implementation based on {@link Statement}s,
 * allowing new operations to be inserted into the appropriate point in the
 * execution flow.
 * 
 * <li>It is published, and extension and reuse are encouraged, whereas {@code
 * JUnit4ClassRunner} was in an internal package, and is now deprecated.
 * </ul>
 */
public class BlockJUnit4ClassRunner extends ParentRunner<FrameworkMethod>
		implements Filterable, Sortable {
	private final List<FrameworkMethod> fTestMethods;

	/**
	 * Creates a BlockJUnit4ClassRunner to run {@code klass}
	 * 
	 * @throws InitializationError
	 *             if the test class is malformed.
	 */
	public BlockJUnit4ClassRunner(Class<?> klass) throws InitializationError {
		super(new TestClass(klass));
		fTestMethods= computeTestMethods();
		validate();
	}

	//
	// Implementation of ParentRunner
	// 

	@Override
	protected void runChild(FrameworkMethod method, RunNotifier notifier) {
		EachTestNotifier eachNotifier= makeNotifier(method, notifier);
		if (method.isIgnored()) {
			eachNotifier.fireTestIgnored();
			return;
		}

		eachNotifier.fireTestStarted();
		try {
			childBlock(method).evaluate();
		} catch (AssumptionViolatedException e) {
			// do nothing: same as passing (for 4.5; may change in 4.6)
		} catch (Throwable e) {
			eachNotifier.addFailure(e);
		} finally {
			eachNotifier.fireTestFinished();
		}
	}

	@Override
	protected Description describeChild(FrameworkMethod method) {
		return Description.createTestDescription(getTestClass().getJavaClass(),
				testName(method), method.getMethod().getAnnotations());
	}

	@Override
	protected List<FrameworkMethod> getChildren() {
		return fTestMethods;
	}

	//
	// Override in subclasses
	//

	/**
	 * Returns the methods that run tests (this should be called just once per
	 * class). Default implementation returns all methods annotated with {@code @Test} 
	 * on this class and superclasses that are not overridden.
	 */
	protected List<FrameworkMethod> computeTestMethods() {
		return getTestClass().getTestMethods();
	}

	@Override
	protected void collectInitializationErrors(List<Throwable> errors) {
		getTestClass().validateMethodsForDefaultRunner(errors);
	}

	/**
	 * Returns a new fixture for running a test. Default implementation executes
	 * the test class's no-argument constructor (validation should have ensured
	 * one exists).
	 */
	protected Object createTest() throws Exception {
		return getTestClass().getConstructor().newInstance();
	}

	/**
	 * Returns the name that describes {@code method} for {@link Description}s.
	 * Default implementation is the method's name
	 */
	protected String testName(FrameworkMethod method) {
		return method.getName();
	}

	/**
	 * Returns a Statement that, when executed, either returns normally if
	 * {@code method} passes, or throws an exception if {@code method} fails.
	 * 
	 * The default implementation has this rough description:
	 * 
	 * <ul>
	 * <li>Invoke {@code method} on the result of {@code createTest()}, and
	 * throw any exceptions thrown by either operation.
	 * <li>HOWEVER, if {@code method}'s {@code @Test} annotation has the {@code
	 * expecting} attribute, return normally only if the previous step threw an
	 * exception of the correct type, and throw an exception otherwise.
	 * <li>HOWEVER, if {@code method}'s {@code @Test} annotation has the {@code
	 * timeout} attribute, throw an exception if the previous step takes more
	 * than the specified number of milliseconds.
	 * <li>ALWAYS run all non-overridden {@code @Before} methods on this class
	 * and superclasses before any of the previous steps; if any throws an
	 * Exception, stop execution and pass the exception on.
	 * <li>ALWAYS run all non-overridden {@code @After} methods on this class
	 * and superclasses before any of the previous steps; all After methods are
	 * always executed: exceptions thrown by previous steps are combined, if
	 * necessary, with exceptions from After methods into a
	 * {@link MultipleFailureException}.
	 * </ul>
	 * 
	 * This can be overridden in subclasses, either by overriding this method,
	 * or the implementations of each substep.
	 */
	protected Statement childBlock(FrameworkMethod method) {
		Object test;
		try {
			test= new ReflectiveCallable() {
				@Override
				protected Object runReflectiveCall() throws Throwable {
					return createTest();
				}
			}.run();
		} catch (Throwable e) {
			return new Fail(e);
		}

		Statement link= invoke(method, test);
		link= possiblyExpectingExceptions(method, test, link);
		link= withPotentialTimeout(method, test, link);
		link= withBefores(method, test, link);
		link= withAfters(method, test, link);
		return link;
	}

	//
	// Statement builders
	//

	/**
	 * Returns a {@link Statement} that invokes {@code method} on {@code test}
	 */
	protected Statement invoke(FrameworkMethod method, Object test) {
		return new InvokeMethod(method, test);
	}

	/**
	 * Returns a {@link Statement}: if {@code method}'s {@code @Test} annotation
	 * has the {@code expecting} attribute, return normally only if {@code next}
	 * throws an exception of the correct type, and throw an exception
	 * otherwise.
	 */
	protected Statement possiblyExpectingExceptions(FrameworkMethod method,
			Object test, Statement next) {
		Test annotation= getAnnotation(method);
		return expectsException(annotation) ? new ExpectException(next,
				getExpectedException(annotation)) : next;
	}

	/**
	 * Returns a {@link Statement}: if {@code method}'s {@code @Test} annotation
	 * has the {@code timeout} attribute, throw an exception if {@code next}
	 * takes more than the specified number of milliseconds.
	 */
	protected Statement withPotentialTimeout(FrameworkMethod method,
			Object test, Statement next) {
		long timeout= getTimeout(getAnnotation(method));
		return timeout > 0 ? new FailOnTimeout(next, timeout) : next;
	}

	/**
	 * Returns a {@link Statement}: run all non-overridden {@code @Before}
	 * methods on this class and superclasses before running {@code next}; if
	 * any throws an Exception, stop execution and pass the exception on.
	 */
	protected Statement withBefores(FrameworkMethod method, Object target,
			Statement link) {
		List<FrameworkMethod> befores= getTestClass().getAnnotatedMethods(
				Before.class);
		return new RunBefores(link, befores, target);
	}

	/**
	 * Returns a {@link Statement}: run all non-overridden {@code @After}
	 * methods on this class and superclasses before running {@code next}; all
	 * After methods are always executed: exceptions thrown by previous steps
	 * are combined, if necessary, with exceptions from After methods into a
	 * {@link MultipleFailureException}.
	 */
	protected Statement withAfters(FrameworkMethod method, Object target,
			Statement link) {
		List<FrameworkMethod> afters= getTestClass().getAnnotatedMethods(
				After.class);
		return new RunAfters(link, afters, target);
	}

	private EachTestNotifier makeNotifier(FrameworkMethod method,
			RunNotifier notifier) {
		Description description= describeChild(method);
		return new EachTestNotifier(notifier, description);
	}

	private Class<? extends Throwable> getExpectedException(Test annotation) {
		if (annotation == null || annotation.expected() == None.class)
			return null;
		else
			return annotation.expected();
	}

	private boolean expectsException(Test annotation) {
		return getExpectedException(annotation) != null;
	}

	private long getTimeout(Test annotation) {
		if (annotation == null)
			return 0;
		return annotation.timeout();
	}

	private Test getAnnotation(FrameworkMethod method) {
		return method.getMethod().getAnnotation(Test.class);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8567.java