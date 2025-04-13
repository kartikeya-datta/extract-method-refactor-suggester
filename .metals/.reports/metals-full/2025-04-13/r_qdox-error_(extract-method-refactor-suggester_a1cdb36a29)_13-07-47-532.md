error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6713.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6713.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,17]

error in qdox parser
file content:
```java
offset: 17
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6713.java
text:
```scala
public abstract v@@oid postCollection() throws IOException;

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

package org.elasticsearch.search.facet;

import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Scorer;
import org.elasticsearch.common.lucene.docset.AndDocIdSet;
import org.elasticsearch.common.lucene.docset.ContextDocIdSet;
import org.elasticsearch.common.lucene.search.XCollector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A facet processor ends up actually executing the relevant facet for a specific
 * search request.
 * <p/>
 * The facet executor requires at least the {@link #collector()} method to be implemented,
 * with an optional {@link #post()} implementation if specific optimizations can be done.
 */
public abstract class FacetExecutor {

    /**
     * A post class extends this class to implement post hits processing.
     */
    public static abstract class Post {

        public abstract void executePost(List<ContextDocIdSet> docSets) throws IOException;

        /**
         * A filtered post execution.
         */
        public static class Filtered extends Post {

            private final Post post;
            private final Filter filter;

            public Filtered(Post post, Filter filter) {
                this.post = post;
                this.filter = filter;
            }

            @Override
            public void executePost(List<ContextDocIdSet> docSets) throws IOException {
                List<ContextDocIdSet> filteredEntries = new ArrayList<>(docSets.size());
                for (int i = 0; i < docSets.size(); i++) {
                    ContextDocIdSet entry = docSets.get(i);
                    DocIdSet filteredSet = filter.getDocIdSet(entry.context, null);
                    if (filteredSet != null) {
                        filteredEntries.add(new ContextDocIdSet(
                                entry.context,
                                // TODO: can we be smart here, maybe AndDocIdSet is not always fastest?
                                new AndDocIdSet(new DocIdSet[]{entry.docSet, filteredSet})
                        ));
                    }
                }
                post.executePost(filteredEntries);
            }
        }

        /**
         * A {@link FacetExecutor.Collector} based post.
         */
        public static class Collector extends Post {

            private final FacetExecutor.Collector collector;

            public Collector(FacetExecutor.Collector collector) {
                this.collector = collector;
            }

            @Override
            public void executePost(List<ContextDocIdSet> docSets) throws IOException {
                for (int i = 0; i < docSets.size(); i++) {
                    ContextDocIdSet docSet = docSets.get(i);
                    collector.setNextReader(docSet.context);
                    DocIdSetIterator it = docSet.docSet.iterator();
                    int doc;
                    while ((doc = it.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
                        collector.collect(doc);
                    }
                }
                collector.postCollection();
            }
        }
    }

    /**
     * Simple extension to {@link XCollector} that implements methods that are typically
     * not needed when doing collector based faceting.
     */
    public static abstract class Collector extends XCollector {

        @Override
        public void setScorer(Scorer scorer) throws IOException {
        }

        @Override
        public boolean acceptsDocsOutOfOrder() {
            return true;
        }

        @Override
        public abstract void postCollection();
    }

    /**
     * The mode of the execution.
     */
    public static enum Mode {
        /**
         * Collector mode, maps to {@link #collector()}.
         */
        COLLECTOR,
        /**
         * Post mode, maps to {@link #post()}.
         */
        POST
    }

    /**
     * Builds the facet.
     */
    public abstract InternalFacet buildFacet(String facetName);

    /**
     * A collector based facet implementation, collection the facet as hits match.
     */
    public abstract Collector collector();

    /**
     * A post based facet that executes the facet using the aggregated docs. By default
     * uses the {@link Post.Collector} based implementation.
     * <p/>
     * Can be overridden if a more optimized non collector based implementation can be implemented.
     */
    public Post post() {
        return new Post.Collector(collector());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6713.java