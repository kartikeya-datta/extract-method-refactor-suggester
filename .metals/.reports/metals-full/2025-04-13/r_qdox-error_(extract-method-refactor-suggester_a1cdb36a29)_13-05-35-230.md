error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9272.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9272.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9272.java
text:
```scala
.@@startObject("properties").startObject("point").field("type", "geo_point").field("lat_lon", true).field("normalize", false).field("validate", false).endObject().endObject()

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

package org.elasticsearch.index.mapper.geopoint;

import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.common.Numbers;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.mapper.DocumentMapper;
import org.elasticsearch.index.mapper.MapperTests;
import org.elasticsearch.index.mapper.ParsedDocument;
import org.elasticsearch.index.search.geo.GeoHashUtils;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author kimchy (shay.banon)
 */
public class LatLonMappingGeoPointTests {

    @Test public void testNormalizeLatLonValuesDefault() throws Exception {
        // default to normalize
        String mapping = XContentFactory.jsonBuilder().startObject().startObject("type")
                .startObject("properties").startObject("point").field("type", "geo_point").endObject().endObject()
                .endObject().endObject().string();

        DocumentMapper defaultMapper = MapperTests.newParser().parse(mapping);

        ParsedDocument doc = defaultMapper.parse("type", "1", XContentFactory.jsonBuilder()
                .startObject()
                .startObject("point").field("lat", 91).field("lon", 181).endObject()
                .endObject()
                .copiedBytes());

        assertThat(doc.rootDoc().get("point"), equalTo("-89.0,-179.0"));

        doc = defaultMapper.parse("type", "1", XContentFactory.jsonBuilder()
                .startObject()
                .startObject("point").field("lat", -91).field("lon", -181).endObject()
                .endObject()
                .copiedBytes());

        assertThat(doc.rootDoc().get("point"), equalTo("89.0,179.0"));

        doc = defaultMapper.parse("type", "1", XContentFactory.jsonBuilder()
                .startObject()
                .startObject("point").field("lat", 181).field("lon", 361).endObject()
                .endObject()
                .copiedBytes());

        assertThat(doc.rootDoc().get("point"), equalTo("1.0,1.0"));
    }

    @Test public void testValidateLatLonValues() throws Exception {
        String mapping = XContentFactory.jsonBuilder().startObject().startObject("type")
                .startObject("properties").startObject("point").field("type", "geo_point").field("lat_lon", true).field("normalize", false).field("validate", true).endObject().endObject()
                .endObject().endObject().string();

        DocumentMapper defaultMapper = MapperTests.newParser().parse(mapping);


        ParsedDocument doc = defaultMapper.parse("type", "1", XContentFactory.jsonBuilder()
                .startObject()
                .startObject("point").field("lat", 90).field("lon", 1.3).endObject()
                .endObject()
                .copiedBytes());

        try {
            defaultMapper.parse("type", "1", XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject("point").field("lat", -91).field("lon", 1.3).endObject()
                    .endObject()
                    .copiedBytes());
            assert false;
        } catch (ElasticSearchIllegalArgumentException e) {

        }

        try {
            defaultMapper.parse("type", "1", XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject("point").field("lat", 91).field("lon", 1.3).endObject()
                    .endObject()
                    .copiedBytes());
            assert false;
        } catch (ElasticSearchIllegalArgumentException e) {

        }

        try {
            defaultMapper.parse("type", "1", XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject("point").field("lat", 1.2).field("lon", -181).endObject()
                    .endObject()
                    .copiedBytes());
            assert false;
        } catch (ElasticSearchIllegalArgumentException e) {

        }

        try {
            defaultMapper.parse("type", "1", XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject("point").field("lat", 1.2).field("lon", 181).endObject()
                    .endObject()
                    .copiedBytes());
            assert false;
        } catch (ElasticSearchIllegalArgumentException e) {

        }
    }


    @Test public void testNoValidateLatLonValues() throws Exception {
        String mapping = XContentFactory.jsonBuilder().startObject().startObject("type")
                .startObject("properties").startObject("point").field("type", "geo_point").field("lat_lon", true).endObject().endObject()
                .endObject().endObject().string();

        DocumentMapper defaultMapper = MapperTests.newParser().parse(mapping);


        ParsedDocument doc = defaultMapper.parse("type", "1", XContentFactory.jsonBuilder()
                .startObject()
                .startObject("point").field("lat", 90).field("lon", 1.3).endObject()
                .endObject()
                .copiedBytes());

        defaultMapper.parse("type", "1", XContentFactory.jsonBuilder()
                .startObject()
                .startObject("point").field("lat", -91).field("lon", 1.3).endObject()
                .endObject()
                .copiedBytes());

        defaultMapper.parse("type", "1", XContentFactory.jsonBuilder()
                .startObject()
                .startObject("point").field("lat", 91).field("lon", 1.3).endObject()
                .endObject()
                .copiedBytes());

        defaultMapper.parse("type", "1", XContentFactory.jsonBuilder()
                .startObject()
                .startObject("point").field("lat", 1.2).field("lon", -181).endObject()
                .endObject()
                .copiedBytes());

        defaultMapper.parse("type", "1", XContentFactory.jsonBuilder()
                .startObject()
                .startObject("point").field("lat", 1.2).field("lon", 181).endObject()
                .endObject()
                .copiedBytes());
    }

    @Test public void testLatLonValues() throws Exception {
        String mapping = XContentFactory.jsonBuilder().startObject().startObject("type")
                .startObject("properties").startObject("point").field("type", "geo_point").field("lat_lon", true).endObject().endObject()
                .endObject().endObject().string();

        DocumentMapper defaultMapper = MapperTests.newParser().parse(mapping);

        ParsedDocument doc = defaultMapper.parse("type", "1", XContentFactory.jsonBuilder()
                .startObject()
                .startObject("point").field("lat", 1.2).field("lon", 1.3).endObject()
                .endObject()
                .copiedBytes());

        assertThat(doc.rootDoc().getFieldable("point.lat"), notNullValue());
        assertThat(doc.rootDoc().getFieldable("point.lat").getBinaryValue(), nullValue());
        assertThat(doc.rootDoc().getFieldable("point.lon"), notNullValue());
        assertThat(doc.rootDoc().getFieldable("point.lon").getBinaryValue(), nullValue());
        assertThat(doc.rootDoc().getFieldable("point.geohash"), nullValue());
        assertThat(doc.rootDoc().get("point"), equalTo("1.2,1.3"));
    }

    @Test public void testLatLonValuesStored() throws Exception {
        String mapping = XContentFactory.jsonBuilder().startObject().startObject("type")
                .startObject("properties").startObject("point").field("type", "geo_point").field("lat_lon", true).field("store", "yes").endObject().endObject()
                .endObject().endObject().string();

        DocumentMapper defaultMapper = MapperTests.newParser().parse(mapping);

        ParsedDocument doc = defaultMapper.parse("type", "1", XContentFactory.jsonBuilder()
                .startObject()
                .startObject("point").field("lat", 1.2).field("lon", 1.3).endObject()
                .endObject()
                .copiedBytes());

        assertThat(doc.rootDoc().getFieldable("point.lat"), notNullValue());
        assertThat(doc.rootDoc().getFieldable("point.lat").getBinaryValue(), equalTo(Numbers.doubleToBytes(1.2)));
        assertThat(doc.rootDoc().getFieldable("point.lon"), notNullValue());
        assertThat(doc.rootDoc().getFieldable("point.lon").getBinaryValue(), equalTo(Numbers.doubleToBytes(1.3)));
        assertThat(doc.rootDoc().getFieldable("point.geohash"), nullValue());
        assertThat(doc.rootDoc().get("point"), equalTo("1.2,1.3"));
    }

    @Test public void testArrayLatLonValues() throws Exception {
        String mapping = XContentFactory.jsonBuilder().startObject().startObject("type")
                .startObject("properties").startObject("point").field("type", "geo_point").field("lat_lon", true).field("store", "yes").endObject().endObject()
                .endObject().endObject().string();

        DocumentMapper defaultMapper = MapperTests.newParser().parse(mapping);

        ParsedDocument doc = defaultMapper.parse("type", "1", XContentFactory.jsonBuilder()
                .startObject()
                .startArray("point")
                .startObject().field("lat", 1.2).field("lon", 1.3).endObject()
                .startObject().field("lat", 1.4).field("lon", 1.5).endObject()
                .endArray()
                .endObject()
                .copiedBytes());

        assertThat(doc.rootDoc().getFieldables("point.lat").length, equalTo(2));
        assertThat(doc.rootDoc().getFieldables("point.lon").length, equalTo(2));
        assertThat(doc.rootDoc().getFieldables("point.lat")[0].getBinaryValue(), equalTo(Numbers.doubleToBytes(1.2)));
        assertThat(doc.rootDoc().getFieldables("point.lon")[0].getBinaryValue(), equalTo(Numbers.doubleToBytes(1.3)));
        assertThat(doc.rootDoc().getFieldables("point")[0].stringValue(), equalTo("1.2,1.3"));
        assertThat(doc.rootDoc().getFieldables("point.lat")[1].getBinaryValue(), equalTo(Numbers.doubleToBytes(1.4)));
        assertThat(doc.rootDoc().getFieldables("point.lon")[1].getBinaryValue(), equalTo(Numbers.doubleToBytes(1.5)));
        assertThat(doc.rootDoc().getFieldables("point")[1].stringValue(), equalTo("1.4,1.5"));
    }

    @Test public void testLatLonInOneValue() throws Exception {
        String mapping = XContentFactory.jsonBuilder().startObject().startObject("type")
                .startObject("properties").startObject("point").field("type", "geo_point").field("lat_lon", true).endObject().endObject()
                .endObject().endObject().string();

        DocumentMapper defaultMapper = MapperTests.newParser().parse(mapping);

        ParsedDocument doc = defaultMapper.parse("type", "1", XContentFactory.jsonBuilder()
                .startObject()
                .field("point", "1.2,1.3")
                .endObject()
                .copiedBytes());

        assertThat(doc.rootDoc().getFieldable("point.lat"), notNullValue());
        assertThat(doc.rootDoc().getFieldable("point.lon"), notNullValue());
        assertThat(doc.rootDoc().get("point"), equalTo("1.2,1.3"));
    }

    @Test public void testLatLonInOneValueStored() throws Exception {
        String mapping = XContentFactory.jsonBuilder().startObject().startObject("type")
                .startObject("properties").startObject("point").field("type", "geo_point").field("lat_lon", true).field("store", "yes").endObject().endObject()
                .endObject().endObject().string();

        DocumentMapper defaultMapper = MapperTests.newParser().parse(mapping);

        ParsedDocument doc = defaultMapper.parse("type", "1", XContentFactory.jsonBuilder()
                .startObject()
                .field("point", "1.2,1.3")
                .endObject()
                .copiedBytes());

        assertThat(doc.rootDoc().getFieldable("point.lat"), notNullValue());
        assertThat(doc.rootDoc().getFieldable("point.lat").getBinaryValue(), equalTo(Numbers.doubleToBytes(1.2)));
        assertThat(doc.rootDoc().getFieldable("point.lon"), notNullValue());
        assertThat(doc.rootDoc().getFieldable("point.lon").getBinaryValue(), equalTo(Numbers.doubleToBytes(1.3)));
        assertThat(doc.rootDoc().get("point"), equalTo("1.2,1.3"));
    }

    @Test public void testLatLonInOneValueArray() throws Exception {
        String mapping = XContentFactory.jsonBuilder().startObject().startObject("type")
                .startObject("properties").startObject("point").field("type", "geo_point").field("lat_lon", true).field("store", "yes").endObject().endObject()
                .endObject().endObject().string();

        DocumentMapper defaultMapper = MapperTests.newParser().parse(mapping);

        ParsedDocument doc = defaultMapper.parse("type", "1", XContentFactory.jsonBuilder()
                .startObject()
                .startArray("point")
                .value("1.2,1.3")
                .value("1.4,1.5")
                .endArray()
                .endObject()
                .copiedBytes());

        assertThat(doc.rootDoc().getFieldables("point.lat").length, equalTo(2));
        assertThat(doc.rootDoc().getFieldables("point.lon").length, equalTo(2));
        assertThat(doc.rootDoc().getFieldables("point.lat")[0].getBinaryValue(), equalTo(Numbers.doubleToBytes(1.2)));
        assertThat(doc.rootDoc().getFieldables("point.lon")[0].getBinaryValue(), equalTo(Numbers.doubleToBytes(1.3)));
        assertThat(doc.rootDoc().getFieldables("point")[0].stringValue(), equalTo("1.2,1.3"));
        assertThat(doc.rootDoc().getFieldables("point.lat")[1].getBinaryValue(), equalTo(Numbers.doubleToBytes(1.4)));
        assertThat(doc.rootDoc().getFieldables("point.lon")[1].getBinaryValue(), equalTo(Numbers.doubleToBytes(1.5)));
        assertThat(doc.rootDoc().getFieldables("point")[1].stringValue(), equalTo("1.4,1.5"));
    }

    @Test public void testGeoHashValue() throws Exception {
        String mapping = XContentFactory.jsonBuilder().startObject().startObject("type")
                .startObject("properties").startObject("point").field("type", "geo_point").field("lat_lon", true).endObject().endObject()
                .endObject().endObject().string();

        DocumentMapper defaultMapper = MapperTests.newParser().parse(mapping);

        ParsedDocument doc = defaultMapper.parse("type", "1", XContentFactory.jsonBuilder()
                .startObject()
                .field("point", GeoHashUtils.encode(1.2, 1.3))
                .endObject()
                .copiedBytes());

        assertThat(doc.rootDoc().getFieldable("point.lat"), notNullValue());
        assertThat(doc.rootDoc().getFieldable("point.lon"), notNullValue());
        assertThat(doc.rootDoc().get("point"), notNullValue());
    }

    @Test public void testLonLatArray() throws Exception {
        String mapping = XContentFactory.jsonBuilder().startObject().startObject("type")
                .startObject("properties").startObject("point").field("type", "geo_point").field("lat_lon", true).endObject().endObject()
                .endObject().endObject().string();

        DocumentMapper defaultMapper = MapperTests.newParser().parse(mapping);

        ParsedDocument doc = defaultMapper.parse("type", "1", XContentFactory.jsonBuilder()
                .startObject()
                .startArray("point").value(1.3).value(1.2).endArray()
                .endObject()
                .copiedBytes());

        assertThat(doc.rootDoc().getFieldable("point.lat"), notNullValue());
        assertThat(doc.rootDoc().getFieldable("point.lat").getBinaryValue(), nullValue());
        assertThat(doc.rootDoc().getFieldable("point.lon"), notNullValue());
        assertThat(doc.rootDoc().getFieldable("point.lon").getBinaryValue(), nullValue());
        assertThat(doc.rootDoc().get("point"), equalTo("1.2,1.3"));
    }

    @Test public void testLonLatArrayStored() throws Exception {
        String mapping = XContentFactory.jsonBuilder().startObject().startObject("type")
                .startObject("properties").startObject("point").field("type", "geo_point").field("lat_lon", true).field("store", "yes").endObject().endObject()
                .endObject().endObject().string();

        DocumentMapper defaultMapper = MapperTests.newParser().parse(mapping);

        ParsedDocument doc = defaultMapper.parse("type", "1", XContentFactory.jsonBuilder()
                .startObject()
                .startArray("point").value(1.3).value(1.2).endArray()
                .endObject()
                .copiedBytes());

        assertThat(doc.rootDoc().getFieldable("point.lat"), notNullValue());
        assertThat(doc.rootDoc().getFieldable("point.lat").getBinaryValue(), equalTo(Numbers.doubleToBytes(1.2)));
        assertThat(doc.rootDoc().getFieldable("point.lon"), notNullValue());
        assertThat(doc.rootDoc().getFieldable("point.lon").getBinaryValue(), equalTo(Numbers.doubleToBytes(1.3)));
        assertThat(doc.rootDoc().get("point"), equalTo("1.2,1.3"));
    }

    @Test public void testLonLatArrayArrayStored() throws Exception {
        String mapping = XContentFactory.jsonBuilder().startObject().startObject("type")
                .startObject("properties").startObject("point").field("type", "geo_point").field("lat_lon", true).field("store", "yes").endObject().endObject()
                .endObject().endObject().string();

        DocumentMapper defaultMapper = MapperTests.newParser().parse(mapping);

        ParsedDocument doc = defaultMapper.parse("type", "1", XContentFactory.jsonBuilder()
                .startObject()
                .startArray("point")
                .startArray().value(1.3).value(1.2).endArray()
                .startArray().value(1.5).value(1.4).endArray()
                .endArray()
                .endObject()
                .copiedBytes());

        assertThat(doc.rootDoc().getFieldables("point.lat").length, equalTo(2));
        assertThat(doc.rootDoc().getFieldables("point.lon").length, equalTo(2));
        assertThat(doc.rootDoc().getFieldables("point.lat")[0].getBinaryValue(), equalTo(Numbers.doubleToBytes(1.2)));
        assertThat(doc.rootDoc().getFieldables("point.lon")[0].getBinaryValue(), equalTo(Numbers.doubleToBytes(1.3)));
        assertThat(doc.rootDoc().getFieldables("point")[0].stringValue(), equalTo("1.2,1.3"));
        assertThat(doc.rootDoc().getFieldables("point.lat")[1].getBinaryValue(), equalTo(Numbers.doubleToBytes(1.4)));
        assertThat(doc.rootDoc().getFieldables("point.lon")[1].getBinaryValue(), equalTo(Numbers.doubleToBytes(1.5)));
        assertThat(doc.rootDoc().getFieldables("point")[1].stringValue(), equalTo("1.4,1.5"));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9272.java