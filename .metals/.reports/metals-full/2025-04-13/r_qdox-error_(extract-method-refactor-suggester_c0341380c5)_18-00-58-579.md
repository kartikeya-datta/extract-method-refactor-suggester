error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17610.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17610.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[157,2]

error in qdox parser
file content:
```java
offset: 4047
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17610.java
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
package wicket.markup.html.form;

import java.util.ArrayList;
import java.util.Collection;

import wicket.RequestCycle;

/**
 * Partial adapter for {@link wicket.markup.html.form.IIdList} that makes
 * it easier to work with anonymous implementations.
 * <p>
 * An example of how to use this:
 * <pre>
 *  class TypesList extends IdListAdapter
 *  {
 *      // load all needed object when attaching
 *      public void doAttach(RequestCycle cycle)
 *      {
 *          List definitionTypes = definitionDAO.findDefinitionTypes();
 *          addAll(definitionTypes);
 *      }
 *
 *      // clear the list when detaching
 *      public void doDetach(RequestCycle cycle)
 *      {
 *          clear();
 *      }
 *
 *      // gets the value that is used for displaying
 *      public String getDisplayValue(int row)
 *      {
 *          DefinitionType type = (DefinitionType)get(row);
 *          return type.getName();
 *      }
 *
 *      // gets the backing id used for rendering the selection
 *      public String getIdValue(int row)
 *      {
 *          DefinitionType type = (DefinitionType)get(row);
 *          return type.getId().toString();
 *      }
 *
 *      // gets the object based on the given id
 *      public Object getObjectById(String id)
 *      {
 *          if(id == null) return null;
 *          return definitionDAO.loadDefinitionType(Long.valueOf(id));
 *          // note: more efficient would be to just look up the object in this list
 *          // as it is loaded allready
 *      }
 *  }
 * </pre>
 * </p>
 *
 * @author Eelco Hillenius
 */
public abstract class IdListAdapter extends ArrayList implements IIdList
{
    /**
     * Transient flag to prevent multiple detach/attach scenario.
     */
    private transient boolean attached = false;

    /**
     * Construct.
     */
    public IdListAdapter()
    {
        super();
    }

    /**
     * Construct.
     * @param initialCapacity the initial capacity
     */
    public IdListAdapter(int initialCapacity)
    {
        super(initialCapacity);
    }

    /**
     * Construct.
     * @param collection a collection
     */
    public IdListAdapter(Collection collection)
    {
        super(collection);
    }

    /**
     * Attach to the current request.
     * @see wicket.markup.html.form.IIdList#attach(wicket.RequestCycle)
     */
    public final void attach(final RequestCycle cycle)
    {
        if (attached)
        {
            return;
        }

        doAttach(cycle);
        attached = true;
    }

    /**
     * Detach from the current request.
     * @see wicket.markup.html.form.IIdList#detach(wicket.RequestCycle)
     */
    public final void detach(final RequestCycle cycle)
    {
        if (!attached)
        {
            return;
        }

        doDetach(cycle);
        attached = false;
    }

    /**
     * Attach to the current request. Implement this method with custom behaviour, such
     * as loading the list of object you need for this list.
     * @param cycle the current request cycle
     */
    protected void doAttach(final RequestCycle cycle)
    {
    }

    /**
     * Detach from the current request. Implement this method with custom behaviour, such as
     * clearing the list.
     * @param cycle the current request cycle
     */
    protected void doDetach(final RequestCycle cycle)
    {
    }
}@@
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17610.java