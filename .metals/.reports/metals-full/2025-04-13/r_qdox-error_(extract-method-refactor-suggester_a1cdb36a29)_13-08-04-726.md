error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6088.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6088.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6088.java
text:
```scala
a@@ssertEquals("a expected:<b> but was:<c>", failure.getMessage());

package junit.tests.framework;

import junit.framework.*;

public class AssertTest extends TestCase {
	/**
		 * Test for the special Double.NaN value.
		 */
	public void testAssertEqualsNaNFails() {
		try {
			assertEquals(1.234, Double.NaN, 0.0);
		} catch (AssertionFailedError e) {
			return;
		}
		fail();
	}

	public void testAssertNaNEqualsFails() {
		try {
			assertEquals(Double.NaN, 1.234, 0.0);
		} catch (AssertionFailedError e) {
			return;
		}
		fail();
	}

	public void testAssertNaNEqualsNaNFails() {
		try {
			assertEquals(Double.NaN, Double.NaN, 0.0);
		} catch (AssertionFailedError e) {
			return;
		}
		fail();
	}

	public void testAssertPosInfinityNotEqualsNegInfinity() {
		try {
			assertEquals(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 0.0);
		} catch (AssertionFailedError e) {
			return;
		}
		fail();
	}

	public void testAssertPosInfinityNotEquals() {
		try {
			assertEquals(Double.POSITIVE_INFINITY, 1.23, 0.0);
		} catch (AssertionFailedError e) {
			return;
		}
		fail();
	}

	public void testAssertPosInfinityEqualsInfinity() {
		assertEquals(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 0.0);
	}

	public void testAssertNegInfinityEqualsInfinity() {
		assertEquals(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 0.0);
	}

	public void testAssertEquals() {
		Object o= new Object();
		assertEquals(o, o);
	}

	public void testAssertEqualsNull() {
		assertEquals(null, null);
	}

	public void testAssertNull() {
		assertNull(null);
	}

	public void testAssertStringEquals() {
		assertEquals("a", "a");
	}

	public void testAssertNullNotEqualsString() {
		try {
			assertEquals(null, "foo");
		} catch (ComparisonFailure e) {
			return;
		}
		fail();
	}

	public void testAssertStringNotEqualsNull() {
		try {
			assertEquals("foo", null);
		} catch (ComparisonFailure e) {
			e.getMessage();
			return;
		}
		fail();
	}

	public void testAssertNullNotEqualsNull() {
		try {
			assertEquals(null, new Object());
		} catch (AssertionFailedError e) {
			e.getMessage();
			return;
		}
		fail();
	}

	public void testAssertSame() {
		Object o= new Object();
		assertSame(o, o);
	}

	public void testAssertSameFails() {
		try {
			assertSame(new Integer(1), new Integer(1));
		} catch (AssertionFailedError e) {
			return;
		}
		fail();
	}

	public void testFail() {
		try {
			fail();
		} catch (AssertionFailedError e) {
			return;
		}
		throw new AssertionFailedError(); // You can't call fail() here
	}

	public void testFailAssertNotNull() {
		try {
			assertNotNull(null);
		} catch (AssertionFailedError e) {
			return;
		}
		fail();
	}

	public void testSucceedAssertNotNull() {
		assertNotNull(new Object());
	}

	public void testAssertFalse() {
		assertFalse(false);
		try {
			assertFalse(true);
		} catch (AssertionFailedError e) {
			return;
		}
		fail();
	}

	public void testAssertNotSame() {
		assertNotSame(new Integer(1), null);
		assertNotSame(null, new Integer(1));
		assertNotSame(new Integer(1), new Integer(1));
	}

	public void testAssertNotSameFails() {
		try {
			Integer obj= new Integer(1);
			assertNotSame(obj, obj);
		} catch (AssertionFailedError e) {
			return;
		}
		fail();
	}

	public void testAssertNotSameFailsNull() {
		try {
			assertNotSame(null, null);
		} catch (AssertionFailedError e) {
			return;
		}
		fail();
	}

	public void testComparisonErrorMessage() {
		ComparisonFailure failure= new ComparisonFailure("a", "b", "c");
		assertEquals("a: expected:<b> but was:<c>", failure.getMessage());
	}

	public void testComparisonErrorStartSame() {
		ComparisonFailure failure= new ComparisonFailure(null, "ba", "bc");
		assertEquals("expected:<...a> but was:<...c>", failure.getMessage());
	}

	public void testComparisonErrorEndSame() {
		ComparisonFailure failure= new ComparisonFailure(null, "ab", "cb");
		assertEquals("expected:<a...> but was:<c...>", failure.getMessage());
	}

	public void testComparisonErrorSame() {
		ComparisonFailure failure= new ComparisonFailure(null, "ab", "ab");
		assertEquals("expected:<ab> but was:<ab>", failure.getMessage());
	}

	public void testComparisonErrorStartAndEndSame() {
		ComparisonFailure failure= new ComparisonFailure(null, "abc", "adc");
		assertEquals("expected:<...b...> but was:<...d...>", failure.getMessage());
	}

	public void testComparisonErrorStartSameComplete() {
		ComparisonFailure failure= new ComparisonFailure(null, "ab", "abc");
		assertEquals("expected:<...> but was:<...c>", failure.getMessage());
	}

	public void testComparisonErrorEndSameComplete() {
		ComparisonFailure failure= new ComparisonFailure(null, "bc", "abc");
		assertEquals("expected:<...> but was:<a...>", failure.getMessage());
	}

	public void testComparisonErrorOverlapingMatches() {
		ComparisonFailure failure= new ComparisonFailure(null, "abc", "abbc");
		assertEquals("expected:<......> but was:<...b...>", failure.getMessage());
	}

	public void testComparisonErrorOverlapingMatches2() {
		ComparisonFailure failure= new ComparisonFailure(null, "abcdde", "abcde");
		assertEquals("expected:<...d...> but was:<......>", failure.getMessage());
	}

	public void testComparisonErrorWithActualNull() {
		ComparisonFailure failure= new ComparisonFailure(null, "a", null);
		assertEquals("expected:<a> but was:<null>", failure.getMessage());
	}
	
	public void testComparisonErrorWithExpectedNull() {
		ComparisonFailure failure= new ComparisonFailure(null, null, "a");
		assertEquals("expected:<null> but was:<a>", failure.getMessage());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6088.java