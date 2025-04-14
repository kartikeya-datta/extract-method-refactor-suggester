error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4761.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4761.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4761.java
text:
```scala
C@@lusterHealthResponse clusterHealthResponse = client.client().admin().cluster().prepareHealth().setWaitForGreenStatus().setWaitForRelocatingShards(0).setTimeout("10m").execute().actionGet();

/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
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

package org.elasticsearch.test.stress.rollingrestart;

import jsr166y.ThreadLocalRandom;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.common.RandomStringGenerator;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.SizeValue;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

import java.util.Date;

/**
 */
public class QuickRollingRestartStressTest {

    public static void main(String[] args) throws Exception {
        System.setProperty("es.logger.prefix", "");

        Settings settings = ImmutableSettings.settingsBuilder().build();

        Node[] nodes = new Node[5];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = NodeBuilder.nodeBuilder().settings(settings).node();
        }

        Node client = NodeBuilder.nodeBuilder().client(true).node();

        long COUNT;
        if (client.client().admin().indices().prepareExists("test").execute().actionGet().exists()) {
            ClusterHealthResponse clusterHealthResponse = client.client().admin().cluster().prepareHealth().setWaitForGreenStatus().setTimeout("10m").execute().actionGet();
            if (clusterHealthResponse.timedOut()) {
                throw new ElasticSearchException("failed to wait for green state on startup...");
            }
            COUNT = client.client().prepareCount().execute().actionGet().count();
            System.out.println("--> existing index, count [" + COUNT + "]");
        } else {
            COUNT = SizeValue.parseSizeValue("100k").singles();
            System.out.println("--> indexing data...");
            for (long i = 0; i < COUNT; i++) {
                client.client().prepareIndex("test", "type", Long.toString(i))
                        .setSource("date", new Date(), "data", RandomStringGenerator.randomAlphabetic(10000))
                        .execute().actionGet();
            }
            System.out.println("--> done indexing data [" + COUNT + "]");
            client.client().admin().indices().prepareRefresh().execute().actionGet();
            for (int i = 0; i < 10; i++) {
                long count = client.client().prepareCount().execute().actionGet().count();
                if (COUNT != count) {
                    System.err.println("--> the indexed docs do not match the count..., got [" + count + "], expected [" + COUNT + "]");
                }
            }
        }

        final int ROLLING_RESTARTS = 100;
        System.out.println("--> starting rolling restarts [" + ROLLING_RESTARTS + "]");
        for (int rollingRestart = 0; rollingRestart < ROLLING_RESTARTS; rollingRestart++) {
            System.out.println("--> doing rolling restart [" + rollingRestart + "]...");
            int nodeId = ThreadLocalRandom.current().nextInt();
            for (int i = 0; i < nodes.length; i++) {
                int nodeIdx = Math.abs(nodeId++) % nodes.length;
                nodes[nodeIdx].close();
                nodes[nodeIdx] = NodeBuilder.nodeBuilder().settings(settings).node();
            }
            System.out.println("--> done rolling restart [" + rollingRestart + "]");

            System.out.println("--> waiting for green state now...");
            ClusterHealthResponse clusterHealthResponse = client.client().admin().cluster().prepareHealth().setWaitForGreenStatus().setTimeout("10m").execute().actionGet();
            if (clusterHealthResponse.timedOut()) {
                System.err.println("--> timed out waiting for green state...");
                ClusterState state = client.client().admin().cluster().prepareState().execute().actionGet().state();
                System.out.println(state.nodes().prettyPrint());
                System.out.println(state.routingTable().prettyPrint());
                System.out.println(state.routingNodes().prettyPrint());
                throw new ElasticSearchException("timed out waiting for green state");
            } else {
                System.out.println("--> got green status");
            }

            System.out.println("--> checking data [" + rollingRestart + "]....");
            boolean failed = false;
            for (int i = 0; i < 10; i++) {
                long count = client.client().prepareCount().execute().actionGet().count();
                if (COUNT != count) {
                    failed = true;
                    System.err.println("--> ERROR the indexed docs do not match the count..., got [" + count + "], expected [" + COUNT + "]");
                }
            }
            if (!failed) {
                System.out.println("--> count verified");
            }
        }

        System.out.println("--> shutting down...");
        client.close();
        for (Node node : nodes) {
            node.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4761.java