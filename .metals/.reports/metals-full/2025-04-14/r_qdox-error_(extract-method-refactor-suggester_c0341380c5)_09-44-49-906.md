error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5799.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5799.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5799.java
text:
```scala
g@@etLogger().debug("DBReceiverJob.execute() called");

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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.Constants;
import org.apache.log4j.scheduler.Job;
import org.apache.log4j.spi.ComponentBase;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;
import org.apache.log4j.spi.location.LocationInfo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 * Actual retrieval of data is made by the instance of DBReceiverJob associated
 * with DBReceiver.
 * 
 * @author Ceki G&uuml;lc&uuml;
 */
class DBReceiverJob extends ComponentBase implements Job {

  long lastId = 0;

  DBReceiver parentDBReceiver;

  DBReceiverJob(DBReceiver parent) {
    parentDBReceiver = parent;
  }

  public void execute() {
    getLogger().info("DBReceiverJob.execute() called");

    Connection connection = null;

    try {
      Logger logger;
      LoggerRepository loggerRepository = parentDBReceiver
          .getLoggerRepository();
      connection = parentDBReceiver.connectionSource.getConnection();

      StringBuffer sql = new StringBuffer();
      sql.append("SELECT ");
      sql.append("sequence_number, ");
      sql.append("timestamp, ");
      sql.append("rendered_message, ");
      sql.append("logger_name, ");
      sql.append("level_string, ");
      sql.append("ndc, ");
      sql.append("thread_name, ");
      sql.append("reference_flag, ");
      sql.append("caller_filename, ");
      sql.append("caller_class, ");
      sql.append("caller_method, ");
      sql.append("caller_line, ");
      sql.append("event_id ");
      sql.append("FROM logging_event ");

      // have subsequent SELECTs start from we left off last time
      sql.append(" WHERE event_id > " + lastId);
      sql.append(" ORDER BY event_id ASC");

      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery(sql.toString());
      //rs.beforeFirst();

      while (rs.next()) {
        LoggingEvent event = new LoggingEvent();

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

        String fileName = rs.getString(9);
        String className = rs.getString(10);
        String methodName = rs.getString(11);
        String lineNumber = rs.getString(12).trim();

        if (fileName.equals(LocationInfo.NA)) {
          event.setLocationInformation(LocationInfo.NA_LOCATION_INFO);
        } else {
          event.setLocationInformation(new LocationInfo(fileName, className,
              methodName, lineNumber));
        }

        long id = rs.getLong(13);
        //LogLog.info("Received event with id=" + id);
        lastId = id;

        // Scott asked for this info to be
        event.setProperty(Constants.LOG4J_ID_KEY, Long.toString(id));

        if ((mask & DBHelper.PROPERTIES_EXIST) != 0) {
          getProperties(connection, id, event);
        }

        if ((mask & DBHelper.EXCEPTION_EXISTS) != 0) {
          getException(connection, id, event);
        }

        if (!parentDBReceiver.isPaused()) {
          parentDBReceiver.doPost(event);
        }
      } // while
      statement.close();
      statement = null;
    } catch (SQLException sqle) {
      getLogger().error("Problem receiving events", sqle);
    } finally {
      closeConnection(connection);
    }
  }

  void closeConnection(Connection connection) {
    if (connection != null) {
      try {
        //LogLog.warn("closing the connection. ", new Exception("x"));
        connection.close();
      } catch (SQLException sqle) {
        // nothing we can do here
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
    String sql = "SELECT mapped_key, mapped_value FROM logging_event_property WHERE event_id='"
        + id + "'";

    Statement statement = null;

    try {
      statement = connection.createStatement();

      ResultSet rs = statement.executeQuery(sql);
      //rs.beforeFirst();

      while (rs.next()) {
        String key = rs.getString(1);
        String value = rs.getString(2);
        event.setProperty(key, value);
      }
    } finally {
      if (statement != null) {
        statement.close();
      }
    }
  }

  /**
   * Retrieve the exception string representation from the
   * logging_event_exception table.
   * 
   * @param connection
   * @param id
   * @param event
   * @throws SQLException
   */
  void getException(Connection connection, long id, LoggingEvent event)
      throws SQLException {
    String sql = "SELECT trace_line FROM logging_event_exception where event_id='"
        + id + "' ORDER by i ASC";

    Statement statement = null;

    try {
      statement = connection.createStatement();

      ResultSet rs = statement.executeQuery(sql);

      Vector v = new Vector();

      while (rs.next()) {
        //int i = rs.getShort(1);
        v.add(rs.getString(1));
      }

      int len = v.size();
      String[] strRep = new String[len];
      for (int i = 0; i < len; i++) {
        strRep[i] = (String) v.get(i);
      }
      // we've filled strRep, we now attach it to the event
      event.setThrowableInformation(new ThrowableInformation(strRep));
    } finally {
      if (statement != null) {
        statement.close();
      }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5799.java