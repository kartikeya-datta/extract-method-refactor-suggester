error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2432.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2432.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2432.java
text:
```scala
c@@heckRandomData(random(), getTestAnalyzer(), 1000 * RANDOM_MULTIPLIER);

package org.apache.lucene.analysis.morfologik;

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
import java.io.StringReader;
import java.util.TreeSet;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/**
 * TODO: The tests below rely on the order of returned lemmas, which is probably not good. 
 */
public class TestMorfologikAnalyzer extends BaseTokenStreamTestCase {

  private Analyzer getTestAnalyzer() {
    return new MorfologikAnalyzer(TEST_VERSION_CURRENT);
  }

  /** Test stemming of single tokens with Morfologik library. */
  public final void testSingleTokens() throws IOException {
    Analyzer a = getTestAnalyzer();
    assertAnalyzesToReuse(a, "a", new String[] { "a" });
    assertAnalyzesToReuse(a, "liście", new String[] { "liście", "liść", "list", "lista" });
    assertAnalyzesToReuse(a, "danych", new String[] { "dany", "dana", "dane", "dać" });
    assertAnalyzesToReuse(a, "ęóąśłżźćń", new String[] { "ęóąśłżźćń" });
  }

  /** Test stemming of multiple tokens and proper term metrics. */
  public final void testMultipleTokens() throws IOException {
    Analyzer a = getTestAnalyzer();
    assertAnalyzesToReuse(
      a,
      "liście danych",
      new String[] { "liście", "liść", "list", "lista", "dany", "dana", "dane", "dać" },
      new int[] { 0, 0, 0, 0, 7, 7, 7, 7 },
      new int[] { 6, 6, 6, 6, 13, 13, 13, 13 },
      new int[] { 1, 0, 0, 0, 1, 0, 0, 0 });
  }

  /** Test reuse of MorfologikFilter with leftover stems. */
  public final void testLeftoverStems() throws IOException {
    Analyzer a = getTestAnalyzer();
    TokenStream ts_1 = a.tokenStream("dummy", new StringReader("liście"));
    CharTermAttribute termAtt_1 = ts_1.getAttribute(CharTermAttribute.class);
    ts_1.reset();
    ts_1.incrementToken();
    assertEquals("first stream", "liście", termAtt_1.toString());

    TokenStream ts_2 = a.tokenStream("dummy", new StringReader("danych"));
    CharTermAttribute termAtt_2 = ts_2.getAttribute(CharTermAttribute.class);
    ts_2.reset();
    ts_2.incrementToken();
    assertEquals("second stream", "dany", termAtt_2.toString());
  }

  /** Test stemming of mixed-case tokens. */
  public final void testCase() throws IOException {
    Analyzer a = getTestAnalyzer();

    assertAnalyzesToReuse(a, "AGD",      new String[] { "AGD", "artykuły gospodarstwa domowego" });
    assertAnalyzesToReuse(a, "agd",      new String[] { "artykuły gospodarstwa domowego" });

    assertAnalyzesToReuse(a, "Poznania", new String[] { "Poznań" });
    assertAnalyzesToReuse(a, "poznania", new String[] { "poznanie", "poznać" });

    assertAnalyzesToReuse(a, "Aarona",   new String[] { "Aaron" });
    assertAnalyzesToReuse(a, "aarona",   new String[] { "aarona" });

    assertAnalyzesToReuse(a, "Liście",   new String[] { "liście", "liść", "list", "lista" });
  }

  private void assertPOSToken(TokenStream ts, String term, String... tags) throws IOException {
    ts.incrementToken();
    assertEquals(term, ts.getAttribute(CharTermAttribute.class).toString());
    
    TreeSet<String> actual = new TreeSet<String>();
    TreeSet<String> expected = new TreeSet<String>();
    for (StringBuilder b : ts.getAttribute(MorphosyntacticTagsAttribute.class).getTags()) {
      actual.add(b.toString());
    }
    for (String s : tags) {
      expected.add(s);
    }
    
    if (!expected.equals(actual)) {
      System.out.println("Expected:\n" + expected);
      System.out.println("Actual:\n" + actual);
      assertEquals(expected, actual);
    }
  }

  /** Test morphosyntactic annotations. */
  public final void testPOSAttribute() throws IOException {
    TokenStream ts = getTestAnalyzer().tokenStream("dummy", new StringReader("liście"));

    assertPOSToken(ts, "liście",  
        "subst:sg:acc:n2",
        "subst:sg:nom:n2",
        "subst:sg:voc:n2");

    assertPOSToken(ts, "liść",  
        "subst:pl:acc:m3",
        "subst:pl:nom:m3",
        "subst:pl:voc:m3");

    assertPOSToken(ts, "list",  
        "subst:sg:loc:m3",
        "subst:sg:voc:m3");

    assertPOSToken(ts, "lista", 
        "subst:sg:dat:f",
        "subst:sg:loc:f");
  }

  /** blast some random strings through the analyzer */
  public void testRandom() throws Exception {
    checkRandomData(random(), getTestAnalyzer(), 10000 * RANDOM_MULTIPLIER); 
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2432.java