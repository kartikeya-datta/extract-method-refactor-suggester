error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17593.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17593.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[161,27]

error in qdox parser
file content:
```java
offset: 5017
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17593.java
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
package wicket.markup.html.debug;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import wicket.Component;
import wicket.Page;
import wicket.RequestCycle;
import wicket.markup.html.basic.Label;
import wicket.markup.html.panel.Panel;
import wicket.markup.html.table.ListItem;
import wicket.markup.html.table.ListView;
import wicket.util.string.Strings;



/**
 * This is a simple Wicket component displaying all components of a 
 * Page in a table representation. Kind of debugging support.
 * <p>
 * Simply add it like this 
 * add(new WicketComponentTree("componentTree", this.getPage()));
 * to your Page as well as 	<span id="wicket-componentTree"/>
 * to your markup.
 * 
 * @author Juergen Donnerstag
 */
public class WicketComponentTree extends Panel 
{
    /**
     * Constructor.
     * @param componentName name of the component
     * @param page the page
     */
    public WicketComponentTree(final String componentName, final Page page)
    {
        super(componentName);

        // Create an empty list. It'll be filled later
        final List data = new ArrayList();
        
        // Create the table
        add(new ListView("rows2", data)
        {
            // Assuming all other components are already populated
            // (and rendered), determine the components and fill
            // the 'our' model object.
            protected void handleRender(final RequestCycle cycle)
            {
                // Get the components data and fill and sort the list
                data.clear();
                data.addAll(getComponentData(page));
                Collections.sort(data, new Comparator()
                {
                    public int compare(Object o1, Object o2)
                    {
                        return ((ComponentData)o1).path.compareTo(((ComponentData)o2).path);
                    }
                });
                
                // Keep on rendering the table
                super.handleRender(cycle);
            }
            
            // Populate the table with Wicket elements
            protected void populateItem(ListItem listItem)
            {
                final ComponentData cdata = (ComponentData)listItem.getModelObject();
                
                listItem.add(new Label("row", new Integer(listItem.getIndex() + 1)));
                listItem.add(new Label("path", cdata.path));
                listItem.add(new Label("type", cdata.type));
                listItem.add(new Label("model", cdata.value));
            }
        });    
    }
    
    /**
     * Get recursively all components of the page, extract the information
     * relevant for us and add them to a list.
     * 
     * @param page
     * @return List of component data objects
     */
    private List getComponentData(final Page page)
    {
        final List data = new ArrayList();
        
        page.visitChildren(new IVisitor()
        {
            public Object component(Component component)
            {
                final ComponentData object = new ComponentData();
                
                // anonymous class? Get the parent's class name
                String name = component.getClass().getName();
                if (name.indexOf("$") > 0)
                {
                    name = component.getClass().getSuperclass().getName();
                }

                // remove the path component
                name = Strings.lastPathComponent(name, '.');
                
                object.path = component.getPageRelativePath();
                object.type = name;
                object.value = component.getModelObjectAsString();
                
                data.add(object);
                return IVisitor.CONTINUE_TRAVERSAL;
            }
        });
        
        return data;
    }

    /**
     * El cheapo data holder.
     * 
     * @author Juergen Donnerstag
     */
    private class ComponentData implements Serializable
    {
        /**
         * Component path.
         */
        public String path;
        /**
         * Component type.
         */
        public String type;
        /**
         * Component value.
         */
        public String value;
    }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17593.java