error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/909.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/909.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/909.java
text:
```scala
S@@tring          reservedToDBO = "42504";

/**
 *  Derby - Class org.apache.derbyTesting.functionTests.tests.lang.SecurityPolicyReloadingTest
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

package org.apache.derbyTesting.functionTests.tests.lang;

import java.io.File;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.SQLException;

import junit.framework.Test;
import junit.extensions.TestSetup;
import junit.framework.TestSuite;

import org.apache.derbyTesting.junit.BaseJDBCTestCase;
import org.apache.derbyTesting.junit.JDBC;
import org.apache.derbyTesting.junit.SecurityManagerSetup;
import org.apache.derbyTesting.junit.SupportFilesSetup;
import org.apache.derbyTesting.junit.SystemPropertyTestSetup;
import org.apache.derbyTesting.junit.TestConfiguration;

/**
 * Test the dynamic reloading of the security policy file while the
 * engine is still running.
 */
public class SecurityPolicyReloadingTest extends BaseJDBCTestCase {

    ///////////////////////////////////////////////////////////////////////////////////
    //
    // CONSTANTS
    //
    ///////////////////////////////////////////////////////////////////////////////////

    private static  final   String  RELOADABLE_INITIAL_SOURCE_POLICY = "functionTests/tests/lang/SecurityPolicyReloadingTest.initial.policy";
    private static  final   String  RELOADABLE_MODIFIED_SOURCE_POLICY = "functionTests/tests/lang/SecurityPolicyReloadingTest.modified.policy";
    private static  final   String  UNRELOADABLE_SOURCE_POLICY = "functionTests/tests/lang/SecurityPolicyReloadingTest.unreloadable.policy";
    private static  final   String  TARGET_POLICY = "server.policy";

    private static  final   String  NON_DBO_USER = "NON_DBO_USER";
    private static  final   String  PASSWORD_TOKEN = "PASSWORD_TOKEN";
    
    ///////////////////////////////////////////////////////////////////////////////////
    //
    // INNER CLASSES
    //
    ///////////////////////////////////////////////////////////////////////////////////

    public  static  class   PropReadingAction   implements PrivilegedExceptionAction
    {
        private final   String  _propName;
        
        public     PropReadingAction( String propName )
        {
            _propName = propName;
        }

        //
        // This will throw an AccessControlException if we don't have
        // privilege to read the property.
        //
        public  Object  run()
        throws Exception
        {
            return System.getProperty( _propName );
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////
    //
    // STATE
    //
    ///////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////
    //
    // CONSTRUCTORS
    //
    ///////////////////////////////////////////////////////////////////////////////////

    public  SecurityPolicyReloadingTest
        (
         )
    {
        super( "testPolicyReloading" );
    }

    ///////////////////////////////////////////////////////////////////////////////////
    //
    // JUnit MACHINERY
    //
    ///////////////////////////////////////////////////////////////////////////////////

    public static Test suite()
    {
        TestSuite       suite = new TestSuite("SecurityPolicyReloadingTest");

        // The reloaded policy requires restricted property-reading permissions,
        // which is easy to do if you can subdivide the protection domains by
        // jar file but is not easy to do with all of the testing and server
        // classes jumbled together in the same class tree.
        if ( !TestConfiguration.loadingFromJars() ) { return suite; }

        suite.addTest( decorateTest() );

        return suite;
    }

    ///////////////////////////////////////////////////////////////////////////////////
    //
    // TEST DECORATION
    //
    ///////////////////////////////////////////////////////////////////////////////////

    /**
     * Add decorators to a test run. Context is established in the reverse order
     * that decorators are declared here. That is, decorators compose in reverse
     * order. The order of the setup methods is:
     *
     * <ul>
     * <li>Copy security policy to visible location.</li>
     * <li>Setup authorization-enabling properties.</li>
     * <li>Install a security manager.</li>
     * <li>Run the tests.</li>
     * </ul>
     */
    private static Test decorateTest()
    {
        SecurityPolicyReloadingTest undecoratedTest = new SecurityPolicyReloadingTest();
        Test                                        test = undecoratedTest;

        //
        // Install a security manager using the initial policy file.
        //
        test = new SecurityManagerSetup( test, undecoratedTest.makeServerPolicyName() );
        
        //
        // Set up authorization with a DBO and non-DBO user
        //
        test = TestConfiguration.sqlAuthorizationDecorator
            (
             test,
             new String[] { NON_DBO_USER },
             PASSWORD_TOKEN
             );
        
        //
        // Copy over the initial policy file we want to use.
        //
        test = new SupportFilesSetup
            (
             test,
             null,
             new String[] { undecoratedTest.getSourcePolicy() },
             null,
             new String[] { undecoratedTest.makeTargetPolicyStub() }
             );

        return test;
    }

        
    ///////////////////////////////////////////////////////////////////////////////////
    //
    // JUnit TESTS
    //
    ///////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Verify that policy file reloading is allowed and forbidden as expected.
     */
    public void testPolicyReloading()
        throws Exception
    {
        //getTestConfiguration().setVerbosity( true );

        doPolicyReloadingIsGranted();
        doPolicyReloadingIsNotGranted();
    }
    
    ////////////////////////////////////////////////////
    //
    // getPolicy() PRIVILEGE GRANTED
    //
    ////////////////////////////////////////////////////
    
    /**
     * Verify that the DBA has the power to reload the security policy file and
     * that a non-DBA does not have this power.
     */
    private void doPolicyReloadingIsGranted()
        throws Exception
    {
        dbaTest();
        nonDbaTest();
    }
    
    /**
     * Verify that the DBA has the power to reload the security policy file.
     */
    private void dbaTest()
        throws Exception
    {
        Connection  conn = openUserConnection( TestConfiguration.TEST_DBO );

        assertTrue( "Initially, should be able to read property.", canReadProperty() );

        // Now prove that the DBO can reload the policy file.
        changePolicyFile( conn, RELOADABLE_MODIFIED_SOURCE_POLICY, true, null );
        assertFalse( "Policy file changed. Should not be able to read the property.", canReadProperty() );

        // Return to initial policy file.
        changePolicyFile( conn, RELOADABLE_INITIAL_SOURCE_POLICY, true, null );
        assertTrue( "Reverted to initial policy. Should be able to read the property again.", canReadProperty() );

        conn.close();
    }
    
    /**
     * Verify that the non-DBA does not have the power to reload the security policy file.
     */
    private void nonDbaTest()
        throws Exception
    {
        String          reservedToDBO = "2850A";
        Connection  conn = openUserConnection( NON_DBO_USER );

        assertTrue( "Initially, should be able to read property.", canReadProperty() );

        // Now prove that the non-DBO can't reload the policy file.
        changePolicyFile( conn, RELOADABLE_MODIFIED_SOURCE_POLICY, false, reservedToDBO );
        assertTrue( "Policy file not changed. Should still be able to read the property.", canReadProperty() );

        // Return to initial policy file.
        changePolicyFile( conn, RELOADABLE_INITIAL_SOURCE_POLICY, false, reservedToDBO );
        assertTrue( "Reverted to initial policy. Should still be able to read the property again.", canReadProperty() );

        conn.close();
    }
    
    /////////////////////////////////////////////
    //
    // getPolicy() IS NOT GRANTED
    //
    /////////////////////////////////////////////
    
    /**
     * Verify that even the DBA can't reload the policy file if getPolicy() has
     * not been granted.
     */
    private void doPolicyReloadingIsNotGranted()
        throws Exception
    {
        String          insufficientPrivilege = "XK000";
        Connection  conn = openUserConnection( TestConfiguration.TEST_DBO );

        // First change to a policy which does not permit policy reloading
        changePolicyFile( conn, UNRELOADABLE_SOURCE_POLICY, true, null );

        // Verify that we get an exception when we try to reload the policy file.
        changePolicyFile( conn, RELOADABLE_INITIAL_SOURCE_POLICY, false, insufficientPrivilege );

        conn.close();
    }
    
    ///////////////////////////////////////////////////////////////////////////////////
    //
    // Object OVERLOADS
    //
    ///////////////////////////////////////////////////////////////////////////////////

    public String toString()
    {
        StringBuffer    buffer = new StringBuffer();

        buffer.append( "SecurityPolicyReloadingTest( " );
        buffer.append( " )" );

        return buffer.toString();
    }
    
    ///////////////////////////////////////////////////////////////////////////////////
    //
    // MINIONS
    //
    ///////////////////////////////////////////////////////////////////////////////////
     
    /**
     * Return true if we have sufficient privilege to read a special property.
     */
    private boolean canReadProperty()
        throws Exception
    {
        try {
            String  propValue = readProperty( "SecurityPolicyReloadingTest.property" );

            return true;
        }
        catch (AccessControlException ace) { return false; }
    }

    /**
     * Read a system property.
     */
    public  static   String readProperty( String propName )
        throws Exception
    {
        PropReadingAction   action = new PropReadingAction( propName );
        
        return (String) AccessController.doPrivileged( action );
    }

    /**
     * A handy method for debugging.
     */
    public static void sleep( long numberOfSeconds )
        throws Exception
    {
        Thread.currentThread().sleep( numberOfSeconds * (1000L) );
    }

    /**
     * Try to change the policy file.
     */
    private void changePolicyFile( Connection conn, String newPolicyFileName, boolean shouldSucceed, String expectedSQLState )
        throws Exception
    {
        boolean     reloaded = true;
        
        writePolicyFile( newPolicyFileName );

        CallableStatement   cs = conn.prepareCall( "call SYSCS_UTIL.SYSCS_RELOAD_SECURITY_POLICY()" );

        try {
            cs.execute();
        }
        catch (SQLException se)
        {
            reloaded = false;

            assertSQLState( expectedSQLState, se );
        }
    
        assertEquals( shouldSucceed, reloaded );
    }

    /**
     * Write a new policy file.
     */
    private void writePolicyFile( String newPolicyFileName )
        throws Exception
    {
        SupportFilesSetup.privCopyFiles
             (
              SupportFilesSetup.EXTINOUT,
              new String[] { newPolicyFileName },
              new String[] { makeTargetPolicyStub() }
             );
   }

    /**
     * Construct the name of the server policy file.
     */
    private String makeServerPolicyName()
    {
        try {
            String  userDir = getSystemProperty( "user.dir" );
            String  fileName = userDir + File.separator + SupportFilesSetup.EXTINOUT + File.separator + makeTargetPolicyStub();
            File      file = new File( fileName );
            String  urlString = file.toURL().toExternalForm();

            return urlString;
        }
        catch (Exception e)
        {
            System.out.println( "Unexpected exception caught by makeServerPolicyName(): " + e );

            return null;
        }
    }

    /**
     * Get the stub name (no directory spec) for the server policy file we create.
     */
    private String makeTargetPolicyStub()
    {
        return TARGET_POLICY;
   }

    /**
     * Get the source file which has the correct permissions.
     */
    private String getSourcePolicy()
    {
        return RELOADABLE_INITIAL_SOURCE_POLICY;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/909.java