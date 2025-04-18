error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3068.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3068.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3068.java
text:
```scala
w@@riter.shutdown();

package org.apache.lucene.analysis;

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

import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Random;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.DocsAndPositionsEnum;
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.TestUtil;
import org.apache.lucene.util.automaton.Automaton;
import org.apache.lucene.util.automaton.AutomatonTestUtil;
import org.apache.lucene.util.automaton.BasicAutomata;
import org.apache.lucene.util.automaton.BasicOperations;
import org.apache.lucene.util.automaton.CharacterRunAutomaton;
import org.apache.lucene.util.automaton.RegExp;

public class TestMockAnalyzer extends BaseTokenStreamTestCase {

  /** Test a configuration that behaves a lot like WhitespaceAnalyzer */
  public void testWhitespace() throws Exception {
    Analyzer a = new MockAnalyzer(random());
    assertAnalyzesTo(a, "A bc defg hiJklmn opqrstuv wxy z ",
        new String[] { "a", "bc", "defg", "hijklmn", "opqrstuv", "wxy", "z" });
    assertAnalyzesTo(a, "aba cadaba shazam",
        new String[] { "aba", "cadaba", "shazam" });
    assertAnalyzesTo(a, "break on whitespace",
        new String[] { "break", "on", "whitespace" });
  }
  
  /** Test a configuration that behaves a lot like SimpleAnalyzer */
  public void testSimple() throws Exception {
    Analyzer a = new MockAnalyzer(random(), MockTokenizer.SIMPLE, true);
    assertAnalyzesTo(a, "a-bc123 defg+hijklmn567opqrstuv78wxy_z ",
        new String[] { "a", "bc", "defg", "hijklmn", "opqrstuv", "wxy", "z" });
    assertAnalyzesTo(a, "aba4cadaba-Shazam",
        new String[] { "aba", "cadaba", "shazam" });
    assertAnalyzesTo(a, "break+on/Letters",
        new String[] { "break", "on", "letters" });
  }
  
  /** Test a configuration that behaves a lot like KeywordAnalyzer */
  public void testKeyword() throws Exception {
    Analyzer a = new MockAnalyzer(random(), MockTokenizer.KEYWORD, false);
    assertAnalyzesTo(a, "a-bc123 defg+hijklmn567opqrstuv78wxy_z ",
        new String[] { "a-bc123 defg+hijklmn567opqrstuv78wxy_z " });
    assertAnalyzesTo(a, "aba4cadaba-Shazam",
        new String[] { "aba4cadaba-Shazam" });
    assertAnalyzesTo(a, "break+on/Nothing",
        new String[] { "break+on/Nothing" });
    // currently though emits no tokens for empty string: maybe we can do it,
    // but we don't want to emit tokens infinitely...
    assertAnalyzesTo(a, "", new String[0]);
  }
  
  // Test some regular expressions as tokenization patterns
  /** Test a configuration where each character is a term */
  public void testSingleChar() throws Exception {
    CharacterRunAutomaton single =
        new CharacterRunAutomaton(new RegExp(".").toAutomaton());
    Analyzer a = new MockAnalyzer(random(), single, false);
    assertAnalyzesTo(a, "foobar",
        new String[] { "f", "o", "o", "b", "a", "r" },
        new int[] { 0, 1, 2, 3, 4, 5 },
        new int[] { 1, 2, 3, 4, 5, 6 }
    );
    checkRandomData(random(), a, 100);
  }
  
  /** Test a configuration where two characters makes a term */
  public void testTwoChars() throws Exception {
    CharacterRunAutomaton single =
        new CharacterRunAutomaton(new RegExp("..").toAutomaton());
    Analyzer a = new MockAnalyzer(random(), single, false);
    assertAnalyzesTo(a, "foobar",
        new String[] { "fo", "ob", "ar"},
        new int[] { 0, 2, 4 },
        new int[] { 2, 4, 6 }
    );
    // make sure when last term is a "partial" match that end() is correct
    assertTokenStreamContents(a.tokenStream("bogus", "fooba"),
        new String[] { "fo", "ob" },
        new int[] { 0, 2 },
        new int[] { 2, 4 },
        new int[] { 1, 1 },
        new Integer(5)
    );
    checkRandomData(random(), a, 100);
  }
  
  /** Test a configuration where three characters makes a term */
  public void testThreeChars() throws Exception {
    CharacterRunAutomaton single =
        new CharacterRunAutomaton(new RegExp("...").toAutomaton());
    Analyzer a = new MockAnalyzer(random(), single, false);
    assertAnalyzesTo(a, "foobar",
        new String[] { "foo", "bar"},
        new int[] { 0, 3 },
        new int[] { 3, 6 }
    );
    // make sure when last term is a "partial" match that end() is correct
    assertTokenStreamContents(a.tokenStream("bogus", "fooba"),
        new String[] { "foo" },
        new int[] { 0 },
        new int[] { 3 },
        new int[] { 1 },
        new Integer(5)
    );
    checkRandomData(random(), a, 100);
  }
  
  /** Test a configuration where word starts with one uppercase */
  public void testUppercase() throws Exception {
    CharacterRunAutomaton single =
        new CharacterRunAutomaton(new RegExp("[A-Z][a-z]*").toAutomaton());
    Analyzer a = new MockAnalyzer(random(), single, false);
    assertAnalyzesTo(a, "FooBarBAZ",
        new String[] { "Foo", "Bar", "B", "A", "Z"},
        new int[] { 0, 3, 6, 7, 8 },
        new int[] { 3, 6, 7, 8, 9 }
    );
    assertAnalyzesTo(a, "aFooBar",
        new String[] { "Foo", "Bar" },
        new int[] { 1, 4 },
        new int[] { 4, 7 }
    );
    checkRandomData(random(), a, 100);
  }
  
  /** Test a configuration that behaves a lot like StopAnalyzer */
  public void testStop() throws Exception {
    Analyzer a = new MockAnalyzer(random(), MockTokenizer.SIMPLE, true, MockTokenFilter.ENGLISH_STOPSET);
    assertAnalyzesTo(a, "the quick brown a fox",
        new String[] { "quick", "brown", "fox" },
        new int[] { 2, 1, 2 });
  }
  
  /** Test a configuration that behaves a lot like KeepWordFilter */
  public void testKeep() throws Exception {
    CharacterRunAutomaton keepWords = 
      new CharacterRunAutomaton(
          BasicOperations.complement(
              Automaton.union(
                  Arrays.asList(BasicAutomata.makeString("foo"), BasicAutomata.makeString("bar")))));
    Analyzer a = new MockAnalyzer(random(), MockTokenizer.SIMPLE, true, keepWords);
    assertAnalyzesTo(a, "quick foo brown bar bar fox foo",
        new String[] { "foo", "bar", "bar", "foo" },
        new int[] { 2, 2, 1, 2 });
  }
  
  /** Test a configuration that behaves a lot like LengthFilter */
  public void testLength() throws Exception {
    CharacterRunAutomaton length5 = new CharacterRunAutomaton(new RegExp(".{5,}").toAutomaton());
    Analyzer a = new MockAnalyzer(random(), MockTokenizer.WHITESPACE, true, length5);
    assertAnalyzesTo(a, "ok toolong fine notfine",
        new String[] { "ok", "fine" },
        new int[] { 1, 2 });
  }
  
  /** Test MockTokenizer encountering a too long token */
  public void testTooLongToken() throws Exception {
    Analyzer whitespace = new Analyzer() {
      @Override
      protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer t = new MockTokenizer(MockTokenizer.WHITESPACE, false, 5);
        return new TokenStreamComponents(t, t);
      }
    };
    
    assertTokenStreamContents(whitespace.tokenStream("bogus", "test 123 toolong ok "),
        new String[] { "test", "123", "toolo", "ng", "ok" },
        new int[] { 0, 5, 9, 14, 17 },
        new int[] { 4, 8, 14, 16, 19 },
        new Integer(20));
    
    assertTokenStreamContents(whitespace.tokenStream("bogus", "test 123 toolo"),
        new String[] { "test", "123", "toolo" },
        new int[] { 0, 5, 9 },
        new int[] { 4, 8, 14 },
        new Integer(14));
  }
  
  public void testLUCENE_3042() throws Exception {
    String testString = "t";
    
    Analyzer analyzer = new MockAnalyzer(random());
    try (TokenStream stream = analyzer.tokenStream("dummy", testString)) {
      stream.reset();
      while (stream.incrementToken()) {
        // consume
      }
      stream.end();
    }
    
    assertAnalyzesTo(analyzer, testString, new String[] { "t" });
  }

  /** blast some random strings through the analyzer */
  public void testRandomStrings() throws Exception {
    checkRandomData(random(), new MockAnalyzer(random()), atLeast(1000));
  }
  
  /** blast some random strings through differently configured tokenizers */
  public void testRandomRegexps() throws Exception {
    int iters = atLeast(30);
    for (int i = 0; i < iters; i++) {
      final CharacterRunAutomaton dfa = new CharacterRunAutomaton(AutomatonTestUtil.randomAutomaton(random()));
      final boolean lowercase = random().nextBoolean();
      final int limit = TestUtil.nextInt(random(), 0, 500);
      Analyzer a = new Analyzer() {
        @Override
        protected TokenStreamComponents createComponents(String fieldName) {
          Tokenizer t = new MockTokenizer(dfa, lowercase, limit);
          return new TokenStreamComponents(t, t);
        }
      };
      checkRandomData(random(), a, 100);
      a.close();
    }
  }
  
  public void testForwardOffsets() throws Exception {
    int num = atLeast(10000);
    for (int i = 0; i < num; i++) {
      String s = TestUtil.randomHtmlishString(random(), 20);
      StringReader reader = new StringReader(s);
      MockCharFilter charfilter = new MockCharFilter(reader, 2);
      MockAnalyzer analyzer = new MockAnalyzer(random());
      try (TokenStream ts = analyzer.tokenStream("bogus", charfilter)) {
        ts.reset();
        while (ts.incrementToken()) {
          ;
        }
        ts.end();
      }
    }
  }
  
  public void testWrapReader() throws Exception {
    // LUCENE-5153: test that wrapping an analyzer's reader is allowed
    final Random random = random();
    
    final Analyzer delegate = new MockAnalyzer(random);
    Analyzer a = new AnalyzerWrapper(delegate.getReuseStrategy()) {
      
      @Override
      protected Reader wrapReader(String fieldName, Reader reader) {
        return new MockCharFilter(reader, 7);
      }
      
      @Override
      protected TokenStreamComponents wrapComponents(String fieldName, TokenStreamComponents components) {
        return components;
      }
      
      @Override
      protected Analyzer getWrappedAnalyzer(String fieldName) {
        return delegate;
      }
    };
    
    checkOneTerm(a, "abc", "aabc");
  }

  public void testChangeGaps() throws Exception {
    // LUCENE-5324: check that it is possible to change the wrapper's gaps
    final int positionGap = random().nextInt(1000);
    final int offsetGap = random().nextInt(1000);
    final Analyzer delegate = new MockAnalyzer(random());
    final Analyzer a = new AnalyzerWrapper(delegate.getReuseStrategy()) {      
      @Override
      protected Analyzer getWrappedAnalyzer(String fieldName) {
        return delegate;
      }
      @Override
      public int getPositionIncrementGap(String fieldName) {
        return positionGap;
      }
      @Override
      public int getOffsetGap(String fieldName) {
        return offsetGap;
      }
    };

    final RandomIndexWriter writer = new RandomIndexWriter(random(), newDirectory());
    final Document doc = new Document();
    final FieldType ft = new FieldType();
    ft.setIndexed(true);
    ft.setIndexOptions(IndexOptions.DOCS_ONLY);
    ft.setTokenized(true);
    ft.setStoreTermVectors(true);
    ft.setStoreTermVectorPositions(true);
    ft.setStoreTermVectorOffsets(true);
    doc.add(new Field("f", "a", ft));
    doc.add(new Field("f", "a", ft));
    writer.addDocument(doc, a);
    final AtomicReader reader = getOnlySegmentReader(writer.getReader());
    final Fields fields = reader.getTermVectors(0);
    final Terms terms = fields.terms("f");
    final TermsEnum te = terms.iterator(null);
    assertEquals(new BytesRef("a"), te.next());
    final DocsAndPositionsEnum dpe = te.docsAndPositions(null, null);
    assertEquals(0, dpe.nextDoc());
    assertEquals(2, dpe.freq());
    assertEquals(0, dpe.nextPosition());
    assertEquals(0, dpe.startOffset());
    final int endOffset = dpe.endOffset();
    assertEquals(1 + positionGap, dpe.nextPosition());
    assertEquals(1 + endOffset + offsetGap, dpe.endOffset());
    assertEquals(null, te.next());
    reader.close();
    writer.close();
    writer.w.getDirectory().close();
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3068.java