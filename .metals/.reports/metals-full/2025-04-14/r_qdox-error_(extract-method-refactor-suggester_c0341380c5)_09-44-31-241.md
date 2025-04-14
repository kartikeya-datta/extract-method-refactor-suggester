error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4338.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4338.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4338.java
text:
```scala
T@@opicConnectionFactory fact = (TopicConnectionFactory)namingContext.lookup("ConnectionFactory");

/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ejb.plugins;

import java.lang.reflect.Method;
import java.security.Principal;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import javax.transaction.Transaction;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.jms.DeliveryMode;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.Connection;
import javax.jms.JMSException;

import org.jboss.ejb.Container;
import org.jboss.invocation.Invocation;
import org.jboss.logging.Logger;
import org.jboss.monitor.MetricsConstants;

/**
 * MetricsInterceptor collects data from the bean invocation call and publishes
 * them on a JMS topic (bound to <tt>topic/metrics</tt> in the name service).
 * <p>
 *
 * @since   jBoss 2.0
 *
 * @author  <a href="mailto:jplindfo@helsinki.fi">Juha Lindfors</a>
 */
public class MetricsInterceptor extends AbstractInterceptor
      implements MetricsConstants 
{
   /** 
    * The name of the application to which this interceptor belongs.
    */
   private String applicationName = "<undefined>";

   /** 
    * Bean name in the container.
    */ 
   private String beanName = "<undefined>";

   /** 
    * Publisher thread.
    */
   private Thread publisher;

   /**
    * Message queue for the outgoing JMS messages. This list is accessed
    * by the interceptor when adding new messages, and by the publisher
    * thread when copying and clearing the contents of the queue. The list
    * must be locked for access and locks should be kept down to minimum
    * as they degrade the interceptor stack performance.
    */
   private List msgQueue = new ArrayList(2000);

   /**
    * Starts the JMS publisher thread.
    */
   public void create() 
   {
      applicationName = getContainer().getEjbModule().getName();
      beanName = getContainer().getBeanMetaData().getJndiName();
      publisher = new Thread(new Publisher());
      publisher.setName("Metrics Publisher Thread for " + beanName + ".");
      publisher.setDaemon(true);
      publisher.start();
   }

   /**
    * Kills the publisher thread.
    */
   public void destroy() 
   {
      publisher.interrupt();    
   }

   public Object invoke(Invocation invocation) throws Exception 
   {
      long begin = System.currentTimeMillis();
      try 
      {
         return getNext().invoke(invocation);
      }
      finally 
      {
         if(invocation.getMethod() != null) 
         {
            addEntry(invocation, begin, System.currentTimeMillis());
         }
      }
   }

   /**
    * Store the required information from this invocation: principal,
    * transaction, method, time.
    *
    * @param begin invocation begin time in ms
    * @param end invocation end time in ms
    */
   private final void addEntry(Invocation invocation, long begin, long end) 
   {
      /* this gets called by the interceptor */
      Transaction tx = invocation.getTransaction();
      Principal princ = invocation.getPrincipal();
      Method method = invocation.getMethod();
      Entry start = new Entry(princ, method, tx, begin, "START");
      Entry stop = new Entry(princ, method, tx, end, "STOP");

      // add both entries, order is guaranteed, synchronized to prevent
      // publisher from touching the queue while working on it
      synchronized(msgQueue)
      {
         // Two entries for now, one should suffice but requires changes in
         // the client.
         msgQueue.add(start);
         msgQueue.add(stop);
      }
   }

   private Message createMessage(
         Session session, 
         String principal, 
         int txID,
         String method, 
         String checkpoint, 
         long time)
   {
      try 
      {           
         Message message = session.createMessage();

         message.setJMSType(INVOCATION_METRICS);
         message.setStringProperty(CHECKPOINT, checkpoint);
         message.setStringProperty(BEAN, beanName);
         message.setObjectProperty(METHOD, method);    
         message.setLongProperty(TIME, time);

         if (txID != -1) 
         {
            message.setStringProperty("ID",  String.valueOf(txID));
         }

         if (principal != null)
         {
            message.setStringProperty("PRINCIPAL", principal);
         }

         return message;
      }
      catch (Exception e)
      {
         // catch JMSExceptions, tx.SystemExceptions, and NPE's
         // don't want to bother the container even if the metrics fail.
         return null;
      }
   }

   /**
    * JMS Publisher thread implementation.
    */
   private class Publisher implements Runnable {

      /** Thread keep-alive field. */
      private boolean running = true;
      /** Thread sleep delay. */
      private int delay = 2000;
      /** JMS Connection */
      private TopicConnection connection = null;

      /**
       * Thread implementation. <p>
       *
       * When started, looks up a topic connection factory from the name
       * service, and attempts to create a publisher to <tt>topic/metrics</tt>
       * topic. <p>
       *
       * While alive, locks the <tt>msgQueue</tt> every two seconds to make a
       * copy of the contents and then clear it. <p>
       *
       * Interrupting this thread will kill it.
       *
       * @see #msgQueue
       * @see java.lang.Thread#interrupt()
       */
      public void run()
      {
         try 
         {
            final boolean IS_TRANSACTED    = true;
            final int     ACKNOWLEDGE_MODE = Session.DUPS_OK_ACKNOWLEDGE;

            // lookup the connection factory and topic and create a JMS session
            Context namingContext       = new InitialContext();
            TopicConnectionFactory fact = (TopicConnectionFactory)namingContext.lookup("TopicConnectionFactory");

            connection  = fact.createTopicConnection();

            Topic topic          = (Topic)namingContext.lookup("topic/metrics");
            TopicSession session = connection.createTopicSession(IS_TRANSACTED, ACKNOWLEDGE_MODE);
            TopicPublisher pub   = session.createPublisher(topic);     

            pub.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            pub.setPriority(Message.DEFAULT_PRIORITY);
            pub.setTimeToLive(Message.DEFAULT_TIME_TO_LIVE);

            // start the JMS connection
            connection.start();

            // copy the message queue every x seconds, and publish the messages
            while (running)
            {
               Object[] array;
               long sleepTime = delay;

               try 
               {
                  Thread.sleep(sleepTime);

                  // measure message processing cost and try to deal
                  // with congestion
                  long begin = System.currentTimeMillis();

                  // synchronized during the copy... the interceptor will
                  // have to wait til done
                  synchronized(msgQueue)
                  {
                     array = msgQueue.toArray();
                     msgQueue.clear();    
                  }

                  // publish the messages
                  for (int i = 0; i < array.length; ++i)
                  {
                     Message msg = createMessage(session,
                           ((Entry)array[i]).principal,
                           ((Entry)array[i]).id,
                           ((Entry)array[i]).method,
                           ((Entry)array[i]).checkpoint,
                           ((Entry)array[i]).time
                           );

                     pub.publish(msg);
                  }

                  // try to deal with congestion a little better, alot of
                  // small messages fast will kill JBossMQ performance, this is
                  // a temp fix to group many messages into one operation
                  try
                  {
                     session.commit();
                  } catch(Exception e) {}

                  // stop the clock and reduce the work time from our
                  // resting time
                  long end = System.currentTimeMillis();

                  sleepTime = delay - (end - begin);
               }
               catch (InterruptedException e)
               {
                  // kill this thread
                  running = false;
               }                          
            }

            // thread cleanup
            connection.close();

         }
         catch (NamingException e)
         {
            log.warn(e);
         }
         catch (JMSException e)
         {
            log.warn(e);
         }
      }
   }

   /**
    * Wrapper class for message queue entries.
    *
    * @see #msgQueue
    */
   private final class Entry
   {
      int id = -1;
      long time;
      String principal;
      String checkpoint;
      String method;

      public Entry(
            Principal principal, 
            Method method, 
            Transaction tx, 
            long time, 
            String checkpoint)
      {
         if(principal != null)
         {
            this.principal = principal.getName();
         }

         this.method = method.getName();

         if(tx != null)
         {
            this.id = tx.hashCode();
         }

         this.time = time;
         this.checkpoint = checkpoint;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4338.java