error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10550.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10550.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10550.java
text:
```scala
a@@ssertEquals("Num registered TELs for DefaultListenersExampleTestCase.", 4,

/*
 * Copyright 2002-2014 the original author or authors.
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

package org.springframework.test.context;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.junit.Test;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import static org.junit.Assert.*;

/**
 * <p>
 * JUnit 4 based unit test for the {@link TestExecutionListeners
 * &#064;TestExecutionListeners} annotation, which verifies:
 * </p>
 * <ul>
 * <li>Proper registering of {@link TestExecutionListener listeners} in
 * conjunction with a {@link TestContextManager}</li>
 * <li><em>Inherited</em> functionality proposed in <a
 * href="http://opensource.atlassian.com/projects/spring/browse/SPR-3896"
 * target="_blank">SPR-3896</a></li>
 * </ul>
 *
 * @author Sam Brannen
 * @since 2.5
 */
public class TestExecutionListenersTests {

	@Test
	public void verifyNumDefaultListenersRegistered() throws Exception {
		TestContextManager testContextManager = new TestContextManager(DefaultListenersExampleTestCase.class);
		assertEquals("Num registered TELs for DefaultListenersExampleTestCase.", 3,
			testContextManager.getTestExecutionListeners().size());
	}

	@Test
	public void verifyNumNonInheritedDefaultListenersRegistered() throws Exception {
		TestContextManager testContextManager = new TestContextManager(
			NonInheritedDefaultListenersExampleTestCase.class);
		assertEquals("Num registered TELs for NonInheritedDefaultListenersExampleTestCase.", 1,
			testContextManager.getTestExecutionListeners().size());
	}

	@Test
	public void verifyNumInheritedDefaultListenersRegistered() throws Exception {
		TestContextManager testContextManager = new TestContextManager(InheritedDefaultListenersExampleTestCase.class);
		assertEquals("Num registered TELs for InheritedDefaultListenersExampleTestCase.", 1,
			testContextManager.getTestExecutionListeners().size());

		testContextManager = new TestContextManager(SubInheritedDefaultListenersExampleTestCase.class);
		assertEquals("Num registered TELs for SubInheritedDefaultListenersExampleTestCase.", 1,
			testContextManager.getTestExecutionListeners().size());

		testContextManager = new TestContextManager(SubSubInheritedDefaultListenersExampleTestCase.class);
		assertEquals("Num registered TELs for SubSubInheritedDefaultListenersExampleTestCase.", 2,
			testContextManager.getTestExecutionListeners().size());
	}

	@Test
	public void verifyNumListenersRegistered() throws Exception {
		TestContextManager testContextManager = new TestContextManager(ExampleTestCase.class);
		assertEquals("Num registered TELs for ExampleTestCase.", 3,
			testContextManager.getTestExecutionListeners().size());
	}

	@Test
	public void verifyNumNonInheritedListenersRegistered() throws Exception {
		TestContextManager testContextManager = new TestContextManager(NonInheritedListenersExampleTestCase.class);
		assertEquals("Num registered TELs for NonInheritedListenersExampleTestCase.", 1,
			testContextManager.getTestExecutionListeners().size());
	}

	@Test
	public void verifyNumInheritedListenersRegistered() throws Exception {
		TestContextManager testContextManager = new TestContextManager(InheritedListenersExampleTestCase.class);
		assertEquals("Num registered TELs for InheritedListenersExampleTestCase.", 4,
			testContextManager.getTestExecutionListeners().size());
	}

	@Test
	public void verifyNumListenersRegisteredViaMetaAnnotation() throws Exception {
		TestContextManager testContextManager = new TestContextManager(MetaExampleTestCase.class);
		assertEquals("Num registered TELs for MetaExampleTestCase.", 3,
			testContextManager.getTestExecutionListeners().size());
	}

	@Test
	public void verifyNumNonInheritedListenersRegisteredViaMetaAnnotation() throws Exception {
		TestContextManager testContextManager = new TestContextManager(MetaNonInheritedListenersExampleTestCase.class);
		assertEquals("Num registered TELs for MetaNonInheritedListenersExampleTestCase.", 1,
			testContextManager.getTestExecutionListeners().size());
	}

	@Test
	public void verifyNumInheritedListenersRegisteredViaMetaAnnotation() throws Exception {
		TestContextManager testContextManager = new TestContextManager(MetaInheritedListenersExampleTestCase.class);
		assertEquals("Num registered TELs for MetaInheritedListenersExampleTestCase.", 4,
			testContextManager.getTestExecutionListeners().size());
	}

	@Test
	public void verifyNumListenersRegisteredViaMetaAnnotationWithOverrides() throws Exception {
		TestContextManager testContextManager = new TestContextManager(MetaWithOverridesExampleTestCase.class);
		assertEquals("Num registered TELs for MetaWithOverridesExampleTestCase.", 3,
			testContextManager.getTestExecutionListeners().size());
	}

	@Test
	public void verifyNumListenersRegisteredViaMetaAnnotationWithInheritedListenersWithOverrides() throws Exception {
		TestContextManager testContextManager = new TestContextManager(
			MetaInheritedListenersWithOverridesExampleTestCase.class);
		assertEquals("Num registered TELs for MetaInheritedListenersWithOverridesExampleTestCase.", 5,
			testContextManager.getTestExecutionListeners().size());
	}

	@Test
	public void verifyNumListenersRegisteredViaMetaAnnotationWithNonInheritedListenersWithOverrides() throws Exception {
		TestContextManager testContextManager = new TestContextManager(
			MetaNonInheritedListenersWithOverridesExampleTestCase.class);
		assertEquals("Num registered TELs for MetaNonInheritedListenersWithOverridesExampleTestCase.", 8,
			testContextManager.getTestExecutionListeners().size());
	}

	@Test(expected = IllegalStateException.class)
	public void verifyDuplicateListenersConfigThrowsException() throws Exception {
		new TestContextManager(DuplicateListenersConfigExampleTestCase.class);
	}


	static class DefaultListenersExampleTestCase {
	}

	@TestExecutionListeners(QuuxTestExecutionListener.class)
	static class InheritedDefaultListenersExampleTestCase extends DefaultListenersExampleTestCase {
	}

	static class SubInheritedDefaultListenersExampleTestCase extends InheritedDefaultListenersExampleTestCase {
	}

	@TestExecutionListeners(EnigmaTestExecutionListener.class)
	static class SubSubInheritedDefaultListenersExampleTestCase extends SubInheritedDefaultListenersExampleTestCase {
	}

	@TestExecutionListeners(listeners = { QuuxTestExecutionListener.class }, inheritListeners = false)
	static class NonInheritedDefaultListenersExampleTestCase extends InheritedDefaultListenersExampleTestCase {
	}

	@TestExecutionListeners({ FooTestExecutionListener.class, BarTestExecutionListener.class,
		BazTestExecutionListener.class })
	static class ExampleTestCase {
	}

	@TestExecutionListeners(QuuxTestExecutionListener.class)
	static class InheritedListenersExampleTestCase extends ExampleTestCase {
	}

	@TestExecutionListeners(listeners = QuuxTestExecutionListener.class, inheritListeners = false)
	static class NonInheritedListenersExampleTestCase extends InheritedListenersExampleTestCase {
	}

	@TestExecutionListeners(listeners = FooTestExecutionListener.class, value = BarTestExecutionListener.class)
	static class DuplicateListenersConfigExampleTestCase {
	}

	@TestExecutionListeners({//
	FooTestExecutionListener.class,//
		BarTestExecutionListener.class,//
		BazTestExecutionListener.class //
	})
	@Retention(RetentionPolicy.RUNTIME)
	static @interface MetaListeners {
	}

	@TestExecutionListeners(QuuxTestExecutionListener.class)
	@Retention(RetentionPolicy.RUNTIME)
	static @interface MetaInheritedListeners {
	}

	@TestExecutionListeners(listeners = QuuxTestExecutionListener.class, inheritListeners = false)
	@Retention(RetentionPolicy.RUNTIME)
	static @interface MetaNonInheritedListeners {
	}

	@TestExecutionListeners
	@Retention(RetentionPolicy.RUNTIME)
	static @interface MetaListenersWithOverrides {

		Class<? extends TestExecutionListener>[] listeners() default { FooTestExecutionListener.class,
			BarTestExecutionListener.class };
	}

	@TestExecutionListeners
	@Retention(RetentionPolicy.RUNTIME)
	static @interface MetaInheritedListenersWithOverrides {

		Class<? extends TestExecutionListener>[] listeners() default QuuxTestExecutionListener.class;

		boolean inheritListeners() default true;
	}

	@TestExecutionListeners
	@Retention(RetentionPolicy.RUNTIME)
	static @interface MetaNonInheritedListenersWithOverrides {

		Class<? extends TestExecutionListener>[] listeners() default QuuxTestExecutionListener.class;

		boolean inheritListeners() default false;
	}

	@MetaListeners
	static class MetaExampleTestCase {
	}

	@MetaInheritedListeners
	static class MetaInheritedListenersExampleTestCase extends MetaExampleTestCase {
	}

	@MetaNonInheritedListeners
	static class MetaNonInheritedListenersExampleTestCase extends MetaInheritedListenersExampleTestCase {
	}

	@MetaListenersWithOverrides(listeners = {//
	FooTestExecutionListener.class,//
		BarTestExecutionListener.class,//
		BazTestExecutionListener.class //
	})
	static class MetaWithOverridesExampleTestCase {
	}

	@MetaInheritedListenersWithOverrides(listeners = { FooTestExecutionListener.class, BarTestExecutionListener.class })
	static class MetaInheritedListenersWithOverridesExampleTestCase extends MetaWithOverridesExampleTestCase {
	}

	@MetaNonInheritedListenersWithOverrides(listeners = {//
	FooTestExecutionListener.class,//
		BarTestExecutionListener.class,//
		BazTestExecutionListener.class //
	},//
	inheritListeners = true)
	static class MetaNonInheritedListenersWithOverridesExampleTestCase extends
			MetaInheritedListenersWithOverridesExampleTestCase {
	}

	static class FooTestExecutionListener extends AbstractTestExecutionListener {
	}

	static class BarTestExecutionListener extends AbstractTestExecutionListener {
	}

	static class BazTestExecutionListener extends AbstractTestExecutionListener {
	}

	static class QuuxTestExecutionListener extends AbstractTestExecutionListener {
	}

	static class EnigmaTestExecutionListener extends AbstractTestExecutionListener {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10550.java