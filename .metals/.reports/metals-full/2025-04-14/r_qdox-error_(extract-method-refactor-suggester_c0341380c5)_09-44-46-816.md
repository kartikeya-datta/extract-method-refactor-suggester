error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3234.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3234.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3234.java
text:
```scala
v@@oid setThreshold(Priority threshold) {

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software
 * License version 1.1, a copy of which has been included with this
 * distribution in the LICENSE.txt file.  */

package org.apache.log4j;

import org.apache.log4j.Layout;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.OptionHandler;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.helpers.OnlyOnceErrorHandler;
import org.apache.log4j.helpers.LogLog;


/** 
   Abstract superclass of the other appenders in the package.
   
   This class provides the code for common functionality, such as
   support for threshold filtering and support for general filters.

   @since 0.8.1
   @author Ceki G&uuml;lc&uuml; */
public abstract class AppenderSkeleton implements Appender, OptionHandler {

  /** The layout variable does not need to be set if the appender
      implementation has its own layout. */
  protected Layout layout;

  /** Appenders are named. */
  protected String name;

  /**
     There is no level threshold filtering by default.  */
  protected Priority threshold;

  /** 
      It is assumed and enforced that errorHandler is never null.
  */
  protected ErrorHandler errorHandler = new OnlyOnceErrorHandler();

  /** The first filter in the filter chain. Set to <code>null</code>
      initially. */
  protected Filter headFilter;
  /** The last filter in the filter chain. */
  protected Filter tailFilter;

  /**
     Is this appender closed? 
   */
  protected boolean closed = false;


  /**
     Derived appenders should override this method if option structure
     requires it.  */
  public
  void activateOptions() {
  }


  /**
     Add a filter to end of the filter list.

     @since 0.9.0
   */
  public
  void addFilter(Filter newFilter) {
    if(headFilter == null) {
      headFilter = tailFilter = newFilter;
    } else {
      tailFilter.next = newFilter;
      tailFilter = newFilter;    
    }
  }

  /**
     Subclasses of <code>AppenderSkeleton</code> should implement this
     method to perform actual logging. See also {@link #doAppend
     AppenderSkeleton.doAppend} method.

     @since 0.9.0
  */
  abstract
  protected
  void append(LoggingEvent event);


  /**
     Clear the filters chain.
     
     @since 0.9.0 */
  public
  void clearFilters() {
    headFilter = tailFilter = null;
  }

  /**
     Finalize this appender by calling the imlenentation's
     <code>close</code> method.

     @since 0.8.4
  */
  public
  void finalize() {
    // An appender might be closed then garbage collected. There is no
    // point in closing twice.
    if(this.closed) 
      return;

    LogLog.debug("Finalizing appender named ["+name+"].");
    close();
  }


  /** 
      Return the currently set {@link ErrorHandler} for this
      Appender.  

      @since 0.9.0 */
  public
  ErrorHandler getErrorHandler() {
    return this.errorHandler;
  }


  /**
     Returns the head Filter.
     
     @since 1.1
  */
  public
  Filter getFilter() {
    return headFilter;
  }

  /** 
      Return the first filter in the filter chain for this
      Appender. The return value may be <code>null</code> if no is
      filter is set.
      
  */
  public
  final
  Filter getFirstFilter() {
    return headFilter;
  }

  /**
     Returns the layout of this appender. The value may be null.
  */
  public
  Layout getLayout() {
    return layout;
  }


  /**
     Returns the name of this FileAppender.
   */
  public
  final
  String getName() {
    return this.name;
  }

  /**
     Returns this appenders threshold level. See the {@link
     #setThreshold} method for the meaning of this option.
     
     @since 1.1 */
  public
  Priority getThreshold() {
    return threshold;
  }


  /**
     Check whether the message level is below the appender's
     threshold. If there is no threshold set, then the return value is
     always <code>true</code>.

  */
  public
  boolean isAsSevereAsThreshold(Priority priority) {
    return ((threshold == null) || priority.isGreaterOrEqual(threshold));
  }


  /**
     This method performs threshold checks and invokes filters before
     delegating actual logging to the subclasses specific {@link
     AppenderSkeleton#append} method.

   */
  public
  synchronized 
  void doAppend(LoggingEvent event) {
    if(closed) {
      LogLog.error("Attempted to append to closed appender named ["+name+"].");
    }

    if(!isAsSevereAsThreshold(event.level)) {
      return;
    }

    Filter f = this.headFilter;
    
    FILTER_LOOP:
    while(f != null) {
      switch(f.decide(event)) {
      case Filter.DENY: return;
      case Filter.ACCEPT: break FILTER_LOOP;
      case Filter.NEUTRAL: f = f.next;
      }
    }
    
    this.append(event);    
  }

  /** 
      Set the {@link ErrorHandler} for this Appender.
      @since 0.9.0
  */
  public
  synchronized
  void setErrorHandler(ErrorHandler eh) {
    if(eh == null) {
      // We do not throw exception here since the cause is probably a
      // bad config file.
      LogLog.warn("You have tried to set a null error-handler.");
    } else {
      this.errorHandler = eh;
    }
  }

  /**
     Set the layout for this appender. Note that some appenders have
     their own (fixed) layouts or do not use one. For example, the
     {@link org.apache.log4j.net.SocketAppender} ignores the layout set
     here. 
  */
  public
  void setLayout(Layout layout) {
    this.layout = layout;
  }

  
  /**
     Set the name of this Appender.
   */
  public
  void setName(String name) {
    this.name = name;
  }


  /**
     Set the threshold level. All log events with lower level
     than the threshold level are ignored by the appender.
     
     <p>In configuration files this option is specified by setting the
     value of the <b>Threshold</b> option to a level
     string, such as "DEBUG", "INFO" and so on.
     
     @since 0.8.3 */
  public
  void setThreshold(Level threshold) {
    this.threshold = threshold;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3234.java