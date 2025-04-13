error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1234.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1234.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1234.java
text:
```scala
p@@ublic class Target

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.tools.ant;

import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.myrmidon.api.TaskException;

/**
 * This class implements a target object with required parameters.
 *
 * @author James Davidson <a href="mailto:duncan@x180.com">duncan@x180.com</a>
 */

public class Target implements TaskContainer
{
    private String ifCondition = "";
    private String unlessCondition = "";
    private Vector dependencies = new Vector( 2 );
    private Vector children = new Vector( 5 );
    private String description = null;

    private String name;
    private Project project;

    public void setDepends( String depS )
        throws TaskException
    {
        if( depS.length() > 0 )
        {
            StringTokenizer tok =
                new StringTokenizer( depS, ",", true );
            while( tok.hasMoreTokens() )
            {
                String token = tok.nextToken().trim();

                //Make sure the dependency is not empty string
                if( token.equals( "" ) || token.equals( "," ) )
                {
                    throw new TaskException( "Syntax Error: Depend attribute " +
                                             "for target \"" + getName() +
                                             "\" has an empty string for dependency." );
                }

                addDependency( token );

                //Make sure that depends attribute does not
                //end in a ,
                if( tok.hasMoreTokens() )
                {
                    token = tok.nextToken();
                    if( !tok.hasMoreTokens() || !token.equals( "," ) )
                    {
                        throw new TaskException( "Syntax Error: Depend attribute " +
                                                 "for target \"" + getName() +
                                                 "\" ends with a , character" );
                    }
                }
            }
        }
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public void setIf( String property )
    {
        this.ifCondition = ( property == null ) ? "" : property;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public void setProject( Project project )
    {
        this.project = project;
    }

    public void setUnless( String property )
    {
        this.unlessCondition = ( property == null ) ? "" : property;
    }

    public Enumeration getDependencies()
    {
        return dependencies.elements();
    }

    public String getDescription()
    {
        return description;
    }

    public String getName()
    {
        return name;
    }

    public Project getProject()
    {
        return project;
    }

    /**
     * Get the current set of tasks to be executed by this target.
     *
     * @return The current set of tasks.
     */
    public Task[] getTasks()
    {
        Vector tasks = new Vector( children.size() );
        Enumeration enum = children.elements();
        while( enum.hasMoreElements() )
        {
            Object o = enum.nextElement();
            if( o instanceof Task )
            {
                tasks.addElement( o );
            }
        }

        Task[] retval = new Task[ tasks.size() ];
        tasks.copyInto( retval );
        return retval;
    }

    public final void performTasks()
    {
        try
        {
            project.fireTargetStarted( this );
            execute();
            project.fireTargetFinished( this, null );
        }
        catch( final TaskException te )
        {
            project.fireTargetFinished( this, te );
        }
        catch( RuntimeException exc )
        {
            project.fireTargetFinished( this, exc );
            throw exc;
        }
    }

    public void addDependency( String dependency )
    {
        dependencies.addElement( dependency );
    }

    public void addTask( Task task )
    {
        children.addElement( task );
    }

    public void execute()
        throws TaskException
    {
        if( testIfCondition() && testUnlessCondition() )
        {
            Enumeration enum = children.elements();
            while( enum.hasMoreElements() )
            {
                Object o = enum.nextElement();
                if( o instanceof Task )
                {
                    Task task = (Task)o;
                    task.perform();
                }
                else
                {
                }
            }
        }
        else if( !testIfCondition() )
        {
            project.log( this, "Skipped because property '" + this.ifCondition + "' not set.",
                         Project.MSG_VERBOSE );
        }
        else
        {
            project.log( this, "Skipped because property '" + this.unlessCondition + "' set.",
                         Project.MSG_VERBOSE );
        }
    }

    public String toString()
    {
        return name;
    }

    void replaceChild( Task el, Object o )
    {
        int index = -1;
        while( ( index = children.indexOf( el ) ) >= 0 )
        {
            children.setElementAt( o, index );
        }
    }

    private boolean testIfCondition()
        throws TaskException
    {
        if( "".equals( ifCondition ) )
        {
            return true;
        }

        String test = project.replaceProperties( ifCondition );
        return project.getProperty( test ) != null;
    }

    private boolean testUnlessCondition()
        throws TaskException
    {
        if( "".equals( unlessCondition ) )
        {
            return true;
        }
        String test = project.replaceProperties( unlessCondition );
        return project.getProperty( test ) == null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1234.java