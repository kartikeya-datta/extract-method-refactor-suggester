error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2183.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2183.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2183.java
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
package org.elasticsearch.search.facet.geodistance;

import com.google.common.collect.Lists;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.geo.GeoUtils;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.fielddata.IndexGeoPointFieldData;
import org.elasticsearch.index.fielddata.IndexNumericFieldData;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.search.facet.FacetExecutor;
import org.elasticsearch.search.facet.FacetParser;
import org.elasticsearch.search.facet.FacetPhaseExecutionException;
import org.elasticsearch.search.internal.SearchContext;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class GeoDistanceFacetParser extends AbstractComponent implements FacetParser {

    @Inject
    public GeoDistanceFacetParser(Settings settings) {
        super(settings);
        InternalGeoDistanceFacet.registerStreams();
    }

    @Override
    public String[] types() {
        return new String[]{GeoDistanceFacet.TYPE, "geoDistance"};
    }

    @Override
    public FacetExecutor.Mode defaultMainMode() {
        return FacetExecutor.Mode.COLLECTOR;
    }

    @Override
    public FacetExecutor.Mode defaultGlobalMode() {
        return FacetExecutor.Mode.COLLECTOR;
    }

    @Override
    public FacetExecutor parse(String facetName, XContentParser parser, SearchContext context) throws IOException {
        String fieldName = null;
        String valueFieldName = null;
        String valueScript = null;
        String scriptLang = null;
        Map<String, Object> params = null;
        GeoPoint point = new GeoPoint();
        DistanceUnit unit = DistanceUnit.KILOMETERS;
        GeoDistance geoDistance = GeoDistance.DEFAULT;
        List<GeoDistanceFacet.Entry> entries = Lists.newArrayList();

        boolean normalizeLon = true;
        boolean normalizeLat = true;

        XContentParser.Token token;
        String currentName = parser.currentName();

        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentName = parser.currentName();
            } else if (token == XContentParser.Token.START_ARRAY) {
                if ("ranges".equals(currentName) || "entries".equals(currentName)) {
                    // "ranges" : [
                    //     { "from" : 0, "to" : 12.5 }
                    //     { "from" : 12.5 }
                    // ]
                    while ((token = parser.nextToken()) != XContentParser.Token.END_ARRAY) {
                        double from = Double.NEGATIVE_INFINITY;
                        double to = Double.POSITIVE_INFINITY;
                        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                            if (token == XContentParser.Token.FIELD_NAME) {
                                currentName = parser.currentName();
                            } else if (token.isValue()) {
                                if ("from".equals(currentName)) {
                                    from = parser.doubleValue();
                                } else if ("to".equals(currentName)) {
                                    to = parser.doubleValue();
                                }
                            }
                        }
                        entries.add(new GeoDistanceFacet.Entry(from, to, 0, 0, 0, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY));
                    }
                } else {
                    GeoPoint.parse(parser, point);
                    fieldName = currentName;
                }
            } else if (token == XContentParser.Token.START_OBJECT) {
                if ("params".equals(currentName)) {
                    params = parser.map();
                } else {
                    // the json in the format of -> field : { lat : 30, lon : 12 }
                    fieldName = currentName;
                    GeoPoint.parse(parser, point);
                }
            } else if (token.isValue()) {
                if (currentName.equals("unit")) {
                    unit = DistanceUnit.fromString(parser.text());
                } else if (currentName.equals("distance_type") || currentName.equals("distanceType")) {
                    geoDistance = GeoDistance.fromString(parser.text());
                } else if ("value_field".equals(currentName) || "valueField".equals(currentName)) {
                    valueFieldName = parser.text();
                } else if ("value_script".equals(currentName) || "valueScript".equals(currentName)) {
                    valueScript = parser.text();
                } else if ("lang".equals(currentName)) {
                    scriptLang = parser.text();
                } else if ("normalize".equals(currentName)) {
                    normalizeLat = parser.booleanValue();
                    normalizeLon = parser.booleanValue();
                } else {
                    // assume the value is the actual value
                    point.resetFromString(parser.text());

                    fieldName = currentName;
                }
            }
        }

        if (entries.isEmpty()) {
            throw new FacetPhaseExecutionException(facetName, "no ranges defined for geo_distance facet");
        }

        if (normalizeLat || normalizeLon) {
            GeoUtils.normalizePoint(point, normalizeLat, normalizeLon);
        }

        FieldMapper keyFieldMapper = context.smartNameFieldMapper(fieldName);
        if (keyFieldMapper == null) {
            throw new FacetPhaseExecutionException(facetName, "failed to find mapping for [" + fieldName + "]");
        }
        IndexGeoPointFieldData keyIndexFieldData = context.fieldData().getForField(keyFieldMapper);

        if (valueFieldName != null) {
            FieldMapper valueFieldMapper = context.smartNameFieldMapper(valueFieldName);
            if (valueFieldMapper == null) {
                throw new FacetPhaseExecutionException(facetName, "failed to find mapping for [" + valueFieldName + "]");
            }
            IndexNumericFieldData valueIndexFieldData = context.fieldData().getForField(valueFieldMapper);
            return new ValueGeoDistanceFacetExecutor(keyIndexFieldData, point.lat(), point.lon(), unit, geoDistance, entries.toArray(new GeoDistanceFacet.Entry[entries.size()]),
                    context, valueIndexFieldData);
        }

        if (valueScript != null) {
            return new ScriptGeoDistanceFacetExecutor(keyIndexFieldData, point.lat(), point.lon(), unit, geoDistance, entries.toArray(new GeoDistanceFacet.Entry[entries.size()]),
                    context, scriptLang, valueScript, params);
        }

        return new GeoDistanceFacetExecutor(keyIndexFieldData, point.lat(), point.lon(), unit, geoDistance, entries.toArray(new GeoDistanceFacet.Entry[entries.size()]),
                context);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2183.java