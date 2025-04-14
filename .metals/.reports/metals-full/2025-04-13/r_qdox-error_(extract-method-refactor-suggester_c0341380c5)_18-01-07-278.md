error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1642.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1642.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1642.java
text:
```scala
M@@essage message = new Message(StorageService.getLocalStorageEndPoint(), StorageService.readStage_, StorageService.touchVerbHandler_, bos.toByteArray());

package org.apache.cassandra.db;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.xml.bind.annotation.XmlElement;

import org.apache.cassandra.io.ICompactSerializer;
import org.apache.cassandra.net.Message;
import org.apache.cassandra.service.StorageService;


public class TouchMessage
{

private static ICompactSerializer<TouchMessage> serializer_;	
	
    static
    {
        serializer_ = new TouchMessageSerializer();
    }

    static ICompactSerializer<TouchMessage> serializer()
    {
        return serializer_;
    }
    
    public static Message makeTouchMessage(TouchMessage touchMessage) throws IOException
    {
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream( bos );
        TouchMessage.serializer().serialize(touchMessage, dos);
        Message message = new Message(StorageService.getLocalStorageEndPoint(), StorageService.readStage_, StorageService.touchVerbHandler_, new Object[]{bos.toByteArray()});         
        return message;
    }
    
    @XmlElement(name="Table")
    private String table_;
    
    @XmlElement(name="Key")
    private String key_;
    
    @XmlElement(name="fData")
    private boolean fData_ = true;
        
    private TouchMessage()
    {
    }
    
    public TouchMessage(String table, String key)
    {
        table_ = table;
        key_ = key;
    }

    public TouchMessage(String table, String key, boolean fData)
    {
        table_ = table;
        key_ = key;
        fData_ = fData;
    }
    

    String table()
    {
        return table_;
    }
    
    String key()
    {
        return key_;
    }

    public boolean isData()
    {
    	return fData_;
    }
}

class TouchMessageSerializer implements ICompactSerializer<TouchMessage>
{
	public void serialize(TouchMessage tm, DataOutputStream dos) throws IOException
	{
		dos.writeUTF(tm.table());
		dos.writeUTF(tm.key());
		dos.writeBoolean(tm.isData());
	}
	
    public TouchMessage deserialize(DataInputStream dis) throws IOException
    {
		String table = dis.readUTF();
		String key = dis.readUTF();
		boolean fData = dis.readBoolean();
		TouchMessage tm = new TouchMessage( table, key, fData);
    	return tm;
    }
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1642.java