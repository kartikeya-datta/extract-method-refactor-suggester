error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4636.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4636.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4636.java
text:
```scala
v@@alues = indexFieldData.load(context).getBytesValues(false);

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

package org.elasticsearch.search.facet.terms.strings;

import com.google.common.collect.ImmutableSet;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.CharsRef;
import org.apache.lucene.util.PriorityQueue;
import org.apache.lucene.util.UnicodeUtil;
import org.elasticsearch.cache.recycler.CacheRecycler;
import org.elasticsearch.common.collect.BoundedTreeSet;
import org.elasticsearch.common.util.IntArray;
import org.elasticsearch.common.util.IntArrays;
import org.elasticsearch.index.fielddata.BytesValues;
import org.elasticsearch.index.fielddata.IndexFieldData;
import org.elasticsearch.index.fielddata.ordinals.Ordinals;
import org.elasticsearch.search.facet.FacetExecutor;
import org.elasticsearch.search.facet.InternalFacet;
import org.elasticsearch.search.facet.terms.TermsFacet;
import org.elasticsearch.search.facet.terms.support.EntryPriorityQueue;
import org.elasticsearch.search.internal.SearchContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class TermsStringOrdinalsFacetExecutor extends FacetExecutor {

    private final IndexFieldData.WithOrdinals indexFieldData;

    final CacheRecycler cacheRecycler;
    private final TermsFacet.ComparatorType comparatorType;
    private final int size;
    private final int shardSize;
    private final int minCount;
    private final ImmutableSet<BytesRef> excluded;
    private final Matcher matcher;
    final int ordinalsCacheAbove;

    final List<ReaderAggregator> aggregators;
    long missing;
    long total;

    public TermsStringOrdinalsFacetExecutor(IndexFieldData.WithOrdinals indexFieldData, int size, int shardSize, TermsFacet.ComparatorType comparatorType, boolean allTerms, SearchContext context,
                                            ImmutableSet<BytesRef> excluded, Pattern pattern, int ordinalsCacheAbove) {
        this.indexFieldData = indexFieldData;
        this.size = size;
        this.shardSize = shardSize;
        this.comparatorType = comparatorType;
        this.ordinalsCacheAbove = ordinalsCacheAbove;

        if (excluded == null || excluded.isEmpty()) {
            this.excluded = null;
        } else {
            this.excluded = excluded;
        }
        this.matcher = pattern != null ? pattern.matcher("") : null;

        // minCount is offset by -1
        if (allTerms) {
            minCount = -1;
        } else {
            minCount = 0;
        }

        this.cacheRecycler = context.cacheRecycler();

        this.aggregators = new ArrayList<ReaderAggregator>(context.searcher().getIndexReader().leaves().size());
    }

    @Override
    public Collector collector() {
        return new Collector();
    }

    @Override
    public InternalFacet buildFacet(String facetName) {
        final CharsRef spare = new CharsRef();
        AggregatorPriorityQueue queue = new AggregatorPriorityQueue(aggregators.size());
        for (ReaderAggregator aggregator : aggregators) {
            if (aggregator.nextPosition()) {
                queue.add(aggregator);
            }
        }

        // YACK, we repeat the same logic, but once with an optimizer priority queue for smaller sizes
        if (shardSize < EntryPriorityQueue.LIMIT) {
            // optimize to use priority size
            EntryPriorityQueue ordered = new EntryPriorityQueue(shardSize, comparatorType.comparator());

            while (queue.size() > 0) {
                ReaderAggregator agg = queue.top();
                BytesRef value = agg.copyCurrent(); // we need to makeSafe it, since we end up pushing it... (can we get around this?)
                int count = 0;
                do {
                    count += agg.counts.get(agg.position);
                    if (agg.nextPosition()) {
                        agg = queue.updateTop();
                    } else {
                        // we are done with this reader
                        queue.pop();
                        agg = queue.top();
                    }
                } while (agg != null && value.equals(agg.current));

                if (count > minCount) {
                    if (excluded != null && excluded.contains(value)) {
                        continue;
                    }
                    if (matcher != null) {
                        UnicodeUtil.UTF8toUTF16(value, spare);
                        assert spare.toString().equals(value.utf8ToString());
                        if (!matcher.reset(spare).matches()) {
                            continue;
                        }
                    }
                    InternalStringTermsFacet.TermEntry entry = new InternalStringTermsFacet.TermEntry(value, count);
                    ordered.insertWithOverflow(entry);
                }
            }
            InternalStringTermsFacet.TermEntry[] list = new InternalStringTermsFacet.TermEntry[ordered.size()];
            for (int i = ordered.size() - 1; i >= 0; i--) {
                list[i] = (InternalStringTermsFacet.TermEntry) ordered.pop();
            }

            return new InternalStringTermsFacet(facetName, comparatorType, size, Arrays.asList(list), missing, total);
        }

        BoundedTreeSet<InternalStringTermsFacet.TermEntry> ordered = new BoundedTreeSet<InternalStringTermsFacet.TermEntry>(comparatorType.comparator(), shardSize);

        while (queue.size() > 0) {
            ReaderAggregator agg = queue.top();
            BytesRef value = agg.copyCurrent(); // we need to makeSafe it, since we end up pushing it... (can we work around that?)
            int count = 0;
            do {
                count += agg.counts.get(agg.position);
                if (agg.nextPosition()) {
                    agg = queue.updateTop();
                } else {
                    // we are done with this reader
                    queue.pop();
                    agg = queue.top();
                }
            } while (agg != null && value.equals(agg.current));

            if (count > minCount) {
                if (excluded != null && excluded.contains(value)) {
                    continue;
                }
                if (matcher != null) {
                    UnicodeUtil.UTF8toUTF16(value, spare);
                    assert spare.toString().equals(value.utf8ToString());
                    if (!matcher.reset(spare).matches()) {
                        continue;
                    }
                }
                InternalStringTermsFacet.TermEntry entry = new InternalStringTermsFacet.TermEntry(value, count);
                ordered.add(entry);
            }
        }

        return new InternalStringTermsFacet(facetName, comparatorType, size, ordered, missing, total);
    }

    class Collector extends FacetExecutor.Collector {

        private long missing;
        private long total;
        private BytesValues.WithOrdinals values;
        private ReaderAggregator current;
        private Ordinals.Docs ordinals;

        @Override
        public void setNextReader(AtomicReaderContext context) throws IOException {
            if (current != null) {
                missing += current.counts.get(0);
                total += current.total - current.counts.get(0);
                if (current.values.ordinals().getNumOrds() > 0) {
                    aggregators.add(current);
                }
            }
            values = indexFieldData.load(context).getBytesValues();
            current = new ReaderAggregator(values, ordinalsCacheAbove, cacheRecycler);
            ordinals = values.ordinals();
        }

        @Override
        public void collect(int doc) throws IOException {
            final int length = ordinals.setDocument(doc);
            int missing = 1;
            for (int i = 0; i < length; i++) {
                current.onOrdinal(doc, ordinals.nextOrd());
                missing = 0;
            }
            current.incrementMissing(missing);
        }

        @Override
        public void postCollection() {
            if (current != null) {
                missing += current.counts.get(0);
                total += current.total - current.counts.get(0);
                // if we have values for this one, add it
                if (current.values.ordinals().getNumOrds() > 0) {
                    aggregators.add(current);
                }
                current = null;
            }
            TermsStringOrdinalsFacetExecutor.this.missing = missing;
            TermsStringOrdinalsFacetExecutor.this.total = total;
        }
    }

    public static final class ReaderAggregator {

        private final long maxOrd;

        final BytesValues.WithOrdinals values;
        final IntArray counts;
        long position = 0;
        BytesRef current;
        int total;


        public ReaderAggregator(BytesValues.WithOrdinals values, int ordinalsCacheLimit, CacheRecycler cacheRecycler) {
            this.values = values;
            this.maxOrd = values.ordinals().getMaxOrd();
            this.counts = IntArrays.allocate(maxOrd);
        }

        final void onOrdinal(int docId, long ordinal) {
            counts.increment(ordinal, 1);
            total++;
        }

        final void incrementMissing(int numMissing) {
            counts.increment(0, numMissing);
            total += numMissing;
        }

        public boolean nextPosition() {
            if (++position >= maxOrd) {
                return false;
            }
            current = values.getValueByOrd(position);
            return true;
        }

        public BytesRef copyCurrent() {
            return values.copyShared();
        }
    }

    public static class AggregatorPriorityQueue extends PriorityQueue<ReaderAggregator> {

        public AggregatorPriorityQueue(int size) {
            super(size);
        }

        @Override
        protected boolean lessThan(ReaderAggregator a, ReaderAggregator b) {
            return a.current.compareTo(b.current) < 0;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4636.java