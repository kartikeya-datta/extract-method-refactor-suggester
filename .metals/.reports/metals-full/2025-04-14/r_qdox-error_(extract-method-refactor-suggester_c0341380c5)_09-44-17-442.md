error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3090.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3090.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3090.java
text:
```scala
i@@f (smartMappers.explicitTypeInNameWithDocMapper()) {

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

package org.elasticsearch.search.facet.termsstats.strings;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Scorer;
import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.common.CacheRecycler;
import org.elasticsearch.common.trove.ExtTHashMap;
import org.elasticsearch.index.cache.field.data.FieldDataCache;
import org.elasticsearch.index.field.data.FieldData;
import org.elasticsearch.index.field.data.FieldDataType;
import org.elasticsearch.index.field.data.NumericFieldData;
import org.elasticsearch.index.mapper.MapperService;
import org.elasticsearch.script.SearchScript;
import org.elasticsearch.search.facet.AbstractFacetCollector;
import org.elasticsearch.search.facet.Facet;
import org.elasticsearch.search.facet.termsstats.TermsStatsFacet;
import org.elasticsearch.search.internal.SearchContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class TermsStatsStringFacetCollector extends AbstractFacetCollector {

    private final TermsStatsFacet.ComparatorType comparatorType;

    private final FieldDataCache fieldDataCache;

    private final String keyFieldName;

    private final String valueFieldName;

    private final int size;

    private final int numberOfShards;

    private final FieldDataType keyFieldDataType;

    private FieldData keyFieldData;

    private final FieldDataType valueFieldDataType;

    private final SearchScript script;

    private final Aggregator aggregator;

    public TermsStatsStringFacetCollector(String facetName, String keyFieldName, String valueFieldName, int size, TermsStatsFacet.ComparatorType comparatorType,
                                          SearchContext context, String scriptLang, String script, Map<String, Object> params) {
        super(facetName);
        this.fieldDataCache = context.fieldDataCache();
        this.size = size;
        this.comparatorType = comparatorType;
        this.numberOfShards = context.numberOfShards();

        MapperService.SmartNameFieldMappers smartMappers = context.smartFieldMappers(keyFieldName);
        if (smartMappers == null || !smartMappers.hasMapper()) {
            this.keyFieldName = keyFieldName;
            this.keyFieldDataType = FieldDataType.DefaultTypes.STRING;
        } else {
            // add type filter if there is exact doc mapper associated with it
            if (smartMappers.hasDocMapper() && smartMappers.explicitTypeInName()) {
                setFilter(context.filterCache().cache(smartMappers.docMapper().typeFilter()));
            }

            this.keyFieldName = smartMappers.mapper().names().indexName();
            this.keyFieldDataType = smartMappers.mapper().fieldDataType();
        }

        if (script == null) {
            smartMappers = context.smartFieldMappers(valueFieldName);
            if (smartMappers == null || !smartMappers.hasMapper()) {
                throw new ElasticSearchIllegalArgumentException("failed to find mappings for [" + valueFieldName + "]");
            }
            this.valueFieldName = smartMappers.mapper().names().indexName();
            this.valueFieldDataType = smartMappers.mapper().fieldDataType();
            this.script = null;
            this.aggregator = new Aggregator();
        } else {
            this.valueFieldName = null;
            this.valueFieldDataType = null;
            this.script = context.scriptService().search(context.lookup(), scriptLang, script, params);
            this.aggregator = new ScriptAggregator(this.script);
        }
    }

    @Override
    public void setScorer(Scorer scorer) throws IOException {
        if (script != null) {
            script.setScorer(scorer);
        }
    }

    @Override
    protected void doSetNextReader(IndexReader reader, int docBase) throws IOException {
        keyFieldData = fieldDataCache.cache(keyFieldDataType, reader, keyFieldName);
        if (script != null) {
            script.setNextReader(reader);
        } else {
            aggregator.valueFieldData = (NumericFieldData) fieldDataCache.cache(valueFieldDataType, reader, valueFieldName);
        }
    }

    @Override
    protected void doCollect(int doc) throws IOException {
        keyFieldData.forEachValueInDoc(doc, aggregator);
    }

    @Override
    public Facet facet() {
        if (aggregator.entries.isEmpty()) {
            return new InternalTermsStatsStringFacet(facetName, comparatorType, size, ImmutableList.<InternalTermsStatsStringFacet.StringEntry>of(), aggregator.missing);
        }
        if (size == 0) { // all terms
            // all terms, just return the collection, we will sort it on the way back
            return new InternalTermsStatsStringFacet(facetName, comparatorType, 0 /* indicates all terms*/, aggregator.entries.values(), aggregator.missing);
        }
        Object[] values = aggregator.entries.internalValues();
        Arrays.sort(values, (Comparator) comparatorType.comparator());

        List<InternalTermsStatsStringFacet.StringEntry> ordered = Lists.newArrayList();
        int limit = size;
        for (int i = 0; i < limit; i++) {
            InternalTermsStatsStringFacet.StringEntry value = (InternalTermsStatsStringFacet.StringEntry) values[i];
            if (value == null) {
                break;
            }
            ordered.add(value);
        }

        CacheRecycler.pushHashMap(aggregator.entries); // fine to push here, we are done with it
        return new InternalTermsStatsStringFacet(facetName, comparatorType, size, ordered, aggregator.missing);
    }

    public static class Aggregator implements FieldData.StringValueInDocProc {

        final ExtTHashMap<String, InternalTermsStatsStringFacet.StringEntry> entries = CacheRecycler.popHashMap();

        int missing = 0;

        NumericFieldData valueFieldData;

        ValueAggregator valueAggregator = new ValueAggregator();

        @Override
        public void onValue(int docId, String value) {
            InternalTermsStatsStringFacet.StringEntry stringEntry = entries.get(value);
            if (stringEntry == null) {
                stringEntry = new InternalTermsStatsStringFacet.StringEntry(value, 0, 0, 0, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
                entries.put(value, stringEntry);
            }
            stringEntry.count++;
            valueAggregator.stringEntry = stringEntry;
            valueFieldData.forEachValueInDoc(docId, valueAggregator);
        }

        @Override
        public void onMissing(int docId) {
            missing++;
        }

        public static class ValueAggregator implements NumericFieldData.DoubleValueInDocProc {

            InternalTermsStatsStringFacet.StringEntry stringEntry;

            @Override
            public void onValue(int docId, double value) {
                if (value < stringEntry.min) {
                    stringEntry.min = value;
                }
                if (value > stringEntry.max) {
                    stringEntry.max = value;
                }
                stringEntry.total += value;
                stringEntry.totalCount++;
            }
        }
    }

    public static class ScriptAggregator extends Aggregator {
        private final SearchScript script;

        public ScriptAggregator(SearchScript script) {
            this.script = script;
        }

        @Override
        public void onValue(int docId, String value) {
            InternalTermsStatsStringFacet.StringEntry stringEntry = entries.get(value);
            if (stringEntry == null) {
                stringEntry = new InternalTermsStatsStringFacet.StringEntry(value, 1, 0, 0, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
                entries.put(value, stringEntry);
            } else {
                stringEntry.count++;
            }

            script.setNextDocId(docId);
            double valueValue = script.runAsDouble();
            if (valueValue < stringEntry.min) {
                stringEntry.min = valueValue;
            }
            if (valueValue > stringEntry.max) {
                stringEntry.max = valueValue;
            }
            stringEntry.total += valueValue;
            stringEntry.totalCount++;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3090.java