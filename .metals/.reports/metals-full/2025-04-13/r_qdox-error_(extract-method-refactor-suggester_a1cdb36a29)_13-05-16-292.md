error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8970.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8970.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8970.java
text:
```scala
public v@@oid activate() {

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

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

import java.io.*;

import java.net.*;

import java.util.*;


/**
  <p>The TelnetAppender is a log4j appender that specializes in
  writing to a read-only socket.  The output is provided in a
  telnet-friendly way so that a log can be monitored over TCP/IP.
  Clients using telnet connect to the socket and receive log data.
  This is handy for remote monitoring, especially when monitoring a
  servlet.

  <p>Here is a list of the available configuration options:

  <table border=1>
   <tr>
   <th>Name</th>
   <th>Requirement</th>
   <th>Description</th>
   <th>Sample Value</th>
   </tr>

   <tr>
   <td>Port</td>
   <td>optional</td>
   <td>This parameter determines the port to use for announcing log events.  The default port is 23 (telnet).</td>
   <td>5875</td>
   </table>

   @author <a HREF="mailto:jay@v-wave.com">Jay Funnell</a>
*/
public class TelnetAppender extends AppenderSkeleton {
  private SocketHandler sh;
  private int port = 23;

  /**
      This appender requires a layout to format the text to the
      attached client(s). */
  public boolean requiresLayout() {
    return true;
  }

  /** all of the options have been set, create the socket handler and
      wait for connections. */
  public void activateOptions() {
    try {
      sh = new SocketHandler(port);
      sh.start();
    } catch (IOException e) {
      getLogger().error("Could not active TelnetAppender options for TelnetAppender named "+getName(), e);
      throw new IllegalStateException("Could not create a SocketHandler for TelnetAppender named "+getName());
    }
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  /** shuts down the appender. */
  public void close() {
      if(sh != null) {
          sh.finalize();
      }
  }

  /** Handles a log event.  For this appender, that means writing the
    message to each connected client.  */
  protected void append(LoggingEvent event) {
    if(sh == null || !sh.hasConnections()) {
      return;
    }

    sh.send(this.layout.format(event));

    if (layout.ignoresThrowable()) {
      String[] s = event.getThrowableStrRep();

      if (s != null) {
        int len = s.length;

        for (int i = 0; i < len; i++) {
          sh.send(s[i]);
          sh.send(Layout.LINE_SEP);
        }
      }
    }
  }

  //---------------------------------------------------------- SocketHandler:

  /** The SocketHandler class is used to accept connections from
      clients.  It is threaded so that clients can connect/disconnect
      asynchronously. */
  protected class SocketHandler extends Thread {
    private boolean done = false;
    private Vector writers = new Vector();
    private Vector connections = new Vector();
    private ServerSocket serverSocket;
    private int MAX_CONNECTIONS = 20;
    
    private String encoding = "UTF-8";
    
    public SocketHandler(int port) throws IOException {
      serverSocket = new ServerSocket(port);
    }

    /** make sure we close all network connections when this handler is destroyed. */
    public void finalize() {
      for (Enumeration e = connections.elements(); e.hasMoreElements();) {
        try {
          ((Socket) e.nextElement()).close();
        } catch (Exception ex) {
        }
      }

      try {
        serverSocket.close();
      } catch (Exception ex) {
      }

      done = true;
    }

    /** sends a message to each of the clients in telnet-friendly output. */
    public void send(String message) {
      boolean hasWriterError = false;
      for (Enumeration e = writers.elements(); e.hasMoreElements();) {
        PrintWriter writer = (PrintWriter) e.nextElement();
        writer.print(message);
        hasWriterError |= writer.checkError();

      }
      //
      //   if any writer had an error then
      //       check all writers and remove any bad ones
      if(hasWriterError) {
         for (int i = writers.size() - 1; i >= 0; i--) {
            if (((PrintWriter) writers.elementAt(i)).checkError()) {
                writers.remove(i);
                connections.remove(i);
            }
         }
      }
    }

    /**
        Continually accepts client connections.  Client connections
        are refused when MAX_CONNECTIONS is reached.
    */
    public void run() {
      while (!done) {
        try {
          Socket newClient = serverSocket.accept();

          // Bugzilla 26117: use an encoding to support EBCDIC machines
          // Could make encoding a JavaBean property or even make TelnetAppender
          // extend WriterAppender, which already has encoding support.
          PrintWriter pw = new PrintWriter(new OutputStreamWriter(newClient.getOutputStream(), encoding));

          if (connections.size() < MAX_CONNECTIONS) {
            connections.addElement(newClient);
            writers.addElement(pw);
            pw.print(
              "TelnetAppender v1.0 (" + connections.size()
              + " active connections)\r\n\r\n");
            pw.flush();
          } else {
            pw.print("Too many connections.\r\n");
            pw.flush();
            newClient.close();
          }
        } catch (Exception e) {
          getLogger().error("Encountered error while in SocketHandler loop.", e);
        }
      }
    }
    
    /**
     *  Determines if socket hander has any active connections.
     *  @returns true if any active connections.
     */
    public boolean hasConnections() {
       return connections.size() > 0;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8970.java