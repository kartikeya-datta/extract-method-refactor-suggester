error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2337.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2337.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2337.java
text:
```scala
s@@uper(dir, id, CODEC_NAME, VERSION_CURRENT, bytesUsed, context, fasterButMoreRam, Type.BYTES_FIXED_SORTED);

package org.apache.lucene.codecs.lucene40.values;

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
import java.util.Comparator;
import java.util.List;

import org.apache.lucene.codecs.lucene40.values.Bytes.BytesReaderBase;
import org.apache.lucene.codecs.lucene40.values.Bytes.BytesSortedSourceBase;
import org.apache.lucene.codecs.lucene40.values.Bytes.DerefBytesWriterBase;
import org.apache.lucene.index.DocValues;
import org.apache.lucene.index.SortedBytesMergeUtils;
import org.apache.lucene.index.DocValues.SortedSource;
import org.apache.lucene.index.DocValues.Type;
import org.apache.lucene.index.SortedBytesMergeUtils.IndexOutputBytesRefConsumer;
import org.apache.lucene.index.SortedBytesMergeUtils.MergeContext;
import org.apache.lucene.index.SortedBytesMergeUtils.SortedSourceSlice;
import org.apache.lucene.index.MergeState;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Counter;
import org.apache.lucene.util.IOUtils;
import org.apache.lucene.util.packed.PackedInts;

// Stores fixed-length byte[] by deref, ie when two docs
// have the same value, they store only 1 byte[]

/**
 * @lucene.experimental
 */
class FixedSortedBytesImpl {

  static final String CODEC_NAME = "FixedSortedBytes";
  static final int VERSION_START = 0;
  static final int VERSION_CURRENT = VERSION_START;

  static final class Writer extends DerefBytesWriterBase {
    private final Comparator<BytesRef> comp;

    public Writer(Directory dir, String id, Comparator<BytesRef> comp,
        Counter bytesUsed, IOContext context, boolean fasterButMoreRam) throws IOException {
      super(dir, id, CODEC_NAME, VERSION_CURRENT, bytesUsed, context, fasterButMoreRam);
      this.comp = comp;
    }

    @Override
    public void merge(MergeState mergeState, DocValues[] docValues)
        throws IOException {
      boolean success = false;
      try {
        final MergeContext ctx = SortedBytesMergeUtils.init(Type.BYTES_FIXED_SORTED, docValues, comp, mergeState.mergedDocCount);
        List<SortedSourceSlice> slices = SortedBytesMergeUtils.buildSlices(mergeState.docBase, mergeState.docMaps, docValues, ctx);
        final IndexOutput datOut = getOrCreateDataOut();
        datOut.writeInt(ctx.sizePerValues);
        final int maxOrd = SortedBytesMergeUtils.mergeRecords(ctx, new IndexOutputBytesRefConsumer(datOut), slices);
        
        final IndexOutput idxOut = getOrCreateIndexOut();
        idxOut.writeInt(maxOrd);
        final PackedInts.Writer ordsWriter = PackedInts.getWriter(idxOut, ctx.docToEntry.length,
            PackedInts.bitsRequired(maxOrd));
        for (SortedSourceSlice slice : slices) {
          slice.writeOrds(ordsWriter);
        }
        ordsWriter.finish();
        success = true;
      } finally {
        releaseResources();
        if (success) {
          IOUtils.close(getIndexOut(), getDataOut());
        } else {
          IOUtils.closeWhileHandlingException(getIndexOut(), getDataOut());
        }

      }
    }

    // Important that we get docCount, in case there were
    // some last docs that we didn't see
    @Override
    public void finishInternal(int docCount) throws IOException {
      fillDefault(docCount);
      final IndexOutput datOut = getOrCreateDataOut();
      final int count = hash.size();
      final int[] address = new int[count];
      datOut.writeInt(size);
      if (size != -1) {
        final int[] sortedEntries = hash.sort(comp);
        // first dump bytes data, recording address as we go
        final BytesRef spare = new BytesRef(size);
        for (int i = 0; i < count; i++) {
          final int e = sortedEntries[i];
          final BytesRef bytes = hash.get(e, spare);
          assert bytes.length == size;
          datOut.writeBytes(bytes.bytes, bytes.offset, bytes.length);
          address[e] = i;
        }
      }
      final IndexOutput idxOut = getOrCreateIndexOut();
      idxOut.writeInt(count);
      writeIndex(idxOut, docCount, count, address, docToEntry);
    }
  }

  static final class Reader extends BytesReaderBase {
    private final int size;
    private final int valueCount;
    private final Comparator<BytesRef> comparator;

    public Reader(Directory dir, String id, int maxDoc, IOContext context,
        Type type, Comparator<BytesRef> comparator) throws IOException {
      super(dir, id, CODEC_NAME, VERSION_START, true, context, type);
      size = datIn.readInt();
      valueCount = idxIn.readInt();
      this.comparator = comparator;
    }

    @Override
    public Source load() throws IOException {
      return new FixedSortedSource(cloneData(), cloneIndex(), size, valueCount,
          comparator);
    }

    @Override
    public Source getDirectSource() throws IOException {
      return new DirectFixedSortedSource(cloneData(), cloneIndex(), size,
          valueCount, comparator, type);
    }

    @Override
    public int getValueSize() {
      return size;
    }
  }

  static final class FixedSortedSource extends BytesSortedSourceBase {
    private final int valueCount;
    private final int size;

    FixedSortedSource(IndexInput datIn, IndexInput idxIn, int size,
        int numValues, Comparator<BytesRef> comp) throws IOException {
      super(datIn, idxIn, comp, size * numValues, Type.BYTES_FIXED_SORTED,
          false);
      this.size = size;
      this.valueCount = numValues;
      closeIndexInput();
    }

    @Override
    public int getValueCount() {
      return valueCount;
    }

    @Override
    public BytesRef getByOrd(int ord, BytesRef bytesRef) {
      return data.fillSlice(bytesRef, (ord * size), size);
    }
  }

  static final class DirectFixedSortedSource extends SortedSource {
    final PackedInts.Reader docToOrdIndex;
    private final IndexInput datIn;
    private final long basePointer;
    private final int size;
    private final int valueCount;

    DirectFixedSortedSource(IndexInput datIn, IndexInput idxIn, int size,
        int valueCount, Comparator<BytesRef> comp, Type type)
        throws IOException {
      super(type, comp);
      docToOrdIndex = PackedInts.getDirectReader(idxIn);
      basePointer = datIn.getFilePointer();
      this.datIn = datIn;
      this.size = size;
      this.valueCount = valueCount;
    }

    @Override
    public int ord(int docID) {
      return (int) docToOrdIndex.get(docID);
    }

    @Override
    public boolean hasPackedDocToOrd() {
      return true;
    }

    @Override
    public PackedInts.Reader getDocToOrd() {
      return docToOrdIndex;
    }

    @Override
    public BytesRef getByOrd(int ord, BytesRef bytesRef) {
      try {
        datIn.seek(basePointer + size * ord);
        bytesRef.grow(size);
        datIn.readBytes(bytesRef.bytes, 0, size);
        bytesRef.length = size;
        bytesRef.offset = 0;
        return bytesRef;
      } catch (IOException ex) {
        throw new IllegalStateException("failed to getByOrd", ex);
      }
    }

    @Override
    public int getValueCount() {
      return valueCount;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2337.java