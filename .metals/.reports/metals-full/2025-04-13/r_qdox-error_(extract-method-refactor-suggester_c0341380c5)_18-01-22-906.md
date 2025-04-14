error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/964.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/964.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/964.java
text:
```scala
public M@@essage getMessage(Integer version)

package org.apache.cassandra.streaming;
/*
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


import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.cassandra.dht.AbstractBounds;
import org.apache.cassandra.dht.Range;
import org.apache.cassandra.io.ICompactSerializer;
import org.apache.cassandra.net.CompactEndpointSerializationHelper;
import org.apache.cassandra.net.Message;
import org.apache.cassandra.net.MessageProducer;
import org.apache.cassandra.net.MessagingService;
import org.apache.cassandra.service.StorageService;
import org.apache.cassandra.utils.FBUtilities;

/**
* This class encapsulates the message that needs to be sent to nodes
* that handoff data. The message contains information about ranges
* that need to be transferred and the target node.
* 
* If a file is specified, ranges and table will not. vice-versa should hold as well.
*/
class StreamRequestMessage implements MessageProducer
{
    private static ICompactSerializer<StreamRequestMessage> serializer_;
    static
    {
        serializer_ = new StreamRequestMessageSerializer();
    }

    protected static ICompactSerializer<StreamRequestMessage> serializer()
    {
        return serializer_;
    }

    protected final long sessionId;
    protected final InetAddress target;
    
    // if this is specified, ranges and table should not be.
    protected final PendingFile file;
    
    // if these are specified, file shoud not be.
    protected final Collection<Range> ranges;
    protected final String table;
    protected final OperationType type;

    StreamRequestMessage(InetAddress target, Collection<Range> ranges, String table, long sessionId, OperationType type)
    {
        this.target = target;
        this.ranges = ranges;
        this.table = table;
        this.sessionId = sessionId;
        this.type = type;
        file = null;
    }

    StreamRequestMessage(InetAddress target, PendingFile file, long sessionId)
    {
        this.target = target;
        this.file = file;
        this.sessionId = sessionId;
        this.type = file.type;
        ranges = null;
        table = null;
    }
    
    public Message getMessage(int version)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try
        {
            StreamRequestMessage.serializer().serialize(this, dos, version);
        }
        catch (IOException e)
        {
            throw new IOError(e);
        }
        return new Message(FBUtilities.getLocalAddress(), StorageService.Verb.STREAM_REQUEST, bos.toByteArray(), version);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder("");
        if (file == null)
        {
            sb.append(table);
            sb.append("@");
            sb.append(target);
            sb.append("------->");
            for ( Range range : ranges )
            {
                sb.append(range);
                sb.append(" ");
            }
            sb.append(type);
        }
        else
        {
            sb.append(file.toString());
        }
        return sb.toString();
    }

    private static class StreamRequestMessageSerializer implements ICompactSerializer<StreamRequestMessage>
    {
        public void serialize(StreamRequestMessage srm, DataOutputStream dos, int version) throws IOException
        {
            dos.writeLong(srm.sessionId);
            CompactEndpointSerializationHelper.serialize(srm.target, dos);
            if (srm.file != null)
            {
                dos.writeBoolean(true);
                PendingFile.serializer().serialize(srm.file, dos, version);
            }
            else
            {
                dos.writeBoolean(false);
                dos.writeUTF(srm.table);
                dos.writeInt(srm.ranges.size());
                for (Range range : srm.ranges)
                {
                    AbstractBounds.serializer().serialize(range, dos);
                }
                if (version > MessagingService.VERSION_07)
                    dos.writeUTF(srm.type.name());
            }
        }

        public StreamRequestMessage deserialize(DataInputStream dis, int version) throws IOException
        {
            long sessionId = dis.readLong();
            InetAddress target = CompactEndpointSerializationHelper.deserialize(dis);
            boolean singleFile = dis.readBoolean();
            if (singleFile)
            {
                PendingFile file = PendingFile.serializer().deserialize(dis, version);
                return new StreamRequestMessage(target, file, sessionId);
            }
            else
            {
                String table = dis.readUTF();
                int size = dis.readInt();
                List<Range> ranges = (size == 0) ? null : new ArrayList<Range>();
                for( int i = 0; i < size; ++i )
                {
                    ranges.add((Range) AbstractBounds.serializer().deserialize(dis));
                }
                OperationType type = OperationType.RESTORE_REPLICA_COUNT;
                if (version > MessagingService.VERSION_07)
                    type = OperationType.valueOf(dis.readUTF());
                return new StreamRequestMessage(target, ranges, table, sessionId, type);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/964.java