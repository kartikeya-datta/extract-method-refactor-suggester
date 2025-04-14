error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14248.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14248.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14248.java
text:
```scala
e@@xe.setCommandline( cmdl );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.tools.ant.taskdefs.optional.sitraka;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.types.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.exec.Execute2;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.ScannerUtil;

/**
 * Convenient task to run the snapshot merge utility for JProbe Coverage.
 *
 * @author <a href="sbailliez@imediation.com">Stephane Bailliez</a>
 */
public class CovMerge extends Task
{

    /**
     * coverage home, it is mandatory
     */
    private File home = null;

    /**
     * the name of the output snapshot
     */
    private File tofile = null;

    /**
     * the filesets that will get all snapshots to merge
     */
    private ArrayList filesets = new ArrayList();

    private boolean verbose;

    //---------------- the tedious job begins here

    public CovMerge()
    {
    }

    /**
     * set the coverage home. it must point to JProbe coverage directories where
     * are stored native librairies and jars
     *
     * @param value The new Home value
     */
    public void setHome( File value )
    {
        this.home = value;
    }

    /**
     * Set the output snapshot file
     *
     * @param value The new Tofile value
     */
    public void setTofile( File value )
    {
        this.tofile = value;
    }

    /**
     * run the merging in verbose mode
     *
     * @param flag The new Verbose value
     */
    public void setVerbose( boolean flag )
    {
        this.verbose = flag;
    }

    /**
     * add a fileset containing the snapshots to include/exclude
     *
     * @param fs The feature to be added to the Fileset attribute
     */
    public void addFileset( FileSet fs )
    {
        filesets.add( fs );
    }

    /**
     * execute the jpcovmerge by providing a parameter file
     *
     * @exception TaskException Description of Exception
     */
    public void execute()
        throws TaskException
    {
        checkOptions();

        File paramfile = createParamFile();
        try
        {
            Commandline cmdl = new Commandline();
            cmdl.setExecutable( new File( home, "jpcovmerge" ).getAbsolutePath() );
            if( verbose )
            {
                cmdl.addArgument( "-v" );
            }
            cmdl.addArgument( "-jp_paramfile=" + paramfile.getAbsolutePath() );

            final Execute2 exe = new Execute2();
            setupLogger( exe );
            getLogger().debug( cmdl.toString() );
            exe.setCommandline( cmdl.getCommandline() );

            // JProbe process always return 0 so  we will not be
            // able to check for failure ! :-(
            int exitValue = exe.execute();
            if( exitValue != 0 )
            {
                throw new TaskException( "JProbe Coverage Merging failed (" + exitValue + ")" );
            }
        }
        catch( IOException e )
        {
            throw new TaskException( "Failed to run JProbe Coverage Merge: " + e );
        }
        finally
        {
            //@todo should be removed once switched to JDK1.2
            paramfile.delete();
        }
    }

    /**
     * get the snapshots from the filesets
     *
     * @return The Snapshots value
     */
    protected File[] getSnapshots()
        throws TaskException
    {
        ArrayList v = new ArrayList();
        final int size = filesets.size();
        for( int i = 0; i < size; i++ )
        {
            FileSet fs = (FileSet)filesets.get( i );
            DirectoryScanner ds = ScannerUtil.getDirectoryScanner( fs );
            ds.scan();
            String[] f = ds.getIncludedFiles();
            for( int j = 0; j < f.length; j++ )
            {
                String pathname = f[ j ];
                File file = new File( ds.getBasedir(), pathname );
                file = resolveFile( file.getPath() );
                v.add( file );
            }
        }

        return (File[])v.toArray( new File[ v.size() ] );
    }

    /**
     * check for mandatory options
     *
     * @exception TaskException Description of Exception
     */
    protected void checkOptions()
        throws TaskException
    {
        if( tofile == null )
        {
            throw new TaskException( "'tofile' attribute must be set." );
        }

        // check coverage home
        if( home == null || !home.isDirectory() )
        {
            throw new TaskException( "Invalid home directory. Must point to JProbe home directory" );
        }
        home = new File( home, "coverage" );
        File jar = new File( home, "coverage.jar" );
        if( !jar.exists() )
        {
            throw new TaskException( "Cannot find Coverage directory: " + home );
        }
    }

    /**
     * create the parameters file that contains all file to merge and the output
     * filename.
     *
     * @return Description of the Returned Value
     * @exception TaskException Description of Exception
     */
    protected File createParamFile()
        throws TaskException
    {
        File[] snapshots = getSnapshots();
        File file = createTmpFile();
        FileWriter fw = null;
        try
        {
            fw = new FileWriter( file );
            PrintWriter pw = new PrintWriter( fw );
            for( int i = 0; i < snapshots.length; i++ )
            {
                pw.println( snapshots[ i ].getAbsolutePath() );
            }
            // last file is the output snapshot
            pw.println( resolveFile( tofile.getPath() ) );
            pw.flush();
        }
        catch( IOException e )
        {
            throw new TaskException( "I/O error while writing to " + file, e );
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
        return file;
    }

    /**
     * create a temporary file in the current dir (For JDK1.1 support)
     *
     * @return Description of the Returned Value
     */
    protected File createTmpFile()
    {
        final long rand = ( new Random( System.currentTimeMillis() ) ).nextLong();
        File file = new File( "jpcovmerge" + rand + ".tmp" );
        return file;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14248.java