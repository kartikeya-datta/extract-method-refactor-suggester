error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14030.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14030.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14030.java
text:
```scala
U@@rl.parse("stateless?0-1.ILinkListener-link&testParam1=testValue1&testParam2=testValue2"));

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
package org.apache.wicket.stateless;

import org.apache.wicket.WicketTestCase;
import org.apache.wicket.ng.request.Url;
import org.apache.wicket.ng.request.component.PageParameters;

/**
 * @author jcompagner
 */
public class StatelessComponentTest extends WicketTestCase
{
	/**
	 * @throws Exception
	 */
	public void testStatelessComponentPage() throws Exception
	{
		executeTest(StatelessComponentPage.class, "StatelessComponentPage_result.html");

		tester.getRequest()
			.setUrl(
				Url.parse("wicket/bookmarkable/org.apache.wicket.stateless.StatelessComponentPage?0-1.ILinkListener-link"));
		try
		{
			tester.processRequest();
			fail();
		}
		catch (Exception e)
		{
			assertEquals("wanted exception", e.getMessage());
		}

	}

	/**
	 * @throws Exception
	 */
	public void testStatelessComponentPageWithMount() throws Exception
	{
		tester.getApplication().mountBookmarkablePage("/stateless", StatelessComponentPage.class);
		// test is always the home page. it doesn't work then
		executeTest(StatelessComponentPage.class, "StatelessComponentPage_mount_result.html");
		tester.getRequest()
			.setUrl(
				Url.parse("stateless?0-1.ILinkListener-link&amp;testParam1=testValue1&amp;testParam2=testValue2"));
		try
		{
			tester.processRequest();
			fail("An exception should have been thrown for this request!");
		}
		catch (Exception e)
		{
			assertEquals("wanted exception", e.getMessage());
		}
	}

	/**
	 * @throws Exception
	 */
	public void testStatelessComponentPageWithParams() throws Exception
	{
		PageParameters params = new PageParameters();
		params.setNamedParameter("testParam1", "testValue1");
		params.setNamedParameter("testParam2", "testValue2");

		executeTest(StatelessComponentPageWithParams.class, params,
			"StatelessComponentPageWithParams_result.html");

		tester.getRequest()
			.setUrl(
				Url.parse("wicket/bookmarkable/org.apache.wicket.stateless.StatelessComponentPageWithParams?0-1.ILinkListener-link&amp;testParam1=testValue1&amp;testParam2=testValue2"));
		try
		{
			tester.processRequest();
			fail();
		}
		catch (Exception e)
		{
			assertEquals("wanted exception", e.getMessage());
		}

	}

	/**
	 * @throws Exception
	 */
	public void testStatelessComponentPageWithParamsWithMount() throws Exception
	{
		PageParameters params = new PageParameters();
		params.setNamedParameter("testParam1", "testValue1");
		params.setNamedParameter("testParam2", "testValue2");
		tester.getApplication().mountBookmarkablePage("/stateless",
			StatelessComponentPageWithParams.class);
		// test is always the home page. it doesn't work then
		executeTest(StatelessComponentPageWithParams.class, params,
			"StatelessComponentPageWithParams_mount_result.html");
		tester.getRequest()
			.setUrl(
				Url.parse("stateless?0-1.ILinkListener-link&amp;testParam1=testValue1&amp;testParam2=testValue2"));
		try
		{
			tester.processRequest();
			fail("An exception should have been thrown for this request!");
		}
		catch (Exception e)
		{
			assertEquals("wanted exception", e.getMessage());
		}
	}

	/**
	 * @throws Exception
	 */
	public void testStatelessComponentPageWithParamsWithIndexMount() throws Exception
	{
		PageParameters params = new PageParameters();
		params.setIndexedParameter(0, "testValue1");
		params.setIndexedParameter(1, "testValue2");
		tester.getApplication().mountBookmarkablePage("/stateless",
			StatelessComponentPageWithParams.class);
		// test is always the home page. it doesn't work then
		executeTest(StatelessComponentPageWithParams.class, params,
			"StatelessComponentPageWithParams_indexed_mount_result.html");
		tester.getRequest().setUrl(
			Url.parse("stateless/testValue1/testValue2?0-1.ILinkListener-link"));
		try
		{
			tester.processRequest();
			fail("An exception should have been thrown for this request!");
		}
		catch (Exception e)
		{
			assertEquals("wanted exception", e.getMessage());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14030.java