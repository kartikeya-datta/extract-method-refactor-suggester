error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10043.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10043.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10043.java
text:
```scala
c@@md.addLine( m_command );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.tools.ant.taskdefs.unix;

import java.io.File;
import java.io.IOException;
import org.apache.myrmidon.api.AbstractTask;
import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.taskdefs.exec.Execute2;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Argument;

/**
 * @author lucas@collab.net
 */
public class Rpm
    extends AbstractTask
{
    /**
     * the rpm command to use
     */
    private String m_command = "-bb";

    /**
     * clean BUILD directory
     */
    private boolean m_cleanBuildDir;

    /**
     * remove spec file
     */
    private boolean m_removeSpec;

    /**
     * remove sources
     */
    private boolean m_removeSource;

    /**
     * the spec file
     */
    private String m_specFile;

    /**
     * the rpm top dir
     */
    private File m_topDir;

    public void setCleanBuildDir( boolean cleanBuildDir )
    {
        m_cleanBuildDir = cleanBuildDir;
    }

    public void setCommand( final String command )
    {
        m_command = command;
    }

    public void setRemoveSource( final boolean removeSource )
    {
        m_removeSource = removeSource;
    }

    public void setRemoveSpec( final boolean removeSpec )
    {
        m_removeSpec = removeSpec;
    }

    public void setSpecFile( final String specFile )
        throws TaskException
    {
        if( ( specFile == null ) || ( specFile.trim().equals( "" ) ) )
        {
            throw new TaskException( "You must specify a spec file" );
        }
        m_specFile = specFile;
    }

    public void setTopDir( final File topDir )
    {
        m_topDir = topDir;
    }

    public void execute()
        throws TaskException
    {
        final Commandline cmd = createCommand();
        final Execute2 exe = new Execute2();
        setupLogger( exe );

        if( m_topDir == null ) m_topDir = getBaseDirectory();
        exe.setWorkingDirectory( m_topDir );

        exe.setCommandline( cmd.getCommandline() );
        try
        {
            final String message = "Building the RPM based on the " + m_specFile + " file";
            getLogger().info( message );

            if( 0 != exe.execute() )
            {
                throw new TaskException( "Failed to execute rpm" );
            }
        }
        catch( IOException e )
        {
            throw new TaskException( "Error", e );
        }
    }

    private Commandline createCommand()
        throws TaskException
    {
        final Commandline cmd = new Commandline();
        cmd.setExecutable( "rpm" );
        if( m_topDir != null )
        {
            cmd.addArgument( "--define" );
            cmd.addArgument( "_topdir" + m_topDir );
        }

        cmd.createArgument().setLine( m_command );

        if( m_cleanBuildDir )
        {
            cmd.addArgument( "--clean" );
        }
        if( m_removeSpec )
        {
            cmd.addArgument( "--rmspec" );
        }
        if( m_removeSource )
        {
            cmd.addArgument( "--rmsource" );
        }

        cmd.addArgument( "SPECS/" + m_specFile );
        return cmd;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10043.java