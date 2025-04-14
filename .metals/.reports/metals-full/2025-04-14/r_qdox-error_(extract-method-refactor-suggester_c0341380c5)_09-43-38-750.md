error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6116.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6116.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6116.java
text:
```scala
m@@apper = new Mapper( getProject() );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.tools.ant.taskdefs.optional;

import java.io.File;
import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.IdentityMapper;
import org.apache.tools.ant.util.SourceFileScanner;

/**
 * Convert files from native encodings to ascii.
 *
 * @author <a href="asudell@acm.org">Drew Sudell</a>
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 */
public class Native2Ascii extends MatchingTask
{

    private boolean reverse = false;// convert from ascii back to native
    private String encoding = null;// encoding to convert to/from
    private File srcDir = null;// Where to find input files
    private File destDir = null;// Where to put output files
    private String extension = null;// Extension of output files if different

    private Mapper mapper;

    /**
     * Set the destination dirctory to place converted files into.
     *
     * @param destDir directory to place output file into.
     */
    public void setDest( File destDir )
    {
        this.destDir = destDir;
    }

    /**
     * Set the encoding to translate to/from. If unset, the default encoding for
     * the JVM is used.
     *
     * @param encoding String containing the name of the Native encoding to
     *      convert from or to.
     */
    public void setEncoding( String encoding )
    {
        this.encoding = encoding;
    }

    /**
     * Set the extension which converted files should have. If unset, files will
     * not be renamed.
     *
     * @param ext File extension to use for converted files.
     */
    public void setExt( String ext )
    {
        this.extension = ext;
    }

    /**
     * Flag the conversion to run in the reverse sense, that is Ascii to Native
     * encoding.
     *
     * @param reverse True if the conversion is to be reversed, otherwise false;
     */
    public void setReverse( boolean reverse )
    {
        this.reverse = reverse;
    }

    /**
     * Set the source directory in which to find files to convert.
     *
     * @param srcDir Direcrory to find input file in.
     */
    public void setSrc( File srcDir )
    {
        this.srcDir = srcDir;
    }

    /**
     * Defines the FileNameMapper to use (nested mapper element).
     *
     * @return Description of the Returned Value
     * @exception TaskException Description of Exception
     */
    public Mapper createMapper()
        throws TaskException
    {
        if( mapper != null )
        {
            throw new TaskException( "Cannot define more than one mapper" );
        }
        mapper = new Mapper( project );
        return mapper;
    }

    public void execute()
        throws TaskException
    {

        Commandline baseCmd = null;// the common portion of our cmd line
        DirectoryScanner scanner = null;// Scanner to find our inputs
        String[] files;// list of files to process

        // default srcDir to basedir
        if( srcDir == null )
        {
            srcDir = getBaseDirectory();
        }

        // Require destDir
        if( destDir == null )
        {
            throw new TaskException( "The dest attribute must be set." );
        }

        // if src and dest dirs are the same, require the extension
        // to be set, so we don't stomp every file.  One could still
        // include a file with the same extension, but ....
        if( srcDir.equals( destDir ) && extension == null && mapper == null )
        {
            throw new TaskException( "The ext attribute or a mapper must be set if"
                                     + " src and dest dirs are the same." );
        }

        FileNameMapper m = null;
        if( mapper == null )
        {
            if( extension == null )
            {
                m = new IdentityMapper();
            }
            else
            {
                m = new ExtMapper();
            }
        }
        else
        {
            m = mapper.getImplementation();
        }

        scanner = getDirectoryScanner( srcDir );
        files = scanner.getIncludedFiles();
        SourceFileScanner sfs = new SourceFileScanner( this );
        files = sfs.restrict( files, srcDir, destDir, m );
        int count = files.length;
        if( count == 0 )
        {
            return;
        }
        String message = "Converting " + count + " file"
            + ( count != 1 ? "s" : "" ) + " from ";
        getLogger().info( message + srcDir + " to " + destDir );
        for( int i = 0; i < files.length; i++ )
        {
            convert( files[ i ], m.mapFileName( files[ i ] )[ 0 ] );
        }
    }

    /**
     * Convert a single file.
     *
     * @param srcName Description of Parameter
     * @param destName Description of Parameter
     * @exception TaskException Description of Exception
     */
    private void convert( String srcName, String destName )
        throws TaskException
    {

        Commandline cmd = new Commandline();// Command line to run
        File srcFile;// File to convert
        File destFile;// where to put the results

        // Set up the basic args (this could be done once, but
        // it's cleaner here)
        if( reverse )
        {
            cmd.createArgument().setValue( "-reverse" );
        }

        if( encoding != null )
        {
            cmd.createArgument().setValue( "-encoding" );
            cmd.createArgument().setValue( encoding );
        }

        // Build the full file names
        srcFile = new File( srcDir, srcName );
        destFile = new File( destDir, destName );

        cmd.createArgument().setFile( srcFile );
        cmd.createArgument().setFile( destFile );
        // Make sure we're not about to clobber something
        if( srcFile.equals( destFile ) )
        {
            throw new TaskException( "file " + srcFile
                                     + " would overwrite its self" );
        }

        // Make intermediate directories if needed
        // XXX JDK 1.1 dosen't have File.getParentFile,
        String parentName = destFile.getParent();
        if( parentName != null )
        {
            File parentFile = new File( parentName );

            if( ( !parentFile.exists() ) && ( !parentFile.mkdirs() ) )
            {
                throw new TaskException( "cannot create parent directory "
                                         + parentName );
            }
        }

        log( "converting " + srcName, Project.MSG_VERBOSE );
        sun.tools.native2ascii.Main n2a
            = new sun.tools.native2ascii.Main();
        if( !n2a.convert( cmd.getArguments() ) )
        {
            throw new TaskException( "conversion failed" );
        }
    }

    private class ExtMapper implements FileNameMapper
    {

        public void setFrom( String s )
        {
        }

        public void setTo( String s )
        {
        }

        public String[] mapFileName( String fileName )
        {
            int lastDot = fileName.lastIndexOf( '.' );
            if( lastDot >= 0 )
            {
                return new String[]{fileName.substring( 0, lastDot ) + extension};
            }
            else
            {
                return new String[]{fileName + extension};
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6116.java