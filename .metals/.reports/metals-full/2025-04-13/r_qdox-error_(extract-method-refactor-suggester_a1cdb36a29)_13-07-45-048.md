error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/182.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/182.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/182.java
text:
```scala
s@@etDatabaseProperty("derby.authentication.server", "noSuchServer.invalid", conn);

/*

   Derby - Class 
       org.apache.derbyTesting.functionTests.tests.jdbcapi.InvalidLDAPServerAuthenticationTest

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package org.apache.derbyTesting.functionTests.tests.jdbcapi;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.derbyTesting.junit.BaseJDBCTestCase;
import org.apache.derbyTesting.junit.JDBC;
import org.apache.derbyTesting.junit.JDBCDataSource;
import org.apache.derbyTesting.junit.SecurityManagerSetup;
import org.apache.derbyTesting.junit.TestConfiguration;


public class InvalidLDAPServerAuthenticationTest extends BaseJDBCTestCase {

    /** Creates a new instance of the Test */
    public InvalidLDAPServerAuthenticationTest(String name) {
        super(name);
    }

    /**
     * Ensure all connections are not in auto commit mode.
     */
    protected void initializeConnection(Connection conn) throws SQLException {
        conn.setAutoCommit(false);
    }

    public static Test suite() {
        if (JDBC.vmSupportsJSR169())
            return new TestSuite("InvalidLDAPServerAuthenticationTest - cannot" +
                " run with JSR169 - missing functionality for " +
                "org.apache.derby.iapi.jdbc.AuthenticationService");
        
        // security manager would choke attempting to resolve to the invalid
        // LDAPServer, so run without
        TestSuite suite = new TestSuite("InvalidLDAPServerAuthenticationTest");
        suite.addTest(SecurityManagerSetup.noSecurityManager(baseSuite(
                "testInvalidLDAPServerConnectionError")));
        suite.addTest(TestConfiguration.clientServerDecorator(
                SecurityManagerSetup.noSecurityManager(
                baseSuite("testInvalidLDAPServerConnectionError"))));
        return suite;            
    }

    public static Test baseSuite(String name) {
        TestSuite suite = new TestSuite(name);
        Test test = new InvalidLDAPServerAuthenticationTest("testInvalidLDAPServerConnectionError");
        suite.addTest(test);

        // This test needs to run in a new single use database without connect
        // for shutdown after, as we're going to make the database unusable
        return TestConfiguration.singleUseDatabaseDecoratorNoShutdown(suite);
    }

    protected void setDatabaseProperty(
            String propertyName, String value, Connection conn) 
    throws SQLException {
        CallableStatement setDBP =  conn.prepareCall(
        "CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(?, ?)");
        setDBP.setString(1, propertyName);
        setDBP.setString(2, value);
        setDBP.execute();
        setDBP.close();
    }

    public void testInvalidLDAPServerConnectionError() throws SQLException {
        // setup 
        Connection conn = getConnection();
        // set the ldap properties
        setDatabaseProperty("derby.connection.requireAuthentication", "true", conn);
        setDatabaseProperty("derby.authentication.provider", "LDAP", conn);
        setDatabaseProperty("derby.authentication.server", "noSuchServer", conn);
        setDatabaseProperty("derby.authentication.ldap.searchBase", "o=dnString", conn);
        setDatabaseProperty("derby.authentication.ldap.searchFilter","(&(objectClass=inetOrgPerson)(uid=%USERNAME%))", conn);
        commit();
        conn.setAutoCommit(true);
        conn.close();
        // shutdown the database as system, so the properties take effect
        TestConfiguration.getCurrent().shutdownDatabase();
        String dbName = TestConfiguration.getCurrent().getDefaultDatabaseName();
        
        // actual test. 
        // first, try datasource connection
        DataSource ds = JDBCDataSource.getDataSource(dbName);
        try {
            ds.getConnection();
            fail("expected java.net.UnknownHostException for datasource");
        } catch (SQLException se) {
            assertSQLState("08004", se);
            // with network server, the java.net.UnknownHostException will be in 
            // derby.log, the client only gets a 08004 and somewhat misleading
            // warning ('Reason: userid or password invalid')
            if (usingEmbedded())
                assertTrue(se.getMessage().indexOf("java.net.UnknownHostException")>1);
        }
        // driver manager connection
        String url2 = TestConfiguration.getCurrent().getJDBCUrl(dbName);
        try {
            DriverManager.getConnection(url2,"user","password").close();
            fail("expected java.net.UnknownHostException for driver");
        } catch (SQLException se) {
            assertSQLState("08004", se);
            // with network server, the java.net.UnknownHostException will be in 
            // derby.log, the client only gets a 08004 and somewhat misleading
            // warning ('Reason: userid or password invalid')
            if (usingEmbedded())
                assertTrue(se.getMessage().indexOf("java.net.UnknownHostException")>1);
        }
        
        // we need to shutdown the system, or the failed connections
        // cling to db.lck causing cleanup to fail.
        // we *can* shutdown because we don't have authentication required
        // set at system level (only database level).
        shutdownSystem();
    }
    
    protected void shutdownSystem()throws SQLException {
        DataSource ds;
        if (usingEmbedded())
        {
            ds = JDBCDataSource.getDataSource();
            JDBCDataSource.clearStringBeanProperty(ds, "databaseName");
        }
        else
        {
            // note: with network server/client, you can't set the databaseName
            // to null, that results in error 08001 - Required DataSource
            // property databaseName not set.
            // so, we rely on passing of an empty string for databaseName,
            // which in the current code is interpreted as system shutdown.
            ds = JDBCDataSource.getDataSource("");
        }
        JDBCDataSource.setBeanProperty(ds, "shutdownDatabase", "shutdown");
        try {
            ds.getConnection();
        } catch (SQLException e) {
            //do nothing;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/182.java