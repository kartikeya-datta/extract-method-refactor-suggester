error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/258.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/258.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/258.java
text:
```scala
T@@BinaryProtocol binaryProtocol = new TBinaryProtocol(transport);

package org.apache.cassandra.hadoop;

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.cassandra.EmbeddedServer;
import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.CfDef;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.KeyRange;
import org.apache.cassandra.thrift.KeySlice;
import org.apache.cassandra.thrift.KsDef;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for the {@link ColumnFamilyOutputFormat}, which reads each
 * &lt;key, value&gt; pair from a sequence file, maps them to a &lt;key,
 * column&gt; pair, and then reduces it by aggregating the columns that
 * correspond to the same key. Finally, the output &lt;key, columns&gt; pairs
 * are written into the (Cassandra) column family associated with this
 * {@link OutputFormat}.
 * 
 * @author Karthick Sankarachary
 * 
 */

public class ColumnFamilyOutputFormatTest extends EmbeddedServer
{
    static final String KEYSPACE = "ColumnFamilyOutputFormatTestKeyspace";
    static final String COLUMN_FAMILY = "outputColumnFamily";

    private static final String INPUT_FOLDER = "columnfamily.outputtest";

    private static final String INPUT_FILE = "rows.txt";

    private static final int NUMBER_OF_ROWS = 3;
    private static final int NUMBER_OF_COLUMNS = 4;

    List<Integer> rowKeys;
    List<Integer> columnValues;

    private static FileSystem fs;

    static
    {
        try
        {
            fs = FileSystem.getLocal(new Configuration());
        }
        catch (IOException ioe)
        {
            throw new IOError(ioe);
        }
    }

    private Cassandra.Client thriftClient;

    @Before
    public void setup() throws Exception
    {
        setupCassandra();
        defineRows();
        createInputFile();
    }

    @After
    public void tearDown() throws Exception
    {
        deleteInputFile();
        deleteRows();
    }

    private void deleteRows() throws InvalidRequestException, TException
    {
        thriftClient.system_drop_column_family(COLUMN_FAMILY);
    }

    private void deleteInputFile() throws IOException
    {
        inputdir = new Path(INPUT_FOLDER);
        if (!fs.delete(inputdir, true))
            throw new IOException("Delete failed to remove " + inputdir.toString());
    }

    private void setupCassandra() throws TException, InvalidRequestException
    {
        /* Establish a thrift connection to the cassandra instance */
        TSocket socket = new TSocket(DatabaseDescriptor.getListenAddress().getHostName(), DatabaseDescriptor.getRpcPort());
        TTransport transport = new TFramedTransport(socket);
        TBinaryProtocol binaryProtocol = new TBinaryProtocol(transport, false, false);
        Cassandra.Client cassandraClient = new Cassandra.Client(binaryProtocol);
        transport.open();
        thriftClient = cassandraClient;
        Set<String> keyspaces = thriftClient.describe_keyspaces();
        if (!keyspaces.contains(KEYSPACE))
        {
            List<CfDef> cfDefs = new ArrayList<CfDef>();
            thriftClient.system_add_keyspace(new KsDef(KEYSPACE, "org.apache.cassandra.locator.RackUnawareStrategy", 1, cfDefs));
        }
        thriftClient.set_keyspace(KEYSPACE);

        CfDef cfDef = new CfDef(KEYSPACE, COLUMN_FAMILY);
        try
        {
            thriftClient.system_add_column_family(cfDef);
        }
        catch (InvalidRequestException e)
        {
            throw new RuntimeException(e);
        }

    }

    private void defineRows()
    {
        rowKeys = new ArrayList<Integer>();
        columnValues = new ArrayList<Integer>();
        for (int key = 0; key < NUMBER_OF_ROWS; key++)
        {
            for (int columnValue = 0; columnValue < NUMBER_OF_COLUMNS; columnValue++)
            {
                rowKeys.add(key);
                columnValues.add(columnValue);
            }
        }
    }

    Path inputdir;

    private void createInputFile() throws IOException
    {
        inputdir = new Path(INPUT_FOLDER);
        if (!fs.mkdirs(inputdir))
        {
            throw new IOException("Mkdirs failed to create " + inputdir.toString());
        }

        Path inputRows = new Path(inputdir, INPUT_FILE);
        SequenceFile.Writer writer = SequenceFile.createWriter(fs, fs.getConf(), inputRows, IntWritable.class, IntWritable.class);
        // OutputStream os = fs.create(inputRows);
        // Writer writer = new OutputStreamWriter(os);
        for (int keyValuePair = 0; keyValuePair < NUMBER_OF_ROWS * NUMBER_OF_COLUMNS; keyValuePair++)
            writer.append(new IntWritable(rowKeys.get(keyValuePair)), new IntWritable(columnValues.get(keyValuePair)));
        writer.close();
    }

    @Test
    public void testCopyCF() throws Exception
    {
        ToolRunner.run(new Configuration(), new SampleColumnFamilyOutputTool(inputdir, COLUMN_FAMILY), new String[] {});
        verifyOutput();

    }

    public void verifyOutput() throws Exception
    {
        List<KeySlice> keySlices = thriftClient.get_range_slices(new ColumnParent().setColumn_family(COLUMN_FAMILY),
                                                                 new SlicePredicate().setColumn_names(new ArrayList<byte[]>()),
                                                                 new KeyRange().setStart_key("".getBytes()).setEnd_key("".getBytes()),
                                                                 ConsistencyLevel.ONE);
        for (KeySlice keySlice : keySlices)
        {
            Integer key = Integer.parseInt(new String(keySlice.getKey()));
            List<Integer> columnValues = new ArrayList<Integer>();
            for (ColumnOrSuperColumn cosc : keySlice.getColumns())
                columnValues.add(Integer.parseInt(new String(cosc.getColumn().getValue())));
            verifyKeyValues(key, columnValues);
        }
    }

    private void verifyKeyValues(Integer key, List<Integer> columnValues) throws Exception
    {
        List<Integer> outputColumnValues = new ArrayList<Integer>();
        for (int i = 0; i < rowKeys.size(); i++)
        {
            if (rowKeys.get(i).equals(key))
                outputColumnValues.add(this.columnValues.get(i));
        }
        columnValues.removeAll(outputColumnValues);
        assert columnValues.isEmpty() : "Some of the input columns could not be found in the column family";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/258.java