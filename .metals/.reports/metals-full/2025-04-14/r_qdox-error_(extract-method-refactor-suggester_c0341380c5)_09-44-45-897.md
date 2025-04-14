error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5139.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5139.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5139.java
text:
```scala
S@@tringBuilder sb = new StringBuilder();

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

package org.elasticsearch.stresstest.fullrestart;

import jsr166y.ThreadLocalRandom;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.io.FileSystemUtils;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.env.NodeEnvironment;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.node.internal.InternalNode;

import java.io.File;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

/**
 *
 */
public class FullRestartStressTest {

    private final ESLogger logger = Loggers.getLogger(getClass());

    private int numberOfNodes = 4;

    private boolean clearNodeWork = false;

    private int numberOfIndices = 5;
    private int textTokens = 150;
    private int numberOfFields = 10;
    private int bulkSize = 1000;
    private int numberOfDocsPerRound = 50000;

    private Settings settings = ImmutableSettings.Builder.EMPTY_SETTINGS;

    private TimeValue period = TimeValue.timeValueMinutes(20);

    private AtomicLong indexCounter = new AtomicLong();

    public FullRestartStressTest numberOfNodes(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
        return this;
    }

    public FullRestartStressTest numberOfIndices(int numberOfIndices) {
        this.numberOfIndices = numberOfIndices;
        return this;
    }

    public FullRestartStressTest textTokens(int textTokens) {
        this.textTokens = textTokens;
        return this;
    }

    public FullRestartStressTest numberOfFields(int numberOfFields) {
        this.numberOfFields = numberOfFields;
        return this;
    }

    public FullRestartStressTest bulkSize(int bulkSize) {
        this.bulkSize = bulkSize;
        return this;
    }

    public FullRestartStressTest numberOfDocsPerRound(int numberOfDocsPerRound) {
        this.numberOfDocsPerRound = numberOfDocsPerRound;
        return this;
    }

    public FullRestartStressTest settings(Settings settings) {
        this.settings = settings;
        return this;
    }

    public FullRestartStressTest period(TimeValue period) {
        this.period = period;
        return this;
    }

    public FullRestartStressTest clearNodeWork(boolean clearNodeWork) {
        this.clearNodeWork = clearNodeWork;
        return this;
    }

    public void run() throws Exception {
        long numberOfRounds = 0;
        Random random = new Random(0);
        long testStart = System.currentTimeMillis();
        while (true) {
            Node[] nodes = new Node[numberOfNodes];
            for (int i = 0; i < nodes.length; i++) {
                nodes[i] = NodeBuilder.nodeBuilder().settings(settings).node();
            }
            Node client = NodeBuilder.nodeBuilder().settings(settings).client(true).node();

            // verify that the indices are there
            for (int i = 0; i < numberOfIndices; i++) {
                try {
                    client.client().admin().indices().prepareCreate("test" + i).execute().actionGet();
                } catch (Exception e) {
                    // might already exists, fine
                }
            }

            logger.info("*** Waiting for GREEN status");
            try {
                ClusterHealthResponse clusterHealth = client.client().admin().cluster().prepareHealth().setWaitForGreenStatus().setTimeout("10m").execute().actionGet();
                if (clusterHealth.isTimedOut()) {
                    logger.warn("timed out waiting for green status....");
                }
            } catch (Exception e) {
                logger.warn("failed to execute cluster health....");
            }

            CountResponse count = client.client().prepareCount().setQuery(matchAllQuery()).execute().actionGet();
            logger.info("*** index_count [{}], expected_count [{}]", count.getCount(), indexCounter.get());
            // verify count
            for (int i = 0; i < (nodes.length * 5); i++) {
                count = client.client().prepareCount().setQuery(matchAllQuery()).execute().actionGet();
                logger.debug("index_count [{}], expected_count [{}]", count.getCount(), indexCounter.get());
                if (count.getCount() != indexCounter.get()) {
                    logger.warn("!!! count does not match, index_count [{}], expected_count [{}]", count.getCount(), indexCounter.get());
                    throw new Exception("failed test, count does not match...");
                }
            }

            // verify search
            for (int i = 0; i < (nodes.length * 5); i++) {
                // do a search with norms field, so we don't rely on match all filtering cache
                SearchResponse search = client.client().prepareSearch().setQuery(matchAllQuery().normsField("field")).execute().actionGet();
                logger.debug("index_count [{}], expected_count [{}]", search.getHits().totalHits(), indexCounter.get());
                if (count.getCount() != indexCounter.get()) {
                    logger.warn("!!! search does not match, index_count [{}], expected_count [{}]", search.getHits().totalHits(), indexCounter.get());
                    throw new Exception("failed test, count does not match...");
                }
            }

            logger.info("*** ROUND {}", ++numberOfRounds);
            // bulk index data
            int numberOfBulks = numberOfDocsPerRound / bulkSize;
            for (int b = 0; b < numberOfBulks; b++) {
                BulkRequestBuilder bulk = client.client().prepareBulk();
                for (int k = 0; k < bulkSize; k++) {
                    StringBuffer sb = new StringBuffer();
                    XContentBuilder json = XContentFactory.jsonBuilder().startObject()
                            .field("field", "value" + ThreadLocalRandom.current().nextInt());

                    int fields = ThreadLocalRandom.current().nextInt() % numberOfFields;
                    for (int i = 0; i < fields; i++) {
                        json.field("num_" + i, ThreadLocalRandom.current().nextDouble());
                        int tokens = ThreadLocalRandom.current().nextInt() % textTokens;
                        sb.setLength(0);
                        for (int j = 0; j < tokens; j++) {
                            sb.append(Strings.randomBase64UUID(random)).append(' ');
                        }
                        json.field("text_" + i, sb.toString());
                    }

                    json.endObject();

                    bulk.add(Requests.indexRequest("test" + (Math.abs(ThreadLocalRandom.current().nextInt()) % numberOfIndices)).type("type1").source(json));
                    indexCounter.incrementAndGet();
                }
                bulk.execute().actionGet();
            }

            client.close();
            for (Node node : nodes) {
                File[] nodeDatas = ((InternalNode) node).injector().getInstance(NodeEnvironment.class).nodeDataLocations();
                node.close();
                if (clearNodeWork && !settings.get("gateway.type").equals("local")) {
                    FileSystemUtils.deleteRecursively(nodeDatas);
                }
            }

            if ((System.currentTimeMillis() - testStart) > period.millis()) {
                logger.info("test finished, full_restart_rounds [{}]", numberOfRounds);
                break;
            }

        }
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("es.logger.prefix", "");

        int numberOfNodes = 2;
        Settings settings = ImmutableSettings.settingsBuilder()
                .put("index.shard.check_on_startup", true)
                .put("gateway.type", "local")
                .put("gateway.recover_after_nodes", numberOfNodes)
                .put("index.number_of_shards", 1)
                .put("path.data", "data/data1,data/data2")
                .build();

        FullRestartStressTest test = new FullRestartStressTest()
                .settings(settings)
                .period(TimeValue.timeValueMinutes(20))
                .clearNodeWork(false) // only applies to shared gateway
                .numberOfNodes(numberOfNodes)
                .numberOfIndices(1)
                .textTokens(150)
                .numberOfFields(10)
                .bulkSize(1000)
                .numberOfDocsPerRound(10000);

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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5139.java