error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1907.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1907.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1907.java
text:
```scala
D@@erby - Class org.apache.derbyTesting.functionTests.tests.tools.SysinfoAPITest

/*

   Derby - Class org.apache.derbyTesting.functionTests.tests.tools.sysinfo_api

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

import java.io.IOException;
import java.io.BufferedReader;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.PrintWriter;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import org.apache.derby.tools.sysinfo;
import org.apache.derbyTesting.junit.BaseJDBCTestCase;
import org.apache.derbyTesting.junit.SecurityManagerSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *  Test all the static public methods of the sysinfo class.
 *  
 *  Formerly named sysinfo_api. Currently disabled from running in
 *  nightly regression tests when run from jar files, only the first
 *  jar in the classpath properly reports its information through the 
 *  sysinfo API.
 */

public class SysinfoAPITest extends BaseJDBCTestCase {

    DatabaseMetaData dm;
    
    public SysinfoAPITest(String name) { 
        super(name); 
    }
    
    public static Test suite() {
    	Test suite = new TestSuite(SysinfoAPITest.class, "Sysinfo API");
    	
    	return suite;
    }

    /**
     *  Test various invocations of sysinfo.getMajorVersion()
     */
    public void testMajorVersion() {
        int dmMajor = dm.getDriverMajorVersion();
        assertEquals(dmMajor, sysinfo.getMajorVersion());
        assertEquals(dmMajor, sysinfo.getMajorVersion(sysinfo.DBMS));
        assertEquals(dmMajor, sysinfo.getMajorVersion(sysinfo.TOOLS));
        assertEquals(dmMajor, sysinfo.getMajorVersion(sysinfo.NET));
        assertEquals(dmMajor, sysinfo.getMajorVersion(sysinfo.CLIENT));
        // bad usage
        assertEquals(-1, sysinfo.getMajorVersion("foo"));
        assertEquals(-1, sysinfo.getMajorVersion(null));
    }

    /**
     *  Test various invocations of sysinfo.getMinorVersion()
     */
    public void testMinorVersion() {
        int dmMinor = dm.getDriverMinorVersion();
        assertEquals(dmMinor, sysinfo.getMinorVersion());
        assertEquals(dmMinor, sysinfo.getMinorVersion(sysinfo.DBMS));
        assertEquals(dmMinor, sysinfo.getMinorVersion(sysinfo.TOOLS));
        assertEquals(dmMinor, sysinfo.getMinorVersion(sysinfo.NET));
        assertEquals(dmMinor, sysinfo.getMinorVersion(sysinfo.CLIENT));
        // bad usage
        assertEquals(-1, sysinfo.getMinorVersion("foo"));
        assertEquals(-1, sysinfo.getMinorVersion(null));
    }

    /**
     *  Test various invocations of sysinfo.getProductName()
     */
    public void testProductName() {
        assertEquals("Apache Derby", sysinfo.getProductName());
        assertEquals("Apache Derby", sysinfo.getProductName(sysinfo.DBMS));
        assertEquals("Apache Derby", sysinfo.getProductName(sysinfo.TOOLS));
        assertEquals("Apache Derby", sysinfo.getProductName(sysinfo.NET));
        assertEquals("Apache Derby", sysinfo.getProductName(sysinfo.CLIENT));
        // bad usage
        assertEquals("<no name found>", sysinfo.getProductName("foo"));
        assertEquals("<no name found>", sysinfo.getProductName(null));
    }

    /**
     *  Test various invocations of sysinfo.getVersionString()
     * 
     *  NOTE: sysinfo.getVersionString() returns the short version string.
     *        We also chomp the version we get back from sysinfo to account
     *        for alpha/beta differences.
     */
    public void testVersionString() throws SQLException {
        String dmPv = dm.getDatabaseProductVersion().substring(0,sysinfo.getVersionString().indexOf(' '));
        assertEquals(dmPv, sysinfo.getVersionString().substring(0,sysinfo.getVersionString().indexOf(' ')));
        assertEquals(dmPv, sysinfo.getVersionString(sysinfo.DBMS).substring(0,sysinfo.getVersionString().indexOf(' ')));
        assertEquals(dmPv, sysinfo.getVersionString(sysinfo.TOOLS).substring(0,sysinfo.getVersionString().indexOf(' ')));
        assertEquals(dmPv, sysinfo.getVersionString(sysinfo.NET).substring(0,sysinfo.getVersionString().indexOf(' ')));
        assertEquals(dmPv, sysinfo.getVersionString(sysinfo.CLIENT).substring(0,sysinfo.getVersionString().indexOf(' ')));
        // bad usage
        assertEquals("<no name found>", sysinfo.getVersionString("foo"));
        assertEquals("<no name found>", sysinfo.getVersionString(null));
    }

    /**
     * Test various invocations of sysinfo.getBuildNumber()
     *
     * Extract the build number from the Database Product Version.
     * Compare with the result from sysinfo.getBuildNumber.
     */
    public void testBuildNumber() throws SQLException {
        String dmBn = dm.getDatabaseProductVersion();
        dmBn = dmBn.substring(dmBn.indexOf('(') + 1,dmBn.indexOf(')'));
        System.out.println(dmBn);
        assertEquals(dmBn, sysinfo.getBuildNumber());
        assertEquals(dmBn, sysinfo.getBuildNumber(sysinfo.DBMS));
        assertEquals(dmBn, sysinfo.getBuildNumber(sysinfo.TOOLS));
        assertEquals(dmBn, sysinfo.getBuildNumber(sysinfo.NET));
        assertEquals(dmBn, sysinfo.getBuildNumber(sysinfo.CLIENT));
        // bad usage
        assertEquals("????", sysinfo.getBuildNumber("foo"));
        assertEquals("????", sysinfo.getBuildNumber(null));
    }

    /**
     * Test sysinfo.getInfo()
     *
     * Currently only tests getInfo() by comparing the first line with the
     * expected first line in English. Because so much of sysinfo changes from
     * machine-to-machine, writing a better test may be difficult.
     *
     * Test spawns a separate thread in which to call sysinfo and feed the
     * PipedWriter. Using PipedWriter and PipedReader from the same thread
     * can cause a deadlock.
     */
    public void testGetInfo() throws IOException {
        sysinfo_api_helper sah = new sysinfo_api_helper();
        sah.start();
        PipedReader pipeR = new PipedReader(sah.getPipedWriter());
        BufferedReader br = new BufferedReader(pipeR);
        assertEquals("------------------ Java Information ------------------",
                     br.readLine());
        br.close();
        pipeR.close();
    }

    /**
     *  setUp - get a DatabaseMetadata object with which to compare
     *          database information with what is reported by sysinfo
     */
    public void setUp() throws SQLException {
        dm = getConnection().getMetaData();
    }


}

/**
 * sysinfo_api_helper - a helper class which calls sysinfo.getInfo() and
 *                      pushes the output into a PipedWriter so that we
 *                      can read it with a PipedReader in testGetInfo().
 *
 */
class sysinfo_api_helper extends Thread { 
    
    private static PipedWriter pipeW = new PipedWriter();

    public void run() {
        PrintWriter pw = new PrintWriter(pipeW, true);
        sysinfo.getInfo(pw);
        try {
            pw.close();
            pipeW.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   public PipedWriter getPipedWriter() {
       return pipeW;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1907.java