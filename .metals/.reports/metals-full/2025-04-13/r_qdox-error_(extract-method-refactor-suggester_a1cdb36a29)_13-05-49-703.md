error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4178.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4178.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4178.java
text:
```scala
public v@@oid emitNoAppenderWarning(Logger logger);

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

package org.apache.log4j.spi;

import org.apache.log4j.*;
import org.apache.log4j.plugins.PluginRegistry;

import java.util.Enumeration;
import java.util.Map;


/**
   A <code>LoggerRepository</code> is used to create and retrieve
   <code>Loggers</code>. The relation between loggers in a repository
   depends on the repository but typically loggers are arranged in a
   named hierarchy.

   <p>In addition to the creational methods, a
   <code>LoggerRepository</code> can be queried for existing loggers,
   can act as a point of registry for events related to loggers.

   @author Ceki G&uuml;lc&uuml;
   @author Mark Womack
   @since 1.2 */
public interface LoggerRepository {
  /**
    Add a {@link LoggerRepositoryEventListener} to the repository. The
    listener will be called when repository events occur.
    @since 1.3*/
  public void addLoggerRepositoryEventListener(
    LoggerRepositoryEventListener listener);

  /**
    Remove a {@link LoggerRepositoryEventListener} from the repository.
    @since 1.3*/
  public void removeLoggerRepositoryEventListener(
    LoggerRepositoryEventListener listener);

  /**
    Add a {@link LoggerEventListener} to the repository. The  listener
    will be called when repository events occur.
    @since 1.3*/
  public void addLoggerEventListener(LoggerEventListener listener);

  
  /**
    Remove a {@link LoggerEventListener} from the repository.
    @since 1.3*/
  public void removeLoggerEventListener(LoggerEventListener listener);

  /**
     Is the repository disabled for a given level? The answer depends
     on the repository threshold and the <code>level</code>
     parameter. See also {@link #setThreshold} method.  */
  boolean isDisabled(int level);


  /**
   * Get the name of this logger repository.
   * @since 1.3
   */
  public String getName();

  
  /**
   * A logger repository is a named entity.
   * @since 1.3
   */
  public void setName(String repoName);

  /**
   * Set the repository-wide threshold. All logging requests below the threshold
   * are immediately dropped. By default, the threshold is set to 
   * <code>Level.ALL</code> which has the lowest possible rank.  
   * 
   * <p>The repository-wide threshold acts as a global on off switch. It avoids
   * the hierarchy walk, hence improving performance. In future log4j versions
   * the speed of the hiearchy walk will be significantly improved obliviating
   * the need for this method.
   * 
   * <p>The repository-wide threshold is a deprecated feature.
   * 
   * @deprecated Will be removed with no replacement. 
   * */
  public void setThreshold(Level level);
  
  /**
      Another form of {@link #setThreshold(Level)} accepting a string
      parameter instead of a <code>Level</code>. */
  public void setThreshold(String val);

  public void emitNoAppenderWarning(Category cat);
  
  /**
     Get the repository-wide threshold. See {@link
     #setThreshold(Level)} for an explanation. */
  public Level getThreshold();

  public Logger getLogger(String name);

  public Logger getLogger(String name, LoggerFactory factory);

  public Logger getRootLogger();

  /**
   * Is the current configuration of the reposiroty in its original (pristine)
   * state?
   * 
   * @since 1.3
   */
  public boolean isPristine();
  
  /**
   *  Set the pristine flag. 
   *  @see #isPristine 
   *  @since 1.3
   */
  public void setPristine(boolean state);
  
  public abstract Logger exists(String name);

  public abstract void shutdown();

  public Enumeration getCurrentLoggers();

  /**
     @deprecated Please use {@link #getCurrentLoggers} instead.  */
  public Enumeration getCurrentCategories();

  public abstract void resetConfiguration();

  /**
    Requests that a appender added event be sent to any registered
    {@link LoggerEventListener}.
    @param logger The logger to which the appender was added.
    @param appender The appender added to the logger.
    @since 1.3*/
  public abstract void fireAddAppenderEvent(Logger logger, Appender appender);

  /**
    Requests that a appender removed event be sent to any registered
    {@link LoggerEventListener}.
    @param logger The logger from which the appender was removed.
    @param appender The appender removed from the logger.
    @since 1.3*/
  public abstract void fireRemoveAppenderEvent(
    Logger logger, Appender appender);

  /**
    Requests that a level changed event be sent to any registered
    {@link LoggerEventListener}.
    @param logger The logger which changed levels.
    @since 1.3*/
  public abstract void fireLevelChangedEvent(Logger logger);

  /**
    Requests that a configuration changed event be sent to any registered
    {@link LoggerRepositoryEventListener}.
    @param logger The logger which changed levels.
    @since 1.3*/
  public abstract void fireConfigurationChangedEvent();
  
  /**
   * Return the PluginRegisty for this LoggerRepository.
   * @since 1.3
   */
  public PluginRegistry getPluginRegistry();

  /** 
   * Get the properties specific for this repository.
   * @since 1.3
   */
  public Map getProperties();

  /** 
   * Get the property of this repository.
   * @since 1.3
   */
  public String getProperty(String key);

  /** 
   * Set a property of this repository.
   * @since 1.3
   */
  public void setProperty(String key, String value);
  
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4178.java