error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6634.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6634.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6634.java
text:
```scala
s@@hard.refresh(new Engine.Refresh().force(true).source("percolator_load_queries"));

package org.elasticsearch.index.percolator;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.lucene.HashedBytesRef;
import org.elasticsearch.common.lucene.search.TermFilter;
import org.elasticsearch.common.lucene.search.XConstantScoreQuery;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.util.concurrent.ConcurrentCollections;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.cache.IndexCache;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.fielddata.IndexFieldDataService;
import org.elasticsearch.index.indexing.IndexingOperationListener;
import org.elasticsearch.index.indexing.ShardIndexingService;
import org.elasticsearch.index.mapper.DocumentTypeListener;
import org.elasticsearch.index.mapper.MapperService;
import org.elasticsearch.index.mapper.internal.TypeFieldMapper;
import org.elasticsearch.index.query.IndexQueryParserService;
import org.elasticsearch.index.query.QueryParseContext;
import org.elasticsearch.index.settings.IndexSettings;
import org.elasticsearch.index.shard.AbstractIndexShardComponent;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.index.shard.service.IndexShard;
import org.elasticsearch.indices.IndicesLifecycle;
import org.elasticsearch.percolator.PercolatorService;

import java.util.concurrent.ConcurrentMap;

/**
 * Each shard will have a percolator registry even if there isn't a _percolator document type in the index.
 * For shards with indices that have no _percolator document type, this will hold no percolate queries.
 * <p/>
 * Once a document type has been created, the real-time percolator will start to listen to write events and update the
 * this registry with queries in real time.
 */
public class PercolatorQueriesRegistry extends AbstractIndexShardComponent {

    // This is a shard level service, but these below are index level service:
    private final IndexQueryParserService queryParserService;
    private final MapperService mapperService;
    private final IndicesLifecycle indicesLifecycle;
    private final IndexCache indexCache;
    private final IndexFieldDataService indexFieldDataService;

    private final ShardIndexingService indexingService;

    private final ConcurrentMap<HashedBytesRef, Query> percolateQueries = ConcurrentCollections.newConcurrentMapWithAggressiveConcurrency();
    private final ShardLifecycleListener shardLifecycleListener = new ShardLifecycleListener();
    private final RealTimePercolatorOperationListener realTimePercolatorOperationListener = new RealTimePercolatorOperationListener();
    private final PercolateTypeListener percolateTypeListener = new PercolateTypeListener();

    private volatile boolean realTimePercolatorEnabled = false;

    @Inject
    public PercolatorQueriesRegistry(ShardId shardId, @IndexSettings Settings indexSettings, IndexQueryParserService queryParserService,
                                     ShardIndexingService indexingService, IndicesLifecycle indicesLifecycle, MapperService mapperService,
                                     IndexCache indexCache, IndexFieldDataService indexFieldDataService) {
        super(shardId, indexSettings);
        this.queryParserService = queryParserService;
        this.mapperService = mapperService;
        this.indicesLifecycle = indicesLifecycle;
        this.indexingService = indexingService;
        this.indexCache = indexCache;
        this.indexFieldDataService = indexFieldDataService;

        indicesLifecycle.addListener(shardLifecycleListener);
        mapperService.addTypeListener(percolateTypeListener);
    }

    public ConcurrentMap<HashedBytesRef, Query> percolateQueries() {
        return percolateQueries;
    }

    public void close() {
        mapperService.removeTypeListener(percolateTypeListener);
        indicesLifecycle.removeListener(shardLifecycleListener);
        indexingService.removeListener(realTimePercolatorOperationListener);
        clear();
    }

    public void clear() {
        percolateQueries.clear();
    }

    void enableRealTimePercolator() {
        if (!realTimePercolatorEnabled) {
            indexingService.addListener(realTimePercolatorOperationListener);
            realTimePercolatorEnabled = true;
        }
    }

    void disableRealTimePercolator() {
        if (realTimePercolatorEnabled) {
            indexingService.removeListener(realTimePercolatorOperationListener);
            realTimePercolatorEnabled = false;
        }
    }

    public void addPercolateQuery(String idAsString, BytesReference source) {
        Query query = parsePercolatorDocument(idAsString, source);
        percolateQueries.put(new HashedBytesRef(new BytesRef(idAsString)), query);
    }

    public void removePercolateQuery(String idAsString) {
        percolateQueries.remove(new HashedBytesRef(idAsString));
    }

    Query parsePercolatorDocument(String id, BytesReference source) {
        String type = null;
        BytesReference querySource = null;

        XContentParser parser = null;
        try {
            parser = XContentHelper.createParser(source);
            String currentFieldName = null;
            XContentParser.Token token = parser.nextToken(); // move the START_OBJECT
            if (token != XContentParser.Token.START_OBJECT) {
                throw new ElasticSearchException("failed to parse query [" + id + "], not starting with OBJECT");
            }
            while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                if (token == XContentParser.Token.FIELD_NAME) {
                    currentFieldName = parser.currentName();
                } else if (token == XContentParser.Token.START_OBJECT) {
                    if ("query".equals(currentFieldName)) {
                        if (type != null) {
                            return parseQuery(type, null, parser);
                        } else {
                            XContentBuilder builder = XContentFactory.contentBuilder(parser.contentType());
                            builder.copyCurrentStructure(parser);
                            querySource = builder.bytes();
                            builder.close();
                        }
                    } else {
                        parser.skipChildren();
                    }
                } else if (token == XContentParser.Token.START_ARRAY) {
                    parser.skipChildren();
                } else if (token.isValue()) {
                    if ("type".equals(currentFieldName)) {
                        type = parser.text();
                    }
                }
            }
            return parseQuery(type, querySource, null);
        } catch (Exception e) {
            throw new PercolatorException(shardId().index(), "failed to parse query [" + id + "]", e);
        } finally {
            if (parser != null) {
                parser.close();
            }
        }
    }

    private Query parseQuery(String type, BytesReference querySource, XContentParser parser) {
        if (type == null) {
            if (parser != null) {
                return queryParserService.parse(parser).query();
            } else {
                return queryParserService.parse(querySource).query();
            }
        }

        String[] previousTypes = QueryParseContext.setTypesWithPrevious(new String[]{type});
        try {
            if (parser != null) {
                return queryParserService.parse(parser).query();
            } else {
                return queryParserService.parse(querySource).query();
            }
        } finally {
            QueryParseContext.setTypes(previousTypes);
        }
    }

    private class PercolateTypeListener implements DocumentTypeListener {

        @Override
        public void created(String type) {
            if (PercolatorService.Constants.TYPE_NAME.equals(type)) {
                enableRealTimePercolator();
            }
        }

        @Override
        public void removed(String type) {
            if (PercolatorService.Constants.TYPE_NAME.equals(type)) {
                disableRealTimePercolator();
                clear();
            }
        }

    }

    private class ShardLifecycleListener extends IndicesLifecycle.Listener {

        @Override
        public void afterIndexShardCreated(IndexShard indexShard) {
            if (hasPercolatorType(indexShard)) {
                enableRealTimePercolator();
            }
        }

        @Override
        public void afterIndexShardStarted(IndexShard indexShard) {
            if (hasPercolatorType(indexShard)) {
                // percolator index has started, fetch what we can from it and initialize the indices
                // we have
                logger.debug("loading percolator queries for index [{}] and shard[{}]...", shardId.index(), shardId.id());
                loadQueries(indexShard);
                logger.trace("done loading percolator queries for index [{}] and shard[{}]", shardId.index(), shardId.id());
            }
        }

        private boolean hasPercolatorType(IndexShard indexShard) {
            ShardId otherShardId = indexShard.shardId();
            return shardId.equals(otherShardId) && mapperService.hasMapping(PercolatorService.Constants.TYPE_NAME);
        }

        private void loadQueries(IndexShard shard) {
            try {
                shard.refresh(new Engine.Refresh().force(true));
                Engine.Searcher searcher = shard.acquireSearcher();
                try {
                    Query query = new XConstantScoreQuery(
                            indexCache.filter().cache(
                                    new TermFilter(new Term(TypeFieldMapper.NAME, PercolatorService.Constants.TYPE_NAME))
                            )
                    );
                    QueriesLoaderCollector queries = new QueriesLoaderCollector(PercolatorQueriesRegistry.this, logger, indexFieldDataService);
                    searcher.searcher().search(query, queries);
                    percolateQueries.putAll(queries.queries());
                } finally {
                    searcher.release();
                }
            } catch (Exception e) {
                throw new PercolatorException(shardId.index(), "failed to load queries from percolator index", e);
            }
        }

    }

    private class RealTimePercolatorOperationListener extends IndexingOperationListener {

        @Override
        public Engine.Create preCreate(Engine.Create create) {
            // validate the query here, before we index
            if (PercolatorService.Constants.TYPE_NAME.equals(create.type())) {
                parsePercolatorDocument(create.id(), create.source());
            }
            return create;
        }

        @Override
        public void postCreateUnderLock(Engine.Create create) {
            // add the query under a doc lock
            if (PercolatorService.Constants.TYPE_NAME.equals(create.type())) {
                addPercolateQuery(create.id(), create.source());
            }
        }

        @Override
        public Engine.Index preIndex(Engine.Index index) {
            // validate the query here, before we index
            if (PercolatorService.Constants.TYPE_NAME.equals(index.type())) {
                parsePercolatorDocument(index.id(), index.source());
            }
            return index;
        }

        @Override
        public void postIndexUnderLock(Engine.Index index) {
            // add the query under a doc lock
            if (PercolatorService.Constants.TYPE_NAME.equals(index.type())) {
                addPercolateQuery(index.id(), index.source());
            }
        }

        @Override
        public void postDeleteUnderLock(Engine.Delete delete) {
            // remove the query under a lock
            if (PercolatorService.Constants.TYPE_NAME.equals(delete.type())) {
                removePercolateQuery(delete.id());
            }
        }

        // Updating the live percolate queries for a delete by query is tricky with the current way delete by queries
        // are handled. It is only possible if we put a big lock around the post delete by query hook...

        // If we implement delete by query, that just runs a query and generates delete operations in a bulk, then
        // updating the live percolator is automatically supported for delete by query.
//        @Override
//        public void postDeleteByQuery(Engine.DeleteByQuery deleteByQuery) {
//        }
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6634.java