error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17677.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17677.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[171,80]

error in qdox parser
file content:
```java
offset: 4407
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17677.java
text:
```scala
{ // TODO finalize javadoc

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
package wicket.protocol.http;

import wicket.Page;
import wicket.RequestCycle;
import wicket.Session;
import wicket.response.ConsoleResponse;
import wicket.response.NullResponse;
import wicket.util.profile.IObjectProfileNode;


/**
 * Tests application pages by rendering them to the console. Also has profiling
 * functionality that can give an idea of how big the pages are.
 * @author Jonathan Locke
 */
public final class HttpPageTester
{
    private final RequestCycle cycle;

    private boolean consoleOutput = true;

    private boolean showObjectSize = true;

    private boolean dumpObjects = false;

    /**
     * Constructor.
     * @param application The application class to instantiate (must extend
     *            HttpApplication)
     */
    public HttpPageTester(final HttpApplication application)
    {
        this(new HttpSession(application, null)
        {
			/** Serial Version ID */
			private static final long serialVersionUID = -5729585004546567932L;
        });
    }

    /**
     * Constructor.
     * @param session Http session object for application
     */
    public HttpPageTester(final HttpSession session)
    {
        this(new HttpRequestCycle((HttpApplication) session.getApplication(), session,
                NullHttpRequest.getInstance(), NullResponse.getInstance()));
    }

    /**
     * Constructor.
     * @param cycle Http request cycle to do page testing with
     */
    public HttpPageTester(final HttpRequestCycle cycle)
    {
        Session.set(cycle.getSession());
        this.cycle = cycle;
    }

    /**
     * Test the given page
     * @param page The page to test
     */
    public void test(final Page page)
    {
        if (showObjectSize || dumpObjects)
        {
            final IObjectProfileNode profile = wicket.util.profile.ObjectProfiler
                    .profile(page);

            if (showObjectSize)
            {
                System.out.println("Page "
                        + page.getClass() + " object size = " + profile.size() + " bytes");
            }

            if (dumpObjects)
            {
                System.out.println("Page " + page.getClass() + " objects: " + profile.dump());
            }
        }

        // Render page using request cycle
        page.render(getRequestCycle());
    }

    /**
     * Returns true if console output is desired.
     * @return Returns the consoleOutput.
     */
    public boolean getConsoleOutput()
    {
        return consoleOutput;
    }

    /**
     * Determines if console output is desired.
     * @param consoleOutput The consoleOutput to set.
     */
    public void setConsoleOutput(final boolean consoleOutput)
    {
        this.consoleOutput = consoleOutput;
    }

    /**
     * @return Returns the showObjectSize.
     */
    public boolean getShowObjectSize()
    {
        return showObjectSize;
    }

    /**
     * @param showObjectSize The showObjectSize to set.
     */
    public void setShowObjectSize(final boolean showObjectSize)
    {
        this.showObjectSize = showObjectSize;
    }

    /**
     * @return Returns the dumpObjects.
     */
    public boolean getDumpObjects()
    {
        return dumpObjects;
    }

    /**
     * @param dumpObjects The dumpObjects to set.
     */
    public void setDumpObjects(final boolean dumpObjects)
    {
        this.dumpObjects = dumpObjects;
    }

    /**
     * @return RequestCycle for this page tester
     */
    private RequestCycle getRequestCycle()
    {
        if (consoleOutput)
        {
            cycle.setResponse(ConsoleResponse.getInstance());
        }

        return cycle;
    }
}

///////////////////////////////// End of File /////////////////////////////////@@
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17677.java