error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/477.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/477.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,25]

error in qdox parser
file content:
```java
offset: 25
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/477.java
text:
```scala
private static volatile F@@ile testDir;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.io.testtools;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 * Base class for testcases doing tests with files.
 * 
 * @author Jeremias Maerki
 * @author Gareth Davis
 */
public abstract class FileBasedTestCase extends TestCase {

    private static File testDir;

    public FileBasedTestCase(String name) {
        super(name);
    }
    
    public static File getTestDirectory() {
        if (testDir == null) {
            testDir = (new File("test/io/")).getAbsoluteFile();
        }
        testDir.mkdirs();
        return testDir;
    }
    
    protected void createFile(File file, long size)
            throws IOException {
        if (!file.getParentFile().exists()) {
            throw new IOException("Cannot create file " + file 
                + " as the parent directory does not exist");
        }
        BufferedOutputStream output =
            new BufferedOutputStream(new java.io.FileOutputStream(file));
        try {
            generateTestData(output, size);
        } finally {
            IOUtils.closeQuietly(output);
        }
    }
    
    protected byte[] generateTestData(long size) {
        try {
            ByteArrayOutputStream baout = new ByteArrayOutputStream();
            generateTestData(baout, size);
            return baout.toByteArray();
        } catch (IOException ioe) {
            throw new RuntimeException("This should never happen: " + ioe.getMessage());
        }
    }
    
    protected void generateTestData(OutputStream out, long size) 
                throws IOException {
        for (int i = 0; i < size; i++) {
            //output.write((byte)'X');

            // nice varied byte pattern compatible with Readers and Writers
            out.write( (byte)( (i % 127) + 1) );
        }
    }

    protected void createLineBasedFile(File file, String[] data) throws IOException {
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            throw new IOException("Cannot create file " + file + " as the parent directory does not exist");
        }
        PrintWriter output = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
        try {
            for (int i = 0; i < data.length; i++) {
                output.println(data[i]);
            }
        } finally {
            IOUtils.closeQuietly(output);
        }
    }

    protected File newFile(String filename) throws IOException {
        File destination = new File( getTestDirectory(), filename );
        /*
        assertTrue( filename + "Test output data file shouldn't previously exist",
                    !destination.exists() );
        */
        if (destination.exists()) {
            FileUtils.forceDelete(destination);
        }
        return destination;
    }

    protected void checkFile( File file, File referenceFile )
                throws Exception {
        assertTrue( "Check existence of output file", file.exists() );
        assertEqualContent( referenceFile, file );
    }

    /** Assert that the content of two files is the same. */
    private void assertEqualContent( File f0, File f1 )
        throws IOException
    {
        /* This doesn't work because the filesize isn't updated until the file
         * is closed.
        assertTrue( "The files " + f0 + " and " + f1 +
                    " have differing file sizes (" + f0.length() +
                    " vs " + f1.length() + ")", ( f0.length() == f1.length() ) );
        */
        InputStream is0 = new java.io.FileInputStream( f0 );
        try {
            InputStream is1 = new java.io.FileInputStream( f1 );
            try {
                byte[] buf0 = new byte[ 1024 ];
                byte[] buf1 = new byte[ 1024 ];
                int n0 = 0;
                int n1 = 0;

                while( -1 != n0 )
                {
                    n0 = is0.read( buf0 );
                    n1 = is1.read( buf1 );
                    assertTrue( "The files " + f0 + " and " + f1 +
                                " have differing number of bytes available (" + n0 +
                                " vs " + n1 + ")", ( n0 == n1 ) );

                    assertTrue( "The files " + f0 + " and " + f1 +
                                " have different content", Arrays.equals( buf0, buf1 ) );
                }
            } finally {
                is1.close();
            }
        } finally {
            is0.close();
        }
    }

    /** Assert that the content of a file is equal to that in a byte[]. */
    protected void assertEqualContent(byte[] b0, File file) throws IOException {
        InputStream is = new java.io.FileInputStream(file);
        int count = 0, numRead = 0;
        byte[] b1 = new byte[b0.length];
        try {
            while (count < b0.length && numRead >= 0) {
                numRead = is.read(b1, count, b0.length);
                count += numRead;
            }
            assertEquals("Different number of bytes: ", b0.length, count);
            for (int i = 0; i < count; i++) {
                assertEquals("byte " + i + " differs", b0[i], b1[i]);
            }
        } finally {
            is.close();
        }
    }

    /** Assert that the content of a file is equal to that in a char[]. */
    protected void assertEqualContent(char[] c0, File file) throws IOException {
        Reader ir = new java.io.FileReader(file);
        int count = 0, numRead = 0;
        char[] c1 = new char[c0.length];
        try {
            while (count < c0.length && numRead >= 0) {
                numRead = ir.read(c1, count, c0.length);
                count += numRead;
            }
            assertEquals("Different number of chars: ", c0.length, count);
            for (int i = 0; i < count; i++) {
                assertEquals("char " + i + " differs", c0[i], c1[i]);
            }
        } finally {
            ir.close();
        }
    }

    protected void checkWrite(OutputStream output) throws Exception {
        try {
            new java.io.PrintStream(output).write(0);
        } catch (Throwable t) {
            throw new AssertionFailedError(
                "The copy() method closed the stream "
                    + "when it shouldn't have. "
                    + t.getMessage());
        }
    }

    protected void checkWrite(Writer output) throws Exception {
        try {
            new java.io.PrintWriter(output).write('a');
        } catch (Throwable t) {
            throw new AssertionFailedError(
                "The copy() method closed the stream "
                    + "when it shouldn't have. "
                    + t.getMessage());
        }
    }

    protected void deleteFile( File file )
        throws Exception {
        if (file.exists()) {
            assertTrue("Couldn't delete file: " + file, file.delete());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/477.java