error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6204.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6204.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6204.java
text:
```scala
B@@yteArrayOutputStream baos = new ByteArrayOutputStream(1000);

/***************************************
 *                                     *
 *  JBoss: The OpenSource J2EE WebOS   *
 *                                     *
 *  Distributable under LGPL license.  *
 *  See terms of license at gnu.org.   *
 *                                     *
 ***************************************/
package org.jboss.invocation.trunk.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.MarshalledObject;

import org.jboss.logging.Logger;

/**
 * This is the response message that will be sent of the socket
 *
 * @author    <a href="mailto:hiram.chirino@jboss.org">Hiram Chirino</a>
 */
public class TrunkResponse
{
   final static private Logger log = Logger.getLogger(TrunkResponse.class);

   final static byte RESULT_VOID = 1;
   final static byte RESULT_OBJECT = 2;
   final static byte RESULT_EXCEPTION = 3;
   private static ClassLoader classLoader = TunkRequest.class.getClassLoader();

   public Integer correlationRequestId;
   public MarshalledObject result;
   public MarshalledObject exception;

   public TrunkResponse()
   {
   }
   public TrunkResponse(TunkRequest request)
   {
      correlationRequestId = request.requestId;
   }

   public Object evalThrowsException() throws Exception
   {
      if (exception != null)
      {
         Throwable e = (Throwable)exception.get();
         if (e instanceof Exception)
         {
            throw (Exception) e;
         }
         else
         {
            log.debug("Protocol violation: unexpected exception found in response: ", e);
            throw new IOException("Protocol violation: unexpected exception found in response: " + exception);
         }
      }
      return result.get();
   }

   public Object evalThrowsThrowable() throws Throwable
   {
      if (exception != null)
      {
         throw (Throwable)exception.get();
      }
      return result.get();
   }

   public byte[] serialize() throws IOException
   {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream out = new CustomObjectOutputStream(baos);

      if (correlationRequestId == null)
      {
         out.writeByte(0);
      }
      else
      {
         out.writeByte(1);
         out.writeInt(correlationRequestId.intValue());
      }

      if (exception != null)
      {
         out.writeByte(RESULT_EXCEPTION);
         out.writeObject(exception);
      }
      else if (result == null)
      {
         out.writeByte(RESULT_VOID);
      }
      else
      {
         out.writeByte(RESULT_OBJECT);
         out.writeObject(result);
      }
      out.close();
      return baos.toByteArray();
   }

   public void deserialize(byte data[]) throws IOException, ClassNotFoundException
   {
      ByteArrayInputStream bais = new ByteArrayInputStream(data);
      ObjectInputStream in = new CustomObjectInputStreamWithClassloader(bais, classLoader);

      if (in.readByte() == 1)
         correlationRequestId = new Integer(in.readInt());

      byte responseType = in.readByte();
      switch (responseType)
      {
         case RESULT_VOID :
            result = null;
            exception = null;
            break;
         case RESULT_EXCEPTION :
            result = null;
            exception = (MarshalledObject) in.readObject();
            break;
         case RESULT_OBJECT :
            exception = null;
            result = (MarshalledObject) in.readObject();
            break;
         default :
            throw new IOException("Protocol Error: Bad response type code '" + responseType + "' ");
      }

      in.close();
   }

   public String toString()
   {
      return "[correlationRequestId:" + correlationRequestId + ",result:" + result + ",exception:" + exception + "]";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6204.java