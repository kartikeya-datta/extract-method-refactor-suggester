error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2605.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2605.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2605.java
text:
```scala
i@@f (ord != Ordinals.MISSING_ORDINAL) {

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

package org.elasticsearch.index.fielddata.plain;

import com.google.common.base.Preconditions;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.Terms;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.BytesRefIterator;
import org.apache.lucene.util.FixedBitSet;
import org.apache.lucene.util.NumericUtils;
import org.apache.lucene.util.packed.MonotonicAppendingLongBuffer;
import org.apache.lucene.util.packed.PackedInts;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.fielddata.*;
import org.elasticsearch.index.fielddata.fieldcomparator.LongValuesComparatorSource;
import org.elasticsearch.index.fielddata.fieldcomparator.SortMode;
import org.elasticsearch.index.fielddata.ordinals.Ordinals;
import org.elasticsearch.index.fielddata.ordinals.Ordinals.Docs;
import org.elasticsearch.index.fielddata.ordinals.OrdinalsBuilder;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.index.settings.IndexSettings;

import java.util.EnumSet;

/**
 * Stores numeric data into bit-packed arrays for better memory efficiency.
 */
public class PackedArrayIndexFieldData extends AbstractIndexFieldData<AtomicNumericFieldData> implements IndexNumericFieldData<AtomicNumericFieldData> {

    public static class Builder implements IndexFieldData.Builder {

        private NumericType numericType;

        public Builder setNumericType(NumericType numericType) {
            this.numericType = numericType;
            return this;
        }

        @Override
        public IndexFieldData<AtomicNumericFieldData> build(Index index, @IndexSettings Settings indexSettings, FieldMapper.Names fieldNames, FieldDataType type, IndexFieldDataCache cache) {
            return new PackedArrayIndexFieldData(index, indexSettings, fieldNames, type, cache, numericType);
        }
    }

    private final NumericType numericType;

    public PackedArrayIndexFieldData(Index index, @IndexSettings Settings indexSettings, FieldMapper.Names fieldNames, FieldDataType fieldDataType, IndexFieldDataCache cache, NumericType numericType) {
        super(index, indexSettings, fieldNames, fieldDataType, cache);
        Preconditions.checkNotNull(numericType);
        Preconditions.checkArgument(EnumSet.of(NumericType.BYTE, NumericType.SHORT, NumericType.INT, NumericType.LONG).contains(numericType), getClass().getSimpleName() + " only supports integer types, not " + numericType);
        this.numericType = numericType;
    }

    @Override
    public NumericType getNumericType() {
        return numericType;
    }

    @Override
    public boolean valuesOrdered() {
        // because we might have single values? we can dynamically update a flag to reflect that
        // based on the atomic field data loaded
        return false;
    }

    @Override
    public AtomicNumericFieldData loadDirect(AtomicReaderContext context) throws Exception {
        AtomicReader reader = context.reader();
        Terms terms = reader.terms(getFieldNames().indexName());
        if (terms == null) {
            return PackedArrayAtomicFieldData.empty(reader.maxDoc());
        }
        // TODO: how can we guess the number of terms? numerics end up creating more terms per value...
        // Lucene encodes numeric data so that the lexicographical (encoded) order matches the integer order so we know the sequence of
        // longs is going to be monotonically increasing
        final MonotonicAppendingLongBuffer values = new MonotonicAppendingLongBuffer();

        final float acceptableTransientOverheadRatio = fieldDataType.getSettings().getAsFloat("acceptable_transient_overhead_ratio", OrdinalsBuilder.DEFAULT_ACCEPTABLE_OVERHEAD_RATIO);
        OrdinalsBuilder builder = new OrdinalsBuilder(-1, reader.maxDoc(), acceptableTransientOverheadRatio);
        try {
            BytesRefIterator iter = builder.buildFromTerms(getNumericType().wrapTermsEnum(terms.iterator(null)));
            BytesRef term;
            assert !getNumericType().isFloatingPoint();
            final boolean indexedAsLong = getNumericType().requiredBits() > 32;
            while ((term = iter.next()) != null) {
                final long value = indexedAsLong
                        ? NumericUtils.prefixCodedToLong(term)
                        : NumericUtils.prefixCodedToInt(term);
                assert values.size() == 0 || value > values.get(values.size() - 1);
                values.add(value);
            }
            Ordinals build = builder.build(fieldDataType.getSettings());

            if (!build.isMultiValued() && CommonSettings.removeOrdsOnSingleValue(fieldDataType)) {
                Docs ordinals = build.ordinals();
                final FixedBitSet set = builder.buildDocsWithValuesSet();

                long minValue, maxValue;
                minValue = maxValue = 0;
                if (values.size() > 0) {
                    minValue = values.get(0);
                    maxValue = values.get(values.size() - 1);
                }

                // Encode document without a value with a special value
                long missingValue = 0;
                if (set != null) {
                    if ((maxValue - minValue + 1) == values.size()) {
                        // values are dense
                        if (minValue > Long.MIN_VALUE) {
                            missingValue = --minValue;
                        } else {
                            assert maxValue != Long.MAX_VALUE;
                            missingValue = ++maxValue;
                        }
                    } else {
                        for (long i = 1; i < values.size(); ++i) {
                            if (values.get(i) > values.get(i - 1) + 1) {
                                missingValue = values.get(i - 1) + 1;
                                break;
                            }
                        }
                    }
                    missingValue -= minValue; // delta
                }

                final long delta = maxValue - minValue;
                final int bitsRequired = delta < 0 ? 64 : PackedInts.bitsRequired(delta);
                final float acceptableOverheadRatio = fieldDataType.getSettings().getAsFloat("acceptable_overhead_ratio", PackedInts.DEFAULT);
                final PackedInts.FormatAndBits formatAndBits = PackedInts.fastestFormatAndBits(reader.maxDoc(), bitsRequired, acceptableOverheadRatio);

                // there's sweet spot where due to low unique value count, using ordinals will consume less memory
                final long singleValuesSize = formatAndBits.format.longCount(PackedInts.VERSION_CURRENT, reader.maxDoc(), formatAndBits.bitsPerValue) * 8L;
                final long uniqueValuesSize = values.ramBytesUsed();
                final long ordinalsSize = build.getMemorySizeInBytes();

                if (uniqueValuesSize + ordinalsSize < singleValuesSize) {
                    return new PackedArrayAtomicFieldData.WithOrdinals(values, reader.maxDoc(), build);
                }

                final PackedInts.Mutable sValues = PackedInts.getMutable(reader.maxDoc(), bitsRequired, acceptableOverheadRatio);
                if (missingValue != 0) {
                    sValues.fill(0, sValues.size(), missingValue);
                }
                for (int i = 0; i < reader.maxDoc(); i++) {
                    final long ord = ordinals.getOrd(i);
                    if (ord > 0) {
                        sValues.set(i, values.get(ord - 1) - minValue);
                    }
                }
                if (set == null) {
                    return new PackedArrayAtomicFieldData.Single(sValues, minValue, reader.maxDoc(), ordinals.getNumOrds());
                } else {
                    return new PackedArrayAtomicFieldData.SingleSparse(sValues, minValue, reader.maxDoc(), missingValue, ordinals.getNumOrds());
                }
            } else {
                return new PackedArrayAtomicFieldData.WithOrdinals(values, reader.maxDoc(), build);
            }
        } finally {
            builder.close();
        }

    }

    @Override
    public XFieldComparatorSource comparatorSource(@Nullable Object missingValue, SortMode sortMode) {
        return new LongValuesComparatorSource(this, missingValue, sortMode);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2605.java