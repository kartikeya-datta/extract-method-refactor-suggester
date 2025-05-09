error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7054.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7054.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7054.java
text:
```scala
C@@lusterBlockException blockException = checkBlock(request, clusterService.state());

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

package org.elasticsearch.action.support.master;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.support.BaseAction;
import org.elasticsearch.cluster.ClusterChangedEvent;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.TimeoutClusterStateListener;
import org.elasticsearch.cluster.block.ClusterBlockException;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.discovery.MasterNotDiscoveredException;
import org.elasticsearch.node.NodeClosedException;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.*;

/**
 * A base class for operations that needs to be performed on the master node.
 *
 * @author kimchy (shay.banon)
 */
public abstract class TransportMasterNodeOperationAction<Request extends MasterNodeOperationRequest, Response extends ActionResponse> extends BaseAction<Request, Response> {

    protected final TransportService transportService;

    protected final ClusterService clusterService;

    final String transportAction;
    final String executor;

    protected TransportMasterNodeOperationAction(Settings settings, TransportService transportService, ClusterService clusterService, ThreadPool threadPool) {
        super(settings, threadPool);
        this.transportService = transportService;
        this.clusterService = clusterService;

        this.transportAction = transportAction();
        this.executor = executor();

        transportService.registerHandler(transportAction, new TransportHandler());
    }

    protected abstract String transportAction();

    protected abstract String executor();

    protected abstract Request newRequest();

    protected abstract Response newResponse();

    protected abstract Response masterOperation(Request request, ClusterState state) throws ElasticSearchException;

    protected boolean localExecute(Request request) {
        return false;
    }

    protected ClusterBlockException checkBlock(Request request, ClusterState state) {
        return null;
    }

    protected void processBeforeDelegationToMaster(Request request, ClusterState state) {

    }

    @Override protected void doExecute(final Request request, final ActionListener<Response> listener) {
        innerExecute(request, listener, false);
    }

    private void innerExecute(final Request request, final ActionListener<Response> listener, final boolean retrying) {
        final ClusterState clusterState = clusterService.state();
        final DiscoveryNodes nodes = clusterState.nodes();
        if (nodes.localNodeMaster() || localExecute(request)) {
            // check for block, if blocked, retry, else, execute locally
            final ClusterBlockException blockException = checkBlock(request, clusterState);
            if (blockException != null) {
                if (!blockException.retryable()) {
                    listener.onFailure(blockException);
                    return;
                }
                clusterService.add(request.masterNodeTimeout(), new TimeoutClusterStateListener() {
                    @Override public void postAdded() {
                        ClusterBlockException blockException = checkBlock(request, clusterState);
                        if (blockException == null || !blockException.retryable()) {
                            clusterService.remove(this);
                            innerExecute(request, listener, false);
                        }
                    }

                    @Override public void onClose() {
                        clusterService.remove(this);
                        listener.onFailure(blockException);
                    }

                    @Override public void onTimeout(TimeValue timeout) {
                        clusterService.remove(this);
                        listener.onFailure(blockException);
                    }

                    @Override public void clusterChanged(ClusterChangedEvent event) {
                        ClusterBlockException blockException = checkBlock(request, event.state());
                        if (blockException == null || !blockException.retryable()) {
                            clusterService.remove(this);
                            innerExecute(request, listener, false);
                        }
                    }
                });
            } else {
                threadPool.executor(executor).execute(new Runnable() {
                    @Override public void run() {
                        try {
                            Response response = masterOperation(request, clusterState);
                            listener.onResponse(response);
                        } catch (Exception e) {
                            listener.onFailure(e);
                        }
                    }
                });
            }
        } else {
            if (nodes.masterNode() == null) {
                if (retrying) {
                    listener.onFailure(new MasterNotDiscoveredException());
                } else {
                    clusterService.add(request.masterNodeTimeout(), new TimeoutClusterStateListener() {
                        @Override public void postAdded() {
                            ClusterState clusterStateV2 = clusterService.state();
                            if (clusterStateV2.nodes().masterNodeId() != null) {
                                // now we have a master, try and execute it...
                                clusterService.remove(this);
                                innerExecute(request, listener, true);
                            }
                        }

                        @Override public void onClose() {
                            clusterService.remove(this);
                            listener.onFailure(new NodeClosedException(nodes.localNode()));
                        }

                        @Override public void onTimeout(TimeValue timeout) {
                            clusterService.remove(this);
                            listener.onFailure(new MasterNotDiscoveredException());
                        }

                        @Override public void clusterChanged(ClusterChangedEvent event) {
                            if (event.nodesDelta().masterNodeChanged()) {
                                clusterService.remove(this);
                                innerExecute(request, listener, true);
                            }
                        }
                    });
                }
                return;
            }
            processBeforeDelegationToMaster(request, clusterState);
            transportService.sendRequest(nodes.masterNode(), transportAction, request, new BaseTransportResponseHandler<Response>() {
                @Override public Response newInstance() {
                    return newResponse();
                }

                @Override public void handleResponse(Response response) {
                    listener.onResponse(response);
                }

                @Override public String executor() {
                    return ThreadPool.Names.SAME;
                }

                @Override public void handleException(final TransportException exp) {
                    if (exp.unwrapCause() instanceof ConnectTransportException) {
                        // we want to retry here a bit to see if a new master is elected
                        clusterService.add(request.masterNodeTimeout(), new TimeoutClusterStateListener() {
                            @Override public void postAdded() {
                                ClusterState clusterStateV2 = clusterService.state();
                                if (!clusterState.nodes().masterNodeId().equals(clusterStateV2.nodes().masterNodeId())) {
                                    // master changes while adding the listener, try here
                                    clusterService.remove(this);
                                    innerExecute(request, listener, false);
                                }
                            }

                            @Override public void onClose() {
                                clusterService.remove(this);
                                listener.onFailure(new NodeClosedException(nodes.localNode()));
                            }

                            @Override public void onTimeout(TimeValue timeout) {
                                clusterService.remove(this);
                                listener.onFailure(new MasterNotDiscoveredException());
                            }

                            @Override public void clusterChanged(ClusterChangedEvent event) {
                                if (event.nodesDelta().masterNodeChanged()) {
                                    clusterService.remove(this);
                                    innerExecute(request, listener, false);
                                }
                            }
                        });
                    } else {
                        listener.onFailure(exp);
                    }
                }
            });
        }
    }

    private class TransportHandler extends BaseTransportRequestHandler<Request> {

        @Override public Request newInstance() {
            return newRequest();
        }

        @Override public String executor() {
            return ThreadPool.Names.SAME;
        }

        @Override public void messageReceived(final Request request, final TransportChannel channel) throws Exception {
            // we just send back a response, no need to fork a listener
            request.listenerThreaded(false);
            execute(request, new ActionListener<Response>() {
                @Override public void onResponse(Response response) {
                    try {
                        channel.sendResponse(response);
                    } catch (Exception e) {
                        onFailure(e);
                    }
                }

                @Override public void onFailure(Throwable e) {
                    try {
                        channel.sendResponse(e);
                    } catch (Exception e1) {
                        logger.warn("Failed to send response", e1);
                    }
                }
            });
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7054.java