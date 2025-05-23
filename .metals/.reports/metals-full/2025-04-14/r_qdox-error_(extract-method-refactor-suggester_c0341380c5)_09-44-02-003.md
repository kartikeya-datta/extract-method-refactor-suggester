error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/231.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/231.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/231.java
text:
```scala
i@@nt retValue = stream.read(b, off, actualLength);

/*

   Derby - Class org.apache.derby.impl.jdbc.UpdateableBlobStream

   Licensed to the Apache Software Foundation (ASF) under one
   or more contributor license agreements.  See the NOTICE file
   distributed with this work for additional information
   regarding copyright ownership.  The ASF licenses this file
   to you under the Apache License, Version 2.0 (the
   "License"); you may not use this file except in compliance
   with the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing,
   software distributed under the License is distributed on an
   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
   KIND, either express or implied.  See the License for the
   specific language governing permissions and limitations
   under the License.

 */

package org.apache.derby.impl.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import org.apache.derby.iapi.reference.SQLState;
import org.apache.derby.iapi.services.i18n.MessageService;

/**
 * Updateable blob stream is a wrapper stream over dvd stream
 * and LOBInputStream.
 * It detects if blob data has moved from dvd to clob control. If this happens,
 * it will update itself to point to LOBInputStream and reflect changes made to
 * the Blob after the current position of the stream.
 */
class UpdateableBlobStream extends InputStream {
    /**
     * Flag to check if it is using stream from LOBStreamControl or from DVD.
     * <code>true</code> means data is read from LOBStreamControl,
     * <code>false</code> means data is read from the DVD.
     */
    private boolean materialized;
    private InputStream stream;
    /* Current position of this stream in number of bytes. */
    private long pos;
    private final EmbedBlob blob;
    
    /**
     * Position in Blob where to stop reading.
     */
    private long maxPos;
    

    /**
     * Constructs UpdateableBlobStream using the the InputStream receives as the
     * parameter. The initial position is set to the <code>0</code>.
     * @param blob EmbedBlob this stream is associated with.
     * @param is InputStream this class is going to use internally.
     */
    UpdateableBlobStream (EmbedBlob blob, InputStream is) {
        stream = is;
        this.pos = 0;
        this.blob = blob;
        //The subset of the Blob
        //has not been requested.
        //Hence set maxPos to -1.
        this.maxPos = -1;
    }
    
    /**
     * Construct an <code>UpdateableBlobStream<code> using the 
     * <code>InputStream</code> received as parameter. The initial
     * position in the stream is set to <code>pos</code> and the
     * stream is restricted to a length of <code>len</code>.
     *
     * @param blob EmbedBlob this stream is associated with.
     * @param is InputStream this class is going to use internally.
     * @param pos initial position
     * @param len The length to which the underlying <code>InputStream</code>
     *            has to be restricted.
     * @throws IOException
     * @throws SQLException
     */
    UpdateableBlobStream (EmbedBlob blob, InputStream is, long pos, long len) 
    throws IOException, SQLException {
        this(blob, is);
        //The length requested cannot exceed the length
        //of the underlying Blob object. Hence chose the
        //minimum of the length of the underlying Blob
        //object and requested length.
        maxPos = Math.min(blob.length(), pos + len);
        
        //Skip to the requested position
        //inside the stream.
        skip(pos);
    }

    /**
     * Checks if this object is using materialized blob
     * if not it checks if the blob was materialized since
     * this stream was last access. If the blob was materialized
     * (due to one of the set methods) it gets the stream again and
     * sets the position to current read position.
     */
    private void updateIfRequired () throws IOException {
        if (materialized)
            return;
        if (blob.isMaterialized()) {
            materialized = true;
            try {
                stream = blob.getBinaryStream();
            } catch (SQLException ex) {
                IOException ioe = new IOException (ex.getMessage());
                ioe.initCause (ex);
                throw ioe;
            }
            long leftToSkip = pos;
            while (leftToSkip > 0) {
                long skipped = stream.skip (leftToSkip);
                if (skipped == 0) {
                    //skipping zero byte check stream has reached eof
                    if (stream.read() < 0) {
                         throw new IOException (
                                 MessageService.getCompleteMessage (
                                 SQLState.STREAM_EOF, new Object [0]));
                    }
                    else {
                        skipped = 1;
                    }
                }
                leftToSkip -= skipped;
            }
        }
    }

    /**
     * Reads the next byte of data from the input stream. The value byte is
     * returned as an <code>int</code> in the range <code>0</code> to
     * <code>255</code>. If no byte is available because the end of the stream
     * has been reached, the value <code>-1</code> is returned. This method
     * blocks until input data is available, the end of the stream is detected,
     * or an exception is thrown.
     * <p>
     * A subclass must provide an implementation of this method.
     * <p>
     * Note that this stream will reflect changes made to the underlying Blob at
     * positions equal to or larger then the current position.
     *
     * @return the next byte of data, or <code>-1</code> if the end of the
     *             stream is reached.
     * @exception IOException  if an I/O error occurs.
     * @see InputStream#read
     */
    public int read() throws IOException {
        updateIfRequired();
        
        //If maxPos is not invalid and the current
        //position inside the stream has exceeded
        //maxPos the read sould return -1 signifying
        //end of stream.
        if (maxPos != -1 && pos >= maxPos) {
            return -1;
        }
        int ret = stream.read();
        if (ret >= 0)
            pos++;
        return ret;
    }

    /**
     * Reads up to <code>len</code> bytes of data from the input stream into
     * an array of bytes.  An attempt is made to read as many as
     * <code>len</code> bytes, but a smaller number may be read.
     * The number of bytes actually read is returned as an integer.
     * <p>
     * Note that this stream will reflect changes made to the underlying Blob at
     * positions equal to or larger then the current position .
     *
     * @param b     the buffer into which the data is read.
     * @param off   the start offset in array <code>b</code>
     *                   at which the data is written.
     * @param len   the maximum number of bytes to read.
     * @return the total number of bytes read into the buffer, or
     *             <code>-1</code> if there is no more data because the end of
     *             the stream has been reached.
     * @exception IOException If the first byte cannot be read for any reason
     * other than end of file, or if the input stream has been closed, or if
     * some other I/O error occurs.
     * @exception NullPointerException If <code>b</code> is <code>null</code>.
     * @exception IndexOutOfBoundsException If <code>off</code> is negative,
     * <code>len</code> is negative, or <code>len</code> is greater than
     * <code>b.length - off</code>
     * @see java.io.InputStream#read(byte[],int,int)
     */
    public int read(byte[] b, int off, int len) throws IOException {
        int actualLength = 0;
        updateIfRequired();
        
        //If maxPos is not invalid then
        //ensure that the length(len) 
        //that is requested falls within
        //the restriction set by maxPos.
        if(maxPos != -1) {
            actualLength 
                    = (int )Math.min(len, maxPos - pos);
        }
        else {
            //maxPos has not been set. Make
            //maxPos the length requested.
            actualLength = len;
        }
        int retValue = super.read(b, off, actualLength);
        if (retValue > 0)
            pos += retValue;
        return retValue;
    }

    /**
     * Reads some number of bytes from the input stream and stores them into
     * the buffer array <code>b</code>. The number of bytes actually read is
     * returned as an integer.  This method blocks until input data is
     * available, end of file is detected, or an exception is thrown.
     * <p>
     * Note that this stream will reflect changes made to the underlying Blob at
     * positions equal to or larger then the current position .
     *
     * @param b   the buffer into which the data is read.
     * @return the total number of bytes read into the buffer, or
     *             <code>-1</code> is there is no more data because the end of
     *             the stream has been reached.
     * @exception IOException  If the first byte cannot be read for any reason
     * other than the end of the file, if the input stream has been closed, or
     * if some other I/O error occurs.
     * @exception NullPointerException  if <code>b</code> is <code>null</code>.
     * @see java.io.InputStream#read(byte[])
     */
    public int read(byte[] b) throws IOException {
        updateIfRequired();
        int actualLength = 0;
        //If maxPos is not invalid
        //then ensure that the length
        //(len of the byte array b) 
        //falls within the restriction 
        //set by maxPos.
        if(maxPos != -1) {
            actualLength 
                    = (int )Math.min(b.length, maxPos - pos);
        }
        else {
            //maxPos has not been set. Make
            //maxPos the length requested.
            actualLength = b.length;
        }
        int retValue = stream.read(b, 0, actualLength);
        if (retValue > 0)
            pos += retValue;
        return retValue;
    }

    /**
     * Skips over and discards <code>n</code> bytes of data from this input
     * stream. The <code>skip</code> method may, for a variety of reasons, end
     * up skipping over some smaller number of bytes, possibly <code>0</code>.
     * This may result from any of a number of conditions; reaching end of file
     * before <code>n</code> bytes have been skipped is only one possibility.
     * The actual number of bytes skipped is returned.  If <code>n</code> is
     * negative, no bytes are skipped.
     * <p>
     * Note that this stream will reflect changes made to the underlying Blob at
     * positions equal to or larger then the current position .
     *
     * @param n   the number of bytes to be skipped.
     * @return the actual number of bytes skipped.
     * @exception IOException  if the stream does not support seek,
     *                      or if some other I/O error occurs.
     * @see java.io.InputStream#skip(long)
     */
    public long skip(long n) throws IOException {
        updateIfRequired();
        long retValue = stream.skip(n);
        if (retValue > 0)
            pos += retValue;
        return retValue;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/231.java