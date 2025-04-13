error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7323.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7323.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7323.java
text:
```scala
private final S@@et<CustomLogDocMergePolicy> policies = new CopyOnWriteArraySet<>();

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

package org.elasticsearch.index.merge.policy;

import org.apache.lucene.index.LogDocMergePolicy;
import org.apache.lucene.index.MergePolicy;
import org.apache.lucene.index.SegmentInfos;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.common.Preconditions;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.settings.IndexSettingsService;
import org.elasticsearch.index.store.Store;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 *
 */
public class LogDocMergePolicyProvider extends AbstractMergePolicyProvider<LogDocMergePolicy> {

    private final IndexSettingsService indexSettingsService;

    private volatile int minMergeDocs;
    private volatile int maxMergeDocs;
    private volatile int mergeFactor;
    private final boolean calibrateSizeByDeletes;
    private boolean asyncMerge;

    private final Set<CustomLogDocMergePolicy> policies = new CopyOnWriteArraySet<CustomLogDocMergePolicy>();

    private final ApplySettings applySettings = new ApplySettings();

    @Inject
    public LogDocMergePolicyProvider(Store store, IndexSettingsService indexSettingsService) {
        super(store);
        Preconditions.checkNotNull(store, "Store must be provided to merge policy");
        this.indexSettingsService = indexSettingsService;

        this.minMergeDocs = componentSettings.getAsInt("min_merge_docs", LogDocMergePolicy.DEFAULT_MIN_MERGE_DOCS);
        this.maxMergeDocs = componentSettings.getAsInt("max_merge_docs", LogDocMergePolicy.DEFAULT_MAX_MERGE_DOCS);
        this.mergeFactor = componentSettings.getAsInt("merge_factor", LogDocMergePolicy.DEFAULT_MERGE_FACTOR);
        this.calibrateSizeByDeletes = componentSettings.getAsBoolean("calibrate_size_by_deletes", true);
        this.asyncMerge = indexSettings.getAsBoolean("index.merge.async", true);
        logger.debug("using [log_doc] merge policy with merge_factor[{}], min_merge_docs[{}], max_merge_docs[{}], calibrate_size_by_deletes[{}], async_merge[{}]",
                mergeFactor, minMergeDocs, maxMergeDocs, calibrateSizeByDeletes, asyncMerge);

        indexSettingsService.addListener(applySettings);
    }

    @Override
    public void close() throws ElasticsearchException {
        indexSettingsService.removeListener(applySettings);
    }

    @Override
    public LogDocMergePolicy newMergePolicy() {
        CustomLogDocMergePolicy mergePolicy;
        if (asyncMerge) {
            mergePolicy = new EnableMergeLogDocMergePolicy(this);
        } else {
            mergePolicy = new CustomLogDocMergePolicy(this);
        }
        mergePolicy.setMinMergeDocs(minMergeDocs);
        mergePolicy.setMaxMergeDocs(maxMergeDocs);
        mergePolicy.setMergeFactor(mergeFactor);
        mergePolicy.setCalibrateSizeByDeletes(calibrateSizeByDeletes);
        mergePolicy.setNoCFSRatio(noCFSRatio);
        policies.add(mergePolicy);
        return mergePolicy;
    }

    public static final String INDEX_MERGE_POLICY_MIN_MERGE_DOCS = "index.merge.policy.min_merge_docs";
    public static final String INDEX_MERGE_POLICY_MAX_MERGE_DOCS = "index.merge.policy.max_merge_docs";
    public static final String INDEX_MERGE_POLICY_MERGE_FACTOR = "index.merge.policy.merge_factor";

    class ApplySettings implements IndexSettingsService.Listener {
        @Override
        public void onRefreshSettings(Settings settings) {
            int minMergeDocs = settings.getAsInt(INDEX_MERGE_POLICY_MIN_MERGE_DOCS, LogDocMergePolicyProvider.this.minMergeDocs);
            if (minMergeDocs != LogDocMergePolicyProvider.this.minMergeDocs) {
                logger.info("updating min_merge_docs from [{}] to [{}]", LogDocMergePolicyProvider.this.minMergeDocs, minMergeDocs);
                LogDocMergePolicyProvider.this.minMergeDocs = minMergeDocs;
                for (CustomLogDocMergePolicy policy : policies) {
                    policy.setMinMergeDocs(minMergeDocs);
                }
            }

            int maxMergeDocs = settings.getAsInt(INDEX_MERGE_POLICY_MAX_MERGE_DOCS, LogDocMergePolicyProvider.this.maxMergeDocs);
            if (maxMergeDocs != LogDocMergePolicyProvider.this.maxMergeDocs) {
                logger.info("updating max_merge_docs from [{}] to [{}]", LogDocMergePolicyProvider.this.maxMergeDocs, maxMergeDocs);
                LogDocMergePolicyProvider.this.maxMergeDocs = maxMergeDocs;
                for (CustomLogDocMergePolicy policy : policies) {
                    policy.setMaxMergeDocs(maxMergeDocs);
                }
            }

            int mergeFactor = settings.getAsInt(INDEX_MERGE_POLICY_MERGE_FACTOR, LogDocMergePolicyProvider.this.mergeFactor);
            if (mergeFactor != LogDocMergePolicyProvider.this.mergeFactor) {
                logger.info("updating merge_factor from [{}] to [{}]", LogDocMergePolicyProvider.this.mergeFactor, mergeFactor);
                LogDocMergePolicyProvider.this.mergeFactor = mergeFactor;
                for (CustomLogDocMergePolicy policy : policies) {
                    policy.setMergeFactor(mergeFactor);
                }
            }

            final double noCFSRatio = parseNoCFSRatio(settings.get(INDEX_COMPOUND_FORMAT, Double.toString(LogDocMergePolicyProvider.this.noCFSRatio)));
            final boolean compoundFormat = noCFSRatio != 0.0;
            if (noCFSRatio != LogDocMergePolicyProvider.this.noCFSRatio) {
                logger.info("updating index.compound_format from [{}] to [{}]", formatNoCFSRatio(LogDocMergePolicyProvider.this.noCFSRatio), formatNoCFSRatio(noCFSRatio));
                LogDocMergePolicyProvider.this.noCFSRatio = noCFSRatio;
                for (CustomLogDocMergePolicy policy : policies) {
                    policy.setNoCFSRatio(noCFSRatio);
                }
            }
        }
    }

    public static class CustomLogDocMergePolicy extends LogDocMergePolicy {

        private final LogDocMergePolicyProvider provider;

        public CustomLogDocMergePolicy(LogDocMergePolicyProvider provider) {
            super();
            this.provider = provider;
        }

        @Override
        public void close() {
            super.close();
            provider.policies.remove(this);
        }
    }

    public static class EnableMergeLogDocMergePolicy extends CustomLogDocMergePolicy {

        public EnableMergeLogDocMergePolicy(LogDocMergePolicyProvider provider) {
            super(provider);
        }

        @Override
        public MergeSpecification findMerges(MergeTrigger trigger, SegmentInfos infos) throws IOException {
            // we don't enable merges while indexing documents, we do them in the background
            if (trigger == MergeTrigger.SEGMENT_FLUSH) {
                return null;
            }
            return super.findMerges(trigger, infos);
        }
        
        @Override
        public MergePolicy clone() {
            // Lucene IW makes a clone internally but since we hold on to this instance 
            // the clone will just be the identity.
            return this;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7323.java