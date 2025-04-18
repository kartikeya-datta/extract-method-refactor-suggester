error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/904.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/904.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/904.java
text:
```scala
D@@erby - Class org.apache.derbyTesting.functionTests.tests.jdbc4.BlobClobTestSetup

/*
 
   Derby - Class BlobClobTestSetup
 
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to you under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at
 
      http://www.apache.org/licenses/LICENSE-2.0
 
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 
 */

package org.apache.derbyTesting.functionTests.tests.jdbc4;

import junit.extensions.TestSetup;
import junit.framework.Test;

import org.apache.derbyTesting.junit.BaseJDBCTestCase;
import org.apache.derbyTesting.junit.BaseJDBCTestSetup;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.*;

/**
 * Create a table with one column for a blob and one column for a clob.
 * This is shared between tests that need a blob or a clob, and is required
 * because the createBlob/-Clob methods are not yet implemented.
 */
public class BlobClobTestSetup
    extends BaseJDBCTestSetup {

    /** Constant for accessing the row with null values. */
    public static final int ID_NULLVALUES = 1;
    /** Constant for accessing the row with sample values. */
    public static final int ID_SAMPLEVALUES = 2;
    /**
     * ID is used to store the latest unique value for the ID column
     * Start from 3 since 1 is used for null values and 2 is used for
     * sample values.
     */
    public static int ID = 3;

    /** Blob data. */
    private static final byte[] blobData = new byte[] {
        0x65, 0x66, 0x67, 0x68, 0x69,
        0x69, 0x68, 0x67, 0x66, 0x65
    };
    /** Clob data. */
    private static final String clobData =
        "This is a string, inserted into a CLOB";
   
    /**
     * Create a test setup for the specified blob or clob test.
     *
     * @param test the test to provide setup for.
     */
    public BlobClobTestSetup(Test test) {
        super(test);
    }

    /**
     * Create a table with BLOB and CLOB, so that such objects can be
     * accessed/used from JDBC.
     */
    protected void setUp() 
        throws IOException, SQLException {
        Connection con = getConnection();
        Statement stmt = con.createStatement();
        stmt.execute("create table BLOBCLOB (ID int primary key, " +
                                            "BLOBDATA blob(1k)," + 
                                            "CLOBDATA clob(1k))");
        stmt.execute("insert into BLOBCLOB VALUES " +
                "(" + ID_NULLVALUES + ", null, null)");
        // Actual data is inserted in the getSample* methods.
        stmt.execute("insert into BLOBCLOB VALUES " +
                "(" + ID_SAMPLEVALUES + ", null, null)");
        stmt.close();
    }

    /**
     * Drop the table we created during setup.
     * @throws Exception 
     */
    protected void tearDown()
        throws Exception {
        Connection con = getConnection();
        Statement stmt = con.createStatement();
        stmt.execute("drop table BLOBCLOB");
        stmt.close();
        super.tearDown();
    }
    
    /**
     * Fetch a sample Blob.
     * If this method fails, the test fails.
     *
     * @param con database connection to fetch data from.
     * @return a sample <code>Blob</code> object.
     */
    public static Blob getSampleBlob(Connection con) 
        throws SQLException {
		InputStream blobInput = new ByteArrayInputStream(blobData, 0, blobData.length);
        PreparedStatement pStmt = 
            con.prepareStatement("update BLOBCLOB set BLOBDATA = ? where ID = ?");
        try {
            blobInput.reset();
        } catch (IOException ioe) {
            fail("Failed to reset blob input stream: " + ioe.getMessage());
        }
        pStmt.setBlob(1, blobInput, blobData.length);
        pStmt.setInt(2, ID_SAMPLEVALUES);
        assertEquals("Invalid update count", 1, pStmt.executeUpdate());
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select BLOBDATA from BLOBCLOB where ID = " +
                ID_SAMPLEVALUES);
        rs.next();
        Blob blob = rs.getBlob(1);
        rs.close();
        stmt.close();
        return blob;
    }
    
    /**
     * Fetch a sample Clob.
     * If this method fails, the test fails.
     *
     * @param con database connection to fetch data from.
     * @return a sample <code>Clob</code> object.
     */
    public static Clob getSampleClob(Connection con) 
        throws SQLException {
		Reader clobInput = new StringReader(clobData);
        PreparedStatement pStmt = 
            con.prepareStatement("update BLOBCLOB set CLOBDATA = ? where ID = ?");
        try {
            clobInput.reset();
        } catch (IOException ioe) {
            fail("Failed to reset clob input stream: " + ioe.getMessage());
        }
        pStmt.setClob(1, clobInput, clobData.length());
        pStmt.setInt(2, ID_SAMPLEVALUES);
        assertEquals("Invalid update count", 1, pStmt.executeUpdate());
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select CLOBDATA from BLOBCLOB where ID = " +
                ID_SAMPLEVALUES);
        rs.next();
        Clob clob = rs.getClob(1);
        rs.close();
        stmt.close();
        return clob;
    }

    /**
     * Returns new unique ID values that can be used in these tests.
     * @return an integer that represents an unique ID value.
     */
    public static int getID() {
        return ID++;
    }

} // End class BlobClobTestSetup
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/904.java