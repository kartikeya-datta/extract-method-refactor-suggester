error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15792.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15792.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15792.java
text:
```scala
P@@ath classpath = new Path();

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.tools.ant.taskdefs.rmic;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Rmic;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.FileNameMapper;

/**
 * This is the default implementation for the RmicAdapter interface. Currently,
 * this is a cut-and-paste of the original rmic task and
 * DefaultCopmpilerAdapter.
 *
 * @author duncan@x180.com
 * @author ludovic.claude@websitewatchers.co.uk
 * @author David Maclean <a href="mailto:david@cm.co.za">david@cm.co.za</a>
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @author Takashi Okamoto <tokamoto@rd.nttdata.co.jp>
 */
public abstract class DefaultRmicAdapter
    extends AbstractLogEnabled
    implements RmicAdapter
{

    private final static Random rand = new Random();

    private Rmic attributes;
    private FileNameMapper mapper;

    public DefaultRmicAdapter()
    {
    }

    public void setRmic( Rmic attributes )
    {
        this.attributes = attributes;
        mapper = new RmicFileNameMapper();
    }

    /**
     * The CLASSPATH this rmic process will use.
     *
     * @return The Classpath value
     */
    public Path getClasspath()
        throws TaskException
    {
        return getCompileClasspath();
    }

    /**
     * This implementation maps *.class to *getStubClassSuffix().class and - if
     * stubversion is not 1.2 - to *getSkelClassSuffix().class.
     *
     * @return The Mapper value
     */
    public FileNameMapper getMapper()
    {
        return mapper;
    }

    public Rmic getRmic()
    {
        return attributes;
    }

    /**
     * setup rmic argument for rmic.
     *
     * @return Description of the Returned Value
     */
    protected Commandline setupRmicCommand()
        throws TaskException
    {
        return setupRmicCommand( null );
    }

    /**
     * setup rmic argument for rmic.
     *
     * @param options additional parameters needed by a specific implementation.
     * @return Description of the Returned Value
     */
    protected Commandline setupRmicCommand( String[] options )
        throws TaskException
    {
        Commandline cmd = new Commandline();

        if( options != null )
        {
            for( int i = 0; i < options.length; i++ )
            {
                cmd.createArgument().setValue( options[ i ] );
            }
        }

        Path classpath = getCompileClasspath();

        cmd.createArgument().setValue( "-d" );
        cmd.createArgument().setFile( attributes.getBase() );

        if( attributes.getExtdirs() != null )
        {
            if( Project.getJavaVersion().startsWith( "1.1" ) )
            {
                /*
                 * XXX - This doesn't mix very well with build.systemclasspath,
                 */
                classpath.addExtdirs( attributes.getExtdirs() );
            }
            else
            {
                cmd.createArgument().setValue( "-extdirs" );
                cmd.createArgument().setPath( attributes.getExtdirs() );
            }
        }

        cmd.createArgument().setValue( "-classpath" );
        cmd.createArgument().setPath( classpath );

        String stubVersion = attributes.getStubVersion();
        if( null != stubVersion )
        {
            if( "1.1".equals( stubVersion ) )
                cmd.createArgument().setValue( "-v1.1" );
            else if( "1.2".equals( stubVersion ) )
                cmd.createArgument().setValue( "-v1.2" );
            else
                cmd.createArgument().setValue( "-vcompat" );
        }

        if( null != attributes.getSourceBase() )
        {
            cmd.createArgument().setValue( "-keepgenerated" );
        }

        if( attributes.getIiop() )
        {
            getLogger().info( "IIOP has been turned on." );
            cmd.createArgument().setValue( "-iiop" );
            if( attributes.getIiopopts() != null )
            {
                getLogger().info( "IIOP Options: " + attributes.getIiopopts() );
                cmd.createArgument().setValue( attributes.getIiopopts() );
            }
        }

        if( attributes.getIdl() )
        {
            cmd.createArgument().setValue( "-idl" );
            getLogger().info( "IDL has been turned on." );
            if( attributes.getIdlopts() != null )
            {
                cmd.createArgument().setValue( attributes.getIdlopts() );
                getLogger().info( "IDL Options: " + attributes.getIdlopts() );
            }
        }

        if( attributes.getDebug() )
        {
            cmd.createArgument().setValue( "-g" );
        }

        logAndAddFilesToCompile( cmd );
        return cmd;
    }

    /**
     * Builds the compilation classpath.
     *
     * @return The CompileClasspath value
     */
    protected Path getCompileClasspath()
        throws TaskException
    {
        // add dest dir to classpath so that previously compiled and
        // untouched classes are on classpath
        Path classpath = new Path( attributes.getProject() );
        classpath.setLocation( attributes.getBase() );

        // Combine the build classpath with the system classpath, in an
        // order determined by the value of build.classpath

        if( attributes.getClasspath() == null )
        {
            if( attributes.getIncludeantruntime() )
            {
                classpath.addExisting( Path.systemClasspath );
            }
        }
        else
        {
            if( attributes.getIncludeantruntime() )
            {
                classpath.addExisting( attributes.getClasspath().concatSystemClasspath( "last" ) );
            }
            else
            {
                classpath.addExisting( attributes.getClasspath().concatSystemClasspath( "ignore" ) );
            }
        }

        if( attributes.getIncludejavaruntime() )
        {
            classpath.addJavaRuntime();
        }
        return classpath;
    }

    protected String getSkelClassSuffix()
    {
        return "_Skel";
    }

    protected String getStubClassSuffix()
    {
        return "_Stub";
    }

    protected String getTieClassSuffix()
    {
        return "_Tie";
    }

    /**
     * Logs the compilation parameters, adds the files to compile and logs the
     * &qout;niceSourceList&quot;
     *
     * @param cmd Description of Parameter
     */
    protected void logAndAddFilesToCompile( Commandline cmd )
    {
        ArrayList compileList = attributes.getCompileList();

        getLogger().debug( "Compilation args: " + cmd.toString() );

        StringBuffer niceSourceList = new StringBuffer( "File" );
        if( compileList.size() != 1 )
        {
            niceSourceList.append( "s" );
        }
        niceSourceList.append( " to be compiled:" );

        for( int i = 0; i < compileList.size(); i++ )
        {
            String arg = (String)compileList.get( i );
            cmd.createArgument().setValue( arg );
            niceSourceList.append( "    " + arg );
        }

        getLogger().debug( niceSourceList.toString() );
    }

    /**
     * Mapper that possibly returns two file names, *_Stub and *_Skel.
     *
     * @author RT
     */
    private class RmicFileNameMapper implements FileNameMapper
    {

        RmicFileNameMapper()
        {
        }

        /**
         * Empty implementation.
         *
         * @param s The new From value
         */
        public void setFrom( String s )
        {
        }

        /**
         * Empty implementation.
         *
         * @param s The new To value
         */
        public void setTo( String s )
        {
        }

        public String[] mapFileName( String name )
        {
            if( name == null
 !name.endsWith( ".class" )
 name.endsWith( getStubClassSuffix() + ".class" )
 name.endsWith( getSkelClassSuffix() + ".class" )
 name.endsWith( getTieClassSuffix() + ".class" ) )
            {
                // Not a .class file or the one we'd generate
                return null;
            }

            String base = name.substring( 0, name.indexOf( ".class" ) );
            String classname = base.replace( File.separatorChar, '.' );
            if( attributes.getVerify() &&
                !attributes.isValidRmiRemote( classname ) )
            {
                return null;
            }

            /*
             * fallback in case we have trouble loading the class or
             * don't know how to handle it (there is no easy way to
             * know what IDL mode would generate.
             *
             * This is supposed to make Ant always recompile the
             * class, as a file of that name should not exist.
             */
            String[] target = new String[]{name + ".tmp." + rand.nextLong()};

            if( !attributes.getIiop() && !attributes.getIdl() )
            {
                // JRMP with simple naming convention
                if( "1.2".equals( attributes.getStubVersion() ) )
                {
                    target = new String[]{
                        base + getStubClassSuffix() + ".class"
                    };
                }
                else
                {
                    target = new String[]{
                        base + getStubClassSuffix() + ".class",
                        base + getSkelClassSuffix() + ".class",
                    };
                }
            }
            else if( !attributes.getIdl() )
            {
                int lastSlash = base.lastIndexOf( File.separatorChar );

                String dirname = "";
                /*
                 * I know, this is not necessary, but I prefer it explicit (SB)
                 */
                int index = -1;
                if( lastSlash == -1 )
                {
                    // no package
                    index = 0;
                }
                else
                {
                    index = lastSlash + 1;
                    dirname = base.substring( 0, index );
                }

                String filename = base.substring( index );

                try
                {
                    Class c = attributes.getLoader().loadClass( classname );

                    if( c.isInterface() )
                    {
                        // only stub, no tie
                        target = new String[]{
                            dirname + "_" + filename + getStubClassSuffix()
                            + ".class"
                        };
                    }
                    else
                    {
                        /*
                         * stub is derived from implementation,
                         * tie from interface name.
                         */
                        Class interf = attributes.getRemoteInterface( c );
                        String iName = interf.getName();
                        String iDir = "";
                        int iIndex = -1;
                        int lastDot = iName.lastIndexOf( "." );
                        if( lastDot == -1 )
                        {
                            // no package
                            iIndex = 0;
                        }
                        else
                        {
                            iIndex = lastDot + 1;
                            iDir = iName.substring( 0, iIndex );
                            iDir = iDir.replace( '.', File.separatorChar );
                        }

                        target = new String[]{
                            dirname + "_" + filename + getTieClassSuffix()
                            + ".class",
                            iDir + "_" + iName.substring( iIndex )
                            + getStubClassSuffix() + ".class"
                        };
                    }
                }
                catch( ClassNotFoundException e )
                {
                    final String message = "Unable to verify class " + classname
                        + ". It could not be found.";
                    getLogger().warn( message );
                }
                catch( NoClassDefFoundError e )
                {
                    final String message = "Unable to verify class " + classname
                        + ". It is not defined.";
                    getLogger().warn( message );
                }
                catch( Throwable t )
                {
                    final String message = "Unable to verify class " + classname
                        + ". Loading caused Exception: "
                        + t.getMessage();
                    getLogger().warn( message );
                }
            }
            return target;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15792.java