error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8082.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8082.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8082.java
text:
```scala
i@@ndicesService.removeIndex(index, "created for alias processing");

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

package org.elasticsearch.cluster.metadata;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.ProcessedClusterStateUpdateTask;
import org.elasticsearch.cluster.action.index.NodeAliasesUpdatedAction;
import org.elasticsearch.common.Priority;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.query.IndexQueryParserService;
import org.elasticsearch.index.service.IndexService;
import org.elasticsearch.indices.IndexMissingException;
import org.elasticsearch.indices.IndicesService;
import org.elasticsearch.indices.InvalidAliasNameException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.elasticsearch.cluster.ClusterState.newClusterStateBuilder;
import static org.elasticsearch.cluster.metadata.IndexMetaData.newIndexMetaDataBuilder;
import static org.elasticsearch.cluster.metadata.MetaData.newMetaDataBuilder;

/**
 *
 */
public class MetaDataIndexAliasesService extends AbstractComponent {

    private final ClusterService clusterService;

    private final IndicesService indicesService;

    private final NodeAliasesUpdatedAction aliasOperationPerformedAction;

    @Inject
    public MetaDataIndexAliasesService(Settings settings, ClusterService clusterService, IndicesService indicesService, NodeAliasesUpdatedAction aliasOperationPerformedAction) {
        super(settings);
        this.clusterService = clusterService;
        this.indicesService = indicesService;
        this.aliasOperationPerformedAction = aliasOperationPerformedAction;
    }

    public void indicesAliases(final Request request, final Listener listener) {
        clusterService.submitStateUpdateTask("index-aliases", Priority.URGENT, new ProcessedClusterStateUpdateTask() {
            @Override
            public ClusterState execute(ClusterState currentState) {

                for (AliasAction aliasAction : request.actions) {
                    if (!currentState.metaData().hasIndex(aliasAction.index())) {
                        listener.onFailure(new IndexMissingException(new Index(aliasAction.index())));
                        return currentState;
                    }
                    if (currentState.metaData().hasIndex(aliasAction.alias())) {
                        listener.onFailure(new InvalidAliasNameException(new Index(aliasAction.index()), aliasAction.alias(), "an index exists with the same name as the alias"));
                        return currentState;
                    }
                    if (aliasAction.indexRouting() != null && aliasAction.indexRouting().indexOf(',') != -1) {
                        listener.onFailure(new ElasticSearchIllegalArgumentException("alias [" + aliasAction.alias() + "] has several routing values associated with it"));
                        return currentState;
                    }
                }

                List<String> indicesToClose = Lists.newArrayList();
                Map<String, IndexService> indices = Maps.newHashMap();
                try {
                    boolean changed = false;
                    MetaData.Builder builder = newMetaDataBuilder().metaData(currentState.metaData());
                    for (AliasAction aliasAction : request.actions) {
                        IndexMetaData indexMetaData = builder.get(aliasAction.index());
                        if (indexMetaData == null) {
                            throw new IndexMissingException(new Index(aliasAction.index()));
                        }
                        IndexMetaData.Builder indexMetaDataBuilder = newIndexMetaDataBuilder(indexMetaData);
                        if (aliasAction.actionType() == AliasAction.Type.ADD) {
                            String filter = aliasAction.filter();
                            if (Strings.hasLength(filter)) {
                                // parse the filter, in order to validate it
                                IndexService indexService = indices.get(indexMetaData.index());
                                if (indexService == null) {
                                    indexService = indicesService.indexService(indexMetaData.index());
                                    if (indexService == null) {
                                        // temporarily create the index so we have can parse the filter
                                        try {
                                            indexService = indicesService.createIndex(indexMetaData.index(), indexMetaData.settings(), currentState.nodes().localNode().id());
                                        } catch (Exception e) {
                                            logger.warn("[{}] failed to temporary create in order to apply alias action", e, indexMetaData.index());
                                            continue;
                                        }
                                        indicesToClose.add(indexMetaData.index());
                                    }
                                    indices.put(indexMetaData.index(), indexService);
                                }

                                // now, parse the filter
                                IndexQueryParserService indexQueryParser = indexService.queryParserService();
                                try {
                                    XContentParser parser = XContentFactory.xContent(filter).createParser(filter);
                                    try {
                                        indexQueryParser.parseInnerFilter(parser);
                                    } finally {
                                        parser.close();
                                    }
                                } catch (Exception e) {
                                    listener.onFailure(new ElasticSearchIllegalArgumentException("failed to parse filter for [" + aliasAction.alias() + "]", e));
                                    return currentState;
                                }
                            }
                            AliasMetaData newAliasMd = AliasMetaData.newAliasMetaDataBuilder(
                                    aliasAction.alias())
                                    .filter(filter)
                                    .indexRouting(aliasAction.indexRouting())
                                    .searchRouting(aliasAction.searchRouting())
                                    .build();
                            // Check if this alias already exists
                            AliasMetaData aliasMd = indexMetaData.aliases().get(aliasAction.alias());
                            if (aliasMd != null && aliasMd.equals(newAliasMd)) {
                                // It's the same alias - ignore it
                                continue;
                            }
                            indexMetaDataBuilder.putAlias(newAliasMd);
                        } else if (aliasAction.actionType() == AliasAction.Type.REMOVE) {
                            if (!indexMetaData.aliases().containsKey(aliasAction.alias())) {
                                // This alias doesn't exist - ignore
                                continue;
                            }
                            indexMetaDataBuilder.removerAlias(aliasAction.alias());
                        }
                        changed = true;
                        builder.put(indexMetaDataBuilder);
                    }

                    if (changed) {
                        ClusterState updatedState = newClusterStateBuilder().state(currentState).metaData(builder).build();
                        // even though changes happened, they resulted in 0 actual changes to metadata
                        // i.e. remove and add the same alias to the same index
                        if (updatedState.metaData().aliases().equals(currentState.metaData().aliases())) {
                            listener.onResponse(new Response(true));
                            return currentState;
                        }
                        // wait for responses from other nodes if needed
                        int responseCount = updatedState.nodes().size();
                        long version = updatedState.version() + 1;
                        logger.trace("waiting for [{}] notifications with version [{}]", responseCount, version);
                        aliasOperationPerformedAction.add(new CountDownListener(responseCount, listener, version), request.timeout);

                        return updatedState;
                    } else {
                        // Nothing to do
                        listener.onResponse(new Response(true));
                        return currentState;
                    }
                } finally {
                    for (String index : indicesToClose) {
                        indicesService.cleanIndex(index, "created for alias processing");
                    }
                }
            }

            @Override
            public void clusterStateProcessed(ClusterState clusterState) {
            }
        });
    }

    public static interface Listener {

        void onResponse(Response response);

        void onFailure(Throwable t);
    }

    public static class Request {

        final AliasAction[] actions;

        final TimeValue timeout;

        public Request(AliasAction[] actions, TimeValue timeout) {
            this.actions = actions;
            this.timeout = timeout;
        }
    }

    public static class Response {
        private final boolean acknowledged;

        public Response(boolean acknowledged) {
            this.acknowledged = acknowledged;
        }

        public boolean acknowledged() {
            return acknowledged;
        }
    }

    private class CountDownListener implements NodeAliasesUpdatedAction.Listener {

        private final AtomicBoolean notified = new AtomicBoolean();
        private final AtomicInteger countDown;
        private final Listener listener;
        private final long version;

        public CountDownListener(int countDown, Listener listener, long version) {
            this.countDown = new AtomicInteger(countDown);
            this.listener = listener;
            this.version = version;
        }

        @Override
        public void onAliasesUpdated(NodeAliasesUpdatedAction.NodeAliasesUpdatedResponse response) {
            if (version <= response.version()) {
                logger.trace("Received NodeAliasesUpdatedResponse with version [{}] from [{}]", response.version(), response.nodeId());
                if (countDown.decrementAndGet() == 0) {
                    aliasOperationPerformedAction.remove(this);
                    if (notified.compareAndSet(false, true)) {
                        listener.onResponse(new Response(true));
                    }
                }
            }
        }

        @Override
        public void onTimeout() {
            aliasOperationPerformedAction.remove(this);
            if (notified.compareAndSet(false, true)) {
                listener.onResponse(new Response(false));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8082.java