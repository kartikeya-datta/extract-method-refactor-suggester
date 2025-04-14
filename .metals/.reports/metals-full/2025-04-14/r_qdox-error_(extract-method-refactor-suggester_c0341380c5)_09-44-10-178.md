error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7546.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7546.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7546.java
text:
```scala
a@@ssertThat(nodesMap.size(), equalTo(immutableCluster().size()));

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
package org.elasticsearch.action.admin;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.cluster.node.hotthreads.NodeHotThreads;
import org.elasticsearch.action.admin.cluster.node.hotthreads.NodesHotThreadsRequestBuilder;
import org.elasticsearch.action.admin.cluster.node.hotthreads.NodesHotThreadsResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.elasticsearch.index.query.FilterBuilders.andFilter;
import static org.elasticsearch.index.query.FilterBuilders.notFilter;
import static org.elasticsearch.index.query.FilterBuilders.queryFilter;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.assertHitCount;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 */
public class HotThreadsTest extends ElasticsearchIntegrationTest {

    @Test
    public void testHotThreadsDontFail() throws ExecutionException, InterruptedException {
        /**
         * This test just checks if nothing crashes or gets stuck etc.
         */
        createIndex("test");
        final int iters = scaledRandomIntBetween(2, 20);
        final AtomicBoolean hasErrors = new AtomicBoolean(false);
        for (int i = 0; i < iters; i++) {
            final String type;
            NodesHotThreadsRequestBuilder nodesHotThreadsRequestBuilder = client().admin().cluster().prepareNodesHotThreads();
            if (randomBoolean()) {
                TimeValue timeValue = new TimeValue(rarely() ? randomIntBetween(500, 5000) : randomIntBetween(20, 500));
                nodesHotThreadsRequestBuilder.setInterval(timeValue);
            }
            if (randomBoolean()) {
                nodesHotThreadsRequestBuilder.setThreads(rarely() ? randomIntBetween(500, 5000) : randomIntBetween(1, 500));
            }
            if (randomBoolean()) {
                switch (randomIntBetween(0, 2)) {
                    case 2:
                        type = "cpu";
                        break;
                    case 1:
                        type = "wait";
                        break;
                    default:
                        type = "block";
                        break;
                }
                assertThat(type, notNullValue());
                nodesHotThreadsRequestBuilder.setType(type);
            } else {
                type = null;
            }
            final CountDownLatch latch = new CountDownLatch(1);
            nodesHotThreadsRequestBuilder.execute(new ActionListener<NodesHotThreadsResponse>() {
                @Override
                public void onResponse(NodesHotThreadsResponse nodeHotThreads) {
                    boolean success = false;
                    try {
                        assertThat(nodeHotThreads, notNullValue());
                        Map<String,NodeHotThreads> nodesMap = nodeHotThreads.getNodesMap();
                        assertThat(nodesMap.size(), equalTo(cluster().size()));
                        for (NodeHotThreads ht : nodeHotThreads) {
                            assertNotNull(ht.getHotThreads());
                            //logger.info(ht.getHotThreads());
                        }
                        success = true;
                    } finally {
                        if (!success) {
                            hasErrors.set(true);
                        }
                        latch.countDown();
                    }
                }

                @Override
                public void onFailure(Throwable e) {
                    logger.error("FAILED", e);
                    hasErrors.set(true);
                    latch.countDown();
                    fail();
                }
            });

            indexRandom(true,
                    client().prepareIndex("test", "type1", "1").setSource("field1", "value1"),
                    client().prepareIndex("test", "type1", "2").setSource("field1", "value2"),
                    client().prepareIndex("test", "type1", "3").setSource("field1", "value3"));
            ensureSearchable();
            while(latch.getCount() > 0) {
                assertHitCount(
                        client().prepareSearch()
                                .setQuery(matchAllQuery())
                                .setPostFilter(
                                        andFilter(
                                                queryFilter(matchAllQuery()),
                                                notFilter(andFilter(queryFilter(termQuery("field1", "value1")),
                                                        queryFilter(termQuery("field1", "value2")))))).get(),
                        3l);
            }
            latch.await();
            assertThat(hasErrors.get(), is(false));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7546.java