error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8468.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8468.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8468.java
text:
```scala
t@@hrow new BuildException( msg );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.tools.ant.taskdefs.optional.ccm;
import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;

/**
 * Class common to all check commands (checkout, checkin,checkin default task);
 *
 * @author Benoit Moussaud benoit.moussaud@criltelecom.com
 */
public class CCMCheck extends Continuus
{

    /**
     * -comment flag -- comment to attach to the file
     */
    public final static String FLAG_COMMENT = "/comment";

    /**
     * -task flag -- associate checckout task with task
     */
    public final static String FLAG_TASK = "/task";

    private File _file = null;
    private String _comment = null;
    private String _task = null;

    public CCMCheck()
    {
        super();
    }

    /**
     * Set the value of comment.
     *
     * @param v Value to assign to comment.
     */
    public void setComment( String v )
    {
        this._comment = v;
    }

    /**
     * Set the value of file.
     *
     * @param v Value to assign to file.
     */
    public void setFile( File v )
    {
        this._file = v;
    }

    /**
     * Set the value of task.
     *
     * @param v Value to assign to task.
     */
    public void setTask( String v )
    {
        this._task = v;
    }

    /**
     * Get the value of comment.
     *
     * @return value of comment.
     */
    public String getComment()
    {
        return _comment;
    }

    /**
     * Get the value of file.
     *
     * @return value of file.
     */
    public File getFile()
    {
        return _file;
    }


    /**
     * Get the value of task.
     *
     * @return value of task.
     */
    public String getTask()
    {
        return _task;
    }


    /**
     * Executes the task. <p>
     *
     * Builds a command line to execute ccm and then calls Exec's run method to
     * execute the command line. </p>
     *
     * @exception BuildException Description of Exception
     */
    public void execute()
        throws BuildException
    {
        Commandline commandLine = new Commandline();
        Project aProj = getProject();
        int result = 0;

        // build the command line from what we got the format is
        // ccm co /t .. files
        // as specified in the CLEARTOOL.EXE help
        commandLine.setExecutable( getCcmCommand() );
        commandLine.createArgument().setValue( getCcmAction() );

        checkOptions( commandLine );

        result = run( commandLine );
        if( result != 0 )
        {
            String msg = "Failed executing: " + commandLine.toString();
            throw new BuildException( msg, location );
        }
    }


    /**
     * Check the command line options.
     *
     * @param cmd Description of Parameter
     */
    private void checkOptions( Commandline cmd )
    {
        if( getComment() != null )
        {
            cmd.createArgument().setValue( FLAG_COMMENT );
            cmd.createArgument().setValue( getComment() );
        }

        if( getTask() != null )
        {
            cmd.createArgument().setValue( FLAG_TASK );
            cmd.createArgument().setValue( getTask() );
        }// end of if ()

        if( getFile() != null )
        {
            cmd.createArgument().setValue( _file.getAbsolutePath() );
        }// end of if ()
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8468.java