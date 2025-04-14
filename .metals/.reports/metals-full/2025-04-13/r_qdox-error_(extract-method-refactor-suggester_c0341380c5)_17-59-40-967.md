error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9047.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9047.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9047.java
text:
```scala
m@@ltRequest.fields(request.paramAsStringArray("mltFields", null));

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

package org.elasticsearch.rest.action.mlt;

import com.google.inject.Inject;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.mlt.MoreLikeThisRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.rest.*;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.util.json.JsonBuilder;
import org.elasticsearch.util.settings.Settings;

import java.io.IOException;

import static org.elasticsearch.client.Requests.*;
import static org.elasticsearch.rest.RestRequest.Method.*;
import static org.elasticsearch.rest.RestResponse.Status.*;
import static org.elasticsearch.rest.action.support.RestActions.*;
import static org.elasticsearch.rest.action.support.RestJsonBuilder.*;
import static org.elasticsearch.util.TimeValue.*;

/**
 * @author kimchy (shay.banon)
 */
public class RestMoreLikeThisAction extends BaseRestHandler {

    @Inject public RestMoreLikeThisAction(Settings settings, Client client, RestController controller) {
        super(settings, client);
        controller.registerHandler(GET, "/{index}/{type}/{id}/_moreLikeThis", this);
        controller.registerHandler(POST, "/{index}/{type}/{id}/_moreLikeThis", this);
        controller.registerHandler(GET, "/{index}/{type}/{id}/_mlt", this);
        controller.registerHandler(POST, "/{index}/{type}/{id}/_mlt", this);
    }

    @Override public void handleRequest(final RestRequest request, final RestChannel channel) {
        MoreLikeThisRequest mltRequest = moreLikeThisRequest(request.param("index")).type(request.param("type")).id(request.param("id"));
        try {
            mltRequest.fields(request.paramAsStringArray("fields", null));
            mltRequest.percentTermsToMatch(request.paramAsFloat("percentTermsToMatch", -1));
            mltRequest.minTermFrequency(request.paramAsInt("minTermFrequency", -1));
            mltRequest.maxQueryTerms(request.paramAsInt("maxQueryTerms", -1));
            mltRequest.stopWords(request.paramAsStringArray("stopWords", null));
            mltRequest.minDocFreq(request.paramAsInt("minDocFreq", -1));
            mltRequest.maxDocFreq(request.paramAsInt("maxDocFreq", -1));
            mltRequest.minWordLen(request.paramAsInt("minWordLen", -1));
            mltRequest.maxWordLen(request.paramAsInt("maxWordLen", -1));
            mltRequest.boostTerms(request.paramAsBoolean("boostTerms", null));
            mltRequest.boostTermsFactor(request.paramAsFloat("boostTermsFactor", -1));

            mltRequest.searchType(parseSearchType(request.param("searchType")));
            mltRequest.searchIndices(request.paramAsStringArray("searchIndices", null));
            mltRequest.searchTypes(request.paramAsStringArray("searchTypes", null));
            mltRequest.searchQueryHint(request.param("searchQueryHint"));
            String searchScroll = request.param("searchScroll");
            if (searchScroll != null) {
                mltRequest.searchScroll(new Scroll(parseTimeValue(searchScroll, null)));
            }
            if (request.hasContent()) {
                mltRequest.searchSource(request.contentAsBytes());
            }
        } catch (Exception e) {
            try {
                JsonBuilder builder = restJsonBuilder(request);
                channel.sendResponse(new JsonRestResponse(request, BAD_REQUEST, builder.startObject().field("error", e.getMessage()).endObject()));
            } catch (IOException e1) {
                logger.error("Failed to send failure response", e1);
            }
            return;
        }

        client.moreLikeThis(mltRequest, new ActionListener<SearchResponse>() {
            @Override public void onResponse(SearchResponse response) {
                try {
                    JsonBuilder builder = restJsonBuilder(request);
                    builder.startObject();
                    response.toJson(builder, request);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9047.java