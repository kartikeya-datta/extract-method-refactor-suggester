error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1464.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1464.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1464.java
text:
```scala
t@@his.defaultKeepAlive = componentSettings.getAsTime("default_keep_alive", timeValueMinutes(2));

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

package org.elasticsearch.search;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import org.apache.lucene.search.TopDocs;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.service.IndexService;
import org.elasticsearch.index.shard.service.IndexShard;
import org.elasticsearch.indices.IndicesService;
import org.elasticsearch.search.dfs.CachedDfSource;
import org.elasticsearch.search.dfs.DfsPhase;
import org.elasticsearch.search.dfs.DfsSearchResult;
import org.elasticsearch.search.fetch.FetchPhase;
import org.elasticsearch.search.fetch.FetchSearchRequest;
import org.elasticsearch.search.fetch.FetchSearchResult;
import org.elasticsearch.search.fetch.QueryFetchSearchResult;
import org.elasticsearch.search.internal.InternalScrollSearchRequest;
import org.elasticsearch.search.internal.InternalSearchRequest;
import org.elasticsearch.search.internal.SearchContext;
import org.elasticsearch.search.query.QueryPhase;
import org.elasticsearch.search.query.QueryPhaseExecutionException;
import org.elasticsearch.search.query.QuerySearchRequest;
import org.elasticsearch.search.query.QuerySearchResult;
import org.elasticsearch.timer.TimerService;
import org.elasticsearch.util.TimeValue;
import org.elasticsearch.util.Unicode;
import org.elasticsearch.util.component.AbstractLifecycleComponent;
import org.elasticsearch.util.concurrent.highscalelib.NonBlockingHashMapLong;
import org.elasticsearch.util.json.Jackson;
import org.elasticsearch.util.settings.Settings;
import org.elasticsearch.util.timer.Timeout;
import org.elasticsearch.util.timer.TimerTask;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.elasticsearch.util.TimeValue.*;

/**
 * @author kimchy (shay.banon)
 */
public class SearchService extends AbstractLifecycleComponent<SearchService> {

    private final JsonFactory jsonFactory = Jackson.defaultJsonFactory();

    private final ClusterService clusterService;

    private final IndicesService indicesService;

    private final TimerService timerService;

    private final DfsPhase dfsPhase;

    private final QueryPhase queryPhase;

    private final FetchPhase fetchPhase;


    private final TimeValue defaultKeepAlive;


    private final AtomicLong idGenerator = new AtomicLong();

    private final NonBlockingHashMapLong<SearchContext> activeContexts = new NonBlockingHashMapLong<SearchContext>();

    private final ImmutableMap<String, SearchParseElement> elementParsers;

    @Inject public SearchService(Settings settings, ClusterService clusterService, IndicesService indicesService, TimerService timerService,
                                 DfsPhase dfsPhase, QueryPhase queryPhase, FetchPhase fetchPhase) {
        super(settings);
        this.clusterService = clusterService;
        this.indicesService = indicesService;
        this.timerService = timerService;
        this.dfsPhase = dfsPhase;
        this.queryPhase = queryPhase;
        this.fetchPhase = fetchPhase;

        this.defaultKeepAlive = componentSettings.getAsTime("defaultKeepAlive", timeValueMinutes(2));

        Map<String, SearchParseElement> elementParsers = new HashMap<String, SearchParseElement>();
        elementParsers.putAll(dfsPhase.parseElements());
        elementParsers.putAll(queryPhase.parseElements());
        elementParsers.putAll(fetchPhase.parseElements());
        this.elementParsers = ImmutableMap.copyOf(elementParsers);
    }

    @Override protected void doStart() throws ElasticSearchException {
    }

    @Override protected void doStop() throws ElasticSearchException {
        for (SearchContext context : activeContexts.values()) {
            freeContext(context);
        }
        activeContexts.clear();
    }

    @Override protected void doClose() throws ElasticSearchException {
    }

    public DfsSearchResult executeDfsPhase(InternalSearchRequest request) throws ElasticSearchException {
        SearchContext context = createContext(request);
        activeContexts.put(context.id(), context);
        dfsPhase.execute(context);
        return context.dfsResult();
    }

    public QuerySearchResult executeQueryPhase(InternalSearchRequest request) throws ElasticSearchException {
        SearchContext context = createContext(request);
        activeContexts.put(context.id(), context);
        queryPhase.execute(context);
        return context.queryResult();
    }

    public QuerySearchResult executeQueryPhase(InternalScrollSearchRequest request) throws ElasticSearchException {
        SearchContext context = findContext(request.id());
        processScroll(request, context);
        queryPhase.execute(context);
        return context.queryResult();
    }

    public QuerySearchResult executeQueryPhase(QuerySearchRequest request) throws ElasticSearchException {
        SearchContext context = findContext(request.id());
        try {
            context.searcher().dfSource(new CachedDfSource(request.dfs(), context.similarityService().defaultSearchSimilarity()));
        } catch (IOException e) {
            throw new QueryPhaseExecutionException(context, "Failed to set aggregated df", e);
        }
        queryPhase.execute(context);
        return context.queryResult();
    }

    public QueryFetchSearchResult executeFetchPhase(InternalSearchRequest request) throws ElasticSearchException {
        SearchContext context = createContext(request);
        queryPhase.execute(context);
        shortcutDocIdsToLoad(context);
        fetchPhase.execute(context);
        if (context.scroll() != null) {
            activeContexts.put(context.id(), context);
        }
        return new QueryFetchSearchResult(context.queryResult(), context.fetchResult());
    }

    public QueryFetchSearchResult executeFetchPhase(QuerySearchRequest request) throws ElasticSearchException {
        SearchContext context = findContext(request.id());
        try {
            context.searcher().dfSource(new CachedDfSource(request.dfs(), context.similarityService().defaultSearchSimilarity()));
        } catch (IOException e) {
            throw new QueryPhaseExecutionException(context, "Failed to set aggregated df", e);
        }
        queryPhase.execute(context);
        shortcutDocIdsToLoad(context);
        fetchPhase.execute(context);
        if (context.scroll() != null) {
            activeContexts.put(context.id(), context);
        }
        return new QueryFetchSearchResult(context.queryResult(), context.fetchResult());
    }

    public QueryFetchSearchResult executeFetchPhase(InternalScrollSearchRequest request) throws ElasticSearchException {
        SearchContext context = findContext(request.id());
        processScroll(request, context);
        queryPhase.execute(context);
        shortcutDocIdsToLoad(context);
        fetchPhase.execute(context);
        if (context.scroll() == null) {
            freeContext(request.id());
        }
        return new QueryFetchSearchResult(context.queryResult(), context.fetchResult());
    }

    public FetchSearchResult executeFetchPhase(FetchSearchRequest request) throws ElasticSearchException {
        SearchContext context = findContext(request.id());
        context.docIdsToLoad(request.docIds());
        fetchPhase.execute(context);
        if (context.scroll() == null) {
            freeContext(request.id());
        }
        return context.fetchResult();
    }

    private SearchContext findContext(long id) throws SearchContextMissingException {
        SearchContext context = activeContexts.get(id);
        if (context == null) {
            throw new SearchContextMissingException(id);
        }
        // update the last access time of the context
        context.accessed(timerService.estimatedTimeInMillis());
        return context;
    }

    private SearchContext createContext(InternalSearchRequest request) throws ElasticSearchException {
        IndexService indexService = indicesService.indexServiceSafe(request.index());
        IndexShard indexShard = indexService.shardSafe(request.shardId());
        Engine.Searcher engineSearcher = indexShard.searcher();

        SearchShardTarget shardTarget = new SearchShardTarget(clusterService.state().nodes().localNodeId(), request.index(), request.shardId());

        SearchContext context = new SearchContext(idGenerator.incrementAndGet(), shardTarget, request.timeout(), request.types(), engineSearcher, indexService);

        context.scroll(request.scroll());

        parseSource(context, request.source());
        parseSource(context, request.extraSource());

        // if the from and size are still not set, default them
        if (context.from() == -1) {
            context.from(0);
        }
        if (context.size() == -1) {
            context.size(10);
        }

        // pre process
        dfsPhase.preProcess(context);
        queryPhase.preProcess(context);
        fetchPhase.preProcess(context);

        // compute the context keep alive
        TimeValue keepAlive = defaultKeepAlive;
        if (request.scroll() != null && request.scroll().keepAlive() != null) {
            keepAlive = request.scroll().keepAlive();
        }
        context.keepAlive(keepAlive);
        context.accessed(timerService.estimatedTimeInMillis());
        context.keepAliveTimeout(timerService.newTimeout(new KeepAliveTimerTask(context), keepAlive));

        return context;
    }

    private void freeContext(long id) {
        SearchContext context = activeContexts.remove(id);
        if (context == null) {
            return;
        }
        freeContext(context);
    }

    private void freeContext(SearchContext context) {
        context.release();
    }

    private void parseSource(SearchContext context, byte[] source) throws SearchParseException {
        // nothing to parse...
        if (source == null || source.length == 0) {
            return;
        }
        try {
            JsonParser jp = jsonFactory.createJsonParser(source);
            JsonToken token;
            while ((token = jp.nextToken()) != JsonToken.END_OBJECT) {
                if (token == JsonToken.FIELD_NAME) {
                    String fieldName = jp.getCurrentName();
                    jp.nextToken();
                    SearchParseElement element = elementParsers.get(fieldName);
                    if (element == null) {
                        throw new SearchParseException(context, "No parser for element [" + fieldName + "]");
                    }
                    element.parse(jp, context);
                } else if (token == null) {
                    break;
                }
            }
        } catch (Exception e) {
            throw new SearchParseException(context, "Failed to parse [" + Unicode.fromBytes(source) + "]", e);
        }
    }

    private static final int[] EMPTY_DOC_IDS = new int[0];

    private void shortcutDocIdsToLoad(SearchContext context) {
        TopDocs topDocs = context.queryResult().topDocs();
        if (topDocs.scoreDocs.length < context.from()) {
            // no more docs...
            context.docIdsToLoad(EMPTY_DOC_IDS);
            return;
        }
        int totalSize = context.from() + context.size();
        int[] docIdsToLoad = new int[context.size()];
        int counter = 0;
        for (int i = context.from(); i < totalSize; i++) {
            if (i < topDocs.scoreDocs.length) {
                docIdsToLoad[counter] = topDocs.scoreDocs[i].doc;
            } else {
                break;
            }
            counter++;
        }
        if (counter < context.size()) {
            docIdsToLoad = Arrays.copyOfRange(docIdsToLoad, 0, counter);
        }
        context.docIdsToLoad(docIdsToLoad);
    }

    private void processScroll(InternalScrollSearchRequest request, SearchContext context) {
        // process scroll
        context.from(context.from() + context.size());
        context.scroll(request.scroll());
        // update the context keep alive based on the new scroll value
        if (request.scroll() != null && request.scroll().keepAlive() != null) {
            context.keepAlive(request.scroll().keepAlive());
        }
    }

    private class KeepAliveTimerTask implements TimerTask {

        private final SearchContext context;

        private KeepAliveTimerTask(SearchContext context) {
            this.context = context;
        }

        @Override public void run(Timeout timeout) throws Exception {
            if (timeout.isCancelled()) {
                return;
            }
            long currentTime = timerService.estimatedTimeInMillis();
            long nextDelay = context.keepAlive().millis() - (currentTime - context.lastAccessTime());
            if (nextDelay <= 0) {
                // Time out, free the context (and remove it from the active context)
                freeContext(context.id());
            } else {
                // Read occurred before the timeout - set a new timeout with shorter delay.
                context.keepAliveTimeout(timerService.newTimeout(this, nextDelay, TimeUnit.MILLISECONDS));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1464.java