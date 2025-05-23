error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/7.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/7.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/7.java
text:
```scala
t@@ablename = "Keyspace5";

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
package org.apache.cassandra.service;

import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.apache.cassandra.CleanupHelper;
import org.apache.cassandra.Util;
import org.apache.cassandra.concurrent.StageManager;
import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.db.*;
import org.apache.cassandra.db.filter.QueryPath;
import org.apache.cassandra.dht.IPartitioner;
import org.apache.cassandra.dht.Range;
import org.apache.cassandra.dht.Token;
import org.apache.cassandra.io.PrecompactedRow;
import org.apache.cassandra.io.util.DataOutputBuffer;
import org.apache.cassandra.locator.AbstractReplicationStrategy;
import org.apache.cassandra.locator.TokenMetadata;
import org.apache.cassandra.utils.FBUtilities;
import org.apache.cassandra.utils.MerkleTree;

import static com.google.common.base.Charsets.UTF_8;
import static org.apache.cassandra.service.AntiEntropyService.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AntiEntropyServiceTest extends CleanupHelper
{
    // table and column family to test against
    public static AntiEntropyService aes;

    public static String tablename;
    public static String cfname;
    public static TreeRequest request;
    public static ColumnFamilyStore store;
    public static InetAddress LOCAL, REMOTE;

    @BeforeClass
    public static void prepareClass() throws Exception
    {
        LOCAL = FBUtilities.getLocalAddress();
        tablename = "Keyspace4";
        StorageService.instance.initServer();
        // generate a fake endpoint for which we can spoof receiving/sending trees
        REMOTE = InetAddress.getByName("127.0.0.2");
        store = Table.open(tablename).getColumnFamilyStores().iterator().next();
        cfname = store.columnFamily_;
    }

    @Before
    public void prepare() throws Exception
    {
        aes = AntiEntropyService.instance;
        TokenMetadata tmd = StorageService.instance.getTokenMetadata();
        tmd.clearUnsafe();
        tmd.updateNormalToken(StorageService.getPartitioner().getRandomToken(), LOCAL);
        tmd.updateNormalToken(StorageService.getPartitioner().getMinimumToken(), REMOTE);
        assert tmd.isMember(REMOTE);
        
        // random session id for each test
        request = new TreeRequest(UUID.randomUUID().toString(), LOCAL, new CFPair(tablename, cfname));
    }

    @After
    public void teardown() throws Exception
    {
        // block for AES to clear before we teardown the token metadata for the next test.
        StageManager.getStage(StageManager.AE_SERVICE_STAGE).submit(new Runnable()
        {
            public void run() { /* no-op */ }
        }).get();
    }

    @Test
    public void testValidatorPrepare() throws Throwable
    {
        Validator validator;

        // write
        List<RowMutation> rms = new LinkedList<RowMutation>();
        RowMutation rm;
        rm = new RowMutation(tablename, "key1".getBytes());
        rm.add(new QueryPath(cfname, null, "Column1".getBytes()), "asdf".getBytes(), new TimestampClock(0));
        rms.add(rm);
        Util.writeColumnFamily(rms);

        // sample
        validator = new Validator(request);
        validator.prepare(store);

        // and confirm that the tree was split
        assertTrue(validator.tree.size() > 1);
    }
    
    @Test
    public void testValidatorComplete() throws Throwable
    {
        Validator validator = new Validator(request);
        validator.prepare(store);
        validator.complete();

        // confirm that the tree was validated
        Token min = validator.tree.partitioner().getMinimumToken();
        assert null != validator.tree.hash(new Range(min, min));

        // wait for queued operations to be flushed
        flushAES().get(5000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testValidatorAdd() throws Throwable
    {
        Validator validator = new Validator(request);
        IPartitioner part = validator.tree.partitioner();
        Token min = part.getMinimumToken();
        Token mid = part.midpoint(min, min);
        validator.prepare(store);

        // add a row with the minimum token
        validator.add(new PrecompactedRow(new DecoratedKey(min, "nonsense!".getBytes(UTF_8)),
                                       new DataOutputBuffer()));

        // and a row after it
        validator.add(new PrecompactedRow(new DecoratedKey(mid, "inconceivable!".getBytes(UTF_8)),
                                       new DataOutputBuffer()));
        validator.complete();

        // confirm that the tree was validated
        assert null != validator.tree.hash(new Range(min, min));
    }

    @Test
    public void testManualRepair() throws Throwable
    {
        AntiEntropyService.RepairSession sess = AntiEntropyService.instance.getRepairSession(tablename, cfname);
        sess.start();
        sess.blockUntilRunning();

        // ensure that the session doesn't end without a response from REMOTE
        sess.join(100);
        assert sess.isAlive();

        // deliver a fake response from REMOTE
        AntiEntropyService.instance.completedRequest(new TreeRequest(sess.getName(), REMOTE, request.cf));

        // block until the repair has completed
        sess.join();
    }

    @Test
    public void testGetNeighborsPlusOne() throws Throwable
    {
        // generate rf+1 nodes, and ensure that all nodes are returned
        Set<InetAddress> expected = addTokens(1 + 1 + DatabaseDescriptor.getReplicationFactor(tablename));
        expected.remove(FBUtilities.getLocalAddress());
        assertEquals(expected, AntiEntropyService.getNeighbors(tablename));
    }

    @Test
    public void testGetNeighborsTimesTwo() throws Throwable
    {
        TokenMetadata tmd = StorageService.instance.getTokenMetadata();

        // generate rf*2 nodes, and ensure that only neighbors specified by the ARS are returned
        addTokens(1 + (2 * DatabaseDescriptor.getReplicationFactor(tablename)));
        AbstractReplicationStrategy ars = StorageService.instance.getReplicationStrategy(tablename);
        Set<InetAddress> expected = new HashSet<InetAddress>();
        for (Range replicaRange : ars.getAddressRanges(tablename).get(FBUtilities.getLocalAddress()))
        {
            expected.addAll(ars.getRangeAddresses(tmd, tablename).get(replicaRange));
        }
        expected.remove(FBUtilities.getLocalAddress());
        assertEquals(expected, AntiEntropyService.getNeighbors(tablename));
    }

    @Test
    public void testDifferencer() throws Throwable
    {
        // generate a tree
        Validator validator = new Validator(request);
        validator.prepare(store);
        validator.complete();
        MerkleTree ltree = validator.tree;

        // and a clone
        validator = new Validator(request);
        validator.prepare(store);
        validator.complete();
        MerkleTree rtree = validator.tree;

        // change a range in one of the trees
        Token min = StorageService.instance.getPartitioner().getMinimumToken();
        ltree.invalidate(min);
        MerkleTree.TreeRange changed = ltree.invalids(new Range(min, min)).next();
        changed.hash("non-empty hash!".getBytes());

        // difference the trees
        Differencer diff = new Differencer(request, ltree, rtree);
        diff.run();
        
        // ensure that the changed range was recorded
        assertEquals("Wrong number of differing ranges", 1, diff.differences.size());
        assertEquals("Wrong differing range", changed, diff.differences.get(0));
    }

    Set<InetAddress> addTokens(int max) throws Throwable
    {
        TokenMetadata tmd = StorageService.instance.getTokenMetadata();
        Set<InetAddress> endpoints = new HashSet<InetAddress>();
        for (int i = 1; i < max; i++)
        {
            InetAddress endpoint = InetAddress.getByName("127.0.0." + i);
            tmd.updateNormalToken(StorageService.getPartitioner().getRandomToken(), endpoint);
            endpoints.add(endpoint);
        }
        return endpoints;
    }

    Future<Object> flushAES()
    {
        return StageManager.getStage(StageManager.AE_SERVICE_STAGE).submit(new Callable<Object>()
        {
            public Boolean call()
            {
                return true;
            }
        });
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/7.java