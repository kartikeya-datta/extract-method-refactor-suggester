error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4882.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4882.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4882.java
text:
```scala
protected L@@ist<T> getFilteredChildren() {

package org.junit.runners;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.internal.runners.model.MultipleFailureException;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Sortable;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

/**
 * Provides most of the functionality specific to a Runner that implements a
 * "parent node" in the test tree, with children defined by objects of some data
 * type {@code T}. (For {@link BlockJUnit4ClassRunner}, {@code T} is
 * {@link Method} . For {@link Suite}, {@code T} is {@link Class}.) Subclasses
 * must implement finding the children of the node, describing each child, and
 * running each child. ParentRunner will filter and sort children, handle
 * {@code @BeforeClass} and {@code @AfterClass} methods, create a composite
 * {@link Description}, and run children sequentially.
 */
public abstract class ParentRunner<T> extends Runner implements Filterable,
		Sortable {
	private final TestClass fTestClass;

	private Filter fFilter= null;

	private Sorter fSorter= Sorter.NULL;
	
	//TODO: add a flag for whether or not to run children in parallel
	// (as a step towards a delegation-based solution that is more general)

	/**
	 * Constructs a new {@code ParentRunner} that will run {@code @TestClass}
	 * @throws InitializationError 
	 */
	protected ParentRunner(Class<?> testClass) throws InitializationError {
		fTestClass= new TestClass(testClass);
		validate();
	}

	//
	// Must be overridden
	//

	/**
	 * Returns a list of objects that define the children of this Runner.
	 */
	protected abstract List<T> getChildren();

	/**
	 * Returns a {@link Description} for {@code child}, which can be assumed to
	 * be an element of the list returned by {@link ParentRunner#getChildren()}
	 */
	protected abstract Description describeChild(T child);

	/**
	 * Runs the test corresponding to {@code child}, which can be assumed to be
	 * an element of the list returned by {@link ParentRunner#getChildren()}.
	 * Subclasses are responsible for making sure that relevant test events are
	 * reported through {@code notifier}
	 */
	protected abstract void runChild(T child, RunNotifier notifier);

	//
	// May be overridden
	//

	/**
	 * Adds to {@code errors} a throwable for each problem noted with the test class (available from {@link #getTestClass()}).
	 * Default implementation adds an error for each method annotated with
	 * {@code @BeforeClass} or {@code @AfterClass} that is not
	 * {@code public static void} with no arguments.
	 */
	protected void collectInitializationErrors(List<Throwable> errors) {
		validatePublicVoidNoArgMethods(BeforeClass.class, true, errors);
		validatePublicVoidNoArgMethods(AfterClass.class, true, errors);
	}

	/**
	 * Adds to {@code errors} if any method in this class is annotated with
	 * {@code annotation}, but:
	 * <ul>
	 * <li>is not public, or
	 * <li>takes parameters, or
	 * <li>returns something other than void, or
	 * <li>is static (given {@code isStatic is false}), or
	 * <li>is not static (given {@code isStatic is true}).
	 */
	protected void validatePublicVoidNoArgMethods(Class<? extends Annotation> annotation,
			boolean isStatic, List<Throwable> errors) {
		List<FrameworkMethod> methods= getTestClass().getAnnotatedMethods(annotation);

		for (FrameworkMethod eachTestMethod : methods)
			eachTestMethod.validatePublicVoidNoArg(isStatic, errors);
	}

	/** 
	 * Constructs a {@code Statement} to run all of the tests in the test class. Override to add pre-/post-processing. 
	 * Here is an outline of the implementation:
	 * <ul>
	 * <li>Call {@link #runChild(Object, RunNotifier)} on each object returned by {@link #getChildren()} (subject to any imposed filter and sort).</li>
	 * <li>ALWAYS run all non-overridden {@code @BeforeClass} methods on this class
	 * and superclasses before the previous step; if any throws an
	 * Exception, stop execution and pass the exception on.
	 * <li>ALWAYS run all non-overridden {@code @AfterClass} methods on this class
	 * and superclasses before any of the previous steps; all AfterClass methods are
	 * always executed: exceptions thrown by previous steps are combined, if
	 * necessary, with exceptions from AfterClass methods into a
	 * {@link MultipleFailureException}.
	 * </ul>
	 * @param notifier
	 * @return {@code Statement}
	 */
	protected Statement classBlock(final RunNotifier notifier) {
		Statement statement= childrenInvoker(notifier);
		statement= withBeforeClasses(statement);
		statement= withAfterClasses(statement);
		return statement;
	}

	/**
	 * Returns a {@link Statement}: run all non-overridden {@code @BeforeClass} methods on this class
	 * and superclasses before executing {@code statement}; if any throws an
	 * Exception, stop execution and pass the exception on.
	 */
	protected Statement withBeforeClasses(Statement statement) {
		List<FrameworkMethod> befores= fTestClass
				.getAnnotatedMethods(BeforeClass.class);
		statement= new RunBefores(statement, befores, null);
		return statement;
	}

	/**
	 * Returns a {@link Statement}: run all non-overridden {@code @AfterClass} methods on this class
	 * and superclasses before executing {@code statement}; all AfterClass methods are
	 * always executed: exceptions thrown by previous steps are combined, if
	 * necessary, with exceptions from AfterClass methods into a
	 * {@link MultipleFailureException}.
	 */
	protected Statement withAfterClasses(Statement statement) {
		List<FrameworkMethod> afters= fTestClass
				.getAnnotatedMethods(AfterClass.class);
		statement= new RunAfters(statement, afters, null);
		return statement;
	}

	/**
	 * Returns a {@link Statement}: Call {@link #runChild(Object, RunNotifier)}
	 * on each object returned by {@link #getChildren()} (subject to any imposed
	 * filter and sort)
	 */
	protected Statement childrenInvoker(final RunNotifier notifier) {
		return new Statement() {
			@Override
			public void evaluate() {
				runChildren(notifier);
			}
		};
	}

	private void runChildren(final RunNotifier notifier) {
		for (T each : getFilteredChildren())
			runChild(each, notifier);
	}

	/**
	 * Returns a name used to describe this Runner
	 */
	protected String getName() {
		return fTestClass.getName();
	}

	//
	// Available for subclasses
	//

	/**
	 * Returns a {@link TestClass} object wrapping the class to be executed.
	 */
	protected final TestClass getTestClass() {
		return fTestClass;
	}

	//
	// Implementation of Runner
	// 
	
	@Override
	public Description getDescription() {
		Description description= Description.createSuiteDescription(getName(),
				fTestClass.getAnnotations());
		for (T child : getFilteredChildren())
			description.addChild(describeChild(child));
		return description;
	}

	@Override
	public void run(final RunNotifier notifier) {
		EachTestNotifier testNotifier= new EachTestNotifier(notifier,
				getDescription());
		try {
			Statement statement= classBlock(notifier);
			statement.evaluate();
		} catch (AssumptionViolatedException e) {
			testNotifier.fireTestIgnored();
		} catch (StoppedByUserException e) {
			throw e;
		} catch (Throwable e) {
			testNotifier.addFailure(e);
		}
	}
	
	//
	// Implementation of Filterable and Sortable
	//

	public void filter(Filter filter) throws NoTestsRemainException {
		fFilter= filter;

		for (T each : getChildren())
			if (shouldRun(each))
				return;
		throw new NoTestsRemainException();
	}

	public void sort(Sorter sorter) {
		fSorter= sorter;
	}
	
	//
	// Private implementation
	// 

	private void validate() throws InitializationError {
		List<Throwable> errors= new ArrayList<Throwable>();
		collectInitializationErrors(errors);
		if (!errors.isEmpty())
			throw new InitializationError(errors);
	}

	private List<T> getFilteredChildren() {
		ArrayList<T> filtered= new ArrayList<T>();
		for (T each : getChildren())
			if (shouldRun(each))
				try {
					filterChild(each);
					sortChild(each);
					filtered.add(each);
				} catch (NoTestsRemainException e) {
					// don't add it
				}
		Collections.sort(filtered, comparator());
		return filtered;
	}

	private void sortChild(T child) {
		fSorter.apply(child);
	}

	private void filterChild(T child) throws NoTestsRemainException {
		if (fFilter != null)
			fFilter.apply(child);
	}

	private boolean shouldRun(T each) {
		return fFilter == null || fFilter.shouldRun(describeChild(each));
	}

	private Comparator<? super T> comparator() {
		return new Comparator<T>() {
			public int compare(T o1, T o2) {
				return fSorter.compare(describeChild(o1), describeChild(o2));
			}
		};
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4882.java