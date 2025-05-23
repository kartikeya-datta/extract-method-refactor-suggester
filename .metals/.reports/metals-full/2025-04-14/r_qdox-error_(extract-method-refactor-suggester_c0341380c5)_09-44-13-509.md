error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3611.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3611.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3611.java
text:
```scala
s@@b.append(cfm == null ? "<anonymous>" : cfm.cfName);

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.cassandra.db;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.cassandra.config.CFMetaData;
import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.db.filter.QueryPath;
import org.apache.cassandra.db.marshal.AbstractType;
import org.apache.cassandra.io.ICompactSerializer2;
import org.apache.cassandra.io.util.IIterableColumns;
import org.apache.cassandra.utils.FBUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColumnFamily implements IColumnContainer, IIterableColumns
{
    private static Logger logger = LoggerFactory.getLogger(ColumnFamily.class);

    /* The column serializer for this Column Family. Create based on config. */
    private static ColumnFamilySerializer serializer = new ColumnFamilySerializer();

    public static ColumnFamilySerializer serializer()
    {
        return serializer;
    }

    public static ColumnFamily create(Integer cfId)
    {
        return create(DatabaseDescriptor.getCFMetaData(cfId));
    }

    public static ColumnFamily create(String tableName, String cfName)
    {
        return create(DatabaseDescriptor.getCFMetaData(tableName, cfName));
    }

    public static ColumnFamily create(CFMetaData cfm)
    {
        assert cfm != null;
        return new ColumnFamily(cfm.cfType, cfm.comparator, cfm.subcolumnComparator, cfm.cfId);
    }

    private final Integer cfid;
    private final ColumnFamilyType type;

    private transient ICompactSerializer2<IColumn> columnSerializer;
    final AtomicLong markedForDeleteAt = new AtomicLong(Long.MIN_VALUE);
    final AtomicInteger localDeletionTime = new AtomicInteger(Integer.MIN_VALUE);
    private ConcurrentSkipListMap<ByteBuffer, IColumn> columns;
    
    public ColumnFamily(ColumnFamilyType type, AbstractType comparator, AbstractType subcolumnComparator, Integer cfid)
    {
        this.type = type;
        columnSerializer = type == ColumnFamilyType.Standard ? Column.serializer() : SuperColumn.serializer(subcolumnComparator);
        columns = new ConcurrentSkipListMap<ByteBuffer, IColumn>(comparator);
        this.cfid = cfid;
     }
    
    public ColumnFamily cloneMeShallow()
    {
        ColumnFamily cf = new ColumnFamily(type, getComparator(), getSubComparator(), cfid);
        cf.markedForDeleteAt.set(markedForDeleteAt.get());
        cf.localDeletionTime.set(localDeletionTime.get());
        return cf;
    }

    public AbstractType getSubComparator()
    {
        return (columnSerializer instanceof SuperColumnSerializer) ? ((SuperColumnSerializer)columnSerializer).getComparator() : null;
    }

    public ColumnFamilyType getColumnFamilyType()
    {
        return type;
    }

    public ColumnFamily cloneMe()
    {
        ColumnFamily cf = cloneMeShallow();
        cf.columns = columns.clone();
        return cf;
    }

    public Integer id()
    {
        return cfid;
    }

    /**
     * @return The CFMetaData for this row, or null if the column family was dropped.
     */
    public CFMetaData metadata()
    {
        return DatabaseDescriptor.getCFMetaData(cfid);
    }

    /*
     *  We need to go through each column
     *  in the column family and resolve it before adding
    */
    public void addAll(ColumnFamily cf)
    {
        for (IColumn column : cf.getSortedColumns())
        {
            addColumn(column);
        }
        delete(cf);
    }

    /**
     * FIXME: Gross.
     */
    public ICompactSerializer2<IColumn> getColumnSerializer()
    {
        return columnSerializer;
    }

    int getColumnCount()
    {
        return columns.size();
    }

    public boolean isSuper()
    {
        return type == ColumnFamilyType.Super;
    }

    public void addColumn(QueryPath path, ByteBuffer value, long timestamp)
    {
        assert path.columnName != null : path;
        addColumn(path.superColumnName, new Column(path.columnName, value, timestamp));
    }

    public void addColumn(QueryPath path, ByteBuffer value, long timestamp, int timeToLive)
    {
        assert path.columnName != null : path;
        Column column;
        if (timeToLive > 0)
            column = new ExpiringColumn(path.columnName, value, timestamp, timeToLive);
        else
            column = new Column(path.columnName, value, timestamp);
        addColumn(path.superColumnName, column);
    }

    public void addTombstone(QueryPath path, ByteBuffer localDeletionTime, long timestamp)
    {
        assert path.columnName != null : path;
        addColumn(path.superColumnName, new DeletedColumn(path.columnName, localDeletionTime, timestamp));
    }

    public void addTombstone(QueryPath path, int localDeletionTime, long timestamp)
    {
        assert path.columnName != null : path;
        addColumn(path.superColumnName, new DeletedColumn(path.columnName, localDeletionTime, timestamp));
    }

    public void addTombstone(ByteBuffer name, int localDeletionTime, long timestamp)
    {
        addColumn(null, new DeletedColumn(name, localDeletionTime, timestamp));
    }

    public void addColumn(ByteBuffer superColumnName, Column column)
    {
        IColumn c;
        if (superColumnName == null)
        {
            c = column;
        }
        else
        {
            assert isSuper();
            c = new SuperColumn(superColumnName, getSubComparator());
            c.addColumn(column); // checks subcolumn name
        }
        addColumn(c);
    }

    public void clear()
    {
        columns.clear();
    }

    /*
     * If we find an old column that has the same name
     * the ask it to resolve itself else add the new column .
    */
    public void addColumn(IColumn column)
    {
        ByteBuffer name = column.name();
        IColumn oldColumn = columns.putIfAbsent(name, column);
        if (oldColumn != null)
        {
            if (oldColumn instanceof SuperColumn)
            {
                ((SuperColumn) oldColumn).putColumn(column);
            }
            else
            {
                // calculate reconciled col from old (existing) col and new col
                IColumn reconciledColumn = column.reconcile(oldColumn);
                while (!columns.replace(name, oldColumn, reconciledColumn))
                {
                    // if unable to replace, then get updated old (existing) col
                    oldColumn = columns.get(name);
                    // re-calculate reconciled col from updated old col and original new col
                    reconciledColumn = column.reconcile(oldColumn);
                    // try to re-update value, again
                }
            }
        }
    }

    public IColumn getColumn(ByteBuffer name)
    {
        return columns.get(name);
    }

    public SortedSet<ByteBuffer> getColumnNames()
    {
        return columns.keySet();
    }

    public Collection<IColumn> getSortedColumns()
    {
        return columns.values();
    }

    public Collection<IColumn> getReverseSortedColumns()
    {
        return columns.descendingMap().values();
    }

    public Map<ByteBuffer, IColumn> getColumnsMap()
    {
        return columns;
    }

    public void remove(ByteBuffer columnName)
    {
        columns.remove(columnName);
    }

    @Deprecated // TODO this is a hack to set initial value outside constructor
    public void delete(int localtime, long timestamp)
    {
        localDeletionTime.set(localtime);
        markedForDeleteAt.set(timestamp);
    }

    public void delete(ColumnFamily cf2)
    {
        FBUtilities.atomicSetMax(localDeletionTime, cf2.getLocalDeletionTime()); // do this first so we won't have a column that's "deleted" but has no local deletion time
        FBUtilities.atomicSetMax(markedForDeleteAt, cf2.getMarkedForDeleteAt());
    }

    public boolean isMarkedForDelete()
    {
        return markedForDeleteAt.get() > Long.MIN_VALUE;
    }

    /*
     * This function will calculate the difference between 2 column families.
     * The external input is assumed to be a superset of internal.
     */
    public ColumnFamily diff(ColumnFamily cfComposite)
    {
        ColumnFamily cfDiff = new ColumnFamily(cfComposite.type, getComparator(), getSubComparator(), cfComposite.id());
        if (cfComposite.getMarkedForDeleteAt() > getMarkedForDeleteAt())
        {
            cfDiff.delete(cfComposite.getLocalDeletionTime(), cfComposite.getMarkedForDeleteAt());
        }

        // (don't need to worry about cfNew containing IColumns that are shadowed by
        // the delete tombstone, since cfNew was generated by CF.resolve, which
        // takes care of those for us.)
        Map<ByteBuffer, IColumn> columns = cfComposite.getColumnsMap();
        Set<ByteBuffer> cNames = columns.keySet();
        for (ByteBuffer cName : cNames)
        {
            IColumn columnInternal = this.columns.get(cName);
            IColumn columnExternal = columns.get(cName);
            if (columnInternal == null)
            {
                cfDiff.addColumn(columnExternal);
            }
            else
            {
                IColumn columnDiff = columnInternal.diff(columnExternal);
                if (columnDiff != null)
                {
                    cfDiff.addColumn(columnDiff);
                }
            }
        }

        if (!cfDiff.getColumnsMap().isEmpty() || cfDiff.isMarkedForDelete())
            return cfDiff;
        return null;
    }

    public AbstractType getComparator()
    {
        return (AbstractType)columns.comparator();
    }

    int size()
    {
        int size = 0;
        for (IColumn column : columns.values())
        {
            size += column.size();
        }
        return size;
    }

    public int hashCode()
    {
        throw new RuntimeException("Not implemented.");
    }

    public boolean equals(Object o)
    {
        throw new RuntimeException("Not implemented.");
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder("ColumnFamily(");
        CFMetaData cfm = metadata();
        sb.append(cfm == null ? "-deleted-" : cfm.cfName);

        if (isMarkedForDelete())
            sb.append(" -deleted at " + getMarkedForDeleteAt() + "-");

        sb.append(" [").append(getComparator().getColumnsString(getSortedColumns())).append("])");
        return sb.toString();
    }

    public static ByteBuffer digest(ColumnFamily cf)
    {
        MessageDigest digest;
        try
        {
            digest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new AssertionError(e);
        }
        if (cf != null)
            cf.updateDigest(digest);

        return ByteBuffer.wrap(digest.digest());
    }

    public void updateDigest(MessageDigest digest)
    {
        for (IColumn column : columns.values())
            column.updateDigest(digest);
    }

    public long getMarkedForDeleteAt()
    {
        return markedForDeleteAt.get();
    }

    public int getLocalDeletionTime()
    {
        return localDeletionTime.get();
    }

    public static AbstractType getComparatorFor(String table, String columnFamilyName, ByteBuffer superColumnName)
    {
        return superColumnName == null
               ? DatabaseDescriptor.getComparator(table, columnFamilyName)
               : DatabaseDescriptor.getSubComparator(table, columnFamilyName);
    }

    public static ColumnFamily diff(ColumnFamily cf1, ColumnFamily cf2)
    {
        if (cf1 == null)
            return cf2;
        return cf1.diff(cf2);
    }

    public static ColumnFamily resolve(ColumnFamily cf1, ColumnFamily cf2)
    {
        if (cf1 == null)
            return cf2;
        cf1.resolve(cf2);
        return cf1;
    }

    public void resolve(ColumnFamily cf)
    {
        // Row _does_ allow null CF objects :(  seems a necessary evil for efficiency
        if (cf == null)
            return;
        addAll(cf);
    }

    public int getEstimatedColumnCount()
    {
        return getColumnCount();
    }

    public Iterator<IColumn> iterator()
    {
        return columns.values().iterator();
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3611.java