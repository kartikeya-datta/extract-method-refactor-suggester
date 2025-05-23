error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5137.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5137.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5137.java
text:
```scala
d@@istanceValues = GeoDistance.distanceValues(geoValues, distance);

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

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.SortedNumericDocValues;
import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.geo.GeoDistance.FixedSourceDistance;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.lucene.ReaderContextAware;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.fielddata.MultiGeoPointValues;
import org.elasticsearch.index.fielddata.SortedBinaryDocValues;
import org.elasticsearch.index.fielddata.SortedNumericDoubleValues;
import org.elasticsearch.search.SearchParseException;
import org.elasticsearch.search.aggregations.Aggregator;
import org.elasticsearch.search.aggregations.AggregatorFactory;
import org.elasticsearch.search.aggregations.bucket.range.InternalRange;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregator;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregator.Unmapped;
import org.elasticsearch.search.aggregations.support.*;
import org.elasticsearch.search.internal.SearchContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GeoDistanceParser implements Aggregator.Parser {

    private static final ParseField ORIGIN_FIELD = new ParseField("origin", "center", "point", "por");

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

        ValuesSourceParser<ValuesSource.GeoPoint> vsParser = ValuesSourceParser.geoPoint(aggregationName, InternalGeoDistance.TYPE, context)
                .requiresSortedValues(true)
                .build();

        GeoPointParser geoPointParser = new GeoPointParser(aggregationName, InternalGeoDistance.TYPE, context, ORIGIN_FIELD);

        List<RangeAggregator.Range> ranges = null;
        DistanceUnit unit = DistanceUnit.DEFAULT;
        GeoDistance distanceType = GeoDistance.DEFAULT;
        boolean keyed = false;

        XContentParser.Token token;
        String currentFieldName = null;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
            } else if (vsParser.token(currentFieldName, token, parser)) {
                continue;
            } else if (geoPointParser.token(currentFieldName, token, parser)) {
                continue;
            } else if (token == XContentParser.Token.VALUE_STRING) {
                if ("unit".equals(currentFieldName)) {
                    unit = DistanceUnit.fromString(parser.text());
                } else if ("distance_type".equals(currentFieldName) || "distanceType".equals(currentFieldName)) {
                    distanceType = GeoDistance.fromString(parser.text());
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
                    ranges = new ArrayList<>();
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
                } else  {
                    throw new SearchParseException(context, "Unknown key for a " + token + " in [" + aggregationName + "]: [" + currentFieldName + "].");
                }
            } else {
                throw new SearchParseException(context, "Unexpected token " + token + " in [" + aggregationName + "].");
            }
        }

        if (ranges == null) {
            throw new SearchParseException(context, "Missing [ranges] in geo_distance aggregator [" + aggregationName + "]");
        }

        GeoPoint origin = geoPointParser.geoPoint();
        if (origin == null) {
            throw new SearchParseException(context, "Missing [origin] in geo_distance aggregator [" + aggregationName + "]");
        }

        return new GeoDistanceFactory(aggregationName, vsParser.config(), InternalGeoDistance.FACTORY, origin, unit, distanceType, ranges, keyed);
    }

    private static class GeoDistanceFactory extends ValuesSourceAggregatorFactory<ValuesSource.GeoPoint> {

        private final GeoPoint origin;
        private final DistanceUnit unit;
        private final GeoDistance distanceType;
        private final InternalRange.Factory rangeFactory;
        private final List<RangeAggregator.Range> ranges;
        private final boolean keyed;

        public GeoDistanceFactory(String name, ValuesSourceConfig<ValuesSource.GeoPoint> valueSourceConfig,
                                  InternalRange.Factory rangeFactory, GeoPoint origin, DistanceUnit unit, GeoDistance distanceType,
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
            return new Unmapped(name, ranges, keyed, null, aggregationContext, parent, rangeFactory);
        }

        @Override
        protected Aggregator create(final ValuesSource.GeoPoint valuesSource, long expectedBucketsCount, AggregationContext aggregationContext, Aggregator parent) {
            DistanceSource distanceSource = new DistanceSource(valuesSource, distanceType, origin, unit);
            aggregationContext.registerReaderContextAware(distanceSource);
            return new RangeAggregator(name, factories, distanceSource, null, rangeFactory, ranges, keyed, aggregationContext, parent);
        }

        private static class DistanceSource extends ValuesSource.Numeric implements ReaderContextAware {

            private final ValuesSource.GeoPoint source;
            private final GeoDistance distanceType;
            private final DistanceUnit unit;
            private final org.elasticsearch.common.geo.GeoPoint origin;
            private final MetaData metaData;
            private SortedNumericDoubleValues distanceValues;

            public DistanceSource(ValuesSource.GeoPoint source, GeoDistance distanceType, org.elasticsearch.common.geo.GeoPoint origin, DistanceUnit unit) {
                this.source = source;
                // even if the geo points are unique, there's no guarantee the distances are
                this.metaData = MetaData.builder(source.metaData()).uniqueness(MetaData.Uniqueness.UNKNOWN).build();
                this.distanceType = distanceType;
                this.unit = unit;
                this.origin = origin;
            }

            @Override
            public void setNextReader(AtomicReaderContext reader) {
                final MultiGeoPointValues geoValues = source.geoPointValues();
                final FixedSourceDistance distance = distanceType.fixedSourceDistance(origin.getLat(), origin.getLon(), unit);
                distanceValues = GeoDistance.distanceValues(distance, geoValues);
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
            public SortedNumericDocValues longValues() {
                throw new UnsupportedOperationException();
            }

            @Override
            public SortedNumericDoubleValues doubleValues() {
                return distanceValues;
            }

            @Override
            public SortedBinaryDocValues bytesValues() {
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5137.java