error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1643.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1643.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1643.java
text:
```scala
b@@yte[] bytes = message.getMessageBody();

package org.apache.cassandra.db;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.util.Arrays;

import org.apache.cassandra.io.ICompactSerializer;
import org.apache.cassandra.io.DataOutputBuffer;
import org.apache.cassandra.io.DataInputBuffer;
import org.apache.cassandra.net.Message;
import org.apache.cassandra.service.StorageService;

public class RangeCommand
{
    private static RangeCommandSerializer serializer = new RangeCommandSerializer();

    public final String table;
    public final String startWith;
    public final String stopAt;
    public final int maxResults;

    public RangeCommand(String table, String startWith, String stopAt, int maxResults)
    {
        this.table = table;
        this.startWith = startWith;
        this.stopAt = stopAt;
        this.maxResults = maxResults;
    }

    public Message getMessage() throws IOException
    {
        DataOutputBuffer dob = new DataOutputBuffer();
        serializer.serialize(this, dob);
        return new Message(StorageService.getLocalStorageEndPoint(),
                           StorageService.readStage_,
                           StorageService.rangeVerbHandler_,
                           Arrays.copyOf(dob.getData(), dob.getLength()));
    }

    public static RangeCommand read(Message message) throws IOException
    {
        byte[] bytes = (byte[]) message.getMessageBody()[0];
        DataInputBuffer dib = new DataInputBuffer();
        dib.reset(bytes, bytes.length);
        return serializer.deserialize(new DataInputStream(dib));
    }
}

class RangeCommandSerializer implements ICompactSerializer<RangeCommand>
{
    public void serialize(RangeCommand command, DataOutputStream dos) throws IOException
    {
        dos.writeUTF(command.table);
        dos.writeUTF(command.startWith);
        dos.writeUTF(command.stopAt);
        dos.writeInt(command.maxResults);
    }

    public RangeCommand deserialize(DataInputStream dis) throws IOException
    {
        return new RangeCommand(dis.readUTF(), dis.readUTF(), dis.readUTF(), dis.readInt());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1643.java