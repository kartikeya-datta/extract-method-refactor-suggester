error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9638.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9638.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9638.java
text:
```scala
M@@ethod m = wmClass.getMethod("setMaxThreads", new Class[] {Integer.TYPE});

/***************************************
 *                                     *
 *  JBoss: The OpenSource J2EE WebOS   *
 *                                     *
 *  Distributable under LGPL license.  *
 *  See terms of license at gnu.org.   *
 *                                     *
 ***************************************/

package org.jboss.invocation.trunk.client;



import EDU.oswego.cs.dl.util.concurrent.ClockDaemon;
import EDU.oswego.cs.dl.util.concurrent.ThreadFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import javax.resource.spi.work.WorkManager;
import org.jboss.invocation.trunk.client.bio.BlockingClient;
import org.jboss.logging.Logger;
import java.lang.reflect.Method;

/**
 * This is a singleton class on that lives on a client which is used find existing 
 * connections that have been established to a sever.
 * 
 * This class also powers a clean-up thread which shuts down clients that have been
 * been inactive for 1 min.
 * 
 * This class is the one that makes the decicion if the client used will be Blocking or Non Blocking.
 * (currently based on jre version)
 * 
 * @author <a href="mailto:hiram.chirino@jboss.org">Hiram Chirino</a>
 */
public class ConnectionManager
{
   private final static Logger log = Logger.getLogger(ConnectionManager.class);
   private static ConnectionManager instance = new ConnectionManager();

   /** 
    * A map of all the connections that we have established due to 
    * a request this vm has made.
    * This field uses copy on write synchronization.
    */
   HashMap requestConnections = new HashMap();

   /**
    * Manages the thread that checks to see if connections have expired.
    */
   protected ClockDaemon clockDaemon = new ClockDaemon();

   /**
    * How often to check for expired connections
    */
   protected long checkPeriod = 1000 * 60;

   /**
    * How long before a connection expires
    */
   protected long expirationPeriod = 1000 * 60;

   /**
    * 
    */
   Object checkTaskId;

   private Class clientClass;

   /**
    * The thread group that will hold al the threads that
    * are servicing this protocol's sockets.
    */
   public ThreadGroup threadGroup = new ThreadGroup("Client Sockets");

   private WorkManager workManager;

   /**
    * We check the connections here.
    */
   class CheckTask implements Runnable
   {
      public void run()
      {
         Iterator i = requestConnections.values().iterator();
         while (i.hasNext())
         {
            AbstractClient c = (AbstractClient) i.next();
            c.checkExpired(expirationPeriod);
         }
      }
   }

   /**
    * The thread group used by all the 
    */
   public static ThreadGroup oiThreadGroup = new ThreadGroup("Optimized Invoker");

   protected ConnectionManager()
   {
      //This sucks.  Should be an mbean, this should be running with jmx
      //on the client.
      try
      {
      ClassLoader cl = Thread.currentThread().getContextClassLoader(); 
      Class wmClass = cl.loadClass("org.jboss.resource.work.BaseWorkManager");
      workManager = (WorkManager)wmClass.newInstance();
      Method m = wmClass.getMethod("setMaxSize", new Class[] {Integer.TYPE});
      m.invoke(workManager, new Object[] {new Integer(50)});
      }
      catch (Exception e)
      {
         throw new RuntimeException("unexpected problem setting up trunk client" + e);
      } // end of catch
      
      clientClass = BlockingClient.class;
      
      // Try to use the NonBlockingClient if possible
      if( "true".equals( System.getProperty("org.jboss.invocation.trunk.enable_nbio", "true") ) ) {
         try
         {
            clientClass = Class.forName("org.jboss.invocation.trunk.client.nbio.NonBlockingClient");
            log.debug("Using the Non Blocking version of the client");
         }
         catch (Throwable e)
         {
            if (log.isTraceEnabled())
               log.trace("Cannot used NBIO: " + e);
            log.debug("Using the Blocking version of the client");
         }
      }
      log.debug("Setting the clockDaemon's thread factory");
      clockDaemon.setThreadFactory(new ThreadFactory()
      {
         public Thread newThread(Runnable r)
         {
            Thread t = new Thread(oiThreadGroup, r, "Connection Cleaner");
            t.setDaemon(true);
            return t;
         }
      });

   }

   private void startCheckThread()
   {
      log.trace("Starting the Check Thread..");
      checkTaskId = clockDaemon.executePeriodically(this.checkPeriod, new CheckTask(), true);
   }

   private void stopCheckThread()
   {
      log.trace("Stopping the Check Thread..");
      clockDaemon.cancel(checkTaskId);
      clockDaemon.shutDown();
   }

   public static ConnectionManager getInstance()
   {
      return instance;
   }

   AbstractClient connect(ServerAddress serverAddress) throws IOException
   {

      boolean tracing = log.isTraceEnabled();
      /* most of the time this will find a connection */
      AbstractClient connection = (AbstractClient) requestConnections.get(serverAddress);
      if (connection != null)
      {
         if (tracing)
            log.trace("Allready connected to that server, Reusing connection: " + connection);
         return connection;
      }

      synchronized (requestConnections)
      {

         connection = (AbstractClient) requestConnections.get(serverAddress);
         if (connection != null)
         {
            if (tracing)
               log.trace("Allready connected to that server, Reusing connection: " + connection);
            return connection;
         }

         if (tracing)
            log.trace("Establishing a new connection to: " + serverAddress);

         AbstractClient c = null;
         try
         {
            c = (AbstractClient) clientClass.newInstance();
         }
         catch (Throwable e)
         {
            throw new IOException("Client could not be initialized: " + e);
         }

         c.setConnectionManager(this);
         c.setWorkManager(workManager);
         c.connect(serverAddress, threadGroup);
         c.start();

         if (tracing)
            log.trace("Connection established: " + c);

         if (requestConnections.size() == 0)
            startCheckThread();

         HashMap t = (HashMap) requestConnections.clone();
         t.put(serverAddress, c);
         requestConnections = t;
         return c;

      }

   }

   public void connectionClosed(AbstractClient connection, Exception reason)
   {
      if (log.isTraceEnabled())
         log.trace("Connection closed: " + connection, reason);
      // A connection was closed.. timeout or failure.
      // Remove form out map of connections.
      synchronized (requestConnections)
      {
         HashMap t = (HashMap) requestConnections.clone();
         t.remove(connection.getServerAddress());
         requestConnections = t;

         if (t.size() == 0)
            stopCheckThread();
      }
   }

   public void handleRequest(AbstractClient connection, TrunkRequest request)
   {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9638.java