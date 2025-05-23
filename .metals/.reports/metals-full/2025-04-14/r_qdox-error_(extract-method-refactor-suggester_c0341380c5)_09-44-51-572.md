error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3064.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3064.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3064.java
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

package org.elasticsearch.search.facet.histogram.bounded;

import org.apache.lucene.index.IndexReader;
import org.elasticsearch.common.CacheRecycler;
import org.elasticsearch.index.cache.field.data.FieldDataCache;
import org.elasticsearch.index.field.data.FieldDataType;
import org.elasticsearch.index.field.data.NumericFieldData;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.index.mapper.MapperService;
import org.elasticsearch.search.facet.AbstractFacetCollector;
import org.elasticsearch.search.facet.Facet;
import org.elasticsearch.search.facet.FacetPhaseExecutionException;
import org.elasticsearch.search.facet.histogram.HistogramFacet;
import org.elasticsearch.search.internal.SearchContext;

import java.io.IOException;

public class BoundedCountHistogramFacetCollector extends AbstractFacetCollector {

    private final String indexFieldName;

    private final HistogramFacet.ComparatorType comparatorType;

    private final FieldDataCache fieldDataCache;

    private final FieldDataType fieldDataType;

    private NumericFieldData fieldData;

    private final HistogramProc histoProc;

    public BoundedCountHistogramFacetCollector(String facetName, String fieldName, long interval, long from, long to, HistogramFacet.ComparatorType comparatorType, SearchContext context) {
        super(facetName);
        this.comparatorType = comparatorType;
        this.fieldDataCache = context.fieldDataCache();

        MapperService.SmartNameFieldMappers smartMappers = context.smartFieldMappers(fieldName);
        if (smartMappers == null || !smartMappers.hasMapper()) {
            throw new FacetPhaseExecutionException(facetName, "No mapping found for field [" + fieldName + "]");
        }

        // add type filter if there is exact doc mapper associated with it
        if (smartMappers.hasDocMapper() && smartMappers.explicitTypeInName()) {
            setFilter(context.filterCache().cache(smartMappers.docMapper().typeFilter()));
        }

        FieldMapper mapper = smartMappers.mapper();

        indexFieldName = mapper.names().indexName();
        fieldDataType = mapper.fieldDataType();

        long normalizedFrom = (((long) ((double) from / interval)) * interval);
        long normalizedTo = (((long) ((double) to / interval)) * interval);
        if ((to % interval) != 0) {
            normalizedTo += interval;
        }
        long offset = -normalizedFrom;
        int size = (int) ((normalizedTo - normalizedFrom) / interval);

        histoProc = new HistogramProc(from, to, interval, offset, size);
    }

    @Override
    protected void doCollect(int doc) throws IOException {
        fieldData.forEachValueInDoc(doc, histoProc);
    }

    @Override
    protected void doSetNextReader(IndexReader reader, int docBase) throws IOException {
        fieldData = (NumericFieldData) fieldDataCache.cache(fieldDataType, reader, indexFieldName);
    }

    @Override
    public Facet facet() {
        return new InternalBoundedCountHistogramFacet(facetName, comparatorType, histoProc.interval, -histoProc.offset, histoProc.size, histoProc.counts, true);
    }

    public static class HistogramProc implements NumericFieldData.LongValueInDocProc {

        final long from;
        final long to;

        final long interval;

        final long offset;

        final int size;

        final int[] counts;

        public HistogramProc(long from, long to, long interval, long offset, int size) {
            this.from = from;
            this.to = to;
            this.interval = interval;
            this.offset = offset;
            this.size = size;
            this.counts = CacheRecycler.popIntArray(size);
        }

        @Override
        public void onValue(int docId, long value) {
            if (value <= from || value > to) { // bounds check
                return;
            }
            counts[((int) ((value + offset) / interval))]++;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3064.java