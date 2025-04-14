error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8737.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8737.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8737.java
text:
```scala
r@@emoteCategory = Category.getInstance(event.categoryName);

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software
 * License version 1.1, a copy of which has been included with this
 * distribution in the LICENSE.txt file.  */

package org.apache.log4j.net;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.Category;
import org.apache.log4j.Hierarchy;
import org.apache.log4j.LogManager;
import org.apache.log4j.spi.RendererSupport;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.or.jms.MessageRenderer;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.LogLog;

import javax.jms.*;

import javax.naming.InitialContext;
import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

/**
   A simple application receiving the logging events sent by a JMSAppender.
   

   @author Ceki G&uuml;lc&uuml;
*/
public class JMSSink  {

  static public void main(String[] args) {
    if(args.length != 3) {
      usage("Wrong number of arguments.");     
    }

    String tcfBindingName = args[0];
    String topicBindingName = args[1];
    PropertyConfigurator.configure(args[2]);
    
    LoggerRepository rep = LogManager.getLoggerRepository();
    if(rep instanceof RendererSupport) {
      ((RendererSupport) rep).setRenderer(Message.class, new MessageRenderer());
    }

    try {
      Context ctx = new InitialContext();      
      TopicConnectionFactory topicConnectionFactory;
      topicConnectionFactory = (TopicConnectionFactory) lookup(ctx, 
							       tcfBindingName);

      TopicConnection topicConnection = 
	                        topicConnectionFactory.createTopicConnection(); 
      topicConnection.start();
    
      TopicSession topicSession = topicConnection.createTopicSession(false,
							Session.AUTO_ACKNOWLEDGE);

      Topic topic = (Topic)ctx.lookup(topicBindingName);

      //TopicSubscriber topicSubscriber = topicSession.createSubscriber(topic);
      TopicSubscriber topicSubscriber = 
           topicSession.createDurableSubscriber(topic, "x");

      
      LoggingEvent event;
      Category remoteCategory;    

      while(true) {
	ObjectMessage msg = (ObjectMessage)topicSubscriber.receive();      
	event = (LoggingEvent) msg.getObject();
	remoteCategory = Category.getInstance(event.loggerName);
	remoteCategory.callAppenders(event);	
	
	// dump the JMSMessage
	// remoteCategory.debug(msg);

      }
    } catch(Exception e) {
      LogLog.error("Could not read JMS message.", e);
    }
  }


  protected
  static
  Object lookup(Context ctx, String name) throws NamingException {
    try {
      return ctx.lookup(name);
    } catch(NameNotFoundException e) {
      LogLog.error("Could not find name ["+name+"].");
      throw e;
    }    
  }  


  static
  void usage(String msg) {
    System.err.println(msg);
    System.err.println("Usage: java " + JMSSink.class.getName()
            + " TopicConnectionFactoryBindingName TopicBindingName configFile");
    System.exit(1);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8737.java