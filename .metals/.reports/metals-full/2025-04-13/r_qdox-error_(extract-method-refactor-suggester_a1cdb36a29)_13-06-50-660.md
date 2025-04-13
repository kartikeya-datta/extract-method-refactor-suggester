error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8662.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8662.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8662.java
text:
```scala
l@@istItem.add(new Label("hours", Double.toString(value.getAmount())));

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
package wicket.examples.displaytag;

import wicket.PageParameters;
import wicket.contrib.markup.html.list.SortableListViewHeader;
import wicket.contrib.markup.html.list.SortableListViewHeaders;
import wicket.examples.displaytag.export.CsvView;
import wicket.examples.displaytag.export.ExcelView;
import wicket.examples.displaytag.export.ExportLink;
import wicket.examples.displaytag.export.XmlView;
import wicket.examples.displaytag.utils.MyPageableListViewNavigator;
import wicket.examples.displaytag.utils.PagedTableWithAlternatingRowStyle;
import wicket.examples.displaytag.utils.ReportList;
import wicket.examples.displaytag.utils.ReportableListObject;
import wicket.markup.html.basic.Label;
import wicket.markup.html.list.ListItem;



/**
 * Pageable + sortable + exportable + grouping table
 * 
 * @author Juergen Donnerstag
 */
public class ExamplePse extends Displaytag
{
    /**
     * Constructor.
     * 
     * @param parameters Page parameters
     */
    public ExamplePse(final PageParameters parameters)
    {
        // Test data
        final ReportList data = new ReportList();

        // Add the table
        final PagedTableWithAlternatingRowStyle table = new PagedTableWithAlternatingRowStyle("rows", data, 10)
        {
            // Groups: value must be equal
            private ReportableListObject previousValue = null;
            
            /**
             * @see wicket.examples.displaytag.utils.PagedTableWithAlternatingRowStyle#populateItem(ListItem)
             */
            public void populateItem(final ListItem listItem)
            {
                super.populateItem(listItem);
                
                final ReportableListObject value = (ReportableListObject) listItem.getModelObject();

                // If first row of table, print anyway
                if (previousValue == null)
                {
                    listItem.add(new Label("city", value.getCity()));
                    listItem.add(new Label("project", value.getProject()));
                } 
                else
                {
	                boolean equal = value.getCity().equals(previousValue.getCity());
	                listItem.add(new Label("city", equal ? "" : value.getCity()));
	                
	                equal &= value.getProject().equals(previousValue.getProject());
	                listItem.add(new Label("project", equal ? "" : value.getProject()));
                }

                // Not included in grouping
                listItem.add(new Label("hours", new Double(value.getAmount())));
                listItem.add(new Label("task", value.getTask()));
                
                // remember the current value for the next row
                previousValue = value;
            }
        };

        add(table);
        
        // Add the sortable header and define how to sort the different columns
        add(new SortableListViewHeaders("header", table)
        {
	        protected Comparable getObjectToCompare(final SortableListViewHeader header, final Object object)
	        {
	            final String name = header.getName();
	            if (name.equals("city"))
	            {
	                return ((ReportableListObject)object).getCity();
	            }
	            if (name.equals("project"))
	            {
	                return ((ReportableListObject)object).getProject();
	            }
	            
	            return "";
	        }
        });

        // Add a table navigator
        add(new MyPageableListViewNavigator("pageTableNav", table));

        // Add export links
        add(new ExportLink("exportCsv", data, new CsvView(data, true, false, false)));
        add(new ExportLink("exportExcel", data, new ExcelView(data, true, false, false)));
        add(new ExportLink("exportXml", data, new XmlView(data, true, false, false)));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8662.java