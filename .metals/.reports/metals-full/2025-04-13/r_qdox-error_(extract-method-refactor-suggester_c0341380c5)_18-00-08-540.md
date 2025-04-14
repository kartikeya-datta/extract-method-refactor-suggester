error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3132.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3132.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3132.java
text:
```scala
w@@riter.shutdown();

package org.apache.lucene.search;

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

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class TestNGramPhraseQuery extends LuceneTestCase {

  private static IndexReader reader;
  private static Directory directory;

  @BeforeClass
  public static void beforeClass() throws Exception {
    directory = newDirectory();
    RandomIndexWriter writer = new RandomIndexWriter(random(), directory);
    writer.close();
    reader = DirectoryReader.open(directory);
  }

  @AfterClass
  public static void afterClass() throws Exception {
    reader.close();
    reader = null;
    directory.close();
    directory = null;
  }
  
  public void testRewrite() throws Exception {
    // bi-gram test ABC => AB/BC => AB/BC
    PhraseQuery pq1 = new NGramPhraseQuery(2);
    pq1.add(new Term("f", "AB"));
    pq1.add(new Term("f", "BC"));
    
    Query q = pq1.rewrite(reader);
    assertTrue(q instanceof NGramPhraseQuery);
    assertSame(pq1, q);
    pq1 = (NGramPhraseQuery)q;
    assertArrayEquals(new Term[]{new Term("f", "AB"), new Term("f", "BC")}, pq1.getTerms());
    assertArrayEquals(new int[]{0, 1}, pq1.getPositions());

    // bi-gram test ABCD => AB/BC/CD => AB//CD
    PhraseQuery pq2 = new NGramPhraseQuery(2);
    pq2.add(new Term("f", "AB"));
    pq2.add(new Term("f", "BC"));
    pq2.add(new Term("f", "CD"));
    
    q = pq2.rewrite(reader);
    assertTrue(q instanceof PhraseQuery);
    assertNotSame(pq2, q);
    pq2 = (PhraseQuery)q;
    assertArrayEquals(new Term[]{new Term("f", "AB"), new Term("f", "CD")}, pq2.getTerms());
    assertArrayEquals(new int[]{0, 2}, pq2.getPositions());

    // tri-gram test ABCDEFGH => ABC/BCD/CDE/DEF/EFG/FGH => ABC///DEF//FGH
    PhraseQuery pq3 = new NGramPhraseQuery(3);
    pq3.add(new Term("f", "ABC"));
    pq3.add(new Term("f", "BCD"));
    pq3.add(new Term("f", "CDE"));
    pq3.add(new Term("f", "DEF"));
    pq3.add(new Term("f", "EFG"));
    pq3.add(new Term("f", "FGH"));
    
    q = pq3.rewrite(reader);
    assertTrue(q instanceof PhraseQuery);
    assertNotSame(pq3, q);
    pq3 = (PhraseQuery)q;
    assertArrayEquals(new Term[]{new Term("f", "ABC"), new Term("f", "DEF"), new Term("f", "FGH")}, pq3.getTerms());
    assertArrayEquals(new int[]{0, 3, 5}, pq3.getPositions());
    
    // LUCENE-4970: boosting test
    PhraseQuery pq4 = new NGramPhraseQuery(2);
    pq4.add(new Term("f", "AB"));
    pq4.add(new Term("f", "BC"));
    pq4.add(new Term("f", "CD"));
    pq4.setBoost(100.0F);
    
    q = pq4.rewrite(reader);
    assertNotSame(pq4, q);
    assertEquals(pq4.getBoost(), q.getBoost(), 0.1f);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3132.java