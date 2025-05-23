error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6820.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6820.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6820.java
text:
```scala
t@@hrow new IOException(ex.getMessage());

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.commons.compress;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;

public abstract class AbstractTestCase extends TestCase {

    protected File dir;

    protected void setUp() throws Exception {
        dir = File.createTempFile("dir", "");
        dir.delete();
        dir.mkdir();

        addURL(new File("src/test/resources").toURL());
    }

    protected File getFile(String path) throws IOException {
        URL url = getClass().getResource(path);
        if (url == null) {
            throw new java.io.FileNotFoundException(path + " doesn't exist");
        }
        try {
            return new File(new URI(url.toString()));
        } catch (java.net.URISyntaxException ex) {
            // impossible since URL.toString() should always yield a valid URI
            throw new IOException(ex.getMessage(), ex);
        }
    }

    protected void tearDown() throws Exception {
        dir.delete();
        dir = null;
    }

    /**
     * Adds a URL to the classpath. This method is necessary when running junit
     * tests from within eclipse.
     * 
     * @param url
     *            the url to add
     * @throws Exception
     *             if an error occurs
     */
    public void addURL(URL url) throws Exception {
        URLClassLoader classLoader = (URLClassLoader) ClassLoader
            .getSystemClassLoader();
        Class clazz = URLClassLoader.class;

        Method method = clazz.getDeclaredMethod("addURL",
                                                new Class[] { URL.class });
        method.setAccessible(true);
        method.invoke(classLoader, new Object[] { url });
    }

    /**
     * Creates an archive of 5 textbased files in several directories. The
     * archivername is the factory identifier for the archiver, for example zip,
     * tar, cpio, jar, ar. The archive is created as a temp file.
     * 
     * The archive contains the following files:
     * <ul>
     * <li>testdata/test1.xml</li>
     * <li>testdata/test2.xml</li>
     * <li>test/test3.xml</li>
     * <li>bla/test4.xml</li>
     * <li>test.txt</li>
     * <li>something/bla</li>
     * <li>test with spaces.txt</li>
     * </ul>
     * 
     * @param archivename
     *            the identifier of this archive
     * @return the newly created file
     * @throws Exception
     *             in case something goes wrong
     */
    protected File createArchive(String archivename) throws Exception {
        ArchiveOutputStream out = null;
        try {
            File temp = File.createTempFile("test", "." + archivename);

            final OutputStream stream = new FileOutputStream(temp);
            out = new ArchiveStreamFactory().createArchiveOutputStream(
                                                                       archivename, stream);

            final File file1 = getFile("test1.xml");
            final File file2 = getFile("test2.xml");
            final File file3 = getFile("test3.xml");
            final File file4 = getFile("test4.xml");
            final File file5 = getFile("test.txt");
            final File file6 = getFile("test with spaces.txt");

            ZipArchiveEntry entry = new ZipArchiveEntry("testdata/test1.xml");
            entry.setSize(file1.length());
            out.putArchiveEntry(entry);
            IOUtils.copy(new FileInputStream(file1), out);
            out.closeArchiveEntry();

            entry = new ZipArchiveEntry("testdata/test2.xml");
            entry.setSize(file1.length());
            out.putArchiveEntry(entry);
            IOUtils.copy(new FileInputStream(file2), out);
            out.closeArchiveEntry();

            entry = new ZipArchiveEntry("test/test3.xml");
            entry.setSize(file1.length());
            out.putArchiveEntry(entry);
            IOUtils.copy(new FileInputStream(file3), out);
            out.closeArchiveEntry();

            entry = new ZipArchiveEntry("bla/test4.xml");
            entry.setSize(file1.length());
            out.putArchiveEntry(entry);
            IOUtils.copy(new FileInputStream(file4), out);
            out.closeArchiveEntry();

            entry = new ZipArchiveEntry("bla/test5.xml");
            entry.setSize(file1.length());
            out.putArchiveEntry(entry);
            IOUtils.copy(new FileInputStream(file4), out);
            out.closeArchiveEntry();

            entry = new ZipArchiveEntry("bla/blubber/test6.xml");
            entry.setSize(file1.length());
            out.putArchiveEntry(entry);
            IOUtils.copy(new FileInputStream(file4), out);
            out.closeArchiveEntry();

            entry = new ZipArchiveEntry("test.txt");
            entry.setSize(file1.length());
            out.putArchiveEntry(entry);
            IOUtils.copy(new FileInputStream(file5), out);
            out.closeArchiveEntry();

            entry = new ZipArchiveEntry("something/bla");
            entry.setSize(file1.length());
            out.putArchiveEntry(entry);
            IOUtils.copy(new FileInputStream(file6), out);
            out.closeArchiveEntry();

            entry = new ZipArchiveEntry("test with spaces.txt");
            entry.setSize(file1.length());
            out.putArchiveEntry(entry);
            IOUtils.copy(new FileInputStream(file6), out);
            out.closeArchiveEntry();

            return temp;
        } finally {
            if (out != null)
                out.close();
        }
    }

    /**
     * Checks if an archive contains all expected files.
     * 
     * @param archive
     *            the archive to check
     * @param expected
     *            a list with expected string filenames
     * @throws Exception
     */
    protected void checkArchiveContent(File archive, List expected)
        throws Exception {
        final InputStream is = new FileInputStream(archive);
        final BufferedInputStream buf = new BufferedInputStream(is);
        final ArchiveInputStream in = new ArchiveStreamFactory()
            .createArchiveInputStream(buf);
        this.checkArchiveContent(in, expected);
    }

    protected void checkArchiveContent(ArchiveInputStream in, List expected)
        throws Exception {
        File result = File.createTempFile("dir-result", "");
        result.delete();
        result.mkdir();

        ArchiveEntry entry = null;
        while ((entry = in.getNextEntry()) != null) {
            File outfile = new File(result.getCanonicalPath() + "/result/"
                                    + entry.getName());
            outfile.getParentFile().mkdirs();
            OutputStream out = new FileOutputStream(outfile);
            IOUtils.copy(in, out);
            out.close();

            if (!outfile.exists()) {
                fail("extraction failed: " + entry.getName());
            }
            if (!expected.remove(entry.getName())) {
                fail("unexpected entry: " + entry.getName());
            }
        }
        in.close();
        if (expected.size() > 0) {
            for (Iterator iterator = expected.iterator(); iterator.hasNext();) {
                String name = (String) iterator.next();
                fail("Expected entry: " + name);
            }
        }
        assertEquals(0, expected.size());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6820.java