error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9790.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9790.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9790.java
text:
```scala
public synchronized S@@et<Entry<String, IndexOutput>> cancelAndClearOpenIndexInputs() {

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

package org.elasticsearch.indices.recovery;

import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexOutput;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.util.concurrent.ConcurrentCollections;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.index.shard.service.InternalIndexShard;
import org.elasticsearch.index.store.Store;
import org.elasticsearch.index.store.StoreFileMetaData;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 *
 */
public class RecoveryStatus {

    final ShardId shardId;
    final long recoveryId;
    final InternalIndexShard indexShard;
    final RecoveryState recoveryState;
    final DiscoveryNode sourceNode;

    public RecoveryStatus(long recoveryId, InternalIndexShard indexShard, DiscoveryNode sourceNode) {
        this.recoveryId = recoveryId;
        this.indexShard = indexShard;
        this.sourceNode = sourceNode;
        this.shardId = indexShard.shardId();
        this.recoveryState = new RecoveryState(shardId);
        recoveryState.getTimer().startTime(System.currentTimeMillis());
    }

    volatile Thread recoveryThread;
    private volatile boolean canceled;
    volatile boolean sentCanceledToSource;

    private volatile ConcurrentMap<String, IndexOutput> openIndexOutputs = ConcurrentCollections.newConcurrentMap();
    public final Store.LegacyChecksums legacyChecksums = new Store.LegacyChecksums();

    public DiscoveryNode sourceNode() {
        return this.sourceNode;
    }

    public RecoveryState recoveryState() {
        return recoveryState;
    }

    public void stage(RecoveryState.Stage stage) {
        recoveryState.setStage(stage);
    }

    public RecoveryState.Stage stage() {
        return recoveryState.getStage();
    }

    public boolean isCanceled() {
        return canceled;
    }
    
    public synchronized void cancel() {
        canceled = true;
    }
    
    public IndexOutput getOpenIndexOutput(String key) {
        final ConcurrentMap<String, IndexOutput> outputs = openIndexOutputs;
        if (canceled || outputs == null) {
            return null;
        }
        return outputs.get(key);
    }
    
    public synchronized Set<Entry<String, IndexOutput>> cancleAndClearOpenIndexInputs() {
        cancel();
        final ConcurrentMap<String, IndexOutput> outputs = openIndexOutputs;
        openIndexOutputs = null;
        if (outputs == null) {
            return null;
        }
        Set<Entry<String, IndexOutput>> entrySet = outputs.entrySet();
        return entrySet;
    }
    

    public IndexOutput removeOpenIndexOutputs(String name) {
        final ConcurrentMap<String, IndexOutput> outputs = openIndexOutputs;
        if (outputs == null) {
            return null;
        }
        return outputs.remove(name);
    }

    public synchronized IndexOutput openAndPutIndexOutput(String key, String fileName, StoreFileMetaData metaData, Store store) throws IOException {
        if (isCanceled()) {
            return null;
        }
        final ConcurrentMap<String, IndexOutput> outputs = openIndexOutputs;
        IndexOutput indexOutput = store.createVerifyingOutput(fileName, IOContext.DEFAULT, metaData);
        outputs.put(key, indexOutput);
        return indexOutput;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9790.java