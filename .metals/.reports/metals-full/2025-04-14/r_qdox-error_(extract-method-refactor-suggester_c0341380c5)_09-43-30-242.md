error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17350.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17350.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17350.java
text:
```scala
final E@@xecMetaData metaData = new ExecMetaData( command, null, workingDirectory );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included  with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.aut.nativelib.impl;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Locale;
import java.util.Properties;
import org.apache.aut.nativelib.ExecException;
import org.apache.aut.nativelib.ExecManager;
import org.apache.aut.nativelib.ExecMetaData;
import org.apache.aut.nativelib.Os;
import org.apache.avalon.excalibur.util.StringUtil;

/**
 * This is the class that can be used to retrieve the environment
 * variables of the native process.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @author <a href="mailto:thomas.haas@softwired-inc.com">Thomas Haas</a>
 * @version $Revision$ $Date$
 */
final class Environment
{
    private final static String[] COMMAND_COM = new String[]{"command.com", "/c", "set"};
    private final static String[] CMD_EXE = new String[]{"cmd", "/c", "set"};

    // Alternatively one could use: /bin/sh -c env
    private final static String[] ENV_CMD = new String[]{"/usr/bin/env"};
    private final static String[] ENV_RAW = new String[]{"env"};

    /**
     * This is a cached version of the native environment variables.
     */
    private Properties m_procEnvironment;

    /**
     * This is the class that is used to invoke the native process
     * to retrieve then environment variables.
     */
    private final ExecManager m_execManager;

    public Environment( final ExecManager execManager )
    {
        m_execManager = execManager;
    }

    /**
     * Retrieve a Properties object that contains the list of all
     * native EnvironmentData Variables for the current process.
     */
    public Properties getNativeEnvironment()
        throws IOException, ExecException
    {
        final Properties properties = new Properties();
        properties.putAll( getEnvironmentVariables() );
        return properties;
    }

    /**
     * Get the Property object with all environment variables and
     * attempt to load it if it has not already been loaded.
     */
    private synchronized Properties getEnvironmentVariables()
        throws IOException, ExecException
    {
        if( null == m_procEnvironment )
        {
            m_procEnvironment = retrieveEnvironmentVariables();
        }

        return m_procEnvironment;
    }

    /**
     * Retrieve a last of environment variables from the native OS.
     */
    private synchronized Properties retrieveEnvironmentVariables()
        throws IOException, ExecException
    {
        final String data = getEnvironmentText();

        final Properties properties = new Properties();
        final BufferedReader in = new BufferedReader( new StringReader( data ) );
        final StringBuffer var = new StringBuffer();
        String line;
        while( null != ( line = in.readLine() ) )
        {
            if( -1 == line.indexOf( '=' ) )
            {
                // Chunk part of previous env var (UNIX env vars can
                // contain embedded new lines).
                var.append( StringUtil.LINE_SEPARATOR );
            }
            else
            {
                // New env var...append the previous one if we have it.
                if( 0 != var.length() )
                {
                    addProperty( properties, var.toString() );
                    var.setLength( 0 );
                }
            }
            var.append( line );
        }

        // Since we "look ahead" before adding, there's one last env var.
        if( 0 != var.length() )
        {
            addProperty( properties, var.toString() );
        }
        return properties;
    }

    /**
     * Parse the specified data into a key=value pair. If there is no
     * '=' character then generate an exception. After parsed data place
     * the key-value pair into the specified Properties object.
     */
    private void addProperty( final Properties properties,
                              final String data )
        throws ExecException
    {
        final int index = data.indexOf( '=' );
        if( -1 == index )
        {
            //Our env variable does not have any = in it.
            final String message = "EnvironmentData variable '" + data +
                "' does not have a '=' character in it";
            throw new ExecException( message );
        }
        else
        {
            final String key = data.substring( 0, index );
            final String value = data.substring( index + 1 );
            properties.setProperty( key, value );
        }
    }

    /**
     * Retrieve the text of data that is the result of
     * running the environment command.
     */
    private String getEnvironmentText()
        throws IOException, ExecException
    {
        final String[] command = getEnvCommand();
        final File workingDirectory = new File( "." );
        final ExecMetaData metaData = new ExecMetaData( command, null, workingDirectory, false );

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final int retval = m_execManager.execute( metaData, null, output, output, 0 );
        if( retval != 0 )
        {
            // Just try to use what we got
        }

        return output.toString();
    }

    /**
     * Retrieve the command that will display a list of environment
     * variables.
     */
    private static String[] getEnvCommand()
        throws ExecException
    {
        if( Os.isFamily( "os/2" ) )
        {
            // OS/2 - use same mechanism as Windows 2000
            return CMD_EXE;
        }
        else if( Os.isFamily( "windows" ) )
        {
            final String osname =
                System.getProperty( "os.name" ).toLowerCase( Locale.US );
            // Determine if we're running under 2000/NT or 98/95
            if( osname.indexOf( "nt" ) >= 0 || osname.indexOf( "2000" ) >= 0 )
            {
                // Windows 2000/NT
                return CMD_EXE;
            }
            else
            {
                // Windows 98/95 - need to use an auxiliary script
                return COMMAND_COM;
            }
        }
        else if( Os.isFamily( "unix" ) )
        {
            // Generic UNIX
            return ENV_CMD;
        }
        else if( Os.isFamily( "netware" ) )
        {
            return ENV_RAW;
        }
        else
        {
            final String message =
                "Unable to determine native environment variables";
            throw new ExecException( message );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17350.java