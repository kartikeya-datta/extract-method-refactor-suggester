error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9285.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9285.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9285.java
text:
```scala
g@@etLogger().info( "Signing Jar : " + jarSource.getAbsolutePath() );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

/**
 * Sign a archive.
 *
 * @author Peter Donald <a href="mailto:donaldp@apache.org">donaldp@apache.org
 *      </a>
 * @author Nick Fortescue <a href="mailto:nick@ox.compsoc.net">
 *      nick@ox.compsoc.net</a>
 */
public class SignJar extends Task
{

    /**
     * the filesets of the jars to sign
     */
    protected Vector filesets = new Vector();

    /**
     * The alias of signer.
     */
    protected String alias;
    protected boolean internalsf;

    /**
     * The name of the jar file.
     */
    protected File jar;
    protected String keypass;

    /**
     * The name of keystore file.
     */
    protected File keystore;
    /**
     * Whether to assume a jar which has an appropriate .SF file in is already
     * signed.
     */
    protected boolean lazy;
    protected boolean sectionsonly;
    protected File sigfile;
    protected File signedjar;

    protected String storepass;
    protected String storetype;
    protected boolean verbose;

    public void setAlias( final String alias )
    {
        this.alias = alias;
    }

    public void setInternalsf( final boolean internalsf )
    {
        this.internalsf = internalsf;
    }

    public void setJar( final File jar )
    {
        this.jar = jar;
    }

    public void setKeypass( final String keypass )
    {
        this.keypass = keypass;
    }

    public void setKeystore( final File keystore )
    {
        this.keystore = keystore;
    }

    public void setLazy( final boolean lazy )
    {
        this.lazy = lazy;
    }

    public void setSectionsonly( final boolean sectionsonly )
    {
        this.sectionsonly = sectionsonly;
    }

    public void setSigfile( final File sigfile )
    {
        this.sigfile = sigfile;
    }

    public void setSignedjar( final File signedjar )
    {
        this.signedjar = signedjar;
    }

    public void setStorepass( final String storepass )
    {
        this.storepass = storepass;
    }

    public void setStoretype( final String storetype )
    {
        this.storetype = storetype;
    }

    public void setVerbose( final boolean verbose )
    {
        this.verbose = verbose;
    }

    /**
     * Adds a set of files (nested fileset attribute).
     *
     * @param set The feature to be added to the Fileset attribute
     */
    public void addFileset( final FileSet set )
    {
        filesets.addElement( set );
    }

    public void execute()
        throws TaskException
    {
        if( null == jar && null == filesets )
        {
            throw new TaskException( "jar must be set through jar attribute or nested filesets" );
        }
        if( null != jar )
        {
            doOneJar( jar, signedjar );
            return;
        }
        else
        {
            //Assume null != filesets

            // deal with the filesets
            for( int i = 0; i < filesets.size(); i++ )
            {
                FileSet fs = (FileSet)filesets.elementAt( i );
                DirectoryScanner ds = fs.getDirectoryScanner( project );
                String[] jarFiles = ds.getIncludedFiles();
                for( int j = 0; j < jarFiles.length; j++ )
                {
                    doOneJar( new File( fs.getDir( project ), jarFiles[ j ] ), null );
                }
            }
        }
    }

    protected boolean isSigned( File file )
    {
        final String SIG_START = "META-INF/";
        final String SIG_END = ".SF";

        if( !file.exists() )
        {
            return false;
        }
        ZipFile jarFile = null;
        try
        {
            jarFile = new ZipFile( file );
            if( null == alias )
            {
                Enumeration entries = jarFile.entries();
                while( entries.hasMoreElements() )
                {
                    String name = ( (ZipEntry)entries.nextElement() ).getName();
                    if( name.startsWith( SIG_START ) && name.endsWith( SIG_END ) )
                    {
                        return true;
                    }
                }
                return false;
            }
            else
            {
                return jarFile.getEntry( SIG_START + alias.toUpperCase() +
                                         SIG_END ) != null;
            }
        }
        catch( IOException e )
        {
            return false;
        }
        finally
        {
            if( jarFile != null )
            {
                try
                {
                    jarFile.close();
                }
                catch( IOException e )
                {
                }
            }
        }
    }

    protected boolean isUpToDate( File jarFile, File signedjarFile )
    {
        if( null == jarFile )
        {
            return false;
        }

        if( null != signedjarFile )
        {

            if( !jarFile.exists() )
                return false;
            if( !signedjarFile.exists() )
                return false;
            if( jarFile.equals( signedjarFile ) )
                return false;
            if( signedjarFile.lastModified() > jarFile.lastModified() )
                return true;
        }
        else
        {
            if( lazy )
            {
                return isSigned( jarFile );
            }
        }

        return false;
    }

    private void doOneJar( File jarSource, File jarTarget )
        throws TaskException
    {
        if( project.getJavaVersion().equals( Project.JAVA_1_1 ) )
        {
            throw new TaskException( "The signjar task is only available on JDK versions 1.2 or greater" );
        }

        if( null == alias )
        {
            throw new TaskException( "alias attribute must be set" );
        }

        if( null == storepass )
        {
            throw new TaskException( "storepass attribute must be set" );
        }

        if( isUpToDate( jarSource, jarTarget ) )
            return;

        final StringBuffer sb = new StringBuffer();

        final ExecTask cmd = (ExecTask)project.createTask( "exec" );
        cmd.setExecutable( "jarsigner" );

        if( null != keystore )
        {
            cmd.createArg().setValue( "-keystore" );
            cmd.createArg().setValue( keystore.toString() );
        }

        if( null != storepass )
        {
            cmd.createArg().setValue( "-storepass" );
            cmd.createArg().setValue( storepass );
        }

        if( null != storetype )
        {
            cmd.createArg().setValue( "-storetype" );
            cmd.createArg().setValue( storetype );
        }

        if( null != keypass )
        {
            cmd.createArg().setValue( "-keypass" );
            cmd.createArg().setValue( keypass );
        }

        if( null != sigfile )
        {
            cmd.createArg().setValue( "-sigfile" );
            cmd.createArg().setValue( sigfile.toString() );
        }

        if( null != jarTarget )
        {
            cmd.createArg().setValue( "-signedjar" );
            cmd.createArg().setValue( jarTarget.toString() );
        }

        if( verbose )
        {
            cmd.createArg().setValue( "-verbose" );
        }

        if( internalsf )
        {
            cmd.createArg().setValue( "-internalsf" );
        }

        if( sectionsonly )
        {
            cmd.createArg().setValue( "-sectionsonly" );
        }

        cmd.createArg().setValue( jarSource.toString() );

        cmd.createArg().setValue( alias );

        log( "Signing Jar : " + jarSource.getAbsolutePath() );
        cmd.setFailonerror( true );
        cmd.execute();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9285.java