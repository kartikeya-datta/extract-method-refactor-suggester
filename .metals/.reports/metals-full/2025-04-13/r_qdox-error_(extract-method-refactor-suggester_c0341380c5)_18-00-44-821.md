error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7620.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7620.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7620.java
text:
```scala
.@@put("index.shard.check_on_startup", true)

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

package org.elasticsearch.test.stress.rollingrestart;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.indices.status.IndexShardStatus;
import org.elasticsearch.action.admin.indices.status.IndicesStatusResponse;
import org.elasticsearch.action.admin.indices.status.ShardStatus;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.UUID;
import org.elasticsearch.common.io.FileSystemUtils;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.util.concurrent.jsr166y.ThreadLocalRandom;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.env.NodeEnvironment;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.node.internal.InternalNode;
import org.elasticsearch.search.SearchHit;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import static org.elasticsearch.common.settings.ImmutableSettings.*;
import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * @author kimchy (shay.banon)
 */
public class RollingRestartStressTest {

    private final ESLogger logger = Loggers.getLogger(getClass());

    private int numberOfShards = 5;
    private int numberOfReplicas = 1;
    private int numberOfNodes = 4;

    private int textTokens = 150;
    private int numberOfFields = 10;
    private long initialNumberOfDocs = 100000;

    private int indexers = 0;

    private TimeValue indexerThrottle = TimeValue.timeValueMillis(100);

    private Settings settings = ImmutableSettings.Builder.EMPTY_SETTINGS;

    private TimeValue period = TimeValue.timeValueMinutes(20);

    private boolean clearNodeData = true;

    private Node client;

    private AtomicLong indexCounter = new AtomicLong();
    private AtomicLong idCounter = new AtomicLong();


    public RollingRestartStressTest numberOfNodes(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
        return this;
    }

    public RollingRestartStressTest numberOfShards(int numberOfShards) {
        this.numberOfShards = numberOfShards;
        return this;
    }

    public RollingRestartStressTest numberOfReplicas(int numberOfReplicas) {
        this.numberOfReplicas = numberOfReplicas;
        return this;
    }

    public RollingRestartStressTest initialNumberOfDocs(long initialNumberOfDocs) {
        this.initialNumberOfDocs = initialNumberOfDocs;
        return this;
    }

    public RollingRestartStressTest textTokens(int textTokens) {
        this.textTokens = textTokens;
        return this;
    }

    public RollingRestartStressTest numberOfFields(int numberOfFields) {
        this.numberOfFields = numberOfFields;
        return this;
    }

    public RollingRestartStressTest indexers(int indexers) {
        this.indexers = indexers;
        return this;
    }

    public RollingRestartStressTest indexerThrottle(TimeValue indexerThrottle) {
        this.indexerThrottle = indexerThrottle;
        return this;
    }

    public RollingRestartStressTest period(TimeValue period) {
        this.period = period;
        return this;
    }

    public RollingRestartStressTest cleanNodeData(boolean clearNodeData) {
        this.clearNodeData = clearNodeData;
        return this;
    }

    public RollingRestartStressTest settings(Settings settings) {
        this.settings = settings;
        return this;
    }

    public void run() throws Exception {
        Node[] nodes = new Node[numberOfNodes];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = NodeBuilder.nodeBuilder().settings(settings).node();
        }
        client = NodeBuilder.nodeBuilder().settings(settings).client(true).node();

        client.client().admin().indices().prepareCreate("test").setSettings(settingsBuilder()
                .put("index.number_of_shards", numberOfShards)
                .put("index.number_of_replicas", numberOfReplicas)
        ).execute().actionGet();

        logger.info("********** [START] INDEXING INITIAL DOCS");
        for (long i = 0; i < initialNumberOfDocs; i++) {
            indexDoc();
        }
        logger.info("********** [DONE ] INDEXING INITIAL DOCS");

        Indexer[] indexerThreads = new Indexer[indexers];
        for (int i = 0; i < indexerThreads.length; i++) {
            indexerThreads[i] = new Indexer();
        }
        for (int i = 0; i < indexerThreads.length; i++) {
            indexerThreads[i].start();
        }

        long testStart = System.currentTimeMillis();

        // start doing the rolling restart
        int nodeIndex = 0;
        while (true) {
            File[] nodeData = ((InternalNode) nodes[nodeIndex]).injector().getInstance(NodeEnvironment.class).nodeDataLocations();
            nodes[nodeIndex].close();
            if (clearNodeData) {
                FileSystemUtils.deleteRecursively(nodeData);
            }

            try {
                ClusterHealthResponse clusterHealth = client.client().admin().cluster().prepareHealth()
                        .setWaitForGreenStatus()
                        .setWaitForNodes(Integer.toString(numberOfNodes + 0 /* client node*/))
                        .setWaitForRelocatingShards(0)
                        .setTimeout("10m").execute().actionGet();
                if (clusterHealth.timedOut()) {
                    logger.warn("timed out waiting for green status....");
                }
            } catch (Exception e) {
                logger.warn("failed to execute cluster health....");
            }

            nodes[nodeIndex] = NodeBuilder.nodeBuilder().settings(settings).node();

            Thread.sleep(1000);

            try {
                ClusterHealthResponse clusterHealth = client.client().admin().cluster().prepareHealth()
                        .setWaitForGreenStatus()
                        .setWaitForNodes(Integer.toString(numberOfNodes + 1 /* client node*/))
                        .setWaitForRelocatingShards(0)
                        .setTimeout("10m").execute().actionGet();
                if (clusterHealth.timedOut()) {
                    logger.warn("timed out waiting for green status....");
                }
            } catch (Exception e) {
                logger.warn("failed to execute cluster health....");
            }

            if (++nodeIndex == nodes.length) {
                nodeIndex = 0;
            }

            if ((System.currentTimeMillis() - testStart) > period.millis()) {
                logger.info("test finished");
                break;
            }
        }

        for (int i = 0; i < indexerThreads.length; i++) {
            indexerThreads[i].close = true;
        }

        Thread.sleep(indexerThrottle.millis() + 10000);

        for (int i = 0; i < indexerThreads.length; i++) {
            if (!indexerThreads[i].closed) {
                logger.warn("thread not closed!");
            }
        }

        client.client().admin().indices().prepareRefresh().execute().actionGet();

        // check the status
        IndicesStatusResponse status = client.client().admin().indices().prepareStatus("test").execute().actionGet();
        for (IndexShardStatus shardStatus : status.index("test")) {
            ShardStatus shard = shardStatus.shards()[0];
            logger.info("shard [{}], docs [{}]", shard.shardId(), shard.getDocs().numDocs());
            for (ShardStatus shardStatu : shardStatus) {
                if (shard.docs().numDocs() != shardStatu.docs().numDocs()) {
                    logger.warn("shard doc number does not match!, got {} and {}", shard.docs().numDocs(), shardStatu.docs().numDocs());
                }
            }
        }

        // check the count
        for (int i = 0; i < (nodes.length * 5); i++) {
            CountResponse count = client.client().prepareCount().setQuery(matchAllQuery()).execute().actionGet();
            logger.info("indexed [{}], count [{}], [{}]", count.count(), indexCounter.get(), count.count() == indexCounter.get() ? "OK" : "FAIL");
            if (count.count() != indexCounter.get()) {
                logger.warn("count does not match!");
            }
        }

        // scan all the docs, verify all have the same version based on the number of replicas
        SearchResponse searchResponse = client.client().prepareSearch()
                .setSearchType(SearchType.SCAN)
                .setQuery(matchAllQuery())
                .setSize(50)
                .setScroll(TimeValue.timeValueMinutes(2))
                .execute().actionGet();
        logger.info("Verifying versions for {} hits...", searchResponse.hits().totalHits());

        while (true) {
            searchResponse = client.client().prepareSearchScroll(searchResponse.scrollId()).setScroll(TimeValue.timeValueMinutes(2)).execute().actionGet();
            if (searchResponse.failedShards() > 0) {
                logger.warn("Search Failures " + Arrays.toString(searchResponse.shardFailures()));
            }
            for (SearchHit hit : searchResponse.hits()) {
                long version = -1;
                for (int i = 0; i < (numberOfReplicas + 1); i++) {
                    GetResponse getResponse = client.client().prepareGet(hit.index(), hit.type(), hit.id()).execute().actionGet();
                    if (version == -1) {
                        version = getResponse.version();
                    } else {
                        if (version != getResponse.version()) {
                            logger.warn("Doc {} has different version numbers {} and {}", hit.id(), version, getResponse.version());
                        }
                    }
                }
            }
            if (searchResponse.hits().hits().length == 0) {
                break;
            }
        }
        logger.info("Done verifying versions");

        client.close();
        for (Node node : nodes) {
            node.close();
        }
    }

    private class Indexer extends Thread {

        volatile boolean close = false;

        volatile boolean closed = false;

        @Override public void run() {
            while (true) {
                if (close) {
                    closed = true;
                    return;
                }
                try {
                    indexDoc();
                    Thread.sleep(indexerThrottle.millis());
                } catch (Exception e) {
                    logger.warn("failed to index / sleep", e);
                }
            }
        }
    }

    private void indexDoc() throws Exception {
        StringBuilder sb = new StringBuilder();
        XContentBuilder json = XContentFactory.jsonBuilder().startObject()
                .field("field", "value" + ThreadLocalRandom.current().nextInt());

        int fields = Math.abs(ThreadLocalRandom.current().nextInt()) % numberOfFields;
        for (int i = 0; i < fields; i++) {
            json.field("num_" + i, ThreadLocalRandom.current().nextDouble());
            int tokens = ThreadLocalRandom.current().nextInt() % textTokens;
            sb.setLength(0);
            for (int j = 0; j < tokens; j++) {
                sb.append(UUID.randomBase64UUID()).append(' ');
            }
            json.field("text_" + i, sb.toString());
        }

        json.endObject();

        String id = Long.toString(idCounter.incrementAndGet());
        client.client().prepareIndex("test", "type1", id)
                .setCreate(true)
                .setSource(json)
                .execute().actionGet();
        indexCounter.incrementAndGet();
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("es.logger.prefix", "");

        Settings settings = settingsBuilder()
                .put("index.shard.check_index", true)
                .put("gateway.type", "none")
                .put("path.data", "data/data1,data/data2")
                .build();

        RollingRestartStressTest test = new RollingRestartStressTest()
                .settings(settings)
                .numberOfNodes(4)
                .numberOfShards(5)
                .numberOfReplicas(1)
                .initialNumberOfDocs(1000)
                .textTokens(150)
                .numberOfFields(10)
                .cleanNodeData(false)
                .indexers(5)
                .indexerThrottle(TimeValue.timeValueMillis(50))
                .period(TimeValue.timeValueMinutes(3));

        test.run();
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7620.java