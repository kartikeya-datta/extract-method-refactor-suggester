error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1287.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1287.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1287.java
text:
```scala
a@@ssert cf.metadata().cfName.equals("Standard1");

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
import java.util.Arrays;
import java.util.TreeMap;

import org.apache.cassandra.SchemaLoader;
import org.junit.Test;

import org.apache.cassandra.io.util.DataOutputBuffer;
import org.apache.cassandra.db.filter.QueryPath;
import static org.apache.cassandra.Util.column;

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
        assert cf.name().equals("Standard1");
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
            assert new String(cf.getColumn(cName.getBytes()).value()).equals(map.get(cName));
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

        assert Arrays.equals("val2".getBytes(), cf.getColumn("col1".getBytes()).value());
    }

    @Test
    public void testMergeAndAdd()
    {
        ColumnFamily cf_new = ColumnFamily.create("Keyspace1", "Standard1");
        ColumnFamily cf_old = ColumnFamily.create("Keyspace1", "Standard1");
        ColumnFamily cf_result = ColumnFamily.create("Keyspace1", "Standard1");
        byte val[] = "sample value".getBytes();
        byte val2[] = "x value ".getBytes();

        // exercise addColumn(QueryPath, ...)
        cf_new.addColumn(QueryPath.column("col1".getBytes()), val, 3);
        cf_new.addColumn(QueryPath.column("col2".getBytes()), val, 4);

        cf_old.addColumn(QueryPath.column("col2".getBytes()), val2, 1);
        cf_old.addColumn(QueryPath.column("col3".getBytes()), val2, 2);

        cf_result.addAll(cf_new);
        cf_result.addAll(cf_old);

        assert 3 == cf_result.getColumnCount() : "Count is " + cf_new.getColumnCount();
        //addcolumns will only add if timestamp >= old timestamp
        assert Arrays.equals(val, cf_result.getColumn("col2".getBytes()).value());
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1287.java