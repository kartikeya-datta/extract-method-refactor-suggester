error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3712.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3712.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3712.java
text:
```scala
i@@f (disableDeleteAllIndices && (request.indices() == null || request.indices().length == 0 || (request.indices().length == 1 && request.indices()[0].equals("_all")))) {

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

package org.elasticsearch.action.admin.indices.delete;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.mapping.delete.DeleteMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.delete.DeleteMappingResponse;
import org.elasticsearch.action.admin.indices.mapping.delete.TransportDeleteMappingAction;
import org.elasticsearch.action.support.master.TransportMasterNodeOperationAction;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.block.ClusterBlockException;
import org.elasticsearch.cluster.block.ClusterBlockLevel;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MetaDataDeleteIndexService;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.percolator.PercolatorService;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Delete index action.
 */
public class TransportDeleteIndexAction extends TransportMasterNodeOperationAction<DeleteIndexRequest, DeleteIndexResponse> {

    private final MetaDataDeleteIndexService deleteIndexService;

    private final TransportDeleteMappingAction deleteMappingAction;

    private final boolean disableDeleteAllIndices;

    @Inject
    public TransportDeleteIndexAction(Settings settings, TransportService transportService, ClusterService clusterService,
                                      ThreadPool threadPool, MetaDataDeleteIndexService deleteIndexService, TransportDeleteMappingAction deleteMappingAction) {
        super(settings, transportService, clusterService, threadPool);
        this.deleteIndexService = deleteIndexService;
        this.deleteMappingAction = deleteMappingAction;

        this.disableDeleteAllIndices = settings.getAsBoolean("action.disable_delete_all_indices", false);
    }

    @Override
    protected String executor() {
        return ThreadPool.Names.MANAGEMENT;
    }

    @Override
    protected String transportAction() {
        return DeleteIndexAction.NAME;
    }

    @Override
    protected DeleteIndexRequest newRequest() {
        return new DeleteIndexRequest();
    }

    @Override
    protected DeleteIndexResponse newResponse() {
        return new DeleteIndexResponse();
    }

    @Override
    protected void doExecute(DeleteIndexRequest request, ActionListener<DeleteIndexResponse> listener) {
        if (disableDeleteAllIndices && (request.indices() == null || request.indices().length == 0)) {
            throw new ElasticSearchIllegalArgumentException("deleting all indices is disabled");
        }
        request.indices(clusterService.state().metaData().concreteIndices(request.indices()));
        super.doExecute(request, listener);
    }

    @Override
    protected ClusterBlockException checkBlock(DeleteIndexRequest request, ClusterState state) {
        return state.blocks().indicesBlockedException(ClusterBlockLevel.METADATA, request.indices());
    }

    @Override
    protected DeleteIndexResponse masterOperation(DeleteIndexRequest request, final ClusterState state) throws ElasticSearchException {
        if (request.indices().length == 0) {
            return new DeleteIndexResponse(true);
        }
        final AtomicReference<DeleteIndexResponse> responseRef = new AtomicReference<DeleteIndexResponse>();
        final AtomicReference<Throwable> failureRef = new AtomicReference<Throwable>();
        final CountDownLatch latch = new CountDownLatch(request.indices().length);
        for (final String index : request.indices()) {
            deleteIndexService.deleteIndex(new MetaDataDeleteIndexService.Request(index).timeout(request.timeout()), new MetaDataDeleteIndexService.Listener() {
                @Override
                public void onResponse(MetaDataDeleteIndexService.Response response) {
                    responseRef.set(new DeleteIndexResponse(response.acknowledged()));
                    // YACK, but here we go: If this index is also percolated, make sure to delete all percolated queries from the _percolator index
                    IndexMetaData percolatorMetaData = state.metaData().index(PercolatorService.INDEX_NAME);
                    if (percolatorMetaData != null && percolatorMetaData.mappings().containsKey(index)) {
                        deleteMappingAction.execute(new DeleteMappingRequest(PercolatorService.INDEX_NAME).type(index), new ActionListener<DeleteMappingResponse>() {
                            @Override
                            public void onResponse(DeleteMappingResponse deleteMappingResponse) {
                                latch.countDown();
                            }

                            @Override
                            public void onFailure(Throwable e) {
                                latch.countDown();
                            }
                        });
                    } else {
                        latch.countDown();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    failureRef.set(t);
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            failureRef.set(e);
        }

        if (failureRef.get() != null) {
            if (failureRef.get() instanceof ElasticSearchException) {
                throw (ElasticSearchException) failureRef.get();
            } else {
                throw new ElasticSearchException(failureRef.get().getMessage(), failureRef.get());
            }
        }

        return responseRef.get();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3712.java