error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14346.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14346.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14346.java
text:
```scala
protected v@@oid resetByteCount()

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

import java.io.IOException;
import java.io.OutputStream;


/**
 * An output stream which triggers an event when a specified number of bytes of
 * data have been written to it. The event can be used, for example, to throw
 * an exception if a maximum has been reached, or to switch the underlying
 * stream type when the threshold is exceeded.
 * <p>
 * This class overrides all <code>OutputStream</code> methods. However, these
 * overrides ultimately call the corresponding methods in the underlying output
 * stream implementation.
 * <p>
 * NOTE: This implementation may trigger the event <em>before</em> the threshold
 * is actually reached, since it triggers when a pending write operation would
 * cause the threshold to be exceeded.
 *
 * @author <a href="mailto:martinc@apache.org">Martin Cooper</a>
 *
 * @version $Id$
 */
public abstract class ThresholdingOutputStream
    extends OutputStream
{

    // ----------------------------------------------------------- Data members


    /**
     * The threshold at which the event will be triggered.
     */
    private int threshold;


    /**
     * The number of bytes written to the output stream.
     */
    private long written;


    /**
     * Whether or not the configured threshold has been exceeded.
     */
    private boolean thresholdExceeded;


    // ----------------------------------------------------------- Constructors


    /**
     * Constructs an instance of this class which will trigger an event at the
     * specified threshold.
     *
     * @param threshold The number of bytes at which to trigger an event.
     */
    public ThresholdingOutputStream(int threshold)
    {
        this.threshold = threshold;
    }


    // --------------------------------------------------- OutputStream methods


    /**
     * Writes the specified byte to this output stream.
     *
     * @param b The byte to be written.
     *
     * @exception IOException if an error occurs.
     */
    public void write(int b) throws IOException
    {
        checkThreshold(1);
        getStream().write(b);
        written++;
    }


    /**
     * Writes <code>b.length</code> bytes from the specified byte array to this
     * output stream.
     *
     * @param b The array of bytes to be written.
     *
     * @exception IOException if an error occurs.
     */
    public void write(byte b[]) throws IOException
    {
        checkThreshold(b.length);
        getStream().write(b);
        written += b.length;
    }


    /**
     * Writes <code>len</code> bytes from the specified byte array starting at
     * offset <code>off</code> to this output stream.
     *
     * @param b   The byte array from which the data will be written.
     * @param off The start offset in the byte array.
     * @param len The number of bytes to write.
     *
     * @exception IOException if an error occurs.
     */
    public void write(byte b[], int off, int len) throws IOException
    {
        checkThreshold(len);
        getStream().write(b, off, len);
        written += len;
    }


    /**
     * Flushes this output stream and forces any buffered output bytes to be
     * written out.
     *
     * @exception IOException if an error occurs.
     */
    public void flush() throws IOException
    {
        getStream().flush();
    }


    /**
     * Closes this output stream and releases any system resources associated
     * with this stream.
     *
     * @exception IOException if an error occurs.
     */
    public void close() throws IOException
    {
        try
        {
            flush();
        }
        catch (IOException ignored)
        {
            // ignore
        }
        getStream().close();
    }


    // --------------------------------------------------------- Public methods


    /**
     * Returns the threshold, in bytes, at which an event will be triggered.
     *
     * @return The threshold point, in bytes.
     */
    public int getThreshold()
    {
        return threshold;
    }


    /**
     * Returns the number of bytes that have been written to this output stream.
     *
     * @return The number of bytes written.
     */
    public long getByteCount()
    {
        return written;
    }


    /**
     * Determines whether or not the configured threshold has been exceeded for
     * this output stream.
     *
     * @return <code>true</code> if the threshold has been reached;
     *         <code>false</code> otherwise.
     */
    public boolean isThresholdExceeded()
    {
        return (written > threshold);
    }


    // ------------------------------------------------------ Protected methods


    /**
     * Checks to see if writing the specified number of bytes would cause the
     * configured threshold to be exceeded. If so, triggers an event to allow
     * a concrete implementation to take action on this.
     *
     * @param count The number of bytes about to be written to the underlying
     *              output stream.
     *
     * @exception IOException if an error occurs.
     */
    protected void checkThreshold(int count) throws IOException
    {
        if (!thresholdExceeded && (written + count > threshold))
        {
            thresholdExceeded = true;
            thresholdReached();
        }
    }

    /**
     * Resets the byteCount to zero.  You can call this from 
     * {@link #thresholdReached()} if you want the event to be triggered again. 
     */
    protected void reset() 
    {
        this.thresholdExceeded = false;
        this.written = 0;
    }

    // ------------------------------------------------------- Abstract methods


    /**
     * Returns the underlying output stream, to which the corresponding
     * <code>OutputStream</code> methods in this class will ultimately delegate.
     *
     * @return The underlying output stream.
     *
     * @exception IOException if an error occurs.
     */
    protected abstract OutputStream getStream() throws IOException;


    /**
     * Indicates that the configured threshold has been reached, and that a
     * subclass should take whatever action necessary on this event. This may
     * include changing the underlying output stream.
     *
     * @exception IOException if an error occurs.
     */
    protected abstract void thresholdReached() throws IOException;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14346.java