error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5949.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5949.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,10]

error in qdox parser
file content:
```java
offset: 10
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5949.java
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

import java.util.HashMap;
import java.util.Iterator;

import org.apache.catalina.Context;
import org.jboss.logging.Logger;

/**
 A snapshot manager that collects all modified sessions over a given
 period of time and distributes them en bloc.
 @author Thomas Peuss <jboss@peuss.de>
 @version $Revision: 1.1 $
 */
public class IntervalSnapshotManager extends SnapshotManager implements Runnable
{
   static Logger log = Logger.getLogger(IntervalSnapshotManager.class);

   // the interval in ms
   protected int interval = 1000;

   // the modified sessions
   protected HashMap sessions = new HashMap();

   // the distribute thread
   protected Thread thread = null;

   // has the thread finished?
   protected boolean threadDone = false;

   public IntervalSnapshotManager(ClusterManager manager, Context context)
   {
      super(manager, context);
   }

   public IntervalSnapshotManager(ClusterManager manager, Context context, int interval)
   {
      super(manager, context);
      this.interval = interval;
   }

   /**
    Store the modified session in a hashmap for the distributor thread
    */
   public void snapshot(String id)
   {
      try
      {
         ClusteredSession session = (ClusteredSession) manager.findSession(id);
         synchronized (sessions)
         {
            sessions.put(id, session);
         }
      }
      catch (Exception e)
      {
         log.warn("Failed to replicate sessionID:" + id, e);
      }
   }

   /**
    Distribute all modified sessions
    */
   protected void processSessions()
   {
      synchronized (sessions)
      {
         Iterator iter = sessions.values().iterator();

         // distribute all modified sessions
         while (iter.hasNext())
         {
            ClusteredSession session = (ClusteredSession) iter.next();
            manager.storeSession(session);
         }
         sessions.clear();
      }
   }

   /**
    Start the snapshot manager
    */
   public void start()
   {
      startThread();
   }

   /**
    Stop the snapshot manager
    */
   public void stop()
   {
      stopThread();
      synchronized (sessions)
      {
         sessions.clear();
      }
   }

   /**
    Start the distributor thread
    */
   protected void startThread()
   {
      if (thread != null)
      {
         return;
      }

      thread = new Thread(this, "ClusteredSessionDistributor[" + contextPath + "]");
      thread.setDaemon(true);
      thread.setContextClassLoader(manager.getContainer().getLoader().getClassLoader());
      threadDone = false;
      thread.start();
   }

   /**
    Stop the distributor thread
    */
   protected void stopThread()
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
      }
      thread = null;
   }

   /**
    Little Thread - sleep awhile...
    */
   protected void threadSleep()
   {
      try
      {
         Thread.sleep(interval);
      }
      catch (InterruptedException e)
      {
      }
   }

   /**
    Thread-loop
    */
   public void run()
   {
      while (!threadDone)
      {
         threadSleep();
         processSessions();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5949.java