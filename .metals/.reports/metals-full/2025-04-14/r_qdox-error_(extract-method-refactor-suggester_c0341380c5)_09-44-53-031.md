error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2390.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2390.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2390.java
text:
```scala
I@@ndexGeoPointFieldData indexFieldData = parseContext.getForField(mapper);

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

package org.elasticsearch.index.query;

import org.apache.lucene.search.Filter;
import org.elasticsearch.ElasticsearchParseException;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.geo.GeoUtils;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.cache.filter.support.CacheKeyFilter;
import org.elasticsearch.index.fielddata.IndexGeoPointFieldData;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.index.mapper.MapperService;
import org.elasticsearch.index.mapper.geo.GeoPointFieldMapper;
import org.elasticsearch.index.search.geo.InMemoryGeoBoundingBoxFilter;
import org.elasticsearch.index.search.geo.IndexedGeoBoundingBoxFilter;

import java.io.IOException;

import static org.elasticsearch.index.query.support.QueryParsers.wrapSmartNameFilter;

/**
 *
 */
public class GeoBoundingBoxFilterParser implements FilterParser {

    public static final String TOP = "top";
    public static final String LEFT = "left";
    public static final String RIGHT = "right";
    public static final String BOTTOM = "bottom";

    public static final String TOP_LEFT = TOP + "_" + LEFT;
    public static final String TOP_RIGHT = TOP + "_" + RIGHT;
    public static final String BOTTOM_LEFT = BOTTOM + "_" + LEFT;
    public static final String BOTTOM_RIGHT = BOTTOM + "_" + RIGHT;

    public static final String TOPLEFT = "topLeft";
    public static final String TOPRIGHT = "topRight";
    public static final String BOTTOMLEFT = "bottomLeft";
    public static final String BOTTOMRIGHT = "bottomRight";

    public static final String NAME = "geo_bbox";
    public static final String FIELD = "field";

    @Inject
    public GeoBoundingBoxFilterParser() {
    }

    @Override
    public String[] names() {
        return new String[]{NAME, "geoBbox", "geo_bounding_box", "geoBoundingBox"};
    }

    @Override
    public Filter parse(QueryParseContext parseContext) throws IOException, QueryParsingException {
        XContentParser parser = parseContext.parser();

        boolean cache = false;
        CacheKeyFilter.Key cacheKey = null;
        String fieldName = null;

        double top = Double.NaN;
        double bottom = Double.NaN;
        double left = Double.NaN;
        double right = Double.NaN;
        
        String filterName = null;
        String currentFieldName = null;
        XContentParser.Token token;
        boolean normalize = true;

        GeoPoint sparse = new GeoPoint();
        
        String type = "memory";

        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
            } else if (token == XContentParser.Token.START_OBJECT) {
                fieldName = currentFieldName;

                while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                    if (token == XContentParser.Token.FIELD_NAME) {
                        currentFieldName = parser.currentName();
                        token = parser.nextToken();
                        if (FIELD.equals(currentFieldName)) {
                            fieldName = parser.text();
                        } else if (TOP.equals(currentFieldName)) {
                            top = parser.doubleValue();
                        } else if (BOTTOM.equals(currentFieldName)) {
                            bottom = parser.doubleValue();
                        } else if (LEFT.equals(currentFieldName)) {
                            left = parser.doubleValue();
                        } else if (RIGHT.equals(currentFieldName)) {
                            right = parser.doubleValue();
                        } else {
                            if (TOP_LEFT.equals(currentFieldName) || TOPLEFT.equals(currentFieldName)) {
                                GeoUtils.parseGeoPoint(parser, sparse);
                                top = sparse.getLat();
                                left = sparse.getLon();
                            } else if (BOTTOM_RIGHT.equals(currentFieldName) || BOTTOMRIGHT.equals(currentFieldName)) {
                                GeoUtils.parseGeoPoint(parser, sparse);
                                bottom = sparse.getLat();
                                right = sparse.getLon();
                            } else if (TOP_RIGHT.equals(currentFieldName) || TOPRIGHT.equals(currentFieldName)) {
                                GeoUtils.parseGeoPoint(parser, sparse);
                                top = sparse.getLat();
                                right = sparse.getLon();
                            } else if (BOTTOM_LEFT.equals(currentFieldName) || BOTTOMLEFT.equals(currentFieldName)) {
                                GeoUtils.parseGeoPoint(parser, sparse);
                                bottom = sparse.getLat();
                                left = sparse.getLon();
                            } else {
                                throw new ElasticsearchParseException("Unexpected field [" + currentFieldName + "]");
                            }
                        }
                    } else {
                        throw new ElasticsearchParseException("fieldname expected but [" + token + "] found");
                    }
                }
            } else if (token.isValue()) {
                if ("_name".equals(currentFieldName)) {
                    filterName = parser.text();
                } else if ("_cache".equals(currentFieldName)) {
                    cache = parser.booleanValue();
                } else if ("_cache_key".equals(currentFieldName) || "_cacheKey".equals(currentFieldName)) {
                    cacheKey = new CacheKeyFilter.Key(parser.text());
                } else if ("normalize".equals(currentFieldName)) {
                    normalize = parser.booleanValue();
                } else if ("type".equals(currentFieldName)) {
                    type = parser.text();
                } else {
                    throw new QueryParsingException(parseContext.index(), "[geo_bbox] filter does not support [" + currentFieldName + "]");
                }
            }
        }

        final GeoPoint topLeft = sparse.reset(top, left);  //just keep the object
        final GeoPoint bottomRight = new GeoPoint(bottom, right);
        
        if (normalize) {
            GeoUtils.normalizePoint(topLeft);
            GeoUtils.normalizePoint(bottomRight);
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

        Filter filter;
        if ("indexed".equals(type)) {
            filter = IndexedGeoBoundingBoxFilter.create(topLeft, bottomRight, geoMapper);
        } else if ("memory".equals(type)) {
            IndexGeoPointFieldData<?> indexFieldData = parseContext.getForField(mapper);
            filter = new InMemoryGeoBoundingBoxFilter(topLeft, bottomRight, indexFieldData);
        } else {
            throw new QueryParsingException(parseContext.index(), "geo bounding box type [" + type + "] not supported, either 'indexed' or 'memory' are allowed");
        }

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2390.java