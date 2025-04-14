error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1166.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1166.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1166.java
text:
```scala
c@@heckRandomData(random(), new BrazilianAnalyzer(TEST_VERSION_CURRENT), 1000*RANDOM_MULTIPLIER);

package org.apache.lucene.analysis.br;

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
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.core.LowerCaseTokenizer;
import org.apache.lucene.analysis.miscellaneous.KeywordMarkerFilter;
import org.apache.lucene.analysis.util.CharArraySet;

/**
 * Test the Brazilian Stem Filter, which only modifies the term text.
 * 
 * It is very similar to the snowball portuguese algorithm but not exactly the same.
 *
 */
public class TestBrazilianStemmer extends BaseTokenStreamTestCase {
  
  public void testWithSnowballExamples() throws Exception {
	 check("boa", "boa");
	 check("boainain", "boainain");
	 check("boas", "boas");
	 check("bôas", "boas"); // removes diacritic: different from snowball portugese
	 check("boassu", "boassu");
	 check("boataria", "boat");
	 check("boate", "boat");
	 check("boates", "boat");
	 check("boatos", "boat");
	 check("bob", "bob");
	 check("boba", "bob");
	 check("bobagem", "bobag");
	 check("bobagens", "bobagens");
	 check("bobalhões", "bobalho"); // removes diacritic: different from snowball portugese
	 check("bobear", "bob");
	 check("bobeira", "bobeir");
	 check("bobinho", "bobinh");
	 check("bobinhos", "bobinh");
	 check("bobo", "bob");
	 check("bobs", "bobs");
	 check("boca", "boc");
	 check("bocadas", "boc");
	 check("bocadinho", "bocadinh");
	 check("bocado", "boc");
	 check("bocaiúva", "bocaiuv"); // removes diacritic: different from snowball portuguese
	 check("boçal", "bocal"); // removes diacritic: different from snowball portuguese
	 check("bocarra", "bocarr");
	 check("bocas", "boc");
	 check("bode", "bod");
	 check("bodoque", "bodoqu");
	 check("body", "body");
	 check("boeing", "boeing");
	 check("boem", "boem");
	 check("boemia", "boem");
	 check("boêmio", "boemi"); // removes diacritic: different from snowball portuguese
	 check("bogotá", "bogot");
	 check("boi", "boi");
	 check("bóia", "boi"); // removes diacritic: different from snowball portuguese
	 check("boiando", "boi");
	 check("quiabo", "quiab");
	 check("quicaram", "quic");
	 check("quickly", "quickly");
	 check("quieto", "quiet");
	 check("quietos", "quiet");
	 check("quilate", "quilat");
	 check("quilates", "quilat");
	 check("quilinhos", "quilinh");
	 check("quilo", "quil");
	 check("quilombo", "quilomb");
	 check("quilométricas", "quilometr"); // removes diacritic: different from snowball portuguese
	 check("quilométricos", "quilometr"); // removes diacritic: different from snowball portuguese
	 check("quilômetro", "quilometr"); // removes diacritic: different from snowball portoguese
	 check("quilômetros", "quilometr"); // removes diacritic: different from snowball portoguese
	 check("quilos", "quil");
	 check("quimica", "quimic");
	 check("quilos", "quil");
	 check("quimica", "quimic");
	 check("quimicas", "quimic");
	 check("quimico", "quimic");
	 check("quimicos", "quimic");
	 check("quimioterapia", "quimioterap");
	 check("quimioterápicos", "quimioterap"); // removes diacritic: different from snowball portoguese
	 check("quimono", "quimon");
	 check("quincas", "quinc");
	 check("quinhão", "quinha"); // removes diacritic: different from snowball portoguese
	 check("quinhentos", "quinhent");
	 check("quinn", "quinn");
	 check("quino", "quin");
	 check("quinta", "quint");
	 check("quintal", "quintal");
	 check("quintana", "quintan");
	 check("quintanilha", "quintanilh");
	 check("quintão", "quinta"); // removes diacritic: different from snowball portoguese
	 check("quintessência", "quintessente"); // versus snowball portuguese 'quintessent'
	 check("quintino", "quintin");
	 check("quinto", "quint");
	 check("quintos", "quint");
	 check("quintuplicou", "quintuplic");
	 check("quinze", "quinz");
	 check("quinzena", "quinzen");
	 check("quiosque", "quiosqu");
  }
  
  public void testNormalization() throws Exception {
    check("Brasil", "brasil"); // lowercase by default
    check("Brasília", "brasil"); // remove diacritics
    check("quimio5terápicos", "quimio5terapicos"); // contains non-letter, diacritic will still be removed
    check("áá", "áá"); // token is too short: diacritics are not removed
    check("ááá", "aaa"); // normally, diacritics are removed
  }
  
  public void testReusableTokenStream() throws Exception {
    Analyzer a = new BrazilianAnalyzer(TEST_VERSION_CURRENT);
    checkReuse(a, "boa", "boa");
    checkReuse(a, "boainain", "boainain");
    checkReuse(a, "boas", "boas");
    checkReuse(a, "bôas", "boas"); // removes diacritic: different from snowball portugese
  }
 
  public void testStemExclusionTable() throws Exception {
    BrazilianAnalyzer a = new BrazilianAnalyzer(TEST_VERSION_CURRENT, 
        CharArraySet.EMPTY_SET, new CharArraySet(TEST_VERSION_CURRENT, asSet("quintessência"), false));
    checkReuse(a, "quintessência", "quintessência"); // excluded words will be completely unchanged.
  }
  
  public void testWithKeywordAttribute() throws IOException {
    CharArraySet set = new CharArraySet(TEST_VERSION_CURRENT, 1, true);
    set.add("Brasília");
    BrazilianStemFilter filter = new BrazilianStemFilter(
        new KeywordMarkerFilter(new LowerCaseTokenizer(TEST_VERSION_CURRENT, new StringReader(
            "Brasília Brasilia")), set));
    assertTokenStreamContents(filter, new String[] { "brasília", "brasil" });
  }

  private void check(final String input, final String expected) throws Exception {
    checkOneTerm(new BrazilianAnalyzer(TEST_VERSION_CURRENT), input, expected);
  }
  
  private void checkReuse(Analyzer a, String input, String expected) throws Exception {
    checkOneTermReuse(a, input, expected);
  }

  /** blast some random strings through the analyzer */
  public void testRandomStrings() throws Exception {
    checkRandomData(random(), new BrazilianAnalyzer(TEST_VERSION_CURRENT), 10000*RANDOM_MULTIPLIER);
  }
  
  public void testEmptyTerm() throws IOException {
    Analyzer a = new Analyzer() {
      @Override
      protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
        Tokenizer tokenizer = new KeywordTokenizer(reader);
        return new TokenStreamComponents(tokenizer, new BrazilianStemFilter(tokenizer));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1166.java