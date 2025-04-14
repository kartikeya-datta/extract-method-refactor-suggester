error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4708.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4708.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4708.java
text:
```scala
private static final b@@oolean TWO_NODES = true;

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

package org.elasticsearch.benchmark.mapping;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.jna.Natives;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.node.Node;
import org.elasticsearch.transport.TransportModule;

import static org.elasticsearch.cluster.metadata.IndexMetaData.SETTING_NUMBER_OF_REPLICAS;
import static org.elasticsearch.cluster.metadata.IndexMetaData.SETTING_NUMBER_OF_SHARDS;
import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

/**
 */
public class ManyMappingsBenchmark {

    private static final String MAPPING = "{\n" +
            "        \"dynamic_templates\": [\n" +
            "          {\n" +
            "            \"t1\": {\n" +
            "              \"mapping\": {\n" +
            "                \"store\": false,\n" +
            "                \"norms\": {\n" +
            "                  \"enabled\": false\n" +
            "                },\n" +
            "                \"type\": \"string\"\n" +
            "              },\n" +
            "              \"match\": \"*_ss\"\n" +
            "            }\n" +
            "          },\n" +
            "          {\n" +
            "            \"t2\": {\n" +
            "              \"mapping\": {\n" +
            "                \"store\": false,\n" +
            "                \"type\": \"date\"\n" +
            "              },\n" +
            "              \"match\": \"*_dt\"\n" +
            "            }\n" +
            "          },\n" +
            "          {\n" +
            "            \"t3\": {\n" +
            "              \"mapping\": {\n" +
            "                \"store\": false,\n" +
            "                \"type\": \"integer\"\n" +
            "              },\n" +
            "              \"match\": \"*_i\"\n" +
            "            }\n" +
            "          }\n" +
            "        ],\n" +
            "        \"_source\": {\n" +
            "          \"enabled\": false\n" +
            "        },\n" +
            "        \"properties\": {}\n" +
            "      }";

    private static final String INDEX_NAME = "index";
    private static final String TYPE_NAME = "type";
    private static final int FIELD_COUNT = 100000;
    private static final int DOC_COUNT = 10000000;
    private static final boolean TWO_NODES = false;

    public static void main(String[] args) throws Exception {
        System.setProperty("es.logger.prefix", "");
        Natives.tryMlockall();
        Settings settings = settingsBuilder()
                .put("gateway.type", "local")
                .put(SETTING_NUMBER_OF_SHARDS, 5)
                .put(SETTING_NUMBER_OF_REPLICAS, 0)
                .build();

        String clusterName = ManyMappingsBenchmark.class.getSimpleName();
        Node node = nodeBuilder().clusterName(clusterName)
                .settings(settingsBuilder().put(settings))
                .node();
        if (TWO_NODES) {
            Node node2 = nodeBuilder().clusterName(clusterName)
                    .settings(settingsBuilder().put(settings))
                    .node();
        }

        Client client = node.client();

        client.admin().indices().prepareDelete(INDEX_NAME)
                .setIndicesOptions(IndicesOptions.lenientExpandOpen())
                .get();
        client.admin().indices().prepareCreate(INDEX_NAME)
                .addMapping(TYPE_NAME, MAPPING)
                .get();

        BulkRequestBuilder builder = client.prepareBulk();
        int fieldCount = 0;
        long time = System.currentTimeMillis();
        final int PRINT = 1000;
        for (int i = 0; i < DOC_COUNT; i++) {
            XContentBuilder sourceBuilder = jsonBuilder().startObject();
            sourceBuilder.field(++fieldCount + "_ss", "xyz");
            sourceBuilder.field(++fieldCount + "_dt", System.currentTimeMillis());
            sourceBuilder.field(++fieldCount + "_i", i % 100);
            sourceBuilder.endObject();

            if (fieldCount >= FIELD_COUNT) {
                fieldCount = 0;
                System.out.println("dynamic fields rolled up");
            }

            builder.add(
                    client.prepareIndex(INDEX_NAME, TYPE_NAME, String.valueOf(i))
                            .setSource(sourceBuilder)
            );

            if (builder.numberOfActions() >= 1000) {
                builder.get();
                builder = client.prepareBulk();
            }

            if (i % PRINT == 0) {
                long took = System.currentTimeMillis() - time;
                time = System.currentTimeMillis();
                System.out.println("Indexed " + i +  " docs, in " + TimeValue.timeValueMillis(took));
            }
        }
        if (builder.numberOfActions() > 0) {
            builder.get();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4708.java