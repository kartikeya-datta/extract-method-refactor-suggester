error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12377.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12377.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12377.java
text:
```scala
a@@ssertNull(sources.remove("a"));

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

package org.springframework.core.env;

import org.junit.Test;

import org.springframework.mock.env.MockPropertySource;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author Chris Beams
 * @author Juergen Hoeller
 */
public class MutablePropertySourcesTests {

	@Test
	public void test() {
		MutablePropertySources sources = new MutablePropertySources();
		sources.addLast(new MockPropertySource("b").withProperty("p1", "bValue"));
		sources.addLast(new MockPropertySource("d").withProperty("p1", "dValue"));
		sources.addLast(new MockPropertySource("f").withProperty("p1", "fValue"));

		assertThat(sources.size(), equalTo(3));
		assertThat(sources.contains("a"), is(false));
		assertThat(sources.contains("b"), is(true));
		assertThat(sources.contains("c"), is(false));
		assertThat(sources.contains("d"), is(true));
		assertThat(sources.contains("e"), is(false));
		assertThat(sources.contains("f"), is(true));
		assertThat(sources.contains("g"), is(false));

		assertThat(sources.get("b"), not(nullValue()));
		assertThat(sources.get("b").getProperty("p1"), equalTo((Object)"bValue"));
		assertThat(sources.get("d"), not(nullValue()));
		assertThat(sources.get("d").getProperty("p1"), equalTo((Object)"dValue"));

		sources.addBefore("b", new MockPropertySource("a"));
		sources.addAfter("b", new MockPropertySource("c"));

		assertThat(sources.size(), equalTo(5));
		assertThat(sources.precedenceOf(PropertySource.named("a")), is(0));
		assertThat(sources.precedenceOf(PropertySource.named("b")), is(1));
		assertThat(sources.precedenceOf(PropertySource.named("c")), is(2));
		assertThat(sources.precedenceOf(PropertySource.named("d")), is(3));
		assertThat(sources.precedenceOf(PropertySource.named("f")), is(4));

		sources.addBefore("f", new MockPropertySource("e"));
		sources.addAfter("f", new MockPropertySource("g"));

		assertThat(sources.size(), equalTo(7));
		assertThat(sources.precedenceOf(PropertySource.named("a")), is(0));
		assertThat(sources.precedenceOf(PropertySource.named("b")), is(1));
		assertThat(sources.precedenceOf(PropertySource.named("c")), is(2));
		assertThat(sources.precedenceOf(PropertySource.named("d")), is(3));
		assertThat(sources.precedenceOf(PropertySource.named("e")), is(4));
		assertThat(sources.precedenceOf(PropertySource.named("f")), is(5));
		assertThat(sources.precedenceOf(PropertySource.named("g")), is(6));

		sources.addLast(new MockPropertySource("a"));
		assertThat(sources.size(), equalTo(7));
		assertThat(sources.precedenceOf(PropertySource.named("b")), is(0));
		assertThat(sources.precedenceOf(PropertySource.named("c")), is(1));
		assertThat(sources.precedenceOf(PropertySource.named("d")), is(2));
		assertThat(sources.precedenceOf(PropertySource.named("e")), is(3));
		assertThat(sources.precedenceOf(PropertySource.named("f")), is(4));
		assertThat(sources.precedenceOf(PropertySource.named("g")), is(5));
		assertThat(sources.precedenceOf(PropertySource.named("a")), is(6));

		sources.addFirst(new MockPropertySource("a"));
		assertThat(sources.size(), equalTo(7));
		assertThat(sources.precedenceOf(PropertySource.named("a")), is(0));
		assertThat(sources.precedenceOf(PropertySource.named("b")), is(1));
		assertThat(sources.precedenceOf(PropertySource.named("c")), is(2));
		assertThat(sources.precedenceOf(PropertySource.named("d")), is(3));
		assertThat(sources.precedenceOf(PropertySource.named("e")), is(4));
		assertThat(sources.precedenceOf(PropertySource.named("f")), is(5));
		assertThat(sources.precedenceOf(PropertySource.named("g")), is(6));

		assertEquals(sources.remove("a"), PropertySource.named("a"));
		assertThat(sources.size(), equalTo(6));
		assertThat(sources.contains("a"), is(false));

		assertEquals(sources.remove("a"), null);
		assertThat(sources.size(), equalTo(6));

		String bogusPS = "bogus";
		try {
			sources.addAfter(bogusPS, new MockPropertySource("h"));
			fail("expected non-existent PropertySource exception");
		}
		catch (IllegalArgumentException ex) {
			assertTrue(ex.getMessage().contains("does not exist"));
		}

		sources.addFirst(new MockPropertySource("a"));
		assertThat(sources.size(), equalTo(7));
		assertThat(sources.precedenceOf(PropertySource.named("a")), is(0));
		assertThat(sources.precedenceOf(PropertySource.named("b")), is(1));
		assertThat(sources.precedenceOf(PropertySource.named("c")), is(2));

		sources.replace("a", new MockPropertySource("a-replaced"));
		assertThat(sources.size(), equalTo(7));
		assertThat(sources.precedenceOf(PropertySource.named("a-replaced")), is(0));
		assertThat(sources.precedenceOf(PropertySource.named("b")), is(1));
		assertThat(sources.precedenceOf(PropertySource.named("c")), is(2));

		sources.replace("a-replaced", new MockPropertySource("a"));

		try {
			sources.replace(bogusPS, new MockPropertySource("bogus-replaced"));
			fail("expected non-existent PropertySource exception");
		}
		catch (IllegalArgumentException ex) {
			assertTrue(ex.getMessage().contains("does not exist"));
		}

		try {
			sources.addBefore("b", new MockPropertySource("b"));
			fail("expected exception");
		}
		catch (IllegalArgumentException ex) {
			assertTrue(ex.getMessage().contains("cannot be added relative to itself"));
		}

		try {
			sources.addAfter("b", new MockPropertySource("b"));
			fail("expected exception");
		}
		catch (IllegalArgumentException ex) {
			assertTrue(ex.getMessage().contains("cannot be added relative to itself"));
		}
	}

	@Test
	public void getNonExistentPropertySourceReturnsNull() {
		MutablePropertySources sources = new MutablePropertySources();
		assertThat(sources.get("bogus"), nullValue());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12377.java