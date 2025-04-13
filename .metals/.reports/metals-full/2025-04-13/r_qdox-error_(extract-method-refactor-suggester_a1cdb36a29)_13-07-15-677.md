error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9813.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9813.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9813.java
text:
```scala
g@@etRequest.realtime(restRequest.paramAsBoolean("realtime", null));

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
package org.elasticsearch.rest.action.percolate;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.percolate.PercolateRequest;
import org.elasticsearch.action.percolate.PercolateResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.broadcast.BroadcastOperationThreading;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.rest.*;
import org.elasticsearch.rest.action.support.RestActions;
import org.elasticsearch.rest.action.support.RestXContentBuilder;

import java.io.IOException;

import static org.elasticsearch.rest.RestRequest.Method.GET;
import static org.elasticsearch.rest.RestRequest.Method.POST;
import static org.elasticsearch.rest.RestStatus.OK;

/**
 *
 */
public class RestPercolateAction extends BaseRestHandler {

    @Inject
    public RestPercolateAction(Settings settings, Client client, RestController controller) {
        super(settings, client);
        controller.registerHandler(GET, "/{index}/{type}/_percolate", this);
        controller.registerHandler(POST, "/{index}/{type}/_percolate", this);

        RestPercolateExistingDocHandler existingDocHandler = new RestPercolateExistingDocHandler();
        controller.registerHandler(GET, "/{index}/{type}/{id}/_percolate", existingDocHandler);
        controller.registerHandler(POST, "/{index}/{type}/{id}/_percolate", existingDocHandler);

        RestCountPercolateDocHandler countHandler = new RestCountPercolateDocHandler();
        controller.registerHandler(GET, "/{index}/{type}/_percolate/count", countHandler);
        controller.registerHandler(POST, "/{index}/{type}/_percolate/count", countHandler);

        RestCountPercolateExistingDocHandler countExistingDocHandler = new RestCountPercolateExistingDocHandler();
        controller.registerHandler(GET, "/{index}/{type}/{id}/_percolate/count", countExistingDocHandler);
        controller.registerHandler(POST, "/{index}/{type}/{id}/_percolate/count", countExistingDocHandler);
    }

    void parseDocPercolate(PercolateRequest percolateRequest, RestRequest restRequest, RestChannel restChannel) {
        percolateRequest.indices(Strings.splitStringByCommaToArray(restRequest.param("index")));
        percolateRequest.documentType(restRequest.param("type"));
        percolateRequest.routing(restRequest.param("routing"));
        percolateRequest.preference(restRequest.param("preference"));
        percolateRequest.source(restRequest.content(), restRequest.contentUnsafe());

        percolateRequest.indicesOptions(IndicesOptions.fromRequest(restRequest, percolateRequest.indicesOptions()));
        executePercolate(percolateRequest, restRequest, restChannel);
    }

    void parseExistingDocPercolate(PercolateRequest percolateRequest, RestRequest restRequest, RestChannel restChannel) {
        String index = restRequest.param("index");
        String type = restRequest.param("type");
        percolateRequest.indices(Strings.splitStringByCommaToArray(restRequest.param("percolate_index", index)));
        percolateRequest.documentType(restRequest.param("percolate_type", type));

        GetRequest getRequest = new GetRequest(index, type,
                restRequest.param("id"));
        getRequest.routing(restRequest.param("routing"));
        getRequest.preference(restRequest.param("preference"));
        getRequest.refresh(restRequest.paramAsBoolean("refresh", getRequest.refresh()));
        getRequest.realtime(restRequest.paramAsBooleanOptional("realtime", null));
        getRequest.version(RestActions.parseVersion(restRequest));
        getRequest.versionType(VersionType.fromString(restRequest.param("version_type"), getRequest.versionType()));

        percolateRequest.getRequest(getRequest);
        percolateRequest.routing(restRequest.param("percolate_routing"));
        percolateRequest.preference(restRequest.param("percolate_preference"));
        percolateRequest.source(restRequest.content(), restRequest.contentUnsafe());

        percolateRequest.indicesOptions(IndicesOptions.fromRequest(restRequest, percolateRequest.indicesOptions()));
        executePercolate(percolateRequest, restRequest, restChannel);
    }

    void executePercolate(final PercolateRequest percolateRequest, final RestRequest restRequest, final RestChannel restChannel) {
        // we just send a response, no need to fork
        percolateRequest.listenerThreaded(false);

        if (restRequest.hasParam("operation_threading")) {
            BroadcastOperationThreading operationThreading = BroadcastOperationThreading.fromString(restRequest.param("operation_threading"), null);
            if (operationThreading == BroadcastOperationThreading.NO_THREADS) {
                // don't do work on the network thread
                operationThreading = BroadcastOperationThreading.SINGLE_THREAD;
            }
            percolateRequest.operationThreading(operationThreading);
        }

        client.percolate(percolateRequest, new ActionListener<PercolateResponse>() {
            @Override
            public void onResponse(PercolateResponse response) {
                try {
                    XContentBuilder builder = RestXContentBuilder.restContentBuilder(restRequest);
                    response.toXContent(builder, restRequest);
                    restChannel.sendResponse(new XContentRestResponse(restRequest, OK, builder));
                } catch (Throwable e) {
                    onFailure(e);
                }
            }

            @Override
            public void onFailure(Throwable e) {
                try {
                    restChannel.sendResponse(new XContentThrowableRestResponse(restRequest, e));
                } catch (IOException e1) {
                    logger.error("Failed to send failure response", e1);
                }
            }
        });
    }

    @Override
    public void handleRequest(RestRequest restRequest, RestChannel restChannel) {
        PercolateRequest percolateRequest = new PercolateRequest();
        parseDocPercolate(percolateRequest, restRequest, restChannel);
    }

    final class RestCountPercolateDocHandler implements RestHandler {

        @Override
        public void handleRequest(RestRequest restRequest, RestChannel restChannel) {
            PercolateRequest percolateRequest = new PercolateRequest();
            percolateRequest.onlyCount(true);
            parseDocPercolate(percolateRequest, restRequest, restChannel);
        }

    }

    final class RestPercolateExistingDocHandler implements RestHandler {

        @Override
        public void handleRequest(RestRequest restRequest, RestChannel restChannel) {
            PercolateRequest percolateRequest = new PercolateRequest();
            parseExistingDocPercolate(percolateRequest, restRequest, restChannel);
        }

    }

    final class RestCountPercolateExistingDocHandler implements RestHandler {

        @Override
        public void handleRequest(RestRequest restRequest, RestChannel restChannel) {
            PercolateRequest percolateRequest = new PercolateRequest();
            percolateRequest.onlyCount(true);
            parseExistingDocPercolate(percolateRequest, restRequest, restChannel);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9813.java