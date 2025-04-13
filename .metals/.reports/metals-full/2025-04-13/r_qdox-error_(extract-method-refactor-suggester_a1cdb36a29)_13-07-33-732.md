error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1169.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1169.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1169.java
text:
```scala
c@@heckRandomData(random(), analyzer, 1000*RANDOM_MULTIPLIER);

package org.apache.lucene.analysis.fr;

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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordTokenizer;

import static org.apache.lucene.analysis.VocabularyAssert.*;

/**
 * Simple tests for {@link FrenchLightStemFilter}
 */
public class TestFrenchLightStemFilter extends BaseTokenStreamTestCase {
  private Analyzer analyzer = new Analyzer() {
    @Override
    protected TokenStreamComponents createComponents(String fieldName,
        Reader reader) {
      Tokenizer source = new MockTokenizer(reader, MockTokenizer.WHITESPACE, false);
      return new TokenStreamComponents(source, new FrenchLightStemFilter(source));
    }
  };
  
  /** Test some examples from the paper */
  public void testExamples() throws IOException {
    checkOneTerm(analyzer, "chevaux", "cheval");
    checkOneTerm(analyzer, "cheval", "cheval");
    
    checkOneTerm(analyzer, "hiboux", "hibou");
    checkOneTerm(analyzer, "hibou", "hibou");
    
    checkOneTerm(analyzer, "chantés", "chant");
    checkOneTerm(analyzer, "chanter", "chant");
    checkOneTerm(analyzer, "chante", "chant");
    checkOneTerm(analyzer, "chant", "chant");
    
    checkOneTerm(analyzer, "baronnes", "baron");
    checkOneTerm(analyzer, "barons", "baron");
    checkOneTerm(analyzer, "baron", "baron");
    
    checkOneTerm(analyzer, "peaux", "peau");
    checkOneTerm(analyzer, "peau", "peau");
    
    checkOneTerm(analyzer, "anneaux", "aneau");
    checkOneTerm(analyzer, "anneau", "aneau");
    
    checkOneTerm(analyzer, "neveux", "neveu");
    checkOneTerm(analyzer, "neveu", "neveu");
    
    checkOneTerm(analyzer, "affreux", "afreu");
    checkOneTerm(analyzer, "affreuse", "afreu");
    
    checkOneTerm(analyzer, "investissement", "investi");
    checkOneTerm(analyzer, "investir", "investi");
    
    checkOneTerm(analyzer, "assourdissant", "asourdi");
    checkOneTerm(analyzer, "assourdir", "asourdi");
    
    checkOneTerm(analyzer, "pratiquement", "pratiqu");
    checkOneTerm(analyzer, "pratique", "pratiqu");
    
    checkOneTerm(analyzer, "administrativement", "administratif");
    checkOneTerm(analyzer, "administratif", "administratif");
    
    checkOneTerm(analyzer, "justificatrice", "justifi");
    checkOneTerm(analyzer, "justificateur", "justifi");
    checkOneTerm(analyzer, "justifier", "justifi");
    
    checkOneTerm(analyzer, "educatrice", "eduqu");
    checkOneTerm(analyzer, "eduquer", "eduqu");
    
    checkOneTerm(analyzer, "communicateur", "comuniqu");
    checkOneTerm(analyzer, "communiquer", "comuniqu");
    
    checkOneTerm(analyzer, "accompagnatrice", "acompagn");
    checkOneTerm(analyzer, "accompagnateur", "acompagn");
    
    checkOneTerm(analyzer, "administrateur", "administr");
    checkOneTerm(analyzer, "administrer", "administr");
    
    checkOneTerm(analyzer, "productrice", "product");
    checkOneTerm(analyzer, "producteur", "product");
    
    checkOneTerm(analyzer, "acheteuse", "achet");
    checkOneTerm(analyzer, "acheteur", "achet");
    
    checkOneTerm(analyzer, "planteur", "plant");
    checkOneTerm(analyzer, "plante", "plant");
    
    checkOneTerm(analyzer, "poreuse", "poreu");
    checkOneTerm(analyzer, "poreux", "poreu");
    
    checkOneTerm(analyzer, "plieuse", "plieu");
    
    checkOneTerm(analyzer, "bijoutière", "bijouti");
    checkOneTerm(analyzer, "bijoutier", "bijouti");
    
    checkOneTerm(analyzer, "caissière", "caisi");
    checkOneTerm(analyzer, "caissier", "caisi");
    
    checkOneTerm(analyzer, "abrasive", "abrasif");
    checkOneTerm(analyzer, "abrasif", "abrasif");
    
    checkOneTerm(analyzer, "folle", "fou");
    checkOneTerm(analyzer, "fou", "fou");
    
    checkOneTerm(analyzer, "personnelle", "person");
    checkOneTerm(analyzer, "personne", "person");
    
    // algo bug: too short length
    //checkOneTerm(analyzer, "personnel", "person");
    
    checkOneTerm(analyzer, "complète", "complet");
    checkOneTerm(analyzer, "complet", "complet");
    
    checkOneTerm(analyzer, "aromatique", "aromat");
    
    checkOneTerm(analyzer, "faiblesse", "faibl");
    checkOneTerm(analyzer, "faible", "faibl");
    
    checkOneTerm(analyzer, "patinage", "patin");
    checkOneTerm(analyzer, "patin", "patin");
    
    checkOneTerm(analyzer, "sonorisation", "sono");
    
    checkOneTerm(analyzer, "ritualisation", "rituel");
    checkOneTerm(analyzer, "rituel", "rituel");
    
    // algo bug: masked by rules above
    //checkOneTerm(analyzer, "colonisateur", "colon");
    
    checkOneTerm(analyzer, "nomination", "nomin");
    
    checkOneTerm(analyzer, "disposition", "dispos");
    checkOneTerm(analyzer, "dispose", "dispos");

    // SOLR-3463 : abusive compression of repeated characters in numbers
    // Trailing repeated char elision :
    checkOneTerm(analyzer, "1234555", "1234555");
    // Repeated char within numbers with more than 4 characters :
    checkOneTerm(analyzer, "12333345", "12333345");
    // Short numbers weren't affected already:
    checkOneTerm(analyzer, "1234", "1234");
    // Ensure behaviour is preserved for words!
    // Trailing repeated char elision :
    checkOneTerm(analyzer, "abcdeff", "abcdef");
    // Repeated char within words with more than 4 characters :
    checkOneTerm(analyzer, "abcccddeef", "abcdef");
    checkOneTerm(analyzer, "créées", "cre");
    // Combined letter and digit repetition
    checkOneTerm(analyzer, "22hh00", "22h00"); // 10:00pm
  }
  
  /** Test against a vocabulary from the reference impl */
  public void testVocabulary() throws IOException {
    assertVocabulary(analyzer, getDataFile("frlighttestdata.zip"), "frlight.txt");
  }
  
  /** blast some random strings through the analyzer */
  public void testRandomStrings() throws Exception {
    checkRandomData(random(), analyzer, 10000*RANDOM_MULTIPLIER);
  }
  
  public void testEmptyTerm() throws IOException {
    Analyzer a = new Analyzer() {
      @Override
      protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
        Tokenizer tokenizer = new KeywordTokenizer(reader);
        return new TokenStreamComponents(tokenizer, new FrenchLightStemFilter(tokenizer));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1169.java