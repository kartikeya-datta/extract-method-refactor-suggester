error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3413.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3413.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3413.java
text:
```scala
a@@ssertEquals(fFiltered, BaseTestRunner.getFilteredTrace(fUnfiltered));

package junit.tests.runner;

import java.io.*;
import junit.framework.*;
import junit.runner.BaseTestRunner;

public class StackFilterTest extends TestCase {
	String fFiltered;
	String fUnfiltered;
	
	public StackFilterTest(String name) {
		super(name);
	}
	
	protected void setUp() {
		StringWriter swin= new StringWriter();
		PrintWriter pwin= new PrintWriter(swin);
		pwin.println("junit.framework.AssertionFailedError");
		pwin.println("	at junit.framework.Assert.fail(Assert.java:144)");
		pwin.println("	at junit.framework.Assert.assert(Assert.java:19)");
		pwin.println("	at junit.framework.Assert.assert(Assert.java:26)");
		pwin.println("	at MyTest.f(MyTest.java:13)");
		pwin.println("	at MyTest.testStackTrace(MyTest.java:8)");
		pwin.println("	at java.lang.reflect.Method.invoke(Native Method)");
		pwin.println("	at junit.framework.TestCase.runTest(TestCase.java:156)");
		pwin.println("	at junit.framework.TestCase.runBare(TestCase.java:130)");
		pwin.println("	at junit.framework.TestResult$1.protect(TestResult.java:100)");
		pwin.println("	at junit.framework.TestResult.runProtected(TestResult.java:118)");
		pwin.println("	at junit.framework.TestResult.run(TestResult.java:103)");
		pwin.println("	at junit.framework.TestCase.run(TestCase.java:121)");
		pwin.println("	at junit.framework.TestSuite.runTest(TestSuite.java:157)");
		pwin.println("	at junit.framework.TestSuite.run(TestSuite.java, Compiled Code)");
		pwin.println("	at junit.swingui.TestRunner$17.run(TestRunner.java:669)");
		fUnfiltered= swin.toString();

		StringWriter swout= new StringWriter();
		PrintWriter pwout= new PrintWriter(swout);
		pwout.println("junit.framework.AssertionFailedError");
		pwout.println("	at MyTest.f(MyTest.java:13)");
		pwout.println("	at MyTest.testStackTrace(MyTest.java:8)");
		fFiltered= swout.toString();
	}
		
	public void testFilter() {
		assertEquals(fFiltered, BaseTestRunner.filterStack(fUnfiltered));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3413.java