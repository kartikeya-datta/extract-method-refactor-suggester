error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2336.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2336.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2336.java
text:
```scala
s@@uper(dir, id, CODEC_NAME, VERSION_CURRENT, bytesUsed, context, Type.BYTES_VAR_DEREF);

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

import org.apache.lucene.codecs.lucene40.values.Bytes.BytesReaderBase;
import org.apache.lucene.codecs.lucene40.values.Bytes.BytesSourceBase;
import org.apache.lucene.codecs.lucene40.values.Bytes.DerefBytesWriterBase;
import org.apache.lucene.index.DocValues.Type;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Counter;
import org.apache.lucene.util.PagedBytes;
import org.apache.lucene.util.packed.PackedInts;

// Stores variable-length byte[] by deref, ie when two docs
// have the same value, they store only 1 byte[] and both
// docs reference that single source

/**
 * @lucene.experimental
 */
class VarDerefBytesImpl {

  static final String CODEC_NAME = "VarDerefBytes";
  static final int VERSION_START = 0;
  static final int VERSION_CURRENT = VERSION_START;

  /*
   * TODO: if impls like this are merged we are bound to the amount of memory we
   * can store into a BytesRefHash and therefore how much memory a ByteBlockPool
   * can address. This is currently limited to 2GB. While we could extend that
   * and use 64bit for addressing this still limits us to the existing main
   * memory as all distinct bytes will be loaded up into main memory. We could
   * move the byte[] writing to #finish(int) and store the bytes in sorted
   * order and merge them in a streamed fashion. 
   */
  static class Writer extends DerefBytesWriterBase {
    public Writer(Directory dir, String id, Counter bytesUsed, IOContext context)
        throws IOException {
      super(dir, id, CODEC_NAME, VERSION_CURRENT, bytesUsed, context);
      size = 0;
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
      final int size = hash.size();
      final long[] addresses = new long[size];
      final IndexOutput datOut = getOrCreateDataOut();
      int addr = 0;
      final BytesRef bytesRef = new BytesRef();
      for (int i = 0; i < size; i++) {
        hash.get(i, bytesRef);
        addresses[i] = addr;
        addr += writePrefixLength(datOut, bytesRef) + bytesRef.length;
        datOut.writeBytes(bytesRef.bytes, bytesRef.offset, bytesRef.length);
      }

      final IndexOutput idxOut = getOrCreateIndexOut();
      // write the max address to read directly on source load
      idxOut.writeLong(addr);
      writeIndex(idxOut, docCount, addresses[addresses.length-1], addresses, docToEntry);
    }
  }

  public static class VarDerefReader extends BytesReaderBase {
    private final long totalBytes;
    VarDerefReader(Directory dir, String id, int maxDoc, IOContext context) throws IOException {
      super(dir, id, CODEC_NAME, VERSION_START, true, context, Type.BYTES_VAR_DEREF);
      totalBytes = idxIn.readLong();
    }

    @Override
    public Source load() throws IOException {
      return new VarDerefSource(cloneData(), cloneIndex(), totalBytes);
    }
   
    @Override
    public Source getDirectSource()
        throws IOException {
      return new DirectVarDerefSource(cloneData(), cloneIndex(), type());
    }
  }
  
  final static class VarDerefSource extends BytesSourceBase {
    private final PackedInts.Reader addresses;

    public VarDerefSource(IndexInput datIn, IndexInput idxIn, long totalBytes)
        throws IOException {
      super(datIn, idxIn, new PagedBytes(PAGED_BYTES_BITS), totalBytes,
          Type.BYTES_VAR_DEREF);
      addresses = PackedInts.getReader(idxIn);
    }

    @Override
    public BytesRef getBytes(int docID, BytesRef bytesRef) {
      return data.fillSliceWithPrefix(bytesRef,
          addresses.get(docID));
    }
  }

  
  final static class DirectVarDerefSource extends DirectSource {
    private final PackedInts.Reader index;

    DirectVarDerefSource(IndexInput data, IndexInput index, Type type)
        throws IOException {
      super(data, type);
      this.index = PackedInts.getDirectReader(index);
    }
    
    @Override
    protected int position(int docID) throws IOException {
      data.seek(baseOffset + index.get(docID));
      final byte sizeByte = data.readByte();
      if ((sizeByte & 128) == 0) {
        // length is 1 byte
        return sizeByte;
      } else {
        return ((sizeByte & 0x7f) << 8) | ((data.readByte() & 0xff));
      }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2336.java