error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1526.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1526.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1526.java
text:
```scala
L@@ogLog.info("id " + eventId + ", key " + key + ", value " + value);

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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.db.dialect.MySQLDialect;
import org.apache.log4j.db.dialect.OracleDialect;
import org.apache.log4j.db.dialect.PostgreSQLDialect;
import org.apache.log4j.db.dialect.SQLDialect;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;


/**
 *
 * @author Ceki G&uuml;lc&uuml;
 *
 */
public class DBAppender
       extends AppenderSkeleton {
  ConnectionSource connectionSource;
  SQLDialect sqlDialect;

  public void activateOptions() {
    if (connectionSource != null) {
      connectionSource.activateOptions();
    } else {
      throw new IllegalStateException("DBAppender cannot function without a connection source");
    }

    switch (connectionSource.getSQLDialect()) {
    case ConnectionSource.POSTGRES_DIALECT :
      sqlDialect = new PostgreSQLDialect();
      break;
    case ConnectionSource.MYSQL_DIALECT :
      sqlDialect = new MySQLDialect();
      break;
    case ConnectionSource.ORACLE_DIALECT :
      sqlDialect = new OracleDialect();
      break;
    }
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


  protected void append(LoggingEvent event) {
 
    try {
      Connection connection = connectionSource.getConnection();
      connection.setAutoCommit(false);

//      sequence_number BIGINT NOT NULL,
//      timestamp         BIGINT NOT NULL,
//      rendered_message  TEXT NOT NULL,
//      logger_name       VARCHAR(254) NOT NULL,
//      level_string      VARCHAR(254) NOT NULL,
//      ndc               TEXT,
//      thread_name       VARCHAR(254),
//      id                INT NOT NULL AUTO_INCREMENT PRIMARY KEY
      StringBuffer sql = new StringBuffer();
      sql.append("INSERT INTO logging_event (");
      sql.append("sequence_number, timestamp, rendered_message, ");
      sql.append("logger_name, level_string, ndc, thread_name, reference_flag) ");
      sql.append(" VALUES (?, ?, ? ,?, ?, ?, ?, ?)");

      PreparedStatement insertStatement = connection.prepareStatement(sql.toString());
      insertStatement.setLong(1, event.getSequenceNumber());
      insertStatement.setLong(2, event.getTimeStamp());
      insertStatement.setString(3, event.getRenderedMessage());
      insertStatement.setString(4, event.getLoggerName());
      insertStatement.setString(5, event.getLevel().toString());
      insertStatement.setString(6, event.getNDC());
      insertStatement.setString(7, event.getThreadName());
      insertStatement.setShort(8, DBHelper.computeReferenceMask(event));
      
      int updateCount = insertStatement.executeUpdate();

      if (updateCount != 1) {
        LogLog.warn("Failed to insert loggingEvent");
      }

      Statement idStatement = connection.createStatement();
      idStatement.setMaxRows(1);

      ResultSet rs = idStatement.executeQuery(sqlDialect.getSelectInsertId());
      rs.first();

      int eventId = rs.getInt(1);
//      LogLog.info("inserted id is " + eventId);

//      event_id        INT NOT NULL,
//      mapped_key        VARCHAR(254) NOT NULL,
//      mapped_value      VARCHAR(254),

      Set propertiesKeys = event.getPropertyKeySet();
      if (propertiesKeys.size() > 0) {
        String insertPropertiesSQL = "INSERT INTO  logging_event_property (event_id, mapped_key, mapped_value) VALUES (?, ?, ?)";
        PreparedStatement insertPropertiesStatement = connection.prepareStatement(insertPropertiesSQL);

        for (Iterator i = propertiesKeys.iterator(); i.hasNext();) {
          String key = (String)i.next();
          String value = (String)event.getProperty(key);
          LogLog.debug("id " + eventId + ", key " + key + ", value " + value);
          insertPropertiesStatement.setInt(1, eventId);
          insertPropertiesStatement.setString(2, key);
          insertPropertiesStatement.setString(3, value);
          insertPropertiesStatement.addBatch();
        }
        insertPropertiesStatement.executeBatch();
      }

      String[] strRep = event.getThrowableStrRep();
      if(strRep != null) {
        LogLog.info("Logging an exception");
//        CREATE TABLE logging_event_exception (
//          event_id         INT NOT NULL,
//          i                SMALLINT NOT NULL,
//          trace_line       VARCHAR(254) NOT NULL)
        String insertExceptionSQL = "INSERT INTO  logging_event_exception (event_id, i, trace_line) VALUES (?, ?, ?)";
        PreparedStatement insertExceptionStatement = connection.prepareStatement(insertExceptionSQL);
        
        for (short i = 0; i < strRep.length; i++) {
          insertExceptionStatement.setInt(1, eventId);
          insertExceptionStatement.setShort(2, i);
          insertExceptionStatement.setString(3, strRep[i]);
          insertExceptionStatement.addBatch();
        }
        insertExceptionStatement.executeBatch();
      }
      connection.commit();
    } catch (SQLException sqle) {
      LogLog.error("problem appending event", sqle);
    }
  }



  public void close() {
    // TODO Auto-generated method st  
  }


  /*
   * The DBAppender does not require a layout.
   */
  public boolean requiresLayout() {
    return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1526.java