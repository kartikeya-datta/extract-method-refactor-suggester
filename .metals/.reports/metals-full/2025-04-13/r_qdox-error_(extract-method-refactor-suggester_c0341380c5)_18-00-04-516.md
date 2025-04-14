error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1979.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1979.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1979.java
text:
```scala
public C@@ollectionFileListener(final boolean clearOnStart) {

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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * {@link FileAlterationListener} implementation that adds created, changed and deleted
 * files/directories to a set of {@link Collection}s.
 */
public class CollectionFileListener implements FileAlterationListener, Serializable {

    private final boolean clearOnStart;
    private final Collection<File> createdFiles = new ArrayList<File>();
    private final Collection<File> changedFiles = new ArrayList<File>();
    private final Collection<File> deletedFiles = new ArrayList<File>();
    private final Collection<File> createdDirectories = new ArrayList<File>();
    private final Collection<File> changedDirectories = new ArrayList<File>();
    private final Collection<File> deletedDirectories = new ArrayList<File>();

    /**
     * Create a new observer.
     *
     * @param clearOnStart true if clear() should be called by onStart().
     */
    public CollectionFileListener(boolean clearOnStart) {
        this.clearOnStart = clearOnStart;
    }

    /**
     * File system observer started checking event.
     *
     * @param observer The file system observer
     */
    public void onStart(final FileAlterationObserver observer) {
        if (clearOnStart) {
            clear();
        }
    }

    /**
     * Clear file collections.
     */
    public void clear() {
        createdFiles.clear();
        changedFiles.clear();
        deletedFiles.clear();
        createdDirectories.clear();
        changedDirectories.clear();
        deletedDirectories.clear();
    }

    /**
     * Return the set of changed directories.
     *
     * @return Directories which have changed
     */
    public Collection<File> getChangedDirectories() {
        return changedDirectories;
    }

    /**
     * Return the set of changed files.
     *
     * @return Files which have changed
     */
    public Collection<File> getChangedFiles() {
        return changedFiles;
    }

    /**
     * Return the set of created directories.
     *
     * @return Directories which have been created
     */
    public Collection<File> getCreatedDirectories() {
        return createdDirectories;
    }

    /**
     * Return the set of created files.
     *
     * @return Files which have been created
     */
    public Collection<File> getCreatedFiles() {
        return createdFiles;
    }

    /**
     * Return the set of deleted directories.
     *
     * @return Directories which been deleted
     */
    public Collection<File> getDeletedDirectories() {
        return deletedDirectories;
    }

    /**
     * Return the set of deleted files.
     *
     * @return Files which been deleted
     */
    public Collection<File> getDeletedFiles() {
        return deletedFiles;
    }

    /**
     * Directory created Event.
     * 
     * @param directory The directory created
     */
    public void onDirectoryCreate(final File directory) {
        createdDirectories.add(directory);
    }

    /**
     * Directory changed Event.
     * 
     * @param directory The directory changed
     */
    public void onDirectoryChange(final File directory) {
        changedDirectories.add(directory);
    }

    /**
     * Directory deleted Event.
     * 
     * @param directory The directory deleted
     */
    public void onDirectoryDelete(final File directory) {
        deletedDirectories.add(directory);
    }

    /**
     * File created Event.
     * 
     * @param file The file created
     */
    public void onFileCreate(final File file) {
        createdFiles.add(file);
    }

    /**
     * File changed Event.
     * 
     * @param file The file changed
     */
    public void onFileChange(final File file) {
        changedFiles.add(file);
    }

    /**
     * File deleted Event.
     * 
     * @param file The file deleted
     */
    public void onFileDelete(final File file) {
        deletedFiles.add(file);
    }

    /**
     * File system observer finished checking event.
     *
     * @param observer The file system observer
     */
    public void onStop(final FileAlterationObserver observer) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1979.java