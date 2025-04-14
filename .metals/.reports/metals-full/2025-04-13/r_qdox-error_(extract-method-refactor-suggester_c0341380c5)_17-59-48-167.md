error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8600.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8600.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8600.java
text:
```scala
private final N@@ettyTransport transport;

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
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

package org.elasticsearch.transport.netty;

import com.google.inject.Inject;
import org.elasticsearch.jmx.MBean;
import org.elasticsearch.jmx.ManagedAttribute;

/**
 * @author kimchy (Shay Banon)
 */
@MBean(objectName = "service=transport,transportType=netty", description = "Netty Transport")
public class NettyTransportManagement {

    private NettyTransport transport;

    @Inject public NettyTransportManagement(NettyTransport transport) {
        this.transport = transport;
    }

    @ManagedAttribute(description = "Number of connections this node has to other nodes")
    public long getNumberOfOutboundConnections() {
        return transport.clientChannels.size();
    }

    @ManagedAttribute(description = "Number if IO worker threads")
    public int getWorkerCount() {
        return transport.workerCount;
    }

    @ManagedAttribute(description = "Port(s) netty was configured to bind on")
    public String getPort() {
        return transport.port;
    }

    @ManagedAttribute(description = "Host to bind to")
    public String getBindHost() {
        return transport.bindHost;
    }

    @ManagedAttribute(description = "Host to publish")
    public String getPublishHost() {
        return transport.publishHost;
    }

    @ManagedAttribute(description = "Connect timeout")
    public String getConnectTimeout() {
        return transport.connectTimeout.toString();
    }

    @ManagedAttribute(description = "Connect retries")
    public int getConnectRetries() {
        return transport.connectRetries;
    }

    @ManagedAttribute(description = "TcpNoDelay")
    public Boolean getTcpNoDelay() {
        return transport.tcpNoDelay;
    }

    @ManagedAttribute(description = "TcpKeepAlive")
    public Boolean getTcpKeepAlive() {
        return transport.tcpKeepAlive;
    }

    @ManagedAttribute(description = "ReuseAddress")
    public Boolean getReuseAddress() {
        return transport.reuseAddress;
    }

    @ManagedAttribute(description = "TcpSendBufferSize")
    public String getTcpSendBufferSize() {
        if (transport.tcpSendBufferSize == null) {
            return null;
        }
        return transport.tcpSendBufferSize.toString();
    }

    @ManagedAttribute(description = "TcpReceiveBufferSize")
    public String getTcpReceiveBufferSize() {
        if (transport.tcpReceiveBufferSize == null) {
            return null;
        }
        return transport.tcpReceiveBufferSize.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8600.java