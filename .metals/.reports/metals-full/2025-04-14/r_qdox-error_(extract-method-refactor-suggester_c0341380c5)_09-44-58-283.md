error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6655.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6655.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6655.java
text:
```scala
t@@able.addCell("primaries.completion.size", "alias:pcs,primariesCompletionSize;default:false;text-align:right;desc:size of completion");

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

package org.elasticsearch.rest.action.cat;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.cluster.health.ClusterIndexHealth;
import org.elasticsearch.action.admin.cluster.state.ClusterStateRequest;
import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.action.admin.indices.stats.IndexStats;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.Table;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.*;
import org.elasticsearch.rest.action.support.RestTable;

import java.io.IOException;
import java.util.Locale;

import static org.elasticsearch.rest.RestRequest.Method.GET;

public class RestIndicesAction extends AbstractCatAction {

    @Inject
    public RestIndicesAction(Settings settings, Client client, RestController controller) {
        super(settings, client);
        controller.registerHandler(GET, "/_cat/indices", this);
        controller.registerHandler(GET, "/_cat/indices/{index}", this);
    }

    @Override
    void documentation(StringBuilder sb) {
        sb.append("/_cat/indices\n");
        sb.append("/_cat/indices/{index}\n");
    }

    @Override
    public void doRequest(final RestRequest request, final RestChannel channel) {
        final String[] indices = Strings.splitStringByCommaToArray(request.param("index"));
        final ClusterStateRequest clusterStateRequest = new ClusterStateRequest();
        clusterStateRequest.filteredIndices(indices);
        clusterStateRequest.local(request.paramAsBoolean("local", clusterStateRequest.local()));
        clusterStateRequest.masterNodeTimeout(request.paramAsTime("master_timeout", clusterStateRequest.masterNodeTimeout()));

        client.admin().cluster().state(clusterStateRequest, new ActionListener<ClusterStateResponse>() {
            @Override
            public void onResponse(final ClusterStateResponse clusterStateResponse) {
                final String[] concreteIndices = clusterStateResponse.getState().metaData().concreteIndicesIgnoreMissing(indices);
                ClusterHealthRequest clusterHealthRequest = Requests.clusterHealthRequest(concreteIndices);
                clusterHealthRequest.local(request.paramAsBoolean("local", clusterHealthRequest.local()));
                client.admin().cluster().health(clusterHealthRequest, new ActionListener<ClusterHealthResponse>() {
                    @Override
                    public void onResponse(final ClusterHealthResponse clusterHealthResponse) {
                        IndicesStatsRequest indicesStatsRequest = new IndicesStatsRequest();
                        indicesStatsRequest.all();
                        client.admin().indices().stats(indicesStatsRequest, new ActionListener<IndicesStatsResponse>() {
                            @Override
                            public void onResponse(IndicesStatsResponse indicesStatsResponse) {
                                try {
                                    Table tab = buildTable(request, concreteIndices, clusterHealthResponse, indicesStatsResponse);
                                    channel.sendResponse(RestTable.buildResponse(tab, request, channel));
                                } catch (Throwable e) {
                                    onFailure(e);
                                }
                            }

                            @Override
                            public void onFailure(Throwable e) {
                                try {
                                    channel.sendResponse(new XContentThrowableRestResponse(request, e));
                                } catch (IOException e1) {
                                    logger.error("Failed to send failure response", e1);
                                }
                            }
                        });

                    }

                    @Override
                    public void onFailure(Throwable e) {
                        try {
                            channel.sendResponse(new XContentThrowableRestResponse(request, e));
                        } catch (IOException e1) {
                            logger.error("Failed to send failure response", e1);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable e) {
                try {
                    channel.sendResponse(new XContentThrowableRestResponse(request, e));
                } catch (IOException e1) {
                    logger.error("Failed to send failure response", e1);
                }
            }
        });
    }

    @Override
    Table getTableWithHeader(final RestRequest request) {
        Table table = new Table();
        table.startHeaders();
        table.addCell("health", "desc:current health status");
        table.addCell("index", "desc:index name");
        table.addCell("pri", "text-align:right;desc:number of primary shards");
        table.addCell("rep", "text-align:right;desc:number of replica shards");
        table.addCell("docs.count", "text-align:right;desc:available docs");
        table.addCell("docs.deleted", "text-align:right;desc:deleted docs");
        table.addCell("primaries.store.size", "text-align:right;desc:store size of primaries");
        table.addCell("total.store.size", "text-align:right;desc:store size of primaries & replicas");

        table.addCell("primaries.completion.size", "default:false;text-align:right;desc:size of completion");

        table.addCell("primaries.fielddata.memory_size", "default:false;text-align:right;desc:used fielddata cache");
        table.addCell("primaries.fielddata.evictions", "default:false;text-align:right;desc:fielddata evictions");

        table.addCell("primaries.filter_cache.memory_size", "default:false;text-align:right;desc:used filter cache");
        table.addCell("primaries.filter_cache.evictions", "default:false;text-align:right;desc:filter cache evictions");

        table.addCell("primaries.flush.total", "default:false;text-align:right;desc:number of flushes");
        table.addCell("primaries.flush.total_time", "default:false;text-align:right;desc:time spent in flush");

        table.addCell("primaries.get.current", "default:false;text-align:right;desc:number of current get ops");
        table.addCell("primaries.get.time", "default:false;text-align:right;desc:time spent in get");
        table.addCell("primaries.get.total", "default:false;text-align:right;desc:number of get ops");
        table.addCell("primaries.get.exists_time", "default:false;text-align:right;desc:time spent in successful gets");
        table.addCell("primaries.get.exists_total", "default:false;text-align:right;desc:number of successful gets");
        table.addCell("primaries.get.missing_time", "default:false;text-align:right;desc:time spent in failed gets");
        table.addCell("primaries.get.missing_total", "default:false;text-align:right;desc:number of failed gets");

        table.addCell("primaries.id_cache.memory_size", "default:false;text-align:right;desc:used id cache");

        table.addCell("primaries.indexing.delete_current", "default:false;text-align:right;desc:number of current deletions");
        table.addCell("primaries.indexing.delete_time", "default:false;text-align:right;desc:time spent in deletions");
        table.addCell("primaries.indexing.delete_total", "default:false;text-align:right;desc:number of delete ops");
        table.addCell("primaries.indexing.index_current", "default:false;text-align:right;desc:number of current indexing ops");
        table.addCell("primaries.indexing.index_time", "default:false;text-align:right;desc:time spent in indexing");
        table.addCell("primaries.indexing.index_total", "default:false;text-align:right;desc:number of indexing ops");

        table.addCell("primaries.merges.current", "default:false;text-align:right;desc:number of current merges");
        table.addCell("primaries.merges.current_docs", "default:false;text-align:right;desc:number of current merging docs");
        table.addCell("primaries.merges.current_size", "default:false;text-align:right;desc:size of current merges");
        table.addCell("primaries.merges.total", "default:false;text-align:right;desc:number of completed merge ops");
        table.addCell("primaries.merges.total_docs", "default:false;text-align:right;desc:docs merged");
        table.addCell("primaries.merges.total_size", "default:false;text-align:right;desc:size merged");
        table.addCell("primaries.merges.total_time", "default:false;text-align:right;desc:time spent in merges");

        table.addCell("primaries.percolate.current", "default:false;text-align:right;desc:number of current percolations");
        table.addCell("primaries.percolate.memory_size", "default:false;text-align:right;desc:memory used by percolations");
        table.addCell("primaries.percolate.queries", "default:false;text-align:right;desc:number of registered percolation queries");
        table.addCell("primaries.percolate.time", "default:false;text-align:right;desc:time spent percolating");
        table.addCell("primaries.percolate.total", "default:false;text-align:right;desc:total percolations");

        table.addCell("primaries.refresh.total", "default:false;text-align:right;desc:total refreshes");
        table.addCell("primaries.refresh.time", "default:false;text-align:right;desc:time spent in refreshes");

        table.addCell("primaries.search.fetch_current", "default:false;text-align:right;desc:current fetch phase ops");
        table.addCell("primaries.search.fetch_time", "default:false;text-align:right;desc:time spent in fetch phase");
        table.addCell("primaries.search.fetch_total", "default:false;text-align:right;desc:total fetch ops");
        table.addCell("primaries.search.open_contexts", "default:false;text-align:right;desc:open search contexts");
        table.addCell("primaries.search.query_current", "default:false;text-align:right;desc:current query phase ops");
        table.addCell("primaries.search.query_time", "default:false;text-align:right;desc:time spent in query phase");
        table.addCell("primaries.search.query_total", "default:false;text-align:right;desc:total query phase ops");

        table.addCell("primaries.segments.count", "default:false;text-align:right;desc:number of segments");
        table.addCell("primaries.segments.memory", "default:false;text-align:right;desc:memory used by segments");

        table.addCell("primaries.warmer.current", "default:false;text-align:right;desc:current warmer ops");
        table.addCell("primaries.warmer.total", "default:false;text-align:right;desc:total warmer ops");
        table.addCell("primaries.warmer.total_time", "default:false;text-align:right;desc:time spent in warmers");

        table.endHeaders();
        return table;
    }

    private Table buildTable(RestRequest request, String[] indices, ClusterHealthResponse health, IndicesStatsResponse stats) {
        Table table = getTableWithHeader(request);

        for (String index : indices) {
            ClusterIndexHealth indexHealth = health.getIndices().get(index);
            IndexStats indexStats = stats.getIndices().get(index);

            table.startRow();
            table.addCell(indexHealth == null ? "red*" : indexHealth.getStatus().toString().toLowerCase(Locale.getDefault()));
            table.addCell(index);
            table.addCell(indexHealth == null ? null : indexHealth.getNumberOfShards());
            table.addCell(indexHealth == null ? null : indexHealth.getNumberOfReplicas());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getDocs().getCount());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getDocs().getDeleted());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getStore().size());
            table.addCell(indexStats == null ? null : indexStats.getTotal().getStore().size());

            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getCompletion().getSize());

            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getFieldData().getMemorySize());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getFieldData().getEvictions());

            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getFilterCache().getMemorySize());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getFilterCache().getEvictions());

            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getFlush().getTotal());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getFlush().getTotalTime());

            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getGet().current());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getGet().getTime());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getGet().getCount());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getGet().getExistsTime());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getGet().getExistsCount());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getGet().getMissingTime());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getGet().getMissingCount());

            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getIdCache().getMemorySize());

            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getIndexing().getTotal().getDeleteCurrent());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getIndexing().getTotal().getDeleteTime());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getIndexing().getTotal().getDeleteCount());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getIndexing().getTotal().getIndexCurrent());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getIndexing().getTotal().getIndexTime());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getIndexing().getTotal().getIndexCount());

            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getMerge().getCurrent());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getMerge().getCurrentNumDocs());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getMerge().getCurrentSize());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getMerge().getTotal());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getMerge().getTotalNumDocs());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getMerge().getTotalSize());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getMerge().getTotalTime());

            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getPercolate().getCurrent());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getPercolate().getMemorySize());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getPercolate().getNumQueries());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getPercolate().getTime());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getPercolate().getCount());

            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getRefresh().getTotal());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getRefresh().getTotalTime());

            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getSearch().getTotal().getFetchCurrent());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getSearch().getTotal().getFetchTime());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getSearch().getTotal().getFetchCount());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getSearch().getOpenContexts());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getSearch().getTotal().getQueryCurrent());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getSearch().getTotal().getQueryTime());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getSearch().getTotal().getQueryCount());

            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getSegments().getCount());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getSegments().getMemory());

            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getWarmer().current());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getWarmer().total());
            table.addCell(indexStats == null ? null : indexStats.getPrimaries().getWarmer().totalTime());

            table.endRow();
        }

        return table;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6655.java