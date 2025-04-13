error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17417.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17417.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17417.java
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
package org.apache.myrmidon.components.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;
import org.apache.myrmidon.interfaces.model.Project;
import org.apache.myrmidon.interfaces.model.Target;
import org.apache.myrmidon.interfaces.model.TypeLib;

/**
 * Default project implementation.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision$ $Date$
 */
public class DefaultProject
    implements Project
{
    private final static Resources REZ =
        ResourceManager.getPackageResources( DefaultProject.class );

    ///The imports
    private final ArrayList m_imports = new ArrayList();

    ///The projects refferred to by this project
    private final HashMap m_projects = new HashMap();

    ///The targets contained by this project
    private final HashMap m_targets = new HashMap();

    ///The implicit target (not present in m_targets)
    private Target m_implicitTarget;

    ///The name of the default target
    private String m_defaultTarget;

    ///The base directory of project
    private File m_baseDirectory;

    ///The project name
    private String m_name;

    /**
     * Returns the project name.
     */
    public String getProjectName()
    {
        return m_name;
    }

    /**
     * Sets the project name.
     */
    public void setProjectName( String name )
    {
        m_name = name;
    }

    /**
     * Get the imports for project.
     *
     * @return the imports
     */
    public TypeLib[] getTypeLibs()
    {
        return (TypeLib[])m_imports.toArray( new TypeLib[ 0 ] );
    }

    /**
     * Get names of projects referred to by this project.
     *
     * @return the names
     */
    public String[] getProjectNames()
    {
        return (String[])m_projects.keySet().toArray( new String[ 0 ] );
    }

    /**
     * Retrieve project reffered to by this project.
     *
     * @param name the project name
     * @return the Project or null if none by that name
     */
    public Project getProject( final String name )
    {
        return (Project)m_projects.get( name );
    }

    /**
     * Retrieve base directory of project.
     *
     * @return the projects base directory
     */
    public final File getBaseDirectory()
    {
        return m_baseDirectory;
    }

    /**
     * Retrieve implicit target.
     * The implicit target contains all the top level tasks.
     *
     * @return the Target
     */
    public final Target getImplicitTarget()
    {
        return m_implicitTarget;
    }

    /**
     * Set ImplicitTarget.
     *
     * @param target the implicit target
     */
    public final void setImplicitTarget( final Target target )
    {
        m_implicitTarget = target;
    }

    /**
     * Retrieve a target by name.
     *
     * @param targetName the name of target
     * @return the Target or null if no target exists with name
     */
    public final Target getTarget( final String targetName )
    {
        return (Target)m_targets.get( targetName );
    }

    /**
     * Get name of default target.
     *
     * @return the default target name
     */
    public final String getDefaultTargetName()
    {
        return m_defaultTarget;
    }

    /**
     * Retrieve names of all targets in project.
     *
     * @return an array target names
     */
    public final String[] getTargetNames()
    {
        return (String[])m_targets.keySet().toArray( new String[ 0 ] );
    }

    /**
     * Set DefaultTargetName.
     *
     * @param defaultTarget the default target name
     */
    public final void setDefaultTargetName( final String defaultTarget )
    {
        m_defaultTarget = defaultTarget;
    }

    /**
     * Retrieve base directory of project.
     */
    public final void setBaseDirectory( final File baseDirectory )
    {
        m_baseDirectory = baseDirectory;
    }

    /**
     * Adds a type library import to the project.
     */
    public final void addTypeLib( final TypeLib typeLib )
    {
        m_imports.add( typeLib );
    }

    /**
     * Add a target.
     *
     * @param name the name of target
     * @param target the Target
     * @exception IllegalArgumentException if target already exists with same name
     */
    public final void addTarget( final String name, final Target target )
    {
        if( null != m_targets.get( name ) )
        {
            final String message = REZ.getString( "duplicate-target.error", name );
            throw new IllegalArgumentException( message );
        }
        else
        {
            m_targets.put( name, target );
        }
    }

    /**
     * Add a project reference.
     *
     * @param name the name of target
     * @param project the Project
     * @exception IllegalArgumentException if project already exists with same name
     */
    public final void addProject( final String name, final Project project )
    {
        if( null != m_projects.get( name ) )
        {
            final String message = REZ.getString( "duplicate-project.error", name );
            throw new IllegalArgumentException( message );
        }
        else
        {
            m_projects.put( name, project );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17417.java