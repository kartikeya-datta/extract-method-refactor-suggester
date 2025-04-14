error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3635.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3635.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3635.java
text:
```scala
l@@ong k = subColumn.name().getLong(subColumn.name().position());

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

import static junit.framework.Assert.assertEquals;
import static org.apache.cassandra.Util.addMutation;
import static org.apache.cassandra.Util.column;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

import org.apache.cassandra.CleanupHelper;
import org.apache.cassandra.Util;
import org.apache.cassandra.db.commitlog.CommitLog;
import org.apache.cassandra.db.filter.QueryPath;
import org.apache.cassandra.utils.ByteBufferUtil;

import org.junit.Test;

public class NameSortTest extends CleanupHelper
{
    @Test
    public void testNameSort1() throws IOException, ExecutionException, InterruptedException
    {
        // single key
        testNameSort(1);
    }

    @Test
    public void testNameSort10() throws IOException, ExecutionException, InterruptedException
    {
        // multiple keys, flushing concurrently w/ inserts
        testNameSort(10);
    }

    @Test
    public void testNameSort100() throws IOException, ExecutionException, InterruptedException
    {
        // enough keys to force compaction concurrently w/ inserts
        testNameSort(100);
    }

    private void testNameSort(int N) throws IOException, ExecutionException, InterruptedException
    {
        Table table = Table.open("Keyspace1");

        for (int i = 0; i < N; ++i)
        {
            ByteBuffer key = ByteBuffer.wrap(Integer.toString(i).getBytes());
            RowMutation rm;

            // standard
            for (int j = 0; j < 8; ++j)
            {
                ByteBuffer bytes = ByteBuffer.wrap(j % 2 == 0 ? "a".getBytes() : "b".getBytes());
                rm = new RowMutation("Keyspace1", key);
                rm.add(new QueryPath("Standard1", null, ByteBuffer.wrap(("Column-" + j).getBytes())), bytes, j);
                rm.applyUnsafe();
            }

            // super
            for (int j = 0; j < 8; ++j)
            {
                rm = new RowMutation("Keyspace1", key);
                for (int k = 0; k < 4; ++k)
                {
                    String value = (j + k) % 2 == 0 ? "a" : "b";
                    addMutation(rm, "Super1", "SuperColumn-" + j, k, value, k);
                }
                rm.applyUnsafe();
            }
        }

        validateNameSort(table, N);

        table.getColumnFamilyStore("Standard1").forceBlockingFlush();
        table.getColumnFamilyStore("Super1").forceBlockingFlush();
        validateNameSort(table, N);
    }

    private void validateNameSort(Table table, int N) throws IOException
    {
        for (int i = 0; i < N; ++i)
        {
            DecoratedKey key = Util.dk(Integer.toString(i));
            ColumnFamily cf;

            cf = Util.getColumnFamily(table, key, "Standard1");
            Collection<IColumn> columns = cf.getSortedColumns();
            for (IColumn column : columns)
            {
                String name = ByteBufferUtil.string(column.name());
                int j = Integer.valueOf(name.substring(name.length() - 1));
                byte[] bytes = j % 2 == 0 ? "a".getBytes() : "b".getBytes();
                assertEquals(new String(bytes), ByteBufferUtil.string(column.value()));
            }

            cf = Util.getColumnFamily(table, key, "Super1");
            assert cf != null : "key " + key + " is missing!";
            Collection<IColumn> superColumns = cf.getSortedColumns();
            assert superColumns.size() == 8 : cf;
            for (IColumn superColumn : superColumns)
            {
                int j = Integer.valueOf(ByteBufferUtil.string(superColumn.name()).split("-")[1]);
                Collection<IColumn> subColumns = superColumn.getSubColumns();
                assert subColumns.size() == 4;
                for (IColumn subColumn : subColumns)
                {
                    long k = subColumn.name().getLong(subColumn.name().position() + subColumn.name().arrayOffset());
                    byte[] bytes = (j + k) % 2 == 0 ? "a".getBytes() : "b".getBytes();
                    assertEquals(new String(bytes), ByteBufferUtil.string(subColumn.value()));
                }
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3635.java