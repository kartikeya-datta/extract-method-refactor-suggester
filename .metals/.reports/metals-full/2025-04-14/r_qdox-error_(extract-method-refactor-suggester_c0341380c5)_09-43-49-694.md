error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3149.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3149.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3149.java
text:
```scala
m@@_exe.getClassPath().add( path );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.tools.todo.taskdefs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import org.apache.myrmidon.api.AbstractTask;
import org.apache.myrmidon.api.TaskException;
import org.apache.myrmidon.framework.java.ExecuteJava;
import org.apache.tools.todo.types.Argument;
import org.apache.myrmidon.framework.file.Path;

/**
 * ANTLR task.
 *
 * @author <a href="mailto:emeade@geekfarm.org">Erik Meade</a>
 * @author <a href="mailto:sbailliez@apache.org>Stephane Bailliez</a>
 */
public class ANTLR
    extends AbstractTask
{
    private final ExecuteJava m_exe = new ExecuteJava();

    /**
     * where to output the result
     */
    private File m_outputDirectory;

    /**
     * the file to process
     */
    private File m_target;

    /**
     * The working directory of the process
     *
     * @param dir The new Dir value
     */
    public void setDir( final File dir )
    {
        m_exe.setWorkingDirectory( dir );
    }

    public void setFork( final boolean fork )
    {
        m_exe.setFork( fork );
    }

    public void setOutputdirectory( final File outputDirectory )
    {
        m_outputDirectory = outputDirectory;
    }

    public void setTarget( final File target )
    {
        m_target = target;
    }

    /**
     * <code>&lt;classpath&gt;</code> allows classpath to be set because a
     * directory might be given for Antlr debug...
     */
    public void addClasspath( final Path path )
    {
        m_exe.getClassPath().addPath( path );
    }

    /**
     * Create a new JVM argument. Ignored if no JVM is forked.
     *
     * @see #setFork(boolean)
     */
    public void addJvmarg( final Argument argument )
    {
        m_exe.getVmArguments().addArgument( argument );
    }

    public void execute()
        throws TaskException
    {
        //Adds the jars or directories containing Antlr this should make the forked
        //JVM work without having to specify it directly.
        addClasspathEntry( "/antlr/Tool.class" );

        validateAttributes();

        //TODO: use ANTLR to parse the grammer file to do this.
        if( m_target.lastModified() <= getGeneratedFile().lastModified() )
        {
            return;
        }

        m_exe.setClassName( "antlr.Tool" );

        m_exe.getArguments().addArgument( "-o" );
        m_exe.getArguments().addArgument( m_outputDirectory );
        m_exe.getArguments().addArgument( m_target );

        m_exe.execute( getContext() );
    }

    /**
     * Search for the given resource and add the directory or archive that
     * contains it to the classpath. <p>
     *
     * Doesn't work for archives in JDK 1.1 as the URL returned by getResource
     * doesn't contain the name of the archive.</p>
     *
     * @param resource The feature to be added to the ClasspathEntry attribute
     */
    protected void addClasspathEntry( final String resource )
    {
        URL url = getClass().getResource( resource );
        if( url != null )
        {
            String u = url.toString();
            if( u.startsWith( "jar:file:" ) )
            {
                int pling = u.indexOf( "!" );
                String jarName = u.substring( 9, pling );
                getContext().debug( "Implicitly adding " + jarName + " to classpath" );
                m_exe.getClassPath().addLocation( new File( jarName ) );
            }
            else if( u.startsWith( "file:" ) )
            {
                int tail = u.indexOf( resource );
                String dirName = u.substring( 5, tail );
                getContext().debug( "Implicitly adding " + dirName + " to classpath" );
                m_exe.getClassPath().addLocation( new File( dirName ) );
            }
            else
            {
                getContext().debug( "Don\'t know how to handle resource URL " + u );
            }
        }
        else
        {
            getContext().debug( "Couldn\'t find " + resource );
        }
    }

    private File getGeneratedFile()
        throws TaskException
    {
        String generatedFileName = null;
        try
        {
            BufferedReader in = new BufferedReader( new FileReader( m_target ) );
            String line;
            while( ( line = in.readLine() ) != null )
            {
                int extendsIndex = line.indexOf( " extends " );
                if( line.startsWith( "class " ) && extendsIndex > -1 )
                {
                    generatedFileName = line.substring( 6, extendsIndex ).trim();
                    break;
                }
            }
            in.close();
        }
        catch( Exception e )
        {
            throw new TaskException( "Unable to determine generated class", e );
        }
        if( generatedFileName == null )
        {
            throw new TaskException( "Unable to determine generated class" );
        }
        return new File( m_outputDirectory, generatedFileName + ".java" );
    }

    private void validateAttributes()
        throws TaskException
    {
        if( m_target == null || !m_target.isFile() )
        {
            throw new TaskException( "Invalid target: " + m_target );
        }

        // if no output directory is specified, used the target's directory
        if( m_outputDirectory == null )
        {
            m_outputDirectory = m_target.getParentFile();
        }
        if( !m_outputDirectory.isDirectory() )
        {
            throw new TaskException( "Invalid output directory: " + m_outputDirectory );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3149.java