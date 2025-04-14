error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3717.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3717.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3717.java
text:
```scala
b@@ufferStart = (long) BUFFER_SIZE * (long) currentBufferIndex;

package org.apache.lucene.store;

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

/**
 * A memory-resident {@link IndexOutput} implementation.
 * 
 * @version $Id$
 */

public class RAMOutputStream extends IndexOutput {
  static final int BUFFER_SIZE = 1024;

  private RAMFile file;

  private byte[] currentBuffer;
  private int currentBufferIndex;
  
  private int bufferPosition;
  private long bufferStart;
  private int bufferLength;

  /** Construct an empty output buffer. */
  public RAMOutputStream() {
    this(new RAMFile());
  }

  RAMOutputStream(RAMFile f) {
    file = f;

    // make sure that we switch to the
    // first needed buffer lazily
    currentBufferIndex = -1;
    currentBuffer = null;
  }

  /** Copy the current contents of this buffer to the named output. */
  public void writeTo(IndexOutput out) throws IOException {
    flush();
    final long end = file.length;
    long pos = 0;
    int buffer = 0;
    while (pos < end) {
      int length = BUFFER_SIZE;
      long nextPos = pos + length;
      if (nextPos > end) {                        // at the last buffer
        length = (int)(end - pos);
      }
      out.writeBytes((byte[])file.buffers.get(buffer++), length);
      pos = nextPos;
    }
  }

  /** Resets this to an empty buffer. */
  public void reset() {
    try {
      seek(0);
    } catch (IOException e) {                     // should never happen
      throw new RuntimeException(e.toString());
    }

    file.setLength(0);
  }

  public void close() throws IOException {
    flush();
  }

  public void seek(long pos) throws IOException {
    // set the file length in case we seek back
    // and flush() has not been called yet
    setFileLength();
    if (pos < bufferStart || pos >= bufferStart + bufferLength) {
      currentBufferIndex = (int) (pos / BUFFER_SIZE);
      switchCurrentBuffer();
    }

    bufferPosition = (int) (pos % BUFFER_SIZE);
  }

  public long length() {
    return file.length;
  }

  public void writeByte(byte b) throws IOException {
    if (bufferPosition == bufferLength) {
      currentBufferIndex++;
      switchCurrentBuffer();
    }
    currentBuffer[bufferPosition++] = b;
  }

  public void writeBytes(byte[] b, int offset, int len) throws IOException {
    while (len > 0) {
      if (bufferPosition ==  bufferLength) {
        currentBufferIndex++;
        switchCurrentBuffer();
      }

      int remainInBuffer = currentBuffer.length - bufferPosition;
      int bytesToCopy = len < remainInBuffer ? len : remainInBuffer;
      System.arraycopy(b, offset, currentBuffer, bufferPosition, bytesToCopy);
      offset += bytesToCopy;
      len -= bytesToCopy;
      bufferPosition += bytesToCopy;
    }
  }

  private final void switchCurrentBuffer() throws IOException {
    if (currentBufferIndex == file.buffers.size()) {
      currentBuffer = file.addBuffer(BUFFER_SIZE);
    } else {
      currentBuffer = (byte[]) file.buffers.get(currentBufferIndex);
    }
    bufferPosition = 0;
    bufferStart = BUFFER_SIZE * currentBufferIndex;
    bufferLength = currentBuffer.length;
  }

  private void setFileLength() {
    long pointer = bufferStart + bufferPosition;
    if (pointer > file.length) {
      file.setLength(pointer);
    }
  }

  public void flush() throws IOException {
    file.setLastModified(System.currentTimeMillis());
    setFileLength();
  }

  public long getFilePointer() {
    return currentBufferIndex < 0 ? 0 : bufferStart + bufferPosition;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3717.java