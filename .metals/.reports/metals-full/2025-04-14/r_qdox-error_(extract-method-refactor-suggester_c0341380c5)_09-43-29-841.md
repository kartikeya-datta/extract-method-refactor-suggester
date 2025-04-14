error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4136.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4136.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4136.java
text:
```scala
C@@omponent comp = con.newInstance(MockPageWithLinkAndComponent.COMPONENT_ID);

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
package org.apache.wicket.ajax;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.wicket.Component;
import org.apache.wicket.MockPageWithLinkAndComponent;
import org.apache.wicket.Page;
import org.apache.wicket.WicketTestCase;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.util.tester.DiffUtil;
import org.apache.wicket.util.tester.ITestPageSource;


/**
 * Test the {@link AjaxRequestTarget}.
 * 
 * @author Frank Bille
 */
public class AjaxRequestTargetTest extends WicketTestCase
{
	/**
	 * Test that a normal <style> header contribution is added correctly.
	 * 
	 * @throws IOException
	 */
	public void testHeaderContribution1() throws IOException
	{
		executeHeaderTest(MockComponent1.class, "MockComponent1-expected.html");
	}

	/**
	 * Test that if there are no headers contributed in any components added to the response, we
	 * then don't add <header-contribution> at all.
	 * 
	 * @throws IOException
	 */
	public void testHeaderContribution2() throws IOException
	{
		executeHeaderTest(MockComponent2.class);
	}

	/**
	 * Test that a link with a wicket:id is added correctly.
	 * 
	 * @throws IOException
	 */
	public void testHeaderContribution3() throws IOException
	{
		executeHeaderTest(MockComponent3.class, "MockComponent3-expected.html");
	}

	private <C extends Component> void executeHeaderTest(final Class<C> componentClass)
		throws IOException
	{
		executeHeaderTest(componentClass, null);
	}

	private <C extends Component> void executeHeaderTest(final Class<C> componentClass,
		String expectedFile) throws IOException
	{
		final MockPageWithLinkAndComponent page = new MockPageWithLinkAndComponent();

		page.add(new WebComponent(MockPageWithLinkAndComponent.COMPONENT_ID).setOutputMarkupId(true));


		page.add(new AjaxLink<Void>(MockPageWithLinkAndComponent.LINK_ID)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				// Create an instance of the component
				try
				{
					Constructor<? extends Component> con = componentClass.getConstructor(new Class[] { String.class });

					Component comp = con.newInstance(new Object[] { MockPageWithLinkAndComponent.COMPONENT_ID });
					page.replace(comp);
					comp.setOutputMarkupId(true);

					target.add(comp);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});

		tester.startPage(new ITestPageSource()
		{
			private static final long serialVersionUID = 1L;

			public Page getTestPage()
			{
				return page;
			}
		});

// System.out.println(tester.getServletResponse().getDocument());
		tester.debugComponentTrees();

		tester.clickLink(MockPageWithLinkAndComponent.LINK_ID);

		String document = tester.getLastResponseAsString();

		Pattern pat = Pattern.compile(".*<header-contribution>(.*?)</header-contribution>.*",
			Pattern.DOTALL);
		Matcher mat = pat.matcher(document);

		String headerContribution = null;

		if (mat.matches())
		{
			headerContribution = mat.group(1);
		}

		// If the filename is empty we use it to say that the headerContribution
		// should be empty.
		// This means that it doesn't exist at all
		if (expectedFile == null)
		{
			assertNull("There was a header contribution on the response: <" + headerContribution +
				">", headerContribution);
		}
		else
		{
			DiffUtil.validatePage(headerContribution, getClass(), expectedFile, true);
		}
	}

	/**
	 * WICKET-2328
	 */
	public void testRenderMyPage()
	{
		// start and render the test page
		tester.startPage(HomePage2.class);

		// assert rendered page class
		tester.assertRenderedPage(HomePage2.class);

		// assert rendered label component
		tester.assertLabel("msg", "onBeforeRender called");

		// click the ajax link; this should have no effect
		tester.clickLink("link");

		// assert rendered label component again to make sure
		// THIS FAILS! even though the same sequence of clicks
		// done in a browser does not cause the label to change
		tester.assertLabel("msg", "onBeforeRender called");
	}

	/**
	 * WICKET-2543
	 */
	public void testVarargsAddComponent()
	{
		tester.startPage(VarargsAddComponentPage.class);

		for (int i = 0; i < VarargsAddComponentPage.NUMBER_OF_LABELS; i++)
		{
			final String labelMarkupId = "label" + i;
			final String expectedContent = String.format(VarargsAddComponentPage.INITIAL_CONTENT, i);
			tester.assertLabel(labelMarkupId, expectedContent);
		}

		tester.clickLink("link");

		for (int i = 0; i < VarargsAddComponentPage.NUMBER_OF_LABELS; i++)
		{
			final String labelMarkupId = "label" + i;
			final String expectedContent = String.format(VarargsAddComponentPage.INITIAL_CONTENT, i) +
				VarargsAddComponentPage.AJAX_APPENDED_SUFFIX;
			tester.assertLabel(labelMarkupId, expectedContent);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4136.java