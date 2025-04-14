error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/986.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/986.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/986.java
text:
```scala
a@@ssertEquals(10, cf.getColumnCount());

/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.apache.cassandra.db;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import org.apache.cassandra.CleanupHelper;
import org.apache.cassandra.db.filter.QueryFilter;
import org.apache.cassandra.db.filter.QueryPath;
import org.apache.cassandra.io.sstable.Descriptor;
import org.apache.cassandra.io.sstable.SSTableReader;
import org.apache.cassandra.Util;

import static junit.framework.Assert.assertEquals;
import static org.apache.cassandra.db.TableTest.assertColumns;
import org.apache.cassandra.utils.ByteBufferUtil;


public class CompactionsPurgeTest extends CleanupHelper
{
    public static final String TABLE1 = "Keyspace1";
    public static final String TABLE2 = "Keyspace2";

    @Test
    public void testMajorCompactionPurge() throws IOException, ExecutionException, InterruptedException
    {
        CompactionManager.instance.disableAutoCompaction();

        Table table = Table.open(TABLE1);
        String cfName = "Standard1";
        ColumnFamilyStore cfs = table.getColumnFamilyStore(cfName);

        DecoratedKey key = Util.dk("key1");
        RowMutation rm;

        // inserts
        rm = new RowMutation(TABLE1, key.key);
        for (int i = 0; i < 10; i++)
        {
            rm.add(new QueryPath(cfName, null, ByteBufferUtil.bytes(String.valueOf(i))), ByteBufferUtil.EMPTY_BYTE_BUFFER, 0);
        }
        rm.apply();
        cfs.forceBlockingFlush();

        // deletes
        for (int i = 0; i < 10; i++)
        {
            rm = new RowMutation(TABLE1, key.key);
            rm.delete(new QueryPath(cfName, null, ByteBufferUtil.bytes(String.valueOf(i))), 1);
            rm.apply();
        }
        cfs.forceBlockingFlush();

        // resurrect one column
        rm = new RowMutation(TABLE1, key.key);
        rm.add(new QueryPath(cfName, null, ByteBufferUtil.bytes(String.valueOf(5))), ByteBufferUtil.EMPTY_BYTE_BUFFER, 2);
        rm.apply();
        cfs.forceBlockingFlush();

        // major compact and test that all columns but the resurrected one is completely gone
        CompactionManager.instance.submitMajor(cfs, 0, Integer.MAX_VALUE).get();
        cfs.invalidateCachedRow(key);
        ColumnFamily cf = cfs.getColumnFamily(QueryFilter.getIdentityFilter(key, new QueryPath(cfName)));
        assertColumns(cf, "5");
        assert cf.getColumn(ByteBufferUtil.bytes(String.valueOf(5))) != null;
    }

    @Test
    public void testMinorCompactionPurge() throws IOException, ExecutionException, InterruptedException
    {
        CompactionManager.instance.disableAutoCompaction();

        Table table = Table.open(TABLE2);
        String cfName = "Standard1";
        ColumnFamilyStore cfs = table.getColumnFamilyStore(cfName);

        RowMutation rm;
        for (int k = 1; k <= 2; ++k) {
            DecoratedKey key = Util.dk("key" + k);

            // inserts
            rm = new RowMutation(TABLE2, key.key);
            for (int i = 0; i < 10; i++)
            {
                rm.add(new QueryPath(cfName, null, ByteBufferUtil.bytes(String.valueOf(i))), ByteBufferUtil.EMPTY_BYTE_BUFFER, 0);
            }
            rm.apply();
            cfs.forceBlockingFlush();

            // deletes
            for (int i = 0; i < 10; i++)
            {
                rm = new RowMutation(TABLE2, key.key);
                rm.delete(new QueryPath(cfName, null, ByteBufferUtil.bytes(String.valueOf(i))), 1);
                rm.apply();
            }
            cfs.forceBlockingFlush();
        }

        DecoratedKey key1 = Util.dk("key1");
        DecoratedKey key2 = Util.dk("key2");

        // flush, remember the current sstable and then resurrect one column
        // for first key. Then submit minor compaction on remembered sstables.
        cfs.forceBlockingFlush();
        Collection<SSTableReader> sstablesIncomplete = cfs.getSSTables();
        rm = new RowMutation(TABLE2, key1.key);
        rm.add(new QueryPath(cfName, null, ByteBufferUtil.bytes(String.valueOf(5))), ByteBufferUtil.EMPTY_BYTE_BUFFER, 2);
        rm.apply();
        cfs.forceBlockingFlush();
        CompactionManager.instance.doCompaction(cfs, sstablesIncomplete, Integer.MAX_VALUE);

        // verify that minor compaction does not GC when key is present
        // in a non-compacted sstable
        ColumnFamily cf = cfs.getColumnFamily(QueryFilter.getIdentityFilter(key1, new QueryPath(cfName)));
        assert cf.getColumnCount() == 10;

        // verify that minor compaction does GC when key is provably not
        // present in a non-compacted sstable
        cf = cfs.getColumnFamily(QueryFilter.getIdentityFilter(key2, new QueryPath(cfName)));
        assert cf == null;
    }

    @Test
    public void testCompactionPurgeOneFile() throws IOException, ExecutionException, InterruptedException
    {
        CompactionManager.instance.disableAutoCompaction();

        Table table = Table.open(TABLE1);
        String cfName = "Standard2";
        ColumnFamilyStore cfs = table.getColumnFamilyStore(cfName);

        DecoratedKey key = Util.dk("key1");
        RowMutation rm;

        // inserts
        rm = new RowMutation(TABLE1, key.key);
        for (int i = 0; i < 5; i++)
        {
            rm.add(new QueryPath(cfName, null, ByteBufferUtil.bytes(String.valueOf(i))), ByteBufferUtil.EMPTY_BYTE_BUFFER, 0);
        }
        rm.apply();

        // deletes
        for (int i = 0; i < 5; i++)
        {
            rm = new RowMutation(TABLE1, key.key);
            rm.delete(new QueryPath(cfName, null, ByteBufferUtil.bytes(String.valueOf(i))), 1);
            rm.apply();
        }
        cfs.forceBlockingFlush();
        assert cfs.getSSTables().size() == 1 : cfs.getSSTables(); // inserts & deletes were in the same memtable -> only deletes in sstable

        // compact and test that the row is completely gone
        Util.compactAll(cfs).get();
        assert cfs.getSSTables().isEmpty();
        ColumnFamily cf = table.getColumnFamilyStore(cfName).getColumnFamily(QueryFilter.getIdentityFilter(key, new QueryPath(cfName)));
        assert cf == null : cf;
    }

    @Test
    public void testCompactionPurgeTombstonedRow() throws IOException, ExecutionException, InterruptedException
    {
        CompactionManager.instance.disableAutoCompaction();

        String tableName = "RowCacheSpace";
        String cfName = "CachedCF";
        Table table = Table.open(tableName);
        ColumnFamilyStore cfs = table.getColumnFamilyStore(cfName);

        DecoratedKey key = Util.dk("key3");
        RowMutation rm;

        // inserts
        rm = new RowMutation(tableName, key.key);
        for (int i = 0; i < 10; i++)
        {
            rm.add(new QueryPath(cfName, null, ByteBufferUtil.bytes(String.valueOf(i))), ByteBufferUtil.EMPTY_BYTE_BUFFER, 0);
        }
        rm.apply();

        // move the key up in row cache
        cfs.getColumnFamily(QueryFilter.getIdentityFilter(key, new QueryPath(cfName)));

        // deletes row
        rm = new RowMutation(tableName, key.key);
        rm.delete(new QueryPath(cfName, null, null), 1);
        rm.apply();

        // flush and major compact
        cfs.forceBlockingFlush();
        Util.compactAll(cfs).get();

        // re-inserts with timestamp lower than delete
        rm = new RowMutation(tableName, key.key);
        for (int i = 0; i < 10; i++)
        {
            rm.add(new QueryPath(cfName, null, ByteBufferUtil.bytes(String.valueOf(i))), ByteBufferUtil.EMPTY_BYTE_BUFFER, 0);
        }
        rm.apply();

        // Check that the second insert did went in
        ColumnFamily cf = cfs.getColumnFamily(QueryFilter.getIdentityFilter(key, new QueryPath(cfName)));
        assert cf.getColumnCount() == 10;
        for (IColumn c : cf)
            assert !c.isMarkedForDelete();
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/986.java