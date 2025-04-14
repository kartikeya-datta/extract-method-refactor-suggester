error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3557.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3557.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3557.java
text:
```scala
P@@riority priority, Object message, Throwable throwable) {

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software
 * License version 1.1, a copy of which has been included with this
 * distribution in the LICENSE.APL file.  */

package org.apache.log4j.spi;

import org.apache.log4j.Category;
import org.apache.log4j.Priority;
import org.apache.log4j.NDC;

import org.apache.log4j.helpers.LogLog;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.Hashtable;

// Contributors:   Nelson Minar <nelson@monkey.org>
//                 Wolf Siberski
//                 Anders Kristensen <akristensen@dynamicsoft.com>

/**
   The internal representation of logging events. When an affirmative
   decision is made to log then a <code>LoggingEvent</code> instance
   is created. This instance is passed around to the different log4j
   components.

   <p>This class is of concern to those wishing to extend log4j. 

   @author Ceki G&uuml;lc&uuml;
   @author James P. Cakalic
   
   @since 0.8.2 */
public class LoggingEvent implements java.io.Serializable {

  private static long startTime = System.currentTimeMillis();

  /** Fully qualified name of the calling category class. */
  transient public final String fqnOfCategoryClass;

  /** The category of the logging event. The categoy field is not
  serialized for performance reasons. 

  <p>It is set by the LoggingEvent constructor or set by a remote
  entity after deserialization. */
  transient public Category category;

  /** The category name. */
  public final String categoryName;
  
  /** Priority of logging event. Priority cannot be serializable
      because it is a flyweight.  Due to its special seralization it
      cannot be declared final either. */
  transient public Priority priority;

  /** The nested diagnostic context (NDC) of logging event. */
  private String ndc;

  /** Have we tried to do an NDC lookup? If we did, there is no need
      to do it again.  Note that its value is always false when
      serialized. Thus, a receiving SocketNode will never use it's own
      (incorrect) NDC. See also writeObject method. */
  private boolean ndcLookupRequired = true;


  /** The application supplied message of logging event. */
  transient private Object message;

  /** The application supplied message rendered through the log4j
      objet rendering mechanism.*/
  private String renderedMessage;

  /** The name of thread in which this logging event was generated. */
  private String threadName;

  /** The throwable associated with this logging event.

      This is field is transient because not all exceptions are
      serializable. More importantly, the stack information does not
      survive serialization.
  */
  transient public final Throwable throwable;

  /** This variable contains the string form of the throwable. This
      field will be serialized if need be.  
  */
  private String throwableInformation;

  /** The number of milliseconds elapsed from 1/1/1970 until logging event
      was created. */
  public final long timeStamp;
  /** Location information for the caller. */
  private LocationInfo locationInfo;

  // Damn serialization
  static final long serialVersionUID = -868428216207166145L;

  static final Integer[] PARAM_ARRAY = new Integer[1];
  static final String TO_PRIORITY = "toPriority";
  static final Class[] TO_PRIORITY_PARAMS = new Class[] {int.class};
  static final Hashtable methodCache = new Hashtable(3); // use a tiny table

  /**
     Instantiate a LoggingEvent from the supplied parameters.
     
     <p>Except {@link #timeStamp} all the other fields of
     <code>LoggingEvent</code> are filled when actually needed.
     <p>
     @param category The category of this event.
     @param priority The priority of this event.
     @param message  The message of this event.
     @param throwable The throwable of this event.  */
  public LoggingEvent(String fqnOfCategoryClass, Category category, 
		      Priority priority, String message, Throwable throwable) {
    this.fqnOfCategoryClass = fqnOfCategoryClass;
    this.category = category;
    this.categoryName = category.getName();
    this.priority = priority;
    this.message = message;
    this.throwable = throwable;
    timeStamp = System.currentTimeMillis();
  }  


  /**
     Return the message for this logging event. 

     <p>Before serialization, the returned object is the message
     passed by the user to generate the logging event. After
     serialization, the returned value equals the String form of the
     message possibly after object rendering. 

     @since 1.1 */
  public
  Object getMessage() {
    if(message != null) {
      return message;
    } else {
      return getRenderedMessage();
    }
  }

  public
  String getNDC() {
    if(ndcLookupRequired) {
      ndcLookupRequired = false;
      ndc = NDC.get();
    }
    return ndc; 
  }

  public
  String getRenderedMessage() {
     if(renderedMessage == null && message != null) {
       if(message instanceof String) {
	renderedMessage = (String) message;
       } else {
	 renderedMessage=
                    category.getHierarchy().getRendererMap().findAndRender(message);
       }
     }
     return renderedMessage;
  }  

  /**
     Returns the time when the application started, in milliseconds
     elapsed since 01.01.1970.  */
  public
  static 
  long getStartTime() {
    return startTime;
  }

  public
  String getThreadName() {
    if(threadName == null)
      threadName = (Thread.currentThread()).getName();
    return threadName;
  }


  /**
     Return the throwable's stack trace if any such information is
     available.  */
  public 
  String getThrowableInformation() {

    if(throwableInformation !=  null)
      return throwableInformation;

    
    if(throwable == null) {
       return null;
    } else {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);

      throwable.printStackTrace(pw);
      throwableInformation = sw.toString();
      return throwableInformation;
    }
  }
	
  private
  void writeObject(ObjectOutputStream oos) throws java.io.IOException {
    // Aside from returning the current thread name the wgetThreadName
    // method sets the threadName variable.
    this.getThreadName();    

    // This call has a side effect of setting this.ndc and
    // setting ndcLookupRequired to false if not already false.
    this.getNDC();

    // This sets the throwableInformation variable to the stack trace
    // of the throwable variable.
    this.getThrowableInformation();

    oos.defaultWriteObject();
    
    // serialize this event's priority
    writePriority(oos);
    

  }

  private 
  void writePriority(ObjectOutputStream oos) throws java.io.IOException {

    oos.writeInt(priority.toInt());

    Class clazz = priority.getClass();
    if(clazz == Priority.class) {
      oos.writeObject(null);
    } else {
      // writing directly the Class object would be nicer, except that
      // serialized a Class object can not be read back by JDK
      // 1.1.x. We have to resort to this hack instead.
      oos.writeObject(clazz.getName());
    }
  }

  private 
  void readPriority(ObjectInputStream ois) 
                      throws java.io.IOException, ClassNotFoundException {

    int p = ois.readInt();
    try {
      String className = (String) ois.readObject();      
      if(className == null) {
	priority = Priority.toPriority(p);
      } else {
	Method m = (Method) methodCache.get(className);	
	if(m == null) {
	  Class clazz = Class.forName(className);
	  m = clazz.getDeclaredMethod(TO_PRIORITY, TO_PRIORITY_PARAMS);
	  methodCache.put(className, m);
	}      
	PARAM_ARRAY[0] = new Integer(p);
	priority = (Priority) m.invoke(null,  PARAM_ARRAY);
      }
    } catch(Exception e) {
	LogLog.warn("Priority deserialization failed, reverting to default.", e);
	priority = Priority.toPriority(p);
    }
  }



  private void readObject(ObjectInputStream ois)
                        throws java.io.IOException, ClassNotFoundException {
    ois.defaultReadObject();    
    readPriority(ois);

    // Make sure that no location info is available to Layouts
    if(locationInfo == null)
      locationInfo = new LocationInfo(null, null);
  }


  /**
     Set the location information for this logging event. The collected
     information is cached for future use.
   */
  public
  LocationInfo getLocationInformation() {
    if(locationInfo == null) {
      locationInfo = new LocationInfo(new Throwable(), fqnOfCategoryClass);
    }
    return locationInfo;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3557.java