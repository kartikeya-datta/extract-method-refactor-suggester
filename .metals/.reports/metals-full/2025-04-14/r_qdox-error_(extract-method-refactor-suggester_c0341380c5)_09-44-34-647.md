error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3804.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3804.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,50]

error in qdox parser
file content:
```java
offset: 50
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3804.java
text:
```scala
"] will ignore refence part assuming custom URL.")@@;

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included  with this distribution in
 * the LICENSE.txt file.
 */

package org.apache.log4j.helpers;

import java.util.Properties;
import java.net.URL;
import org.apache.log4j.Category;
import org.apache.log4j.Hierarchy;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.Configurator;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.VersionHelper;

// Contributors:   Avy Sharell (sharell@online.fr)
//                 Matthieu Verbert (mve@zurich.ibm.com)
//                 Colin Sampaleanu

/**
   A convenience class to convert property values to specific types.

   @author Ceki G&uuml;lc&uuml;
   @author Simon Kitching;
   @author Anders Kristensen
*/	
public class OptionConverter {

  static String DELIM_START = "${";
  static char   DELIM_STOP  = '}';
  static int DELIM_START_LEN = 2;
  static int DELIM_STOP_LEN  = 1;
  
  static StringBuffer sbuf = new StringBuffer();

  /** OptionConverter is a static class. */
  private OptionConverter() {}
  
  public
  static
  String[] concatanateArrays(String[] l, String[] r) {
    int len = l.length + r.length;
    String[] a = new String[len];

    System.arraycopy(l, 0, a, 0, l.length);
    System.arraycopy(r, 0, a, l.length, r.length);

    return a;
  }
  
  public
  static
  String convertSpecialChars(String s) {
    char c;
    int len = s.length();
    StringBuffer sbuf = new StringBuffer(len);
    
    int i = 0;
    while(i < len) {
      c = s.charAt(i++);
      if (c == '\\') {
	c =  s.charAt(i++);
	if(c == 'n')      c = '\n';
	else if(c == 'r') c = '\r';
	else if(c == 't') c = '\t';
	else if(c == 'f') c = '\f';
	else if(c == '\b') c = '\b';					
	else if(c == '\"') c = '\"';				
	else if(c == '\'') c = '\'';			
	else if(c == '\\') c = '\\';			
      }
      sbuf.append(c);      
    }
    return sbuf.toString();
  }


  /**
     Very similar to <code>System.getProperty</code> except
     that the {@link SecurityException} is hidden.

     @param key The key to search for.
     @param def The default value to return.
     @return the string value of the system property, or the default
     value if there is no property with that key.

     @since 1.1 */
  public
  static
  String getSystemProperty(String key, String def) {
    try {
      return System.getProperty(key, def);
    } catch(Throwable e) { // MS-Java throws com.ms.security.SecurityExceptionEx 
      LogLog.debug("Was not allowed to read system property \""+key+"\".");
      return def;
    }
  }

  
  public
  static
  Object instantiateByKey(Properties props, String key, Class superClass,
				Object defaultValue) {

    // Get the value of the property in string form
    String className = findAndSubst(key, props);
    if(className == null) {
      LogLog.error("Could not find value for key " + key);
      return defaultValue;
    }
    // Trim className to avoid trailing spaces that cause problems.
    return OptionConverter.instantiateByClassName(className.trim(), superClass,
						  defaultValue);
  }

  /**
     If <code>value</code> is "true", then <code>true</code> is
     returned. If <code>value</code> is "false", then
     <code>true</code> is returned. Otherwise, <code>default</code> is
     returned.

     <p>Case of value is unimportant.  */
  public
  static
  boolean toBoolean(String value, boolean dEfault) {
    if(value == null)
      return dEfault;
    String trimmedVal = value.trim();
    if("true".equalsIgnoreCase(trimmedVal)) 
      return true;
    if("false".equalsIgnoreCase(trimmedVal))
      return false;
    return dEfault;
  }

  public
  static
  int toInt(String value, int dEfault) {
    if(value != null) {
      String s = value.trim();
      try {
	return Integer.valueOf(s).intValue();
      }
      catch (NumberFormatException e) {
	 LogLog.error("[" + s + "] is not in proper int form.");
	e.printStackTrace();
      }
    }
    return dEfault;
  }

