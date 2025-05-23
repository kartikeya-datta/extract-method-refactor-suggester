error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7472.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7472.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7472.java
text:
```scala
public static final C@@oncurrentMap<AssertingSearcher, RuntimeException> INFLIGHT_ENGINE_SEARCHERS = new ConcurrentHashMap<>();

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

package org.elasticsearch.test.engine;

import org.apache.lucene.index.AssertingDirectoryReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FilterDirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.AssertingIndexSearcher;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.SearcherManager;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.analysis.AnalysisService;
import org.elasticsearch.index.codec.CodecService;
import org.elasticsearch.index.deletionpolicy.SnapshotDeletionPolicy;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.engine.EngineException;
import org.elasticsearch.index.engine.internal.InternalEngine;
import org.elasticsearch.index.indexing.ShardIndexingService;
import org.elasticsearch.index.merge.policy.MergePolicyProvider;
import org.elasticsearch.index.merge.scheduler.MergeSchedulerProvider;
import org.elasticsearch.index.settings.IndexSettings;
import org.elasticsearch.index.settings.IndexSettingsService;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.index.similarity.SimilarityService;
import org.elasticsearch.index.store.Store;
import org.elasticsearch.index.translog.Translog;
import org.elasticsearch.indices.warmer.IndicesWarmer;
import org.elasticsearch.test.TestCluster;
import org.elasticsearch.threadpool.ThreadPool;

import java.lang.reflect.Constructor;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class MockInternalEngine extends InternalEngine implements Engine {
    public static final ConcurrentMap<AssertingSearcher, RuntimeException> INFLIGHT_ENGINE_SEARCHERS = new ConcurrentHashMap<AssertingSearcher, RuntimeException>();
    public static final String WRAP_READER_RATIO = "index.engine.mock.random.wrap_reader_ratio";
    public static final String READER_WRAPPER_TYPE = "index.engine.mock.random.wrapper";

    private final Random random;
    private final boolean wrapReader;
    private final Class<? extends FilterDirectoryReader> wrapper;

    @Inject
    public MockInternalEngine(ShardId shardId, @IndexSettings Settings indexSettings, ThreadPool threadPool,
                              IndexSettingsService indexSettingsService, ShardIndexingService indexingService, @Nullable IndicesWarmer warmer, Store store,
                              SnapshotDeletionPolicy deletionPolicy, Translog translog, MergePolicyProvider mergePolicyProvider,
                              MergeSchedulerProvider mergeScheduler, AnalysisService analysisService, SimilarityService similarityService,
                              CodecService codecService) throws EngineException {
        super(shardId, indexSettings, threadPool, indexSettingsService, indexingService, warmer, store,
                deletionPolicy, translog, mergePolicyProvider, mergeScheduler, analysisService, similarityService, codecService);
        final long seed = indexSettings.getAsLong(TestCluster.SETTING_INDEX_SEED, 0l);
        random = new Random(seed);
        final double ratio = indexSettings.getAsDouble(WRAP_READER_RATIO, 0.0d); // DISABLED by default - AssertingDR is crazy slow
        wrapper = indexSettings.getAsClass(READER_WRAPPER_TYPE, AssertingDirectoryReader.class);
        wrapReader = random.nextDouble() < ratio;
        if (logger.isTraceEnabled()) {
            logger.trace("Using [{}] for shard [{}] seed: [{}] wrapReader: [{}]", this.getClass().getName(), shardId, seed, wrapReader);
        }
    }


    @Override
    public void close() {
        try {
            super.close();
        } finally {
            if (logger.isTraceEnabled()) {
                // log debug if we have pending searchers
                for (Entry<MockInternalEngine.AssertingSearcher, RuntimeException> entry : MockInternalEngine.INFLIGHT_ENGINE_SEARCHERS.entrySet()) {
                    logger.trace("Unreleased Searchers instance for shard [{}]", entry.getValue(), entry.getKey().shardId);
                }
            }
        }
    }

    @Override
    protected Searcher newSearcher(String source, IndexSearcher searcher, SearcherManager manager) throws EngineException {

        IndexReader reader = searcher.getIndexReader();
        IndexReader wrappedReader = reader;
        if (reader instanceof DirectoryReader && wrapReader) {
            wrappedReader = wrapReader((DirectoryReader) reader);
        }
        // this executes basic query checks and asserts that weights are normalized only once etc.
        final AssertingIndexSearcher assertingIndexSearcher = new AssertingIndexSearcher(random, wrappedReader);
        assertingIndexSearcher.setSimilarity(searcher.getSimilarity());
        // pass the original searcher to the super.newSearcher() method to make sure this is the searcher that will
        // be released later on. If we wrap an index reader here must not pass the wrapped version to the manager
        // on release otherwise the reader will be closed too early. - good news, stuff will fail all over the place if we don't get this right here
        return new AssertingSearcher(assertingIndexSearcher, super.newSearcher(source, searcher, manager), shardId);
    }

    private DirectoryReader wrapReader(DirectoryReader reader) {
        try {
            Constructor<?>[] constructors = wrapper.getConstructors();
            Constructor<?> nonRandom = null;
            for (Constructor<?> constructor : constructors) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                if (parameterTypes.length > 0 && parameterTypes[0] == DirectoryReader.class) {
                    if (parameterTypes.length == 1) {
                        nonRandom = constructor;
                    } else if (parameterTypes.length == 2 && parameterTypes[1] == Settings.class) {

                       return (DirectoryReader) constructor.newInstance(reader, indexSettings);
                    }
                }
            }
            if (nonRandom != null) {
                return (DirectoryReader) nonRandom.newInstance(reader);
            }
        } catch (Exception e) {
            throw new ElasticsearchException("Can not wrap reader", e);
        }
        return reader;
    }

    public final class AssertingSearcher implements Searcher {
        private final Searcher wrappedSearcher;
        private final ShardId shardId;
        private final IndexSearcher indexSearcher;
        private RuntimeException firstReleaseStack;
        private final Object lock = new Object();
        private final int initialRefCount;

        public AssertingSearcher(IndexSearcher indexSearcher, Searcher wrappedSearcher, ShardId shardId) {
            // we only use the given index searcher here instead of the IS of the wrapped searcher. the IS might be a wrapped searcher
            // with a wrapped reader.
            this.wrappedSearcher = wrappedSearcher;
            this.shardId = shardId;
            initialRefCount = wrappedSearcher.reader().getRefCount();
            this.indexSearcher = indexSearcher;
            assert initialRefCount > 0 : "IndexReader#getRefCount() was [" + initialRefCount + "] expected a value > [0] - reader is already closed";
            INFLIGHT_ENGINE_SEARCHERS.put(this, new RuntimeException("Unreleased Searcher, source [" + wrappedSearcher.source() + "]"));
        }

        @Override
        public String source() {
            return wrappedSearcher.source();
        }

        @Override
        public boolean release() throws ElasticsearchException {
            RuntimeException remove = INFLIGHT_ENGINE_SEARCHERS.remove(this);
            synchronized (lock) {
                // make sure we only get this once and store the stack of the first caller!
                if (remove == null) {
                    assert firstReleaseStack != null;
                    AssertionError error = new AssertionError("Released Searcher more than once, source [" + wrappedSearcher.source() + "]");
                    error.initCause(firstReleaseStack);
                    throw error;
                } else {
                    assert firstReleaseStack == null;
                    firstReleaseStack = new RuntimeException("Searcher Released first here, source [" + wrappedSearcher.source() + "]");
                }
            }
            final int refCount = wrappedSearcher.reader().getRefCount();
            // this assert seems to be paranoid but given LUCENE-5362 we better add some assertions here to make sure we catch any potential
            // problems.
            assert refCount > 0 : "IndexReader#getRefCount() was [" + refCount + "] expected a value > [0] - reader is already closed. Initial refCount was: [" + initialRefCount + "]";
            try {
                return wrappedSearcher.release();
            } catch (RuntimeException ex) {
                logger.debug("Failed to release searcher", ex);
                throw ex;
            }
        }

        @Override
        public IndexReader reader() {
            return indexSearcher.getIndexReader();
        }

        @Override
        public IndexSearcher searcher() {
            return indexSearcher;
        }

        public ShardId shardId() {
            return shardId;
        }
    }

    public static abstract class DirectoryReaderWrapper extends FilterDirectoryReader {
        protected final SubReaderWrapper subReaderWrapper;

        public DirectoryReaderWrapper(DirectoryReader in, SubReaderWrapper subReaderWrapper) {
            super(in, subReaderWrapper);
            this.subReaderWrapper = subReaderWrapper;
        }

        @Override
        public Object getCoreCacheKey() {
            return in.getCoreCacheKey();
        }

        @Override
        public Object getCombinedCoreAndDeletesKey() {
            return in.getCombinedCoreAndDeletesKey();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7472.java