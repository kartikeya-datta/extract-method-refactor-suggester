error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/86.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/86.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/86.java
text:
```scala
f@@ield.setStringValue(mapInt(codePointTable, i));

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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;

/** 
 * Tests the results of fuzzy against pre-recorded output 
 * The format of the file is the following:
 * 
 * Header Row: # of bits: generate 2^n sequential documents 
 * with a value of Integer.toBinaryString
 * 
 * Entries: an entry is a param spec line, a resultCount line, and
 * then 'resultCount' results lines. The results lines are in the
 * expected order.
 * 
 * param spec line: a comma-separated list of params to FuzzyQuery
 *   (query, prefixLen, pqSize, minScore)
 * query = query text as a number (expand with Integer.toBinaryString)
 * prefixLen = prefix length
 * pqSize = priority queue maximum size for TopTermsBoostOnlyBooleanQueryRewrite
 * minScore = minimum similarity
 * 
 * resultCount line: total number of expected hits.
 * 
 * results line: comma-separated docID, score pair
 **/
public class TestFuzzyQuery2 extends LuceneTestCase {
  /** epsilon for score comparisons */
  static final float epsilon = 0.00001f;

  static int[][] mappings = new int[][] {
    new int[] { 0x40, 0x41 },
    new int[] { 0x40, 0x0195 },
    new int[] { 0x40, 0x0906 },
    new int[] { 0x40, 0x1040F },
    new int[] { 0x0194, 0x0195 },
    new int[] { 0x0194, 0x0906 },
    new int[] { 0x0194, 0x1040F },
    new int[] { 0x0905, 0x0906 },
    new int[] { 0x0905, 0x1040F },
    new int[] { 0x1040E, 0x1040F }
  };
  public void testFromTestData() throws Exception {
    // TODO: randomize!
    assertFromTestData(mappings[random.nextInt(mappings.length)]);
  }

  public void assertFromTestData(int codePointTable[]) throws Exception {
    if (VERBOSE) {
      System.out.println("TEST: codePointTable=" + codePointTable);
    }
    InputStream stream = getClass().getResourceAsStream("fuzzyTestData.txt");
    BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
    
    int bits = Integer.parseInt(reader.readLine());
    int terms = (int) Math.pow(2, bits);
    
    Directory dir = newDirectory();
    RandomIndexWriter writer = new RandomIndexWriter(random, dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random, MockTokenizer.KEYWORD, false)).setMergePolicy(newLogMergePolicy()));

    Document doc = new Document();
    Field field = newField("field", "", TextField.TYPE_UNSTORED);
    doc.add(field);
    
    for (int i = 0; i < terms; i++) {
      field.setValue(mapInt(codePointTable, i));
      writer.addDocument(doc);
    }   
    
    IndexReader r = writer.getReader();
    IndexSearcher searcher = newSearcher(r);
    if (VERBOSE) {
      System.out.println("TEST: searcher=" + searcher);
    }
    // even though this uses a boost-only rewrite, this test relies upon queryNorm being the default implementation,
    // otherwise scores are different!
    searcher.setSimilarity(new DefaultSimilarity());
    
    writer.close();
    String line;
    while ((line = reader.readLine()) != null) {
      String params[] = line.split(",");
      String query = mapInt(codePointTable, Integer.parseInt(params[0]));
      int prefix = Integer.parseInt(params[1]);
      int pqSize = Integer.parseInt(params[2]);
      float minScore = Float.parseFloat(params[3]);
      FuzzyQuery q = new FuzzyQuery(new Term("field", query), minScore, prefix);
      q.setRewriteMethod(new MultiTermQuery.TopTermsBoostOnlyBooleanQueryRewrite(pqSize));
      int expectedResults = Integer.parseInt(reader.readLine());
      TopDocs docs = searcher.search(q, expectedResults);
      assertEquals(expectedResults, docs.totalHits);
      for (int i = 0; i < expectedResults; i++) {
        String scoreDoc[] = reader.readLine().split(",");
        assertEquals(Integer.parseInt(scoreDoc[0]), docs.scoreDocs[i].doc);
        assertEquals(Float.parseFloat(scoreDoc[1]), docs.scoreDocs[i].score, epsilon);
      }
    }
    r.close();
    dir.close();
  }
  
  /* map bits to unicode codepoints */
  private static String mapInt(int codePointTable[], int i) {
    StringBuilder sb = new StringBuilder();
    String binary = Integer.toBinaryString(i);
    for (int j = 0; j < binary.length(); j++)
      sb.appendCodePoint(codePointTable[binary.charAt(j) - '0']);
    return sb.toString();
  }

  /* Code to generate test data
  public static void main(String args[]) throws Exception {
    int bits = 3;
    System.out.println(bits);
    int terms = (int) Math.pow(2, bits);
    
    RAMDirectory dir = new RAMDirectory();
    IndexWriter writer = new IndexWriter(dir, new KeywordAnalyzer(),
        IndexWriter.MaxFieldLength.UNLIMITED);
    
    Document doc = new Document();
    Field field = newField("field", "", Field.Store.NO, Field.Index.ANALYZED);
    doc.add(field);

    for (int i = 0; i < terms; i++) {
      field.setValue(Integer.toBinaryString(i));
      writer.addDocument(doc);
    }
    
    writer.forceMerge(1);
    writer.close();

    IndexSearcher searcher = new IndexSearcher(dir);
    for (int prefix = 0; prefix < bits; prefix++)
      for (int pqsize = 1; pqsize <= terms; pqsize++)
        for (float minscore = 0.1F; minscore < 1F; minscore += 0.2F)
          for (int query = 0; query < terms; query++) {
            FuzzyQuery q = new FuzzyQuery(
                new Term("field", Integer.toBinaryString(query)), minscore, prefix);
            q.setRewriteMethod(new MultiTermQuery.TopTermsBoostOnlyBooleanQueryRewrite(pqsize));
            System.out.println(query + "," + prefix + "," + pqsize + "," + minscore);
            TopDocs docs = searcher.search(q, terms);
            System.out.println(docs.totalHits);
            for (int i = 0; i < docs.totalHits; i++)
              System.out.println(docs.scoreDocs[i].doc + "," + docs.scoreDocs[i].score);
          }
  }
  */
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/86.java