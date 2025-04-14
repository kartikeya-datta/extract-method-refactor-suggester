error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2533.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2533.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2533.java
text:
```scala
i@@f (pos >= valueCount) {

package org.apache.lucene.index.values;

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

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.lucene.index.values.Bytes.BytesBaseSource;
import org.apache.lucene.index.values.Bytes.BytesReaderBase;
import org.apache.lucene.index.values.Bytes.BytesWriterBase;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.util.ByteBlockPool;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.BytesRefHash;
import org.apache.lucene.util.PagedBytes;
import org.apache.lucene.util.RamUsageEstimator;
import org.apache.lucene.util.ByteBlockPool.Allocator;
import org.apache.lucene.util.ByteBlockPool.DirectAllocator;
import org.apache.lucene.util.BytesRefHash.TrackingDirectBytesStartArray;
import org.apache.lucene.util.packed.PackedInts;

// Stores fixed-length byte[] by deref, ie when two docs
// have the same value, they store only 1 byte[]
/**
 * @lucene.experimental
 */
class FixedDerefBytesImpl {

  static final String CODEC_NAME = "FixedDerefBytes";
  static final int VERSION_START = 0;
  static final int VERSION_CURRENT = VERSION_START;

  static class Writer extends BytesWriterBase {
    private int size = -1;
    private int[] docToID;
    private final BytesRefHash hash = new BytesRefHash(pool, BytesRefHash.DEFAULT_CAPACITY,
        new TrackingDirectBytesStartArray(BytesRefHash.DEFAULT_CAPACITY, bytesUsed));

    public Writer(Directory dir, String id, AtomicLong bytesUsed) throws IOException {
      this(dir, id, new DirectAllocator(ByteBlockPool.BYTE_BLOCK_SIZE),
          bytesUsed);
    }

    public Writer(Directory dir, String id, Allocator allocator,
        AtomicLong bytesUsed) throws IOException {
      super(dir, id, CODEC_NAME, VERSION_CURRENT, false, false,
          new ByteBlockPool(allocator), bytesUsed);
      docToID = new int[1];
      bytesUsed.addAndGet(RamUsageEstimator.NUM_BYTES_INT); // TODO BytesRefHash uses bytes too!
    }

    @Override
    synchronized public void add(int docID, BytesRef bytes) throws IOException {
      if (bytes.length == 0) // default value - skip it
        return;
      if (size == -1) {
        size = bytes.length;
        initDataOut();
        datOut.writeInt(size);
      } else if (bytes.length != size) {
        throw new IllegalArgumentException("expected bytes size=" + size
            + " but got " + bytes.length);
      }
      int ord = hash.add(bytes);

      if (ord >= 0) {
        // new added entry
        datOut.writeBytes(bytes.bytes, bytes.offset, bytes.length);
      } else {
        ord = (-ord) - 1;
      }

      if (docID >= docToID.length) {
        final int size = docToID.length;
        docToID = ArrayUtil.grow(docToID, 1 + docID);
        bytesUsed.addAndGet((docToID.length - size)
            * RamUsageEstimator.NUM_BYTES_INT);
      }
      docToID[docID] = 1 + ord;
    }

    // Important that we get docCount, in case there were
    // some last docs that we didn't see
    @Override
    synchronized public void finish(int docCount) throws IOException {
      if (datOut == null) // no added data
        return;
      initIndexOut();
      final int count = 1 + hash.size();
      idxOut.writeInt(count - 1);
      // write index
      final PackedInts.Writer w = PackedInts.getWriter(idxOut, docCount,
          PackedInts.bitsRequired(count - 1));
      final int limit = docCount > docToID.length ? docToID.length : docCount;
      for (int i = 0; i < limit; i++) {
        w.add(docToID[i]);
      }
      // fill up remaining doc with zeros
      for (int i = limit; i < docCount; i++) {
        w.add(0);
      }
      w.finish();
      hash.close();
      super.finish(docCount);
      bytesUsed.addAndGet((-docToID.length)
          * RamUsageEstimator.NUM_BYTES_INT);
      docToID = null;
    }
  }

  public static class Reader extends BytesReaderBase {
    private final int size;

    Reader(Directory dir, String id, int maxDoc) throws IOException {
      super(dir, id, CODEC_NAME, VERSION_START, true);
      try {
        size = datIn.readInt();
      } catch (IOException e) {
        throw e;
      }
    }

    @Override
    public Source load() throws IOException {
      final IndexInput index = cloneIndex();
      return new Source(cloneData(), index, size, index.readInt());
    }

    private static class Source extends BytesBaseSource {
      private final PackedInts.Reader index;
      private final int size;
      private final int numValues;

      protected Source(IndexInput datIn, IndexInput idxIn, int size,
          int numValues) throws IOException {
        super(datIn, idxIn, new PagedBytes(PAGED_BYTES_BITS), size * numValues);
        this.size = size;
        this.numValues = numValues;
        index = PackedInts.getReader(idxIn);
      }

      @Override
      public BytesRef getBytes(int docID, BytesRef bytesRef) {
        final int id = (int) index.get(docID);
        if (id == 0) {
          return null;
        }
        return data.fillSlice(bytesRef, ((id - 1) * size), size);
      }

      @Override
      public int getValueCount() {
        return numValues;
      }

      @Override
      public Type type() {
        return Type.BYTES_FIXED_DEREF;
      }

      @Override
      protected int maxDoc() {
        return index.size();
      }
    }

    @Override
    public DocValuesEnum getEnum(AttributeSource source) throws IOException {
      return new DerefBytesEnum(source, cloneData(), cloneIndex(),
          size);
    }

    static class DerefBytesEnum extends DocValuesEnum {
      protected final IndexInput datIn;
      private final PackedInts.ReaderIterator idx;
      protected final long fp;
      private final int size;
      private final int valueCount;
      private int pos = -1;

      public DerefBytesEnum(AttributeSource source, IndexInput datIn,
          IndexInput idxIn, int size) throws IOException {
        this(source, datIn, idxIn, size, Type.BYTES_FIXED_DEREF);
      }

      protected DerefBytesEnum(AttributeSource source, IndexInput datIn,
          IndexInput idxIn, int size, Type enumType)
          throws IOException {
        super(source, enumType);
        this.datIn = datIn;
        this.size = size;
        idxIn.readInt();// read valueCount
        idx = PackedInts.getReaderIterator(idxIn);
        fp = datIn.getFilePointer();
        bytesRef.grow(this.size);
        bytesRef.length = this.size;
        bytesRef.offset = 0;
        valueCount = idx.size();
      }
      

      protected void copyReferences(DocValuesEnum valuesEnum) {
        bytesRef = valuesEnum.bytesRef;
        if(bytesRef.bytes.length < size) {
          bytesRef.grow(size);
        }
        bytesRef.length = size;
        bytesRef.offset = 0;
      }

      @Override
      public int advance(int target) throws IOException {
        if (target < valueCount) {
          long address;
          while ((address = idx.advance(target)) == 0) {
            if (++target >= valueCount) {
              return pos = NO_MORE_DOCS;
            }
          }
          pos = idx.ord();
          fill(address, bytesRef);
          return pos;
        }
        return pos = NO_MORE_DOCS;
      }

      @Override
      public int nextDoc() throws IOException {
        if (pos < valueCount) {
          return pos = NO_MORE_DOCS;
        }
        return advance(pos + 1);
      }

      public void close() throws IOException {
        datIn.close();
        idx.close();
      }

      protected void fill(long address, BytesRef ref) throws IOException {
        datIn.seek(fp + ((address - 1) * size));
        datIn.readBytes(ref.bytes, 0, size);
        ref.length = size;
        ref.offset = 0;
      }

      @Override
      public int docID() {
        return pos;
      }

    }

    @Override
    public Type type() {
      return Type.BYTES_FIXED_DEREF;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2533.java