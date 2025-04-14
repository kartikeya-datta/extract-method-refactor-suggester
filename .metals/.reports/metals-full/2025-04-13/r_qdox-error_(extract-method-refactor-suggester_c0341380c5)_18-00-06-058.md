error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7369.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7369.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7369.java
text:
```scala
V@@aluesSourceConfig<GeoPointValuesSource> config = new ValuesSourceConfig<>(GeoPointValuesSource.class);

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
package org.elasticsearch.search.aggregations.bucket.geogrid;

import org.elasticsearch.common.geo.GeoHashUtils;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.fielddata.*;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.index.query.GeoBoundingBoxFilterBuilder;
import org.elasticsearch.search.aggregations.Aggregator;
import org.elasticsearch.search.aggregations.AggregatorFactory;
import org.elasticsearch.search.aggregations.bucket.BucketUtils;
import org.elasticsearch.search.aggregations.support.*;
import org.elasticsearch.search.aggregations.support.geopoints.GeoPointValuesSource;
import org.elasticsearch.search.aggregations.support.numeric.NumericValuesSource;
import org.elasticsearch.search.internal.SearchContext;

import java.io.IOException;

/**
 * Aggregates Geo information into cells determined by geohashes of a given precision.
 * WARNING - for high-precision geohashes it may prove necessary to use a {@link GeoBoundingBoxFilterBuilder}
 * aggregation to focus in on a smaller area to avoid generating too many buckets and using too much RAM
 */
public class GeoHashGridParser implements Aggregator.Parser {

    @Override
    public String type() {
        return InternalGeoHashGrid.TYPE.name();
    }

    public static final int DEFAULT_PRECISION = 5;
    public static final int DEFAULT_MAX_NUM_CELLS = 10000;

    @Override
    public AggregatorFactory parse(String aggregationName, XContentParser parser, SearchContext context) throws IOException {

        String field = null;
        int precision = DEFAULT_PRECISION;
        int requiredSize = DEFAULT_MAX_NUM_CELLS;
        int shardSize = -1;


        XContentParser.Token token;
        String currentFieldName = null;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
            } else if (token == XContentParser.Token.VALUE_STRING) {
                if ("field".equals(currentFieldName)) {
                    field = parser.text();
                }
            } else if (token == XContentParser.Token.VALUE_NUMBER) {
                if ("precision".equals(currentFieldName)) {
                    precision = parser.intValue();
                } else if ("size".equals(currentFieldName)) {
                    requiredSize = parser.intValue();
                } else if ("shard_size".equals(currentFieldName) || "shardSize".equals(currentFieldName)) {
                    shardSize = parser.intValue();
                }

            }
        }

        if (shardSize == 0) {
            shardSize = Integer.MAX_VALUE;
        }

        if (requiredSize == 0) {
            requiredSize = Integer.MAX_VALUE;
        }

        if (shardSize < 0) {
            //Use default heuristic to avoid any wrong-ranking caused by distributed counting            
            shardSize = BucketUtils.suggestShardSideQueueSize(requiredSize, context.numberOfShards());
        }

        if (shardSize < requiredSize) {
            shardSize = requiredSize;
        }

        ValuesSourceConfig<GeoPointValuesSource> config = new ValuesSourceConfig<GeoPointValuesSource>(GeoPointValuesSource.class);
        if (field == null) {
            return new GeoGridFactory(aggregationName, config, precision, requiredSize, shardSize);
        }

        FieldMapper<?> mapper = context.smartNameFieldMapper(field);
        if (mapper == null) {
            config.unmapped(true);
            return new GeoGridFactory(aggregationName, config, precision, requiredSize, shardSize);
        }

        IndexFieldData<?> indexFieldData = context.fieldData().getForField(mapper);
        config.fieldContext(new FieldContext(field, indexFieldData));
        return new GeoGridFactory(aggregationName, config, precision, requiredSize, shardSize);
    }


    private static class GeoGridFactory extends ValueSourceAggregatorFactory<GeoPointValuesSource> {

        private int precision;
        private int requiredSize;
        private int shardSize;

        public GeoGridFactory(String name, ValuesSourceConfig<GeoPointValuesSource> valueSourceConfig,
                              int precision, int requiredSize, int shardSize) {
            super(name, InternalGeoHashGrid.TYPE.name(), valueSourceConfig);
            this.precision = precision;
            this.requiredSize = requiredSize;
            this.shardSize = shardSize;
        }

        @Override
        protected Aggregator createUnmapped(AggregationContext aggregationContext, Aggregator parent) {
            return new GeoHashGridAggregator.Unmapped(name, requiredSize, aggregationContext, parent);
        }

        @Override
        protected Aggregator create(final GeoPointValuesSource valuesSource, long expectedBucketsCount, AggregationContext aggregationContext, Aggregator parent) {
            final CellValues cellIdValues = new CellValues(valuesSource, precision);
            FieldDataSource.Numeric cellIdSource = new CellIdSource(cellIdValues, valuesSource.metaData());
            if (cellIdSource.metaData().multiValued()) {
                // we need to wrap to ensure uniqueness
                cellIdSource = new FieldDataSource.Numeric.SortedAndUnique(cellIdSource);
            }
            final NumericValuesSource geohashIdSource = new NumericValuesSource(cellIdSource, null, null);
            return new GeoHashGridAggregator(name, factories, geohashIdSource, requiredSize,
                    shardSize, aggregationContext, parent);

        }

        private static class CellValues extends LongValues {

            private GeoPointValuesSource geoPointValues;
            private GeoPointValues geoValues;
            private int precision;

            protected CellValues(GeoPointValuesSource geoPointValues, int precision) {
                super(true);
                this.geoPointValues = geoPointValues;
                this.precision = precision;
            }

            @Override
            public int setDocument(int docId) {
                geoValues = geoPointValues.values();
                return geoValues.setDocument(docId);
            }

            @Override
            public long nextValue() {
                GeoPoint target = geoValues.nextValue();
                return GeoHashUtils.encodeAsLong(target.getLat(), target.getLon(), precision);
            }

        }

        private static class CellIdSource extends FieldDataSource.Numeric {
            private final LongValues values;
            private MetaData metaData;

            public CellIdSource(LongValues values, MetaData delegate) {
                this.values = values;
                //different GeoPoints could map to the same or different geohash cells.
                this.metaData = MetaData.builder(delegate).uniqueness(MetaData.Uniqueness.UNKNOWN).build();
            }

            @Override
            public boolean isFloatingPoint() {
                return false;
            }

            @Override
            public LongValues longValues() {
                return values;
            }

            @Override
            public DoubleValues doubleValues() {
                throw new UnsupportedOperationException();
            }

            @Override
            public BytesValues bytesValues() {
                throw new UnsupportedOperationException();
            }

            @Override
            public MetaData metaData() {
                return metaData;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7369.java