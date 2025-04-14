error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7655.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7655.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7655.java
text:
```scala
s@@etViewPath( getBaseDirectory().getPath() );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.tools.ant.taskdefs.optional.clearcase;

import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;

/**
 * Task to perform an Update command to ClearCase. <p>
 *
 * The following attributes are interpretted:
 * <tableborder="1">
 *
 *   <tr>
 *
 *     <th>
 *       Attribute
 *     </th>
 *
 *     <th>
 *       Values
 *     </th>
 *
 *     <th>
 *       Required
 *     </th>
 *
 *   </tr>
 *
 *   <tr>
 *
 *     <td>
 *       viewpath
 *     </td>
 *
 *     <td>
 *       Path to the ClearCase view file or directory that the command will
 *       operate on
 *     </td>
 *
 *     <td>
 *       No
 *     </td>
 *
 *     <tr>
 *
 *       <tr>
 *
 *         <td>
 *           graphical
 *         </td>
 *
 *         <td>
 *           Displays a graphical dialog during the update
 *         </td>
 *
 *         <td>
 *           No
 *         </td>
 *
 *         <tr>
 *
 *           <tr>
 *
 *             <td>
 *               log
 *             </td>
 *
 *             <td>
 *               Specifies a log file for ClearCase to write to
 *             </td>
 *
 *             <td>
 *               No
 *             </td>
 *
 *             <tr>
 *
 *               <tr>
 *
 *                 <td>
 *                   overwrite
 *                 </td>
 *
 *                 <td>
 *                   Specifies whether to overwrite hijacked files or not
 *                 </td>
 *
 *                 <td>
 *                   No
 *                 </td>
 *
 *                 <tr>
 *
 *                   <tr>
 *
 *                     <td>
 *                       rename
 *                     </td>
 *
 *                     <td>
 *                       Specifies that hijacked files should be renamed with a
 *                       .keep extension
 *                     </td>
 *
 *                     <td>
 *                       No
 *                     </td>
 *
 *                     <tr>
 *
 *                       <tr>
 *
 *                         <td>
 *                           currenttime
 *                         </td>
 *
 *                         <td>
 *                           Specifies that modification time should be written
 *                           as the current time. Either currenttime or
 *                           preservetime can be specified.
 *                         </td>
 *
 *                         <td>
 *                           No
 *                         </td>
 *
 *                         <tr>
 *
 *                           <tr>
 *
 *                             <td>
 *                               preservetime
 *                             </td>
 *
 *                             <td>
 *                               Specifies that modification time should
 *                               preserved from the VOB time. Either currenttime
 *                               or preservetime can be specified.
 *                             </td>
 *
 *                             <td>
 *                               No
 *                             </td>
 *
 *                             <tr>
 *
 *                             </table>
 *
 *
 * @author Curtis White
 */
public class CCUpdate extends ClearCase
{

    /**
     * -graphical flag -- display graphical dialog during update operation
     */
    public final static String FLAG_GRAPHICAL = "-graphical";
    /**
     * -log flag -- file to log status to
     */
    public final static String FLAG_LOG = "-log";
    /**
     * -overwrite flag -- overwrite hijacked files
     */
    public final static String FLAG_OVERWRITE = "-overwrite";
    /**
     * -noverwrite flag -- do not overwrite hijacked files
     */
    public final static String FLAG_NOVERWRITE = "-noverwrite";
    /**
     * -rename flag -- rename hijacked files with .keep extension
     */
    public final static String FLAG_RENAME = "-rename";
    /**
     * -ctime flag -- modified time is written as the current time
     */
    public final static String FLAG_CURRENTTIME = "-ctime";
    /**
     * -ptime flag -- modified time is written as the VOB time
     */
    public final static String FLAG_PRESERVETIME = "-ptime";
    private boolean m_Graphical = false;
    private boolean m_Overwrite = false;
    private boolean m_Rename = false;
    private boolean m_Ctime = false;
    private boolean m_Ptime = false;
    private String m_Log = null;

    /**
     * Set modified time based on current time
     *
     * @param ct the status to set the flag to
     */
    public void setCurrentTime( boolean ct )
    {
        m_Ctime = ct;
    }

    /**
     * Set graphical flag status
     *
     * @param graphical the status to set the flag to
     */
    public void setGraphical( boolean graphical )
    {
        m_Graphical = graphical;
    }

    /**
     * Set log file where cleartool can record the status of the command
     *
     * @param log the path to the log file
     */
    public void setLog( String log )
    {
        m_Log = log;
    }

    /**
     * Set overwrite hijacked files status
     *
     * @param ow the status to set the flag to
     */
    public void setOverwrite( boolean ow )
    {
        m_Overwrite = ow;
    }

    /**
     * Preserve modified time from the VOB time
     *
     * @param pt the status to set the flag to
     */
    public void setPreserveTime( boolean pt )
    {
        m_Ptime = pt;
    }

    /**
     * Set rename hijacked files status
     *
     * @param ren the status to set the flag to
     */
    public void setRename( boolean ren )
    {
        m_Rename = ren;
    }

    /**
     * Get current time status
     *
     * @return boolean containing status of current time flag
     */
    public boolean getCurrentTime()
    {
        return m_Ctime;
    }

    /**
     * Get graphical flag status
     *
     * @return boolean containing status of graphical flag
     */
    public boolean getGraphical()
    {
        return m_Graphical;
    }

    /**
     * Get log file
     *
     * @return String containing the path to the log file
     */
    public String getLog()
    {
        return m_Log;
    }

    /**
     * Get overwrite hijacked files status
     *
     * @return boolean containing status of overwrite flag
     */
    public boolean getOverwrite()
    {
        return m_Overwrite;
    }

    /**
     * Get preserve time status
     *
     * @return boolean containing status of preserve time flag
     */
    public boolean getPreserveTime()
    {
        return m_Ptime;
    }

    /**
     * Get rename hijacked files status
     *
     * @return boolean containing status of rename flag
     */
    public boolean getRename()
    {
        return m_Rename;
    }

    /**
     * Executes the task. <p>
     *
     * Builds a command line to execute cleartool and then calls Exec's run
     * method to execute the command line.
     *
     * @exception TaskException Description of Exception
     */
    public void execute()
        throws TaskException
    {
        Commandline commandLine = new Commandline();
        Project aProj = getProject();
        int result = 0;

        // Default the viewpath to basedir if it is not specified
        if( getViewPath() == null )
        {
            setViewPath( aProj.getBaseDir().getPath() );
        }

        // build the command line from what we got the format is
        // cleartool update [options...] [viewpath ...]
        // as specified in the CLEARTOOL.EXE help
        commandLine.setExecutable( getClearToolCommand() );
        commandLine.createArgument().setValue( COMMAND_UPDATE );

        // Check the command line options
        checkOptions( commandLine );

        // For debugging
        System.out.println( commandLine.toString() );

        result = run( commandLine );
        if( result != 0 )
        {
            String msg = "Failed executing: " + commandLine.toString();
            throw new TaskException( msg );
        }
    }

    /**
     * Get the 'log' command
     *
     * @param cmd Description of Parameter
     */
    private void getLogCommand( Commandline cmd )
    {
        if( getLog() == null )
        {
            return;
        }
        else
        {
            /*
             * Had to make two separate commands here because if a space is
             * inserted between the flag and the value, it is treated as a
             * Windows filename with a space and it is enclosed in double
             * quotes ("). This breaks clearcase.
             */
            cmd.createArgument().setValue( FLAG_LOG );
            cmd.createArgument().setValue( getLog() );
        }
    }

    /**
     * Check the command line options.
     *
     * @param cmd Description of Parameter
     */
    private void checkOptions( Commandline cmd )
    {
        // ClearCase items
        if( getGraphical() )
        {
            // -graphical
            cmd.createArgument().setValue( FLAG_GRAPHICAL );
        }
        else
        {
            if( getOverwrite() )
            {
                // -overwrite
                cmd.createArgument().setValue( FLAG_OVERWRITE );
            }
            else
            {
                if( getRename() )
                {
                    // -rename
                    cmd.createArgument().setValue( FLAG_RENAME );
                }
                else
                {
                    // -noverwrite
                    cmd.createArgument().setValue( FLAG_NOVERWRITE );
                }
            }

            if( getCurrentTime() )
            {
                // -ctime
                cmd.createArgument().setValue( FLAG_CURRENTTIME );
            }
            else
            {
                if( getPreserveTime() )
                {
                    // -ptime
                    cmd.createArgument().setValue( FLAG_PRESERVETIME );
                }
            }

            // -log logname
            getLogCommand( cmd );
        }

        // viewpath
        cmd.createArgument().setValue( getViewPath() );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7655.java