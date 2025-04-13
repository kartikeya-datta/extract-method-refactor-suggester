error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8510.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8510.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8510.java
text:
```scala
t@@itle.addExpectedChild(new TextContent("Simple Table Page"));

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ================================================================================
 * Copyright (c)
 * All rechten voorbehouden.
 */
package com.voicetribe.wicket.markup.html.table;

import junit.framework.TestCase;

import com.voicetribe.wicket.protocol.http.MockHttpApplication;
import com.voicetribe.wicket.protocol.http.documentvalidation.HtmlDocumentValidator;
import com.voicetribe.wicket.protocol.http.documentvalidation.Tag;
import com.voicetribe.wicket.protocol.http.documentvalidation.TextContent;

/**
 * Test for simple table behaviour.
 */
public class SimpleTableTest extends TestCase
{

    /**
     * Construct.
     * 
     */
    public SimpleTableTest()
    {
        super();
    }

    /**
     * Construct.
     * @param arg0
     */
    public SimpleTableTest(String arg0)
    {
        super(arg0);
    }

    /**
     * Test simple table behaviour.
     * @throws Exception
     */
    public void testSimpleTable() throws Exception
    {
        MockHttpApplication application = new MockHttpApplication(null);
        application.getSettings().setHomePage(SimpleTablePage.class);
        application.setupRequestAndResponse();
        application.processRequestCycle();
        SimpleTablePage page = (SimpleTablePage)application.getLastRenderedPage();
        String document = application.getServletResponse().getDocument();
        assertTrue(validateDocument(document));
    }

    /**
     * Helper method to validate the returned XML document.
     *
     * @param document The document
     * @return The validation result
     */
    private boolean validateDocument(String document) {
        HtmlDocumentValidator validator = new HtmlDocumentValidator();
        Tag html = new Tag("html");
        Tag head = new Tag("head");
        html.addExpectedChild(head);
        Tag title = new Tag("title");
        head.addExpectedChild(title);
        title.addExpectedChild(new TextContent("Test Page"));
        Tag body = new Tag("body");
        html.addExpectedChild(body);
        Tag ul = new Tag("ul");
        ul.addExpectedChild(new Tag("li").addExpectedChild(new Tag("span").addExpectedChild(new TextContent("one"))));
        ul.addExpectedChild(new Tag("li").addExpectedChild(new Tag("span").addExpectedChild(new TextContent("two"))));
        ul.addExpectedChild(new Tag("li").addExpectedChild(new Tag("span").addExpectedChild(new TextContent("three"))));
        body.addExpectedChild(ul);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8510.java