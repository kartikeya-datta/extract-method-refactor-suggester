error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17642.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17642.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[110,80]

error in qdox parser
file content:
```java
offset: 3223
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17642.java
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
import wicket.RenderException;
import wicket.RequestCycle;

/**
 * Links to a given page via an object implementing the IPageLink interface.
 * @author Jonathan Locke
 */
public class PageLink extends Link
{
    /** Serial Version ID */
	private static final long serialVersionUID = 8530958543148278216L;
	
	// The page source
    private final IPageLink pageLink;

    /**
     * Constructor.
     * @param componentName The name of this component
     * @param pageLink The page source object which will produce the page linked to when
     *            the hyperlink is clicked at a later time.
     */
    public PageLink(final String componentName, final IPageLink pageLink)
    {
        super(componentName);
        this.pageLink = pageLink;
    }

    /**
     * Constructor.
     * @param componentName Name of this component
     * @param c Page class
     */
    public PageLink(final String componentName, final Class c)
    {
        this(componentName, new IPageLink()
        {
            /** Serial Version ID */
			private static final long serialVersionUID = 319659497178801753L;

			public Page getPage()
            {
                try
                {
                    return (Page) c.newInstance();
                }
                catch (InstantiationException e)
                {
                    throw new RenderException("Cannot instantiate page class " + c, e);
                }
                catch (IllegalAccessException e)
                {
                    throw new RenderException("Cannot instantiate page class " + c, e);
                }
            }

            public Class getPageClass()
            {
                return c;
            }
        });

        // Ensure that c is a subclass of Page
        if (!Page.class.isAssignableFrom(c))
        {
            throw new IllegalArgumentException("Class " + c + " is not a subclass of Page");
        }
    }

    /**
     * @see wicket.markup.html.link.ILinkListener#linkClicked(wicket.RequestCycle)
     */
    public final void linkClicked(final RequestCycle cycle)
    {
        // Set page source's page as wicket.response page
        cycle.setPage(pageLink.getPage());
    }

    /**
     * @see wicket.markup.html.link.Link#linksTo(wicket.Page)
     */
    public boolean linksTo(final Page page)
    {
        return page.getClass() == pageLink.getPageClass();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17642.java