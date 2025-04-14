error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8149.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8149.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8149.java
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
package org.apache.myrmidon.components.configurer;

import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;
import org.apache.avalon.framework.configuration.ConfigurationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The default property configurer implementation, which uses reflection to
 * create and set property values.
 *
 * @author <a href="mailto:adammurdoch_ml@yahoo.com">Adam Murdoch</a>
 * @version $Revision$ $Date$
 */
class DefaultPropertyConfigurer
    implements PropertyConfigurer
{
    private static final Resources REZ =
        ResourceManager.getPackageResources( DefaultPropertyConfigurer.class );

    private final Class m_type;
    private final Method m_createMethod;
    private final Method m_addMethod;

    public DefaultPropertyConfigurer( Class type,
                                      Method createMethod,
                                      Method addMethod )
    {
        if ( type.isPrimitive() )
        {
            type = getComplexTypeFor(type);
        }
        m_type = type;
        m_createMethod = createMethod;
        m_addMethod = addMethod;
    }

    /**
     * Returns the type of the element.
     */
    public Class getType()
    {
        return m_type;
    }

    /**
     * Determines if the property value must be created via {@link #createValue}.
     */
    public boolean useCreator()
    {
        return (m_createMethod != null);
    }

    /**
     * Creates a nested element.
     */
    public Object createValue( final Object parent )
        throws ConfigurationException
    {
        try
        {
            if( null != m_createMethod )
            {
                return m_createMethod.invoke( parent, null );
            }
            else
            {
                return m_type.newInstance();
            }
        }
        catch( final InvocationTargetException ite )
        {
            final Throwable cause = ite.getTargetException();
            throw new ConfigurationException( cause.getMessage(), cause );
        }
        catch( final Exception e )
        {
            throw new ConfigurationException( e.getMessage(), e );
        }
    }

    /**
     * Sets the nested element, after it has been configured.
     */
    public void setValue( final Object parent, final Object child )
        throws ConfigurationException
    {
        try
        {
            if( null != m_addMethod )
            {
                m_addMethod.invoke( parent, new Object[]{child} );
            }
        }
        catch( final InvocationTargetException ite )
        {
            final Throwable cause = ite.getTargetException();
            throw new ConfigurationException( cause.getMessage(), cause );
        }
        catch( final Exception e )
        {
            throw new ConfigurationException( e.getMessage(), e );
        }
    }

    /**
     * Determines the complex type for a prmitive type.
     */
    private Class getComplexTypeFor( final Class clazz )
    {
        if( String.class == clazz )
        {
            return String.class;
        }
        else if( Integer.TYPE.equals( clazz ) )
        {
            return Integer.class;
        }
        else if( Long.TYPE.equals( clazz ) )
        {
            return Long.class;
        }
        else if( Short.TYPE.equals( clazz ) )
        {
            return Short.class;
        }
        else if( Byte.TYPE.equals( clazz ) )
        {
            return Byte.class;
        }
        else if( Boolean.TYPE.equals( clazz ) )
        {
            return Boolean.class;
        }
        else if( Float.TYPE.equals( clazz ) )
        {
            return Float.class;
        }
        else if( Double.TYPE.equals( clazz ) )
        {
            return Double.class;
        }
        else
        {
            final String message = REZ.getString( "no-complex-type.error", clazz.getName() );
            throw new IllegalArgumentException( message );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8149.java