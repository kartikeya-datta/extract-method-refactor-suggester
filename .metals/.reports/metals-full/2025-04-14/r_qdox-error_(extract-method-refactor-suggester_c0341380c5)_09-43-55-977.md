error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16231.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16231.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16231.java
text:
```scala
private final static R@@esources REZ =

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included  with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.antlib.core;

import java.util.ArrayList;
import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.myrmidon.api.TaskException;
import org.apache.myrmidon.framework.AbstractContainerTask;
import org.apache.myrmidon.framework.Condition;
import org.apache.myrmidon.interfaces.executor.ExecutionFrame;
import org.apache.myrmidon.interfaces.executor.Executor;

/**
 * A simple task to test a supplied condition. If the condition is true
 * then it will execute the inner tasks, else it won't.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision$ $Date$
 * @ant:task name="if"
 */
public class IfTask
    extends AbstractContainerTask
{
    private static final Resources REZ =
        ResourceManager.getPackageResources( IfTask.class );

    private Condition m_condition;
    private ArrayList m_tasks = new ArrayList();

    /**
     * Set if clause on pattern.
     *
     * @param condition the condition
     * @exception TaskException if an error occurs
     */
    public void setTest( final String condition )
        throws TaskException
    {
        verifyConditionNull();
        m_condition = new Condition( true, condition );
    }

    /**
     * Set unless clause of pattern.
     *
     * @param condition the unless clause
     * @exception TaskException if an error occurs
     */
    public void setNotTest( final String condition )
        throws TaskException
    {
        verifyConditionNull();
        m_condition = new Condition( false, condition );
    }

    public void add( final Configuration task )
    {
        m_tasks.add( task );
    }

    public void execute()
        throws TaskException
    {
        if( null == m_condition )
        {
            final String message = REZ.getString( "if.no-condition.error" );
            throw new TaskException( message );
        }

        final Configuration[] tasks =
            (Configuration[])m_tasks.toArray( new Configuration[ m_tasks.size() ] );

        final ExecutionFrame frame = (ExecutionFrame)getService( ExecutionFrame.class );
        final Executor executor = (Executor)getService( Executor.class );

        for( int i = 0; i < tasks.length; i++ )
        {
            final Configuration task = tasks[ i ];
            executor.execute( task, frame );
        }
    }

    public String toString()
    {
        return "If['" + m_condition + "]";
    }

    /**
     * Utility method to make sure condition unset.
     * Made so that it is not possible for both if and unless to be set.
     *
     * @exception TaskException if an error occurs
     */
    private void verifyConditionNull()
        throws TaskException
    {
        if( null != m_condition )
        {
            final String message = REZ.getString( "if.ifelse-duplicate.error" );
            throw new TaskException( message );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16231.java