error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2767.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2767.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2767.java
text:
```scala
G@@ossiper.instance.start(1);

/**
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

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.apache.cassandra.CleanupHelper;
import org.apache.cassandra.Util;
import org.apache.cassandra.concurrent.Stage;
import org.apache.cassandra.config.ConfigurationException;
import org.apache.cassandra.dht.IPartitioner;
import org.apache.cassandra.dht.RandomPartitioner;
import org.apache.cassandra.dht.Token;
import org.apache.cassandra.gms.ApplicationState;
import org.apache.cassandra.gms.Gossiper;
import org.apache.cassandra.gms.VersionedValue;
import org.apache.cassandra.locator.TokenMetadata;
import org.apache.cassandra.net.Message;
import org.apache.cassandra.net.MessagingService;
import org.apache.cassandra.net.sink.IMessageSink;
import org.apache.cassandra.net.sink.SinkManager;
import org.apache.cassandra.streaming.StreamUtil;
import org.apache.cassandra.utils.FBUtilities;

import static org.junit.Assert.*;

public class RemoveTest extends CleanupHelper
{
    StorageService ss = StorageService.instance;
    TokenMetadata tmd = ss.getTokenMetadata();
    IPartitioner oldPartitioner;
    ArrayList<Token> endpointTokens = new ArrayList<Token>();
    ArrayList<Token> keyTokens = new ArrayList<Token>();
    List<InetAddress> hosts = new ArrayList<InetAddress>();
    InetAddress removalhost;
    Token removaltoken;

    @Before
    public void setup() throws IOException, ConfigurationException
    {
        tmd.clearUnsafe();
        IPartitioner partitioner = new RandomPartitioner();

        oldPartitioner = ss.setPartitionerUnsafe(partitioner);

        // create a ring of 5 nodes
        Util.createInitialRing(ss, partitioner, endpointTokens, keyTokens, hosts, 6);

        MessagingService.instance().listen(FBUtilities.getLocalAddress());
        Gossiper.instance.start(FBUtilities.getLocalAddress(), 1);
        for (int i = 0; i < 6; i++)
        {
            Gossiper.instance.initializeNodeUnsafe(hosts.get(i), 1);
        }
        removalhost = hosts.get(5);
        hosts.remove(removalhost);
        removaltoken = endpointTokens.get(5);
        endpointTokens.remove(removaltoken);
    }

    @After
    public void tearDown()
    {
        SinkManager.clear();
        MessagingService.instance().shutdown();
        ss.setPartitionerUnsafe(oldPartitioner);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBadToken()
    {
        final String token = StorageService.getPartitioner().getTokenFactory().toString(keyTokens.get(2));
        ss.removeToken(token);

    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLocalToken()
    {
        //first token should be localhost
        final String token = StorageService.getPartitioner().getTokenFactory().toString(endpointTokens.get(0));
        ss.removeToken(token);
    }

    @Test
    public void testRemoveToken() throws InterruptedException
    {
        IPartitioner partitioner = StorageService.getPartitioner();

        final String token = partitioner.getTokenFactory().toString(removaltoken);
        ReplicationSink rSink = new ReplicationSink();
        SinkManager.add(rSink);

        // start removal in background and send replication confirmations
        final AtomicBoolean success = new AtomicBoolean(false);
        Thread remover = new Thread()
        {
            public void run()
            {
                try
                {
                    ss.removeToken(token);
                }
                catch (Exception e)
                {
                    System.err.println(e);
                    e.printStackTrace();
                    return;
                }
                success.set(true);
            }
        };
        remover.start();

        Thread.sleep(1000); // make sure removal is waiting for confirmation

        assertTrue(tmd.isLeaving(removalhost));
        assertEquals(1, tmd.getLeavingEndpoints().size());

        for (InetAddress host : hosts)
        {
            Message msg = new Message(host, StorageService.Verb.REPLICATION_FINISHED, new byte[0]);
            MessagingService.instance().sendRR(msg, FBUtilities.getLocalAddress());
        }

        remover.join();

        assertTrue(success.get());
        assertTrue(tmd.getLeavingEndpoints().isEmpty());
    }

    @Test
    public void testStartRemoving()
    {
        IPartitioner partitioner = StorageService.getPartitioner();
        VersionedValue.VersionedValueFactory valueFactory = new VersionedValue.VersionedValueFactory(partitioner);

        NotificationSink nSink = new NotificationSink();
        ReplicationSink rSink = new ReplicationSink();
        SinkManager.add(nSink);
        SinkManager.add(rSink);

        assertEquals(0, tmd.getLeavingEndpoints().size());

        ss.onChange(hosts.get(1),
                    ApplicationState.STATUS,
                    valueFactory.removingNonlocal(endpointTokens.get(1), removaltoken));

        assertEquals(1, nSink.callCount);
        assertTrue(tmd.isLeaving(removalhost));
        assertEquals(1, tmd.getLeavingEndpoints().size());
    }

    @Test
    public void testFinishRemoving()
    {
        IPartitioner partitioner = StorageService.getPartitioner();
        VersionedValue.VersionedValueFactory valueFactory = new VersionedValue.VersionedValueFactory(partitioner);

        assertEquals(0, tmd.getLeavingEndpoints().size());

        ss.onChange(hosts.get(1),
                    ApplicationState.STATUS,
                    valueFactory.removedNonlocal(endpointTokens.get(1), removaltoken));

        assertFalse(Gossiper.instance.getLiveMembers().contains(removalhost));
        assertFalse(tmd.isMember(removalhost));
    }

    class ReplicationSink implements IMessageSink
    {
        public Message handleMessage(Message msg, String id, InetAddress to)
        {
            if (!msg.getVerb().equals(StorageService.Verb.STREAM_REQUEST))
                return msg;

            StreamUtil.finishStreamRequest(msg, to);

            return null;
        }
    }

    class NotificationSink implements IMessageSink
    {
        public int callCount = 0;

        public Message handleMessage(Message msg, String id, InetAddress to)
        {
            if (msg.getVerb().equals(StorageService.Verb.REPLICATION_FINISHED))
            {
                callCount++;
                assertEquals(Stage.MISC, msg.getMessageType());
                // simulate a response from remote server
                Message response = msg.getReply(FBUtilities.getLocalAddress(), new byte[]{ });
                MessagingService.instance().sendReply(response, id, FBUtilities.getLocalAddress());
                return null;
            }
            else
            {
                return msg;
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2767.java