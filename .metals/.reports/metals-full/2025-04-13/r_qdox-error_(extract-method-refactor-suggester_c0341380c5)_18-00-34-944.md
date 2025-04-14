error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3610.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3610.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3610.java
text:
```scala
f@@or (EndPoint endpoint: StorageService.instance().getReadStorageEndPoints(key.toString()))

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
 
 /**
  * Cassandra has a backdoor called the Binary Memtable. The purpose of this backdoor is to
  * mass import large amounts of data, without using the Thrift interface.
  *
  * Inserting data through the binary memtable, allows you to skip the commit log overhead, and an ack
  * from Thrift on every insert. The example below utilizes Hadoop to generate all the data necessary 
  * to send to Cassandra, and sends it using the Binary Memtable interface. What Hadoop ends up doing is
  * creating the actual data that gets put into an SSTable as if you were using Thrift. With enough Hadoop nodes
  * inserting the data, the bottleneck at this point should become the network.
  * 
  * We recommend adjusting the compaction threshold to 0, while the import is running. After the import, you need
  * to run `nodeprobe -host <IP> flush_binary <Keyspace>` on every node, as this will flush the remaining data still left 
  * in memory to disk. Then it's recommended to adjust the compaction threshold to it's original value.
  * 
  * The example below is a sample Hadoop job, and it inserts SuperColumns. It can be tweaked to work with normal Columns.
  *
  * You should construct your data you want to import as rows delimited by a new line. You end up grouping by <Key>
  * in the mapper, so that the end result generates the data set into a column oriented subset. Once you get to the
  * reduce aspect, you can generate the ColumnFamilies you want inserted, and send it to your nodes. You need to
  * know your tokens and ip addresses ahead of time.
  *
  * Author : Chris Goffinet (goffinet@digg.com)
  */
  
package org.apache.cassandra.bulkloader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.db.Column;
import org.apache.cassandra.db.ColumnFamily;
import org.apache.cassandra.db.RowMutation;
import org.apache.cassandra.db.filter.QueryPath;
import org.apache.cassandra.dht.BigIntegerToken;
import org.apache.cassandra.io.DataOutputBuffer;
import org.apache.cassandra.net.EndPoint;
import org.apache.cassandra.net.Message;
import org.apache.cassandra.net.MessagingService;
import org.apache.cassandra.net.SelectorManager;
import org.apache.cassandra.service.StorageService;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

public class CassandraBulkLoader {
    public static class Map extends MapReduceBase implements Mapper<Text, Text, Text, Text> {
        private Text word = new Text();

        public void map(Text key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
            // This is a simple key/value mapper.
            output.collect(key, value);
        }
    }
    public static class Reduce extends MapReduceBase implements Reducer<Text, Text, Text, Text> {
        private Path[] localFiles;
        private ArrayList<String> tokens = new ArrayList<String>();
        private JobConf jobconf;

        public void configure(JobConf job) {
            this.jobconf = job;
            String cassConfig;

            // Get the cached files
            try
            {
                localFiles = DistributedCache.getLocalCacheFiles(job);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            cassConfig = localFiles[0].getParent().toString();

            System.setProperty("storage-config",cassConfig);

            startMessagingService();
            /* 
              Populate tokens 
              
              Specify your tokens and ips below. 
              
              tokens.add("0:192.168.0.1")
              tokens.add("14178431955039102644307275309657008810:192.168.0.2")
            */

            for (String token : this.tokens)
            {
                String[] values = token.split(":");
                StorageService.instance().updateTokenMetadata(new BigIntegerToken(new BigInteger(values[0])),new EndPoint(values[1], 7000));
            }
        }
        public void close()
        {
            try
            {
                // release the cache
                DistributedCache.releaseCache(new URI("/cassandra/storage-conf.xml#storage-conf.xml"), this.jobconf);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            catch (URISyntaxException e)
            {
                throw new RuntimeException(e);
            }
            shutdownMessagingService();
        }
        public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException
        {
            ColumnFamily columnFamily;
            String Keyspace = "Keyspace1";
            String CFName = "Super1";
            Message message;
            List<ColumnFamily> columnFamilies;
            columnFamilies = new LinkedList<ColumnFamily>();
            String line;

            /* Create a column family */
            columnFamily = ColumnFamily.create(Keyspace, CFName);
            while (values.hasNext()) {
                // Split the value (line based on your own delimiter)
                line = values.next().toString();
                String[] fields = line.split("\1");
                String SuperColumnName = fields[1];
                String ColumnName = fields[2];
                String ColumnValue = fields[3];
                int timestamp = 0;
                columnFamily.addColumn(new QueryPath(CFName, SuperColumnName.getBytes("UTF-8"), ColumnName.getBytes("UTF-8")), ColumnValue.getBytes(), timestamp);
            }

            columnFamilies.add(columnFamily);

            /* Get serialized message to send to cluster */
            message = createMessage(Keyspace, key.toString(), CFName, columnFamilies);
            for (EndPoint endpoint: StorageService.instance().getNStorageEndPoint(key.toString()))
            {
                /* Send message to end point */
                MessagingService.getMessagingInstance().sendOneWay(message, endpoint);
            }
            
            output.collect(key, new Text(" inserted into Cassandra node(s)"));
        }
    }

    public static void runJob(String[] args)
    {
        JobConf conf = new JobConf(CassandraBulkLoader.class);

        if(args.length >= 4)
        {
          conf.setNumReduceTasks(new Integer(args[3]));
        }

        try
        {
            // We store the cassandra storage-conf.xml on the HDFS cluster
            DistributedCache.addCacheFile(new URI("/cassandra/storage-conf.xml#storage-conf.xml"), conf);
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
        conf.setInputFormat(KeyValueTextInputFormat.class);
        conf.setJobName("CassandraBulkLoader_v2");
        conf.setMapperClass(Map.class);
        conf.setReducerClass(Reduce.class);

        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(conf, new Path(args[1]));
        FileOutputFormat.setOutputPath(conf, new Path(args[2]));
        try
        {
            JobClient.runJob(conf);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    public static Message createMessage(String Keyspace, String Key, String CFName, List<ColumnFamily> ColumnFamiles)
    {
        ColumnFamily baseColumnFamily;
        DataOutputBuffer bufOut = new org.apache.cassandra.io.DataOutputBuffer();
        RowMutation rm;
        Message message;
        Column column;

        /* Get the first column family from list, this is just to get past validation */
        baseColumnFamily = new ColumnFamily(CFName, "Standard",DatabaseDescriptor.getComparator(Keyspace, CFName), DatabaseDescriptor.getSubComparator(Keyspace, CFName));
        
        for(ColumnFamily cf : ColumnFamiles) {
            bufOut.reset();
            try
            {
                ColumnFamily.serializer().serializeWithIndexes(cf, bufOut);
                byte[] data = new byte[bufOut.getLength()];
                System.arraycopy(bufOut.getData(), 0, data, 0, bufOut.getLength());

                column = new Column(cf.name().getBytes("UTF-8"), data, 0, false);
                baseColumnFamily.addColumn(column);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        rm = new RowMutation(Keyspace,StorageService.getPartitioner().decorateKey(Key));
        rm.add(baseColumnFamily);

        try
        {
            /* Make message */
            message = rm.makeRowMutationMessage(StorageService.binaryVerbHandler_);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        return message;
    }
    public static void startMessagingService()
    {
        SelectorManager.getSelectorManager().start();
    }
    public static void shutdownMessagingService()
    {
        try
        {
            // Sleep just in case the number of keys we send over is small
            Thread.sleep(3*1000);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
        MessagingService.flushAndshutdown();
    }
    public static void main(String[] args) throws Exception
    {
        runJob(args);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3610.java