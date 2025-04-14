error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6696.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6696.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6696.java
text:
```scala
private final A@@spectManager getAspectManager()

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.myrmidon.components.executor;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.myrmidon.api.Task;
import org.apache.myrmidon.api.TaskException;
import org.apache.myrmidon.interfaces.aspect.AspectManager;
import org.apache.myrmidon.interfaces.executor.ExecutionFrame;

/**
 * The AspectAwareExecutor executes the tasks but also calls
 * the aspects helpers.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision$ $Date$
 */
public class AspectAwareExecutor
    extends DefaultExecutor
{
    private final static Resources REZ =
        ResourceManager.getPackageResources( AspectAwareExecutor.class );

    private final static Configuration[] EMPTY_ELEMENTS = new Configuration[ 0 ];

    private AspectManager m_aspectManager;

    /**
     * Retrieve relevent services.
     *
     * @param serviceManager the ServiceManager
     * @exception ServiceException if an error occurs
     */
    public void service( final ServiceManager serviceManager )
        throws ServiceException
    {
        super.service( serviceManager );

        m_aspectManager = (AspectManager)serviceManager.lookup( AspectManager.ROLE );
    }

    public void execute( final Configuration taskModel, final ExecutionFrame frame )
        throws TaskException
    {
        try
        {
            executeTask( taskModel, frame );
        }
        catch( final TaskException te )
        {
            final boolean isError = getAspectManager().error( te );
            if( !isError )
            {
                throw te;
            }
        }
    }

    private void executeTask( final Configuration model,
                              final ExecutionFrame frame )
        throws TaskException
    {
        try
        {
            Configuration taskModel = getAspectManager().preCreate( model );
            taskModel = prepareAspects( taskModel );

            final String taskName = taskModel.getName();
            debug( "creating.notice", taskName );
            final Task task = doCreateTask( taskName, frame );
            getAspectManager().postCreate( task );

            debug( "logger.notice", taskName );
            final Logger logger = frame.getLogger();
            getAspectManager().preLogEnabled( logger );

            debug( "contextualizing.notice", taskName );
            doContextualize( task, taskModel, frame.getContext() );

            debug( "configuring.notice", taskName );
            getAspectManager().preConfigure( taskModel );
            doConfigure( task, taskModel, frame.getContext() );

            debug( "executing.notice", taskName );
            getAspectManager().preExecute();
            doExecute( taskModel, task );
            getAspectManager().preDestroy();
        }
        catch( Exception e )
        {
            // Wrap in generic error message
            final String message = REZ.getString( "execute.error",
                                                  model.getName(),
                                                  model.getLocation() );
            throw new TaskException( message, e );
        }
    }

    protected void doExecute( final Configuration taskModel, final Task task )
        throws TaskException
    {
        task.execute();
    }

    //TODO: Extract and clean taskModel here.
    //Get all parameters from model and provide to appropriate aspect.
    //aspect( final Parameters parameters, final Configuration[] elements )
    private final Configuration prepareAspects( final Configuration taskModel )
        throws TaskException
    {
        final DefaultConfiguration newTaskModel =
            new DefaultConfiguration( taskModel.getName(), taskModel.getLocation() );
        final HashMap parameterMap = new HashMap();
        final HashMap elementMap = new HashMap();

        processAttributes( taskModel, newTaskModel, parameterMap );
        processElements( taskModel, newTaskModel, elementMap );
        try
        {
            newTaskModel.setValue( taskModel.getValue() );
        }
        catch( final ConfigurationException cee )
        {
            //Will never occur
        }

        dispatchAspectsSettings( parameterMap, elementMap );
        checkForUnusedSettings( parameterMap, elementMap );

        return newTaskModel;
    }

    private final void dispatchAspectsSettings( final HashMap parameterMap,
                                                final HashMap elementMap )
        throws TaskException
    {
        final String[] names = getAspectManager().getNames();

        for( int i = 0; i < names.length; i++ )
        {
            final ArrayList elementList = (ArrayList)elementMap.remove( names[ i ] );

            Parameters parameters = (Parameters)parameterMap.remove( names[ i ] );
            if( null == parameters )
            {
                parameters = Parameters.EMPTY_PARAMETERS;
            }

            Configuration[] elements = null;
            if( null == elementList )
            {
                elements = EMPTY_ELEMENTS;
            }
            else
            {
                elements = (Configuration[])elementList.toArray( EMPTY_ELEMENTS );
            }

            dispatch( names[ i ], parameters, elements );
        }
    }

    private final void checkForUnusedSettings( final HashMap parameterMap,
                                               final HashMap elementMap )
        throws TaskException
    {
        if( 0 != parameterMap.size() )
        {
            final String[] namespaces =
                (String[])parameterMap.keySet().toArray( new String[ 0 ] );

            for( int i = 0; i < namespaces.length; i++ )
            {
                final String namespace = namespaces[ i ];
                final Parameters parameters = (Parameters)parameterMap.get( namespace );
                final ArrayList elementList = (ArrayList)elementMap.remove( namespace );

                Configuration[] elements = null;

                if( null == elementList )
                {
                    elements = EMPTY_ELEMENTS;
                }
                else
                {
                    elements = (Configuration[])elementList.toArray( EMPTY_ELEMENTS );
                }

                unusedSetting( namespace, parameters, elements );
            }
        }

        if( 0 != elementMap.size() )
        {
            final String[] namespaces =
                (String[])elementMap.keySet().toArray( new String[ 0 ] );

            for( int i = 0; i < namespaces.length; i++ )
            {
                final String namespace = namespaces[ i ];
                final ArrayList elementList = (ArrayList)elementMap.remove( namespace );
                final Configuration[] elements =
                    (Configuration[])elementList.toArray( EMPTY_ELEMENTS );

                unusedSetting( namespace, Parameters.EMPTY_PARAMETERS, elements );
            }
        }
    }

    private void unusedSetting( final String namespace,
                                final Parameters parameters,
                                final Configuration[] elements )
        throws TaskException
    {
        final String message =
            REZ.getString( "unused-settings.error",
                           namespace,
                           Integer.toString( parameters.getNames().length ),
                           Integer.toString( elements.length ) );
        throw new TaskException( message );
    }

    private void dispatch( final String namespace,
                           final Parameters parameters,
                           final Configuration[] elements )
        throws TaskException
    {
        getAspectManager().dispatchAspectSettings( namespace, parameters, elements );

        if( getLogger().isDebugEnabled() )
        {
            final String message =
                REZ.getString( "dispatch-settings.notice",
                               namespace,
                               Integer.toString( parameters.getNames().length ),
                               Integer.toString( elements.length ) );
            getLogger().debug( message );
        }
    }

    private final void processElements( final Configuration taskModel,
                                        final DefaultConfiguration newTaskModel,
                                        final HashMap map )
    {
        final Configuration[] elements = taskModel.getChildren();
        for( int i = 0; i < elements.length; i++ )
        {
            final String name = elements[ i ].getName();
            final int index = name.indexOf( ':' );

            if( -1 == index )
            {
                newTaskModel.addChild( elements[ i ] );
            }
            else
            {
                final String namespace = name.substring( 0, index );
                final ArrayList elementSet = getElements( namespace, map );
                elementSet.add( elements[ i ] );
            }
        }
    }

    private final void processAttributes( final Configuration taskModel,
                                          final DefaultConfiguration newTaskModel,
                                          final HashMap map )
    {
        final String[] attributes = taskModel.getAttributeNames();
        for( int i = 0; i < attributes.length; i++ )
        {
            final String name = attributes[ i ];
            final String value = taskModel.getAttribute( name, null );

            final int index = name.indexOf( ':' );

            if( -1 == index )
            {
                newTaskModel.setAttribute( name, value );
            }
            else
            {
                final String namespace = name.substring( 0, index );
                final String localName = name.substring( index + 1 );
                final Parameters parameters = getParameters( namespace, map );
                parameters.setParameter( localName, value );
            }
        }
    }

    private final ArrayList getElements( final String namespace, final HashMap map )
    {
        ArrayList elements = (ArrayList)map.get( namespace );

        if( null == elements )
        {
            elements = new ArrayList();
            map.put( namespace, elements );
        }

        return elements;
    }

    private final Parameters getParameters( final String namespace, final HashMap map )
    {
        Parameters parameters = (Parameters)map.get( namespace );

        if( null == parameters )
        {
            parameters = new Parameters();
            map.put( namespace, parameters );
        }

        return parameters;
    }

    protected final AspectManager getAspectManager()
    {
        return m_aspectManager;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6696.java