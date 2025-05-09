error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3073.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3073.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3073.java
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.SortedSetDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.SortedSetDocValues;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermContext;
import org.apache.lucene.search.BooleanQuery.BooleanWeight;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.search.similarities.Similarity.SimScorer;
import org.apache.lucene.search.similarities.Similarity.SimWeight;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.TestUtil;
import org.apache.lucene.util.LuceneTestCase.SuppressCodecs;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/** tests BooleanScorer2's minShouldMatch */
@SuppressCodecs({"Lucene40", "Lucene41"})
public class TestMinShouldMatch2 extends LuceneTestCase {
  static Directory dir;
  static DirectoryReader r;
  static AtomicReader reader;
  static IndexSearcher searcher;
  
  static final String alwaysTerms[] = { "a" };
  static final String commonTerms[] = { "b", "c", "d" };
  static final String mediumTerms[] = { "e", "f", "g" };
  static final String rareTerms[]   = { "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };
  
  @BeforeClass
  public static void beforeClass() throws Exception {
    dir = newDirectory();
    RandomIndexWriter iw = new RandomIndexWriter(random(), dir);
    final int numDocs = atLeast(300);
    for (int i = 0; i < numDocs; i++) {
      Document doc = new Document();
      
      addSome(doc, alwaysTerms);
      
      if (random().nextInt(100) < 90) {
        addSome(doc, commonTerms);
      }
      if (random().nextInt(100) < 50) {
        addSome(doc, mediumTerms);
      }
      if (random().nextInt(100) < 10) {
        addSome(doc, rareTerms);
      }
      iw.addDocument(doc);
    }
    iw.forceMerge(1);
    iw.close();
    r = DirectoryReader.open(dir);
    reader = getOnlySegmentReader(r);
    searcher = new IndexSearcher(reader);
    searcher.setSimilarity(new DefaultSimilarity() {
      @Override
      public float queryNorm(float sumOfSquaredWeights) {
        return 1; // we disable queryNorm, both for debugging and ease of impl
      }
    });
  }
  
  @AfterClass
  public static void afterClass() throws Exception {
    reader.close();
    dir.close();
    searcher = null;
    reader = null;
    r = null;
    dir = null;
  }
  
  private static void addSome(Document doc, String values[]) {
    List<String> list = Arrays.asList(values);
    Collections.shuffle(list, random());
    int howMany = TestUtil.nextInt(random(), 1, list.size());
    for (int i = 0; i < howMany; i++) {
      doc.add(new StringField("field", list.get(i), Field.Store.NO));
      doc.add(new SortedSetDocValuesField("dv", new BytesRef(list.get(i))));
    }
  }
  
  private Scorer scorer(String values[], int minShouldMatch, boolean slow) throws Exception {
    BooleanQuery bq = new BooleanQuery();
    for (String value : values) {
      bq.add(new TermQuery(new Term("field", value)), BooleanClause.Occur.SHOULD);
    }
    bq.setMinimumNumberShouldMatch(minShouldMatch);

    BooleanWeight weight = (BooleanWeight) searcher.createNormalizedWeight(bq);
    
    if (slow) {
      return new SlowMinShouldMatchScorer(weight, reader, searcher);
    } else {
      return weight.scorer(reader.getContext(), null);
    }
  }
  
  private void assertNext(Scorer expected, Scorer actual) throws Exception {
    if (actual == null) {
      assertEquals(DocIdSetIterator.NO_MORE_DOCS, expected.nextDoc());
      return;
    }
    int doc;
    while ((doc = expected.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
      assertEquals(doc, actual.nextDoc());
      assertEquals(expected.freq(), actual.freq());
      float expectedScore = expected.score();
      float actualScore = actual.score();
      assertEquals(expectedScore, actualScore, CheckHits.explainToleranceDelta(expectedScore, actualScore));
    }
    assertEquals(DocIdSetIterator.NO_MORE_DOCS, actual.nextDoc());
  }
  
  private void assertAdvance(Scorer expected, Scorer actual, int amount) throws Exception {
    if (actual == null) {
      assertEquals(DocIdSetIterator.NO_MORE_DOCS, expected.nextDoc());
      return;
    }
    int prevDoc = 0;
    int doc;
    while ((doc = expected.advance(prevDoc+amount)) != DocIdSetIterator.NO_MORE_DOCS) {
      assertEquals(doc, actual.advance(prevDoc+amount));
      assertEquals(expected.freq(), actual.freq());
      float expectedScore = expected.score();
      float actualScore = actual.score();
      assertEquals(expectedScore, actualScore, CheckHits.explainToleranceDelta(expectedScore, actualScore));
      prevDoc = doc;
    }
    assertEquals(DocIdSetIterator.NO_MORE_DOCS, actual.advance(prevDoc+amount));
  }
  
  /** simple test for next(): minShouldMatch=2 on 3 terms (one common, one medium, one rare) */
  public void testNextCMR2() throws Exception {
    for (int common = 0; common < commonTerms.length; common++) {
      for (int medium = 0; medium < mediumTerms.length; medium++) {
        for (int rare = 0; rare < rareTerms.length; rare++) {
          Scorer expected = scorer(new String[] { commonTerms[common], mediumTerms[medium], rareTerms[rare] }, 2, true);
          Scorer actual = scorer(new String[] { commonTerms[common], mediumTerms[medium], rareTerms[rare] }, 2, false);
          assertNext(expected, actual);
        }
      }
    }
  }
  
  /** simple test for advance(): minShouldMatch=2 on 3 terms (one common, one medium, one rare) */
  public void testAdvanceCMR2() throws Exception {
    for (int amount = 25; amount < 200; amount += 25) {
      for (int common = 0; common < commonTerms.length; common++) {
        for (int medium = 0; medium < mediumTerms.length; medium++) {
          for (int rare = 0; rare < rareTerms.length; rare++) {
            Scorer expected = scorer(new String[] { commonTerms[common], mediumTerms[medium], rareTerms[rare] }, 2, true);
            Scorer actual = scorer(new String[] { commonTerms[common], mediumTerms[medium], rareTerms[rare] }, 2, false);
            assertAdvance(expected, actual, amount);
          }
        }
      }
    }
  }
  
  /** test next with giant bq of all terms with varying minShouldMatch */
  public void testNextAllTerms() throws Exception {
    List<String> termsList = new ArrayList<>();
    termsList.addAll(Arrays.asList(commonTerms));
    termsList.addAll(Arrays.asList(mediumTerms));
    termsList.addAll(Arrays.asList(rareTerms));
    String terms[] = termsList.toArray(new String[0]);
    
    for (int minNrShouldMatch = 1; minNrShouldMatch <= terms.length; minNrShouldMatch++) {
      Scorer expected = scorer(terms, minNrShouldMatch, true);
      Scorer actual = scorer(terms, minNrShouldMatch, false);
      assertNext(expected, actual);
    }
  }
  
  /** test advance with giant bq of all terms with varying minShouldMatch */
  public void testAdvanceAllTerms() throws Exception {
    List<String> termsList = new ArrayList<>();
    termsList.addAll(Arrays.asList(commonTerms));
    termsList.addAll(Arrays.asList(mediumTerms));
    termsList.addAll(Arrays.asList(rareTerms));
    String terms[] = termsList.toArray(new String[0]);
    
    for (int amount = 25; amount < 200; amount += 25) {
      for (int minNrShouldMatch = 1; minNrShouldMatch <= terms.length; minNrShouldMatch++) {
        Scorer expected = scorer(terms, minNrShouldMatch, true);
        Scorer actual = scorer(terms, minNrShouldMatch, false);
        assertAdvance(expected, actual, amount);
      }
    }
  }
  
  /** test next with varying numbers of terms with varying minShouldMatch */
  public void testNextVaryingNumberOfTerms() throws Exception {
    List<String> termsList = new ArrayList<>();
    termsList.addAll(Arrays.asList(commonTerms));
    termsList.addAll(Arrays.asList(mediumTerms));
    termsList.addAll(Arrays.asList(rareTerms));
    Collections.shuffle(termsList, random());
    for (int numTerms = 2; numTerms <= termsList.size(); numTerms++) {
      String terms[] = termsList.subList(0, numTerms).toArray(new String[0]);
      for (int minNrShouldMatch = 1; minNrShouldMatch <= terms.length; minNrShouldMatch++) {
        Scorer expected = scorer(terms, minNrShouldMatch, true);
        Scorer actual = scorer(terms, minNrShouldMatch, false);
        assertNext(expected, actual);
      }
    }
  }
  
  /** test advance with varying numbers of terms with varying minShouldMatch */
  public void testAdvanceVaryingNumberOfTerms() throws Exception {
    List<String> termsList = new ArrayList<>();
    termsList.addAll(Arrays.asList(commonTerms));
    termsList.addAll(Arrays.asList(mediumTerms));
    termsList.addAll(Arrays.asList(rareTerms));
    Collections.shuffle(termsList, random());
    
    for (int amount = 25; amount < 200; amount += 25) {
      for (int numTerms = 2; numTerms <= termsList.size(); numTerms++) {
        String terms[] = termsList.subList(0, numTerms).toArray(new String[0]);
        for (int minNrShouldMatch = 1; minNrShouldMatch <= terms.length; minNrShouldMatch++) {
          Scorer expected = scorer(terms, minNrShouldMatch, true);
          Scorer actual = scorer(terms, minNrShouldMatch, false);
          assertAdvance(expected, actual, amount);
        }
      }
    }
  }
  
  // TODO: more tests
  
  // a slow min-should match scorer that uses a docvalues field.
  // later, we can make debugging easier as it can record the set of ords it currently matched
  // and e.g. print out their values and so on for the document
  static class SlowMinShouldMatchScorer extends Scorer {
    int currentDoc = -1;     // current docid
    int currentMatched = -1; // current number of terms matched
    
    final SortedSetDocValues dv;
    final int maxDoc;

    final Set<Long> ords = new HashSet<>();
    final SimScorer[] sims;
    final int minNrShouldMatch;
    
    double score = Float.NaN;

    SlowMinShouldMatchScorer(BooleanWeight weight, AtomicReader reader, IndexSearcher searcher) throws IOException {
      super(weight);
      this.dv = reader.getSortedSetDocValues("dv");
      this.maxDoc = reader.maxDoc();
      BooleanQuery bq = (BooleanQuery) weight.getQuery();
      this.minNrShouldMatch = bq.getMinimumNumberShouldMatch();
      this.sims = new SimScorer[(int)dv.getValueCount()];
      for (BooleanClause clause : bq.getClauses()) {
        assert !clause.isProhibited();
        assert !clause.isRequired();
        Term term = ((TermQuery)clause.getQuery()).getTerm();
        long ord = dv.lookupTerm(term.bytes());
        if (ord >= 0) {
          boolean success = ords.add(ord);
          assert success; // no dups
          TermContext context = TermContext.build(reader.getContext(), term);
          SimWeight w = weight.similarity.computeWeight(1f, 
                        searcher.collectionStatistics("field"),
                        searcher.termStatistics(term, context));
          w.getValueForNormalization(); // ignored
          w.normalize(1F, 1F);
          sims[(int)ord] = weight.similarity.simScorer(w, reader.getContext());
        }
      }
    }

    @Override
    public float score() throws IOException {
      assert score != 0 : currentMatched;
      return (float)score * ((BooleanWeight) weight).coord(currentMatched, ((BooleanWeight) weight).maxCoord);
    }

    @Override
    public int freq() throws IOException {
      return currentMatched;
    }

    @Override
    public int docID() {
      return currentDoc;
    }

    @Override
    public int nextDoc() throws IOException {
      assert currentDoc != NO_MORE_DOCS;
      for (currentDoc = currentDoc+1; currentDoc < maxDoc; currentDoc++) {
        currentMatched = 0;
        score = 0;
        dv.setDocument(currentDoc);
        long ord;
        while ((ord = dv.nextOrd()) != SortedSetDocValues.NO_MORE_ORDS) {
          if (ords.contains(ord)) {
            currentMatched++;
            score += sims[(int)ord].score(currentDoc, 1);
          }
        }
        if (currentMatched >= minNrShouldMatch) {
          return currentDoc;
        }
      }
      return currentDoc = NO_MORE_DOCS;
    }

    @Override
    public int advance(int target) throws IOException {
      int doc;
      while ((doc = nextDoc()) < target) {
      }
      return doc;
    }

    @Override
    public long cost() {
      return maxDoc;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3073.java