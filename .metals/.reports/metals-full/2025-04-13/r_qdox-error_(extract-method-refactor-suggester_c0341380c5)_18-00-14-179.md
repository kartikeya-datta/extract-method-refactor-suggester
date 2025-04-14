error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9291.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9291.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9291.java
text:
```scala
g@@etLogger().info( "Building the RPM based on the " + specFile + " file" );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.tools.ant.taskdefs.optional;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.ExecuteStreamHandler;
import org.apache.tools.ant.taskdefs.LogOutputStream;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.taskdefs.PumpStreamHandler;
import org.apache.tools.ant.types.Commandline;

/**
 * @author lucas@collab.net
 */
public class Rpm extends Task
{

    /**
     * the rpm command to use
     */
    private String command = "-bb";

    /**
     * clean BUILD directory
     */
    private boolean cleanBuildDir = false;

    /**
     * remove spec file
     */
    private boolean removeSpec = false;

    /**
     * remove sources
     */
    private boolean removeSource = false;

    /**
     * the file to direct standard error from the command
     */
    private File error;

    /**
     * the file to direct standard output from the command
     */
    private File output;

    /**
     * the spec file
     */
    private String specFile;

    /**
     * the rpm top dir
     */
    private File topDir;

    public void setCleanBuildDir( boolean cbd )
    {
        cleanBuildDir = cbd;
    }

    public void setCommand( String c )
    {
        this.command = c;
    }

    public void setError( File error )
    {
        this.error = error;
    }

    public void setOutput( File output )
    {
        this.output = output;
    }

    public void setRemoveSource( boolean rs )
    {
        removeSource = rs;
    }

    public void setRemoveSpec( boolean rs )
    {
        removeSpec = rs;
    }

    public void setSpecFile( String sf )
        throws TaskException
    {
        if( ( sf == null ) || ( sf.trim().equals( "" ) ) )
        {
            throw new TaskException( "You must specify a spec file" );
        }
        this.specFile = sf;
    }

    public void setTopDir( File td )
    {
        this.topDir = td;
    }

    public void execute()
        throws TaskException
    {

        Commandline toExecute = new Commandline();

        toExecute.setExecutable( "rpm" );
        if( topDir != null )
        {
            toExecute.createArgument().setValue( "--define" );
            toExecute.createArgument().setValue( "_topdir" + topDir );
        }

        toExecute.createArgument().setLine( command );

        if( cleanBuildDir )
        {
            toExecute.createArgument().setValue( "--clean" );
        }
        if( removeSpec )
        {
            toExecute.createArgument().setValue( "--rmspec" );
        }
        if( removeSource )
        {
            toExecute.createArgument().setValue( "--rmsource" );
        }

        toExecute.createArgument().setValue( "SPECS/" + specFile );

        ExecuteStreamHandler streamhandler = null;
        OutputStream outputstream = null;
        OutputStream errorstream = null;
        if( error == null && output == null )
        {
            streamhandler = new LogStreamHandler( this, Project.MSG_INFO,
                                                  Project.MSG_WARN );
        }
        else
        {
            if( output != null )
            {
                try
                {
                    outputstream = new PrintStream( new BufferedOutputStream( new FileOutputStream( output ) ) );
                }
                catch( IOException e )
                {
                    throw new TaskException( "Error", e );
                }
            }
            else
            {
                outputstream = new LogOutputStream( this, Project.MSG_INFO );
            }
            if( error != null )
            {
                try
                {
                    errorstream = new PrintStream( new BufferedOutputStream( new FileOutputStream( error ) ) );
                }
                catch( IOException e )
                {
                    throw new TaskException( "Error", e );
                }
            }
            else
            {
                errorstream = new LogOutputStream( this, Project.MSG_WARN );
            }
            streamhandler = new PumpStreamHandler( outputstream, errorstream );
        }

        Execute exe = new Execute( streamhandler, null );

        exe.setAntRun( project );
        if( topDir == null )
            topDir = project.getBaseDir();
        exe.setWorkingDirectory( topDir );

        exe.setCommandline( toExecute.getCommandline() );
        try
        {
            exe.execute();
            log( "Building the RPM based on the " + specFile + " file" );
        }
        catch( IOException e )
        {
            throw new TaskException( "Error", e );
        }
        finally
        {
            if( output != null )
            {
                try
                {
                    outputstream.close();
                }
                catch( IOException e )
                {
                }
            }
            if( error != null )
            {
                try
                {
                    errorstream.close();
                }
                catch( IOException e )
                {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9291.java