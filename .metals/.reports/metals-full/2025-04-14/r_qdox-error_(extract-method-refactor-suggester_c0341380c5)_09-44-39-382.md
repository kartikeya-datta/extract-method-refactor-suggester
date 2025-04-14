error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9986.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9986.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9986.java
text:
```scala
a@@ddToBorder(new Label("c1", "some border title"));

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
package org.apache.wicket.markup.html;

import org.apache.wicket.IPageManagerProvider;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.WicketTestCase;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.MarkupException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.mock.MockPageManager;
import org.apache.wicket.page.IManageablePage;
import org.apache.wicket.page.IPageManager;
import org.apache.wicket.page.IPageManagerContext;
import org.apache.wicket.util.lang.WicketObjects;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;
import org.apache.wicket.util.tester.WicketTester;

/**
 * @author Pedro Santos
 */
public class TransparentWebMarkupContainerTest extends WicketTestCase
{
	/**
	 * WICKET-3512
	 * 
	 * @throws Exception
	 */
	public void testMarkupInheritanceResolver() throws Exception
	{
		executeTest(MarkupInheritanceResolverTestPage3.class,
			"MarkupInheritanceResolverTestPage_expected.html");
	}

	/**
	 * 
	 */
	public void testUnableToFindComponents()
	{
		try
		{
			tester.startPage(TestPage.class);
			fail();
		}
		catch (MarkupException e)
		{
			assertTrue(e.getMessage(),
				e.getMessage().contains("Unable to find component with id 'c1'"));
		}
	}

	/**
	 * Test if the render is OK even if users define its own component with the same id
	 * WicketTagIdentifier is generation for internal components.
	 */
	public void testUsingGeneratedWicketIdAreSafe1()
	{
		tester.startPage(TestPage2.class);
		assertTrue(tester.getLastResponseAsString().contains("test_message"));

	}

	/**
	 * Same test in different scenario
	 */
	public void testUsingGeneratedWicketIdAreSafe2()
	{
		tester.startPage(TestPage3.class);
		String expected = tester.getApplication()
			.getResourceSettings()
			.getLocalizer()
			.getString("null", null);
		assertTrue(tester.getLastResponseAsString().contains(expected));
	}

	/**
	 * Test case for <a href="https://issues.apache.org/jira/browse/WICKET-3719">WICKET-3719</a>
	 */
	public void testAjaxUpdate()
	{
		WicketTester wicketTester = new WicketTester()
		{
			@Override
			protected IPageManagerProvider newTestPageManagerProvider()
			{
				return new IPageManagerProvider()
				{
					public IPageManager get(IPageManagerContext context)
					{
						return new MockPageManager()
						{
							@Override
							public void touchPage(IManageablePage page)
							{
								page = (IManageablePage)WicketObjects.cloneObject(page);
								super.touchPage(page);
							}
						};
					}
				};
			}

		};

		wicketTester.startPage(TransparentWithAjaxUpdatePage.class);
		wicketTester.clickLink("link", true);
		wicketTester.destroy();
	}

	/** */
	public static class TestPage extends WebPage implements IMarkupResourceStreamProvider
	{
		private static final long serialVersionUID = 1L;

		/** */
		public TestPage()
		{
			add(new TestBorder("border"));
		}

		public IResourceStream getMarkupResourceStream(MarkupContainer container,
			Class<?> containerClass)
		{
			return new StringResourceStream("" + //
				"<html><body>" + //
				"	<div wicket:id=\"border\">" + //
				"		<div wicket:id=\"c1\"></div>" + // component is only at the markup
				"	</div>" + //
				"</body></html>");
		}
	}

	private static class TestBorder extends Border implements IMarkupResourceStreamProvider
	{
		private static final long serialVersionUID = 1L;

		private TestBorder(String id)
		{
			super(id);
			add(new Label("c1", "some border title"));
		}

		public IResourceStream getMarkupResourceStream(MarkupContainer container,
			Class<?> containerClass)
		{
			return new StringResourceStream(
				"<wicket:border><div wicket:id=\"c1\"></div><wicket:body /></wicket:border>");
		}
	}

	/** */
	public static class TestPage2 extends WebPage implements IMarkupResourceStreamProvider
	{
		private static final long serialVersionUID = 1L;

		/** */
		public TestPage2()
		{
			add(new Label("_wicket_enclosure"));
			add(new TransparentWebMarkupContainer("container").add(new Label("msg", "test_message")));
		}

		public IResourceStream getMarkupResourceStream(MarkupContainer container,
			Class<?> containerClass)
		{
			return new StringResourceStream("" + //
				"<html><body>" + //
				"	<div wicket:id=\"_wicket_enclosure\"></div>" + //
				"	<div wicket:id=\"container\">" + //
				"		<wicket:enclosure child=\"msg\">" + //
				"			<span wicket:id=\"msg\"></span>" + //
				"		</wicket:enclosure>" + //
				"	</div>" + //
				"</body></html>");
		}
	}

	/** */
	public static class TestPage3 extends WebPage implements IMarkupResourceStreamProvider
	{
		private static final long serialVersionUID = 1L;

		/** */
		public TestPage3()
		{
			add(new WebComponent("_wicket_message"));
			add(new TransparentWebMarkupContainer("container"));
		}

		public IResourceStream getMarkupResourceStream(MarkupContainer container,
			Class<?> containerClass)
		{
			return new StringResourceStream("" + //
				"<html><body>" + //
				"	<div wicket:id=\"_wicket_message\"></div>" + //
				"	<div wicket:id=\"container\">" + //
				"		<wicket:message key=\"null\" />" + //
				"	</div>" + //
				"</body></html>");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9986.java