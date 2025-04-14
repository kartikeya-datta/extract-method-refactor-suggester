error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9470.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9470.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9470.java
text:
```scala
i@@f(files == null || files.length == 0) {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.patching.runner;

import static org.jboss.as.patching.IoUtils.NO_CONTENT;
import static org.jboss.as.patching.IoUtils.copy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jboss.as.patching.HashUtils;
import org.jboss.as.patching.IoUtils;
import org.jboss.as.patching.PatchMessages;
import org.jboss.as.patching.metadata.ContentItem;
import org.jboss.as.patching.metadata.ContentModification;
import org.jboss.as.patching.metadata.MiscContentItem;
import org.jboss.as.patching.metadata.ModificationType;

/**
 * Task for removing a file. In case the removed file is a directory, this task will create multiple rollback operation
 * for each file in this folder.
 *
 * @author Emanuel Muckenhuber
 */
class FileRemoveTask implements PatchingTask {

    private final MiscContentItem item;
    private final File target;
    private final File backup;
    private final PatchingTaskDescription description;

    private final List<ContentModification> rollback = new ArrayList<ContentModification>();

    FileRemoveTask(PatchingTaskDescription description, File target, File backup) {
        this.target = target;
        this.backup = backup;
        this.description = description;
        this.item = description.getContentItem(MiscContentItem.class);
    }

    @Override
    public ContentItem getContentItem() {
        return item;
    }

    @Override
    public boolean prepare(PatchingTaskContext context) throws IOException {
        // we create the backup in any case, since it is possible that the task
        // will be processed anyhow if the user specified OVERRIDE_ALL policy.
        // If the task is undone, the patch history will be deleted (including this backup).
        backup(target, backup, Collections.<String>emptyList(), rollback);
        // See if the hash matches the metadata

        final byte[] expected = description.getModification().getTargetHash();
        final byte[] actual = HashUtils.hashFile(target);
        return Arrays.equals(expected, actual);
    }

    @Override
    public void execute(PatchingTaskContext context) throws IOException {
        // delete the file or directory recursively
        boolean ok = IoUtils.recursiveDelete(target);
        for(ContentModification mod : rollback) {
            // Add the rollback (add actions)
            context.recordRollbackAction(mod);
        }
        if(! ok) {
            throw PatchMessages.MESSAGES.failedToDelete(target.getAbsolutePath());
        }
    }

    void backup(final File root, final File backupLocation, final List<String> path, final List<ContentModification> rollback) throws IOException {
        if(!root.exists()) {
            // Perhaps an error condition?
        } else if(root.isDirectory()) {
            final File[] files = root.listFiles();
            final String rootName = root.getName();
            if(files.length == 0) {
                // Create empty directory
                rollback.add(createRollbackItem(rootName, path, NO_CONTENT, true));
            } else {
                final List<String> newPath = new ArrayList<String>(path);
                newPath.add(rootName);
                for (File file : files) {
                    final String name = file.getName();
                    final File newBackupLocation = new File(backupLocation, name);
                    backup(file, newBackupLocation, newPath, rollback);
                }
            }
        } else {
            // Copy and record the backup action
            final byte[] hash = copy(root, backupLocation);
            rollback.add(createRollbackItem(root.getName(), path, hash, false));
        }
    }

    static ContentModification createRollbackItem(String name, List<String> path,  byte[] backupHash, boolean directory) {
        final MiscContentItem backupItem = new MiscContentItem(name, path, backupHash, directory);
        return new ContentModification(backupItem, NO_CONTENT, ModificationType.ADD);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9470.java