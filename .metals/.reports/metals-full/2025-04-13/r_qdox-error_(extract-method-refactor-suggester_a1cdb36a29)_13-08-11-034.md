error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/578.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/578.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/578.java
text:
```scala
J@@sonBuilder builder = RestJsonBuilder.restJsonBuilder(request).prettyPrint();

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

package org.elasticsearch.rest.action.main;

import com.google.common.collect.Iterators;
import com.google.inject.Inject;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.elasticsearch.Version;
import org.elasticsearch.client.Client;
import org.elasticsearch.rest.*;
import org.elasticsearch.rest.action.support.RestJsonBuilder;
import org.elasticsearch.util.Classes;
import org.elasticsearch.util.concurrent.ThreadLocalRandom;
import org.elasticsearch.util.json.Jackson;
import org.elasticsearch.util.json.JsonBuilder;
import org.elasticsearch.util.settings.Settings;

import java.io.IOException;

import static org.elasticsearch.rest.RestRequest.Method.*;

/**
 * @author kimchy (Shay Banon)
 */
public class RestMainAction extends BaseRestHandler {

    private final JsonNode rootNode;

    private final int quotesSize;

    @Inject public RestMainAction(Settings settings, Client client, RestController controller) {
        super(settings, client);
        JsonNode rootNode;
        int quotesSize;
        try {
            rootNode = Jackson.newObjectMapper().readValue(Classes.getDefaultClassLoader().getResourceAsStream("org/elasticsearch/rest/action/main/quotes.json"), JsonNode.class);
            ArrayNode arrayNode = (ArrayNode) rootNode.get("quotes");
            quotesSize = Iterators.size(arrayNode.getElements());
        } catch (Exception e) {
            rootNode = null;
            quotesSize = -1;
        }
        this.rootNode = rootNode;
        this.quotesSize = quotesSize;

        controller.registerHandler(GET, "/", this);
    }

    @Override public void handleRequest(RestRequest request, RestChannel channel) {
        try {
            JsonBuilder builder = RestJsonBuilder.cached(request).prettyPrint();
            builder.startObject();
            builder.field("ok", true);
            if (settings.get("name") != null) {
                builder.field("name", settings.get("name"));
            }
            builder.startObject("version").field("number", Version.number()).field("date", Version.date()).field("devBuild", Version.devBuild()).endObject();
            builder.field("version", Version.number());
            builder.field("tagline", "You Know, for Search");
            builder.field("cover", "DON'T PANIC");
            if (rootNode != null) {
                builder.startObject("quote");
                ArrayNode arrayNode = (ArrayNode) rootNode.get("quotes");
                JsonNode quoteNode = arrayNode.get(ThreadLocalRandom.current().nextInt(quotesSize));
                builder.field("book", quoteNode.get("book").getValueAsText());
                builder.field("chapter", quoteNode.get("chapter").getValueAsText());
                ArrayNode textNodes = (ArrayNode) quoteNode.get("text");
//                builder.startArray("text");
//                for (JsonNode textNode : textNodes) {
//                    builder.value(textNode.getValueAsText());
//                }
//                builder.endArray();
                int index = 0;
                for (JsonNode textNode : textNodes) {
                    builder.field("text" + (++index), textNode.getValueAsText());
                }
                builder.endObject();
            }
            builder.endObject();
            channel.sendResponse(new JsonRestResponse(request, RestResponse.Status.OK, builder));
        } catch (Exception e) {
            try {
                channel.sendResponse(new JsonThrowableRestResponse(request, e));
            } catch (IOException e1) {
                logger.warn("Failed to send response", e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/578.java