error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15623.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15623.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15623.java
text:
```scala
final C@@lass clazz = classLoader.loadClass( "org.apache.myrmidon.frontends.CLIMain" );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.myrmidon.launcher;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Basic Loader that is responsible for all the hackery to get classloader to work.
 *
 * @author <a href="mailto:donaldp@apache.org">Peter Donald</a>
 */
public final class Main
{
    /**
     * Magic entry point.
     *
     * @param args the CLI arguments
     * @exception Exception if an error occurs
     */
    public final static void main( final String[] args )
        throws Exception
    {
        try
        {
            //actually try to discover the install directory based on where
            // the myrmidon.jar is
            final File installDirectory = findInstallDir();
            System.setProperty( "myrmidon.home", installDirectory.toString() );

            //setup classloader appropriately for myrmidon jar
            final File libDir = new File( installDirectory, "lib" );
            final URL[] urls = buildURLList( libDir );

            final URLClassLoader classLoader = new URLClassLoader( urls );

            //load class and retrieve appropriate main method.
            final Class clazz = classLoader.loadClass( "org.apache.myrmidon.Main" );
            final Method method = clazz.getMethod( "main", new Class[] { args.getClass() } );

            Thread.currentThread().setContextClassLoader( classLoader );

            //kick the tires and light the fires....
            method.invoke( null, new Object[] { args } );
        }
        catch( final InvocationTargetException ite )
        {
            System.err.println( "Error: " + ite.getTargetException().getMessage() );
            ite.getTargetException().printStackTrace();
        }
        catch( final Throwable throwable )
        {
            System.err.println( "Error: " + throwable.getMessage() );
            throwable.printStackTrace();
        }
    }

    private final static URL[] buildURLList( final File dir )
        throws Exception
    {
        final ArrayList urlList = new ArrayList();

        final File[] contents = dir.listFiles();

        if( null == contents )
        {
            return new URL[ 0 ];
        }

        for( int i = 0; i < contents.length; i++ )
        {
            final File file = contents[ i ];

            if( !file.isFile() || !file.canRead() )
            {
                //ignore non-files or unreadable files
                continue;
            }

            final String name = file.getName();
            if( !name.endsWith( ".jar" ) && !name.endsWith( ".zip" ) )
            {
                //Ifnore files in lib dir that are not jars or zips
                continue;
            }

            urlList.add( file.toURL() );
        }

        return (URL[])urlList.toArray( new URL[ 0 ] );
    }

    /**
     *  Finds the myrmidon.jar file in the classpath.
     */
    private final static File findInstallDir()
        throws Exception
    {
        final String classpath = System.getProperty( "java.class.path" );
        final String pathSeparator = System.getProperty( "path.separator" );
        final StringTokenizer tokenizer = new StringTokenizer( classpath, pathSeparator );

        while( tokenizer.hasMoreTokens() )
        {
            final String element = tokenizer.nextToken();

            if( element.endsWith( "ant.jar" ) )
            {
                File file = (new File( element )).getAbsoluteFile();
                file = file.getParentFile();

                if( null != file )
                {
                    file = file.getParentFile();
                }

                return file;
            }
        }

        throw new Exception( "Unable to locate ant.jar in classpath" );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15623.java