error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5088.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5088.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5088.java
text:
```scala
e@@vent.getLocationInformation();

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software
 * License version 1.1, a copy of which has been included with this
 * distribution in the LICENSE.APL file.  */

package org.apache.log4j;

import org.apache.log4j.Category;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.helpers.BoundedFIFO;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.AppenderAttachable;
import org.apache.log4j.helpers.AppenderAttachableImpl;
import org.apache.log4j.helpers.LogLog;
import java.util.Enumeration;

/**
   The AsyncAppender lets users log events asynchronously. It uses a
   bounded buffer to store logging events.
   
   <p>The AsyncAppender will collect the events sent to it and then
   dispatch them to all the appenders that are attached to it. You can
   attach multiple appenders to an AsyncAppender.

   <p>The AsyncAppender uses a separate thread to serve the events in
   its bounded buffer. 

   <p>Refer to the results in {@link org.apache.log4j.performance.Logging}
   for the impact of using this appender.

   <p><b>Important note:</b> The <code>AsyncAppender</code> can only
   be script configured using the {@link
   org.apache.log4j.xml.DOMConfigurator}. Refer to the
   <code>org/apache/log4j/performace/xml</code> directory for
   examples.
   
   @author Ceki G&uuml;lc&uuml;
   @since version 0.9.1 */
public class AsyncAppender extends AppenderSkeleton 
                                            implements AppenderAttachable {


  /**
     A string constant used in naming the option for setting the the
     location information flag.  Current value of this string
     constant is <b>LocationInfo</b>.  

     <p>Note that all option keys are case sensitive.
  */
  public static final String LOCATION_INFO_OPTION = "LocationInfo";
  
  static final int BUFFER_SIZE = 128;
  //static Category cat = Category.getInstance(AsyncAppender.class.getName());

  BoundedFIFO bf = new BoundedFIFO(BUFFER_SIZE);
  AppenderAttachableImpl aai;
  Dispatcher dispatcher;
  boolean locationInfo = false;

  public
  AsyncAppender() {
    aai = new AppenderAttachableImpl();
    dispatcher = new Dispatcher(bf, aai);
    dispatcher.start();
  }
  
  synchronized  
  public 
  void addAppender(Appender newAppender) {
    aai.addAppender(newAppender);
  }

  public
  void append(LoggingEvent event) {
    // Set the NDC and thread name for the calling thread as these
    // LoggingEvent fields were not set at event creation time.
    event.getNDC();
    event.getThreadName();
    if(locationInfo) {
      event.setLocationInformation();	
    }
    synchronized(bf) {
      if(bf.isFull()) {
	try {
	  //cat.debug("Waiting for free space in buffer.");
	  bf.wait();
	} catch(InterruptedException e) {
	  LogLog.error("AsyncAppender cannot be interrupted.", e);
	}
      }
      //cat.debug("About to put new event in buffer.");      
      bf.put(event);
      if(bf.wasEmpty()) {
	//cat.debug("Notifying dispatcher to process events.");
	bf.notify();
      }
    }
  }

  /**
     Close this <code>AsyncAppender</code> by interrupting the
     dispatcher thread which will process all pending events before
     exiting. */
  public 
  void close() {
    if(closed) // avoid multiple close, otherwise one gets NullPointerException
      return; 

    closed = true;

    dispatcher.close();
    try {
      dispatcher.join();
    } catch(InterruptedException e) {
      LogLog.error("Got an InterruptedException while waiting for the "+
		   "dispatcher to finish.", e);
    }
    dispatcher = null;
    bf = null;
  }

  public
  Enumeration getAllAppenders() {
    return aai.getAllAppenders();
  }

  public
  Appender getAppender(String name) {
    return aai.getAppender(name);
  }

 /**
     Retuns the option names for this component in addition in
     addition to the options of its super class {@link
     AppenderSkeleton}.  */
  public
  String[] getOptionStrings() {
    return OptionConverter.concatanateArrays(super.getOptionStrings(),
          new String[] {LOCATION_INFO_OPTION});
  }

  
  /**
     The <code>AsyncAppender</code> does not require a layout. Hence,
     this method always returns <code>false</code>. */
  public 
  boolean requiresLayout() {
    return false;
  }

  synchronized
  public
  void removeAllAppenders() {
    aai.removeAllAppenders();
  }
  
  synchronized
  public
  void removeAppender(Appender appender) {
   aai.removeAppender(appender);
  }

  synchronized
  public
  void removeAppender(String name) {
    aai.removeAppender(name);
  }

 
 /**
     Set AsyncAppender specific options.

     <p>On top of the options of the super class {@link
     AppenderSkeleton}, the only recognized options is
     <b>LocationInfo</b>.
     
     <p>The <b>LocationInfo</b> option takes a boolean value. By
     default, it is set to false which means there will be no effort
     to extract the location information related to the event. As a
     result, the event that will be ultimately logged will likely to
     contain the wrong location information (if present in the log
     format).

     <p>Location information extraction is comparatively very slow and
     should be avoided unless performance is not a concern.

 */
  public
  void setOption(String option, String value) {
    if(value == null) return;
    super.setOption(option, value);

    if (option.equals(LOCATION_INFO_OPTION))
      locationInfo = OptionConverter.toBoolean(value, locationInfo);
  }

}
// ------------------------------------------------------------------------------
// ------------------------------------------------------------------------------
// ------------------------------------------------------------------------------
class Dispatcher extends Thread {

  BoundedFIFO bf;
  AppenderAttachableImpl aai;
  boolean interrupted = false;

  Dispatcher(BoundedFIFO bf, AppenderAttachableImpl aai) {
    this.bf = bf;
    this.aai = aai;
    // set the dispatcher priority to lowest possible value
    this.setPriority(Thread.MIN_PRIORITY);
    
    // set the dispatcher priority to MIN_PRIORITY plus or minus 2
    // depending on the direction of MIN to MAX_PRIORITY.
    //+ (Thread.MAX_PRIORITY > Thread.MIN_PRIORITY ? 1 : -1)*2);

  }

  void close() {
    synchronized(bf) {
      interrupted = true;   
      // We have a waiting dispacther if and only if bf.length is
      // zero.  In that case, we need to give its death kiss.
      if(bf.length() == 0) {
	bf.notify();
      }
    }
  }



  /**
     The dispatching strategy is to wait until there are events in the
     buffer to process. After having processed an event, we release
     the monitor (variable bf) so that new events can be placed in the
     buffer, instead of keeping the monitor and processing remaning
     events in the buffer. 

    <p>Other approaches might yield better results.

  */
  public
  void run() {

    //Category cat = Category.getInstance(Dispatcher.class.getName());

    LoggingEvent event;
    
    while(true) {
      synchronized(bf) {
	if(bf.length() == 0) {
	  // Exit loop if interrupted but only if the the buffer is empty.
	  if(interrupted) { 
	    //cat.info("Exiting.");
	    return;
	  }
	  try {
	    //cat.debug("Waiting for new event to dispatch.");
	    bf.wait();
	  } catch(InterruptedException e) {
	    LogLog.error("The dispathcer should not be interrupted.");
	    break;
	  }
	}
	//cat.debug("About to get new event.");
	event = bf.get();
	if(bf.wasFull()) {
	  //cat.debug("Notifiying AsyncAppender about freed space.");
	  bf.notify();
	}
      } // synchronized
      
      if(aai != null && event != null)
	aai.appendLoopOnAppenders(event);
    } // while
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5088.java