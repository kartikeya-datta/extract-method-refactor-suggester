error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8160.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8160.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8160.java
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
package org.apache.myrmidon.framework;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;
import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.Composable;
import org.apache.myrmidon.api.AbstractTask;
import org.apache.myrmidon.api.TaskException;
import org.apache.myrmidon.interfaces.role.RoleManager;
import org.apache.myrmidon.interfaces.type.DefaultTypeFactory;
import org.apache.myrmidon.interfaces.type.TypeException;
import org.apache.myrmidon.interfaces.type.TypeManager;

/**
 * Abstract task to extend to define a type.
 *
 * TODO: Make this support classpath sub-element in future
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 */
public abstract class AbstractTypeDef
    extends AbstractTask
    implements Composable
{
    private static final Resources REZ =
        ResourceManager.getPackageResources( AbstractTypeDef.class );

    private File m_lib;
    private String m_name;
    private String m_className;
    private TypeManager m_typeManager;
    private RoleManager m_roleManager;

    public void compose( final ComponentManager componentManager )
        throws ComponentException
    {
        m_typeManager = (TypeManager)componentManager.lookup( TypeManager.ROLE );
        m_roleManager = (RoleManager)componentManager.lookup( RoleManager.ROLE );
    }

    public void setLib( final File lib )
    {
        //In the future this would be replaced by ClassPath sub-element
        m_lib = lib;
    }

    public void setName( final String name )
    {
        m_name = name;
    }

    public void setClassname( final String className )
    {
        m_className = className;
    }

    public void execute()
        throws TaskException
    {
        if( null == m_name )
        {
            final String message = REZ.getString( "typedef.no-name.error" );
            throw new TaskException( message );
        }
        else if( null == m_className )
        {
            final String message = REZ.getString( "typedef.no-classname.error" );
            throw new TaskException( message );
        }

        final String typeName = getTypeName();
        final String role = m_roleManager.getRoleForName( typeName );

        final ClassLoader classLoader = createClassLoader();
        final DefaultTypeFactory factory = new DefaultTypeFactory( classLoader );
        factory.addNameClassMapping( m_name, m_className );

        try
        {
            m_typeManager.registerType( role, m_name, factory );
        }
        catch( final TypeException te )
        {
            final String message = REZ.getString( "typedef.no-register.error" );
            throw new TaskException( message, te );
        }
    }

    protected ClassLoader createClassLoader()
        throws TaskException
    {
        //TODO: Make this support classpath sub-element in future
        try
        {
            final URL url = m_lib.toURL();
            final ClassLoader classLoader =
                Thread.currentThread().getContextClassLoader();

            return new URLClassLoader( new URL[]{url}, classLoader );
        }
        catch( final Exception e )
        {
            final String message = REZ.getString( "typedef.bad-classloader.error", e );
            throw new TaskException( message, e );
        }
    }

    protected final TypeManager getTypeManager()
    {
        return m_typeManager;
    }

    protected abstract String getTypeName();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8160.java