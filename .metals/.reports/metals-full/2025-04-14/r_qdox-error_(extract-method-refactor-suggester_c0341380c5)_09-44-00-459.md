error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3187.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3187.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3187.java
text:
```scala
w@@.shutdown();

package org.apache.lucene.search.grouping;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License")); you may not use this file except in compliance with
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
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.queries.function.valuesource.BytesRefFieldSource;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.grouping.function.FunctionAllGroupsCollector;
import org.apache.lucene.search.grouping.term.TermAllGroupsCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LuceneTestCase;

import java.util.HashMap;

public class AllGroupsCollectorTest extends LuceneTestCase {

  public void testTotalGroupCount() throws Exception {

    final String groupField = "author";
    FieldType customType = new FieldType();
    customType.setStored(true);

    Directory dir = newDirectory();
    RandomIndexWriter w = new RandomIndexWriter(
        random(),
        dir,
        newIndexWriterConfig(TEST_VERSION_CURRENT,
            new MockAnalyzer(random())).setMergePolicy(newLogMergePolicy()));

    // 0
    Document doc = new Document();
    addGroupField(doc, groupField, "author1");
    doc.add(new TextField("content", "random text", Field.Store.YES));
    doc.add(new Field("id", "1", customType));
    w.addDocument(doc);

    // 1
    doc = new Document();
    addGroupField(doc, groupField, "author1");
    doc.add(new TextField("content", "some more random text blob", Field.Store.YES));
    doc.add(new Field("id", "2", customType));
    w.addDocument(doc);

    // 2
    doc = new Document();
    addGroupField(doc, groupField, "author1");
    doc.add(new TextField("content", "some more random textual data", Field.Store.YES));
    doc.add(new Field("id", "3", customType));
    w.addDocument(doc);
    w.commit(); // To ensure a second segment

    // 3
    doc = new Document();
    addGroupField(doc, groupField, "author2");
    doc.add(new TextField("content", "some random text", Field.Store.YES));
    doc.add(new Field("id", "4", customType));
    w.addDocument(doc);

    // 4
    doc = new Document();
    addGroupField(doc, groupField, "author3");
    doc.add(new TextField("content", "some more random text", Field.Store.YES));
    doc.add(new Field("id", "5", customType));
    w.addDocument(doc);

    // 5
    doc = new Document();
    addGroupField(doc, groupField, "author3");
    doc.add(new TextField("content", "random blob", Field.Store.YES));
    doc.add(new Field("id", "6", customType));
    w.addDocument(doc);

    // 6 -- no author field
    doc = new Document();
    doc.add(new TextField("content", "random word stuck in alot of other text", Field.Store.YES));
    doc.add(new Field("id", "6", customType));
    w.addDocument(doc);

    IndexSearcher indexSearcher = newSearcher(w.getReader());
    w.close();

    AbstractAllGroupsCollector<?> allGroupsCollector = createRandomCollector(groupField);
    indexSearcher.search(new TermQuery(new Term("content", "random")), allGroupsCollector);
    assertEquals(4, allGroupsCollector.getGroupCount());

    allGroupsCollector = createRandomCollector(groupField);
    indexSearcher.search(new TermQuery(new Term("content", "some")), allGroupsCollector);
    assertEquals(3, allGroupsCollector.getGroupCount());

    allGroupsCollector = createRandomCollector(groupField);
    indexSearcher.search(new TermQuery(new Term("content", "blob")), allGroupsCollector);
    assertEquals(2, allGroupsCollector.getGroupCount());

    indexSearcher.getIndexReader().close();
    dir.close();
  }

  private void addGroupField(Document doc, String groupField, String value) {
    doc.add(new TextField(groupField, value, Field.Store.YES));
    doc.add(new SortedDocValuesField(groupField, new BytesRef(value)));
  }

  private AbstractAllGroupsCollector<?> createRandomCollector(String groupField) {
    AbstractAllGroupsCollector<?> selected;
    if (random().nextBoolean()) {
      selected = new TermAllGroupsCollector(groupField);
    } else {
      ValueSource vs = new BytesRefFieldSource(groupField);
      selected = new FunctionAllGroupsCollector(vs, new HashMap<>());
    }

    if (VERBOSE) {
      System.out.println("Selected implementation: " + selected.getClass().getName());
    }

    return selected;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3187.java