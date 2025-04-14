error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6965.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6965.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6965.java
text:
```scala
T@@erm uidTerm = new Term(UidFieldMapper.NAME, Uid.createUidAsBytes(request.type(), request.id()));

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

package org.elasticsearch.action.explain;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Explanation;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.support.single.shard.TransportShardSingleOperationAction;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.block.ClusterBlockException;
import org.elasticsearch.cluster.block.ClusterBlockLevel;
import org.elasticsearch.cluster.routing.ShardIterator;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.index.mapper.Uid;
import org.elasticsearch.index.mapper.internal.UidFieldMapper;
import org.elasticsearch.index.query.ParsedQuery;
import org.elasticsearch.index.service.IndexService;
import org.elasticsearch.index.shard.service.IndexShard;
import org.elasticsearch.indices.IndicesService;
import org.elasticsearch.script.ScriptService;
import org.elasticsearch.search.internal.SearchContext;
import org.elasticsearch.search.internal.ShardSearchRequest;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

import java.io.IOException;

/**
 * Explain transport action. Computes the explain on the targeted shard.
 */
// TODO: AggregatedDfs. Currently the idf can be different then when executing a normal search with explain.
public class TransportExplainAction extends TransportShardSingleOperationAction<ExplainRequest, ExplainResponse> {

    private final IndicesService indicesService;

    private final ScriptService scriptService;

    @Inject
    public TransportExplainAction(Settings settings, ThreadPool threadPool, ClusterService clusterService,
                                  TransportService transportService, IndicesService indicesService,
                                  ScriptService scriptService) {
        super(settings, threadPool, clusterService, transportService);
        this.indicesService = indicesService;
        this.scriptService = scriptService;
    }

    protected String transportAction() {
        return ExplainAction.NAME;
    }

    protected String executor() {
        return ThreadPool.Names.GET; // Or use Names.SEARCH?
    }

    @Override
    protected void resolveRequest(ClusterState state, ExplainRequest request) {
        String concreteIndex = state.metaData().concreteIndex(request.index());
        request.filteringAlias(state.metaData().filteringAliases(concreteIndex, request.index()));
        request.index(state.metaData().concreteIndex(request.index()));
    }

    protected ExplainResponse shardOperation(ExplainRequest request, int shardId) throws ElasticSearchException {
        IndexService indexService = indicesService.indexService(request.index());
        IndexShard indexShard = indexService.shardSafe(shardId);
        Term uidTerm = new Term(UidFieldMapper.NAME, Uid.createUid(request.type(), request.id()));
        Engine.GetResult result = indexShard.get(new Engine.Get(false, uidTerm));
        if (!result.exists()) {
            return new ExplainResponse(false);
        }

        SearchContext context = new SearchContext(
                0,
                new ShardSearchRequest().types(new String[]{request.type()})
                        .filteringAliases(request.filteringAlias()),
                null, result.searcher(), indexService, indexShard,
                scriptService
        );
        SearchContext.setCurrent(context);

        try {
            context.parsedQuery(parseQuery(request, indexService));
            context.preProcess();
            int topLevelDocId = result.docIdAndVersion().docId + result.docIdAndVersion().reader.docBase;

            Explanation explanation = context.searcher().explain(context.query(), topLevelDocId);
            if (request.fields() != null) {
                if (request.fields().length == 1 && "_source".equals(request.fields()[0])) {
                    request.fields(null); // Load the _source field
                }
                // Advantage is that we're not opening a second searcher to retrieve the _source. Also
                // because we are working in the same searcher in engineGetResult we can be sure that a
                // doc isn't deleted between the initial get and this call.
                GetResult getResult = indexShard.getService().get(result, request.id(), request.type(), request.fields());
                return new ExplainResponse(true, explanation, getResult);
            } else {
                return new ExplainResponse(true, explanation);
            }
        } catch (IOException e) {
            throw new ElasticSearchException("Could not explain", e);
        } finally {
            context.release();
            SearchContext.removeCurrent();
        }
    }

    private ParsedQuery parseQuery(ExplainRequest request, IndexService indexService) {
        try {
            XContentParser parser = XContentHelper.createParser(request.source());
            for (XContentParser.Token token = parser.nextToken(); token != XContentParser.Token.END_OBJECT; token = parser.nextToken()) {
                if (token == XContentParser.Token.FIELD_NAME) {
                    String fieldName = parser.currentName();
                    if ("query".equals(fieldName)) {
                        return indexService.queryParserService().parse(parser);
                    } else if ("query_binary".equals(fieldName)) {
                        byte[] querySource = parser.binaryValue();
                        XContentParser qSourceParser = XContentFactory.xContent(querySource).createParser(querySource);
                        return indexService.queryParserService().parse(qSourceParser);
                    }
                }
            }
        } catch (Exception e) {
            throw new ElasticSearchException("Couldn't parse query from source.", e);
        }

        throw new ElasticSearchException("No query specified");
    }

    protected ExplainRequest newRequest() {
        return new ExplainRequest();
    }

    protected ExplainResponse newResponse() {
        return new ExplainResponse();
    }

    protected ClusterBlockException checkGlobalBlock(ClusterState state, ExplainRequest request) {
        return state.blocks().globalBlockedException(ClusterBlockLevel.READ);
    }

    protected ClusterBlockException checkRequestBlock(ClusterState state, ExplainRequest request) {
        return state.blocks().indexBlockedException(ClusterBlockLevel.READ, request.index());
    }

    protected ShardIterator shards(ClusterState state, ExplainRequest request) throws ElasticSearchException {
        return clusterService.operationRouting().getShards(
                clusterService.state(), request.index(), request.type(), request.id(), request.routing(), request.preference()
        );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6965.java