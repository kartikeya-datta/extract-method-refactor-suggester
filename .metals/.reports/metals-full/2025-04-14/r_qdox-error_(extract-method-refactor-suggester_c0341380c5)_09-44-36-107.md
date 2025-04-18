error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3199.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3199.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3199.java
text:
```scala
i@@ndexWriter.shutdown();

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
package org.apache.lucene.classification;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.SlowCompositeReaderWrapper;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.TestUtil;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.util.Random;

/**
 * Base class for testing {@link Classifier}s
 */
public abstract class ClassificationTestBase<T> extends LuceneTestCase {
  public final static String POLITICS_INPUT = "Here are some interesting questions and answers about Mitt Romney.. " +
      "If you don't know the answer to the question about Mitt Romney, then simply click on the answer below the question section.";
  public static final BytesRef POLITICS_RESULT = new BytesRef("politics");

  public static final String TECHNOLOGY_INPUT = "Much is made of what the likes of Facebook, Google and Apple know about users." +
      " Truth is, Amazon may know more.";
  public static final BytesRef TECHNOLOGY_RESULT = new BytesRef("technology");

  private RandomIndexWriter indexWriter;
  private Directory dir;
  private FieldType ft;

  String textFieldName;
  String categoryFieldName;
  String booleanFieldName;

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();
    dir = newDirectory();
    indexWriter = new RandomIndexWriter(random(), dir);
    textFieldName = "text";
    categoryFieldName = "cat";
    booleanFieldName = "bool";
    ft = new FieldType(TextField.TYPE_STORED);
    ft.setStoreTermVectors(true);
    ft.setStoreTermVectorOffsets(true);
    ft.setStoreTermVectorPositions(true);
  }

  @Override
  @After
  public void tearDown() throws Exception {
    super.tearDown();
    indexWriter.close();
    dir.close();
  }

  protected void checkCorrectClassification(Classifier<T> classifier, String inputDoc, T expectedResult, Analyzer analyzer, String textFieldName, String classFieldName) throws Exception {
    checkCorrectClassification(classifier, inputDoc, expectedResult, analyzer, textFieldName, classFieldName, null);
  }

  protected void checkCorrectClassification(Classifier<T> classifier, String inputDoc, T expectedResult, Analyzer analyzer, String textFieldName, String classFieldName, Query query) throws Exception {
    AtomicReader atomicReader = null;
    try {
      populateSampleIndex(analyzer);
      atomicReader = SlowCompositeReaderWrapper.wrap(indexWriter.getReader());
      classifier.train(atomicReader, textFieldName, classFieldName, analyzer, query);
      ClassificationResult<T> classificationResult = classifier.assignClass(inputDoc);
      assertNotNull(classificationResult.getAssignedClass());
      assertEquals("got an assigned class of " + classificationResult.getAssignedClass(), expectedResult, classificationResult.getAssignedClass());
      assertTrue("got a not positive score " + classificationResult.getScore(), classificationResult.getScore() > 0);
    } finally {
      if (atomicReader != null)
        atomicReader.close();
    }
  }
  protected void checkOnlineClassification(Classifier<T> classifier, String inputDoc, T expectedResult, Analyzer analyzer, String textFieldName, String classFieldName) throws Exception {
    checkOnlineClassification(classifier, inputDoc, expectedResult, analyzer, textFieldName, classFieldName, null);
  }

  protected void checkOnlineClassification(Classifier<T> classifier, String inputDoc, T expectedResult, Analyzer analyzer, String textFieldName, String classFieldName, Query query) throws Exception {
    AtomicReader atomicReader = null;
    try {
      populateSampleIndex(analyzer);
      atomicReader = SlowCompositeReaderWrapper.wrap(indexWriter.getReader());
      classifier.train(atomicReader, textFieldName, classFieldName, analyzer, query);
      ClassificationResult<T> classificationResult = classifier.assignClass(inputDoc);
      assertNotNull(classificationResult.getAssignedClass());
      assertEquals("got an assigned class of " + classificationResult.getAssignedClass(), expectedResult, classificationResult.getAssignedClass());
      assertTrue("got a not positive score " + classificationResult.getScore(), classificationResult.getScore() > 0);
      updateSampleIndex(analyzer);
      ClassificationResult<T> secondClassificationResult = classifier.assignClass(inputDoc);
      assertEquals(classificationResult.getAssignedClass(), secondClassificationResult.getAssignedClass());
      assertEquals(Double.valueOf(classificationResult.getScore()), Double.valueOf(secondClassificationResult.getScore()));

    } finally {
      if (atomicReader != null)
        atomicReader.close();
    }
  }

  private void populateSampleIndex(Analyzer analyzer) throws IOException {
    indexWriter.deleteAll();
    indexWriter.commit();

    String text;

    Document doc = new Document();
    text = "The traveling press secretary for Mitt Romney lost his cool and cursed at reporters " +
        "who attempted to ask questions of the Republican presidential candidate in a public plaza near the Tomb of " +
        "the Unknown Soldier in Warsaw Tuesday.";
    doc.add(new Field(textFieldName, text, ft));
    doc.add(new Field(categoryFieldName, "politics", ft));
    doc.add(new Field(booleanFieldName, "true", ft));

    indexWriter.addDocument(doc, analyzer);

    doc = new Document();
    text = "Mitt Romney seeks to assure Israel and Iran, as well as Jewish voters in the United" +
        " States, that he will be tougher against Iran's nuclear ambitions than President Barack Obama.";
    doc.add(new Field(textFieldName, text, ft));
    doc.add(new Field(categoryFieldName, "politics", ft));
    doc.add(new Field(booleanFieldName, "true", ft));
    indexWriter.addDocument(doc, analyzer);

    doc = new Document();
    text = "And there's a threshold question that he has to answer for the American people and " +
        "that's whether he is prepared to be commander-in-chief,\" she continued. \"As we look to the past events, we " +
        "know that this raises some questions about his preparedness and we'll see how the rest of his trip goes.\"";
    doc.add(new Field(textFieldName, text, ft));
    doc.add(new Field(categoryFieldName, "politics", ft));
    doc.add(new Field(booleanFieldName, "true", ft));
    indexWriter.addDocument(doc, analyzer);

    doc = new Document();
    text = "Still, when it comes to gun policy, many congressional Democrats have \"decided to " +
        "keep quiet and not go there,\" said Alan Lizotte, dean and professor at the State University of New York at " +
        "Albany's School of Criminal Justice.";
    doc.add(new Field(textFieldName, text, ft));
    doc.add(new Field(categoryFieldName, "politics", ft));
    doc.add(new Field(booleanFieldName, "true", ft));
    indexWriter.addDocument(doc, analyzer);

    doc = new Document();
    text = "Standing amongst the thousands of people at the state Capitol, Jorstad, director of " +
        "technology at the University of Wisconsin-La Crosse, documented the historic moment and shared it with the " +
        "world through the Internet.";
    doc.add(new Field(textFieldName, text, ft));
    doc.add(new Field(categoryFieldName, "technology", ft));
    doc.add(new Field(booleanFieldName, "false", ft));
    indexWriter.addDocument(doc, analyzer);

    doc = new Document();
    text = "So, about all those experts and analysts who've spent the past year or so saying " +
        "Facebook was going to make a phone. A new expert has stepped forward to say it's not going to happen.";
    doc.add(new Field(textFieldName, text, ft));
    doc.add(new Field(categoryFieldName, "technology", ft));
    doc.add(new Field(booleanFieldName, "false", ft));
    indexWriter.addDocument(doc, analyzer);

    doc = new Document();
    text = "More than 400 million people trust Google with their e-mail, and 50 million store files" +
        " in the cloud using the Dropbox service. People manage their bank accounts, pay bills, trade stocks and " +
        "generally transfer or store huge volumes of personal data online.";
    doc.add(new Field(textFieldName, text, ft));
    doc.add(new Field(categoryFieldName, "technology", ft));
    doc.add(new Field(booleanFieldName, "false", ft));
    indexWriter.addDocument(doc, analyzer);

    doc = new Document();
    text = "unlabeled doc";
    doc.add(new Field(textFieldName, text, ft));
    indexWriter.addDocument(doc, analyzer);

    indexWriter.commit();
  }

  protected void checkPerformance(Classifier<T> classifier, Analyzer analyzer, String classFieldName) throws Exception {
    AtomicReader atomicReader = null;
    long trainStart = System.currentTimeMillis();
    try {
      populatePerformanceIndex(analyzer);
      atomicReader = SlowCompositeReaderWrapper.wrap(indexWriter.getReader());
      classifier.train(atomicReader, textFieldName, classFieldName, analyzer);
      long trainEnd = System.currentTimeMillis();
      long trainTime = trainEnd - trainStart;
      assertTrue("training took more than 2 mins : " + trainTime / 1000 + "s", trainTime < 120000);
    } finally {
      if (atomicReader != null)
        atomicReader.close();
    }
  }

  private void populatePerformanceIndex(Analyzer analyzer) throws IOException {
    indexWriter.deleteAll();
    indexWriter.commit();

    FieldType ft = new FieldType(TextField.TYPE_STORED);
    ft.setStoreTermVectors(true);
    ft.setStoreTermVectorOffsets(true);
    ft.setStoreTermVectorPositions(true);
    int docs = 1000;
    Random random = random();
    for (int i = 0; i < docs; i++) {
      boolean b = random.nextBoolean();
      Document doc = new Document();
      doc.add(new Field(textFieldName, createRandomString(random), ft));
      doc.add(new Field(categoryFieldName, b ? "technology" : "politics", ft));
      doc.add(new Field(booleanFieldName, String.valueOf(b), ft));
      indexWriter.addDocument(doc, analyzer);
    }
    indexWriter.commit();
  }

  private String createRandomString(Random random) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < 20; i++) {
      builder.append(TestUtil.randomSimpleString(random, 5));
      builder.append(" ");
    }
    return builder.toString();
  }

  private void updateSampleIndex(Analyzer analyzer) throws Exception {

    String text;

    Document doc = new Document();
    text = "Warren Bennis says John F. Kennedy grasped a key lesson about the presidency that few have followed.";
    doc.add(new Field(textFieldName, text, ft));
    doc.add(new Field(categoryFieldName, "politics", ft));
    doc.add(new Field(booleanFieldName, "true", ft));

    indexWriter.addDocument(doc, analyzer);

    doc = new Document();
    text = "Julian Zelizer says Bill Clinton is still trying to shape his party, years after the White House, while George W. Bush opts for a much more passive role.";
    doc.add(new Field(textFieldName, text, ft));
    doc.add(new Field(categoryFieldName, "politics", ft));
    doc.add(new Field(booleanFieldName, "true", ft));
    indexWriter.addDocument(doc, analyzer);

    doc = new Document();
    text = "Crossfire: Sen. Tim Scott passes on Sen. Lindsey Graham endorsement";
    doc.add(new Field(textFieldName, text, ft));
    doc.add(new Field(categoryFieldName, "politics", ft));
    doc.add(new Field(booleanFieldName, "true", ft));
    indexWriter.addDocument(doc, analyzer);

    doc = new Document();
    text = "Illinois becomes 16th state to allow same-sex marriage.";
    doc.add(new Field(textFieldName, text, ft));
    doc.add(new Field(categoryFieldName, "politics", ft));
    doc.add(new Field(booleanFieldName, "true", ft));
    indexWriter.addDocument(doc, analyzer);

    doc = new Document();
    text = "Apple is developing iPhones with curved-glass screens and enhanced sensors that detect different levels of pressure, according to a new report.";
    doc.add(new Field(textFieldName, text, ft));
    doc.add(new Field(categoryFieldName, "technology", ft));
    doc.add(new Field(booleanFieldName, "false", ft));
    indexWriter.addDocument(doc, analyzer);

    doc = new Document();
    text = "The Xbox One is Microsoft's first new gaming console in eight years. It's a quality piece of hardware but it's also noteworthy because Microsoft is using it to make a statement.";
    doc.add(new Field(textFieldName, text, ft));
    doc.add(new Field(categoryFieldName, "technology", ft));
    doc.add(new Field(booleanFieldName, "false", ft));
    indexWriter.addDocument(doc, analyzer);

    doc = new Document();
    text = "Google says it will replace a Google Maps image after a California father complained it shows the body of his teen-age son, who was shot to death in 2009.";
    doc.add(new Field(textFieldName, text, ft));
    doc.add(new Field(categoryFieldName, "technology", ft));
    doc.add(new Field(booleanFieldName, "false", ft));
    indexWriter.addDocument(doc, analyzer);

    doc = new Document();
    text = "second unlabeled doc";
    doc.add(new Field(textFieldName, text, ft));
    indexWriter.addDocument(doc, analyzer);

    indexWriter.commit();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3199.java