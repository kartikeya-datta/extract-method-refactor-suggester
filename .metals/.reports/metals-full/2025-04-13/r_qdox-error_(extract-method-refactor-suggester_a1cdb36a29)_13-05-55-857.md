error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3336.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3336.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,43]

error in qdox parser
file content:
```java
offset: 43
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3336.java
text:
```scala
"SYSIBM.BLOBCREATELOCATOR() are incorrect",@@ 4, locator);

/*
 *
 * Derby - Class org.apache.derbyTesting.functionTests.tests.lang.BlobStoredProcedureTest
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

package org.apache.derbyTesting.functionTests.tests.jdbcapi;

import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.Assert;
import org.apache.derbyTesting.junit.BaseJDBCTestCase;
import org.apache.derbyTesting.junit.JDBC;
import org.apache.derbyTesting.junit.TestConfiguration;

/**
 * Tests the stored procedures introduced as part of DERBY-208. These stored procedures will
 * used by the Blob methods on the client side.
 */
public class BlobStoredProcedureTest extends BaseJDBCTestCase {

    //The test string that will be used in all the test runs.
    final String testStr = "I am a simple derby test case";

    //Byte array obatined from the string
    final byte [] strBytes = testStr.getBytes();

    //The length of the test string that will be used.
    final long testStrLength = testStr.length();

    /**
     * Public constructor required for running test as standalone JUnit.
     * @param name a string containing the name of the test.
     */
    public BlobStoredProcedureTest(String name) {
        super(name);
    }

    /**
     * Create a suite of tests.
     * @return the test suite created.
     */
    public static Test suite() {
        if (JDBC.vmSupportsJSR169()) {
            return new TestSuite("empty: client not supported on JSR169; procs use DriverMgr");
        }
        else {
            return TestConfiguration.defaultSuite(
                    BlobStoredProcedureTest.class);
        }
    }

    /**
     * Setup the test.
     * @throws a SQLException.
     */
    protected void setUp() throws SQLException {
        //initialize the locator to a default value.
        int locator = -1;
        //set auto commit to false for the connection
        getConnection().setAutoCommit(false);
        //call the stored procedure to return the created locator.
        CallableStatement cs  = prepareCall
            ("? = CALL SYSIBM.BLOBCREATELOCATOR()");
        cs.registerOutParameter(1, java.sql.Types.INTEGER);
        cs.executeUpdate();
        locator = cs.getInt(1);
        cs.close();
        //use this new locator to test the SETBYTES function
        //by inserting the new bytes and testing whether it has
        //been inserted properly.

        //Insert the new substring.
        cs  = prepareCall("CALL SYSIBM.BLOBSETBYTES(?,?,?,?)");
        cs.setInt(1, locator);
        cs.setLong(2, 1L);
        cs.setInt(3, (int)testStrLength);
        cs.setBytes(4, strBytes);
        cs.execute();
        cs.close();
    }

    /**
     * Cleanup the test.
     * @throws SQLException.
     */
    protected void tearDown() throws Exception {
        commit();
        super.tearDown();
    }

    /**
     * test the BLOBGETBYTES stored procedure which will
     * be used in the implementation of Blob.getBytes.
     *
     * @throws a SQLException.
     */
    public void testBlobGetBytesSP() throws SQLException {
        // This string represents the substring that is got from the
        // stored procedure
        String testSubStr = testStr.substring(0, 10);
        byte [] testSubBytes = testSubStr.getBytes();

        //create a callable statement and execute it to call the stored
        //procedure BLOBGETBYTES that will get the bytes
        //inserted into the Blob in the setup method.
        CallableStatement cs  = prepareCall
            ("? = CALL SYSIBM.BLOBGETBYTES(?,?,?)");
        cs.registerOutParameter(1, java.sql.Types.VARBINARY);
        cs.setInt(2, 1);
        cs.setLong(3, 1);
        //set the length of the bytes returned as 10.
        cs.setInt(4, 10);
        cs.executeUpdate();
        byte [] retVal = cs.getBytes(1);

        for (int i=0;i<10;i++){
            assertEquals
                ("The Stored procedure SYSIBM.BLOBGETBYTES " +
                "returns the wrong bytes"
                , testSubBytes[i], retVal[i]);
        }
        cs.close();
    }

