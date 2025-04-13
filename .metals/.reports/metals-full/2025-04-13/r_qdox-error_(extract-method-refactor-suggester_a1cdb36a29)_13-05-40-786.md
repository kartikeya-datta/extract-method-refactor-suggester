error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3580.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3580.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3580.java
text:
```scala
.@@put("gateway.type", "local")

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

package org.elasticsearch.test.stress.manyindices;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

import java.util.Date;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

/**
 *
 */
public class ManyIndicesStressTest {

    private static final ESLogger logger = Loggers.getLogger(ManyIndicesStressTest.class);

    public static void main(String[] args) throws Exception {
        System.setProperty("es.logger.prefix", "");

        int numberOfIndices = 100;
        int numberOfDocs = 100;

        Settings settings = ImmutableSettings.settingsBuilder()
                .put("index.shard.check_on_startup", false)
                .put("gateway.type", "fs")
                .put("index.number_of_shards", 1)
                .build();
        Node node = NodeBuilder.nodeBuilder().settings(settings).node();

        for (int i = 0; i < numberOfIndices; i++) {
            logger.info("START index [{}] ...", i);
            node.client().admin().indices().prepareCreate("index_" + i).execute().actionGet();

            for (int j = 0; j < numberOfDocs; j++) {
                node.client().prepareIndex("index_" + i, "type").setSource("field1", "test", "field2", 2, "field3", new Date()).execute().actionGet();
            }
            logger.info("DONE  index [{}] ...", i);
        }

        node.client().admin().indices().prepareGatewaySnapshot().execute().actionGet();

        logger.info("closing node...");
        node.close();
        logger.info("node closed");

        logger.info("starting node...");
        node = NodeBuilder.nodeBuilder().settings(settings).node();

        ClusterHealthResponse health = node.client().admin().cluster().prepareHealth().setTimeout("5m").setWaitForYellowStatus().execute().actionGet();
        logger.info("health: " + health.status());
        logger.info("active shards: " + health.activeShards());
        logger.info("active primary shards: " + health.activePrimaryShards());
        if (health.timedOut()) {
            logger.error("Timed out on health...");
        }

        ClusterState clusterState = node.client().admin().cluster().prepareState().execute().actionGet().state();
        for (int i = 0; i < numberOfIndices; i++) {
            if (clusterState.blocks().indices().containsKey("index_" + i)) {
                logger.error("index [{}] has blocks: {}", i, clusterState.blocks().indices().get("index_" + i));
            }
        }

        for (int i = 0; i < numberOfIndices; i++) {
            long count = node.client().prepareCount("index_" + i).setQuery(matchAllQuery()).execute().actionGet().count();
            if (count == numberOfDocs) {
                logger.info("VERIFIED [{}], count [{}]", i, count);
            } else {
                logger.error("FAILED [{}], expected [{}], got [{}]", i, numberOfDocs, count);
            }
        }

        logger.info("closing node...");
        node.close();
        logger.info("node closed");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3580.java