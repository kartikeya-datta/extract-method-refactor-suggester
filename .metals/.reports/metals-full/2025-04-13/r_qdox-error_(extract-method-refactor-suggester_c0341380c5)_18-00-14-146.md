error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/214.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/214.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/214.java
text:
```scala
d@@oc.add(new NumericField("trie", value));

package org.apache.lucene.search;

/**
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

import java.util.Locale;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util._TestUtil;

public class TestMultiValuedNumericRangeQuery extends LuceneTestCase {

  /** Tests NumericRangeQuery on a multi-valued field (multiple numeric values per document).
   * This test ensures, that a classical TermRangeQuery returns exactly the same document numbers as
   * NumericRangeQuery (see SOLR-1322 for discussion) and the multiple precision terms per numeric value
   * do not interfere with multiple numeric values.
   */
  public void testMultiValuedNRQ() throws Exception {
    Directory directory = newDirectory();
    RandomIndexWriter writer = new RandomIndexWriter(random, directory,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random))
        .setMaxBufferedDocs(_TestUtil.nextInt(random, 50, 1000)));
    
    DecimalFormat format = new DecimalFormat("00000000000", new DecimalFormatSymbols(Locale.US));
    
    int num = atLeast(500);
    for (int l = 0; l < num; l++) {
      Document doc = new Document();
      for (int m=0, c=random.nextInt(10); m<=c; m++) {
        int value = random.nextInt(Integer.MAX_VALUE);
        doc.add(newField("asc", format.format(value), StringField.TYPE_UNSTORED));
        doc.add(new NumericField("trie").setIntValue(value));
      }
      writer.addDocument(doc);
    }
    IndexReader reader = writer.getReader();
    writer.close();
    
    IndexSearcher searcher=newSearcher(reader);
    num = atLeast(50);
    for (int i = 0; i < num; i++) {
      int lower=random.nextInt(Integer.MAX_VALUE);
      int upper=random.nextInt(Integer.MAX_VALUE);
      if (lower>upper) {
        int a=lower; lower=upper; upper=a;
      }
      TermRangeQuery cq=TermRangeQuery.newStringRange("asc", format.format(lower), format.format(upper), true, true);
      NumericRangeQuery<Integer> tq=NumericRangeQuery.newIntRange("trie", lower, upper, true, true);
      TopDocs trTopDocs = searcher.search(cq, 1);
      TopDocs nrTopDocs = searcher.search(tq, 1);
      assertEquals("Returned count for NumericRangeQuery and TermRangeQuery must be equal", trTopDocs.totalHits, nrTopDocs.totalHits );
    }
    reader.close();
    directory.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/214.java