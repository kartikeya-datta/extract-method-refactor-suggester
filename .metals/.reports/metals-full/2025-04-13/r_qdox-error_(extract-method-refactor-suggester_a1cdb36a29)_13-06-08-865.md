error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1971.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1971.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1971.java
text:
```scala
I@@ndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(Version.LUCENE_50, analyzer));

package org.apache.lucene.analysis.uima;

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

import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

/**
 * Testcase for {@link UIMABaseAnalyzer}
 */
public class UIMABaseAnalyzerTest extends BaseTokenStreamTestCase {

  private UIMABaseAnalyzer analyzer;

  @Before
  public void setUp() throws Exception {
    super.setUp();
    analyzer = new UIMABaseAnalyzer("/uima/AggregateSentenceAE.xml", "org.apache.uima.TokenAnnotation");
  }

  @After
  public void tearDown() throws Exception {
    analyzer.close();
    super.tearDown();
  }

  @Test
  public void baseUIMAAnalyzerStreamTest() throws Exception {
    TokenStream ts = analyzer.tokenStream("text", new StringReader("the big brown fox jumped on the wood"));
    assertTokenStreamContents(ts, new String[]{"the", "big", "brown", "fox", "jumped", "on", "the", "wood"});
  }

  @Test
  public void baseUIMAAnalyzerIntegrationTest() throws Exception {
    Directory dir = new RAMDirectory();
    IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(Version.LUCENE_40, analyzer));
    // add the first doc
    Document doc = new Document();
    String dummyTitle = "this is a dummy title ";
    doc.add(new TextField("title", dummyTitle, Field.Store.YES));
    String dummyContent = "there is some content written here";
    doc.add(new TextField("contents", dummyContent, Field.Store.YES));
    writer.addDocument(doc, analyzer);
    writer.commit();

    // try the search over the first doc
    DirectoryReader directoryReader = DirectoryReader.open(dir);
    IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
    TopDocs result = indexSearcher.search(new MatchAllDocsQuery(), 1);
    assertTrue(result.totalHits > 0);
    Document d = indexSearcher.doc(result.scoreDocs[0].doc);
    assertNotNull(d);
    assertNotNull(d.getField("title"));
    assertEquals(dummyTitle, d.getField("title").stringValue());
    assertNotNull(d.getField("contents"));
    assertEquals(dummyContent, d.getField("contents").stringValue());

    // add a second doc
    doc = new Document();
    String dogmasTitle = "dogmas";
    doc.add(new TextField("title", dogmasTitle, Field.Store.YES));
    String dogmasContents = "white men can't jump";
    doc.add(new TextField("contents", dogmasContents, Field.Store.YES));
    writer.addDocument(doc, analyzer);
    writer.commit();

    directoryReader.close();
    directoryReader = DirectoryReader.open(dir);
    indexSearcher = new IndexSearcher(directoryReader);
    result = indexSearcher.search(new MatchAllDocsQuery(), 2);
    Document d1 = indexSearcher.doc(result.scoreDocs[1].doc);
    assertNotNull(d1);
    assertNotNull(d1.getField("title"));
    assertEquals(dogmasTitle, d1.getField("title").stringValue());
    assertNotNull(d1.getField("contents"));
    assertEquals(dogmasContents, d1.getField("contents").stringValue());

    // do a matchalldocs query to retrieve both docs
    indexSearcher = new IndexSearcher(directoryReader);
    result = indexSearcher.search(new MatchAllDocsQuery(), 2);
    assertEquals(2, result.totalHits);
    writer.close();
    indexSearcher.getIndexReader().close();
    dir.close();
  }

  @Test
  public void testRandomStrings() throws Exception {
    checkRandomData(random(), new UIMABaseAnalyzer("/uima/TestAggregateSentenceAE.xml", "org.apache.lucene.uima.ts.TokenAnnotation"),
        100 * RANDOM_MULTIPLIER);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1971.java