error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9294.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9294.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9294.java
text:
```scala
public S@@ettings indexSettings() {

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

package org.elasticsearch.recovery;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus;
import org.elasticsearch.action.admin.indices.flush.FlushResponse;
import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.junit.Test;

import static org.elasticsearch.client.Requests.*;
import static org.hamcrest.Matchers.equalTo;

/**
 *
 */
public class SimpleRecoveryTests extends ElasticsearchIntegrationTest {

    @Override
    public Settings getSettings() {
        return recoverySettings();
    }
    
    protected Settings recoverySettings() {
        return ImmutableSettings.Builder.EMPTY_SETTINGS;
    }

    @Test
    public void testSimpleRecovery() throws Exception {
        prepareCreate("test", 1).execute().actionGet(5000);

        logger.info("Running Cluster Health");
        ClusterHealthResponse clusterHealth = client().admin().cluster().health(clusterHealthRequest().waitForYellowStatus()).actionGet();
        logger.info("Done Cluster Health, status " + clusterHealth.getStatus());
        assertThat(clusterHealth.isTimedOut(), equalTo(false));
        assertThat(clusterHealth.getStatus(), equalTo(ClusterHealthStatus.YELLOW));

        client().index(indexRequest("test").type("type1").id("1").source(source("1", "test"))).actionGet();
        FlushResponse flushResponse = client().admin().indices().flush(flushRequest("test")).actionGet();
        assertThat(flushResponse.getTotalShards(), equalTo(10));
        assertThat(flushResponse.getSuccessfulShards(), equalTo(5));
        assertThat(flushResponse.getFailedShards(), equalTo(0));
        client().index(indexRequest("test").type("type1").id("2").source(source("2", "test"))).actionGet();
        RefreshResponse refreshResponse = client().admin().indices().refresh(refreshRequest("test")).actionGet();
        assertThat(refreshResponse.getTotalShards(), equalTo(10));
        assertThat(refreshResponse.getSuccessfulShards(), equalTo(5));
        assertThat(refreshResponse.getFailedShards(), equalTo(0));

        allowNodes("test", 2);

        logger.info("Running Cluster Health");
        clusterHealth = client().admin().cluster().health(clusterHealthRequest().waitForGreenStatus().local(true).waitForNodes(">=2")).actionGet();
        logger.info("Done Cluster Health, status " + clusterHealth.getStatus());
        assertThat(clusterHealth.isTimedOut(), equalTo(false));
        assertThat(clusterHealth.getStatus(), equalTo(ClusterHealthStatus.GREEN));

        GetResponse getResult;

        for (int i = 0; i < 5; i++) {
            getResult = client().get(getRequest("test").type("type1").id("1").operationThreaded(false)).actionGet(1000);
            assertThat(getResult.getSourceAsString(), equalTo(source("1", "test")));
            getResult = client().get(getRequest("test").type("type1").id("1").operationThreaded(false)).actionGet(1000);
            assertThat(getResult.getSourceAsString(), equalTo(source("1", "test")));
            getResult = client().get(getRequest("test").type("type1").id("2").operationThreaded(true)).actionGet(1000);
            assertThat(getResult.getSourceAsString(), equalTo(source("2", "test")));
            getResult = client().get(getRequest("test").type("type1").id("2").operationThreaded(true)).actionGet(1000);
            assertThat(getResult.getSourceAsString(), equalTo(source("2", "test")));
        }

        // now start another one so we move some primaries
        allowNodes("test", 3);
        Thread.sleep(200);
        logger.info("Running Cluster Health");
        clusterHealth = client().admin().cluster().health(clusterHealthRequest().waitForGreenStatus().waitForRelocatingShards(0).waitForNodes(">=3")).actionGet();
        logger.info("Done Cluster Health, status " + clusterHealth.getStatus());
        assertThat(clusterHealth.isTimedOut(), equalTo(false));
        assertThat(clusterHealth.getStatus(), equalTo(ClusterHealthStatus.GREEN));

        for (int i = 0; i < 5; i++) {
            getResult = client().get(getRequest("test").type("type1").id("1")).actionGet(1000);
            assertThat(getResult.getSourceAsString(), equalTo(source("1", "test")));
            getResult = client().get(getRequest("test").type("type1").id("1")).actionGet(1000);
            assertThat(getResult.getSourceAsString(), equalTo(source("1", "test")));
            getResult = client().get(getRequest("test").type("type1").id("1")).actionGet(1000);
            assertThat(getResult.getSourceAsString(), equalTo(source("1", "test")));
            getResult = client().get(getRequest("test").type("type1").id("2").operationThreaded(true)).actionGet(1000);
            assertThat(getResult.getSourceAsString(), equalTo(source("2", "test")));
            getResult = client().get(getRequest("test").type("type1").id("2").operationThreaded(true)).actionGet(1000);
            assertThat(getResult.getSourceAsString(), equalTo(source("2", "test")));
            getResult = client().get(getRequest("test").type("type1").id("2").operationThreaded(true)).actionGet(1000);
            assertThat(getResult.getSourceAsString(), equalTo(source("2", "test")));
        }
    }

    private String source(String id, String nameValue) {
        return "{ type1 : { \"id\" : \"" + id + "\", \"name\" : \"" + nameValue + "\" } }";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9294.java