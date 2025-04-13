error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2559.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2559.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2559.java
text:
```scala
i@@mplements JMSNotificationListenerMBean

/*
* JBoss, the OpenSource J2EE webOS
*
* Distributable under LGPL license.
* See terms of license at gnu.org.
*/
package org.jboss.jmx.connector.notification;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;

import javax.management.Notification;
import javax.management.NotificationListener;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

/**
* Remote Listener using JMS to send the event
**/
public class JMSNotificationListener
   implements NotificationListener, Serializable
{

   // JMS Queue Session and Sender must be created on the server-side
   // therefore they are transient and created on the first notification
   // call
   private transient QueueSender mSender;
   private transient QueueSession mSession;
   private String mJNDIName;
   private Queue mQueue;
   
   public JMSNotificationListener(
      String pJNDIName,
      Queue pQueue
   ) throws JMSException
   {
      mJNDIName = pJNDIName;
      mQueue = pQueue;
   }

   /**
   * Handles the given notification by sending this to the remote
   * client listener
   *
   * @param pNotification				Notification to be send
   * @param pHandback					Handback object
   */
   public void handleNotification(
      Notification pNotification,
      Object pHandback
   ) {
      try {
         if( mSender == null ) {
            // Get QueueConnectionFactory, create Connection, Session and then Sender
            QueueConnection lConnection = getQueueConnection( mJNDIName );
            mSession = lConnection.createQueueSession( false, Session.AUTO_ACKNOWLEDGE );
            mSender = ( (QueueSession) mSession).createSender( mQueue );
         }
         // Create a message and send to the Queue
         Message lMessage = mSession.createObjectMessage( pNotification );
         mSender.send( lMessage );
      }
      catch( Exception e ) {
         e.printStackTrace();
      }
   }

   /**
   * Test if this and the given Object are equal. This is true if the given
   * object both refer to the same local listener
   *
   * @param pTest						Other object to test if equal
   *
   * @return							True if both are of same type and
   *									refer to the same local listener
   **/
   public boolean equals( Object pTest ) {
      if( pTest instanceof JMSNotificationListener ) {
         try {
            return mQueue.getQueueName().equals(
               ( (JMSNotificationListener) pTest).mQueue.getQueueName()
            );
         }
         catch( JMSException je ) {
            je.printStackTrace();
         }
      }
      return false;
   }

   /**
   * @return							Hashcode of the local listener
   **/
   public int hashCode() {
      return mQueue.hashCode();
   }

   /**
   * Creates a SurveyManagement bean.
   *
   * @return Returns a SurveyManagement bean for use by the Survey handler.
   **/
   private QueueConnection getQueueConnection( String pJNDIName )
      throws NamingException, JMSException
   {
      Context aJNDIContext = new InitialContext();
      Object aRef = aJNDIContext.lookup( pJNDIName );
      QueueConnectionFactory aFactory = (QueueConnectionFactory) 
         PortableRemoteObject.narrow( aRef, QueueConnectionFactory.class );
      QueueConnection lConnection = aFactory.createQueueConnection();
      lConnection.start();
      return lConnection;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2559.java