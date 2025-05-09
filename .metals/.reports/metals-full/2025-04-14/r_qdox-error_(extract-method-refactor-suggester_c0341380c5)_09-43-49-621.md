error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17647.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17647.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[198,80]

error in qdox parser
file content:
```java
offset: 5446
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17647.java
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
package wicket.markup.html.table;

import java.util.Collections;

import wicket.RequestCycle;
import wicket.markup.html.HtmlContainer;
import wicket.markup.html.link.Link;
import wicket.model.Model;


/**
 * Items of the ListView. 
 * 
 * @author Jonathan Locke
 */
public class ListItem extends HtmlContainer
{
	/** The index of the ListItem in the parent listView*/
    private final int index;

    /** The parent ListView, the ListItem is part of */
    private ListView listView;
    
    /**
     * A constructor which uses the index and the list provided to create a 
     * ListItem. This constructor is the default one.
     * 
     * @param listView The listView that holds this listItem
     * @param index The listItem number
     */
    protected ListItem(final int index, final ListView listView)
    {
        this(index, new Model(listView.getListObject(index)));

        this.listView = listView;
    }
    
    /**
     * This is a special constructor, which allows to create listItems without
     * an underlying listView. Paged table navigation bar is good example.
     * Be aware that some methods e.g. isLast() will throw an exception, because 
     * no underlying List is available.
     * 
     * @param index The listItem number
     * @param model The model object for the listItem
     */
    protected ListItem(final int index, final Model model)
    {
        super(Integer.toString(index), model);

        this.index = index;
        this.listView = null;
    }

    /**
     * Returns a link that will move the given listItem "up" (towards the 
     * beginning) in the listView.
     *
     * @param componentName Name of move-up link component to create
     * @return The link component
     */
    public final Link moveUpLink(final String componentName)
    {
        final Link link = new Link(componentName)
        {
			public void linkClicked(final RequestCycle cycle)
            {
                // Swap listItems and invalidate listView
                Collections.swap(listView.getList(), index, index - 1);
                listView.invalidateModel();
            }
        };

        if (index == 0)
        {
            link.setVisible(false);
        }

        return link;
    }

    /**
     * Returns a link that will move the given listItem "down" (towards 
     * the end) in the listView.
     * 
     * @param componentName Name of move-down link component to create
     * @return The link component
     */
    public final Link moveDownLink(final String componentName)
    {
        final Link link = new Link(componentName)
        {
			public void linkClicked(final RequestCycle cycle)
            {
                // Swap listeItem and invalidate listView
                Collections.swap(listView.getList(), index, index + 1);
                listView.invalidateModel();
            }
        };

        if (index == (listView.getList().size() - 1))
        {
            link.setVisible(false);
        }

        return link;
    }

    /**
     * Returns a link that will remove this listItem from the listView t
     * hat holds it.
     * 
     * @param componentName Name of remove link component to create
     * @return The link component
     */
    public final Link removeLink(final String componentName)
    {
        return new Link(componentName)
        {
			public void linkClicked(final RequestCycle cycle)
            {
                // Remove listItem and invalidate listView
			    listView.getList().remove(index);
			    listView.invalidateModel();
            }
        };
    }

    /**
     * Get the listView that holds this cell.
     * 
     * @return Returns the table.
     */
    protected final ListView getListView()
    {
        return listView;
    }

    /**
     * Gets the index of the listItem in the parent listView.
     * 
     * @return The index of this listItem in the parent listView
     */
    public final int getIndex()
    {
        return index;
    }

    /**
     * @return True if this listItem is the first listItem in the containing 
     * 	   listView
     */
    public final boolean isFirst()
    {
        return index == 0;
    }

    /**
     * @return True if this listItem is the last listItem in the containing listView.
     */
    public final boolean isLast()
    {
        int size = listView.getList().size();
        return ((size == 0) || (index == (size - 1)));
    }
    
    /**
     * Convinience method for ListViews with alternating style for colouring
     * 
     * @return True, if index is even ((index % 2) == 0)
     */
    public final boolean isEvenIndex()
    {
        return (getIndex() % 2) == 0;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17647.java