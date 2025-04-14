error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6786.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6786.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6786.java
text:
```scala
c@@lass ProjectListenerSupport

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.myrmidon.components.workspace;

import org.apache.myrmidon.listeners.LogEvent;
import org.apache.myrmidon.listeners.ProjectListener;

/**
 * Support for the project listener event dispatching.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision$ $Date$
 */
public class ProjectListenerSupport
    implements LogEvent
{
    private ProjectListener[] m_listeners = new ProjectListener[ 0 ];
    private String m_projectName;
    private String m_targetName;
    private String m_taskName;
    private String m_message;
    private Throwable m_throwable;

    /**
     * Add an extra project listener that wants to receive notification of listener events.
     *
     * @param listener the listener
     */
    public void addProjectListener( final ProjectListener listener )
    {
        final ProjectListener[] listeners = new ProjectListener[ m_listeners.length + 1 ];
        System.arraycopy( m_listeners, 0, listeners, 0, m_listeners.length );
        listeners[ m_listeners.length ] = listener;
        m_listeners = listeners;
    }

    /**
     * Remove a project listener that wants to receive notification of listener events.
     *
     * @param listener the listener
     */
    public void removeProjectListener( final ProjectListener listener )
    {
        int found = -1;

        for( int i = 0; i < m_listeners.length; i++ )
        {
            if( listener == m_listeners[ i ] )
            {
                found = i;
                break;
            }
        }

        if( -1 == found )
        {
            return;
        }

        final ProjectListener[] listeners = new ProjectListener[ m_listeners.length - 1 ];
        System.arraycopy( m_listeners, 0, listeners, 0, found );

        final int count = m_listeners.length - found - 1;
        System.arraycopy( m_listeners, found, listeners, found + 1, count );

        m_listeners = listeners;
    }

    /**
     * Fire a projectStarted event.
     */
    public void projectStarted( final String projectName )
    {
        m_projectName = projectName;
        m_targetName = null;
        m_taskName = null;

        for( int i = 0; i < m_listeners.length; i++ )
        {
            m_listeners[ i ].projectStarted( this );
        }
    }

    /**
     * Fire a projectFinished event.
     */
    public void projectFinished( final String projectName )
    {
        m_projectName = projectName;

        for( int i = 0; i < m_listeners.length; i++ )
        {
            m_listeners[ i ].projectFinished( this );
        }

        m_projectName = null;
        m_targetName = null;
        m_taskName = null;
    }

    /**
     * Fire a targetStarted event.
     */
    public void targetStarted( final String projectName, final String targetName )
    {
        m_projectName = projectName;
        m_targetName = targetName;
        m_taskName = null;

        for( int i = 0; i < m_listeners.length; i++ )
        {
            m_listeners[ i ].targetStarted( this );
        }
    }

    /**
     * Fire a targetFinished event.
     */
    public void targetFinished()
    {
        for( int i = 0; i < m_listeners.length; i++ )
        {
            m_listeners[ i ].targetFinished( this );
        }

        m_targetName = null;
        m_taskName = null;
    }

    /**
     * Fire a targetStarted event.
     */
    public void taskStarted( final String taskName )
    {
        m_taskName = taskName;

        for( int i = 0; i < m_listeners.length; i++ )
        {
            m_listeners[ i ].taskStarted( this );
        }
    }

    /**
     * Fire a taskFinished event.
     */
    public void taskFinished()
    {
        for( int i = 0; i < m_listeners.length; i++ )
        {
            m_listeners[ i ].taskFinished( this );
        }

        m_taskName = null;
    }

    /**
     * Fire a log event.
     *
     * @param message the log message
     */
    public void log( String message, Throwable throwable )
    {
        m_message = message;
        m_throwable = throwable;

        try
        {
            for( int i = 0; i < m_listeners.length; i++ )
            {
                m_listeners[ i ].log( this );
            }
        }
        finally
        {
            m_message = null;
            m_throwable = null;
        }
    }

    /**
     * Returns the message.
     */
    public String getMessage()
    {
        return m_message;
    }

    /**
     * Returns the error that occurred.
     */
    public Throwable getThrowable()
    {
        return m_throwable;
    }

    /**
     * Returns the name of the task.
     */
    public String getTaskName()
    {
        return m_taskName;
    }

    /**
     * Returns the name of the target.
     */
    public String getTargetName()
    {
        return m_targetName;
    }

    /**
     * Returns the name of the project.
     */
    public String getProjectName()
    {
        return m_projectName;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6786.java