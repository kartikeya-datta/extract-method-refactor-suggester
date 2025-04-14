error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6693.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6693.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6693.java
text:
```scala
public synchronized v@@oid removeAspectHandler( final String name )

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.myrmidon.components.aspect;

import java.util.HashMap;
import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.myrmidon.api.Task;
import org.apache.myrmidon.api.TaskException;
import org.apache.myrmidon.aspects.AspectHandler;
import org.apache.myrmidon.aspects.NoopAspectHandler;
import org.apache.myrmidon.interfaces.aspect.AspectManager;

/**
 * Manage and propogate Aspects.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision$ $Date$
 */
public class DefaultAspectManager
    implements AspectManager, Initializable
{
    private final static Resources REZ =
        ResourceManager.getPackageResources( DefaultAspectManager.class );

    private HashMap m_aspectMap = new HashMap();
    private AspectHandler[] m_aspects = new AspectHandler[ 0 ];
    private String[] m_names = new String[ 0 ];

    public void initialize()
        throws Exception
    {
        ///UGLY HACK!!!!
        addAspectHandler( "ant", new NoopAspectHandler() );
        addAspectHandler( "doc", new NoopAspectHandler() );
    }

    public synchronized void addAspectHandler( final String name, final AspectHandler handler )
        throws TaskException
    {
        m_aspectMap.put( name, handler );
        rebuildArrays();
    }

    public synchronized void removeAspectHandler( final String name, final AspectHandler handler )
        throws TaskException
    {
        final AspectHandler entry = (AspectHandler)m_aspectMap.remove( name );
        if( null == entry )
        {
            final String message = REZ.getString( "no.aspect", name );
            throw new TaskException( message );
        }

        rebuildArrays();
    }

    private void rebuildArrays()
    {
        m_aspects = (AspectHandler[])m_aspectMap.values().toArray( m_aspects );
        m_names = (String[])m_aspectMap.keySet().toArray( m_names );
    }

    public String[] getNames()
    {
        return m_names;
    }

    public void dispatchAspectSettings( final String name,
                                        final Parameters parameters,
                                        final Configuration[] elements )
        throws TaskException
    {
        final AspectHandler handler = (AspectHandler)m_aspectMap.get( name );
        if( null == handler )
        {
            final String message = REZ.getString( "no.aspect", name );
            throw new TaskException( message );
        }

        handler.aspectSettings( parameters, elements );
    }

    public Configuration preCreate( final Configuration configuration )
        throws TaskException
    {
        Configuration model = configuration;

        final AspectHandler[] aspects = m_aspects;
        for( int i = 0; i < aspects.length; i++ )
        {
            model = aspects[ i ].preCreate( model );
        }

        return model;
    }

    public void aspectSettings( final Parameters parameters, final Configuration[] elements )
        throws TaskException
    {
        final String message = REZ.getString( "no.settings" );
        throw new UnsupportedOperationException( message );
    }

    public void postCreate( final Task task )
        throws TaskException
    {
        final AspectHandler[] aspects = m_aspects;
        for( int i = 0; i < aspects.length; i++ )
        {
            aspects[ i ].postCreate( task );
        }
    }

    public void preLogEnabled( final Logger logger )
        throws TaskException
    {
        final AspectHandler[] aspects = m_aspects;
        for( int i = 0; i < aspects.length; i++ )
        {
            aspects[ i ].preLogEnabled( logger );
        }
    }

    public void preConfigure( final Configuration taskModel )
        throws TaskException
    {
        final AspectHandler[] aspects = m_aspects;
        for( int i = 0; i < aspects.length; i++ )
        {
            aspects[ i ].preConfigure( taskModel );
        }
    }

    public void preExecute()
        throws TaskException
    {
        final AspectHandler[] aspects = m_aspects;
        for( int i = 0; i < aspects.length; i++ )
        {
            aspects[ i ].preExecute();
        }
    }

    public void preDestroy()
        throws TaskException
    {
        final AspectHandler[] aspects = m_aspects;
        for( int i = 0; i < aspects.length; i++ )
        {
            aspects[ i ].preDestroy();
        }
    }

    public boolean error( final TaskException te )
        throws TaskException
    {
        final AspectHandler[] aspects = m_aspects;
        for( int i = 0; i < aspects.length; i++ )
        {
            final boolean isError = aspects[ i ].error( te );
            if( isError )
            {
                return true;
            }
        }

        return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6693.java