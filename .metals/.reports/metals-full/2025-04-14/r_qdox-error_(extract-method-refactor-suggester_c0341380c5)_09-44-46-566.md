error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2419.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2419.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2419.java
text:
```scala
s@@uper(PER_FIELD_REUSE_STRATEGY);

package org.apache.lucene.search.payloads;
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

import org.apache.lucene.analysis.*;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.English;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.QueryUtils;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.CheckHits;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.spans.MultiSpansWrapper;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.search.spans.Spans;
import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.Reader;
import java.io.IOException;


/**
 *
 *
 **/
public class TestPayloadTermQuery extends LuceneTestCase {
  private static IndexSearcher searcher;
  private static IndexReader reader;
  private static Similarity similarity = new BoostingSimilarity();
  private static final byte[] payloadField = new byte[]{1};
  private static final byte[] payloadMultiField1 = new byte[]{2};
  private static final byte[] payloadMultiField2 = new byte[]{4};
  protected static Directory directory;

  private static class PayloadAnalyzer extends Analyzer {

    private PayloadAnalyzer() {
      super(new PerFieldReuseStrategy());
    }

    @Override
    public TokenStreamComponents createComponents(String fieldName, Reader reader) {
      Tokenizer result = new MockTokenizer(reader, MockTokenizer.SIMPLE, true);
      return new TokenStreamComponents(result, new PayloadFilter(result, fieldName));
    }
  }

  private static class PayloadFilter extends TokenFilter {
    private final String fieldName;
    private int numSeen = 0;
    
    private final PayloadAttribute payloadAtt;
    
    public PayloadFilter(TokenStream input, String fieldName) {
      super(input);
      this.fieldName = fieldName;
      payloadAtt = addAttribute(PayloadAttribute.class);
    }
    
    @Override
    public boolean incrementToken() throws IOException {
      boolean hasNext = input.incrementToken();
      if (hasNext) {
        if (fieldName.equals("field")) {
          payloadAtt.setPayload(new BytesRef(payloadField));
        } else if (fieldName.equals("multiField")) {
          if (numSeen % 2 == 0) {
            payloadAtt.setPayload(new BytesRef(payloadMultiField1));
          } else {
            payloadAtt.setPayload(new BytesRef(payloadMultiField2));
          }
          numSeen++;
        }
        return true;
      } else {
        return false;
      }
    }

    @Override
    public void reset() throws IOException {
      super.reset();
      this.numSeen = 0;
    }
  }

  @BeforeClass
  public static void beforeClass() throws Exception {
    directory = newDirectory();
    RandomIndexWriter writer = new RandomIndexWriter(random(), directory, 
        newIndexWriterConfig(TEST_VERSION_CURRENT, new PayloadAnalyzer())
                                                     .setSimilarity(similarity).setMergePolicy(newLogMergePolicy()));
    //writer.infoStream = System.out;
    for (int i = 0; i < 1000; i++) {
      Document doc = new Document();
      Field noPayloadField = newTextField(PayloadHelper.NO_PAYLOAD_FIELD, English.intToEnglish(i), Field.Store.YES);
      //noPayloadField.setBoost(0);
      doc.add(noPayloadField);
      doc.add(newTextField("field", English.intToEnglish(i), Field.Store.YES));
      doc.add(newTextField("multiField", English.intToEnglish(i) + "  " + English.intToEnglish(i), Field.Store.YES));
      writer.addDocument(doc);
    }
    reader = writer.getReader();
    writer.close();

    searcher = newSearcher(reader);
    searcher.setSimilarity(similarity);
  }

  @AfterClass
  public static void afterClass() throws Exception {
    searcher = null;
    reader.close();
    reader = null;
    directory.close();
    directory = null;
  }

  public void test() throws IOException {
    PayloadTermQuery query = new PayloadTermQuery(new Term("field", "seventy"),
            new MaxPayloadFunction());
    TopDocs hits = searcher.search(query, null, 100);
    assertTrue("hits is null and it shouldn't be", hits != null);
    assertTrue("hits Size: " + hits.totalHits + " is not: " + 100, hits.totalHits == 100);

    //they should all have the exact same score, because they all contain seventy once, and we set
    //all the other similarity factors to be 1

    assertTrue(hits.getMaxScore() + " does not equal: " + 1, hits.getMaxScore() == 1);
    for (int i = 0; i < hits.scoreDocs.length; i++) {
      ScoreDoc doc = hits.scoreDocs[i];
      assertTrue(doc.score + " does not equal: " + 1, doc.score == 1);
    }
    CheckHits.checkExplanations(query, PayloadHelper.FIELD, searcher, true);
    Spans spans = MultiSpansWrapper.wrap(searcher.getTopReaderContext(), query);
    assertTrue("spans is null and it shouldn't be", spans != null);
    /*float score = hits.score(0);
    for (int i =1; i < hits.length(); i++)
    {
      assertTrue("scores are not equal and they should be", score == hits.score(i));
    }*/

  }
  
  public void testQuery() {
    PayloadTermQuery boostingFuncTermQuery = new PayloadTermQuery(new Term(PayloadHelper.MULTI_FIELD, "seventy"),
        new MaxPayloadFunction());
    QueryUtils.check(boostingFuncTermQuery);
    
    SpanTermQuery spanTermQuery = new SpanTermQuery(new Term(PayloadHelper.MULTI_FIELD, "seventy"));

    assertTrue(boostingFuncTermQuery.equals(spanTermQuery) == spanTermQuery.equals(boostingFuncTermQuery));
    
    PayloadTermQuery boostingFuncTermQuery2 = new PayloadTermQuery(new Term(PayloadHelper.MULTI_FIELD, "seventy"),
        new AveragePayloadFunction());
    
    QueryUtils.checkUnequal(boostingFuncTermQuery, boostingFuncTermQuery2);
  }

  public void testMultipleMatchesPerDoc() throws Exception {
    PayloadTermQuery query = new PayloadTermQuery(new Term(PayloadHelper.MULTI_FIELD, "seventy"),
            new MaxPayloadFunction());
    TopDocs hits = searcher.search(query, null, 100);
    assertTrue("hits is null and it shouldn't be", hits != null);
    assertTrue("hits Size: " + hits.totalHits + " is not: " + 100, hits.totalHits == 100);

    //they should all have the exact same score, because they all contain seventy once, and we set
    //all the other similarity factors to be 1

    //System.out.println("Hash: " + seventyHash + " Twice Hash: " + 2*seventyHash);
    assertTrue(hits.getMaxScore() + " does not equal: " + 4.0, hits.getMaxScore() == 4.0);
    //there should be exactly 10 items that score a 4, all the rest should score a 2
    //The 10 items are: 70 + i*100 where i in [0-9]
    int numTens = 0;
    for (int i = 0; i < hits.scoreDocs.length; i++) {
      ScoreDoc doc = hits.scoreDocs[i];
      if (doc.doc % 10 == 0) {
        numTens++;
        assertTrue(doc.score + " does not equal: " + 4.0, doc.score == 4.0);
      } else {
        assertTrue(doc.score + " does not equal: " + 2, doc.score == 2);
      }
    }
    assertTrue(numTens + " does not equal: " + 10, numTens == 10);
    CheckHits.checkExplanations(query, "field", searcher, true);
    Spans spans = MultiSpansWrapper.wrap(searcher.getTopReaderContext(), query);
    assertTrue("spans is null and it shouldn't be", spans != null);
    //should be two matches per document
    int count = 0;
    //100 hits times 2 matches per hit, we should have 200 in count
    while (spans.next()) {
      count++;
    }
    assertTrue(count + " does not equal: " + 200, count == 200);
  }

  //Set includeSpanScore to false, in which case just the payload score comes through.
  public void testIgnoreSpanScorer() throws Exception {
    PayloadTermQuery query = new PayloadTermQuery(new Term(PayloadHelper.MULTI_FIELD, "seventy"),
            new MaxPayloadFunction(), false);

    IndexReader reader = DirectoryReader.open(directory);
    IndexSearcher theSearcher = newSearcher(reader);
    theSearcher.setSimilarity(new FullSimilarity());
    TopDocs hits = searcher.search(query, null, 100);
    assertTrue("hits is null and it shouldn't be", hits != null);
    assertTrue("hits Size: " + hits.totalHits + " is not: " + 100, hits.totalHits == 100);

    //they should all have the exact same score, because they all contain seventy once, and we set
    //all the other similarity factors to be 1

    //System.out.println("Hash: " + seventyHash + " Twice Hash: " + 2*seventyHash);
    assertTrue(hits.getMaxScore() + " does not equal: " + 4.0, hits.getMaxScore() == 4.0);
    //there should be exactly 10 items that score a 4, all the rest should score a 2
    //The 10 items are: 70 + i*100 where i in [0-9]
    int numTens = 0;
    for (int i = 0; i < hits.scoreDocs.length; i++) {
      ScoreDoc doc = hits.scoreDocs[i];
      if (doc.doc % 10 == 0) {
        numTens++;
        assertTrue(doc.score + " does not equal: " + 4.0, doc.score == 4.0);
      } else {
        assertTrue(doc.score + " does not equal: " + 2, doc.score == 2);
      }
    }
    assertTrue(numTens + " does not equal: " + 10, numTens == 10);
    CheckHits.checkExplanations(query, "field", searcher, true);
    Spans spans = MultiSpansWrapper.wrap(searcher.getTopReaderContext(), query);
    assertTrue("spans is null and it shouldn't be", spans != null);
    //should be two matches per document
    int count = 0;
    //100 hits times 2 matches per hit, we should have 200 in count
    while (spans.next()) {
      count++;
    }
    reader.close();
  }

  public void testNoMatch() throws Exception {
    PayloadTermQuery query = new PayloadTermQuery(new Term(PayloadHelper.FIELD, "junk"),
            new MaxPayloadFunction());
    TopDocs hits = searcher.search(query, null, 100);
    assertTrue("hits is null and it shouldn't be", hits != null);
    assertTrue("hits Size: " + hits.totalHits + " is not: " + 0, hits.totalHits == 0);

  }

  public void testNoPayload() throws Exception {
    PayloadTermQuery q1 = new PayloadTermQuery(new Term(PayloadHelper.NO_PAYLOAD_FIELD, "zero"),
            new MaxPayloadFunction());
    PayloadTermQuery q2 = new PayloadTermQuery(new Term(PayloadHelper.NO_PAYLOAD_FIELD, "foo"),
            new MaxPayloadFunction());
    BooleanClause c1 = new BooleanClause(q1, BooleanClause.Occur.MUST);
    BooleanClause c2 = new BooleanClause(q2, BooleanClause.Occur.MUST_NOT);
    BooleanQuery query = new BooleanQuery();
    query.add(c1);
    query.add(c2);
    TopDocs hits = searcher.search(query, null, 100);
    assertTrue("hits is null and it shouldn't be", hits != null);
    assertTrue("hits Size: " + hits.totalHits + " is not: " + 1, hits.totalHits == 1);
    int[] results = new int[1];
    results[0] = 0;//hits.scoreDocs[0].doc;
    CheckHits.checkHitCollector(random(), query, PayloadHelper.NO_PAYLOAD_FIELD, searcher, results);
  }

  static class BoostingSimilarity extends DefaultSimilarity {

    @Override
    public float queryNorm(float sumOfSquaredWeights) {
      return 1;
    }
    
    @Override
    public float coord(int overlap, int maxOverlap) {
      return 1;
    }

    // TODO: Remove warning after API has been finalized
    @Override
    public float scorePayload(int docId, int start, int end, BytesRef payload) {
      //we know it is size 4 here, so ignore the offset/length
      return payload.bytes[payload.offset];
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //Make everything else 1 so we see the effect of the payload
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    @Override 
    public float lengthNorm(FieldInvertState state) {
      return state.getBoost();
    }

    @Override
    public float sloppyFreq(int distance) {
      return 1;
    }

    @Override
    public float idf(long docFreq, long numDocs) {
      return 1;
    }

    @Override
    public float tf(float freq) {
      return freq == 0 ? 0 : 1;
    }
  }

  static class FullSimilarity extends DefaultSimilarity{
    public float scorePayload(int docId, String fieldName, byte[] payload, int offset, int length) {
      //we know it is size 4 here, so ignore the offset/length
      return payload[offset];
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2419.java