error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12329.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12329.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 668
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12329.java
text:
```scala
public class RefreshableTargetSourceTests {

/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

p@@ackage org.springframework.aop.target.dynamic;

import org.junit.Test;

import org.springframework.tests.Assume;
import org.springframework.tests.TestGroup;

import static org.junit.Assert.*;

/**
 * @author Rob Harrop
 * @author Chris Beams
 */
public final class RefreshableTargetSourceTests {

	/**
	 * Test what happens when checking for refresh but not refreshing object.
	 */
	@Test
	public void testRefreshCheckWithNonRefresh() throws Exception {
		CountingRefreshableTargetSource ts = new CountingRefreshableTargetSource();
		ts.setRefreshCheckDelay(0);

		Object a = ts.getTarget();
		Thread.sleep(1);
		Object b = ts.getTarget();

		assertEquals("Should be one call to freshTarget to get initial target", 1, ts.getCallCount());
		assertSame("Returned objects should be the same - no refresh should occur", a, b);
	}

	/**
	 * Test what happens when checking for refresh and refresh occurs.
	 */
	@Test
	public void testRefreshCheckWithRefresh() throws Exception {
		CountingRefreshableTargetSource ts = new CountingRefreshableTargetSource(true);
		ts.setRefreshCheckDelay(0);

		Object a = ts.getTarget();
		Thread.sleep(100);
		Object b = ts.getTarget();

		assertEquals("Should have called freshTarget twice", 2, ts.getCallCount());
		assertNotSame("Should be different objects", a, b);
	}

	/**
	 * Test what happens when no refresh occurs.
	 */
	@Test
	public void testWithNoRefreshCheck() throws Exception {
		CountingRefreshableTargetSource ts = new CountingRefreshableTargetSource(true);
		ts.setRefreshCheckDelay(-1);

		Object a = ts.getTarget();
		Object b = ts.getTarget();

		assertEquals("Refresh target should only be called once", 1, ts.getCallCount());
		assertSame("Objects should be the same - refresh check delay not elapsed", a, b);
	}

	@Test
	public void testRefreshOverTime() throws Exception {
		Assume.group(TestGroup.PERFORMANCE);

		CountingRefreshableTargetSource ts = new CountingRefreshableTargetSource(true);
		ts.setRefreshCheckDelay(100);

		Object a = ts.getTarget();
		Object b = ts.getTarget();
		assertEquals("Objects should be same", a, b);

		Thread.sleep(50);

		Object c = ts.getTarget();
		assertEquals("A and C should be same", a, c);

		Thread.sleep(60);

		Object d = ts.getTarget();
		assertNotNull("D should not be null", d);
		assertFalse("A and D should not be equal", a.equals(d));

		Object e = ts.getTarget();
		assertEquals("D and E should be equal", d, e);

		Thread.sleep(110);

		Object f = ts.getTarget();
		assertFalse("E and F should be different", e.equals(f));
	}


	private static class CountingRefreshableTargetSource extends AbstractRefreshableTargetSource {

		private int callCount;

		private boolean requiresRefresh;

		public CountingRefreshableTargetSource() {
		}

		public CountingRefreshableTargetSource(boolean requiresRefresh) {
			this.requiresRefresh = requiresRefresh;
		}

		@Override
		protected Object freshTarget() {
			this.callCount++;
			return new Object();
		}

		public int getCallCount() {
			return this.callCount;
		}

		@Override
		protected boolean requiresRefresh() {
			return this.requiresRefresh;
		}
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12329.java