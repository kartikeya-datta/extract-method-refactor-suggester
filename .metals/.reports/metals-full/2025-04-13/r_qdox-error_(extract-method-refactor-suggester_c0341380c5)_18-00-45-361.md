error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3178.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3178.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3178.java
text:
```scala
w@@riter.shutdown();

package org.apache.lucene.search.spell;

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

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.BytesRefIterator;
import org.apache.lucene.util.LuceneTestCase;

/**
 * Test case for LuceneDictionary.
 * It first creates a simple index and then a couple of instances of LuceneDictionary
 * on different fields and checks if all the right text comes back.
 */
public class TestLuceneDictionary extends LuceneTestCase {

  private Directory store;

  private IndexReader indexReader = null;
  private LuceneDictionary ld;
  private BytesRefIterator it;
  private BytesRef spare = new BytesRef();

  @Override
  public void setUp() throws Exception {
    super.setUp();
    store = newDirectory();
    IndexWriter writer = new IndexWriter(store, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random(), MockTokenizer.WHITESPACE, false)));

    Document doc;

    doc = new  Document();
    doc.add(newTextField("aaa", "foo", Field.Store.YES));
    writer.addDocument(doc);

    doc = new  Document();
    doc.add(newTextField("aaa", "foo", Field.Store.YES));
    writer.addDocument(doc);

    doc = new  Document();
    doc.add(newTextField("contents", "Tom", Field.Store.YES));
    writer.addDocument(doc);

    doc = new  Document();
    doc.add(newTextField("contents", "Jerry", Field.Store.YES));
    writer.addDocument(doc);

    doc = new Document();
    doc.add(newTextField("zzz", "bar", Field.Store.YES));
    writer.addDocument(doc);

    writer.forceMerge(1);
    writer.close();
  }

  @Override
  public void tearDown() throws Exception {
    if (indexReader != null)
      indexReader.close();
    store.close();
    super.tearDown();
  }
  
  public void testFieldNonExistent() throws IOException {
    try {
      indexReader = DirectoryReader.open(store);

      ld = new LuceneDictionary(indexReader, "nonexistent_field");
      it = ld.getEntryIterator();

      assertNull("More elements than expected", spare = it.next());
    } finally {
      if  (indexReader != null) { indexReader.close(); }
    }
  }

  public void testFieldAaa() throws IOException {
    try {
      indexReader = DirectoryReader.open(store);

      ld = new LuceneDictionary(indexReader, "aaa");
      it = ld.getEntryIterator();
      assertNotNull("First element doesn't exist.", spare = it.next());
      assertTrue("First element isn't correct", spare.utf8ToString().equals("foo"));
      assertNull("More elements than expected", it.next());
    } finally {
      if  (indexReader != null) { indexReader.close(); }
    }
  }

  public void testFieldContents_1() throws IOException {
    try {
      indexReader = DirectoryReader.open(store);

      ld = new LuceneDictionary(indexReader, "contents");
      it = ld.getEntryIterator();

      assertNotNull("First element doesn't exist.", spare = it.next());
      assertTrue("First element isn't correct", spare.utf8ToString().equals("Jerry"));
      assertNotNull("Second element doesn't exist.", spare = it.next());
      assertTrue("Second element isn't correct", spare.utf8ToString().equals("Tom"));
      assertNull("More elements than expected", it.next());

      ld = new LuceneDictionary(indexReader, "contents");
      it = ld.getEntryIterator();

      int counter = 2;
      while (it.next() != null) {
        counter--;
      }

      assertTrue("Number of words incorrect", counter == 0);
    }
    finally {
      if  (indexReader != null) { indexReader.close(); }
    }
  }

  public void testFieldContents_2() throws IOException {
    try {
      indexReader = DirectoryReader.open(store);

      ld = new LuceneDictionary(indexReader, "contents");
      it = ld.getEntryIterator();

      // just iterate through words
      assertEquals("First element isn't correct", "Jerry", it.next().utf8ToString());
      assertEquals("Second element isn't correct",  "Tom", it.next().utf8ToString());
      assertNull("Nonexistent element is really null", it.next());
    }
    finally {
      if  (indexReader != null) { indexReader.close(); }
    }
  }

  public void testFieldZzz() throws IOException {
    try {
      indexReader = DirectoryReader.open(store);

      ld = new LuceneDictionary(indexReader, "zzz");
      it = ld.getEntryIterator();

      assertNotNull("First element doesn't exist.", spare = it.next());
      assertEquals("First element isn't correct", "bar", spare.utf8ToString());
      assertNull("More elements than expected", it.next());
    }
    finally {
      if  (indexReader != null) { indexReader.close(); }
    }
  }
  
  public void testSpellchecker() throws IOException {
    Directory dir = newDirectory();
    SpellChecker sc = new SpellChecker(dir);
    indexReader = DirectoryReader.open(store);
    sc.indexDictionary(new LuceneDictionary(indexReader, "contents"), newIndexWriterConfig(TEST_VERSION_CURRENT, null), false);
    String[] suggestions = sc.suggestSimilar("Tam", 1);
    assertEquals(1, suggestions.length);
    assertEquals("Tom", suggestions[0]);
    suggestions = sc.suggestSimilar("Jarry", 1);
    assertEquals(1, suggestions.length);
    assertEquals("Jerry", suggestions[0]);
    indexReader.close();
    sc.close();
    dir.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3178.java