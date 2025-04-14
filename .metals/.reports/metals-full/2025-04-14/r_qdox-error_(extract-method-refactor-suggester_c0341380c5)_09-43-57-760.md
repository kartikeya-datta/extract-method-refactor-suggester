error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7634.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7634.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7634.java
text:
```scala
l@@ogger.debug("Adding {}, addresses {}", nodeMetadata.getName(), nodeMetadata.getPrivateAddresses());

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

package org.elasticsearch.discovery.cloud;

import org.elasticsearch.cloud.compute.CloudComputeService;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.discovery.zen.ping.unicast.UnicastZenPing;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;
import org.elasticsearch.util.settings.Settings;
import org.elasticsearch.util.transport.InetSocketTransportAddress;
import org.elasticsearch.util.transport.PortsRange;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.NodeState;
import org.jclouds.compute.options.GetNodesOptions;
import org.jclouds.domain.Location;

import java.net.InetAddress;
import java.util.List;
import java.util.Set;

import static org.elasticsearch.util.collect.Lists.*;

/**
 * @author kimchy (shay.banon)
 */
public class CloudZenPing extends UnicastZenPing {

    private final ComputeService computeService;

    private final String ports;

    private final String tag;

    private final String location;

    public CloudZenPing(Settings settings, ThreadPool threadPool, TransportService transportService, ClusterName clusterName,
                        CloudComputeService computeService) {
        super(settings, threadPool, transportService, clusterName);
        this.computeService = computeService.context().getComputeService();
        this.tag = componentSettings.get("tag");
        this.location = componentSettings.get("location");
        this.ports = componentSettings.get("ports", "9300-9302");
        // parse the ports just to see that they are valid
        new PortsRange(ports).ports();
    }

    @Override protected List<DiscoveryNode> buildDynamicNodes() {
        List<DiscoveryNode> discoNodes = newArrayList();
        Set<? extends ComputeMetadata> nodes = computeService.listNodes(GetNodesOptions.Builder.withDetails());
        if (logger.isTraceEnabled()) {
            StringBuilder sb = new StringBuilder("Processing Nodes:");
            for (ComputeMetadata node : nodes) {
                sb.append("\n   -> ").append(node);
            }
            logger.trace(sb.toString());
        }
        for (ComputeMetadata node : nodes) {
            NodeMetadata nodeMetadata = (NodeMetadata) node;
            if (tag != null && !nodeMetadata.getTag().equals(tag)) {
                logger.trace("Filtering node {} with unmatched tag {}", nodeMetadata.getName(), nodeMetadata.getTag());
                continue;
            }
            boolean filteredByLocation = true;
            if (location != null) {
                Location nodeLocation = nodeMetadata.getLocation();
                if (location.equals(nodeLocation.getId())) {
                    filteredByLocation = false;
                } else {
                    if (nodeLocation.getParent() != null) {
                        if (location.equals(nodeLocation.getParent().getId())) {
                            filteredByLocation = false;
                        }
                    }
                }
            } else {
                filteredByLocation = false;
            }
            if (filteredByLocation) {
                logger.trace("Filtering node {} with unmatched location {}", nodeMetadata.getName(), nodeMetadata.getLocation());
                continue;
            }
            if (nodeMetadata.getState() == NodeState.PENDING || nodeMetadata.getState() == NodeState.RUNNING) {
                logger.debug("Adding {}/{}", nodeMetadata.getName(), nodeMetadata.getPrivateAddresses());
                for (InetAddress inetAddress : nodeMetadata.getPrivateAddresses()) {
                    for (int port : new PortsRange(ports).ports()) {
                        discoNodes.add(new DiscoveryNode("#cloud-" + inetAddress.getHostAddress() + "-" + port, new InetSocketTransportAddress(inetAddress, port)));
                    }
                }
            }
        }
        return discoNodes;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7634.java