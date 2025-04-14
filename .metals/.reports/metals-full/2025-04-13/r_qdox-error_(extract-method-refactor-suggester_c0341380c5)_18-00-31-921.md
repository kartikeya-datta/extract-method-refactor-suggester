error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7679.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7679.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7679.java
text:
```scala
c@@lear("update_settings");

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.index.cache.field.data.resident;

import com.google.common.base.Objects;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.cache.CacheBuilderHelper;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.metrics.CounterMetric;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.cache.field.data.support.AbstractConcurrentMapFieldDataCache;
import org.elasticsearch.index.field.data.FieldData;
import org.elasticsearch.index.settings.IndexSettings;
import org.elasticsearch.index.settings.IndexSettingsService;

import java.util.concurrent.TimeUnit;

/**
 *
 */
public class ResidentFieldDataCache extends AbstractConcurrentMapFieldDataCache implements RemovalListener<String, FieldData> {

    private final IndexSettingsService indexSettingsService;

    private volatile int maxSize;
    private volatile TimeValue expire;

    private final CounterMetric evictions = new CounterMetric();

    private final ApplySettings applySettings = new ApplySettings();

    @Inject
    public ResidentFieldDataCache(Index index, @IndexSettings Settings indexSettings, IndexSettingsService indexSettingsService) {
        super(index, indexSettings);
        this.indexSettingsService = indexSettingsService;

        this.maxSize = indexSettings.getAsInt("index.cache.field.max_size", componentSettings.getAsInt("max_size", -1));
        this.expire = indexSettings.getAsTime("index.cache.field.expire", componentSettings.getAsTime("expire", null));
        logger.debug("using [resident] field cache with max_size [{}], expire [{}]", maxSize, expire);

        indexSettingsService.addListener(applySettings);
    }

    @Override
    public void close() throws ElasticSearchException {
        indexSettingsService.removeListener(applySettings);
        super.close();
    }

    @Override
    protected Cache<String, FieldData> buildFieldDataMap() {
        CacheBuilder<String, FieldData> cacheBuilder = CacheBuilder.newBuilder().removalListener(this);
        if (maxSize != -1) {
            cacheBuilder.maximumSize(maxSize);
        }
        if (expire != null) {
            cacheBuilder.expireAfterAccess(expire.nanos(), TimeUnit.NANOSECONDS);
        }
        CacheBuilderHelper.disableStats(cacheBuilder);
        return cacheBuilder.build();
    }

    @Override
    public String type() {
        return "resident";
    }

    @Override
    public long evictions() {
        return evictions.count();
    }

    @Override
    public void onRemoval(RemovalNotification<String, FieldData> removalNotification) {
        if (removalNotification.wasEvicted()) {
            evictions.inc();
        }
    }

    static {
        IndexMetaData.addDynamicSettings(
                "index.cache.field.max_size",
                "index.cache.field.expire"
        );
    }

    class ApplySettings implements IndexSettingsService.Listener {
        @Override
        public void onRefreshSettings(Settings settings) {
            int maxSize = settings.getAsInt("index.cache.field.max_size", ResidentFieldDataCache.this.maxSize);
            TimeValue expire = settings.getAsTime("index.cache.field.expire", ResidentFieldDataCache.this.expire);
            boolean changed = false;
            if (maxSize != ResidentFieldDataCache.this.maxSize) {
                logger.info("updating index.cache.field.max_size from [{}] to [{}]", ResidentFieldDataCache.this.maxSize, maxSize);
                changed = true;
                ResidentFieldDataCache.this.maxSize = maxSize;
            }
            if (!Objects.equal(expire, ResidentFieldDataCache.this.expire)) {
                logger.info("updating index.cache.field.expire from [{}] to [{}]", ResidentFieldDataCache.this.expire, expire);
                changed = true;
                ResidentFieldDataCache.this.expire = expire;
            }
            if (changed) {
                clear();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7679.java