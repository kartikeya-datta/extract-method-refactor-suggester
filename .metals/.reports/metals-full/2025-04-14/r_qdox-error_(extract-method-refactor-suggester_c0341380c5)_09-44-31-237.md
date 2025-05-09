error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7560.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7560.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7560.java
text:
```scala
t@@his.filter = Queries.wrap(query);

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

package org.elasticsearch.search.facet.query;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.search.*;
import org.apache.lucene.util.Bits;
import org.elasticsearch.common.lucene.docset.AndDocIdSet;
import org.elasticsearch.common.lucene.docset.ContextDocIdSet;
import org.elasticsearch.common.lucene.docset.DocIdSets;
import org.elasticsearch.common.lucene.search.Queries;
import org.elasticsearch.common.lucene.search.XConstantScoreQuery;
import org.elasticsearch.common.lucene.search.XFilteredQuery;
import org.elasticsearch.search.facet.FacetExecutor;
import org.elasticsearch.search.facet.InternalFacet;

import java.io.IOException;
import java.util.List;

/**
 *
 */
public class QueryFacetExecutor extends FacetExecutor {

    private final Query query;
    private final Filter filter;

    // default to not initialized
    long count = -1;

    public QueryFacetExecutor(Query query) {
        this.query = query;
        Filter possibleFilter = extractFilterIfApplicable(query);
        if (possibleFilter != null) {
            this.filter = possibleFilter;
        } else {
            this.filter = new QueryWrapperFilter(query);
        }
    }

    @Override
    public Collector collector() {
        return new Collector();
    }

    @Override
    public Post post() {
        return new Post();
    }

    @Override
    public InternalFacet buildFacet(String facetName) {
        return new InternalQueryFacet(facetName, count);
    }

    class Post extends FacetExecutor.Post {

        @Override
        public void executePost(List<ContextDocIdSet> docSets) throws IOException {
            int count = 0;
            for (ContextDocIdSet entry : docSets) {
                DocIdSet filteredDocIdSet = filter.getDocIdSet(entry.context, entry.context.reader().getLiveDocs());
                if (filteredDocIdSet == null || entry.docSet == null) {
                    continue;
                }
                DocIdSetIterator iter = new AndDocIdSet(new DocIdSet[]{entry.docSet, filteredDocIdSet}).iterator();
                while (iter.nextDoc() != DocIdSetIterator.NO_MORE_DOCS) {
                    count++;
                }
            }
            QueryFacetExecutor.this.count = count;
        }
    }

    class Collector extends FacetExecutor.Collector {

        private long count = 0;
        private Bits bits;

        @Override
        public void collect(int doc) throws IOException {
            if (bits.get(doc)) {
                count++;
            }
        }

        @Override
        public void setNextReader(AtomicReaderContext context) throws IOException {
            bits = DocIdSets.toSafeBits(context.reader(), filter.getDocIdSet(context, context.reader().getLiveDocs()));
        }

        @Override
        public void postCollection() {
            bits = null;
            QueryFacetExecutor.this.count = count;
        }
    }

    /**
     * If its a filtered query with a match all, then we just need the inner filter.
     */
    private Filter extractFilterIfApplicable(Query query) {
        if (query instanceof XFilteredQuery) {
            XFilteredQuery fQuery = (XFilteredQuery) query;
            if (Queries.isConstantMatchAllQuery(fQuery.getQuery())) {
                return fQuery.getFilter();
            }
        } else if (query instanceof XConstantScoreQuery) {
            return ((XConstantScoreQuery) query).getFilter();
        } else if (query instanceof ConstantScoreQuery) {
            ConstantScoreQuery constantScoreQuery = (ConstantScoreQuery) query;
            if (constantScoreQuery.getFilter() != null) {
                return constantScoreQuery.getFilter();
            }
        }
        return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7560.java