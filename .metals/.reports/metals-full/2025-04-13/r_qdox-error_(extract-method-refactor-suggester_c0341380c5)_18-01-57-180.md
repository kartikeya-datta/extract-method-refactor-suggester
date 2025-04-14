error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6429.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6429.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6429.java
text:
```scala
a@@nchor6.addExpectedAttribute("href", ".*MockWebApplication.*name=test&amp;id=123");

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
package wicket.markup.html.link;

import junit.framework.Assert;
import junit.framework.TestCase;
import wicket.protocol.http.MockWebApplication;
import wicket.protocol.http.documentvalidation.HtmlDocumentValidator;
import wicket.protocol.http.documentvalidation.Tag;
import wicket.protocol.http.documentvalidation.TextContent;


/**
 * Test autolinks (href="...")
 *
 * @author Juergen Donnerstag
 */
public class AutolinkTest extends TestCase 
{
    private MockWebApplication application;

    /**
     * Create the test.
     *
     * @param name The test name
     */
    public AutolinkTest(String name) {
        super(name);
    }

    /**
     * @throws Exception
     */
    public void testRenderHomePage() throws Exception {
        application = new MockWebApplication(null);
        application.getPages().setHomePage(AutolinkPage.class);
        application.getSettings().setAutomaticLinking(true);
        
        // Do the processing
        application.setupRequestAndResponse();
        application.processRequestCycle();

        // Validate the document
        String document = application.getServletResponse().getDocument();
        System.out.println(document);
    	Assert.assertTrue(validateDocument(document));
    	
    	/*
        TODO
        <img href="Page1.gif"> ...   if autolink == true then resolve Page relativ
        <img href="/Page1.gif"> ...   if autolink == true then resolve servlet context absolut
        <link href="*.css"> ...      if autolink == true then replace in <head> as well
        <a href="subdir/Home.html">  if autolink == true then resolve Page relativ
        <a href="/rootDir/Home.html"> if autolink == true then resolve servlet absolut
        */

    }

	/**
	 * Helper method to validate the returned XML document.
	 * @param document The document
	 * @return The validation result
	 */
	private boolean validateDocument(String document)
	{
		HtmlDocumentValidator validator = new HtmlDocumentValidator();
		Tag html = new Tag("html");
		Tag head = new Tag("head");
		html.addExpectedChild(head);
		Tag title = new Tag("title");
		head.addExpectedChild(title);
		title.addExpectedChild(new TextContent("Mock Page"));
		Tag body = new Tag("body");
		html.addExpectedChild(body);
	
		Tag anchor1 = new Tag("a");
		anchor1.addExpectedAttribute("href", ".*MockWebApplication.*");
		anchor1.addExpectedChild(new TextContent("Home"));
		body.addExpectedChild(anchor1);
		
		Tag link1 = new Tag("wicket:link");
		body.addExpectedChild(link1);
		link1.addExpectedChild(new TextContent(".*"));
		
		Tag anchor2 = new Tag("a");
		anchor2.addExpectedAttribute("href", ".*MockWebApplication.*");
		anchor2.addExpectedChild(new TextContent("Home"));
		link1.addExpectedChild(anchor2);
		
		Tag link2 = new Tag("wicket:link");
		body.addExpectedChild(link2);
		link2.addExpectedChild(new TextContent(".*"));
		
		Tag anchor3 = new Tag("a");
		anchor3.addExpectedAttribute("href", "Page1.html");
		anchor3.addExpectedChild(new TextContent("Home"));
		link2.addExpectedChild(anchor3);
		
		Tag link3 = new Tag("wicket:link");
		body.addExpectedChild(link3);
		link3.addExpectedChild(new TextContent(".*"));
		
		Tag anchor4 = new Tag("a");
		anchor4.addExpectedAttribute("href", ".*MockWebApplication.*");
		anchor4.addExpectedChild(new TextContent("Home"));
		link3.addExpectedChild(anchor4);
		
		Tag link4 = new Tag("wicket:link");
		body.addExpectedChild(link4);
		link4.addExpectedChild(new TextContent(".*"));
		
		Tag anchor5 = new Tag("a");
		anchor5.addExpectedAttribute("href", "Page1.html");
		anchor5.addExpectedChild(new TextContent("Home"));
		link4.addExpectedChild(anchor5);

		link4.addExpectedChild(new TextContent(".*"));
		
		Tag link5 = new Tag("wicket:link");
		link4.addExpectedChild(link5);
		link5.addExpectedChild(new TextContent(".*"));
		
		Tag anchor6 = new Tag("a");
		anchor6.addExpectedAttribute("href", ".*MockWebApplication.*name=test&id=123");
		anchor6.addExpectedChild(new TextContent("Home"));
		link5.addExpectedChild(anchor6);
		link5.addExpectedChild(new TextContent(".*"));
	
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6429.java