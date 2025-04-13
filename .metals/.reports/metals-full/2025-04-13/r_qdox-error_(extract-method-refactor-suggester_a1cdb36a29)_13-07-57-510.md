error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5929.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5929.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5929.java
text:
```scala
L@@ist<Throwable> errors= validator.validateMethodsForDefaultRunner();

package org.junit.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import junit.framework.JUnit4TestAdapter;
import junit.framework.TestResult;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.MethodValidator;
import org.junit.internal.runners.TestClassRunner;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class TestMethodTest {

	@SuppressWarnings("all")  
	public static class EverythingWrong {
		private EverythingWrong() {}
		@BeforeClass public void notStaticBC() {}
		@BeforeClass static void notPublicBC() {}
		@BeforeClass public static int nonVoidBC() { return 0; }
		@BeforeClass public static void argumentsBC(int i) {}
		@BeforeClass public static void fineBC() {}
		@AfterClass public void notStaticAC() {}
		@AfterClass static void notPublicAC() {}
		@AfterClass public static int nonVoidAC() { return 0; }
		@AfterClass public static void argumentsAC(int i) {}
		@AfterClass public static void fineAC() {}
		@After public static void staticA() {}
		@After void notPublicA() {}
		@After public int nonVoidA() { return 0; }
		@After public void argumentsA(int i) {}
		@After public void fineA() {}
		@Before public static void staticB() {}
		@Before void notPublicB() {}
		@Before public int nonVoidB() { return 0; }
		@Before public void argumentsB(int i) {}
		@Before public void fineB() {}
		@Test public static void staticT() {}
		@Test void notPublicT() {}
		@Test public int nonVoidT() { return 0; }
		@Test public void argumentsT(int i) {}
		@Test public void fineT() {}
	}
	
	@Test public void testFailures() throws Exception {
		List<Throwable> problems= validateAllMethods(EverythingWrong.class);
		int errorCount= 1 + 4 * 5; // missing constructor plus four invalid methods for each annotation */
		assertEquals(errorCount, problems.size());
	}

	static public class SuperWrong {
		@Test void notPublic() {
		}
	}

	static public class SubWrong extends SuperWrong {
		@Test public void justFine() {
		}
	}

	@Test public void validateInheritedMethods() throws Exception {
		List<Throwable> problems= validateAllMethods(SubWrong.class);
		assertEquals(1, problems.size());
	}

	static public class SubShadows extends SuperWrong {
		@Override
		@Test public void notPublic() {
		}
	}

	@Test public void dontValidateShadowedMethods() throws Exception {
		List<Throwable> problems= validateAllMethods(SubShadows.class);
		assertTrue(problems.isEmpty());
	}

	private List<Throwable> validateAllMethods(Class<?> clazz) {
		try {
			new TestClassRunner(clazz);
		} catch (InitializationError e) {
			return e.getCauses();
		}
		return Collections.emptyList();
	}

	static public class IgnoredTest {
		@Test public void valid() {}
		@Ignore @Test public void ignored() {}
		@Ignore("For testing purposes") @Test public void withReason() {}
	}

	@Test public void ignoreRunner() {
		JUnitCore runner= new JUnitCore();
		Result result= runner.run(IgnoredTest.class);
		assertEquals(2, result.getIgnoreCount());
	}

	@Test public void compatibility() {
		TestResult result= new TestResult();
		new JUnit4TestAdapter(IgnoredTest.class).run(result);
		assertEquals(1, result.runCount());
	}
	
	public static class Confused {
		@Test public void a(Object b) {
		}
		
		@Test public void a() {
		}
	}
	
	@Test public void overloaded() {
		MethodValidator validator= new MethodValidator(Confused.class);
		List<Throwable> errors= validator.validateAllMethods();
		assertEquals(1, errors.size());
	}
	
	public static class OnlyTestIsIgnored {
		@Ignore @Test public void ignored() {}
	}
	
	@Test public void onlyIgnoredMethodsIsStillFineTestClass() {
		Result result= JUnitCore.runClasses(OnlyTestIsIgnored.class);
		assertEquals(0, result.getFailureCount());
		assertEquals(1, result.getIgnoreCount());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5929.java