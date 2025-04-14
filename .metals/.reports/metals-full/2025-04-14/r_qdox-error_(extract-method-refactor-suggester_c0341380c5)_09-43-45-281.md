error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7452.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7452.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7452.java
text:
```scala
n@@ew CopyOnWriteArrayList<>(new IndexShardState[]{newState}));

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
package org.elasticsearch.indices;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.shard.IndexShardState;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.index.shard.service.IndexShard;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.cluster.metadata.IndexMetaData.SETTING_NUMBER_OF_REPLICAS;
import static org.elasticsearch.cluster.metadata.IndexMetaData.SETTING_NUMBER_OF_SHARDS;
import static org.elasticsearch.cluster.routing.allocation.decider.DisableAllocationDecider.CLUSTER_ROUTING_ALLOCATION_DISABLE_ALLOCATION;
import static org.elasticsearch.common.settings.ImmutableSettings.builder;
import static org.elasticsearch.index.shard.IndexShardState.*;
import static org.elasticsearch.test.ElasticsearchIntegrationTest.ClusterScope;
import static org.elasticsearch.test.ElasticsearchIntegrationTest.Scope;
import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.assertAcked;

@ClusterScope(scope = Scope.TEST, numNodes = 0)
public class IndicesLifecycleListenerTests extends ElasticsearchIntegrationTest {

    @Test
    public void testIndexStateShardChanged() throws Throwable {

        //start with a single node
        String node1 = cluster().startNode();
        IndexShardStateChangeListener stateChangeListenerNode1 = new IndexShardStateChangeListener();
        //add a listener that keeps track of the shard state changes
        cluster().getInstance(IndicesLifecycle.class, node1).addListener(stateChangeListenerNode1);

        //create an index
        assertAcked(client().admin().indices().prepareCreate("test")
                .setSettings(SETTING_NUMBER_OF_SHARDS, 6, SETTING_NUMBER_OF_REPLICAS, 0));
        ensureGreen();

        //new shards got started
        assertShardStatesMatch(stateChangeListenerNode1, 6, CREATED, RECOVERING, POST_RECOVERY, STARTED);


        //add a node: 3 out of the 6 shards will be relocated to it
        //disable allocation before starting a new node, as we need to register the listener first
        assertAcked(client().admin().cluster().prepareUpdateSettings()
                .setPersistentSettings(builder().put(CLUSTER_ROUTING_ALLOCATION_DISABLE_ALLOCATION, true)));
        String node2 = cluster().startNode();
        IndexShardStateChangeListener stateChangeListenerNode2 = new IndexShardStateChangeListener();
        //add a listener that keeps track of the shard state changes
        cluster().getInstance(IndicesLifecycle.class, node2).addListener(stateChangeListenerNode2);
        //re-enable allocation
        assertAcked(client().admin().cluster().prepareUpdateSettings()
                .setPersistentSettings(builder().put(CLUSTER_ROUTING_ALLOCATION_DISABLE_ALLOCATION, false)));
        ensureGreen();

        //the 3 relocated shards get closed on the first node
        assertShardStatesMatch(stateChangeListenerNode1, 3, CLOSED);
        //the 3 relocated shards get created on the second node
        assertShardStatesMatch(stateChangeListenerNode2, 3, CREATED, RECOVERING, POST_RECOVERY, STARTED);


        //increase replicas from 0 to 1
        assertAcked(client().admin().indices().prepareUpdateSettings("test").setSettings(builder().put(SETTING_NUMBER_OF_REPLICAS, 1)));
        ensureGreen();

        //3 replicas are allocated to the first node
        assertShardStatesMatch(stateChangeListenerNode1, 3, CREATED, RECOVERING, POST_RECOVERY, STARTED);

        //3 replicas are allocated to the second node
        assertShardStatesMatch(stateChangeListenerNode2, 3, CREATED, RECOVERING, POST_RECOVERY, STARTED);


        //close the index
        assertAcked(client().admin().indices().prepareClose("test"));

        assertShardStatesMatch(stateChangeListenerNode1, 6, CLOSED);
        assertShardStatesMatch(stateChangeListenerNode2, 6, CLOSED);
    }

    private static void assertShardStatesMatch(final IndexShardStateChangeListener stateChangeListener, final int numShards, final IndexShardState... shardStates)
            throws InterruptedException {

        Predicate<Object> waitPredicate = new Predicate<Object>() {
            @Override
            public boolean apply(Object input) {
                if (stateChangeListener.shardStates.size() != numShards) {
                    return false;
                }
                for (List<IndexShardState> indexShardStates : stateChangeListener.shardStates.values()) {
                    if (indexShardStates == null || indexShardStates.size() != shardStates.length) {
                        return false;
                    }
                    for (int i = 0; i < shardStates.length; i++) {
                        if (indexShardStates.get(i) != shardStates[i]) {
                            return false;
                        }
                    }
                }
                return true;
            }
        };
        if (!awaitBusy(waitPredicate, 1, TimeUnit.MINUTES)) {
            fail("failed to observe expect shard states\n" +
                    "expected: [" + numShards + "] shards with states: " + Strings.arrayToCommaDelimitedString(shardStates) + "\n" +
                    "observed:\n" + stateChangeListener);
        }

        stateChangeListener.shardStates.clear();
    }

    private static class IndexShardStateChangeListener extends IndicesLifecycle.Listener {
        //we keep track of all the states (ordered) a shard goes through
        final ConcurrentMap<ShardId, List<IndexShardState>> shardStates = Maps.newConcurrentMap();

        @Override
        public void indexShardStateChanged(IndexShard indexShard, @Nullable IndexShardState previousState, IndexShardState newState, @Nullable String reason) {
            List<IndexShardState> shardStates = this.shardStates.putIfAbsent(indexShard.shardId(),
                    new CopyOnWriteArrayList<IndexShardState>(new IndexShardState[]{newState}));
            if (shardStates != null) {
                shardStates.add(newState);
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<ShardId, List<IndexShardState>> entry : shardStates.entrySet()) {
                sb.append(entry.getKey()).append(" --> ").append(Strings.collectionToCommaDelimitedString(entry.getValue())).append("\n");
            }
            return sb.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7452.java