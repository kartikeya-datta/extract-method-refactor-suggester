error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3978.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3978.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3978.java
text:
```scala
public F@@acet reduce(List<Facet> facets) {

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

import org.elasticsearch.common.CacheRecycler;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentBuilderString;
import org.elasticsearch.search.facet.Facet;
import org.elasticsearch.search.facet.histogram.HistogramFacet;
import org.elasticsearch.search.facet.histogram.InternalHistogramFacet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class InternalBoundedCountHistogramFacet extends InternalHistogramFacet {

    private static final String STREAM_TYPE = "cBdHistogram";

    public static void registerStreams() {
        Streams.registerStream(STREAM, STREAM_TYPE);
    }

    static Stream STREAM = new Stream() {
        @Override
        public Facet readFacet(String type, StreamInput in) throws IOException {
            return readHistogramFacet(in);
        }
    };

    @Override
    public String streamType() {
        return STREAM_TYPE;
    }


    /**
     * A histogram entry representing a single entry within the result of a histogram facet.
     */
    public static class CountEntry implements Entry {
        private final long key;
        private final long count;

        public CountEntry(long key, long count) {
            this.key = key;
            this.count = count;
        }

        @Override
        public long key() {
            return key;
        }

        @Override
        public long getKey() {
            return key();
        }

        @Override
        public long count() {
            return count;
        }

        @Override
        public long getCount() {
            return count();
        }

        @Override
        public double total() {
            return Double.NaN;
        }

        @Override
        public double getTotal() {
            return total();
        }

        @Override
        public long totalCount() {
            return 0;
        }

        @Override
        public long getTotalCount() {
            return 0;
        }

        @Override
        public double mean() {
            return Double.NaN;
        }

        @Override
        public double getMean() {
            return mean();
        }

        @Override
        public double min() {
            return Double.NaN;
        }

        @Override
        public double getMin() {
            return Double.NaN;
        }

        @Override
        public double max() {
            return Double.NaN;
        }

        @Override
        public double getMax() {
            return Double.NaN;
        }
    }

    private String name;

    ComparatorType comparatorType;

    boolean cachedCounts;
    int[] counts;
    int size;
    long interval;
    long offset;

    CountEntry[] entries = null;

    private InternalBoundedCountHistogramFacet() {
    }

    public InternalBoundedCountHistogramFacet(String name, ComparatorType comparatorType, long interval, long offset, int size, int[] counts, boolean cachedCounts) {
        this.name = name;
        this.comparatorType = comparatorType;
        this.interval = interval;
        this.offset = offset;
        this.counts = counts;
        this.size = size;
        this.cachedCounts = cachedCounts;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public String getType() {
        return type();
    }

    @Override
    public List<CountEntry> entries() {
        return Arrays.asList(computeEntries());
    }

    @Override
    public List<CountEntry> getEntries() {
        return entries();
    }

    @Override
    public Iterator<Entry> iterator() {
        return (Iterator) entries().iterator();
    }

    private CountEntry[] computeEntries() {
        if (entries != null) {
            return entries;
        }
        entries = new CountEntry[size];
        for (int i = 0; i < size; i++) {
            entries[i] = new CountEntry((i * interval) + offset, counts[i]);
        }
        releaseCache();
        return entries;
    }

    void releaseCache() {
        if (cachedCounts) {
            cachedCounts = false;
            CacheRecycler.pushIntArray(counts);
            counts = null;
        }
    }

    @Override
    public Facet reduce(String name, List<Facet> facets) {
        if (facets.size() == 1) {
            InternalBoundedCountHistogramFacet firstHistoFacet = (InternalBoundedCountHistogramFacet) facets.get(0);
            if (comparatorType != ComparatorType.KEY) {
                Arrays.sort(firstHistoFacet.entries, comparatorType.comparator());
            }
            return facets.get(0);
        }
        InternalBoundedCountHistogramFacet firstHistoFacet = (InternalBoundedCountHistogramFacet) facets.get(0);
        for (int i = 1; i < facets.size(); i++) {
            InternalBoundedCountHistogramFacet histoFacet = (InternalBoundedCountHistogramFacet) facets.get(i);
            for (int j = 0; j < firstHistoFacet.size; j++) {
                firstHistoFacet.counts[j] += histoFacet.counts[j];
            }
            histoFacet.releaseCache();
        }
        if (comparatorType != ComparatorType.KEY) {
            Arrays.sort(firstHistoFacet.entries, comparatorType.comparator());
        }

        return firstHistoFacet;
    }

    static final class Fields {
        static final XContentBuilderString _TYPE = new XContentBuilderString("_type");
        static final XContentBuilderString ENTRIES = new XContentBuilderString("entries");
        static final XContentBuilderString KEY = new XContentBuilderString("key");
        static final XContentBuilderString COUNT = new XContentBuilderString("count");
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject(name);
        builder.field(Fields._TYPE, HistogramFacet.TYPE);
        builder.startArray(Fields.ENTRIES);
        for (int i = 0; i < size; i++) {
            builder.startObject();
            builder.field(Fields.KEY, (i * interval) + offset);
            builder.field(Fields.COUNT, counts[i]);
            builder.endObject();
        }
        builder.endArray();
        builder.endObject();
        releaseCache();
        return builder;
    }

    public static InternalBoundedCountHistogramFacet readHistogramFacet(StreamInput in) throws IOException {
        InternalBoundedCountHistogramFacet facet = new InternalBoundedCountHistogramFacet();
        facet.readFrom(in);
        return facet;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        name = in.readString();
        comparatorType = ComparatorType.fromId(in.readByte());
        offset = in.readLong();
        interval = in.readVLong();
        size = in.readVInt();
        counts = CacheRecycler.popIntArray(size);
        cachedCounts = true;
        for (int i = 0; i < size; i++) {
            counts[i] = in.readVInt();
        }
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(name);
        out.writeByte(comparatorType.id());
        out.writeLong(offset);
        out.writeVLong(interval);
        out.writeVInt(size);
        for (int i = 0; i < size; i++) {
            out.writeVInt(counts[i]);
        }
        releaseCache();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3978.java