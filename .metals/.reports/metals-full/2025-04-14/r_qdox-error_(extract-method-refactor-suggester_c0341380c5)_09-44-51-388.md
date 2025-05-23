error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10057.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10057.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10057.java
text:
```scala
d@@efaultLoggerRepository.setName(Constants.DEFAULT_REPOSITORY_NAME);

/*
 * Copyright 1999,2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.log4j;

import org.apache.log4j.helpers.Constants;
import org.apache.log4j.helpers.IntializationUtil;
import org.apache.log4j.helpers.Loader;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.selector.ContextJNDISelector;
import org.apache.log4j.spi.DefaultRepositorySelector;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.RepositorySelector;
import org.apache.log4j.spi.RootLogger;
import org.apache.ugli.impl.SimpleLoggerFA;

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
  private static Object guard = null;
  private static RepositorySelector repositorySelector;
  //private static Scheduler schedulerInstance = null;

  /**
   * The default LoggerRepository instance created by LogManager. This instance
   * is provided for the convenience of the {@link RepositorySelector} instance.
   * The selector, if it choses, may ignore this default repository.
   */
  public final static LoggerRepository defaultLoggerRepository;
  
  /**
   * Log4j components resort to this instance of {@link SimpleLoggerFA} in case 
   * an appropriate LoggerRepository was not set or could not be found. It is
   * used only in exceptional cases.
   */
  public final static SimpleLoggerFA  SIMPLE_LOGGER_FA = new SimpleLoggerFA();

  // The following static initializer gets invoked immediately after a call to 
  // Logger.getLogger() is made. Here is a description of the static initializer.
  // 
  // create defaultLoggerRepository
  // configure(defaultLoggerRepository) depending on system properties
  // during the configuration of defaultLoggerRepository a temporary repository 
  // selector is used.
  //
  // 
  static {
    //System.out.println("**Start of LogManager static initializer");
    defaultLoggerRepository = new Hierarchy(new RootLogger(Level.DEBUG));
    defaultLoggerRepository.setName("default");
    
    // temporary repository
    repositorySelector = new DefaultRepositorySelector(defaultLoggerRepository);
    
    //  Attempt to perform automatic configuration of the default repository
    String configuratorClassName =
      OptionConverter.getSystemProperty(Constants.CONFIGURATOR_CLASS_KEY, null);
    String configurationOptionStr = 
      OptionConverter.getSystemProperty(Constants.DEFAULT_CONFIGURATION_KEY, null);

    if (configurationOptionStr == null) {
      if (Loader.getResource(Constants.DEFAULT_XML_CONFIGURATION_FILE) != null) {
        configurationOptionStr = Constants.DEFAULT_XML_CONFIGURATION_FILE;
      } else if (
        Loader.getResource(Constants.DEFAULT_CONFIGURATION_FILE) != null) {
        configurationOptionStr = Constants.DEFAULT_CONFIGURATION_FILE;
      }
    }

    System.out.println("*** configurationOptionStr=" + configurationOptionStr);

    IntializationUtil.initialConfiguration(
        defaultLoggerRepository, configurationOptionStr, configuratorClassName);
    
    String repositorySelectorStr = 
      OptionConverter.getSystemProperty("log4j.repositorySelectorClass", null);

    if (repositorySelectorStr == null) {
      // NOTHING TO DO, the default repository has been configured already
    } else if (repositorySelectorStr.equalsIgnoreCase("JNDI")) {
      System.out.println("*** Will use ContextJNDISelector **");
      repositorySelector = new ContextJNDISelector();
      guard = new Object();
    } else {
      Object r =
        OptionConverter.instantiateByClassName(
          repositorySelectorStr, RepositorySelector.class, null);

      if (r instanceof RepositorySelector) {
        System.out.println(
          "*** Using [" + repositorySelectorStr
          + "] instance as repository selector.");
        repositorySelector = (RepositorySelector) r;
        guard = new Object();
      } else {
        System.out.println(
          "*** Could not insantiate [" + repositorySelectorStr
          + "] as repository selector.");
        System.out.println("*** Using default repository selector");
        repositorySelector = new DefaultRepositorySelector(defaultLoggerRepository);
      }
    }

    System.out.println("** End of LogManager static initializer");
  }

  /**
     Sets <code>RepositorySelector</code> but only if the correct
     <em>guard</em> is passed as parameter.

     <p>Initally the guard is null, unless the JVM is started with the
     log4j.repositorySelectorClass system property
     (-Dlog4j.repositorySelectorClass=[JNDI | <fully qualified class name>]).
     If the guard is
     <code>null</code>, then invoking this method sets the logger
     repository and the guard. Following invocations will throw a {@link
     IllegalArgumentException}, unless the previously set
     <code>guard</code> is passed as the second parameter.

     <p>This allows a high-level component to set the {@link
     RepositorySelector} used by the <code>LogManager</code>.

     <p>For example, when tomcat starts it will be able to install its
     own repository selector. However, if and when Tomcat is embedded
     within JBoss, then JBoss will install its own repository selector
     and Tomcat will use the repository selector set by its container,
     JBoss.  
     */
  public static void setRepositorySelector(
    RepositorySelector selector, Object guard) throws IllegalArgumentException {
    if ((LogManager.guard != null) && (LogManager.guard != guard)) {
      throw new IllegalArgumentException(
        "Attempted to reset the LoggerFactory without possessing the guard.");
    }
    if (selector == null) {
      throw new IllegalArgumentException(
        "RepositorySelector must be non-null.");
    }

    LogManager.guard = guard;
    LogManager.repositorySelector = selector;
  }


  /**
   * Return the repository selector currently in use.
   * 
   * @since 1.3
   * @return {@link RepositorySelector} currently in use.
   */
  public static RepositorySelector getRepositorySelector() {
    return  LogManager.repositorySelector;
  } 
  
  public static LoggerRepository getLoggerRepository() {
    return repositorySelector.getLoggerRepository();
  }

  /**
     Retrieve the appropriate root logger.
   */
  public static Logger getRootLogger() {
    // Delegate the actual manufacturing of the logger to the logger repository.
    return repositorySelector.getLoggerRepository().getRootLogger();
  }

  /**
     Retrieve the appropriate {@link Logger} instance.
  */
  public static Logger getLogger(String name) {
    LogLog.info("LogManager.getLogger("+name+") called");
    // Delegate the actual manufacturing of the logger to the logger repository.
    return repositorySelector.getLoggerRepository().getLogger(name);
  }

  /**
      Retrieve the appropriate {@link Logger} instance.
   */
  public static Logger getLogger(Class clazz) {
    // Delegate the actual manufacturing of the logger to the logger repository.
    return repositorySelector.getLoggerRepository().getLogger(clazz.getName());
  }

  /**
     Retrieve the appropriate {@link Logger} instance.
  */
  public static Logger getLogger(String name, LoggerFactory factory) {
    // Delegate the actual manufacturing of the logger to the logger repository.
    return repositorySelector.getLoggerRepository().getLogger(name, factory);
  }

  public static Logger exists(String name) {
    return repositorySelector.getLoggerRepository().exists(name);
  }

  public static Enumeration getCurrentLoggers() {
    return repositorySelector.getLoggerRepository().getCurrentLoggers();
  }

  public static void shutdown() {
    repositorySelector.getLoggerRepository().shutdown();
  }

  public static void resetConfiguration() {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10057.java