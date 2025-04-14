error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1266.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1266.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1266.java
text:
```scala
r@@eturn new CacheStats(fieldDataCache.evictions(), filterCache.evictions(), filterCache.memEvictions(), fieldDataCache.sizeInBytes(), filterCache.sizeInBytes(), filterCache.count(), bloomCache.sizeInBytes());

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

package org.elasticsearch.index.cache;

import org.apache.lucene.index.IndexReader;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.cluster.ClusterChangedEvent;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterStateListener;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.component.CloseableComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.AbstractIndexComponent;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.cache.bloom.BloomCache;
import org.elasticsearch.index.cache.field.data.FieldDataCache;
import org.elasticsearch.index.cache.filter.FilterCache;
import org.elasticsearch.index.cache.id.IdCache;
import org.elasticsearch.index.cache.query.parser.QueryParserCache;
import org.elasticsearch.index.settings.IndexSettings;

/**
 * @author kimchy (shay.banon)
 */
public class IndexCache extends AbstractIndexComponent implements CloseableComponent, ClusterStateListener {

    private final FilterCache filterCache;

    private final FieldDataCache fieldDataCache;

    private final QueryParserCache queryParserCache;

    private final IdCache idCache;

    private final BloomCache bloomCache;

    private ClusterService clusterService;

    @Inject public IndexCache(Index index, @IndexSettings Settings indexSettings, FilterCache filterCache, FieldDataCache fieldDataCache,
                              QueryParserCache queryParserCache, IdCache idCache, BloomCache bloomCache) {
        super(index, indexSettings);
        this.filterCache = filterCache;
        this.fieldDataCache = fieldDataCache;
        this.queryParserCache = queryParserCache;
        this.idCache = idCache;
        this.bloomCache = bloomCache;
    }

    @Inject(optional = true)
    public void setClusterService(@Nullable ClusterService clusterService) {
        this.clusterService = clusterService;
        if (clusterService != null) {
            clusterService.add(this);
        }
    }

    public CacheStats stats() {
        return new CacheStats(fieldDataCache.evictions(), filterCache.evictions(), fieldDataCache.sizeInBytes(), filterCache.sizeInBytes(), filterCache.count(), bloomCache.sizeInBytes());
    }

    public FilterCache filter() {
        return filterCache;
    }

    public FieldDataCache fieldData() {
        return fieldDataCache;
    }

    public IdCache idCache() {
        return this.idCache;
    }

    public BloomCache bloomCache() {
        return this.bloomCache;
    }

    public QueryParserCache queryParserCache() {
        return this.queryParserCache;
    }

    @Override public void close() throws ElasticSearchException {
        filterCache.close();
        fieldDataCache.close();
        idCache.close();
        queryParserCache.close();
        bloomCache.close();
        if (clusterService != null) {
            clusterService.remove(this);
        }
    }

    public void clear(IndexReader reader) {
        filterCache.clear(reader);
        fieldDataCache.clear(reader);
        idCache.clear(reader);
        bloomCache.clear(reader);
    }

    public void clear() {
        filterCache.clear();
        fieldDataCache.clear();
        idCache.clear();
        queryParserCache.clear();
        bloomCache.clear();
    }

    @Override public void clusterChanged(ClusterChangedEvent event) {
        // clear the query parser cache if the metadata (mappings) changed...
        if (event.metaDataChanged()) {
            queryParserCache.clear();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1266.java