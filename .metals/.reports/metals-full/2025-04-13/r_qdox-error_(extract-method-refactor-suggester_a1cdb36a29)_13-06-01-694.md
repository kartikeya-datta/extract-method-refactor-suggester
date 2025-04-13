error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7392.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7392.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7392.java
text:
```scala
B@@oundedTreeSet<InternalDoubleTermsFacet.DoubleEntry> ordered = new BoundedTreeSet<>(comparatorType.comparator(), shardSize);

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
package org.elasticsearch.search.facet.terms.doubles;

import com.carrotsearch.hppc.DoubleIntOpenHashMap;
import com.carrotsearch.hppc.DoubleOpenHashSet;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.cache.recycler.CacheRecycler;
import org.elasticsearch.common.collect.BoundedTreeSet;
import org.elasticsearch.common.recycler.Recycler;
import org.elasticsearch.index.fielddata.DoubleValues;
import org.elasticsearch.index.fielddata.IndexNumericFieldData;
import org.elasticsearch.index.fielddata.ordinals.Ordinals;
import org.elasticsearch.script.SearchScript;
import org.elasticsearch.search.facet.DoubleFacetAggregatorBase;
import org.elasticsearch.search.facet.FacetExecutor;
import org.elasticsearch.search.facet.InternalFacet;
import org.elasticsearch.search.facet.terms.TermsFacet;
import org.elasticsearch.search.facet.terms.support.EntryPriorityQueue;
import org.elasticsearch.search.internal.SearchContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

/**
 *
 */
public class TermsDoubleFacetExecutor extends FacetExecutor {

    private final IndexNumericFieldData indexFieldData;
    private final TermsFacet.ComparatorType comparatorType;
    private final int size;
    private final int shardSize;
    private final SearchScript script;
    private final ImmutableSet<BytesRef> excluded;

    final Recycler.V<DoubleIntOpenHashMap> facets;
    long missing;
    long total;

    public TermsDoubleFacetExecutor(IndexNumericFieldData indexFieldData, int size, int shardSize, TermsFacet.ComparatorType comparatorType, boolean allTerms, SearchContext context,
                                    ImmutableSet<BytesRef> excluded, SearchScript script, CacheRecycler cacheRecycler) {
        this.indexFieldData = indexFieldData;
        this.size = size;
        this.shardSize = shardSize;
        this.comparatorType = comparatorType;
        this.script = script;
        this.excluded = excluded;

        this.facets = cacheRecycler.doubleIntMap(-1);

        if (allTerms) {
            for (AtomicReaderContext readerContext : context.searcher().getTopReaderContext().leaves()) {
                int maxDoc = readerContext.reader().maxDoc();
                DoubleValues values = indexFieldData.load(readerContext).getDoubleValues();
                if (values instanceof DoubleValues.WithOrdinals) {
                    DoubleValues.WithOrdinals valuesWithOrds = (DoubleValues.WithOrdinals) values;
                    Ordinals.Docs ordinals = valuesWithOrds.ordinals();
                    for (long ord = Ordinals.MIN_ORDINAL; ord < ordinals.getMaxOrd(); ord++) {
                        facets.v().putIfAbsent(valuesWithOrds.getValueByOrd(ord), 0);
                    }
                } else {
                    for (int docId = 0; docId < maxDoc; docId++) {
                        int numValues = values.setDocument(docId);
                        DoubleIntOpenHashMap map = facets.v();
                        for (int i = 0; i < numValues; i++) {
                            map.putIfAbsent(values.nextValue(), 0);
                        }
                    }
                }
            }
        }
    }

    @Override
    public Collector collector() {
        return new Collector();
    }

    @Override
    public InternalFacet buildFacet(String facetName) {
        if (facets.v().isEmpty()) {
            facets.release();
            return new InternalDoubleTermsFacet(facetName, comparatorType, size, ImmutableList.<InternalDoubleTermsFacet.DoubleEntry>of(), missing, total);
        } else {
            final boolean[] states = facets.v().allocated;
            final double[] keys = facets.v().keys;
            final int[] values = facets.v().values;
            if (size < EntryPriorityQueue.LIMIT) {
                EntryPriorityQueue ordered = new EntryPriorityQueue(shardSize, comparatorType.comparator());
                for (int i = 0; i < states.length; i++) {
                    if (states[i]) {
                        ordered.insertWithOverflow(new InternalDoubleTermsFacet.DoubleEntry(keys[i], values[i]));
                    }
                }
                InternalDoubleTermsFacet.DoubleEntry[] list = new InternalDoubleTermsFacet.DoubleEntry[ordered.size()];
                for (int i = ordered.size() - 1; i >= 0; i--) {
                    list[i] = (InternalDoubleTermsFacet.DoubleEntry) ordered.pop();
                }
                facets.release();
                return new InternalDoubleTermsFacet(facetName, comparatorType, size, Arrays.asList(list), missing, total);
            } else {
                BoundedTreeSet<InternalDoubleTermsFacet.DoubleEntry> ordered = new BoundedTreeSet<InternalDoubleTermsFacet.DoubleEntry>(comparatorType.comparator(), shardSize);
                for (int i = 0; i < states.length; i++) {
                    if (states[i]) {
                        ordered.add(new InternalDoubleTermsFacet.DoubleEntry(keys[i], values[i]));
                    }
                }
                facets.release();
                return new InternalDoubleTermsFacet(facetName, comparatorType, size, ordered, missing, total);
            }
        }
    }

    class Collector extends FacetExecutor.Collector {

        private final StaticAggregatorValueProc aggregator;
        private DoubleValues values;

        public Collector() {
            if (script == null && excluded.isEmpty()) {
                aggregator = new StaticAggregatorValueProc(facets.v());
            } else {
                aggregator = new AggregatorValueProc(facets.v(), excluded, script);
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
            values = indexFieldData.load(context).getDoubleValues();
            if (script != null) {
                script.setNextReader(context);
            }
        }

        @Override
        public void collect(int doc) throws IOException {
            aggregator.onDoc(doc, values);
        }

        @Override
        public void postCollection() {
            TermsDoubleFacetExecutor.this.missing = aggregator.missing();
            TermsDoubleFacetExecutor.this.total = aggregator.total();
        }
    }

    public static class AggregatorValueProc extends StaticAggregatorValueProc {

        private final SearchScript script;

        private final DoubleOpenHashSet excluded;

        public AggregatorValueProc(DoubleIntOpenHashMap facets, Set<BytesRef> excluded, SearchScript script) {
            super(facets);
            this.script = script;
            if (excluded == null || excluded.isEmpty()) {
                this.excluded = null;
            } else {
                this.excluded = new DoubleOpenHashSet(excluded.size());
                for (BytesRef s : excluded) {
                    this.excluded.add(Double.parseDouble(s.utf8ToString()));
                }
            }
        }

        @Override
        public void onValue(int docId, double value) {
            if (excluded != null && excluded.contains(value)) {
                return;
            }
            if (script != null) {
                script.setNextDocId(docId);
                script.setNextVar("term", value);
                Object scriptValue = script.run();
                if (scriptValue == null) {
                    return;
                }
                if (scriptValue instanceof Boolean) {
                    if (!((Boolean) scriptValue)) {
                        return;
                    }
                } else {
                    value = ((Number) scriptValue).doubleValue();
                }
            }
            super.onValue(docId, value);
        }
    }

    public static class StaticAggregatorValueProc extends DoubleFacetAggregatorBase {

        private final DoubleIntOpenHashMap facets;

        public StaticAggregatorValueProc(DoubleIntOpenHashMap facets) {
            this.facets = facets;
        }

        @Override
        public void onValue(int docId, double value) {
            facets.addTo(value, 1);
        }

        public final DoubleIntOpenHashMap facets() {
            return facets;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7392.java