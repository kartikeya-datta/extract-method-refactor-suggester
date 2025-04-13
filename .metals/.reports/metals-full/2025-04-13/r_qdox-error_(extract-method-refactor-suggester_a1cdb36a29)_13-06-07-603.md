error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7397.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7397.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7397.java
text:
```scala
L@@ist<InternalTermsStatsStringFacet.StringEntry> stringEntries = new ArrayList<>();

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

package org.elasticsearch.search.facet.termsstats.strings;

import com.carrotsearch.hppc.ObjectObjectOpenHashMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.common.lucene.HashedBytesRef;
import org.elasticsearch.common.recycler.Recycler;
import org.elasticsearch.index.fielddata.BytesValues;
import org.elasticsearch.index.fielddata.DoubleValues;
import org.elasticsearch.index.fielddata.IndexFieldData;
import org.elasticsearch.index.fielddata.IndexNumericFieldData;
import org.elasticsearch.script.SearchScript;
import org.elasticsearch.search.facet.DoubleFacetAggregatorBase;
import org.elasticsearch.search.facet.FacetExecutor;
import org.elasticsearch.search.facet.InternalFacet;
import org.elasticsearch.search.facet.terms.strings.HashedAggregator;
import org.elasticsearch.search.facet.termsstats.TermsStatsFacet;
import org.elasticsearch.search.internal.SearchContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TermsStatsStringFacetExecutor extends FacetExecutor {

    private final TermsStatsFacet.ComparatorType comparatorType;
    final IndexFieldData keyIndexFieldData;
    final IndexNumericFieldData valueIndexFieldData;
    final SearchScript script;
    private final int size;
    private final int shardSize;

    final Recycler.V<ObjectObjectOpenHashMap<HashedBytesRef, InternalTermsStatsStringFacet.StringEntry>> entries;
    long missing;

    public TermsStatsStringFacetExecutor(IndexFieldData keyIndexFieldData, IndexNumericFieldData valueIndexFieldData, SearchScript valueScript,
                                         int size, int shardSize, TermsStatsFacet.ComparatorType comparatorType, SearchContext context) {
        this.keyIndexFieldData = keyIndexFieldData;
        this.valueIndexFieldData = valueIndexFieldData;
        this.script = valueScript;
        this.size = size;
        this.shardSize = shardSize;
        this.comparatorType = comparatorType;

        this.entries = context.cacheRecycler().hashMap(-1);
    }

    @Override
    public Collector collector() {
        return new Collector();
    }

    @Override
    public InternalFacet buildFacet(String facetName) {
        if (entries.v().isEmpty()) {
            entries.release();
            return new InternalTermsStatsStringFacet(facetName, comparatorType, size, ImmutableList.<InternalTermsStatsStringFacet.StringEntry>of(), missing);
        }
        if (size == 0) { // all terms
            // all terms, just return the collection, we will sort it on the way back
            List<InternalTermsStatsStringFacet.StringEntry> stringEntries = new ArrayList<InternalTermsStatsStringFacet.StringEntry>();
            final boolean[] states = entries.v().allocated;
            final Object[] values = entries.v().values;
            for (int i = 0; i < states.length; i++) {
                if (states[i]) {
                    stringEntries.add((InternalTermsStatsStringFacet.StringEntry) values[i]);
                }
            }
            return new InternalTermsStatsStringFacet(facetName, comparatorType, 0 /* indicates all terms*/, stringEntries, missing);
        }
        Object[] values = entries.v().values;
        Arrays.sort(values, (Comparator) comparatorType.comparator());

        List<InternalTermsStatsStringFacet.StringEntry> ordered = Lists.newArrayList();
        int limit = shardSize;
        for (int i = 0; i < limit; i++) {
            InternalTermsStatsStringFacet.StringEntry value = (InternalTermsStatsStringFacet.StringEntry) values[i];
            if (value == null) {
                break;
            }
            ordered.add(value);
        }

        entries.release();
        return new InternalTermsStatsStringFacet(facetName, comparatorType, size, ordered, missing);
    }

    class Collector extends FacetExecutor.Collector {

        private final Aggregator aggregator;
        private BytesValues keyValues;

        public Collector() {
            if (script != null) {
                this.aggregator = new ScriptAggregator(entries.v(), script);
            } else {
                this.aggregator = new Aggregator(entries.v());
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
            keyValues = keyIndexFieldData.load(context).getBytesValues(true);
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
            TermsStatsStringFacetExecutor.this.missing = aggregator.missing;
            aggregator.release();
        }
    }

    public static class Aggregator extends HashedAggregator {

        final ObjectObjectOpenHashMap<HashedBytesRef, InternalTermsStatsStringFacet.StringEntry> entries;
        final HashedBytesRef spare = new HashedBytesRef();
        int missing = 0;

        DoubleValues valueValues;

        ValueAggregator valueAggregator = new ValueAggregator();

        public Aggregator(ObjectObjectOpenHashMap<HashedBytesRef, InternalTermsStatsStringFacet.StringEntry> entries) {
            this.entries = entries;
        }

        @Override
        public void onValue(int docId, BytesRef value, int hashCode, BytesValues values) {
            spare.reset(value, hashCode);
            InternalTermsStatsStringFacet.StringEntry stringEntry = entries.get(spare);
            if (stringEntry == null) {
                HashedBytesRef theValue = new HashedBytesRef(values.copyShared(), hashCode);
                stringEntry = new InternalTermsStatsStringFacet.StringEntry(theValue, 0, 0, 0, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
                entries.put(theValue, stringEntry);
            }
            stringEntry.count++;
            valueAggregator.stringEntry = stringEntry;
            valueAggregator.onDoc(docId, valueValues);
        }

        public static class ValueAggregator extends DoubleFacetAggregatorBase {

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

        public ScriptAggregator(ObjectObjectOpenHashMap<HashedBytesRef, InternalTermsStatsStringFacet.StringEntry> entries, SearchScript script) {
            super(entries);
            this.script = script;
        }

        @Override
        public void onValue(int docId, BytesRef value, int hashCode, BytesValues values) {
            spare.reset(value, hashCode);
            InternalTermsStatsStringFacet.StringEntry stringEntry = entries.get(spare);
            if (stringEntry == null) {
                HashedBytesRef theValue = new HashedBytesRef(values.copyShared(), hashCode);
                stringEntry = new InternalTermsStatsStringFacet.StringEntry(theValue, 1, 0, 0, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
                entries.put(theValue, stringEntry);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7397.java