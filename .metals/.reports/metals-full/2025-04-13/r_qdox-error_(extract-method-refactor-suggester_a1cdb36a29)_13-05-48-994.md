error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3369.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3369.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3369.java
text:
```scala
r@@eturn this.ordinals;

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

package org.elasticsearch.index.fielddata.ordinals;

import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.LongsRef;
import org.apache.lucene.util.RamUsageEstimator;
import org.apache.lucene.util.packed.AppendingLongBuffer;
import org.apache.lucene.util.packed.MonotonicAppendingLongBuffer;
import org.apache.lucene.util.packed.PackedInts;
import org.elasticsearch.index.fielddata.ordinals.Ordinals.Docs.Iter;

/**
 * {@link Ordinals} implementation which is efficient at storing field data ordinals for multi-valued or sparse fields.
 */
public class MultiOrdinals implements Ordinals {

    private static final int OFFSETS_PAGE_SIZE = 1024;
    private static final int OFFSET_INIT_PAGE_COUNT = 16;

    /**
     * Return true if this impl is going to be smaller than {@link SinglePackedOrdinals} by at least 20%.
     */
    public static boolean significantlySmallerThanSinglePackedOrdinals(int maxDoc, int numDocsWithValue, long numOrds) {
        final int bitsPerOrd = PackedInts.bitsRequired(numOrds);
        // Compute the worst-case number of bits per value for offsets in the worst case, eg. if no docs have a value at the
        // beginning of the block and all docs have one at the end of the block
        final float avgValuesPerDoc = (float) numDocsWithValue / maxDoc;
        final int maxDelta = (int) Math.ceil(OFFSETS_PAGE_SIZE * (1 - avgValuesPerDoc) * avgValuesPerDoc);
        final int bitsPerOffset = PackedInts.bitsRequired(maxDelta) + 1; // +1 because of the sign
        final long expectedMultiSizeInBytes = (long) numDocsWithValue * bitsPerOrd + (long) maxDoc * bitsPerOffset;
        final long expectedSingleSizeInBytes = (long) maxDoc * bitsPerOrd;
        return expectedMultiSizeInBytes < 0.8f * expectedSingleSizeInBytes;
    }

    private final boolean multiValued;
    private final long numOrds;
    private final MonotonicAppendingLongBuffer endOffsets;
    private final AppendingLongBuffer ords;

    public MultiOrdinals(OrdinalsBuilder builder) {
        multiValued = builder.getNumMultiValuesDocs() > 0;
        numOrds = builder.getNumOrds();
        endOffsets = new MonotonicAppendingLongBuffer();
        ords = new AppendingLongBuffer(OFFSET_INIT_PAGE_COUNT, OFFSETS_PAGE_SIZE);
        long lastEndOffset = 0;
        for (int i = 0; i < builder.maxDoc(); ++i) {
            final LongsRef docOrds = builder.docOrds(i);
            final long endOffset = lastEndOffset + docOrds.length;
            endOffsets.add(endOffset);
            for (int j = 0; j < docOrds.length; ++j) {
                ords.add(docOrds.longs[docOrds.offset + j] - 1);
            }
            lastEndOffset = endOffset;
        }
        assert endOffsets.size() == builder.maxDoc();
        assert ords.size() == builder.getTotalNumOrds() : ords.size() + " != " + builder.getTotalNumOrds();
    }

    @Override
    public boolean hasSingleArrayBackingStorage() {
        return false;
    }

    @Override
    public Object getBackingStorage() {
        return null;
    }

    @Override
    public long getMemorySizeInBytes() {
        return endOffsets.ramBytesUsed() + ords.ramBytesUsed();
    }

    @Override
    public boolean isMultiValued() {
        return multiValued;
    }

    @Override
    public int getNumDocs() {
        return (int) endOffsets.size();
    }

    @Override
    public long getNumOrds() {
        return numOrds;
    }

    @Override
    public long getMaxOrd() {
        return numOrds + 1;
    }

    @Override
    public Ordinals.Docs ordinals() {
        return new MultiDocs(this);
    }

    static class MultiDocs implements Ordinals.Docs {

        private final MultiOrdinals ordinals;
        private final MonotonicAppendingLongBuffer endOffsets;
        private final AppendingLongBuffer ords;
        private final LongsRef longsScratch;
        private final MultiIter iter;

        MultiDocs(MultiOrdinals ordinals) {
            this.ordinals = ordinals;
            this.endOffsets = ordinals.endOffsets;
            this.ords = ordinals.ords;
            this.longsScratch = new LongsRef(16);
            this.iter = new MultiIter(ords);
        }

        @Override
        public Ordinals ordinals() {
            return null;
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
            final long startOffset = docId > 0 ? endOffsets.get(docId - 1) : 0;
            final long endOffset = endOffsets.get(docId);
            if (startOffset == endOffset) {
                return 0L; // ord for missing values
            } else {
                return 1L + ords.get(startOffset);
            }
        }

        @Override
        public LongsRef getOrds(int docId) {
            final long startOffset = docId > 0 ? endOffsets.get(docId - 1) : 0;
            final long endOffset = endOffsets.get(docId);
            final int numValues = (int) (endOffset - startOffset);
            if (longsScratch.length < numValues) {
                longsScratch.longs = new long[ArrayUtil.oversize(numValues, RamUsageEstimator.NUM_BYTES_LONG)];
            }
            for (int i = 0; i < numValues; ++i) {
                longsScratch.longs[i] = 1L + ords.get(startOffset + i);
            }
            longsScratch.offset = 0;
            longsScratch.length = numValues;
            return longsScratch;
        }

        @Override
        public Iter getIter(int docId) {
            final long startOffset = docId > 0 ? endOffsets.get(docId - 1) : 0;
            final long endOffset = endOffsets.get(docId);
            iter.offset = startOffset;
            iter.endOffset = endOffset;
            return iter;
        }

    }

    static class MultiIter implements Iter {

        final AppendingLongBuffer ordinals;
        long offset, endOffset;

        MultiIter(AppendingLongBuffer ordinals) {
            this.ordinals = ordinals;
        }

        @Override
        public long next() {
            if (offset >= endOffset) {
                return 0L;
            } else {
                return 1L + ordinals.get(offset++);
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3369.java