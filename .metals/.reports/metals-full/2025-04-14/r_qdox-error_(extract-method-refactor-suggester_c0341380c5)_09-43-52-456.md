error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2874.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2874.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2874.java
text:
```scala
L@@ogLog.debug("Could not find resources to perform automatic configuration.");

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software
 * License version 1.1, a copy of which has been included with this
 * distribution in the LICENSE.txt file.  */

package org.apache.log4j;

import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.spi.RepositorySelector;
import org.apache.log4j.spi.DefaultRepositorySelector;
import org.apache.log4j.spi.RootCategory;
import org.apache.log4j.helpers.Loader;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.helpers.LogLog;

import java.net.URL;
import java.net.MalformedURLException;


import java.util.Enumeration;

/**
 * Use the <code>LogManager</code> class to retreive {@link Logger}
 * instances or to operate on the current {@link
 * LoggerRepository}. When the <code>LogManager</code> class is loaded
 * into memory the default initalzation procedure is inititated. The
 * default intialization procedure</a> is described in the <a
 * href="../../../../manual.html#defaultInit">short log4j manual</a>.
 *
 * @author Ceki G&uuml;lc&uuml; */
public class LogManager {

  /**
   * @deprecated This variable is for internal use only. It will
   * become package protected in future versions.
   * */
  static public final String DEFAULT_CONFIGURATION_FILE = "log4j.properties";
  
  static final String DEFAULT_XML_CONFIGURATION_FILE = "log4j.xml";  
   
  /**
   * @deprecated This variable is for internal use only. It will
   * become private in future versions.
   * */
  static final public String DEFAULT_CONFIGURATION_KEY="log4j.configuration";

  /**
   * @deprecated This variable is for internal use only. It will
   * become private in future versions.
   * */
  static final public String CONFIGURATOR_CLASS_KEY="log4j.configuratorClass";

  /**
  * @deprecated This variable is for internal use only. It will
  * become private in future versions.
  */
  public static final String DEFAULT_INIT_OVERRIDE_KEY = 
                                                 "log4j.defaultInitOverride";


  static private Object guard = null;
  static private RepositorySelector repositorySelector;

  static {
    // By default we use a DefaultRepositorySelector which always returns 'h'.
    Hierarchy h = new Hierarchy(new RootCategory(Level.DEBUG));
    repositorySelector = new DefaultRepositorySelector(h);


    Logger logger = LogManager.getLoggerRepository().getLogger("LOG4J");
    logger.setAdditivity(false);
    logger.addAppender(new ConsoleAppender(new PatternLayout("log4j: %-22c{2} - %m%n")));

    /** Search for the properties file log4j.properties in the CLASSPATH.  */
    String override =OptionConverter.getSystemProperty(DEFAULT_INIT_OVERRIDE_KEY,
						       null);
 
    // if there is no default init override, then get the resource
    // specified by the user or the default config file.
    if(override == null || "false".equalsIgnoreCase(override)) {

      String configurationOptionStr = OptionConverter.getSystemProperty(
							  DEFAULT_CONFIGURATION_KEY, 
							  null);

      String configuratorClassName = OptionConverter.getSystemProperty(
                                                   CONFIGURATOR_CLASS_KEY, 
						   null);

      URL url = null;

      // if the user has not specified the log4j.configuration
      // property, we search first for the file "log4j.xml" and then
      // "log4j.properties"
      if(configurationOptionStr == null) {	
	url = Loader.getResource(DEFAULT_XML_CONFIGURATION_FILE);
	if(url == null) {
	  url = Loader.getResource(DEFAULT_CONFIGURATION_FILE);
	}
      } else {
	try {
	  url = new URL(configurationOptionStr);
	} catch (MalformedURLException ex) {
	  // so, resource is not a URL:
	  // attempt to get the resource from the class path
	  url = Loader.getResource(configurationOptionStr); 
	}	
      }
      
      // If we have a non-null url, then delegate the rest of the
      // configuration to the OptionConverter.selectAndConfigure
      // method.
      if(url != null) {
	LogLog.debug("Using URL ["+url+"] for automatic log4j configuration.");      
	OptionConverter.selectAndConfigure(url, configuratorClassName, 
					   LogManager.getLoggerRepository());
      } else {
	LogLog.debug("Could not find resource: ["+configurationOptionStr+"].");
      }
    }  
  } 

  /**
     Sets <code>LoggerFactory</code> but only if the correct
     <em>guard</em> is passed as parameter.
     
     <p>Initally the guard is null.  If the guard is
     <code>null</code>, then invoking this method sets the logger
     factory and the guard. Following invocations will throw a {@link
     IllegalArgumentException}, unless the previously set
     <code>guard</code> is passed as the second parameter.

     <p>This allows a high-level component to set the {@link
     RepositorySelector} used by the <code>LogManager</code>.
     
     <p>For example, when tomcat starts it will be able to install its
     own repository selector. However, if and when Tomcat is embedded
     within JBoss, then JBoss will install its own repository selector
     and Tomcat will use the repository selector set by its container,
     JBoss.  */
  static
  public
  void setRepositorySelector(RepositorySelector selector, Object guard) 
                                                 throws IllegalArgumentException {
    if((LogManager.guard != null) && (LogManager.guard != guard)) {
      throw new IllegalArgumentException(
           "Attempted to reset the LoggerFactory without possessing the guard.");
    }

    if(selector == null) {
      throw new IllegalArgumentException("RepositorySelector must be non-null.");
    }

    LogManager.guard = guard;
    LogManager.repositorySelector = selector;
  }

  static
  public
  LoggerRepository getLoggerRepository() {
    return repositorySelector.getLoggerRepository();
  }

  /**
     Retrieve the appropriate root logger.
   */
  public
  static 
  Logger getRootLogger() {
     // Delegate the actual manufacturing of the logger to the logger repository.
    return repositorySelector.getLoggerRepository().getRootLogger();
  }

  /**
     Retrieve the appropriate {@link Logger} instance.  
  */
  public
  static 
  Logger getLogger(String name) {
     // Delegate the actual manufacturing of the logger to the logger repository.
    return repositorySelector.getLoggerRepository().getLogger(name);
  }

 /**
     Retrieve the appropriate {@link Logger} instance.  
  */
  public
  static 
  Logger getLogger(Class clazz) {
     // Delegate the actual manufacturing of the logger to the logger repository.
    return repositorySelector.getLoggerRepository().getLogger(clazz.getName());
  }


  /**
     Retrieve the appropriate {@link Logger} instance.  
  */
  public
  static 
  Logger getLogger(String name, LoggerFactory factory) {
     // Delegate the actual manufacturing of the logger to the logger repository.
    return repositorySelector.getLoggerRepository().getLogger(name, factory);
  }  

  public
  static
  Logger exists(String name) {
    return repositorySelector.getLoggerRepository().exists(name);
  }

  public
  static
  Enumeration getCurrentLoggers() {
    return repositorySelector.getLoggerRepository().getCurrentLoggers();
  }

  public
  static
  void shutdown() {
    repositorySelector.getLoggerRepository().shutdown();
  }

  public
  static
  void resetConfiguration() {
    repositorySelector.getLoggerRepository().resetConfiguration();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2874.java