error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3808.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3808.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[3,1]

error in qdox parser
file content:
```java
offset: 37
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3808.java
text:
```scala
public final class BytesRefArray {

p@@ackage org.apache.lucene.search.suggest;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import java.util.Arrays;
import java.util.Comparator;

import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.ByteBlockPool;
import org.apache.lucene.util.BytesRefIterator;
import org.apache.lucene.util.Counter;
import org.apache.lucene.util.RamUsageEstimator;
import org.apache.lucene.util.SorterTemplate;

/**
 * A simple append only random-access {@link BytesRef} array that stores full
 * copies of the appended bytes in a {@link ByteBlockPool}.
 * 
 * 
 * <b>Note: This class is not Thread-Safe!</b>
 * 
 * @lucene.internal
 * @lucene.experimental
 */
final class BytesRefArray {
  private final ByteBlockPool pool;
  private int[] offsets = new int[1];
  private int lastElement = 0;
  private int currentOffset = 0;
  private final Counter bytesUsed;
  
  /**
   * Creates a new {@link BytesRefArray} with a counter to track allocated bytes
   */
  public BytesRefArray(Counter bytesUsed) {
    this.pool = new ByteBlockPool(new ByteBlockPool.DirectTrackingAllocator(
        bytesUsed));
    pool.nextBuffer();
    bytesUsed.addAndGet(RamUsageEstimator.NUM_BYTES_ARRAY_HEADER
        + RamUsageEstimator.NUM_BYTES_INT);
    this.bytesUsed = bytesUsed;
  }
 
  /**
   * Clears this {@link BytesRefArray}
   */
  public void clear() {
    lastElement = 0;
    currentOffset = 0;
    Arrays.fill(offsets, 0);
    pool.reset(false, true); // no need to 0 fill the buffers we control the allocator
  }
  
  /**
   * Appends a copy of the given {@link BytesRef} to this {@link BytesRefArray}.
   * @param bytes the bytes to append
   * @return the ordinal of the appended bytes
   */
  public int append(BytesRef bytes) {
    if (lastElement >= offsets.length) {
      int oldLen = offsets.length;
      offsets = ArrayUtil.grow(offsets, offsets.length + 1);
      bytesUsed.addAndGet((offsets.length - oldLen)
          * RamUsageEstimator.NUM_BYTES_INT);
    }
    pool.append(bytes);
    offsets[lastElement++] = currentOffset;
    currentOffset += bytes.length;
    return lastElement;
  }
  
  /**
   * Returns the current size of this {@link BytesRefArray}
   * @return the current size of this {@link BytesRefArray}
   */
  public int size() {
    return lastElement;
  }
  
  /**
   * Returns the <i>n'th</i> element of this {@link BytesRefArray}
   * @param spare a spare {@link BytesRef} instance
   * @param ord the elements ordinal to retrieve 
   * @return the <i>n'th</i> element of this {@link BytesRefArray}
   */
  public BytesRef get(BytesRef spare, int ord) {
    if (lastElement > ord) {
      int offset = offsets[ord];
      int length = ord == lastElement - 1 ? currentOffset - offset
          : offsets[ord + 1] - offset;
      pool.readBytes(spare, offset, length);
      return spare;
    }
    throw new IndexOutOfBoundsException("index " + ord
        + " must be less than the size: " + lastElement);
    
  }
  
  private int[] sort(final Comparator<BytesRef> comp) {
    final int[] orderedEntries = new int[size()];
    for (int i = 0; i < orderedEntries.length; i++) {
      orderedEntries[i] = i;
    }
    new SorterTemplate() {
      @Override
      protected void swap(int i, int j) {
        final int o = orderedEntries[i];
        orderedEntries[i] = orderedEntries[j];
        orderedEntries[j] = o;
      }
      
      @Override
      protected int compare(int i, int j) {
        final int ord1 = orderedEntries[i], ord2 = orderedEntries[j];
        return comp.compare(get(scratch1, ord1), get(scratch2, ord2));
      }
      
      @Override
      protected void setPivot(int i) {
        final int ord = orderedEntries[i];
        get(pivot, ord);
      }
      
      @Override
      protected int comparePivot(int j) {
        final int ord = orderedEntries[j];
        return comp.compare(pivot, get(scratch2, ord));
      }
      
      private final BytesRef pivot = new BytesRef(), scratch1 = new BytesRef(),
          scratch2 = new BytesRef();
    }.quickSort(0, size() - 1);
    return orderedEntries;
  }
  
  /**
   * sugar for {@link #iterator(Comparator)} with a <code>null</code> comparator
   */
  public BytesRefIterator iterator() {
    return iterator(null);
  }
  
  /**
   * <p>
   * Returns a {@link BytesRefIterator} with point in time semantics. The
   * iterator provides access to all so far appended {@link BytesRef} instances.
   * </p>
   * <p>
   * If a non <code>null</code> {@link Comparator} is provided the iterator will
   * iterate the byte values in the order specified by the comparator. Otherwise
   * the order is the same as the values were appended.
   * </p>
   * <p>
   * This is a non-destructive operation.
   * </p>
   */
  public BytesRefIterator iterator(final Comparator<BytesRef> comp) {
    final BytesRef spare = new BytesRef();
    final int size = size();
    final int[] ords = comp == null ? null : sort(comp);
    return new BytesRefIterator() {
      int pos = 0;
      
      @Override
      public BytesRef next() {
        if (pos < size) {
          return get(spare, ords == null ? pos++ : ords[pos++]);
        }
        return null;
      }
      
      @Override
      public Comparator<BytesRef> getComparator() {
        return comp;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3808.java