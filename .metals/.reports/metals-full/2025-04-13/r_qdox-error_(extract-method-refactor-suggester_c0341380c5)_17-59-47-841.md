error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3933.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3933.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3933.java
text:
```scala
public S@@ortableTableHeadersTest(final String name)

/*
 * $Id: SortableTableHeadersTest.java 5395 2006-04-16 13:42:28 +0000 (Sun, 16
 * Apr 2006) jdonnerstag $ $Revision$ $Date: 2006-04-16 13:42:28 +0000
 * (Sun, 16 Apr 2006) $
 * 
 * ==================================================================== Licensed
 * under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.examples.displaytag.list;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wicket.examples.WicketTestCase;
import wicket.markup.html.link.Link;
import wicket.protocol.http.MockHttpServletResponse;
import wicket.settings.IRequestCycleSettings.RenderStrategy;
import wicket.util.diff.DiffUtil;
import wicket.util.tester.WicketTester;


/**
 * Test for simple table behavior.
 */
public class SortableTableHeadersTest extends WicketTestCase
{
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(SortableTableHeadersTest.class);

	/**
	 * Construct.
	 * 
	 * @param name
	 *            name of test
	 */
	public SortableTableHeadersTest(String name)
	{
		super(name);
	}

	/**
	 * Test simple table behavior.
	 * 
	 * @throws Exception
	 */
	public void testPagedTable() throws Exception
	{
		WicketTester application = new WicketTester(SortableTableHeadersPage.class);
		application.getApplication().getRequestCycleSettings().setRenderStrategy(
				RenderStrategy.REDIRECT_TO_BUFFER);
		application.setupRequestAndResponse();
		application.processRequestCycle();
		SortableTableHeadersPage page = (SortableTableHeadersPage)application.getLastRenderedPage();
		String document = application.getServletResponse().getDocument();
		DiffUtil.validatePage(document, this.getClass(),"SortableTableHeadersExpectedResult_1.html",true);

		Link link = (Link)page.get("header:<auto>-id:actionLink");
		assertTrue(link.isEnabled());

		link = (Link)page.get("header:<auto>-name:actionLink");
		assertTrue(link.isEnabled());

		link = (Link)page.get("header:<auto>-email:actionLink");
		assertNull(link);

		link = (Link)page.get("header:<auto>-name:actionLink");
		application.setupRequestAndResponse();
		application.getServletRequest().setRequestToComponent(link);
		application.processRequestCycle();

		// Check that redirect was set as expected and invoke it
		MockHttpServletResponse redirectResponse = application.getServletResponse();

		assertTrue("Response should be a redirect", redirectResponse.isRedirect());
		String redirect = application.getServletResponse().getRedirectLocation();
		application.setupRequestAndResponse();
		application.getServletRequest().setRequestToRedirectString(redirect);
		application.processRequestCycle();

		document = application.getServletResponse().getDocument();
		DiffUtil.validatePage(document, this.getClass(),"SortableTableHeadersExpectedResult_2.html",true);

		// reverse sorting
		link = (Link)page.get("header:<auto>-name:actionLink");
		application.setupRequestAndResponse();
		application.getServletRequest().setRequestToComponent(link);
		application.processRequestCycle();

		// Check that redirect was set as expected and invoke it
		// Check that wicket:border tag gets removed
		assertTrue("Response should be a redirect", application.getServletResponse().isRedirect());
		application.getApplication().getMarkupSettings().setStripWicketTags(true);
		redirect = application.getServletResponse().getRedirectLocation();
		application.setupRequestAndResponse();
		application.getServletRequest().setRequestToRedirectString(redirect);
		application.processRequestCycle();

		document = application.getServletResponse().getDocument();
		DiffUtil.validatePage(document, this.getClass(),"SortableTableHeadersExpectedResult_3.html",true);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3933.java