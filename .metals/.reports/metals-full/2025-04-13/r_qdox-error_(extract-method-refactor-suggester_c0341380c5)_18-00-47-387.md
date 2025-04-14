error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10028.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10028.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10028.java
text:
```scala
t@@ester.getWicketAjaxBaseUrlFromLastRequest());

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
package org.apache.wicket.request.handler;

import java.io.IOException;
import java.text.ParseException;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.WicketTestCase;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.mapper.StalePageException;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.resource.StringResourceStream;
import org.junit.Test;

/**
 * 
 * @see <a href="https://issues.apache.org/jira/browse/WICKET-3252">WICKET-3252</a>
 * @author pedro
 */
public class PageProviderTest extends WicketTestCase
{
	@Test
	public void testStalePageException()
	{
		tester.startPage(TestPage.class);
		TestPage testPage = (TestPage)tester.getLastRenderedPage();

		// cache the link to the first page version
		String firstHRef = tester.urlFor(testPage.link);
		// request a new page
		tester.clickLink("link");

		try
		{
			// just making clear that we are in the tester land
			tester.setExposeExceptions(true);
			// try to get the old one
			tester.getRequest().setURL(firstHRef);
			tester.processRequest();
			fail("Stale page request process should throw StalePageException");
		}
		catch (StalePageException e)
		{
			assertTrue(true);
		}
	}

	/**
	 * Request an old URL in an AJAX request and assert that we have an AJAX response.
	 * 
	 */
	public void testStalePageExceptionOnAjaxRequest() throws IOException,
		ResourceStreamNotFoundException, ParseException
	{
		tester.startPage(TestPage.class);

		TestPage testPage = (TestPage)tester.getLastRenderedPage();
		// cache the old URL
		Url firstAjaxLinkUrl = tester.urlFor(testPage.ajaxLink);

		// request a new page
		tester.clickLink("link");

		tester.setExposeExceptions(false);
		tester.setFollowRedirects(false);
		tester.setUseRequestUrlAsBase(false);

		// execute the old URL
		executeAjaxUrlWithLastBaseUrl(firstAjaxLinkUrl);

		assertTrue(tester.getLastResponseAsString().startsWith("<ajax-response>"));
		assertTrue(tester.getLastResponse().isRedirect());
	}

	/**
	 * 
	 */
	private void executeAjaxUrlWithLastBaseUrl(Url url) throws IOException,
		ResourceStreamNotFoundException, ParseException
	{
		tester.getRequest().setUrl(url);
		tester.getRequest().addHeader("Wicket-Ajax-BaseURL",
			tester.getWicketAjaxBaserUrlFromLastRequest());
		tester.getRequest().addHeader("Wicket-Ajax", "true");
		tester.processRequest();
	}

	public static class TestPage extends WebPage implements IMarkupResourceStreamProvider
	{
		Link<Void> link;
		AjaxLink<Void> ajaxLink;

		public TestPage()
		{
			add(link = new Link<Void>("link")
			{
				@Override
				public void onClick()
				{
				}
			});
			add(ajaxLink = new AjaxLink<Void>("ajaxLink")
			{
				@Override
				public void onClick(AjaxRequestTarget target)
				{
				}
			});
		}

		public IResourceStream getMarkupResourceStream(MarkupContainer container,
			Class<?> containerClass)
		{
			return new StringResourceStream(
				"<html><body><a wicket:id=\"link\"></a><a wicket:id=\"ajaxLink\"></a></body></html>");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10028.java