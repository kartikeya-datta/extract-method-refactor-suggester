error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3710.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3710.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3710.java
text:
```scala
I@@ndexFileNames.segmentFileName(id, Bytes.DV_SEGMENT_SUFFIX, DocValuesWriterBase.DATA_EXTENSION),

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

import org.apache.lucene.codecs.lucene40.values.DocValuesArray.LongValues;
import org.apache.lucene.codecs.lucene40.values.FixedStraightBytesImpl.FixedBytesWriterBase;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DocValues.Source;
import org.apache.lucene.index.DocValues.Type;
import org.apache.lucene.index.DocValues;
import org.apache.lucene.index.IndexFileNames;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.CodecUtil;
import org.apache.lucene.util.Counter;
import org.apache.lucene.util.IOUtils;
import org.apache.lucene.util.packed.PackedInts;

/**
 * Stores integers using {@link PackedInts}
 * 
 * @lucene.experimental
 * */
class PackedIntValues {

  private static final String CODEC_NAME = "PackedInts";
  private static final byte PACKED = 0x00;
  private static final byte FIXED_64 = 0x01;

  static final int VERSION_START = 0;
  static final int VERSION_CURRENT = VERSION_START;

  static class PackedIntsWriter extends FixedBytesWriterBase {

    private long minValue;
    private long maxValue;
    private boolean started;
    private int lastDocId = -1;

    protected PackedIntsWriter(Directory dir, String id, Counter bytesUsed,
        IOContext context) throws IOException {
      super(dir, id, CODEC_NAME, VERSION_CURRENT, bytesUsed, context);
      bytesRef = new BytesRef(8);
    }

    protected void add(int docID, long v) throws IOException {
      assert lastDocId < docID;
      if (!started) {
        started = true;
        minValue = maxValue = v;
      } else {
        if (v < minValue) {
          minValue = v;
        } else if (v > maxValue) {
          maxValue = v;
        }
      }
      lastDocId = docID;
      BytesRefUtils.copyLong(bytesRef, v);
      add(docID, bytesRef);
    }

    @Override
    public void finish(int docCount) throws IOException {
      boolean success = false;
      final IndexOutput dataOut = getOrCreateDataOut();
      try {
        if (!started) {
          minValue = maxValue = 0;
        }
        final long delta = maxValue - minValue;
        // if we exceed the range of positive longs we must switch to fixed
        // ints
        if (delta <= (maxValue >= 0 && minValue <= 0 ? Long.MAX_VALUE
            : Long.MAX_VALUE - 1) && delta >= 0) {
          dataOut.writeByte(PACKED);
          writePackedInts(dataOut, docCount);
          return; // done
        } else {
          dataOut.writeByte(FIXED_64);
        }
        writeData(dataOut);
        writeZeros(docCount - (lastDocID + 1), dataOut);
        success = true;
      } finally {
        resetPool();
        if (success) {
          IOUtils.close(dataOut);
        } else {
          IOUtils.closeWhileHandlingException(dataOut);
        }
      }
    }

    @Override
    protected void mergeDoc(Field scratchField, Source source, int docID, int sourceDoc) throws IOException {
      assert docID > lastDocId : "docID: " + docID
          + " must be greater than the last added doc id: " + lastDocId;
        add(docID, source.getInt(sourceDoc));
    }

    private void writePackedInts(IndexOutput datOut, int docCount) throws IOException {
      datOut.writeLong(minValue);
      
      // write a default value to recognize docs without a value for that
      // field
      final long defaultValue = maxValue >= 0 && minValue <= 0 ? 0 - minValue
          : ++maxValue - minValue;
      datOut.writeLong(defaultValue);
      PackedInts.Writer w = PackedInts.getWriter(datOut, docCount,
          PackedInts.bitsRequired(maxValue - minValue));
      for (int i = 0; i < lastDocID + 1; i++) {
        set(bytesRef, i);
        byte[] bytes = bytesRef.bytes;
        int offset = bytesRef.offset;
        long asLong =  
           (((long)(bytes[offset+0] & 0xff) << 56) |
            ((long)(bytes[offset+1] & 0xff) << 48) |
            ((long)(bytes[offset+2] & 0xff) << 40) |
            ((long)(bytes[offset+3] & 0xff) << 32) |
            ((long)(bytes[offset+4] & 0xff) << 24) |
            ((long)(bytes[offset+5] & 0xff) << 16) |
            ((long)(bytes[offset+6] & 0xff) <<  8) |
            ((long)(bytes[offset+7] & 0xff)));
        w.add(asLong == 0 ? defaultValue : asLong - minValue);
      }
      for (int i = lastDocID + 1; i < docCount; i++) {
        w.add(defaultValue);
      }
      w.finish();
    }

    @Override
    public void add(int docID, IndexableField docValue) throws IOException {
      add(docID, docValue.numericValue().longValue());
    }
  }

  /**
   * Opens all necessary files, but does not read any data in until you call
   * {@link #load}.
   */
  static class PackedIntsReader extends DocValues {
    private final IndexInput datIn;
    private final byte type;
    private final int numDocs;
    private final LongValues values;

    protected PackedIntsReader(Directory dir, String id, int numDocs,
        IOContext context) throws IOException {
      datIn = dir.openInput(
                IndexFileNames.segmentFileName(id, Bytes.DV_SEGMENT_SUFFIX, Writer.DATA_EXTENSION),
          context);
      this.numDocs = numDocs;
      boolean success = false;
      try {
        CodecUtil.checkHeader(datIn, CODEC_NAME, VERSION_START, VERSION_START);
        type = datIn.readByte();
        values = type == FIXED_64 ? new LongValues() : null;
        success = true;
      } finally {
        if (!success) {
          IOUtils.closeWhileHandlingException(datIn);
        }
      }
    }


    /**
     * Loads the actual values. You may call this more than once, eg if you
     * already previously loaded but then discarded the Source.
     */
    @Override
    public Source load() throws IOException {
      boolean success = false;
      final Source source;
      IndexInput input = null;
      try {
        input = (IndexInput) datIn.clone();
        
        if (values == null) {
          source = new PackedIntsSource(input, false);
        } else {
          source = values.newFromInput(input, numDocs);
        }
        success = true;
        return source;
      } finally {
        if (!success) {
          IOUtils.closeWhileHandlingException(input, datIn);
        }
      }
    }

    @Override
    public void close() throws IOException {
      super.close();
      datIn.close();
    }


    @Override
    public Type type() {
      return Type.VAR_INTS;
    }


    @Override
    public Source getDirectSource() throws IOException {
      return values != null ? new FixedStraightBytesImpl.DirectFixedStraightSource((IndexInput) datIn.clone(), 8, Type.FIXED_INTS_64) : new PackedIntsSource((IndexInput) datIn.clone(), true);
    }
  }

  
  static class PackedIntsSource extends Source {
    private final long minValue;
    private final long defaultValue;
    private final PackedInts.Reader values;

    public PackedIntsSource(IndexInput dataIn, boolean direct) throws IOException {
      super(Type.VAR_INTS);
      minValue = dataIn.readLong();
      defaultValue = dataIn.readLong();
      values = direct ? PackedInts.getDirectReader(dataIn) : PackedInts.getReader(dataIn);
    }
    
    @Override
    public BytesRef getBytes(int docID, BytesRef ref) {
      ref.grow(8);
      BytesRefUtils.copyLong(ref, getInt(docID));
      return ref;
    }

    @Override
    public long getInt(int docID) {
      // TODO -- can we somehow avoid 2X method calls
      // on each get? must push minValue down, and make
      // PackedInts implement Ints.Source
      assert docID >= 0;
      final long value = values.get(docID);
      return value == defaultValue ? 0 : minValue + value;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3710.java