error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/951.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/951.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/951.java
text:
```scala
r@@eturn new AssignmentRequest(HasDateMethod.class, ParameterSignature

package org.junit.tests.experimental.theories.runner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.Each.each;
import static org.junit.matchers.StringContains.containsString;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.ParameterSignature;
import org.junit.experimental.theories.PotentialAssignment;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.experimental.theories.internal.AssignmentRequest;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

public class WithDataPointMethod {
	@RunWith(Theories.class)
	public static class HasDataPointMethod {
		@DataPoint
		public int oneHundred() {
			return 100;
		}

		@Theory
		public void allIntsOk(int x) {

		}
	}

	@RunWith(Theories.class)
	public static class HasUglyDataPointMethod {
		@DataPoint
		public int oneHundred() {
			return 100;
		}

		@DataPoint
		public int oneUglyHundred() {
			throw new RuntimeException();
		}

		@Theory
		public void allIntsOk(int x) {

		}
	}

	@Test
	public void pickUpDataPointMethods() {
		assertThat(failures(HasDataPointMethod.class), empty());
	}

	@Test
	public void ignoreExceptionsFromDataPointMethods() {
		assertThat(failures(HasUglyDataPointMethod.class), empty());
	}

	@RunWith(Theories.class)
	public static class DataPointMethodReturnsMutableObject {
		@DataPoint
		public List<Object> empty() {
			return new ArrayList<Object>();
		}

		public static int ONE= 1;

		public static int TWO= 2;

		@Theory
		public void everythingsEmpty(List<Object> first, int number) {
			assertThat(first.size(), is(0));
			first.add("a");
		}
	}

	@Test
	public void mutableObjectsAreCreatedAfresh() {
		assertThat(failures(DataPointMethodReturnsMutableObject.class), empty());
	}

	@RunWith(Theories.class)
	public static class HasDateMethod {
		@DataPoint
		public int oneHundred() {
			return 100;
		}

		public Date notADataPoint() {
			return new Date();
		}

		@Theory
		public void allIntsOk(int x) {

		}

		@Theory
		public void onlyStringsOk(String s) {

		}

		@Theory
		public void onlyDatesOk(Date d) {

		}
	}

	@Test
	public void ignoreDataPointMethodsWithWrongTypes() throws Exception {
		assertThat(potentialValues(
				HasDateMethod.class.getMethod("onlyStringsOk", String.class))
				.toString(), not(containsString("100")));
	}

	@Test
	public void ignoreDataPointMethodsWithoutAnnotation() throws Throwable {
		assertThat(potentialValues(
				HasDateMethod.class.getMethod("onlyDatesOk", Date.class))
				.size(), is(0));
	}

	private List<PotentialAssignment> potentialValues(Method method)
			throws Exception {
		return new AssignmentRequest(new HasDateMethod(), ParameterSignature
				.signatures(method).get(0)).getPotentialAssignments();
	}

	private List<Failure> failures(Class<?> type) {
		return JUnitCore.runClasses(type).getFailures();
	}

	private Matcher<Iterable<Failure>> empty() {
		Matcher<Failure> nullValue= nullValue();
		return each(nullValue);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/951.java