error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9295.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9295.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9295.java
text:
```scala
w@@ipeIndices("test");

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

package org.elasticsearch.search.basic;

import org.apache.lucene.util.LuceneTestCase.Slow;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.junit.Test;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.assertHitCount;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;


/**
 * This test basically verifies that search with a single shard active (cause we indexed to it) and other
 * shards possibly not active at all (cause they haven't allocated) will still work.
 */
public class SearchWhileCreatingIndexTests extends ElasticsearchIntegrationTest {

    @Test
    @Slow
    public void testIndexCausesIndexCreation() throws Exception {
        searchWhileCreatingIndex(-1, 1); // 1 replica in our default...
    }

    @Test
    @Slow
    public void testNoReplicas() throws Exception {
        searchWhileCreatingIndex(10, 0);
    }

    @Test
    @Slow
    public void testOneReplica() throws Exception {
        searchWhileCreatingIndex(10, 1);
    }

    @Test
    @Slow
    public void testTwoReplicas() throws Exception {
        searchWhileCreatingIndex(10, 2);
    }

    private void searchWhileCreatingIndex(int numberOfShards, int numberOfReplicas) throws Exception {

        // make sure we have enough nodes to guaranty default QUORUM consistency.
        // TODO: add a smarter choice based on actual consistency (when that is randomized)
        int shardsNo = numberOfReplicas + 1;
        int neededNodes = shardsNo <= 2 ? 1 : shardsNo / 2 + 1;
        cluster().ensureAtLeastNumNodes(randomIntBetween(neededNodes, shardsNo));
        for (int i = 0; i < 20; i++) {
            logger.info("running iteration {}", i);
            if (numberOfShards > 0) {
                CreateIndexResponse createIndexResponse = prepareCreate("test")
                        .setSettings(settingsBuilder().put("index.number_of_shards", numberOfShards).put("index.number_of_replicas", numberOfReplicas)).get();
                assertThat(createIndexResponse.isAcknowledged(), equalTo(true));
            }
            client().prepareIndex("test", "type1", randomAsciiOfLength(5)).setSource("field", "test").execute().actionGet();
            RefreshResponse refreshResponse = client().admin().indices().prepareRefresh("test").execute().actionGet();
            assertThat(refreshResponse.getSuccessfulShards(), greaterThanOrEqualTo(1)); // at least one shard should be successful when refreshing

            // we want to make sure that while recovery happens, and a replica gets recovered, its properly refreshed
            ClusterHealthStatus status = ClusterHealthStatus.RED;
            while (status != ClusterHealthStatus.GREEN) {
                // first, verify that search on the primary search works
                SearchResponse searchResponse = client().prepareSearch("test").setPreference("_primary").setQuery(QueryBuilders.termQuery("field", "test")).execute().actionGet();
                assertHitCount(searchResponse, 1);
                // now, let it go to primary or replica, though in a randomized re-creatable manner
                String preference = randomAsciiOfLength(5);
                Client client = client();
                searchResponse = client.prepareSearch("test").setPreference(preference).setQuery(QueryBuilders.termQuery("field", "test")).execute().actionGet();
                if (searchResponse.getHits().getTotalHits() != 1) {
                    refresh();
                    SearchResponse searchResponseAfterRefresh = client.prepareSearch("test").setPreference(preference).setQuery(QueryBuilders.termQuery("field", "test")).execute().actionGet();
                    logger.info("hits count mismatch on any shard search failed, post explicit refresh hits are {}", searchResponseAfterRefresh.getHits().getTotalHits());
                    ensureGreen();
                    SearchResponse searchResponseAfterGreen = client.prepareSearch("test").setPreference(preference).setQuery(QueryBuilders.termQuery("field", "test")).execute().actionGet();
                    logger.info("hits count mismatch on any shard search failed, post explicit wait for green hits are {}", searchResponseAfterGreen.getHits().getTotalHits());
                    assertHitCount(searchResponse, 1);
                }
                assertHitCount(searchResponse, 1);
                status = client().admin().cluster().prepareHealth("test").get().getStatus();
                cluster().ensureAtLeastNumNodes(numberOfReplicas + 1);
            }
            wipeIndex("test");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9295.java