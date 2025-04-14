error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2507.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2507.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2507.java
text:
```scala
r@@eturn (alphabet.nextByte() & 0xff);

/*

   Derby - Class org.apache.derbyTesting.functionTests.util.streams.LoopingAlphabetStream

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to you under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package org.apache.derbyTesting.functionTests.util.streams;

import java.io.InputStream;
import java.io.IOException;

/**
 * A stream returning a cycle of the 26 lowercase letters of the modern Latin
 * alphabet.
 */
public class LoopingAlphabetStream
    extends InputStream {

    /**
     * Maximum size of buffer.
     * Balance between size and memory usage.
     */
    private static final int MAX_BUF_SIZE = 32*1024;
    private static final byte SPACE = (byte)' ';

    /** Length of the stream. */
    private final long length;
    private final int trailingBlanks; 
    /** Remaining bytes in the stream. */
    private long remainingBlanks;
    private long remainingNonBlanks;
    private byte[] buffer = new byte[0];
    private final ByteAlphabet alphabet;

    /**
     * Create a looping modern latin alphabet stream of the specified length.
     *
     * @param length the number of characters (and also the number of bytes)
     */
    public LoopingAlphabetStream(long length) {
        this(length, 0);
    }

    public LoopingAlphabetStream(long length, int trailingBlanks) {
        this.length = length;
        this.trailingBlanks = trailingBlanks;
        this.remainingNonBlanks = length - trailingBlanks;
        this.remainingBlanks = trailingBlanks;
        this.alphabet = ByteAlphabet.modernLatinLowercase();
        fillBuffer(alphabet.byteCount());
    }

    /**
     * Create a looping alphabet of the specified type and length.
     *
     * @param length the number of bytes in the stream
     * @param alphabet the alphabet to loop over
     */
    public LoopingAlphabetStream(long length, ByteAlphabet alphabet) {
        this(length, alphabet, 0);
    }

    public LoopingAlphabetStream(long length,
                                 ByteAlphabet alphabet,
                                 int trailingBlanks) {
        this.length = length;
        this.trailingBlanks = trailingBlanks;
        this.remainingNonBlanks = length - trailingBlanks;
        this.remainingBlanks = trailingBlanks;
        this.alphabet = alphabet;
        fillBuffer(alphabet.byteCount());
    }

    public int read() {
        if (remainingBlanks <= 0 && remainingNonBlanks <= 0) {
            return -1;
        }
        if (remainingNonBlanks <= 0) {
            remainingBlanks--;
            return SPACE;
        }
        remainingNonBlanks--;
        return alphabet.nextByte();
    }

    public int read(byte[] buf, int off, int length) {
        if (remainingBlanks <= 0 && remainingNonBlanks <= 0) {
            return -1;
        }
        // We can only read as many bytes as there are in the stream.
        int nonBlankLength = Math.min((int)remainingNonBlanks, length);
        fillBuffer(nonBlankLength);
        int read = 0;
        // Find position of next letter in the buffer.
        int bOff = alphabet.nextByteToRead(0);
        if (nonBlankLength <= (buffer.length - bOff)) {
            System.arraycopy(buffer, bOff, buf, off, nonBlankLength);
            remainingNonBlanks -= nonBlankLength;
            read = nonBlankLength;
            alphabet.nextByteToRead(nonBlankLength);
        } else {
            // Must read several times from the buffer.
            int toRead = 0;
            while (remainingNonBlanks > 0 && read < nonBlankLength) {
                bOff = alphabet.nextByteToRead(toRead);
                toRead = Math.min(buffer.length - bOff, nonBlankLength - read);
                System.arraycopy(buffer, bOff, buf, off + read, toRead);
                remainingNonBlanks -= toRead;
                read += toRead;
            }
            bOff = alphabet.nextByteToRead(toRead);
        }
        if (read < length && remainingBlanks > 0) {
            read += fillBlanks(buf, off + read, length - read);
        }
        return read;
    }

    /**
     * Reset the stream.
     */
    public void reset() {
        remainingNonBlanks = length - trailingBlanks;
        remainingBlanks = trailingBlanks;
        alphabet.reset();
    }

    /**
     * Return remaining bytes in the stream.
     */
    public int available() {
        return (int)(remainingNonBlanks + remainingBlanks);
    }

    /**
     * Fill internal buffer of bytes (from character sequence).
     *
     * @param bufSize the wanted size, might be ignored if too big
     */
    private void fillBuffer(int bufSize) {
        if (bufSize > MAX_BUF_SIZE) {
            bufSize = MAX_BUF_SIZE;
        }
        if (bufSize <= buffer.length) {
            return;
        }
        int curOff = alphabet.nextByteToRead(0);
        // First letter in buffer is always the first letter in the alphabet.
        alphabet.reset();
        buffer = new byte[bufSize];
        for (int i=0; i < bufSize; i++) {
            buffer[i] = alphabet.nextByte();
        }
        // Must reset internal state of the alphabet, as we have not yet
        // delivered any bytes.
        alphabet.reset();
        alphabet.nextByteToRead(curOff);
    }

    private int fillBlanks(byte[] buf, int off, int length) {
        int i=0;
        for (; i < length; i++) {
            if (remainingBlanks > 0) {
                buf[off+i] = SPACE;
                remainingBlanks--;
            } else {
                break;
            }
        }
        return i;
    }
} // End class LoopingAlphabetStream
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2507.java