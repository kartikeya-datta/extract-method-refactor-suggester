error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4113.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4113.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4113.java
text:
```scala
t@@hrow new SearchSourceBuilderException("at least one range must be defined for geo_distance facet [" + name + "]");

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

package org.elasticsearch.search.facets.geodistance;

import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.collect.Maps;
import org.elasticsearch.common.lucene.geo.GeoDistance;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.builder.XContentBuilder;
import org.elasticsearch.index.query.xcontent.XContentFilterBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilderException;
import org.elasticsearch.search.facets.AbstractFacetBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * A geo distance builder allowing to create a facet of distances from a specific location including the
 * number of hits within each distance range, and aggregated data (like totals of either the distance or
 * cusotm value fields).
 *
 * @author kimchy (shay.banon)
 */
public class GeoDistanceFacetBuilder extends AbstractFacetBuilder {

    private String fieldName;

    private String valueFieldName;

    private double lat;

    private double lon;

    private String geohash;

    private GeoDistance geoDistance;

    private DistanceUnit unit;

    private Map<String, Object> params;

    private String valueScript;

    private List<Entry> entries = Lists.newArrayList();

    /**
     * Constructs a new geo distance with the provided facet name.
     */
    public GeoDistanceFacetBuilder(String name) {
        super(name);
    }

    /**
     * The geo point field that will be used to extract the document location(s).
     */
    public GeoDistanceFacetBuilder field(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    /**
     * A custom value field (numeric) that will be used to provide aggregated data for each facet (for example, total).
     */
    public GeoDistanceFacetBuilder valueField(String valueFieldName) {
        this.valueFieldName = valueFieldName;
        return this;
    }

    /**
     * A custom value script (result is numeric) that will be used to provide aggregated data for each facet (for example, total).
     */
    public GeoDistanceFacetBuilder valueScript(String valueScript) {
        this.valueScript = valueScript;
        return this;
    }

    /**
     * Parameters for {@link #valueScript(String)} to improve performance when executing the same script with different parameters.
     */
    public GeoDistanceFacetBuilder scriptParam(String name, Object value) {
        if (params == null) {
            params = Maps.newHashMap();
        }
        params.put(name, value);
        return this;
    }

    /**
     * The point to create the range distance facets from.
     *
     * @param lat latitude.
     * @param lon longitude.
     */
    public GeoDistanceFacetBuilder point(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
        return this;
    }

    /**
     * The latitude to create the range distance facets from.
     */
    public GeoDistanceFacetBuilder lat(double lat) {
        this.lat = lat;
        return this;
    }

    /**
     * The longitude to create the range distance facets from.
     */
    public GeoDistanceFacetBuilder lon(double lon) {
        this.lon = lon;
        return this;
    }

    /**
     * The geohash of the geo point to create the range distance facets from.
     */
    public GeoDistanceFacetBuilder geohash(String geohash) {
        this.geohash = geohash;
        return this;
    }

    /**
     * The geo distance type used to compute the distnace.
     */
    public GeoDistanceFacetBuilder geoDistance(GeoDistance geoDistance) {
        this.geoDistance = geoDistance;
        return this;
    }

    /**
     * Adds a range entry with explicit from and to.
     *
     * @param from The from distance limit
     * @param to   The to distance limit
     */
    public GeoDistanceFacetBuilder addRange(double from, double to) {
        entries.add(new Entry(from, to));
        return this;
    }

    /**
     * Adds a range entry with explicit from and unbounded to.
     *
     * @param from the from distance limit, to is unbounded.
     */
    public GeoDistanceFacetBuilder addUnboundedTo(double from) {
        entries.add(new Entry(from, Double.POSITIVE_INFINITY));
        return this;
    }

    /**
     * Adds a range entry with explicit to and unbounded from.
     *
     * @param to the to distance limit, from is unbounded.
     */
    public GeoDistanceFacetBuilder addUnboundedFrom(double to) {
        entries.add(new Entry(Double.NEGATIVE_INFINITY, to));
        return this;
    }

    /**
     * The distance unit to use. Defaults to {@link org.elasticsearch.common.unit.DistanceUnit#KILOMETERS}
     */
    public GeoDistanceFacetBuilder unit(DistanceUnit unit) {
        this.unit = unit;
        return this;
    }

    public GeoDistanceFacetBuilder global(boolean global) {
        this.global = global;
        return this;
    }

    public GeoDistanceFacetBuilder filter(XContentFilterBuilder filter) {
        this.filter = filter;
        return this;
    }

    @Override public void toXContent(XContentBuilder builder, Params params) throws IOException {
        if (fieldName == null) {
            throw new SearchSourceBuilderException("field must be set on geo_distance facet for facet [" + name + "]");
        }
        if (entries.isEmpty()) {
            throw new SearchSourceBuilderException("at least one range must be defined for geo_distance face [" + name + "]");
        }

        builder.startObject(name);

        builder.startObject(GeoDistanceFacetCollectorParser.NAME);

        if (geohash != null) {
            builder.field(fieldName, geohash);
        } else {
            builder.startArray(fieldName).value(lat).value(lon).endArray();
        }

        if (valueFieldName != null) {
            builder.field("value_field", valueFieldName);
        }

        if (valueScript != null) {
            builder.field("value_script", valueScript);
            if (this.params != null) {
                builder.field("params");
                builder.map(this.params);
            }
        }

        builder.startArray("ranges");
        for (Entry entry : entries) {
            builder.startObject();
            if (!Double.isInfinite(entry.from)) {
                builder.field("from", entry.from);
            }
            if (!Double.isInfinite(entry.to)) {
                builder.field("to", entry.to);
            }
            builder.endObject();
        }
        builder.endArray();

        if (unit != null) {
            builder.field("unit", unit);
        }
        if (geoDistance != null) {
            builder.field("distance_type", geoDistance.name().toLowerCase());
        }

        builder.endObject();

        if (filter != null) {
            builder.field("filter");
            filter.toXContent(builder, params);
        }

        if (global != null) {
            builder.field("global", global);
        }

        builder.endObject();
    }

    private static class Entry {
        final double from;
        final double to;

        private Entry(double from, double to) {
            this.from = from;
            this.to = to;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4113.java