error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17379.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17379.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17379.java
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;
import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;
import org.apache.myrmidon.api.AbstractTask;
import org.apache.myrmidon.api.TaskException;
import org.apache.tools.todo.types.DirectoryScanner;
import org.apache.tools.todo.types.FileSet;
import org.apache.tools.todo.types.ScannerUtil;

/**
 * Touch a file and/or fileset(s) -- corresponds to the Unix touch command.
 *
 * If the file to touch doesn't exist, an empty one is created. </p>
 *
 * @ant.task name="touch"
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @author <a href="mailto:mj@servidium.com">Michael J. Sikorsky</a>
 * @author <a href="mailto:shaw@servidium.com">Robert Shaw</a>
 * @version $Revision$ $Date$
 */
public class Touch
    extends AbstractTask
{
    private final static Resources REZ =
        ResourceManager.getPackageResources( Touch.class );

    private long m_millis = -1;
    private String m_datetime;
    private ArrayList m_filesets = new ArrayList();
    private File m_file;

    /**
     * Date in the format MM/DD/YYYY HH:MM AM_PM.
     */
    public void setDatetime( final String datetime )
    {
        m_datetime = datetime;
    }

    /**
     * Sets a single source file to touch. If the file does not exist an empty
     * file will be created.
     */
    public void setFile( final File file )
    {
        m_file = file;
    }

    /**
     * Milliseconds since 01/01/1970 00:00 am.
     */
    public void setMillis( final long millis )
    {
        m_millis = millis;
    }

    /**
     * Adds a set of files (nested fileset attribute).
     */
    public void addFileset( final FileSet set )
    {
        m_filesets.add( set );
    }

    /**
     * Execute the touch operation.
     *
     * @exception TaskException Description of Exception
     */
    public void execute()
        throws TaskException
    {
        validate();

        if( m_datetime != null )
        {
            final DateFormat format =
                DateFormat.getDateTimeInstance( DateFormat.SHORT,
                                                DateFormat.SHORT,
                                                Locale.US );
            try
            {
                final long millis = format.parse( m_datetime ).getTime();
                if( 0 > millis )
                {
                    final String message = REZ.getString( "touch.neg-time.error", m_datetime );
                    throw new TaskException( message );
                }
                setMillis( millis );
            }
            catch( final ParseException pe )
            {
                throw new TaskException( pe.getMessage(), pe );
            }
        }

        touch();
    }

    private void validate()
        throws TaskException
    {
        if( null == m_file && 0 == m_filesets.size() )
        {
            final String message = REZ.getString( "touch.no-files.error" );
            throw new TaskException( message );
        }

        if( null != m_file && m_file.exists() && m_file.isDirectory() )
        {
            final String message = REZ.getString( "touch.use-fileset.error" );
            throw new TaskException( message );
        }
    }

    private void touch()
        throws TaskException
    {
        if( m_millis < 0 )
        {
            m_millis = System.currentTimeMillis();
        }

        if( null != m_file )
        {
            if( !m_file.exists() )
            {
                if( getContext().isInfoEnabled() )
                {
                    final String message = REZ.getString( "touch.create.notice", m_file );
                    getContext().info( message );
                }

                try
                {
                    FileOutputStream fos = new FileOutputStream( m_file );
                    fos.write( new byte[ 0 ] );
                    fos.close();
                }
                catch( final IOException ioe )
                {
                    final String message = REZ.getString( "touch.no-touch.error", m_file, ioe );
                    throw new TaskException( message, ioe );
                }
            }

            touch( m_file );
        }

        // deal with the filesets
        final int size = m_filesets.size();
        for( int i = 0; i < size; i++ )
        {
            final FileSet fileSet = (FileSet)m_filesets.get( i );
            final DirectoryScanner scanner = ScannerUtil.getDirectoryScanner( fileSet );
            final File fromDir = fileSet.getDir();

            final String[] srcFiles = scanner.getIncludedFiles();
            final String[] srcDirs = scanner.getIncludedDirectories();

            for( int j = 0; j < srcFiles.length; j++ )
            {
                final File file = new File( fromDir, srcFiles[ j ] );
                touch( file );
            }

            for( int j = 0; j < srcDirs.length; j++ )
            {
                final File file = new File( fromDir, srcDirs[ j ] );
                touch( file );
            }
        }
    }

    private void touch( final File file )
        throws TaskException
    {
        if( !file.canWrite() )
        {
            final String message = REZ.getString( "touch.readonly-file.error", file );
            throw new TaskException( message );
        }

        final long time = ( m_millis < 0 ) ? System.currentTimeMillis() : m_millis;
        file.setLastModified( time );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17379.java