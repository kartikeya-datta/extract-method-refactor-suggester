error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2579.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2579.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2579.java
text:
```scala
i@@f ("org.slf4j.impl.Log4jLoggerFactory".equals(slf4jImpl)) {

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.solr.logging;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.core.SolrResourceLoader;
import org.apache.solr.logging.jul.JulWatcher;
import org.apache.solr.logging.log4j.Log4jWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A Class to monitor Logging events and hold N events in memory
 * 
 * This is abstract so we can support both JUL and Log4j (and other logging platforms)
 */
public abstract class LogWatcher<E> {

  private static final Logger log = LoggerFactory.getLogger(LogWatcher.class);
  
  protected CircularList<E> history;
  protected long last = -1;
  
  /**
   * @return The implementation name
   */
  public abstract String getName();
  
  /**
   * @return The valid level names for this framework
   */
  public abstract List<String> getAllLevels();
  
  /**
   * Sets the log level within this framework
   */
  public abstract void setLogLevel(String category, String level);
  
  /**
   * @return all registered loggers
   */
  public abstract Collection<LoggerInfo> getAllLoggers();
  
  public abstract void setThreshold(String level);
  public abstract String getThreshold();

  public void add(E event, long timstamp) {
    history.add(event);
    last = timstamp;
  }
  
  public long getLastEvent() {
    return last;
  }
  
  public int getHistorySize() {
    return (history==null) ? -1 : history.getBufferSize();
  }
  
  public SolrDocumentList getHistory(long since, AtomicBoolean found) {
    if(history==null) {
      return null;
    }
    
    SolrDocumentList docs = new SolrDocumentList();
    Iterator<E> iter = history.iterator();
    while(iter.hasNext()) {
      E e = iter.next();
      long ts = getTimestamp(e);
      if(ts == since) {
        if(found!=null) {
          found.set(true);
        }
      }
      if(ts>since) {
        docs.add(toSolrDocument(e));
      }
    }
    docs.setNumFound(docs.size()); // make it not look too funny
    return docs;
  }
  
  public abstract long getTimestamp(E event);
  public abstract SolrDocument toSolrDocument(E event);
  
  public abstract void registerListener(ListenerConfig cfg);

  public void reset() {
    history.clear();
    last = -1;
  }

  /**
   * Create and register a LogWatcher.
   *
   * JUL and Log4j watchers are supported out-of-the-box.  You can register your own
   * LogWatcher implementation via the plugins architecture
   *
   * @param config a LogWatcherConfig object, containing the configuration for this LogWatcher.
   * @param loader a SolrResourceLoader, to be used to load plugin LogWatcher implementations.
   *               Can be null if one of the built-in implementations is being used.
   *
   * @return a LogWatcher configured for the container's logging framework
   */
  public static LogWatcher newRegisteredLogWatcher(LogWatcherConfig config, SolrResourceLoader loader) {

    if (!config.isEnabled()) {
      log.info("A LogWatcher is not enabled");
      return null;
    }

    LogWatcher logWatcher = createWatcher(config, loader);

    if (logWatcher != null) {
      if (config.getWatcherSize() > 0) {
        log.info("Registering Log Listener [{}]", logWatcher.getName());
        logWatcher.registerListener(config.asListenerConfig());
      }
    }

    return logWatcher;
  }

  private static LogWatcher createWatcher(LogWatcherConfig config, SolrResourceLoader loader) {

    String fname = config.getLoggingClass();
    String slf4jImpl;

    try {
      slf4jImpl = StaticLoggerBinder.getSingleton().getLoggerFactoryClassStr();
      log.info("SLF4J impl is " + slf4jImpl);
      if (fname == null) {
        if (slf4jImpl.indexOf("Log4j") > 0) {
          fname = "Log4j";
        } else if (slf4jImpl.indexOf("JDK") > 0) {
          fname = "JUL";
        }
      }
    }
    catch (Throwable e) {
      log.warn("Unable to read SLF4J version.  LogWatcher will be disabled: " + e);
      return null;
    }

    if (fname == null) {
      log.info("No LogWatcher configured");
      return null;
    }

    if ("JUL".equalsIgnoreCase(fname))
      return new JulWatcher(slf4jImpl);
    if ("Log4j".equals(fname))
      return new Log4jWatcher(slf4jImpl);

    try {
      return loader != null ? loader.newInstance(fname, LogWatcher.class) : null;
    }
    catch (Throwable e) {
      log.warn("Unable to load LogWatcher {}: {}", fname, e);
    }

    return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2579.java