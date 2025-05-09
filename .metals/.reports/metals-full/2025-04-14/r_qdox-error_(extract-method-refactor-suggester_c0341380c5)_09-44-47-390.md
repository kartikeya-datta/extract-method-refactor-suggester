error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14904.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14904.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14904.java
text:
```scala
i@@n.close();

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
package org.apache.commons.compress.archivers.cpio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;

/**
 * CPIOArchiveInputStream is a stream for reading cpio streams. All formats of
 * cpio are supported (old ascii, old binary, new portable format and the new
 * portable format with crc).
 * <p/>
 * <p/>
 * The stream can be read by extracting a cpio entry (containing all
 * informations about a entry) and afterwards reading from the stream the file
 * specified by the entry.
 * <p/>
 * <code><pre>
 * CPIOArchiveInputStream cpioIn = new CPIOArchiveInputStream(
 *         new FileInputStream(new File(&quot;test.cpio&quot;)));
 * CPIOArchiveEntry cpioEntry;
 * <p/>
 * while ((cpioEntry = cpioIn.getNextEntry()) != null) {
 *     System.out.println(cpioEntry.getName());
 *     int tmp;
 *     StringBuffer buf = new StringBuffer();
 *     while ((tmp = cpIn.read()) != -1) {
 *         buf.append((char) tmp);
 *     }
 *     System.out.println(buf.toString());
 * }
 * cpioIn.close();
 * </pre></code>
 * <p/>
 * Note: This implementation should be compatible to cpio 2.5
 * 
 * This class uses mutable fields and is not considered to be threadsafe.
 * 
 * Based on code from the jRPM project (jrpm.sourceforge.net)
 */

public class CpioArchiveInputStream extends ArchiveInputStream implements
        CpioConstants {

    private boolean closed = false;

    private CpioArchiveEntry entry;

    private long entryBytesRead = 0;

    private boolean entryEOF = false;

    private final byte[] singleByteBuf = new byte[1];

    private final byte tmpbuf[] = new byte[4096];

    private long crc = 0;

    private final InputStream in;

    /**
     * Construct the cpio input stream
     * 
     * @param in
     *            The cpio stream
     */
    public CpioArchiveInputStream(final InputStream in) {
        this.in = in;
    }

    /**
     * Returns 0 after EOF has reached for the current entry data, otherwise
     * always return 1.
     * <p/>
     * Programs should not count on this method to return the actual number of
     * bytes that could be read without blocking.
     * 
     * @return 1 before EOF and 0 after EOF has reached for current entry.
     * @throws IOException
     *             if an I/O error has occurred or if a CPIO file error has
     *             occurred
     */
    public int available() throws IOException {
        ensureOpen();
        if (this.entryEOF) {
            return 0;
        }
        return 1;
    }

    /**
     * Closes the CPIO input stream.
     * 
     * @throws IOException
     *             if an I/O error has occurred
     */
    public void close() throws IOException {
        if (!this.closed) {
            super.close();
            this.closed = true;
        }
    }

    /**
     * Closes the current CPIO entry and positions the stream for reading the
     * next entry.
     * 
     * @throws IOException
     *             if an I/O error has occurred or if a CPIO file error has
     *             occurred
     */
    public void closeEntry() throws IOException {
        ensureOpen();
        while (read(this.tmpbuf, 0, this.tmpbuf.length) != -1) {
            // do nothing
        }

        this.entryEOF = true;
    }

    /**
     * Check to make sure that this stream has not been closed
     * 
     * @throws IOException
     *             if the stream is already closed
     */
    private void ensureOpen() throws IOException {
        if (this.closed) {
            throw new IOException("Stream closed");
        }
    }

    /**
     * Reads the next CPIO file entry and positions stream at the beginning of
     * the entry data.
     * 
     * @return the CPIOArchiveEntry just read
     * @throws IOException
     *             if an I/O error has occurred or if a CPIO file error has
     *             occurred
     */
    public CpioArchiveEntry getNextCPIOEntry() throws IOException {
        ensureOpen();
        if (this.entry != null) {
            closeEntry();
        }
        byte magic[] = new byte[2];
        readFully(magic, 0, magic.length);
        if (CpioUtil.byteArray2long(magic, false) == MAGIC_OLD_BINARY) {
            this.entry = readOldBinaryEntry(false);
        } else if (CpioUtil.byteArray2long(magic, true) == MAGIC_OLD_BINARY) {
            this.entry = readOldBinaryEntry(true);
        } else {
            byte more_magic[] = new byte[4];
            readFully(more_magic, 0, more_magic.length);
            byte tmp[] = new byte[6];
            System.arraycopy(magic, 0, tmp, 0, magic.length);
            System.arraycopy(more_magic, 0, tmp, magic.length,
                    more_magic.length);
            String magicString = new String(tmp);
            if (magicString.equals(MAGIC_NEW)) {
                this.entry = readNewEntry(false);
            } else if (magicString.equals(MAGIC_NEW_CRC)) {
                this.entry = readNewEntry(true);
            } else if (magicString.equals(MAGIC_OLD_ASCII)) {
                this.entry = readOldAsciiEntry();
            } else {
                throw new IOException("Unknown magic [" + magicString + "]");
            }
        }

        this.entryBytesRead = 0;
        this.entryEOF = false;
        this.crc = 0;

        if (this.entry.getName().equals(CPIO_TRAILER)) {
            this.entryEOF = true;
            return null;
        }
        return this.entry;
    }

    private void skip(int bytes) throws IOException{
        final byte[] buff = new byte[4]; // Cannot be more than 3 bytes
        if (bytes > 0) {
            readFully(buff, 0, bytes);
        }
    }

    /**
     * Reads a byte of data. This method will block until enough input is
     * available.
     * 
     * @return the byte read, or -1 if end of input is reached
     * @throws IOException
     *             if an I/O error has occurred or if a CPIO file error has
     *             occurred
     */
    public int read() throws IOException {
        return read(this.singleByteBuf, 0, 1) == -1 ? -1
                : this.singleByteBuf[0] & 0xff;
    }

    /**
     * Reads from the current CPIO entry into an array of bytes. Blocks until
     * some input is available.
     * 
     * @param b
     *            the buffer into which the data is read
     * @param off
     *            the start offset of the data
     * @param len
     *            the maximum number of bytes read
     * @return the actual number of bytes read, or -1 if the end of the entry is
     *         reached
     * @throws IOException
     *             if an I/O error has occurred or if a CPIO file error has
     *             occurred
     */
    public int read(final byte[] b, final int off, final int len)
            throws IOException {
        ensureOpen();
        if (off < 0 || len < 0 || off > b.length - len) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        if (this.entry == null || this.entryEOF) {
            return -1;
        }
        if (this.entryBytesRead == this.entry.getSize()) {
            skip(entry.getDataPadCount());
            this.entryEOF = true;
            if (this.entry.getFormat() == FORMAT_NEW_CRC) {
                if (this.crc != this.entry.getChksum()) {
                    throw new IOException("CRC Error");
                }
            }
            return -1; // EOF for this entry
        }
        int tmplength = (int) Math.min(len, this.entry.getSize()
                - this.entryBytesRead);
        if (tmplength < 0) {
            return -1;
        }

        int tmpread = this.in.read(b, off, tmplength);
        // TODO - what about EOF or short reads?
        if (this.entry.getFormat() == FORMAT_NEW_CRC) {
            for (int pos = 0; pos < tmpread; pos++) {
                this.crc += b[pos] & 0xFF;
            }
        }
        this.entryBytesRead += tmpread;

        return tmpread;
    }

    private final int readFully(final byte[] b, final int off, final int len)
            throws IOException {
        if (len < 0) {
            throw new IndexOutOfBoundsException();
        }
        int n = 0;
        while (n < len) {
            int count = this.in.read(b, off + n, len - n);
            if (count < 0) {
                throw new EOFException();
            }
            n += count;
        }
        return n;
    }

    private long readBinaryLong(final int length, final boolean swapHalfWord)
            throws IOException {
        byte tmp[] = new byte[length];
        readFully(tmp, 0, tmp.length);
        return CpioUtil.byteArray2long(tmp, swapHalfWord);
    }

    private long readAsciiLong(final int length, final int radix)
            throws IOException {
        byte tmpBuffer[] = new byte[length];
        readFully(tmpBuffer, 0, tmpBuffer.length);
        return Long.parseLong(new String(tmpBuffer), radix);
    }

    private CpioArchiveEntry readNewEntry(final boolean hasCrc)
            throws IOException {
        CpioArchiveEntry ret;
        if (hasCrc) {
            ret = new CpioArchiveEntry(FORMAT_NEW_CRC);
        } else {
            ret = new CpioArchiveEntry(FORMAT_NEW);
        }

        ret.setInode(readAsciiLong(8, 16));
        long mode = readAsciiLong(8, 16);
        if (mode != 0){ // mode is initialised to 0
            ret.setMode(mode);
        }
        ret.setUID(readAsciiLong(8, 16));
        ret.setGID(readAsciiLong(8, 16));
        ret.setNumberOfLinks(readAsciiLong(8, 16));
        ret.setTime(readAsciiLong(8, 16));
        ret.setSize(readAsciiLong(8, 16));
        ret.setDeviceMaj(readAsciiLong(8, 16));
        ret.setDeviceMin(readAsciiLong(8, 16));
        ret.setRemoteDeviceMaj(readAsciiLong(8, 16));
        ret.setRemoteDeviceMin(readAsciiLong(8, 16));
        long namesize = readAsciiLong(8, 16);
        ret.setChksum(readAsciiLong(8, 16));
        String name = readCString((int) namesize);
        ret.setName(name);
        if (mode == 0 && !name.equals(CPIO_TRAILER)){
            // TODO - change this to throw
            new IOException("Mode 0 only allowed in the trailer. Found: "+name).printStackTrace();
        }
        skip(ret.getHeaderPadCount());

        return ret;
    }

    private CpioArchiveEntry readOldAsciiEntry() throws IOException {
        CpioArchiveEntry ret = new CpioArchiveEntry(FORMAT_OLD_ASCII);

        ret.setDevice(readAsciiLong(6, 8));
        ret.setInode(readAsciiLong(6, 8));
        final long mode = readAsciiLong(6, 8);
        if (mode != 0) {
            ret.setMode(mode);
        }
        ret.setUID(readAsciiLong(6, 8));
        ret.setGID(readAsciiLong(6, 8));
        ret.setNumberOfLinks(readAsciiLong(6, 8));
        ret.setRemoteDevice(readAsciiLong(6, 8));
        ret.setTime(readAsciiLong(11, 8));
        long namesize = readAsciiLong(6, 8);
        ret.setSize(readAsciiLong(11, 8));
        final String name = readCString((int) namesize);
        ret.setName(name);
        if (mode == 0 && !name.equals(CPIO_TRAILER)){
            // TODO - change this to throw
            new IOException("Mode 0 only allowed in the trailer. Found: "+name).printStackTrace();
        }

        return ret;
    }

    private CpioArchiveEntry readOldBinaryEntry(final boolean swapHalfWord)
            throws IOException {
        CpioArchiveEntry ret = new CpioArchiveEntry(FORMAT_OLD_BINARY);

        ret.setDevice(readBinaryLong(2, swapHalfWord));
        ret.setInode(readBinaryLong(2, swapHalfWord));
        final long mode = readBinaryLong(2, swapHalfWord);
        if (mode != 0){
            ret.setMode(mode);            
        }
        ret.setUID(readBinaryLong(2, swapHalfWord));
        ret.setGID(readBinaryLong(2, swapHalfWord));
        ret.setNumberOfLinks(readBinaryLong(2, swapHalfWord));
        ret.setRemoteDevice(readBinaryLong(2, swapHalfWord));
        ret.setTime(readBinaryLong(4, swapHalfWord));
        long namesize = readBinaryLong(2, swapHalfWord);
        ret.setSize(readBinaryLong(4, swapHalfWord));
        final String name = readCString((int) namesize);
        ret.setName(name);
        if (mode == 0 && !name.equals(CPIO_TRAILER)){
            // TODO - change this to throw
            new IOException("Mode 0 only allowed in the trailer. Found: "+name).printStackTrace();
        }
        skip(ret.getHeaderPadCount());

        return ret;
    }

    private String readCString(final int length) throws IOException {
        byte tmpBuffer[] = new byte[length];
        readFully(tmpBuffer, 0, tmpBuffer.length);
        return new String(tmpBuffer, 0, tmpBuffer.length - 1);
    }

    /**
     * Skips specified number of bytes in the current CPIO entry.
     * 
     * @param n
     *            the number of bytes to skip
     * @return the actual number of bytes skipped
     * @throws IOException
     *             if an I/O error has occurred
     * @throws IllegalArgumentException
     *             if n < 0
     */
    public long skip(final long n) throws IOException {
        if (n < 0) {
            throw new IllegalArgumentException("negative skip length");
        }
        ensureOpen();
        int max = (int) Math.min(n, Integer.MAX_VALUE);
        int total = 0;

        while (total < max) {
            int len = max - total;
            if (len > this.tmpbuf.length) {
                len = this.tmpbuf.length;
            }
            len = read(this.tmpbuf, 0, len);
            if (len == -1) {
                this.entryEOF = true;
                break;
            }
            total += len;
        }
        return total;
    }

    public ArchiveEntry getNextEntry() throws IOException {
        return getNextCPIOEntry();
    }

    /**
     * Checks if the signature matches one of the following magic values:
     * 
     * Strings:
     *  
     * "070701" - MAGIC_NEW
     * "070702" - MAGIC_NEW_CRC
     * "070707" - MAGIC_OLD_ASCII
     * 
     * Octal Binary value:
     * 
     * 070707 - MAGIC_OLD_BINARY (held as a short) = 0x71C7 or 0xC771
     */
    public static boolean matches(byte[] signature, int length) {
        if (length < 6) {
            return false;
        }
        
        // Check binary values
        if (signature[0] == 0x71 && (signature[1] & 0xFF) == 0xc7) {
            return true;
        }
        if (signature[1] == 0x71 && (signature[0] & 0xFF) == 0xc7) {
            return true;
        }

        // Check Ascii (String) values
        // 3037 3037 30nn
        if (signature[0] != 0x30) {
            return false;
        }
        if (signature[1] != 0x37) {
            return false;
        }
        if (signature[2] != 0x30) {
            return false;
        }
        if (signature[3] != 0x37) {
            return false;
        }
        if (signature[4] != 0x30) {
            return false;
        }
        // Check last byte
        if (signature[5] == 0x31) {
            return true;
        }
        if (signature[5] == 0x32) {
            return true;
        }
        if (signature[5] == 0x37) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14904.java