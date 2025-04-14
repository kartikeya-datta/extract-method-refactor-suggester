error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3793.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3793.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3793.java
text:
```scala
a@@ssertThat(mgetResponse.getResponses()[1].getFailure().getMessage(), equalTo("routing is required for [test]/[test]/[1]"));

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
package org.elasticsearch.mget;

import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.search.fetch.source.FetchSourceContext;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.assertAcked;
import static org.hamcrest.Matchers.*;

public class SimpleMgetTests extends ElasticsearchIntegrationTest {

    @Test
    public void testThatMgetShouldWorkWithOneIndexMissing() throws IOException {
        createIndex("test");
        ensureYellow();

        client().prepareIndex("test", "test", "1").setSource(jsonBuilder().startObject().field("foo", "bar").endObject()).setRefresh(true).execute().actionGet();

        MultiGetResponse mgetResponse = client().prepareMultiGet()
                .add(new MultiGetRequest.Item("test", "test", "1"))
                .add(new MultiGetRequest.Item("nonExistingIndex", "test", "1"))
                .execute().actionGet();
        assertThat(mgetResponse.getResponses().length, is(2));

        assertThat(mgetResponse.getResponses()[0].getIndex(), is("test"));
        assertThat(mgetResponse.getResponses()[0].isFailed(), is(false));

        assertThat(mgetResponse.getResponses()[1].getIndex(), is("nonExistingIndex"));
        assertThat(mgetResponse.getResponses()[1].isFailed(), is(true));
        assertThat(mgetResponse.getResponses()[1].getFailure().getMessage(), is("[nonExistingIndex] missing"));


        mgetResponse = client().prepareMultiGet()
                .add(new MultiGetRequest.Item("nonExistingIndex", "test", "1"))
                .execute().actionGet();
        assertThat(mgetResponse.getResponses().length, is(1));
        assertThat(mgetResponse.getResponses()[0].getIndex(), is("nonExistingIndex"));
        assertThat(mgetResponse.getResponses()[0].isFailed(), is(true));
        assertThat(mgetResponse.getResponses()[0].getFailure().getMessage(), is("[nonExistingIndex] missing"));

    }

    @Test
    public void testThatParentPerDocumentIsSupported() throws Exception {
        createIndex("test");
        ensureYellow();
        client().admin().indices().preparePutMapping("test").setType("test").setSource(jsonBuilder()
                .startObject()
                .startObject("test")
                .startObject("_parent")
                .field("type", "foo")
                .endObject()
                .endObject()
                .endObject()
        ).get();

        client().prepareIndex("test", "test", "1").setParent("4").setRefresh(true)
                .setSource(jsonBuilder().startObject().field("foo", "bar").endObject())
                .execute().actionGet();

        MultiGetResponse mgetResponse = client().prepareMultiGet()
                .add(new MultiGetRequest.Item("test", "test", "1").parent("4"))
                .add(new MultiGetRequest.Item("test", "test", "1"))
                .execute().actionGet();

        assertThat(mgetResponse.getResponses().length, is(2));
        assertThat(mgetResponse.getResponses()[0].isFailed(), is(false));
        assertThat(mgetResponse.getResponses()[0].getResponse().isExists(), is(true));

        assertThat(mgetResponse.getResponses()[1].isFailed(), is(true));
        assertThat(mgetResponse.getResponses()[1].getResponse(), nullValue());
        assertThat(mgetResponse.getResponses()[1].getFailure().getMessage(), equalTo("routing is required, but hasn't been specified"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testThatSourceFilteringIsSupported() throws Exception {
        createIndex("test");
        ensureYellow();
        BytesReference sourceBytesRef = jsonBuilder().startObject()
                .field("field", "1", "2")
                .startObject("included").field("field", "should be seen").field("hidden_field", "should not be seen").endObject()
                .field("excluded", "should not be seen")
                .endObject().bytes();
        for (int i = 0; i < 100; i++) {
            client().prepareIndex("test", "type", Integer.toString(i)).setSource(sourceBytesRef).get();
        }

        MultiGetRequestBuilder request = client().prepareMultiGet();
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                request.add(new MultiGetRequest.Item("test", "type", Integer.toString(i)).fetchSourceContext(new FetchSourceContext("included", "*.hidden_field")));
            } else {
                request.add(new MultiGetRequest.Item("test", "type", Integer.toString(i)).fetchSourceContext(new FetchSourceContext(false)));
            }
        }

        MultiGetResponse response = request.get();

        assertThat(response.getResponses().length, equalTo(100));
        for (int i = 0; i < 100; i++) {
            MultiGetItemResponse responseItem = response.getResponses()[i];
            if (i % 2 == 0) {
                Map<String, Object> source = responseItem.getResponse().getSourceAsMap();
                assertThat(source.size(), equalTo(1));
                assertThat(source, hasKey("included"));
                assertThat(((Map<String, Object>) source.get("included")).size(), equalTo(1));
                assertThat(((Map<String, Object>) source.get("included")), hasKey("field"));
            } else {
                assertThat(responseItem.getResponse().getSourceAsBytes(), nullValue());
            }
        }


    }

    @Test
    public void testThatRoutingPerDocumentIsSupported() throws Exception {
        assertAcked(prepareCreate("test").setSettings(ImmutableSettings.builder()
                .put(indexSettings())
                .put(IndexMetaData.SETTING_NUMBER_OF_SHARDS, between(2, DEFAULT_MAX_NUM_SHARDS))));
        ensureYellow();

        client().prepareIndex("test", "test", "1").setRefresh(true).setRouting("2")
                .setSource(jsonBuilder().startObject().field("foo", "bar").endObject())
                .execute().actionGet();

        MultiGetResponse mgetResponse = client().prepareMultiGet()
                .add(new MultiGetRequest.Item("test", "test", "1").routing("2"))
                .add(new MultiGetRequest.Item("test", "test", "1"))
                .execute().actionGet();

        assertThat(mgetResponse.getResponses().length, is(2));
        assertThat(mgetResponse.getResponses()[0].isFailed(), is(false));
        assertThat(mgetResponse.getResponses()[0].getResponse().isExists(), is(true));

        assertThat(mgetResponse.getResponses()[1].isFailed(), is(false));
        assertThat(mgetResponse.getResponses()[1].getResponse().isExists(), is(false));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3793.java