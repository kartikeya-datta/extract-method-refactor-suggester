error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7483.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7483.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7483.java
text:
```scala
public b@@oolean hasSuiteMethod() {

package org.junit.internal.requests;

import org.junit.Ignore;
import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.OldTestClassRunner;
import org.junit.internal.runners.TestClassRunner;
import org.junit.runner.Request;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runners.AllTests;

public class ClassRequest extends Request {
	private static final String CONSTRUCTOR_ERROR_FORMAT= "Custom runner class %s should have a public constructor with signature %s(Class testClass)";
	private final Class<?> fTestClass;
	private boolean fCanUseSuiteMethod;

	public ClassRequest(Class<?> testClass, boolean canUseSuiteMethod) {
		fTestClass= testClass;
		fCanUseSuiteMethod= canUseSuiteMethod;
	}

	public ClassRequest(Class<?> testClass) {
		this(testClass, true);
	}
	
	@Override
	public Runner getRunner() {
		return buildRunner(getRunnerClass(fTestClass)); 
	}

	public Runner buildRunner(Class<? extends Runner> runnerClass) {
		try {
			return runnerClass.getConstructor(Class.class).newInstance(new Object[] { fTestClass });
		} catch (NoSuchMethodException e) {
			String simpleName= runnerClass.getSimpleName();
			InitializationError error= new InitializationError(String.format(
					CONSTRUCTOR_ERROR_FORMAT, simpleName, simpleName));
			return Request.errorReport(fTestClass, error).getRunner();
		} catch (Exception e) {
			return Request.errorReport(fTestClass, e).getRunner();
		}
	}

	Class<? extends Runner> getRunnerClass(final Class<?> testClass) {
		if (testClass.getAnnotation(Ignore.class) != null)
			return new IgnoredClassRunner(testClass).getClass();
		RunWith annotation= testClass.getAnnotation(RunWith.class);
		if (annotation != null) {
			return annotation.value();
		} else if (hasSuiteMethod() && fCanUseSuiteMethod) {
			return AllTests.class;
		} else if (isPre4Test(testClass)) {
			return OldTestClassRunner.class; 
		} else {
			return TestClassRunner.class;
		}
	}
	
	private boolean hasSuiteMethod() {
		// TODO: check all attributes
		try {
			fTestClass.getMethod("suite");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			return false;
		}
		return true;
	}

	boolean isPre4Test(Class<?> testClass) {
		return junit.framework.TestCase.class.isAssignableFrom(testClass);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7483.java