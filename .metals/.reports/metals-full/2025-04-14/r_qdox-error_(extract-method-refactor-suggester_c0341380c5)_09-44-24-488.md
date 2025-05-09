error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10628.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10628.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10628.java
text:
```scala
i@@f( Os.isFamily( Os.OS_FAMILY_WINDOWS ) )

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import org.apache.aut.nativelib.Os;
import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.types.DirectoryScanner;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.ScannerUtil;

/**
 * A Task to record explicit dependencies. If any of the target files are out of
 * date with respect to any of the source files, all target files are removed.
 * This is useful where dependencies cannot be computed (for example,
 * dynamically interpreted parameters or files that need to stay in synch but
 * are not directly linked) or where the ant task in question could compute them
 * but does not (for example, the linked DTD for an XML file using the style
 * task). nested arguments:
 * <ul>
 *   <li> srcfileset (fileset describing the source files to examine)
 *   <li> srcfilelist (filelist describing the source files to examine)
 *   <li> targetfileset (fileset describing the target files to examine)
 *   <li> targetfilelist (filelist describing the target files to examine)
 * </ul>
 * At least one instance of either a fileset or filelist for both source and
 * target are required. <p>
 *
 * This task will examine each of the source files against each of the target
 * files. If any target files are out of date with respect to any of the source
 * files, all targets are removed. If any files named in a (src or target)
 * filelist do not exist, all targets are removed. Hint: If missing files should
 * be ignored, specify them as include patterns in filesets, rather than using
 * filelists. </p> <p>
 *
 * This task attempts to optimize speed of dependency checking. It will stop
 * after the first out of date file is found and remove all targets, rather than
 * exhaustively checking every source vs target combination unnecessarily. </p>
 * <p>
 *
 * Example uses:
 * <ul>
 *   <li> Record the fact that an XML file must be up to date with respect to
 *   its XSD (Schema file), even though the XML file itself includes no
 *   reference to its XSD. </li>
 *   <li> Record the fact that an XSL stylesheet includes other sub-stylesheets
 *   </li>
 *   <li> Record the fact that java files must be recompiled if the ant build
 *   file changes </li>
 * </ul>
 *
 *
 * @author <a href="mailto:cstrong@arielpartners.com">Craeg Strong</a>
 * @version $Revision$ $Date$
 */
public class DependSet extends MatchingTask
{

    private ArrayList sourceFileSets = new ArrayList();
    private ArrayList sourceFileLists = new ArrayList();
    private ArrayList targetFileSets = new ArrayList();
    private ArrayList targetFileLists = new ArrayList();

    /**
     * Creates a new DependSet Task.
     */
    public DependSet()
    {
    }

    /**
     * Nested &lt;srcfilelist&gt; element.
     *
     * @param fl The feature to be added to the Srcfilelist attribute
     */
    public void addSrcfilelist( FileList fl )
    {
        sourceFileLists.add( fl );
    }//-- DependSet

    /**
     * Nested &lt;srcfileset&gt; element.
     *
     * @param fs The feature to be added to the Srcfileset attribute
     */
    public void addSrcfileset( FileSet fs )
    {
        sourceFileSets.add( fs );
    }

    /**
     * Nested &lt;targetfilelist&gt; element.
     *
     * @param fl The feature to be added to the Targetfilelist attribute
     */
    public void addTargetfilelist( FileList fl )
    {
        targetFileLists.add( fl );
    }

    /**
     * Nested &lt;targetfileset&gt; element.
     *
     * @param fs The feature to be added to the Targetfileset attribute
     */
    public void addTargetfileset( FileSet fs )
    {
        targetFileSets.add( fs );
    }

    /**
     * Executes the task.
     *
     * @exception TaskException Description of Exception
     */
    public void execute()
        throws TaskException
    {
        if( ( sourceFileSets.size() == 0 ) && ( sourceFileLists.size() == 0 ) )
        {
            throw new TaskException( "At least one <srcfileset> or <srcfilelist> element must be set" );
        }

        if( ( targetFileSets.size() == 0 ) && ( targetFileLists.size() == 0 ) )
        {
            throw new TaskException( "At least one <targetfileset> or <targetfilelist> element must be set" );
        }

        long now = ( new Date() ).getTime();
        /*
         * If we're on Windows, we have to munge the time up to 2 secs to
         * be able to check file modification times.
         * (Windows has a max resolution of two secs for modification times)
         */
        if( Os.isFamily( "windows" ) )
        {
            now += 2000;
        }

        //
        // Grab all the target files specified via filesets
        //
        ArrayList allTargets = new ArrayList();
        Iterator enumTargetSets = targetFileSets.iterator();
        while( enumTargetSets.hasNext() )
        {

            FileSet targetFS = (FileSet)enumTargetSets.next();
            DirectoryScanner targetDS = ScannerUtil.getDirectoryScanner( targetFS );
            String[] targetFiles = targetDS.getIncludedFiles();

            for( int i = 0; i < targetFiles.length; i++ )
            {

                File dest = new File( targetFS.getDir(), targetFiles[ i ] );
                allTargets.add( dest );

                if( dest.lastModified() > now )
                {
                    getLogger().warn( "Warning: " + targetFiles[ i ] + " modified in the future." );
                }
            }
        }

        //
        // Grab all the target files specified via filelists
        //
        boolean upToDate = true;
        Iterator enumTargetLists = targetFileLists.iterator();
        while( enumTargetLists.hasNext() )
        {

            FileList targetFL = (FileList)enumTargetLists.next();
            String[] targetFiles = targetFL.getFiles();

            for( int i = 0; i < targetFiles.length; i++ )
            {

                File dest = new File( targetFL.getDir(), targetFiles[ i ] );
                if( !dest.exists() )
                {
                    getLogger().debug( targetFiles[ i ] + " does not exist." );
                    upToDate = false;
                    continue;
                }
                else
                {
                    allTargets.add( dest );
                }
                if( dest.lastModified() > now )
                {
                    getLogger().warn( "Warning: " + targetFiles[ i ] + " modified in the future." );
                }
            }
        }

        //
        // Check targets vs source files specified via filesets
        //
        if( upToDate )
        {
            Iterator enumSourceSets = sourceFileSets.iterator();
            while( upToDate && enumSourceSets.hasNext() )
            {

                FileSet sourceFS = (FileSet)enumSourceSets.next();
                DirectoryScanner sourceDS = ScannerUtil.getDirectoryScanner( sourceFS );
                String[] sourceFiles = sourceDS.getIncludedFiles();

                for( int i = 0; upToDate && i < sourceFiles.length; i++ )
                {
                    File src = new File( sourceFS.getDir(), sourceFiles[ i ] );

                    if( src.lastModified() > now )
                    {
                        getLogger().warn( "Warning: " + sourceFiles[ i ] + " modified in the future." );
                    }

                    Iterator enumTargets = allTargets.iterator();
                    while( upToDate && enumTargets.hasNext() )
                    {

                        File dest = (File)enumTargets.next();
                        if( src.lastModified() > dest.lastModified() )
                        {
                            getLogger().debug( dest.getPath() + " is out of date with respect to " + sourceFiles[ i ] );
                            upToDate = false;

                        }
                    }
                }
            }
        }

        //
        // Check targets vs source files specified via filelists
        //
        if( upToDate )
        {
            Iterator enumSourceLists = sourceFileLists.iterator();
            while( upToDate && enumSourceLists.hasNext() )
            {

                FileList sourceFL = (FileList)enumSourceLists.next();
                String[] sourceFiles = sourceFL.getFiles();

                int i = 0;
                do
                {
                    File src = new File( sourceFL.getDir(), sourceFiles[ i ] );

                    if( src.lastModified() > now )
                    {
                        getLogger().warn( "Warning: " + sourceFiles[ i ] + " modified in the future." );
                    }

                    if( !src.exists() )
                    {
                        getLogger().debug( sourceFiles[ i ] + " does not exist." );
                        upToDate = false;
                        break;
                    }

                    Iterator enumTargets = allTargets.iterator();
                    while( upToDate && enumTargets.hasNext() )
                    {

                        File dest = (File)enumTargets.next();

                        if( src.lastModified() > dest.lastModified() )
                        {
                            getLogger().debug( dest.getPath() + " is out of date with respect to " + sourceFiles[ i ] );
                            upToDate = false;

                        }
                    }
                } while( upToDate && ( ++i < sourceFiles.length ) );
            }
        }

        if( !upToDate )
        {
            getLogger().debug( "Deleting all target files. " );
            for( Iterator e = allTargets.iterator(); e.hasNext(); )
            {
                File fileToRemove = (File)e.next();
                getLogger().debug( "Deleting file " + fileToRemove.getAbsolutePath() );
                fileToRemove.delete();
            }
        }

    }//-- execute

}//-- DependSet.java
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10628.java