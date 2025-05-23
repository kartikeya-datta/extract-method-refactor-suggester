error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11211.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11211.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11211.java
text:
```scala
C@@onsumesRequestCondition condition2 = new ConsumesRequestCondition();

/*
 * Copyright 2002-2011 the original author or authors.
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

package org.springframework.web.servlet.mvc.method.condition;

import java.util.Set;

import org.junit.Test;

import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.Assert.*;

/**
 * @author Arjen Poutsma
 */
public class ConsumesRequestConditionTests {

	@Test
	public void consumesMatch() {
		RequestCondition condition = new ConsumesRequestCondition("text/plain");

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setContentType("text/plain");

		assertTrue(condition.match(request));
	}
	
	@Test
	public void negatedConsumesMatch() {
		RequestCondition condition = new ConsumesRequestCondition("!text/plain");

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setContentType("text/plain");

		assertFalse(condition.match(request));
	}

	@Test
	public void consumesWildcardMatch() {
		RequestCondition condition = new ConsumesRequestCondition("text/*");

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setContentType("text/plain");

		assertTrue(condition.match(request));
	}

	@Test
	public void consumesMultipleMatch() {
		RequestCondition condition = new ConsumesRequestCondition("text/plain", "application/xml");

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setContentType("text/plain");

		assertTrue(condition.match(request));
	}

	@Test
	public void consumesSingleNoMatch() {
		RequestCondition condition = new ConsumesRequestCondition("text/plain");

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setContentType("application/xml");

		assertFalse(condition.match(request));
	}

	@Test
	public void compareToSingle() {
		ConsumesRequestCondition condition1 = new ConsumesRequestCondition("text/plain");
		ConsumesRequestCondition condition2 = new ConsumesRequestCondition("text/*");

		int result = condition1.compareTo(condition2);
		assertTrue("Invalid comparison result: " + result, result < 0);

		result = condition2.compareTo(condition1);
		assertTrue("Invalid comparison result: " + result, result > 0);
	}

	@Test
	public void compareToMultiple() {
		ConsumesRequestCondition condition1 = new ConsumesRequestCondition("*/*", "text/plain");
		ConsumesRequestCondition condition2 = new ConsumesRequestCondition("text/*", "text/plain;q=0.7");

		int result = condition1.compareTo(condition2);
		assertTrue("Invalid comparison result: " + result, result < 0);

		result = condition2.compareTo(condition1);
		assertTrue("Invalid comparison result: " + result, result > 0);
	}


	@Test
	public void combine() {
		ConsumesRequestCondition condition1 = new ConsumesRequestCondition("text/plain");
		ConsumesRequestCondition condition2 = new ConsumesRequestCondition("application/xml");

		ConsumesRequestCondition result = condition1.combine(condition2);
		assertEquals(condition2, result);
	}
	
	@Test
	public void combineWithDefault() {
		ConsumesRequestCondition condition1 = new ConsumesRequestCondition("text/plain");
		ConsumesRequestCondition condition2 = new ConsumesRequestCondition("*/*");

		ConsumesRequestCondition result = condition1.combine(condition2);
		assertEquals(condition1, result);
	}

	@Test
	public void parseConsumesAndHeaders() {
		String[] consumes = new String[] {"text/plain"};
		String[] headers = new String[]{"foo=bar", "content-type=application/xml,application/pdf"};
		ConsumesRequestCondition condition = RequestConditionFactory.parseConsumes(consumes, headers);

		assertConditions(condition, "text/plain", "application/xml", "application/pdf");
	}

	@Test
	public void parseConsumesDefault() {
		String[] consumes = new String[] {"*/*"};
		String[] headers = new String[0];
		ConsumesRequestCondition condition = RequestConditionFactory.parseConsumes(consumes, headers);

		assertConditions(condition, "*/*");
	}
	@Test
	public void parseConsumesDefaultAndHeaders() {
		String[] consumes = new String[] {"*/*"};
		String[] headers = new String[]{"foo=bar", "content-type=text/plain"};
		ConsumesRequestCondition condition = RequestConditionFactory.parseConsumes(consumes, headers);

		assertConditions(condition, "text/plain");
	}

	@Test
	public void getMatchingCondition() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setContentType("text/plain");

		ConsumesRequestCondition condition = new ConsumesRequestCondition("text/plain", "application/xml");

		ConsumesRequestCondition result = condition.getMatchingCondition(request);
		assertConditions(result, "text/plain");

		condition = new ConsumesRequestCondition("application/xml");

		result = condition.getMatchingCondition(request);
		assertNull(result);
	}

	private void assertConditions(ConsumesRequestCondition condition, String... expected) {
		Set<ConsumesRequestCondition.ConsumeRequestCondition> conditions = condition.getConditions();
		assertEquals("Invalid amount of conditions", conditions.size(), expected.length);
		for (String s : expected) {
			boolean found = false;
			for (ConsumesRequestCondition.ConsumeRequestCondition requestCondition : conditions) {
				String conditionMediaType = requestCondition.getMediaType().toString();
				if (conditionMediaType.equals(s)) {
					found = true;
					break;

				}
			}
			if (!found) {
				fail("Condition [" + s + "] not found");
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11211.java