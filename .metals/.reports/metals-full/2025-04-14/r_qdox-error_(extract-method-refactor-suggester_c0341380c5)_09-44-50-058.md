error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5158.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5158.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5158.java
text:
```scala
r@@eturn current = nestedEnum.nextElement();

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
package org.apache.commons.compress.changes;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Performs ChangeSet operations on a stream.
 * This class is thread safe and can be used multiple times.
 * It operates on a copy of the ChangeSet. If the ChangeSet changes,
 * a new Performer must be created.
 * 
 * @ThreadSafe
 * @Immutable
 */
public class ChangeSetPerformer {
    private final Set<Change> changes;

    /**
     * Constructs a ChangeSetPerformer with the changes from this ChangeSet
     * @param changeSet the ChangeSet which operations are used for performing
     */
    public ChangeSetPerformer(final ChangeSet changeSet) {
        changes = changeSet.getChanges();
    }

    /**
     * Performs all changes collected in this ChangeSet on the input stream and
     * streams the result to the output stream. Perform may be called more than once.
     * 
     * This method finishes the stream, no other entries should be added
     * after that.
     * 
     * @param in
     *            the InputStream to perform the changes on
     * @param out
     *            the resulting OutputStream with all modifications
     * @throws IOException
     *             if an read/write error occurs
     * @return the results of this operation
     */
    public ChangeSetResults perform(ArchiveInputStream in, ArchiveOutputStream out)
            throws IOException {
        return perform(new ArchiveInputStreamIterator(in), out);
    }

    /**
     * Performs all changes collected in this ChangeSet on the ZipFile and
     * streams the result to the output stream. Perform may be called more than once.
     * 
     * This method finishes the stream, no other entries should be added
     * after that.
     * 
     * @param in
     *            the ZipFile to perform the changes on
     * @param out
     *            the resulting OutputStream with all modifications
     * @throws IOException
     *             if an read/write error occurs
     * @return the results of this operation
     * @since 1.5
     */
    public ChangeSetResults perform(ZipFile in, ArchiveOutputStream out)
            throws IOException {
        return perform(new ZipFileIterator(in), out);
    }

    /**
     * Performs all changes collected in this ChangeSet on the input entries and
     * streams the result to the output stream.
     * 
     * This method finishes the stream, no other entries should be added
     * after that.
     * 
     * @param entryIterator
     *            the entries to perform the changes on
     * @param out
     *            the resulting OutputStream with all modifications
     * @throws IOException
     *             if an read/write error occurs
     * @return the results of this operation
     */
    private ChangeSetResults perform(ArchiveEntryIterator entryIterator,
                                     ArchiveOutputStream out)
            throws IOException {
        ChangeSetResults results = new ChangeSetResults();

        Set<Change> workingSet = new LinkedHashSet<Change>(changes);

        for (Iterator<Change> it = workingSet.iterator(); it.hasNext();) {
            Change change = it.next();

            if (change.type() == Change.TYPE_ADD && change.isReplaceMode()) {
                copyStream(change.getInput(), out, change.getEntry());
                it.remove();
                results.addedFromChangeSet(change.getEntry().getName());
            }
        }

        while (entryIterator.hasNext()) {
            ArchiveEntry entry = entryIterator.next();
            boolean copy = true;

            for (Iterator<Change> it = workingSet.iterator(); it.hasNext();) {
                Change change = it.next();

                final int type = change.type();
                final String name = entry.getName();
                if (type == Change.TYPE_DELETE && name != null) {
                    if (name.equals(change.targetFile())) {
                        copy = false;
                        it.remove();
                        results.deleted(name);
                        break;
                    }
                } else if (type == Change.TYPE_DELETE_DIR && name != null) {
                    // don't combine ifs to make future extensions more easy
                    if (name.startsWith(change.targetFile() + "/")) { // NOPMD
                        copy = false;
                        results.deleted(name);
                        break;
                    }
                }
            }

            if (copy
                && !isDeletedLater(workingSet, entry)
                && !results.hasBeenAdded(entry.getName())) {
                copyStream(entryIterator.getInputStream(), out, entry);
                results.addedFromStream(entry.getName());
            }
        }

        // Adds files which hasn't been added from the original and do not have replace mode on
        for (Iterator<Change> it = workingSet.iterator(); it.hasNext();) {
            Change change = it.next();

            if (change.type() == Change.TYPE_ADD && 
                !change.isReplaceMode() && 
                !results.hasBeenAdded(change.getEntry().getName())) {
                copyStream(change.getInput(), out, change.getEntry());
                it.remove();
                results.addedFromChangeSet(change.getEntry().getName());
            }
        }
        out.finish();
        return results;
    }

    /**
     * Checks if an ArchiveEntry is deleted later in the ChangeSet. This is
     * necessary if an file is added with this ChangeSet, but later became
     * deleted in the same set.
     * 
     * @param entry
     *            the entry to check
     * @return true, if this entry has an deletion change later, false otherwise
     */
    private boolean isDeletedLater(Set<Change> workingSet, ArchiveEntry entry) {
        String source = entry.getName();

        if (!workingSet.isEmpty()) {
            for (Change change : workingSet) {
                final int type = change.type();
                String target = change.targetFile();
                if (type == Change.TYPE_DELETE && source.equals(target)) {
                    return true;
                }

                if (type == Change.TYPE_DELETE_DIR && source.startsWith(target + "/")){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Copies the ArchiveEntry to the Output stream
     * 
     * @param in
     *            the stream to read the data from
     * @param out
     *            the stream to write the data to
     * @param entry
     *            the entry to write
     * @throws IOException
     *             if data cannot be read or written
     */
    private void copyStream(InputStream in, ArchiveOutputStream out,
            ArchiveEntry entry) throws IOException {
        out.putArchiveEntry(entry);
        IOUtils.copy(in, out);
        out.closeArchiveEntry();
    }

    /**
     * Used in perform to abstract out getting entries and streams for
     * those entries.
     *
     * <p>Iterator#hasNext is not allowed to throw exceptions that's
     * why we can't use Iterator&lt;ArchiveEntry&gt; directly -
     * otherwise we'd need to convert exceptions thrown in
     * ArchiveInputStream#getNextEntry.</p>
     */
    interface ArchiveEntryIterator {
        boolean hasNext() throws IOException;
        ArchiveEntry next();
        InputStream getInputStream() throws IOException;
    }

    private static class ArchiveInputStreamIterator
        implements ArchiveEntryIterator {
        private final ArchiveInputStream in;
        private ArchiveEntry next;
        ArchiveInputStreamIterator(ArchiveInputStream in) {
            this.in = in;
        }
        public boolean hasNext() throws IOException {
            return (next = in.getNextEntry()) != null;
        }
        public ArchiveEntry next() {
            return next;
        }
        public InputStream getInputStream() {
            return in;
        }
    }

    private static class ZipFileIterator
        implements ArchiveEntryIterator {
        private final ZipFile in;
        private final Enumeration<ZipArchiveEntry> nestedEnum;
        private ZipArchiveEntry current;
        ZipFileIterator(ZipFile in) {
            this.in = in;
            nestedEnum = in.getEntriesInPhysicalOrder();
        }
        public boolean hasNext() {
            return nestedEnum.hasMoreElements();
        }
        public ArchiveEntry next() {
            return (current = nestedEnum.nextElement());
        }
        public InputStream getInputStream() throws IOException {
            return in.getInputStream(current);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5158.java