error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9086.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9086.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9086.java
text:
```scala
public static final i@@nt DEFAULT_PORT = 9991;

/*
 * ============================================================================
 *                   The Apache Software License, Version 1.1
 * ============================================================================
 *
 *    Copyright (C) 1999 The Apache Software Foundation. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modifica-
 * tion, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of  source code must  retain the above copyright  notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include  the following  acknowledgment:  "This product includes  software
 *    developed  by the  Apache Software Foundation  (http://www.apache.org/)."
 *    Alternately, this  acknowledgment may  appear in the software itself,  if
 *    and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "log4j" and  "Apache Software Foundation"  must not be used to
 *    endorse  or promote  products derived  from this  software without  prior
 *    written permission. For written permission, please contact
 *    apache@apache.org.
 *
 * 5. Products  derived from this software may not  be called "Apache", nor may
 *    "Apache" appear  in their name,  without prior written permission  of the
 *    Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * This software  consists of voluntary contributions made  by many individuals
 * on  behalf of the Apache Software  Foundation.  For more  information on the
 * Apache Software Foundation, please see <http://www.apache.org/>.
 *
 */

package org.apache.log4j.net;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

import java.io.IOException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * 
 * 
 *  Sends log information as a UDP datagrams.
 *
 *  <p>The UDPAppender is meant to be used as a diagnostic logging tool
 *  so that logging can be monitored by a simple UDP client.
 *
 *  <p>Messages are not sent as LoggingEvent objects but as text after
 *  applying the designated Layout.
 *
 *  <p>The port and remoteHost properties can be set in configuration properties.
 *  By setting the remoteHost to a broadcast address any number of clients can
 *  listen for log messages.
 *
 *  <p>This was inspired and really extended/copied from {@link SocketAppender}.  Please
 *  see the docs for the proper credit to the authors of that class.
 *
 *  @author  <a href="mailto:kbrown@versatilesolutions.com">Kevin Brown</a>
 *  @author Scott Deboy <sdeboy@apache.org>
 */
public class UDPAppender extends AppenderSkeleton implements PortBased{
  /**
     The default port number for the UDP packets. (9991).
  */
  static final int DEFAULT_PORT = 9991;

  private static final int PACKET_LENGTH = 16384;

  /**
     The default reconnection delay (30000 milliseconds or 30 seconds).
  */
  static final int DEFAULT_RECONNECTION_DELAY = 30000;

  /**
     We remember host name as String in addition to the resolved
     InetAddress so that it can be returned via getOption().
  */
  String localMachine;
  String remoteHost;
  String log4japp;
  String overrideProperties = "true";
  InetAddress address;
  int port = DEFAULT_PORT;
  DatagramSocket outSocket;
  int reconnectionDelay = DEFAULT_RECONNECTION_DELAY;
  boolean locationInfo = false;
  int count = 0;
  private Connector connector;

  public UDPAppender() {
  }

  /**
     Sends UDP packets to the <code>address</code> and <code>port</code>.
  */
  public UDPAppender(InetAddress address, int port) {
    this.address = address;
    this.remoteHost = address.getHostName();
    this.port = port;
    connect(address, port);
  }

  /**
     Sends UDP packets to the <code>address</code> and <code>port</code>.
  */
  public UDPAppender(String host, int port) {
    this.port = port;
    this.address = getAddressByName(host);
    this.remoteHost = host;
    connect(address, port);
  }

  /**
     Open the UDP sender for the <b>RemoteHost</b> and <b>Port</b>.
  */
  public void activateOptions() {
    try {
      localMachine = InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException uhe) {
      try {
        localMachine = InetAddress.getLocalHost().getHostAddress();
      } catch (UnknownHostException uhe2) {
        localMachine = "unknown";
      }
    }

    //allow system property of log4japp to be primary
    if (log4japp == null) {
      log4japp = System.getProperty("log4japp");
    } else {
      if (System.getProperty("log4japp") != null) {
        log4japp = log4japp + "-" + System.getProperty("log4japp");
      }
    }

    //if not passed in, allow null app (app property won't be set)
    connect(address, port);
  }

  /**
     Close this appender.
     <p>This will mark the appender as closed and
     call then {@link #cleanUp} method.
  */
  public synchronized void close() {
    if (closed) {
      return;
    }

    this.closed = true;
    cleanUp();
  }

  /**
     Close the UDP Socket and release the underlying
     connector thread if it has been created
   */
  public void cleanUp() {
    if (outSocket != null) {
      try {
        outSocket.close();
      } catch (Exception e) {
        LogLog.error("Could not close outSocket.", e);
      }

      outSocket = null;
    }

    if (connector != null) {
      //LogLog.debug("Interrupting the connector.");      
      connector.interrupted = true;
      connector = null; // allow gc
    }
  }

  void connect(InetAddress address, int port) {
    if (this.address == null) {
      return;
    }

    try {
      // First, close the previous connection if any.
      cleanUp();
      outSocket = new DatagramSocket();
      outSocket.connect(address, port);
    } catch (IOException e) {
      LogLog.error(
        "Could not open UDP Socket for sending. We will try again later.", e);
      fireConnector();
    }
  }

  public void append(LoggingEvent event) {
    if (event == null) {
      return;
    }

    if (address == null) {
      errorHandler.error(
        "No remote host is set for UDPAppender named \"" + this.name + "\".");

      return;
    }

    if (outSocket != null) {
      //if the values already exist, don't set (useful when forwarding from a simplesocketserver
      if (
        (overrideProperties != null)
          && overrideProperties.equalsIgnoreCase("true")) {
        event.setProperty("log4jmachinename", localMachine);

        if (log4japp != null) {
          event.setProperty("log4japp", log4japp);
        }
      }

      try {
        StringBuffer buf=new StringBuffer(layout.format(event).trim());
        if (buf.length() < PACKET_LENGTH) {        
           buf.append(new char[PACKET_LENGTH - buf.length()]);
        }
        DatagramPacket dp =
           new DatagramPacket(buf.toString().getBytes("ASCII"), buf.length(), address, port);
        outSocket.send(dp);
      } catch (IOException e) {
        outSocket = null;
        LogLog.warn("Detected problem with UDP connection: " + e);

        if (reconnectionDelay > 0) {
          fireConnector();
        }
      }
    }
  }

  void fireConnector() {
    if (connector == null) {
      LogLog.debug("Starting a new connector thread.");
      connector = new Connector();
      connector.setDaemon(true);
      connector.setPriority(Thread.MIN_PRIORITY);
      connector.start();
    }
  }

  static InetAddress getAddressByName(String host) {
    try {
      return InetAddress.getByName(host);
    } catch (Exception e) {
      LogLog.error("Could not find address of [" + host + "].", e);

      return null;
    }
  }

  /**
     The UDPAppender uses layouts. Hence, this method returns
     <code>true</code>.
  */
  public boolean requiresLayout() {
    return true;
  }

  /**
     The <b>RemoteHost</b> option takes a string value which should be
     the host name or ipaddress to send the UDP packets.
   */
  public void setRemoteHost(String host) {
    address = getAddressByName(host);
    remoteHost = host;
  }

  /**
     Returns value of the <b>RemoteHost</b> option.
   */
  public String getRemoteHost() {
    return remoteHost;
  }

  /**
     The <b>App</b> option takes a string value which should be the name of the application getting logged.
     If property was already set (via system property), don't set here.
   */
  public void setLog4JApp(String log4japp) {
    this.log4japp = log4japp;
  }

  /**
     Returns value of the <b>App</b> option.
   */
  public String getLog4JApp() {
    return log4japp;
  }

  /**
     The <b>OverrideProperties</b> option allows configurations where the appender does not apply
     the machinename/appname properties - the properties will be used as provided.
   */
  public void setOverrideProperties(String overrideProperties) {
    this.overrideProperties = overrideProperties;
  }

  /**
     Returns value of the <b>OverrideProperties</b> option.
   */
  public String getOverrideProperties() {
    return overrideProperties;
  }

    /**
     The <b>Port</b> option takes a positive integer representing
     the port where UDP packets will be sent.
   */
  public void setPort(int port) {
    this.port = port;
  }

  /**
     Returns value of the <b>Port</b> option.
   */
  public int getPort() {
    return port;
  }

  /**
     The <b>ReconnectionDelay</b> option takes a positive integer
     representing the number of milliseconds to wait between each
     failed attempt to establish an outgoing socket. The default value of
     this option is 30000 which corresponds to 30 seconds.

     <p>Setting this option to zero turns off reconnection
     capability.
   */
  public void setReconnectionDelay(int delay) {
    this.reconnectionDelay = delay;
  }

  /**
     Returns value of the <b>ReconnectionDelay</b> option.
   */
  public int getReconnectionDelay() {
    return reconnectionDelay;
  }

  /**
     The Connector will retry the UDP socket.
     It does this by attempting to open a new UDP socket every
     <code>reconnectionDelay</code> milliseconds.

     <p>It stops trying whenever a connection is established. It will
     restart to try reconnect to the server when previpously open
     connection is droppped.

     @author  Ceki G&uuml;lc&uuml;
     @since 0.8.4
  */
  class Connector extends Thread {
    boolean interrupted = false;

    public void run() {
      DatagramSocket socket;

      while (!interrupted) {
        try {
          sleep(reconnectionDelay);
          LogLog.debug("Attempting to establish UDP Datagram Socket");
          socket = new DatagramSocket();

          synchronized (this) {
            outSocket = socket;
            connector = null;

            break;
          }
        } catch (InterruptedException e) {
          LogLog.debug("Connector interrupted. Leaving loop.");

          return;
        } catch (IOException e) {
          LogLog.debug("Could not establish an outgoing MulticastSocket." + e);
        }
      }

      //LogLog.debug("Exiting Connector.run() method.");
    }
  }

  /* (non-Javadoc)
   * @see org.apache.log4j.net.NetworkBased#isActive()
   */
  public boolean isActive() {
    // TODO handle active/inactive
    return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9086.java