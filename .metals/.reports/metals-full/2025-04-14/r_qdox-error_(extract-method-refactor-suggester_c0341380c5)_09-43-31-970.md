error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/792.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/792.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/792.java
text:
```scala
r@@amp.setTrunkListener(this);

/***************************************
 *                                     *
 *  JBoss: The OpenSource J2EE WebOS   *
 *                                     *
 *  Distributable under LGPL license.  *
 *  See terms of license at gnu.org.   *
 *                                     *
 ***************************************/

package org.jboss.invocation.trunk.client.nbio;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import javax.resource.spi.work.WorkManager;
import org.jboss.invocation.trunk.client.AbstractClient;
import org.jboss.invocation.trunk.client.CommTrunkRamp;
import org.jboss.invocation.trunk.client.ConnectionManager;
import org.jboss.invocation.trunk.client.ICommTrunk;
import org.jboss.invocation.trunk.client.ServerAddress;
import org.jboss.logging.Logger;

/**
 * Provides a Non-Blocking implemenation for clients running on >= 1.4 JVMs.
 * 
 * It connects to the server with a SocketChannel and sets up a Non-blocking
 * ICommTrunk to handle the IO protocol.
 * 
 * @author    <a href="mailto:hiram.chirino@jboss.org">Hiram Chirino</a>
 */
public class NonBlockingClient extends AbstractClient
{
   private final static Logger log = Logger.getLogger(NonBlockingClient.class);

   private NonBlockingSocketTrunk trunk;
   private SocketChannel socket;
   private ServerAddress serverAddress;
   private SelectorManager selectorManager;
   
   public void connect(ServerAddress serverAddress, 
                       ThreadGroup threadGroup) throws IOException
   {
      this.serverAddress = serverAddress;
      boolean tracing = log.isTraceEnabled();
      if (tracing)
         log.trace("Connecting to : " + serverAddress);

      selectorManager = SelectorManager.getInstance(ConnectionManager.oiThreadGroup);

      socket = SocketChannel.open();
      socket.configureBlocking(true); // Make the connect be blocking.
      socket.connect(new InetSocketAddress(serverAddress.address, serverAddress.port));
      socket.configureBlocking(false); // Make the connect be non-blocking.
            
     // serverAddress.address, serverAddress.port);
      socket.socket().setTcpNoDelay(serverAddress.enableTcpNoDelay);

      trunk = new NonBlockingSocketTrunk(socket, selectorManager.getSelector());
      CommTrunkRamp ramp = new CommTrunkRamp(trunk, workManager);
      trunk.setCommTrunkRamp(ramp);
      ramp.setTrunkListner(this);
      
      if (tracing)
         log.trace("Connection established.");
      
   }

   public ServerAddress getServerAddress()
   {
      return serverAddress;
   }

   public ICommTrunk getCommTrunk()
   {
      return trunk;
   }

   public void start()
   {
      trunk.start();
      selectorManager.start();
   }

   public void stop()
   {
      trunk.stop();
      selectorManager.stop();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/792.java