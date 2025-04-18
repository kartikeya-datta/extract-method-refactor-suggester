error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7394.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7394.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7394.java
text:
```scala
B@@oundedTreeSet<InternalStringTermsFacet.TermEntry> ordered = new BoundedTreeSet<>(comparatorType.comparator(), shardSize);

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

package org.elasticsearch.search.facet.terms.strings;

import com.carrotsearch.hppc.ObjectIntOpenHashMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.cache.recycler.CacheRecycler;
import org.elasticsearch.common.collect.BoundedTreeSet;
import org.elasticsearch.common.recycler.Recycler;
import org.elasticsearch.script.SearchScript;
import org.elasticsearch.search.facet.FacetExecutor;
import org.elasticsearch.search.facet.InternalFacet;
import org.elasticsearch.search.facet.terms.support.EntryPriorityQueue;
import org.elasticsearch.search.internal.SearchContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class ScriptTermsStringFieldFacetExecutor extends FacetExecutor {

    private final InternalStringTermsFacet.ComparatorType comparatorType;
    private final int size;
    private final int shardSize;
    private final SearchScript script;
    private final Matcher matcher;
    private final ImmutableSet<BytesRef> excluded;
    private final int numberOfShards;

    final Recycler.V<ObjectIntOpenHashMap<BytesRef>> facets;
    long missing;
    long total;

    public ScriptTermsStringFieldFacetExecutor(int size, int shardSize, InternalStringTermsFacet.ComparatorType comparatorType, SearchContext context,
                                               ImmutableSet<BytesRef> excluded, Pattern pattern, String scriptLang, String script, Map<String, Object> params,
                                               CacheRecycler cacheRecycler) {
        this.size = size;
        this.shardSize = shardSize;
        this.comparatorType = comparatorType;
        this.numberOfShards = context.numberOfShards();
        this.script = context.scriptService().search(context.lookup(), scriptLang, script, params);

        this.excluded = excluded;
        this.matcher = pattern != null ? pattern.matcher("") : null;

        this.facets = cacheRecycler.objectIntMap(-1);
    }

    @Override
    public Collector collector() {
        return new Collector(matcher, excluded, script, facets.v());
    }

    @Override
    public InternalFacet buildFacet(String facetName) {
        if (facets.v().isEmpty()) {
            facets.release();
            return new InternalStringTermsFacet(facetName, comparatorType, size, ImmutableList.<InternalStringTermsFacet.TermEntry>of(), missing, total);
        } else {
            final boolean[] states = facets.v().allocated;
            final Object[] keys = facets.v().keys;
            final int[] values = facets.v().values;
            if (shardSize < EntryPriorityQueue.LIMIT) {
                EntryPriorityQueue ordered = new EntryPriorityQueue(shardSize, comparatorType.comparator());
                for (int i = 0; i < states.length; i++) {
                    if (states[i]) {
                        BytesRef key = (BytesRef) keys[i];
                        ordered.insertWithOverflow(new InternalStringTermsFacet.TermEntry(key, values[i]));
                    }
                }
                InternalStringTermsFacet.TermEntry[] list = new InternalStringTermsFacet.TermEntry[ordered.size()];
                for (int i = ordered.size() - 1; i >= 0; i--) {
                    list[i] = ((InternalStringTermsFacet.TermEntry) ordered.pop());
                }
                facets.release();
                return new InternalStringTermsFacet(facetName, comparatorType, size, Arrays.asList(list), missing, total);
            } else {
                BoundedTreeSet<InternalStringTermsFacet.TermEntry> ordered = new BoundedTreeSet<InternalStringTermsFacet.TermEntry>(comparatorType.comparator(), shardSize);
                for (int i = 0; i < states.length; i++) {
                    if (states[i]) {
                        BytesRef key = (BytesRef) keys[i];
                        ordered.add(new InternalStringTermsFacet.TermEntry(key, values[i]));
                    }
                }
                facets.release();
                return new InternalStringTermsFacet(facetName, comparatorType, size, ordered, missing, total);
            }
        }
    }

    class Collector extends FacetExecutor.Collector {

        private final Matcher matcher;
        private final ImmutableSet<BytesRef> excluded;
        private final SearchScript script;
        private final ObjectIntOpenHashMap<BytesRef> facets;

        long missing;
        long total;

        Collector(Matcher matcher, ImmutableSet<BytesRef> excluded, SearchScript script, ObjectIntOpenHashMap<BytesRef> facets) {
            this.matcher = matcher;
            this.excluded = excluded;
            this.script = script;
            this.facets = facets;
        }

        @Override
        public void setScorer(Scorer scorer) throws IOException {
            script.setScorer(scorer);
        }

        @Override
        public void setNextReader(AtomicReaderContext context) throws IOException {
            script.setNextReader(context);
        }

        @Override
        public void collect(int doc) throws IOException {
            script.setNextDocId(doc);
            Object o = script.run();
            if (o == null) {
                missing++;
                return;
            }
            if (o instanceof Iterable) {
                boolean found = false;
                for (Object o1 : ((Iterable) o)) {
                    String value = o1.toString();
                    if (match(value)) {
                        found = true;
                        // LUCENE 4 UPGRADE: should be possible to convert directly to BR
                        facets.addTo(new BytesRef(value), 1);
                        total++;
                    }
                }
                if (!found) {
                    missing++;
                }
            } else if (o instanceof Object[]) {
                boolean found = false;
                for (Object o1 : ((Object[]) o)) {
                    String value = o1.toString();
                    if (match(value)) {
                        found = true;
                        // LUCENE 4 UPGRADE: should be possible to convert directly to BR
                        facets.addTo(new BytesRef(value), 1);
                        total++;
                    }
                }
                if (!found) {
                    missing++;
                }
            } else {
                String value = o.toString();
                if (match(value)) {
                    // LUCENE 4 UPGRADE: should be possible to convert directly to BR
                    facets.addTo(new BytesRef(value), 1);
                    total++;
                } else {
                    missing++;
                }
            }
        }

        @Override
        public void postCollection() {
            ScriptTermsStringFieldFacetExecutor.this.missing = missing;
            ScriptTermsStringFieldFacetExecutor.this.total = total;
        }

        private boolean match(String value) {
            if (excluded != null && excluded.contains(new BytesRef(value))) {
                return false;
            }
            if (matcher != null && !matcher.reset(value).matches()) {
                return false;
            }
            return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7394.java