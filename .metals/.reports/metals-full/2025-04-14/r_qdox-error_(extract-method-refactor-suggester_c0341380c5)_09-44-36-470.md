error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3193.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3193.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3193.java
text:
```scala
w@@riter.shutdown();

package org.apache.lucene.index.sorter;

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
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.NumericDocValues;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.CachingWrapperFilter;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.FixedBitSet;
import org.apache.lucene.util.LuceneTestCase;

public class TestBlockJoinSorter extends LuceneTestCase {

  private static class FixedBitSetCachingWrapperFilter extends CachingWrapperFilter {

    public FixedBitSetCachingWrapperFilter(Filter filter) {
      super(filter);
    }

    @Override
    protected DocIdSet cacheImpl(DocIdSetIterator iterator, AtomicReader reader)
        throws IOException {
      final FixedBitSet cached = new FixedBitSet(reader.maxDoc());
      cached.or(iterator);
      return cached;
    }

  }

  public void test() throws IOException {
    final int numParents = atLeast(200);
    IndexWriterConfig cfg = newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random()));
    cfg.setMergePolicy(newLogMergePolicy());
    final RandomIndexWriter writer = new RandomIndexWriter(random(), newDirectory(), cfg);
    final Document parentDoc = new Document();
    final NumericDocValuesField parentVal = new NumericDocValuesField("parent_val", 0L);
    parentDoc.add(parentVal);
    final StringField parent = new StringField("parent", "true", Store.YES);
    parentDoc.add(parent);
    for (int i = 0; i < numParents; ++i) {
      List<Document> documents = new ArrayList<>();
      final int numChildren = random().nextInt(10);
      for (int j = 0; j < numChildren; ++j) {
        final Document childDoc = new Document();
        childDoc.add(new NumericDocValuesField("child_val", random().nextInt(5)));
        documents.add(childDoc);
      }
      parentVal.setLongValue(random().nextInt(50));
      documents.add(parentDoc);
      writer.addDocuments(documents);
    }
    writer.forceMerge(1);
    final DirectoryReader indexReader = writer.getReader();
    writer.close();

    final AtomicReader reader = getOnlySegmentReader(indexReader);
    final Filter parentsFilter = new FixedBitSetCachingWrapperFilter(new QueryWrapperFilter(new TermQuery(new Term("parent", "true"))));
    final FixedBitSet parentBits = (FixedBitSet) parentsFilter.getDocIdSet(reader.getContext(), null);
    final NumericDocValues parentValues = reader.getNumericDocValues("parent_val");
    final NumericDocValues childValues = reader.getNumericDocValues("child_val");

    final Sort parentSort = new Sort(new SortField("parent_val", SortField.Type.LONG));
    final Sort childSort = new Sort(new SortField("child_val", SortField.Type.LONG));

    final Sort sort = new Sort(new SortField("custom", new BlockJoinComparatorSource(parentsFilter, parentSort, childSort)));
    final Sorter sorter = new Sorter(sort);
    final Sorter.DocMap docMap = sorter.sort(reader);
    assertEquals(reader.maxDoc(), docMap.size());

    int[] children = new int[1];
    int numChildren = 0;
    int previousParent = -1;
    for (int i = 0; i < docMap.size(); ++i) {
      final int oldID = docMap.newToOld(i);
      if (parentBits.get(oldID)) {
        // check that we have the right children
        for (int j = 0; j < numChildren; ++j) {
          assertEquals(oldID, parentBits.nextSetBit(children[j]));
        }
        // check that children are sorted
        for (int j = 1; j < numChildren; ++j) {
          final int doc1 = children[j-1];
          final int doc2 = children[j];
          if (childValues.get(doc1) == childValues.get(doc2)) {
            assertTrue(doc1 < doc2); // sort is stable
          } else {
            assertTrue(childValues.get(doc1) < childValues.get(doc2));
          }
        }
        // check that parents are sorted
        if (previousParent != -1) {
          if (parentValues.get(previousParent) == parentValues.get(oldID)) {
            assertTrue(previousParent < oldID);
          } else {
            assertTrue(parentValues.get(previousParent) < parentValues.get(oldID));
          }
        }
        // reset
        previousParent = oldID;
        numChildren = 0;
      } else {
        children = ArrayUtil.grow(children, numChildren+1);
        children[numChildren++] = oldID;
      }
    }
    indexReader.close();
    writer.w.getDirectory().close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3193.java