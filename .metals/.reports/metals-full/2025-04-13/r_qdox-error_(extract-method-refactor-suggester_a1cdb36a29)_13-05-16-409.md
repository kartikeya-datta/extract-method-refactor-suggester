error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5382.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5382.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,56]

error in qdox parser
file content:
```java
offset: 56
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5382.java
text:
```scala
"org.jboss.management.mejb.PollingNotificationListener",@@

/*
* JBoss, the OpenSource J2EE webOS
*
* Distributable under LGPL license.
* See terms of license at gnu.org.
*/
package org.jboss.management.mejb;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import javax.management.JMException;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import org.jboss.jmx.connector.RemoteMBeanServer;

/**
* Local Polling Listener to receive the message and send to the listener
**/
public class PollingClientNotificationListener
   extends ClientNotificationListener
   implements Runnable
{

   private MEJB                        mConnector;
   private int                         mSleepingPeriod = 2000;
   
   public PollingClientNotificationListener(
      ObjectName pSender,
      NotificationListener pClientListener,
      Object pHandback,
      NotificationFilter pFilter,
      int pSleepingPeriod,
      int pMaximumListSize,
      MEJB pConnector
   ) throws
      JMException,
      RemoteException
   {
      super( pSender, pClientListener, pHandback );
      if( pSleepingPeriod > 0 ) {
         mSleepingPeriod = pSleepingPeriod;
      }
      mConnector = pConnector;
      // Register the listener as MBean on the remote JMX server
      createListener(
         pConnector,
         "org.jboss.jmx.connector.notification.PollingNotificationListener",
         new Object[] { new Integer( pMaximumListSize ), new Integer( pMaximumListSize ) },
         new String[] { Integer.TYPE.getName(), Integer.TYPE.getName() }
      );
      addNotificationListener( pConnector, pFilter );
      new Thread( this ).start();
   }

   public void run() {
      while( true ) {
         try {
            try {
               List lNotifications = (List) mConnector.invoke(
                  getRemoteListenerName(),
                  "getNotifications",
                  new Object[] {},
                  new String[] {}
               );
               Iterator i = lNotifications.iterator();
               while( i.hasNext() ) {
                  Notification lNotification = (Notification) i.next();
                  mClientListener.handleNotification(
                     lNotification,
                     mHandback
                  );
               }
            }
            catch( Exception e ) {
               System.out.println( "PollingClientNotificationListener.getNotifications() got exception " + e );
            }
            Thread.sleep( mSleepingPeriod );
         }
         catch( InterruptedException ie ) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5382.java