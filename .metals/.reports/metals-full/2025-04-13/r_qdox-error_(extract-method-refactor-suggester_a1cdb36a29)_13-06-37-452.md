error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11624.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11624.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11624.java
text:
```scala
p@@ublic class HttpHeaderCollectionTest

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.request;

import java.util.Locale;
import java.util.Set;

import org.apache.wicket.util.time.Time;
import org.junit.Test;

import static org.junit.Assert.*;

public class HeadersCollectionTest
{
	@Test
	public void testHeaderCollection()
	{
		HttpHeaderCollection headers = new HttpHeaderCollection();
		assertTrue(headers.isEmpty());

		headers.addHeader("X-Test", "foo");
		headers.addHeader("X-Test", "bar");
		assertArrayEquals(new String[]{"foo", "bar"}, headers.getHeaderValues("X-Test"));

		headers.removeHeader("x-test");
		assertTrue(headers.isEmpty());

		headers.addHeader("   X-Image    ", "    jpeg     ");
		headers.addHeader("X-Image    ", "    gif     ");
		assertArrayEquals(new String[]{"jpeg", "gif"}, headers.getHeaderValues("X-IMAGE"));
		assertEquals(1, headers.getCount());

		headers.addHeader("X-Test", "123");
		assertEquals(2, headers.getCount());

		headers.removeHeader(" x-tesT ");
		assertEquals(1, headers.getCount());
	}

	@Test
	public void getHeaderNames()
	{
		final HttpHeaderCollection headers = new HttpHeaderCollection();

		headers.addHeader("key1", "a");
		headers.addHeader("Key1", "b");
		headers.addHeader("key2", "c");

		Set<String> names = headers.getHeaderNames();
		assertTrue(names.contains("key1"));
		assertFalse(names.contains("Key1"));
		assertTrue(names.contains("key2"));
	}

	@Test
	public void dateValues()
	{
		final HttpHeaderCollection headers = new HttpHeaderCollection();

		final Time time1 = Time.millis(1000000);
		final Time time2 = Time.millis(2000000);

		headers.setDateHeader("date", time1);
		headers.addDateHeader("date", time2);
		headers.addHeader("date", "not-a-date");

		assertEquals(time1, headers.getDateHeader("date"));
		assertEquals("Thu, 01 Jan 1970 00:16:40 GMT", headers.getHeader("date"));

		// a change of the locale must not affect the date format
		final Locale defaultLocale = Locale.getDefault();

		try
		{
			Locale.setDefault(Locale.CHINESE);
			assertEquals("Thu, 01 Jan 1970 00:16:40 GMT", headers.getHeader("date"));
		}
		finally
		{
			Locale.setDefault(defaultLocale);
		}

		assertArrayEquals(new String[]{"Thu, 01 Jan 1970 00:16:40 GMT", "Thu, 01 Jan 1970 00:33:20 GMT", "not-a-date"},
		                  headers.getHeaderValues("date"));
		
		headers.setHeader("date", "foobar");
		try
		{
			Time date = headers.getDateHeader("date");
			fail();
		}
		catch (IllegalStateException e)
		{
			// ok
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11624.java