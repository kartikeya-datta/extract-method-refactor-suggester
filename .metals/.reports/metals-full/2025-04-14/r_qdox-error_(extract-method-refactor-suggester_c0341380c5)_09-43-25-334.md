error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7757.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7757.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7757.java
text:
```scala
public synchronized v@@oid reset() {

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

/*
 * This package is based on the work done by Timothy Gerard Endres
 * (time@ice.com) to whom the Ant project is very grateful for his great code.
 */

package org.apache.commons.compress.archivers.tar;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.utils.ArchiveUtils;

/**
 * The TarInputStream reads a UNIX tar archive as an InputStream.
 * methods are provided to position at each successive entry in
 * the archive, and the read each entry as a normal input stream
 * using read().
 * @NotThreadSafe
 */
public class TarArchiveInputStream extends ArchiveInputStream {
    private static final int SMALL_BUFFER_SIZE = 256;
    private static final int BUFFER_SIZE = 8 * 1024;

    private boolean hasHitEOF;
    private long entrySize;
    private long entryOffset;
    private byte[] readBuf;
    protected final TarBuffer buffer;
    private TarArchiveEntry currEntry;

    /**
     * Constructor for TarInputStream.
     * @param is the input stream to use
     */
    public TarArchiveInputStream(InputStream is) {
        this(is, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
    }

    /**
     * Constructor for TarInputStream.
     * @param is the input stream to use
     * @param blockSize the block size to use
     */
    public TarArchiveInputStream(InputStream is, int blockSize) {
        this(is, blockSize, TarBuffer.DEFAULT_RCDSIZE);
    }

    /**
     * Constructor for TarInputStream.
     * @param is the input stream to use
     * @param blockSize the block size to use
     * @param recordSize the record size to use
     */
    public TarArchiveInputStream(InputStream is, int blockSize, int recordSize) {
        this.buffer = new TarBuffer(is, blockSize, recordSize);
        this.readBuf = null;
        this.hasHitEOF = false;
    }

    /**
     * Closes this stream. Calls the TarBuffer's close() method.
     * @throws IOException on error
     */
    public void close() throws IOException {
        buffer.close();
    }

    /**
     * Get the record size being used by this stream's TarBuffer.
     *
     * @return The TarBuffer record size.
     */
    public int getRecordSize() {
        return buffer.getRecordSize();
    }

    /**
     * Get the available data that can be read from the current
     * entry in the archive. This does not indicate how much data
     * is left in the entire archive, only in the current entry.
     * This value is determined from the entry's size header field
     * and the amount of data already read from the current entry.
     * Integer.MAX_VALUE is returen in case more than Integer.MAX_VALUE
     * bytes are left in the current entry in the archive.
     *
     * @return The number of available bytes for the current entry.
     * @throws IOException for signature
     */
    public int available() throws IOException {
        if (entrySize - entryOffset > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int) (entrySize - entryOffset);
    }

    /**
     * Skip bytes in the input buffer. This skips bytes in the
     * current entry's data, not the entire archive, and will
     * stop at the end of the current entry's data if the number
     * to skip extends beyond that point.
     *
     * @param numToSkip The number of bytes to skip.
     * @return the number actually skipped
     * @throws IOException on error
     */
    public long skip(long numToSkip) throws IOException {
        // REVIEW
        // This is horribly inefficient, but it ensures that we
        // properly skip over bytes via the TarBuffer...
        //
        byte[] skipBuf = new byte[BUFFER_SIZE];
        long skip = numToSkip;
        while (skip > 0) {
            int realSkip = (int) (skip > skipBuf.length ? skipBuf.length : skip);
            int numRead = read(skipBuf, 0, realSkip);
            if (numRead == -1) {
                break;
            }
            skip -= numRead;
        }
        return (numToSkip - skip);
    }

    /**
     * Since we do not support marking just yet, we do nothing.
     */
    public void reset() {
    }

    /**
     * Get the next entry in this tar archive. This will skip
     * over any remaining data in the current entry, if there
     * is one, and place the input stream at the header of the
     * next entry, and read the header and instantiate a new
     * TarEntry from the header bytes and return that entry.
     * If there are no more entries in the archive, null will
     * be returned to indicate that the end of the archive has
     * been reached.
     *
     * @return The next TarEntry in the archive, or null.
     * @throws IOException on error
     */
    public TarArchiveEntry getNextTarEntry() throws IOException {
        if (hasHitEOF) {
            return null;
        }

        if (currEntry != null) {
            long numToSkip = entrySize - entryOffset;

            while (numToSkip > 0) {
                long skipped = skip(numToSkip);
                if (skipped <= 0) {
                    throw new RuntimeException("failed to skip current tar entry");
                }
                numToSkip -= skipped;
            }

            readBuf = null;
        }

        byte[] headerBuf = buffer.readRecord();

        if (headerBuf == null) {
            hasHitEOF = true;
        } else if (buffer.isEOFRecord(headerBuf)) {
            hasHitEOF = true;
        }

        if (hasHitEOF) {
            currEntry = null;
        } else {
            currEntry = new TarArchiveEntry(headerBuf);
            entryOffset = 0;
            entrySize = currEntry.getSize();
        }

        if (currEntry != null && currEntry.isGNULongNameEntry()) {
            // read in the name
            StringBuffer longName = new StringBuffer();
            byte[] buf = new byte[SMALL_BUFFER_SIZE];
            int length = 0;
            while ((length = read(buf)) >= 0) {
                longName.append(new String(buf, 0, length));
            }
            getNextEntry();
            if (currEntry == null) {
                // Bugzilla: 40334
                // Malformed tar file - long entry name not followed by entry
                return null;
            }
            // remove trailing null terminator
            if (longName.length() > 0
                && longName.charAt(longName.length() - 1) == 0) {
                longName.deleteCharAt(longName.length() - 1);
            }
            currEntry.setName(longName.toString());
        }

        return currEntry;
    }

    public ArchiveEntry getNextEntry() throws IOException {
        return getNextTarEntry();
    }

    /**
     * Reads bytes from the current tar archive entry.
     *
     * This method is aware of the boundaries of the current
     * entry in the archive and will deal with them as if they
     * were this stream's start and EOF.
     *
     * @param buf The buffer into which to place bytes read.
     * @param offset The offset at which to place bytes read.
     * @param numToRead The number of bytes to read.
     * @return The number of bytes read, or -1 at EOF.
     * @throws IOException on error
     */
    public int read(byte[] buf, int offset, int numToRead) throws IOException {
        int totalRead = 0;

        if (entryOffset >= entrySize) {
            return -1;
        }

        if ((numToRead + entryOffset) > entrySize) {
            numToRead = (int) (entrySize - entryOffset);
        }

        if (readBuf != null) {
            int sz = (numToRead > readBuf.length) ? readBuf.length
                : numToRead;

            System.arraycopy(readBuf, 0, buf, offset, sz);

            if (sz >= readBuf.length) {
                readBuf = null;
            } else {
                int newLen = readBuf.length - sz;
                byte[] newBuf = new byte[newLen];

                System.arraycopy(readBuf, sz, newBuf, 0, newLen);

                readBuf = newBuf;
            }

            totalRead += sz;
            numToRead -= sz;
            offset += sz;
        }

        while (numToRead > 0) {
            byte[] rec = buffer.readRecord();

            if (rec == null) {
                // Unexpected EOF!
                throw new IOException("unexpected EOF with " + numToRead
                                      + " bytes unread. Occured at byte: " + getBytesRead());
            }
            count(rec.length);
            int sz = numToRead;
            int recLen = rec.length;

            if (recLen > sz) {
                System.arraycopy(rec, 0, buf, offset, sz);

                readBuf = new byte[recLen - sz];

                System.arraycopy(rec, sz, readBuf, 0, recLen - sz);
            } else {
                sz = recLen;

                System.arraycopy(rec, 0, buf, offset, recLen);
            }

            totalRead += sz;
            numToRead -= sz;
            offset += sz;
        }

        entryOffset += totalRead;

        return totalRead;
    }

    protected final TarArchiveEntry getCurrentEntry() {
        return currEntry;
    }

    protected final void setCurrentEntry(TarArchiveEntry e) {
        currEntry = e;
    }

    protected final boolean isAtEOF() {
        return hasHitEOF;
    }

    protected final void setAtEOF(boolean b) {
        hasHitEOF = b;
    }

    // ArchiveInputStream

    public static boolean matches(byte[] signature, int length) {
        if (length < TarConstants.VERSION_OFFSET+TarConstants.VERSIONLEN) {
            return false;
        }

        if (ArchiveUtils.matchAsciiBuffer(TarConstants.MAGIC_POSIX, 
                signature, TarConstants.MAGIC_OFFSET, TarConstants.MAGICLEN)
            &&
            ArchiveUtils.matchAsciiBuffer(TarConstants.VERSION_POSIX, 
                signature, TarConstants.VERSION_OFFSET, TarConstants.VERSIONLEN)
                ){
            return true;
        }
        if (ArchiveUtils.matchAsciiBuffer(TarConstants.MAGIC_GNU, 
                signature, TarConstants.MAGIC_OFFSET, TarConstants.MAGICLEN)
            &&
            (
             ArchiveUtils.matchAsciiBuffer(TarConstants.VERSION_GNU_SPACE, 
                signature, TarConstants.VERSION_OFFSET, TarConstants.VERSIONLEN)

            ArchiveUtils.matchAsciiBuffer(TarConstants.VERSION_GNU_ZERO, 
                signature, TarConstants.VERSION_OFFSET, TarConstants.VERSIONLEN)
            )
                ){
            return true;
        }
        return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7757.java