error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7429.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7429.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,52]

error in qdox parser
file content:
```java
offset: 52
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7429.java
text:
```scala
"org.jboss.management.mejb.JMSNotificationListener",@@

/*
* JBoss, the OpenSource J2EE webOS
*
* Distributable under LGPL license.
* See terms of license at gnu.org.
*/
package org.jboss.management.mejb;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Hashtable;

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

import javax.management.JMException;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.rmi.PortableRemoteObject;

import org.jboss.jmx.connector.RemoteMBeanServer;

/**
* Local JMS Listener to receive the message and send to the listener
**/
public class JMSClientNotificationListener
   extends ClientNotificationListener
   implements MessageListener
{

   public JMSClientNotificationListener(
      ObjectName pSender,
      NotificationListener pClientListener,
      Object pHandback,
      NotificationFilter pFilter,
      String pQueueJNDIName,
      String pServerName,
      MEJB pConnector
   ) throws
      JMSException,
      JMException,
      NamingException,
      RemoteException
   {
      super( pSender, pClientListener, pHandback );

      // Get the JMS QueueConnectionFactory from the J2EE server
      QueueConnection lConnection = getQueueConnection( pServerName, pQueueJNDIName );
      // Create JMS Session and create temporary Queue
      QueueSession lSession = lConnection.createQueueSession( false, Session.AUTO_ACKNOWLEDGE );
      Queue lQueue = lSession.createTemporaryQueue();
      // Register the listener as MBean on the remote JMX server
      createListener(
         pConnector,
         "org.jboss.jmx.connector.notification.JMSNotificationListener",
         new Object[] { pQueueJNDIName, lQueue },
         new String[] { String.class.getName(), Queue.class.getName() }
      );
      // Create JMS message receiver, create local message listener and set it as message
      // listener to the receiver
      QueueReceiver lReceiver = lSession.createReceiver( lQueue, null );
      lReceiver.setMessageListener( this );
      addNotificationListener( pConnector, pFilter );
   }

   public void onMessage( Message pMessage ) {
      try {
         // Unpack the Notification from the Message and hand it over to the clients
         // Notification Listener
         Notification lNotification = (Notification) ( (ObjectMessage) pMessage ).getObject();
         mClientListener.handleNotification( lNotification, mHandback );
      }
      catch( JMSException je ) {
         je.printStackTrace();
      }
   }

   /**
   * Creates a SurveyManagement bean.
   *
   * @return Returns a SurveyManagement bean for use by the Survey handler.
   **/
   private QueueConnection getQueueConnection( String pServerName, String pQueueJNDIName )
      throws NamingException, JMSException
   {
      Context lJNDIContext = null;
      if( pServerName != null ) {
         Hashtable lProperties = new Hashtable();
         lProperties.put( Context.PROVIDER_URL, pServerName );
         lJNDIContext = new InitialContext( lProperties );
      } else {
         lJNDIContext = new InitialContext();
      }
      Object aRef = lJNDIContext.lookup( pQueueJNDIName );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7429.java