  /**
     Converts a standard or custom priority level to a Priority
     object.  <p> If <code>value</code> is of form
     "priority#classname", then the specified class' toPriority method
     is called to process the specified priority string; if no '#'
     character is present, then the default {@link org.apache.log4j.Priority}
     class is used to process the priority value.  

     <p> If any error occurs while converting the value to a priority,
     the dflt value (which may be null) is returned.  

     <p> Case of
     value is unimportant for the priority level, but is significant
     for any class name part present.  
     
     @since 1.1
  */
  public
  static
  Priority toPriority(String value, Priority defaultValue) {
    if(value == null)
      return defaultValue;

    int hashIndex = value.indexOf('#');
    if (hashIndex == -1) {
      // no class name specified : use standard Priority class
      return Priority.toPriority(value, defaultValue);
    }

    Priority result = defaultValue;

    String clazz = value.substring(hashIndex+1);
    String priorityName = value.substring(0, hashIndex);

    LogLog.debug("toPriority" + ":class=[" + clazz + "]" 
		 + ":pri=[" + priorityName + "]");

    try {
      Class customPriority = VersionHelper.getInstance().loadClass(clazz);

      // get a ref to the specified class' static method
      // toPriority(String, org.apache.log4j.Priority)
      Class[] paramTypes = new Class[] { String.class,
					 org.apache.log4j.Priority.class
                                       };
      java.lang.reflect.Method toPriorityMethod =
                      customPriority.getMethod("toPriority", paramTypes);

      // now call the toPriority method, passing priority string + default
      Object[] params = new Object[] {priorityName, defaultValue};
      Object o = toPriorityMethod.invoke(null, params);

      result = (Priority) o;
    } catch(ClassNotFoundException e) {
      LogLog.warn("custom priority class [" + clazz + "] not found.");
    } catch(NoSuchMethodException e) {
      LogLog.warn("custom priority class [" + clazz + "]"
        + " does not have a constructor which takes one string parameter", e);
    } catch(java.lang.reflect.InvocationTargetException e) {
      LogLog.warn("custom priority class [" + clazz + "]"
		   + " could not be instantiated", e);
    } catch(ClassCastException e) {
      LogLog.warn("class [" + clazz
        + "] is not a subclass of org.apache.log4j.Priority", e);
    } catch(IllegalAccessException e) {
      LogLog.warn("class ["+clazz+
		   "] cannot be instantiated due to access restrictions", e);
    } catch(Exception e) {
      LogLog.warn("class ["+clazz+"], priority ["+priorityName+
		   "] conversion failed.", e);
    }
    return result;
   }
 
  public
  static
  long toFileSize(String value, long dEfault) {
    if(value == null)
      return dEfault;
    
    String s = value.trim().toUpperCase();
    long multiplier = 1;
    int index;
    
    if((index = s.indexOf("KB")) != -1) {      
      multiplier = 1024;
      s = s.substring(0, index);
    }
    else if((index = s.indexOf("MB")) != -1) {
      multiplier = 1024*1024;
      s = s.substring(0, index);
    }
    else if((index = s.indexOf("GB")) != -1) {
      multiplier = 1024*1024*1024;
      s = s.substring(0, index);
    }    
    if(s != null) {
      try {
	return Long.valueOf(s).longValue() * multiplier;
      }
      catch (NumberFormatException e) {
	LogLog.error("[" + s + "] is not in proper int form.");
	LogLog.error("[" + value + "] not in expected format.", e);
      }
    }
    return dEfault;
  }

  /**
     Find the value corresponding to <code>key</code> in
     <code>props</code>. Then perform variable substitution on the
     found value.

 */
  public
  static
  String findAndSubst(String key, Properties props) {
    String value = props.getProperty(key);
    if(value == null) 
      return null;      
    
    try {
      return substVars(value, props);
    } catch(IllegalArgumentException e) {
      LogLog.error("Bad option value ["+value+"].", e);
      return value;
    }    
  }
   
  /**
     Instantiate an object given a class name. Check that the
     <code>className</code> is a subclass of
     <code>superClass</code>. If that test fails or the object could
     not be instantiated, then <code>defaultValue</code> is returned.

     @param className The fully qualified class name of the object to instantiate.
     @param superClass The class to which the new object should belong.
     @param defaultValue The object to return in case of non-fulfillment
   */
  public
  static
  Object instantiateByClassName(String className, Class superClass,
				Object defaultValue) {
    if(className != null) {
      try {
	Class classObj = VersionHelper.getInstance().loadClass(className);
	if(!superClass.isAssignableFrom(classObj)) {
	  LogLog.error("A \""+className+"\" object is not assignable to a \""+
		       superClass.getName() + "\" variable.");
	  return defaultValue;	  
	}
	return classObj.newInstance();
      }
      catch (Exception e) {
	LogLog.error("Could not instantiate class [" + className + "].", e);
      }
    }
    return defaultValue;    
  }


