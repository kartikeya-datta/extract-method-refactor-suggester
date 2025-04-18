error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9204.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9204.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9204.java
text:
```scala
final f@@loat acceptableOverheadRatio = settings.getAsFloat("acceptable_overhead_ratio", PackedInts.DEFAULT);

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

import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.FilteredTermsEnum;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.*;
import org.apache.lucene.util.packed.GrowableWriter;
import org.apache.lucene.util.packed.PackedInts;
import org.apache.lucene.util.packed.XPagedGrowableWriter;
import org.elasticsearch.common.settings.Settings;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Simple class to build document ID <-> ordinal mapping. Note: Ordinals are
 * <tt>1</tt> based monotonically increasing positive integers. <tt>0</tt>
 * donates the missing value in this context.
 */
public final class OrdinalsBuilder implements Closeable {

    /** Default acceptable overhead ratio. {@link OrdinalsBuilder} memory usage is mostly transient so it is likely a better trade-off to
     *  trade memory for speed in order to resize less often. */
    public static final float DEFAULT_ACCEPTABLE_OVERHEAD_RATIO = PackedInts.FAST;

    /** The following structure is used to store ordinals. The idea is to store ords on levels of increasing sizes. Level 0 stores
     * 1 value and 1 pointer to level 1. Level 1 stores 2 values and 1 pointer to level 2, ..., Level n stores 2**n values and
     * 1 pointer to level n+1. If at some point an ordinal or a pointer has 0 as a value, this means that there are no remaining
     * values. On the first level, ordinals.get(docId) is the first ordinal for docId or 0 if the document has no ordinals. On
     * subsequent levels, the first 2^level slots are reserved and all have 0 as a value.
     * <pre>
     * Example for an index of 3 docs (O=ordinal, P = pointer)
     * Level 0:
     *   ordinals           [1] [4] [2]
     *   nextLevelSlices    2  0  1
     * Level 1:
     *   ordinals           [0  0] [2  0] [3  4]
     *   nextLevelSlices    0  0  1
     * Level 2:
     *   ordinals           [0  0  0  0] [5  0  0  0]
     *   nextLevelSlices    0  0
     * </pre>
     * On level 0, all documents have an ordinal: 0 has 1, 1 has 4 and 2 has 2 as a first ordinal, this means that we need to read
     * nextLevelEntries to get the index of their ordinals on the next level. The entry for document 1 is 0, meaning that we have
     * already read all its ordinals. On the contrary 0 and 2 have more ordinals which are stored at indices 2 and 1. Let's continue
     * with document 2: it has 2 more ordinals on level 1: 3 and 4 and its next level index is 1 meaning that there are remaining
     * ordinals on the next level. On level 2 at index 1, we can read [5  0  0  0] meaning that 5 is an ordinal as well, but the
     * fact that it is followed by zeros means that there are no more ordinals. In the end, document 2 has 2, 3, 4 and 5 as ordinals.
     *
     * In addition to these structures, there is another array which stores the current position (level + slice + offset in the slice)
     * in order to be able to append data in constant time.
     */
    private static class OrdinalsStore {

        private static final int PAGE_SIZE = 1 << 12;

        /** Number of slots at <code>level</code> */
        private static int numSlots(int level) {
            return 1 << level;
        }

        private static int slotsMask(int level) {
            return numSlots(level) - 1;
        }

        /** Encode the position for the given level and offset. The idea is to encode the level using unary coding in the lower bits and
         *  then the offset in the higher bits. */
        private static long position(int level, long offset) {
            assert level >= 1;
            return (1 << (level - 1)) | (offset << level);
        }

        /** Decode the level from an encoded position. */
        private static int level(long position) {
            return 1 + Long.numberOfTrailingZeros(position);
        }

        /** Decode the offset from the position. */
        private static long offset(long position, int level) {
            return position >>> level;
        }

        /** Get the ID of the slice given an offset. */
        private static long sliceID(int level, long offset) {
            return offset >>> level;
        }

        /** Compute the first offset of the given slice. */
        private static long startOffset(int level, long slice) {
            return slice << level;
        }

        /** Compute the number of ordinals stored for a value given its current position. */
        private static int numOrdinals(int level, long offset) {
            return (1 << level) + (int) (offset & slotsMask(level));
        }

        // Current position
        private XPagedGrowableWriter positions;
        // First level (0) of ordinals and pointers to the next level
        private final GrowableWriter firstOrdinals;
        private XPagedGrowableWriter firstNextLevelSlices;
        // Ordinals and pointers for other levels, starting at 1
        private final XPagedGrowableWriter[] ordinals;
        private final XPagedGrowableWriter[] nextLevelSlices;
        private final int[] sizes;

        private final int startBitsPerValue;
        private final float acceptableOverheadRatio;

        OrdinalsStore(int maxDoc, int startBitsPerValue, float acceptableOverheadRatio) {
            this.startBitsPerValue = startBitsPerValue;
            this.acceptableOverheadRatio = acceptableOverheadRatio;
            positions = new XPagedGrowableWriter(maxDoc, PAGE_SIZE, startBitsPerValue, acceptableOverheadRatio);
            firstOrdinals = new GrowableWriter(startBitsPerValue, maxDoc, acceptableOverheadRatio);
            // over allocate in order to never worry about the array sizes, 24 entries would allow to store several millions of ordinals per doc...
            ordinals = new XPagedGrowableWriter[24];
            nextLevelSlices = new XPagedGrowableWriter[24];
            sizes = new int[24];
            Arrays.fill(sizes, 1); // reserve the 1st slice on every level
        }

        /** Allocate a new slice and return its ID. */
        private long newSlice(int level) {
            final long newSlice = sizes[level]++;
            // Lazily allocate ordinals
            if (ordinals[level] == null) {
                ordinals[level] = new XPagedGrowableWriter(8L * numSlots(level), PAGE_SIZE, startBitsPerValue, acceptableOverheadRatio);
            } else {
                ordinals[level] = ordinals[level].grow(sizes[level] * numSlots(level));
                if (nextLevelSlices[level] != null) {
                    nextLevelSlices[level] = nextLevelSlices[level].grow(sizes[level]);
                }
            }
            return newSlice;
        }

        public int addOrdinal(int docID, long ordinal) {
            final long position = positions.get(docID);

            if (position == 0L) { // on the first level
                // 0 or 1 ordinal
                if (firstOrdinals.get(docID) == 0L) {
                    firstOrdinals.set(docID, ordinal);
                    return 1;
                } else {
                    final long newSlice = newSlice(1);
                    if (firstNextLevelSlices == null) {
                        firstNextLevelSlices = new XPagedGrowableWriter(firstOrdinals.size(), PAGE_SIZE, 3, acceptableOverheadRatio);
                    }
                    firstNextLevelSlices.set(docID, newSlice);
                    final long offset = startOffset(1, newSlice);
                    ordinals[1].set(offset, ordinal);
                    positions.set(docID, position(1, offset)); // current position is on the 1st level and not allocated yet
                    return 2;
                }
            } else {
                int level = level(position);
                long offset = offset(position, level);
                assert offset != 0L;
                if (((offset + 1) & slotsMask(level)) == 0L) {
                    // reached the end of the slice, allocate a new one on the next level
                    final long newSlice = newSlice(level + 1);
                    if (nextLevelSlices[level] == null) {
                        nextLevelSlices[level] = new XPagedGrowableWriter(sizes[level], PAGE_SIZE, 1, acceptableOverheadRatio);
                    }
                    nextLevelSlices[level].set(sliceID(level, offset), newSlice);
                    ++level;
                    offset = startOffset(level, newSlice);
                    assert (offset & slotsMask(level)) == 0L;
                } else {
                    // just go to the next slot
                    ++offset;
                }
                ordinals[level].set(offset, ordinal);
                final long newPosition = position(level, offset);
                positions.set(docID, newPosition);
                return numOrdinals(level, offset);
            }
        }

        public void appendOrdinals(int docID, LongsRef ords) {
            // First level
            final long firstOrd = firstOrdinals.get(docID);
            if (firstOrd == 0L) {
                return;
            }
            ords.longs = ArrayUtil.grow(ords.longs, ords.offset + ords.length + 1);
            ords.longs[ords.offset + ords.length++] = firstOrd;
            if (firstNextLevelSlices == null) {
                return;
            }
            long sliceID = firstNextLevelSlices.get(docID);
            if (sliceID == 0L) {
                return;
            }
            // Other levels
            for (int level = 1; ; ++level) {
                final int numSlots = numSlots(level);
                ords.longs = ArrayUtil.grow(ords.longs, ords.offset + ords.length + numSlots);
                final long offset = startOffset(level, sliceID);
                for (int j = 0; j < numSlots; ++j) {
                    final long ord = ordinals[level].get(offset + j);
                    if (ord == 0L) {
                        return;
                    }
                    ords.longs[ords.offset + ords.length++] = ord;
                }
                if (nextLevelSlices[level] == null) {
                    return;
                }
                sliceID = nextLevelSlices[level].get(sliceID);
                if (sliceID == 0L) {
                    return;
                }
            }
        }

    }

    private final int maxDoc;
    private long currentOrd = 0;
    private int numDocsWithValue = 0;
    private int numMultiValuedDocs = 0;
    private int totalNumOrds = 0;

    private OrdinalsStore ordinals;
    private final LongsRef spare;

    public OrdinalsBuilder(long numTerms, int maxDoc, float acceptableOverheadRatio) throws IOException {
        this.maxDoc = maxDoc;
        int startBitsPerValue = 8;
        if (numTerms >= 0) {
            startBitsPerValue = PackedInts.bitsRequired(numTerms);
        }
        ordinals = new OrdinalsStore(maxDoc, startBitsPerValue, acceptableOverheadRatio);
        spare = new LongsRef();
    }
    
    public OrdinalsBuilder(int maxDoc, float acceptableOverheadRatio) throws IOException {
        this(-1, maxDoc, acceptableOverheadRatio);
    }

    public OrdinalsBuilder(int maxDoc) throws IOException {
        this(maxDoc, DEFAULT_ACCEPTABLE_OVERHEAD_RATIO);
    }

    /**
     * Returns a shared {@link LongsRef} instance for the given doc ID holding all ordinals associated with it.
     */
    public LongsRef docOrds(int docID) {
        spare.offset = spare.length = 0;
        ordinals.appendOrdinals(docID, spare);
        return spare;
    }

    /** Return a {@link PackedInts.Reader} instance mapping every doc ID to its first ordinal if it exists and 0 otherwise. */
    public PackedInts.Reader getFirstOrdinals() {
        return ordinals.firstOrdinals;
    }

    /**
     * Advances the {@link OrdinalsBuilder} to the next ordinal and
     * return the current ordinal.
     */
    public long nextOrdinal() {
        return ++currentOrd;
    }
    
    /**
     * Retruns the current ordinal or <tt>0</tt> if this build has not been advanced via
     * {@link #nextOrdinal()}.
     */
    public long currentOrdinal() {
        return currentOrd;
    }

    /**
     * Associates the given document id with the current ordinal. 
     */
    public OrdinalsBuilder addDoc(int doc) {
        totalNumOrds++;
        final int numValues = ordinals.addOrdinal(doc, currentOrd);
        if (numValues == 1) {
            ++numDocsWithValue;
        } else if (numValues == 2) {
            ++numMultiValuedDocs;
        }
        return this;
    }

    /**
     * Returns <code>true</code> iff this builder contains a document ID that is associated with more than one ordinal. Otherwise <code>false</code>;
     */
    public boolean isMultiValued() {
        return numMultiValuedDocs > 0;
    }

    /**
     * Returns the number distinct of document IDs with one or more values.
     */
    public int getNumDocsWithValue() {
        return numDocsWithValue;
    }

    /**
     * Returns the number distinct of document IDs associated with exactly one value.
     */
    public int getNumSingleValuedDocs() {
        return numDocsWithValue - numMultiValuedDocs;
    }

    /**
     * Returns the number distinct of document IDs associated with two or more values.
     */
    public int getNumMultiValuesDocs() {
        return numMultiValuedDocs;
    }

    /**
     * Returns the number of document ID to ordinal pairs in this builder.
     */
    public int getTotalNumOrds() {
        return totalNumOrds;
    }

    /**
     * Returns the number of distinct ordinals in this builder.  
     */
    public long getNumOrds() {
        return currentOrd;
    }

    /**
     * Builds a {@link FixedBitSet} where each documents bit is that that has one or more ordinals associated with it.
     * if every document has an ordinal associated with it this method returns <code>null</code>
     */
    public FixedBitSet buildDocsWithValuesSet() {
        if (numDocsWithValue == maxDoc) {
            return null;
        }
        final FixedBitSet bitSet = new FixedBitSet(maxDoc);
        for (int docID = 0; docID < maxDoc; ++docID) {
            if (ordinals.firstOrdinals.get(docID) != 0) {
                bitSet.set(docID);
            }
        }
        return bitSet;
    }

    /**
     * Builds an {@link Ordinals} instance from the builders current state. 
     */
    public Ordinals build(Settings settings) {
        final float acceptableOverheadRatio = settings.getAsFloat("acceptable_overhead_ratio", PackedInts.COMPACT);
        if (numMultiValuedDocs > 0 || MultiOrdinals.significantlySmallerThanSinglePackedOrdinals(maxDoc, numDocsWithValue, getNumOrds())) {
            // MultiOrdinals can be smaller than SinglePackedOrdinals for sparse fields
            return new MultiOrdinals(this);
        } else {
            return new SinglePackedOrdinals(this, acceptableOverheadRatio);
        }
    }

    /**
     * Returns the maximum document ID this builder can associate with an ordinal
     */
    public int maxDoc() {
        return maxDoc;
    }
    
    /**
     * A {@link TermsEnum} that iterates only full precision prefix coded 64 bit values.
     * @see #buildFromTerms(TermsEnum, Bits)
     */
    public static TermsEnum wrapNumeric64Bit(TermsEnum termsEnum) {
        return new FilteredTermsEnum(termsEnum, false) {
            @Override
            protected AcceptStatus accept(BytesRef term) throws IOException {
                // we stop accepting terms once we moved across the prefix codec terms - redundant values!
                return NumericUtils.getPrefixCodedLongShift(term) == 0 ? AcceptStatus.YES : AcceptStatus.END;
            }
        };
    }

    /**
     * A {@link TermsEnum} that iterates only full precision prefix coded 32 bit values.
     * @see #buildFromTerms(TermsEnum, Bits)
     */
    public static TermsEnum wrapNumeric32Bit(TermsEnum termsEnum) {
        return new FilteredTermsEnum(termsEnum, false) {
            
            @Override
            protected AcceptStatus accept(BytesRef term) throws IOException {
                // we stop accepting terms once we moved across the prefix codec terms - redundant values!
                return NumericUtils.getPrefixCodedIntShift(term) == 0 ? AcceptStatus.YES : AcceptStatus.END;
            }
        };
    }

    /**
     * This method iterates all terms in the given {@link TermsEnum} and
     * associates each terms ordinal with the terms documents. The caller must
     * exhaust the returned {@link BytesRefIterator} which returns all values
     * where the first returned value is associted with the ordinal <tt>1</tt>
     * etc.
     * <p>
     * If the {@link TermsEnum} contains prefix coded numerical values the terms
     * enum should be wrapped with either {@link #wrapNumeric32Bit(TermsEnum)}
     * or {@link #wrapNumeric64Bit(TermsEnum)} depending on its precision. If
     * the {@link TermsEnum} is not wrapped the returned
     * {@link BytesRefIterator} will contain partial precision terms rather than
     * only full-precision terms.
     * </p>
     */
    public BytesRefIterator buildFromTerms(final TermsEnum termsEnum) throws IOException {
        return new BytesRefIterator() {
            private DocsEnum docsEnum = null;

            @Override
            public BytesRef next() throws IOException {
                BytesRef ref;
                if ((ref = termsEnum.next()) != null) {
                    docsEnum = termsEnum.docs(null, docsEnum, DocsEnum.FLAG_NONE);
                    nextOrdinal();
                    int docId;
                    while((docId = docsEnum.nextDoc()) != DocsEnum.NO_MORE_DOCS) {
                        addDoc(docId);
                    }
                }
                return ref;
            }

            @Override
            public Comparator<BytesRef> getComparator() {
                return termsEnum.getComparator();
            }
        };
    }
    
    /**
     * Closes this builder and release all resources.
     */
    @Override
    public void close() throws IOException {
        ordinals = null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9204.java