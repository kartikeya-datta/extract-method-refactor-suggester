error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17424.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17424.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17424.java
text:
```scala
private static final R@@esources REZ =

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.myrmidon.components.executor;

import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.myrmidon.api.Task;
import org.apache.myrmidon.api.TaskContext;
import org.apache.myrmidon.api.TaskException;
import org.apache.myrmidon.interfaces.configurer.Configurer;
import org.apache.myrmidon.interfaces.executor.ExecutionFrame;
import org.apache.myrmidon.interfaces.executor.Executor;
import org.apache.myrmidon.interfaces.type.TypeException;
import org.apache.myrmidon.interfaces.type.TypeFactory;

/**
 * The basic executor that just executes the tasks.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision$ $Date$
 */
public class DefaultExecutor
    extends AbstractLogEnabled
    implements Executor, Serviceable
{
    private final static Resources REZ =
        ResourceManager.getPackageResources( DefaultExecutor.class );

    private Configurer m_configurer;

    /**
     * Retrieve relevent services needed to deploy.
     *
     * @param serviceManager the ServiceManager
     * @exception ServiceException if an error occurs
     */
    public void service( final ServiceManager serviceManager )
        throws ServiceException
    {
        m_configurer = (Configurer)serviceManager.lookup( Configurer.ROLE );
    }

    /**
     * Executes a task.
     */
    public void execute( final Configuration taskModel, final ExecutionFrame frame )
        throws TaskException
    {
        final String taskName = taskModel.getName();
        try
        {
            debug( "creating.notice", taskName );
            final Task task = doCreateTask( taskName, frame );

            debug( "contextualizing.notice", taskName );
            doContextualize( task, taskModel, frame.getContext() );

            debug( "configuring.notice", taskName );
            doConfigure( task, taskModel, frame.getContext() );

            debug( "executing.notice", taskName );
            task.execute();
        }
        catch( Exception e )
        {
            // Wrap in generic error message
            final String message = REZ.getString( "execute.error", taskName, taskModel.getLocation() );
            throw new TaskException( message, e );
        }
    }

    protected final void debug( final String key, final String taskName )
    {
        if( getLogger().isDebugEnabled() )
        {
            final String message = REZ.getString( key, taskName );
            getLogger().debug( message );
        }
    }

    /**
     * Creates a task instance.
     */
    protected final Task doCreateTask( final String name, final ExecutionFrame frame )
        throws TaskException
    {
        try
        {
            final TypeFactory factory = frame.getTypeManager().getFactory( Task.ROLE );
            return (Task)factory.create( name );
        }
        catch( final TypeException te )
        {
            final String message = REZ.getString( "create.error", name );
            throw new TaskException( message, te );
        }
    }

    /**
     * Configures a task instance.
     */
    protected final void doConfigure( final Task task,
                                      final Configuration taskModel,
                                      final TaskContext taskContext )
        throws ConfigurationException
    {
        m_configurer.configureElement( task, taskModel, taskContext );
    }

    /**
     * Sets the context for a task.
     */
    protected final void doContextualize( final Task task,
                                          final Configuration taskModel,
                                          final TaskContext context )
        throws TaskException
    {
        try
        {
            task.contextualize( context );
        }
        catch( final Throwable throwable )
        {
            final String message =
                REZ.getString( "contextualize.error", taskModel.getName() );
            throw new TaskException( message, throwable );
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17424.java