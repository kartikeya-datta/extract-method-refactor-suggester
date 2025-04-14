error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4284.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4284.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4284.java
text:
```scala
private S@@tring patternString = "";

//The contents of this file are subject to the Mozilla Public License Version 1.1
//(the "License"); you may not use this file except in compliance with the 
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License 
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003. 
//
//All Rights Reserved.
package org.columba.addressbook.gui.table.util;

import org.columba.addressbook.folder.HeaderItem;
import org.columba.addressbook.folder.HeaderItemList;
import org.columba.addressbook.gui.table.AddressbookTableModel;


public class TableModelFilteredView extends TableModelPlugin {
    /*
    private boolean newFlag = true;
    private boolean oldFlag = true;
    private boolean answeredFlag = false;
    private boolean flaggedFlag = false;
    private boolean expungedFlag = false;
    private boolean attachmentFlag = false;
    */
    private String patternItem = new String("displayname");
    private String patternString = new String();
    private boolean dataFiltering = false;
    private HeaderItemList listClone;

    public TableModelFilteredView(AddressbookTableModel tableModel) {
        super(tableModel);
    }

    /************** filter view *********************/
    public void setDataFiltering(boolean b) throws Exception {
        dataFiltering = b;
        manipulateModel(TableModelPlugin.STRUCTURE_CHANGE);

        getTableModel().update();
    }

    public boolean getDataFiltering() {
        return dataFiltering;
    }

    public void setPatternItem(String s) {
        patternItem = s;
    }

    public void setPatternString(String s) throws Exception {
        patternString = s;

        //manipulateModel(TableModelPlugin.STRUCTURE_CHANGE);
    }

    public String getPatternItem() {
        return patternItem;
    }

    public String getPatternString() {
        return patternString;
    }

    public boolean addItem(HeaderItem header) {
        boolean result1 = false;

        if (!(getPatternString().equals(""))) {
            Object o = header.get(getPatternItem());

            if (o != null) {
                if (o instanceof String) {
                    String item = (String) o;

                    //System.out.println("add item?:"+item);
                    String pattern = getPatternString().toLowerCase();

                    if (item.indexOf(pattern) != -1) {
                        result1 = true;
                    } else {
                        result1 = false;
                    }
                } else {
                    result1 = false;
                }
            } else {
                result1 = false;
            }
        } else {
            result1 = true;
        }

        return result1;
    }

    public boolean manipulateModel(int mode) throws Exception {
        switch (mode) {
        case TableModelPlugin.STRUCTURE_CHANGE: {
            HeaderItemList list = getTableModel().getHeaderList();

            if (list == null) {
                return false;
            }

            if (list.count() == 0) {
                return false;
            }

            if (getDataFiltering() == true) {
                //System.out.println("starting filtering");
                HeaderItem item = null;

                for (int i = 0; i < list.count(); i++) {
                    item = list.get(i);

                    boolean result = addItem(item);

                    //ystem.out.println("item: "+i+" - result: "+result);
                    if (!result) {
                        //System.out.println("removing item:"+item);
                        list.getVector().remove(item);
                        i--;

                        /*
                        Object uid = list.getUid(i);
                        MessageNode childNode = new MessageNode( header, uid );
                        rootNode.add( childNode );
                        */
                    }
                }

                // System.out.println("finished filtering");
                return true;
            } else {
                // do not filter anything
                // System.out.println("do not filter anything");
                return false;
            }
        }

        case TableModelPlugin.NODES_INSERTED: {
            HeaderItem item = getTableModel().getSelectedItem();

            boolean result = addItem(item);

            return result;
        }
        }

        return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4284.java