error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/33.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/33.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/33.java
text:
```scala
F@@uture<Integer> ft = CompactionManager.instance.submitMinorIfNeeded(store);

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
import java.net.InetAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

import org.junit.Test;

import org.apache.cassandra.io.SSTableReader;
import org.apache.cassandra.CleanupHelper;
import org.apache.cassandra.db.filter.QueryPath;
import org.apache.cassandra.db.filter.IdentityQueryFilter;
import org.apache.cassandra.utils.FBUtilities;
import static junit.framework.Assert.assertEquals;

public class CompactionsTest extends CleanupHelper
{
    public static final String TABLE1 = "Keyspace1";
    public static final String TABLE2 = "Keyspace2";
    public static final InetAddress LOCAL = FBUtilities.getLocalAddress();

    @Test
    public void testCompactions() throws IOException, ExecutionException, InterruptedException
    {
        CompactionManager.instance.disableAutoCompaction();

        // this test does enough rows to force multiple block indexes to be used
        Table table = Table.open(TABLE1);
        ColumnFamilyStore store = table.getColumnFamilyStore("Standard1");

        final int ROWS_PER_SSTABLE = 10;
        Set<String> inserted = new HashSet<String>();
        for (int j = 0; j < (SSTableReader.indexInterval() * 3) / ROWS_PER_SSTABLE; j++) {
            for (int i = 0; i < ROWS_PER_SSTABLE; i++) {
                String key = String.valueOf(i % 2);
                RowMutation rm = new RowMutation(TABLE1, key);
                rm.add(new QueryPath("Standard1", null, String.valueOf(i / 2).getBytes()), new byte[0], j * ROWS_PER_SSTABLE + i);
                rm.apply();
                inserted.add(key);
            }
            store.forceBlockingFlush();
            assertEquals(inserted.size(), table.getColumnFamilyStore("Standard1").getKeyRange("", "", 10000).keys.size());
        }
        while (true)
        {
            Future<Integer> ft = CompactionManager.instance.submitMinor(store);
            if (ft.get() == 0)
                break;
        }
        if (store.getSSTables().size() > 1)
        {
            CompactionManager.instance.submitMajor(store).get();
        }
        assertEquals(inserted.size(), table.getColumnFamilyStore("Standard1").getKeyRange("", "", 10000).keys.size());
    }

    @Test
    public void testCompactionPurge() throws IOException, ExecutionException, InterruptedException
    {
        CompactionManager.instance.disableAutoCompaction();

        Table table = Table.open(TABLE1);
        String cfName = "Standard1";
        ColumnFamilyStore store = table.getColumnFamilyStore(cfName);

        String key = "key1";
        RowMutation rm;

        // inserts
        rm = new RowMutation(TABLE1, key);
        for (int i = 0; i < 10; i++)
        {
            rm.add(new QueryPath(cfName, null, String.valueOf(i).getBytes()), new byte[0], 0);
        }
        rm.apply();
        store.forceBlockingFlush();

        // deletes
        for (int i = 0; i < 10; i++)
        {
            rm = new RowMutation(TABLE1, key);
            rm.delete(new QueryPath(cfName, null, String.valueOf(i).getBytes()), 1);
            rm.apply();
        }
        store.forceBlockingFlush();

        // resurrect one column
        rm = new RowMutation(TABLE1, key);
        rm.add(new QueryPath(cfName, null, String.valueOf(5).getBytes()), new byte[0], 2);
        rm.apply();
        store.forceBlockingFlush();

        // verify that non-major compaction does no GC to ensure correctness (see CASSANDRA-604)
        Collection<SSTableReader> sstablesIncomplete = store.getSSTables();
        rm = new RowMutation(TABLE1, key + "x");
        rm.add(new QueryPath(cfName, null, "0".getBytes()), new byte[0], 0);
        rm.apply();
        store.forceBlockingFlush();
        CompactionManager.instance.doCompaction(store, sstablesIncomplete, CompactionManager.getDefaultGCBefore());
        ColumnFamily cf = table.getColumnFamilyStore(cfName).getColumnFamily(new IdentityQueryFilter(key, new QueryPath(cfName)));
        assert cf.getColumnCount() == 10;

        // major compact and test that all columns but the resurrected one is completely gone
        CompactionManager.instance.submitMajor(store, 0, Integer.MAX_VALUE).get();
        cf = table.getColumnFamilyStore(cfName).getColumnFamily(new IdentityQueryFilter(key, new QueryPath(cfName)));
        assert cf.getColumnCount() == 1;
        assert cf.getColumn(String.valueOf(5).getBytes()) != null;
    }

    @Test
    public void testCompactionPurgeOneFile() throws IOException, ExecutionException, InterruptedException
    {
        CompactionManager.instance.disableAutoCompaction();

        Table table = Table.open(TABLE1);
        String cfName = "Standard2";
        ColumnFamilyStore store = table.getColumnFamilyStore(cfName);

        String key = "key1";
        RowMutation rm;

        // inserts
        rm = new RowMutation(TABLE1, key);
        for (int i = 0; i < 5; i++)
        {
            rm.add(new QueryPath(cfName, null, String.valueOf(i).getBytes()), new byte[0], 0);
        }
        rm.apply();

        // deletes
        for (int i = 0; i < 5; i++)
        {
            rm = new RowMutation(TABLE1, key);
            rm.delete(new QueryPath(cfName, null, String.valueOf(i).getBytes()), 1);
            rm.apply();
        }
        store.forceBlockingFlush();

        assert store.getSSTables().size() == 1 : store.getSSTables(); // inserts & deletes were in the same memtable -> only deletes in sstable

        // compact and test that the row is completely gone
        CompactionManager.instance.submitMajor(store, 0, Integer.MAX_VALUE).get();
        assert store.getSSTables().isEmpty();
        ColumnFamily cf = table.getColumnFamilyStore(cfName).getColumnFamily(new IdentityQueryFilter(key, new QueryPath(cfName)));
        assert cf == null : cf;
    }

    @Test
    public void testCompactionReadonly() throws IOException, ExecutionException, InterruptedException
    {
        CompactionManager.instance.disableAutoCompaction();

        Table table = Table.open(TABLE2);
        ColumnFamilyStore store = table.getColumnFamilyStore("Standard1");

        final int ROWS_PER_SSTABLE = 10;
        Set<String> inserted = new HashSet<String>();
        for (int j = 0; j < (SSTableReader.indexInterval() * 3) / ROWS_PER_SSTABLE; j++) {
            for (int i = 0; i < ROWS_PER_SSTABLE; i++) {
                String key = String.valueOf(i % 2);
                RowMutation rm = new RowMutation(TABLE2, key);
                rm.add(new QueryPath("Standard1", null, String.valueOf(i / 2).getBytes()), new byte[0], j * ROWS_PER_SSTABLE + i);
                rm.apply();
                inserted.add(key);
            }
            store.forceBlockingFlush();
            assertEquals(inserted.size(), table.getColumnFamilyStore("Standard1").getKeyRange("", "", 10000).keys.size());
        }

        // perform readonly compaction and confirm that no sstables changed
        ArrayList<SSTableReader> oldsstables = new ArrayList<SSTableReader>(store.getSSTables());
        CompactionManager.instance.submitReadonly(store, LOCAL).get();
        assertEquals(oldsstables, new ArrayList<SSTableReader>(store.getSSTables()));
        assertEquals(inserted.size(), table.getColumnFamilyStore("Standard1").getKeyRange("", "", 10000).keys.size());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/33.java