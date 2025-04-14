error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2863.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2863.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2863.java
text:
```scala
a@@ssertSQLState(JDBC.vmSupportsJDBC4() ? "XIE0R" : "XIE0E", e);

/*

   Derby - Class org.apache.derbyTesting.functionTests.tests.tools.ImportExportTest

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

package org.apache.derbyTesting.functionTests.tests.tools;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import org.apache.derbyTesting.junit.BaseJDBCTestCase;
import org.apache.derbyTesting.junit.CleanDatabaseTestSetup;
import org.apache.derbyTesting.junit.JDBC;
import org.apache.derbyTesting.junit.SupportFilesSetup;
import org.apache.derbyTesting.junit.TestConfiguration;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test import and export procedures 
 */
public class ImportExportTest extends BaseJDBCTestCase {

	public ImportExportTest(String name) {
		super(name);
	}
	
	public static Test suite() {
        TestSuite suite = new TestSuite("ImportExportTest");

        // disabled on weme6.1 due at the moment due 
        // to problems with security exceptions.
        if (JDBC.vmSupportsJSR169())
        {
        	return new TestSuite();
        }
        suite.addTest(baseSuite("ImportExportTest:embedded"));

        suite.addTest(
                TestConfiguration.clientServerDecorator(
                        baseSuite("ImportExportTest:client")));    
        return suite;
	}
	
	public static Test baseSuite(String name) {
		TestSuite suite = new TestSuite(ImportExportTest.class, name);
		Test test = new SupportFilesSetup(suite, new String[] {"functionTests/testData/ImportExport/EndOfFile.txt"} );
		return new CleanDatabaseTestSetup(test) {
            protected void decorateSQL(Statement s) throws SQLException {

                s.execute("CREATE TABLE T1 (COLUMN1 VARCHAR(5) , COLUMN2 VARCHAR(8) , " +
						   "COLUMN3 SMALLINT , COLUMN4 CHAR(11) , COLUMN5 DATE , COLUMN6 DECIMAL(5,1) , " +
						   "COLUMN7 DOUBLE PRECISION , COLUMN8 INT , COLUMN9 BIGINT , COLUMN10 NUMERIC , " +
						   "COLUMN11 REAL , COLUMN12 SMALLINT , COLUMN13 TIME , COLUMN14 TIMESTAMP , "+
						   "COLUMN15 SMALLINT , COLUMN16 VARCHAR(1))");
                s.execute("CREATE TABLE T2 (COLUMN1 VARCHAR(5) , COLUMN2 VARCHAR(8) , " +
						   "COLUMN3 SMALLINT, COLUMN4 CHAR(11) , COLUMN5 DATE , COLUMN6 DECIMAL(5,1) , " +
						   "COLUMN7 DOUBLE PRECISION , COLUMN8 INT , COLUMN9 BIGINT , COLUMN10 NUMERIC , " +
						   "COLUMN11 REAL , COLUMN12 SMALLINT , COLUMN13 TIME , COLUMN14 TIMESTAMP , "+
						   "COLUMN15 SMALLINT , COLUMN16 VARCHAR(1))");
                s.execute("create table T4 (   Account int,    Fname   char(30),"+
                        "Lname   char(30), Company varchar(35), Address varchar(40), City    varchar(20),"+
 					   "State   char(5), Zip     char(10), Payment decimal(8,2), Balance decimal(8,2))");
                
            }
        };
	}
	
	public void testImportFromNonExistantFile() {
		try {
			Connection c = getConnection();
			doImport(c, "Z" , "T1" , null , null , null, 0);
		} catch (SQLException e) {
            // DERBY-1440: JDBC 4 client driver doesn't include nested exception SQLStates
			assertSQLState(JDBC.vmSupportsJDBC4() ? "38000" : "XIE04", e);
		}
	}
	
	public void testNullDataFile() {
		try {
			Connection c = getConnection();
			doImport(c, null, "T1" , null , null, null, 0);
		} catch (SQLException e) {
            // DERBY-1440: JDBC 4 client driver doesn't include nested exception SQLStates
			assertSQLState(JDBC.vmSupportsJDBC4() ? "38000" : "XIE05", e);
		}
	}
	
	public void testEmptyTable() throws SQLException {
		Connection c = getConnection();
		doImportAndExport(c, "T1", null, null , null);
	}

	public void testEmptyTableWithDelimitedFormat() throws SQLException {
		Connection c = getConnection();
		doImportAndExport(c, "T1", null, null , "8859_1");
	}

	public void testEmptyTableWithFieldCharDelimiters() throws SQLException {
		Connection c = getConnection();
		doImportAndExport(c, "T1", "\t", "|" , "8859_1");
	}
	
	public void testWithDefaultOptions() throws Exception {
		Connection c = getConnection();
		resetTables();
		doImportAndExport(c, "T1", null, null, null);
	}
	
	public void testWithCodeset() throws Exception {
		Connection c = getConnection();
		resetTables();
		doImportAndExport(c, "T1", null, null , "8859_1");
	}

	public void testDelimiterAndCodeset() throws Exception {
		Connection c = getConnection();
		resetTables();
		doImportAndExport(c, "T1", "\t", "|", "8859_1");
	}
	
	public void testSpecialDelimitersAndCodeset() throws Exception {
		Connection c = getConnection();
		resetTables();
		doImportAndExport(c, "T1", "%", "&", "Cp1252");
	}

	public void testSpecialDelimitersAndUTF16() throws Exception {
		Connection c = getConnection();
		resetTables();
		doImportAndExport(c, "T1", "%", "&", "UTF-16");
	}
	
	public void testInvalidEncoding() throws Exception {
		Connection c = getConnection();
		resetTables();
		try {
		    doImportAndExport(c, "T1", "^", "#", "INAVALID ENCODING");
		} catch (SQLException e) {
			assertSQLState("XIE0I", e);
		}
	}
	
	public void testEarlyEndOfFile() throws Exception {
		Connection c = getConnection();
		try {
			doImportFromFile(c, "extin/EndOfFile.txt" , "T4" , null , null , null, 0);
		} catch (SQLException e) {
			// DERBY-1440: JDBC 4 client driver doesn't include nested exception SQLStates
			assertSQLState(JDBC.vmSupportsJDBC4() ? "38000" : "XIE0E", e);
		}
	}
	
	private void doImport(Connection c, String fromTable, String toTable, 
			 String colDel, String charDel , 
			 String codeset, int replace) throws SQLException 
    {
		String impsql = "call SYSCS_UTIL.SYSCS_IMPORT_TABLE (? , ? , ? , ?, ? , ?, ?)";
		PreparedStatement ps = c.prepareStatement(impsql);
		ps.setString(1 , "APP");
		ps.setString(2, toTable);
		ps.setString(3, (fromTable==null ?  fromTable : "extinout/" + fromTable + ".dat" ));
		ps.setString(4 , colDel);
		ps.setString(5 , charDel);
		ps.setString(6 , codeset);
		ps.setInt(7, replace);
		ps.execute();
		ps.close();
    }
	
	private void doImportFromFile(Connection c, String fileName, String toTable, 
			 String colDel, String charDel , 
			 String codeset, int replace) throws Exception 
    {
		String impsql = "call SYSCS_UTIL.SYSCS_IMPORT_TABLE (? , ? , ? , ?, ? , ?, ?)";
		PreparedStatement ps = c.prepareStatement(impsql);
		ps.setString(1 , "APP");
		ps.setString(2, toTable);
		ps.setString(3, fileName);
		ps.setString(4 , colDel);
		ps.setString(5 , charDel);
		ps.setString(6 , codeset);
		ps.setInt(7, replace);
		ps.execute();
		ps.close();

    }

	private void doImportAndExport(Connection c, String fromTable, String colDel , 
			  String charDel, 
			  String codeset) throws SQLException 
    {
		doExport(c, fromTable , colDel , charDel , codeset);
		doImportAndVerify(c, fromTable, colDel , charDel, codeset,  0);
        // also test with replace
		doImportAndVerify(c, fromTable, colDel , charDel, codeset,  1);
    }
	
	private void doExport(Connection c, String fromTable, String colDel , 
			 String charDel, 
			 String codeset) throws SQLException 
	{
		 String expsql = "call SYSCS_UTIL.SYSCS_EXPORT_TABLE (? , ? , ? , ?, ? , ?)";
		 PreparedStatement ps = c.prepareStatement(expsql);
		 ps.setString(1 , "APP");
		 ps.setString(2, fromTable);
		 ps.setString(3, (fromTable==null ?  fromTable : "extinout/" + fromTable + ".dat" ));
		 ps.setString(4 , colDel);
		 ps.setString(5 , charDel);
		 ps.setString(6 , codeset);
		 ps.execute();
		 ps.close();
    }
	
	/**
	 * doImportAndVerify checks that data which has been imported and
	 * then exported is identical. It imports the requested data, 
	 * which has been exported from T1. Row counts are compared, and
	 * then the data in T2 is again exported. A bytewise comparison 
	 * of the two files is then made to verify that the data has been
	 * gone through the import/export process intact.
	 */
	private void doImportAndVerify(Connection c, String fromTable,  String colDel, 
			  String charDel , String codeset, 
			  int replace) throws SQLException 
    {

		doImport(c, fromTable , "T2" , colDel , charDel , codeset , replace);

		Statement stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + fromTable);
		rs.next();
		int numberOfRowsInT1 = rs.getInt(1);
		rs.close();
		rs = stmt.executeQuery("SELECT COUNT(*) FROM t2");
		rs.next();
		int numberOfRowsInT2 = rs.getInt(1);
		rs.close();
		stmt.close();
		assertEquals(numberOfRowsInT1, numberOfRowsInT2);

		doExport(c, "T2" , colDel , charDel , codeset);

        //check whether the  exported files from T1 and T2  are same now.
		assertEquals(SupportFilesSetup.getReadWrite(fromTable + ".dat"),
				     SupportFilesSetup.getReadWrite("T2.dat"));
    }
	
	/**
	 * Called from each fixture that verifies data in the table.
	 * Ensures that the import and export operate on a consistent
	 * set of data.
	 */
	private void resetTables() throws Exception {
		runSQLCommands("delete from t1");
		runSQLCommands("delete from t2");
		runSQLCommands("INSERT INTO T1 VALUES (null,'aa',1,'a',DATE('1998-06-30'),"+
		               "1,1,1,1,1,1,1,TIME('12:00:00'),TIMESTAMP('1998-06-30 12:00:00.0'),1,'a')");
        runSQLCommands("INSERT INTO T1 VALUES (null,'bb',1,'b',DATE('1998-06-30'),"+
					   "2,2,2,2,2,2,2,TIME('12:00:00'),TIMESTAMP('1998-06-30 12:00:00.0'),2,'b')");
        runSQLCommands("INSERT INTO T1 VALUES (null,'cc',1,'c',DATE('1998-06-30'),"+
					   "3,3,3,3,3,3,3,TIME('12:00:00'),TIMESTAMP('1998-06-30 12:00:00.0'),3,'c')");
        runSQLCommands("INSERT INTO T1 VALUES (null,'dd',1,'d',DATE('1998-06-30'),"+
					   "4,4,4,4,4,4,4,TIME('12:00:00'),TIMESTAMP('1998-06-30 12:00:00.0'),4,'d')");
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2863.java