error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2142.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2142.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2142.java
text:
```scala
public v@@oid retrofitPorts(List<EndPoint> eps)

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import org.apache.cassandra.dht.Token;
import org.apache.cassandra.dht.IPartitioner;
import org.apache.cassandra.gms.FailureDetector;
import org.apache.cassandra.net.EndPoint;

/**
 * This class contains a helper method that will be used by
 * all abstraction that implement the IReplicaPlacementStrategy
 * interface.
*/
public abstract class AbstractReplicationStrategy
{
    protected static final Logger logger_ = Logger.getLogger(AbstractReplicationStrategy.class);

    protected TokenMetadata tokenMetadata_;
    protected IPartitioner partitioner_;
    protected int replicas_;
    protected int storagePort_;

    AbstractReplicationStrategy(TokenMetadata tokenMetadata, IPartitioner partitioner, int replicas, int storagePort)
    {
        tokenMetadata_ = tokenMetadata;
        partitioner_ = partitioner;
        replicas_ = replicas;
        storagePort_ = storagePort;
    }

    public abstract EndPoint[] getWriteStorageEndPoints(Token token);
    public abstract EndPoint[] getReadStorageEndPoints(Token token, Map<Token, EndPoint> tokenToEndPointMap);
    public abstract EndPoint[] getReadStorageEndPoints(Token token);

    /*
     * This method returns the hint map. The key is the endpoint
     * on which the data is being placed and the value is the
     * endpoint which is in the top N.
     * Get the map of top N to the live nodes currently.
     */
    public Map<EndPoint, EndPoint> getHintedStorageEndPoints(Token token)
    {
        return getHintedMapForEndpoints(getWriteStorageEndPoints(token));
    }

    /*
     * This method changes the ports of the endpoints from
     * the control port to the storage ports.
    */
    protected void retrofitPorts(List<EndPoint> eps)
    {
        for ( EndPoint ep : eps )
        {
            ep.setPort(storagePort_);
        }
    }

    protected EndPoint getNextAvailableEndPoint(EndPoint startPoint, List<EndPoint> topN, List<EndPoint> liveNodes)
    {
        EndPoint endPoint = null;
        Map<Token, EndPoint> tokenToEndPointMap = tokenMetadata_.cloneTokenEndPointMap();
        List tokens = new ArrayList(tokenToEndPointMap.keySet());
        Collections.sort(tokens);
        Token token = tokenMetadata_.getToken(startPoint);
        int index = Collections.binarySearch(tokens, token);
        if(index < 0)
        {
            index = (index + 1) * (-1);
            if (index >= tokens.size())
                index = 0;
        }
        int totalNodes = tokens.size();
        int startIndex = (index+1)%totalNodes;
        for (int i = startIndex, count = 1; count < totalNodes ; ++count, i = (i+1)%totalNodes)
        {
            EndPoint tmpEndPoint = tokenToEndPointMap.get(tokens.get(i));
            if(FailureDetector.instance().isAlive(tmpEndPoint) && !topN.contains(tmpEndPoint) && !liveNodes.contains(tmpEndPoint))
            {
                endPoint = tmpEndPoint;
                break;
            }
        }
        return endPoint;
    }

    private Map<EndPoint, EndPoint> getHintedMapForEndpoints(EndPoint[] topN)
    {
        List<EndPoint> liveList = new ArrayList<EndPoint>();
        Map<EndPoint, EndPoint> map = new HashMap<EndPoint, EndPoint>();

        for( int i = 0 ; i < topN.length ; i++)
        {
            if( FailureDetector.instance().isAlive(topN[i]))
            {
                map.put(topN[i], topN[i]);
                liveList.add(topN[i]) ;
            }
            else
            {
                EndPoint endPoint = getNextAvailableEndPoint(topN[i], Arrays.asList(topN), liveList);
                if(endPoint != null)
                {
                    map.put(endPoint, topN[i]);
                    liveList.add(endPoint) ;
                }
                else
                {
                    // log a warning , maybe throw an exception
                    logger_.warn("Unable to find a live Endpoint we might be out of live nodes , This is dangerous !!!!");
                }
            }
        }
        return map;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2142.java