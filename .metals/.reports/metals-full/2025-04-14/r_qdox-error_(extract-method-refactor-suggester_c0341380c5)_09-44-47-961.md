error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7212.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7212.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7212.java
text:
```scala
L@@ist<IndexWarmersMetaData.Entry> entries = new ArrayList<>(warmers.entries().size() + 1);

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

package org.elasticsearch.action.admin.indices.warmer.put;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.TransportSearchAction;
import org.elasticsearch.action.support.master.TransportMasterNodeOperationAction;
import org.elasticsearch.cluster.AckedClusterStateUpdateTask;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.block.ClusterBlockException;
import org.elasticsearch.cluster.block.ClusterBlockLevel;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MetaData;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.Index;
import org.elasticsearch.indices.IndexMissingException;
import org.elasticsearch.search.warmer.IndexWarmersMetaData;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Put warmer action.
 */
public class TransportPutWarmerAction extends TransportMasterNodeOperationAction<PutWarmerRequest, PutWarmerResponse> {

    private final TransportSearchAction searchAction;

    @Inject
    public TransportPutWarmerAction(Settings settings, TransportService transportService, ClusterService clusterService, ThreadPool threadPool,
                                    TransportSearchAction searchAction) {
        super(settings, transportService, clusterService, threadPool);
        this.searchAction = searchAction;
    }

    @Override
    protected String executor() {
        return ThreadPool.Names.SAME;
    }

    @Override
    protected String transportAction() {
        return PutWarmerAction.NAME;
    }

    @Override
    protected PutWarmerRequest newRequest() {
        return new PutWarmerRequest();
    }

    @Override
    protected PutWarmerResponse newResponse() {
        return new PutWarmerResponse();
    }

    @Override
    protected ClusterBlockException checkBlock(PutWarmerRequest request, ClusterState state) {
        String[] concreteIndices = clusterService.state().metaData().concreteIndices(request.searchRequest().indices(), request.searchRequest().indicesOptions());
        return state.blocks().indicesBlockedException(ClusterBlockLevel.METADATA, concreteIndices);
    }

    @Override
    protected void masterOperation(final PutWarmerRequest request, final ClusterState state, final ActionListener<PutWarmerResponse> listener) throws ElasticsearchException {
        // first execute the search request, see that its ok...
        searchAction.execute(request.searchRequest(), new ActionListener<SearchResponse>() {
            @Override
            public void onResponse(SearchResponse searchResponse) {
                if (searchResponse.getFailedShards() > 0) {
                    listener.onFailure(new ElasticsearchException("search failed with failed shards: " + Arrays.toString(searchResponse.getShardFailures())));
                    return;
                }

                clusterService.submitStateUpdateTask("put_warmer [" + request.name() + "]", new AckedClusterStateUpdateTask() {

                    @Override
                    public boolean mustAck(DiscoveryNode discoveryNode) {
                        return true;
                    }

                    @Override
                    public void onAllNodesAcked(@Nullable Throwable t) {
                        listener.onResponse(new PutWarmerResponse(true));
                    }

                    @Override
                    public void onAckTimeout() {
                        listener.onResponse(new PutWarmerResponse(false));
                    }

                    @Override
                    public TimeValue ackTimeout() {
                        return request.timeout();
                    }

                    @Override
                    public TimeValue timeout() {
                        return request.masterNodeTimeout();
                    }

                    @Override
                    public void onFailure(String source, Throwable t) {
                        logger.debug("failed to put warmer [{}] on indices [{}]", t, request.name(), request.searchRequest().indices());
                        listener.onFailure(t);
                    }

                    @Override
                    public ClusterState execute(ClusterState currentState) {
                        MetaData metaData = currentState.metaData();
                        String[] concreteIndices = metaData.concreteIndices(request.searchRequest().indices(), request.searchRequest().indicesOptions());

                        BytesReference source = null;
                        if (request.searchRequest().source() != null && request.searchRequest().source().length() > 0) {
                            source = request.searchRequest().source();
                        } else if (request.searchRequest().extraSource() != null && request.searchRequest().extraSource().length() > 0) {
                            source = request.searchRequest().extraSource();
                        }

                        // now replace it on the metadata
                        MetaData.Builder mdBuilder = MetaData.builder(currentState.metaData());

                        for (String index : concreteIndices) {
                            IndexMetaData indexMetaData = metaData.index(index);
                            if (indexMetaData == null) {
                                throw new IndexMissingException(new Index(index));
                            }
                            IndexWarmersMetaData warmers = indexMetaData.custom(IndexWarmersMetaData.TYPE);
                            if (warmers == null) {
                                logger.info("[{}] putting warmer [{}]", index, request.name());
                                warmers = new IndexWarmersMetaData(new IndexWarmersMetaData.Entry(request.name(), request.searchRequest().types(), source));
                            } else {
                                boolean found = false;
                                List<IndexWarmersMetaData.Entry> entries = new ArrayList<IndexWarmersMetaData.Entry>(warmers.entries().size() + 1);
                                for (IndexWarmersMetaData.Entry entry : warmers.entries()) {
                                    if (entry.name().equals(request.name())) {
                                        found = true;
                                        entries.add(new IndexWarmersMetaData.Entry(request.name(), request.searchRequest().types(), source));
                                    } else {
                                        entries.add(entry);
                                    }
                                }
                                if (!found) {
                                    logger.info("[{}] put warmer [{}]", index, request.name());
                                    entries.add(new IndexWarmersMetaData.Entry(request.name(), request.searchRequest().types(), source));
                                } else {
                                    logger.info("[{}] update warmer [{}]", index, request.name());
                                }
                                warmers = new IndexWarmersMetaData(entries.toArray(new IndexWarmersMetaData.Entry[entries.size()]));
                            }
                            IndexMetaData.Builder indexBuilder = IndexMetaData.builder(indexMetaData).putCustom(IndexWarmersMetaData.TYPE, warmers);
                            mdBuilder.put(indexBuilder);
                        }

                        return ClusterState.builder(currentState).metaData(mdBuilder).build();
                    }

                    @Override
                    public void clusterStateProcessed(String source, ClusterState oldState, ClusterState newState) {

                    }
                });
            }

            @Override
            public void onFailure(Throwable e) {
                listener.onFailure(e);
            }
        });
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7212.java