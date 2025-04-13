error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1935.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1935.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1935.java
text:
```scala
B@@yteBuffer val = cf.getColumn(ByteBufferUtil.bytes(cName)).value();

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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.TreeMap;

import org.apache.cassandra.SchemaLoader;
import org.junit.Test;

import org.apache.cassandra.io.util.DataOutputBuffer;
import org.apache.cassandra.db.filter.QueryPath;
import static org.apache.cassandra.Util.column;
import org.apache.cassandra.utils.ByteBufferUtil;


public class ColumnFamilyTest extends SchemaLoader
{
    // TODO test SuperColumns

    @Test
    public void testSingleColumn() throws IOException
    {
        ColumnFamily cf;

        cf = ColumnFamily.create("Keyspace1", "Standard1");
        cf.addColumn(column("C", "v", 1));
        DataOutputBuffer bufOut = new DataOutputBuffer();
        ColumnFamily.serializer().serialize(cf, bufOut);

        ByteArrayInputStream bufIn = new ByteArrayInputStream(bufOut.getData(), 0, bufOut.getLength());
        cf = ColumnFamily.serializer().deserialize(new DataInputStream(bufIn));
        assert cf != null;
        assert cf.metadata().cfName.equals("Standard1");
        assert cf.getSortedColumns().size() == 1;
    }

    @Test
    public void testManyColumns() throws IOException
    {
        ColumnFamily cf;

        TreeMap<String, String> map = new TreeMap<String, String>();
        for (int i = 100; i < 1000; ++i)
        {
            map.put(Integer.toString(i), "Avinash Lakshman is a good man: " + i);
        }

        // write
        cf = ColumnFamily.create("Keyspace1", "Standard1");
        DataOutputBuffer bufOut = new DataOutputBuffer();
        for (String cName : map.navigableKeySet())
        {
            cf.addColumn(column(cName, map.get(cName), 314));
        }
        ColumnFamily.serializer().serialize(cf, bufOut);

        // verify
        ByteArrayInputStream bufIn = new ByteArrayInputStream(bufOut.getData(), 0, bufOut.getLength());
        cf = ColumnFamily.serializer().deserialize(new DataInputStream(bufIn));
        for (String cName : map.navigableKeySet())
        {
            ByteBuffer val = cf.getColumn(ByteBuffer.wrap(cName.getBytes())).value();
            assert new String(val.array(),val.position(),val.remaining()).equals(map.get(cName));
        }
        assert cf.getColumnNames().size() == map.size();
    }

    @Test
    public void testGetColumnCount()
    {
        ColumnFamily cf = ColumnFamily.create("Keyspace1", "Standard1");

        cf.addColumn(column("col1", "", 1));
        cf.addColumn(column("col2", "", 2));
        cf.addColumn(column("col1", "", 3));

        assert 2 == cf.getColumnCount();
        assert 2 == cf.getSortedColumns().size();
    }

    @Test
    public void testTimestamp()
    {
        ColumnFamily cf = ColumnFamily.create("Keyspace1", "Standard1");

        cf.addColumn(column("col1", "val1", 2));
        cf.addColumn(column("col1", "val2", 2)); // same timestamp, new value
        cf.addColumn(column("col1", "val3", 1)); // older timestamp -- should be ignored

        assert ByteBufferUtil.bytes("val2").equals(cf.getColumn(ByteBufferUtil.bytes("col1")).value());
    }

    @Test
    public void testMergeAndAdd()
    {
        ColumnFamily cf_new = ColumnFamily.create("Keyspace1", "Standard1");
        ColumnFamily cf_old = ColumnFamily.create("Keyspace1", "Standard1");
        ColumnFamily cf_result = ColumnFamily.create("Keyspace1", "Standard1");
        ByteBuffer val = ByteBufferUtil.bytes("sample value");
        ByteBuffer val2 = ByteBufferUtil.bytes("x value ");

        // exercise addColumn(QueryPath, ...)
        cf_new.addColumn(QueryPath.column(ByteBufferUtil.bytes("col1")), val, 3);
        cf_new.addColumn(QueryPath.column(ByteBufferUtil.bytes("col2")), val, 4);

        cf_old.addColumn(QueryPath.column(ByteBufferUtil.bytes("col2")), val2, 1);
        cf_old.addColumn(QueryPath.column(ByteBufferUtil.bytes("col3")), val2, 2);

        cf_result.addAll(cf_new);
        cf_result.addAll(cf_old);

        assert 3 == cf_result.getColumnCount() : "Count is " + cf_new.getColumnCount();
        //addcolumns will only add if timestamp >= old timestamp
        assert val.equals(cf_result.getColumn(ByteBufferUtil.bytes("col2")).value());

        // check that tombstone wins timestamp ties
        cf_result.addTombstone(ByteBufferUtil.bytes("col1"), 0, 3);
        assert cf_result.getColumn(ByteBufferUtil.bytes("col1")).isMarkedForDelete();
        cf_result.addColumn(QueryPath.column(ByteBufferUtil.bytes("col1")), val2, 3);
        assert cf_result.getColumn(ByteBufferUtil.bytes("col1")).isMarkedForDelete();

        // check that column value wins timestamp ties in absence of tombstone
        cf_result.addColumn(QueryPath.column(ByteBufferUtil.bytes("col3")), val, 2);
        assert cf_result.getColumn(ByteBufferUtil.bytes("col3")).value().equals(val2);
        cf_result.addColumn(QueryPath.column(ByteBufferUtil.bytes("col3")), ByteBufferUtil.bytes("z"), 2);
        assert cf_result.getColumn(ByteBufferUtil.bytes("col3")).value().equals(ByteBufferUtil.bytes("z"));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1935.java