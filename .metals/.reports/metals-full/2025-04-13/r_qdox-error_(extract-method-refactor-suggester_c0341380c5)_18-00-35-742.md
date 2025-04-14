error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16613.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16613.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16613.java
text:
```scala
a@@dd(new SortableTableHeaders("header", table)

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
import java.util.ArrayList;
import java.util.List;

import wicket.PageParameters;
import wicket.markup.html.HtmlPage;
import wicket.markup.html.basic.Label;
import wicket.markup.html.table.ListItem;
import wicket.markup.html.table.ListView;
import wicket.markup.html.table.SortableTableHeader;
import wicket.markup.html.table.SortableTableHeaders;


/**
 * Dummy page used for resource testing.
 */
public class SortableTableHeadersPage extends HtmlPage {

    /**
     * Construct.
     * @param parameters page parameters.
     */
    public SortableTableHeadersPage(final PageParameters parameters) {
        super();
        
        List list = new ArrayList();
        addUser(list, 1, "Name-aaa", "mail-1");
        addUser(list, 2, "Name-bbb", "mail-2");
        addUser(list, 3, "Name-ccc", "mail-3");
        addUser(list, 4, "Name-ddd", "mail-4");
        addUser(list, 5, "Name-eee", "mail-5");
        addUser(list, 6, "Name-aba", "mail-6");
        addUser(list, 7, "Name-bab", "mail-7");
        addUser(list, 8, "Name-dca", "mail-8");
        addUser(list, 9, "Name-eaa", "mail-9");
        
        ListView table = new ListView("table", list)
        {
            protected void populateItem(ListItem listItem)
            {
                User user = (User)listItem.getModelObject();
                listItem.add(new Label("id", user, "id"));
                listItem.add(new Label("name", user, "name"));
                listItem.add(new Label("email", user, "email"));
            }
        };

        add(table);
        add(new SortableTableHeaders("header", table, true)
        {
            /*
             * If object does not support equals()
             */
	        protected int compareTo(SortableTableHeader header, Object o1, Object o2)
	        {
	            if (header.getName().equals("id"))
	            {
	                return ((User)o1).id - ((User)o2).id;
	            }
	            
	            return super.compareTo(header, o1, o2);
	        }

	        /**
	         * Define how to do sorting
	         * 
	         * @see wicket.markup.html.table.SortableTableHeaders#getObjectToCompare(wicket.markup.html.table.SortableTableHeader, java.lang.Object)
	         */
	        protected Comparable getObjectToCompare(final SortableTableHeader header, final Object object)
	        {
	            final String name = header.getName();
	            if (name.equals("name"))
	            {
	                return ((User)object).name;
	            }
	            if (name.equals("email"))
	            {
	                return ((User)object).email;
	            }
	            
	            return "";
	        }
        });
                
    }
    
    private void addUser(List data, int id, String name, String email)
    {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        data.add(user);
    }
    
    private class User implements Serializable
    {   
        private int id;
	    private String name;
	    private String email;
        /**
         * Gets email.
         * @return email
         */
        public final String getEmail()
        {
            return email;
        }
        /**
         * Sets email.
         * @param email email
         */
        public final void setEmail(String email)
        {
            this.email = email;
        }
        /**
         * Gets id.
         * @return id
         */
        public final int getId()
        {
            return id;
        }
        /**
         * Sets id.
         * @param id id
         */
        public final void setId(int id)
        {
            this.id = id;
        }
        /**
         * Gets name.
         * @return name
         */
        public final String getName()
        {
            return name;
        }
        /**
         * Sets name.
         * @param name name
         */
        public final void setName(String name)
        {
            this.name = name;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16613.java