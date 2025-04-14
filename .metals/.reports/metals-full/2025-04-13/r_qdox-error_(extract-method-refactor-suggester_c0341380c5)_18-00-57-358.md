error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7526.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7526.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7526.java
text:
```scala
R@@EZ.getString( "no-converter.error",

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.myrmidon.components.converter;

import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;
import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.Composable;
import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.myrmidon.converter.Converter;
import org.apache.myrmidon.converter.ConverterException;
import org.apache.myrmidon.interfaces.converter.ConverterRegistry;
import org.apache.myrmidon.interfaces.converter.MasterConverter;
import org.apache.myrmidon.interfaces.type.TypeException;
import org.apache.myrmidon.interfaces.type.TypeFactory;
import org.apache.myrmidon.interfaces.type.TypeManager;

/**
 * Converter engine to handle converting between types.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 */
public class DefaultMasterConverter
    extends AbstractLogEnabled
    implements MasterConverter, Composable
{
    private static final Resources REZ =
        ResourceManager.getPackageResources( DefaultMasterConverter.class );

    private final static boolean DEBUG = false;

    private ConverterRegistry m_registry;
    private TypeFactory m_factory;

    /**
     * Retrieve relevent services needed to deploy.
     *
     * @param componentManager the ComponentManager
     * @exception ComponentException if an error occurs
     */
    public void compose( final ComponentManager componentManager )
        throws ComponentException
    {
        m_registry = (ConverterRegistry)componentManager.lookup( ConverterRegistry.ROLE );

        final TypeManager typeManager = (TypeManager)componentManager.lookup( TypeManager.ROLE );
        try
        {
            m_factory = typeManager.getFactory( Converter.ROLE );
        }
        catch( final TypeException te )
        {
            final String message = REZ.getString( "no-converter-factory.error" );
            throw new ComponentException( message, te );
        }
    }

    /**
     * Convert object to destination type.
     *
     * @param destination the destination type
     * @param original the original object
     * @param context the context in which to convert
     * @return the converted object
     * @exception Exception if an error occurs
     */
    public Object convert( final Class destination,
                           final Object original,
                           final Context context )
        throws ConverterException
    {
        final Class originalClass = original.getClass();

        if( destination.isAssignableFrom( originalClass ) )
        {
            return original;
        }

        if( DEBUG )
        {
            final String message =
                REZ.getString( "converter-lookup.notice",
                               originalClass.getName(),
                               destination.getName() );
            getLogger().debug( message );
        }

        //Searching inheritance hierarchy for converter
        final String name = getConverterName( originalClass, destination );

        try
        {
            //TODO: Start caching converters instead of repeatedly instantiating em.
            final Converter converter = (Converter)m_factory.create( name );

            if( DEBUG )
            {
                final String message = REZ.getString( "found-converter.notice", converter );
                getLogger().debug( message );
            }

            final Object object = converter.convert( destination, original, context );
            if( destination.isInstance( object ) )
            {
                return object;
            }
            else
            {
                final String message =
                    REZ.getString( "bad-return-type.error",
                                   name,
                                   object,
                                   destination.getName() );
                throw new ConverterException( message );
            }
        }
        catch( final TypeException te )
        {
            final String message = REZ.getString( "bad-typemanager.error" );
            throw new ConverterException( message, te );
        }
    }

    private String getConverterName( final Class originalClass,
                                     final Class destination )
        throws ConverterException
    {
        Class clazz = destination;

        //TODO: Maybe we should search the source classes hierarchy aswell
        final Class terminator = Object.class;
        while( terminator != clazz )
        {
            final String name =
                m_registry.getConverterName( originalClass.getName(),
                                             clazz.getName() );
            if( name != null )
            {
                return name;
            }

            clazz = clazz.getSuperclass();
        }

        final String message =
            REZ.getString( "no-converter.notice",
                           originalClass.getName(),
                           destination.getName() );
        throw new ConverterException( message );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7526.java