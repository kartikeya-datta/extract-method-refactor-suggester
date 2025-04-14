error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3902.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3902.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3902.java
text:
```scala
H@@ttpServerTransport httpServerTransport = internalCluster().getDataNodeInstance(HttpServerTransport.class);

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
package org.elasticsearch.plugin;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.http.HttpServerTransport;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.rest.helper.HttpClient;
import org.elasticsearch.rest.helper.HttpClientResponse;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.elasticsearch.test.ElasticsearchIntegrationTest.ClusterScope;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.elasticsearch.test.ElasticsearchIntegrationTest.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

/**
 * We want to test site plugins
 */
@ClusterScope(scope = Scope.SUITE, numDataNodes = 1)
public class SitePluginTests extends ElasticsearchIntegrationTest {


    @Override
    protected Settings nodeSettings(int nodeOrdinal) {
        try {
            File pluginDir = new File(SitePluginTests.class.getResource("/org/elasticsearch/plugin").toURI());
            return settingsBuilder()
                    .put(super.nodeSettings(nodeOrdinal))
                    .put("path.plugins", pluginDir.getAbsolutePath())
                    .put("force.http.enabled", true)
                    .build();
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    public HttpClient httpClient() {
        HttpServerTransport httpServerTransport = cluster().getDataNodeInstance(HttpServerTransport.class);
        return new HttpClient(httpServerTransport.boundAddress().publishAddress());
    }

    @Test
    public void testRedirectSitePlugin() throws Exception {
        // We use an HTTP Client to test redirection
        HttpClientResponse response = httpClient().request("/_plugin/dummy");
        assertThat(response.errorCode(), equalTo(RestStatus.MOVED_PERMANENTLY.getStatus()));
        assertThat(response.response(), containsString("/_plugin/dummy/"));

        // We test the real URL
        response = httpClient().request("/_plugin/dummy/");
        assertThat(response.errorCode(), equalTo(RestStatus.OK.getStatus()));
        assertThat(response.response(), containsString("<title>Dummy Site Plugin</title>"));
    }

    /**
     * Test direct access to an existing file (index.html)
     */
    @Test
    public void testAnyPage() throws Exception {
        HttpClientResponse response = httpClient().request("/_plugin/dummy/index.html");
        assertThat(response.errorCode(), equalTo(RestStatus.OK.getStatus()));
        assertThat(response.response(), containsString("<title>Dummy Site Plugin</title>"));
    }

    /**
     * Test case for #4845: https://github.com/elasticsearch/elasticsearch/issues/4845
     * Serving _site plugins do not pick up on index.html for sub directories
     */
    @Test
    public void testWelcomePageInSubDirs() throws Exception {
        HttpClientResponse response = httpClient().request("/_plugin/subdir/dir/");
        assertThat(response.errorCode(), equalTo(RestStatus.OK.getStatus()));
        assertThat(response.response(), containsString("<title>Dummy Site Plugin (subdir)</title>"));

        response = httpClient().request("/_plugin/subdir/dir_without_index/");
        assertThat(response.errorCode(), equalTo(RestStatus.FORBIDDEN.getStatus()));

        response = httpClient().request("/_plugin/subdir/dir_without_index/page.html");
        assertThat(response.errorCode(), equalTo(RestStatus.OK.getStatus()));
        assertThat(response.response(), containsString("<title>Dummy Site Plugin (page)</title>"));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3902.java