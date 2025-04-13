error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3082.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3082.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3082.java
text:
```scala
i@@f (listener != null || listenerList.getListenerCount()>0) {

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

package org.apache.log4j.net;

import org.apache.log4j.Logger;
import org.apache.log4j.plugins.Pauseable;
import org.apache.log4j.plugins.Receiver;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.LoggingEvent;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import java.net.Socket;

import java.util.EventListener;

import javax.swing.event.EventListenerList;


// Contributors:  Moses Hohman <mmhohman@rainbow.uchicago.edu>

/**
   Read {@link LoggingEvent} objects sent from a remote client using
   Sockets (TCP). These logging events are logged according to local
   policy, as if they were generated locally.

   <p>For example, the socket node might decide to log events to a
   local file and also resent them to a second socket node.

    @author  Ceki G&uuml;lc&uuml;
    @author  Paul Smith <psmith@apache.org>

    @since 0.8.4
*/
public class SocketNode implements Runnable, Pauseable {
  static Logger logger = Logger.getLogger(SocketNode.class);
  private boolean paused;
  private Socket socket;
  private LoggerRepository hierarchy;
  private Receiver receiver;
  private SocketNodeEventListener listener;
  private EventListenerList listenerList = new EventListenerList();

  /**
    Constructor for socket and logger repository. */
  public SocketNode(Socket socket, LoggerRepository hierarchy) {
    this.socket = socket;
    this.hierarchy = hierarchy;
  }

  /**
    Constructor for socket and reciever. */
  public SocketNode(Socket socket, Receiver receiver) {
    this.socket = socket;
    this.receiver = receiver;
  }

  /**
   * Set the event listener on this node.
   *
   * @deprecated Now supports mutliple listeners, this method
   * simply invokes the removeSocketNodeEventListener() to remove
   * the listener, and then readds it.
   */
  public void setListener(SocketNodeEventListener _listener) {
    removeSocketNodeEventListener(_listener);
    addSocketNodeEventListener(_listener);
  }

  /**
   * Adds the listener to the list of listeners to be notified of the
   * respective event
   * @param listener the listener to add to the list
   */
  public void addSocketNodeEventListener(SocketNodeEventListener listener) {
    listenerList.add(SocketNodeEventListener.class, listener);
  }

  /**
   * Removes the registered Listener from this instances list of
   * listeners.  If the listener has not been registered, then invoking
   * this method has no effect.
   *
   * @param listener the SocketNodeEventListener to remove
   */
  public void removeSocketNodeEventListener(SocketNodeEventListener listener) {
    listenerList.remove(SocketNodeEventListener.class, listener);
  }

  public void run() {
    LoggingEvent event;
    Logger remoteLogger;
    Exception listenerException = null;
    ObjectInputStream ois = null;

    try {
      ois =
        new ObjectInputStream(
          new BufferedInputStream(socket.getInputStream()));
    } catch (Exception e) {
      ois = null;
      listenerException = e;
      logger.error("Exception opening ObjectInputStream to " + socket, e);
    }

    if (ois != null) {
      String remoteInfo =
        socket.getInetAddress().getHostName() + ":" + socket.getPort();

      /**
       * notify the listener that the socket has been
       * opened and this SocketNode is ready and waiting
       */
      fireSocketOpened(remoteInfo);

      try {
        while (true) {
          // read an event from the wire
          event = (LoggingEvent) ois.readObject();

          // store the known remote info in an event property
          event.setProperty("log4j.remoteSourceInfo", remoteInfo);

          // if configured with a receiver, tell it to post the event
          if (!isPaused()) {
            if ((receiver != null)) {
              receiver.doPost(event);

              // else post it via the hierarchy
            } else {
              // get a logger from the hierarchy. The name of the logger
              // is taken to be the name contained in the event.
              remoteLogger = hierarchy.getLogger(event.getLoggerName());

              //event.logger = remoteLogger;
              // apply the logger-level filter
              if (event
                .getLevel()
                .isGreaterOrEqual(remoteLogger.getEffectiveLevel())) {
                // finally log the event as if was generated locally
                remoteLogger.callAppenders(event);
              }
            }
          } else {
            //we simply discard this event.
          }
        }
      } catch (java.io.EOFException e) {
        logger.info("Caught java.io.EOFException closing conneciton.");
        listenerException = e;
      } catch (java.net.SocketException e) {
        logger.info("Caught java.net.SocketException closing conneciton.");
        listenerException = e;
      } catch (IOException e) {
        logger.info("Caught java.io.IOException: " + e);
        logger.info("Closing connection.");
        listenerException = e;
      } catch (Exception e) {
        logger.error("Unexpected exception. Closing connecition.", e);
        listenerException = e;
      }
    }

    // close the socket
    try {
      if (ois != null) {
        ois.close();
      }
    } catch (Exception e) {
      //logger.info("Could not close connection.", e);
    }

    // send event to listener, if configured
    if (listener != null) {
      fireSocketClosedEvent(listenerException);
    }
  }

  /**
   * Notifies all registered listeners regarding the closing of the Socket
   * @param listenerException
   */
  private void fireSocketClosedEvent(Exception listenerException) {
    EventListener[] listeners =
      listenerList.getListeners(SocketNodeEventListener.class);

    for (int i = 0; i < listeners.length; i++) {
      SocketNodeEventListener snel = (SocketNodeEventListener) listeners[i];

      if (snel != null) {
        snel.socketClosedEvent(listenerException);
      }
    }
  }

  /**
   * Notifies all registered listeners regarding the opening of a Socket
   * @param remoteInfo
   */
  private void fireSocketOpened(String remoteInfo) {
    EventListener[] listeners =
      listenerList.getListeners(SocketNodeEventListener.class);

    for (int i = 0; i < listeners.length; i++) {
      SocketNodeEventListener snel = (SocketNodeEventListener) listeners[i];

      if (snel != null) {
        snel.socketOpened(remoteInfo);
      }
    }
  }

  public void setPaused(boolean paused) {
    this.paused = paused;
  }

  public boolean isPaused() {
    return this.paused;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3082.java