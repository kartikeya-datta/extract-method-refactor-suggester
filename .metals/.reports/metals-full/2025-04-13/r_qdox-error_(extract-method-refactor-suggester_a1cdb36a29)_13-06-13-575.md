error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8804.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8804.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8804.java
text:
```scala
m@@etricsPub.publish(msg, DeliveryMode.NON_PERSISTENT, 1, 10000);

/*
 * jBoss, the OpenSource EJB server
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ejb.plugins;

// standard imports
import java.util.Properties;

import java.security.Principal;

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
import javax.jms.JMSException;

import javax.transaction.Transaction;
import javax.transaction.Status;


// jboss imports
import org.jboss.ejb.Container;
import org.jboss.ejb.MethodInvocation;
import org.jboss.monitor.MetricsConstants;

/**
 * MetricsInterceptor is used for gathering data from the container for admin
 * interface.
 *
 * @since   jBoss 2.0
 *
 * @author  <a href="mailto:jplindfo@helsinki.fi">Juha Lindfors</a>
 */
public class MetricsInterceptor extends AbstractInterceptor 
                                implements MetricsConstants {

    // Constants -----------------------------------------------------
    
    // Attributes ----------------------------------------------------
    private Container container           = null;
    private Context  namingContext        = null;
    private TopicPublisher metricsPub     = null;
    private TopicSession metricsSession   = null;
    private Topic metricsTopic            = null;
    
    private String applicationName        = "<undefined>";
    private String beanName               = "<undefined>";
    
    // Static --------------------------------------------------------

    // Constructors --------------------------------------------------
   
    // Public --------------------------------------------------------
    public void setContainer(Container container) {
        this.container  = container;
        
        applicationName = container.getApplication().getName();
        beanName        = container.getBeanMetaData().getJndiName();
    }
    
    public Container getContainer() {
        return container;
    }

    // Interceptor implementation ------------------------------------
   public Object invokeHome(MethodInvocation mi) throws Exception {
     
     try {
        sendMessage(System.currentTimeMillis(), createMessage(mi, "START"));
        return super.invokeHome(mi);
     }
     finally {
         sendMessage(System.currentTimeMillis(), createMessage(mi, "STOP"));
     }
   }

    public Object invoke(MethodInvocation mi) throws Exception {

        try {
            sendMessage(System.currentTimeMillis(), createMessage(mi, "START"));
            return super.invoke(mi);
        }
      
        finally {
            sendMessage(System.currentTimeMillis(), createMessage(mi, "STOP"));
        }
   }

   public void init() {

       try {
           final boolean IS_TRANSACTED    = false;
           final int     ACKNOWLEDGE_MODE = Session.DUPS_OK_ACKNOWLEDGE;
           
           namingContext = new InitialContext();
           
           TopicConnectionFactory factory = (TopicConnectionFactory)
                namingContext.lookup("TopicConnectionFactory");

           TopicConnection connection = factory.createTopicConnection();

           metricsTopic     = (Topic)namingContext.lookup("topic/metrics");
           metricsSession   = connection.createTopicSession(IS_TRANSACTED, ACKNOWLEDGE_MODE);
           metricsPub       = metricsSession.createPublisher(metricsTopic);     
           
           connection.start();
       }
       catch (NamingException e) {
           System.out.println(e);
       }
       catch (JMSException e) {
           System.out.println(e);
       }
       
   }

   
    // Private --------------------------------------------------------

    private void sendMessage(long time, Message msg) {        

        try {
            msg.setLongProperty(TIME,  time);
            metricsPub.publish(msg, DeliveryMode.NON_PERSISTENT, 1, 1);
        }
        catch (Exception e) {
            // catch JMSExceptions, NPE's etc and prevent them from propagating
            // up if the metrics fail
        }
    }
    
    private Message createMessage(MethodInvocation mi, String checkpoint) {
        
        try {            
            Message  msg    =  metricsSession.createMessage();
            Transaction tx  =  mi.getTransaction();
            Principal principal = mi.getPrincipal();
            
            msg.setJMSType(INVOCATION_METRICS);
           // msg.setJMSExpiration(1);
            
            msg.setStringProperty(CHECKPOINT, checkpoint);
            msg.setStringProperty(BEAN,   beanName);
            msg.setObjectProperty(METHOD, mi.getMethod().getName());    
            
            if (tx != null) 
                msg.setStringProperty("ID",  String.valueOf(tx.hashCode()));
                        
            if (principal != null)
                msg.setStringProperty("PRINCIPAL", principal.getName());
                
            return msg;
        }
        catch (Exception e) {
            // catch JMSExceptions, tx.SystemExceptions, and NPE's
            // don't want to bother the container even if the metrics fail.
            return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8804.java