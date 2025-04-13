error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3382.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3382.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3382.java
text:
```scala
l@@ogger_.trace("Size of Gossip packet " + data.length);

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

package org.apache.cassandra.net;

import java.net.SocketAddress;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.cassandra.net.io.ProtocolState;
import org.apache.cassandra.net.sink.SinkManager;
import org.apache.cassandra.utils.BasicUtilities;
import org.apache.cassandra.utils.LogUtil;
import org.apache.log4j.Logger;
import org.apache.cassandra.concurrent.*;
import org.apache.cassandra.utils.*;

/**
 * Author : Avinash Lakshman ( alakshman@facebook.com) & Prashant Malik ( pmalik@facebook.com )
 */

public class UdpConnection extends SelectionKeyHandler
{
    private static Logger logger_ = Logger.getLogger(UdpConnection.class);
    private static final int BUFFER_SIZE = 4096;
    private static final int protocol_ = 0xBADBEEF;
    
    private DatagramChannel socketChannel_;
    private SelectionKey key_;    
    private EndPoint localEndPoint_;
    
    public void init() throws IOException
    {
        socketChannel_ = DatagramChannel.open();
        socketChannel_.socket().setReuseAddress(true);
        socketChannel_.configureBlocking(false);        
    }
    
    public void init(int port) throws IOException
    {
        // TODO: get TCP port from config and add one.
        localEndPoint_ = new EndPoint(port);
        socketChannel_ = DatagramChannel.open();
        socketChannel_.socket().bind(localEndPoint_.getInetAddress());
        socketChannel_.socket().setReuseAddress(true);
        socketChannel_.configureBlocking(false);        
        key_ = SelectorManager.getUdpSelectorManager().register(socketChannel_, this, SelectionKey.OP_READ);
    }
    
    public boolean write(Message message, EndPoint to) throws IOException
    {
        boolean bVal = true;                       
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        Message.serializer().serialize(message, dos);        
        byte[] data = bos.toByteArray();
        if ( data.length > 0 )
        {  
            logger_.debug("Size of Gossip packet " + data.length);
            byte[] protocol = BasicUtilities.intToByteArray(protocol_);
            ByteBuffer buffer = ByteBuffer.allocate(data.length + protocol.length);
            buffer.put( protocol );
            buffer.put(data);
            buffer.flip();
            
            int n  = socketChannel_.send(buffer, to.getInetAddress());
            if ( n == 0 )
            {
                bVal = false;
            }
        }
        return bVal;
    }
    
    void close()
    {
        try
        {
            if ( socketChannel_ != null )
                socketChannel_.close();
        }
        catch ( IOException ex )
        {
            logger_.error( LogUtil.throwableToString(ex) );
        }
    }
    
    public DatagramChannel getDatagramChannel()
    {
        return socketChannel_;
    }
    
    private byte[] gobbleHeaderAndExtractBody(ByteBuffer buffer)
    {
        byte[] body = new byte[0];        
        byte[] protocol = new byte[4];
        buffer = buffer.get(protocol, 0, protocol.length);
        int value = BasicUtilities.byteArrayToInt(protocol);
        
        if ( protocol_ != value )
        {
            logger_.info("Invalid protocol header in the incoming message " + value);
            return body;
        }
        body = new byte[buffer.remaining()];
        buffer.get(body, 0, body.length);       
        return body;
    }
    
    public void read(SelectionKey key)
    {        
        key.interestOps( key.interestOps() & (~SelectionKey.OP_READ) );
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        try
        {
            SocketAddress sa = socketChannel_.receive(buffer);
            if ( sa == null )
            {
                logger_.debug("*** No datagram packet was available to be read ***");
                return;
            }            
            buffer.flip();
            
            byte[] bytes = gobbleHeaderAndExtractBody(buffer);
            if ( bytes.length > 0 )
            {
                DataInputStream dis = new DataInputStream( new ByteArrayInputStream(bytes) );
                Message message = Message.serializer().deserialize(dis);                
                if ( message != null )
                {                                        
                    MessagingService.receive(message);
                }
            }
        }
        catch ( IOException ioe )
        {
            logger_.warn(LogUtil.throwableToString(ioe));
        }
        finally
        {
            key.interestOps( key_.interestOps() | SelectionKey.OP_READ );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3382.java