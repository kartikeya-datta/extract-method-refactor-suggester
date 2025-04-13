error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6714.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6714.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6714.java
text:
```scala
public v@@oid postCollection() throws IOException {

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
package org.elasticsearch.search.facet.nested;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.FixedBitSet;
import org.elasticsearch.common.lucene.docset.ContextDocIdSet;
import org.elasticsearch.common.lucene.docset.DocIdSets;
import org.elasticsearch.common.lucene.search.FilteredCollector;
import org.elasticsearch.common.lucene.search.XCollector;
import org.elasticsearch.index.mapper.MapperService;
import org.elasticsearch.index.mapper.object.ObjectMapper;
import org.elasticsearch.index.search.nested.NonNestedDocsFilter;
import org.elasticsearch.search.SearchParseException;
import org.elasticsearch.search.facet.FacetExecutor;
import org.elasticsearch.search.facet.InternalFacet;
import org.elasticsearch.search.internal.SearchContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class NestedFacetExecutor extends FacetExecutor {

    private final FacetExecutor facetExecutor;
    private final Filter parentFilter;
    private final Filter childFilter;

    public NestedFacetExecutor(FacetExecutor facetExecutor, SearchContext context, String nestedPath) {
        this.facetExecutor = facetExecutor;
        MapperService.SmartNameObjectMapper mapper = context.smartNameObjectMapper(nestedPath);
        if (mapper == null) {
            throw new SearchParseException(context, "facet nested path [" + nestedPath + "] not found");
        }
        ObjectMapper objectMapper = mapper.mapper();
        if (objectMapper == null) {
            throw new SearchParseException(context, "facet nested path [" + nestedPath + "] not found");
        }
        if (!objectMapper.nested().isNested()) {
            throw new SearchParseException(context, "facet nested path [" + nestedPath + "] is not nested");
        }
        parentFilter = context.filterCache().cache(NonNestedDocsFilter.INSTANCE);
        childFilter = context.filterCache().cache(objectMapper.nestedTypeFilter());
    }

    @Override
    public InternalFacet buildFacet(String facetName) {
        return facetExecutor.buildFacet(facetName);
    }

    @Override
    public Collector collector() {
        XCollector collector = facetExecutor.collector();
        if (collector == null) {
            return null;
        }
        return new Collector(collector, parentFilter, childFilter);
    }

    @Override
    public Post post() {
        FacetExecutor.Post post = facetExecutor.post();
        if (post == null) {
            return null;
        }
        return new Post(post, parentFilter, childFilter);
    }

    public static class Post extends FacetExecutor.Post {

        private final FacetExecutor.Post post;
        private final Filter parentFilter;
        private final Filter childFilter;

        public Post(FacetExecutor.Post post, Filter parentFilter, Filter childFilter) {
            this.post = post;
            this.parentFilter = parentFilter;
            this.childFilter = childFilter;
        }

        public Post(Post post, Filter filter) {
            this.post = new FacetExecutor.Post.Filtered(post.post, filter);
            this.parentFilter = post.parentFilter;
            this.childFilter = post.childFilter;
        }

        @Override
        public void executePost(List<ContextDocIdSet> docSets) throws IOException {
            List<ContextDocIdSet> nestedEntries = new ArrayList<>(docSets.size());
            for (int i = 0; i < docSets.size(); i++) {
                ContextDocIdSet entry = docSets.get(i);
                AtomicReaderContext context = entry.context;
                // Can use null as acceptedDocs here, since only live doc ids are being pushed to collect method.
                DocIdSet docIdSet = parentFilter.getDocIdSet(context, null);
                if (DocIdSets.isEmpty(docIdSet)) {
                    continue;
                }
                // Im ES if parent is deleted, then also the children are deleted. Therefore acceptedDocs can also null here.
                Bits childDocs = DocIdSets.toSafeBits(context.reader(), childFilter.getDocIdSet(context, null));
                FixedBitSet parentDocs = (FixedBitSet) docIdSet;

                DocIdSetIterator iter = entry.docSet.iterator();
                int parentDoc = iter.nextDoc();
                if (parentDoc == DocIdSetIterator.NO_MORE_DOCS) {
                    continue;
                }
                if (parentDoc == 0) {
                    parentDoc = iter.nextDoc();
                }
                if (parentDoc == DocIdSetIterator.NO_MORE_DOCS) {
                    continue;
                }
                FixedBitSet childSet = new FixedBitSet(context.reader().maxDoc());
                do {
                    int prevParentDoc = parentDocs.prevSetBit(parentDoc - 1);
                    for (int childDocId = (parentDoc - 1); childDocId > prevParentDoc; childDocId--) {
                        if (childDocs.get(childDocId)) {
                            childSet.set(childDocId);
                        }
                    }
                } while ((parentDoc = iter.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS);
                nestedEntries.add(new ContextDocIdSet(entry.context, childSet));
            }
            post.executePost(nestedEntries);
        }
    }


    public static class Collector extends FacetExecutor.Collector {

        private final org.apache.lucene.search.Collector collector;
        private final Filter parentFilter;
        private final Filter childFilter;
        private Bits childDocs;
        private FixedBitSet parentDocs;

        // We can move
        public Collector(Collector collector, Filter filter) {
            this.collector = new FilteredCollector(collector.collector, filter);
            this.parentFilter = collector.parentFilter;
            this.childFilter = collector.childFilter;
        }

        public Collector(org.apache.lucene.search.Collector collector, Filter parentFilter, Filter childFilter) {
            this.collector = collector;
            this.parentFilter = parentFilter;
            this.childFilter = childFilter;
        }

        @Override
        public void postCollection() {
            if (collector instanceof XCollector) {
                ((XCollector) collector).postCollection();
            }
        }

        @Override
        public void setScorer(Scorer scorer) throws IOException {
            collector.setScorer(scorer);
        }

        @Override
        public void setNextReader(AtomicReaderContext context) throws IOException {
            collector.setNextReader(context);
            // Can use null as acceptedDocs here, since only live doc ids are being pushed to collect method.
            DocIdSet docIdSet = parentFilter.getDocIdSet(context, null);
            // Im ES if parent is deleted, then also the children are deleted. Therefore acceptedDocs can also null here.
            childDocs = DocIdSets.toSafeBits(context.reader(), childFilter.getDocIdSet(context, null));
            if (DocIdSets.isEmpty(docIdSet)) {
                parentDocs = null;
            } else {
                parentDocs = (FixedBitSet) docIdSet;
            }
        }

        @Override
        public boolean acceptsDocsOutOfOrder() {
            return collector.acceptsDocsOutOfOrder();
        }

        @Override
        public void collect(int parentDoc) throws IOException {
            if (parentDoc == 0 || parentDocs == null) {
                return;
            }
            int prevParentDoc = parentDocs.prevSetBit(parentDoc - 1);
            for (int i = (parentDoc - 1); i > prevParentDoc; i--) {
                if (childDocs.get(i)) {
                    collector.collect(i);
                }
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6714.java