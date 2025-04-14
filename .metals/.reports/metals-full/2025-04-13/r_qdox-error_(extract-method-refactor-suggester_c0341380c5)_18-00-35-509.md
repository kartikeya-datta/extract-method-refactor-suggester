error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11943.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11943.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11943.java
text:
```scala
U@@RI result = template.expand("Z\u00fcrich");

/*
 * Copyright 2002-2009 the original author or authors.
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

package org.springframework.web.util;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import org.junit.Test;

/** @author Arjen Poutsma */
public class UriTemplateTests {

	@Test
	public void getVariableNames() throws Exception {
		UriTemplate template = new UriTemplate("http://example.com/hotels/{hotel}/bookings/{booking}");
		List<String> variableNames = template.getVariableNames();
		assertEquals("Invalid variable names", Arrays.asList("hotel", "booking"), variableNames);
	}

	@Test
	public void expandVarArgs() throws Exception {
		UriTemplate template = new UriTemplate("http://example.com/hotels/{hotel}/bookings/{booking}");
		URI result = template.expand("1", "42");
		assertEquals("Invalid expanded template", new URI("http://example.com/hotels/1/bookings/42"), result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void expandVarArgsInvalidAmountVariables() throws Exception {
		UriTemplate template = new UriTemplate("http://example.com/hotels/{hotel}/bookings/{booking}");
		template.expand("1", "42", "100");
	}

	@Test
	public void expandMapDuplicateVariables() throws Exception {
		UriTemplate template = new UriTemplate("/order/{c}/{c}/{c}");
		assertEquals("Invalid variable names", Arrays.asList("c", "c", "c"), template.getVariableNames());
		URI result = template.expand(Collections.singletonMap("c", "cheeseburger"));
		assertEquals("Invalid expanded template", new URI("/order/cheeseburger/cheeseburger/cheeseburger"), result);
	}

	@Test
	public void expandMap() throws Exception {
		Map<String, String> uriVariables = new HashMap<String, String>(2);
		uriVariables.put("booking", "42");
		uriVariables.put("hotel", "1");
		UriTemplate template = new UriTemplate("http://example.com/hotels/{hotel}/bookings/{booking}");
		URI result = template.expand(uriVariables);
		assertEquals("Invalid expanded template", new URI("http://example.com/hotels/1/bookings/42"), result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void expandMapInvalidAmountVariables() throws Exception {
		UriTemplate template = new UriTemplate("http://example.com/hotels/{hotel}/bookings/{booking}");
		template.expand(Collections.singletonMap("hotel", "1"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void expandMapUnboundVariables() throws Exception {
		Map<String, String> uriVariables = new HashMap<String, String>(2);
		uriVariables.put("booking", "42");
		uriVariables.put("bar", "1");
		UriTemplate template = new UriTemplate("http://example.com/hotels/{hotel}/bookings/{booking}");
		template.expand(uriVariables);
	}

	@Test
	public void expandEncoded() throws Exception {
		UriTemplate template = new UriTemplate("http://example.com//hotel list/{hotel}");
		URI result = template.expand("Zrich");
		assertEquals("Invalid expanded template", new URI("http://example.com//hotel%20list/Z%FCrich"), result);
	}

	@Test
	public void matches() throws Exception {
		UriTemplate template = new UriTemplate("http://example.com/hotels/{hotel}/bookings/{booking}");
		assertTrue("UriTemplate does not match", template.matches("http://example.com/hotels/1/bookings/42"));
		assertFalse("UriTemplate matches", template.matches("http://example.com/hotels/bookings"));
		assertFalse("UriTemplate matches", template.matches(""));
		assertFalse("UriTemplate matches", template.matches(null));
	}

	@Test
	public void match() throws Exception {
		Map<String, String> expected = new HashMap<String, String>(2);
		expected.put("booking", "42");
		expected.put("hotel", "1");

		UriTemplate template = new UriTemplate("http://example.com/hotels/{hotel}/bookings/{booking}");
		Map<String, String> result = template.match("http://example.com/hotels/1/bookings/42");
		assertEquals("Invalid match", expected, result);
	}

	@Test
	public void matchDuplicate() throws Exception {
		UriTemplate template = new UriTemplate("/order/{c}/{c}/{c}");
		Map<String, String> result = template.match("/order/cheeseburger/cheeseburger/cheeseburger");
		Map<String, String> expected = Collections.singletonMap("c", "cheeseburger");
		assertEquals("Invalid match", expected, result);
	}

	@Test
	public void matchMultipleInOneSegment() throws Exception {
		UriTemplate template = new UriTemplate("/{foo}-{bar}");
		Map<String, String> result = template.match("/12-34");
		Map<String, String> expected = new HashMap<String, String>(2);
		expected.put("foo", "12");
		expected.put("bar", "34");
		assertEquals("Invalid match", expected, result);
	}

	@Test
	public void queryVariables() throws Exception {
		UriTemplate template = new UriTemplate("/search?q={query}");
		assertTrue(template.matches("/search?q=foo"));
	}

	@Test
	public void fragments() throws Exception {
		UriTemplate template = new UriTemplate("/search#{fragment}");
		assertTrue(template.matches("/search#foo"));

		template = new UriTemplate("/search?query={query}#{fragment}");
		assertTrue(template.matches("/search?query=foo#bar"));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11943.java