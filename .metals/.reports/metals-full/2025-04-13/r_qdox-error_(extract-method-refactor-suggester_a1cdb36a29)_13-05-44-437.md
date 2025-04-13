error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17426.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17426.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17426.java
text:
```scala
private static final R@@esources REZ =

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included  with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.myrmidon.components.role;

import java.util.HashMap;
import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;
import org.apache.myrmidon.interfaces.role.RoleException;
import org.apache.myrmidon.interfaces.role.RoleInfo;
import org.apache.myrmidon.interfaces.role.RoleManager;

/**
 * Interface to manage roles and mapping to names.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version CVS $Revision$ $Date$
 */
public class DefaultRoleManager
    implements RoleManager
{
    private final static Resources REZ =
        ResourceManager.getPackageResources( DefaultRoleManager.class );

    /** Parent <code>RoleManager</code> for nested resolution */
    private final RoleManager m_parent;

    /** Map from shorthand name -> RoleInfo. */
    private final HashMap m_shorthandMap = new HashMap();

    /** Map from role name -> RoleInfo. */
    private final HashMap m_nameMap = new HashMap();

    /** Map from role type -> RoleInfo. */
    private final HashMap m_typeMap = new HashMap();

    /**
     *  constructor--this RoleManager has no parent.
     */
    public DefaultRoleManager()
    {
        this( null );
    }

    /**
     * Alternate constructor--this RoleManager has the specified
     * parent.
     *
     * @param parent The parent <code>RoleManager</code>.
     */
    public DefaultRoleManager( final RoleManager parent )
    {
        m_parent = parent;
    }

    /**
     * Find role based on shorthand name.
     *
     * @param name the shorthand name
     * @return the role, or null if the role cannot be found.
     */
    public RoleInfo getRoleByShorthandName( final String name )
    {
        final RoleInfo role = (RoleInfo)m_shorthandMap.get( name );

        if( null == role && null != m_parent )
        {
            return m_parent.getRoleByShorthandName( name );
        }

        return role;
    }

    /**
     * Find role based on role type.
     *
     * @param type the role type.
     * @return the role, or null if the role cannot be found.
     */
    public RoleInfo getRoleByType( final Class type )
    {
        final RoleInfo role = (RoleInfo)m_typeMap.get( type );

        if( null == role && null != m_parent )
        {
            return m_parent.getRoleByType( type );
        }

        return role;
    }

    /**
     * Find role based on name.
     *
     * @param name the role name
     * @return the role, or null if the role cannot be found.
     */
    public RoleInfo getRole( final String name )
    {
        final RoleInfo role = (RoleInfo)m_nameMap.get( name );

        if( null == role && null != m_parent )
        {
            return m_parent.getRole( name );
        }

        return role;
    }

    /**
     * Adds a role definition.
     */
    public void addRole( final RoleInfo role ) throws RoleException
    {
        // Check for duplicate role names
        final String roleName = role.getName();
        RoleInfo oldRole = (RoleInfo)m_nameMap.get( roleName );
        if( null != oldRole && !oldRole.equals( role ) )
        {
            final String message = REZ.getString( "duplicate-role.error", roleName );
            throw new RoleException( message );
        }

        // Check for duplicate shorthand names
        final String shorthand = role.getShorthand();
        if( shorthand != null )
        {
            oldRole = (RoleInfo)m_shorthandMap.get( shorthand );
            if( null != oldRole && !oldRole.equals( role ) )
            {
                final String message = REZ.getString( "duplicate-shorthand.error", shorthand );
                throw new RoleException( message );
            }
        }

        // Check for duplicate types
        final Class roleType = role.getType();
        if( roleType != null )
        {
            oldRole = (RoleInfo)m_typeMap.get( roleType );
            if( null != oldRole && !oldRole.equals( role ) )
            {
                final String message = REZ.getString( "duplicate-type.error", roleType.getName() );
                throw new RoleException( message );
            }
        }

        // Add the role to the maps
        m_nameMap.put( roleName, role );
        if( shorthand != null )
        {
            m_shorthandMap.put( shorthand, role );
        }
        if( roleType != null )
        {
            m_typeMap.put( roleType, role );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17426.java