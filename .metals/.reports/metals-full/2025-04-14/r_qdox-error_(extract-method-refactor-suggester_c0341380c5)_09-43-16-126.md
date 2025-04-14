error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3373.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3373.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3373.java
text:
```scala
T@@hread.sleep( millis );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.antlib.build;

import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;
import org.apache.myrmidon.api.AbstractTask;
import org.apache.myrmidon.api.TaskException;

/**
 * A task to sleep for a period of time
 *
 * @ant:task name="sleep"
 * @author steve_l@iseran.com steve loughran
 * @version $Revision$ $Date$
 */
public class SleepTask
    extends AbstractTask
{
    private final static Resources REZ =
        ResourceManager.getPackageResources( SleepTask.class );

    private int m_seconds;
    private int m_hours;
    private int m_minutes;
    private int m_milliseconds;

    /**
     * Sets the Hours attribute of the Sleep object
     */
    public void setHours( final int hours )
    {
        m_hours = hours;
    }

    /**
     * Sets the Milliseconds attribute of the Sleep object
     */
    public void setMilliseconds( final int milliseconds )
    {
        m_milliseconds = milliseconds;
    }

    /**
     * Sets the Minutes attribute of the Sleep object
     */
    public void setMinutes( final int minutes )
    {
        m_minutes = minutes;
    }

    /**
     * Sets the Seconds attribute of the Sleep object
     */
    public void setSeconds( final int seconds )
    {
        m_seconds = seconds;
    }

    /**
     * sleep for a period of time
     *
     * @param millis time to sleep
     */
    private void doSleep( final long millis )
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
     * Executes this build task. throws org.apache.tools.ant.TaskException if
     * there is an error during task execution.
     *
     * @exception TaskException Description of Exception
     */
    public void execute()
        throws TaskException
    {
        validate();
        final long sleepTime = getSleepTime();

        final String message = REZ.getString( "sleep.duration.notice", new Long( sleepTime ) );
        getLogger().debug( message );

        doSleep( sleepTime );
    }

    /**
     * verify parameters
     *
     * @throws TaskException if something is invalid
     */
    private void validate()
        throws TaskException
    {
        if( getSleepTime() < 0 )
        {
            final String message = REZ.getString( "sleep.neg-time.error" );
            throw new TaskException( message );
        }
    }

    /**
     * return time to sleep
     *
     * @return sleep time. if below 0 then there is an error
     */
    private long getSleepTime()
    {
        return ( ( ( (long)m_hours * 60 ) + m_minutes ) * 60 + m_seconds ) * 1000 + m_milliseconds;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3373.java