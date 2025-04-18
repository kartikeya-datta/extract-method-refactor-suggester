error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9912.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9912.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9912.java
text:
```scala
r@@eturn AtomicFieldData.WithOrdinals.EMPTY;

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

import com.carrotsearch.hppc.ObjectObjectOpenHashMap;
import com.carrotsearch.hppc.cursors.ObjectObjectCursor;
import org.apache.lucene.index.*;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.PagedBytes;
import org.apache.lucene.util.packed.MonotonicAppendingLongBuffer;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.ElasticsearchIllegalStateException;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.breaker.MemoryCircuitBreaker;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.fielddata.*;
import org.elasticsearch.index.fielddata.fieldcomparator.BytesRefFieldComparatorSource;
import org.elasticsearch.index.fielddata.ordinals.GlobalOrdinalsBuilder;
import org.elasticsearch.index.fielddata.ordinals.GlobalOrdinalsIndexFieldData;
import org.elasticsearch.index.fielddata.ordinals.Ordinals;
import org.elasticsearch.index.fielddata.ordinals.OrdinalsBuilder;
import org.elasticsearch.index.mapper.DocumentMapper;
import org.elasticsearch.index.mapper.DocumentTypeListener;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.index.mapper.MapperService;
import org.elasticsearch.index.mapper.internal.ParentFieldMapper;
import org.elasticsearch.index.mapper.internal.UidFieldMapper;
import org.elasticsearch.index.settings.IndexSettings;
import org.elasticsearch.indices.fielddata.breaker.CircuitBreakerService;
import org.elasticsearch.search.MultiValueMode;

import java.io.IOException;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * ParentChildIndexFieldData is responsible for loading the id cache mapping
 * needed for has_child and has_parent queries into memory.
 */
public class ParentChildIndexFieldData extends AbstractIndexFieldData<ParentChildAtomicFieldData> implements DocumentTypeListener {

    private final NavigableSet<BytesRef> parentTypes;
    private final CircuitBreakerService breakerService;
    private final GlobalOrdinalsBuilder globalOrdinalsBuilder;

    // If child type (a type with _parent field) is added or removed, we want to make sure modifications don't happen
    // while loading.
    private final Object lock = new Object();

    public ParentChildIndexFieldData(Index index, @IndexSettings Settings indexSettings, FieldMapper.Names fieldNames,
                                     FieldDataType fieldDataType, IndexFieldDataCache cache, MapperService mapperService,
                                     CircuitBreakerService breakerService, GlobalOrdinalsBuilder globalOrdinalsBuilder) {
        super(index, indexSettings, fieldNames, fieldDataType, cache);
        parentTypes = new TreeSet<>(BytesRef.getUTF8SortedAsUnicodeComparator());
        this.breakerService = breakerService;
        this.globalOrdinalsBuilder = globalOrdinalsBuilder;
        for (DocumentMapper documentMapper : mapperService) {
            beforeCreate(documentMapper);
        }
        mapperService.addTypeListener(this);
    }

    @Override
    public boolean valuesOrdered() {
        return true;
    }

    @Override
    public XFieldComparatorSource comparatorSource(@Nullable Object missingValue, MultiValueMode sortMode) {
        return new BytesRefFieldComparatorSource(this, missingValue, sortMode);
    }

    @Override
    public ParentChildAtomicFieldData loadDirect(AtomicReaderContext context) throws Exception {
        AtomicReader reader = context.reader();
        final float acceptableTransientOverheadRatio = fieldDataType.getSettings().getAsFloat(
                "acceptable_transient_overhead_ratio", OrdinalsBuilder.DEFAULT_ACCEPTABLE_OVERHEAD_RATIO
        );

        synchronized (lock) {
            boolean success = false;
            ParentChildAtomicFieldData data = null;
            ParentChildFilteredTermsEnum termsEnum = new ParentChildFilteredTermsEnum(
                    new ParentChildIntersectTermsEnum(reader, UidFieldMapper.NAME, ParentFieldMapper.NAME),
                    parentTypes
            );
            ParentChildEstimator estimator = new ParentChildEstimator(breakerService.getBreaker(), termsEnum);
            TermsEnum estimatedTermsEnum = estimator.beforeLoad(null);
            ObjectObjectOpenHashMap<String, TypeBuilder> typeBuilders = ObjectObjectOpenHashMap.newInstance();
            try {
                try {
                    DocsEnum docsEnum = null;
                    for (BytesRef term = estimatedTermsEnum.next(); term != null; term = estimatedTermsEnum.next()) {
                        // Usually this would be estimatedTermsEnum, but the
                        // abstract TermsEnum class does not support the .type()
                        // and .id() methods, so we skip using the wrapped
                        // TermsEnum and delegate directly to the
                        // ParentChildFilteredTermsEnum that was originally wrapped
                        String type = termsEnum.type();
                        TypeBuilder typeBuilder = typeBuilders.get(type);
                        if (typeBuilder == null) {
                            typeBuilders.put(type, typeBuilder = new TypeBuilder(acceptableTransientOverheadRatio, reader));
                        }

                        BytesRef id = termsEnum.id();
                        final long termOrd = typeBuilder.builder.nextOrdinal();
                        assert termOrd == typeBuilder.termOrdToBytesOffset.size();
                        typeBuilder.termOrdToBytesOffset.add(typeBuilder.bytes.copyUsingLengthPrefix(id));
                        docsEnum = estimatedTermsEnum.docs(null, docsEnum, DocsEnum.FLAG_NONE);
                        for (int docId = docsEnum.nextDoc(); docId != DocsEnum.NO_MORE_DOCS; docId = docsEnum.nextDoc()) {
                            typeBuilder.builder.addDoc(docId);
                        }
                    }

                    ImmutableOpenMap.Builder<String, PagedBytesAtomicFieldData> typeToAtomicFieldData = ImmutableOpenMap.builder(typeBuilders.size());
                    for (ObjectObjectCursor<String, TypeBuilder> cursor : typeBuilders) {
                        final long sizePointer = cursor.value.bytes.getPointer();
                        PagedBytes.Reader bytesReader = cursor.value.bytes.freeze(true);
                        final Ordinals ordinals = cursor.value.builder.build(fieldDataType.getSettings());

                        typeToAtomicFieldData.put(
                                cursor.key,
                                new PagedBytesAtomicFieldData(bytesReader, sizePointer, cursor.value.termOrdToBytesOffset, ordinals)
                        );
                    }
                    data = new ParentChildAtomicFieldData(typeToAtomicFieldData.build());
                } finally {
                    for (ObjectObjectCursor<String, TypeBuilder> cursor : typeBuilders) {
                        cursor.value.builder.close();
                    }
                }
                success = true;
                return data;
            } finally {
                if (success) {
                    estimator.afterLoad(estimatedTermsEnum, data.getMemorySizeInBytes());
                } else {
                    estimator.afterLoad(estimatedTermsEnum, 0);
                }
            }
        }
    }

    public WithOrdinals getGlobalParentChild(String type, IndexReader indexReader) {
        ParentTypesGlobalOrdinalsLoading loading = new ParentTypesGlobalOrdinalsLoading();
        ParentChildGlobalOrdinalsIndexFieldData holder = (ParentChildGlobalOrdinalsIndexFieldData) loading.loadGlobal(indexReader);
        return holder.type(type);
    }

    @Override
    public void beforeCreate(DocumentMapper mapper) {
        synchronized (lock) {
            ParentFieldMapper parentFieldMapper = mapper.parentFieldMapper();
            if (parentFieldMapper.active()) {
                // A _parent field can never be added to an existing mapping, so a _parent field either exists on
                // a new created or doesn't exists. This is why we can update the known parent types via DocumentTypeListener
                if (parentTypes.add(new BytesRef(parentFieldMapper.type()))) {
                    clear();
                }
            }
        }
    }

    @Override
    public void afterRemove(DocumentMapper mapper) {
        synchronized (lock) {
            ParentFieldMapper parentFieldMapper = mapper.parentFieldMapper();
            if (parentFieldMapper.active()) {
                parentTypes.remove(new BytesRef(parentFieldMapper.type()));
            }
        }
    }

    class TypeBuilder {

        final PagedBytes bytes;
        final MonotonicAppendingLongBuffer termOrdToBytesOffset;
        final OrdinalsBuilder builder;

        TypeBuilder(float acceptableTransientOverheadRatio, AtomicReader reader) throws IOException {
            bytes = new PagedBytes(15);
            termOrdToBytesOffset = new MonotonicAppendingLongBuffer();
            builder = new OrdinalsBuilder(-1, reader.maxDoc(), acceptableTransientOverheadRatio);
        }
    }

    public static class Builder implements IndexFieldData.Builder {

        @Override
        public IndexFieldData<?> build(Index index, @IndexSettings Settings indexSettings, FieldMapper<?> mapper,
                                       IndexFieldDataCache cache, CircuitBreakerService breakerService,
                                       MapperService mapperService, GlobalOrdinalsBuilder globalOrdinalBuilder) {
            return new ParentChildIndexFieldData(index, indexSettings, mapper.names(), mapper.fieldDataType(), cache,
                    mapperService, breakerService, globalOrdinalBuilder);
        }
    }

    /**
     * Estimator that wraps parent/child id field data by wrapping the data
     * in a RamAccountingTermsEnum.
     */
    public class ParentChildEstimator implements PerValueEstimator {

        private final MemoryCircuitBreaker breaker;
        private final TermsEnum filteredEnum;

        // The TermsEnum is passed in here instead of being generated in the
        // beforeLoad() function since it's filtered inside the previous
        // TermsEnum wrappers
        public ParentChildEstimator(MemoryCircuitBreaker breaker, TermsEnum filteredEnum) {
            this.breaker = breaker;
            this.filteredEnum = filteredEnum;
        }

        /**
         * General overhead for ids is 2 times the length of the ID
         */
        @Override
        public long bytesPerValue(BytesRef term) {
            if (term == null) {
                return 0;
            }
            return 2 * term.length;
        }

        /**
         * Wraps the already filtered {@link TermsEnum} in a
         * {@link RamAccountingTermsEnum} and returns it
         */
        @Override
        public TermsEnum beforeLoad(Terms terms) throws IOException {
            return new RamAccountingTermsEnum(filteredEnum, breaker, this, "parent/child id cache");
        }

        /**
         * Adjusts the breaker based on the difference between the actual usage
         * and the aggregated estimations.
         */
        @Override
        public void afterLoad(TermsEnum termsEnum, long actualUsed) {
            assert termsEnum instanceof RamAccountingTermsEnum;
            long estimatedBytes = ((RamAccountingTermsEnum) termsEnum).getTotalBytes();
            breaker.addWithoutBreaking(-(estimatedBytes - actualUsed));
        }
    }

    private class ParentTypesGlobalOrdinalsLoading implements WithOrdinals {

        public ParentTypesGlobalOrdinalsLoading() {
        }

        @Override
        public AtomicFieldData.WithOrdinals load(AtomicReaderContext context) {
            throw new ElasticsearchIllegalStateException("Shouldn't be invoked");
        }

        @Override
        public AtomicFieldData.WithOrdinals loadDirect(AtomicReaderContext context) {
            throw new ElasticsearchIllegalStateException("Shouldn't be invoked");
        }

        @Override
        public WithOrdinals loadGlobal(IndexReader indexReader) {
            if (indexReader.leaves().size() <= 1) {
                // ordinals are already global
                ImmutableOpenMap.Builder<String, WithOrdinals> globalIfdPerType = ImmutableOpenMap.builder();
                for (BytesRef parentType : parentTypes) {
                    PerType perType = new PerType(parentType.utf8ToString());
                    globalIfdPerType.put(perType.type, perType);
                }
                return new ParentChildGlobalOrdinalsIndexFieldData(globalIfdPerType.build(), 0);
            }

            try {
                return cache.load(indexReader, this);
            } catch (Throwable e) {
                if (e instanceof ElasticsearchException) {
                    throw (ElasticsearchException) e;
                } else {
                    throw new ElasticsearchException(e.getMessage(), e);
                }
            }
        }

        @Override
        public WithOrdinals localGlobalDirect(IndexReader indexReader) throws Exception {
            ImmutableOpenMap.Builder<String, WithOrdinals> globalIfdPerType = ImmutableOpenMap.builder();
            long memorySizeInBytes = 0;
            for (BytesRef parentType : parentTypes) {
                PerType perType = new PerType(parentType.utf8ToString());
                GlobalOrdinalsIndexFieldData globalIfd = (GlobalOrdinalsIndexFieldData) globalOrdinalsBuilder.build(indexReader, perType, indexSettings, breakerService);
                globalIfdPerType.put(perType.type, globalIfd);
                memorySizeInBytes += globalIfd.getMemorySizeInBytes();
            }
            return new ParentChildGlobalOrdinalsIndexFieldData(globalIfdPerType.build(), memorySizeInBytes);
        }

        @Override
        public FieldMapper.Names getFieldNames() {
            return ParentChildIndexFieldData.this.getFieldNames();
        }

        @Override
        public FieldDataType getFieldDataType() {
            return ParentChildIndexFieldData.this.getFieldDataType();
        }

        @Override
        public boolean valuesOrdered() {
            return ParentChildIndexFieldData.this.valuesOrdered();
        }

        @Override
        public XFieldComparatorSource comparatorSource(@Nullable Object missingValue, MultiValueMode sortMode) {
            throw new UnsupportedOperationException("Sort not supported on PerParentTypeGlobalOrdinals...");
        }

        @Override
        public void clear() {
        }

        @Override
        public void clear(IndexReader reader) {
        }

        @Override
        public Index index() {
            return ParentChildIndexFieldData.this.index();
        }

        private final class PerType extends ParentTypesGlobalOrdinalsLoading {

            private final String type;

            public PerType(String type) {
                this.type = type;
            }

            @Override
            public AtomicFieldData.WithOrdinals load(AtomicReaderContext context) {
                return loadDirect(context);
            }

            @Override
            public AtomicFieldData.WithOrdinals loadDirect(AtomicReaderContext context) {
                ParentChildAtomicFieldData parentChildAtomicFieldData = ParentChildIndexFieldData.this.load(context);
                AtomicFieldData.WithOrdinals typeAfd = parentChildAtomicFieldData.getAtomicFieldData(type);
                if(typeAfd != null) {
                    return typeAfd;
                } else {
                    return PagedBytesAtomicFieldData.empty();
                }
            }

            @Override
            public WithOrdinals loadGlobal(IndexReader indexReader) {
                return this;
            }

            @Override
            public WithOrdinals localGlobalDirect(IndexReader indexReader) throws Exception {
                return this;
            }
        }
    }

    // Effectively this is a cache key for in the field data cache
    private final class ParentChildGlobalOrdinalsIndexFieldData extends GlobalOrdinalsIndexFieldData {

        private final ImmutableOpenMap<String, WithOrdinals> typeGlobalOrdinals;

        private ParentChildGlobalOrdinalsIndexFieldData(ImmutableOpenMap<String, WithOrdinals> typeGlobalOrdinals, long memorySizeInBytes) {
            super(ParentChildIndexFieldData.this.index(), ParentChildIndexFieldData.this.indexSettings, ParentChildIndexFieldData.this.getFieldNames(), ParentChildIndexFieldData.this.getFieldDataType(), memorySizeInBytes);
            this.typeGlobalOrdinals = typeGlobalOrdinals;
        }

        @Override
        public AtomicFieldData.WithOrdinals load(AtomicReaderContext context) {
            throw new ElasticsearchIllegalStateException("Can't use directly");
        }

        public WithOrdinals type(String type) {
            return typeGlobalOrdinals.get(type);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9912.java