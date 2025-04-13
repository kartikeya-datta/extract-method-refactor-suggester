error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17653.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17653.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[228,80]

error in qdox parser
file content:
```java
offset: 7888
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17653.java
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

import java.io.Serializable;
import java.util.List;

import wicket.RenderException;
import wicket.model.IModel;


/**
 * Table is similar to ListView but provides in addition pageable views.
 * A table holds pageable rows of information. The rows can be re-ordered 
 * and deleted, either one at a time or many at a time.
 * 
 * @author Jonathan Locke
 */
public abstract class Table extends ListView
{
    /** The page to show */
    private int currentPage;

    /** Number of rows per page of the table */
    private final int rowsPerPage;

    /**
     * Creates a pagable table that uses the provided {@link IModel} as its model. All
     * components have names. A component's name cannot be null.
     * @param name The non-null name of this component
     * @param model the model
     * @param rowsPerPage number of rows on one page
     * @throws RenderException Thrown if the component has been given a null name.
     */
    public Table(String name, IModel model, int rowsPerPage)
    {
        super(name, model);
        this.rowsPerPage = rowsPerPage;
    }

    /**
     * Creates a pagable table that uses the provided instance of {@link IModel} as a
     * dynamic model. This model will be wrapped in an instance of {@link wicket.model.PropertyModel}
     * using the provided expression. Thus, using this constructor is a short-hand for:
     * 
     * <pre>
     * new MyComponent(name, new PropertyModel(myIModel, expression));
     * </pre>
     * 
     * All components have names. A component's name cannot be null.
     * @param name The non-null name of this component
     * @param model the instance of {@link IModel} from which the model object will be
     *            used as the subject for the given expression
     * @param expression the OGNL expression that works on the given object
     * @param rowsPerPage number of pages
     * @throws RenderException Thrown if the component has been given a null name.
     */
    public Table(String name, IModel model, String expression, int rowsPerPage)
    {
        super(name, model, expression);
        this.rowsPerPage = rowsPerPage;
    }

    /**
     * Creates a pagable table that uses the provided object as a simple model. This
     * object will be wrapped in an instance of {@link wicket.model.Model}. All components have names.
     * A component's name cannot be null.
     * @param name The non-null name of this component
     * @param object the object that will be used as a simple model
     * @param rowsPerPage number of pages
     * @throws RenderException Thrown if the component has been given a null name.
     */
    public Table(String name, Serializable object, int rowsPerPage)
    {
        super(name, object);
        this.rowsPerPage = rowsPerPage;
    }

    /**
     * Creates a pagable table that uses the provided object as a dynamic model. This
     * object will be wrapped in an instance of {@link wicket.model.Model} that will be wrapped in an
     * instance of {@link wicket.model.PropertyModel} using the provided expression. Thus, using this
     * constructor is a short-hand for:
     * 
     * <pre>
     * new MyComponent(name, new PropertyModel(new Model(object), expression));
     * </pre>
     * 
     * All components have names. A component's name cannot be null.
     * @param name The non-null name of this component
     * @param object the object that will be used as the subject for the given expression
     * @param expression the OGNL expression that works on the given object
     * @param rowsPerPage number of pages
     * @throws RenderException Thrown if the component has been given a null name.
     */
    public Table(String name, Serializable object, String expression, int rowsPerPage)
    {
        super(name, object, expression);
        this.rowsPerPage = rowsPerPage;
    }

    /**
     * Creates a pagable table that uses the provided object as a simple model. This
     * object will be wrapped in an instance of {@link wicket.model.Model}. All components have names.
     * A component's name cannot be null.
     * @param name The non-null name of this component
     * @param object the object that will be used as a simple model
     * @param rowsPerPage number of pages
     * @throws RenderException Thrown if the component has been given a null name.
     */
    public Table(String name, List object, int rowsPerPage)
    {
        this(name, castToSerializable(object), rowsPerPage);
    }

    /**
     * Gets the index of the current page being displayed by this table.
     * @return Returns the currentPage.
     */
    public final int getCurrentPage()
    {
        // If first cell is out of range, bring page back into range
        while ((currentPage * rowsPerPage) > getList().size())
        {
            currentPage--;
        }

        return currentPage;
    }

    /**
     * Gets the number of pages in this table.
     * @return The number of pages in this table
     */
    public final int getPageCount()
    {
        return ((getList().size() + rowsPerPage) - 1) / rowsPerPage;
    }

    /**
     * Sets the current page that this table should show.
     * @param currentPage The currentPage to set.
     */
    public final void setCurrentPage(final int currentPage)
    {
        if (currentPage < 0)
        {
            throw new IllegalArgumentException("Cannot set current page to " + currentPage);
        }

        int pageCount = getPageCount();
        if (currentPage > 0 && (currentPage >= pageCount))
        {
            throw new IllegalArgumentException("Cannot set current page to "
                    + currentPage + " because table only has " + pageCount + " pages");
        }

        this.currentPage = currentPage;
    }

    /**
     * Get rowsPerPage.
     * 
     * @return rowsPerPage.
     */
    public final int getRowsPerPage()
    {
        return rowsPerPage;
    }
    
    /**
     * 
     * @see wicket.markup.html.table.ListView#getViewSize()
     */
    public int getViewSize()
    {
        if (this.getModelObject() != null)
        {
	        super.setStartIndex(this.getCurrentPage() * this.getRowsPerPage());
	        super.setViewSize(this.getRowsPerPage());
        }
        
        return super.getViewSize();
    }

    /**
     * Prevent users from accidentially using it. Throw an IllegalArgumentException.
     * 
     * @see wicket.markup.html.table.ListView#setStartIndex(int)
     */
    public ListView setStartIndex(int startIndex) throws IllegalArgumentException
    {
        throw new IllegalArgumentException("You must not use setStartIndex() with Table");
    }
    
    /**
     * Prevent users from accidentially using it. Throw an IllegalArgumentException.
     * @param size the view size
     * @return This
     * @throws IllegalArgumentException
     * 
     * @see wicket.markup.html.table.ListView#setStartIndex(int)
     */
    public ListView setViewSize(int size) throws IllegalArgumentException
    {
        throw new IllegalArgumentException("You must not use setViewSize() with Table");
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17653.java