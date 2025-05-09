error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/628.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/628.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/628.java
text:
```scala
b@@oolean cfIrrelevant = ColumnFamilyStore.removeDeletedCF(emptyColumnFamily, gcBefore) == null;

package org.apache.cassandra.io;

import java.io.DataOutput;
import java.io.IOError;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.*;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import org.apache.commons.collections.iterators.CollatingIterator;

import org.apache.cassandra.db.*;
import org.apache.cassandra.db.marshal.AbstractType;
import org.apache.cassandra.io.sstable.SSTableIdentityIterator;
import org.apache.cassandra.io.util.DataOutputBuffer;
import org.apache.cassandra.io.util.IIterableColumns;
import org.apache.cassandra.utils.FBUtilities;
import org.apache.cassandra.utils.ReducingIterator;

/**
 * LazilyCompactedRow only computes the row bloom filter and column index in memory
 * (at construction time); it does this by reading one column at a time from each
 * of the rows being compacted, and merging them as it does so.  So the most we have
 * in memory at a time is the bloom filter, the index, and one column from each
 * pre-compaction row.
 *
 * When write() or update() is called, a second pass is made over the pre-compaction
 * rows to write the merged columns or update the hash, again with at most one column
 * from each row deserialized at a time.
 */
public class LazilyCompactedRow extends AbstractCompactedRow implements IIterableColumns
{
    private final List<SSTableIdentityIterator> rows;
    private final boolean major;
    private final int gcBefore;
    private final DataOutputBuffer headerBuffer;
    private ColumnFamily emptyColumnFamily;
    private LazyColumnIterator iter;
    private int columnCount;
    private long columnSerializedSize;

    public LazilyCompactedRow(List<SSTableIdentityIterator> rows, boolean major, int gcBefore)
    {
        super(rows.get(0).getKey());
        this.major = major;
        this.gcBefore = gcBefore;
        this.rows = new ArrayList<SSTableIdentityIterator>(rows);

        for (SSTableIdentityIterator row : rows)
        {
            ColumnFamily cf = row.getColumnFamily();

            if (emptyColumnFamily == null)
                emptyColumnFamily = cf;
            else
                emptyColumnFamily.delete(cf);
        }

        // initialize row header so isEmpty can be called
        headerBuffer = new DataOutputBuffer();
        ColumnIndexer.serialize(this, headerBuffer);
        // reach into iterator used by ColumnIndexer to get column count and size
        columnCount = iter.size;
        columnSerializedSize = iter.serializedSize;
        iter = null;
    }

    public void write(DataOutput out) throws IOException
    {
        if (rows.size() == 1 && !major)
        {
            SSTableIdentityIterator row = rows.get(0);
            out.writeLong(row.getDataSize());
            row.echoData(out);
            return;
        }

        DataOutputBuffer clockOut = new DataOutputBuffer();
        ColumnFamily.serializer().serializeCFInfo(emptyColumnFamily, clockOut);

        out.writeLong(headerBuffer.getLength() + clockOut.getLength() + columnSerializedSize);
        out.write(headerBuffer.getData(), 0, headerBuffer.getLength());
        out.write(clockOut.getData(), 0, clockOut.getLength());
        out.writeInt(columnCount);

        Iterator<IColumn> iter = iterator();
        while (iter.hasNext())
        {
            IColumn column = iter.next();
            emptyColumnFamily.getColumnSerializer().serialize(column, out);
        }
    }

    public void update(MessageDigest digest)
    {
        // no special-case for rows.size == 1, we're actually skipping some bytes here so just
        // blindly updating everything wouldn't be correct
        digest.update(headerBuffer.getData(), 0, headerBuffer.getLength());
        DataOutputBuffer out = new DataOutputBuffer();
        Iterator<IColumn> iter = iterator();
        while (iter.hasNext())
        {
            IColumn column = iter.next();
            out.reset();
            try
            {
                emptyColumnFamily.getColumnSerializer().serialize(column, out);
            }
            catch (IOException e)
            {
                throw new IOError(e);
            }
            digest.update(out.getData(), 0, out.getLength());
        }
    }

    public boolean isEmpty()
    {
        boolean cfIrrelevant = ColumnFamilyStore.removeDeleted(emptyColumnFamily, gcBefore) == null;
        return cfIrrelevant && columnCount == 0;
    }

    public int getEstimatedColumnCount()
    {
        int n = 0;
        for (SSTableIdentityIterator row : rows)
            n += row.getColumnCount();
        return n;
    }

    public AbstractType getComparator()
    {
        return emptyColumnFamily.getComparator();
    }

    public Iterator<IColumn> iterator()
    {
        for (SSTableIdentityIterator row : rows)
        {
            row.reset();
        }
        Comparator<IColumn> nameComparator = new Comparator<IColumn>()
        {
            public int compare(IColumn o1, IColumn o2)
            {
                return getComparator().compare(o1.name(), o2.name());
            }
        };
        iter = new LazyColumnIterator(new CollatingIterator(nameComparator, rows));
        return Iterators.filter(iter, Predicates.notNull());
    }

    private class LazyColumnIterator extends ReducingIterator<IColumn, IColumn>
    {
        ColumnFamily container = emptyColumnFamily.cloneMeShallow();
        long serializedSize = 4; // int for column count
        int size = 0;

        public LazyColumnIterator(Iterator<IColumn> source)
        {
            super(source);
        }

        @Override
        protected boolean isEqual(IColumn o1, IColumn o2)
        {
            return Arrays.equals(o1.name(), o2.name());
        }

        public void reduce(IColumn current)
        {
            container.addColumn(current);
        }

        protected IColumn getReduced()
        {
            assert container != null;
            IColumn reduced = container.iterator().next();
            ColumnFamily purged = major ? ColumnFamilyStore.removeDeleted(container, gcBefore) : container;
            if (purged == null || !purged.iterator().hasNext())
            {
                container.clear();
                return null;
            }
            container.clear();
            serializedSize += reduced.serializedSize();
            size++;
            return reduced;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/628.java