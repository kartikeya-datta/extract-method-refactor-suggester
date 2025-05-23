error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12910.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12910.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12910.java
text:
```scala
final A@@jaxLink<Void> ajaxLink = new AjaxLink<Void>("ajaxLink")

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
package org.apache.wicket.util.tester.apps_5;

import org.apache.wicket.Page;
import org.apache.wicket.WicketTestCase;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.util.tester.ITestPageSource;


/**
 * Test that the clickLink method also works with AjaxLinks
 * 
 * @author Frank Bille
 */
public class AjaxLinkClickTest extends WicketTestCase
{
	private boolean linkClicked;
	private AjaxRequestTarget ajaxRequestTarget;

	/**
	 * Construct.
	 */
	public AjaxLinkClickTest()
	{
		super("AjaxLink click test");
	}

	/**
	 * Make sure that our test flags are reset between every test.
	 * 
	 * @see org.apache.wicket.WicketTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();

		linkClicked = false;
		ajaxRequestTarget = null;
	}

	/**
	 * Test that an AjaxLink's onClick method is actually invoked.
	 */
	public void testBasicAjaxLinkClick()
	{
		// Create a link, which we test is actually invoked
		final AjaxLink ajaxLink = new AjaxLink("ajaxLink")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				linkClicked = true;
				ajaxRequestTarget = target;
			}
		};

		tester.startPage(new ITestPageSource()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Page getTestPage()
			{
				Page page = new MockPageWithLink();
				page.add(ajaxLink);

				return page;
			}
		});

		tester.clickLink("ajaxLink");

		assertTrue(linkClicked);
		assertNotNull(ajaxRequestTarget);
	}

	/**
	 * Test that clickLink also works with AjaxFallbackLinks
	 * 
	 * AjaxFallbackLinks should be clicked and interpreted as an AjaxLink, which means that
	 * AjaxRequestTarget is not null.
	 */
	public void testAjaxFallbackLinkClick()
	{
		final Page page = new MockPageWithLink();

		// Create a link, which we test is actually invoked
		page.add(new AjaxFallbackLink("ajaxLink")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				linkClicked = true;
				ajaxRequestTarget = target;
			}
		});

		tester.startPage(new ITestPageSource()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Page getTestPage()
			{
				return page;
			}
		});

		tester.clickLink("ajaxLink");

		assertTrue(linkClicked);
		assertNotNull(ajaxRequestTarget);
	}

	/**
	 * Test that when AJAX is disabled, the AjaxFallbackLink is invoked with null as
	 * AjaxRequestTarget.
	 */
	public void testFallbackLinkWithAjaxDisabled()
	{
		final Page page = new MockPageWithLink();

		// Create a link, which we test is actually invoked
		page.add(new AjaxFallbackLink("ajaxLink")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				linkClicked = true;
				ajaxRequestTarget = target;
			}
		});

		tester.startPage(new ITestPageSource()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Page getTestPage()
			{
				return page;
			}
		});

		// Click the link with ajax disabled
		tester.clickLink("ajaxLink", false);

		assertTrue(linkClicked);
		assertNull(ajaxRequestTarget);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12910.java