error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3708.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3708.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[3,1]

error in qdox parser
file content:
```java
offset: 30
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3708.java
text:
```scala
final class BytesRefUtils {

p@@ackage org.apache.lucene.codecs.lucene40.values;

/**
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

import org.apache.lucene.util.BytesRef;

/**
 * Package private BytesRefUtils - can move this into the o.a.l.utils package if
 * needed.
 * 
 * @lucene.internal
 */
public final class BytesRefUtils {

  private BytesRefUtils() {
  }

  /**
   * Copies the given long value and encodes it as 8 byte Big-Endian.
   * <p>
   * NOTE: this method resets the offset to 0, length to 8 and resizes the
   * reference array if needed.
   */
  public static void copyLong(BytesRef ref, long value) {
    if (ref.bytes.length < 8) {
      ref.bytes = new byte[8];
    }
    copyInternal(ref, (int) (value >> 32), ref.offset = 0);
    copyInternal(ref, (int) value, 4);
    ref.length = 8;
  }

  /**
   * Copies the given int value and encodes it as 4 byte Big-Endian.
   * <p>
   * NOTE: this method resets the offset to 0, length to 4 and resizes the
   * reference array if needed.
   */
  public static void copyInt(BytesRef ref, int value) {
    if (ref.bytes.length < 4) {
      ref.bytes = new byte[4];
    }
    copyInternal(ref, value, ref.offset = 0);
    ref.length = 4;
  }

  /**
   * Copies the given short value and encodes it as a 2 byte Big-Endian.
   * <p>
   * NOTE: this method resets the offset to 0, length to 2 and resizes the
   * reference array if needed.
   */
  public static void copyShort(BytesRef ref, short value) {
    if (ref.bytes.length < 2) {
      ref.bytes = new byte[2];
    }
    ref.bytes[ref.offset] = (byte) (value >> 8);
    ref.bytes[ref.offset + 1] = (byte) (value);
    ref.length = 2;
  }

  private static void copyInternal(BytesRef ref, int value, int startOffset) {
    ref.bytes[startOffset] = (byte) (value >> 24);
    ref.bytes[startOffset + 1] = (byte) (value >> 16);
    ref.bytes[startOffset + 2] = (byte) (value >> 8);
    ref.bytes[startOffset + 3] = (byte) (value);
  }

  /**
   * Converts 2 consecutive bytes from the current offset to a short. Bytes are
   * interpreted as Big-Endian (most significant bit first)
   * <p>
   * NOTE: this method does <b>NOT</b> check the bounds of the referenced array.
   */
  public static short asShort(BytesRef b) {
    return (short) (0xFFFF & ((b.bytes[b.offset] & 0xFF) << 8) | (b.bytes[b.offset + 1] & 0xFF));
  }

  /**
   * Converts 4 consecutive bytes from the current offset to an int. Bytes are
   * interpreted as Big-Endian (most significant bit first)
   * <p>
   * NOTE: this method does <b>NOT</b> check the bounds of the referenced array.
   */
  public static int asInt(BytesRef b) {
    return asIntInternal(b, b.offset);
  }

  /**
   * Converts 8 consecutive bytes from the current offset to a long. Bytes are
   * interpreted as Big-Endian (most significant bit first)
   * <p>
   * NOTE: this method does <b>NOT</b> check the bounds of the referenced array.
   */
  public static long asLong(BytesRef b) {
    return (((long) asIntInternal(b, b.offset) << 32) | asIntInternal(b,
        b.offset + 4) & 0xFFFFFFFFL);
  }

  private static int asIntInternal(BytesRef b, int pos) {
    return ((b.bytes[pos++] & 0xFF) << 24) | ((b.bytes[pos++] & 0xFF) << 16)
 ((b.bytes[pos++] & 0xFF) << 8) | (b.bytes[pos] & 0xFF);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3708.java