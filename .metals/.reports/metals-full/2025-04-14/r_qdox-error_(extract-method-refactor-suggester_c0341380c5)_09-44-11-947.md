error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8469.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8469.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8469.java
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
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;


/**
 * Task allows to reconfigure a project, recurcively or not
 *
 * @author Benoit Moussaud benoit.moussaud@criltelecom.com
 */
public class CCMReconfigure extends Continuus
{

    /**
     * /recurse --
     */
    public final static String FLAG_RECURSE = "/recurse";

    /**
     * /recurse --
     */
    public final static String FLAG_VERBOSE = "/verbose";

    /**
     * /project flag -- target project
     */
    public final static String FLAG_PROJECT = "/project";

    private String project = null;
    private boolean recurse = false;
    private boolean verbose = false;

    public CCMReconfigure()
    {
        super();
        setCcmAction( COMMAND_RECONFIGURE );
    }

    /**
     * Set the value of project.
     *
     * @param v Value to assign to project.
     */
    public void setCcmProject( String v )
    {
        this.project = v;
    }

    /**
     * Set the value of recurse.
     *
     * @param v Value to assign to recurse.
     */
    public void setRecurse( boolean v )
    {
        this.recurse = v;
    }

    /**
     * Set the value of verbose.
     *
     * @param v Value to assign to verbose.
     */
    public void setVerbose( boolean v )
    {
        this.verbose = v;
    }

    /**
     * Get the value of project.
     *
     * @return value of project.
     */
    public String getCcmProject()
    {
        return project;
    }


    /**
     * Get the value of recurse.
     *
     * @return value of recurse.
     */
    public boolean isRecurse()
    {
        return recurse;
    }


    /**
     * Get the value of verbose.
     *
     * @return value of verbose.
     */
    public boolean isVerbose()
    {
        return verbose;
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

        // build the command line from what we got the format
        // as specified in the CCM.EXE help
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

        if( isRecurse() == true )
        {
            cmd.createArgument().setValue( FLAG_RECURSE );
        }// end of if ()

        if( isVerbose() == true )
        {
            cmd.createArgument().setValue( FLAG_VERBOSE );
        }// end of if ()

        if( getCcmProject() != null )
        {
            cmd.createArgument().setValue( FLAG_PROJECT );
            cmd.createArgument().setValue( getCcmProject() );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8469.java