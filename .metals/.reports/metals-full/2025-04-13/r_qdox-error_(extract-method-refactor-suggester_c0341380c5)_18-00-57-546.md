error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13757.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13757.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,16]

error in qdox parser
file content:
```java
offset: 16
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13757.java
text:
```scala
private static L@@ogger log = LoggingManager.getLoggerForClass();

package org.apache.jorphan.gui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * @version $Revision$
 */
public class ObjectTableModel extends DefaultTableModel
{
    private static Logger log = LoggingManager.getLoggerFor("jorphan.gui");
    private transient ArrayList objects = new ArrayList();
    private transient List headers = new ArrayList();
    private transient ArrayList classes = new ArrayList();
    private transient Class objectClass;

    private transient ArrayList setMethods = new ArrayList();
    private transient ArrayList getMethods = new ArrayList();

    public ObjectTableModel(String[] headers, String[] propertyNames, Class[] propertyClasses, Class[] renderClasses, Object sampleObject)
    {
        this.headers.addAll(Arrays.asList(headers));
        this.classes.addAll(Arrays.asList(renderClasses));
        objectClass = sampleObject.getClass();
        Class[] emptyClasses = new Class[0];
        for (int i = 0; i < propertyNames.length; i++)
        {
            propertyNames[i] =
                propertyNames[i].substring(0, 1).toUpperCase()
                    + propertyNames[i].substring(1);
            try
            {
                if (!propertyClasses[i].equals(Boolean.class)
                    && !propertyClasses[i].equals(boolean.class))
                {
                    getMethods.add(
                        objectClass.getMethod(
                            "get" + propertyNames[i],
                            emptyClasses));
                }
                else
                {
                    getMethods.add(
                        objectClass.getMethod(
                            "is" + propertyNames[i],
                            emptyClasses));
                }
                setMethods.add(objectClass.getMethod("set" + propertyNames[i], new Class[] { propertyClasses[i] }));
            }
            catch (NoSuchMethodException e)
            {
                log.error("Invalid Method name for class: " + objectClass, e);
            }
        }
    }

    public Iterator iterator()
    {
        return objects.iterator();
    }

    public void clearData()
    {
        int size = getRowCount();
        objects.clear();
        super.fireTableRowsDeleted(0, size);
    }

    public void addRow(Object value)
    {
        objects.add(value);
        super.fireTableRowsInserted(objects.size() - 1, objects.size());
    }

    /**
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount()
    {
        return headers.size();
    }

    /**
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    public String getColumnName(int col)
    {
        return (String) headers.get(col);
    }

    /**
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount()
    {
        if (objects == null)
        {
            return 0;
        }
        return objects.size();
    }

    /**
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int col)
    {
        Object value = objects.get(row);
        Method getMethod = (Method) getMethods.get(col);
        try
        {
            return getMethod.invoke(value, new Object[0]);
        }
        catch (IllegalAccessException e)
        {
            log.error("Illegal method access", e);
        }
        catch (InvocationTargetException e)
        {
            log.error("incorrect method access", e);
        }
        return null;
    }

    /**
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int arg0, int arg1)
    {
        return true;
    }

    /**
     * @see javax.swing.table.DefaultTableModel#moveRow(int, int, int)
     */
    public void moveRow(int start, int end, int to)
    {
        List subList = objects.subList(start, end);
        for (int x = end - 1; x >= start; x--)
        {
            objects.remove(x);
        }
        objects.addAll(to, subList);
        super.fireTableChanged(new TableModelEvent(this));
    }

    /**
     * @see javax.swing.table.DefaultTableModel#removeRow(int)
     */
    public void removeRow(int row)
    {
        objects.remove(row);
        super.fireTableRowsDeleted(row, row);
    }

    /**
     * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
     */
    public void setValueAt(Object cellValue, int row, int col)
    {
        if (row < objects.size())
        {
            Object value = objects.get(row);
            if (col < setMethods.size())
            {
                Method setMethod = (Method) setMethods.get(col);
                try
                {
                    setMethod.invoke(value, new Object[] { cellValue });
                }
                catch (IllegalAccessException e)
                {
                    log.error("Illegal method access", e);
                }
                catch (InvocationTargetException e)
                {
                    log.error("incorrect method access", e);
                }
                super.fireTableDataChanged();
            }
        }
    }

    /**
     * @see javax.swing.table.TableModel#getColumnClass(int)
     */
    public Class getColumnClass(int arg0)
    {
        return (Class) classes.get(arg0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13757.java