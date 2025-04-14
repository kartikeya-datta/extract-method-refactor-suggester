error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10643.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10643.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10643.java
text:
```scala
g@@etLogger().debug( cmdl.toString() );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.tools.ant.taskdefs.optional.metamata;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.exec.Execute;
import org.apache.tools.ant.taskdefs.exec.ExecuteStreamHandler;
import org.apache.tools.ant.taskdefs.exec.LogStreamHandler;
import org.apache.tools.ant.types.Argument;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Path;

/**
 * Simple Metamata MParse task based on the original written by <a
 * href="mailto:thomas.haas@softwired-inc.com">Thomas Haas</a> This version was
 * written for Metamata 2.0 available at <a href="http://www.metamata.com">
 * http://www.metamata.com</a>
 *
 * @author <a href="mailto:sbailliez@imediation.com">Stephane Bailliez</a>
 */
public class MParse extends Task
{

    private Path classpath = null;
    private Path sourcepath = null;
    private File metahome = null;
    private File target = null;
    private boolean verbose = false;
    private boolean debugparser = false;
    private boolean debugscanner = false;
    private boolean cleanup = false;
    private CommandlineJava cmdl = new CommandlineJava();
    private File optionsFile = null;

    public MParse()
    {
        cmdl.setVm( "java" );
        cmdl.setClassname( "com.metamata.jj.MParse" );
    }

    /**
     * create a temporary file in the current directory
     *
     * @return Description of the Returned Value
     */
    protected final static File createTmpFile()
    {
        // must be compatible with JDK 1.1 !!!!
        final long rand = ( new Random( System.currentTimeMillis() ) ).nextLong();
        File file = new File( "metamata" + rand + ".tmp" );
        return file;
    }

    /**
     * set the hack to cleanup the temp file
     *
     * @param value The new Cleanup value
     */
    public void setCleanup( boolean value )
    {
        cleanup = value;
    }

    /**
     * set parser debug mode
     *
     * @param flag The new Debugparser value
     */
    public void setDebugparser( boolean flag )
    {
        debugparser = flag;
    }

    /**
     * set scanner debug mode
     *
     * @param flag The new Debugscanner value
     */
    public void setDebugscanner( boolean flag )
    {
        debugscanner = flag;
    }

    /**
     * -mx or -Xmx depending on VM version
     *
     * @param max The new Maxmemory value
     */
    public void setMaxmemory( String max )
    {
        if( Project.getJavaVersion().startsWith( "1.1" ) )
        {
            createJvmarg().setValue( "-mx" + max );
        }
        else
        {
            createJvmarg().setValue( "-Xmx" + max );
        }
    }

    /**
     * location of metamata dev environment
     *
     * @param metamatahome The new Metamatahome value
     */
    public void setMetamatahome( File metamatahome )
    {
        this.metahome = metamatahome;
    }

    /**
     * the .jj file to process
     *
     * @param target The new Target value
     */
    public void setTarget( File target )
    {
        this.target = target;
    }

    /**
     * set verbose mode
     *
     * @param flag The new Verbose value
     */
    public void setVerbose( boolean flag )
    {
        verbose = flag;
    }

    /**
     * create a classpath entry
     *
     * @return Description of the Returned Value
     */
    public Path createClasspath()
    {
        if( classpath == null )
        {
            classpath = new Path( getProject() );
        }
        return classpath;
    }

    /**
     * Creates a nested jvmarg element.
     *
     * @return Description of the Returned Value
     */
    public Argument createJvmarg()
    {
        return cmdl.createVmArgument();
    }

    /**
     * creates a sourcepath entry
     *
     * @return Description of the Returned Value
     */
    public Path createSourcepath()
    {
        if( sourcepath == null )
        {
            sourcepath = new Path( getProject() );
        }
        return sourcepath;
    }

    /**
     * execute the command line
     *
     * @exception TaskException Description of Exception
     */
    public void execute()
        throws TaskException
    {
        try
        {
            setUp();
            ExecuteStreamHandler handler = createStreamHandler();
            _execute( handler );
        }
        finally
        {
            cleanUp();
        }
    }

    /**
     * check the options and build the command line
     *
     * @exception TaskException Description of Exception
     */
    protected void setUp()
        throws TaskException
    {
        checkOptions();

        // set the classpath as the jar files
        File[] jars = getMetamataLibs();
        final Path classPath = cmdl.createClasspath( getProject() );
        for( int i = 0; i < jars.length; i++ )
        {
            classPath.createPathElement().setLocation( jars[ i ] );
        }

        // set the metamata.home property
        final Argument vmArgs = cmdl.createVmArgument();
        vmArgs.setValue( "-Dmetamata.home=" + metahome.getAbsolutePath() );

        // write all the options to a temp file and use it ro run the process
        String[] options = getOptions();
        optionsFile = createTmpFile();
        generateOptionsFile( optionsFile, options );
        Argument args = cmdl.createArgument();
        args.setLine( "-arguments " + optionsFile.getAbsolutePath() );
    }

    /**
     * return an array of files containing the path to the needed libraries to
     * run metamata. The file are not checked for existence. You should do this
     * yourself if needed or simply let the forked process do it for you.
     *
     * @return array of jars/zips needed to run metamata.
     */
    protected File[] getMetamataLibs()
    {
        ArrayList files = new ArrayList();
        files.add( new File( metahome, "lib/metamata.jar" ) );
        files.add( new File( metahome, "bin/lib/JavaCC.zip" ) );

        File[] array = new File[ files.size() ];
        files.copyInto( array );
        return array;
    }

    /**
     * return all options of the command line as string elements
     *
     * @return The Options value
     */
    protected String[] getOptions()
    {
        ArrayList options = new ArrayList();
        if( verbose )
        {
            options.add( "-verbose" );
        }
        if( debugscanner )
        {
            options.add( "-ds" );
        }
        if( debugparser )
        {
            options.add( "-dp" );
        }
        if( classpath != null )
        {
            options.add( "-classpath" );
            options.add( classpath.toString() );
        }
        if( sourcepath != null )
        {
            options.add( "-sourcepath" );
            options.add( sourcepath.toString() );
        }
        options.add( target.getAbsolutePath() );

        String[] array = new String[ options.size() ];
        options.copyInto( array );
        return array;
    }

    /**
     * execute the process with a specific handler
     *
     * @param handler Description of Parameter
     * @exception TaskException Description of Exception
     */
    protected void _execute( ExecuteStreamHandler handler )
        throws TaskException
    {
        // target has been checked as a .jj, see if there is a matching
        // java file and if it is needed to run to process the grammar
        String pathname = target.getAbsolutePath();
        int pos = pathname.length() - ".jj".length();
        pathname = pathname.substring( 0, pos ) + ".java";
        File javaFile = new File( pathname );
        if( javaFile.exists() && target.lastModified() < javaFile.lastModified() )
        {
            getLogger().info( "Target is already build - skipping (" + target + ")" );
            return;
        }

        final Execute process = new Execute( handler );
        log( cmdl.toString(), Project.MSG_VERBOSE );
        process.setCommandline( cmdl.getCommandline() );
        try
        {
            if( process.execute() != 0 )
            {
                throw new TaskException( "Metamata task failed." );
            }
        }
        catch( IOException e )
        {
            throw new TaskException( "Failed to launch Metamata task: " + e );
        }
    }

    /**
     * validate options set and resolve files and paths
     *
     * @throws TaskException thrown if an option has an incorrect state.
     */
    protected void checkOptions()
        throws TaskException
    {
        // check that the home is ok.
        if( metahome == null || !metahome.exists() )
        {
            throw new TaskException( "'metamatahome' must point to Metamata home directory." );
        }
        metahome = resolveFile( metahome.getPath() );

        // check that the needed jar exists.
        File[] jars = getMetamataLibs();
        for( int i = 0; i < jars.length; i++ )
        {
            if( !jars[ i ].exists() )
            {
                throw new TaskException( jars[ i ] + " does not exist. Check your metamata installation." );
            }
        }

        // check that the target is ok and resolve it.
        if( target == null || !target.isFile() || !target.getName().endsWith( ".jj" ) )
        {
            throw new TaskException( "Invalid target: " + target );
        }
        target = resolveFile( target.getPath() );
    }

    /**
     * clean up all the mess that we did with temporary objects
     */
    protected void cleanUp()
    {
        if( optionsFile != null )
        {
            optionsFile.delete();
            optionsFile = null;
        }
        if( cleanup )
        {
            String name = target.getName();
            int pos = name.length() - ".jj".length();
            name = "__jj" + name.substring( 0, pos ) + ".sunjj";
            final File sunjj = new File( target.getParent(), name );
            if( sunjj.exists() )
            {
                getLogger().info( "Removing stale file: " + sunjj.getName() );
                sunjj.delete();
            }
        }
    }

    /**
     * return the default stream handler for this task
     *
     * @return Description of the Returned Value
     */
    protected ExecuteStreamHandler createStreamHandler()
    {
        return new LogStreamHandler( this, Project.MSG_INFO, Project.MSG_INFO );
    }

    /**
     * write all options to a file with one option / line
     *
     * @param tofile the file to write the options to.
     * @param options the array of options element to write to the file.
     * @throws TaskException thrown if there is a problem while writing to the
     *      file.
     */
    protected void generateOptionsFile( File tofile, String[] options )
        throws TaskException
    {
        FileWriter fw = null;
        try
        {
            fw = new FileWriter( tofile );
            PrintWriter pw = new PrintWriter( fw );
            for( int i = 0; i < options.length; i++ )
            {
                pw.println( options[ i ] );
            }
            pw.flush();
        }
        catch( IOException e )
        {
            throw new TaskException( "Error while writing options file " + tofile, e );
        }
        finally
        {
            if( fw != null )
            {
                try
                {
                    fw.close();
                }
                catch( IOException ignored )
                {
                }
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10643.java