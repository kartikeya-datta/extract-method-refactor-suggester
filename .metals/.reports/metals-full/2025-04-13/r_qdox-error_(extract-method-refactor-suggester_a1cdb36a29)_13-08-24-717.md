error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3116.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3116.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3116.java
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
import java.util.concurrent.CountDownLatch;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.SingleTermsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.Rethrow;
import org.apache.lucene.util.TestUtil;
import org.apache.lucene.util.automaton.Automaton;
import org.apache.lucene.util.automaton.AutomatonTestUtil;
import org.apache.lucene.util.automaton.BasicAutomata;
import org.apache.lucene.util.automaton.BasicOperations;

public class TestAutomatonQuery extends LuceneTestCase {
  private Directory directory;
  private IndexReader reader;
  private IndexSearcher searcher;

  private final String FN = "field";
  
  @Override
  public void setUp() throws Exception {
    super.setUp();
    directory = newDirectory();
    RandomIndexWriter writer = new RandomIndexWriter(random(), directory);
    Document doc = new Document();
    Field titleField = newTextField("title", "some title", Field.Store.NO);
    Field field = newTextField(FN, "this is document one 2345", Field.Store.NO);
    Field footerField = newTextField("footer", "a footer", Field.Store.NO);
    doc.add(titleField);
    doc.add(field);
    doc.add(footerField);
    writer.addDocument(doc);
    field.setStringValue("some text from doc two a short piece 5678.91");
    writer.addDocument(doc);
    field.setStringValue("doc three has some different stuff"
        + " with numbers 1234 5678.9 and letter b");
    writer.addDocument(doc);
    reader = writer.getReader();
    searcher = newSearcher(reader);
    writer.close();
  }
  
  @Override
  public void tearDown() throws Exception {
    reader.close();
    directory.close();
    super.tearDown();
  }
  
  private Term newTerm(String value) {
    return new Term(FN, value);
  }
  
  private int automatonQueryNrHits(AutomatonQuery query) throws IOException {
    if (VERBOSE) {
      System.out.println("TEST: run aq=" + query);
    }
    return searcher.search(query, 5).totalHits;
  }
  
  private void assertAutomatonHits(int expected, Automaton automaton)
      throws IOException {
    AutomatonQuery query = new AutomatonQuery(newTerm("bogus"), automaton);
    
    query.setRewriteMethod(MultiTermQuery.SCORING_BOOLEAN_QUERY_REWRITE);
    assertEquals(expected, automatonQueryNrHits(query));
    
    query.setRewriteMethod(MultiTermQuery.CONSTANT_SCORE_FILTER_REWRITE);
    assertEquals(expected, automatonQueryNrHits(query));
    
    query.setRewriteMethod(MultiTermQuery.CONSTANT_SCORE_BOOLEAN_QUERY_REWRITE);
    assertEquals(expected, automatonQueryNrHits(query));
    
    query.setRewriteMethod(MultiTermQuery.CONSTANT_SCORE_AUTO_REWRITE_DEFAULT);
    assertEquals(expected, automatonQueryNrHits(query));
  }
  
  /**
   * Test some very simple automata.
   */
  public void testBasicAutomata() throws IOException {
    assertAutomatonHits(0, BasicAutomata.makeEmpty());
    assertAutomatonHits(0, BasicAutomata.makeEmptyString());
    assertAutomatonHits(2, BasicAutomata.makeAnyChar());
    assertAutomatonHits(3, BasicAutomata.makeAnyString());
    assertAutomatonHits(2, BasicAutomata.makeString("doc"));
    assertAutomatonHits(1, BasicAutomata.makeChar('a'));
    assertAutomatonHits(2, BasicAutomata.makeCharRange('a', 'b'));
    assertAutomatonHits(2, BasicAutomata.makeInterval(1233, 2346, 0));
    assertAutomatonHits(1, BasicAutomata.makeInterval(0, 2000, 0));
    assertAutomatonHits(2, BasicOperations.union(BasicAutomata.makeChar('a'),
        BasicAutomata.makeChar('b')));
    assertAutomatonHits(0, BasicOperations.intersection(BasicAutomata
        .makeChar('a'), BasicAutomata.makeChar('b')));
    assertAutomatonHits(1, BasicOperations.minus(BasicAutomata.makeCharRange('a', 'b'), 
        BasicAutomata.makeChar('a')));
  }
  
  /**
   * Test that a nondeterministic automaton works correctly. (It should will be
   * determinized)
   */
  public void testNFA() throws IOException {
    // accept this or three, the union is an NFA (two transitions for 't' from
    // initial state)
    Automaton nfa = BasicOperations.union(BasicAutomata.makeString("this"),
        BasicAutomata.makeString("three"));
    assertAutomatonHits(2, nfa);
  }
  
