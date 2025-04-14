error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6565.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6565.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6565.java
text:
```scala
M@@etaData parsedMetaData = MetaData.Builder.fromXContent(XContentFactory.xContent(XContentType.JSON).createParser(metaDataSource));

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

package org.elasticsearch.cluster.metadata;

import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.elasticsearch.cluster.metadata.IndexMetaData.*;
import static org.elasticsearch.cluster.metadata.MetaData.*;
import static org.elasticsearch.common.settings.ImmutableSettings.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author kimchy (shay.banon)
 */
@Test
public class ToAndFromJsonMetaDataTests {

    @Test
    public void testSimpleJsonFromAndTo() throws IOException {
        MetaData metaData = newMetaDataBuilder()
                .put(newIndexMetaDataBuilder("test1")
                        .numberOfShards(1)
                        .numberOfReplicas(2))
                .put(newIndexMetaDataBuilder("test2")
                        .settings(settingsBuilder().put("setting1", "value1").put("setting2", "value2"))
                        .numberOfShards(2)
                        .numberOfReplicas(3))
                .put(newIndexMetaDataBuilder("test3")
                        .numberOfShards(1)
                        .numberOfReplicas(2)
                        .putMapping("mapping1", MAPPING_SOURCE1))
                .put(newIndexMetaDataBuilder("test4")
                        .settings(settingsBuilder().put("setting1", "value1").put("setting2", "value2"))
                        .numberOfShards(1)
                        .numberOfReplicas(2)
                        .putMapping("mapping1", MAPPING_SOURCE1)
                        .putMapping("mapping2", MAPPING_SOURCE2))
                .build();

        String metaDataSource = MetaData.Builder.toXContent(metaData);
        System.out.println("ToJson: " + metaDataSource);

        MetaData parsedMetaData = MetaData.Builder.fromXContent(XContentFactory.xContent(XContentType.JSON).createParser(metaDataSource), null);

        IndexMetaData indexMetaData = parsedMetaData.index("test1");
        assertThat(indexMetaData.numberOfShards(), equalTo(1));
        assertThat(indexMetaData.numberOfReplicas(), equalTo(2));
        assertThat(indexMetaData.settings().getAsMap().size(), equalTo(2));
        assertThat(indexMetaData.mappings().size(), equalTo(0));

        indexMetaData = parsedMetaData.index("test2");
        assertThat(indexMetaData.numberOfShards(), equalTo(2));
        assertThat(indexMetaData.numberOfReplicas(), equalTo(3));
        assertThat(indexMetaData.settings().getAsMap().size(), equalTo(4));
        assertThat(indexMetaData.settings().get("setting1"), equalTo("value1"));
        assertThat(indexMetaData.settings().get("setting2"), equalTo("value2"));
        assertThat(indexMetaData.mappings().size(), equalTo(0));

        indexMetaData = parsedMetaData.index("test3");
        assertThat(indexMetaData.numberOfShards(), equalTo(1));
        assertThat(indexMetaData.numberOfReplicas(), equalTo(2));
        assertThat(indexMetaData.settings().getAsMap().size(), equalTo(2));
        assertThat(indexMetaData.mappings().size(), equalTo(1));
        assertThat(indexMetaData.mappings().get("mapping1").source().string(), equalTo(MAPPING_SOURCE1));

        indexMetaData = parsedMetaData.index("test4");
        assertThat(indexMetaData.numberOfShards(), equalTo(1));
        assertThat(indexMetaData.numberOfReplicas(), equalTo(2));
        assertThat(indexMetaData.settings().getAsMap().size(), equalTo(4));
        assertThat(indexMetaData.settings().get("setting1"), equalTo("value1"));
        assertThat(indexMetaData.settings().get("setting2"), equalTo("value2"));
        assertThat(indexMetaData.mappings().size(), equalTo(2));
        assertThat(indexMetaData.mappings().get("mapping1").source().string(), equalTo(MAPPING_SOURCE1));
        assertThat(indexMetaData.mappings().get("mapping2").source().string(), equalTo(MAPPING_SOURCE2));
    }

    private static final String MAPPING_SOURCE1 = "{\"mapping1\":{\"text1\":{\"type\":\"string\"}}}";
    private static final String MAPPING_SOURCE2 = "{\"mapping2\":{\"text2\":{\"type\":\"string\"}}}";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6565.java