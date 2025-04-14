error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2290.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2290.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2290.java
text:
```scala
s@@t.execute("drop procedure za");

/*

   Derby - Class org.apache.derbyTesting.functionTests.tests.jdbcapi.nullSQLText

   Copyright 2001, 2005 The Apache Software Foundation or its licensors, as applicable.

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

package org.apache.derbyTesting.functionTests.tests.jdbcapi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DatabaseMetaData;
import java.sql.ResultSetMetaData;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.derby.tools.ij;
import org.apache.derbyTesting.functionTests.util.TestUtil;
import org.apache.derbyTesting.functionTests.util.JDBCTestDisplayUtil;

/**
 * Test of null strings in prepareStatement and execute 
 * result set.  Also test comments in SQL text that is
 * passed to an "execute" call.
 *
 * @author peachey
 */

public class nullSQLText { 
	public static void main(String[] args) {
		Connection con;
		PreparedStatement  ps;
		Statement s;
		String nullString = null;
	
		System.out.println("Test nullSQLText starting");
    
		try
		{
			// use the ij utility to read the property file and
			// make the initial connection.
			ij.getPropertyArg(args);
			con = ij.startJBMS();
			con.setAutoCommit(true); // make sure it is true
			s = con.createStatement();

			try
			{
				// test null String in prepared statement
				System.out.println("Test prepareStatement with null argument");
				ps = con.prepareStatement(nullString);
			}
			catch (SQLException e) {
				System.out.println("FAIL -- expected exception");
				dumpSQLExceptions(e);
			}
			try
			{
				// test null String in execute statement
				System.out.println("Test execute with null argument");
				s.execute(nullString);
			}
			catch (SQLException e) {
				System.out.println("FAIL -- expected exception");
				dumpSQLExceptions(e);
			}
			try
			{
				// test null String in execute query statement
				System.out.println("Test executeQuery with null argument");
				s.executeQuery(nullString);
			}
			catch (SQLException e) {
				System.out.println("FAIL -- expected exception");
				dumpSQLExceptions(e);
			}
			try
			{
				// test null String in execute update statement
				System.out.println("Test executeUpdate with null argument");
				s.executeUpdate(nullString);
			}
			catch (SQLException e) {
				System.out.println("FAIL -- expected exception");
				dumpSQLExceptions(e);
			}

			// Test comments in statements.
			derby522(s);

			con.close();
		}
		catch (SQLException e) {
			dumpSQLExceptions(e);
			e.printStackTrace(System.out);
		}
		catch (Throwable e) {
			System.out.println("FAIL -- unexpected exception:");
			e.printStackTrace(System.out);
		}
		
		System.out.println("Test nullSQLText finished");
    }
	static private void dumpSQLExceptions (SQLException se) {
		while (se != null) {
            JDBCTestDisplayUtil.ShowCommonSQLException(System.out, se);			
	         se = se.getNextException();
		}
	}


	/* ****
	 * Derby-522: When a statement with comments at the front
	 * is passed through to an "execute" call, the client throws
	 * error X0Y79 ("executeUpdate cannot be called with a statement
	 * that returns a result set").  The same thing works fine
	 * against Derby embedded.  This method executes several
	 * statements that have comments preceding them; with the
	 * fix for DERBY-522, these should all either pass or
	 * throw the correct syntax errors (i.e. the client should
	 * behave the same way as embedded).
	 */
	private static void derby522(Statement st) throws Exception
	{
		System.out.println("Starting test for DERBY-522.");

		st.execute("create table t1 (i int)");
		st.execute("insert into t1 values 1, 2, 3, 4, 5, 6, 7");
		st.execute("create procedure za() language java external name " +
			"'org.apache.derbyTesting.functionTests.util.ProcedureTest.zeroArg'" +
			" parameter style java");
		
		// These we expect to fail with syntax errors, as in embedded mode.
		testCommentStmt(st, " --", true);
		testCommentStmt(st, " -- ", true);
		testCommentStmt(st, " -- This is a comment \n --", true);
		testCommentStmt(
			st,
			" -- This is a comment\n --And another\n -- Andonemore",
			true);

		// These we expect to return valid results for embedded and
		// Derby Client (as of DERBY-522 fix); for JCC, these will
		// fail.
		testCommentStmt(st, " --\nvalues 2, 4, 8", TestUtil.isJCCFramework());
		testCommentStmt(
			st,
			" -- This is \n -- \n --3 comments\nvalues 8",
			TestUtil.isJCCFramework());
		testCommentStmt(
			st,
			" -- This is a comment\n --And another\n -- Andonemore\nvalues (2,3)",
			TestUtil.isJCCFramework());
		testCommentStmt(st,
			" -- This is a comment\n select i from t1",
			TestUtil.isJCCFramework());
		testCommentStmt(st,
			" --singleword\n insert into t1 values (8)",
			TestUtil.isJCCFramework());
		testCommentStmt(st,
			" --singleword\ncall za()",
			TestUtil.isJCCFramework());
		testCommentStmt(st,
			" -- leading comment\n(\nvalues 4, 8)",
			TestUtil.isJCCFramework());
		testCommentStmt(st,
			" -- leading comment\n\n(\n\n\rvalues 4, 8)",
			TestUtil.isJCCFramework());

		// While we're at it, test comments in the middle and end of the
		// statement.  Prior to the patch for DERBY-522, statements
		// ending with a comment threw syntax errors; that problem
		// was fixed with DERBY-522, as well, so all of these should now
		// succeed in all modes (embedded, Derby Client, and JCC).
		testCommentStmt(st, "select i from t1 -- This is a comment", false);
		testCommentStmt(st, "select i from t1\n -- This is a comment", false);
		testCommentStmt(st, "values 8, 4, 2\n --", false);
		testCommentStmt(st, "values 8, 4,\n -- middle comment\n2\n -- end", false);
		testCommentStmt(st, "values 8, 4,\n -- middle comment\n2\n -- end\n", false);

		// Clean-up.
		try {
			st.execute("drop table t1");
		} catch (SQLException se) {}
		try {
			st.execute("drop procedure proc1");
		} catch (SQLException se) {}

		st.close();
		System.out.println("DERBY-522 test completed.");
	}

	/* ****
	 * Helper method for derby522.
	 */
	private static void testCommentStmt(Statement st, String sql,
		boolean expectError) throws SQLException
	{

		try {

			System.out.println("[ Test Statement ]:\n" + sql);
			st.execute(sql);
			System.out.print("[ Results ]: ");
			ResultSet rs = st.getResultSet();
			if (rs != null) {
				while (rs.next())
					System.out.print(" " + rs.getInt(1));
				System.out.println();
			}
			else
				System.out.println("(NO RESULT SET)");

		} catch (SQLException se) {

			if (expectError)
				System.out.print("[ EXPECTED ERROR ]: ");
			else
				System.out.print("[ FAILED ]: ");
			dumpSQLExceptions(se);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2290.java