error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7163.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7163.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7163.java
text:
```scala
G@@eoDistance geoDistance = GeoDistance.DEFAULT;

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

package org.elasticsearch.index.query;

import org.apache.lucene.search.Filter;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.geo.GeoHashUtils;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.geo.GeoUtils;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.cache.filter.support.CacheKeyFilter;
import org.elasticsearch.index.fielddata.IndexGeoPointFieldData;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.index.mapper.MapperService;
import org.elasticsearch.index.mapper.geo.GeoPointFieldMapper;
import org.elasticsearch.index.search.geo.GeoDistanceFilter;

import java.io.IOException;

import static org.elasticsearch.index.query.support.QueryParsers.wrapSmartNameFilter;

/**
 * <pre>
 * {
 *     "name.lat" : 1.1,
 *     "name.lon" : 1.2,
 * }
 * </pre>
 */
public class GeoDistanceFilterParser implements FilterParser {

    public static final String NAME = "geo_distance";

    @Inject
    public GeoDistanceFilterParser() {
    }

    @Override
    public String[] names() {
        return new String[]{NAME, "geoDistance"};
    }

    @Override
    public Filter parse(QueryParseContext parseContext) throws IOException, QueryParsingException {
        XContentParser parser = parseContext.parser();

        XContentParser.Token token;

        boolean cache = false;
        CacheKeyFilter.Key cacheKey = null;
        String filterName = null;
        String currentFieldName = null;
        GeoPoint point = new GeoPoint();
        String fieldName = null;
        double distance = 0;
        Object vDistance = null;
        DistanceUnit unit = DistanceUnit.KILOMETERS; // default unit
        GeoDistance geoDistance = GeoDistance.ARC;
        String optimizeBbox = "memory";
        boolean normalizeLon = true;
        boolean normalizeLat = true;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
            } else if (token == XContentParser.Token.START_ARRAY) {
                fieldName = currentFieldName;
                GeoPoint.parse(parser, point);
            } else if (token == XContentParser.Token.START_OBJECT) {
                // the json in the format of -> field : { lat : 30, lon : 12 }
                String currentName = parser.currentName();
                fieldName = currentFieldName;
                while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                    if (token == XContentParser.Token.FIELD_NAME) {
                        currentName = parser.currentName();
                    } else if (token.isValue()) {
                        if (currentName.equals(GeoPointFieldMapper.Names.LAT)) {
                            point.resetLat(parser.doubleValue());
                        } else if (currentName.equals(GeoPointFieldMapper.Names.LON)) {
                            point.resetLon(parser.doubleValue());
                        } else if (currentName.equals(GeoPointFieldMapper.Names.GEOHASH)) {
                            GeoHashUtils.decode(parser.text(), point);
                        } else {
                            throw new QueryParsingException(parseContext.index(), "[geo_distance] filter does not support [" + currentFieldName + "]");
                        }
                    }
                }
            } else if (token.isValue()) {
                if (currentFieldName.equals("distance")) {
                    if (token == XContentParser.Token.VALUE_STRING) {
                        vDistance = parser.text(); // a String
                    } else {
                        vDistance = parser.numberValue(); // a Number
                    }
                } else if (currentFieldName.equals("unit")) {
                    unit = DistanceUnit.fromString(parser.text());
                } else if (currentFieldName.equals("distance_type") || currentFieldName.equals("distanceType")) {
                    geoDistance = GeoDistance.fromString(parser.text());
                } else if (currentFieldName.endsWith(GeoPointFieldMapper.Names.LAT_SUFFIX)) {
                    point.resetLat(parser.doubleValue());
                    fieldName = currentFieldName.substring(0, currentFieldName.length() - GeoPointFieldMapper.Names.LAT_SUFFIX.length());
                } else if (currentFieldName.endsWith(GeoPointFieldMapper.Names.LON_SUFFIX)) {
                    point.resetLon(parser.doubleValue());
                    fieldName = currentFieldName.substring(0, currentFieldName.length() - GeoPointFieldMapper.Names.LON_SUFFIX.length());
                } else if (currentFieldName.endsWith(GeoPointFieldMapper.Names.GEOHASH_SUFFIX)) {
                    GeoHashUtils.decode(parser.text(), point);
                    fieldName = currentFieldName.substring(0, currentFieldName.length() - GeoPointFieldMapper.Names.GEOHASH_SUFFIX.length());
                } else if ("_name".equals(currentFieldName)) {
                    filterName = parser.text();
                } else if ("_cache".equals(currentFieldName)) {
                    cache = parser.booleanValue();
                } else if ("_cache_key".equals(currentFieldName) || "_cacheKey".equals(currentFieldName)) {
                    cacheKey = new CacheKeyFilter.Key(parser.text());
                } else if ("optimize_bbox".equals(currentFieldName) || "optimizeBbox".equals(currentFieldName)) {
                    optimizeBbox = parser.textOrNull();
                } else if ("normalize".equals(currentFieldName)) {
                    normalizeLat = parser.booleanValue();
                    normalizeLon = parser.booleanValue();
                } else {
                    point.resetFromString(parser.text());
                    fieldName = currentFieldName;
                }
            }
        }

        if (vDistance instanceof Number) {
            distance = unit.toMiles(((Number) vDistance).doubleValue());
        } else {
            distance = DistanceUnit.parse((String) vDistance, unit, DistanceUnit.MILES);
        }
        distance = geoDistance.normalize(distance, DistanceUnit.MILES);

        if (normalizeLat || normalizeLon) {
            GeoUtils.normalizePoint(point, normalizeLat, normalizeLon);
        }

        MapperService.SmartNameFieldMappers smartMappers = parseContext.smartFieldMappers(fieldName);
        if (smartMappers == null || !smartMappers.hasMapper()) {
            throw new QueryParsingException(parseContext.index(), "failed to find geo_point field [" + fieldName + "]");
        }
        FieldMapper<?> mapper = smartMappers.mapper();
        if (!(mapper instanceof GeoPointFieldMapper)) {
            throw new QueryParsingException(parseContext.index(), "field [" + fieldName + "] is not a geo_point field");
        }
        GeoPointFieldMapper geoMapper = ((GeoPointFieldMapper) mapper);


        IndexGeoPointFieldData<?> indexFieldData = parseContext.fieldData().getForField(mapper);
        Filter filter = new GeoDistanceFilter(point.lat(), point.lon(), distance, geoDistance, indexFieldData, geoMapper, optimizeBbox);
        if (cache) {
            filter = parseContext.cacheFilter(filter, cacheKey);
        }
        filter = wrapSmartNameFilter(filter, smartMappers, parseContext);
        if (filterName != null) {
            parseContext.addNamedFilter(filterName, filter);
        }
        return filter;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7163.java