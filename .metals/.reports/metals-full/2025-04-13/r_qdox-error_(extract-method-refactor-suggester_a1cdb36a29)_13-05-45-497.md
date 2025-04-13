error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/911.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/911.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,16]

error in qdox parser
file content:
```java
offset: 16
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/911.java
text:
```scala
private static L@@ogger log = Logger.getLogger(HierarchyDynamicMBean.class);

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software
 * License version 1.1, a copy of which has been included with this
 * distribution in the LICENSE.txt file.  */

package org.apache.log4j.jmx;


import java.lang.reflect.Constructor;
import org.apache.log4j.*;

import org.apache.log4j.spi.HierarchyEventListener;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.helpers.OptionConverter;

import java.util.Vector;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;

import javax.management.ObjectName;
import javax.management.MBeanInfo;
import javax.management.Attribute;
import javax.management.MBeanServer;

import javax.management.MBeanException;
import javax.management.AttributeNotFoundException;
import javax.management.RuntimeOperationsException;
import javax.management.ReflectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationBroadcaster;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.NotificationFilter;
import javax.management.NotificationFilterSupport;
import javax.management.ListenerNotFoundException;

public class HierarchyDynamicMBean extends AbstractDynamicMBean 
                                   implements HierarchyEventListener,
                                              NotificationBroadcaster {
 
  static final String ADD_APPENDER = "addAppender."; 
  static final String THRESHOLD = "threshold"; 

  private MBeanConstructorInfo[] dConstructors = new MBeanConstructorInfo[1];
  private MBeanOperationInfo[] dOperations = new MBeanOperationInfo[1];

  private Vector vAttributes = new Vector();
  private String dClassName = this.getClass().getName();
  private String dDescription = 
     "This MBean acts as a management facade for org.apache.log4j.Hierarchy.";

  private NotificationBroadcasterSupport nbs = new NotificationBroadcasterSupport();


  private LoggerRepository hierarchy;
  
  private static Logger log = Logger.getInstance(HierarchyDynamicMBean.class);

  public HierarchyDynamicMBean() {
    hierarchy = LogManager.getLoggerRepository();
    buildDynamicMBeanInfo();
  }

  private 
  void buildDynamicMBeanInfo() {
    Constructor[] constructors = this.getClass().getConstructors();
    dConstructors[0] = new MBeanConstructorInfo(
         "HierarchyDynamicMBean(): Constructs a HierarchyDynamicMBean instance",
	 constructors[0]);

    vAttributes.add(new MBeanAttributeInfo(THRESHOLD,
					   "java.lang.String",
					   "The \"threshold\" state of the hiearchy.",
					   true,
					   true,
					   false));

    MBeanParameterInfo[] params = new MBeanParameterInfo[1];
    params[0] = new MBeanParameterInfo("name", "java.lang.String", 
				       "Create a logger MBean" );
    dOperations[0] = new MBeanOperationInfo("addLoggerMBean",
				    "addLoggerMBean(): add a loggerMBean",
				    params , 
				    "javax.management.ObjectName", 
				    MBeanOperationInfo.ACTION);
  }  


  public 
  ObjectName addLoggerMBean(String name) {
    Logger cat = Logger.exists(name);
    
    if(cat != null) {
      return addLoggerMBean(cat);
    } else {
      return null;
    }
  }

  ObjectName addLoggerMBean(Logger logger) {
    String name = logger.getName();
    ObjectName objectName = null;
    try {
      LoggerDynamicMBean loggerMBean = new LoggerDynamicMBean(logger);
      objectName = new ObjectName("log4j", "logger", name);
      server.registerMBean(loggerMBean, objectName);
      
      NotificationFilterSupport nfs = new NotificationFilterSupport();
      nfs.enableType(ADD_APPENDER+logger.getName());

      log.debug("---Adding logger ["+name+"] as listener.");

      nbs.addNotificationListener(loggerMBean, nfs, null);
      

      vAttributes.add(new MBeanAttributeInfo("logger="+name,
					     "javax.management.ObjectName",
					     "The "+name+" logger.",
					     true,
					     true, // this makes the object
					     // clickable
					     false));
      
    } catch(Exception e) {
      log.error("Couls not add loggerMBean for ["+name+"].");
    }
    return objectName;
  }

  public
  void addNotificationListener(NotificationListener listener, 
			       NotificationFilter filter, 
			       java.lang.Object handback) {
    nbs.addNotificationListener(listener, filter, handback);
  }

  protected
  Logger getLogger() {
    return log;
  }

  public 
  MBeanInfo getMBeanInfo() {
    //cat.debug("getMBeanInfo called.");

    MBeanAttributeInfo[] attribs = new MBeanAttributeInfo[vAttributes.size()];
    vAttributes.toArray(attribs);

    return new MBeanInfo(dClassName,
			 dDescription,
			 attribs,
			 dConstructors,
			 dOperations,
			 new MBeanNotificationInfo[0]);
  }

  public
  MBeanNotificationInfo[] getNotificationInfo(){
    return nbs.getNotificationInfo();
  }

  public 
  Object invoke(String operationName, 
		Object params[], 
		String signature[]) throws MBeanException, 
                                           ReflectionException {

    if (operationName == null) {
      throw new RuntimeOperationsException(
        new IllegalArgumentException("Operation name cannot be null"), 
	"Cannot invoke a null operation in " + dClassName);
    }
    // Check for a recognized operation name and call the corresponding operation

    if(operationName.equals("addLoggerMBean")) {
      return addLoggerMBean((String)params[0]);
    } else { 
      throw new ReflectionException(
	    new NoSuchMethodException(operationName), 
	    "Cannot find the operation " + operationName + " in " + dClassName);
    }

  }
  

  public 
  Object getAttribute(String attributeName) throws AttributeNotFoundException, 
                                                    MBeanException, 
                                                    ReflectionException {

    // Check attributeName is not null to avoid NullPointerException later on
    if (attributeName == null) {
      throw new RuntimeOperationsException(new IllegalArgumentException(
			"Attribute name cannot be null"), 
       "Cannot invoke a getter of " + dClassName + " with null attribute name");
    }

    log.debug("Called getAttribute with ["+attributeName+"].");
    
    // Check for a recognized attributeName and call the corresponding getter
    if (attributeName.equals(THRESHOLD)) {
      return hierarchy.getThreshold();
    } else if(attributeName.startsWith("logger")) {
      int k = attributeName.indexOf("%3D");
      String val = attributeName;
      if(k > 0) {
	val = attributeName.substring(0, k)+'='+ attributeName.substring(k+3);
      }
      try {
	return new ObjectName("log4j:"+val);
      } catch(Exception e) {
	log.error("Could not create ObjectName" + val);
      }
    }



    // If attributeName has not been recognized throw an AttributeNotFoundException
    throw(new AttributeNotFoundException("Cannot find " + attributeName + 
					 " attribute in " + dClassName));

  }


  public
  void addAppenderEvent(Category logger, Appender appender) {
    log.debug("addAppenderEvent called: logger="+logger.getName()+
	      ", appender="+appender.getName());
    Notification n = new Notification(ADD_APPENDER+logger.getName(), this, 0);
    n.setUserData(appender);
    log.debug("sending notification.");
    nbs.sendNotification(n);
  }

 public
  void removeAppenderEvent(Category cat, Appender appender) {
    log.debug("removeAppenderCalled: logger="+cat.getName()+
	      ", appender="+appender.getName());
  }

  public
  void postRegister(java.lang.Boolean registrationDone) {
    log.debug("postRegister is called.");
    hierarchy.addHierarchyEventListener(this);
    Logger root = hierarchy.getRootLogger();
    addLoggerMBean(root);
  }

  public
  void removeNotificationListener(NotificationListener listener) 
                                         throws ListenerNotFoundException {
    nbs.removeNotificationListener(listener);
  }
   
  public 
  void setAttribute(Attribute attribute) throws AttributeNotFoundException,
                                                InvalidAttributeValueException,
                                                MBeanException, 
                                                ReflectionException {
    
    // Check attribute is not null to avoid NullPointerException later on
    if (attribute == null) {
      throw new RuntimeOperationsException(
                  new IllegalArgumentException("Attribute cannot be null"), 
	  "Cannot invoke a setter of "+dClassName+" with null attribute");
    }
    String name = attribute.getName();
    Object value = attribute.getValue();

    if (name == null) {
      throw new RuntimeOperationsException(
               new IllegalArgumentException("Attribute name cannot be null"), 
	       "Cannot invoke the setter of "+dClassName+
	       " with null attribute name");
    }

    if(name.equals(THRESHOLD)) {
      Level l = OptionConverter.toLevel((String) value, 
					   hierarchy.getThreshold());
      hierarchy.setThreshold(l);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/911.java