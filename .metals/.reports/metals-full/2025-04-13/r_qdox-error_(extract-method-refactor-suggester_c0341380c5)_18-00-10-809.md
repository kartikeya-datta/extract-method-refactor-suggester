error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8523.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8523.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8523.java
text:
```scala
i@@ndex("test", "type", "" + i, jsonBuilder().startObject().endObject());

/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.test.integration.search.functionscore;

import org.apache.lucene.util._TestUtil;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.functionscore.random.RandomScoreFunctionBuilder;
import org.elasticsearch.test.integration.AbstractSharedClusterTest;
import org.hamcrest.CoreMatchers;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.functionScoreQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

public class RandomScoreFunctionTests extends AbstractSharedClusterTest {

    @Override
    public Settings getSettings() {
        return randomSettingsBuilder()
                .put("index.number_of_shards", 5)
                .put("index.number_of_replicas", 2)
                .build();
    }

    @Override
    protected int numberOfNodes() {
        return 3;
    }

    @Test
    public void consistentHitsWithSameSeed() throws Exception {
        prepareCreate("test").execute().actionGet();
        ensureGreen();

        int docCount = atLeast(100);

        for (int i = 0; i < docCount; i++) {
            index("test", "type", "" + docCount, jsonBuilder().startObject().endObject());
        }

        flush();

        long seed = System.nanoTime();
        String preference = _TestUtil.randomRealisticUnicodeString(getRandom());

        float[] scores = null;

        for (int i = 0; i < 3; i++) {

            SearchResponse searchResponse = client().prepareSearch()
                    .setPreference(preference)
                    .setQuery(functionScoreQuery(matchAllQuery()).add(new RandomScoreFunctionBuilder().seed(seed)))
                    .execute().actionGet();

            assertThat("Failures " + Arrays.toString(searchResponse.getShardFailures()), searchResponse.getShardFailures().length, CoreMatchers.equalTo(0));

            int hitCount = searchResponse.getHits().getHits().length;

            if (scores == null) {

                scores = new float[hitCount];
                for (int j = 0; j < hitCount; j++) {
                    scores[j] = searchResponse.getHits().getAt(j).score();
                }
            } else {
                for (int j = 0; j < hitCount; j++) {
                    assertThat(searchResponse.getHits().getAt(j).score(), CoreMatchers.equalTo(scores[j]));
                }
            }
        }
    }

    @Test @Ignore
    public void distribution() throws Exception {
        int count = 10000;

        prepareCreate("test").execute().actionGet();
        ensureGreen();

        for (int i = 0; i < count; i++) {
            index("test", "type", "" + i, jsonBuilder().startObject().endObject());
        }

        flush();

        int[] matrix = new int[count];

        for (int i = 0; i < count; i++) {

            SearchResponse searchResponse = client().prepareSearch()
                    .setQuery(functionScoreQuery(matchAllQuery()).add(new RandomScoreFunctionBuilder().seed(System.nanoTime())))
                    .execute().actionGet();

            matrix[Integer.valueOf(searchResponse.getHits().getAt(0).id())]++;
        }

        int filled = 0;
        int maxRepeat = 0;
        for (int i = 0; i < matrix.length; i++) {
            int value = matrix[i];
            maxRepeat = Math.max(maxRepeat, value);
            if (value > 0) {
                filled++;
            }
        }
        System.out.println();
        System.out.println("max repeat: " +  maxRepeat);
        System.out.println("distribution: " +  filled/(double)count);

        int percentile50 = filled / 2;
        int percentile25 = (filled / 4);
        int percentile75 = percentile50 + percentile25;

        int sum = 0;

        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i] == 0) {
                continue;
            }
            sum += i * matrix[i];
            if (percentile50 == 0) {
                System.out.println("median: " + i);
            } else if (percentile25 == 0) {
                System.out.println("percentile_25: " + i);
            } else if (percentile75 == 0) {
                System.out.println("percentile_75: " + i);
            }
            percentile50--;
            percentile25--;
            percentile75--;
        }

        System.out.println("mean: " + sum/(double)count);

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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8523.java