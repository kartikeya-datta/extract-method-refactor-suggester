error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2395.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2395.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2395.java
text:
```scala
i@@ncrementBucketDocCount(bucketOrd, numChildren);

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
package org.elasticsearch.search.aggregations.bucket.nested;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.join.FixedBitSetCachingWrapperFilter;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.FixedBitSet;
import org.elasticsearch.common.lucene.ReaderContextAware;
import org.elasticsearch.common.lucene.docset.DocIdSets;
import org.elasticsearch.index.mapper.MapperService;
import org.elasticsearch.index.mapper.object.ObjectMapper;
import org.elasticsearch.index.search.nested.NonNestedDocsFilter;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.aggregations.bucket.SingleBucketAggregator;
import org.elasticsearch.search.aggregations.support.AggregationContext;
import org.elasticsearch.search.internal.SearchContext;

import java.io.IOException;

/**
 *
 */
public class NestedAggregator extends SingleBucketAggregator implements ReaderContextAware {

    private final String nestedPath;
    private final Aggregator parentAggregator;
    private Filter parentFilter;
    private final Filter childFilter;

    private Bits childDocs;
    private FixedBitSet parentDocs;

    public NestedAggregator(String name, AggregatorFactories factories, String nestedPath, AggregationContext aggregationContext, Aggregator parentAggregator) {
        super(name, factories, aggregationContext, parentAggregator);
        this.nestedPath = nestedPath;
        this.parentAggregator = parentAggregator;
        MapperService.SmartNameObjectMapper mapper = aggregationContext.searchContext().smartNameObjectMapper(nestedPath);
        if (mapper == null) {
            throw new AggregationExecutionException("[nested] nested path [" + nestedPath + "] not found");
        }
        ObjectMapper objectMapper = mapper.mapper();
        if (objectMapper == null) {
            throw new AggregationExecutionException("[nested] nested path [" + nestedPath + "] not found");
        }
        if (!objectMapper.nested().isNested()) {
            throw new AggregationExecutionException("[nested] nested path [" + nestedPath + "] is not nested");
        }

        childFilter = aggregationContext.searchContext().filterCache().cache(objectMapper.nestedTypeFilter());
    }

    @Override
    public void setNextReader(AtomicReaderContext reader) {
        if (parentFilter == null) {
            NestedAggregator closestNestedAggregator = findClosestNestedAggregator(parentAggregator);
            final Filter parentFilterNotCached;
            if (closestNestedAggregator == null) {
                parentFilterNotCached = NonNestedDocsFilter.INSTANCE;
            } else {
                // The aggs are instantiated in reverse, first the most inner nested aggs and lastly the top level aggs
                // So at the time a nested 'nested' aggs is parsed its closest parent nested aggs hasn't been constructed.
                // So the trick to set at the last moment just before needed and we can use its child filter as the
                // parent filter.
                parentFilterNotCached = closestNestedAggregator.childFilter;
            }
            parentFilter = SearchContext.current().filterCache().cache(parentFilterNotCached);
            // if the filter cache is disabled, we still need to produce bit sets
            parentFilter = new FixedBitSetCachingWrapperFilter(parentFilter);
        }

        try {
            DocIdSet docIdSet = parentFilter.getDocIdSet(reader, null);
            // In ES if parent is deleted, then also the children are deleted. Therefore acceptedDocs can also null here.
            childDocs = DocIdSets.toSafeBits(reader.reader(), childFilter.getDocIdSet(reader, null));
            if (DocIdSets.isEmpty(docIdSet)) {
                parentDocs = null;
            } else {
                parentDocs = (FixedBitSet) docIdSet;
            }
        } catch (IOException ioe) {
            throw new AggregationExecutionException("Failed to aggregate [" + name + "]", ioe);
        }
    }

    @Override
    public void collect(int parentDoc, long bucketOrd) throws IOException {

        // here we translate the parent doc to a list of its nested docs, and then call super.collect for evey one of them
        // so they'll be collected

        if (parentDoc == 0 || parentDocs == null) {
            return;
        }
        int prevParentDoc = parentDocs.prevSetBit(parentDoc - 1);
        int numChildren = 0;
        for (int i = (parentDoc - 1); i > prevParentDoc; i--) {
            if (childDocs.get(i)) {
                ++numChildren;
                collectBucketNoCounts(i, bucketOrd);
            }
        }
        incrementBucketDocCount(numChildren, bucketOrd);
    }

    @Override
    public InternalAggregation buildAggregation(long owningBucketOrdinal) {
        return new InternalNested(name, bucketDocCount(owningBucketOrdinal), bucketAggregations(owningBucketOrdinal));
    }

    @Override
    public InternalAggregation buildEmptyAggregation() {
        return new InternalNested(name, 0, buildEmptySubAggregations());
    }

    public String getNestedPath() {
        return nestedPath;
    }

    static NestedAggregator findClosestNestedAggregator(Aggregator parent) {
        for (; parent != null; parent = parent.parent()) {
            if (parent instanceof NestedAggregator) {
                return (NestedAggregator) parent;
            }
        }
        return null;
    }

    public static class Factory extends AggregatorFactory {

        private final String path;

        public Factory(String name, String path) {
            super(name, InternalNested.TYPE.name());
            this.path = path;
        }

        @Override
        public Aggregator create(AggregationContext context, Aggregator parent, long expectedBucketsCount) {
            return new NestedAggregator(name, factories, path, context, parent);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2395.java