error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1806.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1806.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1806.java
text:
```scala
C@@ollection<InetAddress> endpoints = tmd.getWriteEndpoints(keyTokens[i], table, strategy.getNaturalEndpoints(keyTokens[i])).left;

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

package org.apache.cassandra.locator;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import org.apache.cassandra.CleanupHelper;
import org.apache.cassandra.config.ConfigurationException;
import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.config.KSMetaData;
import org.apache.cassandra.db.Table;
import org.apache.cassandra.dht.*;
import org.apache.cassandra.service.StorageService;
import org.apache.cassandra.service.StorageServiceAccessor;
import org.apache.cassandra.utils.ByteBufferUtil;

import static org.junit.Assert.*;

public class SimpleStrategyTest extends CleanupHelper
{
    @Test
    public void tryValidTable()
    {
        assert Table.open("Keyspace1").getReplicationStrategy() != null;
    }

    @Test
    public void testBigIntegerEndpoints() throws UnknownHostException, ConfigurationException
    {
        List<Token> endpointTokens = new ArrayList<Token>();
        List<Token> keyTokens = new ArrayList<Token>();
        for (int i = 0; i < 5; i++) {
            endpointTokens.add(new BigIntegerToken(String.valueOf(10 * i)));
            keyTokens.add(new BigIntegerToken(String.valueOf(10 * i + 5)));
        }
        verifyGetNaturalEndpoints(endpointTokens.toArray(new Token[0]), keyTokens.toArray(new Token[0]));
    }

    @Test
    public void testStringEndpoints() throws UnknownHostException, ConfigurationException
    {
        IPartitioner partitioner = new OrderPreservingPartitioner();

        List<Token> endpointTokens = new ArrayList<Token>();
        List<Token> keyTokens = new ArrayList<Token>();
        for (int i = 0; i < 5; i++) {
            endpointTokens.add(new StringToken(String.valueOf((char)('a' + i * 2))));
            keyTokens.add(partitioner.getToken(ByteBufferUtil.bytes(String.valueOf((char)('a' + i * 2 + 1)))));
        }
        verifyGetNaturalEndpoints(endpointTokens.toArray(new Token[0]), keyTokens.toArray(new Token[0]));
    }

    // given a list of endpoint tokens, and a set of key tokens falling between the endpoint tokens,
    // make sure that the Strategy picks the right endpoints for the keys.
    private void verifyGetNaturalEndpoints(Token[] endpointTokens, Token[] keyTokens) throws UnknownHostException, ConfigurationException
    {
        TokenMetadata tmd;
        AbstractReplicationStrategy strategy;
        for (String table : DatabaseDescriptor.getNonSystemTables())
        {
            tmd = new TokenMetadata();
            strategy = getStrategy(table, tmd);
            List<InetAddress> hosts = new ArrayList<InetAddress>();
            for (int i = 0; i < endpointTokens.length; i++)
            {
                InetAddress ep = InetAddress.getByName("127.0.0." + String.valueOf(i + 1));
                tmd.updateNormalToken(endpointTokens[i], ep);
                hosts.add(ep);
            }

            for (int i = 0; i < keyTokens.length; i++)
            {
                List<InetAddress> endpoints = strategy.getNaturalEndpoints(keyTokens[i]);
                assertEquals(strategy.getReplicationFactor(), endpoints.size());
                List<InetAddress> correctEndpoints = new ArrayList<InetAddress>();
                for (int j = 0; j < endpoints.size(); j++)
                    correctEndpoints.add(hosts.get((i + j + 1) % hosts.size()));
                assertEquals(new HashSet<InetAddress>(correctEndpoints), new HashSet<InetAddress>(endpoints));
            }
        }
    }
    
    @Test
    public void testGetEndpointsDuringBootstrap() throws UnknownHostException, ConfigurationException
    {
        // the token difference will be RING_SIZE * 2.
        final int RING_SIZE = 10;
        TokenMetadata tmd = new TokenMetadata();
        TokenMetadata oldTmd = StorageServiceAccessor.setTokenMetadata(tmd);

        Token[] endpointTokens = new Token[RING_SIZE];
        Token[] keyTokens = new Token[RING_SIZE];
        
        for (int i = 0; i < RING_SIZE; i++)
        {
            endpointTokens[i] = new BigIntegerToken(String.valueOf(RING_SIZE * 2 * i));
            keyTokens[i] = new BigIntegerToken(String.valueOf(RING_SIZE * 2 * i + RING_SIZE));
        }
        
        List<InetAddress> hosts = new ArrayList<InetAddress>();
        for (int i = 0; i < endpointTokens.length; i++)
        {
            InetAddress ep = InetAddress.getByName("127.0.0." + String.valueOf(i + 1));
            tmd.updateNormalToken(endpointTokens[i], ep);
            hosts.add(ep);
        }

        // bootstrap at the end of the ring
        Token bsToken = new BigIntegerToken(String.valueOf(210));
        InetAddress bootstrapEndpoint = InetAddress.getByName("127.0.0.11");
        tmd.addBootstrapToken(bsToken, bootstrapEndpoint);

        AbstractReplicationStrategy strategy = null;
        for (String table : DatabaseDescriptor.getNonSystemTables())
        {
            strategy = getStrategy(table, tmd);

            StorageService.calculatePendingRanges(strategy, table);

            int replicationFactor = strategy.getReplicationFactor();

            for (int i = 0; i < keyTokens.length; i++)
            {
                Collection<InetAddress> endpoints = tmd.getWriteEndpoints(keyTokens[i], table, strategy.getNaturalEndpoints(keyTokens[i]));
                assertTrue(endpoints.size() >= replicationFactor);

                for (int j = 0; j < replicationFactor; j++)
                {
                    //Check that the old nodes are definitely included
                    assertTrue(endpoints.contains(hosts.get((i + j + 1) % hosts.size())));
                }

                // bootstrapEndpoint should be in the endpoints for i in MAX-RF to MAX, but not in any earlier ep.
                if (i < RING_SIZE - replicationFactor)
                    assertFalse(endpoints.contains(bootstrapEndpoint));
                else
                    assertTrue(endpoints.contains(bootstrapEndpoint));
            }
        }

        StorageServiceAccessor.setTokenMetadata(oldTmd);
    }

    private AbstractReplicationStrategy getStrategyWithNewTokenMetadata(AbstractReplicationStrategy strategy, TokenMetadata newTmd) throws ConfigurationException
    {
        return AbstractReplicationStrategy.createReplicationStrategy(
                strategy.table,
                strategy.getClass().getName(),
                newTmd,
                strategy.snitch,
                strategy.configOptions);
    }

    private AbstractReplicationStrategy getStrategy(String table, TokenMetadata tmd) throws ConfigurationException
    {
        KSMetaData ksmd =  DatabaseDescriptor.getKSMetaData(table);
        return AbstractReplicationStrategy.createReplicationStrategy(
                table,
                ksmd.strategyClass,
                tmd,
                new SimpleSnitch(),
                ksmd.strategyOptions);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1806.java