  /**
     Perform variable substitution in string <code>val</code> from the
     values of keys found in the system propeties.

     <p>The variable substitution delimeters are <b>${</b> and <b>}</b>.
     
     <p>For example, if the System properties contains "key=value", then
     the call
     <pre>
     String s = OptionConverter.substituteVars("Value of key is ${key}.");
     </pre>
  
     will set the variable <code>s</code> to "Value of key is value.".

     <p>If no value could be found for the specified key, then the
     <code>props</code> parameter is searched, if the value could not
     be found there, then substitution defaults to the empty string.

     <p>For example, if system propeties contains no value for the key
     "inexistentKey", then the call

     <pre>
     String s = OptionConverter.subsVars("Value of inexistentKey is [${inexistentKey}]");
     </pre>
     will set <code>s</code> to "Value of inexistentKey is []"     

     <p>An {@link java.lang.IllegalArgumentException} is thrown if
     <code>val</code> contains a start delimeter "${" which is not
     balanced by a stop delimeter "}". </p>

     <p><b>Author</b> Avy Sharell</a></p>

     @param val The string on which variable substitution is performed.
     @throws IllegalArgumentException if <code>val</code> is malformed.

  */
  public static
  String substVars(String val, Properties props) throws
                        IllegalArgumentException {
    sbuf.setLength(0);

    int i = 0;
    int j, k;
    
    while(true) {
      j=val.indexOf(DELIM_START, i);
      if(j == -1) {
	if(i==0)
	  return val;
	else {
	  sbuf.append(val.substring(i, val.length()));
	  return sbuf.toString();
	}
      }
      else {
	sbuf.append(val.substring(i, j));
	k = val.indexOf(DELIM_STOP, j);
	if(k == -1) {
	  throw new IllegalArgumentException('"'+val+
		      "\" has no closing brace. Opening brace at position " + j 
					     + '.');
	}
	else {
	  j += DELIM_START_LEN;
	  String key = val.substring(j, k);
	  // first try in System properties
	  String replacement = getSystemProperty(key, null);
	  // then try props parameter
	  if(replacement == null && props != null) {
	    replacement =  props.getProperty(key);
	  }

	  if(replacement != null) 
	    sbuf.append(replacement);
	  i = k + DELIM_STOP_LEN;	    
	}
      }
    }
  }


  /**
     Configure log4j given a URL. 

     <p>The URL format is important. Its <em>reference</em> part is
     taken as the class name of the configurator. For example, if you
     invoke your application using the command line

     <pre> java -Dlog4j.configuration=file:/temp/myconfig.xyz#com.myCompany.myConfigurator
     </pre>

     then the log4j will be configured by a new instance of
     <code>com.myCompany.myConfigurator</code> by interpreting the
     file referenced by <code>file:/temp/myconfig.xyz</code>.  The
     configurator you specify <em>must</em> implement the {@link
     Configurator} interface.

     <p>If the URL has no reference part, then the {@link
     PropertyConfigurator} will parse the URL. However, if the URL
     ends with a ".xml" extension, then the {@link DOMConfigurator}
     will be used to parse the URL.

     <p>All configurations steps are taken on the
     <code>hierarchy</code> passed as parameter.
     
     @since 1.0 */
  static
  public
  void selectAndConfigure(URL url, Hierarchy hierarchy) {
    String clazz = url.getRef();

    if (clazz.indexOf('.') == -1 || clazz.indexOf('/') != -1 || clazz.indexOf('\\') != -1) {
      LogLog.warn("Suspicious reference in URL ["+url+
		  "] will ignore refence part assuming BEA Weblogic environment.");
      clazz = null;      
    }
 

    Configurator configurator = null;

    if(clazz != null) {
      LogLog.debug("Preferred configurator class: " + clazz);
      configurator = (Configurator) instantiateByClassName(clazz, 
							   Configurator.class,
							   null);
      if(configurator == null) {
	LogLog.error("Could not instantiate configurator ["+clazz+"].");
	return;
      }
    } else {
      String filename = url.getFile();
      if(filename != null && filename.endsWith(".xml")) {
	try {
	  configurator = new DOMConfigurator();
	} catch(NoClassDefFoundError e) {
	  LogLog.warn("Could not find DOMConfigurator!", e);
	  return;
	}
      } else {
	configurator = new PropertyConfigurator();
      }
    }

    configurator.doConfigure(url, hierarchy);
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3804.java