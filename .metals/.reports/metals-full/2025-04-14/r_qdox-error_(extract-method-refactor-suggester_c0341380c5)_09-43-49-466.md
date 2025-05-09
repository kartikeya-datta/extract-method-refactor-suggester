error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16090.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16090.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16090.java
text:
```scala
t@@ester.getRequest().getPostParameters().setParameterValue(input.getInputName(),

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
package org.apache.wicket.markup.html.border;

import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.WicketTestCase;
import org.apache.wicket.markup.MarkupException;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.settings.IMarkupSettings;

/**
 * Test the component: PageView
 * 
 * @author Juergen Donnerstag
 */
public class BoxBorderTest extends WicketTestCase
{

	/**
	 * Test a simply page containing the debug component
	 * 
	 * @throws Exception
	 */
	public void test1() throws Exception
	{
		executeTest(BoxBorderTestPage_1.class, "BoxBorderTestPage_ExpectedResult_1.html");
	}

	/**
	 * Test a simply page containing the debug component
	 * 
	 * @throws Exception
	 */
	public void test2() throws Exception
	{
		executeTest(BoxBorderTestPage_2.class, "BoxBorderTestPage_ExpectedResult_2.html");
	}

	/**
	 * Test a simply page containing the debug component
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings( { "unchecked" })
	public void test3() throws Exception
	{
		executeTest(BoxBorderTestPage_3.class, "BoxBorderTestPage_ExpectedResult_3.html");

		Border border = (Border)tester.getLastRenderedPage().get("border");
		assertNotNull(border);
		Form<?> form = (Form<?>)tester.getLastRenderedPage().get("border:myForm");

		TextField<String> input = (TextField<String>)tester.getLastRenderedPage().get(
			"border:myForm:border_body:name");
		assertEquals("", input.getDefaultModelObjectAsString());

		tester.getRequest().getPostRequestParameters().setParameterValue(input.getInputName(),
			"jdo");
		tester.submitForm(form.getPageRelativePath());

		input = (TextField<String>)tester.getLastRenderedPage().get(
			"border:myForm:border_body:name");
		assertEquals("jdo", input.getDefaultModelObjectAsString());
	}

	/**
	 * Test to ensure MarkupException is thrown when Markup and Object hierarchy does not match with
	 * a Border involved.
	 * 
	 * @throws Exception
	 */
	public void test4() throws Exception
	{
		Class<? extends Page> pageClass = BorderTestHierarchyPage_4.class;

		System.out.println("=== " + pageClass.getName() + " ===");

		MarkupException markupException = null;
		try
		{
			tester.startPage(pageClass);
		}
		catch (MarkupException e)
		{
			markupException = e;
		}

		assertNotNull("Markup does not match component hierarchy, but exception not thrown.",
			markupException);
	}

	/**
	 * Test to ensure border render wrapped settings functions properly.
	 * 
	 * @throws Exception
	 */
	public void testRenderWrapped() throws Exception
	{
		executeTest(BorderRenderWrappedTestPage_1.class,
			"BorderRenderWrappedTestPage_ExpectedResult_1.html");
	}

	/**
	 * Test a simply page containing the debug component
	 * 
	 * @throws Exception
	 */
	public void test5() throws Exception
	{
		executeTest(BoxBorderTestPage_5.class, "BoxBorderTestPage_ExpectedResult_5.html");
	}

	/**
	 * Test a simply page containing the debug component
	 * 
	 * @throws Exception
	 */
	public void test6() throws Exception
	{
		executeTest(BoxBorderTestPage_6.class, "BoxBorderTestPage_ExpectedResult_6.html");
	}

	/**
	 * Test a simply page containing the debug component
	 * 
	 * @throws Exception
	 */
	public void test7() throws Exception
	{
		final IMarkupSettings markupSettings = Application.get().getMarkupSettings();
		markupSettings.setCompressWhitespace(true);
		markupSettings.setStripComments(true);
		markupSettings.setStripWicketTags(true);
		markupSettings.setStripXmlDeclarationFromOutput(true);

		executeTest(BoxBorderTestPage_1.class, "BoxBorderTestPage_ExpectedResult_7.html");
	}

	/**
	 * Test a simply page containing the debug component
	 * 
	 * @throws Exception
	 */
	public void test8() throws Exception
	{
		executeTest(BoxBorderTestPage_8.class, "BoxBorderTestPage_ExpectedResult_8.html");
	}

	/**
	 * Test a simply page containing the debug component
	 * 
	 * @throws Exception
	 */
	public void test9() throws Exception
	{
		executeTest(BoxBorderTestPage_9.class, "BoxBorderTestPage_ExpectedResult_9.html");
	}

	/**
	 * @throws Exception
	 */
	public void test10() throws Exception
	{
		Exception e = null;
		try
		{
			executeTest(BoxBorderTestPage_10.class, "BoxBorderTestPage_ExpectedResult_10.html");
		}
		catch (WicketRuntimeException ex)
		{
			if (ex.getMessage().startsWith("The border tag must be an open tag."))
			{
				e = ex;
			}
		}
		assertNotNull(
			"Expected a WicketRuntimeException. Border tag must be open tags. Open-close tags are not allowed",
			e);
	}

	/**
	 * Test a simply page containing the debug component
	 * 
	 * @throws Exception
	 */
	public void test11() throws Exception
	{
		executeTest(BoxBorderTestPage_11.class, "BoxBorderTestPage_ExpectedResult_11.html");

		Page page = tester.getLastRenderedPage();
		tester.clickLink("border:title");

		tester.clickLink("border:title");

		tester.clickLink("border:title");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16090.java