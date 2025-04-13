error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/139.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/139.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/139.java
text:
```scala
public l@@ong bytesPastMark(FileMark mark)

/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package org.apache.cassandra.io.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedFileDataInput extends AbstractDataInput implements FileDataInput
{
    private final MappedByteBuffer buffer;
    private final String filename;
    private int position;

    public MappedFileDataInput(FileInputStream stream, String filename, int position) throws IOException
    {
        FileChannel channel = stream.getChannel();
        buffer = channel.map(FileChannel.MapMode.READ_ONLY, position, channel.size());
        this.filename = filename;
        this.position = position;
    }

    public MappedFileDataInput(MappedByteBuffer buffer, String filename, int position)
    {
        assert buffer != null;
        this.buffer = buffer;
        this.filename = filename;
        this.position = position;
    }

    // don't make this public, this is only for seeking WITHIN the current mapped segment
    protected void seekInternal(int pos)
    {
        position = pos;
    }

    @Override
    protected int getPosition()
    {
        return position;
    }

    @Override
    public boolean markSupported()
    {
        return false;
    }

    public void reset(FileMark mark) throws IOException
    {
        assert mark instanceof MappedFileDataInputMark;
        seekInternal(((MappedFileDataInputMark) mark).position);
    }

    public FileMark mark()
    {
        return new MappedFileDataInputMark(position);
    }

    public int bytesPastMark(FileMark mark)
    {
        assert mark instanceof MappedFileDataInputMark;
        assert position >= ((MappedFileDataInputMark) mark).position;
        return position - ((MappedFileDataInputMark) mark).position;
    }

    public boolean isEOF() throws IOException
    {
        return position == buffer.capacity();
    }

    public long bytesRemaining() throws IOException
    {
        return buffer.capacity() - position;
    }

    public String getPath()
    {
        return filename;
    }

    public int read() throws IOException
    {
        if (isEOF())
            return -1;
        return buffer.get(position++) & 0xFF;
    }

    /**
     * Does the same thing as <code>readFully</code> do but without copying data (thread safe)
     * @param length length of the bytes to read
     * @return buffer with portion of file content
     * @throws IOException on any fail of I/O operation
     */
    public synchronized ByteBuffer readBytes(int length) throws IOException
    {
        int remaining = buffer.remaining() - position;

        assert length <= remaining
                : String.format("mmap segment underflow; remaining is %d but %d requested", remaining, length);

        ByteBuffer bytes = buffer.duplicate();
        bytes.position(buffer.position() + position).limit(buffer.position() + position + length);
        position += length;

        return bytes;
    }

    @Override
    public final void readFully(byte[] buffer) throws IOException
    {
        throw new UnsupportedOperationException("use readBytes instead");
    }

    @Override
    public final void readFully(byte[] buffer, int offset, int count) throws IOException
    {
        throw new UnsupportedOperationException("use readBytes instead");
    }

    public int skipBytes(int n) throws IOException
    {
        assert n >= 0 : "skipping negative bytes is illegal: " + n;
        if (n == 0)
            return 0;
        int oldPosition = position;
        assert ((long)oldPosition) + n <= Integer.MAX_VALUE;
        position = Math.min(buffer.capacity(), position + n);
        return position - oldPosition;
    }

    private static class MappedFileDataInputMark implements FileMark
    {
        int position;

        MappedFileDataInputMark(int position)
        {
            this.position = position;
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
               "filename='" + filename + "'" +
               ", position=" + position +
               ")";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/139.java