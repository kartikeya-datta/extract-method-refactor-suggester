error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/828.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/828.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/828.java
text:
```scala
n@@ew ProcessMonitor( process, input, output, error, timeout, false );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included  with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.aut.nativelib.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Properties;
import org.apache.aut.nativelib.ExecException;
import org.apache.aut.nativelib.ExecManager;
import org.apache.aut.nativelib.ExecMetaData;
import org.apache.aut.nativelib.ExecOutputHandler;
import org.apache.aut.nativelib.Os;
import org.apache.aut.nativelib.impl.launchers.CommandLauncher;
import org.apache.aut.nativelib.impl.launchers.DefaultCommandLauncher;
import org.apache.aut.nativelib.impl.launchers.MacCommandLauncher;
import org.apache.aut.nativelib.impl.launchers.ScriptCommandLauncher;
import org.apache.aut.nativelib.impl.launchers.WinNTCommandLauncher;
import org.apache.avalon.excalibur.io.FileUtil;
import org.apache.avalon.excalibur.io.IOUtil;

/**
 * Default implementation of <code>ExecManager</code>.
 * Used to run processes in the ant environment.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @author <a href="mailto:thomas.haas@softwired-inc.com">Thomas Haas</a>
 * @version $Revision$ $Date$
 * @see ExecManager
 * @see ExecMetaData
 * @see Environment
 */
public class DefaultExecManager
    implements ExecManager
{
    /**
     * Used to destroy processes when the VM exits.
     */
    private final ProcessDestroyer m_processDestroyer = new ProcessDestroyer();

    private final CommandLauncher m_launcher;
    private final CommandLauncher m_shellLauncher;

    /**
     * Utility class that is used to load and parse the native
     * environment variables.
     */
    private final Environment m_environment;

    public DefaultExecManager( final File homeDir )
        throws ExecException
    {
        m_launcher = new DefaultCommandLauncher();
        m_shellLauncher = createShellLauncher( homeDir );
        m_environment = new Environment( this );
    }

    /**
     * Retrieve a properties object that contains a list of
     * all the native environment variables.
     */
    public Properties getNativeEnvironment()
        throws ExecException
    {
        try
        {
            return m_environment.getNativeEnvironment();
        }
        catch( final IOException ioe )
        {
            throw new ExecException( ioe.getMessage(), ioe );
        }
    }

    /**
     * Execute a process and wait for it to finish before
     * returning.
     */
    public int execute( final ExecMetaData execMetaData,
                        final ExecOutputHandler handler,
                        long timeout )
        throws IOException, ExecException /*TimeoutException*/
    {
        final LogOutputStream output = new LogOutputStream( handler, false );
        final LogOutputStream error = new LogOutputStream( handler, true );
        try
        {
            return execute( execMetaData, null, output, error, timeout );
        }
        finally
        {
            IOUtil.shutdownStream( output );
            IOUtil.shutdownStream( error );
        }
    }

    /**
     * Execute a process and wait for it to finish before
     * returning.
     */
    public int execute( final ExecMetaData command,
                        final InputStream input,
                        final OutputStream output,
                        final OutputStream error,
                        final long timeout )
        throws IOException, ExecException
    {
        final CommandLauncher launcher = getLauncher( command );
        final Process process = launcher.exec( command );
        final ProcessMonitor monitor =
            new ProcessMonitor( process, input, output, error, timeout );

        final Thread thread = new Thread( monitor, "ProcessMonitor" );
        thread.start();

        // add the process to the list of those to destroy if the VM exits
        m_processDestroyer.add( process );

        waitFor( process );

        //Now wait for monitor to finish aswell
        try
        {
            thread.join();
        }
        catch( InterruptedException e )
        {
            //should never occur.
        }

        // remove the process to the list of those to destroy if the VM exits
        m_processDestroyer.remove( process );

        if( monitor.didProcessTimeout() )
        {
            throw new ExecException( "Process Timed out" );
        }

        return process.exitValue();
    }

    private void waitFor( final Process process )
    {
        //Should loop around until process is terminated.
        try
        {
            process.waitFor();
        }
        catch( final InterruptedException ie )
        {
            //should never happen
        }
    }

    private CommandLauncher getLauncher( final ExecMetaData metaData )
    {
        CommandLauncher launcher = m_launcher;
        if( false ) //!m_useVMLauncher )
        {
            launcher = m_shellLauncher;
        }
        return launcher;
    }

    private CommandLauncher createShellLauncher( final File homeDir )
        throws ExecException
    {
        CommandLauncher launcher = null;

        if( Os.isFamily( "mac" ) )
        {
            // Mac
            launcher = new MacCommandLauncher();
        }
        else if( Os.isFamily( "os/2" ) )
        {
            // OS/2 - use same mechanism as Windows 2000
            launcher = new WinNTCommandLauncher();
        }
        else if( Os.isFamily( "windows" ) )
        {
            // Windows.  Need to determine which JDK we're running in

            // Determine if we're running under 2000/NT or 98/95
            final String osname =
                System.getProperty( "os.name" ).toLowerCase( Locale.US );

            if( osname.indexOf( "nt" ) >= 0 || osname.indexOf( "2000" ) >= 0 )
            {
                // Windows 2000/NT
                launcher = new WinNTCommandLauncher();
            }
            else
            {
                // Windows 98/95 - need to use an auxiliary script
                final String script = resolveCommand( homeDir, "bin/antRun.bat" );
                launcher = new ScriptCommandLauncher( script );
            }
        }
        else if( Os.isFamily( "netware" ) )
        {
            // NetWare.  Need to determine which JDK we're running in
            final String perlScript = resolveCommand( homeDir, "bin/antRun.pl" );
            final String[] script = new String[]{"perl", perlScript};
            launcher = new ScriptCommandLauncher( script );
        }
        else
        {
            // Generic
            final String script = resolveCommand( homeDir, "bin/antRun" );
            launcher = new ScriptCommandLauncher( script );
        }

        return launcher;
    }

    private String resolveCommand( final File antDir, final String command )
    {
        return FileUtil.resolveFile( antDir, command ).toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/828.java