error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3101.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3101.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3101.java
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

import java.io.IOException;
import java.util.Random;

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FloatField;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.TestUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class BaseTestRangeFilter extends LuceneTestCase {
  
  public static final boolean F = false;
  public static final boolean T = true;
  
  /**
   * Collation interacts badly with hyphens -- collation produces different
   * ordering than Unicode code-point ordering -- so two indexes are created:
   * one which can't have negative random integers, for testing collated ranges,
   * and the other which can have negative random integers, for all other tests.
   */
  static class TestIndex {
    int maxR;
    int minR;
    boolean allowNegativeRandomInts;
    Directory index;
    
    TestIndex(Random random, int minR, int maxR, boolean allowNegativeRandomInts) {
      this.minR = minR;
      this.maxR = maxR;
      this.allowNegativeRandomInts = allowNegativeRandomInts;
      index = newDirectory(random);
    }
  }
  
  static IndexReader signedIndexReader;
  static IndexReader unsignedIndexReader;
  
  static TestIndex signedIndexDir;
  static TestIndex unsignedIndexDir;
  
  static int minId = 0;
  static int maxId;
  
  static final int intLength = Integer.toString(Integer.MAX_VALUE).length();
  
  /**
   * a simple padding function that should work with any int
   */
  public static String pad(int n) {
    StringBuilder b = new StringBuilder(40);
    String p = "0";
    if (n < 0) {
      p = "-";
      n = Integer.MAX_VALUE + n + 1;
    }
    b.append(p);
    String s = Integer.toString(n);
    for (int i = s.length(); i <= intLength; i++) {
      b.append("0");
    }
    b.append(s);
    
    return b.toString();
  }
  
  @BeforeClass
  public static void beforeClassBaseTestRangeFilter() throws Exception {
    maxId = atLeast(500);
    signedIndexDir = new TestIndex(random(), Integer.MAX_VALUE, Integer.MIN_VALUE, true);
    unsignedIndexDir = new TestIndex(random(), Integer.MAX_VALUE, 0, false);
    signedIndexReader = build(random(), signedIndexDir);
    unsignedIndexReader = build(random(), unsignedIndexDir);
  }
  
  @AfterClass
  public static void afterClassBaseTestRangeFilter() throws Exception {
    signedIndexReader.close();
    unsignedIndexReader.close();
    signedIndexDir.index.close();
    unsignedIndexDir.index.close();
    signedIndexReader = null;
    unsignedIndexReader = null;
    signedIndexDir = null;
    unsignedIndexDir = null;
  }
  
  private static IndexReader build(Random random, TestIndex index) throws IOException {
    /* build an index */
    
    Document doc = new Document();
    Field idField = newStringField(random, "id", "", Field.Store.YES);
    Field intIdField = new IntField("id_int", 0, Store.YES);
    Field floatIdField = new FloatField("id_float", 0, Store.YES);
    Field longIdField = new LongField("id_long", 0, Store.YES);
    Field doubleIdField = new DoubleField("id_double", 0, Store.YES);
    Field randField = newStringField(random, "rand", "", Field.Store.YES);
    Field bodyField = newStringField(random, "body", "", Field.Store.NO);
    doc.add(idField);
    doc.add(intIdField);
    doc.add(floatIdField);
    doc.add(longIdField);
    doc.add(doubleIdField);
    doc.add(randField);
    doc.add(bodyField);

    RandomIndexWriter writer = new RandomIndexWriter(random, index.index, 
                                                     newIndexWriterConfig(random, TEST_VERSION_CURRENT, new MockAnalyzer(random))
                                                     .setOpenMode(OpenMode.CREATE).setMaxBufferedDocs(TestUtil.nextInt(random, 50, 1000)).setMergePolicy(newLogMergePolicy()));
    TestUtil.reduceOpenFiles(writer.w);

    while(true) {

      int minCount = 0;
      int maxCount = 0;

      for (int d = minId; d <= maxId; d++) {
        idField.setStringValue(pad(d));
        intIdField.setIntValue(d);
        floatIdField.setFloatValue(d);
        longIdField.setLongValue(d);
        doubleIdField.setDoubleValue(d);
        int r = index.allowNegativeRandomInts ? random.nextInt() : random
          .nextInt(Integer.MAX_VALUE);
        if (index.maxR < r) {
          index.maxR = r;
          maxCount = 1;
        } else if (index.maxR == r) {
          maxCount++;
        }

        if (r < index.minR) {
          index.minR = r;
          minCount = 1;
        } else if (r == index.minR) {
          minCount++;
        }
        randField.setStringValue(pad(r));
        bodyField.setStringValue("body");
        writer.addDocument(doc);
      }

      if (minCount == 1 && maxCount == 1) {
        // our subclasses rely on only 1 doc having the min or
        // max, so, we loop until we satisfy that.  it should be
        // exceedingly rare (Yonik calculates 1 in ~429,000)
        // times) that this loop requires more than one try:
        IndexReader ir = writer.getReader();
        writer.close();
        return ir;
      }

      // try again
      writer.deleteAll();
    }
  }
  
  @Test
  public void testPad() {
    
    int[] tests = new int[] {-9999999, -99560, -100, -3, -1, 0, 3, 9, 10, 1000,
        999999999};
    for (int i = 0; i < tests.length - 1; i++) {
      int a = tests[i];
      int b = tests[i + 1];
      String aa = pad(a);
      String bb = pad(b);
      String label = a + ":" + aa + " vs " + b + ":" + bb;
      assertEquals("length of " + label, aa.length(), bb.length());
      assertTrue("compare less than " + label, aa.compareTo(bb) < 0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3101.java