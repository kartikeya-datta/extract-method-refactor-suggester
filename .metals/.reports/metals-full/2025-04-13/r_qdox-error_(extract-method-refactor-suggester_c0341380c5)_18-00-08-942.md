error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8165.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8165.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8165.java
text:
```scala
<@@p>See the <a href="../../../../manual/manual.html">user manual</a> for an

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software
 * License version 1.1, a copy of which has been included with this
 * distribution in the LICENSE.txt file.  */

package org.apache.log4j;

import org.apache.log4j.spi.RootCategory;
import org.apache.log4j.spi.AppenderAttachable;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.NullEnumeration;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.helpers.AppenderAttachableImpl;
import org.apache.log4j.helpers.Loader;
import org.apache.log4j.or.RendererMap;
import org.apache.log4j.or.ObjectRenderer;

import java.util.Enumeration;
import java.util.Vector;
import java.util.MissingResourceException;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.net.URL;
import java.net.MalformedURLException;


/**
  This is the central class in the log4j package. Most logging
  operations, except configuration, are done through this class.

  <p>See the <a href="../../../../manual.html">user manual</a> for an
  introduction on this class. 

  @author Ceki G&uuml;lc&uuml; */
public class Logger extends Category {

  /**
     The fully qualified name of the Level class. See also the 
     getFQCN method. */
  private static final String FQCN = Level.class.getName();


  protected 
  Logger(String name) {
    super(name);
  }

  /** 
    Log a message object with the {@link Level#FINE FINE} level which
    is just an alias for the {@link Level#DEBUG DEBUG} level.

    <p>This method first checks if this category is <code>DEBUG</code>
    enabled by comparing the level of this category with the {@link
    Level#DEBUG DEBUG} level. If this category is
    <code>DEBUG</code> enabled, then it converts the message object
    (passed as parameter) to a string by invoking the appropriate
    {@link ObjectRenderer}. It then proceeds to call all the
    registered appenders in this category and also higher in the
    hierarchy depending on the value of the additivity flag.

    <p><b>WARNING</b> Note that passing a {@link Throwable} to this
    method will print the name of the <code>Throwable</code> but no
    stack trace. To print a stack trace use the {@link #debug(Object,
    Throwable)} form instead.
    
    @param message the message object to log. */
  //public
  //void fine(Object message) {
  //  if(repository.isDisabled(Level.DEBUG_INT)) 
  //	return;    
  //  if(Level.DEBUG.isGreaterOrEqual(this.getChainedLevel())) {
  //	forcedLog(FQCN, Level.DEBUG, message, null);
  //  }
  //}
  

  /**  
   Log a message object with the <code>FINE</code> level including
   the stack trace of the {@link Throwable} <code>t</code> passed as
   parameter.
   
   <p>See {@link #fine(Object)} form for more detailed information.
   
   @param message the message object to log.
   @param t the exception to log, including its stack trace.  */  
  //public
  //void fine(Object message, Throwable t) {
  //  if(repository.isDisabled(Level.DEBUG_INT)) 
  //	return;
  //  if(Level.DEBUG.isGreaterOrEqual(this.getChainedLevel()))
  //	forcedLog(FQCN, Level.FINE, message, t);    
  //}

  /**
     Same as calling {@link LogManager#getLogger(String)}.
  */
  static
  public
  Logger getLogger(String name) {
    return LogManager.getLogger(name);
  }

  /**
     Same as calling {@link LogManager#getLogger(Class)}.
   */
  static
  public
  Logger getLogger(Class clazz) {
    return LogManager.getLogger(clazz.getName());
  }


  /**
     Same as calling {@link LogManager#getRootLogger()}.     
   */
  public
  static 
  Logger getRootLogger() {
    return LogManager.getRootLogger();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8165.java