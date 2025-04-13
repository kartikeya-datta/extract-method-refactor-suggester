error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14965.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14965.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14965.java
text:
```scala
F@@ile dir = fs.getDir();

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.tools.ant.taskdefs.file;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import org.apache.avalon.excalibur.io.FileUtil;
import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.FilterSetCollection;
import org.apache.tools.ant.util.FileUtils;

/**
 * Moves a file or directory to a new file or directory. By default, the
 * destination is overwriten when existing. When overwrite is turned off, then
 * files are only moved if the source file is newer than the destination file,
 * or when the destination file does not exist.</p> <p>
 *
 * Source files and directories are only deleted when the file or directory has
 * been copied to the destination successfully. Filtering also works.</p> <p>
 *
 * This implementation is based on Arnout Kuiper's initial design document, the
 * following mailing list discussions, and the copyfile/copydir tasks.</p>
 *
 * @author Glenn McAllister <a href="mailto:glennm@ca.ibm.com">glennm@ca.ibm.com
 *      </a>
 * @author <a href="mailto:umagesh@rediffmail.com">Magesh Umasankar</a>
 */
public class Move
    extends Copy
{
    public Move()
    {
        setForceOverwrite( true );
    }

    /**
     * Go and delete the directory tree.
     *
     * @param d Description of Parameter
     */
    protected void deleteDir( File d )
        throws TaskException
    {
        String[] list = d.list();
        if( list == null )
            return;// on an io error list() can return null

        for( int i = 0; i < list.length; i++ )
        {
            String s = list[ i ];
            File f = new File( d, s );
            if( f.isDirectory() )
            {
                deleteDir( f );
            }
            else
            {
                throw new TaskException( "UNEXPECTED ERROR - The file " + f.getAbsolutePath() + " should not exist!" );
            }
        }
        getLogger().debug( "Deleting directory " + d.getAbsolutePath() );
        if( !d.delete() )
        {
            throw new TaskException( "Unable to delete directory " + d.getAbsolutePath() );
        }
    }

    //************************************************************************
    //  protected and private methods
    //************************************************************************

    protected void doFileOperations()
        throws TaskException
    {
        //Attempt complete directory renames, if any, first.
        if( getCompleteDirMap().size() > 0 )
        {
            Enumeration e = getCompleteDirMap().keys();
            while( e.hasMoreElements() )
            {
                File fromDir = (File)e.nextElement();
                File toDir = (File)getCompleteDirMap().get( fromDir );
                try
                {
                    final String message = "Attempting to rename dir: " + fromDir +
                        " to " + toDir;
                    getLogger().debug( message );
                    renameFile( fromDir, toDir, isFiltering(), isForceOverwrite() );
                }
                catch( final IOException ioe )
                {
                    final String msg = "Failed to rename dir " + fromDir +
                        " to " + toDir + " due to " + ioe.getMessage();
                    throw new TaskException( msg, ioe );
                }
            }
        }
        if( getFileCopyMap().size() > 0 )
        {// files to move
            getLogger().info( "Moving " + getFileCopyMap().size() + " files to " +
                              getDestDir().getAbsolutePath() );

            Enumeration e = getFileCopyMap().keys();
            while( e.hasMoreElements() )
            {
                String fromFile = (String)e.nextElement();
                String toFile = (String)getFileCopyMap().get( fromFile );

                if( fromFile.equals( toFile ) )
                {
                    getLogger().debug( "Skipping self-move of " + fromFile );
                    continue;
                }

                boolean moved = false;
                File f = new File( fromFile );

                if( f.exists() )
                {//Is this file still available to be moved?
                    File d = new File( toFile );

                    try
                    {
                        final String message =
                            "Attempting to rename: " + fromFile + " to " + toFile;
                        getLogger().debug( message );
                        moved = renameFile( f, d, isFiltering(), isForceOverwrite() );
                    }
                    catch( IOException ioe )
                    {
                        final String msg = "Failed to rename " + fromFile + " to " +
                            toFile + " due to " + ioe.getMessage();
                        throw new TaskException( msg, ioe );
                    }

                    if( !moved )
                    {
                        try
                        {
                            getLogger().debug( "Moving " + fromFile + " to " + toFile );

                            final FilterSetCollection executionFilters = buildFilterSet();
                            if( isForceOverwrite() )
                            {
                                FileUtil.forceDelete( d );
                            }
                            FileUtils.copyFile( f, d, executionFilters );

                            f = new File( fromFile );
                            if( !f.delete() )
                            {
                                throw new TaskException( "Unable to delete file "
                                                         + f.getAbsolutePath() );
                            }
                        }
                        catch( IOException ioe )
                        {
                            String msg = "Failed to copy " + fromFile + " to "
                                + toFile
                                + " due to " + ioe.getMessage();
                            throw new TaskException( msg, ioe );
                        }
                    }
                }
            }
        }

        if( isIncludeEmpty() )
        {
            Enumeration e = getDirCopyMap().elements();
            int count = 0;
            while( e.hasMoreElements() )
            {
                File d = new File( (String)e.nextElement() );
                if( !d.exists() )
                {
                    if( !d.mkdirs() )
                    {
                        getLogger().error( "Unable to create directory " + d.getAbsolutePath() );
                    }
                    else
                    {
                        count++;
                    }
                }
            }

            if( count > 0 )
            {
                getLogger().info( "Moved " + count + " empty directories to " + getDestDir().getAbsolutePath() );
            }
        }

        if( getFilesets().size() > 0 )
        {
            Iterator e = getFilesets().iterator();
            while( e.hasNext() )
            {
                FileSet fs = (FileSet)e.next();
                File dir = fs.getDir( getProject() );

                if( okToDelete( dir ) )
                {
                    deleteDir( dir );
                }
            }
        }
    }

    /**
     * Its only ok to delete a directory tree if there are no files in it.
     *
     * @param d Description of Parameter
     * @return Description of the Returned Value
     */
    protected boolean okToDelete( File d )
    {
        String[] list = d.list();
        if( list == null )
            return false;// maybe io error?

        for( int i = 0; i < list.length; i++ )
        {
            String s = list[ i ];
            File f = new File( d, s );
            if( f.isDirectory() )
            {
                if( !okToDelete( f ) )
                    return false;
            }
            else
            {
                return false;// found a file
            }
        }

        return true;
    }

    /**
     * Attempts to rename a file from a source to a destination. If overwrite is
     * set to true, this method overwrites existing file even if the destination
     * file is newer. Otherwise, the source file is renamed only if the
     * destination file is older than it. Method then checks if token filtering
     * is used. If it is, this method returns false assuming it is the
     * responsibility to the copyFile method.
     *
     * @param sourceFile Description of Parameter
     * @param destFile Description of Parameter
     * @param filtering Description of Parameter
     * @param overwrite Description of Parameter
     * @return Description of the Returned Value
     * @exception TaskException Description of Exception
     * @throws IOException
     */
    protected boolean renameFile( File sourceFile, File destFile,
                                  boolean filtering, boolean overwrite )
        throws IOException, TaskException
    {

        boolean renamed = true;
        if( !filtering )
        {
            // ensure that parent dir of dest file exists!
            // not using getParentFile method to stay 1.1 compat
            String parentPath = destFile.getParent();
            if( parentPath != null )
            {
                File parent = new File( parentPath );
                if( !parent.exists() )
                {
                    parent.mkdirs();
                }
            }

            if( destFile.exists() )
            {
                if( !destFile.delete() )
                {
                    throw new TaskException( "Unable to remove existing file "
                                             + destFile );
                }
            }
            renamed = sourceFile.renameTo( destFile );
        }
        else
        {
            renamed = false;
        }
        return renamed;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14965.java