  public void testEquals() {
    AutomatonQuery a1 = new AutomatonQuery(newTerm("foobar"), BasicAutomata
        .makeString("foobar"));
    // reference to a1
    AutomatonQuery a2 = a1;
    // same as a1 (accepts the same language, same term)
    AutomatonQuery a3 = new AutomatonQuery(newTerm("foobar"), BasicOperations
        .concatenate(BasicAutomata.makeString("foo"), BasicAutomata
            .makeString("bar")));
    // different than a1 (same term, but different language)
    AutomatonQuery a4 = new AutomatonQuery(newTerm("foobar"), BasicAutomata
        .makeString("different"));
    // different than a1 (different term, same language)
    AutomatonQuery a5 = new AutomatonQuery(newTerm("blah"), BasicAutomata
        .makeString("foobar"));
    
    assertEquals(a1.hashCode(), a2.hashCode());
    assertEquals(a1, a2);
    
    assertEquals(a1.hashCode(), a3.hashCode());
    assertEquals(a1, a3);
  
    // different class
    AutomatonQuery w1 = new WildcardQuery(newTerm("foobar"));
    // different class
    AutomatonQuery w2 = new RegexpQuery(newTerm("foobar"));
    
    assertFalse(a1.equals(w1));
    assertFalse(a1.equals(w2));
    assertFalse(w1.equals(w2));
    assertFalse(a1.equals(a4));
    assertFalse(a1.equals(a5));
    assertFalse(a1.equals(null));
  }
  
  /**
   * Test that rewriting to a single term works as expected, preserves
   * MultiTermQuery semantics.
   */
  public void testRewriteSingleTerm() throws IOException {
    AutomatonQuery aq = new AutomatonQuery(newTerm("bogus"), BasicAutomata
        .makeString("piece"));
    Terms terms = MultiFields.getTerms(searcher.getIndexReader(), FN);
    assertTrue(aq.getTermsEnum(terms) instanceof SingleTermsEnum);
    assertEquals(1, automatonQueryNrHits(aq));
  }
  
  /**
   * Test that rewriting to a prefix query works as expected, preserves
   * MultiTermQuery semantics.
   */
  public void testRewritePrefix() throws IOException {
    Automaton pfx = BasicAutomata.makeString("do");
    pfx.expandSingleton(); // expand singleton representation for testing
    Automaton prefixAutomaton = BasicOperations.concatenate(pfx, BasicAutomata
        .makeAnyString());
    AutomatonQuery aq = new AutomatonQuery(newTerm("bogus"), prefixAutomaton);
    Terms terms = MultiFields.getTerms(searcher.getIndexReader(), FN);
    assertTrue(aq.getTermsEnum(terms) instanceof PrefixTermsEnum);
    assertEquals(3, automatonQueryNrHits(aq));
  }
  
  /**
   * Test handling of the empty language
   */
  public void testEmptyOptimization() throws IOException {
    AutomatonQuery aq = new AutomatonQuery(newTerm("bogus"), BasicAutomata
        .makeEmpty());
    // not yet available: assertTrue(aq.getEnum(searcher.getIndexReader())
    // instanceof EmptyTermEnum);
    Terms terms = MultiFields.getTerms(searcher.getIndexReader(), FN);
    assertSame(TermsEnum.EMPTY, aq.getTermsEnum(terms));
    assertEquals(0, automatonQueryNrHits(aq));
  }
  
  public void testHashCodeWithThreads() throws Exception {
    final AutomatonQuery queries[] = new AutomatonQuery[1000];
    for (int i = 0; i < queries.length; i++) {
      queries[i] = new AutomatonQuery(new Term("bogus", "bogus"), AutomatonTestUtil.randomAutomaton(random()));
    }
    final CountDownLatch startingGun = new CountDownLatch(1);
    int numThreads = TestUtil.nextInt(random(), 2, 5);
    Thread[] threads = new Thread[numThreads];
    for (int threadID = 0; threadID < numThreads; threadID++) {
      Thread thread = new Thread() {
          @Override
          public void run() {
            try {
              startingGun.await();
              for (int i = 0; i < queries.length; i++) {
                queries[i].hashCode();
              }
            } catch (Exception e) {
              Rethrow.rethrow(e);
            }
          }
        };
      threads[threadID] = thread;
      thread.start();
    }
    startingGun.countDown();
    for (Thread thread : threads) {
      thread.join();
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3116.java