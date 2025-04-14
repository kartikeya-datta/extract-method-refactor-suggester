error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8647.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8647.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8647.java
text:
```scala
.@@toString().replaceAll("&amp;", "&");

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
		AjaxLink link = (AjaxLink)tester.getComponentFromLastRenderedPage("link");

		// Cannot use executeAjaxEvent or onClick because WicketTester creates
		// an AjaxRequestTarget from scratch
		// tester.executeAjaxEvent(link, "onclick");
		// tester.clickLink("link");

		// FIXME should not be needed
		tester.createRequestCycle();

		// Clear the session to remove the pages
		tester.getWicketSession().invalidateNow();

		// Invoke the call back URL of the ajax event behavior
		String callbackUrl = ((AjaxEventBehavior)link.getBehaviors().get(0)).getCallbackUrl()
				.toString();
		tester.setupRequestAndResponse();

		// Fake an Ajax request
		(tester.getServletRequest()).addHeader("Wicket-Ajax", "Yes");
		// Set ajax mode again, as it is done in setupRequestAndResponse() only
		tester.getWicketResponse().setAjax(tester.getWicketRequest().isAjax());

		tester.getServletRequest().setURL(callbackUrl);

		// Do not call tester.processRequestCycle() because it throws an
		// exception when getting an error page
		WebRequestCycle cycle = tester.createRequestCycle();
		cycle.request();

		tester.assertAjaxLocation();
	}

	/**
	 * Tests internal error page.
	 */
	public void testInternalErrorPage()
	{
		tester.startPage(TestErrorPage.class);
		AjaxLink link = (AjaxLink)tester.getComponentFromLastRenderedPage("link");

		// Cannot use executeAjaxEvent or onClick because WicketTester creates
		// an AjaxRequestTarget from scratch
		// tester.executeAjaxEvent(link, "onclick");
		// tester.clickLink("link");

		// FIXME should not be needed
		tester.createRequestCycle();

		// Invoke the call back URL of the ajax event behavior
		String callbackUrl = ((AjaxEventBehavior)link.getBehaviors().get(0)).getCallbackUrl()
				.toString();
		tester.setupRequestAndResponse();
		// Fake an Ajax request
		(tester.getServletRequest()).addHeader("Wicket-Ajax", "Yes");
		tester.getServletRequest().setURL(callbackUrl);

		// Do not call tester.processRequestCycle() because it throws an
		// exception when getting an error page
		WebRequestCycle cycle = tester.createRequestCycle();
		cycle.request();

		tester.assertAjaxLocation();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8647.java