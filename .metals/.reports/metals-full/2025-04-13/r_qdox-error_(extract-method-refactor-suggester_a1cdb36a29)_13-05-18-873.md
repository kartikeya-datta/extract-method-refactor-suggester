error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17385.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17385.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17385.java
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
package org.apache.antlib.vfile;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.aut.vfs.FileObject;
import org.apache.aut.vfs.FileSystemException;
import org.apache.aut.vfs.FileType;
import org.apache.aut.vfs.NameScope;
import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;
import org.apache.myrmidon.api.AbstractTask;
import org.apache.myrmidon.api.TaskException;

/**
 * A task that copies files.
 *
 * @author <a href="mailto:adammurdoch@apache.org">Adam Murdoch</a>
 *
 * @ant.task name="v-copy"
 */
public class CopyFilesTask
    extends AbstractTask
{
    private final static Resources REZ =
        ResourceManager.getPackageResources( CopyFilesTask.class );

    private FileObject m_srcFile;
    private FileObject m_destFile;
    private FileObject m_destDir;
    private ArrayList m_fileSets = new ArrayList();

    /**
     * Sets the source file.
     */
    public void setSrcfile( final FileObject file )
    {
        m_srcFile = file;
    }

    /**
     * Sets the destination file.
     */
    public void setDestfile( final FileObject file )
    {
        m_destFile = file;
    }

    /**
     * Sets the destination directory.
     */
    public void setDestdir( final FileObject file )
    {
        m_destDir = file;
    }

    /**
     * Sets the source directory.
     */
    public void setSrcdir( final FileObject dir )
    {
        add( new DefaultFileSet( dir ) );
    }

    /**
     * Adds a source file set.
     */
    public void add( final FileSet fileset )
    {
        m_fileSets.add( fileset );
    }

    /**
     * Execute task.
     * This method is called to perform actual work associated with task.
     * It is called after Task has been Configured and Initialized and before
     * beig Disposed (If task implements appropriate interfaces).
     *
     * @exception TaskException if an error occurs
     */
    public void execute()
        throws TaskException
    {
        if( m_srcFile == null && m_fileSets.size() == 0 )
        {
            final String message = REZ.getString( "copyfilestask.no-source.error", getContext().getName() );
            throw new TaskException( message );
        }
        if( m_destFile == null && m_destDir == null )
        {
            final String message = REZ.getString( "copyfilestask.no-destination.error", getContext().getName() );
            throw new TaskException( message );
        }
        if( m_fileSets.size() > 0 && m_destDir == null )
        {
            final String message = REZ.getString( "copyfilestask.no-destination-dir.error", getContext().getName() );
            throw new TaskException( message );
        }

        try
        {
            // Copy the source file across
            if( m_srcFile != null )
            {
                if( m_destFile == null )
                {
                    m_destFile = m_destDir.resolveFile( m_srcFile.getName().getBaseName() );
                }

                getContext().verbose( "copy " + m_srcFile + " to " + m_destFile );
                m_destFile.copy( m_srcFile );
            }

            // Copy the contents of the filesets across
            for( Iterator iterator = m_fileSets.iterator(); iterator.hasNext(); )
            {
                FileSet fileset = (FileSet)iterator.next();
                FileSetResult result = fileset.getResult( getContext() );
                final FileObject[] files = result.getFiles();
                final String[] paths = result.getPaths();
                for( int i = 0; i < files.length; i++ )
                {
                    final FileObject srcFile = files[ i ];
                    final String path = paths[ i ];

                    // TODO - map destination name

                    // TODO - maybe include empty dirs
                    if( srcFile.getType() != FileType.FILE )
                    {
                        continue;
                    }

                    // Locate the destination file
                    final FileObject destFile = m_destDir.resolveFile( path, NameScope.DESCENDENT );

                    // Copy the file across
                    getContext().verbose( "copy " + srcFile + " to " + destFile );
                    destFile.copy( srcFile );
                }
            }
        }
        catch( FileSystemException e )
        {
            throw new TaskException( e.getMessage(), e );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17385.java