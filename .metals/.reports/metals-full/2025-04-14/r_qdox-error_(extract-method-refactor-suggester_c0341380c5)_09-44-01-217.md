error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2681.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2681.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2681.java
text:
```scala
i@@nt fId = ChecksumManager.getFileId(filename_);

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.cassandra.io;

import java.io.*;
import java.util.Arrays;
import java.util.Random;

import org.apache.cassandra.utils.FBUtilities;


/**
 * A <code>ChecksumRandomAccessFile</code> is like a
 * <code>RandomAccessFile</code>, but it uses a private buffer so that most
 * operations do not require a disk access.
 * <P>
 * 
 * Note: The operations on this class are unmonitored. Also, the correct
 * functioning of the <code>RandomAccessFile</code> methods that are not
 * overridden here relies on the implementation of those methods in the
 * superclass.
 * Author : Avinash Lakshman ( alakshman@facebook.com) & Prashant Malik ( pmalik@facebook.com )
 */

public final class ChecksumRandomAccessFile extends RandomAccessFile
{ 
    private static enum ChecksumOperations
    {
        LOG,
        VERIFY
    }
    
    static final int LogBuffSz_ = 16; // 64K buffer
    private static final int checksumSz_ = (1 << LogBuffSz_); // 64K
    public static final int BuffSz_ = (1 << LogBuffSz_);
    static final long BuffMask_ = ~(((long) BuffSz_) - 1L);
    
    /*
     * This implementation is based on the buffer implementation in Modula-3's
     * "Rd", "Wr", "RdClass", and "WrClass" interfaces.
     */
    private boolean dirty_; // true iff unflushed bytes exist
    private boolean closed_; // true iff the file is closed
    private long curr_; // current position in file
    private long lo_, hi_; // bounds on characters in "buff"
    private byte[] buff_; // local buffer
    private long maxHi_; // this.lo + this.buff.length
    private boolean hitEOF_; // buffer contains last file block?
    private long diskPos_; // disk position
    private String filename_; // file name
    
    /*
     * To describe the above fields, we introduce the following abstractions for
     * the file "f":
     * 
     * len(f) the length of the file curr(f) the current position in the file
     * c(f) the abstract contents of the file disk(f) the contents of f's
     * backing disk file closed(f) true iff the file is closed
     * 
     * "curr(f)" is an index in the closed interval [0, len(f)]. "c(f)" is a
     * character sequence of length "len(f)". "c(f)" and "disk(f)" may differ if
     * "c(f)" contains unflushed writes not reflected in "disk(f)". The flush
     * operation has the effect of making "disk(f)" identical to "c(f)".
     * 
     * A file is said to be *valid* if the following conditions hold:
     * 
     * V1. The "closed" and "curr" fields are correct:
     * 
     * f.closed == closed(f) f.curr == curr(f)
     * 
     * V2. The current position is either contained in the buffer, or just past
     * the buffer:
     * 
     * f.lo <= f.curr <= f.hi
     * 
     * V3. Any (possibly) unflushed characters are stored in "f.buff":
     * 
     * (forall i in [f.lo, f.curr): c(f)[i] == f.buff[i - f.lo])
     * 
     * V4. For all characters not covered by V3, c(f) and disk(f) agree:
     * 
     * (forall i in [f.lo, len(f)): i not in [f.lo, f.curr) => c(f)[i] ==
     * disk(f)[i])
     * 
     * V5. "f.dirty" is true iff the buffer contains bytes that should be
     * flushed to the file; by V3 and V4, only part of the buffer can be dirty.
     * 
     * f.dirty == (exists i in [f.lo, f.curr): c(f)[i] != f.buff[i - f.lo])
     * 
     * V6. this.maxHi == this.lo + this.buff.length
     * 
     * Note that "f.buff" can be "null" in a valid file, since the range of
     * characters in V3 is empty when "f.lo == f.curr".
     * 
     * A file is said to be *ready* if the buffer contains the current position,
     * i.e., when:
     * 
     * R1. !f.closed && f.buff != null && f.lo <= f.curr && f.curr < f.hi
     * 
     * When a file is ready, reading or writing a single byte can be performed
     * by reading or writing the in-memory buffer without performing a disk
     * operation.
     */
    
    /**
     * Open a new <code>BufferedRandomAccessFile</code> on <code>file</code>
     * in mode <code>mode</code>, which should be "r" for reading only, or
     * "rw" for reading and writing.
     */
    public ChecksumRandomAccessFile(File file, String mode) throws IOException
    {
        super(file, mode);
        this.init(file.getAbsolutePath(), 0);
    }
    
    public ChecksumRandomAccessFile(File file, String mode, int size) throws IOException
    {
        super(file, mode);
        this.init(file.getAbsolutePath(), size);
    }
    
    /**
     * Open a new <code>BufferedRandomAccessFile</code> on the file named
     * <code>name</code> in mode <code>mode</code>, which should be "r" for
     * reading only, or "rw" for reading and writing.
     */
    public ChecksumRandomAccessFile(String name, String mode) throws IOException
    {
        super(name, mode);
        this.init(name, 0);
    }
    
    public ChecksumRandomAccessFile(String name, String mode, int size) throws FileNotFoundException
    {
        super(name, mode);
        this.init(name, size);
    }
    
    private void init(String name, int size)
    {
        this.dirty_ = this.closed_ = false;
        this.lo_ = this.curr_ = this.hi_ = 0;
        this.buff_ = (size > BuffSz_) ? new byte[size] : new byte[BuffSz_];
        this.maxHi_ = (long) BuffSz_;
        this.hitEOF_ = false;
        this.diskPos_ = 0L;
        this.filename_ = name;
    }
    
    public void close() throws IOException
    {
        this.flush();
        this.closed_ = true;
        super.close();
    }
    
    /**
     * Flush any bytes in the file's buffer that have not yet been written to
     * disk. If the file was created read-only, this method is a no-op.
     */
    public void flush() throws IOException
    {
        this.flushBuffer();
    }
    
    private void doChecksumOperation(ChecksumOperations chksumOps) throws IOException
    {        
        int buffSz = buff_.length;
        /*
         * If the diskPos_ is at the buffer boundary then return 
         * diskPos_ - 1 else return the actual diskPos_.
        */        
        long currentPosition = ( (diskPos_ % buffSz) == 0 ) ? diskPos_ - 1 : diskPos_;
        /* Tells me which buffered chunk I am in. */
        long chunk = (currentPosition / buffSz) + 1; 
        /* Number of checksum chunks within a buffer */
        int chksumChunks = buff_.length / checksumSz_;  
        /* Position of the start of the previous buffer boundary */
        long pos = (chunk == 0) ? 0 : (chunk - 1)*buffSz;
        int startOffset = 0;
        int chksumChunkId = (int)(chksumChunks*(chunk - 1) + 1);
        do
        {            
            int fId = SequenceFile.getFileId(filename_);               
            switch( chksumOps )
            {
                case LOG:                    
                    ChecksumManager.instance(filename_).logChecksum(fId, chksumChunkId++, buff_, startOffset, checksumSz_);
                    break;
                case VERIFY:
                    ChecksumManager.instance(filename_).validateChecksum(filename_, chksumChunkId++, buff_, startOffset, checksumSz_);
                    break;
            }
            pos += checksumSz_;
            startOffset += checksumSz_;
        }
        while ( pos < currentPosition );
    }
    
    /**
     * Flush any dirty bytes in the buffer to disk. 
    */
    private void flushBuffer() throws IOException
    {
        if (this.dirty_)
        {
            if (this.diskPos_ != this.lo_)
                super.seek(this.lo_);
            int len = (int) (this.curr_ - this.lo_);            
            super.write(this.buff_, 0, len);
            this.diskPos_ = this.curr_;
            this.dirty_ = false;
            /* checksum the data before we write to disk */
            doChecksumOperation(ChecksumOperations.LOG);
        }
    }
    
    /**
     * Read at most "this.buff.length" bytes into "this.buff", returning the
     * number of bytes read. If the return result is less than
     * "this.buff.length", then EOF was read.
     */
    private int fillBuffer() throws IOException
    {
        int cnt = 0;
        int rem = this.buff_.length;
        int n = -1;
        while (rem > 0)
        {
            n = super.read(this.buff_, cnt, rem);
            if (n < 0)
                break;
            cnt += n;
            rem -= n;
        }
        if (this.hitEOF_ = (cnt < this.buff_.length))
        {
            // make sure buffer that wasn't read is initialized with -1
            Arrays.fill(this.buff_, cnt, this.buff_.length, (byte) 0xff);             
        }                
        this.diskPos_ += cnt;
        return cnt;
    }
    
    /**
     * This method positions <code>this.curr</code> at position <code>pos</code>.
     * If <code>pos</code> does not fall in the current buffer, it flushes the
     * current buffer and loads the correct one.<p>
     * 
     * On exit from this routine <code>this.curr == this.hi</code> iff <code>pos</code>
     * is at or past the end-of-file, which can only happen if the file was
     * opened in read-only mode.
     */
    public void seek(long pos) throws IOException
    {
        if (pos >= this.hi_ || pos < this.lo_)
        {
            // seeking outside of current buffer -- flush and read
            this.flushBuffer();
            this.lo_ = pos & BuffMask_; // start at BuffSz boundary
            this.maxHi_ = this.lo_ + (long) this.buff_.length;
            if (this.diskPos_ != this.lo_)
            {
                super.seek(this.lo_);
                this.diskPos_ = this.lo_;
            }
            int n = this.fillBuffer();
            if ( n > 0 )
            {
                doChecksumOperation(ChecksumOperations.VERIFY);
            }
            this.hi_ = this.lo_ + (long) n;
        }
        else
        {
            // seeking inside current buffer -- no read required
            if (pos < this.curr_)
            {
                // if seeking backwards, we must flush to maintain V4
                this.flushBuffer();
            }
        }
        this.curr_ = pos;
    }
    
    public long getFilePointer()
    {
        return this.curr_;
    }
    
    public long length() throws IOException
    {
        return Math.max(this.curr_, super.length());
    }
    
    public int read() throws IOException
    {
        if (this.curr_ >= this.hi_)
        {
            // test for EOF
            // if (this.hi < this.maxHi) return -1;
            if (this.hitEOF_)
                return -1;
            
            // slow path -- read another buffer
            this.seek(this.curr_);
            if (this.curr_ == this.hi_)
                return -1;
        }
        byte res = this.buff_[(int) (this.curr_ - this.lo_)];
        this.curr_++;
        return ((int) res) & 0xFF; // convert byte -> int
    }
    
    public int read(byte[] b) throws IOException
    {
        return this.read(b, 0, b.length);
    }
    
    public int read(byte[] b, int off, int len) throws IOException
    {
        if (this.curr_ >= this.hi_)
        {
            // test for EOF
            // if (this.hi < this.maxHi) return -1;
            if (this.hitEOF_)
                return -1;
            
            // slow path -- read another buffer
            this.seek(this.curr_);
            if (this.curr_ == this.hi_)
                return -1;
        }
        len = Math.min(len, (int) (this.hi_ - this.curr_));
        int buffOff = (int) (this.curr_ - this.lo_);
        System.arraycopy(this.buff_, buffOff, b, off, len);
        this.curr_ += len;
        return len;
    }
    
    public void write(int b) throws IOException
    {
        if (this.curr_ >= this.hi_)
        {
            if (this.hitEOF_ && this.hi_ < this.maxHi_)
            {
                // at EOF -- bump "hi"
                this.hi_++;
            }
            else
            {
                // slow path -- write current buffer; read next one
                this.seek(this.curr_);
                if (this.curr_ == this.hi_)
                {
                    // appending to EOF -- bump "hi"
                    this.hi_++;
                }
            }
        }
        this.buff_[(int) (this.curr_ - this.lo_)] = (byte) b;
        this.curr_++;
        this.dirty_ = true;
    }
    
    public void write(byte[] b) throws IOException
    {
        this.write(b, 0, b.length);
    }
    
    public void write(byte[] b, int off, int len) throws IOException
    {
        while (len > 0)
        {
            int n = this.writeAtMost(b, off, len);
            off += n;
            len -= n;
        }
        this.dirty_ = true;
    }
    
    /*
     * Write at most "len" bytes to "b" starting at position "off", and return
     * the number of bytes written.
     */
    private int writeAtMost(byte[] b, int off, int len) throws IOException
    {
        if (this.curr_ >= this.hi_)
        {
            if (this.hitEOF_ && this.hi_ < this.maxHi_)
            {
                // at EOF -- bump "hi"
                this.hi_ = this.maxHi_;
            }
            else
            {
                // slow path -- write current buffer; read next one
                this.seek(this.curr_);
                if (this.curr_ == this.hi_)
                {
                    // appending to EOF -- bump "hi"
                    this.hi_ = this.maxHi_;
                }
            }
        }
        len = Math.min(len, (int) (this.hi_ - this.curr_));
        int buffOff = (int) (this.curr_ - this.lo_);
        System.arraycopy(b, off, this.buff_, buffOff, len);
        this.curr_ += len;
        return len;
    }
    
    public static void main(String[] args) throws Throwable
    {
        
        RandomAccessFile raf = new ChecksumRandomAccessFile("C:\\Engagements\\Table-ColumnFamily-1-Data.dat", "rw");
        byte[] bytes = new byte[32*1024];
        Random random = new Random();
        
        for ( int i = 0; i < 16; ++i )
        {
            random.nextBytes(bytes);
            raf.write(bytes);
        }
        raf.close();
        
        String file = "C:\\Engagements\\Checksum-1.db";
        ChecksumManager.instance("C:\\Engagements\\Table-ColumnFamily-1-Data.dat", file);
        
        raf = new ChecksumRandomAccessFile("C:\\Engagements\\Table-ColumnFamily-1-Data.dat", "rw", 4*1024*1024);
        bytes = new byte[32*1024];
        
        for ( int i = 0; i < 16; ++i )
        {           
            raf.readFully(bytes);
        }
        raf.close();
       
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2681.java