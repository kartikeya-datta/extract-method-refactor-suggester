error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1745.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1745.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1745.java
text:
```scala
S@@tring name = new String(blockBuffer, i + 8, blockBuffer[i + 7]); // TODO default charset?

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
package org.apache.commons.compress.archivers.dump;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

/**
 * The DumpArchiveInputStream reads a UNIX dump archive as an InputStream.
 * Methods are provided to position at each successive entry in
 * the archive, and the read each entry as a normal input stream
 * using read().
 *
 * @NotThreadSafe
 */
public class DumpArchiveInputStream extends ArchiveInputStream {
    private DumpArchiveSummary summary;
    private DumpArchiveEntry active;
    private boolean isClosed;
    private boolean hasHitEOF;
    private long entrySize;
    private long entryOffset;
    private int readIdx;
    private byte[] readBuf = new byte[DumpArchiveConstants.TP_SIZE];
    private byte[] blockBuffer;
    private int recordOffset;
    private long filepos;
    protected TapeInputStream raw;

    // map of ino -> dirent entry. We can use this to reconstruct full paths.
    private Map<Integer, Dirent> names = new HashMap<Integer, Dirent>();

    // map of ino -> (directory) entry when we're missing one or more elements in the path.
    private Map<Integer, DumpArchiveEntry> pending = new HashMap<Integer, DumpArchiveEntry>();

    // queue of (directory) entries where we now have the full path.
    private Queue<DumpArchiveEntry> queue;

    /**
     * Constructor.
     *
     * @param is
     * @throws ArchiveException
     */
    public DumpArchiveInputStream(InputStream is) throws ArchiveException {
        this.raw = new TapeInputStream(is);
        this.hasHitEOF = false;

        try {
            // read header, verify it's a dump archive.
            byte[] headerBytes = raw.readRecord();

            if (!DumpArchiveUtil.verify(headerBytes)) {
                throw new UnrecognizedFormatException();
            }

            // get summary information
            summary = new DumpArchiveSummary(headerBytes);

            // reset buffer with actual block size.
            raw.resetBlockSize(summary.getNTRec(), summary.isCompressed());

            // allocate our read buffer.
            blockBuffer = new byte[4 * DumpArchiveConstants.TP_SIZE];

            // skip past CLRI and BITS segments since we don't handle them yet.
            readCLRI();
            readBITS();
        } catch (IOException ex) {
            throw new ArchiveException(ex.getMessage(), ex);
        }

        // put in a dummy record for the root node.
        Dirent root = new Dirent(2, 2, 4, ".");
        names.put(Integer.valueOf(2), root);

        // use priority based on queue to ensure parent directories are
        // released first.
        queue = new PriorityQueue<DumpArchiveEntry>(10,
                new Comparator<DumpArchiveEntry>() {
                    public int compare(DumpArchiveEntry p, DumpArchiveEntry q) {
                        if ((p.getOriginalName() == null) || (q.getOriginalName() == null)) {
                            return Integer.MAX_VALUE;
                        }

                        return p.getOriginalName().compareTo(q.getOriginalName());
                    }
                });
    }

    @Deprecated
    @Override
    public int getCount() {
        return (int) getBytesRead();
    }

    @Override
    public long getBytesRead() {
        return raw.getBytesRead();
    }

    /**
     * Return the archive summary information.
     */
    public DumpArchiveSummary getSummary() {
        return summary;
    }

    /**
     * Read CLRI (deleted inode) segment.
     */
    private void readCLRI() throws IOException {
        byte[] readBuf = raw.readRecord();

        if (!DumpArchiveUtil.verify(readBuf)) {
            throw new InvalidFormatException();
        }

        active = DumpArchiveEntry.parse(readBuf);

        if (DumpArchiveConstants.SEGMENT_TYPE.CLRI != active.getHeaderType()) {
            throw new InvalidFormatException();
        }

        // we don't do anything with this yet.
        if (raw.skip(DumpArchiveConstants.TP_SIZE * active.getHeaderCount())
            == -1) {
            throw new EOFException();
        }
        readIdx = active.getHeaderCount();
    }

    /**
     * Read BITS segment.
     */
    private void readBITS() throws IOException {
        byte[] readBuf = raw.readRecord();

        if (!DumpArchiveUtil.verify(readBuf)) {
            throw new InvalidFormatException();
        }

        active = DumpArchiveEntry.parse(readBuf);

        if (DumpArchiveConstants.SEGMENT_TYPE.BITS != active.getHeaderType()) {
            throw new InvalidFormatException();
        }

        // we don't do anything with this yet.
        if (raw.skip(DumpArchiveConstants.TP_SIZE * active.getHeaderCount())
            == -1) {
            throw new EOFException();
        }
        readIdx = active.getHeaderCount();
    }

    /**
     * Read the next entry.
     */
    public DumpArchiveEntry getNextDumpEntry() throws IOException {
        return getNextEntry();
    }

    /**
     * Read the next entry.
     */
    @Override
    public DumpArchiveEntry getNextEntry() throws IOException {
        DumpArchiveEntry entry = null;
        String path = null;

        // is there anything in the queue?
        if (!queue.isEmpty()) {
            return queue.remove();
        }

        while (entry == null) {
            if (hasHitEOF) {
                return null;
            }

            // skip any remaining records in this segment for prior file.
            // we might still have holes... easiest to do it
            // block by block. We may want to revisit this if
            // the unnecessary decompression time adds up.
            while (readIdx < active.getHeaderCount()) {
                if (!active.isSparseRecord(readIdx++)
                    && raw.skip(DumpArchiveConstants.TP_SIZE) == -1) {
                    throw new EOFException();
                }
            }

            readIdx = 0;
            filepos = raw.getBytesRead();

            byte[] headerBytes = raw.readRecord();

            if (!DumpArchiveUtil.verify(headerBytes)) {
                throw new InvalidFormatException();
            }

            active = DumpArchiveEntry.parse(headerBytes);

            // skip any remaining segments for prior file.
            while (DumpArchiveConstants.SEGMENT_TYPE.ADDR == active.getHeaderType()) {
                if (raw.skip(DumpArchiveConstants.TP_SIZE
                             * (active.getHeaderCount()
                                - active.getHeaderHoles())) == -1) {
                    throw new EOFException();
                }

                filepos = raw.getBytesRead();
                headerBytes = raw.readRecord();

                if (!DumpArchiveUtil.verify(headerBytes)) {
                    throw new InvalidFormatException();
                }

                active = DumpArchiveEntry.parse(headerBytes);
            }

            // check if this is an end-of-volume marker.
            if (DumpArchiveConstants.SEGMENT_TYPE.END == active.getHeaderType()) {
                hasHitEOF = true;
                isClosed = true;
                raw.close();

                return null;
            }

            entry = active;

            if (entry.isDirectory()) {
                readDirectoryEntry(active);

                // now we create an empty InputStream.
                entryOffset = 0;
                entrySize = 0;
                readIdx = active.getHeaderCount();
            } else {
                entryOffset = 0;
                entrySize = active.getEntrySize();
                readIdx = 0;
            }

            recordOffset = readBuf.length;

            path = getPath(entry);

            if (path == null) {
                entry = null;
            }
        }

        entry.setName(path);
        entry.setSimpleName(names.get(Integer.valueOf(entry.getIno())).getName());
        entry.setOffset(filepos);

        return entry;
    }

    /**
     * Read directory entry.
     */
    private void readDirectoryEntry(DumpArchiveEntry entry)
        throws IOException {
        long size = entry.getEntrySize();
        boolean first = true;

        while (first ||
                (DumpArchiveConstants.SEGMENT_TYPE.ADDR == entry.getHeaderType())) {
            // read the header that we just peeked at.
            if (!first) {
                raw.readRecord();
            }

            if (!names.containsKey(Integer.valueOf(entry.getIno())) &&
                    (DumpArchiveConstants.SEGMENT_TYPE.INODE == entry.getHeaderType())) {
                pending.put(Integer.valueOf(entry.getIno()), entry);
            }

            int datalen = DumpArchiveConstants.TP_SIZE * entry.getHeaderCount();

            if (blockBuffer.length < datalen) {
                blockBuffer = new byte[datalen];
            }

            if (raw.read(blockBuffer, 0, datalen) != datalen) {
                throw new EOFException();
            }

            int reclen = 0;

            for (int i = 0; (i < (datalen - 8)) && (i < (size - 8));
                    i += reclen) {
                int ino = DumpArchiveUtil.convert32(blockBuffer, i);
                reclen = DumpArchiveUtil.convert16(blockBuffer, i + 4);

                byte type = blockBuffer[i + 6];

                String name = new String(blockBuffer, i + 8, blockBuffer[i + 7]);

                if (".".equals(name) || "..".equals(name)) {
                    // do nothing...
                    continue;
                }

                Dirent d = new Dirent(ino, entry.getIno(), type, name);

                /*
                if ((type == 4) && names.containsKey(ino)) {
                    System.out.println("we already have ino: " +
                                       names.get(ino));
                }
                */

                names.put(Integer.valueOf(ino), d);

                // check whether this allows us to fill anything in the pending list.
                for (Map.Entry<Integer, DumpArchiveEntry> e : pending.entrySet()) {
                    String path = getPath(e.getValue());

                    if (path != null) {
                        e.getValue().setName(path);
                        e.getValue()
                         .setSimpleName(names.get(e.getKey()).getName());
                        queue.add(e.getValue());
                    }
                }

                // remove anything that we found. (We can't do it earlier
                // because of concurrent modification exceptions.)
                for (DumpArchiveEntry e : queue) {
                    pending.remove(Integer.valueOf(e.getIno()));
                }
            }

            byte[] peekBytes = raw.peek();

            if (!DumpArchiveUtil.verify(peekBytes)) {
                throw new InvalidFormatException();
            }

            entry = DumpArchiveEntry.parse(peekBytes);
            first = false;
            size -= DumpArchiveConstants.TP_SIZE;
        }
    }

    /**
     * Get full path for specified archive entry, or null if there's a gap.
     *
     * @param entry
     * @return  full path for specified archive entry, or null if there's a gap.
     */
    private String getPath(DumpArchiveEntry entry) {
        // build the stack of elements. It's possible that we're 
        // still missing an intermediate value and if so we
        Stack<String> elements = new Stack<String>();
        Dirent dirent = null;

        for (int i = entry.getIno();; i = dirent.getParentIno()) {
            if (!names.containsKey(Integer.valueOf(i))) {
                elements.clear();
                break;
            }

            dirent = names.get(Integer.valueOf(i));
            elements.push(dirent.getName());

            if (dirent.getIno() == dirent.getParentIno()) {
                break;
            }
        }

        // if an element is missing defer the work and read next entry.
        if (elements.isEmpty()) {
            pending.put(Integer.valueOf(entry.getIno()), entry);

            return null;
        }

        // generate full path from stack of elements.
        StringBuilder sb = new StringBuilder(elements.pop());

        while (!elements.isEmpty()) {
            sb.append('/');
            sb.append(elements.pop());
        }

        return sb.toString();
    }

    /**
     * Reads bytes from the current dump archive entry.
     *
     * This method is aware of the boundaries of the current
     * entry in the archive and will deal with them as if they
     * were this stream's start and EOF.
     *
     * @param buf The buffer into which to place bytes read.
     * @param off The offset at which to place bytes read.
     * @param len The number of bytes to read.
     * @return The number of bytes read, or -1 at EOF.
     * @throws IOException on error
     */
    @Override
    public int read(byte[] buf, int off, int len) throws IOException {
        int totalRead = 0;

        if (isClosed || (entryOffset >= entrySize)) {
            return -1;
        }

        if ((len + entryOffset) > entrySize) {
            len = (int) (entrySize - entryOffset);
        }

        while (len > 0) {
            int sz = (len > (readBuf.length - recordOffset))
                ? (readBuf.length - recordOffset) : len;

            // copy any data we have
            if ((recordOffset + sz) <= readBuf.length) {
                System.arraycopy(readBuf, recordOffset, buf, off, sz);
                totalRead += sz;
                recordOffset += sz;
                len -= sz;
                off += sz;
            }

            // load next block if necessary.
            if (len > 0) {
                if (readIdx >= 512) {
                    byte[] headerBytes = raw.readRecord();

                    if (!DumpArchiveUtil.verify(headerBytes)) {
                        throw new InvalidFormatException();
                    }

                    active = DumpArchiveEntry.parse(headerBytes);
                    readIdx = 0;
                }

                if (!active.isSparseRecord(readIdx++)) {
                    int r = raw.read(readBuf, 0, readBuf.length);
                    if (r != readBuf.length) {
                        throw new EOFException();
                    }
                } else {
                    Arrays.fill(readBuf, (byte) 0);
                }

                recordOffset = 0;
            }
        }

        entryOffset += totalRead;

        return totalRead;
    }

    /**
     * Closes the stream for this entry.
     */
    @Override
    public void close() throws IOException {
        if (!isClosed) {
            isClosed = true;
            raw.close();
        }
    }

    /**
     * Look at the first few bytes of the file to decide if it's a dump
     * archive. With 32 bytes we can look at the magic value, with a full
     * 1k we can verify the checksum.
     */
    public static boolean matches(byte[] buffer, int length) {
        // do we have enough of the header?
        if (length < 32) {
            return false;
        }

        // this is the best test
        if (length >= DumpArchiveConstants.TP_SIZE) {
            return DumpArchiveUtil.verify(buffer);
        }

        // this will work in a pinch.
        return DumpArchiveConstants.NFS_MAGIC == DumpArchiveUtil.convert32(buffer,
            24);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1745.java