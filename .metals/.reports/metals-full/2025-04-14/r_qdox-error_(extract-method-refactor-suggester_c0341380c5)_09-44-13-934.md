error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/64.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/64.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/64.java
text:
```scala
b@@uffer.append( getJavaExecutableName() + " -classpath " );

/*

   Derby - Class org.apache.derbyTesting.functionTests.tests.derbynet.SecureServerTest

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

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Properties;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.derbyTesting.junit.BaseTestCase;
import org.apache.derbyTesting.junit.BaseJDBCTestCase;
import org.apache.derbyTesting.junit.Derby;
import org.apache.derbyTesting.junit.NetworkServerTestSetup;
import org.apache.derbyTesting.junit.SecurityManagerSetup;
import org.apache.derbyTesting.junit.ServerSetup;
import org.apache.derbyTesting.junit.SpawnedProcess;
import org.apache.derbyTesting.junit.SupportFilesSetup;
import org.apache.derbyTesting.junit.SystemPropertyTestSetup;
import org.apache.derbyTesting.junit.TestConfiguration;
import org.apache.derbyTesting.functionTests.util.PrivilegedFileOpsForTests;

import org.apache.derby.drda.NetworkServerControl;

/**
 * This Junit test class tests whether the server comes up under a security
 * manager as expected.
 */

public class SecureServerTest extends BaseJDBCTestCase
{
    ///////////////////////////////////////////////////////////////////////////////////
    //
    // CONSTANTS
    //
    ///////////////////////////////////////////////////////////////////////////////////

    // basic properties file which tests that properties are picked up from derby.properties
    private static  final   String  BASIC = "functionTests/tests/derbynet/SecureServerTest.derby.properties";

    private static  final   String  SST_USER_NAME="MARY";
    private static  final   String  SST_PASSWORD = "marypwd";
    
    private static  final   String  HOSTW = "0.0.0.0";
    private static  final   String  ALTW = "0.00.000.0";
    private static  final   String  IPV6W = "::";

    ///////////////////////////////////////////////////////////////////////////////////
    //
    // INNER CLASSES
    //
    ///////////////////////////////////////////////////////////////////////////////////

    /**
     * <p>
     * Possible outcomes for the experiment of bringing up the server.
     * </p>
     */
    public  static  final   class   Outcome
    {
        private boolean _serverShouldComeUp;
        private String      _expectedServerOutput;

        public Outcome
            (
             boolean serverShouldComeUp,
             String expectedServerOutput
             )
        {
            _serverShouldComeUp =  serverShouldComeUp;
            _expectedServerOutput = expectedServerOutput;
        }

        public  boolean serverShouldComeUp() { return _serverShouldComeUp; }
        public  String    expectedServerOutput() { return _expectedServerOutput; }
    }
    
    ///////////////////////////////////////////////////////////////////////////////////
    //
    // STATE
    //
    ///////////////////////////////////////////////////////////////////////////////////

    private static final Outcome RUNNING_SECURITY_NOT_BOOTED = new Outcome( true, "" );
    private static final Outcome RUNNING_SECURITY_BOOTED = new Outcome( true,  serverBootedOK() );

    /** Reference to the enclosing NetworkServerTestSetup. */
    private NetworkServerTestSetup nsTestSetup;
        
    // startup state
    private boolean _unsecureSet;
    private boolean _authenticationRequired;
    private String   _customDerbyProperties;
    private String _wildCardHost;

    // expected outcomes
    private Outcome _outcome;

    
    ///////////////////////////////////////////////////////////////////////////////////
    //
    // CONSTRUCTORS
    //
    ///////////////////////////////////////////////////////////////////////////////////

    public SecureServerTest
        (
         boolean unsecureSet,
         boolean authenticationRequired,
         String     customDerbyProperties,
         String     wildCardHost,

         Outcome    outcome
        )
    {
         super( "testServerStartup" );

         _unsecureSet =  unsecureSet;
         _authenticationRequired =  authenticationRequired;
         _customDerbyProperties = customDerbyProperties;
         _wildCardHost = wildCardHost;

         _outcome = outcome;
    }

    ///////////////////////////////////////////////////////////////////////////////////
    //
    // JUnit MACHINERY
    //
    ///////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Tests to run.
     */
    public static Test suite()
    {
        //NetworkServerTestSetup.setWaitTime( 10000L );
        
        TestSuite       suite = new TestSuite("SecureServerTest");

        // Server booting requires that we run from the jar files
        if ( !TestConfiguration.loadingFromJars() ) { return suite; }
        
        // Need derbynet.jar in the classpath!
        if (!Derby.hasServer())
            return suite;

        // O = Overriden
        // A = Authenticated
        // C = Custom properties
        // W = Wildcard host
        //
        //      .addTest( decorateTest( O,        A,       C,    W,    Outcome ) );
        //

        suite.addTest( decorateTest( false,  false, null, null, RUNNING_SECURITY_BOOTED ) );
        suite.addTest( decorateTest( false,  false, BASIC, null, RUNNING_SECURITY_BOOTED ) );
        suite.addTest( decorateTest( false,  true, null, null, RUNNING_SECURITY_BOOTED ) );
        suite.addTest( decorateTest( false,  true, null, HOSTW, RUNNING_SECURITY_BOOTED ) );
        suite.addTest( decorateTest( false,  true, null, ALTW, RUNNING_SECURITY_BOOTED ) );

        // this wildcard port is rejected by the server right now
        //suite.addTest( decorateTest( false,  true, null, IPV6W, RUNNING_SECURITY_BOOTED ) );
        
        suite.addTest( decorateTest( true,  false, null, null, RUNNING_SECURITY_NOT_BOOTED ) );
        suite.addTest( decorateTest( true,  true, null, null, RUNNING_SECURITY_NOT_BOOTED ) );
        
        return suite;
    }


    ///////////////////////////////////////////////////////////////////////////////////
    //
    // TEST DECORATION
    //
    ///////////////////////////////////////////////////////////////////////////////////
    
    /**
     * <p>
     * Compose the required decorators to bring up the server in the correct
     * configuration.
     * </p>
     */
    private static  Test    decorateTest
        (
         boolean unsecureSet,
         boolean authenticationRequired,
         String customDerbyProperties,
         String wildCardHost,
         
         Outcome outcome
        )
    {
        SecureServerTest            secureServerTest = new SecureServerTest
            (
             unsecureSet,
             authenticationRequired,
             customDerbyProperties,
             wildCardHost,

             outcome
            );

        String[]        startupProperties = getStartupProperties( authenticationRequired, customDerbyProperties );
        String[]        startupArgs = getStartupArgs( unsecureSet, wildCardHost );

        NetworkServerTestSetup networkServerTestSetup =
                new NetworkServerTestSetup
            (
             secureServerTest,
             startupProperties,
             startupArgs,
             secureServerTest._outcome.serverShouldComeUp()
             );

        secureServerTest.nsTestSetup = networkServerTestSetup;

        Test testSetup =
            SecurityManagerSetup.noSecurityManager(networkServerTestSetup);

        // if using the custom derby.properties, copy the custom properties to a visible place
        if ( customDerbyProperties != null )
        {
            testSetup = new SupportFilesSetup
                (
                 testSetup,
                 null,
                 new String[] { "functionTests/tests/derbynet/SecureServerTest.derby.properties" },
                 null,
                 new String[] { "derby.properties" }
                 );
        }

        Test        test = TestConfiguration.defaultServerDecorator( testSetup );
        // DERBY-2109: add support for user credentials
        test = TestConfiguration.changeUserDecorator( test,
                                                      SST_USER_NAME,
                                                      SST_PASSWORD );

        return test;
    }

    /**
     * <p>
     * Return an array of startup args suitable for booting a server.
     * </p>
     */
    private static  String[]    getStartupArgs( boolean setUnsecureOption, String wildCardHost )
    {
        ArrayList       list = new ArrayList();

        if ( setUnsecureOption )
        {
            list.add( "-noSecurityManager" );
        }
        
        if ( wildCardHost != null )
        {
            list.add( NetworkServerTestSetup.HOST_OPTION );
            list.add( wildCardHost );
        }
        
        String[]    result = new String[ list.size() ];

        list.toArray( result );

        return result;
    }
    
    /**
     * <p>
     * Return a set of startup properties suitable for SystemPropertyTestSetup.
     * </p>
     */
    private static  String[]  getStartupProperties( boolean authenticationRequired, String customDerbyProperties )
    {
        ArrayList       list = new ArrayList();

        if ( authenticationRequired )
        {
            list.add( "derby.connection.requireAuthentication=true" );
            list.add( "derby.authentication.provider=BUILTIN" );
            list.add( "derby.user." + SST_USER_NAME + "=" + SST_PASSWORD );
        }

        if ( customDerbyProperties != null )
        {
            list.add( "derby.system.home=extinout" );
        }

        String[]    result = new String[ list.size() ];

        list.toArray( result );

        return result;
    }
    
    ///////////////////////////////////////////////////////////////////////////////////
    //
    // JUnit TESTS
    //
    ///////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Verify if the server came up and if so, was a security manager installed.
     */
    public void testServerStartup()
        throws Exception
    {	
        String      myName = toString();
        String      serverOutput = getServerOutput();
        boolean     serverCameUp = serverCameUp();
        boolean     outputOK = ( serverOutput.indexOf( _outcome.expectedServerOutput() ) >= 0 );

        assertEquals( myName + ": serverCameUp = " + serverCameUp, _outcome.serverShouldComeUp(), serverCameUp );
        
        assertTrue( myName + "\nExpected: " + _outcome.expectedServerOutput() + "\nBut saw: " + serverOutput , outputOK );

        //
        // make sure that the default policy lets us connect to the server if the hostname was
        // wildcarded (DERBY-2811)
        //
        if ( _authenticationRequired && ( _wildCardHost != null ) ) { connectToServer(); }

        //
        // make sure that we can run sysinfo and turn on tracing (DERBY-3086)
        //
        runsysinfo();
        enableTracing();
        setTraceDirectory();
        disableTracing();
        
        
    }

    private void disableTracing() throws Exception {

        String traceOffOutput = runServerCommand( "trace off" );

        println( "Output for trace off command:\n\n" + traceOffOutput );

        if ( traceOffOutput.indexOf( "Trace turned off for all sessions." ) < 0 )
        { fail( "Failed to turn trace off:\n\n:" + traceOffOutput ); }
    }

    private void setTraceDirectory() throws Exception {

        String  traceDirectoryOutput = runServerCommand( "tracedirectory trace" );
        println( "Output for tracedirectory trace command:\n\n" + traceDirectoryOutput );

        if ( traceDirectoryOutput.indexOf( "Trace directory changed to trace." ) < 0 )
        { fail( "Unexpected output in setting trace directory:" + traceDirectoryOutput ); }

        String pingOutput = runServerCommand( "ping" );

        if (pingOutput.indexOf("Connection obtained for host:") < 0)
        { fail ("Failed ping after changing trace directory: " + pingOutput);}
        assertTrue("directory trace does not exist",
                PrivilegedFileOpsForTests.exists(new File("trace")));
    }

    private void    connectToServer()
        throws Exception
    {
        final TestConfiguration config = getTestConfiguration();
        String  url
            = ( "jdbc:derby://localhost:" + config.getPort()
                + "/" + "wombat;create=true"
                + ";user=" + config.getUserName()
                + ";password=" + config.getUserPassword() );

        println( "XXX in connectToServer(). url = " + url );

        // just try to get a connection
        Class.forName( "org.apache.derby.jdbc.ClientDriver" );
        
        Connection  conn = DriverManager.getConnection(  url );

        assertNotNull( "Connection should not be null...", conn );

        conn.close();
    }

    private void    runsysinfo()
        throws Exception
    {
        String          sysinfoOutput = runServerCommand( "sysinfo" );

        if ( sysinfoOutput.indexOf( "Security Exception:" ) > -1 )
        { fail( "Security exceptions in sysinfo output:\n\n:" + sysinfoOutput ); }
    }

    private void    enableTracing()
        throws Exception
    {
        String          traceOnOutput = runServerCommand( "trace on" );

        println( "Output for trace on command:\n\n" + traceOnOutput );

        if ( traceOnOutput.indexOf( "Trace turned on for all sessions." ) < 0 )
        { fail( "Security exceptions in output of trace enabling command:\n\n:" + traceOnOutput ); }
    }
    
    ///////////////////////////////////////////////////////////////////////////////////
    //
    // Object OVERLOADS
    //
    ///////////////////////////////////////////////////////////////////////////////////

    public String toString()
    {
        StringBuffer    buffer = new StringBuffer();

        buffer.append( "SecureServerTest( " );
        buffer.append( "Opened = " ); buffer.append( _unsecureSet);
        buffer.append( ", Authenticated= " ); buffer.append( _authenticationRequired );
        buffer.append( ", CustomDerbyProperties= " ); buffer.append( _customDerbyProperties );
        buffer.append( ", WildCardHost= " ); buffer.append( _wildCardHost );
        buffer.append( " )" );

        return buffer.toString();
    }
    
    ///////////////////////////////////////////////////////////////////////////////////
    //
    // MINIONS
    //
    ///////////////////////////////////////////////////////////////////////////////////

    /**
     * <p>
     * Run a NetworkServerControl command.
     * </p>
     */
    private String    runServerCommand( String commandSpecifics )
        throws Exception
    {
        String          portNumber = Integer.toString( getTestConfiguration().getPort() );
        StringBuffer    buffer = new StringBuffer();
        String          classpath = getSystemProperty( "java.class.path" );

        buffer.append( "java -classpath " );
        buffer.append( classpath );
        buffer.append( " -Demma.verbosity.level=silent");
        buffer.append( " org.apache.derby.drda.NetworkServerControl -p " + portNumber + " " + commandSpecifics );

        final   String  command = buffer.toString();

        println( "Server command is " + command );

        Process     serverProcess = (Process) AccessController.doPrivileged
            (
             new PrivilegedAction()
             {
                 public Object run()
                 {
                     Process    result = null;
                     try {
                        result = Runtime.getRuntime().exec( command );
                     } catch (Exception ex) {
                         ex.printStackTrace();
                     }
                     
                     return result;
                 }
             }
            );
        
        SpawnedProcess spawned = new SpawnedProcess(serverProcess,
                commandSpecifics);
        
        // Ensure it completes without failures.
        assertEquals(0, spawned.complete(false));
        
        return spawned.getFullServerOutput();
    }

    private String  getServerOutput()
        throws Exception
    {
        return nsTestSetup.getServerProcess().getNextServerOutput();
    }

    private static  String  serverBootedOK()
    {
        return "Security manager installed using the Basic server security policy.";
    }

    private boolean serverCameUp()
        throws Exception
    {
        return NetworkServerTestSetup.pingForServerUp(
            NetworkServerTestSetup.getNetworkServerControl(),
            nsTestSetup.getServerProcess().getProcess(), true);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/64.java