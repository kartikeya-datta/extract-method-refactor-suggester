error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/871.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/871.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,20]

error in qdox parser
file content:
```java
offset: 20
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/871.java
text:
```scala
protected abstract S@@tring getWriteFolderURI() throws Exception;

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.aut.vfs;

import java.io.Writer;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * File system test that check that a file system can be modified.
 *
 * @author <a href="mailto:adammurdoch@apache.org">Adam Murdoch</a>
 */
public abstract class AbstractWritableFileSystemTest extends AbstractFileSystemTest
{
    public AbstractWritableFileSystemTest( String name )
    {
        super( name );
    }

    /**
     * Returns the URI for the area to do tests in.
     */
    protected abstract String getWriteFolderURI();

    /**
     * Sets up a scratch folder for the test to use.
     */
    protected FileObject createScratchFolder() throws Exception
    {
        FileObject scratchFolder = m_manager.resolveFile( getWriteFolderURI() );

        // Make sure the test folder is empty
        scratchFolder.delete();
        scratchFolder.create( FileType.FOLDER );

        return scratchFolder;
    }

    /**
     * Tests folder creation.
     */
    public void testFolderCreate() throws Exception
    {
        FileObject scratchFolder = createScratchFolder();

        // Create direct child of the test folder
        FileObject folder = scratchFolder.resolveFile( "dir1" );
        assertTrue( !folder.exists() );
        folder.create( FileType.FOLDER );
        assertTrue( folder.exists() );
        assertEquals( 0, folder.getChildren().length );

        // Create a descendant, where the intermediate folders don't exist
        folder = scratchFolder.resolveFile( "dir2/dir1/dir1" );
        assertTrue( !folder.exists() );
        assertTrue( !folder.getParent().exists() );
        assertTrue( !folder.getParent().getParent().exists() );
        folder.create( FileType.FOLDER );
        assertTrue( folder.exists() );
        assertEquals( 0, folder.getChildren().length );
        assertTrue( folder.getParent().exists() );
        assertTrue( folder.getParent().getParent().exists() );

        // Test creating a folder that already exists
        folder.create( FileType.FOLDER );
    }

    /**
     * Tests file creation
     */
    public void testFileCreate() throws Exception
    {
        FileObject scratchFolder = createScratchFolder();

        // Create direct child of the test folder
        FileObject file = scratchFolder.resolveFile( "file1.txt" );
        assertTrue( !file.exists() );
        file.create( FileType.FILE );
        assertTrue( file.exists() );
        assertEquals( 0, file.getContent().getSize() );

        // Create a descendant, where the intermediate folders don't exist
        file = scratchFolder.resolveFile( "dir1/dir1/file1.txt" );
        assertTrue( !file.exists() );
        assertTrue( !file.getParent().exists() );
        assertTrue( !file.getParent().getParent().exists() );
        file.create( FileType.FILE );
        assertTrue( file.exists() );
        assertEquals( 0, file.getContent().getSize() );
        assertTrue( file.getParent().exists() );
        assertTrue( file.getParent().getParent().exists() );

        // Test creating a file that already exists
        file.create( FileType.FILE );
    }

    /**
     * Tests file/folder creation with mismatched types.
     */
    public void testFileCreateMismatched() throws Exception
    {
        FileObject scratchFolder = createScratchFolder();

        // Create a test file and folder
        FileObject file = scratchFolder.resolveFile( "dir1/file1.txt" );
        file.create( FileType.FILE );
        assertEquals( FileType.FILE, file.getType() );

        FileObject folder = scratchFolder.resolveFile( "dir1/dir2" );
        folder.create( FileType.FOLDER );
        assertEquals( FileType.FOLDER, folder.getType() );

        // Attempt to create a file that already exists as a folder
        try
        {
            folder.create( FileType.FILE );
            assertTrue( false );
        }
        catch( FileSystemException exc )
        {
        }

        // Attempt to create a folder that already exists as a file
        try
        {
            file.create( FileType.FOLDER );
            assertTrue( false );
        }
        catch( FileSystemException exc )
        {
        }

        // Attempt to create a folder as a child of a file
        FileObject folder2 = file.resolveFile( "some-child" );
        try
        {
            folder2.create( FileType.FOLDER );
            assertTrue( false );
        }
        catch( FileSystemException exc )
        {
        }
    }

    /**
     * Tests deletion
     */
    public void testDelete() throws Exception
    {
        // Set-up the test structure
        FileObject folder = createScratchFolder();
        folder.resolveFile( "file1.txt" ).create( FileType.FILE );
        folder.resolveFile( "emptydir" ).create( FileType.FOLDER );
        folder.resolveFile( "dir1/file1.txt" ).create( FileType.FILE );
        folder.resolveFile( "dir1/dir2/file2.txt" ).create( FileType.FILE );

        // Delete a file
        FileObject file = folder.resolveFile( "file1.txt" );
        assertTrue( file.exists() );
        file.delete();
        assertTrue( !file.exists() );

        // Delete an empty folder
        file = folder.resolveFile( "emptydir" );
        assertTrue( file.exists() );
        file.delete();
        assertTrue( !file.exists() );

        // Recursive delete
        file = folder.resolveFile( "dir1" );
        FileObject file2 = file.resolveFile( "dir2/file2.txt" );
        assertTrue( file.exists() );
        assertTrue( file2.exists() );
        file.delete();
        assertTrue( !file.exists() );
        assertTrue( !file2.exists() );

        // Delete a file that does not exist
        file = folder.resolveFile( "some-folder/some-file" );
        assertTrue( !file.exists() );
        file.delete();
        assertTrue( !file.exists() );
    }

    /**
     * Test that children are handled correctly by create and delete.
     */
    public void testListChildren() throws Exception
    {
        FileObject folder = createScratchFolder();
        HashSet names = new HashSet();

        // Make sure the folder is empty
        assertEquals( 0, folder.getChildren().length );

        // Create a child folder
        folder.resolveFile( "dir1" ).create( FileType.FOLDER );
        names.add( "dir1" );
        assertSameFileSet( names, folder.getChildren() );

        // Create a child file
        folder.resolveFile( "file1.html" ).create( FileType.FILE );
        names.add( "file1.html" );
        assertSameFileSet( names, folder.getChildren() );

        // Create a descendent
        folder.resolveFile( "dir2/file1.txt" ).create( FileType.FILE );
        names.add( "dir2" );
        assertSameFileSet( names, folder.getChildren() );

        // Create a child file via an output stream
        OutputStream outstr = folder.resolveFile( "file2.txt" ).getContent().getOutputStream();
        outstr.close();
        names.add( "file2.txt" );
        assertSameFileSet( names, folder.getChildren() );

        // Delete a child folder
        folder.resolveFile( "dir1" ).delete();
        names.remove( "dir1" );
        assertSameFileSet( names, folder.getChildren() );

        // Delete a child file
        folder.resolveFile( "file1.html" ).delete();
        names.remove( "file1.html" );
        assertSameFileSet( names, folder.getChildren() );

        // Recreate the folder
        folder.delete();
        folder.create( FileType.FOLDER );
        assertEquals( 0, folder.getChildren().length );
    }

    /**
     * Ensures the names of a set of files match an expected set.
     */
    private void assertSameFileSet( Set names, FileObject[] files )
    {
        // Make sure the sets are the same length
        assertEquals( names.size(), files.length );

        // Check for unexpected names
        for( int i = 0; i < files.length; i++ )
        {
            FileObject file = files[ i ];
            assertTrue( names.contains( file.getName().getBaseName() ) );
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/871.java