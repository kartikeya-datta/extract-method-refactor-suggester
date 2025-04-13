error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18349.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18349.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18349.java
text:
```scala
I@@RequestCycleSettings.RenderStrategy.REDIRECT_TO_BUFFER);

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
package org.apache.wicket.protocol.http;

import org.apache.wicket.WicketTestCase;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.settings.IExceptionSettings;
import org.apache.wicket.settings.IRequestCycleSettings;


/**
 * Test exceptions thrown during request
 * 
 * @author <a href="mailto:jbq@apache.org">Jean-Baptiste Quenot</a>
 */
public class WebResponseExceptionsTest extends WicketTestCase
{
	/**
	 * Tests buffered exception error page.
	 */
	public void testBufferedExceptionErrorPage()
	{
		tester.getApplication().getRequestCycleSettings().setRenderStrategy(
			IRequestCycleSettings.REDIRECT_TO_BUFFER);
		tester.getApplication().getExceptionSettings().setUnexpectedExceptionDisplay(
			IExceptionSettings.SHOW_EXCEPTION_PAGE);
		testInternalErrorPage();
	}

	/**
	 * Tests exception error page.
	 */
	public void testExceptionErrorPage()
	{
		tester.getApplication().getExceptionSettings().setUnexpectedExceptionDisplay(
			IExceptionSettings.SHOW_EXCEPTION_PAGE);
		testInternalErrorPage();
	}

	/**
	 * Tests page expired.
	 */
	public void testExpirePage()
	{
		tester.startPage(TestExpirePage.class);
		String document = tester.getServletResponse().getDocument();
		assertTrue(document.contains("Click me to get an error"));

		AjaxLink link = (AjaxLink)tester.getComponentFromLastRenderedPage("link");

		// Clear the session to remove the pages
		// tester.getWicketSession().invalidateNow();
		//
		// tester.setCreateAjaxRequest(true);
		// tester.executeAjaxEvent(link, "onclick");
		// tester.clickLink("link");
		//
		// document = tester.getServletResponse().getDocument();
		// assertTrue(document.contains("-"));
		// tester.assertAjaxLocation();

		WebRequestCycle cycle = tester.setupRequestAndResponse(true);
		tester.getWicketSession().invalidateNow();

		// Clear the session to remove the pages
		tester.getWicketSession().invalidateNow();

		// Invoke the call back URL of the ajax event behavior
		String callbackUrl = ((AjaxEventBehavior)link.getBehaviors().get(0)).getCallbackUrl(true)
			.toString();
		tester.getServletRequest().setURL(callbackUrl);

		// Do not call processRequestCycle() because it throws an
		// exception when getting an error page
		cycle.request();

		document = tester.getServletResponse().getDocument();
		assertTrue(document.equals("-"));
		tester.assertAjaxLocation();
	}

	/**
	 * Tests internal error page.
	 */
	public void testInternalErrorPage()
	{
		tester.startPage(TestErrorPage.class);
		AjaxLink link = (AjaxLink)tester.getComponentFromLastRenderedPage("link");

		try
		{
			tester.executeAjaxEvent(link, "onclick");
			fail("Excepted an error message to be thrown");
		}
		catch (IllegalStateException ex)
		{
			// expected exception
			assertEquals(500, ((MockHttpServletResponse)tester.getWicketResponse()
				.getHttpServletResponse()).getStatus());
		}

		tester.startPage(TestErrorPage.class);

		try
		{
			tester.clickLink("link");
			fail("Excepted an error message to be thrown");
		}
		catch (IllegalStateException ex)
		{
			assertEquals(500, ((MockHttpServletResponse)tester.getWicketResponse()
				.getHttpServletResponse()).getStatus());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18349.java