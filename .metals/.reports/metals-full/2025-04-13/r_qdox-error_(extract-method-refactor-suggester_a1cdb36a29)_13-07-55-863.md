error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18293.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18293.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18293.java
text:
```scala
r@@eturn DiffUtil.validatePage(document, this.getClass(), file,true);

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
package wicket.markup.html.list;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;
import wicket.markup.html.link.Link;
import wicket.util.diff.DiffUtil;
import wicket.util.tester.WicketTester;


/**
 * Test for simple table behavior.
 */
public class PagedTableNavigatorTest extends TestCase
{
	/**
	 * Construct.
	 */
	public PagedTableNavigatorTest()
	{
		super();
	}

	/**
	 * Construct.
	 * 
	 * @param name
	 *            name of test
	 */
	public PagedTableNavigatorTest(String name)
	{
		super(name);
	}

	/**
	 * Test simple table behavior.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void testPagedTable() throws Exception
	{
		WicketTester tester = new WicketTester(PagedTableNavigatorPage.class);
		tester.setupRequestAndResponse();
		tester.processRequestCycle();
		PagedTableNavigatorPage page = (PagedTableNavigatorPage)tester.getLastRenderedPage();
		String document = tester.getServletResponse().getDocument();
		assertTrue(validatePage(document, "PagedTableNavigatorExpectedResult_1.html"));

		Link link = (Link)page.get("navigator:first");
		assertFalse(link.isEnabled());

		link = (Link)page.get("navigator:prev");
		assertFalse(link.isEnabled());

		link = (Link)page.get("navigator:next");
		assertTrue(link.isEnabled());

		link = (Link)page.get("navigator:last");
		assertTrue(link.isEnabled());

		link = (Link)page.get("navigator:next");
		tester.setupRequestAndResponse();
		tester.getServletRequest().setRequestToComponent(link);
		tester.processRequestCycle();
		document = tester.getServletResponse().getDocument();
		assertTrue(validatePage(document, "PagedTableNavigatorExpectedResult_2.html"));

		link = (Link)page.get("navigator:first");
		assertTrue(link.isEnabled());

		link = (Link)page.get("navigator:prev");
		assertTrue(link.isEnabled());

		link = (Link)page.get("navigator:next");
		assertTrue(link.isEnabled());

		link = (Link)page.get("navigator:last");
		assertTrue(link.isEnabled());

		link = (Link)page.get("navigator:prev");
		tester.setupRequestAndResponse();
		tester.getServletRequest().setRequestToComponent(link);
		tester.processRequestCycle();
		document = tester.getServletResponse().getDocument();
		assertTrue(validatePage(document, "PagedTableNavigatorExpectedResult_3.html"));

		link = (Link)page.get("navigator:first");
		assertFalse(link.isEnabled());

		link = (Link)page.get("navigator:prev");
		assertFalse(link.isEnabled());

		link = (Link)page.get("navigator:next");
		assertTrue(link.isEnabled());

		link = (Link)page.get("navigator:last");
		assertTrue(link.isEnabled());

		link = (Link)page.get("navigator:last");
		tester.setupRequestAndResponse();
		tester.getServletRequest().setRequestToComponent(link);
		tester.processRequestCycle();
		document = tester.getServletResponse().getDocument();
		assertTrue(validatePage(document, "PagedTableNavigatorExpectedResult_4.html"));

		link = (Link)page.get("navigator:first");
		assertTrue(link.isEnabled());

		link = (Link)page.get("navigator:prev");
		assertTrue(link.isEnabled());

		link = (Link)page.get("navigator:next");
		assertFalse(link.isEnabled());

		link = (Link)page.get("navigator:last");
		assertFalse(link.isEnabled());

		link = (Link)page.get("navigator:first");
		tester.setupRequestAndResponse();
		tester.getServletRequest().setRequestToComponent(link);
		tester.processRequestCycle();
		document = tester.getServletResponse().getDocument();
		assertTrue(validatePage(document, "PagedTableNavigatorExpectedResult_5.html"));

		link = (Link)page.get("navigator:first");
		assertFalse(link.isEnabled());

		link = (Link)page.get("navigator:prev");
		assertFalse(link.isEnabled());

		link = (Link)page.get("navigator:next");
		assertTrue(link.isEnabled());

		link = (Link)page.get("navigator:last");
		assertTrue(link.isEnabled());

		link = (Link)page.get("navigator:navigation:2:pageLink");
		tester.setupRequestAndResponse();
		tester.getServletRequest().setRequestToComponent(link);
		tester.processRequestCycle();
		document = tester.getServletResponse().getDocument();
		assertTrue(validatePage(document, "PagedTableNavigatorExpectedResult_6.html"));

		link = (Link)page.get("navigator:first");
		assertTrue(link.isEnabled());

		link = (Link)page.get("navigator:prev");
		assertTrue(link.isEnabled());

		link = (Link)page.get("navigator:next");
		assertTrue(link.isEnabled());

		link = (Link)page.get("navigator:last");
		assertTrue(link.isEnabled());

		link = (Link)page.get("navigator:prev");
		tester.setupRequestAndResponse();
		tester.getServletRequest().setRequestToComponent(link);
		tester.processRequestCycle();
		document = tester.getServletResponse().getDocument();
		assertTrue(validatePage(document, "PagedTableNavigatorExpectedResult_7.html"));

		link = (Link)page.get("navigator:first");
		assertTrue(link.isEnabled());

		link = (Link)page.get("navigator:prev");
		assertTrue(link.isEnabled());

		link = (Link)page.get("navigator:next");
		assertTrue(link.isEnabled());

		link = (Link)page.get("navigator:last");
		assertTrue(link.isEnabled());

		// add entries to the model list.
		List<String> modelData = (List)page.get("table").getModelObject();
		modelData.add("add-1");
		modelData.add("add-2");
		modelData.add("add-3");

		link = (Link)page.get("navigator:first");
		tester.setupRequestAndResponse();
		tester.getServletRequest().setRequestToComponent(link);
		tester.processRequestCycle();
		document = tester.getServletResponse().getDocument();
		assertTrue(validatePage(document, "PagedTableNavigatorExpectedResult_8.html"));
	}

	private boolean validatePage(final String document, final String file) throws IOException
	{
		return DiffUtil.validatePage(document, this.getClass(), file);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18293.java