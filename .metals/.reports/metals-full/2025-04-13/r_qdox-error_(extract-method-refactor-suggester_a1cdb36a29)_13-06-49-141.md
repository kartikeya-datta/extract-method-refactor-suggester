error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2828.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2828.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2828.java
text:
```scala
a@@ssertThat(response.mappings().containsKey("index"), equalTo(true));

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

package org.elasticsearch.indices.mapping;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.common.Priority;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.junit.Test;

import java.io.IOException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.hamcrest.Matchers.*;

/**
 *
 */
public class SimpleGetMappingsTests extends ElasticsearchIntegrationTest {

    @Test
    public void getMappingsWhereThereAreNone() {
        createIndex("index");
        GetMappingsResponse response = client().admin().indices().prepareGetMappings().execute().actionGet();
        assertThat(response.mappings(), hasKey("index"));
        assertThat(response.mappings().get("index").size(), equalTo(0));
    }


    private XContentBuilder getMappingForType(String type) throws IOException {
        return jsonBuilder().startObject().startObject(type).startObject("properties")
                .startObject("field1").field("type", "string").endObject()
                .endObject().endObject().endObject();
    }


    @Test
    public void simpleGetMappings() throws Exception {
        client().admin().indices().prepareCreate("indexa")
                .addMapping("typeA", getMappingForType("typeA"))
                .addMapping("typeB", getMappingForType("typeB"))
                .addMapping("Atype", getMappingForType("Atype"))
                .addMapping("Btype", getMappingForType("Btype"))
                .execute().actionGet();
        client().admin().indices().prepareCreate("indexb")
                .addMapping("typeA", getMappingForType("typeA"))
                .addMapping("typeB", getMappingForType("typeB"))
                .addMapping("Atype", getMappingForType("Atype"))
                .addMapping("Btype", getMappingForType("Btype"))
                .execute().actionGet();

        ClusterHealthResponse clusterHealth = client().admin().cluster().prepareHealth().setWaitForEvents(Priority.LANGUID).setWaitForGreenStatus().execute().actionGet();
        assertThat(clusterHealth.isTimedOut(), equalTo(false));

        // Get all mappings
        GetMappingsResponse response = client().admin().indices().prepareGetMappings().execute().actionGet();
        assertThat(response.mappings().size(), equalTo(2));
        assertThat(response.mappings().get("indexa").size(), equalTo(4));
        assertThat(response.mappings().get("indexa").get("typeA"), notNullValue());
        assertThat(response.mappings().get("indexa").get("typeB"), notNullValue());
        assertThat(response.mappings().get("indexa").get("Atype"), notNullValue());
        assertThat(response.mappings().get("indexa").get("Btype"), notNullValue());
        assertThat(response.mappings().get("indexb").size(), equalTo(4));
        assertThat(response.mappings().get("indexb").get("typeA"), notNullValue());
        assertThat(response.mappings().get("indexb").get("typeB"), notNullValue());
        assertThat(response.mappings().get("indexb").get("Atype"), notNullValue());
        assertThat(response.mappings().get("indexb").get("Btype"), notNullValue());

        // Get all mappings, via wildcard support
        response = client().admin().indices().prepareGetMappings("*").setTypes("*").execute().actionGet();
        assertThat(response.mappings().size(), equalTo(2));
        assertThat(response.mappings().get("indexa").size(), equalTo(4));
        assertThat(response.mappings().get("indexa").get("typeA"), notNullValue());
        assertThat(response.mappings().get("indexa").get("typeB"), notNullValue());
        assertThat(response.mappings().get("indexa").get("Atype"), notNullValue());
        assertThat(response.mappings().get("indexa").get("Btype"), notNullValue());
        assertThat(response.mappings().get("indexb").size(), equalTo(4));
        assertThat(response.mappings().get("indexb").get("typeA"), notNullValue());
        assertThat(response.mappings().get("indexb").get("typeB"), notNullValue());
        assertThat(response.mappings().get("indexb").get("Atype"), notNullValue());
        assertThat(response.mappings().get("indexb").get("Btype"), notNullValue());

        // Get all typeA mappings in all indices
        response = client().admin().indices().prepareGetMappings("*").setTypes("typeA").execute().actionGet();
        assertThat(response.mappings().size(), equalTo(2));
        assertThat(response.mappings().get("indexa").size(), equalTo(1));
        assertThat(response.mappings().get("indexa").get("typeA"), notNullValue());
        assertThat(response.mappings().get("indexb").size(), equalTo(1));
        assertThat(response.mappings().get("indexb").get("typeA"), notNullValue());

        // Get all mappings in indexa
        response = client().admin().indices().prepareGetMappings("indexa").execute().actionGet();
        assertThat(response.mappings().size(), equalTo(1));
        assertThat(response.mappings().get("indexa").size(), equalTo(4));
        assertThat(response.mappings().get("indexa").get("typeA"), notNullValue());
        assertThat(response.mappings().get("indexa").get("typeB"), notNullValue());
        assertThat(response.mappings().get("indexa").get("Atype"), notNullValue());
        assertThat(response.mappings().get("indexa").get("Btype"), notNullValue());

        // Get all mappings beginning with A* in indexa
        response = client().admin().indices().prepareGetMappings("indexa").setTypes("A*").execute().actionGet();
        assertThat(response.mappings().size(), equalTo(1));
        assertThat(response.mappings().get("indexa").size(), equalTo(1));
        assertThat(response.mappings().get("indexa").get("Atype"), notNullValue());

        // Get all mappings beginning with B* in all indices
        response = client().admin().indices().prepareGetMappings().setTypes("B*").execute().actionGet();
        assertThat(response.mappings().size(), equalTo(2));
        assertThat(response.mappings().get("indexa").size(), equalTo(1));
        assertThat(response.mappings().get("indexa").get("Btype"), notNullValue());
        assertThat(response.mappings().get("indexb").size(), equalTo(1));
        assertThat(response.mappings().get("indexb").get("Btype"), notNullValue());

        // Get all mappings beginning with B* and A* in all indices
        response = client().admin().indices().prepareGetMappings().setTypes("B*", "A*").execute().actionGet();
        assertThat(response.mappings().size(), equalTo(2));
        assertThat(response.mappings().get("indexa").size(), equalTo(2));
        assertThat(response.mappings().get("indexa").get("Atype"), notNullValue());
        assertThat(response.mappings().get("indexa").get("Btype"), notNullValue());
        assertThat(response.mappings().get("indexb").size(), equalTo(2));
        assertThat(response.mappings().get("indexb").get("Atype"), notNullValue());
        assertThat(response.mappings().get("indexb").get("Btype"), notNullValue());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2828.java