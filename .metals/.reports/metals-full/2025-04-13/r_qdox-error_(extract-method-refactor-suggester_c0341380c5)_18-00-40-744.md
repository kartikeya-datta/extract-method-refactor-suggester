error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6259.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6259.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6259.java
text:
```scala
M@@atcher matcher = Pattern.compile("[^\\[\\]>]+").matcher("");

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

package org.elasticsearch.search.aggregations;

import com.carrotsearch.randomizedtesting.generators.RandomStrings;
import org.elasticsearch.action.search.SearchPhaseExecutionException;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsingTests extends ElasticsearchIntegrationTest {

    @Test(expected=SearchPhaseExecutionException.class)
    public void testTwoTypes() throws Exception {
        createIndex("idx");
        ensureGreen();
        client().prepareSearch("idx").setAggregations(JsonXContent.contentBuilder()
            .startObject()
                .startObject("in_stock")
                    .startObject("filter")
                        .startObject("range")
                            .startObject("stock")
                                .field("gt", 0)
                            .endObject()
                        .endObject()
                    .endObject()
                    .startObject("terms")
                        .field("field", "stock")
                    .endObject()
                .endObject()
            .endObject()).execute().actionGet();
    }

    @Test(expected=SearchPhaseExecutionException.class)
    public void testTwoAggs() throws Exception {
        createIndex("idx");
        ensureGreen();
        client().prepareSearch("idx").setAggregations(JsonXContent.contentBuilder()
            .startObject()
                .startObject("by_date")
                    .startObject("date_histogram")
                        .field("field", "timestamp")
                        .field("interval", "month")
                    .endObject()
                    .startObject("aggs")
                        .startObject("tag_count")
                            .startObject("cardinality")
                                .field("field", "tag")
                            .endObject()
                        .endObject()
                    .endObject()
                    .startObject("aggs") // 2nd "aggs": illegal
                        .startObject("tag_count2")
                            .startObject("cardinality")
                                .field("field", "tag")
                            .endObject()
                        .endObject()
                    .endObject()
            .endObject()).execute().actionGet();
    }

    @Test(expected=SearchPhaseExecutionException.class)
    public void testInvalidAggregationName() throws Exception {

        Matcher matcher = Pattern.compile("[a-zA-Z0-9\\-_]+").matcher("");
        String name;
        SecureRandom rand = new SecureRandom();
        int len = randomIntBetween(1, 5);
        char[] word = new char[len];
        while(true) {
            for (int i = 0; i < word.length; i++) {
                word[i] = (char) rand.nextInt(127);
            }
            name = String.valueOf(word);
            if (!matcher.reset(name).matches()) {
                break;
            }
        }

        createIndex("idx");
        ensureGreen();
        client().prepareSearch("idx").setAggregations(JsonXContent.contentBuilder()
            .startObject()
                .startObject(name)
                    .startObject("filter")
                        .startObject("range")
                            .startObject("stock")
                                .field("gt", 0)
                            .endObject()
                        .endObject()
                    .endObject()
            .endObject()).execute().actionGet();
    }

    @Test(expected=SearchPhaseExecutionException.class)
    public void testSameAggregationName() throws Exception {
        createIndex("idx");
        ensureGreen();
        final String name = RandomStrings.randomAsciiOfLength(getRandom(), 10);
        client().prepareSearch("idx").setAggregations(JsonXContent.contentBuilder()
            .startObject()
                .startObject(name)
                    .startObject("terms")
                        .field("field", "a")
                    .endObject()
                .endObject()
                .startObject(name)
                    .startObject("terms")
                        .field("field", "b")
                    .endObject()
                .endObject()
            .endObject()).execute().actionGet();
    }

    @Test(expected=SearchPhaseExecutionException.class)
    public void testMissingName() throws Exception {
        createIndex("idx");
        ensureGreen();
        client().prepareSearch("idx").setAggregations(JsonXContent.contentBuilder()
            .startObject()
                .startObject("by_date")
                    .startObject("date_histogram")
                        .field("field", "timestamp")
                        .field("interval", "month")
                    .endObject()
                    .startObject("aggs")
                        // the aggregation name is missing
                        //.startObject("tag_count")
                            .startObject("cardinality")
                                .field("field", "tag")
                            .endObject()
                        //.endObject()
                    .endObject()
            .endObject()).execute().actionGet();
    }

    @Test(expected=SearchPhaseExecutionException.class)
    public void testMissingType() throws Exception {
        createIndex("idx");
        ensureGreen();
        client().prepareSearch("idx").setAggregations(JsonXContent.contentBuilder()
            .startObject()
                .startObject("by_date")
                    .startObject("date_histogram")
                        .field("field", "timestamp")
                        .field("interval", "month")
                    .endObject()
                    .startObject("aggs")
                        .startObject("tag_count")
                            // the aggregation type is missing
                            //.startObject("cardinality")
                                .field("field", "tag")
                            //.endObject()
                        .endObject()
                    .endObject()
            .endObject()).execute().actionGet();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6259.java