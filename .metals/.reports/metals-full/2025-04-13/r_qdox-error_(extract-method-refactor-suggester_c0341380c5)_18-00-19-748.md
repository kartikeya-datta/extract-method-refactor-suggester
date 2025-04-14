error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10911.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10911.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10911.java
text:
```scala
final S@@tring message = REZ.getString( "mismatched-type.error", name, object.getClass().getName() );

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

/**
 * This factory acts as a proxy to set of object factorys.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 */
public class MultiSourceTypeFactory
    implements TypeFactory
{
    private static final Resources REZ =
        ResourceManager.getPackageResources( MultiSourceTypeFactory.class );

    ///Parent Selector
    private final MultiSourceTypeFactory m_parent;

    ///Map of name->factory list
    private final HashMap m_factorys = new HashMap();

    ///Type expected to be created from factorys
    private final Class m_type;

    public MultiSourceTypeFactory( final Class type )
    {
        m_type = type;
        m_parent = null;
    }

    public MultiSourceTypeFactory( final MultiSourceTypeFactory parent )
    {
        m_type = parent.getType();
        m_parent = parent;
    }

    /**
     * Populate the ComponentSelector.
     */
    public void register( final String name, final TypeFactory factory )
    {
        m_factorys.put( name, factory );
    }

    /**
     * Create a type instance based on name.
     *
     * @param name the name
     * @return the type instance
     * @exception TypeException if an error occurs
     */
    public Object create( final String name )
        throws TypeException
    {
        TypeFactory factory = getTypeFactory( name );

        if( null == factory && null != m_parent )
        {
            factory = m_parent.getTypeFactory( name );
        }

        if( null == factory )
        {
            final String message = REZ.getString( "no-factory.error", name );
            throw new TypeException( message );
        }
        else
        {
            final Object object = factory.create( name );

            if( !m_type.isInstance( object ) )
            {
                final String message = REZ.getString( "no-factory.error", name, m_type.getName() );
                throw new TypeException( message );
            }

            return object;
        }
    }

    /**
     * Retrieve type managed by selector.
     * Used by other instances of TypedComponentSelector.
     *
     * @return the type class
     */
    protected final Class getType()
    {
        return m_type;
    }

    protected final TypeFactory getTypeFactory( final String name )
    {
        return (TypeFactory)m_factorys.get( name );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10911.java