error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17377.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17377.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17377.java
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
package org.apache.antlib.file;

import java.io.File;
import java.util.ArrayList;
import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;
import org.apache.myrmidon.api.AbstractTask;
import org.apache.myrmidon.api.TaskException;
import org.apache.tools.todo.types.DirectoryScanner;
import org.apache.tools.todo.types.FileSet;
import org.apache.tools.todo.types.ScannerUtil;

/**
 * Deletes a file or directory, or set of files defined by a fileset.
 *
 * @ant.task name="delete"
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @author <a href="mailto:stefano@apache.org">Stefano Mazzocchi</a>
 * @author <a href="mailto:tad1@cornell.edu">Tom Dimock</a>
 * @author <a href="mailto:glennm@ca.ibm.com">Glenn McAllister</a>
 * @author <a href="mailto:jon@latchkey.com">Jon S. Stevens</a>
 * @version $Revision$ $Date$
 */
public class Delete
    extends AbstractTask
{
    private final static Resources REZ =
        ResourceManager.getPackageResources( Delete.class );

    private final ArrayList filesets = new ArrayList();
    private File m_dir;
    private File m_file;
    private boolean m_includeEmpty;// by default, remove matching empty dirs

    /**
     * Set the directory from which files are to be deleted
     *
     * @param dir the directory path.
     */
    public void setDir( final File dir )
    {
        m_dir = dir;
    }

    /**
     * Set the name of a single file to be removed.
     *
     * @param file the file to be deleted
     */
    public void setFile( final File file )
    {
        m_file = file;
    }

    /**
     * Adds a set of files (nested fileset attribute).
     */
    public void addFileset( FileSet set )
    {
        filesets.add( set );
    }

    /**
     * Delete the file(s).
     */
    public void execute()
        throws TaskException
    {
        validate();

        // delete the single file
        if( null != m_file && m_file.exists() )
        {
            deleteFile( m_file );
        }

        // delete the directory
        if( m_dir != null && m_dir.exists() && m_dir.isDirectory() )
        {
            final String message =
                REZ.getString( "delete.delete-dir.notice", m_dir.getAbsolutePath() );
            getContext().info( message );
            deleteDir( m_dir );
        }

        // delete the files in the filesets
        final int size = filesets.size();
        for( int i = 0; i < size; i++ )
        {
            final FileSet fileSet = (FileSet)filesets.get( i );
            final DirectoryScanner scanner =
                ScannerUtil.getDirectoryScanner( fileSet );
            final String[] files = scanner.getIncludedFiles();
            final String[] dirs = scanner.getIncludedDirectories();
            removeFiles( fileSet.getDir(), files, dirs );
        }
    }

    private void validate()
        throws TaskException
    {
        if( null == m_file && null == m_dir && 0 == filesets.size() )
        {
            final String message = REZ.getString( "delete.nofiles.error" );
            throw new TaskException( message );
        }

        if( null != m_file && m_file.exists() && m_file.isDirectory() )
        {
            final String message =
                REZ.getString( "delete.bad-file.error", m_file.getAbsolutePath() );
            throw new TaskException( message );
        }

        if( null != m_file && !m_file.exists() )
        {
            final String message =
                REZ.getString( "delete.missing-file.error", m_file.getAbsolutePath() );
            getContext().debug( message );
        }
    }

    private void deleteDir( final File baseDir )
        throws TaskException
    {
        final File[] list = baseDir.listFiles();
        if( list != null )
        {
            deleteFiles( list );
        }

        if( getContext().isDebugEnabled() )
        {
            final String message =
                REZ.getString( "delete.delete-dir.notice", m_dir.getAbsolutePath() );
            getContext().debug( message );
        }

        if( !baseDir.delete() )
        {
            final String message =
                REZ.getString( "delete.delete-dir.error", m_dir.getAbsolutePath() );
            throw new TaskException( message );
        }
    }

    private void deleteFiles( final File[] list )
        throws TaskException
    {
        for( int i = 0; i < list.length; i++ )
        {
            final File file = list[ i ];
            if( file.isDirectory() )
            {
                deleteDir( file );
            }
            else
            {
                deleteFile( file );
            }
        }
    }

    private void deleteFile( final File file )
        throws TaskException
    {
        if( getContext().isDebugEnabled() )
        {
            final String message =
                REZ.getString( "delete.delete-file.notice", file.getAbsolutePath() );
            getContext().debug( message );
        }

        if( !file.delete() )
        {
            final String message =
                REZ.getString( "delete.delete-file.error", file.getAbsolutePath() );
            throw new TaskException( message );
        }
    }

    /**
     * remove an array of files in a directory, and a list of subdirectories
     * which will only be deleted if 'includeEmpty' is true
     *
     * @param d directory to work from
     * @param files array of files to delete; can be of zero length
     * @param dirs array of directories to delete; can of zero length
     */
    protected void removeFiles( final File baseDir,
                                final String[] files,
                                final String[] dirs )
        throws TaskException
    {
        if( files.length > 0 )
        {
            final String message =
                REZ.getString( "delete.delete-file.error",
                               new Integer( files.length ),
                               baseDir.getAbsolutePath() );
            getContext().info( message );
            for( int i = 0; i < files.length; i++ )
            {
                final File file = new File( baseDir, files[ i ] );
                deleteFile( file );
            }
        }

        if( dirs.length > 0 && m_includeEmpty )
        {
            int dirCount = 0;
            for( int j = dirs.length - 1; j >= 0; j-- )
            {
                final File dir = new File( baseDir, dirs[ j ] );
                final String[] dirFiles = dir.list();
                if( null == dirFiles || 0 == dirFiles.length )
                {
                    deleteDir( dir );
                    dirCount++;
                }
            }

            if( dirCount > 0 )
            {
                final String message =
                    REZ.getString( "delete.summary.notice",
                                   new Integer( dirCount ),
                                   baseDir.getAbsolutePath() );
                getContext().info( message );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17377.java