error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3123.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3123.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3123.java
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

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.*;
import org.apache.lucene.search.FieldValueHitQueue.Entry;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.store.*;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.BytesRef;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestElevationComparator extends LuceneTestCase {

  private final Map<BytesRef,Integer> priority = new HashMap<>();

  //@Test
  public void testSorting() throws Throwable {
    Directory directory = newDirectory();
    IndexWriter writer = new IndexWriter(
        directory,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).
            setMaxBufferedDocs(2).
            setMergePolicy(newLogMergePolicy(1000)).
            setSimilarity(new DefaultSimilarity())
    );
    writer.addDocument(adoc(new String[] {"id", "a", "title", "ipod", "str_s", "a"}));
    writer.addDocument(adoc(new String[] {"id", "b", "title", "ipod ipod", "str_s", "b"}));
    writer.addDocument(adoc(new String[] {"id", "c", "title", "ipod ipod ipod", "str_s","c"}));
    writer.addDocument(adoc(new String[] {"id", "x", "title", "boosted", "str_s", "x"}));
    writer.addDocument(adoc(new String[] {"id", "y", "title", "boosted boosted", "str_s","y"}));
    writer.addDocument(adoc(new String[] {"id", "z", "title", "boosted boosted boosted","str_s", "z"}));

    IndexReader r = DirectoryReader.open(writer, true);
    writer.close();

    IndexSearcher searcher = newSearcher(r);
    searcher.setSimilarity(new DefaultSimilarity());

    runTest(searcher, true);
    runTest(searcher, false);

    r.close();
    directory.close();
  }

  private void runTest(IndexSearcher searcher, boolean reversed) throws Throwable {

    BooleanQuery newq = new BooleanQuery(false);
    TermQuery query = new TermQuery(new Term("title", "ipod"));

    newq.add(query, BooleanClause.Occur.SHOULD);
    newq.add(getElevatedQuery(new String[] {"id", "a", "id", "x"}), BooleanClause.Occur.SHOULD);

    Sort sort = new Sort(
        new SortField("id", new ElevationComparatorSource(priority), false),
        new SortField(null, SortField.Type.SCORE, reversed)
      );

    TopDocsCollector<Entry> topCollector = TopFieldCollector.create(sort, 50, false, true, true, true);
    searcher.search(newq, null, topCollector);

    TopDocs topDocs = topCollector.topDocs(0, 10);
    int nDocsReturned = topDocs.scoreDocs.length;

    assertEquals(4, nDocsReturned);

    // 0 & 3 were elevated
    assertEquals(0, topDocs.scoreDocs[0].doc);
    assertEquals(3, topDocs.scoreDocs[1].doc);

    if (reversed) {
      assertEquals(2, topDocs.scoreDocs[2].doc);
      assertEquals(1, topDocs.scoreDocs[3].doc);
    } else {
      assertEquals(1, topDocs.scoreDocs[2].doc);
      assertEquals(2, topDocs.scoreDocs[3].doc);
    }

    /*
    for (int i = 0; i < nDocsReturned; i++) {
     ScoreDoc scoreDoc = topDocs.scoreDocs[i];
     ids[i] = scoreDoc.doc;
     scores[i] = scoreDoc.score;
     documents[i] = searcher.doc(ids[i]);
     System.out.println("ids[i] = " + ids[i]);
     System.out.println("documents[i] = " + documents[i]);
     System.out.println("scores[i] = " + scores[i]);
   }
    */
 }

 private Query getElevatedQuery(String[] vals) {
   BooleanQuery q = new BooleanQuery(false);
   q.setBoost(0);
   int max = (vals.length / 2) + 5;
   for (int i = 0; i < vals.length - 1; i += 2) {
     q.add(new TermQuery(new Term(vals[i], vals[i + 1])), BooleanClause.Occur.SHOULD);
     priority.put(new BytesRef(vals[i + 1]), Integer.valueOf(max--));
     // System.out.println(" pri doc=" + vals[i+1] + " pri=" + (1+max));
   }
   return q;
 }

 private Document adoc(String[] vals) {
   Document doc = new Document();
   for (int i = 0; i < vals.length - 2; i += 2) {
     doc.add(newTextField(vals[i], vals[i + 1], Field.Store.YES));
   }
   return doc;
 }
}

class ElevationComparatorSource extends FieldComparatorSource {
  private final Map<BytesRef,Integer> priority;

  public ElevationComparatorSource(final Map<BytesRef,Integer> boosts) {
   this.priority = boosts;
  }

  @Override
  public FieldComparator<Integer> newComparator(final String fieldname, final int numHits, int sortPos, boolean reversed) throws IOException {
   return new FieldComparator<Integer>() {

     SortedDocValues idIndex;
     private final int[] values = new int[numHits];
     private final BytesRef tempBR = new BytesRef();
     int bottomVal;

     @Override
     public int compare(int slot1, int slot2) {
       return values[slot2] - values[slot1];  // values will be small enough that there is no overflow concern
     }

     @Override
     public void setBottom(int slot) {
       bottomVal = values[slot];
     }

     @Override
     public void setTopValue(Integer value) {
       throw new UnsupportedOperationException();
     }

     private int docVal(int doc) {
       int ord = idIndex.getOrd(doc);
       if (ord == -1) {
         return 0;
       } else {
         idIndex.lookupOrd(ord, tempBR);
         Integer prio = priority.get(tempBR);
         return prio == null ? 0 : prio.intValue();
       }
     }

     @Override
     public int compareBottom(int doc) {
       return docVal(doc) - bottomVal;
     }

     @Override
     public void copy(int slot, int doc) {
       values[slot] = docVal(doc);
     }

     @Override
     public FieldComparator<Integer> setNextReader(AtomicReaderContext context) throws IOException {
       idIndex = FieldCache.DEFAULT.getTermsIndex(context.reader(), fieldname);
       return this;
     }

     @Override
     public Integer value(int slot) {
       return Integer.valueOf(values[slot]);
     }

     @Override
     public int compareTop(int doc) {
       throw new UnsupportedOperationException();
     }
   };
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3123.java