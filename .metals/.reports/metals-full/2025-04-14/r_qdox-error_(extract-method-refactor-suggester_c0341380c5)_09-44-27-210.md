error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17170.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17170.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17170.java
text:
```scala
a@@pplication.getPages().setHomePage(SortableTableHeadersPage.class);

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
package wicket.markup.html.table;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import wicket.markup.html.link.Link;
import wicket.protocol.http.MockHttpApplication;
import wicket.util.io.Streams;
import wicket.util.string.StringList;

import junit.framework.Assert;
import junit.framework.TestCase;


/**
 * Test for simple table behaviour.
 */
public class SortableTableHeadersTest extends TestCase
{
    /**
     * Construct.
     */
    public SortableTableHeadersTest()
    {
        super();
    }

    /**
     * Construct.
     * @param name name of test
     */
    public SortableTableHeadersTest(String name)
    {
        super(name);
    }

    /**
     * Test simple table behaviour.
     * @throws Exception
     */
    public void testPagedTable() throws Exception
    {
        MockHttpApplication application = new MockHttpApplication(null);
        application.getSettings().setHomePage(SortableTableHeadersPage.class);
        application.setupRequestAndResponse();
        application.processRequestCycle();
        SortableTableHeadersPage page = (SortableTableHeadersPage)application.getLastRenderedPage();
        String document = application.getServletResponse().getDocument();
        assertTrue(validatePage(document, "SortableTableHeadersExpectedResult_1.html"));

        Link link = (Link)page.get("header.id.actionLink");
        assertTrue(link.isEnabled());

        link = (Link)page.get("header.name.actionLink");
        assertTrue(link.isEnabled());

        link = (Link)page.get("header.email.actionLink");
        assertNull(link);

        link = (Link)page.get("header.name.actionLink");
        application.setupRequestAndResponse();
        application.getServletRequest().setRequestToComponent(link);
        application.processRequestCycle();

        // Check that redirect was set as expected and invoke it
        Assert.assertTrue("Response should be a redirect", application.getServletResponse().isRedirect());
        String redirect = application.getServletResponse().getRedirectLocation();
        application.setupRequestAndResponse();
        application.getServletRequest().setRequestToRedirectString(redirect);
        application.processRequestCycle();
        
        document = application.getServletResponse().getDocument();
        assertTrue(validatePage(document, "SortableTableHeadersExpectedResult_2.html"));

        // reverse sorting
        link = (Link)page.get("header.name.actionLink");
        application.setupRequestAndResponse();
        application.getServletRequest().setRequestToComponent(link);
        application.processRequestCycle();

        // Check that redirect was set as expected and invoke it
        Assert.assertTrue("Response should be a redirect", application.getServletResponse().isRedirect());
        redirect = application.getServletResponse().getRedirectLocation();
        application.setupRequestAndResponse();
        application.getServletRequest().setRequestToRedirectString(redirect);
        application.processRequestCycle();
        
        document = application.getServletResponse().getDocument();
        assertTrue(validatePage(document, "SortableTableHeadersExpectedResult_3.html"));
    }

    /**
     * Validates page 1 of paged table.
     *
     * @param document The document
     * @param file the file
     * @return The validation result
     * @throws IOException 
     */
    private boolean validatePage(final String document, final String file) throws IOException
    {
        String filename = this.getClass().getPackage().getName();
        filename = filename.replace('.', '/');
        filename += "/" + file;
        
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(filename);
        if (in == null)
        {
            throw new IOException("File not found: " + filename);
        }
        
        String reference = Streams.readString(in);

        boolean equals = document.equals(reference);
        if (equals == false)
        {
            System.err.println("File name: " + file);
/*  */          
            System.err.println("===================");
            System.err.println(document);
            System.err.println("===================");

            System.err.println(reference);
            System.err.println("===================");
/* */            
	
	        String[] test1 = StringList.tokenize(document, "\n").toArray();
	        String[] test2 = StringList.tokenize(reference, "\n").toArray();
	        Diff diff = new Diff(test1, test2);
	        Diff.change script = diff.diff_2(false);
	        DiffPrint.Base p = new DiffPrint.UnifiedPrint( test1, test2 );
	        p.setOutput(new PrintWriter(System.err));
	        p.print_script(script);
        }
        
        return equals;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17170.java