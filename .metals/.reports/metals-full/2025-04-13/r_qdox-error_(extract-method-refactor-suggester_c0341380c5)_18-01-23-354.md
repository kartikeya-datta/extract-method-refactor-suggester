error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3666.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3666.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3666.java
text:
```scala
C@@olumnFamily hintedColumnFamily = ColumnFamilyStore.removeDeleted(systemTable.get(tableName, HINTS_CF), Integer.MAX_VALUE);

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

package org.apache.cassandra.db;

import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.io.IOException;

import org.apache.log4j.Logger;

import org.apache.cassandra.concurrent.DebuggableThreadPoolExecutor;
import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.gms.FailureDetector;
import java.net.InetAddress;
import org.apache.cassandra.net.Message;
import org.apache.cassandra.net.MessagingService;
import org.apache.cassandra.service.*;
import org.apache.cassandra.db.filter.IdentityQueryFilter;
import org.apache.cassandra.db.filter.QueryPath;


/**
 * For each table (keyspace), there is a row in the system hints CF.
 * SuperColumns in that row are keys for which we have hinted data.
 * Subcolumns names within that supercolumn are host IPs. Subcolumn values are always empty.
 * Instead, we store the row data "normally" in the application table it belongs in.
 *
 * So when we deliver hints we look up endpoints that need data delivered
 * on a per-key basis, then read that entire row out and send it over.
 * (TODO handle rows that have incrementally grown too large for a single message.)
 *
 * HHM never deletes the row from Application tables; there is no way to distinguish that
 * from hinted tombstones!  instead, rely on cleanup compactions to remove data
 * that doesn't belong on this node.  (Cleanup compactions may be started manually
 * -- on a per node basis -- with "nodeprobe cleanup.")
 *
 * TODO this avoids our hint rows from growing excessively large by offloading the
 * message data into application tables.  But, this means that cleanup compactions
 * will nuke HH data.  Probably better would be to store the RowMutation messages
 * in a HHData (non-super) CF, modifying the above to store a UUID value in the
 * HH subcolumn value, which we use as a key to a [standard] HHData system CF
 * that would contain the message bytes.
 *
 * There are two ways hinted data gets delivered to the intended nodes.
 *
 * runHints() runs periodically and pushes the hinted data on this node to
 * every intended node.
 *
 * runDelieverHints() is called when some other node starts up (potentially
 * from a failure) and delivers the hinted data just to that node.
 */

public class HintedHandOffManager
{
    private static HintedHandOffManager instance_;
    private static Lock lock_ = new ReentrantLock();
    private static Logger logger_ = Logger.getLogger(HintedHandOffManager.class);
    final static long INTERVAL_IN_MS = 3600 * 1000;
    private ExecutorService executor_ = new DebuggableThreadPoolExecutor("HINTED-HANDOFF-POOL");
    Timer timer = new Timer("HINTED-HANDOFF-TIMER");
    public static final String HINTS_CF = "HintsColumnFamily";


    public static HintedHandOffManager instance()
    {
        if ( instance_ == null )
        {
            lock_.lock();
            try
            {
                if ( instance_ == null )
                    instance_ = new HintedHandOffManager();
            }
            finally
            {
                lock_.unlock();
            }
        }
        return instance_;
    }

    private static boolean sendMessage(InetAddress endPoint, String tableName, String key) throws IOException
    {
        if (!FailureDetector.instance().isAlive(endPoint))
        {
            return false;
        }

        Table table = Table.open(tableName);
        Row row = table.get(key);
        RowMutation rm = new RowMutation(tableName, row);
        Message message = rm.makeRowMutationMessage();
        WriteResponseHandler responseHandler = new WriteResponseHandler(1);
        MessagingService.instance().sendRR(message, new InetAddress[] { endPoint }, responseHandler);

        try
        {
            responseHandler.get();
        }
        catch (TimeoutException e)
        {
            return false;
        }
        return true;
    }

    private static void deleteEndPoint(byte[] endpointAddress, String tableName, byte[] key, long timestamp) throws IOException
    {
        RowMutation rm = new RowMutation(Table.SYSTEM_TABLE, tableName);
        rm.delete(new QueryPath(HINTS_CF, key, endpointAddress), timestamp);
        rm.apply();
    }

    private static void deleteHintKey(String tableName, byte[] key) throws IOException
    {
        RowMutation rm = new RowMutation(Table.SYSTEM_TABLE, tableName);
        rm.delete(new QueryPath(HINTS_CF, key, null), System.currentTimeMillis());
        rm.apply();
    }

    /** hintStore must be the hints columnfamily from the system table */
    private static void deliverAllHints(ColumnFamilyStore hintStore) throws DigestMismatchException, IOException, InvalidRequestException, TimeoutException
    {
        if (logger_.isDebugEnabled())
          logger_.debug("Started deliverAllHints");

        // 1. Scan through all the keys that we need to handoff
        // 2. For each key read the list of recipients and send
        // 3. Delete that recipient from the key if write was successful
        // 4. If all writes were success for a given key we can even delete the key .
        // 5. Now force a flush
        // 6. Do major compaction to clean up all deletes etc.
        // 7. I guess we are done
        for (String tableName : DatabaseDescriptor.getTables())
        {
            ColumnFamily hintColumnFamily = ColumnFamilyStore.removeDeleted(hintStore.getColumnFamily(new IdentityQueryFilter(tableName, new QueryPath(HINTS_CF))), Integer.MAX_VALUE);
            if (hintColumnFamily == null)
            {
                continue;
            }
            Collection<IColumn> keys = hintColumnFamily.getSortedColumns();

            for (IColumn keyColumn : keys)
            {
                Collection<IColumn> endpoints = keyColumn.getSubColumns();
                String keyStr = new String(keyColumn.name(), "UTF-8");
                int deleted = 0;
                for (IColumn endpoint : endpoints)
                {
                    if (sendMessage(InetAddress.getByAddress(endpoint.name()), tableName, keyStr))
                    {
                        deleteEndPoint(endpoint.name(), tableName, keyColumn.name(), System.currentTimeMillis());
                        deleted++;
                    }
                }
                if (deleted == endpoints.size())
                {
                    deleteHintKey(tableName, keyColumn.name());
                }
            }
        }
        hintStore.forceFlush();
        hintStore.doMajorCompaction(0);

        if (logger_.isDebugEnabled())
          logger_.debug("Finished deliverAllHints");
    }

    private static void deliverHintsToEndpoint(InetAddress endPoint) throws IOException, DigestMismatchException, InvalidRequestException, TimeoutException
    {
        if (logger_.isDebugEnabled())
          logger_.debug("Started hinted handoff for endPoint " + endPoint);

        byte[] targetEPBytes = endPoint.getAddress();
        // 1. Scan through all the keys that we need to handoff
        // 2. For each key read the list of recipients if the endpoint matches send
        // 3. Delete that recipient from the key if write was successful
        Table systemTable = Table.open(Table.SYSTEM_TABLE);
        for (String tableName : DatabaseDescriptor.getTables())
        {
            ColumnFamily hintedColumnFamily = systemTable.get(tableName, HINTS_CF);
            if (hintedColumnFamily == null)
            {
                continue;
            }
            Collection<IColumn> keys = hintedColumnFamily.getSortedColumns();

            for (IColumn keyColumn : keys)
            {
                String keyStr = new String(keyColumn.name(), "UTF-8");
                Collection<IColumn> endpoints = keyColumn.getSubColumns();
                for (IColumn hintEndPoint : endpoints)
                {
                    if (Arrays.equals(hintEndPoint.name(), targetEPBytes) && sendMessage(endPoint, tableName, keyStr))
                    {
                        if (endpoints.size() == 1)
                        {
                            deleteHintKey(tableName, keyColumn.name());
                        }
                        else
                        {
                            deleteEndPoint(hintEndPoint.name(), tableName, keyColumn.name(), System.currentTimeMillis());
                        }
                        break;
                    }
                }
            }
        }

        if (logger_.isDebugEnabled())
          logger_.debug("Finished hinted handoff for endpoint " + endPoint);
    }

    public void scheduleHandoffsFor(final ColumnFamilyStore columnFamilyStore)
    {
        final Runnable r = new Runnable()
        {
            public void run()
            {
                try
                {
                    deliverAllHints(columnFamilyStore);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        };
        timer.schedule(new TimerTask()
        {
            public void run()
            {
                executor_.execute(r);
            }
        }, INTERVAL_IN_MS, INTERVAL_IN_MS);
    }

    /*
     * This method is used to deliver hints to a particular endpoint.
     * When we learn that some endpoint is back up we deliver the data
     * to him via an event driven mechanism.
    */
    public void deliverHints(final InetAddress to)
    {
        Runnable r = new Runnable()
        {
            public void run()
            {
                try
                {
                    deliverHintsToEndpoint(to);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        };
    	executor_.submit(r);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3666.java