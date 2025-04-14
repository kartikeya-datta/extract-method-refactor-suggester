error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/727.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/727.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/727.java
text:
```scala
r@@eturn true;

package org.apache.lucene.search;

/**
 * Copyright 2005 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.List;
import java.util.Iterator;
import java.io.IOException;

import org.apache.lucene.util.PriorityQueue;

/** A Scorer for OR like queries, counterpart of Lucene's <code>ConjunctionScorer</code>.
 * This Scorer implements {@link Scorer#skipTo(int)} and uses skipTo() on the given Scorers. 
 */
public class DisjunctionSumScorer extends Scorer {
  /** The number of subscorers. */ 
  private final int nrScorers;
  
  /** The subscorers. */
  protected final List subScorers;
  
  /** The minimum number of scorers that should match. */
  private final int minimumNrMatchers;
  
  /** The scorerQueue contains all subscorers ordered by their current doc(),
   * with the minimum at the top.
   * <br>The scorerQueue is initialized the first time next() or skipTo() is called.
   * <br>An exhausted scorer is immediately removed from the scorerQueue.
   * <br>If less than the minimumNrMatchers scorers
   * remain in the scorerQueue next() and skipTo() return false.
   * <p>
   * After each to call to next() or skipTo()
   * <code>currentSumScore</code> is the total score of the current matching doc,
   * <code>nrMatchers</code> is the number of matching scorers,
   * and all scorers are after the matching doc, or are exhausted.
   */
  private ScorerQueue scorerQueue = null;
  
  /** The document number of the current match. */
  private int currentDoc = -1;

  /** The number of subscorers that provide the current match. */
  protected int nrMatchers = -1;

  private float currentScore = Float.NaN;
  
  /** Construct a <code>DisjunctionScorer</code>.
   * @param subScorers A collection of at least two subscorers.
   * @param minimumNrMatchers The positive minimum number of subscorers that should
   * match to match this query.
   * <br>When <code>minimumNrMatchers</code> is bigger than
   * the number of <code>subScorers</code>,
   * no matches will be produced.
   * <br>When minimumNrMatchers equals the number of subScorers,
   * it more efficient to use <code>ConjunctionScorer</code>.
   */
  public DisjunctionSumScorer( List subScorers, int minimumNrMatchers) {
    super(null);
    
    nrScorers = subScorers.size();

    if (minimumNrMatchers <= 0) {
      throw new IllegalArgumentException("Minimum nr of matchers must be positive");
    }
    if (nrScorers <= 1) {
      throw new IllegalArgumentException("There must be at least 2 subScorers");
    }

    this.minimumNrMatchers = minimumNrMatchers;
    this.subScorers = subScorers;
  }
  
  /** Construct a <code>DisjunctionScorer</code>, using one as the minimum number
   * of matching subscorers.
   */
  public DisjunctionSumScorer(List subScorers) {
    this(subScorers, 1);
  }

  /** Called the first time next() or skipTo() is called to
   * initialize <code>scorerQueue</code>.
   */
  private void initScorerQueue() throws IOException {
    Iterator si = subScorers.iterator();
    scorerQueue = new ScorerQueue(nrScorers);
    while (si.hasNext()) {
      Scorer se = (Scorer) si.next();
      if (se.next()) { // doc() method will be used in scorerQueue.
        scorerQueue.insert(se);
      }
    }
  }

  /** A <code>PriorityQueue</code> that orders by {@link Scorer#doc()}. */
  private class ScorerQueue extends PriorityQueue {
    ScorerQueue(int size) {
      initialize(size);
    }

    protected boolean lessThan(Object o1, Object o2) {
      return ((Scorer)o1).doc() < ((Scorer)o2).doc();
    }
  }
  
  public boolean next() throws IOException {
    if (scorerQueue == null) {
      initScorerQueue();
    }
    if (scorerQueue.size() < minimumNrMatchers) {
      return false;
    } else {
      return advanceAfterCurrent();
    }
  }


  /** Advance all subscorers after the current document determined by the
   * top of the <code>scorerQueue</code>.
   * Repeat until at least the minimum number of subscorers match on the same
   * document and all subscorers are after that document or are exhausted.
   * <br>On entry the <code>scorerQueue</code> has at least <code>minimumNrMatchers</code>
   * available. At least the scorer with the minimum document number will be advanced.
   * @return true iff there is a match.
   * <br>In case there is a match, </code>currentDoc</code>, </code>currentSumScore</code>,
   * and </code>nrMatchers</code> describe the match.
   *
   * @todo Investigate whether it is possible to use skipTo() when
   * the minimum number of matchers is bigger than one, ie. try and use the
   * character of ConjunctionScorer for the minimum number of matchers.
   */
  protected boolean advanceAfterCurrent() throws IOException {
    do { // repeat until minimum nr of matchers
      Scorer top = (Scorer) scorerQueue.top();
      currentDoc = top.doc();
      currentScore = top.score();
      nrMatchers = 1;
      do { // Until all subscorers are after currentDoc
        if (top.next()) {
          scorerQueue.adjustTop();
        } else {
          scorerQueue.pop();
          if (scorerQueue.size() < (minimumNrMatchers - nrMatchers)) {
            // Not enough subscorers left for a match on this document,
            // and also no more chance of any further match.
            return false;
          }
          if (scorerQueue.size() == 0) {
            break; // nothing more to advance, check for last match.
          }
        }
        top = (Scorer) scorerQueue.top();
        if (top.doc() != currentDoc) {
          break; // All remaining subscorers are after currentDoc.
        } else {
          currentScore += top.score();
          nrMatchers++;
        }
      } while (true);
      
      if (nrMatchers >= minimumNrMatchers) {
        return true;
      } else if (scorerQueue.size() < minimumNrMatchers) {
        return false;
      }
    } while (true);
  }
  
  /** Returns the score of the current document matching the query.
   * Initially invalid, until {@link #next()} is called the first time.
   */
  public float score() throws IOException { return currentScore; }
   
  public int doc() { return currentDoc; }

  /** Returns the number of subscorers matching the current document.
   * Initially invalid, until {@link #next()} is called the first time.
   */
  public int nrMatchers() {
    return nrMatchers;
  }

  /** Skips to the first match beyond the current whose document number is
   * greater than or equal to a given target.
   * <br>When this method is used the {@link #explain(int)} method should not be used.
   * <br>The implementation uses the skipTo() method on the subscorers.
   * @param target The target document number.
   * @return true iff there is such a match.
   */
  public boolean skipTo(int target) throws IOException {
    if (scorerQueue == null) {
      initScorerQueue();
    }
    if (scorerQueue.size() < minimumNrMatchers) {
      return false;
    }
    if (target <= currentDoc) {
      target = currentDoc + 1;
    }
    do {
      Scorer top = (Scorer) scorerQueue.top();
      if (top.doc() >= target) {
        return advanceAfterCurrent();
      } else if (top.skipTo(target)) {
        scorerQueue.adjustTop();
      } else {
        scorerQueue.pop();
        if (scorerQueue.size() < minimumNrMatchers) {
          return false;
        }
      }
    } while (true);
  }

 /** Gives and explanation for the score of a given document.
  * @todo Show the resulting score. See BooleanScorer.explain() on how to do this.
  */
  public Explanation explain(int doc) throws IOException {
    Explanation res = new Explanation();
    res.setDescription("At least " + minimumNrMatchers + " of");
    Iterator ssi = subScorers.iterator();
    while (ssi.hasNext()) {
      res.addDetail( ((Scorer) ssi.next()).explain(doc));
    }
    return res;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/727.java