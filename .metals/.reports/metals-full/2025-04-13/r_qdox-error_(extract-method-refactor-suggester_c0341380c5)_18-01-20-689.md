error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17983.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17983.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17983.java
text:
```scala
S@@tring name = event.getTask().getName();

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.tools.ant;

import java.io.PrintStream;
import org.apache.tools.ant.util.StringUtils;

/**
 * Writes build event to a PrintStream. Currently, it only writes which targets
 * are being executed, and any messages that get logged.
 *
 * @author RT
 */
public class DefaultLogger implements BuildLogger
{
    private static int LEFT_COLUMN_SIZE = 12;
    protected int msgOutputLevel = Project.MSG_ERR;
    private long startTime = System.currentTimeMillis();

    protected boolean emacsMode = false;
    protected PrintStream err;

    protected PrintStream out;

    protected static String formatTime( long millis )
    {
        long seconds = millis / 1000;
        long minutes = seconds / 60;

        if( minutes > 0 )
        {
            return Long.toString( minutes ) + " minute"
                + ( minutes == 1 ? " " : "s " )
                + Long.toString( seconds % 60 ) + " second"
                + ( seconds % 60 == 1 ? "" : "s" );
        }
        else
        {
            return Long.toString( seconds ) + " second"
                + ( seconds % 60 == 1 ? "" : "s" );
        }

    }

    /**
     * Set this logger to produce emacs (and other editor) friendly output.
     *
     * @param emacsMode true if output is to be unadorned so that emacs and
     *      other editors can parse files names, etc.
     */
    public void setEmacsMode( boolean emacsMode )
    {
        this.emacsMode = emacsMode;
    }

    /**
     * Set the output stream to which this logger is to send error messages.
     *
     * @param err the error stream for the logger.
     */
    public void setErrorPrintStream( PrintStream err )
    {
        this.err = new PrintStream( err, true );
    }

    /**
     * Set the msgOutputLevel this logger is to respond to. Only messages with a
     * message level lower than or equal to the given level are output to the
     * log. <P>
     *
     * Constants for the message levels are in Project.java. The order of the
     * levels, from least to most verbose, is MSG_ERR, MSG_WARN, MSG_INFO,
     * MSG_VERBOSE, MSG_DEBUG. The default message level for DefaultLogger is
     * Project.MSG_ERR.
     *
     * @param level the logging level for the logger.
     */
    public void setMessageOutputLevel( int level )
    {
        this.msgOutputLevel = level;
    }

    /**
     * Set the output stream to which this logger is to send its output.
     *
     * @param output the output stream for the logger.
     */
    public void setOutputPrintStream( PrintStream output )
    {
        this.out = new PrintStream( output, true );
    }

    /**
     * Prints whether the build succeeded or failed, and any errors the occured
     * during the build.
     *
     * @param event Description of Parameter
     */
    public void buildFinished( BuildEvent event )
    {
        Throwable error = event.getException();
        StringBuffer message = new StringBuffer();

        if( error == null )
        {
            message.append( StringUtils.LINE_SEP );
            message.append( "BUILD SUCCESSFUL" );
        }
        else
        {
            message.append( StringUtils.LINE_SEP );
            message.append( "BUILD FAILED" );
            message.append( StringUtils.LINE_SEP );

            if( Project.MSG_VERBOSE <= msgOutputLevel ||
                !( error instanceof BuildException ) )
            {
                message.append( StringUtils.getStackTrace( error ) );
            }
            else
            {
                if( error instanceof BuildException )
                {
                    message.append( error.toString() ).append( StringUtils.LINE_SEP );
                }
                else
                {
                    message.append( error.getMessage() ).append( StringUtils.LINE_SEP );
                }
            }
        }
        message.append( StringUtils.LINE_SEP );
        message.append( "Total time: "
                        + formatTime( System.currentTimeMillis() - startTime ) );

        String msg = message.toString();
        if( error == null )
        {
            out.println( msg );
        }
        else
        {
            err.println( msg );
        }
        log( msg );
    }

    public void buildStarted( BuildEvent event )
    {
        startTime = System.currentTimeMillis();
    }

    public void messageLogged( BuildEvent event )
    {
        // Filter out messages based on priority
        if( event.getPriority() <= msgOutputLevel )
        {

            StringBuffer message = new StringBuffer();
            // Print out the name of the task if we're in one
            if( event.getTask() != null )
            {
                String name = event.getTask().getTaskName();

                if( !emacsMode )
                {
                    String label = "[" + name + "] ";
                    for( int i = 0; i < ( LEFT_COLUMN_SIZE - label.length() ); i++ )
                    {
                        message.append( " " );
                    }
                    message.append( label );
                }
            }

            message.append( event.getMessage() );
            String msg = message.toString();
            if( event.getPriority() != Project.MSG_ERR )
            {
                out.println( msg );
            }
            else
            {
                err.println( msg );
            }
            log( msg );
        }
    }

    public void targetFinished( BuildEvent event )
    {
    }

    public void targetStarted( BuildEvent event )
    {
        if( Project.MSG_INFO <= msgOutputLevel )
        {
            String msg = StringUtils.LINE_SEP + event.getTarget().getName() + ":";
            out.println( msg );
            log( msg );
        }
    }

    public void taskFinished( BuildEvent event )
    {
    }

    public void taskStarted( BuildEvent event )
    {
    }

    /**
     * Empty implementation which allows subclasses to receive the same output
     * that is generated here.
     *
     * @param message Description of Parameter
     */
    protected void log( String message )
    {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17983.java