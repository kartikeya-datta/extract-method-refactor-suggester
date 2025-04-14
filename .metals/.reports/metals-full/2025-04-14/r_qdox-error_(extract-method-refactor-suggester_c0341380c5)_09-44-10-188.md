error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10130.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10130.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10130.java
text:
```scala
t@@hrow new IOException("The first code can't be a reference to its preceding code");

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.commons.compress.compressors.z;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.compressors.CompressorInputStream;

/**
 * Input stream that decompresses .Z files.
 * @NotThreadSafe
 * @since 1.7
 */
public class ZCompressorInputStream extends CompressorInputStream {
    private static final int MAGIC_1 = 0x1f;
    private static final int MAGIC_2 = 0x9d;
    private static final int BLOCK_MODE_MASK = 0x80;
    private static final int MAX_CODE_SIZE_MASK = 0x1f;
    private final InputStream in;
    private final boolean blockMode;
    private final int clearCode;
    private final int maxCodeSize;
    private int codeSize = 9;
    private int bitsCached = 0;
    private int bitsCachedSize = 0;
    private long totalCodesRead = 0;
    private int previousCode = -1;
    private int tableSize = 0;
    private final int[] prefixes;
    private final byte[] characters;
    private final byte[] outputStack;
    private int outputStackLocation;
    
    public ZCompressorInputStream(InputStream inputStream) throws IOException {
        this.in = inputStream;
        int firstByte = in.read();
        int secondByte = in.read();
        int thirdByte = in.read();
        if (firstByte != MAGIC_1 || secondByte != MAGIC_2 || thirdByte < 0) {
            throw new IOException("Input is not in .Z format");
        }
        blockMode = ((thirdByte & BLOCK_MODE_MASK) != 0);
        maxCodeSize = thirdByte & MAX_CODE_SIZE_MASK;
        if (blockMode) {
            clearCode = (1 << (codeSize - 1));
        } else {
            clearCode = -1; // unused
        }
        final int maxTableSize = 1 << maxCodeSize;
        prefixes = new int[maxTableSize];
        characters = new byte[maxTableSize];
        outputStack = new byte[maxTableSize];
        outputStackLocation = maxTableSize;
        for (int i = 0; i < (1 << 8); i++) {
            prefixes[i] = -1;
            characters[i] = (byte)i;
        }
        clearEntries();
    }
    
    public void close() throws IOException {
        in.close();
    }
    
    private void clearEntries() {
        tableSize = (1 << 8);
        if (blockMode) {
            tableSize++;
        }
    }

    private int readNextCode() throws IOException {
        while (bitsCachedSize < codeSize) {
            final int nextByte = in.read();
            if (nextByte < 0) {
                return nextByte;
            }
            bitsCached |= (nextByte << bitsCachedSize);
            bitsCachedSize += 8;
        }
        final int mask = (1 << codeSize) - 1;
        final int code = (bitsCached & mask);
        bitsCached >>>= codeSize;
        bitsCachedSize -= codeSize;
        ++totalCodesRead;
        return code;
    }
    
    private void reAlignReading() throws IOException {
        // "compress" works in multiples of 8 symbols, each codeBits bits long.
        // When codeBits changes, the remaining unused symbols in the current
        // group of 8 are still written out, in the old codeSize,
        // as garbage values (usually zeroes) that need to be skipped.
        long codeReadsToThrowAway = 8 - (totalCodesRead % 8);
        if (codeReadsToThrowAway == 8) {
            codeReadsToThrowAway = 0;
        }
        for (long i = 0; i < codeReadsToThrowAway; i++) {
            readNextCode();
        }
        bitsCached = 0;
        bitsCachedSize = 0;
    }
    
    private void addEntry(int previousCode, byte character) throws IOException {
        final int maxTableSize = 1 << codeSize;
        if (tableSize < maxTableSize) {
            prefixes[tableSize] = previousCode;
            characters[tableSize] = character;
            tableSize++;
        }
        if (tableSize == maxTableSize) {
            if (codeSize < maxCodeSize) {
                reAlignReading();
                codeSize++;
            }
        }
    }

    private int decompressNextSymbol() throws IOException {
        //
        //                   table entry    table entry
        //                  _____________   _____
        //    table entry  /             \ /     \
        //    ____________/               \       \
        //   /           / \             / \       \
        //  +---+---+---+---+---+---+---+---+---+---+
        //  | . | . | . | . | . | . | . | . | . | . |
        //  +---+---+---+---+---+---+---+---+---+---+
        //  |<--------->|<------------->|<----->|<->|
        //     symbol        symbol      symbol  symbol
        //
        final int code = readNextCode();
        if (code < 0) {
            return -1;
        } else if (blockMode && code == clearCode) {
            clearEntries();
            reAlignReading();
            codeSize = 9;
            previousCode = -1;
            return 0;
        } else {
            boolean addedUnfinishedEntry = false;
            if (code == tableSize) {
                // must be a repeat of the previous entry we haven't added yet
                if (previousCode == -1) {
                    // ... which isn't possible for the very first code
                    throw new IOException("The first code can't be a reference to code before itself");
                }
                byte firstCharacter = 0;
                for (int last = previousCode; last >= 0; last = prefixes[last]) {
                    firstCharacter = characters[last];
                }
                addEntry(previousCode, firstCharacter);
                addedUnfinishedEntry = true;
            } else if (code > tableSize) {
                throw new IOException(String.format("Invalid %d bit code 0x%x", codeSize, code));
            }
            for (int entry = code; entry >= 0; entry = prefixes[entry]) {
                outputStack[--outputStackLocation] = characters[entry];
            }
            if (previousCode != -1 && !addedUnfinishedEntry) {
                addEntry(previousCode, outputStack[outputStackLocation]);
            }
            previousCode = code;
            return outputStackLocation;
        }
    }
    
    public int read() throws IOException {
        byte[] b = new byte[1];
        int ret;
        while ((ret = read(b)) == 0) {
        }
        if (ret < 0) {
            return ret;
        }
        return 0xff & b[0];
    }
    
    public int read(byte[] b, int off, int len) throws IOException {
        int bytesRead = 0;
        int remainingInStack = outputStack.length - outputStackLocation;
        if (remainingInStack > 0) {
            int maxLength = Math.min(remainingInStack, len);
            System.arraycopy(outputStack, outputStackLocation, b, off, maxLength);
            outputStackLocation += maxLength;
            off += maxLength;
            len -= maxLength;
            bytesRead += maxLength;
        }
        while (len > 0) {
            int result = decompressNextSymbol();
            if (result < 0) {
                if (bytesRead > 0) {
                    count(bytesRead);
                    return bytesRead;
                } else {
                    return result;
                }
            }
            remainingInStack = outputStack.length - outputStackLocation;
            if (remainingInStack > 0) {
                int maxLength = Math.min(remainingInStack, len);
                System.arraycopy(outputStack, outputStackLocation, b, off, maxLength);
                outputStackLocation += maxLength;
                off += maxLength;
                len -= maxLength;
                bytesRead += maxLength;
            }
        }
        count(bytesRead);
        return bytesRead;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10130.java