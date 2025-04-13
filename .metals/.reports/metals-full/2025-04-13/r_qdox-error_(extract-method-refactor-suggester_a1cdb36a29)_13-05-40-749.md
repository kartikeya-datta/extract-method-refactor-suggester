error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/472.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/472.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/472.java
text:
```scala
D@@ocsAndFreqs[] docsAndFreqs) {

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
import java.util.Comparator;

import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.search.similarities.Similarity.ExactSimScorer;
import org.apache.lucene.util.ArrayUtil;

/** Scorer for conjunctions, sets of terms, all of which are required. */
class ConjunctionTermScorer extends Scorer {
  protected final float coord;
  protected int lastDoc = -1;
  protected final DocsAndFreqs[] docsAndFreqs;
  private final DocsAndFreqs lead;

  ConjunctionTermScorer(Weight weight, float coord,
      DocsAndFreqs[] docsAndFreqs) throws IOException {
    super(weight);
    this.coord = coord;
    this.docsAndFreqs = docsAndFreqs;
    // Sort the array the first time to allow the least frequent DocsEnum to
    // lead the matching.
    ArrayUtil.mergeSort(docsAndFreqs, new Comparator<DocsAndFreqs>() {
      public int compare(DocsAndFreqs o1, DocsAndFreqs o2) {
        return o1.docFreq - o2.docFreq;
      }
    });

    lead = docsAndFreqs[0]; // least frequent DocsEnum leads the intersection
  }

  private int doNext(int doc) throws IOException {
    do {
      if (lead.doc == DocIdSetIterator.NO_MORE_DOCS) {
        return NO_MORE_DOCS;
      }
      advanceHead: do {
        for (int i = 1; i < docsAndFreqs.length; i++) {
          if (docsAndFreqs[i].doc < doc) {
            docsAndFreqs[i].doc = docsAndFreqs[i].docs.advance(doc);
          }
          if (docsAndFreqs[i].doc > doc) {
            // DocsEnum beyond the current doc - break and advance lead
            break advanceHead;
          }
        }
        // success - all DocsEnums are on the same doc
        return doc;
      } while (true);
      // advance head for next iteration
      doc = lead.doc = lead.docs.nextDoc();  
    } while (true);
  }

  @Override
  public int advance(int target) throws IOException {
    lead.doc = lead.docs.advance(target);
    return lastDoc = doNext(lead.doc);
  }

  @Override
  public int docID() {
    return lastDoc;
  }

  @Override
  public int nextDoc() throws IOException {
    lead.doc = lead.docs.nextDoc();
    return lastDoc = doNext(lead.doc);
  }

  @Override
  public float score() throws IOException {
    float sum = 0.0f;
    for (DocsAndFreqs docs : docsAndFreqs) {
      sum += docs.docScorer.score(lastDoc, docs.docs.freq());
    }
    return sum * coord;
  }

  static final class DocsAndFreqs {
    final DocsEnum docsAndFreqs;
    final DocsEnum docs;
    final int docFreq;
    final ExactSimScorer docScorer;
    int doc = -1;

    DocsAndFreqs(DocsEnum docsAndFreqs, DocsEnum docs, int docFreq, ExactSimScorer docScorer) {
      this.docsAndFreqs = docsAndFreqs;
      this.docs = docs;
      this.docFreq = docFreq;
      this.docScorer = docScorer;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/472.java