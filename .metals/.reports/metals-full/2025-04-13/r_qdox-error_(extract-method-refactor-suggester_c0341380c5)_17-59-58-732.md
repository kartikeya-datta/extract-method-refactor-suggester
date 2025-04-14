error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/623.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/623.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/623.java
text:
```scala
P@@hrasePositions pp = new PhrasePositions(postings[i].postings, postings[i].position, i);

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

/** Expert: Scoring functionality for phrase queries.
 * <br>A document is considered matching if it contains the phrase-query terms  
 * at "valid" positions. What "valid positions" are
 * depends on the type of the phrase query: for an exact phrase query terms are required 
 * to appear in adjacent locations, while for a sloppy phrase query some distance between 
 * the terms is allowed. The abstract method {@link #phraseFreq()} of extending classes
 * is invoked for each document containing all the phrase query terms, in order to 
 * compute the frequency of the phrase query in that document. A non zero frequency
 * means a match. 
 */
abstract class PhraseScorer extends Scorer {
  protected byte[] norms;
  protected float value;

  private boolean firstTime = true;
  private boolean more = true;
  protected PhraseQueue pq;
  protected PhrasePositions first, last;

  private float freq; //phrase frequency in current doc as computed by phraseFreq().

  protected final Similarity similarity;

  PhraseScorer(Weight weight, PhraseQuery.PostingsAndFreq[] postings,
      Similarity similarity, byte[] norms) {
    super(weight);
    this.similarity = similarity;
    this.norms = norms;
    this.value = weight.getValue();

    // convert tps to a list of phrase positions.
    // note: phrase-position differs from term-position in that its position
    // reflects the phrase offset: pp.pos = tp.pos - offset.
    // this allows to easily identify a matching (exact) phrase 
    // when all PhrasePositions have exactly the same position.
    for (int i = 0; i < postings.length; i++) {
      PhrasePositions pp = new PhrasePositions(postings[i].postings, postings[i].position);
      if (last != null) {			  // add next to end of list
        last.next = pp;
      } else {
        first = pp;
      }
      last = pp;
    }

    pq = new PhraseQueue(postings.length);             // construct empty pq
    first.doc = -1;
  }

  @Override
  public int docID() { return first.doc; }

  @Override
  public int nextDoc() throws IOException {
    if (firstTime) {
      init();
      firstTime = false;
    } else if (more) {
      more = last.next();                         // trigger further scanning
    }
    if (!doNext()) {
      first.doc = NO_MORE_DOCS;
    }
    return first.doc;
  }
  
  // next without initial increment
  private boolean doNext() throws IOException {
    while (more) {
      while (more && first.doc < last.doc) {      // find doc w/ all the terms
        more = first.skipTo(last.doc);            // skip first upto last
        firstToLast();                            // and move it to the end
      }

      if (more) {
        // found a doc with all of the terms
        freq = phraseFreq();                      // check for phrase
        if (freq == 0.0f)                         // no match
          more = last.next();                     // trigger further scanning
        else
          return true;                            // found a match
      }
    }
    return false;                                 // no more matches
  }

  @Override
  public float score() throws IOException {
    //System.out.println("scoring " + first.doc);
    float raw = similarity.tf(freq) * value; // raw score
    return norms == null ? raw : raw * similarity.decodeNormValue(norms[first.doc]); // normalize
  }

  @Override
  public int advance(int target) throws IOException {
    firstTime = false;
    for (PhrasePositions pp = first; more && pp != null; pp = pp.next) {
      more = pp.skipTo(target);
    }
    if (more) {
      sort();                                     // re-sort
    }
    if (!doNext()) {
      first.doc = NO_MORE_DOCS;
    }
    return first.doc;
  }
  
  /**
   * phrase frequency in current doc as computed by phraseFreq().
   */
  @Override
  public final float freq() {
    return freq;
  }

  /**
   * For a document containing all the phrase query terms, compute the
   * frequency of the phrase in that document. 
   * A non zero frequency means a match.
   * <br>Note, that containing all phrase terms does not guarantee a match - they have to be found in matching locations.  
   * @return frequency of the phrase in current doc, 0 if not found. 
   */
  protected abstract float phraseFreq() throws IOException;

  private void init() throws IOException {
    for (PhrasePositions pp = first; more && pp != null; pp = pp.next) {
      more = pp.next();
    }
    if (more) {
      sort();
    }
  }
  
  private void sort() {
    pq.clear();
    for (PhrasePositions pp = first; pp != null; pp = pp.next) {
      pq.add(pp);
    }
    pqToList();
  }

  protected final void pqToList() {
    last = first = null;
    while (pq.top() != null) {
      PhrasePositions pp = pq.pop();
      if (last != null) {			  // add next to end of list
        last.next = pp;
      } else
        first = pp;
      last = pp;
      pp.next = null;
    }
  }

  protected final void firstToLast() {
    last.next = first;			  // move first to end of list
    last = first;
    first = first.next;
    last.next = null;
  }

  @Override
  public String toString() { return "scorer(" + weight + ")"; }
 
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/623.java