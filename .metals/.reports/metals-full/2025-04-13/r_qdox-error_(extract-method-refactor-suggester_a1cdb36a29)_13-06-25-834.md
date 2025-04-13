error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3693.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3693.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3693.java
text:
```scala
S@@treamOut.transferSSTables(session, ssTableReaders, ranges, OperationType.BOOTSTRAP);

package org.apache.cassandra.streaming;

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

import static junit.framework.Assert.assertEquals;
import static org.apache.cassandra.Util.column;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.*;

import org.apache.cassandra.CleanupHelper;
import org.apache.cassandra.Util;
import org.apache.cassandra.config.CFMetaData;
import org.apache.cassandra.db.*;
import org.apache.cassandra.db.columniterator.IdentityQueryFilter;
import org.apache.cassandra.db.filter.IFilter;
import org.apache.cassandra.db.filter.QueryFilter;
import org.apache.cassandra.db.filter.QueryPath;
import org.apache.cassandra.dht.IPartitioner;
import org.apache.cassandra.dht.Range;
import org.apache.cassandra.io.sstable.SSTableUtils;
import org.apache.cassandra.io.sstable.SSTableReader;
import org.apache.cassandra.service.StorageService;
import org.apache.cassandra.thrift.IndexClause;
import org.apache.cassandra.thrift.IndexExpression;
import org.apache.cassandra.thrift.IndexOperator;
import org.apache.cassandra.utils.FBUtilities;

import org.junit.BeforeClass;
import org.junit.Test;
import org.apache.cassandra.utils.ByteBufferUtil;

public class StreamingTransferTest extends CleanupHelper
{
    public static final InetAddress LOCAL = FBUtilities.getLocalAddress();

    @BeforeClass
    public static void setup() throws Exception
    {
        StorageService.instance.initServer();
    }

    @Test
    public void testTransferTable() throws Exception
    {
        Table table = Table.open("Keyspace1");
        ColumnFamilyStore cfs = table.getColumnFamilyStore("Indexed1");
        
        // write a temporary SSTable, and unregister it
        for (int i = 1; i <= 3; i++)
        {
            String key = "key" + i;
            RowMutation rm = new RowMutation("Keyspace1", ByteBuffer.wrap(key.getBytes()));
            ColumnFamily cf = ColumnFamily.create(table.name, cfs.columnFamily);
            cf.addColumn(column(key, "v", 0));
            cf.addColumn(new Column(ByteBufferUtil.bytes("birthdate"), ByteBufferUtil.bytes((long) i), 0));
            rm.add(cf);
            rm.apply();
        }
        cfs.forceBlockingFlush();
        assert cfs.getSSTables().size() == 1;
        SSTableReader sstable = cfs.getSSTables().iterator().next();
        cfs.removeAllSSTables();

        // transfer the first and last key
        IPartitioner p = StorageService.getPartitioner();
        List<Range> ranges = new ArrayList<Range>();
        ranges.add(new Range(p.getMinimumToken(), p.getToken(ByteBufferUtil.bytes("key1"))));
        ranges.add(new Range(p.getToken(ByteBufferUtil.bytes("key2")), p.getMinimumToken()));
        StreamOutSession session = StreamOutSession.create(table.name, LOCAL, null);
        StreamOut.transferSSTables(session, Arrays.asList(sstable), ranges, OperationType.BOOTSTRAP);
        session.await();

        // confirm that the SSTable was transferred and registered
        List<Row> rows = Util.getRangeSlice(cfs);
        assertEquals(2, rows.size());
        assert rows.get(0).key.key.equals( ByteBufferUtil.bytes("key1"));
        assert rows.get(1).key.key.equals( ByteBufferUtil.bytes("key3"));
        assertEquals(2, rows.get(0).cf.getColumnsMap().size());
        assertEquals(2, rows.get(1).cf.getColumnsMap().size());
        assert rows.get(1).cf.getColumn(ByteBufferUtil.bytes("key3")) != null;

        // and that the index and filter were properly recovered
        assert null != cfs.getColumnFamily(QueryFilter.getIdentityFilter(Util.dk("key1"), new QueryPath(cfs.columnFamily)));
        assert null != cfs.getColumnFamily(QueryFilter.getIdentityFilter(Util.dk("key3"), new QueryPath(cfs.columnFamily)));

        // and that the secondary index works
        IndexExpression expr = new IndexExpression(ByteBufferUtil.bytes("birthdate"), IndexOperator.EQ, ByteBufferUtil.bytes(3L));
        IndexClause clause = new IndexClause(Arrays.asList(expr), ByteBufferUtil.EMPTY_BYTE_BUFFER, 100);
        IFilter filter = new IdentityQueryFilter();
        Range range = new Range(p.getMinimumToken(), p.getMinimumToken());
        rows = cfs.scan(clause, range, filter);
        assertEquals(1, rows.size());
        assert rows.get(0).key.key.equals( ByteBufferUtil.bytes("key3")) ;
    }

    @Test
    public void testTransferTableMultiple() throws Exception
    {
        // write a temporary SSTable, but don't register it
        Set<String> content = new HashSet<String>();
        content.add("transfer1");
        content.add("transfer2");
        content.add("transfer3");
        SSTableReader sstable = SSTableUtils.prepare().write(content);
        String tablename = sstable.getTableName();
        String cfname = sstable.getColumnFamilyName();

        Set<String> content2 = new HashSet<String>();
        content2.add("test");
        content2.add("test2");
        content2.add("test3");
        SSTableReader sstable2 = SSTableUtils.prepare().write(content2);

        // transfer the first and last key
        IPartitioner p = StorageService.getPartitioner();
        List<Range> ranges = new ArrayList<Range>();
        ranges.add(new Range(p.getMinimumToken(), p.getToken(ByteBufferUtil.bytes("transfer1"))));
        ranges.add(new Range(p.getToken(ByteBufferUtil.bytes("test2")), p.getMinimumToken()));
        StreamOutSession session = StreamOutSession.create(tablename, LOCAL, null);
        StreamOut.transferSSTables(session, Arrays.asList(sstable, sstable2), ranges, OperationType.BOOTSTRAP);
        session.await();

        // confirm that the SSTable was transferred and registered
        ColumnFamilyStore cfstore = Table.open(tablename).getColumnFamilyStore(cfname);
        List<Row> rows = Util.getRangeSlice(cfstore);
        assertEquals(6, rows.size());
        assert rows.get(0).key.key.equals( ByteBufferUtil.bytes("test"));
        assert rows.get(3).key.key.equals(ByteBuffer.wrap( "transfer1".getBytes() ));
        assert rows.get(0).cf.getColumnsMap().size() == 1;
        assert rows.get(3).cf.getColumnsMap().size() == 1;

        // these keys fall outside of the ranges and should not be transferred.
        assert null != cfstore.getColumnFamily(QueryFilter.getIdentityFilter(Util.dk("transfer2"), new QueryPath("Standard1")));
        assert null != cfstore.getColumnFamily(QueryFilter.getIdentityFilter(Util.dk("transfer3"), new QueryPath("Standard1")));
        
        // and that the index and filter were properly recovered
        assert null != cfstore.getColumnFamily(QueryFilter.getIdentityFilter(Util.dk("test"), new QueryPath("Standard1")));
        assert null != cfstore.getColumnFamily(QueryFilter.getIdentityFilter(Util.dk("transfer1"), new QueryPath("Standard1")));
    }

    @Test
    public void testTransferOfMultipleColumnFamilies() throws Exception
    {
        String keyspace = "Keyspace1";
        IPartitioner p = StorageService.getPartitioner();
        String[] columnFamilies = new String[] { "Standard1", "Standard2", "Standard3" };
        List<SSTableReader> ssTableReaders = new ArrayList<SSTableReader>();

        // ranges to transfer
        List<Range> ranges = new ArrayList<Range>();

        for (String cf : columnFamilies)
        {
            Set<String> content = new HashSet<String>();

            content.add("data-" + cf + "-1");
            content.add("data-" + cf + "-2");
            content.add("data-" + cf + "-3");

            SSTableUtils.Context context = SSTableUtils.prepare().ks(keyspace).cf(cf);

            ssTableReaders.add(context.write(content));
            ranges.add(new Range(p.getMinimumToken(), p.getToken(ByteBufferUtil.bytes("data-" + cf + "-3"))));
        }

        StreamOutSession session = StreamOutSession.create(keyspace, LOCAL, null);
        StreamOut.transferSSTables(session, ssTableReaders, ranges);

        session.await();

        for (String cf : columnFamilies)
        {
            ColumnFamilyStore store = Table.open(keyspace).getColumnFamilyStore(cf);
            List<Row> rows = Util.getRangeSlice(store);

            assert rows.size() >= 3;

            for (int i = 0; i < 3; i++)
            {
                String expectedKey = "data-" + cf + "-" + (i + 1);
                assertEquals(p.decorateKey(ByteBufferUtil.bytes(expectedKey)), rows.get(i).key);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3693.java