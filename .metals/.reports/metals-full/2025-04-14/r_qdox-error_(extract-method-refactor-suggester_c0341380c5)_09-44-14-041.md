error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17652.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17652.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[158,2]

error in qdox parser
file content:
```java
offset: 5165
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17652.java
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.RequestCycle;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupElement;
import wicket.markup.MarkupStream;
import wicket.markup.html.HtmlContainer;


/**
 * This is a convinient component to create sorted table headers very easily.
 * It first scans the markup for &lt;th id="wicket-.*" ..&gt> tags and 
 * automatically creates a SortableTableHeader for each.
 * <p>
 * The component can only be used with &lt;thead&gt; tags. 
 * 
 * @author Juergen Donnerstag
 */
public class SortableTableHeaders extends HtmlContainer
{
    /** Logging */
    final private Log log = LogFactory.getLog(SortableTableHeaders.class);
    
    /** Each SortableTableHeader (without 's)  must be related to a group */
    final private SortableTableHeaderGroup group;

    /**
     * Constructor
     * 
     * @param componentName The component name; must not be null
     * @param listView the list view
     * @param addActionLinkMarkup
     */
    public SortableTableHeaders(final String componentName,
            final ListView listView, final boolean addActionLinkMarkup)
    {
        super(componentName);
        
        this.group = new SortableTableHeaderGroup(this, listView);
        this.group.setAddActionLinkMarkup(addActionLinkMarkup);
    }

    /**
     * Scan the related markup and attach a SortableTableHeader to each 
     * &lt;th&gt; tag found.
     *   
     * @see wicket.Component#handleRender(wicket.RequestCycle)
     */
    protected void handleRender(RequestCycle cycle)
    {
        // Allow anonmous class to access 'this' methods with same name
        final SortableTableHeaders me = this;

        // Get the markup related to the component
        MarkupStream markupStream = this.findMarkupStream();
        
        // Save position in markup stream
        final int markupStart = markupStream.getCurrentIndex();

        // Must be <thead> tag
        ComponentTag tag = markupStream.getTag();
        checkTag(tag, "thead");
        
        // find all <th id="wicket-..." childs
        // Loop through the markup in this container
        markupStream.next();
        while (markupStream.hasMore())
        {
            final MarkupElement element = markupStream.get();
            if (element instanceof ComponentTag)
            {
                // Get element as tag
                tag = (ComponentTag) element;
                if (tag.getName().equalsIgnoreCase("th"))
                {
                    // Get component name
                    final String componentName = tag.getComponentName();
                    if ((componentName != null) && (get(componentName) == null))
                    {
	                    add(new SortableTableHeader(componentName, group)
	                    {
	                        protected int compareTo(final Object o1, final Object o2)
	                        {
	                            return me.compareTo(this, o1, o2);
	                        }
	
	                        protected Comparable getObjectToCompare(final Object object)
	                        {
	                            return me.getObjectToCompare(this, object);
	                        }
	                    });
                    }
                }
            }
            
            markupStream.next();
        }
        
        // Rewind to start of markup
        markupStream.setCurrentIndex(markupStart);

        // Continue with default behaviour
        super.handleRender(cycle);
    }

    /**
     * Compare two object of the column to be sorted, assuming both Objects
     * support compareTo().
     * 
     * @see Comparable#compareTo(java.lang.Object)
     * 
     * @param header
     * @param o1
     * @param o2
     * @return compare result
     */
    protected int compareTo(final SortableTableHeader header, final Object o1, final Object o2)
    {
        Comparable obj1 = getObjectToCompare(header, o1);
        Comparable obj2 = getObjectToCompare(header, o2);
        return obj1.compareTo(obj2);
    }

    /**
     * Get one of the two Object to be compared for sorting a column.
     * 
     * @param header
     * @param object
     * @return comparable object
     */
    protected Comparable getObjectToCompare(final SortableTableHeader header, final Object object)
    {
        return (Comparable) object;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17652.java