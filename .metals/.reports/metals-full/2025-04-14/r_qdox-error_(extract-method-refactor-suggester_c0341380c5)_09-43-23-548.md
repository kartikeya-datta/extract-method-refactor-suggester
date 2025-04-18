error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/611.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/611.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/611.java
text:
```scala
e@@xternalNodes[i] = externalNodes[i].start(client, defaultSettings, NODE_PREFIX + i, cluster.getClusterName(), i);

/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
package org.elasticsearch.test;

import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Iterators;
import com.carrotsearch.randomizedtesting.generators.RandomPicks;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.apache.lucene.util.IOUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.client.FilterClient;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

/**
 * A test cluster implementation that holds a fixed set of external nodes as well as a InternalTestCluster
 * which is used to run mixed version clusters in tests like backwards compatibility tests.
 * Note: this is an experimental API
 */
public class CompositeTestCluster extends TestCluster {
    private final InternalTestCluster cluster;
    private final ExternalNode[] externalNodes;
    private final ExternalClient client = new ExternalClient();
    private static final String NODE_PREFIX = "external_";

    public CompositeTestCluster(InternalTestCluster cluster, int numExternalNodes, ExternalNode externalNode) throws IOException {
        this.cluster = cluster;
        this.externalNodes = new ExternalNode[numExternalNodes];
        for (int i = 0; i < externalNodes.length; i++) {
            externalNodes[i] = externalNode;
        }
    }

    @Override
    public synchronized void afterTest() throws IOException {
        cluster.afterTest();
    }

    @Override
    public synchronized void beforeTest(Random random, double transportClientRatio) throws IOException {
        super.beforeTest(random, transportClientRatio);
        cluster.beforeTest(random, transportClientRatio);
        Settings defaultSettings = cluster.getDefaultSettings();
        final Client client = cluster.size() > 0 ? cluster.client() : cluster.clientNodeClient();
        for (int i = 0; i < externalNodes.length; i++) {
            if (!externalNodes[i].running()) {
                try {
                    externalNodes[i] = externalNodes[i].start(client, defaultSettings, NODE_PREFIX + i, cluster.getClusterName());
                } catch (InterruptedException e) {
                    Thread.interrupted();
                    return;
                }
            }
            externalNodes[i].reset(random.nextLong());
        }
        if (size() > 0) {
            client().admin().cluster().prepareHealth().setWaitForNodes(">=" + Integer.toString(this.size())).get();
        }
    }

    private Collection<ExternalNode> runningNodes() {
        return Collections2.filter(Arrays.asList(externalNodes), new Predicate<ExternalNode>() {
            @Override
            public boolean apply(ExternalNode input) {
                return input.running();
            }
        });
    }

    /**
     * Upgrades one external running node to a node from the version running the tests. Commonly this is used
     * to move from a node with version N-1 to a node running version N. This works seamless since they will
     * share the same data directory. This method will return <tt>true</tt> iff a node got upgraded otherwise if no
     * external node is running it returns <tt>false</tt>
     */
    public synchronized boolean upgradeOneNode() throws InterruptedException, IOException {
      return upgradeOneNode(ImmutableSettings.EMPTY);
    }

    /**
     * Upgrades all external running nodes to a node from the version running the tests.
     * All nodes are shut down before the first upgrade happens.
     * @return <code>true</code> iff at least one node as upgraded.
     */
    public synchronized boolean upgradeAllNodes() throws InterruptedException, IOException {
        return upgradeAllNodes(ImmutableSettings.EMPTY);
    }


    /**
     * Upgrades all external running nodes to a node from the version running the tests.
     * All nodes are shut down before the first upgrade happens.
     * @return <code>true</code> iff at least one node as upgraded.
     * @param nodeSettings settings for the upgrade nodes
     */
    public synchronized boolean upgradeAllNodes(Settings nodeSettings) throws InterruptedException, IOException {
        boolean upgradedOneNode = false;
        while(upgradeOneNode(nodeSettings)) {
            upgradedOneNode = true;
        }
        return upgradedOneNode;
    }

    /**
     * Upgrades one external running node to a node from the version running the tests. Commonly this is used
     * to move from a node with version N-1 to a node running version N. This works seamless since they will
     * share the same data directory. This method will return <tt>true</tt> iff a node got upgraded otherwise if no
     * external node is running it returns <tt>false</tt>
     */
    public synchronized boolean upgradeOneNode(Settings nodeSettings) throws InterruptedException, IOException {
        Collection<ExternalNode> runningNodes = runningNodes();
        if (!runningNodes.isEmpty()) {
            final Client existingClient = cluster.client();
            ExternalNode externalNode = RandomPicks.randomFrom(random, runningNodes);
            externalNode.stop();
            String s = cluster.startNode(nodeSettings);
            ExternalNode.waitForNode(existingClient, s);
            return true;
        }
        return false;
    }


    /**
     * Returns the a simple pattern that matches all "new" nodes in the cluster.
     */
    public String newNodePattern() {
        return cluster.nodePrefix() + "*";
    }

    /**
     * Returns the a simple pattern that matches all "old" / "backwardss" nodes in the cluster.
     */
    public String backwardsNodePattern() {
        return NODE_PREFIX + "*";
    }

    /**
     * Allows allocation of shards of the given indices on all nodes in the cluster.
     */
    public void allowOnAllNodes(String... index) {
        Settings build = ImmutableSettings.builder().put("index.routing.allocation.exclude._name", "").build();
        client().admin().indices().prepareUpdateSettings(index).setSettings(build).execute().actionGet();
    }

    /**
     * Allows allocation of shards of the given indices only on "new" nodes in the cluster.
     * Note: if a shard is allocated on an "old" node and can't be allocated on a "new" node it will only be removed it can
     * be allocated on some other "new" node.
     */
    public void allowOnlyNewNodes(String... index) {
        Settings build = ImmutableSettings.builder().put("index.routing.allocation.exclude._name", backwardsNodePattern()).build();
        client().admin().indices().prepareUpdateSettings(index).setSettings(build).execute().actionGet();
    }

    /**
     * Starts a current version data node
     */
    public void startNewNode() {
        cluster.startNode();
    }


    @Override
    public synchronized Client client() {
       return client;
    }

    @Override
    public synchronized int size() {
        return runningNodes().size() + cluster.size();
    }

    @Override
    public int numDataNodes() {
        return runningNodes().size() + cluster.numDataNodes();
    }

    @Override
    public int numBenchNodes() {
        return cluster.numBenchNodes();
    }

    @Override
    public InetSocketAddress[] httpAddresses() {
        return cluster.httpAddresses();
    }

    @Override
    public void close() throws IOException {
        try {
            IOUtils.close(externalNodes);
        } finally {
            IOUtils.close(cluster);
        }
    }

    @Override
    public boolean hasFilterCache() {
        return true;
    }

    @Override
    public synchronized Iterator<Client> iterator() {
        return Iterators.singletonIterator(client());
    }

    /**
     * Delegates to {@link org.elasticsearch.test.InternalTestCluster#fullRestart()}
     */
    public void fullRestartInternalCluster() throws Exception {
        cluster.fullRestart();
    }

    /**
     * Returns the number of current version data nodes in the cluster
     */
    public int numNewDataNodes() {
        return cluster.numDataNodes();
    }

    /**
     * Returns the number of former version data nodes in the cluster
     */
    public int numBackwardsDataNodes() {
        return runningNodes().size();
    }

    private synchronized Client internalClient() {
        Collection<ExternalNode> externalNodes = runningNodes();
        return random.nextBoolean() && !externalNodes.isEmpty() ? RandomPicks.randomFrom(random, externalNodes).getClient() : cluster.client();
    }

    private final class ExternalClient extends FilterClient {

        public ExternalClient() {
            super(null);
        }

        @Override
        protected Client in() {
            return internalClient();
        }

        @Override
        public ClusterAdminClient cluster() {
            return new ClusterAdmin(null) {

                @Override
                protected ClusterAdminClient in() {
                    return internalClient().admin().cluster();
                }
            };
        }

        @Override
        public IndicesAdminClient indices() {
            return new IndicesAdmin(null) {

                @Override
                protected IndicesAdminClient in() {
                    return internalClient().admin().indices();
                }
            };
        }

        @Override
        public void close() {
            // never close this client
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/611.java