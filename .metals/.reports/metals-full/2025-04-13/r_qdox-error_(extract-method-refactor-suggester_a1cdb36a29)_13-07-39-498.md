error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7409.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7409.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7409.java
text:
```scala
t@@his.docs = new ArrayList<>(size);

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

package org.elasticsearch.search.scan;

import com.google.common.collect.Maps;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.*;
import org.apache.lucene.util.Bits;
import org.elasticsearch.common.lucene.docset.AllDocIdSet;
import org.elasticsearch.common.lucene.search.XFilteredQuery;
import org.elasticsearch.search.internal.SearchContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * The scan context allows to optimize readers we already processed during scanning. We do that by keeping track
 * of the count per reader, and if we are done with it, we no longer process it by using a filter that returns
 * null docIdSet for this reader.
 */
public class ScanContext {

    private final Map<IndexReader, ReaderState> readerStates = Maps.newHashMap();

    public void clear() {
        readerStates.clear();
    }

    public TopDocs execute(SearchContext context) throws IOException {
        ScanCollector collector = new ScanCollector(readerStates, context.from(), context.size(), context.trackScores());
        Query query = new XFilteredQuery(context.query(), new ScanFilter(readerStates, collector));
        try {
            context.searcher().search(query, collector);
        } catch (ScanCollector.StopCollectingException e) {
            // all is well
        }
        return collector.topDocs();
    }

    static class ScanCollector extends Collector {

        private final Map<IndexReader, ReaderState> readerStates;

        private final int from;

        private final int to;

        private final ArrayList<ScoreDoc> docs;

        private final boolean trackScores;

        private Scorer scorer;

        private int docBase;

        private int counter;

        private IndexReader currentReader;
        private ReaderState readerState;

        ScanCollector(Map<IndexReader, ReaderState> readerStates, int from, int size, boolean trackScores) {
            this.readerStates = readerStates;
            this.from = from;
            this.to = from + size;
            this.trackScores = trackScores;
            this.docs = new ArrayList<ScoreDoc>(size);
        }

        void incCounter(int count) {
            this.counter += count;
        }

        public TopDocs topDocs() {
            return new TopDocs(docs.size(), docs.toArray(new ScoreDoc[docs.size()]), 0f);
        }

        @Override
        public void setScorer(Scorer scorer) throws IOException {
            this.scorer = scorer;
        }

        @Override
        public void collect(int doc) throws IOException {
            if (counter >= from) {
                docs.add(new ScoreDoc(docBase + doc, trackScores ? scorer.score() : 0f));
            }
            readerState.count++;
            counter++;
            if (counter >= to) {
                throw StopCollectingException;
            }
        }

        @Override
        public void setNextReader(AtomicReaderContext context) throws IOException {
            // if we have a reader state, and we haven't registered one already, register it
            // we need to check in readersState since even when the filter return null, setNextReader is still
            // called for that reader (before)
            if (currentReader != null && !readerStates.containsKey(currentReader)) {
                assert readerState != null;
                readerState.done = true;
                readerStates.put(currentReader, readerState);
            }
            this.currentReader = context.reader();
            this.docBase = context.docBase;
            this.readerState = new ReaderState();
        }

        @Override
        public boolean acceptsDocsOutOfOrder() {
            return true;
        }

        public static final RuntimeException StopCollectingException = new StopCollectingException();

        static class StopCollectingException extends RuntimeException {
            @Override
            public Throwable fillInStackTrace() {
                return null;
            }
        }
    }

    public static class ScanFilter extends Filter {

        private final Map<IndexReader, ReaderState> readerStates;

        private final ScanCollector scanCollector;

        public ScanFilter(Map<IndexReader, ReaderState> readerStates, ScanCollector scanCollector) {
            this.readerStates = readerStates;
            this.scanCollector = scanCollector;
        }

        @Override
        public DocIdSet getDocIdSet(AtomicReaderContext context, Bits acceptedDocs) throws IOException {
            ReaderState readerState = readerStates.get(context.reader());
            if (readerState != null && readerState.done) {
                scanCollector.incCounter(readerState.count);
                return null;
            }
            return new AllDocIdSet(context.reader().maxDoc());
        }
    }

    static class ReaderState {
        public int count;
        public boolean done;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7409.java