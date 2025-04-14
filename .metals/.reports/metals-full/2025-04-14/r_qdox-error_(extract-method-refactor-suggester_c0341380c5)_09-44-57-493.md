error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3591.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3591.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3591.java
text:
```scala
public v@@oid testJavaScriptEndsWithReturn()

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
package org.apache.wicket.ajax.markup.html.ajaxLink;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.WicketTestCase;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.util.tester.TagTester;


/**
 * 
 */
public class AjaxLinkTest extends WicketTestCase
{
	/**
	 * Construct.
	 * 
	 * @param name
	 */
	public AjaxLinkTest(String name)
	{
		super(name);
	}

	/**
	 * If the AjaxLink is attached to an "a" tag the href value should be replaced with "#" because
	 * we use the onclick to execute the javascript.
	 */
	public void testAnchorGetsHrefReplaced()
	{
		tester.startPage(AjaxLinkPage.class);

		TagTester ajaxLink = tester.getTagByWicketId("ajaxLink");

		// It was a link to google in the markup, but should be replaced to "#"
		assertTrue(ajaxLink.getAttributeIs("href", "#"));
	}

	/**
	 * Tests setting the request target to a normal page request from an ajax request.
	 */
	public void testFromAjaxRequestToNormalPage()
	{
		tester.startPage(AjaxLinkPageToNormalPage.class);
		tester.assertRenderedPage(AjaxLinkPageToNormalPage.class);
		Page page = tester.getLastRenderedPage();
		Component ajaxLink = page.get("ajaxLink");
		AbstractAjaxBehavior behavior = (AbstractAjaxBehavior)ajaxLink.getBehaviors().get(0);
		tester.executeBehavior(behavior);
		tester.assertRenderedPage(NormalPage.class);
	}

	/**
	 * Test that the onclick on ajax link has "return !wcall;" at the end. This ensures that
	 * execution is not turned over to the href attribute, which would then append # to the url.
	 */
	public void testJavascriptEndsWithReturn()
	{
		tester.startPage(AjaxLinkPage.class);

		TagTester ajaxLink = tester.getTagByWicketId("ajaxLink");

		assertTrue(ajaxLink.getAttributeEndsWith("onclick", "return !wcall;"));
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testPage_2() throws Exception
	{
		executeTest(AjaxPage2.class, "AjaxPage2_ExpectedResult.html");

		Page page = tester.getLastRenderedPage();
		Component ajaxLink = page.get("pageLayout:pageLayout_body:ajaxLink");
		AbstractAjaxBehavior behavior = (AbstractAjaxBehavior)ajaxLink.getBehaviors().get(0);

		executeBehavior(behavior, "AjaxPage2-1_ExpectedResult.html");
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testRenderHomePage_1() throws Exception
	{
		executeTest(AjaxLinkPage.class, "AjaxLinkPageExpectedResult.html");
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testRenderHomePage_2() throws Exception
	{
		executeTest(AjaxLinkWithBorderPage.class, "AjaxLinkWithBorderPageExpectedResult.html");

		Page page = tester.getLastRenderedPage();
		Component ajaxLink = page.get("border:border_body:ajaxLink");
		AbstractAjaxBehavior behavior = (AbstractAjaxBehavior)ajaxLink.getBehaviors().get(0);

		executeBehavior(behavior, "AjaxLinkWithBorderPage-1ExpectedResult.html");
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3591.java