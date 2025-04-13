error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7455.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7455.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7455.java
text:
```scala
S@@et<String> indices = new HashSet<>();

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
package org.elasticsearch.indices.template;

import com.carrotsearch.randomizedtesting.LifecycleScope;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.common.io.Streams;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.elasticsearch.test.ElasticsearchIntegrationTest.ClusterScope;
import org.elasticsearch.test.ElasticsearchIntegrationTest.Scope;
import org.junit.Test;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@ClusterScope(scope=Scope.TEST, numNodes=1)
public class IndexTemplateFileLoadingTests extends ElasticsearchIntegrationTest {

    @Override
    protected Settings nodeSettings(int nodeOrdinal) {
        ImmutableSettings.Builder settingsBuilder = ImmutableSettings.settingsBuilder();
        settingsBuilder.put(super.nodeSettings(nodeOrdinal));

        try {
            File directory = newTempDir(LifecycleScope.SUITE);
            settingsBuilder.put("path.conf", directory.getPath());

            File templatesDir = new File(directory + File.separator + "templates");
            templatesDir.mkdir();

            File dst = new File(templatesDir, "template.json");
            String templatePath = "/org/elasticsearch/indices/template/template" + randomInt(5) + ".json";
            logger.info("Picking template path [{}]", templatePath);
            // random template, one uses the 'setting.index.number_of_shards', the other 'settings.number_of_shards'
            String template = Streams.copyToStringFromClasspath(templatePath);
            Files.write(template, dst, Charsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return settingsBuilder.build();
    }

    @Override
    protected int numberOfShards() {
        //number of shards won't be set through index settings, the one from the index templates needs to be used
        return -1;
    }

    @Override
    protected int numberOfReplicas() {
        //number of replicas won't be set through index settings, the one from the index templates needs to be used
        return -1;
    }

    @Test
    public void testThatLoadingTemplateFromFileWorks() throws Exception {
        final int iters = scaledRandomIntBetween(5, 20);
        Set<String> indices = new HashSet<String>();
        for (int i = 0; i < iters; i++) {
            String indexName = "foo" + randomRealisticUnicodeOfLengthBetween(0, 5);
            if (indices.contains(indexName)) {
                continue;
            }
            indices.add(indexName);
            createIndex(indexName);
            ensureYellow(); // ensuring yellow so the test fails faster if the template cannot be loaded

            ClusterStateResponse stateResponse = client().admin().cluster().prepareState().setIndices(indexName).get();
            assertThat(stateResponse.getState().getMetaData().indices().get(indexName).getNumberOfShards(), is(10));
            assertThat(stateResponse.getState().getMetaData().indices().get(indexName).getNumberOfReplicas(), is(0));
            assertThat(stateResponse.getState().getMetaData().indices().get(indexName).aliases().size(), equalTo(1));
            String aliasName = indexName + "-alias";
            assertThat(stateResponse.getState().getMetaData().indices().get(indexName).aliases().get(aliasName).alias(), equalTo(aliasName));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7455.java