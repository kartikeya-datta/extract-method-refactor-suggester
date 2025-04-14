error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1720.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1720.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1720.java
text:
```scala
c@@reateIndex("test");

/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
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

package org.elasticsearch.explain;

import org.elasticsearch.action.explain.ExplainResponse;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.test.AbstractIntegrationTest;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;

import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.queryString;
import static org.hamcrest.Matchers.equalTo;

/**
 */
public class ExplainActionTests extends AbstractIntegrationTest {


    @Test
    public void testSimple() throws Exception {
        client().admin().indices().prepareCreate("test").setSettings(
                ImmutableSettings.settingsBuilder().put("index.refresh_interval", -1)
        ).execute().actionGet();
        client().admin().cluster().prepareHealth("test").setWaitForGreenStatus().execute().actionGet();

        client().prepareIndex("test", "test", "1")
                .setSource("field", "value1")
                .execute().actionGet();

        ExplainResponse response = client().prepareExplain("test", "test", "1")
                .setQuery(QueryBuilders.matchAllQuery())
                .execute().actionGet();
        assertNotNull(response);
        assertFalse(response.isExists()); // not a match b/c not realtime
        assertFalse(response.isMatch()); // not a match b/c not realtime

        client().admin().indices().prepareRefresh("test").execute().actionGet();
        response = client().prepareExplain("test", "test", "1")
                .setQuery(QueryBuilders.matchAllQuery())
                .execute().actionGet();
        assertNotNull(response);
        assertTrue(response.isMatch());
        assertNotNull(response.getExplanation());
        assertTrue(response.getExplanation().isMatch());
        assertThat(response.getExplanation().getValue(), equalTo(1.0f));

        client().admin().indices().prepareRefresh("test").execute().actionGet();
        response = client().prepareExplain("test", "test", "1")
                .setQuery(QueryBuilders.termQuery("field", "value2"))
                .execute().actionGet();
        assertNotNull(response);
        assertTrue(response.isExists());
        assertFalse(response.isMatch());
        assertNotNull(response.getExplanation());
        assertFalse(response.getExplanation().isMatch());

        client().admin().indices().prepareRefresh("test").execute().actionGet();
        response = client().prepareExplain("test", "test", "1")
                .setQuery(QueryBuilders.boolQuery()
                        .must(QueryBuilders.termQuery("field", "value1"))
                        .must(QueryBuilders.termQuery("field", "value2"))
                )
                .execute().actionGet();
        assertNotNull(response);
        assertTrue(response.isExists());
        assertFalse(response.isMatch());
        assertNotNull(response.getExplanation());
        assertFalse(response.getExplanation().isMatch());
        assertThat(response.getExplanation().getDetails().length, equalTo(2));

        response = client().prepareExplain("test", "test", "2")
                .setQuery(QueryBuilders.matchAllQuery())
                .execute().actionGet();
        assertNotNull(response);
        assertFalse(response.isExists());
        assertFalse(response.isMatch());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testExplainWithFields() throws Exception {
        client().admin().indices().prepareCreate("test").execute().actionGet();
        client().admin().cluster().prepareHealth("test").setWaitForGreenStatus().execute().actionGet();

        client().prepareIndex("test", "test", "1")
                .setSource(
                        jsonBuilder().startObject()
                                .startObject("obj1")
                                .field("field1", "value1")
                                .field("field2", "value2")
                                .endObject()
                                .endObject()
                ).execute().actionGet();

        client().admin().indices().prepareRefresh("test").execute().actionGet();
        ExplainResponse response = client().prepareExplain("test", "test", "1")
                .setQuery(QueryBuilders.matchAllQuery())
                .setFields("obj1.field1")
                .execute().actionGet();
        assertNotNull(response);
        assertTrue(response.isMatch());
        assertNotNull(response.getExplanation());
        assertTrue(response.getExplanation().isMatch());
        assertThat(response.getExplanation().getValue(), equalTo(1.0f));
        assertThat(response.getGetResult().isExists(), equalTo(true));
        assertThat(response.getGetResult().getId(), equalTo("1"));
        assertThat(response.getGetResult().getFields().size(), equalTo(1));
        assertThat(response.getGetResult().getFields().get("obj1.field1").getValue().toString(), equalTo("value1"));
        assertThat(response.getGetResult().isSourceEmpty(), equalTo(true));

        client().admin().indices().prepareRefresh("test").execute().actionGet();
        response = client().prepareExplain("test", "test", "1")
                .setQuery(QueryBuilders.matchAllQuery())
                .setFields("obj1.field1")
                .setFetchSource(true)
                .get();
        assertNotNull(response);
        assertTrue(response.isMatch());
        assertNotNull(response.getExplanation());
        assertTrue(response.getExplanation().isMatch());
        assertThat(response.getExplanation().getValue(), equalTo(1.0f));
        assertThat(response.getGetResult().isExists(), equalTo(true));
        assertThat(response.getGetResult().getId(), equalTo("1"));
        assertThat(response.getGetResult().getFields().size(), equalTo(1));
        assertThat(response.getGetResult().getFields().get("obj1.field1").getValue().toString(), equalTo("value1"));
        assertThat(response.getGetResult().isSourceEmpty(), equalTo(false));

        response = client().prepareExplain("test", "test", "1")
                .setQuery(QueryBuilders.matchAllQuery())
                .setFields("_source.obj1")
                .execute().actionGet();
        assertNotNull(response);
        assertTrue(response.isMatch());
        assertThat(response.getGetResult().getFields().size(), equalTo(1));
        Map<String, String> fields = (Map<String, String>) response.getGetResult().field("_source.obj1").getValue();
        assertThat(fields.size(), equalTo(2));
        assertThat(fields.get("field1"), equalTo("value1"));
        assertThat(fields.get("field2"), equalTo("value2"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testExplainWitSource() throws Exception {
        client().admin().indices().prepareCreate("test").execute().actionGet();
        client().admin().cluster().prepareHealth("test").setWaitForGreenStatus().execute().actionGet();

        client().prepareIndex("test", "test", "1")
                .setSource(
                        jsonBuilder().startObject()
                                .startObject("obj1")
                                .field("field1", "value1")
                                .field("field2", "value2")
                                .endObject()
                                .endObject()
                ).execute().actionGet();

        client().admin().indices().prepareRefresh("test").execute().actionGet();
        ExplainResponse response = client().prepareExplain("test", "test", "1")
                .setQuery(QueryBuilders.matchAllQuery())
                .setFetchSource("obj1.field1", null)
                .get();
        assertNotNull(response);
        assertTrue(response.isMatch());
        assertNotNull(response.getExplanation());
        assertTrue(response.getExplanation().isMatch());
        assertThat(response.getExplanation().getValue(), equalTo(1.0f));
        assertThat(response.getGetResult().isExists(), equalTo(true));
        assertThat(response.getGetResult().getId(), equalTo("1"));
        assertThat(response.getGetResult().getSource().size(), equalTo(1));
        assertThat(((Map<String, Object>) response.getGetResult().getSource().get("obj1")).get("field1").toString(), equalTo("value1"));

        response = client().prepareExplain("test", "test", "1")
                .setQuery(QueryBuilders.matchAllQuery())
                .setFetchSource(null, "obj1.field2")
                .execute().actionGet();
        assertNotNull(response);
        assertTrue(response.isMatch());
        assertThat(((Map<String, Object>) response.getGetResult().getSource().get("obj1")).get("field1").toString(), equalTo("value1"));
    }


    @Test
    public void testExplainWithAlias() throws Exception {
        client().admin().indices().prepareCreate("test")
                .execute().actionGet();
        client().admin().cluster().prepareHealth("test").setWaitForGreenStatus().execute().actionGet();

        client().admin().indices().prepareAliases().addAlias("test", "alias1", FilterBuilders.termFilter("field2", "value2"))
                .execute().actionGet();
        client().prepareIndex("test", "test", "1").setSource("field1", "value1", "field2", "value1").execute().actionGet();
        client().admin().indices().prepareRefresh("test").execute().actionGet();

        ExplainResponse response = client().prepareExplain("alias1", "test", "1")
                .setQuery(QueryBuilders.matchAllQuery())
                .execute().actionGet();
        assertNotNull(response);
        assertTrue(response.isExists());
        assertFalse(response.isMatch());
    }

    @Test
    public void explainDateRangeInQueryString() {
        client().admin().indices().prepareCreate("test").setSettings(ImmutableSettings.settingsBuilder().put("index.number_of_shards", 1)).get();

        String aMonthAgo = ISODateTimeFormat.yearMonthDay().print(new DateTime(DateTimeZone.UTC).minusMonths(1));
        String aMonthFromNow = ISODateTimeFormat.yearMonthDay().print(new DateTime(DateTimeZone.UTC).plusMonths(1));

        client().prepareIndex("test", "type", "1").setSource("past", aMonthAgo, "future", aMonthFromNow).get();

        refresh();

        ExplainResponse explainResponse = client().prepareExplain("test", "type", "1").setQuery(queryString("past:[now-2M/d TO now/d]")).get();
        assertThat(explainResponse.isExists(), equalTo(true));
        assertThat(explainResponse.isMatch(), equalTo(true));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1720.java