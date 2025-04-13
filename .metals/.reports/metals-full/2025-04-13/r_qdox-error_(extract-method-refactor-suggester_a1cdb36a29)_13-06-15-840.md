error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8872.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8872.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8872.java
text:
```scala
l@@ogger.setLevel(p);


package org.apache.log4j.jmx;

import java.lang.reflect.Constructor;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.log4j.Appender;
import org.apache.log4j.helpers.OptionConverter;

import java.util.Vector;
import java.util.Enumeration;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.ObjectName;
import javax.management.MBeanInfo;
import javax.management.Attribute;

import javax.management.MBeanException;
import javax.management.AttributeNotFoundException;
import javax.management.RuntimeOperationsException;
import javax.management.ReflectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.NotificationListener;
import javax.management.Notification;

public class LoggerDynamicMBean extends AbstractDynamicMBean
                                  implements NotificationListener {

  private MBeanConstructorInfo[] dConstructors = new MBeanConstructorInfo[1];
  private MBeanOperationInfo[] dOperations = new MBeanOperationInfo[1];

  private Vector dAttributes = new Vector();
  private String dClassName = this.getClass().getName();

  private String dDescription =
     "This MBean acts as a management facade for a org.apache.log4j.Logger instance.";

  // This Logger instance is for logging.
  private static Logger cat = Logger.getLogger(LoggerDynamicMBean.class);

  // We wrap this Logger instance.
  private Logger logger;

  public LoggerDynamicMBean(Logger logger) {
    this.logger = logger;
    buildDynamicMBeanInfo();
  }

  public
  void handleNotification(Notification notification, Object handback) {
    cat.debug("Received notification: "+notification.getType());
    registerAppenderMBean((Appender) notification.getUserData() );


  }

  private
  void buildDynamicMBeanInfo() {
    Constructor[] constructors = this.getClass().getConstructors();
    dConstructors[0] = new MBeanConstructorInfo(
             "HierarchyDynamicMBean(): Constructs a HierarchyDynamicMBean instance",
	     constructors[0]);

    dAttributes.add(new MBeanAttributeInfo("name",
					   "java.lang.String",
					   "The name of this Logger.",
					   true,
					   false,
					   false));

    dAttributes.add(new MBeanAttributeInfo("priority",
					   "java.lang.String",
					   "The priority of this logger.",
					   true,
					   true,
					   false));





    MBeanParameterInfo[] params = new MBeanParameterInfo[2];
    params[0] = new MBeanParameterInfo("class name", "java.lang.String",
				       "add an appender to this logger");
    params[1] = new MBeanParameterInfo("appender name", "java.lang.String",
				       "name of the appender");

    dOperations[0] = new MBeanOperationInfo("addAppender",
					    "addAppender(): add an appender",
					    params,
					    "void",
					    MBeanOperationInfo.ACTION);
  }

  protected
  Logger getLogger() {
    return logger;
  }


  public
  MBeanInfo getMBeanInfo() {
    //cat.debug("getMBeanInfo called.");

    MBeanAttributeInfo[] attribs = new MBeanAttributeInfo[dAttributes.size()];
    dAttributes.toArray(attribs);

    MBeanInfo mb = new MBeanInfo(dClassName,
			 dDescription,
			 attribs,
			 dConstructors,
			 dOperations,
			 new MBeanNotificationInfo[0]);
    //cat.debug("getMBeanInfo exit.");
    return mb;
  }

  public
  Object invoke(String operationName, Object params[], String signature[])
    throws MBeanException,
    ReflectionException {

    if(operationName.equals("addAppender")) {
      addAppender((String) params[0], (String) params[1]);
      return "Hello world.";
    }

    return null;
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

    // Check for a recognized attributeName and call the corresponding getter
    if (attributeName.equals("name")) {
      return logger.getName();
    }  else if(attributeName.equals("priority")) {
      Level l = logger.getLevel();
      if(l == null) {
	return null;
      } else {
	return l.toString();
      }
    } else if(attributeName.startsWith("appender=")) {
      try {
	return new ObjectName("log4j:"+attributeName );
      } catch(Exception e) {
	cat.error("Could not create ObjectName" + attributeName);
      }
    }


    // If attributeName has not been recognized throw an AttributeNotFoundException
    throw(new AttributeNotFoundException("Cannot find " + attributeName +
					 " attribute in " + dClassName));

  }


  void addAppender(String appenderClass, String appenderName) {
    cat.debug("addAppender called with "+appenderClass+", "+appenderName);
    Appender appender = (Appender)
       OptionConverter.instantiateByClassName(appenderClass,
					      org.apache.log4j.Appender.class,
					      null);
    appender.setName(appenderName);
    logger.addAppender(appender);

    //appenderMBeanRegistration();

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
		  "Cannot invoke a setter of " + dClassName +
		  " with null attribute");
    }
    String name = attribute.getName();
    Object value = attribute.getValue();

    if (name == null) {
      throw new RuntimeOperationsException(
                    new IllegalArgumentException("Attribute name cannot be null"),
		    "Cannot invoke the setter of "+dClassName+
		    " with null attribute name");
    }


    if(name.equals("priority")) {
      if (value instanceof String) {
	String s = (String) value;
	Level p = logger.getLevel();
	if(s.equalsIgnoreCase("NULL")) {
	  p = null;
	} else {
	  p = OptionConverter.toLevel(s, p);
	}
	logger.setPriority(p);
      }
    } else {
      throw(new AttributeNotFoundException("Attribute " + name +
					   " not found in " +
					   this.getClass().getName()));
    }
  }

  void appenderMBeanRegistration() {
    Enumeration enum = logger.getAllAppenders();
    while(enum.hasMoreElements()) {
      Appender appender = (Appender) enum.nextElement();
      registerAppenderMBean(appender);
    }
  }

  void registerAppenderMBean(Appender appender) {
    String name = appender.getName();
    cat.debug("Adding AppenderMBean for appender named "+name);
    ObjectName objectName = null;
    try {
      AppenderDynamicMBean appenderMBean = new AppenderDynamicMBean(appender);
      objectName = new ObjectName("log4j", "appender", name);
      server.registerMBean(appenderMBean, objectName);

      dAttributes.add(new MBeanAttributeInfo("appender="+name,
					     "javax.management.ObjectName",
					     "The "+name+" appender.",
					     true,
					     true,
					     false));

    } catch(Exception e) {
      cat.error("Could not add appenderMBean for ["+name+"].", e);
    }
  }

  public
  void postRegister(java.lang.Boolean registrationDone) {
    appenderMBeanRegistration();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8872.java