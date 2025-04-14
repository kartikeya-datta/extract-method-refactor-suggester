error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7276.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7276.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7276.java
text:
```scala
L@@ist<RandomAccessFile> files = new ArrayList<>();

/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.common.io;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.common.unit.TimeValue;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FileSystemUtils {

    private static ESLogger logger = ESLoggerFactory.getLogger(FileSystemUtils.class.getName());

    private static final long mkdirsStallTimeout = TimeValue.timeValueMinutes(5).millis();
    private static final Object mkdirsMutex = new Object();
    private static volatile Thread mkdirsThread;
    private static volatile long mkdirsStartTime;

    public static boolean mkdirs(File dir) {
        synchronized (mkdirsMutex) {
            try {
                mkdirsThread = Thread.currentThread();
                mkdirsStartTime = System.currentTimeMillis();
                return dir.mkdirs();
            } finally {
                mkdirsThread = null;
            }
        }
    }

    public static void checkMkdirsStall(long currentTime) {
        Thread mkdirsThread1 = mkdirsThread;
        long stallTime = currentTime - mkdirsStartTime;
        if (mkdirsThread1 != null && (stallTime > mkdirsStallTimeout)) {
            logger.error("mkdirs stalled for {} on {}, trying to interrupt", new TimeValue(stallTime), mkdirsThread1.getName());
            mkdirsThread1.interrupt(); // try and interrupt it...
        }
    }

    public static int maxOpenFiles(File testDir) {
        boolean dirCreated = false;
        if (!testDir.exists()) {
            dirCreated = true;
            testDir.mkdirs();
        }
        List<RandomAccessFile> files = new ArrayList<RandomAccessFile>();
        try {
            while (true) {
                files.add(new RandomAccessFile(new File(testDir, "tmp" + files.size()), "rw"));
            }
        } catch (IOException ioe) {
            int i = 0;
            for (RandomAccessFile raf : files) {
                try {
                    raf.close();
                } catch (IOException e) {
                    // ignore
                }
                new File(testDir, "tmp" + i++).delete();
            }
            if (dirCreated) {
                deleteRecursively(testDir);
            }
        }
        return files.size();
    }


    public static boolean hasExtensions(File root, String... extensions) {
        if (root != null && root.exists()) {
            if (root.isDirectory()) {
                File[] children = root.listFiles();
                if (children != null) {
                    for (File child : children) {
                        if (child.isDirectory()) {
                            boolean has = hasExtensions(child, extensions);
                            if (has) {
                                return true;
                            }
                        } else {
                            for (String extension : extensions) {
                                if (child.getName().endsWith(extension)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns true if at least one of the files exists.
     */
    public static boolean exists(File... files) {
        for (File file : files) {
            if (file.exists()) {
                return true;
            }
        }
        return false;
    }

    public static boolean deleteRecursively(File[] roots) {
        boolean deleted = true;
        for (File root : roots) {
            deleted &= deleteRecursively(root);
        }
        return deleted;
    }

    public static boolean deleteRecursively(File root) {
        return deleteRecursively(root, true);
    }

    private static boolean innerDeleteRecursively(File root) {
        return deleteRecursively(root, true);
    }

    /**
     * Delete the supplied {@link java.io.File} - for directories,
     * recursively delete any nested directories or files as well.
     *
     * @param root       the root <code>File</code> to delete
     * @param deleteRoot whether or not to delete the root itself or just the content of the root.
     * @return <code>true</code> if the <code>File</code> was deleted,
     *         otherwise <code>false</code>
     */
    public static boolean deleteRecursively(File root, boolean deleteRoot) {
        if (root != null && root.exists()) {
            if (root.isDirectory()) {
                File[] children = root.listFiles();
                if (children != null) {
                    for (File aChildren : children) {
                        innerDeleteRecursively(aChildren);
                    }
                }
            }

            if (deleteRoot) {
                return root.delete();
            } else {
                return true;
            }
        }
        return false;
    }

    public static void syncFile(File fileToSync) throws IOException {
        boolean success = false;
        int retryCount = 0;
        IOException exc = null;
        while (!success && retryCount < 5) {
            retryCount++;
            RandomAccessFile file = null;
            try {
                try {
                    file = new RandomAccessFile(fileToSync, "rw");
                    file.getFD().sync();
                    success = true;
                } finally {
                    if (file != null)
                        file.close();
                }
            } catch (IOException ioe) {
                if (exc == null)
                    exc = ioe;
                try {
                    // Pause 5 msec
                    Thread.sleep(5);
                } catch (InterruptedException ie) {
                    throw new InterruptedIOException(ie.getMessage());
                }
            }
        }
    }

    public static void copyFile(File sourceFile, File destinationFile) throws IOException {
        FileInputStream sourceIs = null;
        FileChannel source = null;
        FileOutputStream destinationOs = null;
        FileChannel destination = null;
        try {
            sourceIs = new FileInputStream(sourceFile);
            source = sourceIs.getChannel();
            destinationOs = new FileOutputStream(destinationFile);
            destination = destinationOs.getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (sourceIs != null) {
                sourceIs.close();
            }
            if (destination != null) {
                destination.close();
            }
            if (destinationOs != null) {
                destinationOs.close();
            }
        }
    }

    /**
     * Check that a directory exists, is a directory and is readable
     * by the current user
     */
    public static boolean isAccessibleDirectory(File directory, ESLogger logger) {
        assert directory != null && logger != null;

        if (!directory.exists()) {
            logger.debug("[{}] directory does not exist.", directory.getAbsolutePath());
            return false;
        }
        if (!directory.isDirectory()) {
            logger.debug("[{}] should be a directory but is not.", directory.getAbsolutePath());
            return false;
        }
        if (!directory.canRead()) {
            logger.debug("[{}] directory is not readable.", directory.getAbsolutePath());
            return false;
        }
        return true;
    }

    private FileSystemUtils() {

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7276.java