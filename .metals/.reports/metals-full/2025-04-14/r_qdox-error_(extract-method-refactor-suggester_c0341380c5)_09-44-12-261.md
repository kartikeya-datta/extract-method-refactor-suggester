error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1846.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1846.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1846.java
text:
```scala
static final Z@@ipShort HEADER_ID = new ZipShort(0x0001);

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
package org.apache.commons.compress.archivers.zip;

import java.util.zip.ZipException;

/**
 * Holds size and other extended information for entries that use Zip64
 * features.
 *
 * <p>From {@link http://www.pkware.com/documents/casestudies/APPNOTE.TXT PKWARE's APPNOTE.TXT}
 * <pre>
 * Zip64 Extended Information Extra Field (0x0001):
 *
 *          The following is the layout of the zip64 extended 
 *          information "extra" block. If one of the size or
 *          offset fields in the Local or Central directory
 *          record is too small to hold the required data,
 *          a Zip64 extended information record is created.
 *          The order of the fields in the zip64 extended 
 *          information record is fixed, but the fields will
 *          only appear if the corresponding Local or Central
 *          directory record field is set to 0xFFFF or 0xFFFFFFFF.
 *
 *          Note: all fields stored in Intel low-byte/high-byte order.
 *
 *          Value      Size       Description
 *          -----      ----       -----------
 *  (ZIP64) 0x0001     2 bytes    Tag for this "extra" block type
 *          Size       2 bytes    Size of this "extra" block
 *          Original 
 *          Size       8 bytes    Original uncompressed file size
 *          Compressed
 *          Size       8 bytes    Size of compressed data
 *          Relative Header
 *          Offset     8 bytes    Offset of local header record
 *          Disk Start
 *          Number     4 bytes    Number of the disk on which
 *                                this file starts 
 *
 *          This entry in the Local header must include BOTH original
 *          and compressed file size fields. If encrypting the 
 *          central directory and bit 13 of the general purpose bit
 *          flag is set indicating masking, the value stored in the
 *          Local Header for the original file size will be zero.
 * </pre></p>
 *
 * <p>Currently Commons Compress doesn't support encrypting the
 * central directory so the not about masking doesn't apply.</p>
 *
 * <p>The implementation relies on data being read from the local file
 * header and assumes that both size values are always present.</p>
 *
 * @since Apache Commons Compress 1.2
 * @NotThreadSafe
 */
public class Zip64ExtendedInformationExtraField implements ZipExtraField {
    // TODO: the LFH should probably not contain relativeHeaderOffset
    // and diskStart but then ZipArchivePOutputStream won't write it to
    // the CD either - need to test interop with other implementations
    // to see whether they do have a problem with the extraneous
    // information inside the LFH

    private static final ZipShort HEADER_ID = new ZipShort(0x0001);

    private static final int WORD = 4, DWORD = 8;

    private ZipEightByteInteger size, compressedSize, relativeHeaderOffset;
    private ZipLong diskStart;

    /**
     * This constructor should only be used by the code that reads
     * archives inside of Commons Compress.
     */
    public Zip64ExtendedInformationExtraField() { }

    /**
     * Creates an extra field based on the original and compressed size.
     *
     * @param size the entry's original size
     * @param compressedSize the entry's compressed size
     *
     * @throws IllegalArgumentException if size or compressedSize is null
     */
    public Zip64ExtendedInformationExtraField(ZipEightByteInteger size,
                                              ZipEightByteInteger compressedSize) {
        this(size, compressedSize, null, null);
    }

    /**
     * Creates an extra field based on all four possible values.
     *
     * @param size the entry's original size
     * @param compressedSize the entry's compressed size
     *
     * @throws IllegalArgumentException if size or compressedSize is null
     */
    public Zip64ExtendedInformationExtraField(ZipEightByteInteger size,
                                              ZipEightByteInteger compressedSize,
                                              ZipEightByteInteger relativeHeaderOffset,
                                              ZipLong diskStart) {
        if (size == null) {
            throw new IllegalArgumentException("size must not be null");
        }
        if (compressedSize == null) {
            throw new IllegalArgumentException("compressedSize must not be null");
        }
        this.size = size;
        this.compressedSize = compressedSize;
        this.relativeHeaderOffset = relativeHeaderOffset;
        this.diskStart = diskStart;
    }

    /** {@inheritDoc} */
    public ZipShort getHeaderId() {
        return HEADER_ID;
    }

    /** {@inheritDoc} */
    public ZipShort getLocalFileDataLength() {
        return getCentralDirectoryLength();
    }

    /** {@inheritDoc} */
    public ZipShort getCentralDirectoryLength() {
        return new ZipShort(2 * DWORD  // both size fields
                            + (relativeHeaderOffset != null ? DWORD : 0)
                            + (diskStart != null ? WORD : 0));
    }

    /** {@inheritDoc} */
    public byte[] getLocalFileDataData() {
        return getCentralDirectoryData();
    }

    /** {@inheritDoc} */
    public byte[] getCentralDirectoryData() {
        byte[] data = new byte[getCentralDirectoryLength().getValue()];
        addSizes(data);
        int off = 2 * DWORD;
        if (relativeHeaderOffset != null) {
            System.arraycopy(relativeHeaderOffset.getBytes(), 0, data, off, DWORD);
            off += DWORD;
        }
        if (diskStart != null) {
            System.arraycopy(diskStart.getBytes(), 0, data, off, WORD);
            off += WORD;
        }
        return data;
    }

    /** {@inheritDoc} */
    public void parseFromLocalFileData(byte[] buffer, int offset, int length)
        throws ZipException {
        if (length < 2 * DWORD) {
            throw new ZipException("Zip64 extended information must contain"
                                   + " both size values in the local file"
                                   + " header.");
        }
        size = new ZipEightByteInteger(buffer, offset);
        offset += DWORD;
        compressedSize = new ZipEightByteInteger(buffer, offset);
        offset += DWORD;
        int remaining = length - 2 * DWORD;
        if (remaining >= DWORD) {
            relativeHeaderOffset = new ZipEightByteInteger(buffer, offset);
            offset += DWORD;
            remaining -= DWORD;
        }
        if (remaining >= WORD) {
            diskStart = new ZipLong(buffer, offset);
            offset += WORD;
            remaining -= WORD;
        }
    }

    /** {@inheritDoc} */
    public void parseFromCentralDirectoryData(byte[] buffer, int offset,
                                              int length)
        throws ZipException {
        // if there is no size information in here, we are screwed and
        // can only hope things will get resolved by LFH data later
        // But there are some cases that can be detected
        // * all data is there
        // * length % 8 == 4 -> at least we can identify the diskStart field
        if (length >= 3 * DWORD + WORD) {
            parseFromLocalFileData(buffer, offset, length);
        } else if (length % DWORD == WORD) {
            diskStart = new ZipLong(buffer, offset + length - WORD);
        }
    }

    /**
     * The uncompressed size stored in this extra field.
     */
    public ZipEightByteInteger getSize() {
        return size;
    }

    /**
     * The compressed size stored in this extra field.
     */
    public ZipEightByteInteger getCompressedSize() {
        return compressedSize;
    }

    /**
     * The relative header offset stored in this extra field.
     */
    public ZipEightByteInteger getRelativeHeaderOffset() {
        return relativeHeaderOffset;
    }

    /**
     * The disk start number stored in this extra field.
     */
    public ZipLong getDiskStartNumber() {
        return diskStart;
    }

    private void addSizes(byte[] data) {
        System.arraycopy(size.getBytes(), 0, data, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, data, DWORD, DWORD);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1846.java