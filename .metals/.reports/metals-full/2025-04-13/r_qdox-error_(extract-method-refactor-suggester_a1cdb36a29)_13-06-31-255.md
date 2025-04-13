error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14429.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14429.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14429.java
text:
```scala
protected I@@nternalLZWInputStream(InputStream inputStream) {

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
package org.apache.commons.compress.compressors.z._internal_;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.compressors.CompressorInputStream;

/**
 * <strong>This class is only public for technical reasons and is not
 * part of Commons Compress' published API - it may change or
 * disappear without warning.</strong>
 *
 * <p>Base-class for traditional Unix ".Z" compression and the
 * Unshrinking method of ZIP archive.</p>
 *
 * @NotThreadSafe
 * @since 1.7
 */
public abstract class InternalLZWInputStream extends CompressorInputStream {
    private final byte[] oneByte = new byte[1];

    protected final InputStream in;
    protected int clearCode = -1;
    protected int codeSize = 9;
    protected int bitsCached = 0;
    protected int bitsCachedSize = 0;
    protected int previousCode = -1;
    protected int tableSize = 0;
    protected int[] prefixes;
    protected byte[] characters;
    private byte[] outputStack;
    private int outputStackLocation;

    protected InternalLZWInputStream(InputStream inputStream) throws IOException {
        this.in = inputStream;
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
    
    @Override
    public int read() throws IOException {
        int ret = read(oneByte);
        if (ret < 0) {
            return ret;
        }
        return 0xff & oneByte[0];
    }
    
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int bytesRead = readFromStack(b, off, len);
        while (len - bytesRead > 0) {
            int result = decompressNextSymbol();
            if (result < 0) {
                if (bytesRead > 0) {
                    count(bytesRead);
                    return bytesRead;
                }
                return result;
            }
            bytesRead += readFromStack(b, off + bytesRead, len - bytesRead);
        }
        count(bytesRead);
        return bytesRead;
    }

    /**
     * Read the next code and expand it.
     */
    protected abstract int decompressNextSymbol() throws IOException;

    /**
     * Add a new entry to the dictionary.
     */
    protected abstract int addEntry(int previousCode, byte character)
        throws IOException;

    /**
     * Sets the clear code based on the code size.
     */
    protected void setClearCode(int codeSize) {
        clearCode = (1 << (codeSize - 1));
    }

    /**
     * Initializes the arrays based on the maximum code size.
     */
    protected void initializeTables(int maxCodeSize) {
        final int maxTableSize = 1 << maxCodeSize;
        prefixes = new int[maxTableSize];
        characters = new byte[maxTableSize];
        outputStack = new byte[maxTableSize];
        outputStackLocation = maxTableSize;
        final int max = 1 << 8;
        for (int i = 0; i < max; i++) {
            prefixes[i] = -1;
            characters[i] = (byte) i;
        }
    }

    /**
     * Reads the next code from the stream.
     */
    protected int readNextCode() throws IOException {
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
        return code;
    }
    
    /**
     * Adds a new entry if the maximum table size hasn't been exceeded
     * and returns the new index.
     */
    protected int addEntry(int previousCode, byte character, int maxTableSize) {
        if (tableSize < maxTableSize) {
            final int index = tableSize;
            prefixes[tableSize] = previousCode;
            characters[tableSize] = character;
            tableSize++;
            return index;
        }
        return -1;
    }

    /**
     * Add entry for repeat of previousCode we haven't added, yet.
     */
    protected int addRepeatOfPreviousCode() throws IOException {
        if (previousCode == -1) {
            // can't have a repeat for the very first code
            throw new IOException("The first code can't be a reference to its preceding code");
        }
        byte firstCharacter = 0;
        for (int last = previousCode; last >= 0; last = prefixes[last]) {
            firstCharacter = characters[last];
        }
        return addEntry(previousCode, firstCharacter);
    }

    /**
     * Expands the entry with index code to the output stack and may
     * create a new entry
     */
    protected int expandCodeToOutputStack(int code, boolean addedUnfinishedEntry)
        throws IOException {
        for (int entry = code; entry >= 0; entry = prefixes[entry]) {
            outputStack[--outputStackLocation] = characters[entry];
        }
        if (previousCode != -1 && !addedUnfinishedEntry) {
            addEntry(previousCode, outputStack[outputStackLocation]);
        }
        previousCode = code;
        return outputStackLocation;
    }

    private int readFromStack(byte[] b, int off, int len) {
        int remainingInStack = outputStack.length - outputStackLocation;
        if (remainingInStack > 0) {
            int maxLength = Math.min(remainingInStack, len);
            System.arraycopy(outputStack, outputStackLocation, b, off, maxLength);
            outputStackLocation += maxLength;
            return maxLength;
        }
        return 0;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14429.java