error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1479.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1479.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1479.java
text:
```scala
k@@eyTokens.add(partitioner.getToken(String.valueOf((char)('a' + i * 2 + 1)).getBytes()));

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

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.cassandra.SchemaLoader;
import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.gms.ApplicationState;
import org.apache.cassandra.service.StorageServiceAccessor;
import org.junit.Test;
import org.apache.cassandra.dht.IPartitioner;
import org.apache.cassandra.dht.RandomPartitioner;
import org.apache.cassandra.dht.BigIntegerToken;
import org.apache.cassandra.dht.Token;
import org.apache.cassandra.dht.OrderPreservingPartitioner;
import org.apache.cassandra.dht.StringToken;
import org.apache.cassandra.service.StorageService;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class RackUnawareStrategyTest extends SchemaLoader
{
    @Test
    public void tryBogusTable()
    {
        AbstractReplicationStrategy rs = StorageService.instance.getReplicationStrategy("Keyspace1");
        assertNotNull(rs);
        try
        {
            rs = StorageService.instance.getReplicationStrategy("SomeBogusTableThatDoesntExist");
            throw new AssertionError("SS.getReplicationStrategy() should have thrown a RuntimeException.");
        }
        catch (RuntimeException ex)
        {
            // This exception should be thrown.
        }
    }

    @Test
    public void testBigIntegerEndpoints() throws UnknownHostException
    {
        TokenMetadata tmd = new TokenMetadata();
        AbstractReplicationStrategy strategy = new RackUnawareStrategy(tmd, null);

        List<Token> endPointTokens = new ArrayList<Token>();
        List<Token> keyTokens = new ArrayList<Token>();
        for (int i = 0; i < 5; i++) {
            endPointTokens.add(new BigIntegerToken(String.valueOf(10 * i)));
            keyTokens.add(new BigIntegerToken(String.valueOf(10 * i + 5)));
        }
        for (String table : DatabaseDescriptor.getNonSystemTables())
            testGetEndpoints(tmd, strategy, endPointTokens.toArray(new Token[0]), keyTokens.toArray(new Token[0]), table);
    }

    @Test
    public void testStringEndpoints() throws UnknownHostException
    {
        TokenMetadata tmd = new TokenMetadata();
        IPartitioner partitioner = new OrderPreservingPartitioner();
        AbstractReplicationStrategy strategy = new RackUnawareStrategy(tmd, null);

        List<Token> endPointTokens = new ArrayList<Token>();
        List<Token> keyTokens = new ArrayList<Token>();
        for (int i = 0; i < 5; i++) {
            endPointTokens.add(new StringToken(String.valueOf((char)('a' + i * 2))));
            keyTokens.add(partitioner.getToken(String.valueOf((char)('a' + i * 2 + 1))));
        }
        for (String table : DatabaseDescriptor.getNonSystemTables())
            testGetEndpoints(tmd, strategy, endPointTokens.toArray(new Token[0]), keyTokens.toArray(new Token[0]), table);
    }

    // given a list of endpoint tokens, and a set of key tokens falling between the endpoint tokens,
    // make sure that the Strategy picks the right endpoints for the keys.
    private void testGetEndpoints(TokenMetadata tmd, AbstractReplicationStrategy strategy, Token[] endPointTokens, Token[] keyTokens, String table) throws UnknownHostException
    {
        List<InetAddress> hosts = new ArrayList<InetAddress>();
        for (int i = 0; i < endPointTokens.length; i++)
        {
            InetAddress ep = InetAddress.getByName("127.0.0." + String.valueOf(i + 1));
            tmd.updateNormalToken(endPointTokens[i], ep);
            hosts.add(ep);
        }

        for (int i = 0; i < keyTokens.length; i++)
        {
            List<InetAddress> endPoints = strategy.getNaturalEndpoints(keyTokens[i], table);
            assertEquals(DatabaseDescriptor.getReplicationFactor(table), endPoints.size());
            for (int j = 0; j < endPoints.size(); j++)
            {
                assertEquals(endPoints.get(j), hosts.get((i + j + 1) % hosts.size()));
            }
        }
    }
    
    @Test
    public void testGetEndpointsDuringBootstrap() throws UnknownHostException
    {
        // the token difference will be RING_SIZE * 2.
        final int RING_SIZE = 10;
        TokenMetadata tmd = new TokenMetadata();
        TokenMetadata oldTmd = StorageServiceAccessor.setTokenMetadata(tmd);
        AbstractReplicationStrategy strategy = new RackUnawareStrategy(tmd, null);

        Token[] endPointTokens = new Token[RING_SIZE];
        Token[] keyTokens = new Token[RING_SIZE];
        
        for (int i = 0; i < RING_SIZE; i++)
        {
            endPointTokens[i] = new BigIntegerToken(String.valueOf(RING_SIZE * 2 * i));
            keyTokens[i] = new BigIntegerToken(String.valueOf(RING_SIZE * 2 * i + RING_SIZE));
        }
        
        List<InetAddress> hosts = new ArrayList<InetAddress>();
        for (int i = 0; i < endPointTokens.length; i++)
        {
            InetAddress ep = InetAddress.getByName("127.0.0." + String.valueOf(i + 1));
            tmd.updateNormalToken(endPointTokens[i], ep);
            hosts.add(ep);
        }

        // bootstrap at the end of the ring
        Token bsToken = new BigIntegerToken(String.valueOf(210));
        InetAddress bootstrapEndPoint = InetAddress.getByName("127.0.0.11");
        tmd.addBootstrapToken(bsToken, bootstrapEndPoint);

        for (String table : DatabaseDescriptor.getNonSystemTables())
        {
            StorageService.calculatePendingRanges(strategy, table);
            int replicationFactor = DatabaseDescriptor.getReplicationFactor(table);

            for (int i = 0; i < keyTokens.length; i++)
            {
                Collection<InetAddress> endPoints = strategy.getWriteEndpoints(keyTokens[i], table, strategy.getNaturalEndpoints(keyTokens[i], table));
                assertTrue(endPoints.size() >= replicationFactor);

                for (int j = 0; j < replicationFactor; j++)
                {
                    //Check that the old nodes are definitely included
                    assertTrue(endPoints.contains(hosts.get((i + j + 1) % hosts.size())));
                }

                // bootstrapEndPoint should be in the endPoints for i in MAX-RF to MAX, but not in any earlier ep.
                if (i < RING_SIZE - replicationFactor)
                    assertFalse(endPoints.contains(bootstrapEndPoint));
                else
                    assertTrue(endPoints.contains(bootstrapEndPoint));
            }
        }

        StorageServiceAccessor.setTokenMetadata(oldTmd);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1479.java