error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/493.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/493.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/493.java
text:
```scala
b@@oolean isLastFilePart = no == 1;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.io.input;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * Reads lines in a file reversely (similar to a BufferedReader, but starting at
 * the last line). Useful for e.g. searching in log files.
 *
 * @since 2.2
 */
public class ReversedLinesFileReader implements Closeable {

    private final int blockSize;
    private final String encoding;

    private final RandomAccessFile randomAccessFile;

    private final long totalByteLength;
    private final long totalBlockCount;

    private final byte[][] newLineSequences;
    private final int avoidNewlineSplitBufferSize;
    private final int byteDecrement;

    private FilePart currentFilePart;

    private boolean trailingNewlineOfFileSkipped = false;

    /**
     * Creates a ReversedLinesFileReader with default block size of 4KB and the
     * platform's default encoding.
     *
     * @param file
     *            the file to be read
     * @throws IOException  if an I/O error occurs
     */
    public ReversedLinesFileReader(final File file) throws IOException {
        this(file, 4096, Charset.defaultCharset().toString());
    }

    /**
     * Creates a ReversedLinesFileReader with the given block size and encoding.
     *
     * @param file
     *            the file to be read
     * @param blockSize
     *            size of the internal buffer (for ideal performance this should
     *            match with the block size of the underlying file system).
     * @param encoding
     *            the encoding of the file
     * @throws IOException  if an I/O error occurs
     */
    public ReversedLinesFileReader(final File file, final int blockSize, final String encoding) throws IOException {
        this.blockSize = blockSize;
        this.encoding = encoding;

        randomAccessFile = new RandomAccessFile(file, "r");
        totalByteLength = randomAccessFile.length();
        int lastBlockLength = (int) (totalByteLength % blockSize);
        if (lastBlockLength > 0) {
            totalBlockCount = totalByteLength / blockSize + 1;
        } else {
            totalBlockCount = totalByteLength / blockSize;
            if (totalByteLength > 0) {
                lastBlockLength = blockSize;
            }
        }
        currentFilePart = new FilePart(totalBlockCount, lastBlockLength, null);

        // --- check & prepare encoding ---
        Charset charset = Charset.forName(encoding);
        CharsetEncoder charsetEncoder = charset.newEncoder();
        float maxBytesPerChar = charsetEncoder.maxBytesPerChar();
        if(maxBytesPerChar==1f) {
            // all one byte encodings are no problem
            byteDecrement = 1;
        } else if(charset == Charset.forName("UTF-8")) {
            // UTF-8 works fine out of the box, for multibyte sequences a second UTF-8 byte can never be a newline byte
            // http://en.wikipedia.org/wiki/UTF-8
            byteDecrement = 1;
        } else if(charset == Charset.forName("Shift_JIS")) {
            // Same as for UTF-8
            // http://www.herongyang.com/Unicode/JIS-Shift-JIS-Encoding.html
            byteDecrement = 1;
        } else if(charset == Charset.forName("UTF-16BE") || charset == Charset.forName("UTF-16LE")) {
            // UTF-16 new line sequences are not allowed as second tuple of four byte sequences,
            // however byte order has to be specified
            byteDecrement = 2;
        } else if(charset == Charset.forName("UTF-16")) {
            throw new UnsupportedEncodingException(
                    "For UTF-16, you need to specify the byte order (use UTF-16BE or UTF-16LE)");
        } else {
            throw new UnsupportedEncodingException(
                    "Encoding "+encoding+" is not supported yet (feel free to submit a patch)");
        }
        // NOTE: The new line sequences are matched in the order given, so it is important that \r\n is BEFORE \n
        newLineSequences = new byte[][] { "\r\n".getBytes(encoding), "\n".getBytes(encoding), "\r".getBytes(encoding) };

        avoidNewlineSplitBufferSize = newLineSequences[0].length;

    }

    /**
     * Returns the lines of the file from bottom to top.
     *
     * @return the next line or null if the start of the file is reached
     * @throws IOException  if an I/O error occurs
     */
    public String readLine() throws IOException {

        String line = currentFilePart.readLine();
        while (line == null) {
            currentFilePart = currentFilePart.rollOver();
            if (currentFilePart != null) {
                line = currentFilePart.readLine();
            } else {
                // no more fileparts: we're done, leave line set to null
                break;
            }
        }

        // aligned behaviour wiht BufferedReader that doesn't return a last, emtpy line
        if("".equals(line) && !trailingNewlineOfFileSkipped) {
            trailingNewlineOfFileSkipped = true;
            line = readLine();
        }

        return line;
    }

    /**
     * Closes underlying resources.
     *
     * @throws IOException  if an I/O error occurs
     */
    public void close() throws IOException {
        randomAccessFile.close();
    }

    private class FilePart {
        private final long no;

        private final byte[] data;

        private byte[] leftOver;

        private int currentLastBytePos;

        /**
         * ctor
         * @param no the part number
         * @param length its length
         * @param leftOverOfLastFilePart remainder
         * @throws IOException if there is a problem reading the file
         */
        private FilePart(final long no, final int length, final byte[] leftOverOfLastFilePart) throws IOException {
            this.no = no;
            int dataLength = length + (leftOverOfLastFilePart != null ? leftOverOfLastFilePart.length : 0);
            this.data = new byte[dataLength];
            final long off = (no - 1) * blockSize;

            // read data
            if (no > 0 /* file not empty */) {
                randomAccessFile.seek(off);
                final int countRead = randomAccessFile.read(data, 0, length);
                if (countRead != length) {
                    throw new IllegalStateException("Count of requested bytes and actually read bytes don't match");
                }
            }
            // copy left over part into data arr
            if (leftOverOfLastFilePart != null) {
                System.arraycopy(leftOverOfLastFilePart, 0, data, length, leftOverOfLastFilePart.length);
            }
            this.currentLastBytePos = data.length - 1;
            this.leftOver = null;
        }

        /**
         * Handles block rollover
         * 
         * @return the new FilePart or null
         * @throws IOException if there was a problem reading the file
         */
        private FilePart rollOver() throws IOException {

            if (currentLastBytePos > -1) {
                throw new IllegalStateException("Current currentLastCharPos unexpectedly positive... "
                        + "last readLine() should have returned something! currentLastCharPos=" + currentLastBytePos);
            }

            if (no > 1) {
                return new FilePart(no - 1, blockSize, leftOver);
            } else {
                // NO 1 was the last FilePart, we're finished
                if (leftOver != null) {
                    throw new IllegalStateException("Unexpected leftover of the last block: leftOverOfThisFilePart="
                            + new String(leftOver, encoding));
                }
                return null;
            }
        }

        /**
         * Reads a line.
         * 
         * @return the line or null
         * @throws IOException if there is an error reading from the file
         */
        private String readLine() throws IOException {

            String line = null;
            int newLineMatchByteCount;

            boolean isLastFilePart = (no == 1);

            int i = currentLastBytePos;
            while (i > -1) {

                if (!isLastFilePart && i < avoidNewlineSplitBufferSize) {
                    // avoidNewlineSplitBuffer: for all except the last file part we
                    // take a few bytes to the next file part to avoid splitting of newlines
                    createLeftOver();
                    break; // skip last few bytes and leave it to the next file part
                }

                // --- check for newline ---
                if ((newLineMatchByteCount = getNewLineMatchByteCount(data, i)) > 0 /* found newline */) {
                    final int lineStart = i + 1;
                    int lineLengthBytes = currentLastBytePos - lineStart + 1;

                    if (lineLengthBytes < 0) {
                        throw new IllegalStateException("Unexpected negative line length="+lineLengthBytes);
                    }
                    byte[] lineData = new byte[lineLengthBytes];
                    System.arraycopy(data, lineStart, lineData, 0, lineLengthBytes);

                    line = new String(lineData, encoding);

                    currentLastBytePos = i - newLineMatchByteCount;
                    break; // found line
                }

                // --- move cursor ---
                i -= byteDecrement;

                // --- end of file part handling ---
                if (i < 0) {
                    createLeftOver();
                    break; // end of file part
                }
            }

            // --- last file part handling ---
            if (isLastFilePart && leftOver != null) {
                // there will be no line break anymore, this is the first line of the file
                line = new String(leftOver, encoding);
                leftOver = null;
            }

            return line;
        }

        /**
         * Creates the buffer containing any left over bytes.
         */
        private void createLeftOver() {
            int lineLengthBytes = currentLastBytePos + 1;
            if (lineLengthBytes > 0) {
                // create left over for next block
                leftOver = new byte[lineLengthBytes];
                System.arraycopy(data, 0, leftOver, 0, lineLengthBytes);
            } else {
                leftOver = null;
            }
            currentLastBytePos = -1;
        }

        /**
         * Finds the new-line sequence and return its length.
         * 
         * @param data buffer to scan
         * @param i start offset in buffer
         * @return length of newline sequence or 0 if none found
         */
        private int getNewLineMatchByteCount(byte[] data, int i) {
            for (byte[] newLineSequence : newLineSequences) {
                boolean match = true;
                for (int j = newLineSequence.length - 1; j >= 0; j--) {
                    int k = i + j - (newLineSequence.length - 1);
                    match &= k >= 0 && data[k] == newLineSequence[j];
                }
                if (match) {
                    return newLineSequence.length;
                }
            }
            return 0;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/493.java