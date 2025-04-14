error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7534.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7534.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7534.java
text:
```scala
a@@pplication.getMarkupSettings().setStripWicketTags(true);

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
package wicket.markup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.WicketTestCase;
import wicket.markup.html.link.Link;
import wicket.markup.html.list.DiffUtil;

/**
 */
public class MarkupInheritanceTest extends WicketTestCase
{
	private static Log log = LogFactory.getLog(MarkupInheritanceTest.class);

	/**
	 * Create the test.
	 * 
	 * @param name
	 *            The test name
	 */
	public MarkupInheritanceTest(String name)
	{
		super(name);
	}
	
	/**
	 * @throws Exception
	 */
	public void testRenderHomePage_1() throws Exception
	{
	    executeTest(MarkupInheritanceExtension_1.class, "MarkupInheritanceExpectedResult_1.html");
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage_2() throws Exception
	{
	    executeTest(MarkupInheritanceExtension_2.class, "MarkupInheritanceExpectedResult_2.html");
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage_3() throws Exception
	{
	    executeTest(MarkupInheritanceExtension_3.class, "MarkupInheritanceExpectedResult_3.html");
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage_4() throws Exception
	{
		System.out.println("=== " + MarkupInheritanceExtension_4.class.getName() + " ===");
		
		application.getRequiredPageSettings().setHomePage(MarkupInheritanceExtension_4.class);

		// Do the processing
		application.setupRequestAndResponse();
		application.processRequestCycle();

		// Validate the document
		assertEquals(MarkupInheritanceExtension_4.class, application.getLastRenderedPage().getClass());
		String document = application.getServletResponse().getDocument();
		assertTrue(DiffUtil.validatePage(document, this.getClass(), "MarkupInheritanceExpectedResult_4.html"));

		MarkupInheritanceExtension_4 page = (MarkupInheritanceExtension_4)application.getLastRenderedPage();

		Link link = (Link)page.get("link");
		application.setupRequestAndResponse();
		application.getServletRequest().setRequestToComponent(link);
		application.processRequestCycle();

		assertEquals(MarkupInheritanceExtension_4.class, application.getLastRenderedPage().getClass());

		document = application.getServletResponse().getDocument();
		assertTrue(DiffUtil.validatePage(document, this.getClass(), "MarkupInheritanceExpectedResult_4-1.html"));
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage_5() throws Exception
	{
	    executeTest(MarkupInheritanceExtension_5.class, "MarkupInheritanceExpectedResult_5.html");
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage_6() throws Exception
	{
	    executeTest(MarkupInheritancePage_6.class, "MarkupInheritanceExpectedResult_6.html");
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage_7() throws Exception
	{
	    executeTest(MarkupInheritanceExtension_7.class, "MarkupInheritanceExpectedResult_7.html");
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage_8() throws Exception
	{
		application.getSettings().setStripWicketTags(true);
	    executeTest(MarkupInheritanceExtension_8.class, "MarkupInheritanceExpectedResult_8.html");
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage_9() throws Exception
	{
	    executeTest(MarkupInheritancePage_9.class, "MarkupInheritanceExpectedResult_9.html");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7534.java