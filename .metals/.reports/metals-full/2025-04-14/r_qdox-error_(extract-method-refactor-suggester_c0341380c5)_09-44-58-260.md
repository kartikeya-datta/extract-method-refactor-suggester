error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3065.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3065.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3065.java
text:
```scala
i@@w.shutdown();

package org.apache.lucene.codecs.lucene40;

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

import java.util.ArrayList;
import java.util.Collections;

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.codecs.Codec;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.TestUtil;
import org.junit.BeforeClass;

public class TestLucene40PostingsReader extends LuceneTestCase {
  static final String terms[] = new String[100];
  static {
    for (int i = 0; i < terms.length; i++) {
      terms[i] = Integer.toString(i+1);
    }
  }
  
  @BeforeClass
  public static void beforeClass() {
    OLD_FORMAT_IMPERSONATION_IS_ACTIVE = true; // explicitly instantiates ancient codec
  }

  /** tests terms with different probabilities of being in the document.
   *  depends heavily on term vectors cross-check at checkIndex
   */
  public void testPostings() throws Exception {
    Directory dir = newFSDirectory(createTempDir("postings"));
    IndexWriterConfig iwc = newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random()));
    iwc.setCodec(Codec.forName("Lucene40"));
    RandomIndexWriter iw = new RandomIndexWriter(random(), dir, iwc);
    
    Document doc = new Document();
    
    // id field
    FieldType idType = new FieldType(StringField.TYPE_NOT_STORED);
    idType.setStoreTermVectors(true);
    Field idField = new Field("id", "", idType);
    doc.add(idField);
    
    // title field: short text field
    FieldType titleType = new FieldType(TextField.TYPE_NOT_STORED);
    titleType.setStoreTermVectors(true);
    titleType.setStoreTermVectorPositions(true);
    titleType.setStoreTermVectorOffsets(true);
    titleType.setIndexOptions(indexOptions());
    Field titleField = new Field("title", "", titleType);
    doc.add(titleField);
    
    // body field: long text field
    FieldType bodyType = new FieldType(TextField.TYPE_NOT_STORED);
    bodyType.setStoreTermVectors(true);
    bodyType.setStoreTermVectorPositions(true);
    bodyType.setStoreTermVectorOffsets(true);
    bodyType.setIndexOptions(indexOptions());
    Field bodyField = new Field("body", "", bodyType);
    doc.add(bodyField);
    
    int numDocs = atLeast(1000);
    for (int i = 0; i < numDocs; i++) {
      idField.setStringValue(Integer.toString(i));
      titleField.setStringValue(fieldValue(1));
      bodyField.setStringValue(fieldValue(3));
      iw.addDocument(doc);
      if (random().nextInt(20) == 0) {
        iw.deleteDocuments(new Term("id", Integer.toString(i)));
      }
    }
    if (random().nextBoolean()) {
      // delete 1-100% of docs
      iw.deleteDocuments(new Term("title", terms[random().nextInt(terms.length)]));
    }
    iw.close();
    dir.close(); // checkindex
  }
  
  IndexOptions indexOptions() {
    switch(random().nextInt(4)) {
      case 0: return IndexOptions.DOCS_ONLY;
      case 1: return IndexOptions.DOCS_AND_FREQS;
      case 2: return IndexOptions.DOCS_AND_FREQS_AND_POSITIONS;
      default: return IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS;
    }
  }
  
  String fieldValue(int maxTF) {
    ArrayList<String> shuffled = new ArrayList<>();
    StringBuilder sb = new StringBuilder();
    int i = random().nextInt(terms.length);
    while (i < terms.length) {
      int tf =  TestUtil.nextInt(random(), 1, maxTF);
      for (int j = 0; j < tf; j++) {
        shuffled.add(terms[i]);
      }
      i++;
    }
    Collections.shuffle(shuffled, random());
    for (String term : shuffled) {
      sb.append(term);
      sb.append(' ');
    }
    return sb.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3065.java