error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1851.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1851.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1851.java
text:
```scala
public b@@oolean score(Collector collector, int max, int firstDocID)

package org.apache.lucene.search;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexReader.ReaderContext;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.util.PriorityQueue;

/**
 * Holds all implementations of classes in the o.a.l.search package as a
 * back-compatibility test. It does not run any tests per-se, however if 
 * someone adds a method to an interface or abstract method to an abstract
 * class, one of the implementations here will fail to compile and so we know
 * back-compat policy was violated.
 */
final class JustCompileSearch {

  private static final String UNSUPPORTED_MSG = "unsupported: used for back-compat testing only !";

  static final class JustCompileCollector extends Collector {

    @Override
    public void collect(int doc) throws IOException {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    public void setNextReader(IndexReader reader, int docBase)
        throws IOException {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    public void setScorer(Scorer scorer) throws IOException {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }
    
    @Override
    public boolean acceptsDocsOutOfOrder() {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

  }
  
  static final class JustCompileDocIdSet extends DocIdSet {

    @Override
    public DocIdSetIterator iterator() throws IOException {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }
    
  }

  static final class JustCompileDocIdSetIterator extends DocIdSetIterator {

    @Override
    public int docID() {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    public int nextDoc() throws IOException {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }
    
    @Override
    public int advance(int target) throws IOException {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }
  }
  
  static final class JustCompileExtendedFieldCacheLongParser implements FieldCache.LongParser {

    public long parseLong(BytesRef string) {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }
    
  }
  
  static final class JustCompileExtendedFieldCacheDoubleParser implements FieldCache.DoubleParser {
    
    public double parseDouble(BytesRef term) {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }
    
  }

  static final class JustCompileFieldComparator extends FieldComparator {

    @Override
    public int compare(int slot1, int slot2) {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    public int compareBottom(int doc) throws IOException {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    public void copy(int slot, int doc) throws IOException {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    public void setBottom(int slot) {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    public FieldComparator setNextReader(IndexReader reader, int docBase)
        throws IOException {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    public Comparable<?> value(int slot) {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }
    
  }

  static final class JustCompileFieldComparatorSource extends FieldComparatorSource {

    @Override
    public FieldComparator newComparator(String fieldname, int numHits,
        int sortPos, boolean reversed) throws IOException {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }
    
  }

  static final class JustCompileFilter extends Filter {
    // Filter is just an abstract class with no abstract methods. However it is
    // still added here in case someone will add abstract methods in the future.
    
    @Override
    public DocIdSet getDocIdSet(ReaderContext context) throws IOException {
      return null;
    }
  }

  static final class JustCompileFilteredDocIdSet extends FilteredDocIdSet {

    public JustCompileFilteredDocIdSet(DocIdSet innerSet) {
      super(innerSet);
    }

    @Override
    protected boolean match(int docid) {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }
    
  }

  static final class JustCompileFilteredDocIdSetIterator extends FilteredDocIdSetIterator {

    public JustCompileFilteredDocIdSetIterator(DocIdSetIterator innerIter) {
      super(innerIter);
    }

    @Override
    protected boolean match(int doc) {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }
    
  }

  static final class JustCompilePhraseScorer extends PhraseScorer {

    JustCompilePhraseScorer(Weight weight, PhraseQuery.PostingsAndFreq[] postings,
        Similarity similarity, byte[] norms) {
      super(weight, postings, similarity, norms);
    }

    @Override
    protected float phraseFreq() throws IOException {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }
    
  }

  static final class JustCompileQuery extends Query {

    @Override
    public String toString(String field) {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }
    
  }
  
  static final class JustCompileScorer extends Scorer {

    protected JustCompileScorer(Similarity similarity) {
      super(similarity);
    }

    @Override
    protected boolean score(Collector collector, int max, int firstDocID)
        throws IOException {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }
    
    @Override
    public float score() throws IOException {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    public int docID() {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    public int nextDoc() throws IOException {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }
    
    @Override
    public int advance(int target) throws IOException {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }
  }
  
  static final class JustCompileSimilarity extends Similarity {

    @Override
    public float coord(int overlap, int maxOverlap) {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    public float idf(int docFreq, int numDocs) {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    public float computeNorm(String fieldName, FieldInvertState state) {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    public float queryNorm(float sumOfSquaredWeights) {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    public float sloppyFreq(int distance) {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    public float tf(float freq) {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }
    
  }

  static final class JustCompileSpanFilter extends SpanFilter {

    @Override
    public SpanFilterResult bitSpans(IndexReader reader) throws IOException {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }
    
    @Override
    public DocIdSet getDocIdSet(ReaderContext context) throws IOException {
      return null;
    }    
  }

  static final class JustCompileTopDocsCollector extends TopDocsCollector<ScoreDoc> {

    protected JustCompileTopDocsCollector(PriorityQueue<ScoreDoc> pq) {
      super(pq);
    }

    @Override
    public void collect(int doc) throws IOException {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    public void setNextReader(IndexReader reader, int docBase)
        throws IOException {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    public void setScorer(Scorer scorer) throws IOException {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }
    
    @Override
    public boolean acceptsDocsOutOfOrder() {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    public TopDocs topDocs() {
        throw new UnsupportedOperationException( UNSUPPORTED_MSG );
    }

    @Override
    public TopDocs topDocs( int start ) {
        throw new UnsupportedOperationException( UNSUPPORTED_MSG );
    }

    @Override
    public TopDocs topDocs( int start, int end ) {
        throw new UnsupportedOperationException( UNSUPPORTED_MSG );
    }
    
  }

  static final class JustCompileWeight extends Weight {

    @Override
    public Explanation explain(ReaderContext context, int doc) throws IOException {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    public Query getQuery() {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    public float getValue() {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    public void normalize(float norm) {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    public float sumOfSquaredWeights() throws IOException {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    public Scorer scorer(ReaderContext context, boolean scoreDocsInOrder, boolean topScorer)
        throws IOException {
      throw new UnsupportedOperationException(UNSUPPORTED_MSG);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1851.java