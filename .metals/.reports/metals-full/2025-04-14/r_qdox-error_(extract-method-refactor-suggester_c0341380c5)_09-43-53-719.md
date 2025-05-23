error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2182.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2182.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2182.java
text:
```scala
D@@istanceUnit unit = DistanceUnit.DEFAULT;

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
package org.elasticsearch.search.aggregations.bucket.range.geodistance;

import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.fielddata.*;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.search.SearchParseException;
import org.elasticsearch.search.aggregations.Aggregator;
import org.elasticsearch.search.aggregations.AggregatorFactory;
import org.elasticsearch.search.aggregations.bucket.range.AbstractRangeBase;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregator;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregator.Unmapped;
import org.elasticsearch.search.aggregations.support.*;
import org.elasticsearch.search.aggregations.support.geopoints.GeoPointValuesSource;
import org.elasticsearch.search.aggregations.support.numeric.NumericValuesSource;
import org.elasticsearch.search.internal.SearchContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GeoDistanceParser implements Aggregator.Parser {

    @Override
    public String type() {
        return InternalGeoDistance.TYPE.name();
    }

    private static String key(String key, double from, double to) {
        if (key != null) {
            return key;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(from == 0 ? "*" : from);
        sb.append("-");
        sb.append(Double.isInfinite(to) ? "*" : to);
        return sb.toString();
    }

    @Override
    public AggregatorFactory parse(String aggregationName, XContentParser parser, SearchContext context) throws IOException {

        String field = null;
        List<RangeAggregator.Range> ranges = null;
        GeoPoint origin = null;
        DistanceUnit unit = DistanceUnit.KILOMETERS;
        GeoDistance distanceType = GeoDistance.DEFAULT;
        boolean keyed = false;

        XContentParser.Token token;
        String currentFieldName = null;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
            } else if (token == XContentParser.Token.VALUE_STRING) {
                if ("field".equals(currentFieldName)) {
                    field = parser.text();
                } else if ("unit".equals(currentFieldName)) {
                    unit = DistanceUnit.fromString(parser.text());
                } else if ("distance_type".equals(currentFieldName) || "distanceType".equals(currentFieldName)) {
                    distanceType = GeoDistance.fromString(parser.text());
                } else if ("point".equals(currentFieldName) || "origin".equals(currentFieldName) || "center".equals(currentFieldName)) {
                    origin = new GeoPoint();
                    origin.resetFromString(parser.text());
                } else {
                    throw new SearchParseException(context, "Unknown key for a " + token + " in [" + aggregationName + "]: [" + currentFieldName + "].");
                }
            } else if (token == XContentParser.Token.VALUE_BOOLEAN) {
                if ("keyed".equals(currentFieldName)) {
                    keyed = parser.booleanValue();
                } else {
                    throw new SearchParseException(context, "Unknown key for a " + token + " in [" + aggregationName + "]: [" + currentFieldName + "].");
                }
            } else if (token == XContentParser.Token.START_ARRAY) {
                if ("ranges".equals(currentFieldName)) {
                    ranges = new ArrayList<RangeAggregator.Range>();
                    while ((token = parser.nextToken()) != XContentParser.Token.END_ARRAY) {
                        String fromAsStr = null;
                        String toAsStr = null;
                        double from = 0.0;
                        double to = Double.POSITIVE_INFINITY;
                        String key = null;
                        String toOrFromOrKey = null;
                        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                            if (token == XContentParser.Token.FIELD_NAME) {
                                toOrFromOrKey = parser.currentName();
                            } else if (token == XContentParser.Token.VALUE_NUMBER) {
                                if ("from".equals(toOrFromOrKey)) {
                                    from = parser.doubleValue();
                                } else if ("to".equals(toOrFromOrKey)) {
                                    to = parser.doubleValue();
                                }
                            } else if (token == XContentParser.Token.VALUE_STRING) {
                                if ("key".equals(toOrFromOrKey)) {
                                    key = parser.text();
                                } else if ("from".equals(toOrFromOrKey)) {
                                    fromAsStr = parser.text();
                                } else if ("to".equals(toOrFromOrKey)) {
                                    toAsStr = parser.text();
                                }
                            }
                        }
                        ranges.add(new RangeAggregator.Range(key(key, from, to), from, fromAsStr, to, toAsStr));
                    }
                } else if ("point".equals(currentFieldName) || "origin".equals(currentFieldName) || "center".equals(currentFieldName)) {
                    double lat = Double.NaN;
                    double lon = Double.NaN;
                    while ((token = parser.nextToken()) != XContentParser.Token.END_ARRAY) {
                        if (Double.isNaN(lon)) {
                            lon = parser.doubleValue();
                        } else if (Double.isNaN(lat)) {
                            lat = parser.doubleValue();
                        } else {
                            throw new SearchParseException(context, "malformed [origin] geo point array in geo_distance aggregator [" + aggregationName + "]. " +
                                    "a geo point array must be of the form [lon, lat]");
                        }
                    }
                    origin = new GeoPoint(lat, lon);
                } else {
                    throw new SearchParseException(context, "Unknown key for a " + token + " in [" + aggregationName + "]: [" + currentFieldName + "].");
                }
            } else if (token == XContentParser.Token.START_OBJECT) {
                if ("point".equals(currentFieldName) || "origin".equals(currentFieldName) || "center".equals(currentFieldName)) {
                    double lat = Double.NaN;
                    double lon = Double.NaN;
                    while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                        if (token == XContentParser.Token.FIELD_NAME) {
                            currentFieldName = parser.currentName();
                        } else if (token == XContentParser.Token.VALUE_NUMBER) {
                            if ("lat".equals(currentFieldName)) {
                                lat = parser.doubleValue();
                            } else if ("lon".equals(currentFieldName)) {
                                lon = parser.doubleValue();
                            }
                        }
                    }
                    if (Double.isNaN(lat) || Double.isNaN(lon)) {
                        throw new SearchParseException(context, "malformed [origin] geo point object. either [lat] or [lon] (or both) are " +
                                "missing in geo_distance aggregator [" + aggregationName + "]");
                    }
                    origin = new GeoPoint(lat, lon);
                } else {
                    throw new SearchParseException(context, "Unknown key for a " + token + " in [" + aggregationName + "]: [" + currentFieldName + "].");
                }
            } else {
                throw new SearchParseException(context, "Unexpected token " + token + " in [" + aggregationName + "].");
            }
        }

        if (ranges == null) {
            throw new SearchParseException(context, "Missing [ranges] in geo_distance aggregator [" + aggregationName + "]");
        }

        if (origin == null) {
            throw new SearchParseException(context, "Missing [origin] in geo_distance aggregator [" + aggregationName + "]");
        }

        ValuesSourceConfig<GeoPointValuesSource> config = new ValuesSourceConfig<GeoPointValuesSource>(GeoPointValuesSource.class);

        if (field == null) {
            return new GeoDistanceFactory(aggregationName, config, InternalGeoDistance.FACTORY, origin, unit, distanceType, ranges, keyed);
        }

        FieldMapper<?> mapper = context.smartNameFieldMapper(field);
        if (mapper == null) {
            config.unmapped(true);
            return new GeoDistanceFactory(aggregationName, config, InternalGeoDistance.FACTORY, origin, unit, distanceType, ranges, keyed);
        }

        IndexFieldData<?> indexFieldData = context.fieldData().getForField(mapper);
        config.fieldContext(new FieldContext(field, indexFieldData));
        return new GeoDistanceFactory(aggregationName, config, InternalGeoDistance.FACTORY, origin, unit, distanceType, ranges, keyed);
    }

    private static class GeoDistanceFactory extends ValueSourceAggregatorFactory<GeoPointValuesSource> {

        private final GeoPoint origin;
        private final DistanceUnit unit;
        private final GeoDistance distanceType;
        private final AbstractRangeBase.Factory rangeFactory;
        private final List<RangeAggregator.Range> ranges;
        private final boolean keyed;

        public GeoDistanceFactory(String name, ValuesSourceConfig<GeoPointValuesSource> valueSourceConfig,
                                  AbstractRangeBase.Factory rangeFactory, GeoPoint origin, DistanceUnit unit, GeoDistance distanceType,
                                  List<RangeAggregator.Range> ranges, boolean keyed) {
            super(name, rangeFactory.type(), valueSourceConfig);
            this.origin = origin;
            this.unit = unit;
            this.distanceType = distanceType;
            this.rangeFactory = rangeFactory;
            this.ranges = ranges;
            this.keyed = keyed;
        }

        @Override
        protected Aggregator createUnmapped(AggregationContext aggregationContext, Aggregator parent) {
            return new Unmapped(name, ranges, keyed, valuesSourceConfig.formatter(), valuesSourceConfig.parser(), aggregationContext, parent, rangeFactory);
        }

        @Override
        protected Aggregator create(final GeoPointValuesSource valuesSource, long expectedBucketsCount, AggregationContext aggregationContext, Aggregator parent) {
            final DistanceValues distanceValues = new DistanceValues(valuesSource, distanceType, origin, unit);
            FieldDataSource.Numeric distanceSource = new DistanceSource(distanceValues, valuesSource.metaData());
            if (distanceSource.metaData().multiValued()) {
                // we need to ensure uniqueness
                distanceSource = new FieldDataSource.Numeric.SortedAndUnique(distanceSource);
            }
            final NumericValuesSource numericSource = new NumericValuesSource(distanceSource, null, null);
            return new RangeAggregator(name, factories, numericSource, rangeFactory, ranges, keyed, aggregationContext, parent);
        }

        private static class DistanceValues extends DoubleValues {

            private final GeoPointValuesSource geoPointValues;
            private GeoPointValues geoValues;
            private final GeoDistance distanceType;
            private final GeoPoint origin;
            private final DistanceUnit unit;

            protected DistanceValues(GeoPointValuesSource geoPointValues, GeoDistance distanceType, GeoPoint origin, DistanceUnit unit) {
                super(true);
                this.geoPointValues = geoPointValues;
                this.distanceType = distanceType;
                this.origin = origin;
                this.unit = unit;
            }

            @Override
            public int setDocument(int docId) {
                geoValues = geoPointValues.values();
                return geoValues.setDocument(docId);
            }

            @Override
            public double nextValue() {
                final GeoPoint target = geoValues.nextValue();
                return distanceType.calculate(origin.getLat(), origin.getLon(), target.getLat(), target.getLon(), unit);
            }

        }

        private static class DistanceSource extends FieldDataSource.Numeric {

            private final DoubleValues values;
            private final MetaData metaData;

            public DistanceSource(DoubleValues values, MetaData metaData) {
                this.values = values;
                // even if the geo points are unique, there's no guarantee the distances are
                this.metaData = MetaData.builder(metaData).uniqueness(MetaData.Uniqueness.UNKNOWN).build();
            }

            @Override
            public MetaData metaData() {
                return metaData;
            }

            @Override
            public boolean isFloatingPoint() {
                return true;
            }

            @Override
            public LongValues longValues() {
                throw new UnsupportedOperationException();
            }

            @Override
            public DoubleValues doubleValues() {
                return values;
            }

            @Override
            public BytesValues bytesValues() {
                throw new UnsupportedOperationException();
            }

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2182.java