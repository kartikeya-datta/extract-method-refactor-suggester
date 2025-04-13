error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12785.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12785.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12785.java
text:
```scala
final S@@tring filename = "src" + File.separator + "test" + File.separator +

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included  with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.aut.bzip2.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import junit.framework.TestCase;
import org.apache.avalon.excalibur.io.FileUtil;
import org.apache.avalon.excalibur.io.IOUtil;
import org.apache.aut.bzip2.CBZip2OutputStream;
import org.apache.aut.bzip2.CBZip2InputStream;

/**
 * A test the stress tested the BZip implementation to verify
 * that it behaves correctly.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision$ $Date$
 */
public class BzipTestCase
    extends TestCase
{
    private final static byte[] HEADER = new byte[]{(byte)'B', (byte)'Z'};

    public BzipTestCase( final String name )
    {
        super( name );
    }

    public void testBzipOutputStream()
        throws Exception
    {
        final InputStream input = getInputStream( "asf-logo-huge.tar" );
        final File outputFile = getOutputFile( ".tar.bz2" );
        final OutputStream output = new FileOutputStream( outputFile );
        final CBZip2OutputStream packedOutput = getPackedOutput( output );
        IOUtil.copy( input, packedOutput );
        IOUtil.shutdownStream( input );
        IOUtil.shutdownStream( packedOutput );
        IOUtil.shutdownStream( output );
        compareContents( "asf-logo-huge.tar.bz2", outputFile );
        FileUtil.forceDelete( outputFile );
    }

    public void testBzipInputStream()
        throws Exception
    {
        final InputStream input = getInputStream( "asf-logo-huge.tar.bz2" );
        final File outputFile = getOutputFile( ".tar" );
        final OutputStream output = new FileOutputStream( outputFile );
        final CBZip2InputStream packedInput = getPackedInput( input );
        IOUtil.copy( packedInput, output );
        IOUtil.shutdownStream( input );
        IOUtil.shutdownStream( packedInput );
        IOUtil.shutdownStream( output );
        compareContents( "asf-logo-huge.tar", outputFile );
        FileUtil.forceDelete( outputFile );
    }

    private void compareContents( final String initial, final File generated )
        throws Exception
    {
        final InputStream input1 = getInputStream( initial );
        final InputStream input2 = new FileInputStream( generated );
        final boolean test = IOUtil.contentEquals( input1, input2 );
        IOUtil.shutdownStream( input1 );
        IOUtil.shutdownStream( input2 );
        assertTrue( "Contents of " + initial + " matches generated version " + generated, test );
    }

    private CBZip2InputStream getPackedInput( final InputStream input )
        throws IOException
    {
        final int b1 = input.read();
        final int b2 = input.read();
        assertEquals( "Equal header byte1", b1, 'B' );
        assertEquals( "Equal header byte2", b2, 'Z' );
        return new CBZip2InputStream( input );
    }

    private CBZip2OutputStream getPackedOutput( final OutputStream output )
        throws IOException
    {
        output.write( HEADER );
        return new CBZip2OutputStream( output );
    }

    private File getOutputFile( final String postfix )
        throws IOException
    {
        final File cwd = new File( "." );
        return File.createTempFile( "ant-test", postfix, cwd );
    }

    private InputStream getInputStream( final String resource )
        throws Exception
    {
        final String filename = "src" + File.separator + "testcases" + File.separator +
            getClass().getName().replace( '.', File.separatorChar );
        final String path = FileUtil.getPath( filename );
        final File input = new File( path, resource );
        return new FileInputStream( input );
        //final ClassLoader loader = getClass().getClassLoader();
        //return loader.getResourceAsStream( resource );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12785.java