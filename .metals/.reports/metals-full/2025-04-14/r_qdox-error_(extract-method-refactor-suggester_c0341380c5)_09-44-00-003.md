error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1133.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1133.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1133.java
text:
```scala
protected R@@eader initReader(String fieldName, Reader reader) {

package org.apache.lucene.analysis.cjk;

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
import java.io.Reader;
import java.util.Random;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.CharReader;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.charfilter.MappingCharFilter;
import org.apache.lucene.analysis.charfilter.NormalizeCharMap;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.analysis.util.CharArraySet;

/**
 * Most tests adopted from TestCJKTokenizer
 */
public class TestCJKAnalyzer extends BaseTokenStreamTestCase {
  private Analyzer analyzer = new CJKAnalyzer(TEST_VERSION_CURRENT);
  
  public void testJa1() throws IOException {
    assertAnalyzesTo(analyzer, "一二三四五六七八九十",
      new String[] { "一二", "二三", "三四", "四五", "五六", "六七", "七八", "八九", "九十" },
      new int[] { 0, 1, 2, 3, 4, 5, 6, 7,  8 },
      new int[] { 2, 3, 4, 5, 6, 7, 8, 9, 10 },
      new String[] { "<DOUBLE>", "<DOUBLE>", "<DOUBLE>", "<DOUBLE>", "<DOUBLE>", "<DOUBLE>", "<DOUBLE>", "<DOUBLE>", "<DOUBLE>" },
      new int[] { 1, 1, 1, 1, 1, 1, 1, 1,  1 });
  }
  
  public void testJa2() throws IOException {
    assertAnalyzesTo(analyzer, "一 二三四 五六七八九 十",
      new String[] { "一", "二三", "三四", "五六", "六七", "七八", "八九", "十" },
      new int[] { 0, 2, 3, 6, 7,  8,  9, 12 },
      new int[] { 1, 4, 5, 8, 9, 10, 11, 13 },
      new String[] { "<SINGLE>", "<DOUBLE>", "<DOUBLE>", "<DOUBLE>", "<DOUBLE>", "<DOUBLE>", "<DOUBLE>", "<SINGLE>" },
      new int[] { 1, 1, 1, 1, 1,  1,  1,  1 });
  }
  
  public void testC() throws IOException {
    assertAnalyzesTo(analyzer, "abc defgh ijklmn opqrstu vwxy z",
      new String[] { "abc", "defgh", "ijklmn", "opqrstu", "vwxy", "z" },
      new int[] { 0, 4, 10, 17, 25, 30 },
      new int[] { 3, 9, 16, 24, 29, 31 },
      new String[] { "<ALPHANUM>", "<ALPHANUM>", "<ALPHANUM>", "<ALPHANUM>", "<ALPHANUM>", "<ALPHANUM>" },
      new int[] { 1, 1,  1,  1,  1,  1 });
  }
  
  /**
   * LUCENE-2207: wrong offset calculated by end() 
   */
  public void testFinalOffset() throws IOException {
    assertAnalyzesTo(analyzer, "あい",
      new String[] { "あい" },
      new int[] { 0 },
      new int[] { 2 },
      new String[] { "<DOUBLE>" },
      new int[] { 1 });
    
    assertAnalyzesTo(analyzer, "あい   ",
      new String[] { "あい" },
      new int[] { 0 },
      new int[] { 2 },
      new String[] { "<DOUBLE>" },
      new int[] { 1 });

    assertAnalyzesTo(analyzer, "test",
      new String[] { "test" },
      new int[] { 0 },
      new int[] { 4 },
      new String[] { "<ALPHANUM>" },
      new int[] { 1 });
    
    assertAnalyzesTo(analyzer, "test   ",
      new String[] { "test" },
      new int[] { 0 },
      new int[] { 4 },
      new String[] { "<ALPHANUM>" },
      new int[] { 1 });
    
    assertAnalyzesTo(analyzer, "あいtest",
      new String[] { "あい", "test" },
      new int[] { 0, 2 },
      new int[] { 2, 6 },
      new String[] { "<DOUBLE>", "<ALPHANUM>" },
      new int[] { 1, 1 });
    
    assertAnalyzesTo(analyzer, "testあい    ",
      new String[] { "test", "あい" },
      new int[] { 0, 4 },
      new int[] { 4, 6 },
      new String[] { "<ALPHANUM>", "<DOUBLE>" },
      new int[] { 1, 1 });
  }
  
  public void testMix() throws IOException {
    assertAnalyzesTo(analyzer, "あいうえおabcかきくけこ",
      new String[] { "あい", "いう", "うえ", "えお", "abc", "かき", "きく", "くけ", "けこ" },
      new int[] { 0, 1, 2, 3, 5,  8,  9, 10, 11 },
      new int[] { 2, 3, 4, 5, 8, 10, 11, 12, 13 },
      new String[] { "<DOUBLE>", "<DOUBLE>", "<DOUBLE>", "<DOUBLE>", "<ALPHANUM>", "<DOUBLE>", "<DOUBLE>", "<DOUBLE>", "<DOUBLE>" },
      new int[] { 1, 1, 1, 1, 1,  1,  1,  1,  1});
  }
  
  public void testMix2() throws IOException {
    assertAnalyzesTo(analyzer, "あいうえおabんcかきくけ こ",
      new String[] { "あい", "いう", "うえ", "えお", "ab", "ん", "c", "かき", "きく", "くけ", "こ" },
      new int[] { 0, 1, 2, 3, 5, 7, 8,  9, 10, 11, 14 },
      new int[] { 2, 3, 4, 5, 7, 8, 9, 11, 12, 13, 15 },
      new String[] { "<DOUBLE>", "<DOUBLE>", "<DOUBLE>", "<DOUBLE>", "<ALPHANUM>", "<SINGLE>", "<ALPHANUM>", "<DOUBLE>", "<DOUBLE>", "<DOUBLE>", "<SINGLE>" },
      new int[] { 1, 1, 1, 1, 1, 1, 1,  1,  1,  1,  1 });
  }
  
  /**
   * Non-english text (outside of CJK) is treated normally, according to unicode rules 
   */
  public void testNonIdeographic() throws IOException {
    assertAnalyzesTo(analyzer, "一 روبرت موير",
      new String[] { "一", "روبرت", "موير" },
      new int[] { 0, 2, 8 },
      new int[] { 1, 7, 12 },
      new String[] { "<SINGLE>", "<ALPHANUM>", "<ALPHANUM>" },
      new int[] { 1, 1, 1 });
  }
  
  /**
   * Same as the above, except with a nonspacing mark to show correctness.
   */
  public void testNonIdeographicNonLetter() throws IOException {
    assertAnalyzesTo(analyzer, "一 رُوبرت موير",
      new String[] { "一", "رُوبرت", "موير" },
      new int[] { 0, 2, 9 },
      new int[] { 1, 8, 13 },
      new String[] { "<SINGLE>", "<ALPHANUM>", "<ALPHANUM>" },
      new int[] { 1, 1, 1 });
  }
  
  public void testSurrogates() throws IOException {
    assertAnalyzesTo(analyzer, "𩬅艱鍟䇹愯瀛",
      new String[] { "𩬅艱", "艱鍟", "鍟䇹", "䇹愯", "愯瀛" },
      new int[] { 0, 2, 3, 4, 5 },
      new int[] { 3, 4, 5, 6, 7 },
      new String[] { "<DOUBLE>", "<DOUBLE>", "<DOUBLE>", "<DOUBLE>", "<DOUBLE>" },
      new int[] { 1, 1, 1, 1, 1 });
  }
  
  public void testReusableTokenStream() throws IOException {
    assertAnalyzesToReuse(analyzer, "あいうえおabcかきくけこ",
        new String[] { "あい", "いう", "うえ", "えお", "abc", "かき", "きく", "くけ", "けこ" },
        new int[] { 0, 1, 2, 3, 5,  8,  9, 10, 11 },
        new int[] { 2, 3, 4, 5, 8, 10, 11, 12, 13 },
        new String[] { "<DOUBLE>", "<DOUBLE>", "<DOUBLE>", "<DOUBLE>", "<ALPHANUM>", "<DOUBLE>", "<DOUBLE>", "<DOUBLE>", "<DOUBLE>" },
        new int[] { 1, 1, 1, 1, 1,  1,  1,  1,  1});
    
    assertAnalyzesToReuse(analyzer, "あいうえおabんcかきくけ こ",
        new String[] { "あい", "いう", "うえ", "えお", "ab", "ん", "c", "かき", "きく", "くけ", "こ" },
        new int[] { 0, 1, 2, 3, 5, 7, 8,  9, 10, 11, 14 },
        new int[] { 2, 3, 4, 5, 7, 8, 9, 11, 12, 13, 15 },
        new String[] { "<DOUBLE>", "<DOUBLE>", "<DOUBLE>", "<DOUBLE>", "<ALPHANUM>", "<SINGLE>", "<ALPHANUM>", "<DOUBLE>", "<DOUBLE>", "<DOUBLE>", "<SINGLE>" },
        new int[] { 1, 1, 1, 1, 1, 1, 1,  1,  1,  1,  1 });
  }
  
  public void testSingleChar() throws IOException {
    assertAnalyzesTo(analyzer, "一",
      new String[] { "一" },
      new int[] { 0 },
      new int[] { 1 },
      new String[] { "<SINGLE>" },
      new int[] { 1 });
  }
  
  public void testTokenStream() throws IOException {
    assertAnalyzesTo(analyzer, "一丁丂", 
      new String[] { "一丁", "丁丂"},
      new int[] { 0, 1 },
      new int[] { 2, 3 },
      new String[] { "<DOUBLE>", "<DOUBLE>" },
      new int[] { 1, 1 });
  }
  
  /** test that offsets are correct when mappingcharfilter is previously applied */
  public void testChangedOffsets() throws IOException {
    final NormalizeCharMap.Builder builder = new NormalizeCharMap.Builder();
    builder.add("a", "一二");
    builder.add("b", "二三");
    final NormalizeCharMap norm = builder.build();
    Analyzer analyzer = new Analyzer() {
      @Override
      protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
        Tokenizer tokenizer = new StandardTokenizer(TEST_VERSION_CURRENT, reader);
        return new TokenStreamComponents(tokenizer, new CJKBigramFilter(tokenizer));
      }

      @Override
      protected Reader initReader(Reader reader) {
        return new MappingCharFilter(norm, CharReader.get(reader));
      }
    };
    
    assertAnalyzesTo(analyzer, "ab",
        new String[] { "一二", "二二", "二三" },
        new int[] { 0, 0, 1 },
        new int[] { 1, 1, 2 });
    
    // note: offsets are strange since this is how the charfilter maps them... 
    // before bigramming, the 4 tokens look like:
    //   { 0, 0, 1, 1 },
    //   { 0, 1, 1, 2 }
  }

  private static class FakeStandardTokenizer extends TokenFilter {
    final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
    
    public FakeStandardTokenizer(TokenStream input) {
      super(input);
    }

    @Override
    public boolean incrementToken() throws IOException {
      if (input.incrementToken()) {
        typeAtt.setType(StandardTokenizer.TOKEN_TYPES[StandardTokenizer.IDEOGRAPHIC]);
        return true;
      } else {
        return false;
      }
    }
  }

  public void testSingleChar2() throws Exception {
    Analyzer analyzer = new Analyzer() {

      @Override
      protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
        Tokenizer tokenizer = new MockTokenizer(reader, MockTokenizer.WHITESPACE, false);
        TokenFilter filter = new FakeStandardTokenizer(tokenizer);
        filter = new StopFilter(TEST_VERSION_CURRENT, filter, CharArraySet.EMPTY_SET);
        filter = new CJKBigramFilter(filter);
        return new TokenStreamComponents(tokenizer, filter);
      }
    };
    
    assertAnalyzesTo(analyzer, "一",
        new String[] { "一" },
        new int[] { 0 },
        new int[] { 1 },
        new String[] { "<SINGLE>" },
        new int[] { 1 });
  }
  
  /** blast some random strings through the analyzer */
  public void testRandomStrings() throws Exception {
    checkRandomData(random(), new CJKAnalyzer(TEST_VERSION_CURRENT), 10000*RANDOM_MULTIPLIER);
  }
  
  /** blast some random strings through the analyzer */
  public void testRandomHugeStrings() throws Exception {
    Random random = random();
    checkRandomData(random, new CJKAnalyzer(TEST_VERSION_CURRENT), 200*RANDOM_MULTIPLIER, 8192);
  }
  
  public void testEmptyTerm() throws IOException {
    Analyzer a = new Analyzer() {
      @Override
      protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
        Tokenizer tokenizer = new KeywordTokenizer(reader);
        return new TokenStreamComponents(tokenizer, new CJKBigramFilter(tokenizer));
      }
    };
    checkOneTermReuse(a, "", "");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1133.java