error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3169.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3169.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3169.java
text:
```scala
i@@f(this.repository == null) {

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

import org.apache.log4j.Layout;
import org.apache.log4j.helpers.OnlyOnceErrorHandler;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.OptionHandler;


/**
 * Abstract superclass of the other appenders in the package. This class
 * provides the code for common functionality, such as support for threshold
 * filtering and support for general filters.
 *
 * @author Ceki G&uuml;lc&uuml;
 *
 * @since 0.8.1
 */
public abstract class AppenderSkeleton implements Appender, OptionHandler {
  
  /*
   * An instance specific logger which must be accessed through the getLogger()
   * method. 
   */
  private Logger logger;
  
  /**
   * The layout variable does not need to be set if the appender
   * implementation has its own layout.
   */
  protected Layout layout;

  /**
   * Appenders are named.
   */
  protected String name;

  /**
   * There is no level threshold filtering by default.
   */
  protected Level threshold;

  /**
   * It is assumed and enforced that errorHandler is never null.
   */
  protected ErrorHandler errorHandler = new OnlyOnceErrorHandler();

  /**
   * The first filter in the filter chain. Set to <code>null</code> initially.
   */
  protected Filter headFilter;

  /**
   * The last filter in the filter chain.
   */
  protected Filter tailFilter;

  /**
   * Is this appender closed?
   */
  protected boolean closed = false;

  /**
   * The guard prevents an appender from repeatedly calling its own doAppend
   * method.
   */
  private boolean guard = false;

  
  private LoggerRepository repository;
  
  /**
   * Derived appenders should override this method if option structure
   * requires it.
   */
  public void activateOptions() {
  }

  /**
   * Add a filter to end of the filter list.
   *
   * @since 0.9.0
   */
  public void addFilter(Filter newFilter) {
    if (headFilter == null) {
      headFilter = newFilter;
      tailFilter = newFilter;
    } else {
      tailFilter.setNext(newFilter);
      tailFilter = newFilter;
    }
  }

  /**
   * Subclasses of <code>AppenderSkeleton</code> should implement this method
   * to perform actual logging. See also {@link #doAppend
   * AppenderSkeleton.doAppend} method.
   *
   * @since 0.9.0
   */
  protected abstract void append(LoggingEvent event);

  /**
   * Clear the filters chain.
   *
   * @since 0.9.0
   */
  public void clearFilters() {
    headFilter = null;
    tailFilter = null;
  }

  /**
   * Finalize this appender by calling the derived class' <code>close</code>
   * method.
   *
   * @since 0.8.4
   */
  public void finalize() {
    // An appender might be closed then garbage collected. There is no
    // point in closing twice.
    if (this.closed) {
      return;
    }

    getLogger().debug("Finalizing appender named [{}].", name);
    close();
  }

  /**
   * Return the currently set {@link ErrorHandler} for this Appender.
   *
   * @since 0.9.0
   */
  public ErrorHandler getErrorHandler() {
    return this.errorHandler;
  }

  /**
   * Returns the head Filter.
   *
   * @since 1.1
   */
  public Filter getFilter() {
    return headFilter;
  }

  /**
   * Return the first filter in the filter chain for this Appender. The return
   * value may be <code>null</code> if no is filter is set.
   */
  public final Filter getFirstFilter() {
    return headFilter;
  }

  /**
   * Returns the layout of this appender. The value may be null.
   */
  public Layout getLayout() {
    return layout;
  }

  /**
   * Returns the name of this FileAppender.
   */
  public final String getName() {
    return this.name;
  }

  /**
   * Returns this appenders threshold level. See the {@link #setThreshold}
   * method for the meaning of this option.
   *
   * @since 1.1
   */
  public Level getThreshold() {
    return threshold;
  }

  /**
   * Check whether the message level is below the appender's threshold. If
   * there is no threshold set, then the return value is always
   * <code>true</code>.
   */
  public boolean isAsSevereAsThreshold(Level level) {
    return ((threshold == null) || level.isGreaterOrEqual(threshold));
  }

  /**
   * This method performs threshold checks and invokes filters before
   * delegating actual logging to the subclasses specific {@link
   * AppenderSkeleton#append} method.
   */
  public synchronized void doAppend(LoggingEvent event) {
    // WARNING: The guard check MUST be the first statement in the
    // doAppend() method.
    
    // prevent re-entry.
    if (guard) {
      return;
    }

    try {
      guard = true;

      if (closed) {
        getLogger().error(
          "Attempted to append to closed appender named [{}].", name);

        return;
      }

      if (!isAsSevereAsThreshold(event.getLevel())) {
        return;
      }

      Filter f = this.headFilter;

FILTER_LOOP: 
      while (f != null) {
        switch (f.decide(event)) {
        case Filter.DENY:
          return;

        case Filter.ACCEPT:
          break FILTER_LOOP;

        case Filter.NEUTRAL:
          f = f.getNext();
        }
      }

      this.append(event);
    } finally {
      guard = false;
    }
  }

  /**
   * Set the {@link ErrorHandler} for this Appender.
   *
   * @since 0.9.0
   */
  public synchronized void setErrorHandler(ErrorHandler eh) {
    if (eh == null) {
      // We do not throw exception here since the cause is probably a
      // bad config file.
      getLogger().warn("You have tried to set a null error-handler.");
    } else {
      this.errorHandler = eh;
    }
  }

  /**
   * Set the layout for this appender. Note that some appenders have their own
   * (fixed) layouts or do not use one. For example, the {@link
   * org.apache.log4j.net.SocketAppender} ignores the layout set here.
   */
  public void setLayout(Layout layout) {
    this.layout = layout;
  }

  /**
   * Set the name of this Appender.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the threshold level. All log events with lower level than the
   * threshold level are ignored by the appender.
   * 
   * <p>
   * In configuration files this option is specified by setting the value of
   * the <b>Threshold</b> option to a level string, such as "DEBUG", "INFO"
   * and so on.
   * </p>
   *
   * @since 0.8.3
   */
  public void setThreshold(Level threshold) {
    this.threshold = threshold;
  }
  
  /**
   * Return an instance specific logger to be used by the Appender itself.
   * This logger is not intended to be used by Mrs. Piggy, our proverbial user,
   * hence the protected keyword.
   * 
   * @return instance specific logger
   */
  protected Logger getLogger() {
    if(logger == null) {
      logger = LogManager.getLogger(this.getClass().getName());
    }
    return logger;
  }
  
  /**
   * Returns the repository where this appender is attached. If not set, the
   * returned valyue may be null.
   * 
   * @return The repository where this appender is attached.
   * @since 1.3
   */
  protected LoggerRepository getLoggerRepository() {
    return repository;
  }
  

  /**
   * @see Appender#setLoggerRepository(LoggerRepository)
   */
  public void setLoggerRepository(LoggerRepository repository) throws IllegalStateException {
    if(repository == null) {
      throw new IllegalArgumentException("repository argument cannot be null");
    }
    if(this.repository != null) {
      this.repository = repository;
    } else {
      throw new IllegalStateException("Repository has been already set");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3169.java