error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12692.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12692.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12692.java
text:
```scala
a@@ssertEquals("test2/Integer0/Integer1/a%3Ab/wicket:pageMapName/mypagemap", url2);

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
package org.apache.wicket.markup.html.link;

import org.apache.wicket.IRequestTarget;
import org.apache.wicket.PageParameters;
import org.apache.wicket.WicketTestCase;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.request.IRequestCodingStrategy;
import org.apache.wicket.request.RequestParameters;
import org.apache.wicket.request.target.coding.IndexedParamUrlCodingStrategy;
import org.apache.wicket.request.target.component.BookmarkablePageRequestTarget;


/**
 * @author jcompagner
 */
public class IndexedParamUrlCodingTest extends WicketTestCase
{

	/**
	 * Construct.
	 * 
	 * @param name
	 */
	public IndexedParamUrlCodingTest(String name)
	{
		super(name);
	}

	/**
	 * @throws Exception
	 */
	public void testIndexedLink() throws Exception
	{
		tester.getApplication().mount(
				"/test1",
				new IndexedParamUrlCodingStrategy("/test1", BookmarkableHomePageLinksPage.class,
						null));
		tester.getApplication().mount(
				"/test2",
				new IndexedParamUrlCodingStrategy("/test2", BookmarkableHomePageLinksPage.class,
						"mypagemap"));

		tester.setupRequestAndResponse();
		WebRequestCycle cycle = tester.createRequestCycle();

		PageParameters parameters = new PageParameters();
		parameters.add("0", "Integer0");
		parameters.add("1", "Integer1");
		parameters.add("2", "a:b");

		String url1 = cycle.urlFor(
				new BookmarkablePageRequestTarget(BookmarkableHomePageLinksPage.class, parameters))
				.toString();
		String url2 = cycle.urlFor(
				new BookmarkablePageRequestTarget("mypagemap", BookmarkableHomePageLinksPage.class,
						parameters)).toString();
		assertEquals("test1/Integer0/Integer1/a%3Ab", url1);
		assertEquals("test2/Integer0/Integer1/a%3Ab/org.apache.wicket:pageMapName/mypagemap", url2);

		tester.setupRequestAndResponse();
		tester.getServletRequest().setURL("/" + url1);
		cycle = tester.createRequestCycle();
		IRequestCodingStrategy encoder = cycle.getProcessor().getRequestCodingStrategy();

		RequestParameters requestParameters = encoder.decode(tester.getWicketRequest());

		IRequestTarget target1 = cycle.getProcessor().resolve(cycle, requestParameters);
		if (target1 instanceof BookmarkablePageRequestTarget)
		{
			assertNull(((BookmarkablePageRequestTarget)target1).getPageMapName());
		}
		else
		{
			fail("url: " + url1 + " wasn't resolved to a bookmarkable target: " + target1);
		}
		PageParameters params = ((BookmarkablePageRequestTarget)target1).getPageParameters();
		assertEquals("Integer0", params.getString("0"));
		assertEquals("Integer1", params.getString("1"));
		assertEquals("a:b", params.getString("2"));

		tester.setupRequestAndResponse();
		tester.getServletRequest().setURL("/" + url2);
		cycle = tester.createRequestCycle();
		encoder = cycle.getProcessor().getRequestCodingStrategy();

		requestParameters = encoder.decode(tester.getWicketRequest());

		IRequestTarget target2 = cycle.getProcessor().resolve(cycle, requestParameters);

		if (target2 instanceof BookmarkablePageRequestTarget)
		{
			assertEquals("mypagemap", ((BookmarkablePageRequestTarget)target2).getPageMapName());
		}
		else
		{
			fail("url: " + url2 + " wasn't resolved to a bookmarkable target: " + target2);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12692.java