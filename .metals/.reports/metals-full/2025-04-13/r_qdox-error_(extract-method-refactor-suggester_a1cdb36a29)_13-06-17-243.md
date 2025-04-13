error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8823.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8823.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8823.java
text:
```scala
l@@og( "Created dir: " + dir.getAbsolutePath() );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.tools.ant.taskdefs.optional.vss;

import java.io.File;
import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;

/**
 * Task to perform CheckOut commands to Microsoft Visual Source Safe.
 *
 * @author Martin Poeschl
 */
public class MSVSSCHECKOUT extends MSVSS
{

    private String m_LocalPath = null;
    private boolean m_Recursive = false;
    private String m_Version = null;
    private String m_Date = null;
    private String m_Label = null;
    private String m_AutoResponse = null;

    public void setAutoresponse( String response )
    {
        if( response.equals( "" ) || response.equals( "null" ) )
        {
            m_AutoResponse = null;
        }
        else
        {
            m_AutoResponse = response;
        }
    }

    /**
     * Set the stored date string <p>
     *
     * Note we assume that if the supplied string has the value "null" that
     * something went wrong and that the string value got populated from a null
     * object. This happens if a ant variable is used e.g. date="${date}" when
     * date has not been defined to ant!
     *
     * @param date The new Date value
     */
    public void setDate( String date )
    {
        if( date.equals( "" ) || date.equals( "null" ) )
        {
            m_Date = null;
        }
        else
        {
            m_Date = date;
        }
    }

    /**
     * Set the labeled version to operate on in SourceSafe <p>
     *
     * Note we assume that if the supplied string has the value "null" that
     * something went wrong and that the string value got populated from a null
     * object. This happens if a ant variable is used e.g.
     * label="${label_server}" when label_server has not been defined to ant!
     *
     * @param label The new Label value
     */
    public void setLabel( String label )
    {
        if( label.equals( "" ) || label.equals( "null" ) )
        {
            m_Label = null;
        }
        else
        {
            m_Label = label;
        }
    }

    /**
     * Set the local path.
     *
     * @param localPath The new Localpath value
     */
    public void setLocalpath( Path localPath )
    {
        m_LocalPath = localPath.toString();
    }

    /**
     * Set behaviour recursive or non-recursive
     *
     * @param recursive The new Recursive value
     */
    public void setRecursive( boolean recursive )
    {
        m_Recursive = recursive;
    }

    /**
     * Set the stored version string <p>
     *
     * Note we assume that if the supplied string has the value "null" that
     * something went wrong and that the string value got populated from a null
     * object. This happens if a ant variable is used e.g.
     * version="${ver_server}" when ver_server has not been defined to ant!
     *
     * @param version The new Version value
     */
    public void setVersion( String version )
    {
        if( version.equals( "" ) || version.equals( "null" ) )
        {
            m_Version = null;
        }
        else
        {
            m_Version = version;
        }
    }

    /**
     * Checks the value set for the autoResponse. if it equals "Y" then we
     * return -I-Y if it equals "N" then we return -I-N otherwise we return -I
     *
     * @param cmd Description of Parameter
     */
    public void getAutoresponse( Commandline cmd )
    {

        if( m_AutoResponse == null )
        {
            cmd.createArgument().setValue( FLAG_AUTORESPONSE_DEF );
        }
        else if( m_AutoResponse.equalsIgnoreCase( "Y" ) )
        {
            cmd.createArgument().setValue( FLAG_AUTORESPONSE_YES );

        }
        else if( m_AutoResponse.equalsIgnoreCase( "N" ) )
        {
            cmd.createArgument().setValue( FLAG_AUTORESPONSE_NO );
        }
        else
        {
            cmd.createArgument().setValue( FLAG_AUTORESPONSE_DEF );
        }// end of else

    }

    /**
     * Builds and returns the -GL flag command if required <p>
     *
     * The localpath is created if it didn't exist
     *
     * @param cmd Description of Parameter
     */
    public void getLocalpathCommand( Commandline cmd )
        throws TaskException
    {
        if( m_LocalPath == null )
        {
            return;
        }
        else
        {
            // make sure m_LocalDir exists, create it if it doesn't
            File dir = resolveFile( m_LocalPath );
            if( !dir.exists() )
            {
                boolean done = dir.mkdirs();
                if( done == false )
                {
                    String msg = "Directory " + m_LocalPath + " creation was not " +
                        "succesful for an unknown reason";
                    throw new TaskException( msg );
                }
                project.log( "Created dir: " + dir.getAbsolutePath() );
            }

            cmd.createArgument().setValue( FLAG_OVERRIDE_WORKING_DIR + m_LocalPath );
        }
    }

    /**
     * @param cmd Description of Parameter
     */
    public void getRecursiveCommand( Commandline cmd )
    {
        if( !m_Recursive )
        {
            return;
        }
        else
        {
            cmd.createArgument().setValue( FLAG_RECURSION );
        }
    }

    /**
     * Simple order of priority. Returns the first specified of version, date,
     * label If none of these was specified returns ""
     *
     * @param cmd Description of Parameter
     */
    public void getVersionCommand( Commandline cmd )
    {

        if( m_Version != null )
        {
            cmd.createArgument().setValue( FLAG_VERSION + m_Version );
        }
        else if( m_Date != null )
        {
            cmd.createArgument().setValue( FLAG_VERSION_DATE + m_Date );
        }
        else if( m_Label != null )
        {
            cmd.createArgument().setValue( FLAG_VERSION_LABEL + m_Label );
        }
    }

    /**
     * Executes the task. <p>
     *
     * Builds a command line to execute ss and then calls Exec's run method to
     * execute the command line.
     *
     * @exception TaskException Description of Exception
     */
    public void execute()
        throws TaskException
    {
        Commandline commandLine = new Commandline();
        int result = 0;

        // first off, make sure that we've got a command and a vssdir ...
        if( getVsspath() == null )
        {
            String msg = "vsspath attribute must be set!";
            throw new TaskException( msg );
        }

        // now look for illegal combinations of things ...

        // build the command line from what we got the format is
        // ss Checkout VSS items [-G] [-C] [-H] [-I-] [-N] [-O] [-R] [-V] [-Y] [-?]
        // as specified in the SS.EXE help
        commandLine.setExecutable( getSSCommand() );
        commandLine.createArgument().setValue( COMMAND_CHECKOUT );

        // VSS items
        commandLine.createArgument().setValue( getVsspath() );
        // -GL
        getLocalpathCommand( commandLine );
        // -I- or -I-Y or -I-N
        getAutoresponse( commandLine );
        // -R
        getRecursiveCommand( commandLine );
        // -V
        getVersionCommand( commandLine );
        // -Y
        getLoginCommand( commandLine );

        result = run( commandLine );
        if( result != 0 )
        {
            String msg = "Failed executing: " + commandLine.toString();
            throw new TaskException( msg );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8823.java