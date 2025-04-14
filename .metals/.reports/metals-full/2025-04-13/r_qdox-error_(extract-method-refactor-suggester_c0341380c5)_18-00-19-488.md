error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/675.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/675.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,17]

error in qdox parser
file content:
```java
offset: 17
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/675.java
text:
```scala
public abstract C@@olumnIterator getSSTableColumnIterator(SSTableReader sstable);

package org.apache.cassandra.db.filter;
/*
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */


import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Arrays;

import org.apache.cassandra.io.sstable.SSTableReader;
import org.apache.cassandra.utils.ReducingIterator;
import org.apache.cassandra.db.*;
import org.apache.cassandra.db.marshal.AbstractType;

public abstract class QueryFilter
{
    public final String key;
    public final QueryPath path;

    protected QueryFilter(String key, QueryPath path)
    {
        this.key = key;
        this.path = path;
    }

    /**
     * returns an iterator that returns columns from the given memtable
     * matching the Filter criteria in sorted order.
     */
    public abstract ColumnIterator getMemColumnIterator(Memtable memtable, ColumnFamily cf, AbstractType comparator);

    public ColumnIterator getMemColumnIterator(Memtable memtable, AbstractType comparator)
    {
        return getMemColumnIterator(memtable, memtable.getColumnFamily(key), comparator);
    }

    /**
     * returns an iterator that returns columns from the given SSTable
     * matching the Filter criteria in sorted order.
     */
    public abstract ColumnIterator getSSTableColumnIterator(SSTableReader sstable) throws IOException;

    /**
     * collects columns from reducedColumns into returnCF.  Termination is determined
     * by the filter code, which should have some limit on the number of columns
     * to avoid running out of memory on large rows.
     */
    public abstract void collectReducedColumns(IColumnContainer container, Iterator<IColumn> reducedColumns, int gcBefore);

    /**
     * subcolumns of a supercolumn are unindexed, so to pick out parts of those we operate in-memory.
     * @param superColumn may be modified by filtering op.
     */
    public abstract SuperColumn filterSuperColumn(SuperColumn superColumn, int gcBefore);

    public Comparator<IColumn> getColumnComparator(final AbstractType comparator)
    {
        return new Comparator<IColumn>()
        {
            public int compare(IColumn c1, IColumn c2)
            {
                return comparator.compare(c1.name(), c2.name());
            }
        };
    }
    
    public void collectCollatedColumns(final ColumnFamily returnCF, Iterator<IColumn> collatedColumns, int gcBefore)
    {
        // define a 'reduced' iterator that merges columns w/ the same name, which
        // greatly simplifies computing liveColumns in the presence of tombstones.
        ReducingIterator<IColumn, IColumn> reduced = new ReducingIterator<IColumn, IColumn>(collatedColumns)
        {
            ColumnFamily curCF = returnCF.cloneMeShallow();

            protected boolean isEqual(IColumn o1, IColumn o2)
            {
                return Arrays.equals(o1.name(), o2.name());
            }

            public void reduce(IColumn current)
            {
                curCF.addColumn(current);
            }

            protected IColumn getReduced()
            {
                IColumn c = curCF.getSortedColumns().iterator().next();
                curCF.clear();
                return c;
            }
        };

        collectReducedColumns(returnCF, reduced, gcBefore);
    }

    public String getColumnFamilyName()
    {
        return path.columnFamilyName;
    }

    public static boolean isRelevant(IColumn column, IColumnContainer container, int gcBefore)
    {
        // the column itself must be not gc-able (it is live, or a still relevant tombstone, or has live subcolumns), (1)
        // and if its container is deleted, the column must be changed more recently than the container tombstone (2)
        // (since otherwise, the only thing repair cares about is the container tombstone)
        long maxChange = column.mostRecentLiveChangeAt();
        return (!column.isMarkedForDelete() || column.getLocalDeletionTime() > gcBefore || maxChange > column.getMarkedForDeleteAt()) // (1)
               && (!container.isMarkedForDelete() || maxChange > container.getMarkedForDeleteAt()); // (2)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/675.java