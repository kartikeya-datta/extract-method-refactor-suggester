error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7692.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7692.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7692.java
text:
```scala
l@@og( "setting a recorder for name " + filename,

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.tools.ant.taskdefs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Hashtable;
import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.EnumeratedAttribute;

/**
 * This task is the manager for RecorderEntry's. It is this class that holds all
 * entries, modifies them every time the &lt;recorder&gt; task is called, and
 * addes them to the build listener process.
 *
 * @author <a href="mailto:jayglanville@home.com">J D Glanville</a>
 * @version 0.5
 * @see RecorderEntry
 */
public class Recorder extends Task
{
    /**
     * The list of recorder entries.
     */
    private static Hashtable recorderEntries = new Hashtable();

    //////////////////////////////////////////////////////////////////////
    // ATTRIBUTES

    /**
     * The name of the file to record to.
     */
    private String filename = null;
    /**
     * Whether or not to append. Need Boolean to record an unset state (null).
     */
    private Boolean append = null;
    /**
     * Whether to start or stop recording. Need Boolean to record an unset state
     * (null).
     */
    private Boolean start = null;
    /**
     * What level to log? -1 means not initialized yet.
     */
    private int loglevel = -1;

    /**
     * Sets the action for the associated recorder entry.
     *
     * @param action The action for the entry to take: start or stop.
     */
    public void setAction( ActionChoices action )
    {
        if( action.getValue().equalsIgnoreCase( "start" ) )
        {
            start = Boolean.TRUE;
        }
        else
        {
            start = Boolean.FALSE;
        }
    }

    /**
     * Whether or not the logger should append to a previous file.
     *
     * @param append The new Append value
     */
    public void setAppend( boolean append )
    {
        this.append = new Boolean( append );
    }

    /**
     * Sets the level to which this recorder entry should log to.
     *
     * @param level The new Loglevel value
     * @see VerbosityLevelChoices
     */
    public void setLoglevel( VerbosityLevelChoices level )
    {
        //I hate cascading if/elseif clauses !!!
        String lev = level.getValue();
        if( lev.equalsIgnoreCase( "error" ) )
        {
            loglevel = Project.MSG_ERR;
        }
        else if( lev.equalsIgnoreCase( "warn" ) )
        {
            loglevel = Project.MSG_WARN;
        }
        else if( lev.equalsIgnoreCase( "info" ) )
        {
            loglevel = Project.MSG_INFO;
        }
        else if( lev.equalsIgnoreCase( "verbose" ) )
        {
            loglevel = Project.MSG_VERBOSE;
        }
        else if( lev.equalsIgnoreCase( "debug" ) )
        {
            loglevel = Project.MSG_DEBUG;
        }
    }

    //////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS / INITIALIZERS

    //////////////////////////////////////////////////////////////////////
    // ACCESSOR METHODS

    /**
     * Sets the name of the file to log to, and the name of the recorder entry.
     *
     * @param fname File name of logfile.
     */
    public void setName( String fname )
    {
        filename = fname;
    }

    //////////////////////////////////////////////////////////////////////
    // CORE / MAIN BODY

    /**
     * The main execution.
     *
     * @exception TaskException Description of Exception
     */
    public void execute()
        throws TaskException
    {
        if( filename == null )
            throw new TaskException( "No filename specified" );

        getProject().log( "setting a recorder for name " + filename,
                          Project.MSG_DEBUG );

        // get the recorder entry
        RecorderEntry recorder = getRecorder( filename, getProject() );
        // set the values on the recorder
        recorder.setMessageOutputLevel( loglevel );
        recorder.setRecordState( start );
    }

    /**
     * Gets the recorder that's associated with the passed in name. If the
     * recorder doesn't exist, then a new one is created.
     *
     * @param name Description of Parameter
     * @param proj Description of Parameter
     * @return The Recorder value
     * @exception TaskException Description of Exception
     */
    protected RecorderEntry getRecorder( String name, Project proj )
        throws TaskException
    {
        Object o = recorderEntries.get( name );
        RecorderEntry entry;
        if( o == null )
        {
            // create a recorder entry
            try
            {
                entry = new RecorderEntry( name );
                PrintStream out = null;
                if( append == null )
                {
                    out = new PrintStream(
                        new FileOutputStream( name ) );
                }
                else
                {
                    out = new PrintStream(
                        new FileOutputStream( name, append.booleanValue() ) );
                }
                entry.setErrorPrintStream( out );
                entry.setOutputPrintStream( out );
            }
            catch( IOException ioe )
            {
                throw new TaskException( "Problems creating a recorder entry",
                                         ioe );
            }
            proj.addBuildListener( entry );
            recorderEntries.put( name, entry );
        }
        else
        {
            entry = (RecorderEntry)o;
        }
        return entry;
    }

    //////////////////////////////////////////////////////////////////////
    // INNER CLASSES

    /**
     * A list of possible values for the <code>setAction()</code> method.
     * Possible values include: start and stop.
     *
     * @author RT
     */
    public static class ActionChoices extends EnumeratedAttribute
    {
        private final static String[] values = {"start", "stop"};

        public String[] getValues()
        {
            return values;
        }
    }

    /**
     * A list of possible values for the <code>setLoglevel()</code> method.
     * Possible values include: error, warn, info, verbose, debug.
     *
     * @author RT
     */
    public static class VerbosityLevelChoices extends EnumeratedAttribute
    {
        private final static String[] values = {"error", "warn", "info",
                                                "verbose", "debug"};

        public String[] getValues()
        {
            return values;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7692.java