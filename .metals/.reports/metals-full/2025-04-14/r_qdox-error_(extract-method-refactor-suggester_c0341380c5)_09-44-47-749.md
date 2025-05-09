error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6764.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6764.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6764.java
text:
```scala
w@@ipeIndices();

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

package org.elasticsearch.ttl;

import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.common.Priority;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.test.AbstractIntegrationTest;
import org.elasticsearch.test.AbstractIntegrationTest.ClusterScope;
import org.elasticsearch.test.AbstractIntegrationTest.Scope;
import org.junit.Test;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.hamcrest.Matchers.*;

@ClusterScope(scope=Scope.TEST)
public class SimpleTTLTests extends AbstractIntegrationTest {

    static private final long PURGE_INTERVAL = 200;
    
    @Override
    protected Settings nodeSettings(int nodeOrdinal) {
        return settingsBuilder()
                .put(super.nodeSettings(nodeOrdinal))
                .put("indices.ttl.interval", PURGE_INTERVAL)
                .put("index.number_of_shards", 2) // 2 shards to test TTL purge with routing properly
                .put("cluster.routing.operation.use_type", false) // make sure we control the shard computation
                .put("cluster.routing.operation.hash.type", "djb")
                .build();
    }

    @Test
    public void testSimpleTTL() throws Exception {
        client().admin().indices().prepareDelete().execute().actionGet();

        client().admin().indices().prepareCreate("test")
                .addMapping("type1", XContentFactory.jsonBuilder()
                        .startObject()
                        .startObject("type1")
                        .startObject("_timestamp").field("enabled", true).field("store", "yes").endObject()
                        .startObject("_ttl").field("enabled", true).field("store", "yes").endObject()
                        .endObject()
                        .endObject())
                .addMapping("type2", XContentFactory.jsonBuilder()
                        .startObject()
                        .startObject("type2")
                        .startObject("_timestamp").field("enabled", true).field("store", "yes").endObject()
                        .startObject("_ttl").field("enabled", true).field("store", "yes").field("default", "1d").endObject()
                        .endObject()
                        .endObject())
                .execute().actionGet();
        client().admin().cluster().prepareHealth().setWaitForEvents(Priority.LANGUID).setWaitForGreenStatus().execute().actionGet();

        long providedTTLValue = 3000;
        logger.info("--> checking ttl");
        // Index one doc without routing, one doc with routing, one doc with not TTL and no default and one doc with default TTL
        client().prepareIndex("test", "type1", "1").setSource("field1", "value1").setTTL(providedTTLValue).setRefresh(true).execute().actionGet();
        long now = System.currentTimeMillis();
        client().prepareIndex("test", "type1", "with_routing").setSource("field1", "value1").setTTL(providedTTLValue).setRouting("routing").setRefresh(true).execute().actionGet();
        client().prepareIndex("test", "type1", "no_ttl").setSource("field1", "value1").execute().actionGet();
        client().prepareIndex("test", "type2", "default_ttl").setSource("field1", "value1").execute().actionGet();

        // realtime get check
        long currentTime = System.currentTimeMillis();
        GetResponse getResponse = client().prepareGet("test", "type1", "1").setFields("_ttl").setRealtime(true).execute().actionGet();
        long ttl0;
        if (getResponse.isExists()) {
            ttl0 = ((Number) getResponse.getField("_ttl").getValue()).longValue();
            assertThat(ttl0, greaterThan(-PURGE_INTERVAL));
            assertThat(ttl0, lessThan(providedTTLValue - (currentTime - now)));
        } else {
            assertThat(providedTTLValue - (currentTime - now), lessThan(0l));
        }
        // verify the ttl is still decreasing when going to the replica
        currentTime = System.currentTimeMillis();
        getResponse = client().prepareGet("test", "type1", "1").setFields("_ttl").setRealtime(true).execute().actionGet();
        if (getResponse.isExists()) {
            ttl0 = ((Number) getResponse.getField("_ttl").getValue()).longValue();
            assertThat(ttl0, greaterThan(-PURGE_INTERVAL));
            assertThat(ttl0, lessThan(providedTTLValue - (currentTime - now)));
        } else {
            assertThat(providedTTLValue - (currentTime - now), lessThan(0l));
        }
        // non realtime get (stored)
        currentTime = System.currentTimeMillis();
        getResponse = client().prepareGet("test", "type1", "1").setFields("_ttl").setRealtime(false).execute().actionGet();
        if (getResponse.isExists()) {
            ttl0 = ((Number) getResponse.getField("_ttl").getValue()).longValue();
            assertThat(ttl0, greaterThan(-PURGE_INTERVAL));
            assertThat(ttl0, lessThan(providedTTLValue - (currentTime - now)));
        } else {
            assertThat(providedTTLValue - (currentTime - now), lessThan(0l));
        }
        // non realtime get going the replica
        currentTime = System.currentTimeMillis();
        getResponse = client().prepareGet("test", "type1", "1").setFields("_ttl").setRealtime(false).execute().actionGet();
        if (getResponse.isExists()) {
            ttl0 = ((Number) getResponse.getField("_ttl").getValue()).longValue();
            assertThat(ttl0, greaterThan(-PURGE_INTERVAL));
            assertThat(ttl0, lessThan(providedTTLValue - (currentTime - now)));
        } else {
            assertThat(providedTTLValue - (currentTime - now), lessThan(0l));
        }

        // no TTL provided so no TTL fetched
        getResponse = client().prepareGet("test", "type1", "no_ttl").setFields("_ttl").setRealtime(true).execute().actionGet();
        assertThat(getResponse.getField("_ttl"), nullValue());
        // no TTL provided make sure it has default TTL
        getResponse = client().prepareGet("test", "type2", "default_ttl").setFields("_ttl").setRealtime(true).execute().actionGet();
        ttl0 = ((Number) getResponse.getField("_ttl").getValue()).longValue();
        assertThat(ttl0, greaterThan(0L));

        // make sure the purger has done its job for all indexed docs that are expired
        long shouldBeExpiredDate = now + providedTTLValue + PURGE_INTERVAL + 2000;
        currentTime = System.currentTimeMillis();
        if (shouldBeExpiredDate - currentTime > 0) {
            Thread.sleep(shouldBeExpiredDate - currentTime);
        }

        // We can't assume that after waiting for ttl + purgeInterval (waitTime) that the document have actually been deleted.
        // The ttl purging happens in the background in a different thread, and might not have been completed after waiting for waitTime.
        // But we can use index statistics' delete count to be sure that deletes have been executed, that must be incremented before
        // ttl purging has finished.
        logger.info("--> checking purger");
        long currentDeleteCount;
        do {
            if (rarely()) {
                client().admin().indices().prepareFlush("test").setFull(true).execute().actionGet();
            } else if (rarely()) {
                client().admin().indices().prepareOptimize("test").setMaxNumSegments(1).execute().actionGet();
            }
            IndicesStatsResponse response = client().admin().indices().prepareStats("test")
                    .clear().setIndexing(true)
                    .execute().actionGet();
            currentDeleteCount = response.getIndices().get("test").getTotal().getIndexing().getTotal().getDeleteCount();
        } while (currentDeleteCount < 4); // TTL deletes two docs, but it is indexed in the primary shard and replica shard.
        assertThat(currentDeleteCount, equalTo(4l));

        // realtime get check
        getResponse = client().prepareGet("test", "type1", "1").setFields("_ttl").setRealtime(true).execute().actionGet();
        assertThat(getResponse.isExists(), equalTo(false));
        getResponse = client().prepareGet("test", "type1", "with_routing").setRouting("routing").setFields("_ttl").setRealtime(true).execute().actionGet();
        assertThat(getResponse.isExists(), equalTo(false));
        // replica realtime get check
        getResponse = client().prepareGet("test", "type1", "1").setFields("_ttl").setRealtime(true).execute().actionGet();
        assertThat(getResponse.isExists(), equalTo(false));
        getResponse = client().prepareGet("test", "type1", "with_routing").setRouting("routing").setFields("_ttl").setRealtime(true).execute().actionGet();
        assertThat(getResponse.isExists(), equalTo(false));

        // Need to run a refresh, in order for the non realtime get to work.
        client().admin().indices().prepareRefresh("test").execute().actionGet();

        // non realtime get (stored) check
        getResponse = client().prepareGet("test", "type1", "1").setFields("_ttl").setRealtime(false).execute().actionGet();
        assertThat(getResponse.isExists(), equalTo(false));
        getResponse = client().prepareGet("test", "type1", "with_routing").setRouting("routing").setFields("_ttl").setRealtime(false).execute().actionGet();
        assertThat(getResponse.isExists(), equalTo(false));
        // non realtime get going the replica check
        getResponse = client().prepareGet("test", "type1", "1").setFields("_ttl").setRealtime(false).execute().actionGet();
        assertThat(getResponse.isExists(), equalTo(false));
        getResponse = client().prepareGet("test", "type1", "with_routing").setRouting("routing").setFields("_ttl").setRealtime(false).execute().actionGet();
        assertThat(getResponse.isExists(), equalTo(false));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6764.java