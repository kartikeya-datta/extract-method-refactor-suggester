error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7579.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7579.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7579.java
text:
```scala
g@@etWriter().println( ExceptionUtil.printStackTrace( throwable, 8, true ) );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.myrmidon.listeners;

import java.io.PrintWriter;
import org.apache.avalon.framework.ExceptionUtil;

/**
 * Classic listener that emulates the default ant1.x listener.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision$ $Date$
 * @ant.type type="listener" name="classic"
 */
public class ClassicProjectListener
    extends AbstractProjectListener
{
    private final PrintWriter m_printWriter;

    public ClassicProjectListener()
    {
        m_printWriter = new PrintWriter( System.out, true );
    }

    /**
     * Notify listener of targetStarted event.
     */
    public void targetStarted( final TargetEvent event )
    {
        writeTargetHeader( event );
    }

    /**
     * Notify listener of targetFinished event.
     */
    public void targetFinished( TargetEvent event )
    {
        getWriter().println();
    }

    /**
     * Notify listener of log message event.
     */
    public void log( final LogEvent event )
    {
        writeMessage( event );
        writeThrowable( event );
    }

    /**
     * Returns the PrintWriter to write to.
     */
    protected PrintWriter getWriter()
    {
        return m_printWriter;
    }

    /**
     * Writes the target header.
     */
    protected void writeTargetHeader( final TargetEvent event )
    {
        getWriter().println( event.getTargetName() + ":" );
    }

    /**
     * Writes a message
     */
    protected void writeMessage( final LogEvent event )
    {
        // Write the message
        final String message = event.getMessage();
        final String task = event.getTaskName();
        if( null != task )
        {
            getWriter().println( "\t[" + task + "] " + message );
        }
        else
        {
            getWriter().println( message );
        }
    }

    /**
     * Writes a throwable.
     */
    private void writeThrowable( final LogEvent event )
    {
        // Write the exception, if any
        final Throwable throwable = event.getThrowable();
        if( throwable != null )
        {
            getWriter().println( ExceptionUtil.printStackTrace( throwable, 5, true ) );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7579.java