error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5462.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5462.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5462.java
text:
```scala
L@@ist<Object> subList = new ArrayList<Object>(objects.subList(start, end));

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.jorphan.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.reflect.Functor;
import org.apache.log.Logger;

/**
 * The ObjectTableModel is a TableModel whose rows are objects;
 * columns are defined as Functors on the object.
 */
public class ObjectTableModel extends DefaultTableModel {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final long serialVersionUID = 240L;

    private transient ArrayList<Object> objects = new ArrayList<Object>();

    private transient List<String> headers = new ArrayList<String>();

    private transient ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

    private transient ArrayList<Functor> readFunctors = new ArrayList<Functor>();

    private transient ArrayList<Functor> writeFunctors = new ArrayList<Functor>();

    private transient Class<?> objectClass = null; // if provided
    
    private transient boolean cellEditable = true;

    /**
     * The ObjectTableModel is a TableModel whose rows are objects;
     * columns are defined as Functors on the object.
     *
     * @param headers - Column names
     * @param _objClass - Object class that will be used
     * @param readFunctors - used to get the values
     * @param writeFunctors - used to set the values
     * @param editorClasses - class for each column
     */
    public ObjectTableModel(String[] headers, Class<?> _objClass, Functor[] readFunctors, Functor[] writeFunctors, Class<?>[] editorClasses) {
        this(headers, readFunctors, writeFunctors, editorClasses);
        this.objectClass=_objClass;
    }
    
    /**
     * The ObjectTableModel is a TableModel whose rows are objects;
     * columns are defined as Functors on the object.
     *
     * @param headers - Column names
     * @param _objClass - Object class that will be used
     * @param readFunctors - used to get the values
     * @param writeFunctors - used to set the values
     * @param editorClasses - class for each column
     * @param cellEditable - if cell must editable (false to allow double click on cell)
     */
    public ObjectTableModel(String[] headers, Class<?> _objClass, Functor[] readFunctors, 
            Functor[] writeFunctors, Class<?>[] editorClasses, boolean cellEditable) {
        this(headers, readFunctors, writeFunctors, editorClasses);
        this.objectClass=_objClass;
        this.cellEditable = cellEditable;
    }

    /**
     * The ObjectTableModel is a TableModel whose rows are objects;
     * columns are defined as Functors on the object.
     *
     * @param headers - Column names
     * @param readFunctors - used to get the values
     * @param writeFunctors - used to set the values
     * @param editorClasses - class for each column
     */
    public ObjectTableModel(String[] headers, Functor[] readFunctors, Functor[] writeFunctors, Class<?>[] editorClasses) {
        this.headers.addAll(Arrays.asList(headers));
        this.classes.addAll(Arrays.asList(editorClasses));
        this.readFunctors = new ArrayList<Functor>(Arrays.asList(readFunctors));
        this.writeFunctors = new ArrayList<Functor>(Arrays.asList(writeFunctors));

        int numHeaders = headers.length;

        int numClasses = classes.size();
        if (numClasses != numHeaders){
            log.warn("Header count="+numHeaders+" but classes count="+numClasses);
        }

        // Functor count = 0 is handled specially
        int numWrite = writeFunctors.length;
        if (numWrite > 0 && numWrite != numHeaders){
            log.warn("Header count="+numHeaders+" but writeFunctor count="+numWrite);
        }

        int numRead = readFunctors.length;
        if (numRead > 0 && numRead != numHeaders){
            log.warn("Header count="+numHeaders+" but readFunctor count="+numRead);
        }
    }

    private Object readResolve() {
        objects = new ArrayList<Object>();
        headers = new ArrayList<String>();
        classes = new ArrayList<Class<?>>();
        readFunctors = new ArrayList<Functor>();
        writeFunctors = new ArrayList<Functor>();
        return this;
    }

    public Iterator<?> iterator() {
        return objects.iterator();
    }

    public void clearData() {
        int size = getRowCount();
        objects.clear();
        super.fireTableRowsDeleted(0, size);
    }

    public void addRow(Object value) {
        log.debug("Adding row value: " + value);
        if (objectClass != null) {
            final Class<?> valueClass = value.getClass();
            if (!objectClass.isAssignableFrom(valueClass)){
                throw new IllegalArgumentException("Trying to add class: "+valueClass.getName()
                        +"; expecting class: "+objectClass.getName());
            }
        }
        objects.add(value);
        super.fireTableRowsInserted(objects.size() - 1, objects.size());
    }

    public void insertRow(Object value, int index) {
        objects.add(index, value);
        super.fireTableRowsInserted(index, index + 1);
    }

    /** {@inheritDoc} */
    @Override
    public int getColumnCount() {
        return headers.size();
    }

    /** {@inheritDoc} */
    @Override
    public String getColumnName(int col) {
        return headers.get(col);
    }

    /** {@inheritDoc} */
    @Override
    public int getRowCount() {
        if (objects == null) {
            return 0;
        }
        return objects.size();
    }

    /** {@inheritDoc} */
    @Override
    public Object getValueAt(int row, int col) {
        log.debug("Getting row value");
        Object value = objects.get(row);
        if(headers.size() == 1 && col >= readFunctors.size()) {
            return value;
        }
        Functor getMethod = readFunctors.get(col);
        if (getMethod != null && value != null) {
            return getMethod.invoke(value);
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isCellEditable(int arg0, int arg1) {
        return cellEditable;
    }

    /** {@inheritDoc} */
    @Override
    public void moveRow(int start, int end, int to) {
        List<Object> subList = new ArrayList(objects.subList(start, end));
        for (int x = end - 1; x >= start; x--) {
            objects.remove(x);
        }
        objects.addAll(to, subList);
        super.fireTableChanged(new TableModelEvent(this));
    }

    /** {@inheritDoc} */
    @Override
    public void removeRow(int row) {
        objects.remove(row);
        super.fireTableRowsDeleted(row, row);
    }

    /** {@inheritDoc} */
    @Override
    public void setValueAt(Object cellValue, int row, int col) {
        if (row < objects.size()) {
            Object value = objects.get(row);
            if (col < writeFunctors.size()) {
                Functor setMethod = writeFunctors.get(col);
                if (setMethod != null) {
                    setMethod.invoke(value, new Object[] { cellValue });
                    super.fireTableDataChanged();
                }
            }
            else if(headers.size() == 1)
            {
                objects.set(row,cellValue);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public Class<?> getColumnClass(int arg0) {
        return classes.get(arg0);
    }

    /**
     * Check all registered functors.
     * <p>
     * <b>** only for use in unit test code **</b>
     * </p>
     *
     * @param _value - an instance of the table model row data item
     * (if null, use the class passed to the constructor).
     *
     * @param caller - class of caller.
     *
     * @return false if at least one Functor cannot be found.
     */
    @SuppressWarnings("deprecation")
    public boolean checkFunctors(Object _value, Class<?> caller){
        Object value;
        if (_value == null && objectClass != null) {
            try {
                value = objectClass.newInstance();
            } catch (InstantiationException e) {
                log.error("Cannot create instance of class "+objectClass.getName(),e);
                return false;
            } catch (IllegalAccessException e) {
                log.error("Cannot create instance of class "+objectClass.getName(),e);
                return false;
            }
        } else {
            value = _value;
        }
        boolean status = true;
        for(int i=0;i<getColumnCount();i++){
            Functor setMethod = writeFunctors.get(i);
            if (setMethod != null) {
                if (!setMethod.checkMethod(value,getColumnClass(i))){
                    status=false;
                    log.warn(caller.getName()+" is attempting to use nonexistent "+setMethod.toString());
                }
            }
            Functor getMethod = readFunctors.get(i);
            if (getMethod != null) {
                if (!getMethod.checkMethod(value)){
                    status=false;
                    log.warn(caller.getName()+" is attempting to use nonexistent "+getMethod.toString());
                }
            }

        }
        return status;
    }

    public Object getObjectList() { // used by TableEditor
        return objects;
    }

    public void setRows(Iterable<?> rows) { // used by TableEditor
        clearData();
        for(Object val : rows)
        {
            addRow(val);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5462.java