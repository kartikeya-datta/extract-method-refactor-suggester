error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5616.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5616.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5616.java
text:
```scala
final b@@yte[] nameBytes = entry.getName().getBytes(); // TODO is it correct to use the default charset here?

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
package org.apache.commons.compress.archivers.tar;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;

/**
 * The TarOutputStream writes a UNIX tar archive as an OutputStream.
 * Methods are provided to put entries, and then write their contents
 * by writing to this stream using write().
 * @NotThreadSafe
 */
public class TarArchiveOutputStream extends ArchiveOutputStream {
    /** Fail if a long file name is required in the archive. */
    public static final int LONGFILE_ERROR = 0;

    /** Long paths will be truncated in the archive. */
    public static final int LONGFILE_TRUNCATE = 1;

    /** GNU tar extensions are used to store long file names in the archive. */
    public static final int LONGFILE_GNU = 2;

    private long      currSize;
    private String    currName;
    private long      currBytes;
    private final byte[]    recordBuf;
    private int       assemLen;
    private final byte[]    assemBuf;
    protected final TarBuffer buffer;
    private int       longFileMode = LONGFILE_ERROR;

    private boolean closed = false;

    private final OutputStream out;

    /**
     * Constructor for TarInputStream.
     * @param os the output stream to use
     */
    public TarArchiveOutputStream(OutputStream os) {
        this(os, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
    }

    /**
     * Constructor for TarInputStream.
     * @param os the output stream to use
     * @param blockSize the block size to use
     */
    public TarArchiveOutputStream(OutputStream os, int blockSize) {
        this(os, blockSize, TarBuffer.DEFAULT_RCDSIZE);
    }

    /**
     * Constructor for TarInputStream.
     * @param os the output stream to use
     * @param blockSize the block size to use
     * @param recordSize the record size to use
     */
    public TarArchiveOutputStream(OutputStream os, int blockSize, int recordSize) {
        out = os;

        this.buffer = new TarBuffer(os, blockSize, recordSize);
        this.assemLen = 0;
        this.assemBuf = new byte[recordSize];
        this.recordBuf = new byte[recordSize];
    }

    /**
     * Set the long file mode.
     * This can be LONGFILE_ERROR(0), LONGFILE_TRUNCATE(1) or LONGFILE_GNU(2).
     * This specifies the treatment of long file names (names >= TarConstants.NAMELEN).
     * Default is LONGFILE_ERROR.
     * @param longFileMode the mode to use
     */
    public void setLongFileMode(int longFileMode) {
        this.longFileMode = longFileMode;
    }


    /**
     * Ends the TAR archive without closing the underlying OutputStream.
     * 
     * An archive consists of a series of file entries terminated by an
     * end-of-archive entry, which consists of two 512 blocks of zero bytes. 
     * POSIX.1 requires two EOF records, like some other implementations.
     * 
     * @throws IOException on error
     */
    public void finish() throws IOException {
        writeEOFRecord();
        writeEOFRecord();
    }

    /**
     * Ends the TAR archive and closes the underlying OutputStream.
     * This means that finish() is called followed by calling the
     * TarBuffer's close().
     * @throws IOException on error
     */
    public void close() throws IOException {
        if (!closed) {
            finish();
            buffer.close();
            out.close();
            closed = true;
        }
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
     * Put an entry on the output stream. This writes the entry's
     * header record and positions the output stream for writing
     * the contents of the entry. Once this method is called, the
     * stream is ready for calls to write() to write the entry's
     * contents. Once the contents are written, closeArchiveEntry()
     * <B>MUST</B> be called to ensure that all buffered data
     * is completely written to the output stream.
     *
     * @param archiveEntry The TarEntry to be written to the archive.
     * @throws IOException on error
     * @throws ClassCastException if archiveEntry is not an instance of TarArchiveEntry
     */
    public void putArchiveEntry(ArchiveEntry archiveEntry) throws IOException {
        TarArchiveEntry entry = (TarArchiveEntry) archiveEntry;
        if (entry.getName().length() >= TarConstants.NAMELEN) {

            if (longFileMode == LONGFILE_GNU) {
                // create a TarEntry for the LongLink, the contents
                // of which are the entry's name
                TarArchiveEntry longLinkEntry = new TarArchiveEntry(TarConstants.GNU_LONGLINK,
                                                                    TarConstants.LF_GNUTYPE_LONGNAME);

                final byte[] nameBytes = entry.getName().getBytes();
                longLinkEntry.setSize(nameBytes.length + 1); // +1 for NUL
                putArchiveEntry(longLinkEntry);
                write(nameBytes);
                write(0); // NUL terminator
                closeArchiveEntry();
            } else if (longFileMode != LONGFILE_TRUNCATE) {
                throw new RuntimeException("file name '" + entry.getName()
                                           + "' is too long ( > "
                                           + TarConstants.NAMELEN + " bytes)");
            }
        }

        entry.writeEntryHeader(recordBuf);
        buffer.writeRecord(recordBuf);

        currBytes = 0;

        if (entry.isDirectory()) {
            currSize = 0;
        } else {
            currSize = entry.getSize();
        }
        currName = entry.getName();
    }

    /**
     * Close an entry. This method MUST be called for all file
     * entries that contain data. The reason is that we must
     * buffer data written to the stream in order to satisfy
     * the buffer's record based writes. Thus, there may be
     * data fragments still being assembled that must be written
     * to the output stream before this entry is closed and the
     * next entry written.
     * @throws IOException on error
     */
    public void closeArchiveEntry() throws IOException {
        if (assemLen > 0) {
            for (int i = assemLen; i < assemBuf.length; ++i) {
                assemBuf[i] = 0;
            }

            buffer.writeRecord(assemBuf);

            currBytes += assemLen;
            assemLen = 0;
        }

        if (currBytes < currSize) {
            throw new IOException("entry '" + currName + "' closed at '"
                                  + currBytes
                                  + "' before the '" + currSize
                                  + "' bytes specified in the header were written");
        }
    }

    /**
     * Writes bytes to the current tar archive entry. This method
     * is aware of the current entry and will throw an exception if
     * you attempt to write bytes past the length specified for the
     * current entry. The method is also (painfully) aware of the
     * record buffering required by TarBuffer, and manages buffers
     * that are not a multiple of recordsize in length, including
     * assembling records from small buffers.
     *
     * @param wBuf The buffer to write to the archive.
     * @param wOffset The offset in the buffer from which to get bytes.
     * @param numToWrite The number of bytes to write.
     * @throws IOException on error
     */
    public void write(byte[] wBuf, int wOffset, int numToWrite) throws IOException {
        if ((currBytes + numToWrite) > currSize) {
            throw new IOException("request to write '" + numToWrite
                                  + "' bytes exceeds size in header of '"
                                  + currSize + "' bytes for entry '"
                                  + currName + "'");

            //
            // We have to deal with assembly!!!
            // The programmer can be writing little 32 byte chunks for all
            // we know, and we must assemble complete records for writing.
            // REVIEW Maybe this should be in TarBuffer? Could that help to
            // eliminate some of the buffer copying.
            //
        }

        if (assemLen > 0) {
            if ((assemLen + numToWrite) >= recordBuf.length) {
                int aLen = recordBuf.length - assemLen;

                System.arraycopy(assemBuf, 0, recordBuf, 0,
                                 assemLen);
                System.arraycopy(wBuf, wOffset, recordBuf,
                                 assemLen, aLen);
                buffer.writeRecord(recordBuf);

                currBytes += recordBuf.length;
                wOffset += aLen;
                numToWrite -= aLen;
                assemLen = 0;
            } else {
                System.arraycopy(wBuf, wOffset, assemBuf, assemLen,
                                 numToWrite);

                wOffset += numToWrite;
                assemLen += numToWrite;
                numToWrite = 0;
            }
        }

        //
        // When we get here we have EITHER:
        // o An empty "assemble" buffer.
        // o No bytes to write (numToWrite == 0)
        //
        while (numToWrite > 0) {
            if (numToWrite < recordBuf.length) {
                System.arraycopy(wBuf, wOffset, assemBuf, assemLen,
                                 numToWrite);

                assemLen += numToWrite;

                break;
            }

            buffer.writeRecord(wBuf, wOffset);

            int num = recordBuf.length;

            currBytes += num;
            numToWrite -= num;
            wOffset += num;
        }
    }

    /**
     * Write an EOF (end of archive) record to the tar archive.
     * An EOF record consists of a record of all zeros.
     */
    private void writeEOFRecord() throws IOException {
        for (int i = 0; i < recordBuf.length; ++i) {
            recordBuf[i] = 0;
        }

        buffer.writeRecord(recordBuf);
    }

    // used to be implemented via FilterOutputStream
    public void flush() throws IOException {
        out.flush();
    }

    public ArchiveEntry createArchiveEntry(File inputFile, String entryName)
            throws IOException {
        return new TarArchiveEntry(inputFile, entryName);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5616.java