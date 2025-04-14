error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4325.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4325.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4325.java
text:
```scala
S@@tring classPath= getClass().getClassLoader().getResource(".").getFile() + File.pathSeparator + System.getProperty("java.class.path");

package org.junit.tests;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import static org.junit.Assert.*;

public class JUnitCoreTest {
	
	static public class Fail {
		@Test public void kaboom() {
			fail();
		}
	}
	
	@Test public void failureCausesExitCodeOf1() throws Exception {
		runClass("org.junit.tests.JUnitCoreTest$Fail", 1);
	}

	@Test public void missingClassCausesExitCodeOf1() throws Exception {
		runClass("Foo", 1);
	}

	static public class Succeed {
		@Test public void peacefulSilence() {
		}
	}
	
	@Test public void successCausesExitCodeOf0() throws Exception {
		runClass("org.junit.tests.JUnitCoreTest$Succeed", 0);
	}

	private void runClass(String className, int returnCode) throws IOException, InterruptedException {
		String java= System.getProperty("java.home")+File.separator+"bin"+File.separator+"java";
		String classPath= getClass().getClassLoader().getResource(".").getFile() + ";" + System.getProperty("java.class.path");
		String [] cmd= { java, "-cp", classPath, "org.junit.runner.JUnitCore", className}; 
		Process process= Runtime.getRuntime().exec(cmd);
		InputStream input= process.getInputStream();
		while((input.read()) != -1); 
		assertEquals(returnCode, process.waitFor());
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(JUnitCoreTest.class);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4325.java