error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/271.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/271.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[3,1]

error in qdox parser
file content:
```java
offset: 69
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/271.java
text:
```scala
final class BulkOperationPackedSingleBlock extends BulkOperation {

p@@ackage org.apache.lucene.util.packed;

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

/**
 * Non-specialized {@link BulkOperation} for {@link PackedInts.Format#PACKED_SINGLE_BLOCK}.
 */
class BulkOperationPackedSingleBlock extends BulkOperation {

  private static final int BLOCK_COUNT = 1;

  private final int bitsPerValue;
  private final int valueCount;
  private final long mask;

  public BulkOperationPackedSingleBlock(int bitsPerValue) {
    this.bitsPerValue = bitsPerValue;
    this.valueCount = 64 / bitsPerValue;
    this.mask = (1L << bitsPerValue) - 1;
  }

  @Override
  public final int blockCount() {
    return BLOCK_COUNT;
  }

  @Override
  public int valueCount() {
    return valueCount;
  }

  private static long readLong(byte[] blocks, int blocksOffset) {
    return (blocks[blocksOffset++] & 0xFFL) << 56
 (blocks[blocksOffset++] & 0xFFL) << 48
 (blocks[blocksOffset++] & 0xFFL) << 40
 (blocks[blocksOffset++] & 0xFFL) << 32
 (blocks[blocksOffset++] & 0xFFL) << 24
 (blocks[blocksOffset++] & 0xFFL) << 16
 (blocks[blocksOffset++] & 0xFFL) << 8
 blocks[blocksOffset++] & 0xFFL;
  }

  private int decode(long block, long[] values, int valuesOffset) {
    values[valuesOffset++] = block & mask;
    for (int j = 1; j < valueCount; ++j) {
      block >>>= bitsPerValue;
      values[valuesOffset++] = block & mask;
    }
    return valuesOffset;
  }

  private int decode(long block, int[] values, int valuesOffset) {
    values[valuesOffset++] = (int) (block & mask);
    for (int j = 1; j < valueCount; ++j) {
      block >>>= bitsPerValue;
      values[valuesOffset++] = (int) (block & mask);
    }
    return valuesOffset;
  }

  private long encode(long[] values, int valuesOffset) {
    long block = values[valuesOffset++];
    for (int j = 1; j < valueCount; ++j) {
      block |= values[valuesOffset++] << (j * bitsPerValue);
    }
    return block;
  }

  private long encode(int[] values, int valuesOffset) {
    long block = values[valuesOffset++] & 0xFFFFFFFFL;
    for (int j = 1; j < valueCount; ++j) {
      block |= (values[valuesOffset++] & 0xFFFFFFFFL) << (j * bitsPerValue);
    }
    return block;
  }

  @Override
  public void decode(long[] blocks, int blocksOffset, long[] values,
      int valuesOffset, int iterations) {
    for (int i = 0; i < iterations; ++i) {
      final long block = blocks[blocksOffset++];
      valuesOffset = decode(block, values, valuesOffset);
    }
  }

  @Override
  public void decode(byte[] blocks, int blocksOffset, long[] values,
      int valuesOffset, int iterations) {
    for (int i = 0; i < iterations; ++i) {
      final long block = readLong(blocks, blocksOffset);
      blocksOffset += 8;
      valuesOffset = decode(block, values, valuesOffset);
    }
  }

  @Override
  public void decode(long[] blocks, int blocksOffset, int[] values,
      int valuesOffset, int iterations) {
    if (bitsPerValue > 32) {
      throw new UnsupportedOperationException("Cannot decode " + bitsPerValue + "-bits values into an int[]");
    }
    for (int i = 0; i < iterations; ++i) {
      final long block = blocks[blocksOffset++];
      valuesOffset = decode(block, values, valuesOffset);
    }
  }

  @Override
  public void decode(byte[] blocks, int blocksOffset, int[] values,
      int valuesOffset, int iterations) {
    if (bitsPerValue > 32) {
      throw new UnsupportedOperationException("Cannot decode " + bitsPerValue + "-bits values into an int[]");
    }
    for (int i = 0; i < iterations; ++i) {
      final long block = readLong(blocks, blocksOffset);
      blocksOffset += 8;
      valuesOffset = decode(block, values, valuesOffset);
    }
  }

  @Override
  public void encode(long[] values, int valuesOffset, long[] blocks,
      int blocksOffset, int iterations) {
    for (int i = 0; i < iterations; ++i) {
      blocks[blocksOffset++] = encode(values, valuesOffset);
      valuesOffset += valueCount;
    }
  }

  @Override
  public void encode(int[] values, int valuesOffset, long[] blocks,
      int blocksOffset, int iterations) {
    for (int i = 0; i < iterations; ++i) {
      blocks[blocksOffset++] = encode(values, valuesOffset);
      valuesOffset += valueCount;
    }
  }

  @Override
  public void encode(long[] values, int valuesOffset, byte[] blocks, int blocksOffset, int iterations) {
    for (int i = 0; i < iterations; ++i) {
      final long block = encode(values, valuesOffset);
      valuesOffset += valueCount;
      blocksOffset = writeLong(block, blocks, blocksOffset);
    }
  }

  @Override
  public void encode(int[] values, int valuesOffset, byte[] blocks,
      int blocksOffset, int iterations) {
    for (int i = 0; i < iterations; ++i) {
      final long block = encode(values, valuesOffset);
      valuesOffset += valueCount;
      blocksOffset = writeLong(block, blocks, blocksOffset);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/271.java