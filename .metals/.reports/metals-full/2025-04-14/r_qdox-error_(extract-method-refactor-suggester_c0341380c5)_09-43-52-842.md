error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1616.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1616.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1616.java
text:
```scala
i@@nt mid = (lo + hi) >>> 1;

package org.apache.lucene.index;

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

/** Remaps docIDs after a merge has completed, where the
 *  merged segments had at least one deletion.  This is used
 *  to renumber the buffered deletes in IndexWriter when a
 *  merge of segments with deletions commits. */

final class MergeDocIDRemapper {
  int[] starts;                                 // used for binary search of mapped docID
  int[] newStarts;                              // starts, minus the deletes
  int[][] docMaps;                              // maps docIDs in the merged set
  int minDocID;                                 // minimum docID that needs renumbering
  int maxDocID;                                 // 1+ the max docID that needs renumbering
  int docShift;                                 // total # deleted docs that were compacted by this merge

  public MergeDocIDRemapper(SegmentInfos infos, int[][] docMaps, int[] delCounts, MergePolicy.OneMerge merge, int mergedDocCount) {
    this.docMaps = docMaps;
    SegmentInfo firstSegment = merge.segments.info(0);
    int i = 0;
    while(true) {
      SegmentInfo info = infos.info(i);
      if (info.equals(firstSegment))
        break;
      minDocID += info.docCount;
      i++;
    }

    int numDocs = 0;
    for(int j=0;j<docMaps.length;i++,j++) {
      numDocs += infos.info(i).docCount;
      assert infos.info(i).equals(merge.segments.info(j));
    }
    maxDocID = minDocID + numDocs;

    starts = new int[docMaps.length];
    newStarts = new int[docMaps.length];

    starts[0] = minDocID;
    newStarts[0] = minDocID;
    for(i=1;i<docMaps.length;i++) {
      final int lastDocCount = merge.segments.info(i-1).docCount;
      starts[i] = starts[i-1] + lastDocCount;
      newStarts[i] = newStarts[i-1] + lastDocCount - delCounts[i-1];
    }
    docShift = numDocs - mergedDocCount;

    // There are rare cases when docShift is 0.  It happens
    // if you try to delete a docID that's out of bounds,
    // because the SegmentReader still allocates deletedDocs
    // and pretends it has deletions ... so we can't make
    // this assert here
    // assert docShift > 0;

    // Make sure it all adds up:
    assert docShift == maxDocID - (newStarts[docMaps.length-1] + merge.segments.info(docMaps.length-1).docCount - delCounts[docMaps.length-1]);
  }

  public int remap(int oldDocID) {
    if (oldDocID < minDocID)
      // Unaffected by merge
      return oldDocID;
    else if (oldDocID >= maxDocID)
      // This doc was "after" the merge, so simple shift
      return oldDocID - docShift;
    else {
      // Binary search to locate this document & find its new docID
      int lo = 0;                                      // search starts array
      int hi = docMaps.length - 1;                  // for first element less

      while (hi >= lo) {
        int mid = (lo + hi) >> 1;
        int midValue = starts[mid];
        if (oldDocID < midValue)
          hi = mid - 1;
        else if (oldDocID > midValue)
          lo = mid + 1;
        else {                                      // found a match
          while (mid+1 < docMaps.length && starts[mid+1] == midValue) {
            mid++;                                  // scan to last match
          }
          if (docMaps[mid] != null)
            return newStarts[mid] + docMaps[mid][oldDocID-starts[mid]];
          else
            return newStarts[mid] + oldDocID-starts[mid];
        }
      }
      if (docMaps[hi] != null)
        return newStarts[hi] + docMaps[hi][oldDocID-starts[hi]];
      else
        return newStarts[hi] + oldDocID-starts[hi];
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1616.java