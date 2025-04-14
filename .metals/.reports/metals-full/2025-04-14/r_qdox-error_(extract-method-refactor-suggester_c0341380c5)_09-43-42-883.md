error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3529.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3529.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,17]

error in qdox parser
file content:
```java
offset: 17
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3529.java
text:
```scala
abstract public v@@oid testList(Connection conn) throws SQLException;

/*

   Derby - Class org.apache.derbyTesting.functionTests.harness.procedure

   Copyright 2005 The Apache Software Foundation or its licensors, as applicable.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

package org.apache.derbyTesting.functionTests.tests.store;

import org.apache.derby.iapi.services.sanity.SanityManager;

import org.apache.derby.tools.ij;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



/**
Common utility functions that can be shared across store .java tests.
<p>
If more than one store tests wants a function, put it here rather than copy
it.  Hopefully going forward, with enough utility functions adding new store
tests will be easier.  New store tests should extend this test to pick
up access to utility routines - see OnlineCompressTest.java as an example.

**/
public abstract class BaseTest
{
    private static boolean debug_system_procedures_created = false;

    abstract void testList(Connection conn) throws SQLException;

    void runTests(String[] argv)
        throws Throwable
    {
   		ij.getPropertyArg(argv); 
        Connection conn = ij.startJBMS();
        System.out.println("conn from ij.startJBMS() = " + conn);
        conn.setAutoCommit(false);

        try
        {
            testList(conn);
        }
        catch (SQLException sqle)
        {
			org.apache.derby.tools.JDBCDisplayUtil.ShowSQLException(
                System.out, sqle);
			sqle.printStackTrace(System.out);
		}
    }

    public BaseTest()
    {
    }

    protected void beginTest(
    Connection  conn,
    String      str)
        throws SQLException
    {
        log("Beginning test: " + str);
        conn.commit();
    }

    protected void testProgress(
    String      str)
        throws SQLException
    {
        log("Executing test: " + str);
    }

    protected void endTest(
    Connection  conn,
    String      str)
        throws SQLException
    {
        conn.commit();
        log("Ending test: " + str);
    }

    protected void log(String   str)
    {
        System.out.println(str);
    }

    protected void logError(String   str)
    {
        System.out.println("ERROR: " + str);
    }

    /**
     * Simple wrapper to execute a sql string.
     **/
    public void executeQuery(
    Connection  conn,
    String      stmt_str,
    boolean     commit_query)
        throws SQLException
    {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(stmt_str);
        stmt.close();
        if (commit_query)
            conn.commit();
    }

    /**
     * Call consistency checker on the table.
     * <p>
     **/
    protected boolean checkConsistency(
    Connection  conn,
    String      schemaName,
    String      tableName)
		throws SQLException
    {
        Statement s = conn.createStatement();

        ResultSet rs = 
            s.executeQuery(
                "values SYSCS_UTIL.SYSCS_CHECK_TABLE('" + 
                schemaName + "', '" + 
                tableName  + "')");

        if (!rs.next())
        {
            if (SanityManager.DEBUG)
            {
                SanityManager.THROWASSERT("no value from values clause.");
            }
        }

        boolean consistent = rs.getBoolean(1);

        rs.close();

        conn.commit();

        return(consistent);
    }

    /**
     * Call consistency checker on all the tables.
     * <p>
     **/
    protected boolean checkAllConsistency(
    Connection  conn)
		throws SQLException
    {
        Statement s = conn.createStatement();

        ResultSet rs = 
            s.executeQuery(
                "select schemaname, tablename, SYSCS_UTIL.SYSCS_CHECK_TABLE(schemaname, tablename) " + 
                "from sys.systables a,  sys.sysschemas b where a.schemaid = b.schemaid");

        int table_count = 0;

        while (rs.next())
        {
            table_count++;
            if (rs.getInt(3) != 1)
            {
                System.out.println(
                    "Bad return from consistency check of " + 
                    rs.getString(1) + "." + rs.getString(2));
            }
        }

        if (table_count < 5)
        {
            // there are at least 5 system catalogs.
            System.out.println(
                "Something wrong with consistency check query, found only " + 
                table_count + " tables.");
        }

        rs.close();
        s.close();

        conn.commit();

        return(true);
    }

    /**
     * Create a system procedures to access SANE debug table routines.
     * <p>
     **/
    protected void createDebugSystemProcedures(
    Connection  conn)
		throws SQLException
    {
        Statement s = conn.createStatement();
        s.executeUpdate(
            "CREATE FUNCTION D_CONGLOMID_PRINT(DBNAME VARCHAR(128), CONGLOMID INT) RETURNS VARCHAR(32000) RETURNS NULL ON NULL INPUT EXTERNAL NAME 'org.apache.derby.impl.store.raw.data.D_DiagnosticUtil.diag_conglomid' LANGUAGE JAVA PARAMETER STYLE JAVA");
        s.executeUpdate(
            "CREATE FUNCTION DIAG_CONGLOMID(DBNAME VARCHAR(128), CONGLOMID INT) RETURNS VARCHAR(32000) RETURNS NULL ON NULL INPUT EXTERNAL NAME 'org.apache.derby.impl.store.raw.data.D_DiagnosticUtil.diag_conglomid' LANGUAGE JAVA PARAMETER STYLE JAVA");
        s.close();
        conn.commit();

        debug_system_procedures_created = true;
    }

