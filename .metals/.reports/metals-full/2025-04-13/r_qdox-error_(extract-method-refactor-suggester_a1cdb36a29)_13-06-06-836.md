error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6261.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6261.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6261.java
text:
```scala
r@@eturn new EntriesStats(sizeInBytes, segmentsCount == 0 ? 0 : totalCount / segmentsCount);

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

package org.elasticsearch.index.cache.filter.support;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.Filter;
import org.apache.lucene.util.OpenBitSet;
import org.elasticsearch.common.RamUsage;
import org.elasticsearch.common.collect.MapMaker;
import org.elasticsearch.common.lab.LongsLAB;
import org.elasticsearch.common.lucene.docset.DocSet;
import org.elasticsearch.common.lucene.docset.DocSets;
import org.elasticsearch.common.lucene.docset.OpenBitDocSet;
import org.elasticsearch.common.lucene.docset.SlicedOpenBitSet;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.index.AbstractIndexComponent;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.cache.filter.FilterCache;
import org.elasticsearch.index.settings.IndexSettings;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

import static org.elasticsearch.common.util.concurrent.ConcurrentCollections.*;

/**
 * A base concurrent filter cache that accepts the actual cache to use.
 *
 * @author kimchy (shay.banon)
 */
public abstract class AbstractConcurrentMapFilterCache extends AbstractIndexComponent implements FilterCache {

    final ConcurrentMap<Object, ReaderValue> cache;

    final boolean labEnabled;
    final ByteSizeValue labMaxAlloc;
    final ByteSizeValue labChunkSize;

    final int labMaxAllocBytes;
    final int labChunkSizeBytes;

    protected AbstractConcurrentMapFilterCache(Index index, @IndexSettings Settings indexSettings) {
        super(index, indexSettings);
        // weak keys is fine, it will only be cleared once IndexReader references will be removed
        // (assuming clear(...) will not be called)
        this.cache = buildCache();

        // The LAB is stored per reader, so whole chunks will be cleared once reader is discarded.
        // This means that with filter entry specific based eviction, like access time
        // we might get into cases where the LAB is held by a puny filter and other filters have been released.
        // This usually will not be that bad, compared to the GC benefit of using a LAB, but, that is why
        // the soft filter cache is recommended.
        this.labEnabled = componentSettings.getAsBoolean("lab", false);
        // These values should not be too high, basically we want to cached the small readers and use the LAB for
        // them, 1M docs on OpenBitSet is around 110kb.
        this.labMaxAlloc = componentSettings.getAsBytesSize("lab.max_alloc", new ByteSizeValue(128, ByteSizeUnit.KB));
        this.labChunkSize = componentSettings.getAsBytesSize("lab.chunk_size", new ByteSizeValue(1, ByteSizeUnit.MB));

        this.labMaxAllocBytes = (int) (labMaxAlloc.bytes() / RamUsage.NUM_BYTES_LONG);
        this.labChunkSizeBytes = (int) (labChunkSize.bytes() / RamUsage.NUM_BYTES_LONG);
    }

    protected ConcurrentMap<Object, ReaderValue> buildCache() {
        return new MapMaker().weakKeys().makeMap();
    }

    protected ConcurrentMap<Filter, DocSet> buildFilterMap() {
        return newConcurrentMap();
    }

    @Override public void close() {
        cache.clear();
    }

    @Override public void clear() {
        cache.clear();
    }

    @Override public void clear(IndexReader reader) {
        ReaderValue readerValue = cache.remove(reader.getCoreCacheKey());
        // help soft/weak handling GC
        if (readerValue != null) {
            readerValue.filters().clear();
        }
    }

    @Override public EntriesStats entriesStats() {
        long sizeInBytes = 0;
        long totalCount = 0;
        int segmentsCount = 0;
        for (ReaderValue readerValue : cache.values()) {
            segmentsCount++;
            for (DocSet docSet : readerValue.filters().values()) {
                sizeInBytes += docSet.sizeInBytes();
                totalCount++;
            }
        }
        return new EntriesStats(sizeInBytes, totalCount / segmentsCount);
    }

    @Override public Filter cache(Filter filterToCache) {
        if (isCached(filterToCache)) {
            return filterToCache;
        }
        return new FilterCacheFilterWrapper(filterToCache, this);
    }

    @Override public boolean isCached(Filter filter) {
        return filter instanceof FilterCacheFilterWrapper;
    }

    // LUCENE MONITOR: Check next version Lucene for CachingWrapperFilter, consider using that logic
    // and not use the DeletableConstantScoreQuery, instead pass the DeletesMode enum to the cache method
    // see: https://issues.apache.org/jira/browse/LUCENE-2468

    static class FilterCacheFilterWrapper extends Filter {

        private final Filter filter;

        private final AbstractConcurrentMapFilterCache cache;

        FilterCacheFilterWrapper(Filter filter, AbstractConcurrentMapFilterCache cache) {
            this.filter = filter;
            this.cache = cache;
        }

        @Override public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
            ReaderValue readerValue = cache.cache.get(reader.getCoreCacheKey());
            if (readerValue == null) {
                LongsLAB longsLAB = null;
                if (cache.labEnabled) {
                    longsLAB = new LongsLAB(cache.labChunkSizeBytes, cache.labMaxAllocBytes);
                }
                readerValue = new ReaderValue(cache.buildFilterMap(), longsLAB);
                ReaderValue prev = cache.cache.putIfAbsent(reader.getCoreCacheKey(), readerValue);
                if (prev != null) {
                    readerValue = prev;
                }
            }
            DocSet docSet = readerValue.filters().get(filter);
            if (docSet != null) {
                return docSet;
            }
            DocIdSet docIdSet = filter.getDocIdSet(reader);
            docSet = cacheable(reader, readerValue, docIdSet);
            DocSet prev = readerValue.filters().putIfAbsent(filter, docSet);
            if (prev != null) {
                docSet = prev;
            }
            return docSet;
        }

        public String toString() {
            return "FilterCacheFilterWrapper(" + filter + ")";
        }

        public boolean equals(Object o) {
            if (!(o instanceof FilterCacheFilterWrapper)) return false;
            return this.filter.equals(((FilterCacheFilterWrapper) o).filter);
        }

        public int hashCode() {
            return filter.hashCode() ^ 0x1117BF25;
        }

        private DocSet cacheable(IndexReader reader, ReaderValue readerValue, DocIdSet set) throws IOException {
            if (set == null) {
                return DocSet.EMPTY_DOC_SET;
            }
            if (set == DocIdSet.EMPTY_DOCIDSET) {
                return DocSet.EMPTY_DOC_SET;
            }

            DocIdSetIterator it = set.iterator();
            if (it == null) {
                return DocSet.EMPTY_DOC_SET;
            }
            int doc = it.nextDoc();
            if (doc == DocIdSetIterator.NO_MORE_DOCS) {
                return DocSet.EMPTY_DOC_SET;
            }

            // we have a LAB, check if can be used...
            if (readerValue.longsLAB() == null) {
                return DocSets.cacheable(reader, set);
            }

            int numOfWords = OpenBitSet.bits2words(reader.maxDoc());
            LongsLAB.Allocation allocation = readerValue.longsLAB().allocateLongs(numOfWords);
            if (allocation == null) {
                return DocSets.cacheable(reader, set);
            }
            // we have an allocation, use it to create SlicedOpenBitSet
            if (set instanceof OpenBitSet) {
                return new SlicedOpenBitSet(allocation.getData(), allocation.getOffset(), (OpenBitSet) set);
            } else if (set instanceof OpenBitDocSet) {
                return new SlicedOpenBitSet(allocation.getData(), allocation.getOffset(), ((OpenBitDocSet) set).set());
            } else {
                SlicedOpenBitSet slicedSet = new SlicedOpenBitSet(allocation.getData(), numOfWords, allocation.getOffset());
                slicedSet.fastSet(doc); // we already have an open iterator, so use it, and don't forget to set the initial one
                while ((doc = it.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
                    slicedSet.fastSet(doc);
                }
                return slicedSet;
            }
        }
    }

    public static class ReaderValue {
        private final ConcurrentMap<Filter, DocSet> filters;
        private final LongsLAB longsLAB;

        public ReaderValue(ConcurrentMap<Filter, DocSet> filters, LongsLAB longsLAB) {
            this.filters = filters;
            this.longsLAB = longsLAB;
        }

        public ConcurrentMap<Filter, DocSet> filters() {
            return filters;
        }

        public LongsLAB longsLAB() {
            return longsLAB;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6261.java