error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3785.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3785.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3785.java
text:
```scala
s@@qle = e;

/*
 *
 * Derby - Class org.apache.derbyTesting.functionTests.util.CleanDatabase
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific 
 * language governing permissions and limitations under the License.
 */
package org.apache.derbyTesting.junit;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;

/**
 * Test decorator that cleans a database on setUp and
 * tearDown to provide a test with a consistent empty
 * database as a starting point.
 * <P>
 * Tests can extend to provide a decorator that defines
 * some schema items and then have CleanDatabaseTestSetup
 * automatically clean them up by implementing the decorateSQL method.. 
 * As an example:
 * <code>
        return new CleanDatabaseTestSetup(suite) {
            protected void decorateSQL(Statement s) throws SQLException {

                s.execute("CREATE TABLE T (I INT)");
                s.execute("CREATE INDEX TI ON T(I)")

            }
        };
 * </code>
 * 
 */
public class CleanDatabaseTestSetup extends BaseJDBCTestSetup {

    /**
     * Decorator this test with the cleaner
     */
    public CleanDatabaseTestSetup(Test test) {
        super(test);
    }

    /**
     * Clean the default database using the default connection
     * and calls the decorateSQL to allow sub-classes to
     * initialize their schema requirments.
     */
    protected void setUp() throws Exception {
        Connection conn = getConnection();
        conn.setAutoCommit(false);
        CleanDatabaseTestSetup.cleanDatabase(conn);  
        
        Statement s = conn.createStatement();
        decorateSQL(s);

        s.close();
        conn.commit();
        conn.close();
    }
    
    /**
     * Sub-classes can override this method to execute
     * SQL statements executed at setUp time once the
     * database has been cleaned.
     * Once this method returns the statement will be closed,
     * commit called and the connection closed. The connection
     * returned by s.getConnection() is the default connection
     * and is in auto-commit false mode.
     * <BR>
     * This implementation does nothing. Sub-classes need not call it.
     * @throws SQLException
     */
    protected void decorateSQL(Statement s) throws SQLException
    {
        // nothing in the default case.
    }

    /**
     * Clean the default database using the default connection.
     */
    protected void tearDown() throws Exception {
        Connection conn = getConnection();
        conn.setAutoCommit(false);
        CleanDatabaseTestSetup.cleanDatabase(conn);       
        super.tearDown();
    }

    /**
     * Clean a complete database
     * @param conn Connection to be used, must not be in auto-commit mode.
     * @throws SQLException database error
     */
     public static void cleanDatabase(Connection conn) throws SQLException {
         clearProperties(conn);
         removeObjects(conn);
         compressObjects(conn);
     }
     
     /**
      * Set of database properties that will be set to NULL (unset)
      * as part of cleaning a database.
      */
     private static final String[] CLEAR_DB_PROPERTIES =
     {
         "derby.database.classpath",
     };
     
     /**
      * Clear all database properties.
      */
     private static void clearProperties(Connection conn) throws SQLException {

         PreparedStatement ps = conn.prepareCall(
           "CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(?, NULL)");
         
         for (int i = 0; i < CLEAR_DB_PROPERTIES.length; i++)
         {
             ps.setString(1, CLEAR_DB_PROPERTIES[i]);
             ps.executeUpdate();
         }
         ps.close();
         conn.commit();
     }
     
     
     /**
      * Remove all objects in all schemas from the database.
      */
     private static void removeObjects(Connection conn) throws SQLException {
   
        DatabaseMetaData dmd = conn.getMetaData();

        SQLException sqle = null;
        // Loop a number of arbitary times to catch cases
        // where objects are dependent on objects in
        // different schemas.
        for (int count = 0; count < 5; count++) {
            // Fetch all the user schemas into a list
            List schemas = new ArrayList();
            ResultSet rs = dmd.getSchemas();
            while (rs.next()) {
    
                String schema = rs.getString("TABLE_SCHEM");
                if (schema.startsWith("SYS"))
                    continue;
                if (schema.equals("SQLJ"))
                    continue;
                if (schema.equals("NULLID"))
                    continue;
    
                schemas.add(schema);
            }
            rs.close();
    
            // DROP all the user schemas.
            sqle = null;
            for (Iterator i = schemas.iterator(); i.hasNext();) {
                String schema = (String) i.next();
                try {
                    JDBC.dropSchema(dmd, schema);
                } catch (SQLException e) {
                    sqle = null;
                }
            }
            // No errors means all the schemas we wanted to
            // drop were dropped, so nothing more to do.
            if (sqle == null)
                return;
        }
        throw sqle;
    }

     /**
      * Set of objects that will be compressed as part of cleaning a database.
      */
     private static final String[] COMPRESS_DB_OBJECTS =
     {
         "SYS.SYSDEPENDS",
     };
     
     /**
      * Compress the objects in the database.
      * 
      * @param conn the db connection
      * @throws SQLException database error
      */
     private static void compressObjects(Connection conn) throws SQLException {
    	 
    	 CallableStatement cs = conn.prepareCall
    	     ("CALL SYSCS_UTIL.SYSCS_INPLACE_COMPRESS_TABLE(?, ?, 1, 1, 1)");
    	 
    	 for (int i = 0; i < COMPRESS_DB_OBJECTS.length; i++)
    	 {
    		 int delim = COMPRESS_DB_OBJECTS[i].indexOf(".");
             cs.setString(1, COMPRESS_DB_OBJECTS[i].substring(0, delim) );
             cs.setString(2, COMPRESS_DB_OBJECTS[i].substring(delim+1) );
             cs.execute();
    	 }
    	 
    	 cs.close();
    	 conn.commit();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3785.java