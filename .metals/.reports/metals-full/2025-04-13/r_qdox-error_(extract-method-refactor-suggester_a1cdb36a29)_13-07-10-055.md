error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2093.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2093.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2093.java
text:
```scala
i@@f (bytes.length > left || currentBlock==null) {

package org.apache.lucene.util;

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

import org.apache.lucene.store.IndexInput;

import java.util.List;
import java.util.ArrayList;
import java.io.Closeable;
import java.io.IOException;

/** Represents a logical byte[] as a series of pages.  You
 *  can write-once into the logical byte[] (append only),
 *  using copy, and then retrieve slices (BytesRef) into it
 *  using fill.
 *
 * <p>@lucene.internal</p>*/
public final class PagedBytes {
  private final List<byte[]> blocks = new ArrayList<byte[]>();
  private final List<Integer> blockEnd = new ArrayList<Integer>();
  private final int blockSize;
  private final int blockBits;
  private final int blockMask;
  private int upto;
  private byte[] currentBlock;

  private static final byte[] EMPTY_BYTES = new byte[0];

  public final static class Reader implements Closeable {
    private final byte[][] blocks;
    private final int[] blockEnds;
    private final int blockBits;
    private final int blockMask;
    private final int blockSize;
    private final CloseableThreadLocal<byte[]> threadBuffers = new CloseableThreadLocal<byte[]>();

    public Reader(PagedBytes pagedBytes) {
      blocks = new byte[pagedBytes.blocks.size()][];
      for(int i=0;i<blocks.length;i++) {
        blocks[i] = pagedBytes.blocks.get(i);
      }
      blockEnds = new int[blocks.length];
      for(int i=0;i< blockEnds.length;i++) {
        blockEnds[i] = pagedBytes.blockEnd.get(i);
      }
      blockBits = pagedBytes.blockBits;
      blockMask = pagedBytes.blockMask;
      blockSize = pagedBytes.blockSize;
    }

    /** Get a slice out of the byte array. */
    public BytesRef fill(BytesRef b, long start, int length) {
      assert length >= 0: "length=" + length;
      final int index = (int) (start >> blockBits);
      final int offset = (int) (start & blockMask);
      b.length = length;
      if (blockSize - offset >= length) {
        // Within block
        b.bytes = blocks[index];
        b.offset = offset;
      } else {
        // Split
        byte[] buffer = threadBuffers.get();
        if (buffer == null) {
          buffer = new byte[length];
          threadBuffers.set(buffer);
        } else if (buffer.length < length) {
          buffer = ArrayUtil.grow(buffer, length);
          threadBuffers.set(buffer);
        }
        b.bytes = buffer;
        b.offset = 0;
        System.arraycopy(blocks[index], offset, buffer, 0, blockSize-offset);
        System.arraycopy(blocks[1+index], 0, buffer, blockSize-offset, length-(blockSize-offset));
      }
      return b;
    }

    /** Reads length as 1 or 2 byte vInt prefix, starting @ start */
    public BytesRef fillUsingLengthPrefix(BytesRef b, long start) {
      final int index = (int) (start >> blockBits);
      final int offset = (int) (start & blockMask);
      final byte[] block = b.bytes = blocks[index];

      if ((block[offset] & 128) == 0) {
        b.length = block[offset];
        b.offset = offset+1;
      } else {
        b.length = ((block[offset] & 0x7f) << 8) | (block[1+offset] & 0xff);
        b.offset = offset+2;
        assert b.length > 0;
      }
      return b;
    }

    /** @lucene.internal  Reads length as 1 or 2 byte vInt prefix, starting @ start.  Returns the block number of the term. */
    public int fillUsingLengthPrefix2(BytesRef b, long start) {
      final int index = (int) (start >> blockBits);
      final int offset = (int) (start & blockMask);
      final byte[] block = b.bytes = blocks[index];

      if ((block[offset] & 128) == 0) {
        b.length = block[offset];
        b.offset = offset+1;
      } else {
        b.length = ((block[offset] & 0x7f) << 8) | (block[1+offset] & 0xff);
        b.offset = offset+2;
        assert b.length > 0;
      }
      return index;
    }

    /** @lucene.internal  Reads length as 1 or 2 byte vInt prefix, starting @ start. 
     * Returns the start offset of the next part, suitable as start parameter on next call
     * to sequentially read all BytesRefs. */
    public long fillUsingLengthPrefix3(BytesRef b, long start) {
      final int index = (int) (start >> blockBits);
      final int offset = (int) (start & blockMask);
      final byte[] block = b.bytes = blocks[index];

      if ((block[offset] & 128) == 0) {
        b.length = block[offset];
        b.offset = offset+1;
        start += 1L + b.length;
      } else {
        b.length = ((block[offset] & 0x7f) << 8) | (block[1+offset] & 0xff);
        b.offset = offset+2;
        start += 2L + b.length;
        assert b.length > 0;
      }
      return start;
    }

    /** @lucene.internal */
    public byte[][] getBlocks() {
      return blocks;
    }

    /** @lucene.internal */
    public int[] getBlockEnds() {
      return blockEnds;
    }

    public void close() {
      threadBuffers.close();
    }
  }

  /** 1<<blockBits must be bigger than biggest single
   *  BytesRef slice that will be pulled */
  public PagedBytes(int blockBits) {
    this.blockSize = 1 << blockBits;
    this.blockBits = blockBits;
    blockMask = blockSize-1;
    upto = blockSize;
  }

  /** Read this many bytes from in */
  public void copy(IndexInput in, long byteCount) throws IOException {
    while (byteCount > 0) {
      int left = blockSize - upto;
      if (left == 0) {
        if (currentBlock != null) {
          blocks.add(currentBlock);
          blockEnd.add(upto);
        }
        currentBlock = new byte[blockSize];
        upto = 0;
        left = blockSize;
      }
      if (left < byteCount) {
        in.readBytes(currentBlock, upto, left, false);
        upto = blockSize;
        byteCount -= left;
      } else {
        in.readBytes(currentBlock, upto, (int) byteCount, false);
        upto += byteCount;
        break;
      }
    }
  }

  /** Copy BytesRef in */
  public void copy(BytesRef bytes) throws IOException {
    int byteCount = bytes.length;
    int bytesUpto = bytes.offset;
    while (byteCount > 0) {
      int left = blockSize - upto;
      if (left == 0) {
        if (currentBlock != null) {
          blocks.add(currentBlock);
          blockEnd.add(upto);          
        }
        currentBlock = new byte[blockSize];
        upto = 0;
        left = blockSize;
      }
      if (left < byteCount) {
        System.arraycopy(bytes.bytes, bytesUpto, currentBlock, upto, left);
        upto = blockSize;
        byteCount -= left;
        bytesUpto += left;
      } else {
        System.arraycopy(bytes.bytes, bytesUpto, currentBlock, upto, byteCount);
        upto += byteCount;
        break;
      }
    }
  }

  /** Copy BytesRef in, setting BytesRef out to the result.
   * Do not use this if you will use freeze(true).
   * This only supports bytes.length <= blockSize */
  public void copy(BytesRef bytes, BytesRef out) throws IOException {
    int left = blockSize - upto;
    if (bytes.length > left) {
      if (currentBlock != null) {
        blocks.add(currentBlock);
        blockEnd.add(upto);
      }
      currentBlock = new byte[blockSize];
      upto = 0;
      left = blockSize;
      assert bytes.length <= blockSize;
      // TODO: we could also support variable block sizes
    }

    out.bytes = currentBlock;
    out.offset = upto;
    out.length = bytes.length;

    System.arraycopy(bytes.bytes, bytes.offset, currentBlock, upto, bytes.length);
    upto += bytes.length;
  }

  /** Commits final byte[], trimming it if necessary and if trim=true */
  public Reader freeze(boolean trim) {
    if (trim && upto < blockSize) {
      final byte[] newBlock = new byte[upto];
      System.arraycopy(currentBlock, 0, newBlock, 0, upto);
      currentBlock = newBlock;
    }
    if (currentBlock == null) {
      currentBlock = EMPTY_BYTES;
    }
    blocks.add(currentBlock);
    blockEnd.add(upto); 
    currentBlock = null;
    return new Reader(this);
  }

  public long getPointer() {
    if (currentBlock == null) {
      return 0;
    } else {
      return (blocks.size() * ((long) blockSize)) + upto;
    }
  }

  /** Copy bytes in, writing the length as a 1 or 2 byte
   *  vInt prefix. */
  public long copyUsingLengthPrefix(BytesRef bytes) throws IOException {

    if (upto + bytes.length + 2 > blockSize) {
      if (bytes.length + 2 > blockSize) {
        throw new IllegalArgumentException("block size " + blockSize + " is too small to store length " + bytes.length + " bytes");
      }
      if (currentBlock != null) {
        blocks.add(currentBlock);
        blockEnd.add(upto);        
      }
      currentBlock = new byte[blockSize];
      upto = 0;
    }

    final long pointer = getPointer();

    if (bytes.length < 128) {
      currentBlock[upto++] = (byte) bytes.length;
    } else {
      currentBlock[upto++] = (byte) (0x80 | (bytes.length >> 8));
      currentBlock[upto++] = (byte) (bytes.length & 0xff);
    }
    System.arraycopy(bytes.bytes, bytes.offset, currentBlock, upto, bytes.length);
    upto += bytes.length;

    return pointer;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2093.java