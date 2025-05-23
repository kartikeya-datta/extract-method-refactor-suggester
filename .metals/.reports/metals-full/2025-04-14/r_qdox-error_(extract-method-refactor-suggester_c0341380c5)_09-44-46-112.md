error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3963.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3963.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3963.java
text:
```scala
t@@his.bigArrays = bigArrays.withCircuitBreaking();

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
package org.elasticsearch.percolator;

import com.google.common.collect.ImmutableList;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.*;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.action.percolate.PercolateShardRequest;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.cache.recycler.CacheRecycler;
import org.elasticsearch.cache.recycler.PageCacheRecycler;
import org.elasticsearch.common.lease.Releasables;
import org.elasticsearch.common.text.StringText;
import org.elasticsearch.common.util.BigArrays;
import org.elasticsearch.index.analysis.AnalysisService;
import org.elasticsearch.index.cache.docset.DocSetCache;
import org.elasticsearch.index.cache.filter.FilterCache;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.fielddata.IndexFieldDataService;
import org.elasticsearch.index.fieldvisitor.JustSourceFieldsVisitor;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.index.mapper.FieldMappers;
import org.elasticsearch.index.mapper.MapperService;
import org.elasticsearch.index.mapper.ParsedDocument;
import org.elasticsearch.index.query.IndexQueryParserService;
import org.elasticsearch.index.query.ParsedFilter;
import org.elasticsearch.index.query.ParsedQuery;
import org.elasticsearch.index.service.IndexService;
import org.elasticsearch.index.shard.service.IndexShard;
import org.elasticsearch.index.similarity.SimilarityService;
import org.elasticsearch.script.ScriptService;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchShardTarget;
import org.elasticsearch.search.aggregations.SearchContextAggregations;
import org.elasticsearch.search.dfs.DfsSearchResult;
import org.elasticsearch.search.facet.SearchContextFacets;
import org.elasticsearch.search.fetch.FetchSearchResult;
import org.elasticsearch.search.fetch.FetchSubPhase;
import org.elasticsearch.search.fetch.fielddata.FieldDataFieldsContext;
import org.elasticsearch.search.fetch.partial.PartialFieldsContext;
import org.elasticsearch.search.fetch.script.ScriptFieldsContext;
import org.elasticsearch.search.fetch.source.FetchSourceContext;
import org.elasticsearch.search.highlight.SearchContextHighlight;
import org.elasticsearch.search.internal.*;
import org.elasticsearch.search.lookup.SearchLookup;
import org.elasticsearch.search.query.QuerySearchResult;
import org.elasticsearch.search.rescore.RescoreSearchContext;
import org.elasticsearch.search.scan.ScanContext;
import org.elasticsearch.search.suggest.SuggestionSearchContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 */
public class PercolateContext extends SearchContext {

    public boolean limit;
    private int size;
    public boolean doSort;
    public byte percolatorTypeId;
    private boolean trackScores;

    private final SearchShardTarget searchShardTarget;
    private final IndexService indexService;
    private final IndexFieldDataService fieldDataService;
    private final IndexShard indexShard;
    private final CacheRecycler cacheRecycler;
    private final PageCacheRecycler pageCacheRecycler;
    private final BigArrays bigArrays;
    private final ScriptService scriptService;
    private final ConcurrentMap<BytesRef, Query> percolateQueries;
    private final int numberOfShards;
    private String[] types;

    private Engine.Searcher docSearcher;
    private Engine.Searcher engineSearcher;
    private ContextIndexSearcher searcher;

    private SearchContextHighlight highlight;
    private SearchLookup searchLookup;
    private ParsedQuery parsedQuery;
    private Query query;
    private boolean queryRewritten;
    private Query percolateQuery;
    private FetchSubPhase.HitContext hitContext;
    private SearchContextFacets facets;
    private SearchContextAggregations aggregations;
    private QuerySearchResult querySearchResult;
    private Sort sort;

    public PercolateContext(PercolateShardRequest request, SearchShardTarget searchShardTarget, IndexShard indexShard,
                            IndexService indexService, CacheRecycler cacheRecycler, PageCacheRecycler pageCacheRecycler,
                            BigArrays bigArrays, ScriptService scriptService) {
        this.indexShard = indexShard;
        this.indexService = indexService;
        this.fieldDataService = indexService.fieldData();
        this.searchShardTarget = searchShardTarget;
        this.percolateQueries = indexShard.percolateRegistry().percolateQueries();
        this.types = new String[]{request.documentType()};
        this.cacheRecycler = cacheRecycler;
        this.pageCacheRecycler = pageCacheRecycler;
        this.bigArrays = bigArrays;
        this.querySearchResult = new QuerySearchResult(0, searchShardTarget);
        this.engineSearcher = indexShard.acquireSearcher("percolate");
        this.searcher = new ContextIndexSearcher(this, engineSearcher);
        this.scriptService = scriptService;
        this.numberOfShards = request.getNumberOfShards();
    }

    public IndexSearcher docSearcher() {
        return docSearcher.searcher();
    }

    public void initialize(Engine.Searcher docSearcher, ParsedDocument parsedDocument) {
        this.docSearcher = docSearcher;

        IndexReader indexReader = docSearcher.reader();
        AtomicReaderContext atomicReaderContext = indexReader.leaves().get(0);
        lookup().setNextReader(atomicReaderContext);
        lookup().setNextDocId(0);
        lookup().source().setNextSource(parsedDocument.source());

        Map<String, SearchHitField> fields = new HashMap<>();
        for (IndexableField field : parsedDocument.rootDoc().getFields()) {
            fields.put(field.name(), new InternalSearchHitField(field.name(), ImmutableList.of()));
        }
        hitContext().reset(
                new InternalSearchHit(0, "unknown", new StringText(parsedDocument.type()), fields),
                atomicReaderContext, 0, indexReader, 0, new JustSourceFieldsVisitor()
        );
    }

    public IndexShard indexShard() {
        return indexShard;
    }

    public IndexService indexService() {
        return indexService;
    }

    public ConcurrentMap<BytesRef, Query> percolateQueries() {
        return percolateQueries;
    }

    public Query percolateQuery() {
        return percolateQuery;
    }

    public void percolateQuery(Query percolateQuery) {
        this.percolateQuery = percolateQuery;
    }

    public FetchSubPhase.HitContext hitContext() {
        if (hitContext == null) {
            hitContext = new FetchSubPhase.HitContext();
        }
        return hitContext;
    }

    @Override
    public SearchContextHighlight highlight() {
        return highlight;
    }

    @Override
    public void highlight(SearchContextHighlight highlight) {
        if (highlight != null) {
            // Enforce highlighting by source, because MemoryIndex doesn't support stored fields.
            highlight.globalForceSource(true);
        }
        this.highlight = highlight;
    }

    @Override
    public SearchShardTarget shardTarget() {
        return searchShardTarget;
    }

    @Override
    public SearchLookup lookup() {
        if (searchLookup == null) {
            searchLookup = new SearchLookup(mapperService(), fieldData(), types);
        }
        return searchLookup;
    }

    @Override
    protected void doClose() {
        Releasables.close(engineSearcher, docSearcher);
    }

    @Override
    public MapperService mapperService() {
        return indexService.mapperService();
    }

    @Override
    public SearchContext parsedQuery(ParsedQuery query) {
        this.parsedQuery = query;
        this.query = query.query();
        this.queryRewritten = false;
        return this;
    }

    @Override
    public ParsedQuery parsedQuery() {
        return parsedQuery;
    }

    @Override
    public Query query() {
        return query;
    }

    @Override
    public boolean queryRewritten() {
        return queryRewritten;
    }

    @Override
    public SearchContext updateRewriteQuery(Query rewriteQuery) {
        queryRewritten = true;
        query = rewriteQuery;
        return this;
    }

    @Override
    public String[] types() {
        return types;
    }

    public void types(String[] types) {
        this.types = types;
        searchLookup = new SearchLookup(mapperService(), fieldData(), types);
    }

    @Override
    public IndexFieldDataService fieldData() {
        return fieldDataService;
    }

    @Override
    public SearchContextAggregations aggregations() {
        return aggregations;
    }

    @Override
    public SearchContext aggregations(SearchContextAggregations aggregations) {
        this.aggregations = aggregations;
        return this;
    }

    @Override
    public SearchContextFacets facets() {
        return facets;
    }

    @Override
    public SearchContext facets(SearchContextFacets facets) {
        this.facets = facets;
        return this;
    }

    // Unused:
    @Override
    public void preProcess() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Filter searchFilter(String[] types) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long id() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String source() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ShardSearchRequest request() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SearchType searchType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SearchContext searchType(SearchType searchType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int numberOfShards() {
        return numberOfShards;
    }

    @Override
    public boolean hasTypes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public float queryBoost() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SearchContext queryBoost(float queryBoost) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long nowInMillis() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Scroll scroll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SearchContext scroll(Scroll scroll) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SuggestionSearchContext suggest() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void suggest(SuggestionSearchContext suggest) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<RescoreSearchContext> rescore() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addRescore(RescoreSearchContext rescore) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasFieldDataFields() {
        throw new UnsupportedOperationException();
    }

    @Override
    public FieldDataFieldsContext fieldDataFields() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasScriptFields() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ScriptFieldsContext scriptFields() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasPartialFields() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PartialFieldsContext partialFields() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean sourceRequested() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasFetchSourceContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public FetchSourceContext fetchSourceContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SearchContext fetchSourceContext(FetchSourceContext fetchSourceContext) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ContextIndexSearcher searcher() {
        return searcher;
    }

    @Override
    public AnalysisService analysisService() {
        return indexService.analysisService();
    }

    @Override
    public IndexQueryParserService queryParserService() {
        return indexService.queryParserService();
    }

    @Override
    public SimilarityService similarityService() {
        return indexService.similarityService();
    }

    @Override
    public ScriptService scriptService() {
        return scriptService;
    }

    @Override
    public CacheRecycler cacheRecycler() {
        return cacheRecycler;
    }

    @Override
    public PageCacheRecycler pageCacheRecycler() {
        return pageCacheRecycler;
    }

    @Override
    public BigArrays bigArrays() {
        return bigArrays;
    }

    @Override
    public FilterCache filterCache() {
        return indexService.cache().filter();
    }

    @Override
    public DocSetCache docSetCache() {
        return indexService.cache().docSet();
    }

    @Override
    public long timeoutInMillis() {
        return -1;
    }

    @Override
    public void timeoutInMillis(long timeoutInMillis) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int terminateAfter() {
        return DEFAULT_TERMINATE_AFTER;
    }

    @Override
    public void terminateAfter(int terminateAfter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SearchContext minimumScore(float minimumScore) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Float minimumScore() {
        return null;
    }

    @Override
    public SearchContext sort(Sort sort) {
        this.sort = sort;
        return this;
    }

    @Override
    public Sort sort() {
        return sort;
    }

    @Override
    public SearchContext trackScores(boolean trackScores) {
        this.trackScores = trackScores;
        return this;
    }

    @Override
    public boolean trackScores() {
        return trackScores;
    }

    @Override
    public SearchContext parsedPostFilter(ParsedFilter postFilter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ParsedFilter parsedPostFilter() {
        return null;
    }

    @Override
    public Filter aliasFilter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int from() {
        return 0;
    }

    @Override
    public SearchContext from(int from) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public SearchContext size(int size) {
        this.size = size;
        this.limit = true;
        return this;
    }

    @Override
    public boolean hasFieldNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> fieldNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void emptyFieldNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean explain() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void explain(boolean explain) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> groupStats() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void groupStats(List<String> groupStats) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean version() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void version(boolean version) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int[] docIdsToLoad() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int docIdsToLoadFrom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int docIdsToLoadSize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SearchContext docIdsToLoad(int[] docIdsToLoad, int docsIdsToLoadFrom, int docsIdsToLoadSize) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void accessed(long accessTime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long lastAccessTime() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long keepAlive() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void keepAlive(long keepAlive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void lastEmittedDoc(ScoreDoc doc) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ScoreDoc lastEmittedDoc() {
        throw new UnsupportedOperationException();
    }

    @Override
    public DfsSearchResult dfsResult() {
        throw new UnsupportedOperationException();
    }

    @Override
    public QuerySearchResult queryResult() {
        return querySearchResult;
    }

    @Override
    public FetchSearchResult fetchResult() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ScanContext scanContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MapperService.SmartNameFieldMappers smartFieldMappers(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FieldMappers smartNameFieldMappers(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FieldMapper smartNameFieldMapper(String name) {
        return mapperService().smartNameFieldMapper(name, types);
    }

    @Override
    public MapperService.SmartNameObjectMapper smartNameObjectMapper(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean useSlowScroll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SearchContext useSlowScroll(boolean useSlowScroll) {
        throw new UnsupportedOperationException();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3963.java