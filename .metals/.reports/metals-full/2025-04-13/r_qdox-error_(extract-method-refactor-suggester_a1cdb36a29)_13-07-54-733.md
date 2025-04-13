error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1517.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1517.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[22,1]

error in qdox parser
file content:
```java
offset: 949
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1517.java
text:
```scala
final public class NetworkServerTestSetup extends BaseTestSetup {

/*
 *
 * Derby - Class org.apache.derbyTesting.junit.NetworkServerTestSetup
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
p@@ackage org.apache.derbyTesting.junit;

import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.AccessController;
import java.security.PrivilegedAction;
import junit.extensions.TestSetup;
import junit.framework.Test;
import org.apache.derby.drda.NetworkServerControl;

/**
 * Test decorator that starts the network server on startup
 * and stops it on teardown.
 * 
 *
 * Currently it will start the network server in the same VM
 * and it does not support starting it from a remote 
 * machine.
 */
final public class NetworkServerTestSetup extends TestSetup {

    /** Setting maximum wait time to 300 seconds.   For some systems it looks
     *  like restarting a server to listen on the same port is blocked waiting
     *  for a system specific interval.  This number looks to be something
     *  like 240 seconds on XP.  Waiting shorter than this time causes
     *  intermittent failures on a laptop running XP with a software firewall
     *  and a VPN.  Increasing the wait time should not adversely affect those
     *  systems with fast port turnaround as the actual code loops for 
     *  SLEEP_TIME intervals, so should never see WAIT_TIME.
     */
    private static final long WAIT_TIME = 300000;
    
    /** Sleep for 500 ms before pinging the network server (again) */
    private static final int SLEEP_TIME = 500;

    private static  long    waitTime = WAIT_TIME;
    
    private FileOutputStream serverOutput;
    private final boolean asCommand;

    private final boolean useSeparateProcess;
    private final boolean serverShouldComeUp;
    private final InputStream[] inputStreamHolder;
    private final String[]    systemProperties;
    private final String[]    startupArgs;
    
    /**
     * Decorator this test with the NetworkServerTestSetup
     */
    public NetworkServerTestSetup(Test test, boolean asCommand) {
        super(test);
        this.asCommand = asCommand;

        this.systemProperties = null;
        this.startupArgs = null;
        this.useSeparateProcess = false;
        this.serverShouldComeUp = true;
        this.inputStreamHolder = null;
}

     /**
     * Decorator for starting up with specific command args.
     */
    public NetworkServerTestSetup
        (
         Test test,
         String[] systemProperties,
         String[] startupArgs,
         boolean useSeparateProcess,
         boolean serverShouldComeUp,
         InputStream[] inputStreamHolder
        )
    {
        super(test);
        
        this.asCommand = true;

        this.systemProperties = systemProperties;
        this.startupArgs = startupArgs;
        this.useSeparateProcess = true;
        this.serverShouldComeUp = serverShouldComeUp;
        this.inputStreamHolder = inputStreamHolder;
    }

    /**
     * Start the network server.
     */
    protected void setUp() throws Exception {
        BaseTestCase.println("Starting network server:");
        
        networkServerController = getNetworkServerControl();

        if (useSeparateProcess)
        { startSeparateProcess(); }
        else if (asCommand)
        { startWithCommand(); }
        else
        { startWithAPI(); }
        
        if ( serverShouldComeUp ) { waitForServerStart(networkServerController); }
    }

    private void startWithAPI() throws Exception
    {
            
            serverOutput = (FileOutputStream)
            AccessController.doPrivileged(new PrivilegedAction() {
                public Object run() {
                    File logs = new File("logs");
                    logs.mkdir();
                    File console = new File(logs, "serverConsoleOutput.log");
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(console.getPath(), true);
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    return fos;
                }
            });
            
            networkServerController.start(new PrintWriter(serverOutput));
    }
    
    private void startWithCommand() throws Exception
    {
        final TestConfiguration config = TestConfiguration.getCurrent();
        
        // start the server through the command line
        // arguments using a new thread to do so.
        new Thread(
        new Runnable() {
            public void run() {

                String[]    args = getDefaultStartupArgs();
                
                org.apache.derby.drda.NetworkServerControl.main( args );
            }
            
        }, "NetworkServerTestSetup command").start();
    }

    private void startSeparateProcess() throws Exception
    {
        StringBuffer    buffer = new StringBuffer();
        String              classpath = BaseTestCase.getSystemProperty( "java.class.path" );

        buffer.append( "java -classpath " );
        buffer.append( classpath );
        buffer.append( " " );

        int         count = systemProperties.length;
        for ( int i = 0; i < count; i++ )
        {
            buffer.append( " -D" );
            buffer.append( systemProperties[ i ] );
        }

        buffer.append( " org.apache.derby.drda.NetworkServerControl " );

        String[]    defaultArgs = getDefaultStartupArgs();

        count = defaultArgs.length;
        for ( int i = 0; i < count; i++ )
        {
            buffer.append( " " );
            buffer.append( defaultArgs[ i ] );
        }

        count = startupArgs.length;
        for ( int i = 0; i < count; i++ )
        {
            buffer.append( " " );
            buffer.append( startupArgs[ i ] );
        }

        final   String  command = buffer.toString();

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

        inputStreamHolder[ 0 ] = serverProcess.getInputStream();
    }

    /**
     * Stop the network server if it still
     * appears to be running.
     */
    protected void tearDown() throws Exception {

        if (networkServerController != null) {
            boolean running = false;
            try {
                networkServerController.ping();
                running = true;
            } catch (Exception e) {
            }
      
            if (running)
                networkServerController.shutdown();
 
            if ( serverOutput != null ) { serverOutput.close(); }
            networkServerController = null;
            serverOutput = null;
        }
    }
    
    /**
     * Get the default command arguments for booting the network server.
     */
    public  static String[] getDefaultStartupArgs()
    {
        TestConfiguration config = TestConfiguration.getCurrent();
        
        return new String[] {
            "start",
            "-h",
            config.getHostName(),
            "-p",
            Integer.toString(config.getPort())
        };
    }
    
    /* Network Server Control */
    private NetworkServerControl networkServerController;
        
    /*
     * Utility methods related to controlling network server.
     */
    
    /**
     * Return a new NetworkServerControl for the current configuration.
     */
    public static NetworkServerControl getNetworkServerControl()
        throws Exception
    {
        TestConfiguration config = TestConfiguration.getCurrent();
        return new NetworkServerControl
        (InetAddress.getByName(config.getHostName()), config.getPort());
    }
    
    /**
     * Ping the server until it has started. Asserts a failure
     * if the server has not started within sixty seconds.
     */
    public static void waitForServerStart(NetworkServerControl networkServerController)
       throws InterruptedException 
    {
        if (!pingForServerStart(networkServerController))
            fail("Timed out waiting for network server to start");
    }
    
     /**
     * Set the number of milliseconds to wait before declaring server startup
     * a failure.
     * 
     */
    public static void setWaitTime( long newWaitTime )
   {
        waitTime = newWaitTime;
    }
    
    /**
     * Ping server for upto sixty seconds. If the server responds
     * in that time then return true, otherwise return false.
     * 
     */
    public static boolean pingForServerStart(NetworkServerControl networkServerController)
        throws InterruptedException
    {
        final long startTime = System.currentTimeMillis();
        while (true) {
            Thread.sleep(SLEEP_TIME);
            try {
                networkServerController.ping();
                return true;
            } catch (Exception e) {
                if (System.currentTimeMillis() - startTime > waitTime) {
                    return false;
                }
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1517.java