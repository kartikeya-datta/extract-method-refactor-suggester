error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4009.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4009.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4009.java
text:
```scala
t@@his.percolator.setIndicesService(indicesService);

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

package org.elasticsearch.index.percolator;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.elasticsearch.common.collect.Maps;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.lucene.search.TermFilter;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.AbstractIndexComponent;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.field.data.FieldData;
import org.elasticsearch.index.field.data.FieldDataType;
import org.elasticsearch.index.mapper.IdFieldMapper;
import org.elasticsearch.index.mapper.SourceFieldMapper;
import org.elasticsearch.index.mapper.SourceFieldSelector;
import org.elasticsearch.index.mapper.TypeFieldMapper;
import org.elasticsearch.index.service.IndexService;
import org.elasticsearch.index.settings.IndexSettings;
import org.elasticsearch.index.shard.IndexShardState;
import org.elasticsearch.index.shard.service.IndexShard;
import org.elasticsearch.index.shard.service.OperationListener;
import org.elasticsearch.indices.IndicesLifecycle;
import org.elasticsearch.indices.IndicesService;

import java.io.IOException;
import java.util.Map;

/**
 * @author kimchy (shay.banon)
 */
public class PercolatorService extends AbstractIndexComponent {

    public static final String INDEX_NAME = "_percolator";

    private final IndicesService indicesService;

    private final PercolatorExecutor percolator;

    private final ShardLifecycleListener shardLifecycleListener;

    private final Object mutex = new Object();

    private boolean initialQueriesFetchDone = false;

    @Inject public PercolatorService(Index index, @IndexSettings Settings indexSettings, IndicesService indicesService,
                                     PercolatorExecutor percolator) {
        super(index, indexSettings);
        this.indicesService = indicesService;
        this.percolator = percolator;
        this.shardLifecycleListener = new ShardLifecycleListener();
        this.indicesService.indicesLifecycle().addListener(shardLifecycleListener);
        this.percolator.setIndicesLifecycle(indicesService.indicesLifecycle());
    }

    public void close() {
        this.indicesService.indicesLifecycle().removeListener(shardLifecycleListener);
    }

    public PercolatorExecutor.Response percolate(PercolatorExecutor.SourceRequest request) throws PercolatorException {
        return percolator.percolate(request);
    }

    public PercolatorExecutor.Response percolate(PercolatorExecutor.DocAndSourceQueryRequest request) throws PercolatorException {
        return percolator.percolate(request);
    }

    private void loadQueries(String indexName) {
        IndexService indexService = percolatorIndexService();
        IndexShard shard = indexService.shard(0);
        Engine.Searcher searcher = shard.searcher();
        try {
            // create a query to fetch all queries that are registered under the index name (which is the type
            // in the percolator).
            Query query = new DeletionAwareConstantScoreQuery(indexQueriesFilter(indexName));
            QueriesLoaderCollector queries = new QueriesLoaderCollector();
            searcher.searcher().search(query, queries);
            percolator.addQueries(queries.queries());
        } catch (IOException e) {
            throw new PercolatorException(index, "failed to load queries from percolator index");
        } finally {
            searcher.release();
        }
    }

    private Filter indexQueriesFilter(String indexName) {
        return percolatorIndexService().cache().filter().cache(new TermFilter(new Term(TypeFieldMapper.NAME, indexName)));
    }

    private boolean percolatorAllocated() {
        if (!indicesService.hasIndex(INDEX_NAME)) {
            return false;
        }
        if (percolatorIndexService().numberOfShards() == 0) {
            return false;
        }
        if (percolatorIndexService().shard(0).state() != IndexShardState.STARTED) {
            return false;
        }
        return true;
    }

    private IndexService percolatorIndexService() {
        return indicesService.indexService(INDEX_NAME);
    }

    class QueriesLoaderCollector extends Collector {

        private FieldData fieldData;

        private IndexReader reader;

        private Map<String, Query> queries = Maps.newHashMap();

        public Map<String, Query> queries() {
            return this.queries;
        }

        @Override public void setScorer(Scorer scorer) throws IOException {
        }

        @Override public void collect(int doc) throws IOException {
            String id = fieldData.stringValue(doc);
            // the _source is the query
            Document document = reader.document(doc, SourceFieldSelector.INSTANCE);
            byte[] source = document.getBinaryValue(SourceFieldMapper.NAME);
            try {
                queries.put(id, percolator.parseQuery(id, source, 0, source.length));
            } catch (Exception e) {
                logger.warn("failed to add query [{}]", e, id);
            }
        }

        @Override public void setNextReader(IndexReader reader, int docBase) throws IOException {
            this.reader = reader;
            fieldData = percolatorIndexService().cache().fieldData().cache(FieldDataType.DefaultTypes.STRING, reader, IdFieldMapper.NAME);
        }

        @Override public boolean acceptsDocsOutOfOrder() {
            return true;
        }
    }

    class ShardLifecycleListener extends IndicesLifecycle.Listener {

        @Override public void afterIndexShardCreated(IndexShard indexShard) {
            if (indexShard.shardId().index().name().equals(INDEX_NAME)) {
                indexShard.addListener(new RealTimePercolatorOperationListener());
            }
        }

        @Override public void afterIndexShardStarted(IndexShard indexShard) {
            if (indexShard.shardId().index().name().equals(INDEX_NAME)) {
                // percolator index has started, fetch what we can from it and initialize the indices
                // we have
                synchronized (mutex) {
                    if (initialQueriesFetchDone) {
                        return;
                    }
                    // we load the queries for all existing indices
                    for (IndexService indexService : indicesService) {
                        loadQueries(indexService.index().name());
                    }
                    initialQueriesFetchDone = true;
                }
            }
            if (!indexShard.shardId().index().equals(index())) {
                // not our index, bail
                return;
            }
            if (!percolatorAllocated()) {
                return;
            }
            // we are only interested when the first shard on this node has been created for an index
            // when it does, fetch the relevant queries if not fetched already
            if (indicesService.indexService(indexShard.shardId().index().name()).numberOfShards() != 1) {
                return;
            }
            synchronized (mutex) {
                if (initialQueriesFetchDone) {
                    return;
                }
                // we load queries for this index
                loadQueries(index.name());
                initialQueriesFetchDone = true;
            }
        }
    }

    class RealTimePercolatorOperationListener extends OperationListener {

        @Override public Engine.Create beforeCreate(Engine.Create create) {
            percolator.addQuery(create.id(), create.source());
            return create;
        }

        @Override public Engine.Index beforeIndex(Engine.Index index) {
            percolator.addQuery(index.id(), index.source());
            return index;
        }

        @Override public Engine.Delete beforeDelete(Engine.Delete delete) {
            percolator.removeQuery(delete.id());
            return delete;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4009.java