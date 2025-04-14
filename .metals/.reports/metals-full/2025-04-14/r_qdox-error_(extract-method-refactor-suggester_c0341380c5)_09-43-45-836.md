error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2075.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2075.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2075.java
text:
```scala
r@@s.beforeFirst();

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

package org.apache.log4j.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.plugins.Pauseable;
import org.apache.log4j.plugins.Receiver;
import org.apache.log4j.scheduler.Job;
import org.apache.log4j.scheduler.Scheduler;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;


/**
 *
 * @author Scott Deboy <sdeboy@apache.org>
 * @author Ceki G&uuml;lc&uuml;
 *
 */
public class DBReceiver
       extends Receiver
       implements Pauseable {
  
  /**
   * By default we refresh data every 1000 milliseconds.
   * @see #setRefreshMillis
   */
  static int DEFAULT_REFRESH_MILLIS = 1000;
  
  ConnectionSource connectionSource;
  int refreshMillis = DEFAULT_REFRESH_MILLIS;
  DBReceiverJob receiverJob;
  boolean paused = false;
  
  public void activateOptions() {
    if (connectionSource != null) {
      LogLog.info("activating connectionSource");
      connectionSource.activateOptions();
      receiverJob = new DBReceiverJob();
      Scheduler scheduler = LogManager.getSchedulerInstance();
      scheduler.schedule(receiverJob, System.currentTimeMillis()+500, refreshMillis);
      
    } else {
      throw new IllegalStateException("DBAppender cannot function without a connection source");
    }
  }


  public void setRefreshMillis(int refreshMillis) {
    this.refreshMillis = refreshMillis;
  }


  public int getRefreshMillis() {
    return refreshMillis;
  }


  /**
   * @return Returns the connectionSource.
   */
  public ConnectionSource getConnectionSource() {
    return connectionSource;
  }


  /**
   * @param connectionSource The connectionSource to set.
   */
  public void setConnectionSource(ConnectionSource connectionSource) {
    this.connectionSource = connectionSource;
  }


  /* (non-Javadoc)
   * @see org.apache.log4j.plugins.Plugin#shutdown()
   */
  public void shutdown() {
    LogLog.info("removing receiverJob from the Scheduler.");
    Scheduler scheduler = LogManager.getSchedulerInstance();
    scheduler.delete(receiverJob);
  }


  /* (non-Javadoc)
   * @see org.apache.log4j.plugins.Pauseable#setPaused(boolean)
   */
  public void setPaused(boolean paused) {
    this.paused = paused;
  }

  /* (non-Javadoc)
   * @see org.apache.log4j.plugins.Pauseable#isPaused()
   */
  public boolean isPaused() {
    return paused;
  }

  /**
   * Actual retrieval of data is made by the instance of DBReceiverJob associated
   * with DBReceiver.
   * 
   * @author Ceki G&uuml;lc&uuml;
   */
  class DBReceiverJob implements Job {
    
    long lastId = 1;
    
   
    public void execute() {
      LogLog.info("DBReceiverJob.execute() called");
      try {
        Logger logger;
        LoggerRepository loggerRepository = getLoggerRepository();
        Connection connection = connectionSource.getConnection();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT sequence_number, timestamp, rendered_message, ");
        sql.append("logger_name, level_string, ndc, thread_name, ");
        sql.append("reference_flag, event_id from logging_event ");
        // have subsequent SELECTs start from we left off last time
        sql.append(" WHERE event_id > "+lastId);
        sql.append(" ORDER BY event_id ASC");
        
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql.toString());
        rs.beforeFirst();
        
        while (rs.next()) {
          LoggingEvent event = new LoggingEvent();
          long id;
          event.setSequenceNumber(rs.getLong(1));
          event.setTimeStamp(rs.getLong(2));
          event.setRenderedMessage(rs.getString(3));
          event.setLoggerName(rs.getString(4));

          String levelStr = rs.getString(5);

          // TODO CG The conversion of levelStr should be more general
          Level level = Level.toLevel(levelStr);
          event.setLevel(level);
          event.setNDC(rs.getString(6));
          event.setThreadName(rs.getString(7));

          short mask = rs.getShort(8);

          id = rs.getLong(9);
          lastId = id;
          
          //event.setProperty("id", Long.toString(id));
          if ((mask & DBHelper.PROPERTIES_EXIST) != 0) {
            getProperties(connection, id, event);
          }

          if ((mask & DBHelper.EXCEPTION_EXISTS) != 0) {
            getException(connection, id, event);
          }

          if (! DBReceiver.this.isPaused()) {
            DBReceiver.this.doPost(event);
          }
          
        }
      } catch (SQLException sqle) {
        LogLog.error("Problem receiving events", sqle);
      }
    }
  }

  /**
   * Retrieve the event properties from the logging_event_property table.
   * 
   * @param connection
   * @param id
   * @param event
   * @throws SQLException
   */
  void getProperties(Connection connection, long id, LoggingEvent event)
         throws SQLException {
    String sql = "SELECT mapped_key, mapped_value FROM logging_event_property WHERE event_id='" + id + "'";
    Statement statement = connection.createStatement();
    ResultSet rs = statement.executeQuery(sql);
    rs.first();

    while (rs.next()) {
      String key = rs.getString(1);
      String value = rs.getString(2);
      event.setProperty(key, value);
    }
  }

  /**
   * Retrieve the exception string representation from the logging_event_exception
   * table.
   * 
   * @param connection
   * @param id
   * @param event
   * @throws SQLException
   */
  void getException(Connection connection, long id, LoggingEvent event)
         throws SQLException {
    String sql = "SELECT i, trace_line FROM logging_event_exception where event_id='" + id + "'";
    Statement statement = connection.createStatement();
    ResultSet rs = statement.executeQuery(sql);
    
    // if rs has results, then extract the exception
    if(rs.last()) {
      int len = rs.getRow();
      String[] strRep = new String[len];      
      rs.beforeFirst();
      while (rs.next()) {
        int i = rs.getShort(1);
        strRep[i] = rs.getString(2);
      }
      // we've filled strRep, we now attach it to the event
      event.setThrowableInformation(new ThrowableInformation(strRep));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2075.java