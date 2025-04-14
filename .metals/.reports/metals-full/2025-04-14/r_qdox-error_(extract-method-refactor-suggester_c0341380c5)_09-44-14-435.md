error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6815.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6815.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6815.java
text:
```scala
r@@eturn translog.numberOfOperations();

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

package org.elasticsearch.index.shard;

import org.elasticsearch.common.component.CloseableComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.settings.IndexSettings;
import org.elasticsearch.index.shard.service.IndexShard;
import org.elasticsearch.index.store.Store;
import org.elasticsearch.index.translog.Translog;
import org.elasticsearch.jmx.JmxService;
import org.elasticsearch.jmx.MBean;
import org.elasticsearch.jmx.ManagedAttribute;

import java.io.IOException;

import static org.elasticsearch.index.IndexServiceManagement.*;

/**
 * @author kimchy (Shay Banon)
 */
@MBean(objectName = "", description = "")
public class IndexShardManagement extends AbstractIndexShardComponent implements CloseableComponent {

    public static String buildShardGroupName(ShardId shardId) {
        return buildIndexGroupName(shardId.index()) + ",subService=shards,shard=" + shardId.id();
    }

    private final JmxService jmxService;

    private final IndexShard indexShard;

    private final Store store;

    private final Translog translog;

    @Inject public IndexShardManagement(ShardId shardId, @IndexSettings Settings indexSettings, JmxService jmxService, IndexShard indexShard,
                                        Store store, Translog translog) {
        super(shardId, indexSettings);
        this.jmxService = jmxService;
        this.indexShard = indexShard;
        this.store = store;
        this.translog = translog;
    }

    public void close() {
        jmxService.unregisterGroup(buildShardGroupName(indexShard.shardId()));
    }

    @ManagedAttribute(description = "Index Name")
    public String getIndex() {
        return indexShard.shardId().index().name();
    }

    @ManagedAttribute(description = "Shard Id")
    public int getShardId() {
        return indexShard.shardId().id();
    }

    @ManagedAttribute(description = "Storage Size")
    public String getStoreSize() {
        try {
            return store.estimateSize().toString();
        } catch (IOException e) {
            return "NA";
        }
    }

    @ManagedAttribute(description = "The current transaction log id")
    public long getTranslogId() {
        return translog.currentId();
    }

    @ManagedAttribute(description = "Number of transaction log operations")
    public long getTranslogNumberOfOperations() {
        return translog.size();
    }

    @ManagedAttribute(description = "Estimated size in memory the transaction log takes")
    public String getTranslogSize() {
        return new ByteSizeValue(translog.memorySizeInBytes()).toString();
    }

    @ManagedAttribute(description = "The state of the shard")
    public String getState() {
        return indexShard.state().toString();
    }

    @ManagedAttribute(description = "Primary")
    public boolean isPrimary() {
        return indexShard.routingEntry().primary();
    }

    @ManagedAttribute(description = "The state of the shard as perceived by the cluster")
    public String getRoutingState() {
        return indexShard.routingEntry().state().toString();
    }

    @ManagedAttribute(description = "The number of documents in the index")
    public int getNumDocs() {
        Engine.Searcher searcher = indexShard.searcher();
        try {
            return searcher.reader().numDocs();
        } finally {
            searcher.release();
        }
    }

    @ManagedAttribute(description = "The total number of documents in the index (including deleted ones)")
    public int getMaxDoc() {
        Engine.Searcher searcher = indexShard.searcher();
        try {
            return searcher.reader().maxDoc();
        } finally {
            searcher.release();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6815.java