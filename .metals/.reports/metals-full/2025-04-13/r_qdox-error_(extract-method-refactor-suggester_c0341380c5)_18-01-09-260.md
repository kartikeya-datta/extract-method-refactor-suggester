error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2339.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2339.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2339.java
text:
```scala
s@@uper(dir, id, CODEC_NAME, VERSION_CURRENT, bytesUsed, context, fasterButMoreRam, Type.BYTES_VAR_SORTED);

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

// Stores variable-length byte[] by deref, ie when two docs
// have the same value, they store only 1 byte[] and both
// docs reference that single source

/**
 * @lucene.experimental
 */
final class VarSortedBytesImpl {

  static final String CODEC_NAME = "VarDerefBytes";
  static final int VERSION_START = 0;
  static final int VERSION_CURRENT = VERSION_START;

  final static class Writer extends DerefBytesWriterBase {
    private final Comparator<BytesRef> comp;

    public Writer(Directory dir, String id, Comparator<BytesRef> comp,
        Counter bytesUsed, IOContext context, boolean fasterButMoreRam) throws IOException {
      super(dir, id, CODEC_NAME, VERSION_CURRENT, bytesUsed, context, fasterButMoreRam);
      this.comp = comp;
      size = 0;
    }

    @Override
    public void merge(MergeState mergeState, DocValues[] docValues)
        throws IOException {
      boolean success = false;
      try {
        MergeContext ctx = SortedBytesMergeUtils.init(Type.BYTES_VAR_SORTED, docValues, comp, mergeState.mergedDocCount);
        final List<SortedSourceSlice> slices = SortedBytesMergeUtils.buildSlices(mergeState.docBase, mergeState.docMaps, docValues, ctx);
        IndexOutput datOut = getOrCreateDataOut();
        
        ctx.offsets = new long[1];
        final int maxOrd = SortedBytesMergeUtils.mergeRecords(ctx, new IndexOutputBytesRefConsumer(datOut), slices);
        final long[] offsets = ctx.offsets;
        maxBytes = offsets[maxOrd-1];
        final IndexOutput idxOut = getOrCreateIndexOut();
        
        idxOut.writeLong(maxBytes);
        final PackedInts.Writer offsetWriter = PackedInts.getWriter(idxOut, maxOrd+1,
            PackedInts.bitsRequired(maxBytes));
        offsetWriter.add(0);
        for (int i = 0; i < maxOrd; i++) {
          offsetWriter.add(offsets[i]);
        }
        offsetWriter.finish();
        
        final PackedInts.Writer ordsWriter = PackedInts.getWriter(idxOut, ctx.docToEntry.length,
            PackedInts.bitsRequired(maxOrd-1));
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

    @Override
    protected void checkSize(BytesRef bytes) {
      // allow var bytes sizes
    }

    // Important that we get docCount, in case there were
    // some last docs that we didn't see
    @Override
    public void finishInternal(int docCount) throws IOException {
      fillDefault(docCount);
      final int count = hash.size();
      final IndexOutput datOut = getOrCreateDataOut();
      final IndexOutput idxOut = getOrCreateIndexOut();
      long offset = 0;
      final int[] index = new int[count];
      final int[] sortedEntries = hash.sort(comp);
      // total bytes of data
      idxOut.writeLong(maxBytes);
      PackedInts.Writer offsetWriter = PackedInts.getWriter(idxOut, count+1,
          bitsRequired(maxBytes));
      // first dump bytes data, recording index & write offset as
      // we go
      final BytesRef spare = new BytesRef();
      for (int i = 0; i < count; i++) {
        final int e = sortedEntries[i];
        offsetWriter.add(offset);
        index[e] = i;
        final BytesRef bytes = hash.get(e, spare);
        // TODO: we could prefix code...
        datOut.writeBytes(bytes.bytes, bytes.offset, bytes.length);
        offset += bytes.length;
      }
      // write sentinel
      offsetWriter.add(offset);
      offsetWriter.finish();
      // write index
      writeIndex(idxOut, docCount, count, index, docToEntry);

    }
  }

  public static class Reader extends BytesReaderBase {

    private final Comparator<BytesRef> comparator;

    Reader(Directory dir, String id, int maxDoc,
        IOContext context, Type type, Comparator<BytesRef> comparator)
        throws IOException {
      super(dir, id, CODEC_NAME, VERSION_START, true, context, type);
      this.comparator = comparator;
    }

    @Override
    public org.apache.lucene.index.DocValues.Source load()
        throws IOException {
      return new VarSortedSource(cloneData(), cloneIndex(), comparator);
    }

    @Override
    public Source getDirectSource() throws IOException {
      return new DirectSortedSource(cloneData(), cloneIndex(), comparator, type());
    }
    
  }
  private static final class VarSortedSource extends BytesSortedSourceBase {
    private final int valueCount;

    VarSortedSource(IndexInput datIn, IndexInput idxIn,
        Comparator<BytesRef> comp) throws IOException {
      super(datIn, idxIn, comp, idxIn.readLong(), Type.BYTES_VAR_SORTED, true);
      valueCount = ordToOffsetIndex.size()-1; // the last value here is just a dummy value to get the length of the last value
      closeIndexInput();
    }

    @Override
    public BytesRef getByOrd(int ord, BytesRef bytesRef) {
      final long offset = ordToOffsetIndex.get(ord);
      final long nextOffset = ordToOffsetIndex.get(1 + ord);
      data.fillSlice(bytesRef, offset, (int) (nextOffset - offset));
      return bytesRef;
    }

    @Override
    public int getValueCount() {
      return valueCount;
    }
  }

  private static final class DirectSortedSource extends SortedSource {
    private final PackedInts.Reader docToOrdIndex;
    private final PackedInts.Reader ordToOffsetIndex;
    private final IndexInput datIn;
    private final long basePointer;
    private final int valueCount;
    
    DirectSortedSource(IndexInput datIn, IndexInput idxIn,
        Comparator<BytesRef> comparator, Type type) throws IOException {
      super(type, comparator);
      idxIn.readLong();
      ordToOffsetIndex = PackedInts.getDirectReader(idxIn);
      valueCount = ordToOffsetIndex.size()-1; // the last value here is just a dummy value to get the length of the last value
      // advance this iterator to the end and clone the stream once it points to the docToOrdIndex header
      ordToOffsetIndex.get(valueCount);
      docToOrdIndex = PackedInts.getDirectReader((IndexInput) idxIn.clone()); // read the ords in to prevent too many random disk seeks
      basePointer = datIn.getFilePointer();
      this.datIn = datIn;
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
        final long offset = ordToOffsetIndex.get(ord);
        // 1+ord is safe because we write a sentinel at the end
        final long nextOffset = ordToOffsetIndex.get(1+ord);
        datIn.seek(basePointer + offset);
        final int length = (int) (nextOffset - offset);
        bytesRef.grow(length);
        datIn.readBytes(bytesRef.bytes, 0, length);
        bytesRef.length = length;
        bytesRef.offset = 0;
        return bytesRef;
      } catch (IOException ex) {
        throw new IllegalStateException("failed", ex);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2339.java