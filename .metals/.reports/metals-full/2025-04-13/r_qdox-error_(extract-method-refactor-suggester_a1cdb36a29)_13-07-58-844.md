error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3867.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3867.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3867.java
text:
```scala
protected C@@ollectionFilesystemListener listener;

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
package org.apache.commons.io.monitor;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;

import junit.framework.TestCase;

/**
 * {@link FilesystemObserver} Test Case.
 */
public abstract class AbstractMonitorTestCase extends TestCase {

    /** Filesystem observer */
    protected FilesystemObserver observer;

    /** Listener which collects file changes */
    protected CollectionFilesystemListener listener = new CollectionFilesystemListener(true);

    /** Test diretory name */
    protected String testDirName = null;

    /** Directory for test files */
    protected File testDir;

    /** Time in milliseconds to pause in tests */
    protected long pauseTime = 100L;

    /**
     * Construct a new test case.
     *
     * @param name The name of the test
     */
    public AbstractMonitorTestCase(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        testDir = new File(new File("."), testDirName);
        if (testDir.exists()) {
            FileUtils.cleanDirectory(testDir);
        } else {
            testDir.mkdir();
        }

        IOFileFilter files = FileFilterUtils.fileFileFilter();
        IOFileFilter javaSuffix = FileFilterUtils.suffixFileFilter(".java");
        IOFileFilter fileFilter = FileFilterUtils.and(files, javaSuffix);
        
        IOFileFilter directories = FileFilterUtils.directoryFileFilter();
        IOFileFilter visible = HiddenFileFilter.VISIBLE;
        IOFileFilter dirFilter = FileFilterUtils.and(directories, visible);

        IOFileFilter filter = FileFilterUtils.or(dirFilter, fileFilter);
        
        createObserver(testDir, filter);
    }

    /**
     * Create a {@link FilesystemObserver}.
     * 
     * @param file The directory to observe
     * @param fileFilter The file filter to apply
     */
    protected void createObserver(File file, FileFilter fileFilter) {
        observer = new FilesystemObserver(file, fileFilter);
        observer.addListener(listener);
        try {
            observer.initialize();
        } catch (Exception e) {
            fail("Observer init() threw " + e);
        }
    }

    @Override
    protected void tearDown() throws Exception {
        FileUtils.deleteDirectory(testDir);
    }

    /**
     * Check all the Collections are empty
     */
    protected void checkCollectionsEmpty(String label) {
        checkCollectionSizes("EMPTY-" + label, 0, 0, 0, 0, 0, 0);
    }

    /**
     * Check all the Collections have the expected sizes.
     */
    protected void checkCollectionSizes(String label, int dirCreate, int dirChange, int dirDelete, int fileCreate, int fileChange, int fileDelete) {
        label = label + "[" + listener.getCreatedDirectories().size() +
                        " " + listener.getChangedDirectories().size() +
                        " " + listener.getDeletedDirectories().size() +
                        " " + listener.getCreatedFiles().size() +
                        " " + listener.getChangedFiles().size() +
                        " " + listener.getDeletedFiles().size() + "]";
        assertEquals(label + ": No. of directories created",  dirCreate,  listener.getCreatedDirectories().size());
        assertEquals(label + ": No. of directories changed",  dirChange,  listener.getChangedDirectories().size());
        assertEquals(label + ": No. of directories deleted",  dirDelete,  listener.getDeletedDirectories().size());
        assertEquals(label + ": No. of files created", fileCreate, listener.getCreatedFiles().size());
        assertEquals(label + ": No. of files changed", fileChange, listener.getChangedFiles().size());
        assertEquals(label + ": No. of files deleted", fileDelete, listener.getDeletedFiles().size());
    }

    /**
     * Either creates a file if it doesn't exist or updates the last modified date/time
     * if it does.
     *
     * @param file The file to touch
     * @return The file
     */
    protected File touch(File file) {
        long lastModified = file.exists() ? file.lastModified() : 0;
        try {
            FileUtils.touch(file);
            file = new File(file.getParent(), file.getName());
            while (lastModified == file.lastModified()) {
                sleepHandleInterruped(pauseTime);
                FileUtils.touch(file);
                file = new File(file.getParent(), file.getName());
            }
        } catch (Exception e) {
            fail("Touching " + file + ": " + e);
        }
        sleepHandleInterruped(pauseTime);
        return file;
    }

    /**
     * Thread.sleep(timeInMilliseconds) - ignore InterruptedException
     */
    protected void sleepHandleInterruped(long timeInMilliseconds) {
        try {
            Thread.sleep(timeInMilliseconds);
        } catch(InterruptedException ie) {
            // ignore
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3867.java