    /**
     * Tests the locator value returned by the stored procedure
     * BLOBCREATELOCATOR.
     *
     * @throws SQLException.
     *
     */
    public void testBlobCreateLocatorSP() throws SQLException {
        //initialize the locator to a default value.
        int locator = -1;
        //call the stored procedure to return the created locator.
        CallableStatement cs  = prepareCall
            ("? = CALL SYSIBM.BLOBCREATELOCATOR()");
        cs.registerOutParameter(1, java.sql.Types.INTEGER);
        cs.executeUpdate();
        locator = cs.getInt(1);
        //verify if the locator rturned and expected are equal.
        //remember in setup a locator is already created
        //hence expected value is 2
        assertEquals("The locator values returned by " +
            "SYSIBM.BLOBCREATELOCATOR() are incorrect", 2, locator);
        cs.close();
    }

    /**
     * Tests the SYSIBM.BLOBRELEASELOCATOR stored procedure.
     *
     * @throws SQLException
     */
    public void testBlobReleaseLocatorSP() throws SQLException {
        CallableStatement cs  = prepareCall
            ("CALL SYSIBM.BLOBRELEASELOCATOR(?)");
        cs.setInt(1, 1);
        cs.execute();
        cs.close();

        //once the locator has been released the BLOBGETLENGTH on that
        //locator value will throw an SQLException. This assures that
        //the locator has been properly released.

        cs  = prepareCall("? = CALL SYSIBM.BLOBGETLENGTH(?)");
        cs.registerOutParameter(1, java.sql.Types.BIGINT);
        cs.setInt(2, 1);
        try {
            cs.executeUpdate();
        } catch(SQLException sqle) {
            //on expected lines. The test was successful.
            return;
        }
        //The exception was not thrown. The test has failed here.
        fail("Error the locator was not released by SYSIBM.BLOBRELEASELOCATOR");
        cs.close();
    }

    /**
     * Tests the SYSIBM.BLOBGETLENGTH stored procedure.
     *
     * @throws SQLException.
     */
    public void testBlobGetLengthSP() throws SQLException {
        CallableStatement cs  = prepareCall
            ("? = CALL SYSIBM.BLOBGETLENGTH(?)");
        cs.registerOutParameter(1, java.sql.Types.BIGINT);
        cs.setInt(2, 1);
        cs.executeUpdate();
        //compare the actual length of the test string and the returned length.
        assertEquals("Error SYSIBM.BLOBGETLENGTH returns " +
            "the wrong value for the length of the Blob", testStrLength, cs.getLong(1));
        cs.close();
    }

    /**
     * Tests the SYSIBM.BLOBGETPOSITIONFROMBYTES stored procedure.
     *
     * @throws SQLException.
     */
    public void testBlobGetPositionFromBytesSP() throws Exception {
        CallableStatement cs  = prepareCall
            ("? = CALL SYSIBM.BLOBGETPOSITIONFROMBYTES(?,?,?)");
        cs.registerOutParameter(1, java.sql.Types.BIGINT);
        cs.setInt(2, 1);
        //find the position of the bytes corresponding to
        //the String simple in the test string.
        cs.setBytes(3, (new String("simple")).getBytes());
        cs.setLong(4, 1L);
        cs.executeUpdate();
        //check to see that the returned position and the expected position
        //of the substring simple in the string are matching.
        assertEquals("Error SYSIBM.BLOBGETPOSITIONFROMBYTES returns " +
            "the wrong value for the position of the Blob", 8, cs.getLong(1));
        cs.close();
    }

