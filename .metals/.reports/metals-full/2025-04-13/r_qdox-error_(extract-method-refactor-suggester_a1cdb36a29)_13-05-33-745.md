error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17985.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17985.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17985.java
text:
```scala
S@@tring name = "[" + event.getTask().getName() + "]";

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.tools.ant.taskdefs;

import java.io.PrintStream;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.StringUtils;

/**
 * This is a class that represents a recorder. This is the listener to the build
 * process.
 *
 * @author <a href="mailto:jayglanville@home.com">J D Glanville</a>
 * @version 0.5
 */
public class RecorderEntry implements BuildLogger
{

    //////////////////////////////////////////////////////////////////////
    // ATTRIBUTES

    /**
     * The name of the file associated with this recorder entry.
     */
    private String filename = null;
    /**
     * The state of the recorder (recorder on or off).
     */
    private boolean record = true;
    /**
     * The current verbosity level to record at.
     */
    private int loglevel = Project.MSG_INFO;
    /**
     * The output PrintStream to record to.
     */
    private PrintStream out = null;
    /**
     * The start time of the last know target.
     */
    private long targetStartTime = 0l;

    //////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS / INITIALIZERS

    /**
     * @param name The name of this recorder (used as the filename).
     */
    protected RecorderEntry( String name )
    {
        filename = name;
    }

    private static String formatTime( long millis )
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

    public void setEmacsMode( boolean emacsMode )
    {
        throw new java.lang.RuntimeException( "Method setEmacsMode() not yet implemented." );
    }

    public void setErrorPrintStream( PrintStream err )
    {
        out = err;
    }

    public void setMessageOutputLevel( int level )
    {
        if( level >= Project.MSG_ERR && level <= Project.MSG_DEBUG )
            loglevel = level;
    }

    public void setOutputPrintStream( PrintStream output )
    {
        out = output;
    }

    /**
     * Turns off or on this recorder.
     *
     * @param state true for on, false for off, null for no change.
     */
    public void setRecordState( Boolean state )
    {
        if( state != null )
            record = state.booleanValue();
    }

    //////////////////////////////////////////////////////////////////////
    // ACCESSOR METHODS

    /**
     * @return the name of the file the output is sent to.
     */
    public String getFilename()
    {
        return filename;
    }

    public void buildFinished( BuildEvent event )
    {
        log( "< BUILD FINISHED", Project.MSG_DEBUG );

        Throwable error = event.getException();
        if( error == null )
        {
            out.println( StringUtils.LINE_SEP + "BUILD SUCCESSFUL" );
        }
        else
        {
            out.println( StringUtils.LINE_SEP + "BUILD FAILED" + StringUtils.LINE_SEP );
            error.printStackTrace( out );
        }
        out.flush();
        out.close();
    }

    public void buildStarted( BuildEvent event )
    {
        log( "> BUILD STARTED", Project.MSG_DEBUG );
    }

    public void messageLogged( BuildEvent event )
    {
        log( "--- MESSAGE LOGGED", Project.MSG_DEBUG );

        StringBuffer buf = new StringBuffer();
        if( event.getTask() != null )
        {
            String name = "[" + event.getTask().getTaskName() + "]";
            /**
             * @todo replace 12 with DefaultLogger.LEFT_COLUMN_SIZE
             */
            for( int i = 0; i < ( 12 - name.length() ); i++ )
            {
                buf.append( " " );
            }// for
            buf.append( name );
        }// if
        buf.append( event.getMessage() );

        log( buf.toString(), event.getPriority() );
    }

    public void targetFinished( BuildEvent event )
    {
        log( "<< TARGET FINISHED -- " + event.getTarget(), Project.MSG_DEBUG );
        String time = formatTime( System.currentTimeMillis() - targetStartTime );
        log( event.getTarget() + ":  duration " + time, Project.MSG_VERBOSE );
        out.flush();
    }

    public void targetStarted( BuildEvent event )
    {
        log( ">> TARGET STARTED -- " + event.getTarget(), Project.MSG_DEBUG );
        log( StringUtils.LINE_SEP + event.getTarget().getName() + ":", Project.MSG_INFO );
        targetStartTime = System.currentTimeMillis();
    }

    public void taskFinished( BuildEvent event )
    {
        log( "<<< TASK FINISHED -- " + event.getTask(), Project.MSG_DEBUG );
        out.flush();
    }

    public void taskStarted( BuildEvent event )
    {
        log( ">>> TASK STARTED -- " + event.getTask(), Project.MSG_DEBUG );
    }

    /**
     * The thing that actually sends the information to the output.
     *
     * @param mesg The message to log.
     * @param level The verbosity level of the message.
     */
    private void log( String mesg, int level )
    {
        if( record && ( level <= loglevel ) )
        {
            out.println( mesg );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17985.java