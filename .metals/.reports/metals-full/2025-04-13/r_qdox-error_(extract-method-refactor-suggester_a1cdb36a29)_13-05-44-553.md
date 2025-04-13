error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3792.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3792.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3792.java
text:
```scala
M@@ap<Range, List<String>> rangeMap = ssProxy.getRangeToEndpointMap(null);

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

package org.apache.cassandra.contrib.circuit;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import org.apache.cassandra.dht.Range;
import org.apache.cassandra.service.StorageServiceMBean;

/**
 * This class provides data abstraction for the JMX instrumentation
 * of Cassandra nodes. 
 */
public class RingModel
{
    public static final int defaultPort = 8080;
    private static final String fmtUrl = "service:jmx:rmi:///jndi/rmi://%s:%d/jmxrmi";
    private static final String ssObjName = "org.apache.cassandra.service:type=StorageService";

    private String seedName;
    private String seedAddr;
    private int seedPort;
    private List<Node> nodes;
    
    /**
     * Constructs a RingModel using the named reference host and port number.
     * 
     * @param seedName the hostname or IP of the startup node
     * @param seedPort JMX port number
     * @throws IOException if unable to setup the JMX connection
     */
    public RingModel(String seedName, int seedPort)
    {
        this.seedName = seedName;
        this.seedPort = seedPort;
        
        try
        {
            seedAddr = InetAddress.getByName(seedName).getHostAddress();
        }
        catch (UnknownHostException e)
        {
            System.err.println("Error unknown host: " + seedName);
            seedAddr = seedName;
        }
    }
    
    /**
     * Constructs a RingModel using the named reference host .
     * 
     * @param seedName the hostname or IP of the startup node
     */
    public RingModel(String seedName) throws IOException
    {
        this(seedName, defaultPort);
    }

    // @throws IOException if unable to setup the JMX connection
    private static List<Node> retrieveRingData(String seedAddress, String remoteHost, int port) throws IOException
    {
        JMXServiceURL jmxUrl = new JMXServiceURL(String.format(fmtUrl, remoteHost, port));
        JMXConnector jmxc = JMXConnectorFactory.connect(jmxUrl, null);
        StorageServiceMBean ssProxy;
        MBeanServerConnection mbeanServerConn = jmxc.getMBeanServerConnection();

        try
        {
            ObjectName name = new ObjectName(ssObjName);
            ssProxy = JMX.newMBeanProxy(mbeanServerConn, name, StorageServiceMBean.class);
        } catch (MalformedObjectNameException e)
        {
            throw new RuntimeException(
                    "Invalid ObjectName? Please report this as a bug.", e);
        }

        Map<Range, List<String>> rangeMap = ssProxy.getRangeToEndPointMap(null);
        List<Range> ranges = new ArrayList<Range>(rangeMap.keySet());
        Collections.sort(ranges);
        
        List<Node> nodes = new ArrayList<Node>();
        
        for (Range r : ranges)
        {
            String host = rangeMap.get(r).get(0);
            
            NodeStatus status;
            if (host.equals(seedAddress))
                status = NodeStatus.ISSEED;
            else
                status = NodeStatus.OK;
            
            String token = r.left.toString();
            nodes.add(new Node(host, status, token));
        }
        
        return nodes;
    }
    
    private List<Node> retrieveRingData(String remoteHost) throws IOException
    {
        return retrieveRingData(seedAddr, remoteHost, seedPort);
    }
    
    /**
     * Retrieves the nodes that are known to the reference host.
     * @return the list of nodes seen by the reference host.
     */
    public List<Node> getNodes()
    {
        if (this.nodes == null)
        {
            List<Node> nodes = new ArrayList<Node>();
            
            try
            {
                nodes = retrieveRingData(seedAddr, seedName, seedPort);
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
                nodes.add(new Node(seedName, NodeStatus.UNKNOWN, null));
            }
            
            this.nodes = nodes;
        }
        return nodes;
    }
    
    /**
     * Query the specified host for a list of nodes.
     * 
     * @param remoteHost the hostname or IP address of a node
     * @return the list of nodes seen by the specified host
     * @throws IOException if an error is encountered communicating with the node
     */
    public List<Node> getRemoteNodes(String remoteHost) throws IOException
    {
        return retrieveRingData(remoteHost);
    }
}

/**
 * Represents a node in the cluster.
 */
class Node
{
    public String host;
    public volatile NodeStatus nodeStatus;
    public String startToken;
    public volatile boolean isSelected;

    public Node(String host, NodeStatus status, String startToken)
    {
        this.host = host;
        this.nodeStatus = status;
        this.startToken = startToken;
    }
    
    public String getStartToken()
    {
        return startToken;
    }

    public String getHost()
    {
        return host;
    }

    public NodeStatus getStatus()
    {
        return nodeStatus;
    }
    
    public String toString()
    {
        return host;
    }
    
    public boolean isSelected()
    {
        return isSelected;
    }

    public void setSelected(boolean isSelected)
    {
        this.isSelected = isSelected;
    }
    
    public boolean isSeed()
    {
        return nodeStatus == NodeStatus.ISSEED ? true : false;
    }
    
    public void setStatus(NodeStatus status)
    {
        nodeStatus = status;
    }
    
    public boolean equals(Object o)
    {
        if (!(o instanceof Node))
            return false;
        
        Node other = (Node)o;
        return other.getHost().equals(host);
    }
    
    public int hashCode()
    {
        return (startToken + host).hashCode();
    }
}

enum NodeStatus
{
    OK,
    ISSEED,
    SHORT,
    LONG,
    UNKNOWN,
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3792.java