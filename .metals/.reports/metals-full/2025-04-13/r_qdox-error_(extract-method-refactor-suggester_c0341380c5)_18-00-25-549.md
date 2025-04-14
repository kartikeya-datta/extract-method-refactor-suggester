error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3465.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3465.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3465.java
text:
```scala
S@@tring remoteHost = remoteAddress.getAddress().getHostAddress();

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

package org.apache.cassandra.net.io;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.io.*;

import org.apache.cassandra.db.Table;
import org.apache.log4j.Logger;

/**
 * Author : Avinash Lakshman ( alakshman@facebook.com) & Prashant Malik ( pmalik@facebook.com )
 */


class ContentStreamState extends StartState
{       
    private static Logger logger_ = Logger.getLogger(ContentStreamState.class);
    private static long count_ = 64*1024*1024;
    /* Return this byte array to exit event loop */
    private static byte[] bytes_ = new byte[1];
    private long bytesRead_ = 0L;
    private FileChannel fc_;
    private StreamContextManager.StreamContext streamContext_;
    private StreamContextManager.StreamStatus streamStatus_;
    
    ContentStreamState(TcpReader stream)
    {
        super(stream); 
        SocketChannel socketChannel = stream.getStream();
        InetSocketAddress remoteAddress = (InetSocketAddress)socketChannel.socket().getRemoteSocketAddress();
        String remoteHost = remoteAddress.getHostName();        
        streamContext_ = StreamContextManager.getStreamContext(remoteHost);   
        streamStatus_ = StreamContextManager.getStreamStatus(remoteHost);
    }
    
    private void createFileChannel() throws IOException
    {
        if ( fc_ == null )
        {
            if (logger_.isDebugEnabled())
              logger_.debug("Creating file for " + streamContext_.getTargetFile());
            FileOutputStream fos = new FileOutputStream( streamContext_.getTargetFile(), true );
            fc_ = fos.getChannel();            
        }
    }

    public byte[] read() throws IOException, ReadNotCompleteException
    {        
        SocketChannel socketChannel = stream_.getStream();
        InetSocketAddress remoteAddress = (InetSocketAddress)socketChannel.socket().getRemoteSocketAddress();
        String remoteHostIp = remoteAddress.getAddress().getHostAddress();
        createFileChannel();
        if ( streamContext_ != null )
        {  
            try
            {
                bytesRead_ += fc_.transferFrom(socketChannel, bytesRead_, ContentStreamState.count_);
                if ( bytesRead_ != streamContext_.getExpectedBytes() )
                    throw new ReadNotCompleteException("Specified number of bytes have not been read from the Socket Channel");
            }
            catch ( IOException ex )
            {
                /* Ask the source node to re-stream this file. */
                streamStatus_.setAction(StreamContextManager.StreamCompletionAction.STREAM);                
                handleStreamCompletion(remoteHostIp);
                /* Delete the orphaned file. */
                File file = new File(streamContext_.getTargetFile());
                file.delete();
                throw ex;
            }
            if ( bytesRead_ == streamContext_.getExpectedBytes() )
            {       
                if (logger_.isDebugEnabled())
                    logger_.debug("Removing stream context " + streamContext_);                 
                handleStreamCompletion(remoteHostIp);                              
                bytesRead_ = 0L;
                fc_.close();
                morphState();
            }                            
        }
        
        return new byte[0];
    }
    
    private void handleStreamCompletion(String remoteHost) throws IOException
    {
        /* 
         * Streaming is complete. If all the data that has to be received inform the sender via 
         * the stream completion callback so that the source may perform the requisite cleanup. 
        */
        IStreamComplete streamComplete = StreamContextManager.getStreamCompletionHandler(remoteHost);
        if ( streamComplete != null )
        {                    
            streamComplete.onStreamCompletion(remoteHost, streamContext_, streamStatus_);                    
        }
    }

    public void morphState() throws IOException
    {        
        /* We instantiate an array of size 1 so that we can exit the event loop of the read. */                
        StartState nextState = stream_.getSocketState(TcpReader.TcpReaderState.DONE);
        if ( nextState == null )
        {
            nextState = new DoneState(stream_, ContentStreamState.bytes_);
            stream_.putSocketState( TcpReader.TcpReaderState.DONE, nextState );
        }
        else
        {
            nextState.setContextData(ContentStreamState.bytes_);
        }
        stream_.morphState( nextState );  
    }
    
    public void setContextData(Object data)
    {
        throw new UnsupportedOperationException("This method is not supported in the ContentStreamState");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3465.java