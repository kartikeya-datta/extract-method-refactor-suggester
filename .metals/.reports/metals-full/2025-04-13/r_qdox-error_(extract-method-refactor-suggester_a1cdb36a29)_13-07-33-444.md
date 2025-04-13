error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3206.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3206.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3206.java
text:
```scala
i@@w.shutdown();

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

import java.util.BitSet;
import java.util.Random;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.TestUtil;
import org.apache.lucene.util.automaton.BasicAutomata;
import org.apache.lucene.util.automaton.CharacterRunAutomaton;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Simple base class for checking search equivalence.
 * Extend it, and write tests that create {@link #randomTerm()}s
 * (all terms are single characters a-z), and use 
 * {@link #assertSameSet(Query, Query)} and 
 * {@link #assertSubsetOf(Query, Query)}
 */
public abstract class SearchEquivalenceTestBase extends LuceneTestCase {
  protected static IndexSearcher s1, s2;
  protected static Directory directory;
  protected static IndexReader reader;
  protected static Analyzer analyzer;
  protected static String stopword; // we always pick a character as a stopword
  
  @BeforeClass
  public static void beforeClass() throws Exception {
    Random random = random();
    directory = newDirectory();
    stopword = "" + randomChar();
    CharacterRunAutomaton stopset = new CharacterRunAutomaton(BasicAutomata.makeString(stopword));
    analyzer = new MockAnalyzer(random, MockTokenizer.WHITESPACE, false, stopset);
    RandomIndexWriter iw = new RandomIndexWriter(random, directory, analyzer);
    Document doc = new Document();
    Field id = new StringField("id", "", Field.Store.NO);
    Field field = new TextField("field", "", Field.Store.NO);
    doc.add(id);
    doc.add(field);
    
    // index some docs
    int numDocs = atLeast(1000);
    for (int i = 0; i < numDocs; i++) {
      id.setStringValue(Integer.toString(i));
      field.setStringValue(randomFieldContents());
      iw.addDocument(doc);
    }
    
    // delete some docs
    int numDeletes = numDocs/20;
    for (int i = 0; i < numDeletes; i++) {
      Term toDelete = new Term("id", Integer.toString(random.nextInt(numDocs)));
      if (random.nextBoolean()) {
        iw.deleteDocuments(toDelete);
      } else {
        iw.deleteDocuments(new TermQuery(toDelete));
      }
    }
    
    reader = iw.getReader();
    s1 = newSearcher(reader);
    s2 = newSearcher(reader);
    iw.close();
  }
  
  @AfterClass
  public static void afterClass() throws Exception {
    reader.close();
    directory.close();
    analyzer.close();
    reader = null;
    directory = null;
    analyzer = null;
    s1 = s2 = null;
  }
  
  /**
   * populate a field with random contents.
   * terms should be single characters in lowercase (a-z)
   * tokenization can be assumed to be on whitespace.
   */
  static String randomFieldContents() {
    // TODO: zipf-like distribution
    StringBuilder sb = new StringBuilder();
    int numTerms = random().nextInt(15);
    for (int i = 0; i < numTerms; i++) {
      if (sb.length() > 0) {
        sb.append(' '); // whitespace
      }
      sb.append(randomChar());
    }
    return sb.toString();
  }

  /**
   * returns random character (a-z)
   */
  static char randomChar() {
    return (char) TestUtil.nextInt(random(), 'a', 'z');
  }

  /**
   * returns a term suitable for searching.
   * terms are single characters in lowercase (a-z)
   */
  protected Term randomTerm() {
    return new Term("field", "" + randomChar());
  }
  
  /**
   * Returns a random filter over the document set
   */
  protected Filter randomFilter() {
    return new QueryWrapperFilter(TermRangeQuery.newStringRange("field", "a", "" + randomChar(), true, true));
  }

  /**
   * Asserts that the documents returned by <code>q1</code>
   * are the same as of those returned by <code>q2</code>
   */
  public void assertSameSet(Query q1, Query q2) throws Exception {
    assertSubsetOf(q1, q2);
    assertSubsetOf(q2, q1);
  }
  
  /**
   * Asserts that the documents returned by <code>q1</code>
   * are a subset of those returned by <code>q2</code>
   */
  public void assertSubsetOf(Query q1, Query q2) throws Exception {   
    // test without a filter
    assertSubsetOf(q1, q2, null);
    
    // test with a filter (this will sometimes cause advance'ing enough to test it)
    assertSubsetOf(q1, q2, randomFilter());
  }
  
  /**
   * Asserts that the documents returned by <code>q1</code>
   * are a subset of those returned by <code>q2</code>.
   * 
   * Both queries will be filtered by <code>filter</code>
   */
  protected void assertSubsetOf(Query q1, Query q2, Filter filter) throws Exception {
    // TRUNK ONLY: test both filter code paths
    if (filter != null && random().nextBoolean()) {
      q1 = new FilteredQuery(q1, filter, TestUtil.randomFilterStrategy(random()));
      q2 = new FilteredQuery(q2, filter,  TestUtil.randomFilterStrategy(random()));
      filter = null;
    }
    
    // not efficient, but simple!
    TopDocs td1 = s1.search(q1, filter, reader.maxDoc());
    TopDocs td2 = s2.search(q2, filter, reader.maxDoc());
    assertTrue(td1.totalHits <= td2.totalHits);
    
    // fill the superset into a bitset
    BitSet bitset = new BitSet();
    for (int i = 0; i < td2.scoreDocs.length; i++) {
      bitset.set(td2.scoreDocs[i].doc);
    }
    
    // check in the subset, that every bit was set by the super
    for (int i = 0; i < td1.scoreDocs.length; i++) {
      assertTrue(bitset.get(td1.scoreDocs[i].doc));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3206.java