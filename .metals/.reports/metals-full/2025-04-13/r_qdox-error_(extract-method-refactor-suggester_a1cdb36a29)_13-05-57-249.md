error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/267.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/267.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/267.java
text:
```scala
A@@bstractReconciler reconciler = TimestampReconciler.instance; // TODO generalize

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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

import com.google.common.collect.AbstractIterator;

import org.apache.cassandra.auth.AllowAllAuthenticator;
import org.apache.cassandra.auth.SimpleAuthenticator;

import org.apache.cassandra.config.ConfigurationException;
import org.apache.cassandra.db.*;
import org.apache.cassandra.db.clock.AbstractReconciler;
import org.apache.cassandra.db.clock.TimestampReconciler;
import org.apache.cassandra.db.marshal.AbstractType;
import org.apache.cassandra.dht.IPartitioner;
import org.apache.cassandra.thrift.*;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.SuperColumn;
import org.apache.cassandra.utils.FBUtilities;
import org.apache.cassandra.utils.Pair;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;

public class ColumnFamilyRecordReader extends RecordReader<byte[], SortedMap<byte[], IColumn>>
{
    private ColumnFamilySplit split;
    private RowIterator iter;
    private Pair<byte[], SortedMap<byte[], IColumn>> currentRow;
    private SlicePredicate predicate;
    private int totalRowCount; // total number of rows to fetch
    private int batchRowCount; // fetch this many per batch
    private String cfName;
    private String keyspace;
    private TSocket socket;
    private Cassandra.Client client;

    public void close() 
    {
        if (socket != null && socket.isOpen())
        {
            socket.close();
            socket = null;
            client = null;
        }
    }
    
    public byte[] getCurrentKey()
    {
        return currentRow.left;
    }

    public SortedMap<byte[], IColumn> getCurrentValue()
    {
        return currentRow.right;
    }
    
    public float getProgress()
    {
        // the progress is likely to be reported slightly off the actual but close enough
        return ((float)iter.rowsRead()) / totalRowCount;
    }
    
    public void initialize(InputSplit split, TaskAttemptContext context) throws IOException
    {
        this.split = (ColumnFamilySplit) split;
        Configuration conf = context.getConfiguration();
        predicate = ConfigHelper.getInputSlicePredicate(conf);
        totalRowCount = ConfigHelper.getInputSplitSize(conf);
        batchRowCount = ConfigHelper.getRangeBatchSize(conf);
        cfName = ConfigHelper.getInputColumnFamily(conf);
        keyspace = ConfigHelper.getInputKeyspace(conf);
        
        try
        {
            // only need to connect once
            if (socket != null && socket.isOpen())
                return;

            // create connection using thrift
            String location = getLocation();
            socket = new TSocket(location, ConfigHelper.getRpcPort(conf));
            TBinaryProtocol binaryProtocol = new TBinaryProtocol(new TFramedTransport(socket));
            client = new Cassandra.Client(binaryProtocol);
            socket.open();

            // log in
            client.set_keyspace(keyspace);
            if (ConfigHelper.getInputKeyspaceUserName(conf) != null)
            {
                Map<String, String> creds = new HashMap<String, String>();
                creds.put(SimpleAuthenticator.USERNAME_KEY, ConfigHelper.getInputKeyspaceUserName(conf));
                creds.put(SimpleAuthenticator.PASSWORD_KEY, ConfigHelper.getInputKeyspacePassword(conf));
                AuthenticationRequest authRequest = new AuthenticationRequest(creds);
                client.login(authRequest);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        iter = new RowIterator();
    }
    
    public boolean nextKeyValue() throws IOException
    {
        if (!iter.hasNext())
            return false;
        currentRow = iter.next();
        return true;
    }

    // we don't use endpointsnitch since we are trying to support hadoop nodes that are
    // not necessarily on Cassandra machines, too.  This should be adequate for single-DC clusters, at least.
    private String getLocation()
    {
        InetAddress[] localAddresses;
        try
        {
            localAddresses = InetAddress.getAllByName(InetAddress.getLocalHost().getHostAddress());
        }
        catch (UnknownHostException e)
        {
            throw new AssertionError(e);
        }
        for (InetAddress address : localAddresses)
        {
            for (String location : split.getLocations())
            {
                InetAddress locationAddress = null;
                try
                {
                    locationAddress = InetAddress.getByName(location);
                }
                catch (UnknownHostException e)
                {
                    throw new AssertionError(e);
                }
                if (address.equals(locationAddress))
                {
                    return location;
                }
            }
        }
        return split.getLocations()[0];
    }

    private class RowIterator extends AbstractIterator<Pair<byte[], SortedMap<byte[], IColumn>>>
    {
        private List<KeySlice> rows;
        private String startToken;
        private int totalRead = 0;
        private int i = 0;
        private final AbstractType comparator;
        private final AbstractType subComparator;
        private final IPartitioner partitioner;

        private RowIterator()
        {
            try
            {
                partitioner = FBUtilities.newPartitioner(client.describe_partitioner());

                // Get the Keyspace metadata, then get the specific CF metadata
                // in order to populate the sub/comparator.
                KsDef ks_def = client.describe_keyspace(keyspace);
                List<String> cfnames = new ArrayList<String>();
                for (CfDef cfd : ks_def.cf_defs)
                    cfnames.add(cfd.name);
                int idx = cfnames.indexOf(cfName);
                CfDef cf_def = ks_def.cf_defs.get(idx);

                comparator = FBUtilities.getComparator(cf_def.comparator_type);
                subComparator = cf_def.subcomparator_type == null ? null : FBUtilities.getComparator(cf_def.subcomparator_type);
            }
            catch (ConfigurationException e)
            {
                throw new RuntimeException("unable to load sub/comparator", e);
            }
            catch (TException e)
            {
                throw new RuntimeException("error communicating via Thrift", e);
            }
            catch (NotFoundException e)
            {
                throw new RuntimeException("server reports no such keyspace " + keyspace, e);
            }
        }

        private void maybeInit()
        {
            // check if we need another batch 
            if (rows != null && i >= rows.size())
                rows = null;
            
            if (rows != null)
                return;

            if (startToken == null)
            {
                startToken = split.getStartToken();
            } 
            else if (startToken.equals(split.getEndToken()))
            {
                rows = null;
                return;
            }
            
            KeyRange keyRange = new KeyRange(batchRowCount)
                                .setStart_token(startToken)
                                .setEnd_token(split.getEndToken());
            try
            {
                rows = client.get_range_slices(new ColumnParent(cfName),
                                               predicate,
                                               keyRange,
                                               ConsistencyLevel.ONE);
                  
                // nothing new? reached the end
                if (rows.isEmpty())
                {
                    rows = null;
                    return;
                }
                               
                // reset to iterate through this new batch
                i = 0;
                
                // prepare for the next slice to be read
                KeySlice lastRow = rows.get(rows.size() - 1);
                byte[] rowkey = lastRow.getKey();
                startToken = partitioner.getTokenFactory().toString(partitioner.getToken(rowkey));
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }

        /**
         * @return total number of rows read by this record reader
         */
        public int rowsRead()
        {
            return totalRead;
        }

        @Override
        protected Pair<byte[], SortedMap<byte[], IColumn>> computeNext()
        {
            maybeInit();
            if (rows == null)
                return endOfData();
            
            totalRead++;
            KeySlice ks = rows.get(i++);
            SortedMap<byte[], IColumn> map = new TreeMap<byte[], IColumn>(comparator);
            for (ColumnOrSuperColumn cosc : ks.columns)
            {
                IColumn column = unthriftify(cosc);
                map.put(column.name(), column);
            }
            return new Pair<byte[], SortedMap<byte[], IColumn>>(ks.key, map);
        }

        private IColumn unthriftify(ColumnOrSuperColumn cosc)
        {
            if (cosc.column == null)
                return unthriftifySuper(cosc.super_column);
            return unthriftifySimple(cosc.column);
        }

        private IColumn unthriftifySuper(SuperColumn super_column)
        {
            ClockType clockType = ClockType.Timestamp; // TODO generalize
            AbstractReconciler reconciler = new TimestampReconciler(); // TODO generalize
            org.apache.cassandra.db.SuperColumn sc = new org.apache.cassandra.db.SuperColumn(super_column.name, subComparator, clockType, reconciler);
            for (Column column : super_column.columns)
            {
                sc.addColumn(unthriftifySimple(column));
            }
            return sc;
        }

        private IColumn unthriftifySimple(Column column)
        {
            return new org.apache.cassandra.db.Column(column.name, column.value, unthriftifyClock(column.clock));
        }

        private IClock unthriftifyClock(Clock clock)
        {
            return new org.apache.cassandra.db.TimestampClock(clock.getTimestamp());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/267.java