error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3892.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3892.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3892.java
text:
```scala
D@@iscoverySettings discoverySettings = internalCluster().getInstance(DiscoverySettings.class);

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

package org.elasticsearch.cluster.settings;

import org.elasticsearch.action.admin.cluster.settings.ClusterUpdateSettingsResponse;
import org.elasticsearch.cluster.routing.allocation.decider.DisableAllocationDecider;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.discovery.DiscoverySettings;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.elasticsearch.test.ElasticsearchIntegrationTest.ClusterScope;
import static org.elasticsearch.test.ElasticsearchIntegrationTest.Scope.TEST;
import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.assertAcked;
import static org.hamcrest.Matchers.*;

@ClusterScope(scope = TEST)
public class ClusterSettingsTests extends ElasticsearchIntegrationTest {

    @Test
    public void clusterNonExistingSettingsUpdate() {
        String key1 = "no_idea_what_you_are_talking_about";
        int value1 = 10;

        ClusterUpdateSettingsResponse response = client().admin().cluster()
                .prepareUpdateSettings()
                .setTransientSettings(ImmutableSettings.builder().put(key1, value1).build())
                .get();

        assertAcked(response);
        assertThat(response.getTransientSettings().getAsMap().entrySet(), Matchers.emptyIterable());
    }

    @Test
    public void clusterSettingsUpdateResponse() {
        String key1 = "indices.cache.filter.size";
        int value1 = 10;

        String key2 = DisableAllocationDecider.CLUSTER_ROUTING_ALLOCATION_DISABLE_ALLOCATION;
        boolean value2 = true;

        Settings transientSettings1 = ImmutableSettings.builder().put(key1, value1).build();
        Settings persistentSettings1 = ImmutableSettings.builder().put(key2, value2).build();

        ClusterUpdateSettingsResponse response1 = client().admin().cluster()
                .prepareUpdateSettings()
                .setTransientSettings(transientSettings1)
                .setPersistentSettings(persistentSettings1)
                .execute()
                .actionGet();

        assertAcked(response1);
        assertThat(response1.getTransientSettings().get(key1), notNullValue());
        assertThat(response1.getTransientSettings().get(key2), nullValue());
        assertThat(response1.getPersistentSettings().get(key1), nullValue());
        assertThat(response1.getPersistentSettings().get(key2), notNullValue());

        Settings transientSettings2 = ImmutableSettings.builder().put(key1, value1).put(key2, value2).build();
        Settings persistentSettings2 = ImmutableSettings.EMPTY;

        ClusterUpdateSettingsResponse response2 = client().admin().cluster()
                .prepareUpdateSettings()
                .setTransientSettings(transientSettings2)
                .setPersistentSettings(persistentSettings2)
                .execute()
                .actionGet();

        assertAcked(response2);
        assertThat(response2.getTransientSettings().get(key1), notNullValue());
        assertThat(response2.getTransientSettings().get(key2), notNullValue());
        assertThat(response2.getPersistentSettings().get(key1), nullValue());
        assertThat(response2.getPersistentSettings().get(key2), nullValue());

        Settings transientSettings3 = ImmutableSettings.EMPTY;
        Settings persistentSettings3 = ImmutableSettings.builder().put(key1, value1).put(key2, value2).build();

        ClusterUpdateSettingsResponse response3 = client().admin().cluster()
                .prepareUpdateSettings()
                .setTransientSettings(transientSettings3)
                .setPersistentSettings(persistentSettings3)
                .execute()
                .actionGet();

        assertAcked(response3);
        assertThat(response3.getTransientSettings().get(key1), nullValue());
        assertThat(response3.getTransientSettings().get(key2), nullValue());
        assertThat(response3.getPersistentSettings().get(key1), notNullValue());
        assertThat(response3.getPersistentSettings().get(key2), notNullValue());
    }

    @Test
    public void testUpdateDiscoveryPublishTimeout() {

        DiscoverySettings discoverySettings = cluster().getInstance(DiscoverySettings.class);

        assertThat(discoverySettings.getPublishTimeout(), equalTo(DiscoverySettings.DEFAULT_PUBLISH_TIMEOUT));

        ClusterUpdateSettingsResponse response = client().admin().cluster()
                .prepareUpdateSettings()
                .setTransientSettings(ImmutableSettings.builder().put(DiscoverySettings.PUBLISH_TIMEOUT, "1s").build())
                .get();

        assertAcked(response);
        assertThat(response.getTransientSettings().getAsMap().get(DiscoverySettings.PUBLISH_TIMEOUT), equalTo("1s"));
        assertThat(discoverySettings.getPublishTimeout().seconds(), equalTo(1l));

        response = client().admin().cluster()
                .prepareUpdateSettings()
                .setTransientSettings(ImmutableSettings.builder().put(DiscoverySettings.PUBLISH_TIMEOUT, "whatever").build())
                .get();

        assertAcked(response);
        assertThat(response.getTransientSettings().getAsMap().entrySet(), Matchers.emptyIterable());
        assertThat(discoverySettings.getPublishTimeout().seconds(), equalTo(1l));

        response = client().admin().cluster()
                .prepareUpdateSettings()
                .setTransientSettings(ImmutableSettings.builder().put(DiscoverySettings.PUBLISH_TIMEOUT, -1).build())
                .get();

        assertAcked(response);
        assertThat(response.getTransientSettings().getAsMap().entrySet(), Matchers.emptyIterable());
        assertThat(discoverySettings.getPublishTimeout().seconds(), equalTo(1l));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3892.java