error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7653.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7653.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7653.java
text:
```scala
public i@@nt freq() throws IOException {

/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.index.search.child;

import gnu.trove.map.hash.TObjectFloatHashMap;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.ToStringUtils;
import org.elasticsearch.ElasticSearchIllegalStateException;
import org.elasticsearch.common.CacheRecycler;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.bytes.HashedBytesArray;
import org.elasticsearch.common.lucene.search.NoopCollector;
import org.elasticsearch.index.cache.id.IdReaderTypeCache;
import org.elasticsearch.search.internal.ScopePhase;
import org.elasticsearch.search.internal.SearchContext;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * A query implementation that executes the wrapped parent query and
 * connects the matching parent docs to the related child documents
 * using the {@link IdReaderTypeCache}.
 */
public class ParentQuery extends Query implements ScopePhase.CollectorPhase {

    private final SearchContext searchContext;
    private final Query parentQuery;
    private final String parentType;
    private final Filter childrenFilter;
    private final List<String> childTypes;
    private final String scope;

    private TObjectFloatHashMap<HashedBytesArray> uidToScore;

    public ParentQuery(SearchContext searchContext, Query parentQuery, String parentType, List<String> childTypes, Filter childrenFilter, String scope) {
        this.searchContext = searchContext;
        this.parentQuery = parentQuery;
        this.parentType = parentType;
        this.childTypes = childTypes;
        this.childrenFilter = childrenFilter;
        this.scope = scope;
    }

    private ParentQuery(ParentQuery unwritten, Query rewrittenParentQuery) {
        this.searchContext = unwritten.searchContext;
        this.parentQuery = rewrittenParentQuery;
        this.parentType = unwritten.parentType;
        this.childrenFilter = unwritten.childrenFilter;
        this.childTypes = unwritten.childTypes;
        this.scope = unwritten.scope;

        this.uidToScore = unwritten.uidToScore;
    }

    @Override
    public boolean requiresProcessing() {
        return uidToScore == null;
    }

    @Override
    public Collector collector() {
        uidToScore = CacheRecycler.popObjectFloatMap();
        return new ParentUidCollector(uidToScore, searchContext, parentType);
    }

    @Override
    public void processCollector(Collector collector) {
        // Do nothing, we already have the references to the parent scores.
    }

    @Override
    public String scope() {
        return scope;
    }

    @Override
    public void clear() {
        if (uidToScore != null) {
            CacheRecycler.pushObjectFloatMap(uidToScore);
        }
        uidToScore = null;
    }

    @Override
    public Query query() {
        return parentQuery;
    }

    @Override
    public String toString(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("ParentQuery[").append(parentType).append("/").append(childTypes)
                .append("](").append(parentQuery.toString(field)).append(')')
                .append(ToStringUtils.boost(getBoost()));
        return sb.toString();
    }

    @Override
    public Query rewrite(IndexReader reader) throws IOException {
        Query rewrittenChildQuery = parentQuery.rewrite(reader);
        if (rewrittenChildQuery == parentQuery) {
            return this;
        }
        ParentQuery rewrite = new ParentQuery(this, rewrittenChildQuery);
        int index = searchContext.scopePhases().indexOf(this);
        searchContext.scopePhases().set(index, rewrite);
        return rewrite;
    }

    @Override
    public void extractTerms(Set<Term> terms) {
        parentQuery.extractTerms(terms);
    }

    @Override
    public Weight createWeight(IndexSearcher searcher) throws IOException {
        if (uidToScore == null) {
            throw new ElasticSearchIllegalStateException("has_parent query hasn't executed properly");
        }
        return new ChildWeight(parentQuery.createWeight(searcher));
    }

    static class ParentUidCollector extends NoopCollector {

        final TObjectFloatHashMap<HashedBytesArray> uidToScore;
        final SearchContext searchContext;
        final String parentType;

        Scorer scorer;
        IdReaderTypeCache typeCache;

        ParentUidCollector(TObjectFloatHashMap<HashedBytesArray> uidToScore, SearchContext searchContext, String parentType) {
            this.uidToScore = uidToScore;
            this.searchContext = searchContext;
            this.parentType = parentType;
        }

        @Override
        public void collect(int doc) throws IOException {
            if (typeCache == null) {
                return;
            }

            HashedBytesArray parentUid = typeCache.idByDoc(doc);
            uidToScore.put(parentUid, scorer.score());
        }

        @Override
        public void setScorer(Scorer scorer) throws IOException {
            this.scorer = scorer;
        }

        @Override
        public void setNextReader(AtomicReaderContext context) throws IOException {
            typeCache = searchContext.idCache().reader(context.reader()).type(parentType);
        }
    }

    class ChildWeight extends Weight {

        private final Weight parentWeight;

        ChildWeight(Weight parentWeight) {
            this.parentWeight = parentWeight;
        }

        @Override
        public Explanation explain(AtomicReaderContext context, int doc) throws IOException {
            return new Explanation(getBoost(), "not implemented yet...");
        }

        @Override
        public Query getQuery() {
            return ParentQuery.this;
        }

        @Override
        public float getValueForNormalization() throws IOException {
            float sum = parentWeight.getValueForNormalization();
            sum *= getBoost() * getBoost();
            return sum;
        }

        @Override
        public void normalize(float norm, float topLevelBoost) {
        }

        @Override
        public Scorer scorer(AtomicReaderContext context, boolean scoreDocsInOrder, boolean topScorer, Bits acceptDocs) throws IOException {
            DocIdSet childrenDocSet = childrenFilter.getDocIdSet(context, acceptDocs);
            if (childrenDocSet == null || childrenDocSet == DocIdSet.EMPTY_DOCIDSET) {
                return null;
            }
            IdReaderTypeCache idTypeCache = searchContext.idCache().reader(context.reader()).type(parentType);
            return new ChildScorer(this, uidToScore, childrenDocSet.iterator(), idTypeCache);
        }
    }

    static class ChildScorer extends Scorer {

        final TObjectFloatHashMap<HashedBytesArray> uidToScore;
        final DocIdSetIterator childrenIterator;
        final IdReaderTypeCache typeCache;

        int currentChildDoc = -1;
        float currentScore;

        ChildScorer(Weight weight, TObjectFloatHashMap<HashedBytesArray> uidToScore, DocIdSetIterator childrenIterator, IdReaderTypeCache typeCache) {
            super(weight);
            this.uidToScore = uidToScore;
            this.childrenIterator = childrenIterator;
            this.typeCache = typeCache;
        }

        @Override
        public float score() throws IOException {
            return currentScore;
        }

        @Override
        public float freq() throws IOException {
            // We don't have the original child query hit info here...
            // But the freq of the children could be collector and returned here, but makes this Scorer more expensive.
            return 1;
        }

        @Override
        public int docID() {
            return currentChildDoc;
        }

        @Override
        public int nextDoc() throws IOException {
            while (true) {
                currentChildDoc = childrenIterator.nextDoc();
                if (currentChildDoc == DocIdSetIterator.NO_MORE_DOCS) {
                    return currentChildDoc;
                }

                BytesReference uid = typeCache.parentIdByDoc(currentChildDoc);
                if (uid == null) {
                    continue;
                }
                currentScore = uidToScore.get(uid);
                if (Float.compare(currentScore, 0) != 0) {
                    return currentChildDoc;
                }
            }
        }

        @Override
        public int advance(int target) throws IOException {
            currentChildDoc = childrenIterator.advance(target);
            if (currentChildDoc == DocIdSetIterator.NO_MORE_DOCS) {
                return currentChildDoc;
            }
            BytesReference uid = typeCache.idByDoc(currentChildDoc);
            if (uid == null) {
                return nextDoc();
            }
            currentScore = uidToScore.get(uid);
            if (Float.compare(currentScore, 0) == 0) {
                return nextDoc();
            }
            return currentChildDoc;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7653.java