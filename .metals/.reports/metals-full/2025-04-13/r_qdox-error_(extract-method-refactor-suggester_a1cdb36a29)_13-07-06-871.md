error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12984.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12984.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12984.java
text:
```scala
protected F@@ile getInstallDirectory()

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included  with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.myrmidon;

import java.io.File;
import java.io.IOException;
import junit.framework.TestCase;
import org.apache.avalon.excalibur.i18n.Resources;
import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.framework.ExceptionUtil;
import org.apache.avalon.framework.logger.Logger;
import org.apache.myrmidon.frontends.BasicLogger;

/**
 * A base class for Myrmidon tests.  Provides utility methods for locating
 * test resources.
 *
 * @author <a href="mailto:adammurdoch@apache.org">Adam Murdoch</a>
 */
public abstract class AbstractMyrmidonTest
    extends TestCase
{
    private final File m_testBaseDir;
    private final File m_baseDir;
    private Logger m_logger;

    protected static final Resources getResourcesForTested( final Class clazz )
    {
        final Package pkg = clazz.getPackage();

        String baseName;
        if( null == pkg )
        {
            final String name = clazz.getName();
            if( -1 == name.lastIndexOf( "." ) )
            {
                baseName = "";
            }
            else
            {
                baseName = name.substring( 0, name.lastIndexOf( "." ) );
            }
        }
        else
        {
            baseName = pkg.getName();
        }

        if( baseName.endsWith( ".test" ) )
        {
            baseName = baseName.substring( 0, baseName.length() - 5 );
        }

        return ResourceManager.getBaseResources( baseName + ".Resources", AbstractMyrmidonTest.class.getClassLoader() );
    }

    public AbstractMyrmidonTest( String name )
    {
        super( name );
        final String baseDirProp = System.getProperty( "test.basedir" );
        m_baseDir = getCanonicalFile( new File( baseDirProp ) );
        String packagePath = getClass().getName();
        int idx = packagePath.lastIndexOf( '.' );
        packagePath = packagePath.substring( 0, idx );
        packagePath = packagePath.replace( '.', File.separatorChar );
        m_testBaseDir = getCanonicalFile( new File( m_baseDir, packagePath ) );
    }

    /**
     * Locates a test resource, and asserts that the resource exists
     *
     * @param name path of the resource, relative to this test's base directory.
     */
    protected File getTestResource( final String name )
    {
        return getTestResource( name, true );
    }

    /**
     * Locates a test resource.
     *
     * @param name path of the resource, relative to this test's base directory.
     */
    protected File getTestResource( final String name, final boolean mustExist )
    {
        File file = new File( m_testBaseDir, name );
        file = getCanonicalFile( file );
        if( mustExist )
        {
            assertTrue( "Test file \"" + file + "\" does not exist.", file.exists() );
        }
        else
        {
            assertTrue( "Test file \"" + file + "\" should not exist.", !file.exists() );
        }

        return file;
    }

    /**
     * Locates the base directory for this test.
     */
    protected File getTestDirectory()
    {
        return m_testBaseDir;
    }

    /**
     * Locates a test directory, creating it if it does not exist.
     *
     * @param name path of the directory, relative to this test's base directory.
     */
    protected File getTestDirectory( final String name )
    {
        File file = new File( m_testBaseDir, name );
        file = getCanonicalFile( file );
        assertTrue( "Test directory \"" + file + "\" does not exist or is not a directory.",
                    file.isDirectory() || file.mkdirs() );
        return file;
    }

    /**
     * Returns the directory containing a Myrmidon install.
     */
    protected File getHomeDirectory()
    {
        final File file = new File( m_baseDir, "dist" );
        return getCanonicalFile( file );
    }

    /**
     * Makes a file canonical
     */
    private File getCanonicalFile( final File file )
    {
        try
        {
            return file.getCanonicalFile();
        }
        catch( IOException e )
        {
            return file.getAbsoluteFile();
        }
    }

    /**
     * Creates a logger.
     */
    protected Logger getLogger()
    {
        if( m_logger == null )
        {
            m_logger = new BasicLogger( "[test]", BasicLogger.LEVEL_WARN );
        }
        return m_logger;
    }

    /**
     * Asserts that an exception chain contains the expected messages.
     *
     * @param messages The messages, in order.  A null entry in this array
     *                 indicates that the message should be ignored.
     */
    protected void assertSameMessage( final String[] messages, final Throwable throwable )
    {
        //System.out.println( "exception:" );
        //for( Throwable t = throwable; t != null; t = ExceptionUtil.getCause( t, true ) )
        //{
        //    System.out.println( "  " + t.getMessage() );
        //}

        Throwable current = throwable;
        for( int i = 0; i < messages.length; i++ )
        {
            String message = messages[ i ];
            assertNotNull( current );
            if( message != null )
            {
                assertEquals( message, current.getMessage() );
            }

            // Get the next exception in the chain
            current = ExceptionUtil.getCause( current, true );
        }
    }

    /**
     * Asserts that an exception contains the expected message.
     */
    protected void assertSameMessage( final String message, final Throwable throwable )
    {
        assertSameMessage( new String[]{message}, throwable );
    }

    /**
     * Compares 2 objects for equality, nulls are equal.  Used by the test
     * classes' equals() methods.
     */
    public static boolean equals( final Object o1, final Object o2 )
    {
        if( o1 == null && o2 == null )
        {
            return true;
        }
        if( o1 == null || o2 == null )
        {
            return false;
        }
        return o1.equals( o2 );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12984.java