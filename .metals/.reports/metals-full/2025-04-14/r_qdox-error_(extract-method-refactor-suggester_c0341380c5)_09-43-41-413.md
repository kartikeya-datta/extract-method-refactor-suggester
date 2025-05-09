error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7234.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7234.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7234.java
text:
```scala
f@@ieldMap = new ObjectLongOpenHashMap<>();

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

package org.elasticsearch.action.termvector;

import com.carrotsearch.hppc.ObjectLongOpenHashMap;
import com.carrotsearch.hppc.cursors.ObjectLongCursor;
import org.apache.lucene.index.*;
import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.RamUsageEstimator;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.io.stream.BytesStreamInput;

import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;

import static org.apache.lucene.util.ArrayUtil.grow;

/**
 * This class represents the result of a {@link TermVectorRequest}. It works
 * exactly like the {@link Fields} class except for one thing: It can return
 * offsets and payloads even if positions are not present. You must call
 * nextPosition() anyway to move the counter although this method only returns
 * <tt>-1,</tt>, if no positions were returned by the {@link TermVectorRequest}.
 * <p/>
 * The data is stored in two byte arrays ({@code headerRef} and
 * {@code termVectors}, both {@link ByteRef}) that have the following format:
 * <p/>
 * {@code headerRef}: Stores offsets per field in the {@code termVectors} array
 * and some header information as {@link BytesRef}. Format is
 * <ul>
 * <li>String : "TV"</li>
 * <li>vint: version (=-1)</li>
 * <li>boolean: hasTermStatistics (are the term statistics stored?)</li>
 * <li>boolean: hasFieldStatitsics (are the field statistics stored?)</li>
 * <li>vint: number of fields</li>
 * <ul>
 * <li>String: field name 1</li>
 * <li>vint: offset in {@code termVectors} for field 1</li>
 * <li>...</li>
 * <li>String: field name last field</li>
 * <li>vint: offset in {@code termVectors} for last field</li>
 * </ul>
 * </ul>
 * <p/>
 * termVectors: Stores the actual term vectors as a {@link BytesRef}.
 * <p/>
 * Term vectors for each fields are stored in blocks, one for each field. The
 * offsets in {@code headerRef} are used to find where the block for a field
 * starts. Each block begins with a
 * <ul>
 * <li>vint: number of terms</li>
 * <li>boolean: positions (has it positions stored?)</li>
 * <li>boolean: offsets (has it offsets stored?)</li>
 * <li>boolean: payloads (has it payloads stored?)</li>
 * </ul>
 * If the field statistics were requested ({@code hasFieldStatistics} is true,
 * see {@code headerRef}), the following numbers are stored:
 * <ul>
 * <li>vlong: sum of total term freqencies of the field (sumTotalTermFreq)</li>
 * <li>vlong: sum of document frequencies for each term (sumDocFreq)</li>
 * <li>vint: number of documents in the shard that has an entry for this field
 * (docCount)</li>
 * </ul>
 * <p/>
 * After that, for each term it stores
 * <ul>
 * <ul>
 * <li>vint: term lengths</li>
 * <li>BytesRef: term name</li>
 * </ul>
 * <p/>
 * If term statistics are requested ({@code hasTermStatistics} is true, see
 * {@code headerRef}):
 * <ul>
 * <li>vint: document frequency, how often does this term appear in documents?</li>
 * <li>vlong: total term frequency. Sum of terms in this field.</li>
 * </ul>
 * After that
 * <ul>
 * <li>vint: frequency (always returned)</li>
 * <ul>
 * <li>vint: position_1 (if positions == true)</li>
 * <li>vint: startOffset_1 (if offset == true)</li>
 * <li>vint: endOffset_1 (if offset == true)</li>
 * <li>BytesRef: payload_1 (if payloads == true)</li>
 * <li>...</li>
 * <li>vint: endOffset_freqency (if offset == true)</li>
 * <li>BytesRef: payload_freqency (if payloads == true)</li>
 * <ul>
 * </ul> </ul>
 */

public final class TermVectorFields extends Fields {

    private final ObjectLongOpenHashMap<String> fieldMap;
    private final BytesReference termVectors;
    final boolean hasTermStatistic;
    final boolean hasFieldStatistic;

    /**
     * @param headerRef   Stores offsets per field in the {@code termVectors} and some
     *                    header information as {@link BytesRef}.
     * @param termVectors Stores the actual term vectors as a {@link BytesRef}.
     */
    public TermVectorFields(BytesReference headerRef, BytesReference termVectors) throws IOException {
        BytesStreamInput header = new BytesStreamInput(headerRef);
        fieldMap = new ObjectLongOpenHashMap<String>();

        // here we read the header to fill the field offset map
        String headerString = header.readString();
        assert headerString.equals("TV");
        int version = header.readInt();
        assert version == -1;
        hasTermStatistic = header.readBoolean();
        hasFieldStatistic = header.readBoolean();
        final int numFields = header.readVInt();
        for (int i = 0; i < numFields; i++) {
            fieldMap.put((header.readString()), header.readVLong());
        }
        header.close();
        // reference to the term vector data
        this.termVectors = termVectors;
    }

    @Override
    public Iterator<String> iterator() {
        final Iterator<ObjectLongCursor<String>> iterator = fieldMap.iterator();
        return new Iterator<String>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public String next() {
                return iterator.next().key;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public Terms terms(String field) throws IOException {
        // first, find where in the termVectors bytes the actual term vector for
        // this field is stored
        if (!fieldMap.containsKey(field)) {
            return null; // we don't have it.
        }
        long offset = fieldMap.lget();
        final BytesStreamInput perFieldTermVectorInput = new BytesStreamInput(this.termVectors);
        perFieldTermVectorInput.reset();
        perFieldTermVectorInput.skip(offset);

        // read how many terms....
        final long numTerms = perFieldTermVectorInput.readVLong();
        // ...if positions etc. were stored....
        final boolean hasPositions = perFieldTermVectorInput.readBoolean();
        final boolean hasOffsets = perFieldTermVectorInput.readBoolean();
        final boolean hasPayloads = perFieldTermVectorInput.readBoolean();
        // read the field statistics
        final long sumTotalTermFreq = hasFieldStatistic ? readPotentiallyNegativeVLong(perFieldTermVectorInput) : -1;
        final long sumDocFreq = hasFieldStatistic ? readPotentiallyNegativeVLong(perFieldTermVectorInput) : -1;
        final int docCount = hasFieldStatistic ? readPotentiallyNegativeVInt(perFieldTermVectorInput) : -1;

        return new Terms() {

            @Override
            public TermsEnum iterator(TermsEnum reuse) throws IOException {
                // convert bytes ref for the terms to actual data
                return new TermsEnum() {
                    int currentTerm = 0;
                    int freq = 0;
                    int docFreq = -1;
                    long totalTermFrequency = -1;
                    int[] positions = new int[1];
                    int[] startOffsets = new int[1];
                    int[] endOffsets = new int[1];
                    BytesRef[] payloads = new BytesRef[1];
                    final BytesRef spare = new BytesRef();

                    @Override
                    public BytesRef next() throws IOException {
                        if (currentTerm++ < numTerms) {
                            // term string. first the size...
                            int termVectorSize = perFieldTermVectorInput.readVInt();
                            spare.grow(termVectorSize);
                            // ...then the value.
                            perFieldTermVectorInput.readBytes(spare.bytes, 0, termVectorSize);
                            spare.length = termVectorSize;
                            if (hasTermStatistic) {
                                docFreq = readPotentiallyNegativeVInt(perFieldTermVectorInput);
                                totalTermFrequency = readPotentiallyNegativeVLong(perFieldTermVectorInput);

                            }

                            freq = readPotentiallyNegativeVInt(perFieldTermVectorInput);
                            // grow the arrays to read the values. this is just
                            // for performance reasons. Re-use memory instead of
                            // realloc.
                            growBuffers();
                            // finally, read the values into the arrays
                            // curentPosition etc. so that we can just iterate
                            // later
                            writeInfos(perFieldTermVectorInput);
                            return spare;

                        } else {
                            return null;
                        }

                    }

                    private void writeInfos(final BytesStreamInput input) throws IOException {
                        for (int i = 0; i < freq; i++) {
                            if (hasPositions) {
                                positions[i] = input.readVInt();
                            }
                            if (hasOffsets) {
                                startOffsets[i] = input.readVInt();
                                endOffsets[i] = input.readVInt();
                            }
                            if (hasPayloads) {
                                int payloadLength = input.readVInt();
                                if (payloads[i] == null) {
                                    payloads[i] = new BytesRef(payloadLength);
                                } else {
                                    payloads[i].grow(payloadLength);
                                }
                                input.readBytes(payloads[i].bytes, 0, payloadLength);
                                payloads[i].length = payloadLength;
                                payloads[i].offset = 0;
                            }
                        }
                    }

                    private void growBuffers() {

                        if (hasPositions) {
                            positions = grow(positions, freq);
                        }
                        if (hasOffsets) {
                            startOffsets = grow(startOffsets, freq);
                            endOffsets = grow(endOffsets, freq);
                        }
                        if (hasPayloads) {
                            if (payloads.length < freq) {
                                final BytesRef[] newArray = new BytesRef[ArrayUtil.oversize(freq, RamUsageEstimator.NUM_BYTES_OBJECT_REF)];
                                System.arraycopy(payloads, 0, newArray, 0, payloads.length);
                                payloads = newArray;
                            }
                        }
                    }

                    @Override
                    public Comparator<BytesRef> getComparator() {
                        return BytesRef.getUTF8SortedAsUnicodeComparator();
                    }

                    @Override
                    public SeekStatus seekCeil(BytesRef text) throws IOException {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public void seekExact(long ord) throws IOException {
                        throw new UnsupportedOperationException("Seek is not supported");
                    }

                    @Override
                    public BytesRef term() throws IOException {
                        return spare;
                    }

                    @Override
                    public long ord() throws IOException {
                        throw new UnsupportedOperationException("ordinals are not supported");
                    }

                    @Override
                    public int docFreq() throws IOException {
                        return docFreq;
                    }

                    @Override
                    public long totalTermFreq() throws IOException {
                        return totalTermFrequency;
                    }

                    @Override
                    public DocsEnum docs(Bits liveDocs, DocsEnum reuse, int flags) throws IOException {
                        return docsAndPositions(liveDocs, reuse instanceof DocsAndPositionsEnum ? (DocsAndPositionsEnum) reuse : null, 0);
                    }

                    @Override
                    public DocsAndPositionsEnum docsAndPositions(Bits liveDocs, DocsAndPositionsEnum reuse, int flags) throws IOException {
                        final TermVectorsDocsAndPosEnum retVal = (reuse instanceof TermVectorsDocsAndPosEnum ? (TermVectorsDocsAndPosEnum) reuse
                                : new TermVectorsDocsAndPosEnum());
                        return retVal.reset(hasPositions ? positions : null, hasOffsets ? startOffsets : null, hasOffsets ? endOffsets
                                : null, hasPayloads ? payloads : null, freq);
                    }

                };
            }

            @Override
            public Comparator<BytesRef> getComparator() {
                return BytesRef.getUTF8SortedAsUnicodeComparator();
            }

            @Override
            public long size() throws IOException {
                return numTerms;
            }

            @Override
            public long getSumTotalTermFreq() throws IOException {
                return sumTotalTermFreq;
            }

            @Override
            public long getSumDocFreq() throws IOException {
                return sumDocFreq;
            }

            @Override
            public int getDocCount() throws IOException {
                return docCount;
            }
            
            @Override
            public boolean hasFreqs() {
                return true;
            }

            @Override
            public boolean hasOffsets() {
                return hasOffsets;
            }

            @Override
            public boolean hasPositions() {
                return hasPositions;
            }

            @Override
            public boolean hasPayloads() {
                return hasPayloads;
            }

        };
    }

    @Override
    public int size() {
        return fieldMap.size();
    }

    private final class TermVectorsDocsAndPosEnum extends DocsAndPositionsEnum {
        private boolean hasPositions;
        private boolean hasOffsets;
        private boolean hasPayloads;
        int curPos = -1;
        int doc = -1;
        private int freq;
        private int[] startOffsets;
        private int[] positions;
        private BytesRef[] payloads;
        private int[] endOffsets;

        private DocsAndPositionsEnum reset(int[] positions, int[] startOffsets, int[] endOffsets, BytesRef[] payloads, int freq) {
            curPos = -1;
            doc = -1;
            this.hasPositions = positions != null;
            this.hasOffsets = startOffsets != null;
            this.hasPayloads = payloads != null;
            this.freq = freq;
            this.startOffsets = startOffsets;
            this.endOffsets = endOffsets;
            this.payloads = payloads;
            this.positions = positions;
            return this;
        }

        @Override
        public int nextDoc() throws IOException {
            return doc = (doc == -1 ? 0 : NO_MORE_DOCS);
        }

        @Override
        public int docID() {
            return doc;
        }

        @Override
        public int advance(int target) throws IOException {
            while (nextDoc() < target && doc != NO_MORE_DOCS) {
            }
            return doc;
        }

        @Override
        public int freq() throws IOException {
            return freq;
        }

        // call nextPosition once before calling this one
        // because else counter is not advanced
        @Override
        public int startOffset() throws IOException {
            assert curPos < freq && curPos >= 0;
            return hasOffsets ? startOffsets[curPos] : -1;

        }

        @Override
        // can return -1 if posistions were not requested or
        // stored but offsets were stored and requested
        public int nextPosition() throws IOException {
            assert curPos + 1 < freq;
            ++curPos;
            // this is kind of cheating but if you don't need positions
            // we safe lots fo space on the wire
            return hasPositions ? positions[curPos] : -1;
        }

        @Override
        public BytesRef getPayload() throws IOException {
            assert curPos < freq && curPos >= 0;
            return hasPayloads ? payloads[curPos] : null;
        }

        @Override
        public int endOffset() throws IOException {
            assert curPos < freq && curPos >= 0;
            return hasOffsets ? endOffsets[curPos] : -1;
        }

        @Override
        public long cost() {
            return 1;
        }
    }

    // read a vInt. this is used if the integer might be negative. In this case,
    // the writer writes a 0 for -1 or value +1 and accordingly we have to
    // substract 1 again
    // adds one to mock not existing term freq
    int readPotentiallyNegativeVInt(BytesStreamInput stream) throws IOException {
        return stream.readVInt() - 1;
    }

    // read a vLong. this is used if the integer might be negative. In this
    // case, the writer writes a 0 for -1 or value +1 and accordingly we have to
    // substract 1 again
    // adds one to mock not existing term freq
    long readPotentiallyNegativeVLong(BytesStreamInput stream) throws IOException {
        return stream.readVLong() - 1;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7234.java