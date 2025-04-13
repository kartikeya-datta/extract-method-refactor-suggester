error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/826.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/826.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/826.java
text:
```scala
a@@ssertEquals("test/param0/param1?test=testval1&test=testval2&foo=fooval", url);

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

import java.io.OutputStream;

import org.apache.wicket.PageParameters;
import org.apache.wicket.Resource;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.WicketTestCase;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.value.ValueMap;

/**
 * 
 * @author
 */
public class IndexedSharedResourceCodingStrategyTest extends WicketTestCase
{
	private static final String URL_PREFIX = "/WicketTester$DummyWebApplication/WicketTester$DummyWebApplication/";
	private static final String RESOURCE_NAME = "test";

	private TestResource resource;

	/**
	 * 
	 * @see org.apache.wicket.WicketTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		resource = new TestResource();
		tester.getApplication().getSharedResources().add(RESOURCE_NAME, resource);
		final String key = new ResourceReference(RESOURCE_NAME).getSharedResourceKey();
		tester.getApplication().mount(new IndexedSharedResourceCodingStrategy("/test", key));
	}

	/**
	 * 
	 */
	public void testEmptyRequest()
	{
		final WebRequestCycle cycle = tester.setupRequestAndResponse();
		final String url = cycle.urlFor(new ResourceReference(RESOURCE_NAME)).toString();
		assertEquals("test", url);
		tester.getServletRequest().setURL(URL_PREFIX + url);
		tester.processRequestCycle(cycle);
		assertTrue(resource.params.isEmpty());
	}

	/**
	 * 
	 */
	public void testRequestWithIndexedParams()
	{
		final WebRequestCycle cycle = tester.setupRequestAndResponse();
		final ValueMap params = new ValueMap();
		params.add("0", "foo");
		params.add("1", "bar");
		final String url = cycle.urlFor(new ResourceReference(RESOURCE_NAME), params).toString();
		assertEquals("test/foo/bar", url);
		tester.getServletRequest().setURL(URL_PREFIX + url);
		tester.processRequestCycle(cycle);
		assertEquals(2, resource.params.size());
		assertEquals("foo", resource.params.getString("0"));
		assertEquals("bar", resource.params.getString("1"));
	}

	/**
	 * 
	 */
	public void testRequestWithIndexedParamsAndQueryString()
	{
		final WebRequestCycle cycle = tester.setupRequestAndResponse();
		final PageParameters params = new PageParameters();

		params.add("0", "param0");
		params.add("1", "param1");
		params.put("test", new String[] { "testval1", "testval2" });
		params.add("foo", "fooval");
		final String url = cycle.urlFor(new ResourceReference(RESOURCE_NAME), params).toString();
		assertEquals("test/param0/param1?foo=fooval&test=testval1&test=testval2", url);
		tester.getServletRequest().setURL(URL_PREFIX + url);
		tester.processRequestCycle(cycle);
		assertEquals(4, resource.params.size());
		assertEquals("fooval", resource.params.getString("foo"));

		final String[] arr = resource.params.getStringArray("test");
		assertEquals(2, arr.length);
		assertEquals("testval1", arr[0]);
		assertEquals("testval2", arr[1]);

		assertEquals("param0", resource.params.getString("0"));
		assertEquals("param1", resource.params.getString("1"));
	}

	/**
	 * 
	 */
	private static class TestResource extends Resource
	{
		private static final long serialVersionUID = 6033856371536194742L;

		public ValueMap params;

		/**
		 * 
		 * @see org.apache.wicket.Resource#getResourceStream()
		 */
		@Override
		public IResourceStream getResourceStream()
		{
			params = getParameters();

			return new AbstractResourceStreamWriter()
			{
				private static final long serialVersionUID = 1680545160545385303L;

				public void write(final OutputStream output)
				{
					// nada
				}

				public String getContentType()
				{
					return "text/plain";
				}
			};
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/826.java