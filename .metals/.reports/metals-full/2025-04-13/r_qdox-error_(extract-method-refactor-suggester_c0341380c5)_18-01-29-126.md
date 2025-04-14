error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2149.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2149.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2149.java
text:
```scala
a@@ssertSQLState("08006", e);

/*

   Derby - Class org.apache.derbyTesting.functionTests.tests.derbynet.SSLTest

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

package org.apache.derbyTesting.functionTests.tests.derbynet;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.sql.DriverManager;
import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.SQLException;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.derbyTesting.junit.BaseTestCase;
import org.apache.derbyTesting.junit.BaseJDBCTestCase;
import org.apache.derbyTesting.junit.Derby;
import org.apache.derbyTesting.junit.NetworkServerTestSetup;
import org.apache.derbyTesting.junit.SecurityManagerSetup;
import org.apache.derbyTesting.junit.ServerSetup;
import org.apache.derbyTesting.junit.SupportFilesSetup;
import org.apache.derbyTesting.junit.SystemPropertyTestSetup;
import org.apache.derbyTesting.junit.TestConfiguration;
import org.apache.derbyTesting.junit.JDBCDataSource;

import org.apache.derby.drda.NetworkServerControl;

/**
 * Tests connects to an SSL server
 */

public class SSLTest extends BaseJDBCTestCase
{
    // helper state for intercepting server error messages
    private InputStream[]  _inputStreamHolder;
    /** Reference to the enclosing NetworkServerTestSetup. */
    private NetworkServerTestSetup nsTestSetup;

    // Constructors

    public SSLTest(String testName)
    {
        super(testName);
        _inputStreamHolder = new InputStream[1];
    }
    
    // JUnit machinery
    
    /**
     * Tests to run.
     */
    public static Test suite()
    {
        //NetworkServerTestSetup.setWaitTime(10000L);
        
        TestSuite suite = new TestSuite("SSLTest");
        
        // Server booting requires that we run from the jar files
        if (!TestConfiguration.loadingFromJars()) { return suite; }
        
        // Need derbynet.jar in the classpath!
        if (!Derby.hasServer())
            return suite;
        
        suite.addTest(decorateTest("testSSLBasicDSConnect"));
        suite.addTest(decorateTest("testSSLBasicDSPlainConnect"));
        return suite;
    }
    
    /**
     * Release resources.
     */

    protected void tearDown() throws Exception
    {
        _inputStreamHolder = null;
    }
    

    // Test decoration
    
    /**
     * <p>
     * Compose the required decorators to bring up the server in the correct
     * configuration.
     * </p>
     */
    private static Test decorateTest(String testName)
    {
        SSLTest sslTest = 
            new SSLTest(testName);
        
        String[] startupProperties = 
            getStartupProperties();

        String[] startupArgs = new String[]{};
        
        NetworkServerTestSetup networkServerTestSetup =
            new NetworkServerTestSetup(sslTest,
                                       startupProperties,
                                       startupArgs,
                                       true,
                                       true, 
                                       sslTest._inputStreamHolder);
        
        sslTest.nsTestSetup = networkServerTestSetup;
        
        Test testSetup =
            SecurityManagerSetup.noSecurityManager(networkServerTestSetup);
        
        testSetup = 
            new SupportFilesSetup(testSetup,
                                  null,
                                  new String[] 
                                  {"functionTests/tests/derbynet/SSLTestServerKey.key"},
                                  null,
                                  new String[] 
                                  {"SSLTestServerKey.key"}
                                  );
        Test test = TestConfiguration.defaultServerDecorator(testSetup);

        test = TestConfiguration.changeSSLDecorator(test, "basic");

        return test;
    }
    
    /**
     * <p>
     * Return a set of startup properties suitable for SSLTest.
     * </p>
     */
    private static  String[]  getStartupProperties()
    {
        ArrayList list = new ArrayList();
        list.add("javax.net.ssl.keyStore=extinout/SSLTestServerKey.key");
        list.add("javax.net.ssl.keyStorePassword=qwerty");
        String[] result = new String[ list.size()];
        list.toArray(result);
        return result;
    }
    
    // JUnit Tests
    
    /**
     * Test that a basic SSL connect succeeds.
     **/

    public void testSSLBasicDSConnect()
        throws Exception
    {   
        DataSource ds = JDBCDataSource.getDataSource();
        JDBCDataSource.setBeanProperty(ds,"createDatabase","create");
        JDBCDataSource.setBeanProperty(ds,"ssl","basic");
        Connection c1 = ds.getConnection();
        c1.close();
    }

    /**
     * Test that a plaintext connect will fail.
     **/

    public void testSSLBasicDSPlainConnect()
        throws Exception
    {   
        DataSource ds = JDBCDataSource.getDataSource();
        JDBCDataSource.setBeanProperty(ds,"createDatabase","create");
        
        try {
            Connection c2 = ds.getConnection();
            c2.close();
            fail();
        } catch (SQLException e) {
            assertSQLState("58009", e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2149.java