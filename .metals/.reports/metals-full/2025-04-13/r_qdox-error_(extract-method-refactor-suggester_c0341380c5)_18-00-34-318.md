error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4876.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4876.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4876.java
text:
```scala
a@@ssertThat(results.toString(), both(containsString("Spain")).and(containsString("INVALID ASSUMPTION 1)")));

package org.junit.tests.listening;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;
import static org.junit.matchers.JUnitMatchers.both;
import static org.junit.matchers.JUnitMatchers.containsString;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.tests.TestSystem;

public class TextListenerTest extends TestCase {
	
	private JUnitCore runner;
	private OutputStream results;
	private TextListener listener;

	@Override
	public void setUp() {
		runner= new JUnitCore();
		TestSystem system= new TestSystem();
		results= system.outContents();
		listener= new TextListener(system);
		runner.addListener(listener);
	}
	
	public static class OneTest {
		@Test public void one() {}
	}
	
	public void testSuccess() throws Exception {
		runner.run(OneTest.class);
		assertTrue(results.toString().startsWith(convert(".\nTime: ")));
		assertTrue(results.toString().endsWith(convert("\n\nOK (1 test)\n\n")));
	}
	
	public static class ErrorTest {
		@Test public void error() throws Exception {throw new Exception();}
	}
	
	public void testError() throws Exception {
		runner.run(ErrorTest.class);
		assertTrue(results.toString().startsWith(convert(".E\nTime: ")));
		assertTrue(results.toString().indexOf(convert("\nThere was 1 failure:\n1) error(org.junit.tests.listening.TextListenerTest$ErrorTest)\njava.lang.Exception")) != -1);
	}
	
	public static class Slow {
		@Test public void pause() throws InterruptedException {
			Thread.sleep(1000);
		}
	}
	
	public void testTime() {
		runner.run(Slow.class);
		assertFalse(results.toString().contains("Time: 0"));
	}
	
	private String convert(String string) {
		OutputStream resultsStream= new ByteArrayOutputStream();
		PrintStream writer= new PrintStream(resultsStream);
		writer.println();
		return string.replace("\n", resultsStream.toString());
	}
	
	public static class IgnoreTest {
		@Ignore("Antimilos is a Greek island in the Cyclades, and an odd reason to ignore a test.") 
		@Test public void failsIfRun() {
			fail();
		}
	}
	
	public void testIgnore() {
		runner.run(IgnoreTest.class);
		assertThat(results.toString(), both(containsString("Antimilos")).and(containsString("IGNORED TEST 1)")));
	}
	
	public static class AssumptionFailureTest {
		@Test public void failsAssumption() {
			assumeThat("Greece", is("Spain"));
		}
	}
	
	public void testAssumptionFailureIsReflected() {
		runner.run(AssumptionFailureTest.class);
		assertThat(results.toString(), containsString("Spain"));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4876.java