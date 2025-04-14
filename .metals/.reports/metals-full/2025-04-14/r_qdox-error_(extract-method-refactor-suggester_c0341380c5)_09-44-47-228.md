error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3577.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3577.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,13]

error in qdox parser
file content:
```java
offset: 13
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3577.java
text:
```scala
+ " to: " + t@@o + "target file already exists");

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
package org.elasticsearch.index.store;

import org.apache.lucene.index.IndexFileNames;
import org.apache.lucene.store.*;
import org.apache.lucene.util.IOUtils;
import org.elasticsearch.common.math.MathUtils;
import org.elasticsearch.common.util.concurrent.ConcurrentCollections;
import org.elasticsearch.index.store.distributor.Distributor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A directory implementation that uses the Elasticsearch {@link Distributor} abstraction to distribute
 * files across multiple data directories.
 */
public final class DistributorDirectory extends BaseDirectory {

    private final Distributor distributor;
    private final ConcurrentMap<String, Directory> nameDirMapping = ConcurrentCollections.newConcurrentMap();

    /**
     * Creates a new DistributorDirectory from multiple directories. Note: The first directory in the given array
     * is used as the primary directory holding the file locks as well as the SEGMENTS_GEN file. All remaining
     * directories are used in a round robin fashion.
     */
    public DistributorDirectory(final Directory... dirs) throws IOException {
        this(new Distributor() {
            final AtomicInteger count = new AtomicInteger();

            @Override
            public Directory primary() {
                return dirs[0];
            }

            @Override
            public Directory[] all() {
                return dirs;
            }

            @Override
            public synchronized Directory any() {
                return dirs[MathUtils.mod(count.incrementAndGet(), dirs.length)];
            }
        });
    }

    /**
     * Creates a new DistributorDirectory form the given Distributor.
     */
    public DistributorDirectory(Distributor distributor) throws IOException {
        this.distributor = distributor;
        for (Directory dir : distributor.all()) {
            for (String file : dir.listAll()) {
                if (!usePrimary(file)) {
                    nameDirMapping.put(file, dir);
                }
            }
        }
    }

    @Override
    public final String[] listAll() throws IOException {
        final ArrayList<String> files = new ArrayList<>();
        for (Directory dir : distributor.all()) {
            for (String file : dir.listAll()) {
                files.add(file);
            }
        }
        return files.toArray(new String[files.size()]);
    }

    @Override
    public boolean fileExists(String name) throws IOException {
        try {
            return getDirectory(name).fileExists(name);
        } catch (FileNotFoundException ex) {
            return false;
        }
    }

    @Override
    public void deleteFile(String name) throws IOException {
        getDirectory(name, true, true).deleteFile(name);
        Directory remove = nameDirMapping.remove(name);
        assert usePrimary(name) || remove != null : "Tried to delete file " + name + " but couldn't";
    }

    @Override
    public long fileLength(String name) throws IOException {
        return getDirectory(name).fileLength(name);
    }

    @Override
    public IndexOutput createOutput(String name, IOContext context) throws IOException {
        return getDirectory(name, false, false).createOutput(name, context);
    }

    @Override
    public void sync(Collection<String> names) throws IOException {
        for (Directory dir : distributor.all()) {
            dir.sync(names);
        }
    }

    @Override
    public IndexInput openInput(String name, IOContext context) throws IOException {
        return getDirectory(name).openInput(name, context);
    }

    @Override
    public void close() throws IOException {
        IOUtils.close(distributor.all());
    }

    /**
     * Returns the directory that has previously been associated with this file name.
     *
     * @throws IOException if the name has not yet been associated with any directory ie. fi the file does not exists
     */
    private Directory getDirectory(String name) throws IOException {
        return getDirectory(name, true, false);
    }

    /**
     * Returns true if the primary directory should be used for the given file.
     */
    private boolean usePrimary(String name) {
        return IndexFileNames.SEGMENTS_GEN.equals(name) || Store.isChecksum(name);
    }

    /**
     * Returns the directory that has previously been associated with this file name or associates the name with a directory
     * if failIfNotAssociated is set to false.
     */
    private Directory getDirectory(String name, boolean failIfNotAssociated, boolean iterate) throws IOException {
        if (usePrimary(name)) {
            return distributor.primary();
        }
        Directory directory = nameDirMapping.get(name);
        if (directory == null) {
            // name is not yet bound to a directory:

            if (iterate) { // in order to get stuff like "write.lock" that might not be written through this directory
                for (Directory dir : distributor.all()) {
                    if (dir.fileExists(name)) {
                        directory = nameDirMapping.putIfAbsent(name, dir);
                        return directory == null ? dir : directory;
                    }
                }
            }

            if (failIfNotAssociated) {
                throw new FileNotFoundException("No such file [" + name + "]");
            }

            // Pick a directory and associate this new file with it:
            final Directory dir = distributor.any();
            directory = nameDirMapping.putIfAbsent(name, dir);
            if (directory == null) {
                // putIfAbsent did in fact put dir:
                directory = dir;
            }
        }
            
        return directory;
    }

    @Override
    public Lock makeLock(String name) {
        return distributor.primary().makeLock(name);
    }

    @Override
    public void clearLock(String name) throws IOException {
        distributor.primary().clearLock(name);
    }

    @Override
    public LockFactory getLockFactory() {
        return distributor.primary().getLockFactory();
    }

    @Override
    public void setLockFactory(LockFactory lockFactory) throws IOException {
        distributor.primary().setLockFactory(lockFactory);
    }

    @Override
    public String getLockID() {
        return distributor.primary().getLockID();
    }

    @Override
    public String toString() {
        return distributor.toString();
    }

    /**
     * Renames the given source file to the given target file unless the target already exists.
     *
     * @param directoryService the DirecotrySerivce to use.
     * @param from the source file name.
     * @param to the target file name
     * @throws IOException if the target file already exists.
     */
    public void renameFile(DirectoryService directoryService, String from, String to) throws IOException {
        Directory directory = getDirectory(from);
        if (nameDirMapping.putIfAbsent(to, directory) != null) {
            throw new IOException("Can't rename file from " + from
                    + " to: " + to + ": target file already exists");
        }
        boolean success = false;
        try {
            directoryService.renameFile(directory, from, to);
            nameDirMapping.remove(from);
            success = true;
        } finally {
            if (!success) {
                nameDirMapping.remove(to);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3577.java