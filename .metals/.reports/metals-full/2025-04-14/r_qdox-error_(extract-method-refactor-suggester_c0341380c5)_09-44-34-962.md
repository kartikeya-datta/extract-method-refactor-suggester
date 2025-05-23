error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6213.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6213.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6213.java
text:
```scala
D@@eleteWarmerResponse deleteWarmerResponse = client().admin().indices().prepareDeleteWarmer().setIndices("test").setName("warmer_1").execute().actionGet();

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

package org.elasticsearch.indices.warmer;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.indices.warmer.delete.DeleteWarmerResponse;
import org.elasticsearch.action.admin.indices.warmer.put.PutWarmerResponse;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.common.Priority;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.warmer.IndexWarmersMetaData;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.elasticsearch.test.ElasticsearchIntegrationTest.ClusterScope;
import org.elasticsearch.test.ElasticsearchIntegrationTest.Scope;
import org.elasticsearch.test.TestCluster.RestartCallback;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.hamcrest.Matchers.equalTo;

/**
 */
@ClusterScope(numNodes=0, scope=Scope.TEST)
public class LocalGatewayIndicesWarmerTests extends ElasticsearchIntegrationTest {

    private final ESLogger logger = Loggers.getLogger(LocalGatewayIndicesWarmerTests.class);

    @Test
    public void testStatePersistence() throws Exception {

        logger.info("--> starting 1 nodes");
        cluster().startNode(settingsBuilder().put("gateway.type", "local"));

        logger.info("--> putting two templates");
        client().admin().indices().prepareCreate("test")
                .setSettings(ImmutableSettings.settingsBuilder().put("index.number_of_shards", 1))
                .execute().actionGet();

        client().admin().cluster().prepareHealth().setWaitForEvents(Priority.LANGUID).setWaitForYellowStatus().execute().actionGet();

        PutWarmerResponse putWarmerResponse = client().admin().indices().preparePutWarmer("warmer_1")
                .setSearchRequest(client().prepareSearch("test").setQuery(QueryBuilders.termQuery("field", "value1")))
                .execute().actionGet();
        assertThat(putWarmerResponse.isAcknowledged(), equalTo(true));
        putWarmerResponse = client().admin().indices().preparePutWarmer("warmer_2")
                .setSearchRequest(client().prepareSearch("test").setQuery(QueryBuilders.termQuery("field", "value2")))
                .execute().actionGet();
        assertThat(putWarmerResponse.isAcknowledged(), equalTo(true));

        logger.info("--> put template with warmer");
        client().admin().indices().preparePutTemplate("template_1")
                .setSource("{\n" +
                        "    \"template\" : \"xxx\",\n" +
                        "    \"warmers\" : {\n" +
                        "        \"warmer_1\" : {\n" +
                        "            \"types\" : [],\n" +
                        "            \"source\" : {\n" +
                        "                \"query\" : {\n" +
                        "                    \"match_all\" : {}\n" +
                        "                }\n" +
                        "            }\n" +
                        "        }\n" +
                        "    }\n" +
                        "}")
                .execute().actionGet();


        logger.info("--> verify warmers are registered in cluster state");
        ClusterState clusterState = client().admin().cluster().prepareState().execute().actionGet().getState();
        IndexWarmersMetaData warmersMetaData = clusterState.metaData().index("test").custom(IndexWarmersMetaData.TYPE);
        assertThat(warmersMetaData, Matchers.notNullValue());
        assertThat(warmersMetaData.entries().size(), equalTo(2));

        IndexWarmersMetaData templateWarmers = clusterState.metaData().templates().get("template_1").custom(IndexWarmersMetaData.TYPE);
        assertThat(templateWarmers, Matchers.notNullValue());
        assertThat(templateWarmers.entries().size(), equalTo(1));

        logger.info("--> restarting the node");
        cluster().fullRestart(new RestartCallback() {
            @Override
            public Settings onNodeStopped(String nodeName) throws Exception {
                return settingsBuilder().put("gateway.type", "local").build();
            }
        });

        ClusterHealthResponse healthResponse = client().admin().cluster().prepareHealth().setWaitForEvents(Priority.LANGUID).setWaitForYellowStatus().execute().actionGet();
        assertThat(healthResponse.isTimedOut(), equalTo(false));

        logger.info("--> verify warmers are recovered");
        clusterState = client().admin().cluster().prepareState().execute().actionGet().getState();
        IndexWarmersMetaData recoveredWarmersMetaData = clusterState.metaData().index("test").custom(IndexWarmersMetaData.TYPE);
        assertThat(recoveredWarmersMetaData.entries().size(), equalTo(warmersMetaData.entries().size()));
        for (int i = 0; i < warmersMetaData.entries().size(); i++) {
            assertThat(recoveredWarmersMetaData.entries().get(i).name(), equalTo(warmersMetaData.entries().get(i).name()));
            assertThat(recoveredWarmersMetaData.entries().get(i).source(), equalTo(warmersMetaData.entries().get(i).source()));
        }

        logger.info("--> verify warmers in template are recovered");
        IndexWarmersMetaData recoveredTemplateWarmers = clusterState.metaData().templates().get("template_1").custom(IndexWarmersMetaData.TYPE);
        assertThat(recoveredTemplateWarmers.entries().size(), equalTo(templateWarmers.entries().size()));
        for (int i = 0; i < templateWarmers.entries().size(); i++) {
            assertThat(recoveredTemplateWarmers.entries().get(i).name(), equalTo(templateWarmers.entries().get(i).name()));
            assertThat(recoveredTemplateWarmers.entries().get(i).source(), equalTo(templateWarmers.entries().get(i).source()));
        }


        logger.info("--> delete warmer warmer_1");
        DeleteWarmerResponse deleteWarmerResponse = client().admin().indices().prepareDeleteWarmer().setIndices("test").setNames("warmer_1").execute().actionGet();
        assertThat(deleteWarmerResponse.isAcknowledged(), equalTo(true));

        logger.info("--> verify warmers (delete) are registered in cluster state");
        clusterState = client().admin().cluster().prepareState().execute().actionGet().getState();
        warmersMetaData = clusterState.metaData().index("test").custom(IndexWarmersMetaData.TYPE);
        assertThat(warmersMetaData, Matchers.notNullValue());
        assertThat(warmersMetaData.entries().size(), equalTo(1));

        logger.info("--> restarting the node");
        cluster().fullRestart(new RestartCallback() {
            @Override
            public Settings onNodeStopped(String nodeName) throws Exception {
                return settingsBuilder().put("gateway.type", "local").build();
            }
        });

        healthResponse = client().admin().cluster().prepareHealth().setWaitForEvents(Priority.LANGUID).setWaitForYellowStatus().execute().actionGet();
        assertThat(healthResponse.isTimedOut(), equalTo(false));

        logger.info("--> verify warmers are recovered");
        clusterState = client().admin().cluster().prepareState().execute().actionGet().getState();
        recoveredWarmersMetaData = clusterState.metaData().index("test").custom(IndexWarmersMetaData.TYPE);
        assertThat(recoveredWarmersMetaData.entries().size(), equalTo(warmersMetaData.entries().size()));
        for (int i = 0; i < warmersMetaData.entries().size(); i++) {
            assertThat(recoveredWarmersMetaData.entries().get(i).name(), equalTo(warmersMetaData.entries().get(i).name()));
            assertThat(recoveredWarmersMetaData.entries().get(i).source(), equalTo(warmersMetaData.entries().get(i).source()));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6213.java