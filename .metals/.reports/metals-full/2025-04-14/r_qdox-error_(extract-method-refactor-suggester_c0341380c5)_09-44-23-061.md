error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8465.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8465.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8465.java
text:
```scala
t@@hrow new BuildException( "Error", e );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.tools.ant.taskdefs;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * A task to sleep for a period of time
 *
 * @author steve_l@iseran.com steve loughran
 * @created 01 May 2001
 */

public class Sleep extends Task
{
    /**
     * failure flag
     */
    private boolean failOnError = true;

    /**
     * Description of the Field
     */
    private int seconds = 0;
    /**
     * Description of the Field
     */
    private int hours = 0;
    /**
     * Description of the Field
     */
    private int minutes = 0;
    /**
     * Description of the Field
     */
    private int milliseconds = 0;


    /**
     * Creates new instance
     */
    public Sleep() { }


    /**
     * Sets the FailOnError attribute of the MimeMail object
     *
     * @param failOnError The new FailOnError value
     */
    public void setFailOnError( boolean failOnError )
    {
        this.failOnError = failOnError;
    }


    /**
     * Sets the Hours attribute of the Sleep object
     *
     * @param hours The new Hours value
     */
    public void setHours( int hours )
    {
        this.hours = hours;
    }


    /**
     * Sets the Milliseconds attribute of the Sleep object
     *
     * @param milliseconds The new Milliseconds value
     */
    public void setMilliseconds( int milliseconds )
    {
        this.milliseconds = milliseconds;
    }


    /**
     * Sets the Minutes attribute of the Sleep object
     *
     * @param minutes The new Minutes value
     */
    public void setMinutes( int minutes )
    {
        this.minutes = minutes;
    }


    /**
     * Sets the Seconds attribute of the Sleep object
     *
     * @param seconds The new Seconds value
     */
    public void setSeconds( int seconds )
    {
        this.seconds = seconds;
    }


    /**
     * sleep for a period of time
     *
     * @param millis time to sleep
     */
    public void doSleep( long millis )
    {
        try
        {
            Thread.currentThread().sleep( millis );
        }
        catch( InterruptedException ie )
        {
        }
    }


    /**
     * Executes this build task. throws org.apache.tools.ant.BuildException if
     * there is an error during task execution.
     *
     * @exception BuildException Description of Exception
     */
    public void execute()
        throws BuildException
    {
        try
        {
            validate();
            long sleepTime = getSleepTime();
            log( "sleeping for " + sleepTime + " milliseconds",
                Project.MSG_VERBOSE );
            doSleep( sleepTime );
        }
        catch( Exception e )
        {
            if( failOnError )
            {
                throw new BuildException( e );
            }
            else
            {
                String text = e.toString();
                log( text, Project.MSG_ERR );
            }
        }
    }


    /**
     * verify parameters
     *
     * @throws BuildException if something is invalid
     */
    public void validate()
        throws BuildException
    {
        long sleepTime = getSleepTime();
        if( getSleepTime() < 0 )
        {
            throw new BuildException( "Negative sleep periods are not supported" );
        }
    }


    /**
     * return time to sleep
     *
     * @return sleep time. if below 0 then there is an error
     */

    private long getSleepTime()
    {
        return ( ( ( ( long )hours * 60 ) + minutes ) * 60 + seconds ) * 1000 + milliseconds;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8465.java