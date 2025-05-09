error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/710.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/710.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/710.java
text:
```scala
r@@eturn !isThresholdExceeded();

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
package org.apache.commons.io.output;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;


/**
 * An output stream which will retain data in memory until a specified
 * threshold is reached, and only then commit it to disk. If the stream is
 * closed before the threshold is reached, the data will not be written to
 * disk at all.
 * <p>
 * This class originated in FileUpload processing. In this use case, you do
 * not know in advance the size of the file being uploaded. If the file is small
 * you want to store it in memory (for speed), but if the file is large you want
 * to store it to file (to avoid memory issues).
 *
 * @version $Id$
 */
public class DeferredFileOutputStream
    extends ThresholdingOutputStream
{

    // ----------------------------------------------------------- Data members


    /**
     * The output stream to which data will be written prior to the theshold
     * being reached.
     */
    private ByteArrayOutputStream memoryOutputStream;


    /**
     * The output stream to which data will be written at any given time. This
     * will always be one of <code>memoryOutputStream</code> or
     * <code>diskOutputStream</code>.
     */
    private OutputStream currentOutputStream;


    /**
     * The file to which output will be directed if the threshold is exceeded.
     */
    private File outputFile;

    /**
     * The temporary file prefix.
     */
    private final String prefix;

    /**
     * The temporary file suffix.
     */
    private final String suffix;

    /**
     * The directory to use for temporary files.
     */
    private final File directory;

    
    /**
     * True when close() has been called successfully.
     */
    private boolean closed = false;

    // ----------------------------------------------------------- Constructors


    /**
     * Constructs an instance of this class which will trigger an event at the
     * specified threshold, and save data to a file beyond that point.
     *
     * @param threshold  The number of bytes at which to trigger an event.
     * @param outputFile The file to which data is saved beyond the threshold.
     */
    public DeferredFileOutputStream(int threshold, File outputFile)
    {
        this(threshold,  outputFile, null, null, null);
    }


    /**
     * Constructs an instance of this class which will trigger an event at the
     * specified threshold, and save data to a temporary file beyond that point.
     *
     * @param threshold  The number of bytes at which to trigger an event.
     * @param prefix Prefix to use for the temporary file.
     * @param suffix Suffix to use for the temporary file.
     * @param directory Temporary file directory.
     *
     * @since Commons IO 1.4
     */
    public DeferredFileOutputStream(int threshold, String prefix, String suffix, File directory)
    {
        this(threshold,   null, prefix, suffix, directory);
        if (prefix == null) {
            throw new IllegalArgumentException("Temporary file prefix is missing");
        }
    }

    /**
     * Constructs an instance of this class which will trigger an event at the
     * specified threshold, and save data either to a file beyond that point.
     * 
     * @param threshold  The number of bytes at which to trigger an event.
     * @param outputFile The file to which data is saved beyond the threshold.
     * @param prefix Prefix to use for the temporary file.
     * @param suffix Suffix to use for the temporary file.
     * @param directory Temporary file directory.
     */
    private DeferredFileOutputStream(int threshold, File outputFile, String prefix, String suffix, File directory) {
        super(threshold);
        this.outputFile = outputFile;

        memoryOutputStream = new ByteArrayOutputStream();
        currentOutputStream = memoryOutputStream;
        this.prefix = prefix;
        this.suffix = suffix;
        this.directory = directory;
    }


    // --------------------------------------- ThresholdingOutputStream methods


    /**
     * Returns the current output stream. This may be memory based or disk
     * based, depending on the current state with respect to the threshold.
     *
     * @return The underlying output stream.
     *
     * @exception IOException if an error occurs.
     */
    @Override
    protected OutputStream getStream() throws IOException
    {
        return currentOutputStream;
    }


    /**
     * Switches the underlying output stream from a memory based stream to one
     * that is backed by disk. This is the point at which we realise that too
     * much data is being written to keep in memory, so we elect to switch to
     * disk-based storage.
     *
     * @exception IOException if an error occurs.
     */
    @Override
    protected void thresholdReached() throws IOException
    {
        if (prefix != null) {
            outputFile = File.createTempFile(prefix, suffix, directory);
        }
        FileOutputStream fos = new FileOutputStream(outputFile);
        memoryOutputStream.writeTo(fos);
        currentOutputStream = fos;
        memoryOutputStream = null;
    }


    // --------------------------------------------------------- Public methods


    /**
     * Determines whether or not the data for this output stream has been
     * retained in memory.
     *
     * @return <code>true</code> if the data is available in memory;
     *         <code>false</code> otherwise.
     */
    public boolean isInMemory()
    {
        return (!isThresholdExceeded());
    }


    /**
     * Returns the data for this output stream as an array of bytes, assuming
     * that the data has been retained in memory. If the data was written to
     * disk, this method returns <code>null</code>.
     *
     * @return The data for this output stream, or <code>null</code> if no such
     *         data is available.
     */
    public byte[] getData()
    {
        if (memoryOutputStream != null)
        {
            return memoryOutputStream.toByteArray();
        }
        return null;
    }


    /**
     * Returns either the output file specified in the constructor or
     * the temporary file created or null.
     * <p>
     * If the constructor specifying the file is used then it returns that
     * same output file, even when threashold has not been reached.
     * <p>
     * If constructor specifying a temporary file prefix/suffix is used
     * then the temporary file created once the threashold is reached is returned
     * If the threshold was not reached then <code>null</code> is returned.
     *
     * @return The file for this output stream, or <code>null</code> if no such
     *         file exists.
     */
    public File getFile()
    {
        return outputFile;
    }
    
        
    /**
     * Closes underlying output stream, and mark this as closed
     *
     * @exception IOException if an error occurs.
     */
    @Override
    public void close() throws IOException
    {
        super.close();
        closed = true;
    }
    
    
    /**
     * Writes the data from this output stream to the specified output stream,
     * after it has been closed.
     *
     * @param out output stream to write to.
     * @exception IOException if this stream is not yet closed or an error occurs.
     */
    public void writeTo(OutputStream out) throws IOException 
    {
        // we may only need to check if this is closed if we are working with a file
        // but we should force the habit of closing wether we are working with
        // a file or memory.
        if (!closed)
        {
            throw new IOException("Stream not closed");
        }
        
        if(isInMemory())
        {
            memoryOutputStream.writeTo(out);
        }
        else
        {
            FileInputStream fis = new FileInputStream(outputFile);
            try {
                IOUtils.copy(fis, out);
            } finally {
                IOUtils.closeQuietly(fis);
            }
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/710.java