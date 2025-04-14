error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/433.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/433.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/433.java
text:
```scala
private M@@ultiPhraseQuery randomPhraseQuery(long seed) {

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

import java.util.Random;

import org.apache.lucene.index.Term;
import org.apache.lucene.util._TestUtil;

/**
 * random sloppy phrase query tests
 */
public class TestSloppyPhraseQuery2 extends SearchEquivalenceTestBase {
  /** "A B"~N ⊆ "A B"~N+1 */
  public void testIncreasingSloppiness() throws Exception {
    Term t1 = randomTerm();
    Term t2 = randomTerm();
    PhraseQuery q1 = new PhraseQuery();
    q1.add(t1);
    q1.add(t2);
    PhraseQuery q2 = new PhraseQuery();
    q2.add(t1);
    q2.add(t2);
    for (int i = 0; i < 10; i++) {
      q1.setSlop(i);
      q2.setSlop(i+1);
      assertSubsetOf(q1, q2);
    }
  }
  
  /** same as the above with posincr */
  public void testIncreasingSloppinessWithHoles() throws Exception {
    Term t1 = randomTerm();
    Term t2 = randomTerm();
    PhraseQuery q1 = new PhraseQuery();
    q1.add(t1);
    q1.add(t2, 2);
    PhraseQuery q2 = new PhraseQuery();
    q2.add(t1);
    q2.add(t2, 2);
    for (int i = 0; i < 10; i++) {
      q1.setSlop(i);
      q2.setSlop(i+1);
      assertSubsetOf(q1, q2);
    }
  }
  
  /** "A B C"~N ⊆ "A B C"~N+1 */
  public void testIncreasingSloppiness3() throws Exception {
    Term t1 = randomTerm();
    Term t2 = randomTerm();
    Term t3 = randomTerm();
    PhraseQuery q1 = new PhraseQuery();
    q1.add(t1);
    q1.add(t2);
    q1.add(t3);
    PhraseQuery q2 = new PhraseQuery();
    q2.add(t1);
    q2.add(t2);
    q2.add(t3);
    for (int i = 0; i < 10; i++) {
      q1.setSlop(i);
      q2.setSlop(i+1);
      assertSubsetOf(q1, q2);
    }
  }
  
  /** same as the above with posincr */
  public void testIncreasingSloppiness3WithHoles() throws Exception {
    Term t1 = randomTerm();
    Term t2 = randomTerm();
    Term t3 = randomTerm();
    int pos1 = 1 + random().nextInt(3);
    int pos2 = pos1 + 1 + random().nextInt(3);
    PhraseQuery q1 = new PhraseQuery();
    q1.add(t1);
    q1.add(t2, pos1);
    q1.add(t3, pos2);
    PhraseQuery q2 = new PhraseQuery();
    q2.add(t1);
    q2.add(t2, pos1);
    q2.add(t3, pos2);
    for (int i = 0; i < 10; i++) {
      q1.setSlop(i);
      q2.setSlop(i+1);
      assertSubsetOf(q1, q2);
    }
  }
  
  /** "A A"~N ⊆ "A A"~N+1 */
  public void testRepetitiveIncreasingSloppiness() throws Exception {
    Term t = randomTerm();
    PhraseQuery q1 = new PhraseQuery();
    q1.add(t);
    q1.add(t);
    PhraseQuery q2 = new PhraseQuery();
    q2.add(t);
    q2.add(t);
    for (int i = 0; i < 10; i++) {
      q1.setSlop(i);
      q2.setSlop(i+1);
      assertSubsetOf(q1, q2);
    }
  }
  
  /** same as the above with posincr */
  public void testRepetitiveIncreasingSloppinessWithHoles() throws Exception {
    Term t = randomTerm();
    PhraseQuery q1 = new PhraseQuery();
    q1.add(t);
    q1.add(t, 2);
    PhraseQuery q2 = new PhraseQuery();
    q2.add(t);
    q2.add(t, 2);
    for (int i = 0; i < 10; i++) {
      q1.setSlop(i);
      q2.setSlop(i+1);
      assertSubsetOf(q1, q2);
    }
  }
  
  /** "A A A"~N ⊆ "A A A"~N+1 */
  public void testRepetitiveIncreasingSloppiness3() throws Exception {
    Term t = randomTerm();
    PhraseQuery q1 = new PhraseQuery();
    q1.add(t);
    q1.add(t);
    q1.add(t);
    PhraseQuery q2 = new PhraseQuery();
    q2.add(t);
    q2.add(t);
    q2.add(t);
    for (int i = 0; i < 10; i++) {
      q1.setSlop(i);
      q2.setSlop(i+1);
      assertSubsetOf(q1, q2);
    }
  }
  
  /** same as the above with posincr */
  public void testRepetitiveIncreasingSloppiness3WithHoles() throws Exception {
    Term t = randomTerm();
    int pos1 = 1 + random().nextInt(3);
    int pos2 = pos1 + 1 + random().nextInt(3);
    PhraseQuery q1 = new PhraseQuery();
    q1.add(t);
    q1.add(t, pos1);
    q1.add(t, pos2);
    PhraseQuery q2 = new PhraseQuery();
    q2.add(t);
    q2.add(t, pos1);
    q2.add(t, pos2);
    for (int i = 0; i < 10; i++) {
      q1.setSlop(i);
      q2.setSlop(i+1);
      assertSubsetOf(q1, q2);
    }
  }
  
  /** MultiPhraseQuery~N ⊆ MultiPhraseQuery~N+1 */
  public void testRandomIncreasingSloppiness() throws Exception {
    long seed = random().nextLong();
    MultiPhraseQuery q1 = randomPhraseQuery(seed);
    MultiPhraseQuery q2 = randomPhraseQuery(seed);
    for (int i = 0; i < 10; i++) {
      q1.setSlop(i);
      q2.setSlop(i+1);
      assertSubsetOf(q1, q2);
    }
  }
  
  private MultiPhraseQuery randomPhraseQuery(long seed) throws Exception {
    Random random = new Random(seed);
    int length = _TestUtil.nextInt(random, 2, 5);
    MultiPhraseQuery pq = new MultiPhraseQuery();
    int position = 0;
    for (int i = 0; i < length; i++) {
      int depth = _TestUtil.nextInt(random, 1, 3);
      Term terms[] = new Term[depth];
      for (int j = 0; j < depth; j++) {
        terms[j] = new Term("field", "" + (char) _TestUtil.nextInt(random, 'a', 'z'));
      }
      pq.add(terms, position);
      position += _TestUtil.nextInt(random, 1, 3);
    }
    return pq;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/433.java