error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8613.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8613.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8613.java
text:
```scala
a@@ssertAcked(prepareCreate("test").addMapping("type", "field1", "type=string"));

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
package org.elasticsearch.percolator;

import org.elasticsearch.action.ShardOperationFailedException;
import org.elasticsearch.action.percolate.MultiPercolateRequestBuilder;
import org.elasticsearch.action.percolate.MultiPercolateResponse;
import org.elasticsearch.action.percolate.PercolateSourceBuilder;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.junit.Test;

import java.io.IOException;

import static org.elasticsearch.action.percolate.PercolateSourceBuilder.docBuilder;
import static org.elasticsearch.common.xcontent.XContentFactory.*;
import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.percolator.PercolatorTests.convertFromTextArray;
import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.assertAcked;
import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.assertMatchCount;
import static org.hamcrest.Matchers.*;

/**
 */
public class MultiPercolatorTests extends ElasticsearchIntegrationTest {

    @Test
    public void testBasics() throws Exception {
        client().admin().indices().prepareCreate("test").execute().actionGet();
        ensureGreen();

        logger.info("--> register a queries");
        client().prepareIndex("test", PercolatorService.TYPE_NAME, "1")
                .setSource(jsonBuilder().startObject().field("query", matchQuery("field1", "b")).field("a", "b").endObject())
                .execute().actionGet();
        client().prepareIndex("test", PercolatorService.TYPE_NAME, "2")
                .setSource(jsonBuilder().startObject().field("query", matchQuery("field1", "c")).endObject())
                .execute().actionGet();
        client().prepareIndex("test", PercolatorService.TYPE_NAME, "3")
                .setSource(jsonBuilder().startObject().field("query", boolQuery()
                        .must(matchQuery("field1", "b"))
                        .must(matchQuery("field1", "c"))
                ).endObject())
                .execute().actionGet();
        client().prepareIndex("test", PercolatorService.TYPE_NAME, "4")
                .setSource(jsonBuilder().startObject().field("query", matchAllQuery()).endObject())
                .execute().actionGet();

        MultiPercolateResponse response = client().prepareMultiPercolate()
                .add(client().preparePercolate()
                        .setIndices("test").setDocumentType("type")
                        .setPercolateDoc(docBuilder().setDoc(jsonBuilder().startObject().field("field1", "b").endObject())))
                .add(client().preparePercolate()
                        .setIndices("test").setDocumentType("type")
                        .setPercolateDoc(docBuilder().setDoc(yamlBuilder().startObject().field("field1", "c").endObject())))
                .add(client().preparePercolate()
                        .setIndices("test").setDocumentType("type")
                        .setPercolateDoc(docBuilder().setDoc(smileBuilder().startObject().field("field1", "b c").endObject())))
                .add(client().preparePercolate()
                        .setIndices("test").setDocumentType("type")
                        .setPercolateDoc(docBuilder().setDoc(jsonBuilder().startObject().field("field1", "d").endObject())))
                .add(client().preparePercolate() // non existing doc, so error element
                        .setIndices("test").setDocumentType("type")
                        .setGetRequest(Requests.getRequest("test").type("type").id("5")))
                .execute().actionGet();

        MultiPercolateResponse.Item item = response.getItems()[0];
        assertMatchCount(item.response(), 2l);
        assertThat(item.getResponse().getMatches(), arrayWithSize(2));
        assertThat(item.errorMessage(), nullValue());
        assertThat(convertFromTextArray(item.getResponse().getMatches(), "test"), arrayContainingInAnyOrder("1", "4"));

        item = response.getItems()[1];
        assertThat(item.errorMessage(), nullValue());

        assertMatchCount(item.response(), 2l);
        assertThat(item.getResponse().getMatches(), arrayWithSize(2));
        assertThat(convertFromTextArray(item.getResponse().getMatches(), "test"), arrayContainingInAnyOrder("2", "4"));

        item = response.getItems()[2];
        assertThat(item.errorMessage(), nullValue());
        assertMatchCount(item.response(), 4l);
        assertThat(convertFromTextArray(item.getResponse().getMatches(), "test"), arrayContainingInAnyOrder("1", "2", "3", "4"));

        item = response.getItems()[3];
        assertThat(item.errorMessage(), nullValue());
        assertMatchCount(item.response(), 1l);
        assertThat(item.getResponse().getMatches(), arrayWithSize(1));
        assertThat(convertFromTextArray(item.getResponse().getMatches(), "test"), arrayContaining("4"));

        item = response.getItems()[4];
        assertThat(item.getResponse(), nullValue());
        assertThat(item.errorMessage(), notNullValue());
        assertThat(item.errorMessage(), containsString("document missing"));
    }

    @Test
    public void testExistingDocsOnly() throws Exception {
        createIndex("test");

        int numQueries = randomIntBetween(50, 100);
        logger.info("--> register a queries");
        for (int i = 0; i < numQueries; i++) {
            client().prepareIndex("test", PercolatorService.TYPE_NAME, Integer.toString(i))
                    .setSource(jsonBuilder().startObject().field("query", matchAllQuery()).endObject())
                    .execute().actionGet();
        }

        client().prepareIndex("test", "type", "1")
                .setSource(jsonBuilder().startObject().field("field", "a"))
                .execute().actionGet();

        MultiPercolateRequestBuilder builder = client().prepareMultiPercolate();
        int numPercolateRequest = randomIntBetween(50, 100);
        for (int i = 0; i < numPercolateRequest; i++) {
            builder.add(
                    client().preparePercolate()
                            .setGetRequest(Requests.getRequest("test").type("type").id("1"))
                            .setIndices("test").setDocumentType("type"));
        }

        MultiPercolateResponse response = builder.execute().actionGet();
        assertThat(response.items().length, equalTo(numPercolateRequest));
        for (MultiPercolateResponse.Item item : response) {
            assertThat(item.isFailure(), equalTo(false));
            assertMatchCount(item.response(), numQueries);
            assertThat(item.getResponse().getMatches().length, equalTo(numQueries));
        }

        // Non existing doc
        builder = client().prepareMultiPercolate();
        for (int i = 0; i < numPercolateRequest; i++) {
            builder.add(
                    client().preparePercolate()
                            .setGetRequest(Requests.getRequest("test").type("type").id("2"))
                            .setIndices("test").setDocumentType("type"));
        }

        response = builder.execute().actionGet();
        assertThat(response.items().length, equalTo(numPercolateRequest));
        for (MultiPercolateResponse.Item item : response) {
            assertThat(item.isFailure(), equalTo(true));
            assertThat(item.errorMessage(), containsString("document missing"));
            assertThat(item.getResponse(), nullValue());
        }

        // One existing doc
        builder = client().prepareMultiPercolate();
        for (int i = 0; i < numPercolateRequest; i++) {
            builder.add(
                    client().preparePercolate()
                            .setGetRequest(Requests.getRequest("test").type("type").id("2"))
                            .setIndices("test").setDocumentType("type"));
        }
        builder.add(
                client().preparePercolate()
                        .setGetRequest(Requests.getRequest("test").type("type").id("1"))
                        .setIndices("test").setDocumentType("type"));

        response = builder.execute().actionGet();
        assertThat(response.items().length, equalTo(numPercolateRequest + 1));
        assertThat(response.items()[numPercolateRequest].isFailure(), equalTo(false));
        assertMatchCount(response.items()[numPercolateRequest].response(), numQueries);
        assertThat(response.items()[numPercolateRequest].getResponse().getMatches().length, equalTo(numQueries));
    }

    @Test
    public void testWithDocsOnly() throws Exception {
        createIndex("test");
        ensureGreen();

        NumShards test = getNumShards("test");

        int numQueries = randomIntBetween(50, 100);
        logger.info("--> register a queries");
        for (int i = 0; i < numQueries; i++) {
            client().prepareIndex("test", PercolatorService.TYPE_NAME, Integer.toString(i))
                    .setSource(jsonBuilder().startObject().field("query", matchAllQuery()).endObject())
                    .execute().actionGet();
        }

        MultiPercolateRequestBuilder builder = client().prepareMultiPercolate();
        int numPercolateRequest = randomIntBetween(50, 100);
        for (int i = 0; i < numPercolateRequest; i++) {
            builder.add(
                    client().preparePercolate()
                            .setIndices("test").setDocumentType("type")
                            .setPercolateDoc(docBuilder().setDoc(jsonBuilder().startObject().field("field", "a").endObject())));
        }

        MultiPercolateResponse response = builder.execute().actionGet();
        assertThat(response.items().length, equalTo(numPercolateRequest));
        for (MultiPercolateResponse.Item item : response) {
            assertThat(item.isFailure(), equalTo(false));
            assertMatchCount(item.response(), numQueries);
            assertThat(item.getResponse().getMatches().length, equalTo(numQueries));
        }

        // All illegal json
        builder = client().prepareMultiPercolate();
        for (int i = 0; i < numPercolateRequest; i++) {
            builder.add(
                    client().preparePercolate()
                            .setIndices("test").setDocumentType("type")
                            .setSource("illegal json"));
        }

        response = builder.execute().actionGet();
        assertThat(response.items().length, equalTo(numPercolateRequest));
        for (MultiPercolateResponse.Item item : response) {
            assertThat(item.isFailure(), equalTo(false));
            assertThat(item.getResponse().getSuccessfulShards(), equalTo(0));
            assertThat(item.getResponse().getShardFailures().length, equalTo(test.numPrimaries));
            for (ShardOperationFailedException shardFailure : item.getResponse().getShardFailures()) {
                assertThat(shardFailure.reason(), containsString("Failed to derive xcontent from"));
                assertThat(shardFailure.status().getStatus(), equalTo(500));
            }
        }

        // one valid request
        builder = client().prepareMultiPercolate();
        for (int i = 0; i < numPercolateRequest; i++) {
            builder.add(
                    client().preparePercolate()
                            .setIndices("test").setDocumentType("type")
                            .setSource("illegal json"));
        }
        builder.add(
                client().preparePercolate()
                        .setIndices("test").setDocumentType("type")
                        .setPercolateDoc(docBuilder().setDoc(jsonBuilder().startObject().field("field", "a").endObject())));

        response = builder.execute().actionGet();
        assertThat(response.items().length, equalTo(numPercolateRequest + 1));
        assertThat(response.items()[numPercolateRequest].isFailure(), equalTo(false));
        assertMatchCount(response.items()[numPercolateRequest].response(), numQueries);
        assertThat(response.items()[numPercolateRequest].getResponse().getMatches().length, equalTo(numQueries));
    }


    @Test
    public void testNestedMultiPercolation() throws IOException {
        initNestedIndexAndPercolation();
        MultiPercolateRequestBuilder mpercolate= client().prepareMultiPercolate();
        mpercolate.add(client().preparePercolate().setPercolateDoc(new PercolateSourceBuilder.DocBuilder().setDoc(getNotMatchingNestedDoc())).setIndices("nestedindex").setDocumentType("company"));
        mpercolate.add(client().preparePercolate().setPercolateDoc(new PercolateSourceBuilder.DocBuilder().setDoc(getMatchingNestedDoc())).setIndices("nestedindex").setDocumentType("company"));
        MultiPercolateResponse response = mpercolate.get();
        assertEquals(response.getItems()[0].getResponse().getMatches().length, 0);
        assertEquals(response.getItems()[1].getResponse().getMatches().length, 1);
        assertEquals(response.getItems()[1].getResponse().getMatches()[0].getId().string(), "Q");
    }

    void initNestedIndexAndPercolation() throws IOException {
        XContentBuilder mapping = XContentFactory.jsonBuilder();
        mapping.startObject().startObject("properties").startObject("companyname").field("type", "string").endObject()
                .startObject("employee").field("type", "nested").startObject("properties")
                .startObject("name").field("type", "string").endObject().endObject().endObject().endObject()
                .endObject();

        assertAcked(client().admin().indices().prepareCreate("nestedindex").addMapping("company", mapping));
        ensureGreen("nestedindex");

        client().prepareIndex("nestedindex", PercolatorService.TYPE_NAME, "Q").setSource(jsonBuilder().startObject()
                .field("query", QueryBuilders.nestedQuery("employee", QueryBuilders.matchQuery("employee.name", "virginia potts").operator(MatchQueryBuilder.Operator.AND)).scoreMode("avg")).endObject()).get();

        refresh();

    }

    XContentBuilder getMatchingNestedDoc() throws IOException {
        XContentBuilder doc = XContentFactory.jsonBuilder();
        doc.startObject().field("companyname", "stark").startArray("employee")
                .startObject().field("name", "virginia potts").endObject()
                .startObject().field("name", "tony stark").endObject()
                .endArray().endObject();
        return doc;
    }

    XContentBuilder getNotMatchingNestedDoc() throws IOException {
        XContentBuilder doc = XContentFactory.jsonBuilder();
        doc.startObject().field("companyname", "notstark").startArray("employee")
                .startObject().field("name", "virginia stark").endObject()
                .startObject().field("name", "tony potts").endObject()
                .endArray().endObject();
        return doc;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8613.java