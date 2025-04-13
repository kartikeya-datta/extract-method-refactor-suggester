error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8237.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8237.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8237.java
text:
```scala
r@@eturn weight.scorer(context, acceptDocs);

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
package org.elasticsearch.index.search.child;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.util.Bits;
import org.elasticsearch.search.internal.SearchContext;

import java.io.IOException;

/**
 * This filters just exist for wrapping parent child queries in the delete by query api.
 * Don't use this filter for other purposes.
 *
 * @elasticsearch.internal
 */
public class DeleteByQueryWrappingFilter extends Filter {

    private final Query query;

    private IndexSearcher searcher;
    private Weight weight;

    /** Constructs a filter which only matches documents matching
     * <code>query</code>.
     */
    public DeleteByQueryWrappingFilter(Query query) {
        if (query == null)
            throw new NullPointerException("Query may not be null");
        this.query = query;
    }

    /** returns the inner Query */
    public final Query getQuery() {
        return query;
    }

    @Override
    public DocIdSet getDocIdSet(final AtomicReaderContext context, final Bits acceptDocs) throws IOException {
        SearchContext searchContext = SearchContext.current();
        if (weight == null) {
            assert searcher == null;
            searcher = searchContext.searcher();
            IndexReader indexReader = SearchContext.current().searcher().getIndexReader();
            IndexReader multiReader = null;
            try {
                if (!contains(indexReader, context)) {
                    multiReader = new MultiReader(new IndexReader[]{indexReader, context.reader()}, false);
                    Similarity similarity = searcher.getSimilarity();
                    searcher = new IndexSearcher(new MultiReader(indexReader, context.reader()));
                    searcher.setSimilarity(similarity);
                }
                weight = searcher.createNormalizedWeight(query);
            } finally {
                if (multiReader != null) {
                    multiReader.close();
                }
            }
        } else {
            IndexReader indexReader = searcher.getIndexReader();
            if (!contains(indexReader, context)) {
                try (IndexReader multiReader = new MultiReader(new IndexReader[]{indexReader, context.reader()}, false)) {
                    Similarity similarity = searcher.getSimilarity();
                    searcher = new IndexSearcher(multiReader);
                    searcher.setSimilarity(similarity);
                    weight = searcher.createNormalizedWeight(query);
                }
            }
        }

        return new DocIdSet() {
            @Override
            public DocIdSetIterator iterator() throws IOException {
                return weight.scorer(context, true, false, acceptDocs);
            }
            @Override
            public boolean isCacheable() { return false; }
        };
    }

    @Override
    public String toString() {
        return "DeleteByQueryWrappingFilter(" + query + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DeleteByQueryWrappingFilter))
            return false;
        return this.query.equals(((DeleteByQueryWrappingFilter)o).query);
    }

    @Override
    public int hashCode() {
        return query.hashCode() ^ 0x823D64CA;
    }

    static boolean contains(IndexReader indexReader, AtomicReaderContext context) {
        for (AtomicReaderContext atomicReaderContext : indexReader.leaves()) {
            if (context.reader().getCoreCacheKey().equals(atomicReaderContext.reader().getCoreCacheKey())) {
                return true;
            }
        }
        return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8237.java