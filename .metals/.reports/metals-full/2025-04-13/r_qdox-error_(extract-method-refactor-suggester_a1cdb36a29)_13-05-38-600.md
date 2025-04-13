error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17554.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17554.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[147,27]

error in qdox parser
file content:
```java
offset: 4889
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17554.java
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
package wicket;

import wicket.protocol.http.HttpRequest;

/**
 * A factory class to load Pages. 
 * 
 * @author Juergen Donnerstag
 */
public interface IPageFactory
{
    /**
     * Creates a new page. If Page with PageParameter argument constructor
     * exists, PageParameter will be null.
     * 
     * @param pageClass The page class to instantiate
     * @return The page
     * @throws RenderException
     */
    public abstract Page newPage(final Class pageClass);

    /**
     * Creates a new page. If Page with PageParameter argument constructor
     * exists, PageParameter will be null.
     * 
     * @param className The name of the page class to instantiate
     * @return The page
     * @throws RenderException
     */
    public abstract Page newPage(final String className);

    /**
     * Creates a new page. Take the PageParameters from the request.
     * 
     * @param pageClass The page class to instantiate
     * @param request The HTTP request to get the page parameters from
     * @return The page
     * @throws RenderException
     */
    public abstract Page newPage(final Class pageClass, 
            final HttpRequest request);

    /**
     * Creates a new page. Take the PageParameters from the request.
     * 
     * @param pageClassName The name of the page class to instantiate
     * @param request The HTTP request to get the page parameters from
     * @return The page
     * @throws RenderException
     */
    public abstract Page newPage(final String pageClassName, 
            final HttpRequest request);

    /**
     * Creates a new Page and apply the PageParameters to the Page constructor
     * if a proper constructor exists. Else use the default constructor.
     * 
     * @param pageClass The page class to create
     * @param parameters The page parameters
     * @return The new page
     * @throws RenderException
     */
    public abstract Page newPage(final Class pageClass,
            final PageParameters parameters);

    /**
     * Creates a new Page and apply the PageParameters to the Page constructor
     * if a proper constructor exists. Else use the default constructor.
     * 
     * @param pageClassName The name of the page class to create
     * @param parameters The page parameters
     * @return The new page
     * @throws RenderException
     */
    public abstract Page newPage(final String pageClassName,
            final PageParameters parameters);

    /**
     * Creates a new instance of a page using the given class name.
     * If pageClass implements a constructor with Page argument, page
     * will be forwarded to that constructor. Else, the default
     * constructor will be used and page be neglected.
     * 
     * @param pageClass The class of page to create
     * @param page Parameter to page constructor
     * @return The new page
     * @throws RenderException
     */
    public abstract Page newPage(final Class pageClass, final Page page);

    /**
     * Creates a new instance of a page using the given class name.
     * If pageClass implements a constructor with Page argument, page
     * will be forwarded to that constructor. Else, the default
     * constructor will be used and page be neglected.
     * 
     * @param pageClassName The name of the class of page to create
     * @param page Parameter to page constructor
     * @return The new page
     * @throws RenderException
     */
    public abstract Page newPage(final String pageClassName, final Page page);

    /**
     * Object Factory: Simply load the Class with name. Subclasses may overwrite
     * it to load Groovy classes e.g..
     * 
     * @param classname fully qualified classname
     * @return Class
     */
    public abstract Class getClassInstance(final String classname);
    
    /**
     * Get next (child-) factory from the chain of factories.
     * 
     * @return null, if no child factory defined.
     */
    public abstract IPageFactory getChildFactory();
    
    /**
     * Set the next factory in the chain
     * 
     * @param childFactory The child-factory.
     */
    public abstract void setChildFactory(final IPageFactory childFactory);
}
 No newline at end of file@@
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17554.java