error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3158.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3158.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3158.java
text:
```scala
w@@.shutdown();

package org.apache.lucene.index;

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

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.TestUtil;
import java.io.IOException;

import org.junit.Before;
import org.junit.After;

/**
 * Tests that a useful exception is thrown when attempting to index a term that is 
 * too large
 *
 * @see IndexWriter#MAX_TERM_LENGTH
 */
public class TestExceedMaxTermLength extends LuceneTestCase {

  private final static int minTestTermLength = IndexWriter.MAX_TERM_LENGTH + 1;
  private final static int maxTestTermLegnth = IndexWriter.MAX_TERM_LENGTH * 2;

  Directory dir = null;

  @Before
  public void createDir() {
    dir = newDirectory();
  }
  @After
  public void destroyDir() throws IOException {
    dir.close();
    dir = null;
  }

  public void test() throws Exception {
    
    IndexWriter w = new IndexWriter
      (dir, newIndexWriterConfig(random(), 
                                 TEST_VERSION_CURRENT,
                                 new MockAnalyzer(random())));
    try {
      final FieldType ft = new FieldType();
      ft.setIndexed(true);
      ft.setStored(random().nextBoolean());
      ft.freeze();
      
      final Document doc = new Document();
      if (random().nextBoolean()) {
        // totally ok short field value
        doc.add(new Field(TestUtil.randomSimpleString(random(), 1, 10),
                          TestUtil.randomSimpleString(random(), 1, 10),
                          ft));
      }
      // problematic field
      final String name = TestUtil.randomSimpleString(random(), 1, 50);
      final String value = TestUtil.randomSimpleString(random(),
                                                       minTestTermLength,
                                                       maxTestTermLegnth);
      final Field f = new Field(name, value, ft);
      if (random().nextBoolean()) {
        // totally ok short field value
        doc.add(new Field(TestUtil.randomSimpleString(random(), 1, 10),
                          TestUtil.randomSimpleString(random(), 1, 10),
                          ft));
      }
      doc.add(f);
      
      try {
        w.addDocument(doc);
        fail("Did not get an exception from adding a monster term");
      } catch (IllegalArgumentException e) {
        final String maxLengthMsg = String.valueOf(IndexWriter.MAX_TERM_LENGTH);
        final String msg = e.getMessage();
        assertTrue("IllegalArgumentException didn't mention 'immense term': " + msg,
                   msg.contains("immense term"));
        assertTrue("IllegalArgumentException didn't mention max length ("+maxLengthMsg+"): " + msg,
                   msg.contains(maxLengthMsg));
        assertTrue("IllegalArgumentException didn't mention field name ("+name+"): " + msg,
                   msg.contains(name));
      }
    } finally {
      w.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3158.java