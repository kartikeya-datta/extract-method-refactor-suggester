error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/614.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/614.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/614.java
text:
```scala
public static final B@@yteBuffer COLUMN = ByteBufferUtil.bytes("birthdate");

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import org.apache.cassandra.CleanupHelper;
import org.apache.cassandra.Util;
import org.apache.cassandra.config.ConfigurationException;
import org.apache.cassandra.db.columniterator.IdentityQueryFilter;
import org.apache.cassandra.db.filter.IFilter;
import org.apache.cassandra.db.filter.QueryPath;
import org.apache.cassandra.dht.IPartitioner;
import org.apache.cassandra.dht.Range;
import org.apache.cassandra.locator.TokenMetadata;
import org.apache.cassandra.service.StorageService;
import org.apache.cassandra.thrift.IndexClause;
import org.apache.cassandra.thrift.IndexExpression;
import org.apache.cassandra.thrift.IndexOperator;
import org.apache.cassandra.utils.ByteBufferUtil;
import org.apache.cassandra.utils.NodeId;

public class CleanupTest extends CleanupHelper
{
    public static final int LOOPS = 200;
    public static final String TABLE1 = "Keyspace1";
    public static final String CF1 = "Indexed1";
    public static final String CF2 = "Standard1";
    public static final ByteBuffer COLUMN = ByteBuffer.wrap("birthdate".getBytes());
    public static final ByteBuffer VALUE = ByteBuffer.allocate(8);
    static
    {
        VALUE.putLong(20101229);
        VALUE.flip();
    }

    @Test
    public void testCleanup() throws IOException, ExecutionException, InterruptedException, ConfigurationException
    {
        StorageService.instance.initServer();

        Table table = Table.open(TABLE1);
        ColumnFamilyStore cfs = table.getColumnFamilyStore(CF2);

        List<Row> rows;

        // insert data and verify we get it back w/ range query
        fillCF(cfs, LOOPS);
        rows = cfs.getRangeSlice(null, Util.range("", ""), 1000, new IdentityQueryFilter());
        assertEquals(LOOPS, rows.size());

        // with one token in the ring, owned by the local node, cleanup should be a no-op
        CompactionManager.instance.performCleanup(cfs, new NodeId.OneShotRenewer());

        // check data is still there
        rows = cfs.getRangeSlice(null, Util.range("", ""), 1000, new IdentityQueryFilter());
        assertEquals(LOOPS, rows.size());
    }

    @Test
    public void testCleanupWithIndexes() throws IOException, ExecutionException, InterruptedException
    {
        Table table = Table.open(TABLE1);
        ColumnFamilyStore cfs = table.getColumnFamilyStore(CF1);
        assertEquals(cfs.getIndexedColumns().iterator().next(), COLUMN);

        List<Row> rows;

        // insert data and verify we get it back w/ range query
        fillCF(cfs, LOOPS);
        rows = cfs.getRangeSlice(null, Util.range("", ""), 1000, new IdentityQueryFilter());
        assertEquals(LOOPS, rows.size());

        ColumnFamilyStore cfi = cfs.getIndexedColumnFamilyStore(COLUMN);
        assertTrue(cfi.isIndexBuilt());

        // verify we get it back w/ index query too
        IndexExpression expr = new IndexExpression(COLUMN, IndexOperator.EQ, VALUE);
        IndexClause clause = new IndexClause(Arrays.asList(expr), ByteBufferUtil.EMPTY_BYTE_BUFFER, Integer.MAX_VALUE);
        IFilter filter = new IdentityQueryFilter();
        IPartitioner p = StorageService.getPartitioner();
        Range range = new Range(p.getMinimumToken(), p.getMinimumToken());
        rows = table.getColumnFamilyStore(CF1).scan(clause, range, filter);
        assertEquals(LOOPS, rows.size());

        // nuke our token so cleanup will remove everything
        TokenMetadata tmd = StorageService.instance.getTokenMetadata();
        tmd.clearUnsafe();
        assert StorageService.instance.getLocalRanges(TABLE1).isEmpty();

        CompactionManager.instance.performCleanup(cfs, new NodeId.OneShotRenewer());

        // row data should be gone
        rows = cfs.getRangeSlice(null, Util.range("", ""), 1000, new IdentityQueryFilter());
        assertEquals(0, rows.size());

        // not only should it be gone but there should be no data on disk, not even tombstones
        assert cfs.getSSTables().isEmpty();

        // 2ary indexes should result in no results, too (although tombstones won't be gone until compacted)
        rows = cfs.scan(clause, range, filter);
        assertEquals(0, rows.size());
    }

    protected void fillCF(ColumnFamilyStore cfs, int rowsPerSSTable) throws ExecutionException, InterruptedException, IOException
    {
        CompactionManager.instance.disableAutoCompaction();

        for (int i = 0; i < rowsPerSSTable; i++)
        {
            String key = String.valueOf(i);
            // create a row and update the birthdate value, test that the index query fetches the new version
            RowMutation rm;
            rm = new RowMutation(TABLE1, ByteBufferUtil.bytes(key));
            rm.add(new QueryPath(cfs.getColumnFamilyName(), null, COLUMN), VALUE, System.currentTimeMillis());
            rm.applyUnsafe();
        }

        cfs.forceBlockingFlush();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/614.java