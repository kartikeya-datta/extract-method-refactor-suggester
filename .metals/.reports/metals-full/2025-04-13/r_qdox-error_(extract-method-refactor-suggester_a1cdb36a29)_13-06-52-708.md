error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13699.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13699.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13699.java
text:
```scala
e@@xecuteTest(SimplePage_7.class, "SimplePageExpectedResult_7.html");

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.markup.html.basic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.WicketRuntimeException;
import wicket.WicketTestCase;
import wicket.markup.MarkupException;
import wicket.markup.MarkupNotFoundException;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.border.Border;
import wicket.markup.html.panel.Panel;
import wicket.util.value.ValueMap;

/**
 * Simple application that demonstrates the mock http application code (and
 * checks that it is working)
 * 
 * @author Chris Turner
 */
public class SimplePageTest extends WicketTestCase
{
	private static Log log = LogFactory.getLog(SimplePageTest.class);

	/**
	 * Create the test.
	 * 
	 * @param name
	 *            The test name
	 */
	public SimplePageTest(String name)
	{
		super(name);
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage() throws Exception
	{
	    executeTest(SimplePage.class, "SimplePageExpectedResult.html");
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage_2() throws Exception
	{
	    executeTest(SimplePage.class, "SimplePageExpectedResult.html");

	    Label label = (Label)application.getLastRenderedPage().get("myLabel");
	    assertNotNull(label);
		application.processRequestCycle(label);
		String document = application.getServletResponse().getDocument();
		assertNotNull(document);
		assertEquals("<span wicket:id=\"myLabel\">Test Label</span>", document);
		
	    Panel panel = (Panel)application.getLastRenderedPage().get("myPanel");
	    assertNotNull(panel);
		application.processRequestCycle(panel);
		document = application.getServletResponse().getDocument();
		assertNotNull(document);
		assertEquals("<wicket:panel>Inside the panel<span wicket:id=\"label\">mein Label</span></wicket:panel>", document);
		
	    label = (Label)application.getLastRenderedPage().get("myPanel:label");
	    assertNotNull(label);
		application.processRequestCycle(label);
		document = application.getServletResponse().getDocument();
		assertNotNull(document);
		assertFalse("".equals(document));
		assertEquals("<span wicket:id=\"label\">mein Label</span>", document);
		
	    Border border = (Border)application.getLastRenderedPage().get("myBorder");
	    assertNotNull(border);
		application.processRequestCycle(border);
		document = application.getServletResponse().getDocument();
		assertNotNull(document);
		assertFalse("".equals(document));
		assertEquals("<wicket:border>before body - <wicket:body>border</wicket:body> - after body</wicket:border>", document);
		
	    border = (Border)application.getLastRenderedPage().get("myBorder2");
	    assertNotNull(border);
		application.processRequestCycle(border);
		document = application.getServletResponse().getDocument();
		assertNotNull(document);
		assertFalse("".equals(document));
		assertEquals("<span wicket:id=\"myBorder2\" testAttr=\"myValue\"><wicket:border>before body - <wicket:body>border</wicket:body> - after body</wicket:border></span>", document);

		// do the same test twice. Igor reported a problem with that, so we have to test it.
	    border = (Border)application.getLastRenderedPage().get("myBorder2");
	    assertNotNull(border);
		application.processRequestCycle(border);
		document = application.getServletResponse().getDocument();
		assertNotNull(document);
		assertFalse("".equals(document));
		assertEquals("<span wicket:id=\"myBorder2\" testAttr=\"myValue\"><wicket:border>before body - <wicket:body>border</wicket:body> - after body</wicket:border></span>", document);
		
	    WebMarkupContainer container = (WebMarkupContainer)application.getLastRenderedPage().get("test");
	    assertNotNull(container);
		application.processRequestCycle(container);
		document = application.getServletResponse().getDocument();
		assertNotNull(document);
		assertFalse("".equals(document));
		assertEquals("body<span wicket:id=\"myLabel2\">Test Label2</span>", document);
		
	    label = (Label)application.getLastRenderedPage().get("test:myLabel2");
	    assertNotNull(label);
		application.processRequestCycle(label);
		document = application.getServletResponse().getDocument();
		assertNotNull(document);
		assertFalse("".equals(document));
		assertEquals("<span wicket:id=\"myLabel2\">Test Label2</span>", document);
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage_2a() throws Exception
	{
		// Render the component without having rendered the page previously
		SimplePage page = new SimplePage();

	    Label label = (Label)page.get("myLabel");
	    assertNotNull(label);
		application.processRequestCycle(label);
		String document = application.getServletResponse().getDocument();
		assertNotNull(document);
		assertEquals("<span wicket:id=\"myLabel\">Test Label</span>", document);
		
	    Panel panel = (Panel)page.get("myPanel");
	    assertNotNull(panel);
		application.processRequestCycle(panel);
		document = application.getServletResponse().getDocument();
		assertNotNull(document);
		assertEquals("<wicket:panel>Inside the panel<span wicket:id=\"label\">mein Label</span></wicket:panel>", document);
		
	    label = (Label)page.get("myPanel:label");
	    assertNotNull(label);
		application.processRequestCycle(label);
		document = application.getServletResponse().getDocument();
		assertNotNull(document);
		assertFalse("".equals(document));
		assertEquals("<span wicket:id=\"label\">mein Label</span>", document);
		
	    Border border = (Border)page.get("myBorder");
	    assertNotNull(border);
		application.processRequestCycle(border);
		document = application.getServletResponse().getDocument();
		assertNotNull(document);
		assertFalse("".equals(document));
		assertEquals("<wicket:border>before body - <wicket:body>border</wicket:body> - after body</wicket:border>", document);
		
	    border = (Border)page.get("myBorder2");
	    assertNotNull(border);
		application.processRequestCycle(border);
		document = application.getServletResponse().getDocument();
		assertNotNull(document);
		assertFalse("".equals(document));
		assertEquals("<span wicket:id=\"myBorder2\" testAttr=\"myValue\"><wicket:border>before body - <wicket:body>border</wicket:body> - after body</wicket:border></span>", document);

		// do the same test twice. Igor reported a problem with that, so we have to test it.
	    border = (Border)page.get("myBorder2");
	    assertNotNull(border);
		application.processRequestCycle(border);
		document = application.getServletResponse().getDocument();
		assertNotNull(document);
		assertFalse("".equals(document));
		assertEquals("<span wicket:id=\"myBorder2\" testAttr=\"myValue\"><wicket:border>before body - <wicket:body>border</wicket:body> - after body</wicket:border></span>", document);
		
	    WebMarkupContainer container = (WebMarkupContainer)page.get("test");
	    assertNotNull(container);
		application.processRequestCycle(container);
		document = application.getServletResponse().getDocument();
		assertNotNull(document);
		assertFalse("".equals(document));
		assertEquals("body<span wicket:id=\"myLabel2\">Test Label2</span>", document);
		
	    label = (Label)page.get("test:myLabel2");
	    assertNotNull(label);
		application.processRequestCycle(label);
		document = application.getServletResponse().getDocument();
		assertNotNull(document);
		assertFalse("".equals(document));
		assertEquals("<span wicket:id=\"myLabel2\">Test Label2</span>", document);
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage_2b() throws Exception
	{
		// Render the component without having rendered the page previously
		SimplePage page = new SimplePage();

	    Label label = (Label)page.get("myLabel");
	    assertNotNull(label);
		ValueMap attr = label.getMarkupAttributes();
		assertNotNull(attr);
		assertEquals("myLabel", attr.getString("wicket:id"));
		
	    Panel panel = (Panel)page.get("myPanel");
	    assertNotNull(panel);
		attr = panel.getMarkupAttributes();
		assertNotNull(attr);
		assertEquals("myPanel", attr.getString("wicket:id"));
		
	    label = (Label)page.get("myPanel:label");
	    assertNotNull(label);
		attr = label.getMarkupAttributes();
		assertNotNull(attr);
		assertEquals("label", attr.getString("wicket:id"));
		
	    Border border = (Border)page.get("myBorder");
	    assertNotNull(border);
		attr = border.getMarkupAttributes();
		assertNotNull(attr);
		assertEquals("myBorder", attr.getString("wicket:id"));
		
	    border = (Border)page.get("myBorder2");
	    assertNotNull(border);
		attr = border.getMarkupAttributes();
		assertNotNull(attr);
		assertEquals("myBorder2", attr.getString("wicket:id"));

		// do the same test twice. Igor reported a problem with that, so we have to test it.
	    border = (Border)page.get("myBorder2");
	    assertNotNull(border);
		attr = border.getMarkupAttributes();
		assertNotNull(attr);
		assertEquals("myBorder2", attr.getString("wicket:id"));
		
	    WebMarkupContainer container = (WebMarkupContainer)page.get("test");
	    assertNotNull(container);
		attr = container.getMarkupAttributes();
		assertNotNull(attr);
		assertEquals("test", attr.getString("wicket:id"));
		
	    label = (Label)page.get("test:myLabel2");
	    assertNotNull(label);
		attr = label.getMarkupAttributes();
		assertNotNull(attr);
		assertEquals("myLabel2", attr.getString("wicket:id"));
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage_3() throws Exception
	{
	    executeTest(SimplePage_3.class, "SimplePageExpectedResult_3.html");
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage_4() throws Exception
	{
		boolean hit = false;
		try
		{
			executeTest(SimplePage_4.class, "SimplePageExpectedResult_4.html");
		}
		catch (MarkupException mex)
		{
			hit = true;
			
			assertNotNull(mex.getMarkupStream());
			assertTrue(mex.getMessage().indexOf("<span>") != -1);
			assertTrue(mex.getMessage().indexOf("SimplePage_4.html") != -1);
		}
		assertTrue("Did expect a MarkupException", hit);
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage_5() throws Exception
	{
		boolean hit = false;
		try
		{
			executeTest(SimplePage_5.class, "SimplePageExpectedResult_5.html");
		}
		catch (WicketRuntimeException ex)
		{
			if ((ex.getCause() != null) && (ex.getCause() instanceof MarkupNotFoundException))
			{
				hit = true;
			}
		}
		assertTrue("Did expect a MarkupNotFoundException", hit);
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage_6() throws Exception
	{
		boolean hit = false;
		try
		{
			executeTest(SimplePage_6.class, "SimplePageExpectedResult_6.html");
		}
		catch (MarkupException ex)
		{
			hit = true;
		}
		assertTrue("Did expect a MarkupException", hit);
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage_7() throws Exception
	{
//		executeTest(SimplePage_7.class, "SimplePageExpectedResult_7.html");
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage_8() throws Exception
	{
		boolean hit = false;
		try
		{
			executeTest(SimplePage_8.class, "SimplePageExpectedResult_8.html");
		}
		catch (MarkupException ex)
		{
			hit = true;
		}
		assertTrue("Did expect a MarkupException", hit);
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage_9() throws Exception
	{
		executeTest(SimplePage_9.class, "SimplePageExpectedResult_9.html");
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage_10() throws Exception
	{
	    executeTest(SimplePage_10.class, "SimplePageExpectedResult_10.html");

	    Panel panel = (Panel)application.getLastRenderedPage().get("myPanel");
	    assertNotNull(panel);
	    panel.setVisible(true);
		application.processRequestCycle(panel);
		String document = application.getServletResponse().getDocument();
		assertNotNull(document);
		assertEquals("<wicket:panel>Inside the panel<span wicket:id=\"label\">mein Label</span></wicket:panel>", document);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13699.java