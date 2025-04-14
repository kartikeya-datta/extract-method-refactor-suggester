error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/410.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/410.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/410.java
text:
```scala
r@@eturn settingsBuilder().put(super.nodeSettings(nodeOrdinal)).put("plugin.types", CustomScriptPlugin.class.getName()).build();

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

package org.elasticsearch.script;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.plugins.AbstractPlugin;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.elasticsearch.test.ElasticsearchIntegrationTest.ClusterScope;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.elasticsearch.test.ElasticsearchIntegrationTest.*;
import static org.hamcrest.Matchers.equalTo;

@ClusterScope(scope = Scope.SUITE, numDataNodes = 3)
public class ScriptFieldTests extends ElasticsearchIntegrationTest {

    @Override
    protected Settings nodeSettings(int nodeOrdinal) {
        return settingsBuilder().put("plugin.types", CustomScriptPlugin.class.getName()).put(super.nodeSettings(nodeOrdinal)).build();
    }

    static int[] intArray = { Integer.MAX_VALUE, Integer.MIN_VALUE, 3 };
    static long[] longArray = { Long.MAX_VALUE, Long.MIN_VALUE, 9223372036854775807l };
    static float[] floatArray = { Float.MAX_VALUE, Float.MIN_VALUE, 3.3f };
    static double[] doubleArray = { Double.MAX_VALUE, Double.MIN_VALUE, 3.3d };

    public void testNativeScript() throws InterruptedException, ExecutionException {

        indexRandom(true, client().prepareIndex("test", "type1", "1").setSource("text", "doc1"), client()
                .prepareIndex("test", "type1", "2").setSource("text", "doc2"),
                client().prepareIndex("test", "type1", "3").setSource("text", "doc3"), client().prepareIndex("test", "type1", "4")
                        .setSource("text", "doc4"), client().prepareIndex("test", "type1", "5").setSource("text", "doc5"), client()
                        .prepareIndex("test", "type1", "6").setSource("text", "doc6"));

        client().admin().indices().prepareFlush("test").execute().actionGet();
        SearchResponse sr = client().prepareSearch("test").setQuery(QueryBuilders.matchAllQuery())
                .addScriptField("int", "native", "int", null).addScriptField("float", "native", "float", null)
                .addScriptField("double", "native", "double", null).addScriptField("long", "native", "long", null).execute().actionGet();
        assertThat(sr.getHits().hits().length, equalTo(6));
        for (SearchHit hit : sr.getHits().getHits()) {
            Object result = hit.getFields().get("int").getValues().get(0);
            assertThat(result, equalTo((Object) intArray));
            result = hit.getFields().get("long").getValues().get(0);
            assertThat(result, equalTo((Object) longArray));
            result = hit.getFields().get("float").getValues().get(0);
            assertThat(result, equalTo((Object) floatArray));
            result = hit.getFields().get("double").getValues().get(0);
            assertThat(result, equalTo((Object) doubleArray));
        }
    }

    static class IntArrayScriptFactory implements NativeScriptFactory {
        @Override
        public ExecutableScript newScript(@Nullable Map<String, Object> params) {
            return new IntScript();
        }
    }

    static class IntScript extends AbstractSearchScript {
        @Override
        public Object run() {
            return intArray;
        }
    }

    static class LongArrayScriptFactory implements NativeScriptFactory {
        @Override
        public ExecutableScript newScript(@Nullable Map<String, Object> params) {
            return new LongScript();
        }
    }

    static class LongScript extends AbstractSearchScript {
        @Override
        public Object run() {
            return longArray;
        }
    }

    static class FloatArrayScriptFactory implements NativeScriptFactory {
        @Override
        public ExecutableScript newScript(@Nullable Map<String, Object> params) {
            return new FloatScript();
        }
    }

    static class FloatScript extends AbstractSearchScript {
        @Override
        public Object run() {
            return floatArray;
        }
    }

    static class DoubleArrayScriptFactory implements NativeScriptFactory {
        @Override
        public ExecutableScript newScript(@Nullable Map<String, Object> params) {
            return new DoubleScript();
        }
    }

    static class DoubleScript extends AbstractSearchScript {
        @Override
        public Object run() {
            return doubleArray;
        }
    }

    public static class CustomScriptPlugin extends AbstractPlugin {

        @Override
        public String name() {
            return "custom_script";
        }

        @Override
        public String description() {
            return "script ";
        }

        public void onModule(ScriptModule scriptModule) {
            scriptModule.registerScript("int", IntArrayScriptFactory.class);
            scriptModule.registerScript("long", LongArrayScriptFactory.class);
            scriptModule.registerScript("float", FloatArrayScriptFactory.class);
            scriptModule.registerScript("double", DoubleArrayScriptFactory.class);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/410.java