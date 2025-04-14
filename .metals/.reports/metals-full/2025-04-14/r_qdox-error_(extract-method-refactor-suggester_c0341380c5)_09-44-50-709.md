error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4036.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4036.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4036.java
text:
```scala
.@@path(RandomizedTest.randomFrom(restApi.getFinalPaths(pathParts)));

/*
 * Licensed to Elasticsearch under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elasticsearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.elasticsearch.test.rest.client;

import com.carrotsearch.randomizedtesting.RandomizedTest;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.test.rest.client.http.HttpRequestBuilder;
import org.elasticsearch.test.rest.client.http.HttpResponse;
import org.elasticsearch.test.rest.spec.RestApi;
import org.elasticsearch.test.rest.spec.RestSpec;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * REST client used to test the elasticsearch REST layer
 * Holds the {@link RestSpec} used to translate api calls into REST calls
 */
public class RestClient implements Closeable {

    private static final ESLogger logger = Loggers.getLogger(RestClient.class);

    private final RestSpec restSpec;
    private final CloseableHttpClient httpClient;

    private final String host;
    private final int port;

    private final String esVersion;

    public RestClient(String host, int port, RestSpec restSpec) throws IOException, RestException {
        this.restSpec = restSpec;
        this.httpClient = createHttpClient();
        this.host = host;
        this.port = port;
        this.esVersion = readVersion();
        logger.info("REST client initialized [{}:{}], elasticsearch version: [{}]", host, port, esVersion);
    }

    private String readVersion() throws IOException, RestException {
        //we make a manual call here without using callApi method, mainly because we are initializing
        //and the randomized context doesn't exist for the current thread (would be used to choose the method otherwise)
        RestApi restApi = restApi("info");
        assert restApi.getPaths().size() == 1;
        assert restApi.getMethods().size() == 1;
        RestResponse restResponse = new RestResponse(httpRequestBuilder()
                .path(restApi.getPaths().get(0))
                .method(restApi.getMethods().get(0)).execute());
        checkStatusCode(restResponse);
        Object version = restResponse.evaluate("version.number");
        if (version == null) {
            throw new RuntimeException("elasticsearch version not found in the response");
        }
        return version.toString();
    }

    public String getEsVersion() {
        return esVersion;
    }

    /**
     * Calls an api with the provided parameters
     * @throws RestException if the obtained status code is non ok, unless the specific error code needs to be ignored
     * according to the ignore parameter received as input (which won't get sent to elasticsearch)
     */
    public RestResponse callApi(String apiName, String... params) throws IOException, RestException {
        if (params.length % 2 != 0) {
            throw new IllegalArgumentException("The number of params passed must be even but was [" + params.length + "]");
        }

        Map<String, String> paramsMap = Maps.newHashMap();
        for (int i = 0; i < params.length; i++) {
            paramsMap.put(params[i++], params[i]);
        }

        return callApi(apiName, paramsMap, null);
    }

    /**
     * Calls an api with the provided parameters and body
     * @throws RestException if the obtained status code is non ok, unless the specific error code needs to be ignored
     * according to the ignore parameter received as input (which won't get sent to elasticsearch)
     */
    public RestResponse callApi(String apiName, Map<String, String> params, String body) throws IOException, RestException {

        List<Integer> ignores = Lists.newArrayList();
        Map<String, String> requestParams = null;
        if (params != null) {
            //makes a copy of the parameters before modifying them for this specific request
            requestParams = Maps.newHashMap(params);
            //ignore is a special parameter supported by the clients, shouldn't be sent to es
            String ignoreString = requestParams.remove("ignore");
            if (Strings.hasLength(ignoreString)) {
                try {
                    ignores.add(Integer.valueOf(ignoreString));
                } catch(NumberFormatException e) {
                    throw new IllegalArgumentException("ignore value should be a number, found [" + ignoreString + "] instead");
                }
            }
        }

        HttpRequestBuilder httpRequestBuilder = callApiBuilder(apiName, requestParams, body);
        logger.debug("calling api [{}]", apiName);
        HttpResponse httpResponse = httpRequestBuilder.execute();

        //http HEAD doesn't support response body
        // For the few api (exists class of api) that use it we need to accept 404 too
        if (!httpResponse.supportsBody()) {
            ignores.add(404);
        }

        RestResponse restResponse = new RestResponse(httpResponse);
        checkStatusCode(restResponse, ignores);
        return restResponse;
    }

    private void checkStatusCode(RestResponse restResponse, List<Integer> ignores) throws RestException {
        //ignore is a catch within the client, to prevent the client from throwing error if it gets non ok codes back
        if (ignores.contains(restResponse.getStatusCode())) {
            if (logger.isDebugEnabled()) {
                logger.debug("ignored non ok status codes {} as requested", ignores);
            }
            return;
        }
        checkStatusCode(restResponse);
    }

    private void checkStatusCode(RestResponse restResponse) throws RestException {
        if (restResponse.isError()) {
            throw new RestException("non ok status code [" + restResponse.getStatusCode() + "] returned", restResponse);
        }
    }

    private HttpRequestBuilder callApiBuilder(String apiName, Map<String, String> params, String body) {
        RestApi restApi = restApi(apiName);

        HttpRequestBuilder httpRequestBuilder = httpRequestBuilder().body(body);

        //divide params between ones that go within query string and ones that go within path
        Map<String, String> pathParts = Maps.newHashMap();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (restApi.getPathParts().contains(entry.getKey())) {
                    pathParts.put(entry.getKey(), entry.getValue());
                } else {
                    httpRequestBuilder.addParam(entry.getKey(), entry.getValue());
                }
            }
        }

        //the http method is randomized (out of the available ones with the chosen api)
        return httpRequestBuilder.method(RandomizedTest.randomFrom(restApi.getSupportedMethods(pathParts.keySet())))
                .path(restApi.getFinalPath(pathParts));
    }

    private RestApi restApi(String apiName) {
        RestApi restApi = restSpec.getApi(apiName);
        if (restApi == null) {
            throw new IllegalArgumentException("rest api [" + apiName + "] doesn't exist in the rest spec");
        }
        return restApi;
    }

    protected HttpRequestBuilder httpRequestBuilder() {
        return new HttpRequestBuilder(httpClient).host(host).port(port);
    }

    protected CloseableHttpClient createHttpClient() {
        return HttpClients.createDefault();
    }

    /**
     * Closes the REST client and the underlying http client
     */
    public void close() {
        try {
            httpClient.close();
        } catch(IOException e) {
            logger.error(e.getMessage(), e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4036.java