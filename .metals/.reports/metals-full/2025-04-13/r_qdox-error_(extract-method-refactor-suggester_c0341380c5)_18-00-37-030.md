error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8162.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8162.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8162.java
text:
```scala
private final static H@@ashMap c_levels = new HashMap();

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.myrmidon.framework;

import java.util.HashMap;
import java.util.Set;
import org.apache.avalon.framework.Enum;
import org.apache.avalon.framework.logger.Logger;

/**
 * Type safe Enum for Log Levels and utility method
 * for using enum to write to logger.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 */
public final class LogLevel
    extends Enum
{
    //Map for all the levels
    private static final HashMap c_levels = new HashMap();

    //standard enums for version of JVM
    public final static LogLevel FATAL_ERROR = new LogLevel( "fatalError" );
    public final static LogLevel ERROR = new LogLevel( "error" );
    public final static LogLevel WARN = new LogLevel( "warn" );
    public final static LogLevel INFO = new LogLevel( "info" );
    public final static LogLevel DEBUG = new LogLevel( "debug" );

    /**
     * Retrieve the log level for the specified name.
     *
     * @param name the name of the LogLevel object to retrieve
     * @returns The LogLevel for specified name or null
     */
    public static LogLevel getByName( final String name )
    {
        return (LogLevel)c_levels.get( name );
    }

    /**
     * Retrieve the names of all the LogLevels.
     *
     * @returns The names of all the LogLevels
     */
    public static String[] getNames()
    {
        final Set keys = c_levels.keySet();
        return (String[])keys.toArray( new String[ keys.size() ] );
    }

    /**
     * Log a message to the Logger at the specified level.
     */
    public static void log( final Logger logger,
                            final String message,
                            final LogLevel level )
    {
        if( LogLevel.FATAL_ERROR == level )
        {
            logger.fatalError( message );
        }
        else if( LogLevel.ERROR == level )
        {
            logger.error( message );
        }
        else if( LogLevel.WARN == level )
        {
            logger.warn( message );
        }
        else if( LogLevel.INFO == level )
        {
            logger.info( message );
        }
        else
        {
            logger.debug( message );
        }
    }

    /**
     * Log a message to the Logger at the specified level.
     */
    public static void log( final Logger logger,
                            final String message,
                            final Throwable throwable,
                            final LogLevel level )
    {
        if( LogLevel.FATAL_ERROR == level )
        {
            logger.fatalError( message, throwable );
        }
        else if( LogLevel.ERROR == level )
        {
            logger.error( message, throwable );
        }
        else if( LogLevel.WARN == level )
        {
            logger.warn( message, throwable );
        }
        else if( LogLevel.INFO == level )
        {
            logger.info( message, throwable );
        }
        else
        {
            logger.debug( message, throwable );
        }
    }

    /**
     * Private constructor so no instance except here can be defined.
     *
     * @param name the name of Log Level
     */
    private LogLevel( final String name )
    {
        super( name, c_levels );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8162.java