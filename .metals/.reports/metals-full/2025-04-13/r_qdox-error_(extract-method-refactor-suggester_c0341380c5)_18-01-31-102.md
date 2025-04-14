error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1911.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1911.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1911.java
text:
```scala
public static D@@escription createTestDescription(Class<?> clazz, String name) {

package org.junit.runner;

import java.util.ArrayList;

/**
 * <p>A <code>Description</code> describes a test which is to be run or has been run. <code>Descriptions</code> 
 * can be atomic (a single test) or compound (containing children tests). <code>Descriptions</code> are used
 * to provide feedback about the tests that are about to run (for example, the tree view
 * visible in many IDEs) or tests that have been run (for example, the failures view).</p>
 * 
 * <p><code>Descriptions</code> are implemented as a single class rather than a Composite because
 * they are entirely informational. They contain no logic aside from counting their tests.</p>
 * 
 * <p>In the past, we used the raw {@link junit.framework.TestCase}s and {@link junit.framework.TestSuite}s
 * to display the tree of tests. This was no longer viable in JUnit 4 because atomic tests no longer have 
 * a superclass below {@link Object}. We needed a way to pass a class and name together. Description 
 * emerged from this.</p>
 * 
 * @see org.junit.runner.Request
 * @see org.junit.runner.Runner
 */
public class Description {
	
	/**
	 * Create a <code>Description</code> named <code>name</code>.
	 * Generally, you will add children to this <code>Description</code>.
	 * @param name the name of the <code>Description</code> 
	 * @return a <code>Description</code> named <code>name</code>
	 */
	public static Description createSuiteDescription(String name) {
		return new Description(name);
	}

	/**
	 * Create a <code>Description</code> of a single test named <code>name</code> in the class <code>clazz</code>.
	 * Generally, this will be a leaf <code>Description</code>.
	 * @param clazz the class of the test
	 * @param name the name of the test (a method name for test annotated with {@link org.junit.Test})
	 * @return a <code>Description</code> named <code>name</code>
	 */
	public static Description createTestDescription(Class clazz, String name) {
		return new Description(String.format("%s(%s)", name, clazz.getName()));
	}
	
	/**
	 * Create a generic <code>Description</code> that says there are tests in <code>testClass</code>.
	 * This is used as a last resort when you cannot precisely describe the individual tests in the class.
	 * @param testClass A {@link Class} containing tests 
	 * @return a <code>Description</code> of <code>testClass</code>
	 */
	public static Description createSuiteDescription(Class<?> testClass) {
		return new Description(testClass.getName());
	}
	
	public static Description TEST_MECHANISM = new Description("Test mechanism");
	private final ArrayList<Description> fChildren= new ArrayList<Description>();
	private final String fDisplayName;

	//TODO we seem to be using the static factories exclusively
	protected Description(final String displayName) {
		fDisplayName= displayName;
	}

	/**
	 * @return a user-understandable label
	 */
	public String getDisplayName() {
		return fDisplayName;
	}

	/**
	 * Add <code>Description</code> as a child of the receiver.
	 * @param description the soon-to-be child.
	 */
	public void addChild(Description description) {
		getChildren().add(description);
	}

	/**
	 * @return the receiver's children, if any
	 */
	public ArrayList<Description> getChildren() {
		return fChildren;
	}

	/**
	 * @return <code>true</code> if the receiver is a suite
	 */
	public boolean isSuite() {
		return !isTest();
	}

	/**
	 * @return <code>true</code> if the receiver is an atomic test
	 */
	public boolean isTest() {
		return getChildren().isEmpty();
	}

	/**
	 * @return the total number of atomic tests in the receiver
	 */
	public int testCount() {
		if (isTest())
			return 1;
		int result= 0;
		for (Description child : getChildren())
			result+= child.testCount();
		return result;
	}

	@Override
	public int hashCode() {
		return getDisplayName().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Description))
			return false;
		Description d = (Description) obj;
		return getDisplayName().equals(d.getDisplayName())
				&& getChildren().equals(d.getChildren());
	}
	
	@Override
	public String toString() {
		return getDisplayName();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1911.java