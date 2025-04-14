error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8262.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8262.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8262.java
text:
```scala
t@@runkRamp.setTrunkListener(optimizedInvoker);

/***************************************
 *                                     *
 *  JBoss: The OpenSource J2EE WebOS   *
 *                                     *
 *  Distributable under LGPL license.  *
 *  See terms of license at gnu.org.   *
 *                                     *
 ***************************************/

package org.jboss.invocation.trunk.server.nbio;


import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import javax.resource.spi.work.WorkManager;
import org.jboss.invocation.trunk.client.CommTrunkRamp;
import org.jboss.invocation.trunk.client.ConnectionManager;
import org.jboss.invocation.trunk.client.nbio.NonBlockingSocketTrunk;
import org.jboss.invocation.trunk.client.nbio.SelectionAction;
import org.jboss.invocation.trunk.client.nbio.SelectorManager;
import org.jboss.invocation.trunk.server.IServer;
import org.jboss.invocation.trunk.server.TrunkInvoker;
import org.jboss.logging.Logger;

/**
 * Provides a Non Blocking implemenation of the IServer interface.
 * 
 * Sets up a non-blocking ServerSocketChannel to accept NBIO client connections.
 *
 * @author    <a href="mailto:hiram.chirino@jboss.org">Hiram Chirino</a>
 */
public final class NonBlockingServer implements IServer
{

   /**
    * logger instance.
    */
   final static private Logger log = Logger.getLogger(NonBlockingServer.class);

   /**
    * If the TcpNoDelay option should be used on the socket.
    */
   private boolean enableTcpNoDelay = false;

   /**
    * The listening socket that receives incomming connections
    * for servicing.
    */
   private ServerSocketChannel serverSocket;

   /**
    * Manages the selector.
    */
   SelectorManager selectorManager;

   TrunkInvoker optimizedInvoker;

   private WorkManager workManager;

   public ServerSocket bind(
      TrunkInvoker optimizedInvoker,
      InetAddress address,
      int port,
      int connectBackLog,
      boolean enableTcpNoDelay,
      WorkManager workManager)
      throws IOException
   {
      this.optimizedInvoker = optimizedInvoker;
      this.enableTcpNoDelay = enableTcpNoDelay;
      this.workManager = workManager;

      selectorManager = SelectorManager.getInstance(ConnectionManager.oiThreadGroup);

      // Create a new non-blocking ServerSocketChannel, bind it to port 8000, and
      // register it with the Selector
      serverSocket = ServerSocketChannel.open(); // Open channel
      serverSocket.configureBlocking(false); // Non-blocking
      serverSocket.socket().bind(new InetSocketAddress(address, port), connectBackLog); // Bind to port      
      //serverSocket.socket().setSoTimeout(SO_TIMEOUT);
      serverSocket.register(selectorManager.getSelector(), SelectionKey.OP_ACCEPT, new AcceptConnectionAction());

      return serverSocket.socket();
   }

   public void start() throws IOException
   {
      selectorManager.start();
   }

   public void stop() throws IOException
   {
      selectorManager.stop();
   }

   class AcceptConnectionAction implements SelectionAction
   {

      public void service(SelectionKey selection)
      {
         // Activity on the ServerSocketChannel means a client
         // is trying to connect to the server.
         if (!selection.isAcceptable())
            return;

         ServerSocketChannel server = (ServerSocketChannel) selection.channel();

         try
         {
            if (log.isTraceEnabled())
               log.trace("Accepting client connection");

            // Accept the client connection
            SocketChannel client = server.accept();
            NonBlockingSocketTrunk trunk = new NonBlockingSocketTrunk(client, selectorManager.getSelector());
            CommTrunkRamp trunkRamp = new CommTrunkRamp(trunk, workManager);
            trunk.setCommTrunkRamp(trunkRamp);
            trunkRamp.setTrunkListner(optimizedInvoker);
            trunk.start();

         }
         catch (IOException e)
         {
            log.debug("Error establishing connection with client: ", e);
         }
      }
   }

}
// vim:expandtab:tabstop=3:shiftwidth=3
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8262.java