    /**
     * Tests the stored procedure SYSIBM.BLOBSETBYTES
     *
     * @throws SQLException.
     */
    public void testBlobSetBytes() throws SQLException {
        String newString = "123456789012345";
        byte [] newBytes = newString.getBytes();
        //initialize the locator to a default value.
        int locator = -1;
        //call the stored procedure to return the created locator.
        CallableStatement cs  = prepareCall
            ("? = CALL SYSIBM.BLOBCREATELOCATOR()");
        cs.registerOutParameter(1, java.sql.Types.INTEGER);
        cs.executeUpdate();
        locator = cs.getInt(1);
        cs.close();

        //use this new locator to test the SETBYTES function
        //by inserting the new bytes and testing whether it has
        //been inserted properly.

        //Insert the new substring.
        cs  = prepareCall("CALL SYSIBM.BLOBSETBYTES(?,?,?,?)");
        cs.setInt(1, locator);
        cs.setLong(2, 1L);
        cs.setInt(3, newString.length());
        cs.setBytes(4, newBytes);
        cs.execute();
        cs.close();

        //check the new locator to see if the value has been inserted correctly.
        cs  = prepareCall("? = CALL " +
            "SYSIBM.BLOBGETBYTES(?,?,?)");
        cs.registerOutParameter(1, java.sql.Types.VARBINARY);
        cs.setInt(2, locator);
        cs.setLong(3, 1);
        cs.setInt(4, newString.length());
        cs.executeUpdate();
        byte [] retVal = cs.getBytes(1);
        //compare the new bytes and the bytes returned by the stored
        //procedure to see of they are the same.
        for (int i=0;i<newString.length();i++){
            assertEquals
                ("The Stored procedure SYSIBM.BLOBGETBYTES " +
                "returns the wrong bytes"
                , newBytes[i], retVal[i]);
        }
        cs.close();
    }

    /**
     * Test the stored procedure SYSIBM.BLOBGETLENGTH
     *
     * @throws SQLException
     */
    public void testBlobTruncateSP() throws SQLException {
        CallableStatement cs = prepareCall
            ("CALL SYSIBM.BLOBTRUNCATE(?,?)");
        cs.setInt(1, 1);
        cs.setLong(2, 10L);
        cs.execute();
        cs.close();

        cs  = prepareCall
            ("? = CALL SYSIBM.BLOBGETLENGTH(?)");
        cs.registerOutParameter(1, java.sql.Types.BIGINT);
        cs.setInt(2, 1);
        cs.executeUpdate();
        //compare the actual length of the test string and the returned length.
        assertEquals("Error SYSIBM.BLOBGETLENGTH returns " +
            "the wrong value for the length of the Blob", 10L
            , cs.getLong(1));
        cs.close();
     }

    /**
     * Tests the SYSIBM.BLOBGETPOSITIONFROMLOCATOR stored procedure.
     *
     * @throws SQLException.
     */
    public void testBlobGetPositionFromLocatorSP() throws SQLException {
        String newString = "simple";
        byte [] newBytes = newString.getBytes();
        //initialize the locator to a default value.
        int locator = -1;
        //call the stored procedure to return the created locator.
        CallableStatement cs  = prepareCall
            ("? = CALL SYSIBM.BLOBCREATELOCATOR()");
        cs.registerOutParameter(1, java.sql.Types.INTEGER);
        cs.executeUpdate();
        locator = cs.getInt(1);
        cs.close();

        //use this new locator to test the SETBYTES function
        //by inserting the new bytes and testing whether it has
        //been inserted properly.

        //Insert the new substring.
        cs  = prepareCall("CALL SYSIBM.BLOBSETBYTES(?,?,?,?)");
        cs.setInt(1, locator);
        cs.setLong(2, 1L);
        cs.setInt(3, newString.length());
        cs.setBytes(4, newBytes);
        cs.execute();
        cs.close();

        cs  = prepareCall
            ("? = CALL SYSIBM.BLOBGETPOSITIONFROMLOCATOR(?,?,?)");
        cs.registerOutParameter(1, java.sql.Types.BIGINT);
        cs.setInt(2, 1);
        //find the position of the bytes corresponding to
        //the String simple in the test string.
        cs.setInt(3, locator);
        cs.setLong(4, 1L);
        cs.executeUpdate();
        //check to see that the returned position and the expected position
        //of the substring simple in the string are matching.
        assertEquals("Error SYSIBM.BLOBGETPOSITIONFROMLOCATOR returns " +
            "the wrong value for the position of the Blob", 8, cs.getLong(1));
        cs.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3336.java