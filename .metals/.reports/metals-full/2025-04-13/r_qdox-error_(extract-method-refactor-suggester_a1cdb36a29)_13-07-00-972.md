error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2302.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2302.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2302.java
text:
```scala
F@@ile dirF = f.getParentFile();

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.PatternSet;
import org.apache.tools.ant.util.FileUtils;

/**
 * Unzip a file.
 *
 * @author costin@dnt.ro
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @author <a href="mailto:umagesh@rediffmail.com">Magesh Umasankar</a>
 */
public class Expand extends MatchingTask
{// req
    private boolean overwrite = true;
    private Vector patternsets = new Vector();
    private Vector filesets = new Vector();
    private File dest;//req
    private File source;

    /**
     * Set the destination directory. File will be unzipped into the destination
     * directory.
     *
     * @param d Path to the directory.
     */
    public void setDest( File d )
    {
        this.dest = d;
    }

    /**
     * Should we overwrite files in dest, even if they are newer than the
     * corresponding entries in the archive?
     *
     * @param b The new Overwrite value
     */
    public void setOverwrite( boolean b )
    {
        overwrite = b;
    }

    /**
     * Set the path to zip-file.
     *
     * @param s Path to zip-file.
     */
    public void setSrc( File s )
    {
        this.source = s;
    }

    /**
     * Add a fileset
     *
     * @param set The feature to be added to the Fileset attribute
     */
    public void addFileset( FileSet set )
    {
        filesets.addElement( set );
    }

    /**
     * Add a patternset
     *
     * @param set The feature to be added to the Patternset attribute
     */
    public void addPatternset( PatternSet set )
    {
        patternsets.addElement( set );
    }

    /**
     * Do the work.
     *
     * @exception TaskException Thrown in unrecoverable error.
     */
    public void execute()
        throws TaskException
    {
        if( source == null && filesets.size() == 0 )
        {
            throw new TaskException( "src attribute and/or filesets must be specified" );
        }

        if( dest == null )
        {
            throw new TaskException(
                "Dest attribute must be specified" );
        }

        if( dest.exists() && !dest.isDirectory() )
        {
            throw new TaskException( "Dest must be a directory." );
        }

        FileUtils fileUtils = FileUtils.newFileUtils();

        if( source != null )
        {
            if( source.isDirectory() )
            {
                throw new TaskException( "Src must not be a directory." +
                                         " Use nested filesets instead." );
            }
            else
            {
                expandFile( fileUtils, source, dest );
            }
        }
        if( filesets.size() > 0 )
        {
            for( int j = 0; j < filesets.size(); j++ )
            {
                FileSet fs = (FileSet)filesets.elementAt( j );
                DirectoryScanner ds = fs.getDirectoryScanner( project );
                File fromDir = fs.getDir( project );

                String[] files = ds.getIncludedFiles();
                for( int i = 0; i < files.length; ++i )
                {
                    File file = new File( fromDir, files[ i ] );
                    expandFile( fileUtils, file, dest );
                }
            }
        }
    }

    /*
     * This method is to be overridden by extending unarchival tasks.
     */
    protected void expandFile( FileUtils fileUtils, File srcF, File dir )
        throws TaskException
    {
        ZipInputStream zis = null;
        try
        {
            // code from WarExpand
            zis = new ZipInputStream( new FileInputStream( srcF ) );
            ZipEntry ze = null;

            while( ( ze = zis.getNextEntry() ) != null )
            {
                extractFile( fileUtils, srcF, dir, zis,
                             ze.getName(),
                             new Date( ze.getTime() ),
                             ze.isDirectory() );
            }

            log( "expand complete", Project.MSG_VERBOSE );
        }
        catch( IOException ioe )
        {
            throw new TaskException( "Error while expanding " + srcF.getPath(), ioe );
        }
        finally
        {
            if( zis != null )
            {
                try
                {
                    zis.close();
                }
                catch( IOException e )
                {
                }
            }
        }
    }

    protected void extractFile( FileUtils fileUtils, File srcF, File dir,
                                InputStream compressedInputStream,
                                String entryName,
                                Date entryDate, boolean isDirectory )
        throws IOException, TaskException
    {

        if( patternsets != null && patternsets.size() > 0 )
        {
            String name = entryName;
            boolean included = false;
            for( int v = 0; v < patternsets.size(); v++ )
            {
                PatternSet p = (PatternSet)patternsets.elementAt( v );
                String[] incls = p.getIncludePatterns( project );
                if( incls != null )
                {
                    for( int w = 0; w < incls.length; w++ )
                    {
                        boolean isIncl = DirectoryScanner.match( incls[ w ], name );
                        if( isIncl )
                        {
                            included = true;
                            break;
                        }
                    }
                }
                String[] excls = p.getExcludePatterns( project );
                if( excls != null )
                {
                    for( int w = 0; w < excls.length; w++ )
                    {
                        boolean isExcl = DirectoryScanner.match( excls[ w ], name );
                        if( isExcl )
                        {
                            included = false;
                            break;
                        }
                    }
                }
            }
            if( !included )
            {
                //Do not process this file
                return;
            }
        }

        File f = fileUtils.resolveFile( dir, entryName );
        try
        {
            if( !overwrite && f.exists()
                && f.lastModified() >= entryDate.getTime() )
            {
                log( "Skipping " + f + " as it is up-to-date",
                     Project.MSG_DEBUG );
                return;
            }

            log( "expanding " + entryName + " to " + f,
                 Project.MSG_VERBOSE );
            // create intermediary directories - sometimes zip don't add them
            File dirF = fileUtils.getParentFile( f );
            dirF.mkdirs();

            if( isDirectory )
            {
                f.mkdirs();
            }
            else
            {
                byte[] buffer = new byte[ 1024 ];
                int length = 0;
                FileOutputStream fos = null;
                try
                {
                    fos = new FileOutputStream( f );

                    while( ( length =
                        compressedInputStream.read( buffer ) ) >= 0 )
                    {
                        fos.write( buffer, 0, length );
                    }

                    fos.close();
                    fos = null;
                }
                finally
                {
                    if( fos != null )
                    {
                        try
                        {
                            fos.close();
                        }
                        catch( IOException e )
                        {
                        }
                    }
                }
            }

            f.setLastModified( entryDate.getTime() );
        }
        catch( FileNotFoundException ex )
        {
            log( "Unable to expand to file " + f.getPath(), Project.MSG_WARN );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2302.java