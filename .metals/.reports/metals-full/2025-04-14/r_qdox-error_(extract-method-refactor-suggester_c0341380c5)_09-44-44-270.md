error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15793.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15793.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15793.java
text:
```scala
c@@lasspath = new Path();

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.tools.ant.types;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import org.apache.myrmidon.api.TaskException;
import org.apache.myrmidon.framework.Os;
import org.apache.myrmidon.framework.exec.Environment;
import org.apache.myrmidon.framework.exec.ExecException;
import org.apache.tools.ant.Project;

/**
 * A representation of a Java command line that is nothing more than a composite
 * of 2 <tt>Commandline</tt> . 1 for the vm/options and 1 for the
 * classname/arguments. It provides specific methods for a java command line.
 *
 * @author thomas.haas@softwired-inc.com
 * @author <a href="sbailliez@apache.org">Stephane Bailliez</a>
 */
public class CommandlineJava implements Cloneable
{

    private Commandline vmCommand = new Commandline();
    private Commandline javaCommand = new Commandline();
    private SysProperties sysProperties = new SysProperties();
    private Path classpath = null;
    private String maxMemory = null;

    /**
     * Indicate whether it will execute a jar file or not, in this case the
     * first vm option must be a -jar and the 'executable' is a jar file.
     */
    private boolean executeJar = false;
    private String vmVersion;

    public CommandlineJava()
    {
        setVm( getJavaExecutableName() );
        setVmversion( Project.getJavaVersion() );
    }

    /**
     * set the classname to execute
     *
     * @param classname the fully qualified classname.
     */
    public void setClassname( String classname )
    {
        javaCommand.setExecutable( classname );
        executeJar = false;
    }

    /**
     * set a jar file to execute via the -jar option.
     *
     * @param jarpathname The new Jar value
     */
    public void setJar( String jarpathname )
    {
        javaCommand.setExecutable( jarpathname );
        executeJar = true;
    }

    /**
     * -mx or -Xmx depending on VM version
     *
     * @param max The new Maxmemory value
     */
    public void setMaxmemory( String max )
    {
        this.maxMemory = max;
    }

    public void setSystemProperties()
        throws TaskException
    {
        sysProperties.setSystem();
    }

    public void setVm( String vm )
    {
        vmCommand.setExecutable( vm );
    }

    public void setVmversion( String value )
    {
        vmVersion = value;
    }

    /**
     * @return the name of the class to run or <tt>null</tt> if there is no
     *      class.
     * @see #getJar()
     */
    public String getClassname()
    {
        if( !executeJar )
        {
            return javaCommand.getExecutable();
        }
        return null;
    }

    public Path getClasspath()
    {
        return classpath;
    }

    /**
     * get the command line to run a java vm.
     *
     * @return the list of all arguments necessary to run the vm.
     */
    public String[] getCommandline()
        throws TaskException
    {
        String[] result = new String[ size() ];
        int pos = 0;
        String[] vmArgs = getActualVMCommand().getCommandline();
        // first argument is the java.exe path...
        result[ pos++ ] = vmArgs[ 0 ];

        // -jar must be the first option in the command line.
        if( executeJar )
        {
            result[ pos++ ] = "-jar";
        }
        // next follows the vm options
        System.arraycopy( vmArgs, 1, result, pos, vmArgs.length - 1 );
        pos += vmArgs.length - 1;
        // properties are part of the vm options...
        if( sysProperties.size() > 0 )
        {
            System.arraycopy( sysProperties.getJavaVariables(), 0,
                              result, pos, sysProperties.size() );
            pos += sysProperties.size();
        }
        // classpath is a vm option too..
        Path fullClasspath = classpath != null ? classpath.concatSystemClasspath( "ignore" ) : null;
        if( fullClasspath != null && fullClasspath.toString().trim().length() > 0 )
        {
            result[ pos++ ] = "-classpath";
            result[ pos++ ] = fullClasspath.toString();
        }
        // this is the classname to run as well as its arguments.
        // in case of 'executeJar', the executable is a jar file.
        System.arraycopy( javaCommand.getCommandline(), 0,
                          result, pos, javaCommand.size() );
        return result;
    }

    /**
     * @return the pathname of the jar file to run via -jar option or <tt>null
     *      </tt> if there is no jar to run.
     * @see #getClassname()
     */
    public String getJar()
    {
        if( executeJar )
        {
            return javaCommand.getExecutable();
        }
        return null;
    }

    public Commandline getJavaCommand()
    {
        return javaCommand;
    }

    public SysProperties getSystemProperties()
    {
        return sysProperties;
    }

    public Commandline getVmCommand()
    {
        return getActualVMCommand();
    }

    public String getVmversion()
    {
        return vmVersion;
    }

    public void addSysproperty( EnvironmentVariable sysp )
    {
        sysProperties.addVariable( sysp );
    }

    /**
     * Clear out the java arguments.
     */
    public void clearJavaArgs()
    {
        javaCommand.clearArgs();
    }

    public Object clone()
    {
        CommandlineJava c = new CommandlineJava();
        c.vmCommand = (Commandline)vmCommand.clone();
        c.javaCommand = (Commandline)javaCommand.clone();
        c.sysProperties = (SysProperties)sysProperties.clone();
        c.maxMemory = maxMemory;
        if( classpath != null )
        {
            c.classpath = (Path)classpath.clone();
        }
        c.vmVersion = vmVersion;
        c.executeJar = executeJar;
        return c;
    }

    public Argument createArgument()
    {
        return javaCommand.createArgument();
    }

    public Path createClasspath( Project p )
    {
        if( classpath == null )
        {
            classpath = new Path( p );
        }
        return classpath;
    }

    public Argument createVmArgument()
    {
        return vmCommand.createArgument();
    }

    public void restoreSystemProperties()
        throws TaskException
    {
        sysProperties.restoreSystem();
    }

    /**
     * The size of the java command line.
     *
     * @return the total number of arguments in the java command line.
     * @see #getCommandline()
     */
    public int size()
        throws TaskException
    {
        int size = getActualVMCommand().size() + javaCommand.size() + sysProperties.size();
        // classpath is "-classpath <classpath>" -> 2 args
        Path fullClasspath = classpath != null ? classpath.concatSystemClasspath( "ignore" ) : null;
        if( fullClasspath != null && fullClasspath.toString().trim().length() > 0 )
        {
            size += 2;
        }
        // jar execution requires an additional -jar option
        if( executeJar )
        {
            size++;
        }
        return size;
    }

    public String toString()
    {
        try
        {
            return Commandline.toString( getCommandline() );
        }
        catch( TaskException e )
        {
            return e.toString();
        }
    }

    private Commandline getActualVMCommand()
    {
        Commandline actualVMCommand = (Commandline)vmCommand.clone();
        if( maxMemory != null )
        {
            if( vmVersion.startsWith( "1.1" ) )
            {
                actualVMCommand.createArgument().setValue( "-mx" + maxMemory );
            }
            else
            {
                actualVMCommand.createArgument().setValue( "-Xmx" + maxMemory );
            }
        }
        return actualVMCommand;
    }

    private String getJavaExecutableName()
    {
        // This is the most common extension case - exe for windows and OS/2,
        // nothing for *nix.
        String extension = Os.isFamily( "dos" ) ? ".exe" : "";

        // Look for java in the java.home/../bin directory.  Unfortunately
        // on Windows java.home doesn't always refer to the correct location,
        // so we need to fall back to assuming java is somewhere on the
        // PATH.
        java.io.File jExecutable =
            new java.io.File( System.getProperty( "java.home" ) +
                              "/../bin/java" + extension );

        if( jExecutable.exists() && !Os.isFamily( "netware" ) )
        {
            // NetWare may have a "java" in that directory, but 99% of
            // the time, you don't want to execute it -- Jeff Tulley
            // <JTULLEY@novell.com>
            return jExecutable.getAbsolutePath();
        }
        else
        {
            return "java";
        }
    }

    /**
     * Specialized EnvironmentData class for System properties
     *
     * @author RT
     */
    public static class SysProperties extends EnvironmentData implements Cloneable
    {
        Properties sys = null;

        public void setSystem()
            throws TaskException
        {
            try
            {
                Properties p = new Properties( sys = System.getProperties() );

                for( Iterator e = m_variables.iterator(); e.hasNext(); )
                {
                    EnvironmentVariable v = (EnvironmentVariable)e.next();
                    p.put( v.getKey(), v.getValue() );
                }
                System.setProperties( p );
            }
            catch( SecurityException e )
            {
                throw new TaskException( "Cannot modify system properties", e );
            }
        }

        public String[] getJavaVariables()
            throws TaskException
        {
            String props[] = new String[ 0 ];
            try
            {
                props = Environment.toNativeFormat( super.getVariables() );
            }
            catch( final ExecException ee )
            {
                throw new TaskException( ee.getMessage(), ee );
            }

            if( props == null )
                return null;

            for( int i = 0; i < props.length; i++ )
            {
                props[ i ] = "-D" + props[ i ];
            }
            return props;
        }

        public Object clone()
        {
            try
            {
                SysProperties c = (SysProperties)super.clone();
                c.m_variables.addAll( (ArrayList)m_variables.clone() );
                return c;
            }
            catch( CloneNotSupportedException e )
            {
                return null;
            }
        }

        public void restoreSystem()
            throws TaskException
        {
            if( sys == null )
                throw new TaskException( "Unbalanced nesting of SysProperties" );

            try
            {
                System.setProperties( sys );
                sys = null;
            }
            catch( SecurityException e )
            {
                throw new TaskException( "Cannot modify system properties", e );
            }
        }

        public int size()
        {
            return m_variables.size();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15793.java