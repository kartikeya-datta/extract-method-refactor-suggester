error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/412.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/412.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/412.java
text:
```scala
v@@oid add(int ordinal, int parentOrdinal) {

package org.apache.lucene.facet.taxonomy.directory;

import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DocsAndPositionsEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;

import org.apache.lucene.facet.taxonomy.TaxonomyReader;

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

// getParent() needs to be extremely efficient, to the point that we need
// to fetch all the data in advance into memory, and answer these calls
// from memory. Currently we use a large integer array, which is
// initialized when the taxonomy is opened, and potentially enlarged
// when it is refresh()ed.
/**
 * @lucene.experimental
 */
class ParentArray {

  // These arrays are not syncrhonized. Rather, the reference to the array
  // is volatile, and the only writing operation (refreshPrefetchArrays)
  // simply creates a new array and replaces the reference. The volatility
  // of the reference ensures the correct atomic replacement and its
  // visibility properties (the content of the array is visible when the
  // new reference is visible).
  private volatile int prefetchParentOrdinal[] = null;

  public int[] getArray() {
    return prefetchParentOrdinal;
  }

  /**
   * refreshPrefetch() refreshes the parent array. Initially, it fills the
   * array from the positions of an appropriate posting list. If called during
   * a refresh(), when the arrays already exist, only values for new documents
   * (those beyond the last one in the array) are read from the positions and
   * added to the arrays (that are appropriately enlarged). We assume (and
   * this is indeed a correct assumption in our case) that existing categories
   * are never modified or deleted.
   */
  void refresh(IndexReader indexReader) throws IOException {
    // Note that it is not necessary for us to obtain the read lock.
    // The reason is that we are only called from refresh() (precluding
    // another concurrent writer) or from the constructor (when no method
    // could be running).
    // The write lock is also not held during the following code, meaning
    // that reads *can* happen while this code is running. The "volatile"
    // property of the prefetchParentOrdinal and prefetchDepth array
    // references ensure the correct visibility property of the assignment
    // but other than that, we do *not* guarantee that a reader will not
    // use an old version of one of these arrays (or both) while a refresh
    // is going on. But we find this acceptable - until a refresh has
    // finished, the reader should not expect to see new information
    // (and the old information is the same in the old and new versions).
    int first;
    int num = indexReader.maxDoc();
    if (prefetchParentOrdinal==null) {
      prefetchParentOrdinal = new int[num];
      // Starting Lucene 2.9, following the change LUCENE-1542, we can
      // no longer reliably read the parent "-1" (see comment in
      // LuceneTaxonomyWriter.SinglePositionTokenStream). We have no way
      // to fix this in indexing without breaking backward-compatibility
      // with existing indexes, so what we'll do instead is just
      // hard-code the parent of ordinal 0 to be -1, and assume (as is
      // indeed the case) that no other parent can be -1.
      if (num>0) {
        prefetchParentOrdinal[0] = TaxonomyReader.INVALID_ORDINAL;
      }
      first = 1;
    } else {
      first = prefetchParentOrdinal.length;
      if (first==num) {
        return; // nothing to do - no category was added
      }
      // In Java 6, we could just do Arrays.copyOf()...
      int[] newarray = new int[num];
      System.arraycopy(prefetchParentOrdinal, 0, newarray, 0,
          prefetchParentOrdinal.length);
      prefetchParentOrdinal = newarray;
    }

    // Read the new part of the parents array from the positions:
    // TODO (Facet): avoid Multi*?
    Bits liveDocs = MultiFields.getLiveDocs(indexReader);
    DocsAndPositionsEnum positions = MultiFields.getTermPositionsEnum(indexReader, liveDocs,
                                                                      Consts.FIELD_PAYLOADS, new BytesRef(Consts.PAYLOAD_PARENT),
                                                                      false);
      if ((positions == null || positions.advance(first) == DocIdSetIterator.NO_MORE_DOCS) && first < num) {
        throw new CorruptIndexException("Missing parent data for category " + first);
      }
      for (int i=first; i<num; i++) {
        // Note that we know positions.doc() >= i (this is an
        // invariant kept throughout this loop)
        if (positions.docID()==i) {
          if (positions.freq() == 0) { // shouldn't happen
            throw new CorruptIndexException(
                "Missing parent data for category "+i);
          }

          // TODO (Facet): keep a local (non-volatile) copy of the prefetchParentOrdinal
          // reference, because access to volatile reference is slower (?).
          // Note: The positions we get here are one less than the position
          // increment we added originally, so we get here the right numbers:
          prefetchParentOrdinal[i] = positions.nextPosition();

          if (positions.nextDoc() == DocIdSetIterator.NO_MORE_DOCS) {
            if ( i+1 < num ) {
              throw new CorruptIndexException(
                  "Missing parent data for category "+(i+1));
            }
            break;
          }
        } else { // this shouldn't happen
        throw new CorruptIndexException(
            "Missing parent data for category "+i);
      }
    }
  }

  /**
   * add() is used in LuceneTaxonomyWriter, not in LuceneTaxonomyReader.
   * It is only called from a synchronized method, so it is not reentrant,
   * and also doesn't need to worry about reads happening at the same time.
   * 
   * NOTE: add() and refresh() CANNOT be used together. If you call add(),
   * this changes the arrays and refresh() can no longer be used.
   */
  void add(int ordinal, int parentOrdinal) throws IOException {
    if (ordinal >= prefetchParentOrdinal.length) {
      // grow the array, if necessary.
      // In Java 6, we could just do Arrays.copyOf()...
      int[] newarray = new int[ordinal*2+1];
      System.arraycopy(prefetchParentOrdinal, 0, newarray, 0,
          prefetchParentOrdinal.length);
      prefetchParentOrdinal = newarray;
    }
    prefetchParentOrdinal[ordinal] = parentOrdinal;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/412.java