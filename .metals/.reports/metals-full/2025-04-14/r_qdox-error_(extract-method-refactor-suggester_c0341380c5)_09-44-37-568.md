error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6214.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6214.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6214.java
text:
```scala
.@@addPartialField("partial2", null, "obj1.*")

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

package org.elasticsearch.search.fields;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.Base64;
import org.elasticsearch.common.Priority;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.collect.MapBuilder;
import org.elasticsearch.common.joda.Joda;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.client.Requests.refreshRequest;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.assertNoFailures;
import static org.hamcrest.Matchers.*;

/**
 *
 */
public class SearchFieldsTests extends ElasticsearchIntegrationTest {

    @Override
    public Settings indexSettings() {
        return ImmutableSettings.builder()
                .put("index.number_of_shards", 1) // why just one?
                .put("index.number_of_replicas", 0)
                .build();
    }

    @Test
    public void testStoredFields() throws Exception {
        createIndex("test");
        client().admin().cluster().prepareHealth().setWaitForEvents(Priority.LANGUID).setWaitForYellowStatus().execute().actionGet();

        String mapping = XContentFactory.jsonBuilder().startObject().startObject("type1").startObject("properties")
                .startObject("field1").field("type", "string").field("store", "yes").endObject()
                .startObject("field2").field("type", "string").field("store", "no").endObject()
                .startObject("field3").field("type", "string").field("store", "yes").endObject()
                .endObject().endObject().endObject().string();

        client().admin().indices().preparePutMapping().setType("type1").setSource(mapping).execute().actionGet();

        client().prepareIndex("test", "type1", "1").setSource(jsonBuilder().startObject()
                .field("field1", "value1")
                .field("field2", "value2")
                .field("field3", "value3")
                .endObject()).execute().actionGet();

        client().admin().indices().prepareRefresh().execute().actionGet();

        SearchResponse searchResponse = client().prepareSearch().setQuery(matchAllQuery()).addField("field1").execute().actionGet();
        assertThat(searchResponse.getHits().getTotalHits(), equalTo(1l));
        assertThat(searchResponse.getHits().hits().length, equalTo(1));
        assertThat(searchResponse.getHits().getAt(0).fields().size(), equalTo(1));
        assertThat(searchResponse.getHits().getAt(0).fields().get("field1").value().toString(), equalTo("value1"));

        // field2 is not stored, check that it gets extracted from source
        searchResponse = client().prepareSearch().setQuery(matchAllQuery()).addField("field2").execute().actionGet();
        assertThat(searchResponse.getHits().getTotalHits(), equalTo(1l));
        assertThat(searchResponse.getHits().hits().length, equalTo(1));
        assertThat(searchResponse.getHits().getAt(0).fields().size(), equalTo(1));
        assertThat(searchResponse.getHits().getAt(0).fields().get("field2").value().toString(), equalTo("value2"));

        searchResponse = client().prepareSearch().setQuery(matchAllQuery()).addField("field3").execute().actionGet();
        assertThat(searchResponse.getHits().getTotalHits(), equalTo(1l));
        assertThat(searchResponse.getHits().hits().length, equalTo(1));
        assertThat(searchResponse.getHits().getAt(0).fields().size(), equalTo(1));
        assertThat(searchResponse.getHits().getAt(0).fields().get("field3").value().toString(), equalTo("value3"));

        searchResponse = client().prepareSearch().setQuery(matchAllQuery()).addField("*").execute().actionGet();
        assertThat(searchResponse.getHits().getTotalHits(), equalTo(1l));
        assertThat(searchResponse.getHits().hits().length, equalTo(1));
        assertThat(searchResponse.getHits().getAt(0).source(), nullValue());
        assertThat(searchResponse.getHits().getAt(0).fields().size(), equalTo(2));
        assertThat(searchResponse.getHits().getAt(0).fields().get("field1").value().toString(), equalTo("value1"));
        assertThat(searchResponse.getHits().getAt(0).fields().get("field3").value().toString(), equalTo("value3"));

        searchResponse = client().prepareSearch().setQuery(matchAllQuery()).addField("*").addField("_source").execute().actionGet();
        assertThat(searchResponse.getHits().getTotalHits(), equalTo(1l));
        assertThat(searchResponse.getHits().hits().length, equalTo(1));
        assertThat(searchResponse.getHits().getAt(0).source(), notNullValue());
        assertThat(searchResponse.getHits().getAt(0).fields().size(), equalTo(2));
        assertThat(searchResponse.getHits().getAt(0).fields().get("field1").value().toString(), equalTo("value1"));
        assertThat(searchResponse.getHits().getAt(0).fields().get("field3").value().toString(), equalTo("value3"));
    }

    @Test
    public void testScriptDocAndFields() throws Exception {
        createIndex("test");
        client().admin().cluster().prepareHealth().setWaitForEvents(Priority.LANGUID).setWaitForYellowStatus().execute().actionGet();

        String mapping = XContentFactory.jsonBuilder().startObject().startObject("type1").startObject("properties")
                .startObject("num1").field("type", "double").field("store", "yes").endObject()
                .endObject().endObject().endObject().string();

        client().admin().indices().preparePutMapping().setType("type1").setSource(mapping).execute().actionGet();

        client().prepareIndex("test", "type1", "1")
                .setSource(jsonBuilder().startObject().field("test", "value beck").field("num1", 1.0f).field("date", "1970-01-01T00:00:00").endObject())
                .execute().actionGet();
        client().admin().indices().prepareFlush().execute().actionGet();
        client().prepareIndex("test", "type1", "2")
                .setSource(jsonBuilder().startObject().field("test", "value beck").field("num1", 2.0f).field("date", "1970-01-01T00:00:25").endObject())
                .execute().actionGet();
        client().admin().indices().prepareFlush().execute().actionGet();
        client().prepareIndex("test", "type1", "3")
                .setSource(jsonBuilder().startObject().field("test", "value beck").field("num1", 3.0f).field("date", "1970-01-01T00:02:00").endObject())
                .execute().actionGet();
        client().admin().indices().refresh(refreshRequest()).actionGet();

        logger.info("running doc['num1'].value");
        SearchResponse response = client().prepareSearch()
                .setQuery(matchAllQuery())
                .addSort("num1", SortOrder.ASC)
                .addScriptField("sNum1", "doc['num1'].value")
                .addScriptField("sNum1_field", "_fields['num1'].value")
                .addScriptField("date1", "doc['date'].date.millis")
                .execute().actionGet();

        assertNoFailures(response);

        assertThat(response.getHits().totalHits(), equalTo(3l));
        assertThat(response.getHits().getAt(0).isSourceEmpty(), equalTo(true));
        assertThat(response.getHits().getAt(0).id(), equalTo("1"));
        assertThat((Double) response.getHits().getAt(0).fields().get("sNum1").values().get(0), equalTo(1.0));
        assertThat((Double) response.getHits().getAt(0).fields().get("sNum1_field").values().get(0), equalTo(1.0));
        assertThat((Long) response.getHits().getAt(0).fields().get("date1").values().get(0), equalTo(0l));
        assertThat(response.getHits().getAt(1).id(), equalTo("2"));
        assertThat((Double) response.getHits().getAt(1).fields().get("sNum1").values().get(0), equalTo(2.0));
        assertThat((Double) response.getHits().getAt(1).fields().get("sNum1_field").values().get(0), equalTo(2.0));
        assertThat((Long) response.getHits().getAt(1).fields().get("date1").values().get(0), equalTo(25000l));
        assertThat(response.getHits().getAt(2).id(), equalTo("3"));
        assertThat((Double) response.getHits().getAt(2).fields().get("sNum1").values().get(0), equalTo(3.0));
        assertThat((Double) response.getHits().getAt(2).fields().get("sNum1_field").values().get(0), equalTo(3.0));
        assertThat((Long) response.getHits().getAt(2).fields().get("date1").values().get(0), equalTo(120000l));

        logger.info("running doc['num1'].value * factor");
        Map<String, Object> params = MapBuilder.<String, Object>newMapBuilder().put("factor", 2.0).map();
        response = client().prepareSearch()
                .setQuery(matchAllQuery())
                .addSort("num1", SortOrder.ASC)
                .addScriptField("sNum1", "doc['num1'].value * factor", params)
                .execute().actionGet();

        assertThat(response.getHits().totalHits(), equalTo(3l));
        assertThat(response.getHits().getAt(0).id(), equalTo("1"));
        assertThat((Double) response.getHits().getAt(0).fields().get("sNum1").values().get(0), equalTo(2.0));
        assertThat(response.getHits().getAt(1).id(), equalTo("2"));
        assertThat((Double) response.getHits().getAt(1).fields().get("sNum1").values().get(0), equalTo(4.0));
        assertThat(response.getHits().getAt(2).id(), equalTo("3"));
        assertThat((Double) response.getHits().getAt(2).fields().get("sNum1").values().get(0), equalTo(6.0));
    }

    @Test
    public void testScriptFieldUsingSource() throws Exception {
        createIndex("test");
        client().admin().cluster().prepareHealth().setWaitForEvents(Priority.LANGUID).setWaitForYellowStatus().execute().actionGet();

        client().prepareIndex("test", "type1", "1")
                .setSource(jsonBuilder().startObject()
                        .startObject("obj1").field("test", "something").endObject()
                        .startObject("obj2").startArray("arr2").value("arr_value1").value("arr_value2").endArray().endObject()
                        .startArray("arr3").startObject().field("arr3_field1", "arr3_value1").endObject().endArray()
                        .endObject())
                .execute().actionGet();
        client().admin().indices().refresh(refreshRequest()).actionGet();

        SearchResponse response = client().prepareSearch()
                .setQuery(matchAllQuery())
                .addField("_source.obj1") // we also automatically detect _source in fields
                .addScriptField("s_obj1", "_source.obj1")
                .addScriptField("s_obj1_test", "_source.obj1.test")
                .addScriptField("s_obj2", "_source.obj2")
                .addScriptField("s_obj2_arr2", "_source.obj2.arr2")
                .addScriptField("s_arr3", "_source.arr3")
                .execute().actionGet();

        assertThat("Failures " + Arrays.toString(response.getShardFailures()), response.getShardFailures().length, equalTo(0));

        Map<String, Object> sObj1 = response.getHits().getAt(0).field("_source.obj1").value();
        assertThat(sObj1.get("test").toString(), equalTo("something"));
        assertThat(response.getHits().getAt(0).field("s_obj1_test").value().toString(), equalTo("something"));

        sObj1 = response.getHits().getAt(0).field("s_obj1").value();
        assertThat(sObj1.get("test").toString(), equalTo("something"));
        assertThat(response.getHits().getAt(0).field("s_obj1_test").value().toString(), equalTo("something"));

        Map<String, Object> sObj2 = response.getHits().getAt(0).field("s_obj2").value();
        List sObj2Arr2 = (List) sObj2.get("arr2");
        assertThat(sObj2Arr2.size(), equalTo(2));
        assertThat(sObj2Arr2.get(0).toString(), equalTo("arr_value1"));
        assertThat(sObj2Arr2.get(1).toString(), equalTo("arr_value2"));

        sObj2Arr2 = (List) response.getHits().getAt(0).field("s_obj2_arr2").value();
        assertThat(sObj2Arr2.size(), equalTo(2));
        assertThat(sObj2Arr2.get(0).toString(), equalTo("arr_value1"));
        assertThat(sObj2Arr2.get(1).toString(), equalTo("arr_value2"));

        List sObj2Arr3 = (List) response.getHits().getAt(0).field("s_arr3").value();
        assertThat(((Map) sObj2Arr3.get(0)).get("arr3_field1").toString(), equalTo("arr3_value1"));
    }

    @Test
    public void testPartialFields() throws Exception {
        createIndex("test");
        client().admin().cluster().prepareHealth().setWaitForEvents(Priority.LANGUID).setWaitForYellowStatus().execute().actionGet();


        client().prepareIndex("test", "type1", "1").setSource(XContentFactory.jsonBuilder().startObject()
                .field("field1", "value1")
                .startObject("obj1")
                .startArray("arr1")
                .startObject().startObject("obj2").field("field2", "value21").endObject().endObject()
                .startObject().startObject("obj2").field("field2", "value22").endObject().endObject()
                .endArray()
                .endObject()
                .endObject())
                .execute().actionGet();

        client().admin().indices().prepareRefresh().execute().actionGet();

        SearchResponse response = client().prepareSearch("test")
                .addPartialField("partial1", "obj1.arr1.*", null)
                .addPartialField("partial2", null, "obj1")
                .execute().actionGet();
        assertThat("Failures " + Arrays.toString(response.getShardFailures()), response.getShardFailures().length, equalTo(0));

        Map<String, Object> partial1 = response.getHits().getAt(0).field("partial1").value();
        assertThat(partial1, notNullValue());
        assertThat(partial1.containsKey("field1"), equalTo(false));
        assertThat(partial1.containsKey("obj1"), equalTo(true));
        assertThat(((Map) partial1.get("obj1")).get("arr1"), instanceOf(List.class));

        Map<String, Object> partial2 = response.getHits().getAt(0).field("partial2").value();
        assertThat(partial2, notNullValue());
        assertThat(partial2.containsKey("obj1"), equalTo(false));
        assertThat(partial2.containsKey("field1"), equalTo(true));
    }

    @Test
    public void testStoredFieldsWithoutSource() throws Exception {
        createIndex("test");
        client().admin().cluster().prepareHealth().setWaitForEvents(Priority.LANGUID).setWaitForYellowStatus().execute().actionGet();

        String mapping = XContentFactory.jsonBuilder().startObject().startObject("type1").startObject("properties")
                .startObject("_source").field("enabled", false).endObject()
                .startObject("byte_field").field("type", "byte").field("store", "yes").endObject()
                .startObject("short_field").field("type", "short").field("store", "yes").endObject()
                .startObject("integer_field").field("type", "integer").field("store", "yes").endObject()
                .startObject("long_field").field("type", "long").field("store", "yes").endObject()
                .startObject("float_field").field("type", "float").field("store", "yes").endObject()
                .startObject("double_field").field("type", "double").field("store", "yes").endObject()
                .startObject("date_field").field("type", "date").field("store", "yes").endObject()
                .startObject("boolean_field").field("type", "boolean").field("store", "yes").endObject()
                .startObject("binary_field").field("type", "binary").field("store", "yes").endObject()
                .endObject().endObject().endObject().string();

        client().admin().indices().preparePutMapping().setType("type1").setSource(mapping).execute().actionGet();

        client().prepareIndex("test", "type1", "1").setSource(jsonBuilder().startObject()
                .field("byte_field", (byte) 1)
                .field("short_field", (short) 2)
                .field("integer_field", 3)
                .field("long_field", 4l)
                .field("float_field", 5.0f)
                .field("double_field", 6.0d)
                .field("date_field", Joda.forPattern("dateOptionalTime").printer().print(new DateTime(2012, 3, 22, 0, 0, DateTimeZone.UTC)))
                .field("boolean_field", true)
                .field("binary_field", Base64.encodeBytes("testing text".getBytes("UTF8")))
                .endObject()).execute().actionGet();

        client().admin().indices().prepareRefresh().execute().actionGet();

        SearchResponse searchResponse = client().prepareSearch().setQuery(matchAllQuery())
                .addField("byte_field")
                .addField("short_field")
                .addField("integer_field")
                .addField("long_field")
                .addField("float_field")
                .addField("double_field")
                .addField("date_field")
                .addField("boolean_field")
                .addField("binary_field")
                .execute().actionGet();

        assertThat(searchResponse.getHits().getTotalHits(), equalTo(1l));
        assertThat(searchResponse.getHits().hits().length, equalTo(1));
        assertThat(searchResponse.getHits().getAt(0).fields().size(), equalTo(9));


        assertThat(searchResponse.getHits().getAt(0).fields().get("byte_field").value().toString(), equalTo("1"));
        assertThat(searchResponse.getHits().getAt(0).fields().get("short_field").value().toString(), equalTo("2"));
        assertThat(searchResponse.getHits().getAt(0).fields().get("integer_field").value(), equalTo((Object) 3));
        assertThat(searchResponse.getHits().getAt(0).fields().get("long_field").value(), equalTo((Object) 4l));
        assertThat(searchResponse.getHits().getAt(0).fields().get("float_field").value(), equalTo((Object) 5.0f));
        assertThat(searchResponse.getHits().getAt(0).fields().get("double_field").value(), equalTo((Object) 6.0d));
        String dateTime = Joda.forPattern("dateOptionalTime").printer().print(new DateTime(2012, 3, 22, 0, 0, DateTimeZone.UTC));
        assertThat(searchResponse.getHits().getAt(0).fields().get("date_field").value(), equalTo((Object) dateTime));
        assertThat(searchResponse.getHits().getAt(0).fields().get("boolean_field").value(), equalTo((Object) Boolean.TRUE));
        assertThat(((BytesReference) searchResponse.getHits().getAt(0).fields().get("binary_field").value()).toBytesArray(), equalTo((BytesReference) new BytesArray("testing text".getBytes("UTF8"))));

    }

    @Test
    public void testSearchFields_metaData() throws Exception {
        client().prepareIndex("my-index", "my-type1", "1")
                .setRouting("1")
                .setSource(jsonBuilder().startObject().field("field1", "value").endObject())
                .setRefresh(true)
                .get();

        SearchResponse searchResponse = client().prepareSearch("my-index")
                .setTypes("my-type1")
                .addField("field1").addField("_routing")
                .get();

        assertThat(searchResponse.getHits().totalHits(), equalTo(1l));
        assertThat(searchResponse.getHits().getAt(0).field("field1").isMetadataField(), equalTo(false));
        assertThat(searchResponse.getHits().getAt(0).field("field1").getValue().toString(), equalTo("value"));
        assertThat(searchResponse.getHits().getAt(0).field("_routing").isMetadataField(), equalTo(true));
        assertThat(searchResponse.getHits().getAt(0).field("_routing").getValue().toString(), equalTo("1"));
    }

    @Test
    public void testSearchFields_nonLeafField() throws Exception {
        client().prepareIndex("my-index", "my-type1", "1")
                .setSource(jsonBuilder().startObject().startObject("field1").field("field2", "value1").endObject().endObject())
                .setRefresh(true)
                .get();

        SearchResponse searchResponse = client().prepareSearch("my-index").setTypes("my-type1").addField("field1").get();
        assertThat(searchResponse.getShardFailures().length, equalTo(1));
        assertThat(searchResponse.getShardFailures()[0].status(), equalTo(RestStatus.BAD_REQUEST));
        assertThat(searchResponse.getShardFailures()[0].reason(), containsString("field [field1] isn't a leaf field"));
    }

    @Test
    public void testGetFields_complexField() throws Exception {
        client().admin().indices().prepareCreate("my-index")
                .setSettings(ImmutableSettings.settingsBuilder().put("index.refresh_interval", -1))
                .addMapping("my-type2", jsonBuilder().startObject().startObject("my-type2").startObject("properties")
                        .startObject("field1").field("type", "object")
                        .startObject("field2").field("type", "object")
                        .startObject("field3").field("type", "object")
                        .startObject("field4").field("type", "string").field("store", "yes")
                        .endObject()
                        .endObject()
                        .endObject()
                        .endObject().endObject().endObject())
                .get();

        BytesReference source = jsonBuilder().startObject()
                .startArray("field1")
                    .startObject()
                        .startObject("field2")
                            .startArray("field3")
                                .startObject()
                                    .field("field4", "value1")
                                .endObject()
                            .endArray()
                        .endObject()
                    .endObject()
                    .startObject()
                        .startObject("field2")
                            .startArray("field3")
                                .startObject()
                                    .field("field4", "value2")
                                .endObject()
                            .endArray()
                        .endObject()
                    .endObject()
                .endArray()
                .endObject().bytes();

        client().prepareIndex("my-index", "my-type1", "1").setSource(source).get();
        client().prepareIndex("my-index", "my-type2", "1").setRefresh(true).setSource(source).get();


        String field = "field1.field2.field3.field4";
        SearchResponse searchResponse = client().prepareSearch("my-index").setTypes("my-type1").addField(field).get();
        assertThat(searchResponse.getHits().totalHits(), equalTo(1l));
        assertThat(searchResponse.getHits().getAt(0).field(field).isMetadataField(), equalTo(false));
        assertThat(searchResponse.getHits().getAt(0).field(field).getValues().size(), equalTo(2));
        assertThat(searchResponse.getHits().getAt(0).field(field).getValues().get(0).toString(), equalTo("value1"));
        assertThat(searchResponse.getHits().getAt(0).field(field).getValues().get(1).toString(), equalTo("value2"));

        searchResponse = client().prepareSearch("my-index").setTypes("my-type2").addField(field).get();
        assertThat(searchResponse.getHits().totalHits(), equalTo(1l));
        assertThat(searchResponse.getHits().getAt(0).field(field).isMetadataField(), equalTo(false));
        assertThat(searchResponse.getHits().getAt(0).field(field).getValues().size(), equalTo(2));
        assertThat(searchResponse.getHits().getAt(0).field(field).getValues().get(0).toString(), equalTo("value1"));
        assertThat(searchResponse.getHits().getAt(0).field(field).getValues().get(1).toString(), equalTo("value2"));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6214.java