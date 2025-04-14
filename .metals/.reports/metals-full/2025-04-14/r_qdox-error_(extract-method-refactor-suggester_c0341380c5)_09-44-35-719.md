error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/529.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/529.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/529.java
text:
```scala
f@@ound.add(BytesRef.deepCopyOf(te.term()));

package org.apache.lucene.index;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.TermsEnum.SeekStatus;
import org.apache.lucene.index.codecs.Codec;
import org.apache.lucene.search.AutomatonQuery;
import org.apache.lucene.search.CheckHits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util._TestUtil;
import org.apache.lucene.util.automaton.Automaton;
import org.apache.lucene.util.automaton.AutomatonTestUtil;
import org.apache.lucene.util.automaton.BasicOperations;
import org.apache.lucene.util.automaton.CompiledAutomaton;
import org.apache.lucene.util.automaton.DaciukMihovAutomatonBuilder;
import org.apache.lucene.util.automaton.RegExp;
import org.apache.lucene.util.automaton.SpecialOperations;

public class TestTermsEnum2 extends LuceneTestCase {
  private Directory dir;
  private IndexReader reader;
  private IndexSearcher searcher;
  private SortedSet<BytesRef> terms; // the terms we put in the index
  private Automaton termsAutomaton; // automata of the same
  int numIterations;

  public void setUp() throws Exception {
    super.setUp();
    // we generate aweful regexps: good for testing.
    // but for preflex codec, the test can be very slow, so use less iterations.
    numIterations = Codec.getDefault().getName().equals("Lucene3x") ? 10 * RANDOM_MULTIPLIER : atLeast(50);
    dir = newDirectory();
    RandomIndexWriter writer = new RandomIndexWriter(random, dir,
        newIndexWriterConfig(TEST_VERSION_CURRENT,
            new MockAnalyzer(random, MockTokenizer.KEYWORD, false))
            .setMaxBufferedDocs(_TestUtil.nextInt(random, 50, 1000)));
    Document doc = new Document();
    Field field = newField("field", "", StringField.TYPE_STORED);
    doc.add(field);
    terms = new TreeSet<BytesRef>();
 
    int num = atLeast(200);
    for (int i = 0; i < num; i++) {
      String s = _TestUtil.randomUnicodeString(random);
      field.setValue(s);
      terms.add(new BytesRef(s));
      writer.addDocument(doc);
    }
    
    termsAutomaton = DaciukMihovAutomatonBuilder.build(terms);
    
    reader = writer.getReader();
    searcher = newSearcher(reader);
    writer.close();
  }
  
  public void tearDown() throws Exception {
    searcher.close();
    reader.close();
    dir.close();
    super.tearDown();
  }
  
  /** tests a pre-intersected automaton against the original */
  public void testFiniteVersusInfinite() throws Exception {
    for (int i = 0; i < numIterations; i++) {
      String reg = AutomatonTestUtil.randomRegexp(random);
      Automaton automaton = new RegExp(reg, RegExp.NONE).toAutomaton();
      final List<BytesRef> matchedTerms = new ArrayList<BytesRef>();
      for(BytesRef t : terms) {
        if (BasicOperations.run(automaton, t.utf8ToString())) {
          matchedTerms.add(t);
        }
      }

      Automaton alternate = DaciukMihovAutomatonBuilder.build(matchedTerms);
      //System.out.println("match " + matchedTerms.size() + " " + alternate.getNumberOfStates() + " states, sigma=" + alternate.getStartPoints().length);
      //AutomatonTestUtil.minimizeSimple(alternate);
      //System.out.println("minmize done");
      AutomatonQuery a1 = new AutomatonQuery(new Term("field", ""), automaton);
      AutomatonQuery a2 = new AutomatonQuery(new Term("field", ""), alternate);
      CheckHits.checkEqual(a1, searcher.search(a1, 25).scoreDocs, searcher.search(a2, 25).scoreDocs);
    }
  }
  
  /** seeks to every term accepted by some automata */
  public void testSeeking() throws Exception {
    for (int i = 0; i < numIterations; i++) {
      String reg = AutomatonTestUtil.randomRegexp(random);
      Automaton automaton = new RegExp(reg, RegExp.NONE).toAutomaton();
      TermsEnum te = MultiFields.getTerms(reader, "field").iterator(null);
      ArrayList<BytesRef> unsortedTerms = new ArrayList<BytesRef>(terms);
      Collections.shuffle(unsortedTerms, random);

      for (BytesRef term : unsortedTerms) {
        if (BasicOperations.run(automaton, term.utf8ToString())) {
          // term is accepted
          if (random.nextBoolean()) {
            // seek exact
            assertTrue(te.seekExact(term, random.nextBoolean()));
          } else {
            // seek ceil
            assertEquals(SeekStatus.FOUND, te.seekCeil(term, random.nextBoolean()));
            assertEquals(term, te.term());
          }
        }
      }
    }
  }
  
  /** mixes up seek and next for all terms */
  public void testSeekingAndNexting() throws Exception {
    for (int i = 0; i < numIterations; i++) {
      TermsEnum te = MultiFields.getTerms(reader, "field").iterator(null);

      for (BytesRef term : terms) {
        int c = random.nextInt(3);
        if (c == 0) {
          assertEquals(term, te.next());
        } else if (c == 1) {
          assertEquals(SeekStatus.FOUND, te.seekCeil(term, random.nextBoolean()));
          assertEquals(term, te.term());
        } else {
          assertTrue(te.seekExact(term, random.nextBoolean()));
        }
      }
    }
  }
  
  /** tests intersect: TODO start at a random term! */
  public void testIntersect() throws Exception {
    for (int i = 0; i < numIterations; i++) {
      String reg = AutomatonTestUtil.randomRegexp(random);
      Automaton automaton = new RegExp(reg, RegExp.NONE).toAutomaton();
      CompiledAutomaton ca = new CompiledAutomaton(automaton, SpecialOperations.isFinite(automaton), false);
      TermsEnum te = MultiFields.getTerms(reader, "field").intersect(ca, null);
      Automaton expected = BasicOperations.intersection(termsAutomaton, automaton);
      TreeSet<BytesRef> found = new TreeSet<BytesRef>();
      while (te.next() != null) {
        found.add(new BytesRef(te.term()));
      }
      
      Automaton actual = DaciukMihovAutomatonBuilder.build(found);     
      assertTrue(BasicOperations.sameLanguage(expected, actual));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/529.java