error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8741.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8741.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8741.java
text:
```scala
@Test public v@@oid testPythonFilter() throws Exception {

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

package org.elasticsearch.script.python;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.client.Requests.*;
import static org.elasticsearch.common.xcontent.XContentFactory.*;
import static org.elasticsearch.index.query.xcontent.FilterBuilders.*;
import static org.elasticsearch.index.query.xcontent.QueryBuilders.*;
import static org.elasticsearch.search.builder.SearchSourceBuilder.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author kimchy (shay.banon)
 */
public class PythonScriptSearchTests {

    protected final ESLogger logger = Loggers.getLogger(getClass());

    private Node node;

    private Client client;

    @BeforeMethod public void createNodes() throws Exception {
        node = NodeBuilder.nodeBuilder().settings(ImmutableSettings.settingsBuilder().put("gateway.type", "none").put("number_of_shards", 1)).node();
        client = node.client();
    }

    @AfterMethod public void closeNodes() {
        client.close();
        node.close();
    }

    @Test public void testJavaScriptFilter() throws Exception {
        client.admin().indices().prepareCreate("test").execute().actionGet();
        client.prepareIndex("test", "type1", "1")
                .setSource(jsonBuilder().startObject().field("test", "value beck").field("num1", 1.0f).endObject())
                .execute().actionGet();
        client.admin().indices().prepareFlush().execute().actionGet();
        client.prepareIndex("test", "type1", "2")
                .setSource(jsonBuilder().startObject().field("test", "value beck").field("num1", 2.0f).endObject())
                .execute().actionGet();
        client.admin().indices().prepareFlush().execute().actionGet();
        client.prepareIndex("test", "type1", "3")
                .setSource(jsonBuilder().startObject().field("test", "value beck").field("num1", 3.0f).endObject())
                .execute().actionGet();
        client.admin().indices().refresh(refreshRequest()).actionGet();

        logger.info("running doc['num1'].value > 1");
        SearchResponse response = client.prepareSearch()
                .setQuery(filtered(matchAllQuery(), scriptFilter("doc['num1'].value > 1").lang("python")))
                .addSort("num1", SortOrder.ASC)
                .addScriptField("sNum1", "python", "doc['num1'].value", null)
                .execute().actionGet();

        assertThat(response.hits().totalHits(), equalTo(2l));
        assertThat(response.hits().getAt(0).id(), equalTo("2"));
        assertThat((Double) response.hits().getAt(0).fields().get("sNum1").values().get(0), equalTo(2.0));
        assertThat(response.hits().getAt(1).id(), equalTo("3"));
        assertThat((Double) response.hits().getAt(1).fields().get("sNum1").values().get(0), equalTo(3.0));

        logger.info("running doc['num1'].value > param1");
        response = client.prepareSearch()
                .setQuery(filtered(matchAllQuery(), scriptFilter("doc['num1'].value > param1").lang("python").addParam("param1", 2)))
                .addSort("num1", SortOrder.ASC)
                .addScriptField("sNum1", "python", "doc['num1'].value", null)
                .execute().actionGet();

        assertThat(response.hits().totalHits(), equalTo(1l));
        assertThat(response.hits().getAt(0).id(), equalTo("3"));
        assertThat((Double) response.hits().getAt(0).fields().get("sNum1").values().get(0), equalTo(3.0));

        logger.info("running doc['num1'].value > param1");
        response = client.prepareSearch()
                .setQuery(filtered(matchAllQuery(), scriptFilter("doc['num1'].value > param1").lang("python").addParam("param1", -1)))
                .addSort("num1", SortOrder.ASC)
                .addScriptField("sNum1", "python", "doc['num1'].value", null)
                .execute().actionGet();

        assertThat(response.hits().totalHits(), equalTo(3l));
        assertThat(response.hits().getAt(0).id(), equalTo("1"));
        assertThat((Double) response.hits().getAt(0).fields().get("sNum1").values().get(0), equalTo(1.0));
        assertThat(response.hits().getAt(1).id(), equalTo("2"));
        assertThat((Double) response.hits().getAt(1).fields().get("sNum1").values().get(0), equalTo(2.0));
        assertThat(response.hits().getAt(2).id(), equalTo("3"));
        assertThat((Double) response.hits().getAt(2).fields().get("sNum1").values().get(0), equalTo(3.0));
    }

    @Test public void testScriptFieldUsingSource() throws Exception {
        client.admin().indices().prepareCreate("test").execute().actionGet();
        client.prepareIndex("test", "type1", "1")
                .setSource(jsonBuilder().startObject()
                        .startObject("obj1").field("test", "something").endObject()
                        .startObject("obj2").startArray("arr2").value("arr_value1").value("arr_value2").endArray().endObject()
                        .endObject())
                .execute().actionGet();
        client.admin().indices().refresh(refreshRequest()).actionGet();

        SearchResponse response = client.prepareSearch()
                .setQuery(matchAllQuery())
                .addField("_source.obj1") // we also automatically detect _source in fields
                .addScriptField("s_obj1", "python", "_source['obj1']", null)
                .addScriptField("s_obj1_test", "python", "_source['obj1']['test']", null)
                .addScriptField("s_obj2", "python", "_source['obj2']", null)
                .addScriptField("s_obj2_arr2", "python", "_source['obj2']['arr2']", null)
                .execute().actionGet();

        Map<String, Object> sObj1 = (Map<String, Object>) response.hits().getAt(0).field("_source.obj1").value();
        assertThat(sObj1.get("test").toString(), equalTo("something"));
        assertThat(response.hits().getAt(0).field("s_obj1_test").value().toString(), equalTo("something"));

        sObj1 = (Map<String, Object>) response.hits().getAt(0).field("s_obj1").value();
        assertThat(sObj1.get("test").toString(), equalTo("something"));
        assertThat(response.hits().getAt(0).field("s_obj1_test").value().toString(), equalTo("something"));

        Map<String, Object> sObj2 = (Map<String, Object>) response.hits().getAt(0).field("s_obj2").value();
        List sObj2Arr2 = (List) sObj2.get("arr2");
        assertThat(sObj2Arr2.size(), equalTo(2));
        assertThat(sObj2Arr2.get(0).toString(), equalTo("arr_value1"));
        assertThat(sObj2Arr2.get(1).toString(), equalTo("arr_value2"));

        sObj2Arr2 = (List) response.hits().getAt(0).field("s_obj2_arr2").value();
        assertThat(sObj2Arr2.size(), equalTo(2));
        assertThat(sObj2Arr2.get(0).toString(), equalTo("arr_value1"));
        assertThat(sObj2Arr2.get(1).toString(), equalTo("arr_value2"));
    }

    @Test public void testCustomScriptBoost() throws Exception {
        // execute a search before we create an index
        try {
            client.prepareSearch().setQuery(termQuery("test", "value")).execute().actionGet();
            assert false : "should fail";
        } catch (Exception e) {
            // ignore, no indices
        }

        try {
            client.prepareSearch("test").setQuery(termQuery("test", "value")).execute().actionGet();
            assert false : "should fail";
        } catch (Exception e) {
            // ignore, no indices
        }

        client.admin().indices().create(createIndexRequest("test")).actionGet();
        client.index(indexRequest("test").type("type1").id("1")
                .source(jsonBuilder().startObject().field("test", "value beck").field("num1", 1.0f).endObject())).actionGet();
        client.index(indexRequest("test").type("type1").id("2")
                .source(jsonBuilder().startObject().field("test", "value check").field("num1", 2.0f).endObject())).actionGet();
        client.admin().indices().refresh(refreshRequest()).actionGet();

        logger.info("--- QUERY_THEN_FETCH");

        logger.info("running doc['num1'].value");
        SearchResponse response = client.search(searchRequest()
                .searchType(SearchType.QUERY_THEN_FETCH)
                .source(searchSource().explain(true).query(customScoreQuery(termQuery("test", "value")).script("doc['num1'].value").lang("python")))
        ).actionGet();

        assertThat("Failures " + Arrays.toString(response.shardFailures()), response.shardFailures().length, equalTo(0));

        assertThat(response.hits().totalHits(), equalTo(2l));
        logger.info("Hit[0] {} Explanation {}", response.hits().getAt(0).id(), response.hits().getAt(0).explanation());
        logger.info("Hit[1] {} Explanation {}", response.hits().getAt(1).id(), response.hits().getAt(1).explanation());
        assertThat(response.hits().getAt(0).id(), equalTo("2"));
        assertThat(response.hits().getAt(1).id(), equalTo("1"));

        logger.info("running -doc['num1'].value");
        response = client.search(searchRequest()
                .searchType(SearchType.QUERY_THEN_FETCH)
                .source(searchSource().explain(true).query(customScoreQuery(termQuery("test", "value")).script("-doc['num1'].value").lang("python")))
        ).actionGet();

        assertThat("Failures " + Arrays.toString(response.shardFailures()), response.shardFailures().length, equalTo(0));

        assertThat(response.hits().totalHits(), equalTo(2l));
        logger.info("Hit[0] {} Explanation {}", response.hits().getAt(0).id(), response.hits().getAt(0).explanation());
        logger.info("Hit[1] {} Explanation {}", response.hits().getAt(1).id(), response.hits().getAt(1).explanation());
        assertThat(response.hits().getAt(0).id(), equalTo("1"));
        assertThat(response.hits().getAt(1).id(), equalTo("2"));


        logger.info("running doc['num1'].value * score");
        response = client.search(searchRequest()
                .searchType(SearchType.QUERY_THEN_FETCH)
                .source(searchSource().explain(true).query(customScoreQuery(termQuery("test", "value")).script("doc['num1'].value * score").lang("python")))
        ).actionGet();

        assertThat("Failures " + Arrays.toString(response.shardFailures()), response.shardFailures().length, equalTo(0));

        assertThat(response.hits().totalHits(), equalTo(2l));
        logger.info("Hit[0] {} Explanation {}", response.hits().getAt(0).id(), response.hits().getAt(0).explanation());
        logger.info("Hit[1] {} Explanation {}", response.hits().getAt(1).id(), response.hits().getAt(1).explanation());
        assertThat(response.hits().getAt(0).id(), equalTo("2"));
        assertThat(response.hits().getAt(1).id(), equalTo("1"));

        logger.info("running param1 * param2 * score");
        response = client.search(searchRequest()
                .searchType(SearchType.QUERY_THEN_FETCH)
                .source(searchSource().explain(true).query(customScoreQuery(termQuery("test", "value")).script("param1 * param2 * score").param("param1", 2).param("param2", 2).lang("python")))
        ).actionGet();

        assertThat("Failures " + Arrays.toString(response.shardFailures()), response.shardFailures().length, equalTo(0));

        assertThat(response.hits().totalHits(), equalTo(2l));
        logger.info("Hit[0] {} Explanation {}", response.hits().getAt(0).id(), response.hits().getAt(0).explanation());
        logger.info("Hit[1] {} Explanation {}", response.hits().getAt(1).id(), response.hits().getAt(1).explanation());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8741.java