error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8088.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8088.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8088.java
text:
```scala
public v@@oid close() throws ElasticSearchException {

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

package org.elasticsearch.index.store.support;

import org.apache.lucene.store.StoreRateLimiting;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.index.AbstractIndexComponent;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.service.IndexService;
import org.elasticsearch.index.settings.IndexSettings;
import org.elasticsearch.index.settings.IndexSettingsService;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.index.store.IndexStore;
import org.elasticsearch.indices.store.IndicesStore;

import java.io.IOException;

/**
 *
 */
public abstract class AbstractIndexStore extends AbstractIndexComponent implements IndexStore {

    public static final String INDEX_STORE_THROTTLE_TYPE = "index.store.throttle.type";
    public static final String INDEX_STORE_THROTTLE_MAX_BYTES_PER_SEC = "index.store.throttle.max_bytes_per_sec";

    class ApplySettings implements IndexSettingsService.Listener {
        @Override
        public void onRefreshSettings(Settings settings) {
            String rateLimitingType = indexSettings.get(INDEX_STORE_THROTTLE_TYPE, AbstractIndexStore.this.rateLimitingType);
            if (!rateLimitingType.equals(AbstractIndexStore.this.rateLimitingType)) {
                logger.info("updating index.store.throttle.type from [{}] to [{}]", AbstractIndexStore.this.rateLimitingType, rateLimitingType);
                if (rateLimitingType.equalsIgnoreCase("node")) {
                    AbstractIndexStore.this.rateLimitingType = rateLimitingType;
                    AbstractIndexStore.this.nodeRateLimiting = true;
                } else {
                    StoreRateLimiting.Type.fromString(rateLimitingType);
                    AbstractIndexStore.this.rateLimitingType = rateLimitingType;
                    AbstractIndexStore.this.nodeRateLimiting = false;
                    AbstractIndexStore.this.rateLimiting.setType(rateLimitingType);
                }
            }

            ByteSizeValue rateLimitingThrottle = settings.getAsBytesSize(INDEX_STORE_THROTTLE_MAX_BYTES_PER_SEC, AbstractIndexStore.this.rateLimitingThrottle);
            if (!rateLimitingThrottle.equals(AbstractIndexStore.this.rateLimitingThrottle)) {
                logger.info("updating index.store.throttle.max_bytes_per_sec from [{}] to [{}], note, type is [{}]", AbstractIndexStore.this.rateLimitingThrottle, rateLimitingThrottle, AbstractIndexStore.this.rateLimitingType);
                AbstractIndexStore.this.rateLimitingThrottle = rateLimitingThrottle;
                AbstractIndexStore.this.rateLimiting.setMaxRate(rateLimitingThrottle);
            }
        }
    }


    protected final IndexService indexService;

    protected final IndicesStore indicesStore;

    private volatile String rateLimitingType;
    private volatile ByteSizeValue rateLimitingThrottle;
    private volatile boolean nodeRateLimiting;

    private final StoreRateLimiting rateLimiting = new StoreRateLimiting();

    private final ApplySettings applySettings = new ApplySettings();

    protected AbstractIndexStore(Index index, @IndexSettings Settings indexSettings, IndexService indexService, IndicesStore indicesStore) {
        super(index, indexSettings);
        this.indexService = indexService;
        this.indicesStore = indicesStore;

        this.rateLimitingType = indexSettings.get(INDEX_STORE_THROTTLE_TYPE, "node");
        if (rateLimitingType.equalsIgnoreCase("node")) {
            nodeRateLimiting = true;
        } else {
            nodeRateLimiting = false;
            rateLimiting.setType(rateLimitingType);
        }
        this.rateLimitingThrottle = indexSettings.getAsBytesSize(INDEX_STORE_THROTTLE_MAX_BYTES_PER_SEC, new ByteSizeValue(0));
        rateLimiting.setMaxRate(rateLimitingThrottle);

        logger.debug("using index.store.throttle.type [{}], with index.store.throttle.max_bytes_per_sec [{}]", rateLimitingType, rateLimitingThrottle);

        indexService.settingsService().addListener(applySettings);
    }

    @Override
    public void close(boolean delete) throws ElasticSearchException {
        indexService.settingsService().removeListener(applySettings);
    }

    @Override
    public boolean canDeleteUnallocated(ShardId shardId) {
        return false;
    }

    @Override
    public void deleteUnallocated(ShardId shardId) throws IOException {
        // do nothing here...
    }

    @Override
    public IndicesStore indicesStore() {
        return indicesStore;
    }

    @Override
    public StoreRateLimiting rateLimiting() {
        return nodeRateLimiting ? indicesStore.rateLimiting() : this.rateLimiting;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8088.java