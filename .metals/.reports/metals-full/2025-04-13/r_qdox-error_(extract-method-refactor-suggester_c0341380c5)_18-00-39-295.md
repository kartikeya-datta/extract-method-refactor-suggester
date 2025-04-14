error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1818.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1818.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1818.java
text:
```scala
S@@STableReader orig = SSTableUtils.prepare().cf("Indexed1").writeRaw(entries);

package org.apache.cassandra.io.sstable;
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


import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.cassandra.CleanupHelper;
import org.apache.cassandra.db.*;
import org.apache.cassandra.db.filter.IFilter;
import org.apache.cassandra.db.columniterator.IdentityQueryFilter;
import org.apache.cassandra.db.filter.QueryPath;
import org.apache.cassandra.dht.IPartitioner;
import org.apache.cassandra.dht.Range;
import org.apache.cassandra.io.util.DataOutputBuffer;
import org.apache.cassandra.io.util.FileUtils;
import org.apache.cassandra.service.StorageService;
import org.apache.cassandra.thrift.IndexClause;
import org.apache.cassandra.thrift.IndexExpression;
import org.apache.cassandra.thrift.IndexOperator;
import org.apache.cassandra.utils.FBUtilities;
import org.junit.Test;
import org.apache.cassandra.utils.ByteBufferUtil;

public class SSTableWriterTest extends CleanupHelper {

    @Test
    public void testRecoverAndOpen() throws IOException, ExecutionException, InterruptedException
    {
        RowMutation rm;

        rm = new RowMutation("Keyspace1", ByteBufferUtil.bytes("k1"));
        rm.add(new QueryPath("Indexed1", null, ByteBufferUtil.bytes("birthdate")), FBUtilities.toByteBuffer(1L), 0);
        rm.apply();
        
        ColumnFamily cf = ColumnFamily.create("Keyspace1", "Indexed1");        
        cf.addColumn(new Column(ByteBufferUtil.bytes("birthdate"), FBUtilities.toByteBuffer(1L), 0));
        cf.addColumn(new Column(ByteBufferUtil.bytes("anydate"), FBUtilities.toByteBuffer(1L), 0));
        
        Map<ByteBuffer, ByteBuffer> entries = new HashMap<ByteBuffer, ByteBuffer>();
        
        DataOutputBuffer buffer = new DataOutputBuffer();
        ColumnFamily.serializer().serializeWithIndexes(cf, buffer);
        entries.put(ByteBufferUtil.bytes("k2"), ByteBuffer.wrap(Arrays.copyOf(buffer.getData(), buffer.getLength())));        
        cf.clear();
        
        cf.addColumn(new Column(ByteBufferUtil.bytes("anydate"), FBUtilities.toByteBuffer(1L), 0));
        buffer = new DataOutputBuffer();
        ColumnFamily.serializer().serializeWithIndexes(cf, buffer);               
        entries.put(ByteBufferUtil.bytes("k3"), ByteBuffer.wrap(Arrays.copyOf(buffer.getData(), buffer.getLength())));
        
        SSTableReader orig = SSTableUtils.writeRawSSTable("Keyspace1", "Indexed1", entries);        
        // whack the index to trigger the recover
        FileUtils.deleteWithConfirm(orig.descriptor.filenameFor(Component.PRIMARY_INDEX));
        FileUtils.deleteWithConfirm(orig.descriptor.filenameFor(Component.FILTER));

        SSTableReader sstr = CompactionManager.instance.submitSSTableBuild(orig.descriptor).get();
        assert sstr != null;
        ColumnFamilyStore cfs = Table.open("Keyspace1").getColumnFamilyStore("Indexed1");
        cfs.addSSTable(sstr);
        cfs.buildSecondaryIndexes(cfs.getSSTables(), cfs.getIndexedColumns());
        
        IndexExpression expr = new IndexExpression(ByteBufferUtil.bytes("birthdate"), IndexOperator.EQ, FBUtilities.toByteBuffer(1L));
        IndexClause clause = new IndexClause(Arrays.asList(expr), FBUtilities.EMPTY_BYTE_BUFFER, 100);
        IFilter filter = new IdentityQueryFilter();
        IPartitioner p = StorageService.getPartitioner();
        Range range = new Range(p.getMinimumToken(), p.getMinimumToken());
        List<Row> rows = cfs.scan(clause, range, filter);
        
        assertEquals("IndexExpression should return two rows on recoverAndOpen", 2, rows.size());
        assertTrue("First result should be 'k1'",ByteBufferUtil.bytes("k1").equals(rows.get(0).key.key));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1818.java