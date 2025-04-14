error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9825.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9825.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9825.java
text:
```scala
final b@@oolean termsAsArray = request.paramAsBoolean("termsAsArray", true);

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

package org.elasticsearch.rest.action.terms;

import com.google.inject.Inject;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.support.broadcast.BroadcastOperationThreading;
import org.elasticsearch.action.terms.FieldTermsFreq;
import org.elasticsearch.action.terms.TermFreq;
import org.elasticsearch.action.terms.TermsRequest;
import org.elasticsearch.action.terms.TermsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.rest.*;
import org.elasticsearch.rest.action.support.RestJsonBuilder;
import org.elasticsearch.util.json.JsonBuilder;
import org.elasticsearch.util.settings.Settings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.elasticsearch.rest.RestResponse.Status.*;
import static org.elasticsearch.rest.action.support.RestActions.*;

/**
 * @author kimchy (Shay Banon)
 */
public class RestTermsAction extends BaseRestHandler {

    private final static Pattern fieldsPattern;

    static {
        fieldsPattern = Pattern.compile(",");
    }

    @Inject public RestTermsAction(Settings settings, Client client, RestController controller) {
        super(settings, client);
        controller.registerHandler(RestRequest.Method.POST, "/_terms", this);
        controller.registerHandler(RestRequest.Method.GET, "/_terms", this);
        controller.registerHandler(RestRequest.Method.POST, "/{index}/_terms", this);
        controller.registerHandler(RestRequest.Method.GET, "/{index}/_terms", this);
    }

    @Override public void handleRequest(final RestRequest request, final RestChannel channel) {
        TermsRequest termsRequest = new TermsRequest(splitIndices(request.param("index")));
        // we just send back a response, no need to fork a listener
        termsRequest.listenerThreaded(false);
        try {
            BroadcastOperationThreading operationThreading = BroadcastOperationThreading.fromString(request.param("operationThreading"), BroadcastOperationThreading.SINGLE_THREAD);
            if (operationThreading == BroadcastOperationThreading.NO_THREADS) {
                // since we don't spawn, don't allow no_threads, but change it to a single thread
                operationThreading = BroadcastOperationThreading.SINGLE_THREAD;
            }
            termsRequest.operationThreading(operationThreading);

            List<String> fields = request.params("field");
            if (fields == null) {
                fields = new ArrayList<String>();
            }
            String sField = request.param("fields");
            if (sField != null) {
                String[] sFields = fieldsPattern.split(sField);
                if (sFields != null) {
                    for (String field : sFields) {
                        fields.add(field);
                    }
                }
            }
            termsRequest.fields(fields.toArray(new String[fields.size()]));

            termsRequest.from(request.param("from"));
            termsRequest.to(request.param("to"));
            termsRequest.fromInclusive(request.paramAsBoolean("fromInclusive", termsRequest.fromInclusive()));
            termsRequest.toInclusive(request.paramAsBoolean("toInclusive", termsRequest.toInclusive()));
            termsRequest.exact(request.paramAsBoolean("exact", termsRequest.exact()));
            termsRequest.minFreq(request.paramAsInt("minFreq", termsRequest.minFreq()));
            termsRequest.maxFreq(request.paramAsInt("maxFreq", termsRequest.maxFreq()));
            termsRequest.size(request.paramAsInt("size", termsRequest.size()));
            termsRequest.convert(request.paramAsBoolean("convert", termsRequest.convert()));
            termsRequest.prefix(request.param("prefix"));
            termsRequest.regexp(request.param("regexp"));
            termsRequest.sortType(TermsRequest.SortType.fromString(request.param("sort"), termsRequest.sortType()));
        } catch (Exception e) {
            try {
                channel.sendResponse(new JsonRestResponse(request, BAD_REQUEST, JsonBuilder.jsonBuilder().startObject().field("error", e.getMessage()).endObject()));
            } catch (IOException e1) {
                logger.error("Failed to send failure response", e1);
            }
            return;
        }

        final boolean termsAsArray = request.paramAsBoolean("termsAsArray", false);
        client.execTerms(termsRequest, new ActionListener<TermsResponse>() {
            @Override public void onResponse(TermsResponse response) {
                try {
                    JsonBuilder builder = RestJsonBuilder.cached(request);
                    builder.startObject();

                    buildBroadcastShardsHeader(builder, response);

                    builder.startObject("docs");
                    builder.field("numDocs", response.numDocs());
                    builder.field("maxDoc", response.maxDoc());
                    builder.field("deletedDocs", response.deletedDocs());
                    builder.endObject();

                    builder.startObject("fields");
                    for (FieldTermsFreq fieldTermsFreq : response.fields()) {
                        builder.startObject(fieldTermsFreq.fieldName());

                        if (!termsAsArray) {
                            builder.startObject("terms");
                            for (TermFreq termFreq : fieldTermsFreq.termsFreqs()) {
                                builder.startObject(termFreq.term());
                                builder.field("docFreq", termFreq.docFreq());
                                builder.endObject();
                            }
                            builder.endObject();
                        } else {
                            builder.startArray("terms");
                            for (TermFreq termFreq : fieldTermsFreq.termsFreqs()) {
                                builder.startObject();
                                builder.field("term", termFreq.term());
                                builder.field("docFreq", termFreq.docFreq());
                                builder.endObject();
                            }
                            builder.endArray();
                        }

                        builder.endObject();
                    }
                    builder.endObject();

                    builder.endObject();
                    channel.sendResponse(new JsonRestResponse(request, OK, builder));
                } catch (Exception e) {
                    onFailure(e);
                }
            }

            @Override public void onFailure(Throwable e) {
                try {
                    channel.sendResponse(new JsonThrowableRestResponse(request, e));
                } catch (IOException e1) {
                    logger.error("Failed to send failure response", e1);
                }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9825.java