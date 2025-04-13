error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3182.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3182.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3182.java
text:
```scala
w@@riter.shutdown();

package org.apache.lucene.analysis.shingle;

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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;

/**
 * A test class for ShingleAnalyzerWrapper as regards queries and scoring.
 */
public class ShingleAnalyzerWrapperTest extends BaseTokenStreamTestCase {
  private Analyzer analyzer;
  private IndexSearcher searcher;
  private IndexReader reader;
  private Directory directory;

  /**
   * Set up a new index in RAM with three test phrases and the supplied Analyzer.
   *
   * @throws Exception if an error occurs with index writer or searcher
   */
  @Override
  public void setUp() throws Exception {
    super.setUp();
    analyzer = new ShingleAnalyzerWrapper(new MockAnalyzer(random(), MockTokenizer.WHITESPACE, false), 2);
    directory = newDirectory();
    IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(TEST_VERSION_CURRENT, analyzer));

    Document doc;
    doc = new Document();
    doc.add(new TextField("content", "please divide this sentence into shingles", Field.Store.YES));
    writer.addDocument(doc);

    doc = new Document();
    doc.add(new TextField("content", "just another test sentence", Field.Store.YES));
    writer.addDocument(doc);

    doc = new Document();
    doc.add(new TextField("content", "a sentence which contains no test", Field.Store.YES));
    writer.addDocument(doc);

    writer.close();

    reader = DirectoryReader.open(directory);
    searcher = newSearcher(reader);
  }
  
  @Override
  public void tearDown() throws Exception {
    reader.close();
    directory.close();
    super.tearDown();
  }

  protected void compareRanks(ScoreDoc[] hits, int[] ranks) throws Exception {
    assertEquals(ranks.length, hits.length);
    for (int i = 0; i < ranks.length; i++) {
      assertEquals(ranks[i], hits[i].doc);
    }
  }

  /*
   * This shows how to construct a phrase query containing shingles.
   */
  public void testShingleAnalyzerWrapperPhraseQuery() throws Exception {
    PhraseQuery q = new PhraseQuery();

    try (TokenStream ts = analyzer.tokenStream("content", "this sentence")) {
      int j = -1;
    
      PositionIncrementAttribute posIncrAtt = ts.addAttribute(PositionIncrementAttribute.class);
      CharTermAttribute termAtt = ts.addAttribute(CharTermAttribute.class);
    
      ts.reset();
      while (ts.incrementToken()) {
        j += posIncrAtt.getPositionIncrement();
        String termText = termAtt.toString();
        q.add(new Term("content", termText), j);
      }
      ts.end();
    }

    ScoreDoc[] hits = searcher.search(q, null, 1000).scoreDocs;
    int[] ranks = new int[] { 0 };
    compareRanks(hits, ranks);
  }

  /*
   * How to construct a boolean query with shingles. A query like this will
   * implicitly score those documents higher that contain the words in the query
   * in the right order and adjacent to each other.
   */
  public void testShingleAnalyzerWrapperBooleanQuery() throws Exception {
    BooleanQuery q = new BooleanQuery();

    try (TokenStream ts = analyzer.tokenStream("content", "test sentence")) {
      CharTermAttribute termAtt = ts.addAttribute(CharTermAttribute.class);
    
      ts.reset();
      while (ts.incrementToken()) {
        String termText =  termAtt.toString();
        q.add(new TermQuery(new Term("content", termText)),
            BooleanClause.Occur.SHOULD);
      }
      ts.end();
    }

    ScoreDoc[] hits = searcher.search(q, null, 1000).scoreDocs;
    int[] ranks = new int[] { 1, 2, 0 };
    compareRanks(hits, ranks);
  }
  
  public void testReusableTokenStream() throws Exception {
    Analyzer a = new ShingleAnalyzerWrapper(new MockAnalyzer(random(), MockTokenizer.WHITESPACE, false), 2);
    assertAnalyzesTo(a, "please divide into shingles",
        new String[] { "please", "please divide", "divide", "divide into", "into", "into shingles", "shingles" },
        new int[] { 0, 0, 7, 7, 14, 14, 19 },
        new int[] { 6, 13, 13, 18, 18, 27, 27 },
        new int[] { 1, 0, 1, 0, 1, 0, 1 });
    assertAnalyzesTo(a, "divide me up again",
        new String[] { "divide", "divide me", "me", "me up", "up", "up again", "again" },
        new int[] { 0, 0, 7, 7, 10, 10, 13 },
        new int[] { 6, 9, 9, 12, 12, 18, 18 },
        new int[] { 1, 0, 1, 0, 1, 0, 1 });
  }

  public void testNonDefaultMinShingleSize() throws Exception {
    ShingleAnalyzerWrapper analyzer 
      = new ShingleAnalyzerWrapper(new MockAnalyzer(random(), MockTokenizer.WHITESPACE, false), 3, 4);
    assertAnalyzesTo(analyzer, "please divide this sentence into shingles",
                          new String[] { "please",   "please divide this",   "please divide this sentence", 
                                         "divide",   "divide this sentence", "divide this sentence into", 
                                         "this",     "this sentence into",   "this sentence into shingles",
                                         "sentence", "sentence into shingles",
                                         "into",
                                         "shingles" },
                          new int[] { 0,  0,  0,  7,  7,  7, 14, 14, 14, 19, 19, 28, 33 },
                          new int[] { 6, 18, 27, 13, 27, 32, 18, 32, 41, 27, 41, 32, 41 },
                          new int[] { 1,  0,  0,  1,  0,  0,  1,  0,  0,  1,  0,  1,  1 });

    analyzer = new ShingleAnalyzerWrapper(
        new MockAnalyzer(random(), MockTokenizer.WHITESPACE, false), 3, 4,
        ShingleFilter.DEFAULT_TOKEN_SEPARATOR, false, false, ShingleFilter.DEFAULT_FILLER_TOKEN);
    assertAnalyzesTo(analyzer, "please divide this sentence into shingles",
                          new String[] { "please divide this",   "please divide this sentence", 
                                         "divide this sentence", "divide this sentence into", 
                                         "this sentence into",   "this sentence into shingles",
                                         "sentence into shingles" },
                          new int[] {  0,  0,  7,  7, 14, 14, 19 },
                          new int[] { 18, 27, 27, 32, 32, 41, 41 },
                          new int[] {  1,  0,  1,  0,  1,  0,  1 });
  }
  
  public void testNonDefaultMinAndSameMaxShingleSize() throws Exception {
    ShingleAnalyzerWrapper analyzer
      = new ShingleAnalyzerWrapper(new MockAnalyzer(random(), MockTokenizer.WHITESPACE, false), 3, 3);
    assertAnalyzesTo(analyzer, "please divide this sentence into shingles",
                          new String[] { "please",   "please divide this", 
                                         "divide",   "divide this sentence", 
                                         "this",     "this sentence into",
                                         "sentence", "sentence into shingles",
                                         "into",
                                         "shingles" },
                          new int[] { 0,  0,  7,  7, 14, 14, 19, 19, 28, 33 },
                          new int[] { 6, 18, 13, 27, 18, 32, 27, 41, 32, 41 },
                          new int[] { 1,  0,  1,  0,  1,  0,  1,  0,  1,  1 });

    analyzer = new ShingleAnalyzerWrapper(
        new MockAnalyzer(random(), MockTokenizer.WHITESPACE, false), 3, 3,
        ShingleFilter.DEFAULT_TOKEN_SEPARATOR, false, false, ShingleFilter.DEFAULT_FILLER_TOKEN);
    assertAnalyzesTo(analyzer, "please divide this sentence into shingles",
                          new String[] { "please divide this", 
                                         "divide this sentence", 
                                         "this sentence into",
                                         "sentence into shingles" },
                          new int[] {  0,  7, 14, 19 },
                          new int[] { 18, 27, 32, 41 },
                          new int[] {  1,  1,  1,  1 });
  }

  public void testNoTokenSeparator() throws Exception {
    ShingleAnalyzerWrapper analyzer = new ShingleAnalyzerWrapper(
        new MockAnalyzer(random(), MockTokenizer.WHITESPACE, false),
        ShingleFilter.DEFAULT_MIN_SHINGLE_SIZE,
        ShingleFilter.DEFAULT_MAX_SHINGLE_SIZE,
        "", true, false,
        ShingleFilter.DEFAULT_FILLER_TOKEN);
    assertAnalyzesTo(analyzer, "please divide into shingles",
                          new String[] { "please", "pleasedivide", 
                                         "divide", "divideinto", 
                                         "into", "intoshingles", 
                                         "shingles" },
                          new int[] { 0,  0,  7,  7, 14, 14, 19 },
                          new int[] { 6, 13, 13, 18, 18, 27, 27 },
                          new int[] { 1,  0,  1,  0,  1,  0,  1 });

    analyzer = new ShingleAnalyzerWrapper(
        new MockAnalyzer(random(), MockTokenizer.WHITESPACE, false),
        ShingleFilter.DEFAULT_MIN_SHINGLE_SIZE,
        ShingleFilter.DEFAULT_MAX_SHINGLE_SIZE,
        "", false, false,
        ShingleFilter.DEFAULT_FILLER_TOKEN);
    assertAnalyzesTo(analyzer, "please divide into shingles",
                          new String[] { "pleasedivide", 
                                         "divideinto", 
                                         "intoshingles" },
                          new int[] {  0,  7, 14 },
                          new int[] { 13, 18, 27 },
                          new int[] {  1,  1,  1 });
  }

  public void testNullTokenSeparator() throws Exception {
    ShingleAnalyzerWrapper analyzer = new ShingleAnalyzerWrapper(
        new MockAnalyzer(random(), MockTokenizer.WHITESPACE, false),
        ShingleFilter.DEFAULT_MIN_SHINGLE_SIZE,
        ShingleFilter.DEFAULT_MAX_SHINGLE_SIZE,
        null, true, false,
        ShingleFilter.DEFAULT_FILLER_TOKEN);
    assertAnalyzesTo(analyzer, "please divide into shingles",
                          new String[] { "please", "pleasedivide", 
                                         "divide", "divideinto", 
                                         "into", "intoshingles", 
                                         "shingles" },
                          new int[] { 0,  0,  7,  7, 14, 14, 19 },
                          new int[] { 6, 13, 13, 18, 18, 27, 27 },
                          new int[] { 1,  0,  1,  0,  1,  0,  1 });

    analyzer = new ShingleAnalyzerWrapper(
        new MockAnalyzer(random(), MockTokenizer.WHITESPACE, false),
        ShingleFilter.DEFAULT_MIN_SHINGLE_SIZE,
        ShingleFilter.DEFAULT_MAX_SHINGLE_SIZE,
        "", false, false,
        ShingleFilter.DEFAULT_FILLER_TOKEN);
    assertAnalyzesTo(analyzer, "please divide into shingles",
                          new String[] { "pleasedivide", 
                                         "divideinto", 
                                         "intoshingles" },
                          new int[] {  0,  7, 14 },
                          new int[] { 13, 18, 27 },
                          new int[] {  1,  1,  1 });
  }

  public void testAltTokenSeparator() throws Exception {
    ShingleAnalyzerWrapper analyzer = new ShingleAnalyzerWrapper(
        new MockAnalyzer(random(), MockTokenizer.WHITESPACE, false),
        ShingleFilter.DEFAULT_MIN_SHINGLE_SIZE,
        ShingleFilter.DEFAULT_MAX_SHINGLE_SIZE,
        "<SEP>", true, false,
        ShingleFilter.DEFAULT_FILLER_TOKEN);
    assertAnalyzesTo(analyzer, "please divide into shingles",
                          new String[] { "please", "please<SEP>divide", 
                                         "divide", "divide<SEP>into", 
                                         "into", "into<SEP>shingles", 
                                         "shingles" },
                          new int[] { 0,  0,  7,  7, 14, 14, 19 },
                          new int[] { 6, 13, 13, 18, 18, 27, 27 },
                          new int[] { 1,  0,  1,  0,  1,  0,  1 });

    analyzer = new ShingleAnalyzerWrapper(
        new MockAnalyzer(random(), MockTokenizer.WHITESPACE, false),
        ShingleFilter.DEFAULT_MIN_SHINGLE_SIZE,
        ShingleFilter.DEFAULT_MAX_SHINGLE_SIZE,
        "<SEP>", false, false,
        ShingleFilter.DEFAULT_FILLER_TOKEN);
    assertAnalyzesTo(analyzer, "please divide into shingles",
                          new String[] { "please<SEP>divide", 
                                         "divide<SEP>into", 
                                         "into<SEP>shingles" },
                          new int[] {  0,  7, 14 },
                          new int[] { 13, 18, 27 },
                          new int[] {  1,  1,  1 });
  }

  public void testAltFillerToken() throws Exception {
    Analyzer delegate = new Analyzer() {
      @Override
      protected TokenStreamComponents createComponents(String fieldName) {
        CharArraySet stopSet = StopFilter.makeStopSet(TEST_VERSION_CURRENT, "into");
        Tokenizer tokenizer = new MockTokenizer(MockTokenizer.WHITESPACE, false);
        TokenFilter filter = new StopFilter(TEST_VERSION_CURRENT, tokenizer, stopSet);
        return new TokenStreamComponents(tokenizer, filter);
      }
    };

    ShingleAnalyzerWrapper analyzer = new ShingleAnalyzerWrapper(
        delegate,
        ShingleFilter.DEFAULT_MIN_SHINGLE_SIZE,
        ShingleFilter.DEFAULT_MAX_SHINGLE_SIZE,
        ShingleFilter.DEFAULT_TOKEN_SEPARATOR,
        true, false, "--");
    assertAnalyzesTo(analyzer, "please divide into shingles",
                     new String[] { "please", "please divide",
                                    "divide", "divide --",
                                    "-- shingles", "shingles" },
                     new int[] { 0,  0,  7,  7, 19, 19 },
                     new int[] { 6, 13, 13, 19, 27, 27 },
                     new int[] { 1,  0,  1,  0,  1,  1 });

    analyzer = new ShingleAnalyzerWrapper(
        delegate,
        ShingleFilter.DEFAULT_MIN_SHINGLE_SIZE,
        ShingleFilter.DEFAULT_MAX_SHINGLE_SIZE,
        ShingleFilter.DEFAULT_TOKEN_SEPARATOR,
        false, false, null);
    assertAnalyzesTo(analyzer, "please divide into shingles",
                     new String[] { "please divide", "divide ", " shingles" },
                     new int[] {  0,  7, 19 },
                     new int[] { 13, 19, 27 },
                     new int[] {  1,  1,  1 });

    analyzer = new ShingleAnalyzerWrapper(
        delegate,
        ShingleFilter.DEFAULT_MIN_SHINGLE_SIZE,
        ShingleFilter.DEFAULT_MAX_SHINGLE_SIZE,
        ShingleFilter.DEFAULT_TOKEN_SEPARATOR,
        false, false, "");
    assertAnalyzesTo(analyzer, "please divide into shingles",
                     new String[] { "please divide", "divide ", " shingles" },
                     new int[] {  0,  7, 19 },
                     new int[] { 13, 19, 27 },
                     new int[] {  1,  1,  1 });
  }

  public void testOutputUnigramsIfNoShinglesSingleToken() throws Exception {
    ShingleAnalyzerWrapper analyzer = new ShingleAnalyzerWrapper(
        new MockAnalyzer(random(), MockTokenizer.WHITESPACE, false),
        ShingleFilter.DEFAULT_MIN_SHINGLE_SIZE,
        ShingleFilter.DEFAULT_MAX_SHINGLE_SIZE,
        "", false, true,
        ShingleFilter.DEFAULT_FILLER_TOKEN);
    assertAnalyzesTo(analyzer, "please",
                          new String[] { "please" },
                          new int[] { 0 },
                          new int[] { 6 },
                          new int[] { 1 });
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3182.java