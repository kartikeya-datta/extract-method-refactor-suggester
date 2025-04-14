error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6788.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6788.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6788.java
text:
```scala
r@@eturn OptionConverter.toLevel(v, (Level) Level.DEBUG);

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included  with this distribution in
 * the LICENSE.APL file.
 */

// Contributors:  Georg Lundesgaard

package org.apache.log4j.config;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import org.apache.log4j.*;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.OptionHandler;

/**
   General purpose Object property setter. Clients repeatedly invokes
   {@link #setProperty setProperty(name,value)} in order to invoke setters
   on the Object specified in the constructor. This class relies on the
   JavaBeans {@link Introspector} to analyze the given Object Class using
   reflection.
   
   <p>Usage:
   <pre>
     PropertySetter ps = new PropertySetter(anObject);
     ps.set("name", "Joe");
     ps.set("age", "32");
     ps.set("isMale", "true");
   </pre>
   will cause the invocations anObject.setName("Joe"), anObject.setAge(32),
   and setMale(true) if such methods exist with those signatures.
   Otherwise an {@link IntrospectionException} are thrown.
  
   @author Anders Kristensen
   @since 1.1
 */
public class PropertySetter {
  protected Object obj;
  protected PropertyDescriptor[] props;
  
  /**
    Create a new PropertySetter for the specified Object. This is done
    in prepartion for invoking {@link #setProperty} one or more times.
    
    @param obj  the object for which to set properties
   */
  public
  PropertySetter(Object obj) {
    this.obj = obj;
  }
  
  /**
     Uses JavaBeans {@link Introspector} to computer setters of object to be
     configured.
   */
  protected
  void introspect() {
    try {
      BeanInfo bi = Introspector.getBeanInfo(obj.getClass());
      props = bi.getPropertyDescriptors();
    } catch (IntrospectionException ex) {
      LogLog.error("Failed to introspect "+obj+": " + ex.getMessage());
      props = new PropertyDescriptor[0];
    }
  }
  

  /**
     Set the properties of an object passed as a parameter in one
     go. The <code>properties</code> are parsed relative to a
     <code>prefix</code>.

     @param obj The object to configure.
     @param properties A java.util.Properties containing keys and values.
     @param prefix Only keys having the specified prefix will be set.
  */
  public
  static
  void setProperties(Object obj, Properties properties, String prefix) {
    new PropertySetter(obj).setProperties(properties, prefix);
  }
  

  /**
     Set the properites for the object that match the
     <code>prefix</code> passed as parameter.

     
   */
  public
  void setProperties(Properties properties, String prefix) {
    int len = prefix.length();
    
    for (Enumeration e = properties.propertyNames(); e.hasMoreElements(); ) {
      String key = (String) e.nextElement();
      
      // handle only properties that start with the desired frefix.
      if (key.startsWith(prefix)) {

	
	// ignore key if it contains dots after the prefix
        if (key.indexOf('.', len + 1) > 0) {
	  //System.err.println("----------Ignoring---["+key
	  //	     +"], prefix=["+prefix+"].");
	  continue;
	}
        
	String value = OptionConverter.findAndSubst(key, properties);
        key = key.substring(len);
        if ("layout".equals(key) && obj instanceof Appender) {
          continue;
        }        
        setProperty(key, value);
      }
    }
    activate();
  }
  
  /**
     Set a property on this PropertySetter's Object. If successful, this
     method will invoke a setter method on the underlying Object. The
     setter is the one for the specified property name and the value is
     determined partly from the setter argument type and partly from the
     value specified in the call to this method.
     
     <p>If the setter expects a String no conversion is necessary.
     If it expects an int, then an attempt is made to convert 'value'
     to an int using new Integer(value). If the setter expects a boolean,
     the conversion is by new Boolean(value).
     
     @param name    name of the property
     @param value   String value of the property
   */
  public
  void setProperty(String name, String value) {
    if (value == null) return;
    
    name = Introspector.decapitalize(name);
    PropertyDescriptor prop = getPropertyDescriptor(name);
    
    //LogLog.debug("---------Key: "+name+", type="+prop.getPropertyType());

    if (prop == null) {
      LogLog.warn("No such property [" + name + "] in "+
		  obj.getClass().getName()+"." );
    } else {
      try {
        setProperty(prop, name, value);
      } catch (PropertySetterException ex) {
        LogLog.warn("Failed to set property [" + name +
                    "] to value \"" + value + "\". " + ex.getMessage());
      }
    }
  }
  
  /** 
      Set the named property given a {@link PropertyDescriptor}.

      @param prop A PropertyDescriptor describing the characteristics
      of the property to set.
      @param name The named of the property to set.
      @param value The value of the property.      
   */
  public
  void setProperty(PropertyDescriptor prop, String name, String value)
    throws PropertySetterException {
    Method setter = prop.getWriteMethod();
    if (setter == null) {
      throw new PropertySetterException("No setter for property ["+name+"].");
    }
    Class[] paramTypes = setter.getParameterTypes();
    if (paramTypes.length != 1) {
      throw new PropertySetterException("#params for setter != 1");
    }
    
    Object arg;
    try {
      arg = convertArg(value, paramTypes[0]);
    } catch (Throwable t) {
      throw new PropertySetterException("Conversion to type ["+paramTypes[0]+
					"] failed. Reason: "+t);
    }
    if (arg == null) {
      throw new PropertySetterException(
          "Conversion to type ["+paramTypes[0]+"] failed.");
    }
    LogLog.debug("Setting property [" + name + "] to [" +arg+"].");
    try {
      setter.invoke(obj, new Object[]  { arg });
    } catch (Exception ex) {
      throw new PropertySetterException(ex);
    }
  }
  

  /**
     Convert <code>val</code> a String parameter to an object of a
     given type.
  */
  protected
  Object convertArg(String val, Class type) {
    if(val == null)
      return null;

    String v = val.trim();
    if (String.class.isAssignableFrom(type)) {
      return val;
    } else if (Integer.TYPE.isAssignableFrom(type)) {
      return new Integer(v);
    } else if (Long.TYPE.isAssignableFrom(type)) {
      return new Long(v);
    } else if (Boolean.TYPE.isAssignableFrom(type)) {
      if ("true".equalsIgnoreCase(v)) {
        return Boolean.TRUE;
      } else if ("false".equalsIgnoreCase(v)) {
        return Boolean.FALSE;
      }
    } else if (Priority.class.isAssignableFrom(type)) {
      return OptionConverter.toLevel(v, Level.DEBUG);
    }
    return null;
  }
  
  
  protected
  PropertyDescriptor getPropertyDescriptor(String name) {
    if (props == null) introspect();
    
    for (int i = 0; i < props.length; i++) {
      if (name.equals(props[i].getName())) {
	return props[i];
      }
    }
    return null;
  }
  
  public
  void activate() {
    if (obj instanceof OptionHandler) {
      ((OptionHandler) obj).activateOptions();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6788.java