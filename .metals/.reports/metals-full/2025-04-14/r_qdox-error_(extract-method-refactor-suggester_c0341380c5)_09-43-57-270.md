error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7269.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7269.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7269.java
text:
```scala
e@@vent.initializeProperties();

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


// Contibutors:  Aaron Greenhouse <aarong@cs.cmu.edu>
//               Thomas Tuft Muller <ttm@online.no>
package org.apache.log4j;

import org.apache.log4j.helpers.AppenderAttachableImpl;
import org.apache.log4j.helpers.BoundedFIFO;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.AppenderAttachable;
import org.apache.log4j.spi.LoggingEvent;

import java.util.Enumeration;


/**
 * The AsyncAppender lets users log events asynchronously. It uses a bounded
 * buffer to store logging events.
 * 
 * <p>
 * The AsyncAppender will collect the events sent to it and then dispatch them
 * to all the appenders that are attached to it. You can attach multiple
 * appenders to an AsyncAppender.
 * </p>
 * 
 * <p>
 * The AsyncAppender uses a separate thread to serve the events in its bounded
 * buffer.
 * </p>
 * 
 * <p>
 * Refer to the results in {@link org.apache.log4j.performance.Logging} for
 * the impact of using this appender.
 * </p>
 * 
 * <p>
 * <b>Important note:</b> The <code>AsyncAppender</code> can only be script
 * configured using the {@link org.apache.log4j.xml.DOMConfigurator}.
 * </p>
 *
 * @author Ceki G&uuml;lc&uuml;
 *
 * @since 0.9.1
 */
public class AsyncAppender extends AppenderSkeleton
  implements AppenderAttachable {
  /**
   * The default buffer size is set to 128 events.
   */
  public static final int DEFAULT_BUFFER_SIZE = 128;

  //static Category cat = Category.getInstance(AsyncAppender.class.getName());
  private BoundedFIFO bf = new BoundedFIFO(DEFAULT_BUFFER_SIZE);
  AppenderAttachableImpl aai;
  private Dispatcher dispatcher;
  private boolean locationInfo = false;
  private boolean interruptedWarningMessage = false;

  public AsyncAppender() {
    // Note: The dispatcher code assumes that the aai is set once and
    // for all.
    aai = new AppenderAttachableImpl();
    dispatcher = new Dispatcher(bf, this);
    dispatcher.start();
  }

  public void addAppender(Appender newAppender) {
    synchronized (aai) {
      aai.addAppender(newAppender);
    }
  }

  public void append(LoggingEvent event) {
    // Set the NDC and thread name for the calling thread as these
    // LoggingEvent fields were not set at event creation time.
    event.getNDC();
    event.getThreadName();

    // Get a copy of this thread's MDC.
    event.createProperties();

    if (locationInfo) {
      event.getLocationInformation();
    }

    synchronized (bf) {
      while (bf.isFull()) {
        try {
          //LogLog.debug("Waiting for free space in buffer, "+bf.length());
          bf.wait();
        } catch (InterruptedException e) {
          if (!interruptedWarningMessage) {
            interruptedWarningMessage = true;
            LogLog.warn("AsyncAppender interrupted.", e);
          } else {
            LogLog.warn("AsyncAppender interrupted again.");
          }
        }
      }

      //cat.debug("About to put new event in buffer.");
      bf.put(event);

      if (bf.wasEmpty()) {
        //cat.debug("Notifying dispatcher to process events.");
        bf.notify();
      }
    }
  }

  /**
   * Close this <code>AsyncAppender</code> by interrupting the dispatcher
   * thread which will process all pending events before exiting.
   */
  public void close() {
    synchronized (this) {
      // avoid multiple close, otherwise one gets NullPointerException
      if (closed) {
        return;
      }

      closed = true;
    }

    // The following cannot be synchronized on "this" because the
    // dispatcher synchronizes with "this" in its while loop. If we
    // did synchronize we would systematically get deadlocks when
    // close was called.
    dispatcher.close();

    try {
      dispatcher.join();
    } catch (InterruptedException e) {
      LogLog.error(
        "Got an InterruptedException while waiting for the "
        + "dispatcher to finish.", e);
    }

    dispatcher = null;
    bf = null;
  }

  public Enumeration getAllAppenders() {
    synchronized (aai) {
      return aai.getAllAppenders();
    }
  }

  public Appender getAppender(String name) {
    synchronized (aai) {
      return aai.getAppender(name);
    }
  }

  /**
   * Returns the current value of the <b>LocationInfo</b> option.
   */
  public boolean getLocationInfo() {
    return locationInfo;
  }

  /**
   * Is the appender passed as parameter attached to this category?
   */
  public boolean isAttached(Appender appender) {
    return aai.isAttached(appender);
  }

  /**
   * The <code>AsyncAppender</code> does not require a layout. Hence, this
   * method always returns <code>false</code>.
   */
  public boolean requiresLayout() {
    return false;
  }

  public void removeAllAppenders() {
    synchronized (aai) {
      aai.removeAllAppenders();
    }
  }

  public void removeAppender(Appender appender) {
    synchronized (aai) {
      aai.removeAppender(appender);
    }
  }

  public void removeAppender(String name) {
    synchronized (aai) {
      aai.removeAppender(name);
    }
  }

  /**
   * The <b>LocationInfo</b> option takes a boolean value. By default, it is
   * set to false which means there will be no effort to extract the location
   * information related to the event. As a result, the event that will be
   * ultimately logged will likely to contain the wrong location information
   * (if present in the log format).
   * 
   * <p>
   * Location information extraction is comparatively very slow and should be
   * avoided unless performance is not a concern.
   * </p>
   */
  public void setLocationInfo(boolean flag) {
    locationInfo = flag;
  }

  /**
   * The <b>BufferSize</b> option takes a non-negative integer value. This
   * integer value determines the maximum size of the bounded buffer.
   * Increasing the size of the buffer is always safe. However, if an
   * existing buffer holds unwritten elements, then <em>decreasing the buffer
   * size will result in event loss.</em> Nevertheless, while script
   * configuring the AsyncAppender, it is safe to set a buffer size smaller
   * than the {@link #DEFAULT_BUFFER_SIZE default buffer size} because
   * configurators guarantee that an appender cannot be used before being
   * completely configured.
   */
  public void setBufferSize(int size) {
    bf.resize(size);
  }

  /**
   * Returns the current value of the <b>BufferSize</b> option.
   */
  public int getBufferSize() {
    return bf.getMaxSize();
  }
}


// ------------------------------------------------------------------------------
// ------------------------------------------------------------------------------
// ----------------------------------------------------------------------------
class Dispatcher extends Thread {
  private BoundedFIFO bf;
  private AppenderAttachableImpl aai;
  private boolean interrupted = false;
  AsyncAppender container;

  Dispatcher(BoundedFIFO bf, AsyncAppender container) {
    this.bf = bf;
    this.container = container;
    this.aai = container.aai;

    // It is the user's responsibility to close appenders before
    // exiting. 
    this.setDaemon(true);

    // set the dispatcher priority to lowest possible value
    this.setPriority(Thread.MIN_PRIORITY);
    this.setName("Dispatcher-" + getName());

    // set the dispatcher priority to MIN_PRIORITY plus or minus 2
    // depending on the direction of MIN to MAX_PRIORITY.
    //+ (Thread.MAX_PRIORITY > Thread.MIN_PRIORITY ? 1 : -1)*2);
  }

  void close() {
    synchronized (bf) {
      interrupted = true;

      // We have a waiting dispacther if and only if bf.length is
      // zero.  In that case, we need to give it a death kiss.
      if (bf.length() == 0) {
        bf.notify();
      }
    }
  }

  /**
   * The dispatching strategy is to wait until there are events in the buffer
   * to process. After having processed an event, we release the monitor
   * (variable bf) so that new events can be placed in the buffer, instead of
   * keeping the monitor and processing the remaining events in the buffer.
   * 
   * <p>
   * Other approaches might yield better results.
   * </p>
   */
  public void run() {
    //Category cat = Category.getInstance(Dispatcher.class.getName());
    LoggingEvent event;

    while (true) {
      synchronized (bf) {
        if (bf.length() == 0) {
          // Exit loop if interrupted but only if the the buffer is empty.
          if (interrupted) {
            //cat.info("Exiting.");
            break;
          }

          try {
            //LogLog.debug("Waiting for new event to dispatch.");
            bf.wait();
          } catch (InterruptedException e) {
            LogLog.error("The dispathcer should not be interrupted.");

            break;
          }
        }

        event = bf.get();

        if (bf.wasFull()) {
          //LogLog.debug("Notifying AsyncAppender about freed space.");
          bf.notify();
        }
      }
       // synchronized

      synchronized (container.aai) {
        if ((aai != null) && (event != null)) {
          aai.appendLoopOnAppenders(event);
        }
      }
    }
     // while

    // close and remove all appenders
    aai.removeAllAppenders();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7269.java