error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8187.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8187.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8187.java
text:
```scala
f@@ireTableDataChanged();

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

import org.columba.addressbook.folder.HeaderItem;
import org.columba.addressbook.folder.HeaderItemList;
import org.columba.addressbook.gui.table.util.HeaderColumnInterface;
import org.columba.addressbook.gui.table.util.TableModelFilteredView;
import org.columba.addressbook.gui.table.util.TableModelPlugin;
import org.columba.addressbook.gui.table.util.TableModelSorter;

import org.columba.core.logging.ColumbaLogger;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;


public class AddressbookTableModel extends AbstractTableModel {
    private List columns;
    private Hashtable table;
    private HeaderItemList rows;
    private List tableModelPlugins;
    private HeaderItem selected;
    public boolean editable = false;

    public AddressbookTableModel() {
        super();

        rows = new HeaderItemList();

        columns = new Vector();

        tableModelPlugins = new Vector();
    }

    public void registerPlugin(TableModelPlugin plugin) {
        tableModelPlugins.add(plugin);
    }

    public int getSize() {
        return getHeaderList().count();
    }

    public void removeItem(Object[] items) {
        for (int i = 0; i < items.length; i++) {
            getHeaderList().remove((HeaderItem) items[i]);
        }

        // recreate whole tablemodel
        update();
    }

    public TableModelFilteredView getTableModelFilteredView() {
        return (TableModelFilteredView) tableModelPlugins.get(0);
    }

    public TableModelSorter getTableModelSorter() {
        return (TableModelSorter) tableModelPlugins.get(1);
    }

    public HeaderItemList getHeaderList() {
        return rows;
    }

    public void update() {
        fireTableDataChanged();
    }

    public HeaderItem getSelectedItem() {
        return selected;
    }

    public void setSelectedItem(HeaderItem selected) {
        this.selected = selected;
    }

    public void addItem(HeaderItem item) throws Exception {
        ColumbaLogger.log.info("item=" + item.toString());

        int count = 0;

        if (getHeaderList() != null) {
            count = getHeaderList().count();
        }

        if (count == 0) {
            rows = new HeaderItemList();

            // first message
            getHeaderList().add(item);

            //fireTableRowsInserted(0, 0);
            update();
        } else {
            setSelectedItem(item);

            boolean result = true;

            if (tableModelPlugins.size() != 0) {
                result = getTableModelFilteredView().manipulateModel(TableModelPlugin.NODES_INSERTED);
            }

            if (result == true) {
                if (tableModelPlugins.size() != 0) {
                    int index = getTableModelSorter().getInsertionSortIndex(item);

                    getHeaderList().insertElementAt(item, index);

                    fireTableRowsInserted(index, index);
                } else {
                    getHeaderList().add(item);

                    //fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
                    update();
                }
            }
        }
    }

    public void addColumn(HeaderColumnInterface column) {
        String name = column.getName();

        columns.add(column);

        fireTableStructureChanged();
    }

    public HeaderColumnInterface getHeaderColumn(String name) {
        int index = getColumnNumber(name);

        return (HeaderColumnInterface) columns.get(index);
    }

    public void setHeaderList(HeaderItemList list) {
        if (list == null) {
            ColumbaLogger.log.info("list == null");
            rows = new HeaderItemList();

            fireTableDataChanged();

            return;
        }

        ColumbaLogger.log.info("list size=" + list.count());

        List clone = (Vector) ((Vector) list.getVector()).clone();
        rows = new HeaderItemList(clone);

        if (tableModelPlugins.size() != 0) {
            getTableModelSorter().manipulateModel(TableModelPlugin.STRUCTURE_CHANGE);
        }

        fireTableDataChanged();
    }

    public void setHeaderItem(int row, HeaderItem item) {
        rows.replace(row, item);

        //fireTableDataChanged();
    }

    public int getColumnCount() {
        return columns.size();
    }

    public int getRowCount() {
        if (rows == null) {
            return 0;
        } else {
            return rows.count();
        }
    }

    public String getColumnName(int col) {
        HeaderColumnInterface column = (HeaderColumnInterface) columns.get(col);
        String name = column.getName();

        return name;
    }

    public int getColumnNumber(String str) {
        for (int i = 0; i < getColumnCount(); i++) {
            if (str.equals(getColumnName(i))) {
                return i;
            }
        }

        return -1;
    }

    public HeaderItem getHeaderItem(int row) {
        if (rows == null) {
            return null;
        }

        HeaderItem item = rows.get(row);

        return item;
    }

    public Object getValueAt(int row, int col) {
        if (rows == null) {
            return null;
        }

        HeaderItem item = rows.get(row);

        HeaderColumnInterface column = (HeaderColumnInterface) columns.get(col);
        String name = column.getName();

        Object value = column.getValue(item);

        return value;
    }

    public Class getColumnClass(int c) {
        if (rows == null) {
            return null;
        }

        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col) {
        if (editable == false) {
            return false;
        }

        if ((col == 0) || (col == 1)) {
            return true;
        } else {
            return false;
        }
    }

    public void setValueAt(Object value, int row, int col) {
        if (col == 1) {
            HeaderItem item = rows.get(row);

            item.add("displayname", value);
            fireTableCellUpdated(row, col);
        } else if (col == 0) {
            HeaderItem item = rows.get(row);
            item.add("field", value);
            fireTableCellUpdated(row, col);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8187.java