error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7396.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7396.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7396.java
text:
```scala
L@@ist<InternalTermsStatsLongFacet.LongEntry> longEntries = new ArrayList<>(entries.v().size());

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

package org.elasticsearch.search.facet.termsstats.longs;

import com.carrotsearch.hppc.LongObjectOpenHashMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.search.Scorer;
import org.elasticsearch.common.recycler.Recycler;
import org.elasticsearch.index.fielddata.DoubleValues;
import org.elasticsearch.index.fielddata.IndexNumericFieldData;
import org.elasticsearch.index.fielddata.LongValues;
import org.elasticsearch.script.SearchScript;
import org.elasticsearch.search.facet.DoubleFacetAggregatorBase;
import org.elasticsearch.search.facet.FacetExecutor;
import org.elasticsearch.search.facet.InternalFacet;
import org.elasticsearch.search.facet.LongFacetAggregatorBase;
import org.elasticsearch.search.facet.termsstats.TermsStatsFacet;
import org.elasticsearch.search.internal.SearchContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TermsStatsLongFacetExecutor extends FacetExecutor {

    private final TermsStatsFacet.ComparatorType comparatorType;
    final IndexNumericFieldData keyIndexFieldData;
    final IndexNumericFieldData valueIndexFieldData;
    final SearchScript script;

    private final int size;
    private final int shardSize;

    final Recycler.V<LongObjectOpenHashMap<InternalTermsStatsLongFacet.LongEntry>> entries;
    long missing;

    public TermsStatsLongFacetExecutor(IndexNumericFieldData keyIndexFieldData, IndexNumericFieldData valueIndexFieldData, SearchScript script,
                                       int size, int shardSize, TermsStatsFacet.ComparatorType comparatorType, SearchContext context) {
        this.size = size;
        this.shardSize = shardSize;
        this.comparatorType = comparatorType;
        this.keyIndexFieldData = keyIndexFieldData;
        this.valueIndexFieldData = valueIndexFieldData;
        this.script = script;

        this.entries = context.cacheRecycler().longObjectMap(-1);
    }

    @Override
    public Collector collector() {
        return new Collector();
    }

    @Override
    public InternalFacet buildFacet(String facetName) {
        if (entries.v().isEmpty()) {
            entries.release();
            return new InternalTermsStatsLongFacet(facetName, comparatorType, size, ImmutableList.<InternalTermsStatsLongFacet.LongEntry>of(), missing);
        }
        if (size == 0) { // all terms
            // all terms, just return the collection, we will sort it on the way back
            List<InternalTermsStatsLongFacet.LongEntry> longEntries = new ArrayList<InternalTermsStatsLongFacet.LongEntry>(entries.v().size());
            boolean[] states = entries.v().allocated;
            Object[] values = entries.v().values;
            for (int i = 0; i < states.length; i++) {
                if (states[i]) {
                    longEntries.add((InternalTermsStatsLongFacet.LongEntry) values[i]);
                }
            }

            entries.release();
            return new InternalTermsStatsLongFacet(facetName, comparatorType, 0 /* indicates all terms*/, longEntries, missing);
        }

        // we need to fetch facets of "size * numberOfShards" because of problems in how they are distributed across shards
        Object[] values = entries.v().values;
        Arrays.sort(values, (Comparator) comparatorType.comparator());

        int limit = shardSize;
        List<InternalTermsStatsLongFacet.LongEntry> ordered = Lists.newArrayList();
        for (int i = 0; i < limit; i++) {
            InternalTermsStatsLongFacet.LongEntry value = (InternalTermsStatsLongFacet.LongEntry) values[i];
            if (value == null) {
                break;
            }
            ordered.add(value);
        }
        entries.release();
        return new InternalTermsStatsLongFacet(facetName, comparatorType, size, ordered, missing);
    }

    class Collector extends FacetExecutor.Collector {

        private final Aggregator aggregator;
        private LongValues keyValues;

        public Collector() {
            if (script == null) {
                this.aggregator = new Aggregator(entries.v());
            } else {
                this.aggregator = new ScriptAggregator(entries.v(), script);
            }
        }

        @Override
        public void setScorer(Scorer scorer) throws IOException {
            if (script != null) {
                script.setScorer(scorer);
            }
        }

        @Override
        public void setNextReader(AtomicReaderContext context) throws IOException {
            keyValues = keyIndexFieldData.load(context).getLongValues();
            if (script != null) {
                script.setNextReader(context);
            } else {
                aggregator.valueValues = valueIndexFieldData.load(context).getDoubleValues();
            }
        }

        @Override
        public void collect(int doc) throws IOException {
            aggregator.onDoc(doc, keyValues);
        }

        @Override
        public void postCollection() {
            TermsStatsLongFacetExecutor.this.missing = aggregator.missing();
        }
    }

    public static class Aggregator extends LongFacetAggregatorBase {

        final LongObjectOpenHashMap<InternalTermsStatsLongFacet.LongEntry> entries;
        DoubleValues valueValues;
        final ValueAggregator valueAggregator = new ValueAggregator();

        public Aggregator(LongObjectOpenHashMap<InternalTermsStatsLongFacet.LongEntry> entries) {
            this.entries = entries;
        }

        @Override
        public void onValue(int docId, long value) {
            InternalTermsStatsLongFacet.LongEntry longEntry = entries.get(value);
            if (longEntry == null) {
                longEntry = new InternalTermsStatsLongFacet.LongEntry(value, 0, 0, 0, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
                entries.put(value, longEntry);
            }
            longEntry.count++;
            valueAggregator.longEntry = longEntry;
            valueAggregator.onDoc(docId, valueValues);
        }


        public final static class ValueAggregator extends DoubleFacetAggregatorBase {

            InternalTermsStatsLongFacet.LongEntry longEntry;

            @Override
            public void onValue(int docId, double value) {
                if (value < longEntry.min) {
                    longEntry.min = value;
                }
                if (value > longEntry.max) {
                    longEntry.max = value;
                }
                longEntry.total += value;
                longEntry.totalCount++;
            }
        }
    }

    public static class ScriptAggregator extends Aggregator {

        private final SearchScript script;

        public ScriptAggregator(LongObjectOpenHashMap<InternalTermsStatsLongFacet.LongEntry> entries, SearchScript script) {
            super(entries);
            this.script = script;
        }

        @Override
        public void onValue(int docId, long value) {
            InternalTermsStatsLongFacet.LongEntry longEntry = entries.get(value);
            if (longEntry == null) {
                longEntry = new InternalTermsStatsLongFacet.LongEntry(value, 1, 0, 0, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
                entries.put(value, longEntry);
            } else {
                longEntry.count++;
            }
            script.setNextDocId(docId);
            double valueValue = script.runAsDouble();
            if (valueValue < longEntry.min) {
                longEntry.min = valueValue;
            }
            if (valueValue > longEntry.max) {
                longEntry.max = valueValue;
            }
            longEntry.totalCount++;
            longEntry.total += valueValue;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7396.java