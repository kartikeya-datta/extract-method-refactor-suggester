error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/497.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/497.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/497.java
text:
```scala
r@@eturn result == 0;

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
package org.apache.commons.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.testtools.FileBasedTestCase;

/**
 * Test cases for FileUtils.cleanDirectory() method.
 *
 * @version $Id$
 */
public class FileUtilsCleanDirectoryTestCase extends FileBasedTestCase {
    final File top = getLocalTestDirectory();

    public FileUtilsCleanDirectoryTestCase(String name) {
        super(name);
    }

    private File getLocalTestDirectory() {
        return new File(getTestDirectory(), "list-files");
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        top.mkdirs();
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        chmod(top, 775, true);
        FileUtils.deleteDirectory(top);
    }

    //-----------------------------------------------------------------------
    public void testCleanEmpty() throws Exception {
        assertEquals(0, top.list().length);

        FileUtils.cleanDirectory(top);

        assertEquals(0, top.list().length);
    }

    public void testDeletesRegular() throws Exception {
        FileUtils.touch(new File(top, "regular"));
        FileUtils.touch(new File(top, ".hidden"));

        assertEquals(2, top.list().length);

        FileUtils.cleanDirectory(top);

        assertEquals(0, top.list().length);
    }

    public void testDeletesNested() throws Exception {
        final File nested = new File(top, "nested");

        assertTrue(nested.mkdirs());

        FileUtils.touch(new File(nested, "file"));

        assertEquals(1, top.list().length);

        FileUtils.cleanDirectory(top);

        assertEquals(0, top.list().length);
    }

    public void testThrowsOnNullList() throws Exception {
        if (System.getProperty("os.name").startsWith("Win")  ||  !chmod(top, 0, false)) {
            // test wont work if we can't restrict permissions on the
            // directory, so skip it.
            return;
        }

        try {
            FileUtils.cleanDirectory(top);
            fail("expected IOException");
        } catch (IOException e) {
            assertEquals("Failed to list contents of " +
                    top.getAbsolutePath(), e.getMessage());
        }
    }

    public void testThrowsOnCannotDeleteFile() throws Exception {
        final File file = new File(top, "restricted");
        FileUtils.touch(file);

        if (System.getProperty("os.name").startsWith("Win")  ||  !chmod(top, 500, false)) {
            // test wont work if we can't restrict permissions on the
            // directory, so skip it.
            return;
        }

        try {
            FileUtils.cleanDirectory(top);
            fail("expected IOException");
        } catch (IOException e) {
            assertEquals("Unable to delete file: " +
                    file.getAbsolutePath(), e.getMessage());
        }
    }

    private boolean chmod(File file, int mode, boolean recurse)
            throws InterruptedException {
        // TODO: Refactor this to FileSystemUtils
        List<String> args = new ArrayList<String>();
        args.add("chmod");

        if (recurse) {
            args.add("-R");
        }

        args.add(Integer.toString(mode));
        args.add(file.getAbsolutePath());

        Process proc;

        try {
            proc = Runtime.getRuntime().exec(
                    args.toArray(new String[args.size()]));
        } catch (IOException e) {
            return false;
        }
        int result = proc.waitFor();
        return (result == 0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/497.java