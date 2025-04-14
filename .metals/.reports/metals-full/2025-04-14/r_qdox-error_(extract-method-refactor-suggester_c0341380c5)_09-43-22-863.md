error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3200.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3200.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3200.java
text:
```scala
i@@ndexWriter.shutdown();

package org.apache.lucene.classification.utils;

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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.SlowCompositeReaderWrapper;
import org.apache.lucene.store.BaseDirectoryWrapper;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.TestUtil;
import org.apache.lucene.util.LuceneTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

/**
 * Testcase for {@link org.apache.lucene.classification.utils.DatasetSplitter}
 */
public class DataSplitterTest extends LuceneTestCase {

  private AtomicReader originalIndex;
  private RandomIndexWriter indexWriter;
  private Directory dir;

  private String textFieldName = "text";
  private String classFieldName = "class";
  private String idFieldName = "id";

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();
    dir = newDirectory();
    indexWriter = new RandomIndexWriter(random(), dir);

    FieldType ft = new FieldType(TextField.TYPE_STORED);
    ft.setStoreTermVectors(true);
    ft.setStoreTermVectorOffsets(true);
    ft.setStoreTermVectorPositions(true);

    Analyzer analyzer = new MockAnalyzer(random());

    Document doc;
    Random rnd = random();
    for (int i = 0; i < 100; i++) {
      doc = new Document();
      doc.add(new Field(idFieldName, Integer.toString(i), ft));
      doc.add(new Field(textFieldName, TestUtil.randomUnicodeString(rnd, 1024), ft));
      doc.add(new Field(classFieldName, TestUtil.randomUnicodeString(rnd, 10), ft));
      indexWriter.addDocument(doc, analyzer);
    }

    indexWriter.commit();

    originalIndex = SlowCompositeReaderWrapper.wrap(indexWriter.getReader());

  }

  @Override
  @After
  public void tearDown() throws Exception {
    originalIndex.close();
    indexWriter.close();
    dir.close();
    super.tearDown();
  }


  @Test
  public void testSplitOnAllFields() throws Exception {
    assertSplit(originalIndex, 0.1, 0.1);
  }


  @Test
  public void testSplitOnSomeFields() throws Exception {
    assertSplit(originalIndex, 0.2, 0.35, idFieldName, textFieldName);
  }

  public static void assertSplit(AtomicReader originalIndex, double testRatio, double crossValidationRatio, String... fieldNames) throws Exception {

    BaseDirectoryWrapper trainingIndex = newDirectory();
    BaseDirectoryWrapper testIndex = newDirectory();
    BaseDirectoryWrapper crossValidationIndex = newDirectory();

    try {
      DatasetSplitter datasetSplitter = new DatasetSplitter(testRatio, crossValidationRatio);
      datasetSplitter.split(originalIndex, trainingIndex, testIndex, crossValidationIndex, new MockAnalyzer(random()), fieldNames);

      assertNotNull(trainingIndex);
      assertNotNull(testIndex);
      assertNotNull(crossValidationIndex);

      DirectoryReader trainingReader = DirectoryReader.open(trainingIndex);
      assertTrue((int) (originalIndex.maxDoc() * (1d - testRatio - crossValidationRatio)) == trainingReader.maxDoc());
      DirectoryReader testReader = DirectoryReader.open(testIndex);
      assertTrue((int) (originalIndex.maxDoc() * testRatio) == testReader.maxDoc());
      DirectoryReader cvReader = DirectoryReader.open(crossValidationIndex);
      assertTrue((int) (originalIndex.maxDoc() * crossValidationRatio) == cvReader.maxDoc());

      trainingReader.close();
      testReader.close();
      cvReader.close();
      closeQuietly(trainingReader);
      closeQuietly(testReader);
      closeQuietly(cvReader);
    } finally {
      if (trainingIndex != null) {
        trainingIndex.close();
      }
      if (testIndex != null) {
        testIndex.close();
      }
      if (crossValidationIndex != null) {
        crossValidationIndex.close();
      }
    }
  }

  private static void closeQuietly(IndexReader reader) throws IOException {
    try {
      if (reader != null)
        reader.close();
    } catch (Exception e) {
      // do nothing
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3200.java