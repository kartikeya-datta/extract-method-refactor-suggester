error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1736.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1736.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1736.java
text:
```scala
S@@tringBuilder buffer = new StringBuilder();

package org.apache.lucene.analysis.ja;

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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.CharFilter;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.Tokenizer;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class TestJapaneseIterationMarkCharFilter extends BaseTokenStreamTestCase {

  private Analyzer keywordAnalyzer = new Analyzer() {
    @Override
    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
      Tokenizer tokenizer = new MockTokenizer(reader, MockTokenizer.KEYWORD, false);
      return new TokenStreamComponents(tokenizer, tokenizer);
    }

    @Override
    protected Reader initReader(String fieldName, Reader reader) {
      return new JapaneseIterationMarkCharFilter(reader);
    }
  };

  private Analyzer japaneseAnalyzer = new Analyzer() {
    @Override
    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
      Tokenizer tokenizer = new JapaneseTokenizer(reader, null, false, JapaneseTokenizer.Mode.SEARCH);
      return new TokenStreamComponents(tokenizer, tokenizer);
    }

    @Override
    protected Reader initReader(String fieldName, Reader reader) {
      return new JapaneseIterationMarkCharFilter(reader);
    }
  };
  
  public void testKanji() throws IOException {
    // Test single repetition
    assertAnalyzesTo(keywordAnalyzer, "時々", new String[]{"時時"});
    assertAnalyzesTo(japaneseAnalyzer, "時々", new String[]{"時時"});

    // Test multiple repetitions
    assertAnalyzesTo(keywordAnalyzer, "馬鹿々々しい", new String[]{"馬鹿馬鹿しい"});
    assertAnalyzesTo(japaneseAnalyzer, "馬鹿々々しい", new String[]{"馬鹿馬鹿しい"});
  }

  public void testKatakana() throws IOException {
    // Test single repetition
    assertAnalyzesTo(keywordAnalyzer, "ミスヾ", new String[]{"ミスズ"});
    assertAnalyzesTo(japaneseAnalyzer, "ミスヾ", new String[]{"ミ", "スズ"}); // Side effect
  }

  public void testHiragana() throws IOException {
    // Test single unvoiced iteration
    assertAnalyzesTo(keywordAnalyzer, "おゝの", new String[]{"おおの"});
    assertAnalyzesTo(japaneseAnalyzer, "おゝの", new String[]{"お", "おの"}); // Side effect

    // Test single voiced iteration
    assertAnalyzesTo(keywordAnalyzer, "みすゞ", new String[]{"みすず"});
    assertAnalyzesTo(japaneseAnalyzer, "みすゞ", new String[]{"みすず"});

    // Test single voiced iteration
    assertAnalyzesTo(keywordAnalyzer, "じゞ", new String[]{"じじ"});
    assertAnalyzesTo(japaneseAnalyzer, "じゞ", new String[]{"じじ"});

    // Test single unvoiced iteration with voiced iteration
    assertAnalyzesTo(keywordAnalyzer, "じゝ", new String[]{"じし"});
    assertAnalyzesTo(japaneseAnalyzer, "じゝ", new String[]{"じし"});

    // Test multiple repetitions with voiced iteration
    assertAnalyzesTo(keywordAnalyzer, "ところゞゝゝ", new String[]{"ところどころ"});
    assertAnalyzesTo(japaneseAnalyzer, "ところゞゝゝ", new String[]{"ところどころ"});
  }

  public void testMalformed() throws IOException {
    // We can't iterate c here, so emit as it is
    assertAnalyzesTo(keywordAnalyzer, "abcところゝゝゝゝ", new String[]{"abcところcところ"});

    // We can't iterate c (with dakuten change) here, so emit it as-is
    assertAnalyzesTo(keywordAnalyzer, "abcところゞゝゝゝ", new String[]{"abcところcところ"});

    // We can't iterate before beginning of stream, so emit characters as-is
    assertAnalyzesTo(keywordAnalyzer, "ところゞゝゝゞゝゞ", new String[]{"ところどころゞゝゞ"});

    // We can't iterate an iteration mark only, so emit as-is
    assertAnalyzesTo(keywordAnalyzer, "々", new String[]{"々"});
    assertAnalyzesTo(keywordAnalyzer, "ゞ", new String[]{"ゞ"});
    assertAnalyzesTo(keywordAnalyzer, "ゞゝ", new String[]{"ゞゝ"});

    // We can't iterate a full stop punctuation mark (because we use it as a flush marker)
    assertAnalyzesTo(keywordAnalyzer, "。ゝ", new String[]{"。ゝ"});
    assertAnalyzesTo(keywordAnalyzer, "。。ゝゝ", new String[]{"。。ゝゝ"});

    // We can iterate other punctuation marks
    assertAnalyzesTo(keywordAnalyzer, "？ゝ", new String[]{"？？"});

    // We can not get a dakuten variant of ぽ -- this is also a corner case test for inside()
    assertAnalyzesTo(keywordAnalyzer, "ねやぽゞつむぴ", new String[]{"ねやぽぽつむぴ"});
    assertAnalyzesTo(keywordAnalyzer, "ねやぽゝつむぴ", new String[]{"ねやぽぽつむぴ"});
  }

  public void testEmpty() throws IOException {
    // Empty input stays empty
    assertAnalyzesTo(keywordAnalyzer, "", new String[0]);
    assertAnalyzesTo(japaneseAnalyzer, "", new String[0]);
  }

  public void testFullStop() throws IOException {
    // Test full stops   
    assertAnalyzesTo(keywordAnalyzer, "。", new String[]{"。"});
    assertAnalyzesTo(keywordAnalyzer, "。。", new String[]{"。。"});
    assertAnalyzesTo(keywordAnalyzer, "。。。", new String[]{"。。。"});
  }

  public void testKanjiOnly() throws IOException {
    // Test kanji only repetition marks
    CharFilter filter = new JapaneseIterationMarkCharFilter(
        new StringReader("時々、おゝのさんと一緒にお寿司が食べたいです。abcところゞゝゝ。"),
        true, // kanji
        false // no kana
    );
    assertCharFilterEquals(filter, "時時、おゝのさんと一緒にお寿司が食べたいです。abcところゞゝゝ。");
  }

  public void testKanaOnly() throws IOException {
    // Test kana only repetition marks
    CharFilter filter = new JapaneseIterationMarkCharFilter(
        new StringReader("時々、おゝのさんと一緒にお寿司が食べたいです。abcところゞゝゝ。"),
        false, // no kanji
        true   // kana
    );
    assertCharFilterEquals(filter, "時々、おおのさんと一緒にお寿司が食べたいです。abcところどころ。");
  }

  public void testNone() throws IOException {
    // Test no repetition marks
    CharFilter filter = new JapaneseIterationMarkCharFilter(
        new StringReader("時々、おゝのさんと一緒にお寿司が食べたいです。abcところゞゝゝ。"),
        false, // no kanji
        false  // no kana
    );
    assertCharFilterEquals(filter, "時々、おゝのさんと一緒にお寿司が食べたいです。abcところゞゝゝ。");
  }

  public void testCombinations() throws IOException {
    assertAnalyzesTo(keywordAnalyzer, "時々、おゝのさんと一緒にお寿司を食べに行きます。",
        new String[]{"時時、おおのさんと一緒にお寿司を食べに行きます。"}
    );
  }
  
  public void testHiraganaCoverage() throws IOException {
    // Test all hiragana iteration variants
    String source = "かゝがゝきゝぎゝくゝぐゝけゝげゝこゝごゝさゝざゝしゝじゝすゝずゝせゝぜゝそゝぞゝたゝだゝちゝぢゝつゝづゝてゝでゝとゝどゝはゝばゝひゝびゝふゝぶゝへゝべゝほゝぼゝ";
    String target = "かかがかききぎきくくぐくけけげけここごこささざさししじしすすずすせせぜせそそぞそたただたちちぢちつつづつててでてととどとははばはひひびひふふぶふへへべへほほぼほ";
    assertAnalyzesTo(keywordAnalyzer, source, new String[]{target});

    // Test all hiragana iteration variants with dakuten
    source = "かゞがゞきゞぎゞくゞぐゞけゞげゞこゞごゞさゞざゞしゞじゞすゞずゞせゞぜゞそゞぞゞたゞだゞちゞぢゞつゞづゞてゞでゞとゞどゞはゞばゞひゞびゞふゞぶゞへゞべゞほゞぼゞ";
    target = "かがががきぎぎぎくぐぐぐけげげげこごごごさざざざしじじじすずずずせぜぜぜそぞぞぞただだだちぢぢぢつづづづてでででとどどどはばばばひびびびふぶぶぶへべべべほぼぼぼ";
    assertAnalyzesTo(keywordAnalyzer, source, new String[]{target});
  }

  public void testKatakanaCoverage() throws IOException {
    // Test all katakana iteration variants
    String source = "カヽガヽキヽギヽクヽグヽケヽゲヽコヽゴヽサヽザヽシヽジヽスヽズヽセヽゼヽソヽゾヽタヽダヽチヽヂヽツヽヅヽテヽデヽトヽドヽハヽバヽヒヽビヽフヽブヽヘヽベヽホヽボヽ";
    String target = "カカガカキキギキククグクケケゲケココゴコササザサシシジシススズスセセゼセソソゾソタタダタチチヂチツツヅツテテデテトトドトハハバハヒヒビヒフフブフヘヘベヘホホボホ";
    assertAnalyzesTo(keywordAnalyzer, source, new String[]{target});

    // Test all katakana iteration variants with dakuten
    source = "カヾガヾキヾギヾクヾグヾケヾゲヾコヾゴヾサヾザヾシヾジヾスヾズヾセヾゼヾソヾゾヾタヾダヾチヾヂヾツヾヅヾテヾデヾトヾドヾハヾバヾヒヾビヾフヾブヾヘヾベヾホヾボヾ";
    target = "カガガガキギギギクグググケゲゲゲコゴゴゴサザザザシジジジスズズズセゼゼゼソゾゾゾタダダダチヂヂヂツヅヅヅテデデデトドドドハバババヒビビビフブブブヘベベベホボボボ";
    assertAnalyzesTo(keywordAnalyzer, source, new String[]{target});
  }
    
  public void testRandomStrings() throws Exception {
    // Blast some random strings through
    checkRandomData(random(), keywordAnalyzer, 1000 * RANDOM_MULTIPLIER);
  }
  
  public void testRandomHugeStrings() throws Exception {
    // Blast some random strings through
    checkRandomData(random(), keywordAnalyzer, 100 * RANDOM_MULTIPLIER, 8192);
  }

  private void assertCharFilterEquals(CharFilter filter, String expected) throws IOException {
    String actual = readFully(filter);
    assertEquals(expected, actual);
  }

  private String readFully(Reader stream) throws IOException {
    StringBuffer buffer = new StringBuffer();
    int ch;
    while ((ch = stream.read()) != -1) {
      buffer.append((char) ch);
    }
    return buffer.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1736.java