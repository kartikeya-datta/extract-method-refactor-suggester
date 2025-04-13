error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/960.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/960.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/960.java
text:
```scala
public M@@essage getMessage(Integer version)

/**
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
 */

package org.apache.cassandra.db;

import java.io.*;
import java.util.Arrays;

import org.apache.cassandra.dht.AbstractBounds;
import org.apache.cassandra.io.ICompactSerializer2;
import org.apache.cassandra.io.util.DataOutputBuffer;
import org.apache.cassandra.net.Message;
import org.apache.cassandra.net.MessageProducer;
import org.apache.cassandra.service.StorageService;
import org.apache.cassandra.thrift.IndexClause;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.utils.FBUtilities;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TSerializer;
import org.apache.cassandra.thrift.TBinaryProtocol;

public class IndexScanCommand implements MessageProducer
{
    private static final IndexScanCommandSerializer serializer = new IndexScanCommandSerializer();

    public final String keyspace;
    public final String column_family;
    public final IndexClause index_clause;
    public final SlicePredicate predicate;
    public final AbstractBounds range;

    public IndexScanCommand(String keyspace, String column_family, IndexClause index_clause, SlicePredicate predicate, AbstractBounds range)
    {

        this.keyspace = keyspace;
        this.column_family = column_family;
        this.index_clause = index_clause;
        this.predicate = predicate;
        this.range = range;
    }

    public Message getMessage(int version)
    {
        DataOutputBuffer dob = new DataOutputBuffer();
        try
        {
            serializer.serialize(this, dob);
        }
        catch (IOException e)
        {
            throw new IOError(e);
        }
        return new Message(FBUtilities.getLocalAddress(),
                           StorageService.Verb.INDEX_SCAN,
                           Arrays.copyOf(dob.getData(), dob.getLength()),
                           version);
    }

    public static IndexScanCommand read(Message message) throws IOException
    {
        byte[] bytes = message.getMessageBody();
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        return serializer.deserialize(new DataInputStream(bis));
    }

    private static class IndexScanCommandSerializer implements ICompactSerializer2<IndexScanCommand>
    {
        public void serialize(IndexScanCommand o, DataOutput out) throws IOException
        {
            out.writeUTF(o.keyspace);
            out.writeUTF(o.column_family);
            TSerializer ser = new TSerializer(new TBinaryProtocol.Factory());
            FBUtilities.serialize(ser, o.index_clause, out);
            FBUtilities.serialize(ser, o.predicate, out);
            AbstractBounds.serializer().serialize(o.range, out);
        }

        public IndexScanCommand deserialize(DataInput in) throws IOException
        {
            String keyspace = in.readUTF();
            String columnFamily = in.readUTF();

            TDeserializer dser = new TDeserializer(new TBinaryProtocol.Factory());
            IndexClause indexClause = new IndexClause();
            FBUtilities.deserialize(dser, indexClause, in);
            SlicePredicate predicate = new SlicePredicate();
            FBUtilities.deserialize(dser, predicate, in);
            AbstractBounds range = AbstractBounds.serializer().deserialize(in);

            return new IndexScanCommand(keyspace, columnFamily, indexClause, predicate, range);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/960.java