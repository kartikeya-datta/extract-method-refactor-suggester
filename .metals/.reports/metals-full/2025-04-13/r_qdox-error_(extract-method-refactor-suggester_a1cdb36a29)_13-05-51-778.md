error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/875.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/875.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/875.java
text:
```scala
b@@ody.addExpectedChild(new Tag("span").addExpectedChild(new Tag("em")

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
package wicket.markup.html.list;

import wicket.markup.html.link.Link;
import wicket.protocol.http.MockWebApplication;
import wicket.protocol.http.documentvalidation.HtmlDocumentValidator;
import wicket.protocol.http.documentvalidation.Tag;
import wicket.protocol.http.documentvalidation.TextContent;
import junit.framework.TestCase;


/**
 * Test for simple table behaviour.
 */
public class IncrementalTableNavigationTest extends TestCase
{

	/**
	 * Construct.
	 */
	public IncrementalTableNavigationTest()
	{
		super();
	}

	/**
	 * Construct.
	 * @param name name of test
	 */
	public IncrementalTableNavigationTest(String name)
	{
		super(name);
	}

	/**
	 * Test simple table behaviour.
	 * @throws Exception
	 */
	public void testPagedTable() throws Exception
	{
		MockWebApplication application = new MockWebApplication(null);
		application.getPages().setHomePage(IncrementalTableNavigationPage.class);
		application.setupRequestAndResponse();
		application.processRequestCycle();
		IncrementalTableNavigationPage page = (IncrementalTableNavigationPage)application
				.getLastRenderedPage();
		String document = application.getServletResponse().getDocument();
		assertTrue(validatePage1(document));

		Link link = (Link)page.get("nextNext");
		application.setupRequestAndResponse();
		application.getServletRequest().setRequestToComponent(link);
		application.processRequestCycle();
		document = application.getServletResponse().getDocument();
		assertTrue(validatePage2(document));

		link = (Link)page.get("prev");
		application.setupRequestAndResponse();
		application.getServletRequest().setRequestToComponent(link);
		application.processRequestCycle();
		document = application.getServletResponse().getDocument();
		assertTrue(validatePage3(document));
	}

	/**
	 * Validates page 1 of paged table.
	 * @param document The document
	 * @return The validation result
	 */
	private boolean validatePage1(String document)
	{
		HtmlDocumentValidator validator = new HtmlDocumentValidator();
		Tag html = new Tag("html");
		Tag head = new Tag("head");
		html.addExpectedChild(head);
		Tag title = new Tag("title");
		head.addExpectedChild(title);
		title.addExpectedChild(new TextContent("Paged Table Page"));
		Tag body = new Tag("body");
		html.addExpectedChild(body);

		Tag ulTable = new Tag("ul");
		ulTable.addExpectedChild(new Tag("li").addExpectedChild(new Tag("span")
				.addExpectedChild(new TextContent("one"))));
		ulTable.addExpectedChild(new Tag("li").addExpectedChild(new Tag("span")
				.addExpectedChild(new TextContent("two"))));
		// note that we DO NOT expect the third element as this is not on the current page
		body.addExpectedChild(ulTable);

		body.addExpectedChild(new Tag("span").addExpectedChild(new Tag("i")
				.addExpectedChild(new TextContent("Prev"))));

		body.addExpectedChild(new Tag("a").addExpectedChild(new TextContent("NextNext")));

		validator.addRootElement(html);

		return validator.isDocumentValid(document);
	}

	/**
	 * Validate page 2 of the paged table.
	 * @param document The document
	 * @return The validation result
	 */
	private boolean validatePage2(String document)
	{
		HtmlDocumentValidator validator = new HtmlDocumentValidator();
		Tag html = new Tag("html");
		Tag head = new Tag("head");
		html.addExpectedChild(head);
		Tag title = new Tag("title");
		head.addExpectedChild(title);
		title.addExpectedChild(new TextContent("Paged Table Page"));
		Tag body = new Tag("body");
		html.addExpectedChild(body);

		Tag ulTable = new Tag("ul");
		ulTable.addExpectedChild(new Tag("li").addExpectedChild(new Tag("span")
				.addExpectedChild(new TextContent("five"))));
		ulTable.addExpectedChild(new Tag("li").addExpectedChild(new Tag("span")
				.addExpectedChild(new TextContent("six"))));
		// note that we DO NOT expect the third element as this is not on the current page
		body.addExpectedChild(ulTable);

		body.addExpectedChild(new Tag("a").addExpectedChild(new TextContent("Prev")));

		body.addExpectedChild(new Tag("a").addExpectedChild(new TextContent("NextNext")));

		validator.addRootElement(html);

		return validator.isDocumentValid(document);
	}

	/**
	 * Validate page 3 of the paged table.
	 * @param document The document
	 * @return The validation result
	 */
	private boolean validatePage3(String document)
	{
		HtmlDocumentValidator validator = new HtmlDocumentValidator();
		Tag html = new Tag("html");
		Tag head = new Tag("head");
		html.addExpectedChild(head);
		Tag title = new Tag("title");
		head.addExpectedChild(title);
		title.addExpectedChild(new TextContent("Paged Table Page"));
		Tag body = new Tag("body");
		html.addExpectedChild(body);

		Tag ulTable = new Tag("ul");
		ulTable.addExpectedChild(new Tag("li").addExpectedChild(new Tag("span")
				.addExpectedChild(new TextContent("three"))));
		ulTable.addExpectedChild(new Tag("li").addExpectedChild(new Tag("span")
				.addExpectedChild(new TextContent("four"))));
		// note that we DO NOT expect the third element as this is not on the current page
		body.addExpectedChild(ulTable);

		body.addExpectedChild(new Tag("a").addExpectedChild(new TextContent("Prev")));

		body.addExpectedChild(new Tag("a").addExpectedChild(new TextContent("NextNext")));

		validator.addRootElement(html);

		return validator.isDocumentValid(document);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/875.java