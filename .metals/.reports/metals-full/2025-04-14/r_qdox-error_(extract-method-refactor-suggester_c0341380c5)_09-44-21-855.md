error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1552.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1552.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1552.java
text:
```scala
t@@hreadPool.setMaxStopTimeMs(1000);

/**
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

package org.apache.solr.client.solrj.embedded;

import java.io.IOException;
import java.util.Random;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.servlet.SolrDispatchFilter;
import org.mortbay.component.LifeCycle;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.FilterHolder;
import org.mortbay.jetty.servlet.HashSessionIdManager;
import org.mortbay.log.Logger;
import org.mortbay.thread.QueuedThreadPool;

/**
 * Run solr using jetty
 * 
 * @since solr 1.3
 */
public class JettySolrRunner {
  Server server;

  FilterHolder dispatchFilter;

  String context;

  private String solrConfigFilename;
  private String schemaFilename;

  private boolean waitOnSolr = false;

  private int lastPort;

  private String shards;

  private String dataDir;
  
  private volatile boolean startedBefore = false;

  private String solrHome;

  private boolean stopAtShutdown;

  public JettySolrRunner(String solrHome, String context, int port) {
    this.init(solrHome, context, port, true);
  }

  public JettySolrRunner(String solrHome, String context, int port, String solrConfigFilename, String schemaFileName) {
    this.init(solrHome, context, port, true);
    this.solrConfigFilename = solrConfigFilename;
    this.schemaFilename = schemaFileName;
  }
  
  public JettySolrRunner(String solrHome, String context, int port,
      String solrConfigFilename, String schemaFileName, boolean stopAtShutdown) {
    this.init(solrHome, context, port, stopAtShutdown);
    this.solrConfigFilename = solrConfigFilename;
    this.schemaFilename = schemaFileName;
  }

  private void init(String solrHome, String context, int port, boolean stopAtShutdown) {
    this.context = context;
    server = new Server(port);
    this.solrHome = solrHome;
    this.stopAtShutdown = stopAtShutdown;
    server.setStopAtShutdown(stopAtShutdown);
    System.setProperty("solr.solr.home", solrHome);
    if (System.getProperty("jetty.testMode") != null) {
      // SelectChannelConnector connector = new SelectChannelConnector();
      // Normal SocketConnector is what solr's example server uses by default
      SocketConnector connector = new SocketConnector();
      connector.setPort(port);
      connector.setReuseAddress(true);
      QueuedThreadPool threadPool = (QueuedThreadPool) connector.getThreadPool();
      if (threadPool != null) {
        threadPool.setMaxStopTimeMs(100);
      }
      server.setConnectors(new Connector[] { connector });
      server.setSessionIdManager(new HashSessionIdManager(new Random()));
    } else {
      for (Connector connector : server.getConnectors()) {
        if (connector instanceof SocketConnector) {
          QueuedThreadPool threadPool = (QueuedThreadPool) ((SocketConnector) connector)
              .getThreadPool();
          if (threadPool != null) {
            threadPool.setMaxStopTimeMs(100);
          }
        }
      }
    }

    // Initialize the servlets
    final Context root = new Context(server, context, Context.SESSIONS);
    server.addLifeCycleListener(new LifeCycle.Listener() {

      public void lifeCycleStopping(LifeCycle arg0) {
        System.clearProperty("hostPort");
      }

      public void lifeCycleStopped(LifeCycle arg0) {}

      public void lifeCycleStarting(LifeCycle arg0) {
        synchronized (JettySolrRunner.this) {
          waitOnSolr = true;
          JettySolrRunner.this.notify();
        }
      }

      public void lifeCycleStarted(LifeCycle arg0) {
        lastPort = getLocalPort();
        System.setProperty("hostPort", Integer.toString(lastPort));
        if (solrConfigFilename != null) System.setProperty("solrconfig",
            solrConfigFilename);
        if (schemaFilename != null) System.setProperty("schema", 
            schemaFilename);
//        SolrDispatchFilter filter = new SolrDispatchFilter();
//        FilterHolder fh = new FilterHolder(filter);
        dispatchFilter = root.addFilter(SolrDispatchFilter.class, "*",
            Handler.REQUEST);
        if (solrConfigFilename != null) System.clearProperty("solrconfig");
        if (schemaFilename != null) System.clearProperty("schema");
        System.clearProperty("solr.solr.home");
        
      }

      public void lifeCycleFailure(LifeCycle arg0, Throwable arg1) {
        System.clearProperty("hostPort");
      }
    });

    // for some reason, there must be a servlet for this to get applied
    root.addServlet(Servlet404.class, "/*");

  }

  public FilterHolder getDispatchFilter() {
    return dispatchFilter;
  }

  public boolean isRunning() {
    return server.isRunning();
  }
  
  public boolean isStopped() {
    return server.isStopped();
  }

  // ------------------------------------------------------------------------------------------------
  // ------------------------------------------------------------------------------------------------

  public void start() throws Exception {
    start(true);
  }

  public void start(boolean waitForSolr) throws Exception {
    // if started before, make a new server
    if (startedBefore) {
      waitOnSolr = false;
      init(solrHome, context, lastPort, stopAtShutdown);
    } else {
      startedBefore = true;
    }
    
    if( dataDir != null) {
      System.setProperty("solr.data.dir", dataDir);
    }
    if(shards != null) {
      System.setProperty("shard", shards);
    }
    
    if (!server.isRunning()) {
      server.start();
    }
    synchronized (JettySolrRunner.this) {
      int cnt = 0;
      while (!waitOnSolr) {
        this.wait(100);
        if (cnt++ == 5) {
          throw new RuntimeException("Jetty/Solr unresponsive");
        }
      }
    }
    
    System.clearProperty("shard");
    System.clearProperty("solr.data.dir");
  }

  public void stop() throws Exception {
    if (!server.isStopped() && !server.isStopping()) {
      server.stop();
    }
    server.join();
  }

  /**
   * Returns the Local Port of the first Connector found for the jetty Server.
   * 
   * @exception RuntimeException if there is no Connector
   */
  public int getLocalPort() {
    Connector[] conns = server.getConnectors();
    if (0 == conns.length) {
      throw new RuntimeException("Jetty Server has no Connectors");
    }
    return conns[0].getLocalPort();
  }

  // --------------------------------------------------------------
  // --------------------------------------------------------------

  /**
   * This is a stupid hack to give jetty something to attach to
   */
  public static class Servlet404 extends HttpServlet {
    @Override
    public void service(HttpServletRequest req, HttpServletResponse res)
        throws IOException {
      res.sendError(404, "Can not find: " + req.getRequestURI());
    }
  }

  /**
   * A main class that starts jetty+solr This is useful for debugging
   */
  public static void main(String[] args) {
    try {
      JettySolrRunner jetty = new JettySolrRunner(".", "/solr", 8983);
      jetty.start();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void setShards(String shardList) {
     this.shards = shardList;
  }

  public void setDataDir(String dataDir) {
    this.dataDir = dataDir;
  }
}

class NoLog implements Logger {
  private static boolean debug = System.getProperty("DEBUG", null) != null;

  private final String name;

  public NoLog() {
    this(null);
  }

  public NoLog(String name) {
    this.name = name == null ? "" : name;
  }

  public boolean isDebugEnabled() {
    return debug;
  }

  public void setDebugEnabled(boolean enabled) {
    debug = enabled;
  }

  public void info(String msg, Object arg0, Object arg1) {
  }

  public void debug(String msg, Throwable th) {
  }

  public void debug(String msg, Object arg0, Object arg1) {
  }

  public void warn(String msg, Object arg0, Object arg1) {
  }

  public void warn(String msg, Throwable th) {
  }

  public Logger getLogger(String name) {
    if ((name == null && this.name == null)
 (name != null && name.equals(this.name)))
      return this;
    return new NoLog(name);
  }

  @Override
  public String toString() {
    return "NOLOG[" + name + "]";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1552.java