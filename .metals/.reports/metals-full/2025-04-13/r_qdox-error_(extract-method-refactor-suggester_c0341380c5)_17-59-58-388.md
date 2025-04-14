error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17338.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17338.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17338.java
text:
```scala
final S@@tring antHome = System.getProperty( "myrmidon.home" );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.tools.ant.taskdefs.exec;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import org.apache.avalon.framework.logger.Logger;
import org.apache.myrmidon.api.TaskException;
import org.apache.myrmidon.framework.exec.ExecException;
import org.apache.myrmidon.framework.exec.ExecMetaData;
import org.apache.myrmidon.framework.exec.impl.DefaultExecManager;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Commandline;

/**
 * Runs an external program.
 *
 * @author thomas.haas@softwired-inc.com
 */
public class Execute
{
    private String[] m_command;
    private Properties m_environment = new Properties();
    private File m_workingDirectory = new File( "." );
    private boolean m_newEnvironment;
    private OutputStream m_output;
    private OutputStream m_error;
    private long m_timeout;

    /**
     * Controls whether the VM is used to launch commands, where possible
     */
    private boolean m_useVMLauncher = true;

    private static File getAntHomeDirectory()
    {
        final String antHome = System.getProperty( "ant.home" );
        if( null == antHome )
        {
            final String message =
                "Cannot locate antRun script: Property 'ant.home' not specified";
            throw new IllegalStateException( message );
        }

        return new File( antHome );
    }

    /**
     * A utility method that runs an external command. Writes the output and
     * error streams of the command to the project log.
     *
     * @param task The task that the command is part of. Used for logging
     * @param cmdline The command to execute.
     * @throws TaskException if the command does not return 0.
     */
    public static void runCommand( final Task task, final String[] cmdline )
        throws TaskException
    {
        try
        {
            final Logger logger = task.hackGetLogger();
            logger.debug( Commandline.toString( cmdline ) );
            final Execute exe = new Execute();
            exe.setOutput( new LogOutputStream( logger, false ) );
            exe.setError( new LogOutputStream( logger, true ) );

            exe.setCommandline( cmdline );
            int retval = exe.execute();
            if( retval != 0 )
            {
                throw new TaskException( cmdline[ 0 ] + " failed with return code " + retval );
            }
        }
        catch( final IOException ioe )
        {
            throw new TaskException( "Could not launch " + cmdline[ 0 ] + ": " + ioe );
        }
    }

    /**
     * Creates a new execute object.
     *
     * @param streamHandler the stream handler used to handle the input and
     *      output streams of the subprocess.
     */
    public Execute( final ExecuteStreamHandler streamHandler )
    {
        //m_streamHandler = streamHandler;
    }

    public Execute()
    {
    }

    public void setTimeout( final long timeout )
    {
        m_timeout = timeout;
    }

    public void setOutput( final OutputStream output )
    {
        m_output = output;
    }

    public void setError( final OutputStream error )
    {
        m_error = error;
    }

    /**
     * Sets the commandline of the subprocess to launch.
     *
     * @param commandline the commandline of the subprocess to launch
     */
    public void setCommandline( String[] commandline )
    {
        m_command = commandline;
    }

    public void setEnvironment( final Properties environment )
    {
        if( null == environment )
        {
            throw new NullPointerException( "environment" );
        }
        m_environment = environment;
    }

    /**
     * Set whether to propagate the default environment or not.
     *
     * @param newenv whether to propagate the process environment.
     */
    public void setNewenvironment( boolean newEnvironment )
    {
        m_newEnvironment = newEnvironment;
    }

    /**
     * Launch this execution through the VM, where possible, rather than through
     * the OS's shell. In some cases and operating systems using the shell will
     * allow the shell to perform additional processing such as associating an
     * executable with a script, etc
     *
     * @param useVMLauncher The new VMLauncher value
     */
    public void setVMLauncher( boolean useVMLauncher )
    {
        m_useVMLauncher = useVMLauncher;
    }

    /**
     * Sets the working directory of the process to execute. <p>
     *
     * @param workingDirectory the working directory of the process.
     */
    public void setWorkingDirectory( final File workingDirectory )
    {
        m_workingDirectory = workingDirectory;
    }

    /**
     * Runs a process defined by the command line and returns its exit status.
     *
     * @return the exit status of the subprocess or <code>INVALID</code>
     * @exception IOException Description of Exception
     */
    public int execute()
        throws IOException, TaskException
    {
        try
        {
            final DefaultExecManager manager =
                new DefaultExecManager( getAntHomeDirectory() );

            final ExecMetaData metaData =
                new ExecMetaData( m_command, m_environment,
                                  m_workingDirectory, m_newEnvironment );
            return manager.execute( metaData, null, m_output, m_error, m_timeout );
        }
        catch( final ExecException ee )
        {
            throw new TaskException( ee.getMessage(), ee );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17338.java