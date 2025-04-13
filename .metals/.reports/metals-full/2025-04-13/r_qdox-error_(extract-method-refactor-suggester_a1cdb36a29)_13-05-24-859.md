error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7882.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7882.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7882.java
text:
```scala
public v@@oid afterIndexShardClosed(ShardId shardId, @Nullable IndexShard indexShard) {

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

import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.service.IndexService;
import org.elasticsearch.index.shard.IndexShardState;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.index.shard.service.IndexShard;

/**
 * A global component allowing to register for lifecycle of an index (create/closed) and
 * an index shard (created/closed).
 */
public interface IndicesLifecycle {

    /**
     * Add a listener.
     */
    void addListener(Listener listener);

    /**
     * Remove a listener.
     */
    void removeListener(Listener listener);

    /**
     * A listener for index and index shard lifecycle events (create/closed).
     */
    public abstract static class Listener {

        /**
         * Called when the shard routing has changed state.
         *
         * @param indexShard The index shard
         * @param oldRouting The old routing state (can be null)
         * @param newRouting The new routing state
         */
        public void shardRoutingChanged(IndexShard indexShard, @Nullable ShardRouting oldRouting, ShardRouting newRouting) {

        }

        /**
         * Called before the index gets created.
         */
        public void beforeIndexCreated(Index index) {

        }

        /**
         * Called after the index has been created.
         */
        public void afterIndexCreated(IndexService indexService) {

        }

        /**
         * Called before the index shard gets created.
         */
        public void beforeIndexShardCreated(ShardId shardId) {

        }

        /**
         * Called after the index shard has been created.
         */
        public void afterIndexShardCreated(IndexShard indexShard) {

        }

        public void afterIndexShardPostRecovery(IndexShard indexShard) {

        }

        /**
         * Called after the index shard has been started.
         */
        public void afterIndexShardStarted(IndexShard indexShard) {

        }

        /**
         * Called before the index get closed.
         *
         * @param indexService The index service
         */
        public void beforeIndexClosed(IndexService indexService) {

        }

        /**
         * Called after the index has been closed.
         *
         * @param index The index
         */
        public void afterIndexClosed(Index index) {

        }

        /**
         * Called before the index shard gets closed.
         *
         * @param indexShard The index shard
         */
        public void beforeIndexShardClosed(ShardId shardId, @Nullable IndexShard indexShard) {

        }

        /**
         * Called after the index shard has been closed.
         *
         * @param shardId The shard id
         */
        public void afterIndexShardClosed(ShardId shardId) {

        }

        /**
         * Called after a shard's {@link org.elasticsearch.index.shard.IndexShardState} changes.
         * The order of concurrent events is preserved. The execution must be lightweight.
         *
         * @param indexShard the shard the new state was applied to
         * @param previousState the previous index shard state if there was one, null otherwise
         * @param currentState the new shard state
         * @param reason the reason for the state change if there is one, null otherwise
         */
        public void indexShardStateChanged(IndexShard indexShard, @Nullable IndexShardState previousState, IndexShardState currentState, @Nullable String reason) {

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7882.java