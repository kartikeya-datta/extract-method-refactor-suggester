error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13056.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13056.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13056.java
text:
```scala
R@@micAdapter adapter = RmicAdapterFactory.getRmic( compiler );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.rmi.Remote;
import java.util.ArrayList;
import org.apache.avalon.excalibur.io.FileUtil;
import org.apache.myrmidon.api.TaskException;
import org.apache.myrmidon.api.AbstractTask;
import org.apache.tools.ant.taskdefs.rmic.RmicAdapter;
import org.apache.tools.ant.taskdefs.rmic.RmicAdapterFactory;
import org.apache.tools.ant.types.DirectoryScanner;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.PathUtil;
import org.apache.tools.ant.types.SourceFileScanner;
import org.apache.tools.ant.util.mappers.FileNameMapper;

/**
 * Task to compile RMI stubs and skeletons. This task can take the following
 * arguments:
 * <ul>
 *   <li> base: The base directory for the compiled stubs and skeletons
 *   <li> class: The name of the class to generate the stubs from
 *   <li> stubVersion: The version of the stub prototol to use (1.1, 1.2,
 *   compat)
 *   <li> sourceBase: The base directory for the generated stubs and skeletons
 *
 *   <li> classpath: Additional classpath, appended before the system classpath
 *
 *   <li> iiop: Generate IIOP compatable output
 *   <li> iiopopts: Include IIOP options
 *   <li> idl: Generate IDL output
 *   <li> idlopts: Include IDL options
 *   <li> includeantruntime
 *   <li> includejavaruntime
 *   <li> extdirs
 * </ul>
 * Of these arguments, <b>base</b> is required. <p>
 *
 * If classname is specified then only that classname will be compiled. If it is
 * absent, then <b>base</b> is traversed for classes according to patterns. <p>
 *
 *
 *
 * @author duncan@x180.com
 * @author ludovic.claude@websitewatchers.co.uk
 * @author David Maclean <a href="mailto:david@cm.co.za">david@cm.co.za</a>
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @author Takashi Okamoto tokamoto@rd.nttdata.co.jp
 */

public class Rmic extends MatchingTask
{

    private final static String FAIL_MSG
        = "Rmic failed, messages should have been provided.";
    private boolean verify;

    private boolean iiop;
    private boolean idl;
    private boolean debug;
    private boolean includeAntRuntime;
    private boolean includeJavaRuntime;

    private ArrayList compileList = new ArrayList();

    private ClassLoader loader;

    private File baseDir;
    private String classname;
    private Path compileClasspath;
    private Path extdirs;
    private String idlopts;
    private String iiopopts;
    private File sourceBase;
    private String stubVersion;

    /**
     * Sets the base directory to output generated class.
     *
     * @param base The new Base value
     */
    public void setBase( File base )
    {
        this.baseDir = base;
    }

    /**
     * Sets the class name to compile.
     *
     * @param classname The new Classname value
     */
    public void setClassname( String classname )
    {
        this.classname = classname;
    }

    /**
     * Add an element to the classpath to be used for this compilation.
     *
     * @param classpath The new Classpath value
     */
    public void addClasspath( Path classpath )
        throws TaskException
    {
        if( compileClasspath == null )
        {
            compileClasspath = classpath;
        }
        else
        {
            compileClasspath.addPath( classpath );
        }
    }

    /**
     * Sets the debug flag.
     *
     * @param debug The new Debug value
     */
    public void setDebug( boolean debug )
    {
        this.debug = debug;
    }

    /**
     * Adds an element to the extension directories that will be used during
     * the compilation.
     *
     * @param extdirs The new Extdirs value
     */
    public void addExtdirs( Path extdirs )
        throws TaskException
    {
        if( this.extdirs == null )
        {
            this.extdirs = extdirs;
        }
        else
        {
            this.extdirs.addPath( extdirs );
        }
    }

    /**
     * Indicates that IDL output should be generated. This defaults to false if
     * not set.
     *
     * @param idl The new Idl value
     */
    public void setIdl( boolean idl )
    {
        this.idl = idl;
    }

    /**
     * pass additional arguments for idl compile
     *
     * @param idlopts The new Idlopts value
     */
    public void setIdlopts( String idlopts )
    {
        this.idlopts = idlopts;
    }

    /**
     * Indicates that IIOP compatible stubs should be generated. This defaults
     * to false if not set.
     *
     * @param iiop The new Iiop value
     */
    public void setIiop( boolean iiop )
    {
        this.iiop = iiop;
    }

    /**
     * pass additional arguments for iiop
     *
     * @param iiopopts The new Iiopopts value
     */
    public void setIiopopts( String iiopopts )
    {
        this.iiopopts = iiopopts;
    }

    /**
     * Include ant's own classpath in this task's classpath?
     *
     * @param include The new Includeantruntime value
     */
    public void setIncludeantruntime( boolean include )
    {
        includeAntRuntime = include;
    }

    /**
     * Sets whether or not to include the java runtime libraries to this task's
     * classpath.
     *
     * @param include The new Includejavaruntime value
     */
    public void setIncludejavaruntime( boolean include )
    {
        includeJavaRuntime = include;
    }

    /**
     * Sets the source dirs to find the source java files.
     *
     * @param sourceBase The new SourceBase value
     */
    public void setSourceBase( File sourceBase )
    {
        this.sourceBase = sourceBase;
    }

    /**
     * Sets the stub version.
     *
     * @param stubVersion The new StubVersion value
     */
    public void setStubVersion( String stubVersion )
    {
        this.stubVersion = stubVersion;
    }

    /**
     * Indicates that the classes found by the directory match should be checked
     * to see if they implement java.rmi.Remote. This defaults to false if not
     * set.
     *
     * @param verify The new Verify value
     */
    public void setVerify( boolean verify )
    {
        this.verify = verify;
    }

    /**
     * Gets the base directory to output generated class.
     *
     * @return The Base value
     */
    public File getBase()
    {
        return this.baseDir;
    }

    /**
     * Gets the class name to compile.
     *
     * @return The Classname value
     */
    public String getClassname()
    {
        return classname;
    }

    /**
     * Gets the classpath.
     *
     * @return The Classpath value
     */
    public Path getClasspath()
    {
        return compileClasspath;
    }

    public ArrayList getCompileList()
    {
        return compileList;
    }

    /**
     * Gets the debug flag.
     *
     * @return The Debug value
     */
    public boolean getDebug()
    {
        return debug;
    }

    /**
     * Gets the extension directories that will be used during the compilation.
     *
     * @return The Extdirs value
     */
    public Path getExtdirs()
    {
        return extdirs;
    }

    /*
     * Gets IDL flags.
     */
    public boolean getIdl()
    {
        return idl;
    }

    /**
     * Gets additional arguments for idl compile.
     *
     * @return The Idlopts value
     */
    public String getIdlopts()
    {
        return idlopts;
    }

    /**
     * Gets iiop flags.
     *
     * @return The Iiop value
     */
    public boolean getIiop()
    {
        return iiop;
    }

    /**
     * Gets additional arguments for iiop.
     *
     * @return The Iiopopts value
     */
    public String getIiopopts()
    {
        return iiopopts;
    }

    /**
     * Gets whether or not the ant classpath is to be included in the task's
     * classpath.
     *
     * @return The Includeantruntime value
     */
    public boolean getIncludeantruntime()
    {
        return includeAntRuntime;
    }

    /**
     * Gets whether or not the java runtime should be included in this task's
     * classpath.
     *
     * @return The Includejavaruntime value
     */
    public boolean getIncludejavaruntime()
    {
        return includeJavaRuntime;
    }

    /**
     * Classloader for the user-specified classpath.
     *
     * @return The Loader value
     */
    public ClassLoader getLoader()
    {
        return loader;
    }

    /**
     * Returns the topmost interface that extends Remote for a given class - if
     * one exists.
     *
     * @param testClass Description of Parameter
     * @return The RemoteInterface value
     */
    public Class getRemoteInterface( Class testClass )
    {
        if( Remote.class.isAssignableFrom( testClass ) )
        {
            Class[] interfaces = testClass.getInterfaces();
            if( interfaces != null )
            {
                for( int i = 0; i < interfaces.length; i++ )
                {
                    if( Remote.class.isAssignableFrom( interfaces[ i ] ) )
                    {
                        return interfaces[ i ];
                    }
                }
            }
        }
        return null;
    }

    /**
     * Gets the source dirs to find the source java files.
     *
     * @return The SourceBase value
     */
    public File getSourceBase()
    {
        return sourceBase;
    }

    public String getStubVersion()
    {
        return stubVersion;
    }

    /**
     * Get verify flag.
     *
     * @return The Verify value
     */
    public boolean getVerify()
    {
        return verify;
    }

    /**
     * Load named class and test whether it can be rmic'ed
     *
     * @param classname Description of Parameter
     * @return The ValidRmiRemote value
     */
    public boolean isValidRmiRemote( String classname )
    {
        try
        {
            Class testClass = loader.loadClass( classname );
            // One cannot RMIC an interface for "classic" RMI (JRMP)
            if( testClass.isInterface() && !iiop && !idl )
            {
                return false;
            }
            return isValidRmiRemote( testClass );
        }
        catch( ClassNotFoundException e )
        {
            final String message = "Unable to verify class " + classname +
                ". It could not be found.";
            getLogger().warn( message );
        }
        catch( NoClassDefFoundError e )
        {
            final String message = "Unable to verify class " + classname +
                ". It is not defined.";
            getLogger().warn( message );
        }
        catch( Throwable t )
        {
            final String message = "Unable to verify class " + classname +
                ". Loading caused Exception: " +
                t.getMessage();
            getLogger().warn( message );
        }
        // we only get here if an exception has been thrown
        return false;
    }

    public void execute()
        throws TaskException
    {
        if( baseDir == null )
        {
            throw new TaskException( "base attribute must be set!" );
        }
        if( !baseDir.exists() )
        {
            throw new TaskException( "base does not exist!" );
        }

        if( verify )
        {
            getLogger().info( "Verify has been turned on." );
        }

        String compiler = getContext().getProperty( "build.rmic" ).toString();
        RmicAdapter adapter = RmicAdapterFactory.getRmic( compiler, this );

        // now we need to populate the compiler adapter
        adapter.setRmic( this );

        Path classpath = adapter.getClasspath();
        final URL[] urls = PathUtil.toURLs( classpath );
        loader = new URLClassLoader( urls );

        // scan base dirs to build up compile lists only if a
        // specific classname is not given
        if( classname == null )
        {
            DirectoryScanner ds = this.getDirectoryScanner( baseDir );
            String[] files = ds.getIncludedFiles();
            scanDir( baseDir, files, adapter.getMapper() );
        }
        else
        {
            // otherwise perform a timestamp comparison - at least
            scanDir( baseDir,
                     new String[]{classname.replace( '.', File.separatorChar ) + ".class"},
                     adapter.getMapper() );
        }

        int fileCount = compileList.size();
        if( fileCount > 0 )
        {
            getLogger().info( "RMI Compiling " + fileCount + " class" + ( fileCount > 1 ? "es" : "" ) + " to " + baseDir );

            // finally, lets execute the compiler!!
            if( !adapter.execute() )
            {
                throw new TaskException( FAIL_MSG );
            }
        }

        /*
         * Move the generated source file to the base directory.  If
         * base directory and sourcebase are the same, the generated
         * sources are already in place.
         */
        if( null != sourceBase && !baseDir.equals( sourceBase ) )
        {
            if( idl )
            {
                getLogger().warn( "Cannot determine sourcefiles in idl mode, " );
                getLogger().warn( "sourcebase attribute will be ignored." );
            }
            else
            {
                for( int j = 0; j < fileCount; j++ )
                {
                    moveGeneratedFile( baseDir, sourceBase,
                                       (String)compileList.get( j ),
                                       adapter );
                }
            }
        }
        compileList.clear();
    }

    /**
     * Scans the directory looking for class files to be compiled. The result is
     * returned in the class variable compileList.
     *
     * @param baseDir Description of Parameter
     * @param files Description of Parameter
     * @param mapper Description of Parameter
     */
    protected void scanDir( File baseDir, String files[],
                            FileNameMapper mapper )
        throws TaskException
    {

        String[] newFiles = files;
        if( idl )
        {
            getLogger().debug( "will leave uptodate test to rmic implementation in idl mode." );
        }
        else if( iiop
            && iiopopts != null && iiopopts.indexOf( "-always" ) > -1 )
        {
            getLogger().debug( "no uptodate test as -always option has been specified" );
        }
        else
        {
            final SourceFileScanner scanner = new SourceFileScanner();
            setupLogger( scanner );
            newFiles = scanner.restrict( files, baseDir, baseDir, mapper );
        }

        for( int i = 0; i < newFiles.length; i++ )
        {
            String classname = newFiles[ i ].replace( File.separatorChar, '.' );
            classname = classname.substring( 0, classname.lastIndexOf( ".class" ) );
            compileList.add( classname );
        }
    }

    /**
     * Check to see if the class or (super)interfaces implement java.rmi.Remote.
     *
     * @param testClass Description of Parameter
     * @return The ValidRmiRemote value
     */
    private boolean isValidRmiRemote( Class testClass )
    {
        return getRemoteInterface( testClass ) != null;
    }

    /**
     * Move the generated source file(s) to the base directory
     */
    private void moveGeneratedFile( File baseDir, File sourceBaseFile,
                                    String classname,
                                    RmicAdapter adapter )
        throws TaskException
    {

        String classFileName =
            classname.replace( '.', File.separatorChar ) + ".class";
        String[] generatedFiles =
            adapter.getMapper().mapFileName( classFileName );

        for( int i = 0; i < generatedFiles.length; i++ )
        {
            String sourceFileName =
                classFileName.substring( 0, classFileName.length() - 6 ) + ".java";
            File oldFile = new File( baseDir, sourceFileName );
            File newFile = new File( sourceBaseFile, sourceFileName );
            try
            {
                FileUtil.copyFile( oldFile, newFile );
                oldFile.delete();
            }
            catch( IOException ioe )
            {
                String msg = "Failed to copy " + oldFile + " to " +
                    newFile + " due to " + ioe.getMessage();
                throw new TaskException( msg, ioe );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13056.java