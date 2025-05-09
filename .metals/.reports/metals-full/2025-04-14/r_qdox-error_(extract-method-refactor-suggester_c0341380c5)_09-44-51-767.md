error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13169.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13169.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13169.java
text:
```scala
public v@@oid addContent( String s )

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.tools.ant.taskdefs.optional.net;

import com.oroinc.net.telnet.TelnetClient;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.Task;

/**
 * Class to provide automated telnet protocol support for the Ant build tool
 *
 * @author <a href="mailto:ScottCarlson@email.com">ScottCarlson@email.com</a>
 * @version $Revision$
 */

public class TelnetTask extends Task
{
    /**
     * The userid to login with, if automated login is used
     */
    private String userid = null;

    /**
     * The password to login with, if automated login is used
     */
    private String password = null;

    /**
     * The server to connect to.
     */
    private String server = null;

    /**
     * The tcp port to connect to.
     */
    private int port = 23;

    /**
     * The Object which handles the telnet session.
     */
    private AntTelnetClient telnet = null;

    /**
     * The list of read/write commands for this session
     */
    private ArrayList telnetTasks = new ArrayList();

    /**
     * If true, adds a CR to beginning of login script
     */
    private boolean addCarriageReturn = false;

    /**
     * Default time allowed for waiting for a valid response for all child
     * reads. A value of 0 means no limit.
     */
    private Integer defaultTimeout = null;

    /**
     * Set the tcp port to connect to attribute
     *
     * @param b The new InitialCR value
     */
    public void setInitialCR( boolean b )
    {
        this.addCarriageReturn = b;
    }

    /**
     * Set the password attribute
     *
     * @param p The new Password value
     */
    public void setPassword( String p )
    {
        this.password = p;
    }

    /**
     * Set the tcp port to connect to attribute
     *
     * @param p The new Port value
     */
    public void setPort( int p )
    {
        this.port = p;
    }

    /**
     * Set the server address attribute
     *
     * @param m The new Server value
     */
    public void setServer( String m )
    {
        this.server = m;
    }

    /**
     * Change the default timeout to wait for valid responses
     *
     * @param i The new Timeout value
     */
    public void setTimeout( Integer i )
    {
        this.defaultTimeout = i;
    }

    /**
     * Set the userid attribute
     *
     * @param u The new Userid value
     */
    public void setUserid( String u )
    {
        this.userid = u;
    }

    /**
     * A subTask &lt;read&gt; tag was found. Create the object, Save it in our
     * list, and return it.
     *
     * @return Description of the Returned Value
     */

    public TelnetSubTask createRead()
    {
        TelnetSubTask task = (TelnetSubTask)new TelnetRead();
        telnetTasks.add( task );
        return task;
    }

    /**
     * A subTask &lt;write&gt; tag was found. Create the object, Save it in our
     * list, and return it.
     *
     * @return Description of the Returned Value
     */
    public TelnetSubTask createWrite()
    {
        TelnetSubTask task = (TelnetSubTask)new TelnetWrite();
        telnetTasks.add( task );
        return task;
    }

    /**
     * Verify that all parameters are included. Connect and possibly login
     * Iterate through the list of Reads and writes
     *
     * @exception TaskException Description of Exception
     */
    public void execute()
        throws TaskException
    {
        /**
         * A server name is required to continue
         */
        if( server == null )
            throw new TaskException( "No Server Specified" );
        /**
         * A userid and password must appear together if they appear. They are
         * not required.
         */
        if( userid == null && password != null )
            throw new TaskException( "No Userid Specified" );
        if( password == null && userid != null )
            throw new TaskException( "No Password Specified" );

        /**
         * Create the telnet client object
         */
        telnet = new AntTelnetClient();
        try
        {
            telnet.connect( server, port );
        }
        catch( IOException e )
        {
            throw new TaskException( "Can't connect to " + server );
        }
        /**
         * Login if userid and password were specified
         */
        if( userid != null && password != null )
            login();
        /**
         * Process each sub command
         */
        Iterator tasksToRun = telnetTasks.iterator();
        while( tasksToRun != null && tasksToRun.hasNext() )
        {
            TelnetSubTask task = (TelnetSubTask)tasksToRun.next();
            if( task instanceof TelnetRead && defaultTimeout != null )
                ( (TelnetRead)task ).setDefaultTimeout( defaultTimeout );
            task.execute( telnet );
        }
    }

    /**
     * Process a 'typical' login. If it differs, use the read and write tasks
     * explicitely
     */
    private void login()
    {
        if( addCarriageReturn )
            telnet.sendString( "\n", true );
        telnet.waitForString( "ogin:" );
        telnet.sendString( userid, true );
        telnet.waitForString( "assword:" );
        telnet.sendString( password, false );
    }

    /**
     * This class handles the abstraction of the telnet protocol. Currently it
     * is a wrapper around <a href="www.oroinc.com">ORO</a> 's NetComponents
     *
     * @author RT
     */
    public class AntTelnetClient extends TelnetClient
    {

        /**
         * Write this string to the telnet session.
         *
         * @param s Description of Parameter
         * @param echoString Description of Parameter
         * @parm echoString Logs string sent
         */
        public void sendString( String s, boolean echoString )
        {
            OutputStream os = this.getOutputStream();
            try
            {
                os.write( ( s + "\n" ).getBytes() );
                if( echoString )
                    getLogger().info( s );
                os.flush();
            }
            catch( Exception e )
            {
                throw new TaskException( "Error", e );
            }
        }

        /**
         * Read from the telnet session until the string we are waiting for is
         * found
         *
         * @param s Description of Parameter
         * @parm s The string to wait on
         */
        public void waitForString( String s )
        {
            waitForString( s, null );
        }

        /**
         * Read from the telnet session until the string we are waiting for is
         * found or the timeout has been reached
         *
         * @param s Description of Parameter
         * @param timeout Description of Parameter
         * @parm s The string to wait on
         * @parm timeout The maximum number of seconds to wait
         */
        public void waitForString( String s, Integer timeout )
        {
            InputStream is = this.getInputStream();
            try
            {
                StringBuffer sb = new StringBuffer();
                if( timeout == null || timeout.intValue() == 0 )
                {
                    while( sb.toString().indexOf( s ) == -1 )
                    {
                        sb.append( (char)is.read() );
                    }
                }
                else
                {
                    Calendar endTime = Calendar.getInstance();
                    endTime.add( Calendar.SECOND, timeout.intValue() );
                    while( sb.toString().indexOf( s ) == -1 )
                    {
                        while( Calendar.getInstance().before( endTime ) &&
                            is.available() == 0 )
                        {
                            Thread.sleep( 250 );
                        }
                        if( is.available() == 0 )
                            throw new TaskException( "Response Timed-Out" );
                        sb.append( (char)is.read() );
                    }
                }
                getLogger().info( sb.toString() );
            }
            catch( TaskException be )
            {
                throw be;
            }
            catch( Exception e )
            {
                throw new TaskException( "Error", e );
            }
        }
    }

    /**
     * This class reads the output from the connected server until the required
     * string is found.
     *
     * @author RT
     */
    public class TelnetRead extends TelnetSubTask
    {
        private Integer timeout = null;

        /**
         * Sets the default timeout if none has been set already
         *
         * @param defaultTimeout The new DefaultTimeout value
         */
        public void setDefaultTimeout( Integer defaultTimeout )
        {
            if( timeout == null )
                timeout = defaultTimeout;
        }

        /**
         * Override any default timeouts
         *
         * @param i The new Timeout value
         */
        public void setTimeout( Integer i )
        {
            this.timeout = i;
        }

        public void execute( AntTelnetClient telnet )
            throws TaskException
        {
            telnet.waitForString( taskString, timeout );
        }
    }

    /**
     * This class is the parent of the Read and Write tasks. It handles the
     * common attributes for both.
     *
     * @author RT
     */
    public class TelnetSubTask
    {
        protected String taskString = "";

        public void setString( String s )
        {
            taskString += s;
        }

        public void addText( String s )
        {
            setString( s );
        }

        public void execute( AntTelnetClient telnet )
            throws TaskException
        {
            throw new TaskException( "Shouldn't be able instantiate a SubTask directly" );
        }
    }

    /**
     * This class sends text to the connected server
     *
     * @author RT
     */
    public class TelnetWrite extends TelnetSubTask
    {
        private boolean echoString = true;

        public void setEcho( boolean b )
        {
            echoString = b;
        }

        public void execute( AntTelnetClient telnet )
            throws TaskException
        {
            telnet.sendString( taskString, echoString );
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13169.java