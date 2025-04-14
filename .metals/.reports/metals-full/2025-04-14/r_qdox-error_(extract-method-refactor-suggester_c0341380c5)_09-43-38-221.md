error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16080.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16080.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16080.java
text:
```scala
a@@ddExtdirs( cp );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.tools.ant.taskdefs.compilers;

import java.lang.reflect.Method;
import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;

/**
 * The implementation of the Java compiler for KJC. This is primarily a
 * cut-and-paste from Jikes.java and DefaultCompilerAdapter.
 *
 * @author <a href="mailto:tora@debian.org">Takashi Okamoto</a> +
 */
public class Kjc extends DefaultCompilerAdapter
{

    public boolean execute()
        throws TaskException
    {
        getLogger().debug( "Using kjc compiler" );
        Commandline cmd = setupKjcCommand();

        try
        {
            Class c = Class.forName( "at.dms.kjc.Main" );

            // Call the compile() method
            Method compile = c.getMethod( "compile",
                                          new Class[]{String[].class} );
            Boolean ok = (Boolean)compile.invoke( null,
                                                  new Object[]{cmd.getArguments()} );
            return ok.booleanValue();
        }
        catch( ClassNotFoundException ex )
        {
            throw new TaskException( "Cannot use kjc compiler, as it is not available" +
                                     " A common solution is to set the environment variable" +
                                     " CLASSPATH to your kjc archive (kjc.jar)." );
        }
        catch( Exception ex )
        {
            if( ex instanceof TaskException )
            {
                throw (TaskException)ex;
            }
            else
            {
                throw new TaskException( "Error starting kjc compiler: ", ex );
            }
        }
    }

    /**
     * setup kjc command arguments.
     *
     * @return Description of the Returned Value
     */
    protected Commandline setupKjcCommand()
        throws TaskException
    {
        Commandline cmd = new Commandline();

        // generate classpath, because kjc does't support sourcepath.
        Path classpath = getCompileClasspath();

        if( m_deprecation == true )
        {
            cmd.createArgument().setValue( "-deprecation" );
        }

        if( m_destDir != null )
        {
            cmd.createArgument().setValue( "-d" );
            cmd.createArgument().setFile( m_destDir );
        }

        // generate the clsspath
        cmd.createArgument().setValue( "-classpath" );

        Path cp = new Path();

        // kjc don't have bootclasspath option.
        if( m_bootclasspath != null )
        {
            cp.append( m_bootclasspath );
        }

        if( m_extdirs != null )
        {
            cp.addExtdirs( m_extdirs );
        }

        cp.append( classpath );
        cp.append( src );

        cmd.createArgument().setPath( cp );

        // kjc-1.5A doesn't support -encoding option now.
        // but it will be supported near the feature.
        if( m_encoding != null )
        {
            cmd.createArgument().setValue( "-encoding" );
            cmd.createArgument().setValue( m_encoding );
        }

        if( m_debug )
        {
            cmd.createArgument().setValue( "-g" );
        }

        if( m_optimize )
        {
            cmd.createArgument().setValue( "-O2" );
        }

        if( m_verbose )
        {
            cmd.createArgument().setValue( "-verbose" );
        }

        addCurrentCompilerArgs( cmd );

        logAndAddFilesToCompile( cmd );
        return cmd;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16080.java