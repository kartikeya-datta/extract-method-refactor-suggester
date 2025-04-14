error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1440.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1440.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1440.java
text:
```scala
t@@his.readerCleanerSchedule = componentSettings.getAsTime("reader_cleaner_schedule", TimeValue.timeValueMinutes(1));

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
import org.apache.lucene.search.Filter;
import org.elasticsearch.index.AbstractIndexComponent;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.cache.filter.FilterCache;
import org.elasticsearch.index.settings.IndexSettings;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.util.TimeValue;
import org.elasticsearch.util.settings.Settings;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;

import static org.elasticsearch.util.concurrent.ConcurrentMaps.*;
import static org.elasticsearch.util.lucene.docidset.DocIdSets.*;

/**
 * @author kimchy (Shay Banon)
 */
public abstract class AbstractConcurrentMapFilterCache extends AbstractIndexComponent implements FilterCache {

    private final ConcurrentMap<IndexReader, ConcurrentMap<Filter, DocIdSet>> cache;

    private final TimeValue readerCleanerSchedule;

    private final Future scheduleFuture;

    protected AbstractConcurrentMapFilterCache(Index index, @IndexSettings Settings indexSettings, ThreadPool threadPool) {
        super(index, indexSettings);

        this.readerCleanerSchedule = componentSettings.getAsTime("readerCleanerSchedule", TimeValue.timeValueMinutes(1));

        logger.debug("Using [" + type() + "] filter cache with readerCleanerSchedule [{}]", readerCleanerSchedule);

        this.cache = newConcurrentMap();
        this.scheduleFuture = threadPool.scheduleWithFixedDelay(new IndexReaderCleaner(), readerCleanerSchedule);
    }

    @Override public void close() {
        scheduleFuture.cancel(false);
        cache.clear();
    }

    @Override public void clear() {
        cache.clear();
    }

    @Override public Filter cache(Filter filterToCache) {
        return new FilterCacheFilterWrapper(filterToCache);
    }

    private class IndexReaderCleaner implements Runnable {
        @Override public void run() {
            for (Iterator<IndexReader> readerIt = cache.keySet().iterator(); readerIt.hasNext();) {
                IndexReader reader = readerIt.next();
                if (reader.getRefCount() <= 0) {
                    readerIt.remove();
                }
            }
        }
    }

    protected abstract ConcurrentMap<Filter, DocIdSet> buildMap();

    private class FilterCacheFilterWrapper extends Filter {

        private final Filter filter;

        private FilterCacheFilterWrapper(Filter filter) {
            this.filter = filter;
        }

        @Override public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
            ConcurrentMap<Filter, DocIdSet> cachedFilters = cache.get(reader);
            if (cachedFilters == null) {
                cachedFilters = buildMap();
                cache.putIfAbsent(reader, cachedFilters);
            }
            DocIdSet docIdSet = cachedFilters.get(filter);
            if (docIdSet != null) {
                return docIdSet;
            }
            docIdSet = filter.getDocIdSet(reader);
            docIdSet = cacheable(reader, docIdSet);
            cachedFilters.putIfAbsent(filter, docIdSet);
            return docIdSet;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1440.java