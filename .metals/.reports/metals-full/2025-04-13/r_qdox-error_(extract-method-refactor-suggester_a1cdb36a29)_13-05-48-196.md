error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/576.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/576.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/576.java
text:
```scala
(@@n.length() > 16 || n.contains(" "))) {

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
package org.apache.commons.compress.archivers.ar;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;

/**
 * Implements the "ar" archive format as an output stream.
 * 
 * @NotThreadSafe
 */
public class ArArchiveOutputStream extends ArchiveOutputStream {
    /** Fail if a long file name is required in the archive. */
    public static final int LONGFILE_ERROR = 0;

    /** BSD ar extensions are used to store long file names in the archive. */
    public static final int LONGFILE_BSD = 1;

    private final OutputStream out;
    private long entryOffset = 0;
    private ArArchiveEntry prevEntry;
    private boolean haveUnclosedEntry = false;
    private int longFileMode = LONGFILE_ERROR;

    /** indicates if this archive is finished */
    private boolean finished = false;

    public ArArchiveOutputStream( final OutputStream pOut ) {
        this.out = pOut;
    }

    /**
     * Set the long file mode.
     * This can be LONGFILE_ERROR(0) or LONGFILE_BSD(1).
     * This specifies the treatment of long file names (names &gt;= 16).
     * Default is LONGFILE_ERROR.
     * @param longFileMode the mode to use
     * @since 1.3
     */
    public void setLongFileMode(int longFileMode) {
        this.longFileMode = longFileMode;
    }

    private long writeArchiveHeader() throws IOException {
        byte [] header = ArchiveUtils.toAsciiBytes(ArArchiveEntry.HEADER);
        out.write(header);
        return header.length;
    }

    @Override
    public void closeArchiveEntry() throws IOException {
        if(finished) {
            throw new IOException("Stream has already been finished");
        }
        if (prevEntry == null || !haveUnclosedEntry){
            throw new IOException("No current entry to close");
        }
        if (entryOffset % 2 != 0) {
            out.write('\n'); // Pad byte
        }
        haveUnclosedEntry = false;
    }

    @Override
    public void putArchiveEntry( final ArchiveEntry pEntry ) throws IOException {
        if(finished) {
            throw new IOException("Stream has already been finished");
        }

        ArArchiveEntry pArEntry = (ArArchiveEntry)pEntry;
        if (prevEntry == null) {
            writeArchiveHeader();
        } else {
            if (prevEntry.getLength() != entryOffset) {
                throw new IOException("length does not match entry (" + prevEntry.getLength() + " != " + entryOffset);
            }

            if (haveUnclosedEntry) {
                closeArchiveEntry();
            }
        }

        prevEntry = pArEntry;

        writeEntryHeader(pArEntry);

        entryOffset = 0;
        haveUnclosedEntry = true;
    }

    private long fill( final long pOffset, final long pNewOffset, final char pFill ) throws IOException { 
        final long diff = pNewOffset - pOffset;

        if (diff > 0) {
            for (int i = 0; i < diff; i++) {
                write(pFill);
            }
        }

        return pNewOffset;
    }

    private long write( final String data ) throws IOException {
        final byte[] bytes = data.getBytes("ascii");
        write(bytes);
        return bytes.length;
    }

    private long writeEntryHeader( final ArArchiveEntry pEntry ) throws IOException {

        long offset = 0;
        boolean mustAppendName = false;

        final String n = pEntry.getName();
        if (LONGFILE_ERROR == longFileMode && n.length() > 16) {
            throw new IOException("filename too long, > 16 chars: "+n);
        }
        if (LONGFILE_BSD == longFileMode && 
            (n.length() > 16 || n.indexOf(" ") > -1)) {
            mustAppendName = true;
            offset += write(ArArchiveInputStream.BSD_LONGNAME_PREFIX
                            + String.valueOf(n.length()));
        } else {
            offset += write(n);
        }

        offset = fill(offset, 16, ' ');
        final String m = "" + pEntry.getLastModified();
        if (m.length() > 12) {
            throw new IOException("modified too long");
        }
        offset += write(m);

        offset = fill(offset, 28, ' ');
        final String u = "" + pEntry.getUserId();
        if (u.length() > 6) {
            throw new IOException("userid too long");
        }
        offset += write(u);

        offset = fill(offset, 34, ' ');
        final String g = "" + pEntry.getGroupId();
        if (g.length() > 6) {
            throw new IOException("groupid too long");
        }
        offset += write(g);

        offset = fill(offset, 40, ' ');
        final String fm = "" + Integer.toString(pEntry.getMode(), 8);
        if (fm.length() > 8) {
            throw new IOException("filemode too long");
        }
        offset += write(fm);

        offset = fill(offset, 48, ' ');
        final String s =
            String.valueOf(pEntry.getLength()
                           + (mustAppendName ? n.length() : 0));
        if (s.length() > 10) {
            throw new IOException("size too long");
        }
        offset += write(s);

        offset = fill(offset, 58, ' ');

        offset += write(ArArchiveEntry.TRAILER);

        if (mustAppendName) {
            offset += write(n);
        }

        return offset;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);
        count(len);
        entryOffset += len;
    }

    /**
     * Calls finish if necessary, and then closes the OutputStream
     */
    @Override
    public void close() throws IOException {
        if(!finished) {
            finish();
        }
        out.close();
        prevEntry = null;
    }

    @Override
    public ArchiveEntry createArchiveEntry(File inputFile, String entryName)
            throws IOException {
        if(finished) {
            throw new IOException("Stream has already been finished");
        }
        return new ArArchiveEntry(inputFile, entryName);
    }

    @Override
    public void finish() throws IOException {
        if(haveUnclosedEntry) {
            throw new IOException("This archive contains unclosed entries.");
        } else if(finished) {
            throw new IOException("This archive has already been finished");
        }
        finished = true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/576.java