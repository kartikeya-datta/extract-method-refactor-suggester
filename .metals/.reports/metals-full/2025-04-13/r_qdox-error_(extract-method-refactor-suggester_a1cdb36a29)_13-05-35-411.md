error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8156.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8156.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8156.java
text:
```scala
private final static R@@esources REZ =

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.myrmidon.components.type;

import java.util.HashMap;
import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;
import org.apache.myrmidon.interfaces.type.TypeException;
import org.apache.myrmidon.interfaces.type.TypeFactory;
import org.apache.myrmidon.interfaces.type.TypeManager;

/**
 * The interface that is used to manage types.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 */
public class DefaultTypeManager
    implements TypeManager
{
    private static final Resources REZ =
        ResourceManager.getPackageResources( DefaultTypeManager.class );

    ///Parent type manager to inherit values from.
    private final DefaultTypeManager m_parent;

    ///Maps role to MultiSourceTypeFactory.
    private final HashMap m_roleMap = new HashMap();

    public DefaultTypeManager()
    {
        this( null );
    }

    public DefaultTypeManager( final DefaultTypeManager parent )
    {
        m_parent = parent;
    }

    public void registerType( final String role,
                              final String shorthandName,
                              final TypeFactory factory )
        throws TypeException
    {
        final MultiSourceTypeFactory msFactory = createFactory( role );
        msFactory.register( shorthandName, factory );
    }

    public TypeFactory getFactory( final String role )
        throws TypeException
    {
        return createFactory( role );
    }

    public TypeManager createChildTypeManager()
    {
        return new DefaultTypeManager( this );
    }

    protected final MultiSourceTypeFactory lookupFactory( final String role )
    {
        return (MultiSourceTypeFactory)m_roleMap.get( role );
    }

    /**
     * Get a factory of appropriate role.
     * Create a Factory if none exists with same name.
     *
     * @param role the role name(must be name of work interface)
     * @return the Factory for interface
     * @exception TypeException role does not specify accessible work interface
     */
    private MultiSourceTypeFactory createFactory( final String role )
        throws TypeException
    {
        MultiSourceTypeFactory factory = (MultiSourceTypeFactory)m_roleMap.get( role );
        if( null != factory )
        {
            return factory;
        }

        final MultiSourceTypeFactory parentFactory = getParentTypedFactory( role );
        if( null != parentFactory )
        {
            factory = new MultiSourceTypeFactory( parentFactory );
        }

        ///If we haven't got factory try to create a new one
        if( null == factory )
        {
            try
            {
                //TODO: Should we use ContextClassLoader here ??? Or perhaps try that on failure??
                final Class clazz = Class.forName( role );
                factory = new MultiSourceTypeFactory( clazz );
            }
            catch( final Exception e )
            {
                final String message = REZ.getString( "no-work-interface.error", role );
                throw new TypeException( message );
            }
        }

        m_roleMap.put( role, factory );

        return factory;
    }

    private MultiSourceTypeFactory getParentTypedFactory( final String role )
    {
        if( null != m_parent )
        {
            return m_parent.lookupFactory( role );
        }
        else
        {
            return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8156.java