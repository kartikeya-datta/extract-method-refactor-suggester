error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14244.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14244.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14244.java
text:
```scala
_@@exe.setCommandline( _commandLine );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.tools.ant.taskdefs.optional.dotnet;// imports

import java.io.File;
import java.io.IOException;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.exec.Execute2;
import org.apache.tools.ant.types.Commandline;

/**
 * This is a helper class to spawn net commands out. In its initial form it
 * contains no .net specifics, just contains all the command line/exe
 * construction stuff. However, it may be handy in future to have a means of
 * setting the path to point to the dotnet bin directory; in which case the
 * shared code should go in here.
 *
 * @author Steve Loughran steve_l@iseran.com
 * @version 0.3
 * @created 2000-11-01
 */

public class NetCommand
    extends AbstractLogEnabled
{
    /**
     * trace flag
     */
    protected boolean _traceCommandLine = false;

    /**
     * what is the command line
     */
    protected Commandline _commandLine;

    /**
     * executabe
     */
    protected Execute2 _exe;

    /**
     * flag to control action on execution trouble
     */
    protected boolean _failOnError;

    /**
     * owner project
     */
    protected Task _owner;

    /**
     * actual program to invoke
     */
    protected String _program;

    /**
     * title of the command
     */
    protected String _title;

    /**
     * constructor
     *
     * @param title (for logging/errors)
     * @param owner Description of Parameter
     * @param program Description of Parameter
     */

    public NetCommand( Task owner, String title, String program )
        throws TaskException
    {
        _owner = owner;
        _title = title;
        _program = program;
        _commandLine = new Commandline();
        _commandLine.setExecutable( _program );
        prepareExecutor();
    }

    /**
     * set fail on error flag
     *
     * @param b fail flag -set to true to cause an exception to be raised if the
     *      return value != 0
     */
    public void setFailOnError( boolean b )
    {
        _failOnError = b;
    }

    /**
     * turn tracing on or off
     *
     * @param b trace flag
     */
    public void setTraceCommandLine( boolean b )
    {
        _traceCommandLine = b;
    }

    /**
     * query fail on error flag
     *
     * @return The FailFailOnError value
     */
    public boolean getFailFailOnError()
    {
        return _failOnError;
    }

    /**
     * add an argument to a command line; do nothing if the arg is null or empty
     * string
     *
     * @param argument The feature to be added to the Argument attribute
     */
    public void addArgument( String argument )
    {
        if( argument != null && argument.length() != 0 )
        {
            _commandLine.addArgument( argument );
        }
    }

    /**
     * Run the command using the given Execute instance.
     *
     * @exception TaskException Description of Exception
     * @throws an exception of something goes wrong and the failOnError flag is
     *      true
     */
    public void runCommand()
        throws TaskException
    {
        int err = -1;// assume the worst
        try
        {
            if( _traceCommandLine )
            {
                //_owner.getLogger().info( _commandLine.toString() );
            }
            else
            {
                //in verbose mode we always log stuff
                logVerbose( _commandLine.toString() );
            }
            _exe.setCommandline( _commandLine.getCommandline() );
            err = _exe.execute();
            if( err != 0 )
            {
                if( _failOnError )
                {
                    throw new TaskException( _title + " returned: " + err );
                }
                else
                {
                    getLogger().error( _title + "  Result: " + err );
                }
            }
        }
        catch( IOException e )
        {
            throw new TaskException( _title + " failed: " + e, e );
        }
    }

    /**
     * error text log
     *
     * @param msg message to display as an error
     */
    protected void logError( String msg )
    {
        getLogger().error( msg );
    }

    /**
     * verbose text log
     *
     * @param msg string to add to log iff verbose is defined for the build
     */
    protected void logVerbose( String msg )
    {
        getLogger().debug( msg );
    }

    /**
     * set up the command sequence..
     */
    protected void prepareExecutor()
        throws TaskException
    {
        // default directory to the project's base directory
        File dir = _owner.getBaseDirectory();
        _exe = new Execute2();
        setupLogger( _exe );
        _exe.setWorkingDirectory( dir );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14244.java