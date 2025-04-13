error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1171.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1171.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1171.java
text:
```scala
c@@heckRandomData(random(), new DutchAnalyzer(TEST_VERSION_CURRENT), 1000*RANDOM_MULTIPLIER);

package org.apache.lucene.analysis.nl;

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

import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;

/**
 * Test the Dutch Stem Filter, which only modifies the term text.
 * 
 * The code states that it uses the snowball algorithm, but tests reveal some differences.
 * 
 */
public class TestDutchStemmer extends BaseTokenStreamTestCase {
  
  public void testWithSnowballExamples() throws Exception {
	 check("lichaamsziek", "lichaamsziek");
	 check("lichamelijk", "licham");
	 check("lichamelijke", "licham");
	 check("lichamelijkheden", "licham");
	 check("lichamen", "licham");
	 check("lichere", "licher");
	 check("licht", "licht");
	 check("lichtbeeld", "lichtbeeld");
	 check("lichtbruin", "lichtbruin");
	 check("lichtdoorlatende", "lichtdoorlat");
	 check("lichte", "licht");
	 check("lichten", "licht");
	 check("lichtende", "lichtend");
	 check("lichtenvoorde", "lichtenvoord");
	 check("lichter", "lichter");
	 check("lichtere", "lichter");
	 check("lichters", "lichter");
	 check("lichtgevoeligheid", "lichtgevoel");
	 check("lichtgewicht", "lichtgewicht");
	 check("lichtgrijs", "lichtgrijs");
	 check("lichthoeveelheid", "lichthoevel");
	 check("lichtintensiteit", "lichtintensiteit");
	 check("lichtje", "lichtj");
	 check("lichtjes", "lichtjes");
	 check("lichtkranten", "lichtkrant");
	 check("lichtkring", "lichtkring");
	 check("lichtkringen", "lichtkring");
	 check("lichtregelsystemen", "lichtregelsystem");
	 check("lichtste", "lichtst");
	 check("lichtstromende", "lichtstrom");
	 check("lichtte", "licht");
	 check("lichtten", "licht");
	 check("lichttoetreding", "lichttoetred");
	 check("lichtverontreinigde", "lichtverontreinigd");
	 check("lichtzinnige", "lichtzinn");
	 check("lid", "lid");
	 check("lidia", "lidia");
	 check("lidmaatschap", "lidmaatschap");
	 check("lidstaten", "lidstat");
	 check("lidvereniging", "lidveren");
	 check("opgingen", "opging");
	 check("opglanzing", "opglanz");
	 check("opglanzingen", "opglanz");
	 check("opglimlachten", "opglimlacht");
	 check("opglimpen", "opglimp");
	 check("opglimpende", "opglimp");
	 check("opglimping", "opglimp");
	 check("opglimpingen", "opglimp");
	 check("opgraven", "opgrav");
	 check("opgrijnzen", "opgrijnz");
	 check("opgrijzende", "opgrijz");
	 check("opgroeien", "opgroei");
	 check("opgroeiende", "opgroei");
	 check("opgroeiplaats", "opgroeiplat");
	 check("ophaal", "ophal");
	 check("ophaaldienst", "ophaaldienst");
	 check("ophaalkosten", "ophaalkost");
	 check("ophaalsystemen", "ophaalsystem");
	 check("ophaalt", "ophaalt");
	 check("ophaaltruck", "ophaaltruck");
	 check("ophalen", "ophal");
	 check("ophalend", "ophal");
	 check("ophalers", "ophaler");
	 check("ophef", "ophef");
	 check("opheldering", "ophelder");
	 check("ophemelde", "ophemeld");
	 check("ophemelen", "ophemel");
	 check("opheusden", "opheusd");
	 check("ophief", "ophief");
	 check("ophield", "ophield");
	 check("ophieven", "ophiev");
	 check("ophoepelt", "ophoepelt");
	 check("ophoog", "ophog");
	 check("ophoogzand", "ophoogzand");
	 check("ophopen", "ophop");
	 check("ophoping", "ophop");
	 check("ophouden", "ophoud");
  }
  
  public void testSnowballCorrectness() throws Exception {
    Analyzer a = new DutchAnalyzer(TEST_VERSION_CURRENT);
    checkOneTermReuse(a, "opheffen", "opheff");
    checkOneTermReuse(a, "opheffende", "opheff");
    checkOneTermReuse(a, "opheffing", "opheff");
  }
  
  public void testReusableTokenStream() throws Exception {
    Analyzer a = new DutchAnalyzer(TEST_VERSION_CURRENT); 
    checkOneTermReuse(a, "lichaamsziek", "lichaamsziek");
    checkOneTermReuse(a, "lichamelijk", "licham");
    checkOneTermReuse(a, "lichamelijke", "licham");
    checkOneTermReuse(a, "lichamelijkheden", "licham");
  }
  
  public void testExclusionTableViaCtor() throws IOException {
    CharArraySet set = new CharArraySet(TEST_VERSION_CURRENT, 1, true);
    set.add("lichamelijk");
    DutchAnalyzer a = new DutchAnalyzer(TEST_VERSION_CURRENT, CharArraySet.EMPTY_SET, set);
    assertAnalyzesToReuse(a, "lichamelijk lichamelijke", new String[] { "lichamelijk", "licham" });
    
    a = new DutchAnalyzer(TEST_VERSION_CURRENT, CharArraySet.EMPTY_SET, set);
    assertAnalyzesTo(a, "lichamelijk lichamelijke", new String[] { "lichamelijk", "licham" });

  }
  
  /** 
   * check that the default stem overrides are used
   * even if you use a non-default ctor.
   */
  public void testStemOverrides() throws IOException {
    DutchAnalyzer a = new DutchAnalyzer(TEST_VERSION_CURRENT, CharArraySet.EMPTY_SET);
    checkOneTerm(a, "fiets", "fiets");
  }
  
  /**
   * Test that stopwords are not case sensitive
   */
  public void testStopwordsCasing() throws IOException {
    DutchAnalyzer a = new DutchAnalyzer(TEST_VERSION_CURRENT);
    assertAnalyzesTo(a, "Zelf", new String[] { });
  }
  
  private void check(final String input, final String expected) throws Exception {
    checkOneTerm(new DutchAnalyzer(TEST_VERSION_CURRENT), input, expected); 
  }
  
  /** blast some random strings through the analyzer */
  public void testRandomStrings() throws Exception {
    checkRandomData(random(), new DutchAnalyzer(TEST_VERSION_CURRENT), 10000*RANDOM_MULTIPLIER);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1171.java