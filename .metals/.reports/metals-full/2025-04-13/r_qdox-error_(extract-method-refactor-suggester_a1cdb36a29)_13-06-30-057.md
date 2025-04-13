error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7914.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7914.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7914.java
text:
```scala
.@@put(BalancedShardsAllocator.SETTING_THRESHOLD, 100.0f).build(); // use less aggressive settings

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
package org.elasticsearch.gateway.local;

import org.apache.lucene.util.LuceneTestCase;
import org.elasticsearch.Version;
import org.elasticsearch.action.admin.indices.recovery.RecoveryResponse;
import org.elasticsearch.action.admin.indices.recovery.ShardRecoveryResponse;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.routing.allocation.allocator.BalancedShardsAllocator;
import org.elasticsearch.cluster.routing.allocation.decider.EnableAllocationDecider;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.indices.recovery.RecoveryState;
import org.elasticsearch.test.ElasticsearchBackwardsCompatIntegrationTest;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.junit.Test;

import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.assertAcked;
import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.assertHitCount;
import static org.hamcrest.Matchers.*;

@ElasticsearchIntegrationTest.ClusterScope(numDataNodes = 0, scope = ElasticsearchIntegrationTest.Scope.TEST, numClientNodes = 0, transportClientRatio = 0.0)
public class RecoveryBackwardsCompatibilityTests extends ElasticsearchBackwardsCompatIntegrationTest {


    protected Settings nodeSettings(int nodeOrdinal) {
        return ImmutableSettings.builder()
                .put(super.nodeSettings(nodeOrdinal))
                .put("action.admin.cluster.node.shutdown.delay", "10ms")
                .put("gateway.recover_after_nodes", 2)
                .put(BalancedShardsAllocator.SETTING_THRESHOLD, 1.1f).build(); // use less aggressive settings
    }

    protected int minExternalNodes() {
        return 2;
    }

    protected int maxExternalNodes() {
        return 3;
    }


    @Test
    @LuceneTestCase.Slow
    public void testReusePeerRecovery() throws Exception {
        assertAcked(prepareCreate("test").setSettings(ImmutableSettings.builder().put(indexSettings()).put(IndexMetaData.SETTING_NUMBER_OF_REPLICAS, 0)));
        logger.info("--> indexing docs");
        int numDocs = scaledRandomIntBetween(100, 1000);
        IndexRequestBuilder[] builders = new IndexRequestBuilder[numDocs];
        for (int i = 0; i < builders.length; i++) {
            builders[i] = client().prepareIndex("test", "type").setSource("field", "value");
        }
        indexRandom(true, builders);
        ensureGreen();

        logger.info("--> bump number of replicas from 0 to 1");
        client().admin().indices().prepareFlush().execute().actionGet();
        client().admin().indices().prepareUpdateSettings("test").setSettings(ImmutableSettings.builder().put(IndexMetaData.SETTING_NUMBER_OF_REPLICAS, "1").build()).get();
        ensureGreen();

        assertAllShardsOnNodes("test", backwardsCluster().backwardsNodePattern());

        logger.info("--> upgrade cluster");
        logClusterState();
        CountResponse countResponse = client().prepareCount().get();
        assertHitCount(countResponse, numDocs);

        client().admin().cluster().prepareUpdateSettings().setTransientSettings(ImmutableSettings.settingsBuilder().put(EnableAllocationDecider.CLUSTER_ROUTING_ALLOCATION_ENABLE, "none")).execute().actionGet();
        backwardsCluster().upgradeAllNodes();
        client().admin().cluster().prepareUpdateSettings().setTransientSettings(ImmutableSettings.settingsBuilder().put(EnableAllocationDecider.CLUSTER_ROUTING_ALLOCATION_ENABLE, "all")).execute().actionGet();
        ensureGreen();

        countResponse = client().prepareCount().get();
        assertHitCount(countResponse, numDocs);

        RecoveryResponse recoveryResponse = client().admin().indices().prepareRecoveries("test").setDetailed(true).get();
        for (ShardRecoveryResponse response : recoveryResponse.shardResponses().get("test")) {
            RecoveryState recoveryState = response.recoveryState();
            if (!recoveryState.getPrimary()) {
                RecoveryState.Index index = recoveryState.getIndex();
                if (compatibilityVersion().onOrAfter(Version.V_1_2_0)) {
                    assertThat(index.toString(), index.recoveredByteCount(), equalTo(0l));
                    assertThat(index.toString(), index.reusedByteCount(), greaterThan(0l));
                    assertThat(index.toString(), index.reusedByteCount(), equalTo(index.totalByteCount()));
                    assertThat(index.toString(), index.recoveredFileCount(), equalTo(0));
                    assertThat(index.toString(), index.reusedFileCount(), equalTo(index.totalFileCount()));
                    assertThat(index.toString(), index.reusedFileCount(), greaterThan(0));
                    assertThat(index.toString(), index.percentBytesRecovered(), equalTo(0.f));
                    assertThat(index.toString(), index.percentFilesRecovered(), equalTo(0.f));
                    assertThat(index.toString(), index.reusedByteCount(), greaterThan(index.numberOfRecoveredBytes()));
                } else {
                    /* We added checksums on 1.3 but they were available on 1.2 already since this uses Lucene 4.8.
                     * yet in this test we upgrade the entire cluster and therefor the 1.3 nodes try to read the checksum
                     * from the files even if they haven't been written with ES 1.3. Due to that we don't have to recover
                     * the segments files if we are on 1.2 or above...*/
                    assertThat(index.toString(), index.recoveredByteCount(), greaterThan(0l));
                    assertThat(index.toString(), index.recoveredFileCount(), greaterThan(0));
                    assertThat(index.toString(), index.reusedByteCount(), greaterThan(0l));
                    assertThat(index.toString(), index.percentBytesRecovered(), greaterThan(0.0f));
                    assertThat(index.toString(), index.percentBytesRecovered(), lessThan(100.0f));
                    assertThat(index.toString(), index.percentFilesRecovered(), greaterThan(0.0f));
                    assertThat(index.toString(), index.percentFilesRecovered(), lessThan(100.0f));
                    assertThat(index.toString(), index.reusedByteCount(), greaterThan(index.numberOfRecoveredBytes()));
                }
                // TODO upgrade via optimize?
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7914.java