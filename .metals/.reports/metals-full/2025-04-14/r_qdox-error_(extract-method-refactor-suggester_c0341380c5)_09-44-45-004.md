error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1970.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1970.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1970.java
text:
```scala
s@@uite.addTest(TestConfiguration.clientServerDecorator(baseSuite("LargeDataLocksTest:client")));

/**
 *  Derby - Class org.apache.derbyTesting.functionTests.tests.jdbcapi.LargeDataLocksTest
 *  
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.derbyTesting.functionTests.tests.jdbcapi;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.derbyTesting.junit.BaseJDBCTestCase;
import org.apache.derbyTesting.junit.CleanDatabaseTestSetup;
import org.apache.derbyTesting.junit.TestConfiguration;
import org.apache.derbyTesting.junit.Utilities;

public class LargeDataLocksTest extends BaseJDBCTestCase {

    public LargeDataLocksTest(String name) {
        super(name);
    }

    /**
     * Test that ResultSet.getCharacterStream does not hold locks after the
     * ResultSet is closed
     * 
     * @throws SQLException
     * @throws IOException
     */
    public void testGetCharacterStream() throws SQLException, IOException {
        // getCharacterStream() no locks expected after retrieval
        int numChars = 0;
        Statement stmt = createStatement();
        String sql = "SELECT bc from t1";
        // First with getCharacterStream
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        java.io.Reader characterStream = rs.getCharacterStream(1);
        // Extract all the characters
        int read = characterStream.read();
        while (read != -1) {
            read = characterStream.read();
            numChars++;
        }
        assertEquals(38000, numChars);
        rs.close();
        assertEquals(0, countLocks());
        commit();
    }

    /**
     * Verify that getBytes does not hold locks after ResultSet is closed.
     * 
     * @throws SQLException
     */
    public void testGetBytes() throws SQLException {
        // getBytes() no locks expected after retrieval
        Statement stmt = createStatement();
        String sql = "SELECT bincol from t1";
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        byte[] value = rs.getBytes(1);
        assertEquals(38000, value.length);
        rs.close();
        assertEquals(0, countLocks());
        commit();

    }

    /**
     * Verify that getBinaryStream() does not hold locks after retrieval
     * 
     * @throws SQLException
     * @throws IOException
     */
    public void testGetBinaryStream() throws SQLException, IOException {
        int numBytes = 0;
        getConnection().setAutoCommit(false);
        Statement stmt = createStatement();
        String sql = "SELECT bincol from t1";
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        InputStream stream = rs.getBinaryStream(1);
        int read = stream.read();
        while (read != -1) {
            read = stream.read();
            numBytes++;
        }
        assertEquals(38000, numBytes);
        rs.close();
        assertEquals(0, countLocks());
        commit();
    }

    /**
     * Test that ResultSet.getString() does not hold locks after the ResultSet
     * is closed
     * 
     * @throws SQLException
     * @throws IOException
     */
    public void testGetString() throws SQLException, IOException {
        // getString() no locks expected after retrieval
        Statement stmt = createStatement();
        String sql = "SELECT bc from t1";
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        String value = rs.getString(1);
        assertEquals(38000, value.length());
        rs.close();
        assertEquals(0, countLocks());
        commit();
    }

    /**
     * Create a new connection and count the number of locks held.
     * 
     * @return number of locks held
     * 
     * @throws SQLException
     */
    public int countLocks() throws SQLException {
        Connection conn = openDefaultConnection();
        String sql;
        Statement stmt = conn.createStatement();

        sql = "Select count(*) from new org.apache.derby.diag.LockTable() as LT";
        ResultSet lockrs = stmt.executeQuery(sql);
        lockrs.next();
        int count = lockrs.getInt(1);
        lockrs.close();
        stmt.close();
        conn.close();
        return count;
    }

    public static Test baseSuite(String name) {

        TestSuite suite = new TestSuite(name);
        suite.addTestSuite(LargeDataLocksTest.class);

        return new CleanDatabaseTestSetup(suite) {

            /**
             * Create and populate table
             * 
             * @see org.apache.derbyTesting.junit.CleanDatabaseTestSetup#decorateSQL(java.sql.Statement)
             */
            protected void decorateSQL(Statement s) throws SQLException {
                Connection conn = getConnection();
                conn.setAutoCommit(false);
                PreparedStatement ps = null;
                String sql;

                sql = "CREATE TABLE t1 (bc CLOB(1M), bincol BLOB(1M), datalen int)";
                s.executeUpdate(sql);

                // Insert big and little values
                sql = "INSERT into t1 values(?,?,?)";
                ps = conn.prepareStatement(sql);

                ps.setCharacterStream(1, new java.io.StringReader(Utilities
                        .repeatChar("a", 38000)), 38000);
                ps.setBytes(2, Utilities.repeatChar("a", 38000).getBytes());
                ps.setInt(3, 38000);
                ps.executeUpdate();
                ps.close();
                conn.commit();
            }
        };
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("LargeDataLocksTest");
        suite.addTest(baseSuite("LargeDataLocksTest:embedded"));
        // Disable for client until DERBY-2892 is fixed
        //suite.addTest(TestConfiguration.clientServerDecorator(baseSuite("LargeDataLocksTest:client")));
        return suite;

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1970.java