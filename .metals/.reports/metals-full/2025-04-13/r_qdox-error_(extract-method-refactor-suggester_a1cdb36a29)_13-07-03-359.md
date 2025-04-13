error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10027.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10027.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10027.java
text:
```scala
r@@eturn null;

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.index.search.child;

import gnu.trove.set.hash.THashSet;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.FixedBitSet;
import org.elasticsearch.ElasticSearchIllegalStateException;
import org.elasticsearch.common.CacheRecycler;
import org.elasticsearch.common.bytes.HashedBytesArray;
import org.elasticsearch.common.lucene.docset.GetDocSet;
import org.elasticsearch.common.lucene.search.NoopCollector;
import org.elasticsearch.index.cache.id.IdReaderTypeCache;
import org.elasticsearch.search.internal.ScopePhase;
import org.elasticsearch.search.internal.SearchContext;

import java.io.IOException;
import java.util.Map;

/**
 *
 */
public abstract class HasChildFilter extends Filter implements ScopePhase.CollectorPhase {

    final Query childQuery;
    final String scope;
    final String parentType;
    final String childType;
    final SearchContext searchContext;

    protected HasChildFilter(Query childQuery, String scope, String parentType, String childType, SearchContext searchContext) {
        this.searchContext = searchContext;
        this.parentType = parentType;
        this.childType = childType;
        this.scope = scope;
        this.childQuery = childQuery;
    }

    public Query query() {
        return childQuery;
    }

    public String scope() {
        return scope;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("child_filter[").append(childType).append("/").append(parentType).append("](").append(childQuery).append(')');
        return sb.toString();
    }

    public static HasChildFilter create(Query childQuery, String scope, String parentType, String childType, SearchContext searchContext, String executionType) {
        // This mechanism is experimental and will most likely be removed.
        if ("bitset".equals(executionType)) {
            return new Bitset(childQuery, scope, parentType, childType, searchContext);
        } else if ("uid".endsWith(executionType)) {
            return new Uid(childQuery, scope, parentType, childType, searchContext);
        }
        throw new ElasticSearchIllegalStateException("Illegal has_child execution type: " + executionType);
    }

    static class Bitset extends HasChildFilter {

        private Map<Object, FixedBitSet> parentDocs;

        public Bitset(Query childQuery, String scope, String parentType, String childType, SearchContext searchContext) {
            super(childQuery, scope, parentType, childType, searchContext);
        }

        public boolean requiresProcessing() {
            return parentDocs == null;
        }

        public Collector collector() {
            return new ChildCollector(parentType, searchContext);
        }

        public void processCollector(Collector collector) {
            this.parentDocs = ((ChildCollector) collector).parentDocs();
        }

        public void clear() {
            parentDocs = null;
        }

        public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
            if (parentDocs == null) {
                throw new ElasticSearchIllegalStateException("has_child filter/query hasn't executed properly");
            }

            // ok to return null
            return parentDocs.get(reader.getCoreCacheKey());
        }

    }

    static class Uid extends HasChildFilter {

        THashSet<HashedBytesArray> collectedUids;

        Uid(Query childQuery, String scope, String parentType, String childType, SearchContext searchContext) {
            super(childQuery, scope, parentType, childType, searchContext);
        }

        public boolean requiresProcessing() {
            return collectedUids == null;
        }

        public Collector collector() {
            collectedUids = CacheRecycler.popHashSet();
            return new UidCollector(parentType, searchContext, collectedUids);
        }

        public void processCollector(Collector collector) {
            collectedUids = ((UidCollector) collector).collectedUids;
        }

        public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
            if (collectedUids == null) {
                throw new ElasticSearchIllegalStateException("has_child filter/query hasn't executed properly");
            }

            IdReaderTypeCache idReaderTypeCache = searchContext.idCache().reader(reader).type(parentType);
            if (idReaderTypeCache != null) {
                return new ParentDocSet(reader, collectedUids, idReaderTypeCache);
            } else {
                return DocIdSet.EMPTY_DOCIDSET;
            }
        }

        public void clear() {
            if (collectedUids != null) {
                CacheRecycler.pushHashSet(collectedUids);
            }
            collectedUids = null;
        }

        static class ParentDocSet extends GetDocSet {

            final IndexReader reader;
            final THashSet<HashedBytesArray> parents;
            final IdReaderTypeCache typeCache;

            ParentDocSet(IndexReader reader, THashSet<HashedBytesArray> parents, IdReaderTypeCache typeCache) {
                super(reader.maxDoc());
                this.reader = reader;
                this.parents = parents;
                this.typeCache = typeCache;
            }

            public boolean get(int doc) {
                return !reader.isDeleted(doc) && parents.contains(typeCache.idByDoc(doc));
            }
        }

        static class UidCollector extends NoopCollector {

            final String parentType;
            final SearchContext context;
            final THashSet<HashedBytesArray> collectedUids;

            private IdReaderTypeCache typeCache;

            UidCollector(String parentType, SearchContext context, THashSet<HashedBytesArray> collectedUids) {
                this.parentType = parentType;
                this.context = context;
                this.collectedUids = collectedUids;
            }

            @Override
            public void collect(int doc) throws IOException {
                collectedUids.add(typeCache.parentIdByDoc(doc));
            }

            @Override
            public void setNextReader(IndexReader reader, int docBase) throws IOException {
                typeCache = context.idCache().reader(reader).type(parentType);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10027.java