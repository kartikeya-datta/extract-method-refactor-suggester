error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15509.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15509.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15509.java
text:
```scala
a@@ssertTrue(mex.getMessage().contains("Tag does not have a close tag"));

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
package org.apache.wicket.markup.html.panel;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.WicketTestCase;
import org.apache.wicket.markup.MarkupException;
import org.apache.wicket.markup.MarkupNotFoundException;
import org.apache.wicket.markup.html.markupId.MyPanel;
import org.apache.wicket.util.tester.WicketTester;


/**
 * Simple application that demonstrates the mock http application code (and checks that it is
 * working)
 * 
 * @author Chris Turner
 */
public class PanelTest extends WicketTestCase
{
	/**
	 * Create the test.
	 * 
	 * @param name
	 *            The test name
	 */
	public PanelTest(String name)
	{
		super(name);
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage_1() throws Exception
	{
		boolean hit = false;
		try
		{
			executeTest(PanelPage_1.class, "Dummy.html");
		}
		catch (MarkupException mex)
		{
			hit = true;

			assertNotNull(mex.getMarkupStream());
			assertTrue(mex.getMessage().contains("Expected close tag for "));
			assertTrue(mex.getMessage().contains("SimplePanel_1.html"));
		}
		assertTrue("Did expect a MarkupException", hit);
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage_2() throws Exception
	{
		boolean hit = false;
		try
		{
			executeTest(PanelPage_2.class, "Dummy.html");
		}
		catch (MarkupNotFoundException mex)
		{
			hit = true;

			assertTrue(mex.getMessage().contains("Expected to find <wicket:panel>"));
			assertTrue(mex.getMessage().contains("SimplePanel_2.html"));
		}
		assertTrue("Did expect a MarkupException", hit);
	}

	/**
	 * @throws Exception
	 */
	public void testPanel3() throws Exception
	{
		executeTest(PanelPage_3.class, "PanelPageExpectedResult_3.html");
	}

	/**
	 * @throws Exception
	 */
	public void testPanel4() throws Exception
	{
		executeTest(PanelPage_4.class, "PanelPageExpectedResult_4.html");
	}

	/**
	 * @throws Exception
	 */
	public void testInlinePanel() throws Exception
	{
		executeTest(InlinePanelPage_1.class, "InlinePanelPageExpectedResult_1.html");
	}

	/**
	 * @throws Exception
	 */
	public void testInlinePanel_2() throws Exception
	{
		executeTest(InlinePanelPage_2.class, "InlinePanelPageExpectedResult_2.html");
	}

	/**
	 * @throws Exception
	 */
	public void testInlinePanel_3() throws Exception
	{
		executeTest(InlinePanelPage_3.class, "InlinePanelPageExpectedResult_3.html");
	}

	/**
	 * @throws Exception
	 */
	public void testInlinePanel_4() throws Exception
	{
		executeTest(InlinePanelPage_4.class, "InlinePanelPageExpectedResult_4.html");
	}

	/**
	 * @throws Exception
	 */
	public void testInlinePanel_5() throws Exception
	{
		executeTest(InlinePanelPage_5.class, "InlinePanelPageExpectedResult_5.html");
	}

	/**
	 * @throws Exception
	 */
	// TODO FIX the implementation. Fragment markup provider can not be a
	// sibling of the panel.
	public void testInlinePanel_6() throws Exception
	{
		executeTest(InlinePanelPage_6.class, "InlinePanelPageExpectedResult_6.html");
	}

	/**
	 * @throws Exception
	 */
	public void testPanelWithAttributeModifier() throws Exception
	{
		executeTest(PanelWithAttributeModifierPage.class,
			"PanelWithAttributeModifierPageExpectedResult_1.html");
	}

	/**
	 * @throws Exception
	 */
	public void testInlinePanel_7() throws Exception
	{
		executeTest(InlinePanelPage_7.class, "InlinePanelPageExpectedResult_7.html");
	}

	/**
	 * @throws Exception
	 */
	public void testInlinePanel_8() throws Exception
	{
		executeTest(InlinePanelPage_8.class, "InlinePanelPageExpectedResult_8.html");
		Page page = tester.getLastRenderedPage();
		MarkupContainer node = (MarkupContainer)page.get("first:nextContainer");
		assertNotNull(node);
		tester.clickLink("add");
		tester.assertComponentOnAjaxResponse(node);
	}

	/**
	 * 
	 */
	public void testStartPanel()
	{
		WicketTester tester = new WicketTester();
		tester.startPanel(MyPanel.class);
		tester.assertLabel("label", "Hello, World!");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15509.java