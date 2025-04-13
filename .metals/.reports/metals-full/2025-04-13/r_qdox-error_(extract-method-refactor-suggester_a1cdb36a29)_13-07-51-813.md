error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3184.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3184.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3184.java
text:
```scala
w@@riter.shutdown();

package org.apache.lucene.analysis.miscellaneous;

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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.TestUtil;

public class TestLimitTokenCountAnalyzer extends BaseTokenStreamTestCase {

  public void testLimitTokenCountAnalyzer() throws IOException {
    for (boolean consumeAll : new boolean[] { true, false }) {
      MockAnalyzer mock = new MockAnalyzer(random());

      // if we are consuming all tokens, we can use the checks, 
      // otherwise we can't
      mock.setEnableChecks(consumeAll);
      Analyzer a = new LimitTokenCountAnalyzer(mock, 2, consumeAll);
    
      // dont use assertAnalyzesTo here, as the end offset is not the end of the string (unless consumeAll is true, in which case its correct)!
      assertTokenStreamContents(a.tokenStream("dummy", "1  2     3  4  5"), new String[] { "1", "2" }, new int[] { 0, 3 }, new int[] { 1, 4 }, consumeAll ? 16 : null);
      assertTokenStreamContents(a.tokenStream("dummy", "1 2 3 4 5"), new String[] { "1", "2" }, new int[] { 0, 2 }, new int[] { 1, 3 }, consumeAll ? 9 : null);
      
      // less than the limit, ensure we behave correctly
      assertTokenStreamContents(a.tokenStream("dummy", "1  "), new String[] { "1" }, new int[] { 0 }, new int[] { 1 }, consumeAll ? 3 : null);
    
      // equal to limit
      assertTokenStreamContents(a.tokenStream("dummy", "1  2  "), new String[] { "1", "2" }, new int[] { 0, 3 }, new int[] { 1, 4 }, consumeAll ? 6 : null);
    }
  }

  public void testLimitTokenCountIndexWriter() throws IOException {
    
    for (boolean consumeAll : new boolean[] { true, false }) {
      Directory dir = newDirectory();
      int limit = TestUtil.nextInt(random(), 50, 101000);
      MockAnalyzer mock = new MockAnalyzer(random());

      // if we are consuming all tokens, we can use the checks, 
      // otherwise we can't
      mock.setEnableChecks(consumeAll);
      Analyzer a = new LimitTokenCountAnalyzer(mock, limit, consumeAll);

      IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig
                                           (TEST_VERSION_CURRENT, a));

      Document doc = new Document();
      StringBuilder b = new StringBuilder();
      for(int i=1;i<limit;i++)
        b.append(" a");
      b.append(" x");
      b.append(" z");
      doc.add(newTextField("field", b.toString(), Field.Store.NO));
      writer.addDocument(doc);
      writer.close();
      
      IndexReader reader = DirectoryReader.open(dir);
      Term t = new Term("field", "x");
      assertEquals(1, reader.docFreq(t));
      t = new Term("field", "z");
      assertEquals(0, reader.docFreq(t));
      reader.close();
      dir.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3184.java