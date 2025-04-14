error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6888.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6888.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6888.java
text:
```scala
b@@uilder.field("_type", HistogramFacetCollectorParser.NAME);

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
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

package org.elasticsearch.search.facets.histogram;

import org.elasticsearch.common.collect.ImmutableList;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.trove.TLongDoubleHashMap;
import org.elasticsearch.common.trove.TLongDoubleIterator;
import org.elasticsearch.common.trove.TLongLongHashMap;
import org.elasticsearch.common.trove.TLongLongIterator;
import org.elasticsearch.common.xcontent.builder.XContentBuilder;
import org.elasticsearch.search.facets.Facet;
import org.elasticsearch.search.facets.internal.InternalFacet;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/**
 * @author kimchy (shay.banon)
 */
public class InternalHistogramFacet implements HistogramFacet, InternalFacet {

    private static final TLongLongHashMap EMPTY_LONG_LONG_MAP = new TLongLongHashMap();
    private static final TLongDoubleHashMap EMPTY_LONG_DOUBLE_MAP = new TLongDoubleHashMap();

    private String name;

    private String keyFieldName;
    private String valueFieldName;

    private long interval;

    private ComparatorType comparatorType;

    private TLongLongHashMap counts;

    private TLongDoubleHashMap totals;

    private Collection<Entry> entries = null;

    private InternalHistogramFacet() {
    }

    public InternalHistogramFacet(String name, String keyFieldName, String valueFieldName, long interval, ComparatorType comparatorType, TLongLongHashMap counts, TLongDoubleHashMap totals) {
        this.name = name;
        this.keyFieldName = keyFieldName;
        this.valueFieldName = valueFieldName;
        this.interval = interval;
        this.comparatorType = comparatorType;
        this.counts = counts;
        this.totals = totals;
    }

    @Override public String name() {
        return this.name;
    }

    @Override public String getName() {
        return name();
    }

    @Override public String keyFieldName() {
        return this.keyFieldName;
    }

    @Override public String getKeyFieldName() {
        return keyFieldName();
    }

    @Override public String valueFieldName() {
        return this.valueFieldName;
    }

    @Override public String getValueFieldName() {
        return valueFieldName();
    }

    @Override public Type type() {
        return Type.HISTOGRAM;
    }

    @Override public Type getType() {
        return type();
    }

    @Override public List<Entry> entries() {
        computeEntries();
        if (!(entries instanceof List)) {
            entries = ImmutableList.copyOf(entries);
        }
        return (List<Entry>) entries;
    }

    @Override public List<Entry> getEntries() {
        return entries();
    }

    @Override public Iterator<Entry> iterator() {
        return computeEntries().iterator();
    }

    private Collection<Entry> computeEntries() {
        if (entries != null) {
            return entries;
        }
        TreeSet<Entry> set = new TreeSet<Entry>(comparatorType.comparator());
        for (TLongLongIterator it = counts.iterator(); it.hasNext();) {
            it.advance();
            set.add(new Entry(it.key(), it.value(), totals.get(it.key())));
        }
        entries = set;
        return entries;
    }

    @Override public Facet aggregate(Iterable<Facet> facets) {
        TLongLongHashMap counts = null;
        TLongDoubleHashMap totals = null;

        for (Facet facet : facets) {
            if (!facet.name().equals(name)) {
                continue;
            }
            InternalHistogramFacet histoFacet = (InternalHistogramFacet) facet;
            if (!histoFacet.counts.isEmpty()) {
                if (counts == null) {
                    counts = histoFacet.counts;
                } else {
                    for (TLongLongIterator it = histoFacet.counts.iterator(); it.hasNext();) {
                        it.advance();
                        counts.adjustOrPutValue(it.key(), it.value(), it.value());
                    }
                }
            }

            if (!histoFacet.totals.isEmpty()) {
                if (totals == null) {
                    totals = histoFacet.totals;
                } else {
                    for (TLongDoubleIterator it = histoFacet.totals.iterator(); it.hasNext();) {
                        it.advance();
                        totals.adjustOrPutValue(it.key(), it.value(), it.value());
                    }
                }
            }
        }
        if (counts == null) {
            counts = EMPTY_LONG_LONG_MAP;
        }
        if (totals == null) {
            totals = EMPTY_LONG_DOUBLE_MAP;
        }

        return new InternalHistogramFacet(name, keyFieldName, valueFieldName, interval, comparatorType, counts, totals);
    }

    @Override public void toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject(name);
        builder.field("_type", "histogram");
        builder.field("_key_field", keyFieldName);
        builder.field("_value_field", valueFieldName);
        builder.field("_comparator", comparatorType.description());
        builder.field("_interval", interval);
        builder.startArray("entries");
        for (Entry entry : computeEntries()) {
            builder.startObject();
            builder.field("key", entry.key());
            builder.field("count", entry.count());
            builder.field("total", entry.total());
            builder.field("mean", entry.mean());
            builder.endObject();
        }
        builder.endArray();
        builder.endObject();
    }

    public static InternalHistogramFacet readHistogramFacet(StreamInput in) throws IOException {
        InternalHistogramFacet facet = new InternalHistogramFacet();
        facet.readFrom(in);
        return facet;
    }

    @Override public void readFrom(StreamInput in) throws IOException {
        name = in.readUTF();
        keyFieldName = in.readUTF();
        valueFieldName = in.readUTF();
        interval = in.readVLong();
        comparatorType = ComparatorType.fromId(in.readByte());

        int size = in.readVInt();
        if (size == 0) {
            counts = EMPTY_LONG_LONG_MAP;
            totals = EMPTY_LONG_DOUBLE_MAP;
        } else {
            counts = new TLongLongHashMap(size);
            totals = new TLongDoubleHashMap(size);
            for (int i = 0; i < size; i++) {
                long key = in.readLong();
                counts.put(key, in.readVLong());
                totals.put(key, in.readDouble());
            }
        }
    }

    @Override public void writeTo(StreamOutput out) throws IOException {
        out.writeUTF(name);
        out.writeUTF(keyFieldName);
        out.writeUTF(valueFieldName);
        out.writeVLong(interval);
        out.writeByte(comparatorType.id());
        // optimize the write, since we know we have the same buckets as keys
        out.writeVInt(counts.size());
        for (TLongLongIterator it = counts.iterator(); it.hasNext();) {
            it.advance();
            out.writeLong(it.key());
            out.writeVLong(it.value());
            out.writeDouble(totals.get(it.key()));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6888.java