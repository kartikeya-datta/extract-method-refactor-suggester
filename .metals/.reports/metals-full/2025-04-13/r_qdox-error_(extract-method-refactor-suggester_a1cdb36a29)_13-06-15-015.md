error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/613.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/613.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/613.java
text:
```scala
r@@m.add(new QueryPath("Super3", ByteBufferUtil.bytes("sc"), ByteBufferUtil.bytes(String.valueOf(i))), ByteBuffer.wrap(new byte[ROWS_PER_SSTABLE * 10 - i * 2]), i);

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
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import org.apache.cassandra.Util;

import org.junit.Test;

import org.apache.cassandra.io.sstable.SSTableReader;
import org.apache.cassandra.CleanupHelper;
import org.apache.cassandra.db.filter.QueryPath;
import org.apache.cassandra.utils.FBUtilities;
import static junit.framework.Assert.assertEquals;
import org.apache.cassandra.utils.ByteBufferUtil;


public class RowIterationTest extends CleanupHelper
{
    public static final String TABLE1 = "Keyspace2";
    public static final InetAddress LOCAL = FBUtilities.getLocalAddress();

    @Test
    public void testRowIteration() throws IOException, ExecutionException, InterruptedException
    {
        Table table = Table.open(TABLE1);
        ColumnFamilyStore store = table.getColumnFamilyStore("Super3");

        final int ROWS_PER_SSTABLE = 10;
        Set<DecoratedKey> inserted = new HashSet<DecoratedKey>();
        for (int i = 0; i < ROWS_PER_SSTABLE; i++) {
            DecoratedKey key = Util.dk(String.valueOf(i));
            RowMutation rm = new RowMutation(TABLE1, key.key);
            rm.add(new QueryPath("Super3", ByteBufferUtil.bytes("sc"), ByteBuffer.wrap(String.valueOf(i).getBytes())), ByteBuffer.wrap(new byte[ROWS_PER_SSTABLE * 10 - i * 2]), i);
            rm.apply();
            inserted.add(key);
        }
        store.forceBlockingFlush();
        assertEquals(inserted.toString(), inserted.size(), Util.getRangeSlice(store).size());
    }

    @Test
    public void testRowIterationDeletionTime() throws IOException, ExecutionException, InterruptedException
    {
        Table table = Table.open(TABLE1);
        String CF_NAME = "Standard3";
        ColumnFamilyStore store = table.getColumnFamilyStore(CF_NAME);
        DecoratedKey key = Util.dk("key");

        // Delete row in first sstable
        RowMutation rm = new RowMutation(TABLE1, key.key);
        rm.delete(new QueryPath(CF_NAME, null, null), 0);
        rm.add(new QueryPath(CF_NAME, null, ByteBufferUtil.bytes("c")), ByteBufferUtil.bytes("values"), 0L);
        int tstamp1 = rm.getColumnFamilies().iterator().next().getLocalDeletionTime();
        rm.apply();
        store.forceBlockingFlush();

        // Delete row in second sstable with higher timestamp
        rm = new RowMutation(TABLE1, key.key);
        rm.delete(new QueryPath(CF_NAME, null, null), 1);
        rm.add(new QueryPath(CF_NAME, null, ByteBufferUtil.bytes("c")), ByteBufferUtil.bytes("values"), 1L);
        int tstamp2 = rm.getColumnFamilies().iterator().next().getLocalDeletionTime();
        rm.apply();
        store.forceBlockingFlush();

        ColumnFamily cf = Util.getRangeSlice(store).iterator().next().cf;
        assert cf.getMarkedForDeleteAt() == 1L;
        assert cf.getLocalDeletionTime() == tstamp2;
    }

    @Test
    public void testRowIterationDeletion() throws IOException, ExecutionException, InterruptedException
    {
        Table table = Table.open(TABLE1);
        String CF_NAME = "Standard3";
        ColumnFamilyStore store = table.getColumnFamilyStore(CF_NAME);
        DecoratedKey key = Util.dk("key");

        // Delete a row in first sstable
        RowMutation rm = new RowMutation(TABLE1, key.key);
        rm.delete(new QueryPath(CF_NAME, null, null), 0);
        rm.apply();
        store.forceBlockingFlush();

        ColumnFamily cf = Util.getRangeSlice(store).iterator().next().cf;
        assert cf != null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/613.java