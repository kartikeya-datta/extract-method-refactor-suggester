error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7712.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7712.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7712.java
text:
```scala
I@@nternalIndexShard indexShard = (InternalIndexShard) (indicesService.indexService(index).shardSafe(shardId));

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

package org.elasticsearch.indices.store;

import org.apache.lucene.store.Directory;
import org.elasticsearch.env.NodeEnvironment;
import org.elasticsearch.index.shard.service.InternalIndexShard;
import org.elasticsearch.index.store.IndexStoreModule;
import org.elasticsearch.indices.IndicesService;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Set;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.hamcrest.Matchers.*;

/**
 *
 */
public class SimpleDistributorTests extends ElasticsearchIntegrationTest {

    @Test
    public void testAvailableSpaceDetection() {
        for (IndexStoreModule.Type store : IndexStoreModule.Type.values()) {
            if (store.fsStore()) {
                createIndexWithStoreType("test", store, StrictDistributor.class.getCanonicalName());
            }
        }
    }

    @Test
    public void testDirectoryToString() throws IOException {
        internalCluster().wipeTemplates(); // no random settings please
        createIndexWithStoreType("test", IndexStoreModule.Type.NIOFS, "least_used");
        String storeString = getStoreDirectory("test", 0).toString();
        logger.info(storeString);
        File[] dataPaths = dataPaths();
        assertThat(storeString.toLowerCase(Locale.ROOT), startsWith("store(least_used[rate_limited(niofs(" + dataPaths[0].getAbsolutePath().toLowerCase(Locale.ROOT)));
        if (dataPaths.length > 1) {
            assertThat(storeString.toLowerCase(Locale.ROOT), containsString("), rate_limited(niofs(" + dataPaths[1].getAbsolutePath().toLowerCase(Locale.ROOT)));
        }
        assertThat(storeString, endsWith(", type=MERGE, rate=20.0)])"));

        createIndexWithStoreType("test", IndexStoreModule.Type.NIOFS, "random");
        storeString = getStoreDirectory("test", 0).toString();
        logger.info(storeString);
        dataPaths = dataPaths();
        assertThat(storeString.toLowerCase(Locale.ROOT), startsWith("store(random[rate_limited(niofs(" + dataPaths[0].getAbsolutePath().toLowerCase(Locale.ROOT)));
        if (dataPaths.length > 1) {
            assertThat(storeString.toLowerCase(Locale.ROOT), containsString("), rate_limited(niofs(" + dataPaths[1].getAbsolutePath().toLowerCase(Locale.ROOT)));
        }
        assertThat(storeString, endsWith(", type=MERGE, rate=20.0)])"));

        createIndexWithStoreType("test", IndexStoreModule.Type.MMAPFS, "least_used");
        storeString = getStoreDirectory("test", 0).toString();
        logger.info(storeString);
        dataPaths = dataPaths();
        assertThat(storeString.toLowerCase(Locale.ROOT), startsWith("store(least_used[rate_limited(mmapfs(" + dataPaths[0].getAbsolutePath().toLowerCase(Locale.ROOT)));
        if (dataPaths.length > 1) {
            assertThat(storeString.toLowerCase(Locale.ROOT), containsString("), rate_limited(mmapfs(" + dataPaths[1].getAbsolutePath().toLowerCase(Locale.ROOT)));
        }
        assertThat(storeString, endsWith(", type=MERGE, rate=20.0)])"));

        createIndexWithStoreType("test", IndexStoreModule.Type.SIMPLEFS, "least_used");
        storeString = getStoreDirectory("test", 0).toString();
        logger.info(storeString);
        dataPaths = dataPaths();
        assertThat(storeString.toLowerCase(Locale.ROOT), startsWith("store(least_used[rate_limited(simplefs(" + dataPaths[0].getAbsolutePath().toLowerCase(Locale.ROOT)));
        if (dataPaths.length > 1) {
            assertThat(storeString.toLowerCase(Locale.ROOT), containsString("), rate_limited(simplefs(" + dataPaths[1].getAbsolutePath().toLowerCase(Locale.ROOT)));
        }
        assertThat(storeString, endsWith(", type=MERGE, rate=20.0)])"));

        createIndexWithStoreType("test", IndexStoreModule.Type.DEFAULT, "least_used");
        storeString = getStoreDirectory("test", 0).toString();
        logger.info(storeString);
        dataPaths = dataPaths();
        assertThat(storeString.toLowerCase(Locale.ROOT), startsWith("store(least_used[rate_limited(default(mmapfs(" + dataPaths[0].getAbsolutePath().toLowerCase(Locale.ROOT)));
        assertThat(storeString.toLowerCase(Locale.ROOT), containsString("),niofs(" + dataPaths[0].getAbsolutePath().toLowerCase(Locale.ROOT)));

        if (dataPaths.length > 1) {
            assertThat(storeString.toLowerCase(Locale.ROOT), containsString("), rate_limited(default(mmapfs(" + dataPaths[1].getAbsolutePath().toLowerCase(Locale.ROOT)));
        }
        assertThat(storeString, endsWith(", type=MERGE, rate=20.0)])"));

        createIndexWithStoreType("test", IndexStoreModule.Type.MEMORY, "least_used");
        storeString = getStoreDirectory("test", 0).toString();
        logger.info(storeString);
        dataPaths = dataPaths();
        assertThat(storeString, equalTo("store(least_used[ram])"));

        createIndexWithoutRateLimitingStoreType("test", IndexStoreModule.Type.NIOFS, "least_used");
        storeString = getStoreDirectory("test", 0).toString();
        logger.info(storeString);
        dataPaths = dataPaths();
        assertThat(storeString.toLowerCase(Locale.ROOT), startsWith("store(least_used[niofs(" + dataPaths[0].getAbsolutePath().toLowerCase(Locale.ROOT)));
        if (dataPaths.length > 1) {
            assertThat(storeString.toLowerCase(Locale.ROOT), containsString("), niofs(" + dataPaths[1].getAbsolutePath().toLowerCase(Locale.ROOT)));
        }
        assertThat(storeString, endsWith(")])"));
    }

    private void createIndexWithStoreType(String index, IndexStoreModule.Type storeType, String distributor) {
        cluster().wipeIndices(index);
        client().admin().indices().prepareCreate(index)
                .setSettings(settingsBuilder()
                        .put("index.store.distributor", distributor)
                        .put("index.store.type", storeType.name())
                        .put("index.number_of_replicas", 0)
                        .put("index.number_of_shards", 1)
                )
                .execute().actionGet();
        assertThat(client().admin().cluster().prepareHealth("test").setWaitForGreenStatus().execute().actionGet().isTimedOut(), equalTo(false));
    }

    private void createIndexWithoutRateLimitingStoreType(String index, IndexStoreModule.Type storeType, String distributor) {
        cluster().wipeIndices(index);
        client().admin().indices().prepareCreate(index)
                .setSettings(settingsBuilder()
                        .put("index.store.distributor", distributor)
                        .put("index.store.type", storeType)
                        .put("index.store.throttle.type", "none")
                        .put("index.number_of_replicas", 0)
                        .put("index.number_of_shards", 1)
                )
                .execute().actionGet();
        assertThat(client().admin().cluster().prepareHealth("test").setWaitForGreenStatus().execute().actionGet().isTimedOut(), equalTo(false));
    }


    private File[] dataPaths() {
        Set<String> nodes = internalCluster().nodesInclude("test");
        assertThat(nodes.isEmpty(), equalTo(false));
        NodeEnvironment env = internalCluster().getInstance(NodeEnvironment.class, nodes.iterator().next());
        return env.nodeDataLocations();
    }

    private Directory getStoreDirectory(String index, int shardId) {
        Set<String> nodes = internalCluster().nodesInclude("test");
        assertThat(nodes.isEmpty(), equalTo(false));
        IndicesService indicesService = internalCluster().getInstance(IndicesService.class, nodes.iterator().next());
        InternalIndexShard indexShard = (InternalIndexShard) (indicesService.indexService(index).shard(shardId));
        return indexShard.store().directory();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7712.java