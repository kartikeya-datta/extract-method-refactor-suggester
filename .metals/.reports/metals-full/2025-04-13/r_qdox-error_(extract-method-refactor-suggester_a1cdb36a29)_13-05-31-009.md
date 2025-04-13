error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3111.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3111.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3111.java
text:
```scala
w@@riter.shutdown();

package org.apache.lucene.search;

/*
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

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;

public class TestScoreCachingWrappingScorer extends LuceneTestCase {

  private static final class SimpleScorer extends Scorer {
    private int idx = 0;
    private int doc = -1;
    
    public SimpleScorer(Weight weight) {
      super(weight);
    }
    
    @Override public float score() {
      // advance idx on purpose, so that consecutive calls to score will get
      // different results. This is to emulate computation of a score. If
      // ScoreCachingWrappingScorer is used, this should not be called more than
      // once per document.
      return idx == scores.length ? Float.NaN : scores[idx++];
    }
    
    @Override public int freq() throws IOException {
      return 1;
    }

    @Override public int docID() { return doc; }

    @Override public int nextDoc() {
      return ++doc < scores.length ? doc : NO_MORE_DOCS;
    }
    
    @Override public int advance(int target) {
      doc = target;
      return doc < scores.length ? doc : NO_MORE_DOCS;
    }
    
    @Override
    public long cost() {
      return scores.length;
    }
  }
  
  private static final class ScoreCachingCollector extends SimpleCollector {

    private int idx = 0;
    private Scorer scorer;
    float[] mscores;
    
    public ScoreCachingCollector(int numToCollect) {
      mscores = new float[numToCollect];
    }
    
    @Override public void collect(int doc) throws IOException {
      // just a sanity check to avoid IOOB.
      if (idx == mscores.length) {
        return; 
      }
      
      // just call score() a couple of times and record the score.
      mscores[idx] = scorer.score();
      mscores[idx] = scorer.score();
      mscores[idx] = scorer.score();
      ++idx;
    }

    @Override public void setScorer(Scorer scorer) {
      this.scorer = new ScoreCachingWrappingScorer(scorer);
    }
    
    @Override public boolean acceptsDocsOutOfOrder() {
      return true;
    }

  }

  private static final float[] scores = new float[] { 0.7767749f, 1.7839992f,
      8.9925785f, 7.9608946f, 0.07948637f, 2.6356435f, 7.4950366f, 7.1490803f,
      8.108544f, 4.961808f, 2.2423935f, 7.285586f, 4.6699767f };
  
  public void testGetScores() throws Exception {
    Directory directory = newDirectory();
    RandomIndexWriter writer = new RandomIndexWriter(random(), directory);
    writer.commit();
    IndexReader ir = writer.getReader();
    writer.close();
    IndexSearcher searcher = newSearcher(ir);
    Weight fake = new TermQuery(new Term("fake", "weight")).createWeight(searcher);
    Scorer s = new SimpleScorer(fake);
    ScoreCachingCollector scc = new ScoreCachingCollector(scores.length);
    scc.setScorer(s);
    
    // We need to iterate on the scorer so that its doc() advances.
    int doc;
    while ((doc = s.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
      scc.collect(doc);
    }
    
    for (int i = 0; i < scores.length; i++) {
      assertEquals(scores[i], scc.mscores[i], 0f);
    }
    ir.close();
    directory.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3111.java