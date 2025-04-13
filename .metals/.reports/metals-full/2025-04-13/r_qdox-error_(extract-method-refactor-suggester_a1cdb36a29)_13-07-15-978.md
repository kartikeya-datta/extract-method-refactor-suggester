error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3779.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3779.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3779.java
text:
```scala
C@@olumnFamily resolved = store.getColumnFamily(new NamesQueryFilter("key1", new QueryPath("Super2", "SC1".getBytes()), getBytes(2)), Integer.MAX_VALUE);

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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.Collection;

import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.cassandra.db.filter.IdentityQueryFilter;
import org.apache.cassandra.db.filter.NamesQueryFilter;
import org.apache.cassandra.db.filter.QueryPath;
import static org.apache.cassandra.Util.addMutation;
import static org.apache.cassandra.Util.getBytes;
import org.apache.cassandra.Util;
import static junit.framework.Assert.assertNotNull;

public class RemoveSuperColumnTest
{
    @Test
    public void testRemoveSuperColumn() throws IOException, ExecutionException, InterruptedException
    {
        ColumnFamilyStore store = Table.open("Keyspace1").getColumnFamilyStore("Super1");
        RowMutation rm;

        // add data
        rm = new RowMutation("Keyspace1", "key1");
        addMutation(rm, "Super1", "SC1", 1, "val1", 0);
        rm.apply();
        store.forceBlockingFlush();

        // remove
        rm = new RowMutation("Keyspace1", "key1");
        rm.delete(new QueryPath("Super1", "SC1".getBytes()), 1);
        rm.apply();

        validateRemoveTwoSources();

        store.forceBlockingFlush();
        validateRemoveTwoSources();

        Future<Integer> ft = CompactionManager.instance().submit(store, 2, 32);
        ft.get();
        assertEquals(1, store.getSSTables().size());
        validateRemoveCompacted();
    }

    @Test
    public void testRemoveDeletedSubColumn() throws IOException, ExecutionException, InterruptedException
    {
        ColumnFamilyStore store = Table.open("Keyspace1").getColumnFamilyStore("Super3");
        RowMutation rm;

        // add data
        rm = new RowMutation("Keyspace1", "key1");
        addMutation(rm, "Super3", "SC1", 1, "val1", 0);
        addMutation(rm, "Super3", "SC1", 2, "val1", 0);
        rm.apply();
        store.forceBlockingFlush();

        // remove
        rm = new RowMutation("Keyspace1", "key1");
        rm.delete(new QueryPath("Super3", "SC1".getBytes(), Util.getBytes(1)), 1);
        rm.apply();

        validateRemoveSubColumn();

        store.forceBlockingFlush();
        validateRemoveSubColumn();
    }

    private void validateRemoveSubColumn() throws IOException
    {
        ColumnFamilyStore store = Table.open("Keyspace1").getColumnFamilyStore("Super3");
        assertNull(store.getColumnFamily(new NamesQueryFilter("key1", new QueryPath("Super3", "SC1".getBytes()), Util.getBytes(1)), Integer.MAX_VALUE));
        assertNotNull(store.getColumnFamily(new NamesQueryFilter("key1", new QueryPath("Super3", "SC1".getBytes()), Util.getBytes(2)), Integer.MAX_VALUE));
    }

    private void validateRemoveTwoSources() throws IOException
    {
        ColumnFamilyStore store = Table.open("Keyspace1").getColumnFamilyStore("Super1");
        ColumnFamily resolved = store.getColumnFamily(new NamesQueryFilter("key1", new QueryPath("Super1"), "SC1".getBytes()));
        assert resolved.getSortedColumns().iterator().next().getMarkedForDeleteAt() == 1 : resolved;
        assert resolved.getSortedColumns().iterator().next().getSubColumns().size() == 0 : resolved;
        assertNull(ColumnFamilyStore.removeDeleted(resolved, Integer.MAX_VALUE));
        assertNull(store.getColumnFamily(new NamesQueryFilter("key1", new QueryPath("Super1"), "SC1".getBytes()), Integer.MAX_VALUE));
        assertNull(store.getColumnFamily(new IdentityQueryFilter("key1", new QueryPath("Super1")), Integer.MAX_VALUE));
        assertNull(ColumnFamilyStore.removeDeleted(store.getColumnFamily(new IdentityQueryFilter("key1", new QueryPath("Super1"))), Integer.MAX_VALUE));
    }

    private void validateRemoveCompacted() throws IOException
    {
        ColumnFamilyStore store = Table.open("Keyspace1").getColumnFamilyStore("Super1");
        ColumnFamily resolved = store.getColumnFamily(new NamesQueryFilter("key1", new QueryPath("Super1"), "SC1".getBytes()));
        assert resolved.getSortedColumns().iterator().next().getMarkedForDeleteAt() == 1;
        Collection<IColumn> subColumns = resolved.getSortedColumns().iterator().next().getSubColumns();
        assert subColumns.size() == 0;
    }

    @Test
    public void testRemoveSuperColumnWithNewData() throws IOException, ExecutionException, InterruptedException
    {
        ColumnFamilyStore store = Table.open("Keyspace1").getColumnFamilyStore("Super2");
        RowMutation rm;

        // add data
        rm = new RowMutation("Keyspace1", "key1");
        addMutation(rm, "Super2", "SC1", 1, "val1", 0);
        rm.apply();
        store.forceBlockingFlush();

        // remove
        rm = new RowMutation("Keyspace1", "key1");
        rm.delete(new QueryPath("Super2", "SC1".getBytes()), 1);
        rm.apply();

        // new data
        rm = new RowMutation("Keyspace1", "key1");
        addMutation(rm, "Super2", "SC1", 2, "val2", 2);
        rm.apply();

        validateRemoveWithNewData();

        store.forceBlockingFlush();
        validateRemoveWithNewData();

        Future<Integer> ft = CompactionManager.instance().submit(store, 2, 32);
        ft.get();
        assertEquals(1, store.getSSTables().size());
        validateRemoveWithNewData();
    }

    private void validateRemoveWithNewData() throws IOException
    {
        ColumnFamilyStore store = Table.open("Keyspace1").getColumnFamilyStore("Super2");
        ColumnFamily resolved = store.getColumnFamily(new NamesQueryFilter("key1", new QueryPath("Super2", "SC1".getBytes()), getBytes(2)));
        Collection<IColumn> subColumns = resolved.getSortedColumns().iterator().next().getSubColumns();
        assert subColumns.size() == 1;
        assert subColumns.iterator().next().timestamp() == 2;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3779.java