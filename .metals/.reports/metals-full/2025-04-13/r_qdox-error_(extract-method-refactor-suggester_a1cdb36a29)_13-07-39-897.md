error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17634.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17634.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[132,80]

error in qdox parser
file content:
```java
offset: 3846
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17634.java
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
package wicket.markup.html.link;

import wicket.Page;
import wicket.PageParameters;
import wicket.RequestCycle;

/**
 * Renders a stable link which can be cached in a web browser and used at a later time.
 * @author Jonathan Locke
 */
public final class BookmarkablePageLink extends Link
{
    /** Serial Version ID */
	private static final long serialVersionUID = 2396751463296314926L;

	// The page class that this link links to
    private final Class pageClass;

    // The parameters to pass to the class constructor when instantiated
    private final PageParameters parameters;

    /**
     * Constructor.
     * @param componentName The name of this component
     * @param pageClass The class of page to link to
     */
    public BookmarkablePageLink(final String componentName, final Class pageClass)
    {
        this(componentName, pageClass, new PageParameters());
    }

    /**
     * Constructor.
     * @param componentName The name of this component
     * @param pageClass The class of page to link to
     * @param parameters The parameters to pass to the new page when the link is clicked
     */
    public BookmarkablePageLink(final String componentName, final Class pageClass,
            final PageParameters parameters)
    {
        super(componentName);
        this.pageClass = pageClass;
        this.parameters = parameters;
    }

    /**
     * @see wicket.markup.html.link.Link#linksTo(wicket.Page)
     */
    public boolean linksTo(final Page page)
    {
        return page.getClass() == pageClass;
    }

    /**
     * @see wicket.markup.html.link.Link#linkClicked(wicket.RequestCycle)
     */
    public void linkClicked(final RequestCycle cycle)
    {
        // Bookmarkable links do not have a click handler.
        // Instead they are dispatched by the request handling servlet.
    }

    /**
     * @see wicket.markup.html.link.Link#getURL(wicket.RequestCycle)
     */
    protected String getURL(final RequestCycle cycle)
    {
        // add href using url to the dispatcher
        return cycle.urlFor(pageClass, parameters);
    }

    /**
     * Adds a given page property value to this link
     * @param property The property
     * @param value The value
     * @return This
     */
    public BookmarkablePageLink setParameter(final String property, final String value)
    {
        parameters.put(property, value);

        return this;
    }

    /**
     * Adds a given page property value to this link
     * @param property The property
     * @param value The value
     * @return This
     */
    public BookmarkablePageLink setParameter(final String property, final long value)
    {
        parameters.put(property, Long.toString(value));

        return this;
    }

    /**
     * Adds a given page property value to this link
     * @param property The property
     * @param value The value
     * @return This
     */
    public BookmarkablePageLink setParameter(final String property, final int value)
    {
        parameters.put(property, Integer.toString(value));

        return this;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17634.java