error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2017.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2017.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2017.java
text:
```scala
r@@eturn OBJECT_NAME;

/*
 * JBoss, the OpenSource EJB server
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.util;

import java.security.InvalidParameterException;
import java.util.Date;

import javax.management.MalformedObjectNameException;
import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.timer.TimerNotification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import org.jboss.logging.Logger;
import org.jboss.system.ServiceMBeanSupport;

/**
* Scheduler Instance to allow clients to run this as a scheduling service for
* any Schedulable instances.
* <br>
* ATTENTION: The scheduler instance only allows to run one schedule at a time.
* Therefore when you want to run two schedules create to instances with this
* MBean. Suggested Object Name for the MBean are:<br>
* jboss:service=Scheduler,schedule=<you schedule name><br>
* This way you should not run into a name conflict.
*
* @author <a href="mailto:andreas@jboss.org">Andreas Schaefer</a>
* @author Cameron (camtabor)
*
* <p><b>Revisions:</b></p>
* <p><b>20010814 Cameron:</b>
* <ul>
* <li>Checks if the TimerMBean is already loaded</li>
* <li>Created a SchedulerNotificationFilter so that each Scheduler only
*     get its notifications</li>
* <li>Stop was broken because removeNotification( Integer ) was broken</li>
* </ul>
* </p>
* <p><b>20011026 Andy:</b>
* <ul>
* <li>Move the SchedulerNotificationFilter to become an internal class
*     and renamed to NotificationFilter</li>
* <li>MBean is not bind/unbind to JNDI server anymore</li>
* </ul>
* </p>
* <p><b>20020117 Andy:</b>
* <ul>
* <li>Change the behaviour when the Start Date is in the past. Now the
*     Scheduler will behave as the Schedule is never stopped and find
*     the next available time to start with respect to the settings.
*     Therefore you can restart JBoss without adjust your Schedule
*     every time. BUT you will still loose the calls during the Schedule
*     was donw.</li>
* <li>Added parsing capabilities to setInitialStartDate. Now NOW: current time,
*     and a string in a format the SimpleDataFormat understand in your environment
*     (US: m/d/yy h:m a) but of course the time in ms since 1/1/1970.</li>
* <li>Some fixes like the stopping a Schedule even if it already stopped etc.</li>
* </ul>
* </p>
**/
public class SchedulableExample
   extends ServiceMBeanSupport
   implements SchedulableExampleMBean
{

   // -------------------------------------------------------------------------
   // Constants
   // -------------------------------------------------------------------------
   
   /** Class logger. */
   private static final Logger log = Logger.getLogger( SchedulableExample.class );
   
   // -------------------------------------------------------------------------
   // Members
   // -------------------------------------------------------------------------
   
   // -------------------------------------------------------------------------
   // Constructors
   // -------------------------------------------------------------------------
   
   /**
    * Default (no-args) Constructor
    **/
   public SchedulableExample()
   {
   }
   
   // -------------------------------------------------------------------------
   // SchedulableExampleMBean Methods
   // -------------------------------------------------------------------------
   
   public void hit( Notification lNotification, Date lDate, long lRepetitions, ObjectName lName, String lTest ) {
      log.info( "got hit"
         + ", notification: " + lNotification
         + ", date: " + lDate
         + ", remaining repetitions: " + lRepetitions
         + ", scheduler name: " + lName
         + ", test string: " + lTest
      );
   }
   
   // -------------------------------------------------------------------------
   // Methods
   // -------------------------------------------------------------------------

   public ObjectName getObjectName(
      MBeanServer pServer,
      ObjectName pName
   )
      throws MalformedObjectNameException
   {
      return new ObjectName( OBJECT_NAME );
   }

   public String getName() {
      return "JBoss Schedulable Example MBean";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2017.java