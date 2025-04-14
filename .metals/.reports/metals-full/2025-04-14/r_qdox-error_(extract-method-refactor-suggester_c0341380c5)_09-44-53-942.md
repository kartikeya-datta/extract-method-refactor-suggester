error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2518.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2518.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,18]

error in qdox parser
file content:
```java
offset: 18
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2518.java
text:
```scala
protected static E@@ndPoint sentinelLocalEndPoint_;

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


import java.io.IOException;
import java.io.Serializable;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;

import org.apache.cassandra.utils.FBUtilities;
import org.apache.cassandra.utils.LogUtil;
import org.apache.log4j.Logger;

/**
 * Author : Avinash Lakshman ( alakshman@facebook.com) & Prashant Malik ( pmalik@facebook.com )
 */

public class EndPoint implements Serializable, Comparable<EndPoint>
{
    // logging and profiling.
    private static Logger logger_ = Logger.getLogger(EndPoint.class);
    private static final long serialVersionUID = -4962625949179835907L;
    private static Map<CharBuffer, String> hostNames_ = new HashMap<CharBuffer, String>();

    // use as a kind of magic number to send ourselves a message indicating listening state
    protected static final int sentinelPort_ = 5555;
    public static EndPoint sentinelLocalEndPoint_;
    
    static
    {
        try
        {
            sentinelLocalEndPoint_ = new EndPoint(FBUtilities.getHostAddress(), EndPoint.sentinelPort_);
        }        
        catch ( IOException ex )
        {
            logger_.warn(LogUtil.throwableToString(ex));
        }
    }

    private String host_;
    private int port_;

    private transient InetSocketAddress ia_;

    public EndPoint(String host, int port)
    {
        assert host.matches("\\d+\\.\\d+\\.\\d+\\.\\d+") : host;
        host_ = host;
        port_ = port;
    }

    // create a local endpoint id
    public EndPoint(int port)
    {
        try
        {
            host_ = FBUtilities.getHostAddress();
        }
        catch (UnknownHostException e)
        {
            throw new RuntimeException(e);
        }
        port_ = port;
    }

    public String getHost()
    {
        return host_;
    }

    public int getPort()
    {
        return port_;
    }

    public void setPort(int port)
    {
        port_ = port;
    }

    public InetSocketAddress getInetAddress()
    {
        if (ia_ == null || ia_.isUnresolved())
        {
            ia_ = new InetSocketAddress(host_, port_);
        }
        return ia_;
    }

    public boolean equals(Object o)
    {
        if (!(o instanceof EndPoint))
            return false;

        EndPoint rhs = (EndPoint) o;
        return (host_.equals(rhs.host_) && port_ == rhs.port_);
    }

    public int hashCode()
    {
        return (host_ + port_).hashCode();
    }

    public int compareTo(EndPoint rhs)
    {
        return host_.compareTo(rhs.host_);
    }

    public String toString()
    {
        return (host_ + ":" + port_);
    }

    public static EndPoint fromString(String str)
    {
        String[] values = str.split(":");
        return new EndPoint(values[0], Integer.parseInt(values[1]));
    }

    public static byte[] toBytes(EndPoint ep)
    {
        ByteBuffer buffer = ByteBuffer.allocate(6);
        byte[] iaBytes = ep.getInetAddress().getAddress().getAddress();
        buffer.put(iaBytes);
        buffer.put(MessagingService.toByteArray((short) ep.getPort()));
        buffer.flip();
        return buffer.array();
    }

    public static EndPoint fromBytes(byte[] bytes)
    {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        System.arraycopy(bytes, 0, buffer.array(), 0, 4);
        byte[] portBytes = new byte[2];
        System.arraycopy(bytes, 4, portBytes, 0, portBytes.length);
        try
        {
            CharBuffer charBuffer = buffer.asCharBuffer();
            String host = hostNames_.get(charBuffer);
            if (host == null)
            {               
                host = InetAddress.getByAddress(buffer.array()).getHostAddress();              
                hostNames_.put(charBuffer, host);
            }
            int port = (int) MessagingService.byteArrayToShort(portBytes);
            return new EndPoint(host, port);
        }
        catch (UnknownHostException e)
        {
            throw new IllegalArgumentException(e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2518.java