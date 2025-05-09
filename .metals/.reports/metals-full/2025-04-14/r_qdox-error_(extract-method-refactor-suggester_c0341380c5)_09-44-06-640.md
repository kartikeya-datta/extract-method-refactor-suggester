error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3765.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3765.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3765.java
text:
```scala
c@@heckRandomData(random(), a, 200, 20, false, false);

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

package org.apache.lucene.analysis.miscellaneous;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.cz.CzechStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;

import static org.apache.lucene.analysis.miscellaneous.WordDelimiterFilter.*;
import static org.apache.lucene.analysis.miscellaneous.WordDelimiterIterator.DEFAULT_WORD_DELIM_TABLE;

/**
 * New WordDelimiterFilter tests... most of the tests are in ConvertedLegacyTest
 * TODO: should explicitly test things like protWords and not rely on
 * the factory tests in Solr.
 */
public class TestWordDelimiterFilter extends BaseTokenStreamTestCase {

  /***
  public void testPerformance() throws IOException {
    String s = "now is the time-for all good men to come to-the aid of their country.";
    Token tok = new Token();
    long start = System.currentTimeMillis();
    int ret=0;
    for (int i=0; i<1000000; i++) {
      StringReader r = new StringReader(s);
      TokenStream ts = new WhitespaceTokenizer(r);
      ts = new WordDelimiterFilter(ts, 1,1,1,1,0);

      while (ts.next(tok) != null) ret++;
    }

    System.out.println("ret="+ret+" time="+(System.currentTimeMillis()-start));
  }
  ***/

  @Test
  public void testOffsets() throws IOException {
    int flags = GENERATE_WORD_PARTS | GENERATE_NUMBER_PARTS | CATENATE_ALL | SPLIT_ON_CASE_CHANGE | SPLIT_ON_NUMERICS | STEM_ENGLISH_POSSESSIVE;
    // test that subwords and catenated subwords have
    // the correct offsets.
    WordDelimiterFilter wdf = new WordDelimiterFilter(new SingleTokenTokenStream(new Token("foo-bar", 5, 12)), DEFAULT_WORD_DELIM_TABLE, flags, null);

    assertTokenStreamContents(wdf, 
        new String[] { "foo", "bar", "foobar" },
        new int[] { 5, 9, 5 }, 
        new int[] { 8, 12, 12 },
        null, null, null, null, false);

    wdf = new WordDelimiterFilter(new SingleTokenTokenStream(new Token("foo-bar", 5, 6)), DEFAULT_WORD_DELIM_TABLE, flags, null);
    
    assertTokenStreamContents(wdf,
        new String[] { "foo", "bar", "foobar" },
        new int[] { 5, 5, 5 },
        new int[] { 6, 6, 6 },
        null, null, null, null, false);
  }
  
  @Test
  public void testOffsetChange() throws Exception {
    int flags = GENERATE_WORD_PARTS | GENERATE_NUMBER_PARTS | CATENATE_ALL | SPLIT_ON_CASE_CHANGE | SPLIT_ON_NUMERICS | STEM_ENGLISH_POSSESSIVE;
    WordDelimiterFilter wdf = new WordDelimiterFilter(new SingleTokenTokenStream(new Token("übelkeit)", 7, 16)), DEFAULT_WORD_DELIM_TABLE, flags, null);
    
    assertTokenStreamContents(wdf,
        new String[] { "übelkeit" },
        new int[] { 7 },
        new int[] { 15 });
  }
  
  @Test
  public void testOffsetChange2() throws Exception {
    int flags = GENERATE_WORD_PARTS | GENERATE_NUMBER_PARTS | CATENATE_ALL | SPLIT_ON_CASE_CHANGE | SPLIT_ON_NUMERICS | STEM_ENGLISH_POSSESSIVE;
    WordDelimiterFilter wdf = new WordDelimiterFilter(new SingleTokenTokenStream(new Token("(übelkeit", 7, 17)), DEFAULT_WORD_DELIM_TABLE, flags, null);
    
    assertTokenStreamContents(wdf,
        new String[] { "übelkeit" },
        new int[] { 8 },
        new int[] { 17 });
  }
  
  @Test
  public void testOffsetChange3() throws Exception {
    int flags = GENERATE_WORD_PARTS | GENERATE_NUMBER_PARTS | CATENATE_ALL | SPLIT_ON_CASE_CHANGE | SPLIT_ON_NUMERICS | STEM_ENGLISH_POSSESSIVE;
    WordDelimiterFilter wdf = new WordDelimiterFilter(new SingleTokenTokenStream(new Token("(übelkeit", 7, 16)), DEFAULT_WORD_DELIM_TABLE, flags, null);
    
    assertTokenStreamContents(wdf,
        new String[] { "übelkeit" },
        new int[] { 8 },
        new int[] { 16 });
  }
  
  @Test
  public void testOffsetChange4() throws Exception {
    int flags = GENERATE_WORD_PARTS | GENERATE_NUMBER_PARTS | CATENATE_ALL | SPLIT_ON_CASE_CHANGE | SPLIT_ON_NUMERICS | STEM_ENGLISH_POSSESSIVE;
    WordDelimiterFilter wdf = new WordDelimiterFilter(new SingleTokenTokenStream(new Token("(foo,bar)", 7, 16)), DEFAULT_WORD_DELIM_TABLE, flags, null);
    
    assertTokenStreamContents(wdf,
        new String[] { "foo", "bar", "foobar"},
        new int[] { 8, 12, 8 },
        new int[] { 11, 15, 15 },
        null, null, null, null, false);
  }

  public void doSplit(final String input, String... output) throws Exception {
    int flags = GENERATE_WORD_PARTS | GENERATE_NUMBER_PARTS | SPLIT_ON_CASE_CHANGE | SPLIT_ON_NUMERICS | STEM_ENGLISH_POSSESSIVE;
    WordDelimiterFilter wdf = new WordDelimiterFilter(new MockTokenizer(
                new StringReader(input), MockTokenizer.KEYWORD, false), WordDelimiterIterator.DEFAULT_WORD_DELIM_TABLE, flags, null);
    
    assertTokenStreamContents(wdf, output);
  }

  @Test
  public void testSplits() throws Exception {
    doSplit("basic-split","basic","split");
    doSplit("camelCase","camel","Case");

    // non-space marking symbol shouldn't cause split
    // this is an example in Thai    
    doSplit("\u0e1a\u0e49\u0e32\u0e19","\u0e1a\u0e49\u0e32\u0e19");
    // possessive followed by delimiter
    doSplit("test's'", "test");

    // some russian upper and lowercase
    doSplit("Роберт", "Роберт");
    // now cause a split (russian camelCase)
    doSplit("РобЕрт", "Роб", "Ерт");

    // a composed titlecase character, don't split
    doSplit("aǅungla", "aǅungla");
    
    // a modifier letter, don't split
    doSplit("ســـــــــــــــــلام", "ســـــــــــــــــلام");
    
    // enclosing mark, don't split
    doSplit("test⃝", "test⃝");
    
    // combining spacing mark (the virama), don't split
    doSplit("हिन्दी", "हिन्दी");
    
    // don't split non-ascii digits
    doSplit("١٢٣٤", "١٢٣٤");
    
    // don't split supplementaries into unpaired surrogates
    doSplit("𠀀𠀀", "𠀀𠀀");
  }
  
  public void doSplitPossessive(int stemPossessive, final String input, final String... output) throws Exception {
    int flags = GENERATE_WORD_PARTS | GENERATE_NUMBER_PARTS | SPLIT_ON_CASE_CHANGE | SPLIT_ON_NUMERICS;
    flags |= (stemPossessive == 1) ? STEM_ENGLISH_POSSESSIVE : 0;
    WordDelimiterFilter wdf = new WordDelimiterFilter(new MockTokenizer(
        new StringReader(input), MockTokenizer.KEYWORD, false), flags, null);

    assertTokenStreamContents(wdf, output);
  }
  
  /*
   * Test option that allows disabling the special "'s" stemming, instead treating the single quote like other delimiters. 
   */
  @Test
  public void testPossessives() throws Exception {
    doSplitPossessive(1, "ra's", "ra");
    doSplitPossessive(0, "ra's", "ra", "s");
  }
  
  /*
   * Set a large position increment gap of 10 if the token is "largegap" or "/"
   */
  private final class LargePosIncTokenFilter extends TokenFilter {
    private CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private PositionIncrementAttribute posIncAtt = addAttribute(PositionIncrementAttribute.class);
    
    protected LargePosIncTokenFilter(TokenStream input) {
      super(input);
    }

    @Override
    public boolean incrementToken() throws IOException {
      if (input.incrementToken()) {
        if (termAtt.toString().equals("largegap") || termAtt.toString().equals("/"))
          posIncAtt.setPositionIncrement(10);
        return true;
      } else {
        return false;
      }
    }  
  }
  
  @Test
  public void testPositionIncrements() throws Exception {
    final int flags = GENERATE_WORD_PARTS | GENERATE_NUMBER_PARTS | CATENATE_ALL | SPLIT_ON_CASE_CHANGE | SPLIT_ON_NUMERICS | STEM_ENGLISH_POSSESSIVE;
    final CharArraySet protWords = new CharArraySet(TEST_VERSION_CURRENT, new HashSet<String>(Arrays.asList("NUTCH")), false);
    
    /* analyzer that uses whitespace + wdf */
    Analyzer a = new Analyzer() {
      @Override
      public TokenStreamComponents createComponents(String field, Reader reader) {
        Tokenizer tokenizer = new MockTokenizer(reader, MockTokenizer.WHITESPACE, false);
        return new TokenStreamComponents(tokenizer, new WordDelimiterFilter(
            tokenizer,
            flags, protWords));
      }
    };

    /* in this case, works as expected. */
    assertAnalyzesTo(a, "LUCENE / SOLR", new String[] { "LUCENE", "SOLR" },
        new int[] { 0, 9 },
        new int[] { 6, 13 },
        null,
        new int[] { 1, 1 },
        null,
        false);
    
    /* only in this case, posInc of 2 ?! */
    assertAnalyzesTo(a, "LUCENE / solR", new String[] { "LUCENE", "sol", "R", "solR" },
        new int[] { 0, 9, 12, 9 },
        new int[] { 6, 12, 13, 13 },
        null,
        new int[] { 1, 1, 1, 0 },
        null,
        false);
    
    assertAnalyzesTo(a, "LUCENE / NUTCH SOLR", new String[] { "LUCENE", "NUTCH", "SOLR" },
        new int[] { 0, 9, 15 },
        new int[] { 6, 14, 19 },
        null,
        new int[] { 1, 1, 1 },
        null,
        false);
    
    /* analyzer that will consume tokens with large position increments */
    Analyzer a2 = new Analyzer() {
      @Override
      public TokenStreamComponents createComponents(String field, Reader reader) {
        Tokenizer tokenizer = new MockTokenizer(reader, MockTokenizer.WHITESPACE, false);
        return new TokenStreamComponents(tokenizer, new WordDelimiterFilter(
            new LargePosIncTokenFilter(tokenizer),
            flags, protWords));
      }
    };
    
    /* increment of "largegap" is preserved */
    assertAnalyzesTo(a2, "LUCENE largegap SOLR", new String[] { "LUCENE", "largegap", "SOLR" },
        new int[] { 0, 7, 16 },
        new int[] { 6, 15, 20 },
        null,
        new int[] { 1, 10, 1 },
        null,
        false);
    
    /* the "/" had a position increment of 10, where did it go?!?!! */
    assertAnalyzesTo(a2, "LUCENE / SOLR", new String[] { "LUCENE", "SOLR" },
        new int[] { 0, 9 },
        new int[] { 6, 13 },
        null,
        new int[] { 1, 11 },
        null,
        false);
    
    /* in this case, the increment of 10 from the "/" is carried over */
    assertAnalyzesTo(a2, "LUCENE / solR", new String[] { "LUCENE", "sol", "R", "solR" },
        new int[] { 0, 9, 12, 9 },
        new int[] { 6, 12, 13, 13 },
        null,
        new int[] { 1, 11, 1, 0 },
        null,
        false);
    
    assertAnalyzesTo(a2, "LUCENE / NUTCH SOLR", new String[] { "LUCENE", "NUTCH", "SOLR" },
        new int[] { 0, 9, 15 },
        new int[] { 6, 14, 19 },
        null,
        new int[] { 1, 11, 1 },
        null,
        false);

    Analyzer a3 = new Analyzer() {
      @Override
      public TokenStreamComponents createComponents(String field, Reader reader) {
        Tokenizer tokenizer = new MockTokenizer(reader, MockTokenizer.WHITESPACE, false);
        StopFilter filter = new StopFilter(TEST_VERSION_CURRENT,
            tokenizer, StandardAnalyzer.STOP_WORDS_SET);
        filter.setEnablePositionIncrements(true);
        return new TokenStreamComponents(tokenizer, new WordDelimiterFilter(filter, flags, protWords));
      }
    };

    assertAnalyzesTo(a3, "lucene.solr", 
        new String[] { "lucene", "solr", "lucenesolr" },
        new int[] { 0, 7, 0 },
        new int[] { 6, 11, 11 },
        null,
        new int[] { 1, 1, 0 },
        null,
        false);

    /* the stopword should add a gap here */
    assertAnalyzesTo(a3, "the lucene.solr", 
        new String[] { "lucene", "solr", "lucenesolr" }, 
        new int[] { 4, 11, 4 }, 
        new int[] { 10, 15, 15 },
        null,
        new int[] { 2, 1, 0 },
        null,
        false);
  }
  
  /** blast some random strings through the analyzer */
  public void testRandomStrings() throws Exception {
    int numIterations = atLeast(5);
    for (int i = 0; i < numIterations; i++) {
      final int flags = random().nextInt(512);
      final CharArraySet protectedWords;
      if (random().nextBoolean()) {
        protectedWords = new CharArraySet(TEST_VERSION_CURRENT, new HashSet<String>(Arrays.asList("a", "b", "cd")), false);
      } else {
        protectedWords = null;
      }
      
      Analyzer a = new Analyzer() {
        
        @Override
        protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
          Tokenizer tokenizer = new MockTokenizer(reader, MockTokenizer.WHITESPACE, false);
          return new TokenStreamComponents(tokenizer, new WordDelimiterFilter(tokenizer, flags, protectedWords));
        }
      };
      checkRandomData(random(), a, 10000*RANDOM_MULTIPLIER, 20, false, false);
    }
  }
  
  public void testEmptyTerm() throws IOException {
    Random random = random();
    for (int i = 0; i < 512; i++) {
      final int flags = i;
      final CharArraySet protectedWords;
      if (random.nextBoolean()) {
        protectedWords = new CharArraySet(TEST_VERSION_CURRENT, new HashSet<String>(Arrays.asList("a", "b", "cd")), false);
      } else {
        protectedWords = null;
      }
    
      Analyzer a = new Analyzer() { 
        @Override
        protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
          Tokenizer tokenizer = new KeywordTokenizer(reader);
          return new TokenStreamComponents(tokenizer, new WordDelimiterFilter(tokenizer, flags, protectedWords));
        }
      };
      // depending upon options, this thing may or may not preserve the empty term
      checkAnalysisConsistency(random, a, random.nextBoolean(), "");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3765.java