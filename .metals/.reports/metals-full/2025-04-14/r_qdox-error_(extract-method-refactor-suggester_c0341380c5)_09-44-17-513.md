error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3685.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3685.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3685.java
text:
```scala
F@@ieldType customType2 = new FieldType(TextField.TYPE_NOT_STORED);

package org.apache.lucene.queries.function;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queries.function.valuesource.ByteFieldSource;
import org.apache.lucene.queries.function.valuesource.FloatFieldSource;
import org.apache.lucene.queries.function.valuesource.IntFieldSource;
import org.apache.lucene.queries.function.valuesource.ShortFieldSource;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util._TestUtil;
import org.junit.AfterClass;
import org.junit.Ignore;

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
/**
 * Setup for function tests
 */
@Ignore
public abstract class FunctionTestSetup extends LuceneTestCase {

  /**
   * Actual score computation order is slightly different than assumptios
   * this allows for a small amount of variation
   */
  protected static float TEST_SCORE_TOLERANCE_DELTA = 0.001f;

  protected static final int N_DOCS = 17; // select a primary number > 2

  protected static final String ID_FIELD = "id";
  protected static final String TEXT_FIELD = "text";
  protected static final String INT_FIELD = "iii";
  protected static final String FLOAT_FIELD = "fff";

  protected ValueSource BYTE_VALUESOURCE = new ByteFieldSource(INT_FIELD);
  protected ValueSource SHORT_VALUESOURCE = new ShortFieldSource(INT_FIELD);
  protected ValueSource INT_VALUESOURCE = new IntFieldSource(INT_FIELD);
  protected ValueSource INT_AS_FLOAT_VALUESOURCE = new FloatFieldSource(INT_FIELD);
  protected ValueSource FLOAT_VALUESOURCE = new FloatFieldSource(FLOAT_FIELD);

  private static final String DOC_TEXT_LINES[] = {
          "Well, this is just some plain text we use for creating the ",
          "test documents. It used to be a text from an online collection ",
          "devoted to first aid, but if there was there an (online) lawyers ",
          "first aid collection with legal advices, \"it\" might have quite ",
          "probably advised one not to include \"it\"'s text or the text of ",
          "any other online collection in one's code, unless one has money ",
          "that one don't need and one is happy to donate for lawyers ",
          "charity. Anyhow at some point, rechecking the usage of this text, ",
          "it became uncertain that this text is free to use, because ",
          "the web site in the disclaimer of he eBook containing that text ",
          "was not responding anymore, and at the same time, in projGut, ",
          "searching for first aid no longer found that eBook as well. ",
          "So here we are, with a perhaps much less interesting ",
          "text for the test, but oh much much safer. ",
  };

  protected static Directory dir;
  protected static Analyzer anlzr;

  @AfterClass
  public static void afterClassFunctionTestSetup() throws Exception {
    dir.close();
    dir = null;
    anlzr = null;
  }

  protected static void createIndex(boolean doMultiSegment) throws Exception {
    if (VERBOSE) {
      System.out.println("TEST: setUp");
    }
    // prepare a small index with just a few documents.
    dir = newDirectory();
    anlzr = new MockAnalyzer(random());
    IndexWriterConfig iwc = newIndexWriterConfig( TEST_VERSION_CURRENT, anlzr).setMergePolicy(newLogMergePolicy());
    if (doMultiSegment) {
      iwc.setMaxBufferedDocs(_TestUtil.nextInt(random(), 2, 7));
    }
    RandomIndexWriter iw = new RandomIndexWriter(random(), dir, iwc);
    // add docs not exactly in natural ID order, to verify we do check the order of docs by scores
    int remaining = N_DOCS;
    boolean done[] = new boolean[N_DOCS];
    int i = 0;
    while (remaining > 0) {
      if (done[i]) {
        throw new Exception("to set this test correctly N_DOCS=" + N_DOCS + " must be primary and greater than 2!");
      }
      addDoc(iw, i);
      done[i] = true;
      i = (i + 4) % N_DOCS;
      remaining --;
    }
    if (!doMultiSegment) {
      if (VERBOSE) {
        System.out.println("TEST: setUp full merge");
      }
      iw.forceMerge(1);
    }
    iw.close();
    if (VERBOSE) {
      System.out.println("TEST: setUp done close");
    }
  }

  private static void addDoc(RandomIndexWriter iw, int i) throws Exception {
    Document d = new Document();
    Field f;
    int scoreAndID = i + 1;

    FieldType customType = new FieldType(TextField.TYPE_STORED);
    customType.setTokenized(false);
    customType.setOmitNorms(true);
    
    f = newField(ID_FIELD, id2String(scoreAndID), customType); // for debug purposes
    d.add(f);

    FieldType customType2 = new FieldType(TextField.TYPE_UNSTORED);
    customType2.setOmitNorms(true);
    f = newField(TEXT_FIELD, "text of doc" + scoreAndID + textLine(i), customType2); // for regular search
    d.add(f);

    f = newField(INT_FIELD, "" + scoreAndID, customType); // for function scoring
    d.add(f);

    f = newField(FLOAT_FIELD, scoreAndID + ".000", customType); // for function scoring
    d.add(f);

    iw.addDocument(d);
    log("added: " + d);
  }

  // 17 --> ID00017
  protected static String id2String(int scoreAndID) {
    String s = "000000000" + scoreAndID;
    int n = ("" + N_DOCS).length() + 3;
    int k = s.length() - n;
    return "ID" + s.substring(k);
  }

  // some text line for regular search
  private static String textLine(int docNum) {
    return DOC_TEXT_LINES[docNum % DOC_TEXT_LINES.length];
  }

  // extract expected doc score from its ID Field: "ID7" --> 7.0
  protected static float expectedFieldScore(String docIDFieldVal) {
    return Float.parseFloat(docIDFieldVal.substring(2));
  }

  // debug messages (change DBG to true for anything to print)
  protected static void log(Object o) {
    if (VERBOSE) {
      System.out.println(o.toString());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3685.java