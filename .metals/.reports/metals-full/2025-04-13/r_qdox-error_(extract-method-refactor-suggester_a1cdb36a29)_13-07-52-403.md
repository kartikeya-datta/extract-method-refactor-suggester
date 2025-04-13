error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6994.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6994.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6994.java
text:
```scala
final I@@ntArray hashes = BigArrays.NON_RECYCLING_INSTANCE.newIntArray(1L + valueCount);

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

package org.elasticsearch.index.fielddata.plain;

import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.SortedSetDocValues;
import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LongsRef;
import org.elasticsearch.ElasticsearchIllegalStateException;
import org.elasticsearch.common.util.BigArrays;
import org.elasticsearch.common.util.IntArray;
import org.elasticsearch.index.fielddata.AtomicFieldData;
import org.elasticsearch.index.fielddata.BytesValues;
import org.elasticsearch.index.fielddata.ordinals.Ordinals;

import java.io.IOException;

/**
 * {@link AtomicFieldData} impl based on Lucene's {@link SortedSetDocValues}.
 * <p><b>Implementation note</b>: Lucene's ordinal for unset values is -1 whereas Elasticsearch's is 0, this is why there are all
 * these +1 to translate from Lucene's ordinals to ES's.
 */
abstract class SortedSetDVAtomicFieldData {

    private final AtomicReader reader;
    private final String field;
    private volatile IntArray hashes;

    SortedSetDVAtomicFieldData(AtomicReader reader, String field) {
        this.reader = reader;
        this.field = field;
    }

    public boolean isMultiValued() {
        // we could compute it when loading the values for the first time and then cache it but it would defeat the point of
        // doc values which is to make loading faster
        return true;
    }

    public int getNumDocs() {
        return reader.maxDoc();
    }

    public long getNumberUniqueValues() {
        final SortedSetDocValues values = getValuesNoException(reader, field);
        return values.getValueCount();
    }

    public long getMemorySizeInBytes() {
        // There is no API to access memory usage per-field and RamUsageEstimator can't help since there are often references
        // from a per-field instance to all other instances handled by the same format
        return -1L;
    }

    public void close() {
        // no-op
    }

    public org.elasticsearch.index.fielddata.BytesValues.WithOrdinals getBytesValues(boolean needsHashes) {
        final SortedSetDocValues values = getValuesNoException(reader, field);
        return new SortedSetValues(reader, field, values);
    }

    public org.elasticsearch.index.fielddata.BytesValues.WithOrdinals getHashedBytesValues() {
        final SortedSetDocValues values = getValuesNoException(reader, field);
        if (hashes == null) {
            synchronized (this) {
                if (hashes == null) {
                    final long valueCount = values.getValueCount();
                    final IntArray hashes = BigArrays.newIntArray(1L + valueCount);
                    BytesRef scratch = new BytesRef(16);
                    hashes.set(0, scratch.hashCode());
                    for (long i = 0; i < valueCount; ++i) {
                        values.lookupOrd(i, scratch);
                        hashes.set(1L + i, scratch.hashCode());
                    }
                    this.hashes = hashes;
                }
            }
        }
        return new SortedSetHashedValues(reader, field, values, hashes);
    }

    private static SortedSetDocValues getValuesNoException(AtomicReader reader, String field) {
        try {
            SortedSetDocValues values = reader.getSortedSetDocValues(field);
            if (values == null) {
                // This field has not been populated
                assert reader.getFieldInfos().fieldInfo(field) == null;
                values = SortedSetDocValues.EMPTY;
            }
            return values;
        } catch (IOException e) {
            throw new ElasticsearchIllegalStateException("Couldn't load doc values", e);
        }
    }

    static class SortedSetValues extends BytesValues.WithOrdinals {

        protected final SortedSetDocValues values;

        SortedSetValues(AtomicReader reader, String field, SortedSetDocValues values) {
            super(new SortedSetDocs(new SortedSetOrdinals(reader, field, values.getValueCount()), values));
            this.values = values;
        }

        @Override
        public BytesRef getValueByOrd(long ord) {
            assert ord != Ordinals.MISSING_ORDINAL;
            values.lookupOrd(ord - 1, scratch);
            return scratch;
        }

        @Override
        public BytesRef nextValue() {
            values.lookupOrd(ordinals.nextOrd()-1, scratch);
            return scratch;
        }
    }

    static final class SortedSetHashedValues extends SortedSetValues {

        private final IntArray hashes;

        SortedSetHashedValues(AtomicReader reader, String field, SortedSetDocValues values, IntArray hashes) {
            super(reader, field, values);
            this.hashes = hashes;
        }

        @Override
        public int currentValueHash() {
            assert ordinals.currentOrd() >= 0;
            return hashes.get(ordinals.currentOrd());
        }
    }

    static final class SortedSetOrdinals implements Ordinals {

        // We don't store SortedSetDocValues as a member because Ordinals must be thread-safe
        private final AtomicReader reader;
        private final String field;
        private final long numOrds;

        public SortedSetOrdinals(AtomicReader reader, String field, long numOrds) {
            super();
            this.reader = reader;
            this.field = field;
            this.numOrds = numOrds;
        }

        @Override
        public long getMemorySizeInBytes() {
            // Ordinals can't be distinguished from the atomic field data instance
            return -1;
        }

        @Override
        public boolean isMultiValued() {
            return true;
        }

        @Override
        public int getNumDocs() {
            return reader.maxDoc();
        }

        @Override
        public long getNumOrds() {
            return numOrds;
        }

        @Override
        public long getMaxOrd() {
            return 1 + numOrds;
        }

        @Override
        public Docs ordinals() {
            final SortedSetDocValues values = getValuesNoException(reader, field);
            assert values.getValueCount() == numOrds;
            return new SortedSetDocs(this, values);
        }

    }

    static class SortedSetDocs implements Ordinals.Docs {

        private final SortedSetOrdinals ordinals;
        private final SortedSetDocValues values;
        private final LongsRef longScratch;
        private int ordIndex = Integer.MAX_VALUE;
        private long currentOrdinal = -1;

        SortedSetDocs(SortedSetOrdinals ordinals, SortedSetDocValues values) {
            this.ordinals = ordinals;
            this.values = values;
            longScratch = new LongsRef(8);
        }

        @Override
        public Ordinals ordinals() {
            return ordinals;
        }

        @Override
        public int getNumDocs() {
            return ordinals.getNumDocs();
        }

        @Override
        public long getNumOrds() {
            return ordinals.getNumOrds();
        }

        @Override
        public long getMaxOrd() {
            return ordinals.getMaxOrd();
        }

        @Override
        public boolean isMultiValued() {
            return ordinals.isMultiValued();
        }

        @Override
        public long getOrd(int docId) {
            values.setDocument(docId);
            return currentOrdinal = 1 + values.nextOrd();
        }

        @Override
        public LongsRef getOrds(int docId) {
            values.setDocument(docId);
            longScratch.offset = 0;
            longScratch.length = 0;
            for (long ord = values.nextOrd(); ord != SortedSetDocValues.NO_MORE_ORDS; ord = values.nextOrd()) {
                longScratch.longs = ArrayUtil.grow(longScratch.longs, longScratch.length + 1);
                longScratch.longs[longScratch.length++] = 1 + ord;
            }
            return longScratch;
        }

        @Override
        public long nextOrd() {
            assert ordIndex < longScratch.length;
            return currentOrdinal = longScratch.longs[ordIndex++];
        }

        @Override
        public int setDocument(int docId) {
            // For now, we consume all ords and pass them to the iter instead of doing it in a streaming way because Lucene's
            // SORTED_SET doc values are cached per thread, you can't have a fully independent instance
            final LongsRef ords = getOrds(docId);
            ordIndex = 0;
            return ords.length;
        }

        @Override
        public long currentOrd() {
            return currentOrdinal;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6994.java