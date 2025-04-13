error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1956.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1956.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1956.java
text:
```scala
i@@nput.readInt(); // Read total size

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

package org.apache.cassandra.streaming;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.gms.Gossiper;
import org.apache.cassandra.io.compress.CompressedRandomAccessReader;
import org.apache.cassandra.io.util.RandomAccessReader;
import org.apache.cassandra.io.util.FileUtils;
import org.apache.cassandra.net.Header;
import org.apache.cassandra.net.Message;
import org.apache.cassandra.net.MessagingService;
import org.apache.cassandra.net.OutboundTcpConnection;
import org.apache.cassandra.service.StorageService;
import org.apache.cassandra.utils.ByteBufferUtil;
import org.apache.cassandra.utils.Pair;
import org.apache.cassandra.utils.Throttle;
import org.apache.cassandra.utils.WrappedRunnable;

import com.ning.compress.lzf.LZFOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileStreamTask extends WrappedRunnable
{
    private static Logger logger = LoggerFactory.getLogger(FileStreamTask.class);
    
    public static final int CHUNK_SIZE = 64 * 1024;
    // around 10 minutes at the default rpctimeout
    public static final int MAX_CONNECT_ATTEMPTS = 8;

    protected final StreamHeader header;
    protected final InetAddress to;

    // communication socket
    private Socket socket;
    // socket's output/input stream
    private OutputStream output;
    private OutputStream compressedoutput;
    private DataInputStream input;
    // allocate buffer to use for transfers only once
    private final byte[] transferBuffer = new byte[CHUNK_SIZE];
    // outbound global throughput limiter
    private final Throttle throttle;
    private final StreamReplyVerbHandler handler = new StreamReplyVerbHandler();

    public FileStreamTask(StreamHeader header, InetAddress to)
    {
        this.header = header;
        this.to = to;
        this.throttle = new Throttle(toString(), new Throttle.ThroughputFunction()
        {
            /** @return Instantaneous throughput target in bytes per millisecond. */
            public int targetThroughput()
            {
                if (DatabaseDescriptor.getStreamThroughputOutboundMegabitsPerSec() < 1)
                    // throttling disabled
                    return 0;
                // total throughput
                int totalBytesPerMS = DatabaseDescriptor.getStreamThroughputOutboundMegabitsPerSec() * 1024 * 1024 / 8 / 1000;
                // per stream throughput (target bytes per MS)
                return totalBytesPerMS / Math.max(1, MessagingService.instance().getActiveStreamsOutbound());
            }
        });
    }
    
    public void runMayThrow() throws IOException
    {
        try
        {
            connectAttempt();
            // successfully connected: stream.
            // (at this point, if we fail, it is the receiver's job to re-request)
            stream();
            if (StreamOutSession.get(to, header.sessionId).getFiles().size() == 0)
            {
                // we are the last of our kind, receive the final confirmation before closing
                receiveReply();
                logger.info("Finished streaming session to {}", to);
            }
        }
        finally
        {
            try
            {
                close();
            }
            catch (IOException e)
            {
                if (logger.isDebugEnabled())
                    logger.debug("error closing socket", e);
            }
        }
        if (logger.isDebugEnabled())
            logger.debug("Done streaming " + header.file);
    }

    /**
     * Stream file by it's sections specified by this.header
     * @throws IOException on any I/O error
     */
    private void stream() throws IOException
    {
        ByteBuffer HeaderBuffer = MessagingService.instance().constructStreamHeader(header, false, Gossiper.instance.getVersion(to));
        // write header (this should not be compressed for compatibility with other messages)
        output.write(ByteBufferUtil.getArray(HeaderBuffer));

        if (header.file == null)
            return;

        // TODO just use a raw RandomAccessFile since we're managing our own buffer here
        RandomAccessReader file = (header.file.sstable.compression) // try to skip kernel page cache if possible
                                ? CompressedRandomAccessReader.open(header.file.getFilename(), header.file.sstable.getCompressionMetadata(), true)
                                : RandomAccessReader.open(new File(header.file.getFilename()), true);

        // setting up data compression stream
        compressedoutput = new LZFOutputStream(output);

        MessagingService.instance().incrementActiveStreamsOutbound();
        try
        {
            // stream each of the required sections of the file
            for (Pair<Long, Long> section : header.file.sections)
            {
                // seek to the beginning of the section
                file.seek(section.left);

                // length of the section to stream
                long length = section.right - section.left;
                // tracks write progress
                long bytesTransferred = 0;

                while (bytesTransferred < length)
                {
                    long lastWrite = write(file, length, bytesTransferred);
                    bytesTransferred += lastWrite;
                    // store streaming progress
                    header.file.progress += lastWrite;
                }

                // make sure that current section is send
                compressedoutput.flush();

                if (logger.isDebugEnabled())
                    logger.debug("Bytes transferred " + bytesTransferred + "/" + header.file.size);
            }
            // receive reply confirmation
            receiveReply();
        }
        finally
        {
            MessagingService.instance().decrementActiveStreamsOutbound();

            // no matter what happens close file
            FileUtils.closeQuietly(file);
        }
    }

    private void receiveReply() throws IOException
    {
        MessagingService.validateMagic(input.readInt());
        int msheader = input.readInt();
        assert MessagingService.getBits(msheader, 3, 1) == 0 : "Stream received before stream reply";
        int version = MessagingService.getBits(msheader, 15, 8);

        int totalSize = input.readInt();
        String id = input.readUTF();
        Header header = Header.serializer().deserialize(input, version);

        int bodySize = input.readInt();
        byte[] body = new byte[bodySize];
        input.readFully(body);
        Message message = new Message(header, body, version);
        assert message.getVerb() == StorageService.Verb.STREAM_REPLY : "Non-reply message received on stream socket";
        handler.doVerb(message, id);
    }

    /**
     * Sequentially read bytes from the file and write them to the output stream
     *
     * @param reader The file reader to read from
     * @param length The full length that should be transferred
     * @param bytesTransferred Number of bytes remaining to transfer
     *
     * @return Number of bytes transferred
     *
     * @throws IOException on any I/O error
     */
    protected long write(RandomAccessReader reader, long length, long bytesTransferred) throws IOException
    {
        int toTransfer = (int) Math.min(CHUNK_SIZE, length - bytesTransferred);

        reader.readFully(transferBuffer, 0, toTransfer);
        compressedoutput.write(transferBuffer, 0, toTransfer);
        throttle.throttleDelta(toTransfer);

        return toTransfer;
    }

    /**
     * Connects to the destination, with backoff for failed attempts.
     * TODO: all nodes on a cluster must currently use the same storage port
     * @throws IOException If all attempts fail.
     */
    private void connectAttempt() throws IOException
    {
        int attempts = 0;
        while (true)
        {
            try
            {
                socket = MessagingService.instance().getConnectionPool(to).newSocket();
                output = socket.getOutputStream();
                input = new DataInputStream(socket.getInputStream());
                break;
            }
            catch (IOException e)
            {
                if (++attempts >= MAX_CONNECT_ATTEMPTS)
                    throw e;

                long waitms = DatabaseDescriptor.getRpcTimeout() * (long)Math.pow(2, attempts);
                logger.warn("Failed attempt " + attempts + " to connect to " + to + " to stream " + header.file + ". Retrying in " + waitms + " ms. (" + e + ")");
                try
                {
                    Thread.sleep(waitms);
                }
                catch (InterruptedException wtf)
                {
                    throw new RuntimeException(wtf);
                }
            }
        }
    }

    protected void close() throws IOException
    {
        output.close();
    }

    public String toString()
    {
        return String.format("FileStreamTask(session=%s, to=%s)", header.sessionId, to);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1956.java