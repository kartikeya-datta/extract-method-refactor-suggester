error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7802.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7802.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7802.java
text:
```scala
c@@heckIndex.fixIndex(status);

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

package org.elasticsearch.index.shard.service;

import com.google.common.base.Charsets;
import org.apache.lucene.codecs.PostingsFormat;
import org.apache.lucene.index.CheckIndex;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.ThreadInterruptedException;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.ElasticSearchIllegalStateException;
import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.cluster.routing.ShardRoutingState;
import org.elasticsearch.common.Booleans;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.stream.BytesStreamOutput;
import org.elasticsearch.common.lucene.Lucene;
import org.elasticsearch.common.lucene.search.XFilteredQuery;
import org.elasticsearch.common.metrics.MeanMetric;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.aliases.IndexAliasesService;
import org.elasticsearch.index.cache.IndexCache;
import org.elasticsearch.index.cache.filter.FilterCacheStats;
import org.elasticsearch.index.cache.filter.ShardFilterCache;
import org.elasticsearch.index.cache.id.IdCacheStats;
import org.elasticsearch.index.cache.id.ShardIdCache;
import org.elasticsearch.index.codec.CodecService;
import org.elasticsearch.index.engine.*;
import org.elasticsearch.index.fielddata.FieldDataStats;
import org.elasticsearch.index.fielddata.ShardFieldData;
import org.elasticsearch.index.flush.FlushStats;
import org.elasticsearch.index.get.GetStats;
import org.elasticsearch.index.get.ShardGetService;
import org.elasticsearch.index.indexing.IndexingStats;
import org.elasticsearch.index.indexing.ShardIndexingService;
import org.elasticsearch.index.mapper.*;
import org.elasticsearch.index.merge.MergeStats;
import org.elasticsearch.index.merge.scheduler.MergeSchedulerProvider;
import org.elasticsearch.index.percolator.PercolatorQueriesRegistry;
import org.elasticsearch.index.percolator.stats.ShardPercolateService;
import org.elasticsearch.index.query.IndexQueryParserService;
import org.elasticsearch.index.refresh.RefreshStats;
import org.elasticsearch.index.search.nested.NonNestedDocsFilter;
import org.elasticsearch.index.search.stats.SearchStats;
import org.elasticsearch.index.search.stats.ShardSearchService;
import org.elasticsearch.index.settings.IndexSettings;
import org.elasticsearch.index.settings.IndexSettingsService;
import org.elasticsearch.index.shard.*;
import org.elasticsearch.index.store.Store;
import org.elasticsearch.index.store.StoreStats;
import org.elasticsearch.index.termvectors.ShardTermVectorService;
import org.elasticsearch.index.translog.Translog;
import org.elasticsearch.index.warmer.ShardIndexWarmerService;
import org.elasticsearch.index.warmer.WarmerStats;
import org.elasticsearch.indices.IndicesLifecycle;
import org.elasticsearch.indices.InternalIndicesLifecycle;
import org.elasticsearch.indices.recovery.RecoveryStatus;
import org.elasticsearch.search.suggest.completion.Completion090PostingsFormat;
import org.elasticsearch.search.suggest.completion.CompletionStats;
import org.elasticsearch.threadpool.ThreadPool;

import java.io.PrintStream;
import java.nio.channels.ClosedByInterruptException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.index.mapper.SourceToParse.source;

/**
 *
 */
public class InternalIndexShard extends AbstractIndexShardComponent implements IndexShard {

    private final ThreadPool threadPool;
    private final IndexSettingsService indexSettingsService;
    private final MapperService mapperService;
    private final IndexQueryParserService queryParserService;
    private final IndexCache indexCache;
    private final InternalIndicesLifecycle indicesLifecycle;
    private final Store store;
    private final MergeSchedulerProvider mergeScheduler;
    private final Engine engine;
    private final Translog translog;
    private final IndexAliasesService indexAliasesService;
    private final ShardIndexingService indexingService;
    private final ShardSearchService searchService;
    private final ShardGetService getService;
    private final ShardIndexWarmerService shardWarmerService;
    private final ShardFilterCache shardFilterCache;
    private final ShardIdCache shardIdCache;
    private final ShardFieldData shardFieldData;
    private final PercolatorQueriesRegistry percolatorQueriesRegistry;
    private final ShardPercolateService shardPercolateService;
    private final CodecService codecService;
    private final ShardTermVectorService termVectorService;

    private final Object mutex = new Object();
    private final String checkIndexOnStartup;
    private long checkIndexTook = 0;
    private volatile IndexShardState state;

    private TimeValue refreshInterval;
    private final TimeValue mergeInterval;

    private volatile ScheduledFuture refreshScheduledFuture;
    private volatile ScheduledFuture mergeScheduleFuture;
    private volatile ShardRouting shardRouting;

    private RecoveryStatus peerRecoveryStatus;

    private ApplyRefreshSettings applyRefreshSettings = new ApplyRefreshSettings();

    private final MeanMetric refreshMetric = new MeanMetric();
    private final MeanMetric flushMetric = new MeanMetric();

    @Inject
    public InternalIndexShard(ShardId shardId, @IndexSettings Settings indexSettings, IndexSettingsService indexSettingsService, IndicesLifecycle indicesLifecycle, Store store, Engine engine, MergeSchedulerProvider mergeScheduler, Translog translog,
                              ThreadPool threadPool, MapperService mapperService, IndexQueryParserService queryParserService, IndexCache indexCache, IndexAliasesService indexAliasesService, ShardIndexingService indexingService, ShardGetService getService, ShardSearchService searchService, ShardIndexWarmerService shardWarmerService,
                              ShardFilterCache shardFilterCache, ShardIdCache shardIdCache, ShardFieldData shardFieldData,
                              PercolatorQueriesRegistry percolatorQueriesRegistry, ShardPercolateService shardPercolateService, CodecService codecService,
                              ShardTermVectorService termVectorService) {
        super(shardId, indexSettings);
        this.indicesLifecycle = (InternalIndicesLifecycle) indicesLifecycle;
        this.indexSettingsService = indexSettingsService;
        this.store = store;
        this.engine = engine;
        this.mergeScheduler = mergeScheduler;
        this.translog = translog;
        this.threadPool = threadPool;
        this.mapperService = mapperService;
        this.queryParserService = queryParserService;
        this.indexCache = indexCache;
        this.indexAliasesService = indexAliasesService;
        this.indexingService = indexingService;
        this.getService = getService.setIndexShard(this);
        this.termVectorService = termVectorService.setIndexShard(this);
        this.searchService = searchService;
        this.shardWarmerService = shardWarmerService;
        this.shardFilterCache = shardFilterCache;
        this.shardIdCache = shardIdCache;
        this.shardFieldData = shardFieldData;
        this.percolatorQueriesRegistry = percolatorQueriesRegistry;
        this.shardPercolateService = shardPercolateService;
        this.codecService = codecService;
        state = IndexShardState.CREATED;

        this.refreshInterval = indexSettings.getAsTime("engine.robin.refresh_interval", indexSettings.getAsTime(INDEX_REFRESH_INTERVAL, engine.defaultRefreshInterval()));
        this.mergeInterval = indexSettings.getAsTime("index.merge.async_interval", TimeValue.timeValueSeconds(1));

        indexSettingsService.addListener(applyRefreshSettings);

        logger.debug("state: [CREATED]");

        this.checkIndexOnStartup = indexSettings.get("index.shard.check_on_startup", "false");
    }

    public MergeSchedulerProvider mergeScheduler() {
        return this.mergeScheduler;
    }

    public Store store() {
        return this.store;
    }

    public Engine engine() {
        return engine;
    }

    public Translog translog() {
        return translog;
    }

    public ShardIndexingService indexingService() {
        return this.indexingService;
    }

    @Override
    public ShardGetService getService() {
        return this.getService;
    }

    @Override
    public ShardTermVectorService termVectorService() {
        return termVectorService;
    }


    @Override
    public ShardSearchService searchService() {
        return this.searchService;
    }

    @Override
    public ShardIndexWarmerService warmerService() {
        return this.shardWarmerService;
    }

    @Override
    public ShardFilterCache filterCache() {
        return this.shardFilterCache;
    }

    @Override
    public ShardIdCache idCache() {
        return this.shardIdCache;
    }

    @Override
    public ShardFieldData fieldData() {
        return this.shardFieldData;
    }

    @Override
    public ShardRouting routingEntry() {
        return this.shardRouting;
    }

    public InternalIndexShard routingEntry(ShardRouting newRouting) {
        ShardRouting currentRouting = this.shardRouting;
        if (!newRouting.shardId().equals(shardId())) {
            throw new ElasticSearchIllegalArgumentException("Trying to set a routing entry with shardId [" + newRouting.shardId() + "] on a shard with shardId [" + shardId() + "]");
        }
        if (currentRouting != null) {
            if (!newRouting.primary() && currentRouting.primary()) {
                logger.warn("suspect illegal state: trying to move shard from primary mode to replica mode");
            }
            // if its the same routing, return
            if (currentRouting.equals(newRouting)) {
                return this;
            }
        }

        // make sure we refresh on state change due to cluster state changes
        if (newRouting.state() == ShardRoutingState.STARTED && (currentRouting == null || currentRouting.state() != ShardRoutingState.STARTED)) {
            try {
                engine.refresh(new Engine.Refresh("cluster_state_started").force(true));
            } catch (Throwable t) {
                logger.debug("failed to refresh due to move to cluster wide started", t);
            }
            synchronized (mutex) {
                if (state != IndexShardState.POST_RECOVERY) {
                    logger.debug("suspected wrong state when acting on cluster state started state, current state {}", state);
                }
                logger.debug("state: [{}]->[{}], reason [global state moved to started]", state, IndexShardState.STARTED);
                state = IndexShardState.STARTED;
            }
            indicesLifecycle.afterIndexShardStarted(this);
        }
        this.shardRouting = newRouting;
        indicesLifecycle.shardRoutingChanged(this, currentRouting, newRouting);
        return this;
    }

    /**
     * Marks the shard as recovering, fails with exception is recovering is not allowed to be set.
     */
    public IndexShardState recovering(String reason) throws IndexShardStartedException,
            IndexShardRelocatedException, IndexShardRecoveringException, IndexShardClosedException {
        synchronized (mutex) {
            IndexShardState returnValue = state;
            if (state == IndexShardState.CLOSED) {
                throw new IndexShardClosedException(shardId);
            }
            if (state == IndexShardState.STARTED) {
                throw new IndexShardStartedException(shardId);
            }
            if (state == IndexShardState.RELOCATED) {
                throw new IndexShardRelocatedException(shardId);
            }
            if (state == IndexShardState.RECOVERING) {
                throw new IndexShardRecoveringException(shardId);
            }
            if (state == IndexShardState.POST_RECOVERY) {
                throw new IndexShardRecoveringException(shardId);
            }
            logger.debug("state: [{}]->[{}], reason [{}]", state, IndexShardState.RECOVERING, reason);
            state = IndexShardState.RECOVERING;
            return returnValue;
        }
    }

    public InternalIndexShard relocated(String reason) throws IndexShardNotStartedException {
        synchronized (mutex) {
            if (state != IndexShardState.STARTED) {
                throw new IndexShardNotStartedException(shardId, state);
            }
            logger.debug("state: [{}]->[{}], reason [{}]", state, IndexShardState.RELOCATED, reason);
            state = IndexShardState.RELOCATED;
        }
        return this;
    }

    @Override
    public IndexShardState state() {
        return state;
    }

    @Override
    public Engine.Create prepareCreate(SourceToParse source) throws ElasticSearchException {
        long startTime = System.nanoTime();
        DocumentMapper docMapper = mapperService.documentMapperWithAutoCreate(source.type());
        ParsedDocument doc = docMapper.parse(source);
        return new Engine.Create(docMapper, docMapper.uidMapper().term(doc.uid().stringValue()), doc).startTime(startTime);
    }

    @Override
    public ParsedDocument create(Engine.Create create) throws ElasticSearchException {
        writeAllowed(create.origin());
        create = indexingService.preCreate(create);
        if (logger.isTraceEnabled()) {
            logger.trace("index {}", create.docs());
        }
        engine.create(create);
        create.endTime(System.nanoTime());
        indexingService.postCreate(create);
        return create.parsedDoc();
    }

    @Override
    public Engine.Index prepareIndex(SourceToParse source) throws ElasticSearchException {
        long startTime = System.nanoTime();
        DocumentMapper docMapper = mapperService.documentMapperWithAutoCreate(source.type());
        ParsedDocument doc = docMapper.parse(source);
        return new Engine.Index(docMapper, docMapper.uidMapper().term(doc.uid().stringValue()), doc).startTime(startTime);
    }

    @Override
    public ParsedDocument index(Engine.Index index) throws ElasticSearchException {
        writeAllowed(index.origin());
        index = indexingService.preIndex(index);
        try {
            if (logger.isTraceEnabled()) {
                logger.trace("index {}", index.docs());
            }
            engine.index(index);
            index.endTime(System.nanoTime());
        } catch (RuntimeException ex) {
            indexingService.failedIndex(index);
            throw ex;
        }
        indexingService.postIndex(index);
        return index.parsedDoc();
    }

    @Override
    public Engine.Delete prepareDelete(String type, String id, long version) throws ElasticSearchException {
        long startTime = System.nanoTime();
        DocumentMapper docMapper = mapperService.documentMapperWithAutoCreate(type);
        return new Engine.Delete(type, id, docMapper.uidMapper().term(type, id)).version(version).startTime(startTime);
    }

    @Override
    public void delete(Engine.Delete delete) throws ElasticSearchException {
        writeAllowed(delete.origin());
        delete = indexingService.preDelete(delete);
        try {
            if (logger.isTraceEnabled()) {
                logger.trace("delete [{}]", delete.uid().text());
            }
            engine.delete(delete);
            delete.endTime(System.nanoTime());
        } catch (RuntimeException ex) {
            indexingService.failedDelete(delete);
            throw ex;
        }
        indexingService.postDelete(delete);
    }

    @Override
    public Engine.DeleteByQuery prepareDeleteByQuery(BytesReference querySource, @Nullable String[] filteringAliases, String... types) throws ElasticSearchException {
        long startTime = System.nanoTime();
        if (types == null) {
            types = Strings.EMPTY_ARRAY;
        }
        Query query = queryParserService.parse(querySource).query();
        query = filterQueryIfNeeded(query, types);

        Filter aliasFilter = indexAliasesService.aliasFilter(filteringAliases);
        Filter parentFilter = mapperService.hasNested() ? indexCache.filter().cache(NonNestedDocsFilter.INSTANCE) : null;
        return new Engine.DeleteByQuery(query, querySource, filteringAliases, aliasFilter, parentFilter, types).startTime(startTime);
    }

    @Override
    public void deleteByQuery(Engine.DeleteByQuery deleteByQuery) throws ElasticSearchException {
        writeAllowed(deleteByQuery.origin());
        if (logger.isTraceEnabled()) {
            logger.trace("delete_by_query [{}]", deleteByQuery.query());
        }
        deleteByQuery = indexingService.preDeleteByQuery(deleteByQuery);
        engine.delete(deleteByQuery);
        deleteByQuery.endTime(System.nanoTime());
        indexingService.postDeleteByQuery(deleteByQuery);
    }

    @Override
    public Engine.GetResult get(Engine.Get get) throws ElasticSearchException {
        readAllowed();
        return engine.get(get);
    }

    @Override
    public void refresh(Engine.Refresh refresh) throws ElasticSearchException {
        verifyNotClosed();
        if (logger.isTraceEnabled()) {
            logger.trace("refresh with {}", refresh);
        }
        long time = System.nanoTime();
        engine.refresh(refresh);
        refreshMetric.inc(System.nanoTime() - time);
    }

    @Override
    public RefreshStats refreshStats() {
        return new RefreshStats(refreshMetric.count(), TimeUnit.NANOSECONDS.toMillis(refreshMetric.sum()));
    }

    @Override
    public FlushStats flushStats() {
        return new FlushStats(flushMetric.count(), TimeUnit.NANOSECONDS.toMillis(flushMetric.sum()));
    }

    @Override
    public DocsStats docStats() {
        try {
            final Engine.Searcher searcher = acquireSearcher("doc_stats");
            try {
                return new DocsStats(searcher.reader().numDocs(), searcher.reader().numDeletedDocs());
            } finally {
                searcher.release();
            }
        } catch (Throwable e) {
            logger.debug("Can not build 'doc stats' from engine shard state [{}]", e, state);
            return new DocsStats();
        }
    }

    @Override
    public IndexingStats indexingStats(String... types) {
        return indexingService.stats(types);
    }

    @Override
    public SearchStats searchStats(String... groups) {
        return searchService.stats(groups);
    }

    @Override
    public GetStats getStats() {
        return getService.stats();
    }

    @Override
    public StoreStats storeStats() {
        try {
            return store.stats();
        } catch (Throwable e) {
            logger.debug("Can not build 'store stats' from engine shard state [{}]", e, state);
            return new StoreStats();
        }
    }

    @Override
    public MergeStats mergeStats() {
        return mergeScheduler.stats();
    }

    @Override
    public WarmerStats warmerStats() {
        return shardWarmerService.stats();
    }

    @Override
    public FilterCacheStats filterCacheStats() {
        return shardFilterCache.stats();
    }

    @Override
    public FieldDataStats fieldDataStats(String... fields) {
        return shardFieldData.stats(fields);
    }

    @Override
    public PercolatorQueriesRegistry percolateRegistry() {
        return percolatorQueriesRegistry;
    }

    @Override
    public ShardPercolateService shardPercolateService() {
        return shardPercolateService;
    }

    @Override
    public IdCacheStats idCacheStats() {
        return shardIdCache.stats();
    }

    @Override
    public CompletionStats completionStats(String... fields) {
        CompletionStats completionStats = new CompletionStats();
        try {
            final Engine.Searcher currentSearcher = acquireSearcher("completion_stats");
            try {
                PostingsFormat postingsFormat = this.codecService.postingsFormatService().get(Completion090PostingsFormat.CODEC_NAME).get();
                if (postingsFormat instanceof Completion090PostingsFormat) {
                    Completion090PostingsFormat completionPostingsFormat = (Completion090PostingsFormat) postingsFormat;
                    completionStats.add(completionPostingsFormat.completionStats(currentSearcher.reader(), fields));
                }
            } finally {
                currentSearcher.release();
            }
        } catch (Throwable e) {
            logger.debug("Can not build 'completion stats' from engine shard state [{}]", e, state);
            // if we are called during engine stop / start or before start we can run into Exceptions
            // like the engine is already closed or no saercher is present at this point.
        }
        return completionStats;
    }

    @Override
    public void flush(Engine.Flush flush) throws ElasticSearchException {
        // we allows flush while recovering, since we allow for operations to happen
        // while recovering, and we want to keep the translog at bay (up to deletes, which
        // we don't gc).
        verifyStartedOrRecovering();
        if (logger.isTraceEnabled()) {
            logger.trace("flush with {}", flush);
        }
        long time = System.nanoTime();
        engine.flush(flush);
        flushMetric.inc(System.nanoTime() - time);
    }

    @Override
    public void optimize(Engine.Optimize optimize) throws ElasticSearchException {
        verifyStarted();
        if (logger.isTraceEnabled()) {
            logger.trace("optimize with {}", optimize);
        }
        engine.optimize(optimize);
    }

    @Override
    public <T> T snapshot(Engine.SnapshotHandler<T> snapshotHandler) throws EngineException {
        IndexShardState state = this.state; // one time volatile read
        // we allow snapshot on closed index shard, since we want to do one after we close the shard and before we close the engine
        if (state != IndexShardState.POST_RECOVERY && state != IndexShardState.STARTED && state != IndexShardState.RELOCATED && state != IndexShardState.CLOSED) {
            throw new IllegalIndexShardStateException(shardId, state, "snapshot is not allowed");
        }
        return engine.snapshot(snapshotHandler);
    }

    @Override
    public void recover(Engine.RecoveryHandler recoveryHandler) throws EngineException {
        verifyStarted();
        engine.recover(recoveryHandler);
    }

    @Override
    public Engine.Searcher acquireSearcher(String source) {
        readAllowed();
        return engine.acquireSearcher(source);
    }

    public void close(String reason) {
        synchronized (mutex) {
            indexSettingsService.removeListener(applyRefreshSettings);
            if (state != IndexShardState.CLOSED) {
                if (refreshScheduledFuture != null) {
                    refreshScheduledFuture.cancel(true);
                    refreshScheduledFuture = null;
                }
                if (mergeScheduleFuture != null) {
                    mergeScheduleFuture.cancel(true);
                    mergeScheduleFuture = null;
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug("state: [{}]->[{}], reason [{}]", state, IndexShardState.CLOSED, reason);
            }
            state = IndexShardState.CLOSED;
        }
    }

    public long checkIndexTook() {
        return this.checkIndexTook;
    }


    public InternalIndexShard postRecovery(String reason) throws IndexShardStartedException, IndexShardRelocatedException, IndexShardClosedException {
        synchronized (mutex) {
            if (state == IndexShardState.CLOSED) {
                throw new IndexShardClosedException(shardId);
            }
            if (state == IndexShardState.STARTED) {
                throw new IndexShardStartedException(shardId);
            }
            if (state == IndexShardState.RELOCATED) {
                throw new IndexShardRelocatedException(shardId);
            }
            if (Booleans.parseBoolean(checkIndexOnStartup, false)) {
                checkIndex(true);
            }
            engine.start();
            startScheduledTasksIfNeeded();
            logger.debug("state: [{}]->[{}], reason [{}]", state, IndexShardState.POST_RECOVERY, reason);
            state = IndexShardState.POST_RECOVERY;
        }
        return this;
    }

    /**
     * After the store has been recovered, we need to start the engine in order to apply operations
     */
    public void performRecoveryPrepareForTranslog() throws ElasticSearchException {
        if (state != IndexShardState.RECOVERING) {
            throw new IndexShardNotRecoveringException(shardId, state);
        }
        // also check here, before we apply the translog
        if (Booleans.parseBoolean(checkIndexOnStartup, false)) {
            checkIndex(true);
        }
        // we disable deletes since we allow for operations to be executed against the shard while recovering
        // but we need to make sure we don't loose deletes until we are done recovering
        engine.enableGcDeletes(false);
        engine.start();
    }

    /**
     * The peer recovery status if this shard recovered from a peer shard.
     */
    public RecoveryStatus peerRecoveryStatus() {
        return this.peerRecoveryStatus;
    }

    public void performRecoveryFinalization(boolean withFlush, RecoveryStatus peerRecoveryStatus) throws ElasticSearchException {
        performRecoveryFinalization(withFlush);
        this.peerRecoveryStatus = peerRecoveryStatus;
    }

    public void performRecoveryFinalization(boolean withFlush) throws ElasticSearchException {
        if (withFlush) {
            engine.flush(new Engine.Flush());
        }
        // clear unreferenced files
        translog.clearUnreferenced();
        engine.refresh(new Engine.Refresh("recovery_finalization").force(true));
        synchronized (mutex) {
            logger.debug("state: [{}]->[{}], reason [post recovery]", state, IndexShardState.POST_RECOVERY);
            state = IndexShardState.POST_RECOVERY;
        }
        startScheduledTasksIfNeeded();
        engine.enableGcDeletes(true);
    }

    public void performRecoveryOperation(Translog.Operation operation) throws ElasticSearchException {
        if (state != IndexShardState.RECOVERING) {
            throw new IndexShardNotRecoveringException(shardId, state);
        }
        try {
            switch (operation.opType()) {
                case CREATE:
                    Translog.Create create = (Translog.Create) operation;
                    engine.create(prepareCreate(source(create.source()).type(create.type()).id(create.id())
                            .routing(create.routing()).parent(create.parent()).timestamp(create.timestamp()).ttl(create.ttl())).version(create.version())
                            .origin(Engine.Operation.Origin.RECOVERY));
                    break;
                case SAVE:
                    Translog.Index index = (Translog.Index) operation;
                    engine.index(prepareIndex(source(index.source()).type(index.type()).id(index.id())
                            .routing(index.routing()).parent(index.parent()).timestamp(index.timestamp()).ttl(index.ttl())).version(index.version())
                            .origin(Engine.Operation.Origin.RECOVERY));
                    break;
                case DELETE:
                    Translog.Delete delete = (Translog.Delete) operation;
                    Uid uid = Uid.createUid(delete.uid().text());
                    engine.delete(new Engine.Delete(uid.type(), uid.id(), delete.uid()).version(delete.version())
                            .origin(Engine.Operation.Origin.RECOVERY));
                    break;
                case DELETE_BY_QUERY:
                    Translog.DeleteByQuery deleteByQuery = (Translog.DeleteByQuery) operation;
                    engine.delete(prepareDeleteByQuery(deleteByQuery.source(), deleteByQuery.filteringAliases(), deleteByQuery.types()).origin(Engine.Operation.Origin.RECOVERY));
                    break;
                default:
                    throw new ElasticSearchIllegalStateException("No operation defined for [" + operation + "]");
            }
        } catch (ElasticSearchException e) {
            boolean hasIgnoreOnRecoveryException = false;
            ElasticSearchException current = e;
            while (true) {
                if (current instanceof IgnoreOnRecoveryEngineException) {
                    hasIgnoreOnRecoveryException = true;
                    break;
                }
                if (current.getCause() instanceof ElasticSearchException) {
                    current = (ElasticSearchException) current.getCause();
                } else {
                    break;
                }
            }
            if (!hasIgnoreOnRecoveryException) {
                throw e;
            }
        }
    }

    /**
     * Returns <tt>true</tt> if this shard can ignore a recovery attempt made to it (since the already doing/done it)
     */
    public boolean ignoreRecoveryAttempt() {
        IndexShardState state = state(); // one time volatile read
        return state == IndexShardState.POST_RECOVERY || state == IndexShardState.RECOVERING || state == IndexShardState.STARTED ||
                state == IndexShardState.RELOCATED || state == IndexShardState.CLOSED;
    }

    public void readAllowed() throws IllegalIndexShardStateException {
        IndexShardState state = this.state; // one time volatile read
        if (state != IndexShardState.STARTED && state != IndexShardState.RELOCATED) {
            throw new IllegalIndexShardStateException(shardId, state, "operations only allowed when started/relocated");
        }
    }

    private void writeAllowed(Engine.Operation.Origin origin) throws IllegalIndexShardStateException {
        IndexShardState state = this.state; // one time volatile read

        if (origin == Engine.Operation.Origin.PRIMARY) {
            // for primaries, we only allow to write when actually started (so the cluster has decided we started)
            // otherwise, we need to retry, we also want to still allow to index if we are relocated in case it fails
            if (state != IndexShardState.STARTED && state != IndexShardState.RELOCATED) {
                throw new IllegalIndexShardStateException(shardId, state, "operation only allowed when started/recovering, origin [" + origin + "]");
            }
        } else {
            // for replicas, we allow to write also while recovering, since we index also during recovery to replicas
            // and rely on version checks to make sure its consistent
            if (state != IndexShardState.STARTED && state != IndexShardState.RELOCATED && state != IndexShardState.RECOVERING && state != IndexShardState.POST_RECOVERY) {
                throw new IllegalIndexShardStateException(shardId, state, "operation only allowed when started/recovering, origin [" + origin + "]");
            }
        }
    }

    private void verifyStartedOrRecovering() throws IllegalIndexShardStateException {
        IndexShardState state = this.state; // one time volatile read
        if (state != IndexShardState.STARTED && state != IndexShardState.RECOVERING && state != IndexShardState.POST_RECOVERY) {
            throw new IllegalIndexShardStateException(shardId, state, "operation only allowed when started/recovering");
        }
    }

    private void verifyNotClosed() throws IllegalIndexShardStateException {
        IndexShardState state = this.state; // one time volatile read
        if (state == IndexShardState.CLOSED) {
            throw new IllegalIndexShardStateException(shardId, state, "operation only allowed when not closed");
        }
    }

    private void verifyStarted() throws IllegalIndexShardStateException {
        IndexShardState state = this.state; // one time volatile read
        if (state != IndexShardState.STARTED) {
            throw new IndexShardNotStartedException(shardId, state);
        }
    }

    private void startScheduledTasksIfNeeded() {
        if (refreshInterval.millis() > 0) {
            refreshScheduledFuture = threadPool.schedule(refreshInterval, ThreadPool.Names.SAME, new EngineRefresher());
            logger.debug("scheduling refresher every {}", refreshInterval);
        } else {
            logger.debug("scheduled refresher disabled");
        }
        // since we can do async merging, it will not be called explicitly when indexing (adding / deleting docs), and only when flushing
        // so, make sure we periodically call it, this need to be a small enough value so mergine will actually
        // happen and reduce the number of segments
        if (mergeInterval.millis() > 0) {
            mergeScheduleFuture = threadPool.schedule(mergeInterval, ThreadPool.Names.SAME, new EngineMerger());
            logger.debug("scheduling optimizer / merger every {}", mergeInterval);
        } else {
            logger.debug("scheduled optimizer / merger disabled");
        }
    }

    private Query filterQueryIfNeeded(Query query, String[] types) {
        Filter searchFilter = mapperService.searchFilter(types);
        if (searchFilter != null) {
            query = new XFilteredQuery(query, indexCache.filter().cache(searchFilter));
        }
        return query;
    }

    public static final String INDEX_REFRESH_INTERVAL = "index.refresh_interval";

    private class ApplyRefreshSettings implements IndexSettingsService.Listener {
        @Override
        public void onRefreshSettings(Settings settings) {
            synchronized (mutex) {
                if (state == IndexShardState.CLOSED) {
                    return;
                }
                TimeValue refreshInterval = settings.getAsTime("engine.robin.refresh_interval", settings.getAsTime(INDEX_REFRESH_INTERVAL, InternalIndexShard.this.refreshInterval));
                if (!refreshInterval.equals(InternalIndexShard.this.refreshInterval)) {
                    logger.info("updating refresh_interval from [{}] to [{}]", InternalIndexShard.this.refreshInterval, refreshInterval);
                    if (refreshScheduledFuture != null) {
                        refreshScheduledFuture.cancel(false);
                        refreshScheduledFuture = null;
                    }
                    InternalIndexShard.this.refreshInterval = refreshInterval;
                    if (refreshInterval.millis() > 0) {
                        refreshScheduledFuture = threadPool.schedule(refreshInterval, ThreadPool.Names.SAME, new EngineRefresher());
                    }
                }
            }
        }
    }

    class EngineRefresher implements Runnable {
        @Override
        public void run() {
            // we check before if a refresh is needed, if not, we reschedule, otherwise, we fork, refresh, and then reschedule
            if (!engine().refreshNeeded()) {
                synchronized (mutex) {
                    if (state != IndexShardState.CLOSED) {
                        refreshScheduledFuture = threadPool.schedule(refreshInterval, ThreadPool.Names.SAME, this);
                    }
                }
                return;
            }
            threadPool.executor(ThreadPool.Names.REFRESH).execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (engine.refreshNeeded()) {
                            refresh(new Engine.Refresh("scheduled").force(false));
                        }
                    } catch (EngineClosedException e) {
                        // we are being closed, ignore
                    } catch (RefreshFailedEngineException e) {
                        if (e.getCause() instanceof InterruptedException) {
                            // ignore, we are being shutdown
                        } else if (e.getCause() instanceof ClosedByInterruptException) {
                            // ignore, we are being shutdown
                        } else if (e.getCause() instanceof ThreadInterruptedException) {
                            // ignore, we are being shutdown
                        } else {
                            if (state != IndexShardState.CLOSED) {
                                logger.warn("Failed to perform scheduled engine refresh", e);
                            }
                        }
                    } catch (Exception e) {
                        if (state != IndexShardState.CLOSED) {
                            logger.warn("Failed to perform scheduled engine refresh", e);
                        }
                    }
                    synchronized (mutex) {
                        if (state != IndexShardState.CLOSED) {
                            refreshScheduledFuture = threadPool.schedule(refreshInterval, ThreadPool.Names.SAME, EngineRefresher.this);
                        }
                    }
                }
            });
        }
    }

    class EngineMerger implements Runnable {
        @Override
        public void run() {
            if (!engine().possibleMergeNeeded()) {
                synchronized (mutex) {
                    if (state != IndexShardState.CLOSED) {
                        mergeScheduleFuture = threadPool.schedule(mergeInterval, ThreadPool.Names.SAME, this);
                    }
                }
                return;
            }
            threadPool.executor(ThreadPool.Names.MERGE).execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        engine.maybeMerge();
                    } catch (EngineClosedException e) {
                        // we are being closed, ignore
                    } catch (OptimizeFailedEngineException e) {
                        if (e.getCause() instanceof EngineClosedException) {
                            // ignore, we are being shutdown
                        } else if (e.getCause() instanceof InterruptedException) {
                            // ignore, we are being shutdown
                        } else if (e.getCause() instanceof ClosedByInterruptException) {
                            // ignore, we are being shutdown
                        } else if (e.getCause() instanceof ThreadInterruptedException) {
                            // ignore, we are being shutdown
                        } else {
                            if (state != IndexShardState.CLOSED) {
                                logger.warn("Failed to perform scheduled engine optimize/merge", e);
                            }
                        }
                    } catch (Exception e) {
                        if (state != IndexShardState.CLOSED) {
                            logger.warn("Failed to perform scheduled engine optimize/merge", e);
                        }
                    }
                    synchronized (mutex) {
                        if (state != IndexShardState.CLOSED) {
                            mergeScheduleFuture = threadPool.schedule(mergeInterval, ThreadPool.Names.SAME, EngineMerger.this);
                        }
                    }
                }
            });
        }
    }

    private void checkIndex(boolean throwException) throws IndexShardException {
        try {
            checkIndexTook = 0;
            long time = System.currentTimeMillis();
            if (!Lucene.indexExists(store.directory())) {
                return;
            }
            CheckIndex checkIndex = new CheckIndex(store.directory());
            BytesStreamOutput os = new BytesStreamOutput();
            PrintStream out = new PrintStream(os, false, Charsets.UTF_8.name());
            checkIndex.setInfoStream(out);
            out.flush();
            CheckIndex.Status status = checkIndex.checkIndex();
            if (!status.clean) {
                if (state == IndexShardState.CLOSED) {
                    // ignore if closed....
                    return;
                }
                logger.warn("check index [failure]\n{}", new String(os.bytes().toBytes(), Charsets.UTF_8));
                if ("fix".equalsIgnoreCase(checkIndexOnStartup)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("fixing index, writing new segments file ...");
                    }
                    checkIndex.fixIndex(status, codecService.codec(indexSettings.get(Engine.INDEX_CODEC, "default")));
                    if (logger.isDebugEnabled()) {
                        logger.debug("index fixed, wrote new segments file \"{}\"", status.segmentsFileName);
                    }
                } else {
                    // only throw a failure if we are not going to fix the index
                    if (throwException) {
                        throw new IndexShardException(shardId, "index check failure");
                    }
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("check index [success]\n{}", new String(os.bytes().toBytes(), Charsets.UTF_8));
                }
            }
            checkIndexTook = System.currentTimeMillis() - time;
        } catch (Exception e) {
            logger.warn("failed to check index", e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7802.java