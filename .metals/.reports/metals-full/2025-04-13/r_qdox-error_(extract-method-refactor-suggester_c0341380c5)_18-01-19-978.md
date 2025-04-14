error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11550.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11550.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11550.java
text:
```scala
p@@ublic static class TestPage extends WebPage

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
package org.apache.wicket.request.target.coding;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.apache.wicket.util.tester.WicketTester;
import org.apache.wicket.util.value.ValueMap;

/**
 * Tests for {@link MixedParamUrlCodingStrategy}.
 * 
 * @author erik.van.oosten
 */
public class MixedParamUrlCodingStrategyTest extends TestCase
{
	/**
	 * Test class.
	 * 
	 * @author erik.van.oosten
	 */
	public static class TestPage extends WebPage<Void>
	{
		private static final long serialVersionUID = 1L;
		// EMPTY
	}

	private WicketTester tester;

	/**
	 * Test method for
	 * {@link MixedParamUrlCodingStrategy#appendParameters(org.apache.wicket.util.string.AppendingStringBuffer, java.util.Map)}.
	 */
	public void testAppendParametersAppendingStringBufferMap1()
	{
		Map parameters = new HashMap();
		parameters.put("a", "1");
		parameters.put("d", "4");
		parameters.put("e", "5");

		String[] parameterNames = new String[] { "a", "b", "c" };
		MixedParamUrlCodingStrategy npucs = new MixedParamUrlCodingStrategy("mnt", TestPage.class,
				parameterNames);

		AppendingStringBuffer url = new AppendingStringBuffer(40);
		npucs.appendParameters(url, parameters);
		String urlStr = url.toString();
		assertTrue("/1/?d=4&e=5".equals(urlStr) || "/1/?e=5&d=4".equals(urlStr));
	}

	/**
	 * Test method for
	 * {@link MixedParamUrlCodingStrategy#appendParameters(org.apache.wicket.util.string.AppendingStringBuffer, java.util.Map)}.
	 */
	public void testAppendParametersAppendingStringBufferMap2()
	{
		Map parameters = new HashMap();
		parameters.put("a", "1");
		parameters.put("b", "2");
		parameters.put("c", "3");
		parameters.put("d", "4");
		parameters.put("e", "5");

		String[] parameterNames = new String[] { "a", "b", "c" };
		MixedParamUrlCodingStrategy npucs = new MixedParamUrlCodingStrategy("mnt", TestPage.class,
				parameterNames);

		AppendingStringBuffer url = new AppendingStringBuffer(40);
		npucs.appendParameters(url, parameters);
		String urlStr = url.toString();
		assertTrue("/1/2/3/?d=4&e=5".equals(urlStr) || "/1/2/3/?e=5&d=4".equals(urlStr));
	}

	/**
	 * Test method for
	 * {@link MixedParamUrlCodingStrategy#appendParameters(org.apache.wicket.util.string.AppendingStringBuffer, java.util.Map)}.
	 */
	public void testAppendParametersAppendingStringBufferMap3()
	{
		Map parameters = new HashMap();
		parameters.put("a", "1");
		parameters.put("b", "2");
		parameters.put("c", "3");
		parameters.put("d", "4");
		parameters.put("e", "5");

		String[] parameterNames = new String[] {};
		MixedParamUrlCodingStrategy npucs = new MixedParamUrlCodingStrategy("mnt", TestPage.class,
				parameterNames);

		AppendingStringBuffer url = new AppendingStringBuffer(40);
		npucs.appendParameters(url, parameters);
		String urlStr = url.toString();
		assertEquals(21, urlStr.length());
		assertTrue(urlStr.indexOf("a=1") != -1);
		assertTrue(urlStr.indexOf("b=2") != -1);
		assertTrue(urlStr.indexOf("c=3") != -1);
		assertTrue(urlStr.indexOf("d=4") != -1);
		assertTrue(urlStr.indexOf("e=5") != -1);
		assertTrue(urlStr.matches("^/\\?([abcde]=[12345]&){4}([abcde]=[12345])$"));
	}

	/**
	 * Test method for
	 * {@link MixedParamUrlCodingStrategy#appendParameters(org.apache.wicket.util.string.AppendingStringBuffer, java.util.Map)}.
	 */
	public void testAppendParametersAppendingStringBufferMap4()
	{
		Map parameters = new HashMap();

		String[] parameterNames = new String[] { "a", "b" };
		MixedParamUrlCodingStrategy npucs = new MixedParamUrlCodingStrategy("mnt", TestPage.class,
				parameterNames);

		AppendingStringBuffer url = new AppendingStringBuffer(40);
		npucs.appendParameters(url, parameters);
		assertEquals("/", url.toString());
	}

	/**
	 * Test method for
	 * {@link MixedParamUrlCodingStrategy#appendParameters(org.apache.wicket.util.string.AppendingStringBuffer, java.util.Map)}.
	 */
	public void testAppendParametersAppendingStringBufferMap5()
	{
		Map parameters = new HashMap();
		parameters.put("a", "1");
		parameters.put("b", "2");
		parameters.put("c", "3");

		String[] parameterNames = new String[] { "a", "b", "c" };
		MixedParamUrlCodingStrategy npucs = new MixedParamUrlCodingStrategy("mnt", TestPage.class,
				parameterNames);

		AppendingStringBuffer url = new AppendingStringBuffer(40);
		npucs.appendParameters(url, parameters);
		assertEquals("/1/2/3/", url.toString());
	}

	/**
	 * Test method for
	 * {@link MixedParamUrlCodingStrategy#appendParameters(org.apache.wicket.util.string.AppendingStringBuffer, java.util.Map)}.
	 */
	public void testAppendParametersAppendingStringBufferMap6()
	{
		Map parameters = new HashMap();
		parameters.put("a", "1");
		parameters.put("c", "3");
		parameters.put("d", "4");
		parameters.put("e", "5");

		String[] parameterNames = new String[] { "a", "b", "c" };
		MixedParamUrlCodingStrategy npucs = new MixedParamUrlCodingStrategy("mnt", TestPage.class,
				parameterNames);

		AppendingStringBuffer url = new AppendingStringBuffer(40);
		npucs.appendParameters(url, parameters);
		String urlStr = url.toString();
		assertTrue("/1//3/?d=4&e=5".equals(urlStr) || "/1//3/?e=5&d=4".equals(urlStr));
	}

	/**
	 * Test method for
	 * {@link MixedParamUrlCodingStrategy#decodeParameters(java.lang.String, java.util.Map)}.
	 */
	public void testDecodeParametersStringMap1()
	{
		String[] parameterNames = new String[] { "a", "b", "c" };
		MixedParamUrlCodingStrategy npucs = new MixedParamUrlCodingStrategy("mnt", TestPage.class,
				parameterNames);

		Map urlMap = new HashMap();
		urlMap.put("d", "4");
		urlMap.put("e", "5");

		ValueMap parameterMap = npucs.decodeParameters("/1", urlMap);
		assertEquals(3, parameterMap.size());
		assertContains(parameterMap, "a", "1");
		assertContains(parameterMap, "d", "4");
		assertContains(parameterMap, "e", "5");
	}

	/**
	 * Test method for
	 * {@link MixedParamUrlCodingStrategy#decodeParameters(java.lang.String, java.util.Map)}.
	 */
	public void testDecodeParametersStringMap2()
	{
		String[] parameterNames = new String[] { "a", "b", "c" };
		MixedParamUrlCodingStrategy npucs = new MixedParamUrlCodingStrategy("mnt", TestPage.class,
				parameterNames);

		Map urlMap = new HashMap();
		urlMap.put("d", "4");
		urlMap.put("e", "5");

		ValueMap parameterMap = npucs.decodeParameters("/%C3%A8/2/3", urlMap);
		assertEquals(5, parameterMap.size());
		assertContains(parameterMap, "a", "\u00e8");
		assertContains(parameterMap, "b", "2");
		assertContains(parameterMap, "c", "3");
		assertContains(parameterMap, "d", "4");
		assertContains(parameterMap, "e", "5");
	}

	/**
	 * Test method for
	 * {@link MixedParamUrlCodingStrategy#decodeParameters(java.lang.String, java.util.Map)}.
	 */
	public void testDecodeParametersStringMap3()
	{
		String[] parameterNames = new String[] {};
		MixedParamUrlCodingStrategy npucs = new MixedParamUrlCodingStrategy("mnt", TestPage.class,
				parameterNames);

		Map urlMap = new HashMap();
		urlMap.put("a", "1");
		urlMap.put("b", "2");
		urlMap.put("c", "3");
		urlMap.put("d", "4");
		urlMap.put("e", "5");

		ValueMap parameterMap = npucs.decodeParameters("", urlMap);
		assertEquals(5, parameterMap.size());
		assertContains(parameterMap, "a", "1");
		assertContains(parameterMap, "b", "2");
		assertContains(parameterMap, "c", "3");
		assertContains(parameterMap, "d", "4");
		assertContains(parameterMap, "e", "5");
	}

	/**
	 * Test method for
	 * {@link MixedParamUrlCodingStrategy#decodeParameters(java.lang.String, java.util.Map)}.
	 */
	public void testDecodeParametersStringMap4()
	{
		String[] parameterNames = new String[] { "a", "b" };
		MixedParamUrlCodingStrategy npucs = new MixedParamUrlCodingStrategy("mnt", TestPage.class,
				parameterNames);

		Map urlMap = new HashMap();

		ValueMap parameterMap = npucs.decodeParameters("", urlMap);
		assertEquals(0, parameterMap.size());
	}

	/**
	 * Test method for
	 * {@link MixedParamUrlCodingStrategy#decodeParameters(java.lang.String, java.util.Map)}.
	 */
	public void testDecodeParametersStringMap5()
	{
		String[] parameterNames = new String[] { "a", "b", "c" };
		MixedParamUrlCodingStrategy npucs = new MixedParamUrlCodingStrategy("mnt", TestPage.class,
				parameterNames);

		Map urlMap = new HashMap();

		ValueMap parameterMap = npucs.decodeParameters("/1/2/3", urlMap);
		assertEquals(3, parameterMap.size());
		assertContains(parameterMap, "a", "1");
		assertContains(parameterMap, "b", "2");
		assertContains(parameterMap, "c", "3");
	}

	/**
	 * Test method for
	 * {@link MixedParamUrlCodingStrategy#decodeParameters(java.lang.String, java.util.Map)}.
	 */
	public void testDecodeParametersStringMap6()
	{
		String[] parameterNames = new String[] { "a", "b", "c" };
		MixedParamUrlCodingStrategy npucs = new MixedParamUrlCodingStrategy("mnt", TestPage.class,
				parameterNames);

		// Note nasty, but ignored c parameter
		Map urlMap = new HashMap();
		urlMap.put("c", "XXXXXXX");
		urlMap.put("d", "4");
		urlMap.put("e", "5");

		ValueMap parameterMap = npucs.decodeParameters("/1//3", urlMap);
		assertEquals(5, parameterMap.size());
		assertContains(parameterMap, "a", "1");
		// Note: missing b is translated to empty string.
		assertContains(parameterMap, "b", "");
		assertContains(parameterMap, "c", "3");
		assertContains(parameterMap, "d", "4");
		assertContains(parameterMap, "e", "5");
	}

	/** {@inheritDoc} */
	protected void setUp() throws Exception
	{
		tester = new WicketTester();
		tester.getApplication().getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
	}

	protected void tearDown() throws Exception
	{
		tester.destroy();
	}

	/**
	 * @param parameterMap
	 *            a map
	 * @param key
	 *            expected key
	 * @param value
	 *            expected value
	 */
	private void assertContains(Map parameterMap, String key, String value)
	{
		assertEquals(value, parameterMap.get(key));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11550.java