error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1996.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1996.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1996.java
text:
```scala
r@@esult.put(colName, resultSet.getTimestamp(colName));

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
package org.apache.solr.handler.dataimport;

import static org.apache.solr.handler.dataimport.DataImportHandlerException.wrapAndThrow;
import static org.apache.solr.handler.dataimport.DataImportHandlerException.SEVERE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.sql.*;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * <p> A DataSource implementation which can fetch data using JDBC. </p> <p/> <p> Refer to <a
 * href="http://wiki.apache.org/solr/DataImportHandler">http://wiki.apache.org/solr/DataImportHandler</a> for more
 * details. </p>
 * <p/>
 * <b>This API is experimental and may change in the future.</b>
 *
 * @since solr 1.3
 */
public class JdbcDataSource extends
        DataSource<Iterator<Map<String, Object>>> {
  private static final Logger LOG = LoggerFactory.getLogger(JdbcDataSource.class);

  protected Callable<Connection> factory;

  private long connLastUsed = 0;

  private Connection conn;

  private Map<String, Integer> fieldNameVsType = new HashMap<String, Integer>();

  private boolean convertType = false;

  private int batchSize = FETCH_SIZE;

  private int maxRows = 0;

  @Override
  public void init(Context context, Properties initProps) {
    Object o = initProps.get(CONVERT_TYPE);
    if (o != null)
      convertType = Boolean.parseBoolean(o.toString());

    factory = createConnectionFactory(context, initProps);

    String bsz = initProps.getProperty("batchSize");
    if (bsz != null) {
      bsz = context.replaceTokens(bsz);
      try {
        batchSize = Integer.parseInt(bsz);
        if (batchSize == -1)
          batchSize = Integer.MIN_VALUE;
      } catch (NumberFormatException e) {
        LOG.warn("Invalid batch size: " + bsz);
      }
    }

    for (Map<String, String> map : context.getAllEntityFields()) {
      String n = map.get(DataImporter.COLUMN);
      String t = map.get(DataImporter.TYPE);
      if ("sint".equals(t) || "integer".equals(t))
        fieldNameVsType.put(n, Types.INTEGER);
      else if ("slong".equals(t) || "long".equals(t))
        fieldNameVsType.put(n, Types.BIGINT);
      else if ("float".equals(t) || "sfloat".equals(t))
        fieldNameVsType.put(n, Types.FLOAT);
      else if ("double".equals(t) || "sdouble".equals(t))
        fieldNameVsType.put(n, Types.DOUBLE);
      else if ("date".equals(t))
        fieldNameVsType.put(n, Types.DATE);
      else if ("boolean".equals(t))
        fieldNameVsType.put(n, Types.BOOLEAN);
      else if ("binary".equals(t))
        fieldNameVsType.put(n, Types.BLOB);
      else
        fieldNameVsType.put(n, Types.VARCHAR);
    }
  }

  protected Callable<Connection> createConnectionFactory(final Context context,
                                       final Properties initProps) {
//    final VariableResolver resolver = context.getVariableResolver();
    resolveVariables(context, initProps);
    final String jndiName = initProps.getProperty(JNDI_NAME);
    final String url = initProps.getProperty(URL);
    final String driver = initProps.getProperty(DRIVER);

    if (url == null && jndiName == null)
      throw new DataImportHandlerException(SEVERE,
              "JDBC URL or JNDI name has to be specified");

    if (driver != null) {
      try {
        DocBuilder.loadClass(driver, context.getSolrCore());
      } catch (ClassNotFoundException e) {
        wrapAndThrow(SEVERE, e, "Could not load driver: " + driver);
      }
    } else {
      if(jndiName == null){
        throw new DataImportHandlerException(SEVERE, "One of driver or jndiName must be specified in the data source");
      }
    }

    String s = initProps.getProperty("maxRows");
    if (s != null) {
      maxRows = Integer.parseInt(s);
    }

    return factory = new Callable<Connection>() {
      @Override
      public Connection call() throws Exception {
        LOG.info("Creating a connection for entity "
                + context.getEntityAttribute(DataImporter.NAME) + " with URL: "
                + url);
        long start = System.currentTimeMillis();
        Connection c = null;

        if (jndiName != null) {
          c = getFromJndi(initProps, jndiName);
        } else if (url != null) {
          try {
            c = DriverManager.getConnection(url, initProps);
          } catch (SQLException e) {
            // DriverManager does not allow you to use a driver which is not loaded through
            // the class loader of the class which is trying to make the connection.
            // This is a workaround for cases where the user puts the driver jar in the
            // solr.home/lib or solr.home/core/lib directories.
            Driver d = (Driver) DocBuilder.loadClass(driver, context.getSolrCore()).newInstance();
            c = d.connect(url, initProps);
          }
        }
        if (c != null) {
          try {
            initializeConnection(c, initProps);
          } catch (SQLException e) {
            try {
              c.close();
            } catch (SQLException e2) {
              LOG.warn("Exception closing connection during cleanup", e2);
            }

            throw new DataImportHandlerException(SEVERE, "Exception initializing SQL connection", e);
          }
        }
        LOG.info("Time taken for getConnection(): "
            + (System.currentTimeMillis() - start));
        return c;
      }

      private void initializeConnection(Connection c, final Properties initProps)
          throws SQLException {
        if (Boolean.parseBoolean(initProps.getProperty("readOnly"))) {
          c.setReadOnly(true);
          // Add other sane defaults
          c.setAutoCommit(true);
          c.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
          c.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
        }
        if (!Boolean.parseBoolean(initProps.getProperty("autoCommit"))) {
          c.setAutoCommit(false);
        }
        String transactionIsolation = initProps.getProperty("transactionIsolation");
        if ("TRANSACTION_READ_UNCOMMITTED".equals(transactionIsolation)) {
          c.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        } else if ("TRANSACTION_READ_COMMITTED".equals(transactionIsolation)) {
          c.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        } else if ("TRANSACTION_REPEATABLE_READ".equals(transactionIsolation)) {
          c.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
        } else if ("TRANSACTION_SERIALIZABLE".equals(transactionIsolation)) {
          c.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        } else if ("TRANSACTION_NONE".equals(transactionIsolation)) {
          c.setTransactionIsolation(Connection.TRANSACTION_NONE);
        }
        String holdability = initProps.getProperty("holdability");
        if ("CLOSE_CURSORS_AT_COMMIT".equals(holdability)) {
          c.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
        } else if ("HOLD_CURSORS_OVER_COMMIT".equals(holdability)) {
          c.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
        }
      }

      private Connection getFromJndi(final Properties initProps, final String jndiName) throws NamingException,
          SQLException {

        Connection c = null;
        InitialContext ctx =  new InitialContext();
        Object jndival =  ctx.lookup(jndiName);
        if (jndival instanceof javax.sql.DataSource) {
          javax.sql.DataSource dataSource = (javax.sql.DataSource) jndival;
          String user = (String) initProps.get("user");
          String pass = (String) initProps.get("password");
          if(user == null || user.trim().equals("")){
            c = dataSource.getConnection();
          } else {
            c = dataSource.getConnection(user, pass);
          }
        } else {
          throw new DataImportHandlerException(SEVERE,
                  "the jndi name : '"+jndiName +"' is not a valid javax.sql.DataSource");
        }
        return c;
      }
    };
  }

  private void resolveVariables(Context ctx, Properties initProps) {
    for (Map.Entry<Object, Object> entry : initProps.entrySet()) {
      if (entry.getValue() != null) {
        entry.setValue(ctx.replaceTokens((String) entry.getValue()));
      }
    }
  }

  @Override
  public Iterator<Map<String, Object>> getData(String query) {
    ResultSetIterator r = new ResultSetIterator(query);
    return r.getIterator();
  }

  private void logError(String msg, Exception e) {
    LOG.warn(msg, e);
  }

  private List<String> readFieldNames(ResultSetMetaData metaData)
          throws SQLException {
    List<String> colNames = new ArrayList<String>();
    int count = metaData.getColumnCount();
    for (int i = 0; i < count; i++) {
      colNames.add(metaData.getColumnLabel(i + 1));
    }
    return colNames;
  }

  private class ResultSetIterator {
    ResultSet resultSet;

    Statement stmt = null;

    List<String> colNames;

    Iterator<Map<String, Object>> rSetIterator;

    public ResultSetIterator(String query) {

      try {
        Connection c = getConnection();
        stmt = c.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        stmt.setFetchSize(batchSize);
        stmt.setMaxRows(maxRows);
        LOG.debug("Executing SQL: " + query);
        long start = System.currentTimeMillis();
        if (stmt.execute(query)) {
          resultSet = stmt.getResultSet();
        }
        LOG.trace("Time taken for sql :"
                + (System.currentTimeMillis() - start));
        colNames = readFieldNames(resultSet.getMetaData());
      } catch (Exception e) {
        wrapAndThrow(SEVERE, e, "Unable to execute query: " + query);
      }
      if (resultSet == null) {
        rSetIterator = new ArrayList<Map<String, Object>>().iterator();
        return;
      }

      rSetIterator = new Iterator<Map<String, Object>>() {
        @Override
        public boolean hasNext() {
          return hasnext();
        }

        @Override
        public Map<String, Object> next() {
          return getARow();
        }

        @Override
        public void remove() {/* do nothing */
        }
      };
    }

    private Iterator<Map<String, Object>> getIterator() {
      return rSetIterator;
    }

    private Map<String, Object> getARow() {
      if (resultSet == null)
        return null;
      Map<String, Object> result = new HashMap<String, Object>();
      for (String colName : colNames) {
        try {
          if (!convertType) {
            // Use underlying database's type information
            result.put(colName, resultSet.getObject(colName));
            continue;
          }

          Integer type = fieldNameVsType.get(colName);
          if (type == null)
            type = Types.VARCHAR;
          switch (type) {
            case Types.INTEGER:
              result.put(colName, resultSet.getInt(colName));
              break;
            case Types.FLOAT:
              result.put(colName, resultSet.getFloat(colName));
              break;
            case Types.BIGINT:
              result.put(colName, resultSet.getLong(colName));
              break;
            case Types.DOUBLE:
              result.put(colName, resultSet.getDouble(colName));
              break;
            case Types.DATE:
              result.put(colName, resultSet.getDate(colName));
              break;
            case Types.BOOLEAN:
              result.put(colName, resultSet.getBoolean(colName));
              break;
            case Types.BLOB:
              result.put(colName, resultSet.getBytes(colName));
              break;
            default:
              result.put(colName, resultSet.getString(colName));
              break;
          }
        } catch (SQLException e) {
          logError("Error reading data ", e);
          wrapAndThrow(SEVERE, e, "Error reading data from database");
        }
      }
      return result;
    }

    private boolean hasnext() {
      if (resultSet == null)
        return false;
      try {
        if (resultSet.next()) {
          return true;
        } else {
          close();
          return false;
        }
      } catch (SQLException e) {
        close();
        wrapAndThrow(SEVERE,e);
        return false;
      }
    }

    private void close() {
      try {
        if (resultSet != null)
          resultSet.close();
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
        logError("Exception while closing result set", e);
      } finally {
        resultSet = null;
        stmt = null;
      }
    }
  }

  private Connection getConnection() throws Exception {
    long currTime = System.currentTimeMillis();
    if (currTime - connLastUsed > CONN_TIME_OUT) {
      synchronized (this) {
        Connection tmpConn = factory.call();
        closeConnection();
        connLastUsed = System.currentTimeMillis();
        return conn = tmpConn;
      }

    } else {
      connLastUsed = currTime;
      return conn;
    }
  }

  @Override
  protected void finalize() throws Throwable {
    try {
      if(!isClosed){
        LOG.error("JdbcDataSource was not closed prior to finalize(), indicates a bug -- POSSIBLE RESOURCE LEAK!!!");
        close();
      }
    } finally {
      super.finalize();
    }
  }

  private boolean isClosed = false;

  @Override
  public void close() {
    try {
      closeConnection();
    } finally {
      isClosed = true;
    }
  }

  private void closeConnection()  {
    try {
      if (conn != null) {
        try {
          //SOLR-2045
          conn.commit();
        } catch(Exception ex) {
          //ignore.
        }
        conn.close();
      }
    } catch (Exception e) {
      LOG.error("Ignoring Error when closing connection", e);
    }
  }

  private static final long CONN_TIME_OUT = 10 * 1000; // 10 seconds

  private static final int FETCH_SIZE = 500;

  public static final String URL = "url";

  public static final String JNDI_NAME = "jndiName";

  public static final String DRIVER = "driver";

  public static final String CONVERT_TYPE = "convertType";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1996.java