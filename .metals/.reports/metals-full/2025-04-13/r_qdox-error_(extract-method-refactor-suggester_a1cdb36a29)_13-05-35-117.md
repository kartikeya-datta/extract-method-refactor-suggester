error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2161.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2161.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2161.java
text:
```scala
t@@ermsRequest.sortType(request.param("sort"));

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
 * @author kimchy (shay.banon)
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
            BroadcastOperationThreading operationThreading = BroadcastOperationThreading.fromString(request.param("operation_threading"), BroadcastOperationThreading.SINGLE_THREAD);
            if (operationThreading == BroadcastOperationThreading.NO_THREADS) {
                // since we don't spawn, don't allow no_threads, but change it to a single thread
                operationThreading = BroadcastOperationThreading.SINGLE_THREAD;
            }
            termsRequest.operationThreading(operationThreading);

            List<String> fields = request.params("field");
            String sField = request.param("fields");
            if (sField != null) {
                String[] sFields = fieldsPattern.split(sField);
                if (sFields != null) {
                    if (fields == null) {
                        fields = new ArrayList<String>();
                    }
                    for (String field : sFields) {
                        fields.add(field);
                    }
                }
            }
            termsRequest.fields(fields.toArray(new String[fields.size()]));

            termsRequest.from(request.param("from"));
            termsRequest.to(request.param("to"));
            termsRequest.fromInclusive(request.paramAsBoolean("from_inclusive", termsRequest.fromInclusive()));
            termsRequest.toInclusive(request.paramAsBoolean("to_inclusive", termsRequest.toInclusive()));

            Object temp = request.param("gt");
            if (temp != null) {
                termsRequest.gt(temp);
            } else {
                temp = request.param("gte");
                if (temp != null) {
                    termsRequest.gte(temp);
                }
            }
            temp = request.param("lt");
            if (temp != null) {
                termsRequest.lt(temp);
            } else {
                temp = request.param("lte");
                if (temp != null) {
                    termsRequest.lte(temp);
                }
            }

            termsRequest.exact(request.paramAsBoolean("exact", termsRequest.exact()));
            termsRequest.minFreq(request.paramAsInt("min_freq", termsRequest.minFreq()));
            termsRequest.maxFreq(request.paramAsInt("max_freq", termsRequest.maxFreq()));
            termsRequest.size(request.paramAsInt("size", termsRequest.size()));
            termsRequest.prefix(request.param("prefix"));
            termsRequest.regexp(request.param("regexp"));
            termsRequest.sortType(TermsRequest.SortType.fromString(request.param("sort"), termsRequest.sortType()));
        } catch (Exception e) {
            try {
                JsonBuilder builder = RestJsonBuilder.restJsonBuilder(request);
                channel.sendResponse(new JsonRestResponse(request, BAD_REQUEST, builder.startObject().field("error", e.getMessage()).endObject()));
            } catch (IOException e1) {
                logger.error("Failed to send failure response", e1);
            }
            return;
        }

        final boolean termsAsArray = request.paramAsBoolean("terms_as_array", true);
        client.terms(termsRequest, new ActionListener<TermsResponse>() {
            @Override public void onResponse(TermsResponse response) {
                try {
                    JsonBuilder builder = RestJsonBuilder.restJsonBuilder(request);
                    builder.startObject();

                    buildBroadcastShardsHeader(builder, response);

                    builder.startObject("docs");
                    builder.field("num_docs", response.numDocs());
                    builder.field("max_doc", response.maxDoc());
                    builder.field("deleted_docs", response.deletedDocs());
                    builder.endObject();

                    builder.startObject("fields");
                    for (FieldTermsFreq fieldTermsFreq : response.fields()) {
                        builder.startObject(fieldTermsFreq.fieldName());

                        if (!termsAsArray) {
                            builder.startObject("terms");
                            for (TermFreq termFreq : fieldTermsFreq.termsFreqs()) {
                                builder.startObject(termFreq.termAsString());
                                builder.field("doc_freq", termFreq.docFreq());
                                builder.endObject();
                            }
                            builder.endObject();
                        } else {
                            builder.startArray("terms");
                            for (TermFreq termFreq : fieldTermsFreq.termsFreqs()) {
                                builder.startObject();
                                builder.field("term", termFreq.term());
                                builder.field("doc_freq", termFreq.docFreq());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2161.java