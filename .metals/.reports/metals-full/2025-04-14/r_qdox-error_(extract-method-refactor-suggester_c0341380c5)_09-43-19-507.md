error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2418.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2418.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2418.java
text:
```scala
.@@createParser(request.contentByteArray(), request.contentByteArrayOffset(), request.contentLength()).mapOrderedAndClose();

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.rest.action.admin.indices.template.put;

import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateRequest;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentBuilderString;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.support.XContentMapValues;
import org.elasticsearch.rest.*;
import org.elasticsearch.rest.action.support.RestXContentBuilder;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.common.unit.TimeValue.timeValueSeconds;
import static org.elasticsearch.rest.RestStatus.OK;

/**
 *
 */
public class RestPutIndexTemplateAction extends BaseRestHandler {

    @Inject
    public RestPutIndexTemplateAction(Settings settings, Client client, RestController controller) {
        super(settings, client);
        controller.registerHandler(RestRequest.Method.PUT, "/_template/{name}", this);
        controller.registerHandler(RestRequest.Method.POST, "/_template/{name}", new CreateHandler());
    }

    final class CreateHandler implements RestHandler {
        @Override
        public void handleRequest(RestRequest request, RestChannel channel) {
            request.params().put("create", "true");
            RestPutIndexTemplateAction.this.handleRequest(request, channel);
        }
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void handleRequest(final RestRequest request, final RestChannel channel) {
        PutIndexTemplateRequest putRequest = new PutIndexTemplateRequest(request.param("name"));

        try {
            putRequest.create(request.paramAsBoolean("create", false));
            putRequest.cause(request.param("cause", ""));
            putRequest.timeout(request.paramAsTime("timeout", timeValueSeconds(10)));

            // parse the parameters
            Map<String, Object> source = XContentFactory.xContent(request.contentByteArray(), request.contentByteArrayOffset(), request.contentLength())
                    .createParser(request.contentByteArray(), request.contentByteArrayOffset(), request.contentLength()).mapAndClose();

            if (source.containsKey("template")) {
                putRequest.template(source.get("template").toString());
            }
            if (source.containsKey("order")) {
                putRequest.order(XContentMapValues.nodeIntegerValue(source.get("order"), putRequest.order()));
            }
            if (source.containsKey("settings")) {
                if (!(source.get("settings") instanceof Map)) {
                    throw new ElasticSearchIllegalArgumentException("Malformed settings section, should include an inner object");
                }
                putRequest.settings((Map<String, Object>) source.get("settings"));
            }
            if (source.containsKey("mappings")) {
                Map<String, Object> mappings = (Map<String, Object>) source.get("mappings");
                for (Map.Entry<String, Object> entry : mappings.entrySet()) {
                    if (!(entry.getValue() instanceof Map)) {
                        throw new ElasticSearchIllegalArgumentException("Malformed mappings section for type [" + entry.getKey() + "], should include an inner object describing the mapping");
                    }
                    putRequest.mapping(entry.getKey(), (Map<String, Object>) entry.getValue());
                }
            }
        } catch (Exception e) {
            try {
                channel.sendResponse(new XContentThrowableRestResponse(request, e));
            } catch (IOException e1) {
                logger.warn("Failed to send response", e1);
            }
            return;
        }

        putRequest.template(request.param("template", putRequest.template()));
        putRequest.order(request.paramAsInt("order", putRequest.order()));

        client.admin().indices().putTemplate(putRequest, new ActionListener<PutIndexTemplateResponse>() {
            @Override
            public void onResponse(PutIndexTemplateResponse response) {
                try {
                    XContentBuilder builder = RestXContentBuilder.restContentBuilder(request);
                    builder.startObject()
                            .field(Fields.OK, true)
                            .field(Fields.ACKNOWLEDGED, response.acknowledged())
                            .endObject();
                    channel.sendResponse(new XContentRestResponse(request, OK, builder));
                } catch (IOException e) {
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

    static final class Fields {
        static final XContentBuilderString OK = new XContentBuilderString("ok");
        static final XContentBuilderString ACKNOWLEDGED = new XContentBuilderString("acknowledged");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2418.java