    /**
     * Return string with table information.
     * <p>
     * Dumps summary store information about the table, also dumps extra
     * information about individual pages into the error log file.
     **/
    String dump_table(
    Connection  conn,
    String      schemaName,
    String      tableName,
    boolean     commit_transaction)
		throws SQLException
    {
        if (!debug_system_procedures_created)
            createDebugSystemProcedures(conn);

        // run the following query:
        //
        // select
        //     sys.systables.tablename,
        //     sys.sysconglomerates.conglomeratenumber,
        //     DIAG_CONGLOMID('wombat', conglomeratenumber)
        // from sys.systables, sys.sysconglomerates
        // where
        //     sys.systables.tableid = sys.sysconglomerates.tableid and
        //     sys.systables.schemaid = sys.sysconglomerates.schemaid and
        //     sys.systables.tablename = tableName;
        //
        // TODO - really should join with schemaName too.

        PreparedStatement ps = 
            conn.prepareStatement(
                "select sys.systables.tablename, sys.sysconglomerates.conglomeratenumber, DIAG_CONGLOMID('wombat', conglomeratenumber) from sys.systables, sys.sysconglomerates where sys.systables.tableid = sys.sysconglomerates.tableid and sys.systables.schemaid = sys.sysconglomerates.schemaid and sys.systables.tablename = ?");
        ps.setString(1, tableName);
        ResultSet rs = ps.executeQuery();

        if (!rs.next())
        {
            if (SanityManager.DEBUG)
            {
                SanityManager.THROWASSERT("no value from values clause.");
            }
        }

        String dump_table_info = rs.getString(3);

        rs.close();

        if (commit_transaction)
            conn.commit();

        return(dump_table_info);

    }

    /**
     * Get lock table.
     * <p>
     * Returns a single string with a dump of the entire lock table.
     * <p>
     *
	 * @return The lock table.
     *
     * @param conn                  The connection to use.
     * @param include_system_locks  If true include non-user locks like those
     *                              requested by background internal threads.
     *
     **/
    protected String get_lock_info(
    Connection  conn,
    boolean     include_system_locks)
		throws SQLException
    {
        // Run the following query to get the current locks in the system,
        // toggling the "t.type='UserTransaction'" based on 
        // include_system_locks input:
        //
        // select
        //     cast(l.xid as char(8)) as xid,
        //     cast(username as char(8)) as username,
        //     cast(t.type as char(8)) as trantype,
        //     cast(l.type as char(8)) as type,
        //     cast(lockcount as char(3)) as cnt,
        //     cast(mode as char(4)) as mode,
        //     cast(tablename as char(12)) as tabname,
        //     cast(lockname as char(10)) as lockname,
        //     state,
        //     status
        // from
        //     SYSCS_DIAG.LOCK_TABLE l  
        // right outer join SYSCS_DIAG.TRANSACTION_TABLE t
        //     on l.xid = t.xid where l.tableType <> 'S' and 
        //        t.type='UserTransaction'
        // order by
        //     tabname, type desc, mode, cnt, lockname;
        String lock_query = 
            "select cast(l.xid as char(8)) as xid, cast(username as char(8)) as username, cast(t.type as char(8)) as trantype, cast(l.type as char(8)) as type, cast(lockcount as char(3)) as cnt, cast(mode as char(4)) as mode, cast(tablename as char(12)) as tabname, cast(lockname as char(10)) as lockname, state, status from SYSCS_DIAG.LOCK_TABLE l right outer join SYSCS_DIAG.LOCK_TABLE t on l.xid = t.xid where l.tableType <> 'S' ";
        if (!include_system_locks)
            lock_query += "and t.type='UserTransaction' ";
        
        lock_query += "order by tabname, type desc, mode, cnt, lockname";

        PreparedStatement ps = conn.prepareStatement(lock_query);

        ResultSet rs = ps.executeQuery();

        String lock_output = 
        "xid     |username|trantype|type    |cnt|mode|tabname     |lockname  |state|status\n" +
        "---------------------------------------------------------------------------------\n";
        while (rs.next())
        {
            String username     = rs.getString(1);
            String trantype     = rs.getString(2);
            String type         = rs.getString(3);
            String lockcount    = rs.getString(4);
            String mode         = rs.getString(5);
            String tabname      = rs.getString(6);
            String lockname     = rs.getString(7);
            String state        = rs.getString(8);
            String status       = rs.getString(9);

            lock_output +=
                username + "|" +
                trantype + "|" +
                type     + "|" +
                lockcount+ "|" +
                mode     + "|" +
                tabname  + "|" +
                lockname + "|" +
                state    + "|" +
                status   + "\n";
        }

        rs.close();

        return(lock_output);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3529.java