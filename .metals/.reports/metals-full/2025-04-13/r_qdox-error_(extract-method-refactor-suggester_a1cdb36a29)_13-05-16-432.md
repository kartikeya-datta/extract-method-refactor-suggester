error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8565.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8565.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8565.java
text:
```scala
S@@tring str = "";

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
package org.columba.addressbook.gui.table;

import org.columba.addressbook.config.AdapterNode;

import org.columba.core.config.DefaultXmlConfig;

import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;


public class GroupTableModel extends AbstractTableModel {
    private AdapterNode node;
    private List groupList;
    private String[] columnNames = {
        "Display Name", "Address", "First Name", "Last Name"
    };
    private int count;

    //private AddressbookXmlConfig config;
    protected DefaultXmlConfig config;

    public GroupTableModel(DefaultXmlConfig config) {
        super();

        //this.config = config;
        count = 0;
        groupList = new Vector();
    }

    public void setNode(AdapterNode n) {
        node = n;

        update();
    }

    public void update() {
        /*
        AdapterNode listNode = node.getChild("grouplist");
        count = listNode.getChildCount();

        GroupItem item = config.getGroupItem( node );
        if ( item != null ) System.out.println("item found") ;

        Vector ve = item.getListNodes();
        int uid;
        AdapterNode n, itemNode;

        groupList.clear();

        for ( int i=0; i<ve.size(); i++ )
        {
            n = ( AdapterNode ) ve.get(i);
            uid = (new Integer( n.getValue() ) ).intValue();
            itemNode = config.getNode( uid );
            groupList.addElement( itemNode );
        }

        fireTableDataChanged();
        */
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return count;
    }

    public String getColumnName(int col) {
        String s = (String) columnNames[col];

        return s;
    }

    protected int getColumnNumber(String str) {
        for (int i = 0; i < getColumnCount(); i++) {
            if (str.equals(getColumnName(i))) {
                return i;
            }
        }

        return -1;
    }

    public Object getValueAt(int row, int col) {
        AdapterNode contact;

        contact = (AdapterNode) groupList.get(row);

        if (contact == null) {
            return "";
        }

        AdapterNode child = null;
        String str = new String("");

        if (contact.getName().equals("contact")) {
            if (col == 0) {
                child = contact.getChild("displayname");

                str = child.getValue();
            } else if (col == 1) {
                child = contact.getChild("address");

                str = child.getCDATAValue();
            } else if (col == 2) {
                child = contact.getChild("firstname");
                str = child.getValue();
            }

            if (col == 3) {
                child = contact.getChild("lastname");
                str = child.getValue();
            }

            /*

            if ( col == 1 )
            {
              child = contact.getChild("firstname");

              str = child.getValue();
            }
            else if ( col == 0 )
            {
              child = contact.getChild("lastname");
              str = child.getValue();
            }
            if ( col == 2 )
            {
              child = contact.getChild("address");
              str = child.getCDATAValue();
            }
            */
        }

        return str;
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8565.java