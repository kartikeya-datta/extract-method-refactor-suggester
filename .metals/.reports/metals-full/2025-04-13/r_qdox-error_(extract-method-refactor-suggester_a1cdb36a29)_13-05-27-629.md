error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3029.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3029.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3029.java
text:
```scala
final L@@ist<Listener> listeners = new ArrayList<>();

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

package org.elasticsearch.index.fielddata;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.SegmentReader;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.lucene.SegmentReaderUtils;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.index.service.IndexService;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.index.shard.ShardUtils;
import org.elasticsearch.index.shard.service.IndexShard;
import org.elasticsearch.indices.fielddata.cache.IndicesFieldDataCacheListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * A simple field data cache abstraction on the *index* level.
 */
public interface IndexFieldDataCache {

    <FD extends AtomicFieldData, IFD extends IndexFieldData<FD>> FD load(AtomicReaderContext context, IFD indexFieldData) throws Exception;

    /**
     * Clears all the field data stored cached in on this index.
     */
    void clear();

    /**
     * Clears all the field data stored cached in on this index for the specified field name.
     */
    void clear(String fieldName);

    void clear(Object coreCacheKey);

    interface Listener {

        void onLoad(FieldMapper.Names fieldNames, FieldDataType fieldDataType, AtomicFieldData fieldData);

        void onUnload(FieldMapper.Names fieldNames, FieldDataType fieldDataType, boolean wasEvicted, long sizeInBytes, @Nullable AtomicFieldData fieldData);
    }

    /**
     * The resident field data cache is a *per field* cache that keeps all the values in memory.
     */
    static abstract class FieldBased implements IndexFieldDataCache, SegmentReader.CoreClosedListener, RemovalListener<FieldBased.Key, AtomicFieldData> {
        @Nullable
        private final IndexService indexService;
        private final FieldMapper.Names fieldNames;
        private final FieldDataType fieldDataType;
        private final Cache<Key, AtomicFieldData> cache;
        private final IndicesFieldDataCacheListener indicesFieldDataCacheListener;

        protected FieldBased(@Nullable IndexService indexService, FieldMapper.Names fieldNames, FieldDataType fieldDataType, CacheBuilder cache, IndicesFieldDataCacheListener indicesFieldDataCacheListener) {
            this.indexService = indexService;
            this.fieldNames = fieldNames;
            this.fieldDataType = fieldDataType;
            this.indicesFieldDataCacheListener = indicesFieldDataCacheListener;
            cache.removalListener(this);
            //noinspection unchecked
            this.cache = cache.build();
        }

        @Override
        public void onRemoval(RemovalNotification<Key, AtomicFieldData> notification) {
            Key key = notification.getKey();
            assert key != null && key.listeners != null;

            AtomicFieldData value = notification.getValue();
            long sizeInBytes = key.sizeInBytes;
            if (sizeInBytes == -1 && value != null) {
                sizeInBytes = value.getMemorySizeInBytes();
            }
            for (Listener listener : key.listeners) {
                listener.onUnload(fieldNames, fieldDataType, notification.wasEvicted(), sizeInBytes, value);
            }
        }

        @Override
        public <FD extends AtomicFieldData, IFD extends IndexFieldData<FD>> FD load(final AtomicReaderContext context, final IFD indexFieldData) throws Exception {
            final Key key = new Key(context.reader().getCoreCacheKey());
            //noinspection unchecked
            return (FD) cache.get(key, new Callable<AtomicFieldData>() {
                @Override
                public AtomicFieldData call() throws Exception {
                    SegmentReaderUtils.registerCoreListener(context.reader(), FieldBased.this);
                    AtomicFieldData fieldData = indexFieldData.loadDirect(context);
                    key.sizeInBytes = fieldData.getMemorySizeInBytes();
                    key.listeners.add(indicesFieldDataCacheListener);

                    if (indexService != null) {
                        ShardId shardId = ShardUtils.extractShardId(context.reader());
                        if (shardId != null) {
                            IndexShard shard = indexService.shard(shardId.id());
                            if (shard != null) {
                                key.listeners.add(shard.fieldData());
                            }
                        }
                    }
                    for (Listener listener : key.listeners) {
                        listener.onLoad(fieldNames, fieldDataType, fieldData);
                    }
                    return fieldData;
                }
            });
        }

        @Override
        public void clear() {
            cache.invalidateAll();
        }

        @Override
        public void clear(String fieldName) {
            cache.invalidateAll();
        }

        @Override
        public void clear(Object coreCacheKey) {
            cache.invalidate(new Key(coreCacheKey));
        }

        @Override
        public void onClose(Object coreCacheKey) {
            cache.invalidate(new Key(coreCacheKey));
        }

        static class Key {
            final Object readerKey;
            final List<Listener> listeners = new ArrayList<>(); // optional stats listener
            long sizeInBytes = -1; // optional size in bytes (we keep it here in case the values are soft references)

            Key(Object readerKey) {
                this.readerKey = readerKey;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                Key key = (Key) o;
                if (!readerKey.equals(key.readerKey)) return false;
                return true;
            }

            @Override
            public int hashCode() {
                return readerKey.hashCode();
            }
        }
    }

    static class Resident extends FieldBased {

        public Resident(@Nullable IndexService indexService, FieldMapper.Names fieldNames, FieldDataType fieldDataType, IndicesFieldDataCacheListener indicesFieldDataCacheListener) {
            super(indexService, fieldNames, fieldDataType, CacheBuilder.newBuilder(), indicesFieldDataCacheListener);
        }
    }

    static class Soft extends FieldBased {

        public Soft(@Nullable IndexService indexService, FieldMapper.Names fieldNames, FieldDataType fieldDataType, IndicesFieldDataCacheListener indicesFieldDataCacheListener) {
            super(indexService, fieldNames, fieldDataType, CacheBuilder.newBuilder().softValues(), indicesFieldDataCacheListener);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3029.java