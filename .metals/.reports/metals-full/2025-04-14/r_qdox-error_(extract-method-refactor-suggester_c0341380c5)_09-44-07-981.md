error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/507.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/507.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/507.java
text:
```scala
private static final L@@ogger logger = LoggerFactory.getLogger(ConfigHelper.class);

package org.apache.cassandra.hadoop;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.cassandra.config.ConfigurationException;
import org.apache.cassandra.dht.IPartitioner;
import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.KeyRange;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.TBinaryProtocol;
import org.apache.cassandra.utils.FBUtilities;
import org.apache.cassandra.utils.Hex;
import org.apache.hadoop.conf.Configuration;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConfigHelper
{
    private static final String PARTITIONER_CONFIG = "cassandra.partitioner.class";
    private static final String INPUT_KEYSPACE_CONFIG = "cassandra.input.keyspace";
    private static final String OUTPUT_KEYSPACE_CONFIG = "cassandra.output.keyspace";
    private static final String INPUT_KEYSPACE_USERNAME_CONFIG = "cassandra.input.keyspace.username";
    private static final String INPUT_KEYSPACE_PASSWD_CONFIG = "cassandra.input.keyspace.passwd";
    private static final String OUTPUT_KEYSPACE_USERNAME_CONFIG = "cassandra.output.keyspace.username";
    private static final String OUTPUT_KEYSPACE_PASSWD_CONFIG = "cassandra.output.keyspace.passwd";
    private static final String INPUT_COLUMNFAMILY_CONFIG = "cassandra.input.columnfamily";
    private static final String OUTPUT_COLUMNFAMILY_CONFIG = "cassandra.output.columnfamily";
    private static final String INPUT_PREDICATE_CONFIG = "cassandra.input.predicate";
    private static final String INPUT_KEYRANGE_CONFIG = "cassandra.input.keyRange";
    private static final String OUTPUT_PREDICATE_CONFIG = "cassandra.output.predicate";
    private static final String INPUT_SPLIT_SIZE_CONFIG = "cassandra.input.split.size";
    private static final int DEFAULT_SPLIT_SIZE = 64 * 1024;
    private static final String RANGE_BATCH_SIZE_CONFIG = "cassandra.range.batch.size";
    private static final int DEFAULT_RANGE_BATCH_SIZE = 4096;
    private static final String THRIFT_PORT = "cassandra.thrift.port";
    private static final String INITIAL_THRIFT_ADDRESS = "cassandra.thrift.address";
    private static final String READ_CONSISTENCY_LEVEL = "cassandra.consistencylevel.read";
    private static final String WRITE_CONSISTENCY_LEVEL = "cassandra.consistencylevel.write";
    
    private static final Logger logger = LoggerFactory.getLogger(ColumnFamilyInputFormat.class);


    /**
     * Set the keyspace and column family for the input of this job.
     * Comparator and Partitioner types will be read from storage-conf.xml.
     *
     * @param conf         Job configuration you are about to run
     * @param keyspace
     * @param columnFamily
     */
    public static void setInputColumnFamily(Configuration conf, String keyspace, String columnFamily)
    {
        if (keyspace == null)
        {
            throw new UnsupportedOperationException("keyspace may not be null");
        }
        if (columnFamily == null)
        {
            throw new UnsupportedOperationException("columnfamily may not be null");
        }

        conf.set(INPUT_KEYSPACE_CONFIG, keyspace);
        conf.set(INPUT_COLUMNFAMILY_CONFIG, columnFamily);
    }

    /**
     * Set the keyspace and column family for the output of this job.
     *
     * @param conf Job configuration you are about to run
     * @param keyspace
     * @param columnFamily
     */
    public static void setOutputColumnFamily(Configuration conf, String keyspace, String columnFamily)
    {
        if (keyspace == null)
        {
            throw new UnsupportedOperationException("keyspace may not be null");
        }
        if (columnFamily == null)
        {
            throw new UnsupportedOperationException("columnfamily may not be null");
        }

        conf.set(OUTPUT_KEYSPACE_CONFIG, keyspace);
        conf.set(OUTPUT_COLUMNFAMILY_CONFIG, columnFamily);
    }

    /**
     * The number of rows to request with each get range slices request.
     * Too big and you can either get timeouts when it takes Cassandra too
     * long to fetch all the data. Too small and the performance
     * will be eaten up by the overhead of each request.
     *
     * @param conf      Job configuration you are about to run
     * @param batchsize Number of rows to request each time
     */
    public static void setRangeBatchSize(Configuration conf, int batchsize)
    {
        conf.setInt(RANGE_BATCH_SIZE_CONFIG, batchsize);
    }

    /**
     * The number of rows to request with each get range slices request.
     * Too big and you can either get timeouts when it takes Cassandra too
     * long to fetch all the data. Too small and the performance
     * will be eaten up by the overhead of each request.
     *
     * @param conf Job configuration you are about to run
     * @return Number of rows to request each time
     */
    public static int getRangeBatchSize(Configuration conf)
    {
        return conf.getInt(RANGE_BATCH_SIZE_CONFIG, DEFAULT_RANGE_BATCH_SIZE);
    }

    /**
     * Set the size of the input split.
     * This affects the number of maps created, if the number is too small
     * the overhead of each map will take up the bulk of the job time.
     *
     * @param conf      Job configuration you are about to run
     * @param splitsize Size of the input split
     */
    public static void setInputSplitSize(Configuration conf, int splitsize)
    {
        conf.setInt(INPUT_SPLIT_SIZE_CONFIG, splitsize);
    }

    public static int getInputSplitSize(Configuration conf)
    {
        return conf.getInt(INPUT_SPLIT_SIZE_CONFIG, DEFAULT_SPLIT_SIZE);
    }

    /**
     * Set the predicate that determines what columns will be selected from each row.
     *
     * @param conf      Job configuration you are about to run
     * @param predicate
     */
    public static void setInputSlicePredicate(Configuration conf, SlicePredicate predicate)
    {
        conf.set(INPUT_PREDICATE_CONFIG, predicateToString(predicate));
    }

    public static SlicePredicate getInputSlicePredicate(Configuration conf)
    {
        return predicateFromString(conf.get(INPUT_PREDICATE_CONFIG));
    }

    public static String getRawInputSlicePredicate(Configuration conf)
    {
        return conf.get(INPUT_PREDICATE_CONFIG);
    }

    private static String predicateToString(SlicePredicate predicate)
    {
        assert predicate != null;
        // this is so awful it's kind of cool!
        TSerializer serializer = new TSerializer(new TBinaryProtocol.Factory());
        try
        {
            return Hex.bytesToHex(serializer.serialize(predicate));
        }
        catch (TException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static SlicePredicate predicateFromString(String st)
    {
        assert st != null;
        TDeserializer deserializer = new TDeserializer(new TBinaryProtocol.Factory());
        SlicePredicate predicate = new SlicePredicate();
        try
        {
            deserializer.deserialize(predicate, Hex.hexToBytes(st));
        }
        catch (TException e)
        {
            throw new RuntimeException(e);
        }
        return predicate;
    }

    /**
     * Set the KeyRange to limit the rows.
     * @param conf Job configuration you are about to run
     */
    public static void setInputRange(Configuration conf, String startToken, String endToken)
    {
        KeyRange range = new KeyRange().setStart_token(startToken).setEnd_token(endToken);
        conf.set(INPUT_KEYRANGE_CONFIG, keyRangeToString(range));
    }

    /** may be null if unset */
    public static KeyRange getInputKeyRange(Configuration conf)
    {
        String str = conf.get(INPUT_KEYRANGE_CONFIG);
        return null != str ? keyRangeFromString(str) : null;
    }

    private static String keyRangeToString(KeyRange keyRange)
    {
        assert keyRange != null;
        TSerializer serializer = new TSerializer(new TBinaryProtocol.Factory());
        try
        {
            return Hex.bytesToHex(serializer.serialize(keyRange));
        }
        catch (TException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static KeyRange keyRangeFromString(String st)
    {
        assert st != null;
        TDeserializer deserializer = new TDeserializer(new TBinaryProtocol.Factory());
        KeyRange keyRange = new KeyRange();
        try
        {
            deserializer.deserialize(keyRange, Hex.hexToBytes(st));
        }
        catch (TException e)
        {
            throw new RuntimeException(e);
        }
        return keyRange;
    }

    public static String getInputKeyspace(Configuration conf)
    {
        return conf.get(INPUT_KEYSPACE_CONFIG);
    }
    
    public static String getOutputKeyspace(Configuration conf)
    {
        return conf.get(OUTPUT_KEYSPACE_CONFIG);
    }
    
    public static String getInputKeyspaceUserName(Configuration conf)
    {
    	return conf.get(INPUT_KEYSPACE_USERNAME_CONFIG);
    }
    
    public static String getInputKeyspacePassword(Configuration conf)
    {
    	return conf.get(INPUT_KEYSPACE_PASSWD_CONFIG);
    }

    public static String getOutputKeyspaceUserName(Configuration conf)
    {
    	return conf.get(OUTPUT_KEYSPACE_USERNAME_CONFIG);
    }
    
    public static String getOutputKeyspacePassword(Configuration conf)
    {
    	return conf.get(OUTPUT_KEYSPACE_PASSWD_CONFIG);
    }

    public static String getInputColumnFamily(Configuration conf)
    {
        return conf.get(INPUT_COLUMNFAMILY_CONFIG);
    }
    
    public static String getOutputColumnFamily(Configuration conf)
    {
        return conf.get(OUTPUT_COLUMNFAMILY_CONFIG);
    }

    public static String getReadConsistencyLevel(Configuration conf)
    {
        return conf.get(READ_CONSISTENCY_LEVEL, "ONE");
    }

    public static String getWriteConsistencyLevel(Configuration conf)
    {
        return conf.get(WRITE_CONSISTENCY_LEVEL, "ONE");
    }

    public static int getRpcPort(Configuration conf)
    {
        return Integer.parseInt(conf.get(THRIFT_PORT));
    }

    public static void setRpcPort(Configuration conf, String port)
    {
        conf.set(THRIFT_PORT, port);
    }

    public static String getInitialAddress(Configuration conf)
    {
        return conf.get(INITIAL_THRIFT_ADDRESS);
    }

    public static void setInitialAddress(Configuration conf, String address)
    {
        conf.set(INITIAL_THRIFT_ADDRESS, address);
    }

    public static void setPartitioner(Configuration conf, String classname)
    {
        conf.set(PARTITIONER_CONFIG, classname); 
    }

    public static IPartitioner getPartitioner(Configuration conf)
    {
        try
        {
            return FBUtilities.newPartitioner(conf.get(PARTITIONER_CONFIG)); 
        }
        catch (ConfigurationException e)
        {
            throw new RuntimeException(e);
        }
    }
    

    public static Cassandra.Client getClientFromAddressList(Configuration conf) throws IOException
    {
        String[] addresses = ConfigHelper.getInitialAddress(conf).split(",");
        Cassandra.Client client = null;
        List<IOException> exceptions = new ArrayList<IOException>();
        for (String address : addresses)
        {
            try
            {
                client = createConnection(address, ConfigHelper.getRpcPort(conf), true);
                break;
            }
            catch (IOException ioe)
            {
                exceptions.add(ioe);
            }
        }
        if (client == null)
        {
            logger.error("failed to connect to any initial addresses");
            for (IOException ioe : exceptions)
            {
                logger.error("", ioe);
            }
            throw exceptions.get(exceptions.size() - 1);
        }
        return client;
    }

    public static Cassandra.Client createConnection(String host, Integer port, boolean framed)
            throws IOException
    {
        TSocket socket = new TSocket(host, port);
        TTransport trans = framed ? new TFramedTransport(socket) : socket;
        try
        {
            trans.open();
        }
        catch (TTransportException e)
        {
            throw new IOException("unable to connect to server", e);
        }
        return new Cassandra.Client(new TBinaryProtocol(trans));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/507.java