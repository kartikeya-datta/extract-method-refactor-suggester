error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17654.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17654.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[200,80]

error in qdox parser
file content:
```java
offset: 5335
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17654.java
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

import wicket.markup.html.basic.Label;
import wicket.model.Model;


/**
 * A navigation for a table that holds links to other pages of the table.
 * <p>
 * For each row (one page of the list of pages) a {@link TableNavigationLink} will be
 * added that contains a {@link Label}with the page number of that link (1..n).
 * 
 * <pre>
 * 
 * 
 * 
 *      &lt;td id=&quot;wicket-navigation&quot;&gt;
 *          &lt;a id=&quot;wicket-pageLink&quot; href=&quot;SearchCDPage.html&quot;&gt;
 *             &lt;span id=&quot;wicket-pageNumber&quot;/&gt;
 *          &lt;/a&gt;
 *      &lt;/td&gt;
 * 
 * 
 *  
 * </pre>
 * 
 * thus renders like:
 * 
 * <pre>
 * 
 * 
 * 
 *      1 |  2 |  3 |  4 |  5 |  6 |  7 |  8 |  9 |
 * 
 * 
 *  
 * </pre>
 * 
 * </p>
 * <p>
 * Override method populateItem to customize the rendering of the navigation. For
 * instance:
 * 
 * <pre>
 * 
 * protected void populateItem(ListItem listItem)
 * {
 *     final int page = ((Integer) listItem.getModelObject()).intValue();
 *     final TableNavigationLink link = new TableNavigationLink(&quot;pageLink&quot;, table, page);
 *     if (page &gt; 0)
 *     {
 *         listItem.add(new Label(&quot;separator&quot;, &quot;|&quot;));
 *     }
 *     else
 *     {
 *         listItem.add(new Label(&quot;separator&quot;, &quot;&quot;));
 *     }
 *     link.add(new Label(&quot;pageNumber&quot;, String.valueOf(page + 1)));
 *     link.add(new Label(&quot;pageLabel&quot;, &quot;page&quot;));
 *     listItem.add(link);
 * }
 * </pre>
 * 
 * With:
 * 
 * <pre>
 * 
 * 
 * 
 *      &lt;td id=&quot;wicket-navigation&quot;&gt;
 *          &lt;span id=&quot;wicket-separator&quot;/&gt;
 *          &lt;a id=&quot;wicket-pageLink&quot; href=&quot;#&quot;&gt;
 *            &lt;span id=&quot;wicket-pageLabel&quot;/&gt;&lt;span id=&quot;wicket-pageNumber&quot;/&gt;
 *          &lt;/a&gt;
 *      &lt;/td&gt;
 * 
 * 
 *  
 * </pre>
 * 
 * renders like:
 * 
 * <pre>
 * page1 | page2 | page3 | page4 | page5 | page6 | page7 | page8 | page9
 * </pre>
 * 
 * </p>
 * In addition
 *
 * @author Jonathan Locke
 * @author Eelco Hillenius
 * @author Juergen Donnerstag
 */
public class TableNavigation extends ListView
{
	/** Serial Version ID. */
	private static final long serialVersionUID = 8591577491410447609L;

    /** The table this navigation is navigating. */
    protected Table table;

    /**
     * Constructor.
     * @param componentName The name of this component
     * @param table The table to navigate
     */
    public TableNavigation(final String componentName, final Table table)
    {
        super(componentName, new Model(null));
        this.table = table;
        
        this.setStartIndex(0);
    }

    /**
     * Adds a {@link TableNavigationLink}to the cell. Override this to add custom
     * components for your navigation. Use (TableNavigationLink)cell.getModel() to get the
     * current link.
     * @param listItem the list item to populate
     * @see wicket.markup.html.table.Table#populateItem(wicket.markup.html.table.ListItem)
     */
    protected void populateItem(final ListItem listItem)
    {
        // Get link
        final int page = ((Integer) listItem.getModelObject()).intValue();
        final TableNavigationLink link = new TableNavigationLink("pageLink", table, page);

        // Add pagenumber label (1..n) to the navigation link
        link.add(new Label("pageNumber", String.valueOf(page + 1)));

        // Add the navigation link to the cell
        listItem.add(link);
    }
   
    /**
     * Creates a new listItem  for the given listItem index of this listView.
     * 
     * @param index ListItem index
     * @return The new ListItem
     */
    protected ListItem newItem(final int index)
    {
        return new ListItem(index, new Model(new Integer(index)));
    }

    /**
     * Gets the table that is used to get the number of pages.
     * @return the table that is used to get the number of pages
     */
    public Table getTable()
    {
        return table;
    }

    /**
     * Sets the table that is used to get the number of pages.
     * @param table the table that is used to get the number of pages
     */
    public void setTable(Table table)
    {
        this.table = table;
    }

    /**
     * @see wicket.markup.html.table.ListView#getViewSize()
     */
    public int getViewSize()
    {
        if(table != null)
        {
            return Math.min(table.getPageCount(), this.viewSize);
        }
        else
        {
            return 0;
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17654.java