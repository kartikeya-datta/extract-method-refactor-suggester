error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1325.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1325.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1325.java
text:
```scala
a@@ssert nextPos - pos <= count: "nextPos=" + nextPos + " pos=" + pos + " count=" + count;

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

import java.io.IOException;
import java.io.Reader;

/** Acts like a forever growing char[] as you read
 *  characters into it from the provided reader, but
 *  internally it uses a circular buffer to only hold the
 *  characters that haven't been freed yet.  This is like a
 *  PushbackReader, except you don't have to specify
 *  up-front the max size of the buffer, but you do have to
 *  periodically call {@link #freeBefore}. */

public final class RollingCharBuffer {

  private Reader reader;

  private char[] buffer = new char[32];

  // Next array index to write to in buffer:
  private int nextWrite;

  // Next absolute position to read from reader:
  private int nextPos;

  // How many valid chars (wrapped) are in the buffer:
  private int count;

  // True if we hit EOF
  private boolean end;
    
  /** Clear array and switch to new reader. */
  public void reset(Reader reader) {
    this.reader = reader;
    nextPos = 0;
    nextWrite = 0;
    count = 0;
    end = false;
  }

  /* Absolute position read.  NOTE: pos must not jump
   * ahead by more than 1!  Ie, it's OK to read arbitarily
   * far back (just not prior to the last {@link
   * #freeBefore}), but NOT ok to read arbitrarily far
   * ahead.  Returns -1 if you hit EOF. */
  public int get(int pos) throws IOException {
    //System.out.println("    get pos=" + pos + " nextPos=" + nextPos + " count=" + count);
    if (pos == nextPos) {
      if (end) {
        return -1;
      }
      final int ch = reader.read();
      if (ch == -1) {
        end = true;
        return -1;
      }
      if (count == buffer.length) {
        // Grow
        final char[] newBuffer = new char[ArrayUtil.oversize(1+count, RamUsageEstimator.NUM_BYTES_CHAR)];
        //System.out.println(Thread.currentThread().getName() + ": cb grow " + newBuffer.length);
        System.arraycopy(buffer, nextWrite, newBuffer, 0, buffer.length - nextWrite);
        System.arraycopy(buffer, 0, newBuffer, buffer.length - nextWrite, nextWrite);
        nextWrite = buffer.length;
        buffer = newBuffer;
      }
      if (nextWrite == buffer.length) {
        nextWrite = 0;
      }
      buffer[nextWrite++] = (char) ch;
      count++;
      nextPos++;
      return ch;
    } else {
      // Cannot read from future (except by 1):
      assert pos < nextPos;

      // Cannot read from already freed past:
      assert nextPos - pos <= count;

      final int index = getIndex(pos);
      return buffer[index];
    }
  }

  // For assert:
  private boolean inBounds(int pos) {
    return pos >= 0 && pos < nextPos && pos >= nextPos - count;
  }

  private int getIndex(int pos) {
    int index = nextWrite - (nextPos - pos);
    if (index < 0) {
      // Wrap:
      index += buffer.length;
      assert index >= 0;
    }
    return index;
  }

  public char[] get(int posStart, int length) {
    assert length > 0;
    assert inBounds(posStart): "posStart=" + posStart + " length=" + length;
    //System.out.println("    buffer.get posStart=" + posStart + " len=" + length);
      
    final int startIndex = getIndex(posStart);
    final int endIndex = getIndex(posStart + length);
    //System.out.println("      startIndex=" + startIndex + " endIndex=" + endIndex);

    final char[] result = new char[length];
    if (endIndex >= startIndex && length < buffer.length) {
      System.arraycopy(buffer, startIndex, result, 0, endIndex-startIndex);
    } else {
      // Wrapped:
      final int part1 = buffer.length-startIndex;
      System.arraycopy(buffer, startIndex, result, 0, part1);
      System.arraycopy(buffer, 0, result, buffer.length-startIndex, length-part1);
    }
    return result;
  }

  /** Call this to notify us that no chars before this
   *  absolute position are needed anymore. */
  public void freeBefore(int pos) {
    assert pos >= 0;
    assert pos <= nextPos;
    final int newCount = nextPos - pos;
    assert newCount <= count: "newCount=" + newCount + " count=" + count;
    assert newCount <= buffer.length: "newCount=" + newCount + " buf.length=" + buffer.length;
    count = newCount;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1325.java