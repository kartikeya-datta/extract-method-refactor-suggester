error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6267.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6267.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,10]

error in qdox parser
file content:
```java
offset: 10
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6267.java
text:
```scala
@version $@@Revision: 1.2 $

/*
 * JBoss, the OpenSource WebOS
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.web.tomcat.session;

import org.apache.catalina.Session;
import org.jboss.logging.Logger;

/** This class removes expired sessions from the clustered
 *  session manager

 @see org.jboss.ha.httpsession.server.ClusteredHTTPSessionService

 @author Thomas Peuss <jboss@peuss.de>
 @version $Revision: 1.1 $
 */
public class ClusteredSessionCleanup implements Runnable
{
   // The Manager for which sessions are checked
   protected ClusterManager manager;

   // A Log-object
   protected Logger log;

   // Interval of checks in seconds
   protected int checkInterval = 60;

   // Has the thread ended?
   protected boolean threadDone = false;

   // The cleaner-thread
   protected Thread thread = null;

   // The name for this thread
   protected String threadName = null;

   public ClusteredSessionCleanup(ClusterManager manager, Logger log)
   {
      this.manager = manager;
      this.log = log;
   }

   /**
    * Go through all sessions and look if they have expired
    */
   protected void processExpires()
   {
      // What's the time?
      long timeNow = System.currentTimeMillis();

      // Get all sessions
      Session sessions[] = manager.findSessions();

      log.debug("Looking for sessions that have expired");

      for (int i = 0; i < sessions.length; ++i)
      {
         ClusteredSession session = (ClusteredSession) sessions[i];

         // We only look at valid sessions
         if (!session.isValid())
         {
            continue;
         }

         // How long are they allowed to be idle?
         int maxInactiveInterval = session.getMaxInactiveInterval();

         // Negative values = never expire
         if (maxInactiveInterval < 0)
         {
            continue;
         }

         // How long has this session been idle?
         int timeIdle =
               (int) ((timeNow - session.getLastAccessedTime()) / 1000L);

         // Too long?
         if (timeIdle >= maxInactiveInterval)
         {
            try
            {
               log.debug("Session with id = " + session.getId() + " has expired on local node");
               // Did another node access this session?
               // Try to get the session from the clustered store
               ClusteredSession clusteredSession = manager.loadSession(session.getId());
               if (clusteredSession != null)
               {
                  int timeIdleCluster =
                        (int) ((timeNow - clusteredSession.getLastAccessedTime()) / 1000L);
                  if (timeIdleCluster < maxInactiveInterval)
                  {
                     log.debug("Session " + session.getId() + " has only expired on local node but is alive on another node - removing only from local store");
                     // Remove from local store, because the session is
                     // alive on another node
                     manager.removeLocal(session);
                     continue;
                  }

                  log.debug("Session " + session.getId() + " has also expired on all other nodes - removing globally");
               }


               // Kick this session
               session.expire();
            }
            catch (Throwable t)
            {
               log.error("Problems while expiring session with id = " + session.getId(), t);
            }
         }
      }
   }

   /**
    * Sleep for the duration specified by the <code>checkInterval</code>
    * property.
    */
   protected void threadSleep()
   {

      try
      {
         Thread.sleep(checkInterval * 1000L);
      }
      catch (InterruptedException e)
      {
         ;
      }

   }

   /**
    * Start up the cleanup thread
    */
   protected void threadStart()
   {
      if (thread != null)
      {
         return;
      }

      threadDone = false;
      threadName = "ClusterManagerCleanupThread[" + manager.contextPath + "]";
      thread = new Thread(this, threadName);
      thread.setDaemon(true);
      thread.setContextClassLoader(manager.getContainer().getLoader().getClassLoader());
      thread.start();
   }

   /**
    * Stop the cleanup thread
    */
   protected void threadStop()
   {
      if (thread == null)
      {
         return;
      }

      threadDone = true;
      thread.interrupt();
      try
      {
         thread.join();
      }
      catch (InterruptedException e)
      {
         // ignore
      }

      thread = null;
   }

   /**
    * Work-loop
    */
   public void run()
   {
      while (!threadDone)
      {
         threadSleep();
         processExpires();
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6267.java