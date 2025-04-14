error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10168.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10168.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10168.java
text:
```scala
C@@hannelBuffer buffer = ChannelBuffers.dynamicBuffer(512, channel.getConfig().getBufferFactory());

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.transport.netty;

import org.elasticsearch.transport.NotSerializableTransportException;
import org.elasticsearch.transport.RemoteTransportException;
import org.elasticsearch.transport.TransportChannel;
import org.elasticsearch.util.io.ByteArrayDataOutputStream;
import org.elasticsearch.util.io.Streamable;
import org.elasticsearch.util.io.ThrowableObjectOutputStream;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;

import java.io.IOException;
import java.io.NotSerializableException;

import static org.elasticsearch.transport.Transport.Helper.*;

/**
 * @author kimchy (Shay Banon)
 */
public class NettyTransportChannel implements TransportChannel {

    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];

    private final NettyTransport transport;

    private final String action;

    private final Channel channel;

    private final long requestId;

    public NettyTransportChannel(NettyTransport transport, String action, Channel channel, long requestId) {
        this.transport = transport;
        this.action = action;
        this.channel = channel;
        this.requestId = requestId;
    }

    @Override public String action() {
        return this.action;
    }

    @Override public void sendResponse(Streamable message) throws IOException {
        ByteArrayDataOutputStream stream = ByteArrayDataOutputStream.Cached.cached();
        stream.write(LENGTH_PLACEHOLDER); // fake size
        stream.writeLong(requestId);
        byte status = 0;
        status = setResponse(status);
        stream.writeByte(status); // 0 for request, 1 for response.
        message.writeTo(stream);
        ChannelBuffer buffer = ChannelBuffers.wrappedBuffer(stream.copiedByteArray());
        buffer.setInt(0, buffer.writerIndex() - 4); // update real size.
        channel.write(buffer);
    }

    @Override public void sendResponse(Throwable error) throws IOException {
        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
        ChannelBufferOutputStream os = new ChannelBufferOutputStream(buffer);

        os.write(LENGTH_PLACEHOLDER);
        os.writeLong(requestId);

        byte status = 0;
        status = setResponse(status);
        status = setError(status);
        os.writeByte(status);

        // mark the buffer, so we can reset it when the exception is not serializable
        os.flush();
        buffer.markWriterIndex();
        try {
            RemoteTransportException tx = new RemoteTransportException(transport.nodeName(), transport.wrapAddress(channel.getLocalAddress()), action, error);
            ThrowableObjectOutputStream too = new ThrowableObjectOutputStream(os);
            too.writeObject(tx);
            too.close();
        } catch (NotSerializableException e) {
            buffer.resetWriterIndex();
            RemoteTransportException tx = new RemoteTransportException(transport.nodeName(), transport.wrapAddress(channel.getLocalAddress()), action, new NotSerializableTransportException(error));
            ThrowableObjectOutputStream too = new ThrowableObjectOutputStream(os);
            too.writeObject(tx);
            too.close();
        }

        buffer.setInt(0, buffer.writerIndex() - 4); // update real size.
        channel.write(buffer);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10168.java