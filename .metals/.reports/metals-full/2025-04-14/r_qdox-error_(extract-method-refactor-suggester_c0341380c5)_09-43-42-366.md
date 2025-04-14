error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/620.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/620.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/620.java
text:
```scala
r@@eturn bytesToHex(ByteBufferUtil.bytes(str));

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
package org.apache.cassandra.tools;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.apache.cassandra.SchemaLoader;
import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.db.ColumnFamily;
import org.apache.cassandra.db.CounterColumn;
import org.apache.cassandra.db.ExpiringColumn;
import org.apache.cassandra.db.filter.QueryFilter;
import org.apache.cassandra.db.filter.QueryPath;
import org.apache.cassandra.dht.IPartitioner;
import org.apache.cassandra.io.sstable.Descriptor;
import org.apache.cassandra.io.sstable.SSTableReader;
import org.apache.cassandra.io.sstable.SSTableWriter;
import org.apache.cassandra.utils.ByteBufferUtil;

import static org.apache.cassandra.io.sstable.SSTableUtils.tempSSTableFile;
import static org.apache.cassandra.utils.ByteBufferUtil.bytesToHex;
import static org.apache.cassandra.utils.ByteBufferUtil.hexToBytes;
import static org.junit.Assert.assertTrue;

import org.apache.cassandra.Util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.junit.Test;

public class SSTableExportTest extends SchemaLoader
{
    public String asHex(String str)
    {
        return bytesToHex(ByteBuffer.wrap(str.getBytes()));
    }

    @Test
    public void testEnumeratekeys() throws IOException
    {
        File tempSS = tempSSTableFile("Keyspace1", "Standard1");
        ColumnFamily cfamily = ColumnFamily.create("Keyspace1", "Standard1");
        SSTableWriter writer = new SSTableWriter(tempSS.getPath(), 2);
        
        // Add rowA
        cfamily.addColumn(new QueryPath("Standard1", null, ByteBufferUtil.bytes("colA")), ByteBufferUtil.bytes("valA"), System.currentTimeMillis());
        writer.append(Util.dk("rowA"), cfamily);
        cfamily.clear();
        
        // Add rowB
        cfamily.addColumn(new QueryPath("Standard1", null, ByteBufferUtil.bytes("colB")), ByteBufferUtil.bytes("valB"), System.currentTimeMillis());
        writer.append(Util.dk("rowB"), cfamily);
        cfamily.clear();
     
        writer.closeAndOpenReader();
        
        // Enumerate and verify
        File temp = File.createTempFile("Standard1", ".txt");
        SSTableExport.enumeratekeys(writer.getFilename(), new PrintStream(temp.getPath()));

        
        FileReader file = new FileReader(temp);
        char[] buf = new char[(int) temp.length()];
        file.read(buf);
        String output = new String(buf);

        String sep = System.getProperty("line.separator");
        assert output.equals(asHex("rowA") + sep + asHex("rowB") + sep) : output;
    }

    @Test
    public void testExportSimpleCf() throws IOException
    {
        File tempSS = tempSSTableFile("Keyspace1", "Standard1");
        ColumnFamily cfamily = ColumnFamily.create("Keyspace1", "Standard1");
        SSTableWriter writer = new SSTableWriter(tempSS.getPath(), 2);
        
        int nowInSec = (int)(System.currentTimeMillis() / 1000) + 42; //live for 42 seconds
        // Add rowA
        cfamily.addColumn(new QueryPath("Standard1", null, ByteBufferUtil.bytes("colA")), ByteBufferUtil.bytes("valA"), System.currentTimeMillis());
        cfamily.addColumn(null, new ExpiringColumn(ByteBufferUtil.bytes("colExp"), ByteBufferUtil.bytes("valExp"), System.currentTimeMillis(), 42, nowInSec));
        writer.append(Util.dk("rowA"), cfamily);
        cfamily.clear();
        
        // Add rowB
        cfamily.addColumn(new QueryPath("Standard1", null, ByteBufferUtil.bytes("colB")), ByteBufferUtil.bytes("valB"), System.currentTimeMillis());
        writer.append(Util.dk("rowB"), cfamily);
        cfamily.clear();

        // Add rowExclude
        cfamily.addColumn(new QueryPath("Standard1", null, ByteBufferUtil.bytes("colX")), ByteBufferUtil.bytes("valX"), System.currentTimeMillis());
        writer.append(Util.dk("rowExclude"), cfamily);
        cfamily.clear();

        SSTableReader reader = writer.closeAndOpenReader();
        
        // Export to JSON and verify
        File tempJson = File.createTempFile("Standard1", ".json");
        SSTableExport.export(reader, new PrintStream(tempJson.getPath()), new String[]{asHex("rowExclude")});
        
        JSONObject json = (JSONObject)JSONValue.parse(new FileReader(tempJson));
        
        JSONArray rowA = (JSONArray)json.get(asHex("rowA"));
        JSONArray colA = (JSONArray)rowA.get(0);
        assert hexToBytes((String)colA.get(1)).equals(ByteBufferUtil.bytes("valA"));

        JSONArray colExp = (JSONArray)rowA.get(1);
        assert ((Long)colExp.get(4)) == 42;
        assert ((Long)colExp.get(5)) == nowInSec;
        
        JSONArray rowB = (JSONArray)json.get(asHex("rowB"));
        JSONArray colB = (JSONArray)rowB.get(0);
        assert colB.size() == 3;

        JSONArray rowExclude = (JSONArray)json.get(asHex("rowExclude"));
        assert rowExclude == null;
    }

    @Test
    public void testExportSuperCf() throws IOException
    {
        File tempSS = tempSSTableFile("Keyspace1", "Super4");
        ColumnFamily cfamily = ColumnFamily.create("Keyspace1", "Super4");
        SSTableWriter writer = new SSTableWriter(tempSS.getPath(), 2);
        
        // Add rowA
        cfamily.addColumn(new QueryPath("Super4", ByteBufferUtil.bytes("superA"), ByteBufferUtil.bytes("colA")), ByteBufferUtil.bytes("valA"), System.currentTimeMillis());
        writer.append(Util.dk("rowA"), cfamily);
        cfamily.clear();
        
        // Add rowB
        cfamily.addColumn(new QueryPath("Super4", ByteBufferUtil.bytes("superB"), ByteBufferUtil.bytes("colB")), ByteBufferUtil.bytes("valB"), System.currentTimeMillis());
        writer.append(Util.dk("rowB"), cfamily);
        cfamily.clear();

        // Add rowExclude
        cfamily.addColumn(new QueryPath("Super4", ByteBufferUtil.bytes("superX"), ByteBufferUtil.bytes("colX")), ByteBufferUtil.bytes("valX"), System.currentTimeMillis());
        writer.append(Util.dk("rowExclude"), cfamily);
        cfamily.clear();

        SSTableReader reader = writer.closeAndOpenReader();
        
        // Export to JSON and verify
        File tempJson = File.createTempFile("Super4", ".json");
        SSTableExport.export(reader, new PrintStream(tempJson.getPath()), new String[]{asHex("rowExclude")});
        
        JSONObject json = (JSONObject)JSONValue.parse(new FileReader(tempJson));
        
        JSONObject rowA = (JSONObject)json.get(asHex("rowA"));
        JSONObject superA = (JSONObject)rowA.get(cfamily.getComparator().getString(ByteBufferUtil.bytes("superA")));
        JSONArray subColumns = (JSONArray)superA.get("subColumns");
        JSONArray colA = (JSONArray)subColumns.get(0);
        JSONObject rowExclude = (JSONObject)json.get(asHex("rowExclude"));
        assert hexToBytes((String)colA.get(1)).equals(ByteBufferUtil.bytes("valA"));
        assert colA.size() == 3;
        assert rowExclude == null;
    }
    
    @Test
    public void testRoundTripStandardCf() throws IOException, ParseException
    {
        File tempSS = tempSSTableFile("Keyspace1", "Standard1");
        ColumnFamily cfamily = ColumnFamily.create("Keyspace1", "Standard1");
        SSTableWriter writer = new SSTableWriter(tempSS.getPath(), 2);
        
        // Add rowA
        cfamily.addColumn(new QueryPath("Standard1", null, ByteBufferUtil.bytes("name")), ByteBufferUtil.bytes("val"), System.currentTimeMillis());
        writer.append(Util.dk("rowA"), cfamily);
        cfamily.clear();

        // Add rowExclude
        cfamily.addColumn(new QueryPath("Standard1", null, ByteBufferUtil.bytes("name")), ByteBufferUtil.bytes("val"), System.currentTimeMillis());
        writer.append(Util.dk("rowExclude"), cfamily);
        cfamily.clear();

        SSTableReader reader = writer.closeAndOpenReader();
        
        // Export to JSON and verify
        File tempJson = File.createTempFile("Standard1", ".json");
        SSTableExport.export(reader, new PrintStream(tempJson.getPath()), new String[]{asHex("rowExclude")});
        
        // Import JSON to another SSTable file
        File tempSS2 = tempSSTableFile("Keyspace1", "Standard1");
        SSTableImport.importJson(tempJson.getPath(), "Keyspace1", "Standard1", tempSS2.getPath());

        reader = SSTableReader.open(Descriptor.fromFilename(tempSS2.getPath()));
        QueryFilter qf = QueryFilter.getNamesFilter(Util.dk("rowA"), new QueryPath("Standard1", null, null), ByteBufferUtil.bytes("name"));
        ColumnFamily cf = qf.getSSTableColumnIterator(reader).getColumnFamily();
        assertTrue(cf != null);
        assertTrue(cf.getColumn(ByteBufferUtil.bytes("name")).value().equals(hexToBytes("76616c")));

        qf = QueryFilter.getNamesFilter(Util.dk("rowExclude"), new QueryPath("Standard1", null, null), ByteBufferUtil.bytes("name"));
        cf = qf.getSSTableColumnIterator(reader).getColumnFamily();
        assert cf == null;
    }

    @Test
    public void testExportCounterCf() throws IOException
    {
        File tempSS = tempSSTableFile("Keyspace1", "Counter1");
        ColumnFamily cfamily = ColumnFamily.create("Keyspace1", "Counter1");
        SSTableWriter writer = new SSTableWriter(tempSS.getPath(), 2);

        // Add rowA
        cfamily.addColumn(null, new CounterColumn(ByteBufferUtil.bytes("colA"), 42, System.currentTimeMillis()));
        writer.append(Util.dk("rowA"), cfamily);
        cfamily.clear();

        SSTableReader reader = writer.closeAndOpenReader();

        // Export to JSON and verify
        File tempJson = File.createTempFile("Counter1", ".json");
        SSTableExport.export(reader, new PrintStream(tempJson.getPath()), new String[0]);

        JSONObject json = (JSONObject)JSONValue.parse(new FileReader(tempJson));

        JSONArray rowA = (JSONArray)json.get(asHex("rowA"));
        JSONArray colA = (JSONArray)rowA.get(0);
        assert hexToBytes((String)colA.get(0)).equals(ByteBufferUtil.bytes("colA"));
        assert ((String) colA.get(3)).equals("c");
        assert (Long) colA.get(4) == Long.MIN_VALUE;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/620.java