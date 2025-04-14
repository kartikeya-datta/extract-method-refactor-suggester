error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5654.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5654.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5654.java
text:
```scala
l@@ogger.error("failed to initial restart on service wrapper", e);

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

package org.elasticsearch.action.admin.cluster.node.restart;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.ElasticSearchIllegalStateException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.support.nodes.NodeOperationRequest;
import org.elasticsearch.action.support.nodes.TransportNodesOperationAction;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.node.Node;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReferenceArray;

import static com.google.common.collect.Lists.newArrayList;
import static org.elasticsearch.common.unit.TimeValue.readTimeValue;

/**
 *
 */
public class TransportNodesRestartAction extends TransportNodesOperationAction<NodesRestartRequest, NodesRestartResponse, TransportNodesRestartAction.NodeRestartRequest, NodesRestartResponse.NodeRestartResponse> {

    private final Node node;

    private final boolean disabled;

    private AtomicBoolean restartRequested = new AtomicBoolean();

    @Inject
    public TransportNodesRestartAction(Settings settings, ClusterName clusterName, ThreadPool threadPool,
                                       ClusterService clusterService, TransportService transportService,
                                       Node node) {
        super(settings, clusterName, threadPool, clusterService, transportService);
        this.node = node;
        disabled = componentSettings.getAsBoolean("disabled", false);
    }

    @Override
    protected void doExecute(NodesRestartRequest nodesRestartRequest, ActionListener<NodesRestartResponse> listener) {
        listener.onFailure(new ElasticSearchIllegalStateException("restart is disabled (for now) ...."));
    }

    @Override
    protected String executor() {
        return ThreadPool.Names.GENERIC;
    }

    @Override
    protected String transportAction() {
        return NodesRestartAction.NAME;
    }

    @Override
    protected NodesRestartResponse newResponse(NodesRestartRequest nodesShutdownRequest, AtomicReferenceArray responses) {
        final List<NodesRestartResponse.NodeRestartResponse> nodeRestartResponses = newArrayList();
        for (int i = 0; i < responses.length(); i++) {
            Object resp = responses.get(i);
            if (resp instanceof NodesRestartResponse.NodeRestartResponse) {
                nodeRestartResponses.add((NodesRestartResponse.NodeRestartResponse) resp);
            }
        }
        return new NodesRestartResponse(clusterName, nodeRestartResponses.toArray(new NodesRestartResponse.NodeRestartResponse[nodeRestartResponses.size()]));
    }

    @Override
    protected NodesRestartRequest newRequest() {
        return new NodesRestartRequest();
    }

    @Override
    protected NodeRestartRequest newNodeRequest() {
        return new NodeRestartRequest();
    }

    @Override
    protected NodeRestartRequest newNodeRequest(String nodeId, NodesRestartRequest request) {
        return new NodeRestartRequest(nodeId, request);
    }

    @Override
    protected NodesRestartResponse.NodeRestartResponse newNodeResponse() {
        return new NodesRestartResponse.NodeRestartResponse();
    }

    @Override
    protected NodesRestartResponse.NodeRestartResponse nodeOperation(NodeRestartRequest request) throws ElasticSearchException {
        if (disabled) {
            throw new ElasticSearchIllegalStateException("Restart is disabled");
        }
        if (!restartRequested.compareAndSet(false, true)) {
            return new NodesRestartResponse.NodeRestartResponse(clusterService.state().nodes().localNode());
        }
        logger.info("Restarting in [{}]", request.delay);
        threadPool.schedule(request.delay, ThreadPool.Names.GENERIC, new Runnable() {
            @Override
            public void run() {
                boolean restartWithWrapper = false;
                if (System.getProperty("elasticsearch-service") != null) {
                    try {
                        Class wrapperManager = settings.getClassLoader().loadClass("org.tanukisoftware.wrapper.WrapperManager");
                        logger.info("Initiating requested restart (using service)");
                        wrapperManager.getMethod("restartAndReturn").invoke(null);
                        restartWithWrapper = true;
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
                if (!restartWithWrapper) {
                    logger.info("Initiating requested restart");
                    try {
                        node.stop();
                        node.start();
                    } catch (Exception e) {
                        logger.warn("Failed to restart", e);
                    } finally {
                        restartRequested.set(false);
                    }
                }
            }
        });
        return new NodesRestartResponse.NodeRestartResponse(clusterService.state().nodes().localNode());
    }

    @Override
    protected boolean accumulateExceptions() {
        return false;
    }

    protected static class NodeRestartRequest extends NodeOperationRequest {

        TimeValue delay;

        private NodeRestartRequest() {
        }

        private NodeRestartRequest(String nodeId, NodesRestartRequest request) {
            super(request, nodeId);
            this.delay = request.delay;
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            super.readFrom(in);
            delay = readTimeValue(in);
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            super.writeTo(out);
            delay.writeTo(out);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5654.java