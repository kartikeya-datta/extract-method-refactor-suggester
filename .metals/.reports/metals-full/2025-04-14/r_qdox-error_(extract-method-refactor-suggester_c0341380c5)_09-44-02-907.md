error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3263.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3263.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3263.java
text:
```scala
e@@ndPointSnitch = (DatacenterEndPointSnitch) StorageService.instance.getEndPointSnitch();

package org.apache.cassandra.locator;
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
import java.io.IOError;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.Map.Entry;

import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.dht.Token;
import org.apache.cassandra.service.*;

/**
 * This Stategy is little diffrent than the Rack aware Statergy. If there is
 * replication factor is N. We will make sure that (N-1)%2 of the nodes are in
 * other Datacenter.... For example if we have 5 nodes this stategy will make
 * sure to make 2 copies out of 5 in other dataceneter.
 * <p/>
 * This class also caches the EndPoints and invalidates the cache if there is a
 * change in the number of tokens.
 */
public class DatacenterShardStategy extends AbstractReplicationStrategy
{
    private static Map<String, List<Token>> dcMap = new HashMap<String, List<Token>>();
    private static Map<String, Integer> dcReplicationFactor = new HashMap<String, Integer>();
    private static Map<String, Integer> quorumRepFactor = new HashMap<String, Integer>();
    private static int locQFactor = 0;
    private static DatacenterEndPointSnitch endPointSnitch;
    ArrayList<Token> tokens;

    private List<InetAddress> localEndPoints = new ArrayList<InetAddress>();

    private List<InetAddress> getLocalEndPoints()
    {
        return new ArrayList<InetAddress>(localEndPoints);
    }

    private Map<String, Integer> getQuorumRepFactor()
    {
        return new HashMap<String, Integer>(quorumRepFactor);
    }

    /**
     * This Method will get the required information of the EndPoint from the
     * DataCenterEndPointSnitch and poopulates this singleton class.
     */
    private synchronized void loadEndPoints(TokenMetadata metadata) throws IOException
    {
        endPointSnitch = (DatacenterEndPointSnitch) StorageService.instance().getEndPointSnitch();
        this.tokens = new ArrayList<Token>(tokens);
        String localDC = endPointSnitch.getLocation(InetAddress.getLocalHost());
        dcMap = new HashMap<String, List<Token>>();
        for (Token token : this.tokens)
        {
            InetAddress endPoint = metadata.getEndPoint(token);
            String dataCenter = endPointSnitch.getLocation(endPoint);
            if (dataCenter.equals(localDC))
            {
                localEndPoints.add(endPoint);
            }
            List<Token> lst = dcMap.get(dataCenter);
            if (lst == null)
            {
                lst = new ArrayList<Token>();
            }
            lst.add(token);
            dcMap.put(dataCenter, lst);
        }
        for (Entry<String, List<Token>> entry : dcMap.entrySet())
        {
            List<Token> valueList = entry.getValue();
            Collections.sort(valueList);
            dcMap.put(entry.getKey(), valueList);
        }
        dcReplicationFactor = endPointSnitch.getMapReplicationFactor();
        for (Entry<String, Integer> entry : dcReplicationFactor.entrySet())
        {
            String datacenter = entry.getKey();
            int qFactor = (entry.getValue() / 2 + 1);
            quorumRepFactor.put(datacenter, qFactor);
            if (datacenter.equals(localDC))
            {
                locQFactor = qFactor;
            }
        }
    }

    public DatacenterShardStategy(TokenMetadata tokenMetadata, int replicas)
    throws UnknownHostException
    {
        super(tokenMetadata, replicas);
        if ((!(DatabaseDescriptor.getEndPointSnitch() instanceof DatacenterEndPointSnitch)))
        {
            throw new IllegalArgumentException("DatacenterShardStrategy requires DatacenterEndpointSnitch");
        }
    }

    public ArrayList<InetAddress> getNaturalEndpoints(Token token, TokenMetadata metadata)
    {
        try
        {
            return getNaturalEndpointsInternal(token, metadata);
        }
        catch (IOException e)
        {
            throw new IOError(e);
        }
    }

    private ArrayList<InetAddress> getNaturalEndpointsInternal(Token searchToken, TokenMetadata metadata) throws IOException
    {
        ArrayList<InetAddress> endpoints = new ArrayList<InetAddress>();

        if (metadata.sortedTokens().size() == 0)
            return endpoints;

        if (null == tokens || tokens.size() != metadata.sortedTokens().size())
        {
            loadEndPoints(metadata);
        }

        for (String dc : dcMap.keySet())
        {
            int foundCount = 0;
            ArrayList<InetAddress> forloopReturn = new ArrayList<InetAddress>();
            int replicas_ = dcReplicationFactor.get(dc);
            List tokens = dcMap.get(dc);
            boolean bOtherRack = false;
            boolean doneDataCenterItr;
            int index = Collections.binarySearch(tokens, searchToken);
            if (index < 0)
            {
                index = (index + 1) * (-1);
                if (index >= tokens.size())
                {
                    index = 0;
                }
            }
            int totalNodes = tokens.size();
            // Add the node at the index by default
            InetAddress primaryHost = metadata.getEndPoint((Token) tokens.get(index));
            forloopReturn.add(primaryHost);
            foundCount++;
            if (replicas_ == 1)
            {
                continue;
            }

            int startIndex = (index + 1) % totalNodes;
            for (int i = startIndex, count = 1; count < totalNodes && foundCount < replicas_; ++count, i = (i + 1) % totalNodes)
            {
                InetAddress endPointOfIntrest = metadata.getEndPoint((Token) tokens.get(i));
                if ((replicas_ - 1) > foundCount)
                {
                    forloopReturn.add(endPointOfIntrest);
                    foundCount++;
                    continue;
                }
                else
                {
                    doneDataCenterItr = true;
                }
                
                // Now try to find one on a different rack
                if (!bOtherRack)
                {
                    if (!endPointSnitch.isOnSameRack(primaryHost, endPointOfIntrest))
                    {
                        forloopReturn.add(metadata.getEndPoint((Token) tokens.get(i)));
                        bOtherRack = true;
                        foundCount++;
                    }
                }
                // If both already found exit loop.
                if (doneDataCenterItr && bOtherRack)
                {
                    break;
                }
            }

            /*
            * If we found N number of nodes we are good. This loop wil just
            * exit. Otherwise just loop through the list and add until we
            * have N nodes.
            */
            for (int i = startIndex, count = 1; count < totalNodes && foundCount < replicas_; ++count, i = (i + 1) % totalNodes)
            {
                Token t = (Token) tokens.get(i);
                if (!forloopReturn.contains(metadata.getEndPoint(t)))
                {
                    forloopReturn.add(metadata.getEndPoint(t));
                    foundCount++;
                }
            }
            endpoints.addAll(forloopReturn);
        }

        return endpoints;
    }

    /**
     * This method will generate the QRH object and returns. If the Consistency
     * level is DCQUORUM then it will return a DCQRH with a map of local rep
     * factor alone. If the consistency level is DCQUORUMSYNC then it will
     * return a DCQRH with a map of all the DC rep facor.
     */
    @Override
    public WriteResponseHandler getWriteResponseHandler(int blockFor, ConsistencyLevel consistency_level)
    {
        if (consistency_level == ConsistencyLevel.DCQUORUM)
        {
            return new DatacenterWriteResponseHandler(locQFactor);
        }
        else if (consistency_level == ConsistencyLevel.DCQUORUMSYNC)
        {
            return new DatacenterSyncWriteResponseHandler(getQuorumRepFactor());
        }
        return super.getWriteResponseHandler(blockFor, consistency_level);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3263.java