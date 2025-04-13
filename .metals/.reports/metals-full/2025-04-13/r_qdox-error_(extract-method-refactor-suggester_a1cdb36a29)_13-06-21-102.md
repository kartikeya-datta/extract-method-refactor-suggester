error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5470.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5470.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,16]

error in qdox parser
file content:
```java
offset: 16
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5470.java
text:
```scala
private static S@@tring[] toNativeEnvironment( final Properties environment )

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included  with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.myrmidon.framework.exec.launchers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import org.apache.myrmidon.framework.exec.Environment;
import org.apache.myrmidon.framework.exec.ExecException;
import org.apache.myrmidon.framework.exec.ExecMetaData;

/**
 * A set of utility functions useful when writing CommandLaunchers.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision$ $Date$
 */
class ExecUtil
{
    /**
     * The file representing the current working directory.
     */
    private static final File c_cwd;

    static
    {
        try
        {
            c_cwd = ( new File( "." ) ).getCanonicalFile();
        }
        catch( final IOException ioe )
        {
            //Should never happen
            throw new IllegalStateException();
        }
    }

    /**
     * Private constructor to block instantiation.
     */
    private ExecUtil()
    {
    }

    /**
     * Create a new ExecMetaData representing the same command with the specified
     * prefix. This is useful when you are launching the native executable via a
     * script of some sort.
     */
    protected static ExecMetaData prepend( final ExecMetaData metaData,
                                           final String[] prefix )
    {
        final String[] original = metaData.getCommand();
        final String[] command = new String[ original.length + prefix.length ];

        System.arraycopy( prefix, 0, command, 0, prefix.length );
        System.arraycopy( original, 0, command, prefix.length, original.length );

        return new ExecMetaData( command,
                                 metaData.getEnvironment(),
                                 metaData.getWorkingDirectory(),
                                 metaData.isEnvironmentAdditive() );
    }

    /**
     * Utility method to check if specified file is equal
     * to the current working directory.
     */
    protected static boolean isCwd( final File file )
        throws IOException
    {
        return file.getCanonicalFile().equals( getCwd() );
    }

    protected static String[] toNativeEnvironment( final Properties environment )
        throws ExecException
    {
        if( null == environment )
        {
            return null;
        }
        else
        {
            final ArrayList newEnvironment = new ArrayList();

            final Iterator keys = environment.keySet().iterator();
            while( keys.hasNext() )
            {
                final String key = (String)keys.next();
                final String value = environment.getProperty( key );
                newEnvironment.add( key + '=' + value );
            }

            return (String[])newEnvironment.toArray( new String[ newEnvironment.size() ] );
        }
    }

    /**
     * Return the current working directory of the JVM.
     * This value is initialized when this class is first loaded.
     */
    protected static File getCwd()
    {
        return c_cwd;
    }

    /**
     * Get the native environment according to proper rules.
     * Return null if no environment specified, return environment combined
     * with native environment if environment data is additive else just return
     * converted environment data.
     */
    protected static String[] getEnvironmentSpec( final ExecMetaData metaData )
        throws ExecException, IOException
    {
        final Properties environment = metaData.getEnvironment();
        if( 0 == environment.size() )
        {
            return null;
        }
        else
        {
            if( metaData.isEnvironmentAdditive() )
            {
                final Properties newEnvironment = new Properties();
                newEnvironment.putAll( Environment.getNativeEnvironment() );
                newEnvironment.putAll( environment );
                return toNativeEnvironment( newEnvironment );
            }
            else
            {
                return toNativeEnvironment( environment );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5470.java