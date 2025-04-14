error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3231.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3231.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3231.java
text:
```scala
s@@kipExecution = contentItem.getContentType() == ContentType.MISC && backupHash != NO_CONTENT;

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

import java.io.IOException;
import java.util.Arrays;

import org.jboss.as.patching.PatchMessages;
import org.jboss.as.patching.metadata.ContentItem;
import org.jboss.as.patching.metadata.ContentModification;
import org.jboss.as.patching.metadata.ContentType;

/**
 * Basic patching task implementation.
 *
 * @author Emanuel Muckenhuber
 */
abstract class AbstractPatchingTask<T extends ContentItem> implements PatchingTask {

    protected final T contentItem;
    protected final PatchingTaskDescription description;

    private boolean ignoreApply;   // completely ignore the apply step
    private boolean skipExecution; // Skip the execution step
    private byte[] backupHash = NO_CONTENT; // The backup hash

    AbstractPatchingTask(PatchingTaskDescription description, Class<T> expected) {
        this.description = description;
        this.contentItem = description.getContentItem(expected);
    }

    @Override
    public T getContentItem() {
        return contentItem;
    }

    /**
     * Backup the content.
     *
     * @param context the patching context
     * @return the hash of the content
     * @throws IOException
     */
    abstract byte[] backup(PatchingTaskContext context) throws IOException;

    /**
     * Apply the modification.
     *
     * @param context the patching context
     * @param loader the patch content loader
     * @return the actual copied content hash
     * @throws IOException
     */
    abstract byte[] apply(final PatchingTaskContext context, final PatchContentLoader loader) throws IOException;

    /**
     * Create the rollback entry.
     *
     * @param original the original content modification
     * @param targetHash the new target hash code (current content)
     * @param itemHash the new content item hash (backup content)
     * @return the rollback modification information
     */
    abstract ContentModification createRollbackEntry(ContentModification original, byte[] targetHash, byte[] itemHash);

    /**
     * Fail if the copied content is different from the one specified in the metadata. This should be true in most of the
     * cases. Only removing a module does not really match this, since we are creating a removed-module rather than
     * removing the contents.
     *
     * @param context the task context
     * @return
     */
    protected boolean failOnContentMismatch(PatchingTaskContext context) {
        return context.getCurrentMode() != PatchingTaskContext.Mode.UNDO;
    }

    /**
     * Get the original modification. Tasks like module remove can override this and fix the hashes based on the created content.
     *
     * @param targetHash the new target hash code (current content)
     * @param itemHash the new content item hash (backup content)
     * @return the original modification
     */
    protected ContentModification getOriginalModification(byte[] targetHash, byte[] itemHash) {
        return description.getModification();
    }

    /**
     * Completely skip the apply step.
     */
    protected void setIgnoreApply() {
        ignoreApply = true;
    }

    @Override
    public boolean prepare(final PatchingTaskContext context) throws IOException {
        // Backup
        backupHash = backup(context);
        // If the content is already present just resolve any conflict automatically
        final byte[] contentHash = contentItem.getContentHash();
        if(Arrays.equals(backupHash, contentHash)) {
            // Skip execute for misc items only
            skipExecution = contentItem.getContentType() == ContentType.MISC;
            return true;
        }
        // See if the content matches our expected target
        final byte[] expected = description.getModification().getTargetHash();
        if(Arrays.equals(backupHash, expected)) {
            // Don't resolve conflicts from the history
            return ! description.hasConflicts();
        }
        return false;
    }

    @Override
    public void execute(final PatchingTaskContext context) throws IOException {
        if (ignoreApply) {
            return;
        }
        final PatchContentLoader contentLoader = description.getLoader();
        final boolean skip = skipExecution | context.isExcluded(contentItem);
        final byte[] contentHash;
        if(skip) {
            contentHash = backupHash; // Reuse the backup hash
        } else {
            contentHash = apply(context, contentLoader); // Copy the content
        }
        // Add the rollback action
        final ContentModification original = getOriginalModification(contentHash, backupHash);
        final ContentModification rollbackAction = createRollbackEntry(original, contentHash, backupHash);
        context.recordChange(original, rollbackAction);
        // Fail after adding the undo action
        if (! Arrays.equals(contentHash, contentItem.getContentHash()) && failOnContentMismatch(context)) {
            throw PatchMessages.MESSAGES.wrongCopiedContent(contentItem);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3231.java