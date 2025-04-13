error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5712.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5712.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5712.java
text:
```scala
S@@earchResponse searchResponse = client().prepareSearch("mapped_idx", "unmapped_idx")

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

package org.elasticsearch.test.integration.search.facet.terms;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.search.facet.FacetBuilders.termsFacet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.IOException;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.Priority;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.search.facet.terms.TermsFacet;
import org.elasticsearch.test.integration.AbstractSharedClusterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 */
public class UnmappedFieldsTermsFacetsTests extends AbstractSharedClusterTest {
    
    @Override
    public Settings getSettings() {
        return randomSettingsBuilder()
                .put("index.number_of_shards", numberOfShards())
                .put("index.number_of_replicas", 0)
                .build();
    }

    protected int numberOfShards() {
        return 5;
    }
    
    @Override
    protected int numberOfNodes() {
        return 1;
    }

    /**
     * Tests the terms facet when faceting on unmapped field
     */
    @Test
    public void testUnmappedField() throws Exception {
        createIndex("idx");
        client().admin().cluster().prepareHealth().setWaitForEvents(Priority.LANGUID).setWaitForGreenStatus().execute().actionGet();

        for (int i = 0; i < 10; i++) {
            client().prepareIndex("idx", "type", ""+i).setSource(jsonBuilder().startObject()
                    .field("mapped", ""+i)
                    .endObject()).execute().actionGet();
        }

        client().admin().indices().prepareFlush().setRefresh(true).execute().actionGet();
        SearchResponse searchResponse = client().prepareSearch("idx")
                .setQuery(matchAllQuery())
                .addFacet(termsFacet("mapped").field("mapped").size(10))
                .addFacet(termsFacet("unmapped_bool").field("unmapped_bool").size(10))
                .addFacet(termsFacet("unmapped_str").field("unmapped_str").size(10))
                .addFacet(termsFacet("unmapped_byte").field("unmapped_byte").size(10))
                .addFacet(termsFacet("unmapped_short").field("unmapped_short").size(10))
                .addFacet(termsFacet("unmapped_int").field("unmapped_int").size(10))
                .addFacet(termsFacet("unmapped_long").field("unmapped_long").size(10))
                .addFacet(termsFacet("unmapped_float").field("unmapped_float").size(10))
                .addFacet(termsFacet("unmapped_double").field("unmapped_double").size(10))
                .execute().actionGet();

        assertThat(searchResponse.getHits().getTotalHits(), equalTo(10l));

        // all values should be returned for the mapped field
        TermsFacet facet = searchResponse.getFacets().facet("mapped");
        assertThat(facet.getName(), equalTo("mapped"));
        assertThat(facet.getEntries().size(), is(10));
        assertThat(facet.getTotalCount(), is(10l));
        assertThat(facet.getMissingCount(), is(0l));

        // no values should be returned for the unmapped field (all docs are missing)

        facet = searchResponse.getFacets().facet("unmapped_str");
        assertThat(facet.getName(), equalTo("unmapped_str"));
        assertThat(facet.getEntries().size(), is(0));
        assertThat(facet.getTotalCount(), is(0l));
        assertThat(facet.getOtherCount(), is(0l));
        assertThat(facet.getMissingCount(), is(10l));

        facet = searchResponse.getFacets().facet("unmapped_bool");
        assertThat(facet.getName(), equalTo("unmapped_bool"));
        assertThat(facet.getEntries().size(), is(0));
        assertThat(facet.getTotalCount(), is(0l));
        assertThat(facet.getOtherCount(), is(0l));
        assertThat(facet.getMissingCount(), is(10l));

        facet = searchResponse.getFacets().facet("unmapped_byte");
        assertThat(facet.getName(), equalTo("unmapped_byte"));
        assertThat(facet.getEntries().size(), is(0));
        assertThat(facet.getTotalCount(), is(0l));
        assertThat(facet.getOtherCount(), is(0l));
        assertThat(facet.getMissingCount(), is(10l));

        facet = searchResponse.getFacets().facet("unmapped_short");
        assertThat(facet.getName(), equalTo("unmapped_short"));
        assertThat(facet.getEntries().size(), is(0));
        assertThat(facet.getTotalCount(), is(0l));
        assertThat(facet.getOtherCount(), is(0l));
        assertThat(facet.getMissingCount(), is(10l));

        facet = searchResponse.getFacets().facet("unmapped_int");
        assertThat(facet.getName(), equalTo("unmapped_int"));
        assertThat(facet.getEntries().size(), is(0));
        assertThat(facet.getTotalCount(), is(0l));
        assertThat(facet.getOtherCount(), is(0l));
        assertThat(facet.getMissingCount(), is(10l));

        facet = searchResponse.getFacets().facet("unmapped_long");
        assertThat(facet.getName(), equalTo("unmapped_long"));
        assertThat(facet.getEntries().size(), is(0));
        assertThat(facet.getTotalCount(), is(0l));
        assertThat(facet.getOtherCount(), is(0l));
        assertThat(facet.getMissingCount(), is(10l));

        facet = searchResponse.getFacets().facet("unmapped_float");
        assertThat(facet.getName(), equalTo("unmapped_float"));
        assertThat(facet.getEntries().size(), is(0));
        assertThat(facet.getTotalCount(), is(0l));
        assertThat(facet.getOtherCount(), is(0l));
        assertThat(facet.getMissingCount(), is(10l));

        facet = searchResponse.getFacets().facet("unmapped_double");
        assertThat(facet.getName(), equalTo("unmapped_double"));
        assertThat(facet.getEntries().size(), is(0));
        assertThat(facet.getTotalCount(), is(0l));
        assertThat(facet.getOtherCount(), is(0l));
        assertThat(facet.getMissingCount(), is(10l));

    }


    /**
     * Tests the terms facet when faceting on partially unmapped field. An example for this scenario is when searching
     * across indices, where the field is mapped in some indices and unmapped in others.
     */
    @Test
    public void testPartiallyUnmappedField() throws ElasticSearchException, IOException {
        client().admin().indices().prepareCreate("mapped_idx")
                .setSettings(getSettings())
                .addMapping("type", jsonBuilder().startObject().startObject("type").startObject("properties")
                        .startObject("partially_mapped_byte").field("type", "byte").endObject()
                        .startObject("partially_mapped_short").field("type", "short").endObject()
                        .startObject("partially_mapped_int").field("type", "integer").endObject()
                        .startObject("partially_mapped_long").field("type", "long").endObject()
                        .startObject("partially_mapped_float").field("type", "float").endObject()
                        .startObject("partially_mapped_double").field("type", "double").endObject()
                        .endObject().endObject().endObject())
                .execute().actionGet();
        client().admin().cluster().prepareHealth().setWaitForEvents(Priority.LANGUID).setWaitForGreenStatus().execute().actionGet();
        
        createIndex("unmapped_idx");
        client().admin().cluster().prepareHealth().setWaitForEvents(Priority.LANGUID).setWaitForGreenStatus().execute().actionGet();

        for (int i = 0; i < 10; i++) {
            client().prepareIndex("mapped_idx", "type", ""+i).setSource(jsonBuilder().startObject()
                    .field("mapped", "" + i)
                    .field("partially_mapped_str", ""+i)
                    .field("partially_mapped_bool", i%2 == 0)
                    .field("partially_mapped_byte", i)
                    .field("partially_mapped_short", i)
                    .field("partially_mapped_int", i)
                    .field("partially_mapped_long", i)
                    .field("partially_mapped_float", i)
                    .field("partially_mapped_double", i)
                    .endObject()).execute().actionGet();
        }

        for (int i = 10; i < 20; i++) {
            client().prepareIndex("unmapped_idx", "type", ""+i).setSource(jsonBuilder().startObject()
                    .field("mapped", ""+i)
                    .endObject()).execute().actionGet();
        }


        client().admin().indices().prepareFlush().setRefresh(true).execute().actionGet();

        SearchResponse searchResponse = client().prepareSearch()
                .setQuery(matchAllQuery())
                .addFacet(termsFacet("mapped").field("mapped").size(10))
                .addFacet(termsFacet("partially_mapped_str").field("partially_mapped_str").size(10))
                .addFacet(termsFacet("partially_mapped_bool").field("partially_mapped_bool").size(10))
                .addFacet(termsFacet("partially_mapped_byte").field("partially_mapped_byte").size(10))
                .addFacet(termsFacet("partially_mapped_short").field("partially_mapped_short").size(10))
                .addFacet(termsFacet("partially_mapped_int").field("partially_mapped_int").size(10))
                .addFacet(termsFacet("partially_mapped_long").field("partially_mapped_long").size(10))
                .addFacet(termsFacet("partially_mapped_float").field("partially_mapped_float").size(10))
                .addFacet(termsFacet("partially_mapped_double").field("partially_mapped_double").size(10))
                .execute().actionGet();

        assertThat(searchResponse.getHits().getTotalHits(), equalTo(20l));

        // all values should be returned for the mapped field
        TermsFacet facet = searchResponse.getFacets().facet("mapped");
        assertThat(facet.getName(), equalTo("mapped"));
        assertThat(facet.getEntries().size(), is(10));
        assertThat(facet.getTotalCount(), is(20l));
        assertThat(facet.getOtherCount(), is(10l));
        assertThat(facet.getMissingCount(), is(0l));

        // only the values of the mapped index should be returned for the partially mapped field (all docs of
        // the unmapped index should be missing)

        facet = searchResponse.getFacets().facet("partially_mapped_str");
        assertThat(facet.getName(), equalTo("partially_mapped_str"));
        assertThat(facet.getEntries().size(), is(10));
        assertThat(facet.getTotalCount(), is(10l));
        assertThat(facet.getOtherCount(), is(0l));
        assertThat(facet.getMissingCount(), is(10l));

        facet = searchResponse.getFacets().facet("partially_mapped_bool");
        assertThat(facet.getName(), equalTo("partially_mapped_bool"));
        assertThat(facet.getEntries().size(), is(2));
        assertThat(facet.getTotalCount(), is(10l));
        assertThat(facet.getOtherCount(), is(0l));
        assertThat(facet.getMissingCount(), is(10l));

        facet = searchResponse.getFacets().facet("partially_mapped_byte");
        assertThat(facet.getName(), equalTo("partially_mapped_byte"));
        assertThat(facet.getEntries().size(), is(10));
        assertThat(facet.getTotalCount(), is(10l));
        assertThat(facet.getOtherCount(), is(0l));
        assertThat(facet.getMissingCount(), is(10l));

        facet = searchResponse.getFacets().facet("partially_mapped_short");
        assertThat(facet.getName(), equalTo("partially_mapped_short"));
        assertThat(facet.getEntries().size(), is(10));
        assertThat(facet.getTotalCount(), is(10l));
        assertThat(facet.getOtherCount(), is(0l));
        assertThat(facet.getMissingCount(), is(10l));

        facet = searchResponse.getFacets().facet("partially_mapped_int");
        assertThat(facet.getName(), equalTo("partially_mapped_int"));
        assertThat(facet.getEntries().size(), is(10));
        assertThat(facet.getTotalCount(), is(10l));
        assertThat(facet.getOtherCount(), is(0l));
        assertThat(facet.getMissingCount(), is(10l));

        facet = searchResponse.getFacets().facet("partially_mapped_long");
        assertThat(facet.getName(), equalTo("partially_mapped_long"));
        assertThat(facet.getEntries().size(), is(10));
        assertThat(facet.getTotalCount(), is(10l));
        assertThat(facet.getOtherCount(), is(0l));
        assertThat(facet.getMissingCount(), is(10l));

        facet = searchResponse.getFacets().facet("partially_mapped_float");
        assertThat(facet.getName(), equalTo("partially_mapped_float"));
        assertThat(facet.getEntries().size(), is(10));
        assertThat(facet.getTotalCount(), is(10l));
        assertThat(facet.getOtherCount(), is(0l));
        assertThat(facet.getMissingCount(), is(10l));

        facet = searchResponse.getFacets().facet("partially_mapped_float");
        assertThat(facet.getName(), equalTo("partially_mapped_float"));
        assertThat(facet.getEntries().size(), is(10));
        assertThat(facet.getTotalCount(), is(10l));
        assertThat(facet.getOtherCount(), is(0l));
        assertThat(facet.getMissingCount(), is(10l));
    }

    @Test
    public void testMappedYetMissingField() throws IOException {
        client().admin().indices().prepareCreate("idx")
                .setSettings(getSettings())
                .addMapping("type", jsonBuilder().startObject()
                        .field("type").startObject()
                            .field("properties").startObject()
                                .field("string").startObject().field("type", "string").endObject()
                                .field("long").startObject().field("type", "long").endObject()
                                .field("double").startObject().field("type", "double").endObject()
                            .endObject()
                        .endObject())
                .execute().actionGet();
        client().admin().cluster().prepareHealth().setWaitForEvents(Priority.LANGUID).setWaitForGreenStatus().execute().actionGet();

        for (int i = 0; i < 10; i++) {
            client().prepareIndex("idx", "type", ""+i).setSource(jsonBuilder().startObject()
                    .field("foo", "bar")
                    .endObject()).execute().actionGet();
        }
        client().admin().indices().prepareFlush().setRefresh(true).execute().actionGet();

        SearchResponse searchResponse = client().prepareSearch()
                .setQuery(matchAllQuery())
                .addFacet(termsFacet("string").field("string").size(10))
                .addFacet(termsFacet("long").field("long").size(10))
                .addFacet(termsFacet("double").field("double").size(10))
                .execute().actionGet();

        TermsFacet facet = searchResponse.getFacets().facet("string");
        assertThat(facet.getName(), equalTo("string"));
        assertThat(facet.getEntries().size(), is(0));
        assertThat(facet.getTotalCount(), is(0l));
        assertThat(facet.getMissingCount(), is(10l));

        facet = searchResponse.getFacets().facet("long");
        assertThat(facet.getName(), equalTo("long"));
        assertThat(facet.getEntries().size(), is(0));
        assertThat(facet.getTotalCount(), is(0l));
        assertThat(facet.getMissingCount(), is(10l));

        facet = searchResponse.getFacets().facet("double");
        assertThat(facet.getName(), equalTo("double"));
        assertThat(facet.getEntries().size(), is(0));
        assertThat(facet.getTotalCount(), is(0l));
        assertThat(facet.getMissingCount(), is(10l));
    }

    /**
     * Tests the terms facet when faceting on multiple fields
     * case 1: some but not all the fields are mapped
     * case 2: all the fields are unmapped
     */
    @Test
    public void testMultiFields() throws Exception {
        createIndex("idx");
        client().admin().cluster().prepareHealth().setWaitForEvents(Priority.LANGUID).setWaitForGreenStatus().execute().actionGet();

        for (int i = 0; i < 10; i++) {
            client().prepareIndex("idx", "type", ""+i).setSource(jsonBuilder().startObject()
                    .field("mapped_str", ""+i)
                    .field("mapped_long", i)
                    .field("mapped_double", i)
                    .endObject()).execute().actionGet();
        }

        client().admin().indices().prepareFlush().setRefresh(true).execute().actionGet();
        SearchResponse searchResponse = client().prepareSearch()
                .setQuery(matchAllQuery())
                .addFacet(termsFacet("string").fields("mapped_str", "unmapped").size(10))
                .addFacet(termsFacet("long").fields("mapped_long", "unmapped").size(10))
                .addFacet(termsFacet("double").fields("mapped_double", "unmapped").size(10))
                .addFacet(termsFacet("all_unmapped").fields("unmapped", "unmapped_1").size(10))
                .execute().actionGet();

        assertThat(searchResponse.getHits().getTotalHits(), equalTo(10l));

        TermsFacet facet = searchResponse.getFacets().facet("string");
        assertThat(facet.getName(), equalTo("string"));
        assertThat(facet.getEntries().size(), is(10));
        assertThat(facet.getTotalCount(), is(10l));
        assertThat(facet.getMissingCount(), is(0l));

        facet = searchResponse.getFacets().facet("long");
        assertThat(facet.getName(), equalTo("long"));
        assertThat(facet.getEntries().size(), is(10));
        assertThat(facet.getTotalCount(), is(10l));
        assertThat(facet.getMissingCount(), is(0l));

        facet = searchResponse.getFacets().facet("double");
        assertThat(facet.getName(), equalTo("double"));
        assertThat(facet.getEntries().size(), is(10));
        assertThat(facet.getTotalCount(), is(10l));
        assertThat(facet.getMissingCount(), is(0l));

        facet = searchResponse.getFacets().facet("all_unmapped");
        assertThat(facet.getName(), equalTo("all_unmapped"));
        assertThat(facet.getEntries().size(), is(0));
        assertThat(facet.getTotalCount(), is(0l));
        assertThat(facet.getMissingCount(), is(10l));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5712.java