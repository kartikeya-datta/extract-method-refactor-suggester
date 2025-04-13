error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/612.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/612.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/612.java
text:
```scala
B@@yteBuffer row = ByteBufferUtil.bytes((rowPrefix + nRows));

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
package org.apache.cassandra.client;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Collection;

import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.ColumnPath;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.commons.lang.StringUtils;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.cassandra.utils.ByteBufferUtil;


/**
 *  Sample code that uses RingCache in the client.
 */
public class TestRingCache
{
    private RingCache ringCache;
    private Cassandra.Client thriftClient;

    public TestRingCache(String keyspace) throws IOException
    {
        String seed = DatabaseDescriptor.getSeeds().iterator().next().getHostAddress();
    	ringCache = new RingCache(keyspace, DatabaseDescriptor.getPartitioner(), seed, DatabaseDescriptor.getRpcPort());
    }
    
    private void setup(String server, int port) throws Exception
    {
        /* Establish a thrift connection to the cassandra instance */
        TSocket socket = new TSocket(server, port);
        System.out.println(" connected to " + server + ":" + port + ".");
        TBinaryProtocol binaryProtocol = new TBinaryProtocol(new TFramedTransport(socket));
        Cassandra.Client cassandraClient = new Cassandra.Client(binaryProtocol);
        socket.open();
        thriftClient = cassandraClient;
    }

    /**
     * usage: java -cp <configpath> org.apache.cassandra.client.TestRingCache [keyspace row-id-prefix row-id-int]
     * to test a single keyspace/row, use the parameters. row-id-prefix and row-id-int are appended together to form a
     * single row id.  If you supply now parameters, 'Keyspace1' is assumed and will check 9 rows ('row1' through 'row9').
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Throwable
    {
        int minRow;
        int maxRow;
        String rowPrefix, keyspace = "Keyspace1";
        
        if (args.length > 0)
        {
            keyspace = args[0];
            rowPrefix = args[1];
            minRow = Integer.parseInt(args[2]);
            maxRow = minRow + 1;
        }
        else
        {
            minRow = 1;
            maxRow = 10;
            rowPrefix = "row";
        }
        
        TestRingCache tester = new TestRingCache(keyspace);

        for (int nRows = minRow; nRows < maxRow; nRows++)
        {
            ByteBuffer row = ByteBuffer.wrap((rowPrefix + nRows).getBytes());
            ColumnPath col = new ColumnPath("Standard1").setSuper_column((ByteBuffer)null).setColumn("col1".getBytes());
            ColumnParent parent = new ColumnParent("Standard1").setSuper_column((ByteBuffer)null);

            Collection<InetAddress> endpoints = tester.ringCache.getEndpoint(row);
            InetAddress firstEndpoint = endpoints.iterator().next();
            System.out.printf("hosts with key %s : %s; choose %s%n",
                              new String(row.array()), StringUtils.join(endpoints, ","), firstEndpoint);

            // now, read the row back directly from the host owning the row locally
            tester.setup(firstEndpoint.getHostAddress(), DatabaseDescriptor.getRpcPort());
            tester.thriftClient.set_keyspace(keyspace);
            tester.thriftClient.insert(row, parent, new Column(ByteBufferUtil.bytes("col1"), ByteBufferUtil.bytes("val1"), 1), ConsistencyLevel.ONE);
            Column column = tester.thriftClient.get(row, col, ConsistencyLevel.ONE).column;
            System.out.println("read row " + new String(row.array()) + " " + new String(column.name.array()) + ":" + new String(column.value.array()) + ":" + column.timestamp);
        }

        System.exit(1);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/612.java