error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2941.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2941.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2941.java
text:
```scala
I@@ndexReader reader = IndexReader.open(dir, true);

package org.apache.lucene.search;

/**
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.English;
import org.apache.lucene.util.LuceneTestCase;

public class TestSpanQueryFilter extends LuceneTestCase {


  public TestSpanQueryFilter(String s) {
    super(s);
  }

  public void testFilterWorks() throws Exception {
    Directory dir = new RAMDirectory();
    IndexWriter writer = new IndexWriter(dir, new SimpleAnalyzer(), true, 
                                         IndexWriter.MaxFieldLength.LIMITED);
    for (int i = 0; i < 500; i++) {
      Document document = new Document();
      document.add(new Field("field", English.intToEnglish(i) + " equals " + English.intToEnglish(i),
              Field.Store.NO, Field.Index.ANALYZED));
      writer.addDocument(document);
    }
    writer.close();

    IndexReader reader = IndexReader.open(dir);

    SpanTermQuery query = new SpanTermQuery(new Term("field", English.intToEnglish(10).trim()));
    SpanQueryFilter filter = new SpanQueryFilter(query);
    SpanFilterResult result = filter.bitSpans(reader);
    DocIdSet docIdSet = result.getDocIdSet();
    assertTrue("docIdSet is null and it shouldn't be", docIdSet != null);
    assertContainsDocId("docIdSet doesn't contain docId 10", docIdSet, 10);
    List spans = result.getPositions();
    assertTrue("spans is null and it shouldn't be", spans != null);
    int size = getDocIdSetSize(docIdSet);
    assertTrue("spans Size: " + spans.size() + " is not: " + size, spans.size() == size);
    for (Iterator iterator = spans.iterator(); iterator.hasNext();) {
       SpanFilterResult.PositionInfo info = (SpanFilterResult.PositionInfo) iterator.next();
      assertTrue("info is null and it shouldn't be", info != null);
      //The doc should indicate the bit is on
      assertContainsDocId("docIdSet doesn't contain docId " + info.getDoc(), docIdSet, info.getDoc());
      //There should be two positions in each
      assertTrue("info.getPositions() Size: " + info.getPositions().size() + " is not: " + 2, info.getPositions().size() == 2);
    }
    reader.close();
  }
  
  int getDocIdSetSize(DocIdSet docIdSet) throws Exception {
    int size = 0;
    DocIdSetIterator it = docIdSet.iterator();
    while (it.nextDoc() != DocIdSetIterator.NO_MORE_DOCS) {
      size++;
    }
    return size;
  }
  
  public void assertContainsDocId(String msg, DocIdSet docIdSet, int docId) throws Exception {
    DocIdSetIterator it = docIdSet.iterator();
    assertTrue(msg, it.advance(docId) != DocIdSetIterator.NO_MORE_DOCS);
    assertTrue(msg, it.docID() == docId);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2941.java