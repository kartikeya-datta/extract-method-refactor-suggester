error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/602.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/602.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/602.java
text:
```scala
r@@edirectToInterceptPage(newPage(SignIn.class));

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
package wicket.examples.library;

import wicket.Component;
import wicket.MarkupContainer;
import wicket.examples.WicketExamplePage;
import wicket.markup.html.border.Border;

/**
 * Ensures that user is authenticated in session.  If no user is signed in, a sign
 * in is forced by redirecting the browser to the SignIn page.
 * <p>
 * This base class also creates a border for each page subclass, automatically adding
 * children of the page to the border.  This accomplishes two important things:
 * (1) subclasses do not have to repeat the code to create the border navigation and
 * (2) since subclasses do not repeat this code, they are not hardwired to page
 * navigation structure details
 *
 * @author Jonathan Locke
 */
public class AuthenticatedWebPage extends WicketExamplePage
{
    private Border border;

    /**
     * Adding children to instances of this class causes those children to
     * be added to the border child instead.
     * @see wicket.MarkupContainer#add(wicket.Component)
     */
    public MarkupContainer add(final Component child)
    {
        // Add children of the page to the page's border component
        if (border == null)
        {
            // Create border and add it to the page
            border = new LibraryApplicationBorder("border");
            super.add(border);   
        }
        border.add(child);
        return this;
    }

    /**
     * Removing children from instances of this class causes those children to
     * be removed from the border child instead.
     * @see wicket.MarkupContainer#removeAll()
     */
    public void removeAll()
    {
        border.removeAll();
    }

    /**
     * Replacing children on instances of this class causes those children
     * to be replaced on the border child instead.
     * @see wicket.MarkupContainer#replace(wicket.Component)
     */
    public MarkupContainer replace(Component child)
    {
        return border.replace(child);
    }
    
    /**
     * Get downcast session object
     * 
     * @return The session
     */
    public LibrarySession getLibrarySession()
    {
        return (LibrarySession)getSession();
    }

    /**
     * @see wicket.Page#checkAccess()
     */
    protected boolean checkAccess()
    {
        // Is user signed in?
        if (getLibrarySession().isSignedIn())
        {
            // okay to proceed
            return true;
        }
        else
        {
            // Force sign in
            getRequestCycle().redirectToInterceptPage(SignIn.class);
            return false;
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/602.java