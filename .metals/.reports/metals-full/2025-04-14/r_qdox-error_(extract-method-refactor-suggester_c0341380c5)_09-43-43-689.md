error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11212.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11212.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11212.java
text:
```scala
P@@roducesRequestCondition condition2 = new ProducesRequestCondition();

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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.Assert.*;

/**
 * @author Arjen Poutsma
 */
public class ProducesRequestConditionTests {

	@Test
	public void consumesMatch() {
		RequestCondition condition = new ProducesRequestCondition("text/plain");

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Accept", "text/plain");

		assertTrue(condition.match(request));
	}
	
	@Test
	public void negatedConsumesMatch() {
		RequestCondition condition = new ProducesRequestCondition("!text/plain");

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Accept", "text/plain");

		assertFalse(condition.match(request));
	}

	@Test
	public void consumesWildcardMatch() {
		RequestCondition condition = new ProducesRequestCondition("text/*");

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Accept", "text/plain");

		assertTrue(condition.match(request));
	}

	@Test
	public void consumesMultipleMatch() {
		RequestCondition condition = new ProducesRequestCondition("text/plain", "application/xml");

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Accept", "text/plain");

		assertTrue(condition.match(request));
	}

	@Test
	public void consumesSingleNoMatch() {
		RequestCondition condition = new ProducesRequestCondition("text/plain");

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Accept", "application/xml");

		assertFalse(condition.match(request));
	}

	@Test
	public void compareToSingle() {
		ProducesRequestCondition condition1 = new ProducesRequestCondition("text/plain");
		ProducesRequestCondition condition2 = new ProducesRequestCondition("text/*");

		List<MediaType> accept = Collections.singletonList(MediaType.TEXT_PLAIN);

		int result = condition1.compareTo(condition2, accept);
		assertTrue("Invalid comparison result: " + result, result < 0);

		result = condition2.compareTo(condition1, accept);
		assertTrue("Invalid comparison result: " + result, result > 0);
	}

	@Test
	public void compareToMultiple() {
		ProducesRequestCondition condition1 = new ProducesRequestCondition("*/*", "text/plain");
		ProducesRequestCondition condition2 = new ProducesRequestCondition("text/*", "text/plain;q=0.7");

		List<MediaType> accept = Collections.singletonList(MediaType.TEXT_PLAIN);

		int result = condition1.compareTo(condition2, accept);
		assertTrue("Invalid comparison result: " + result, result < 0);

		result = condition2.compareTo(condition1, accept);
		assertTrue("Invalid comparison result: " + result, result > 0);

		condition1 = new ProducesRequestCondition("*/*");
		condition2 = new ProducesRequestCondition("text/*");

		accept = Collections.singletonList(new MediaType("text", "*"));

		result = condition1.compareTo(condition2, accept);
		assertTrue("Invalid comparison result: " + result, result > 0);

		result = condition2.compareTo(condition1, accept);
		assertTrue("Invalid comparison result: " + result, result < 0);
	}

	@Test
	public void compareToMultipleAccept() {
		ProducesRequestCondition condition1 = new ProducesRequestCondition("text/*", "text/plain");
		ProducesRequestCondition condition2 = new ProducesRequestCondition("application/*", "application/xml");

		List<MediaType> accept = Arrays.asList(MediaType.TEXT_PLAIN, MediaType.APPLICATION_XML);

		int result = condition1.compareTo(condition2, accept);
		assertTrue("Invalid comparison result: " + result, result < 0);

		result = condition2.compareTo(condition1, accept);
		assertTrue("Invalid comparison result: " + result, result > 0);

		accept = Arrays.asList(MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN);

		result = condition1.compareTo(condition2, accept);
		assertTrue("Invalid comparison result: " + result, result > 0);

		result = condition2.compareTo(condition1, accept);
		assertTrue("Invalid comparison result: " + result, result < 0);
	}

	@Test
	public void combine() {
		ProducesRequestCondition condition1 = new ProducesRequestCondition("text/plain");
		ProducesRequestCondition condition2 = new ProducesRequestCondition("application/xml");

		ProducesRequestCondition result = condition1.combine(condition2);
		assertEquals(condition2, result);
	}
	
	@Test
	public void combineWithDefault() {
		ProducesRequestCondition condition1 = new ProducesRequestCondition("text/plain");
		ProducesRequestCondition condition2 = new ProducesRequestCondition("*/*");

		ProducesRequestCondition result = condition1.combine(condition2);
		assertEquals(condition1, result);
	}

	@Test
	public void parseConsumesAndHeaders() {
		String[] consumes = new String[] {"text/plain"};
		String[] headers = new String[]{"foo=bar", "accept=application/xml,application/pdf"};
		ProducesRequestCondition condition = RequestConditionFactory.parseProduces(consumes, headers);

		assertConditions(condition, "text/plain", "application/xml", "application/pdf");
	}

	@Test
	public void parseConsumesDefault() {
		String[] consumes = new String[] {"*/*"};
		String[] headers = new String[0];
		ProducesRequestCondition condition = RequestConditionFactory.parseProduces(consumes, headers);

		assertConditions(condition, "*/*");
	}
	@Test
	public void parseConsumesDefaultAndHeaders() {
		String[] consumes = new String[] {"*/*"};
		String[] headers = new String[]{"foo=bar", "accept=text/plain"};
		ProducesRequestCondition condition = RequestConditionFactory.parseProduces(consumes, headers);

		assertConditions(condition, "text/plain");
	}

	@Test
	public void getMatchingCondition() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Accept", "text/plain");

		ProducesRequestCondition condition = new ProducesRequestCondition("text/plain", "application/xml");

		ProducesRequestCondition result = condition.getMatchingCondition(request);
		assertConditions(result, "text/plain");

		condition = new ProducesRequestCondition("application/xml");

		result = condition.getMatchingCondition(request);
		assertNull(result);
	}

	private void assertConditions(ProducesRequestCondition condition, String... expected) {
		Set<ProducesRequestCondition.ProduceRequestCondition> conditions = condition.getConditions();
		assertEquals("Invalid amount of conditions", conditions.size(), expected.length);
		for (String s : expected) {
			boolean found = false;
			for (ProducesRequestCondition.ProduceRequestCondition requestCondition : conditions) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11212.java