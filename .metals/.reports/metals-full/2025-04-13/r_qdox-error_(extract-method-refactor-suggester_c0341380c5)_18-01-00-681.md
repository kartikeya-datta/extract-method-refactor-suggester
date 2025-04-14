error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5979.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5979.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[25,1]

error in qdox parser
file content:
```java
offset: 1108
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5979.java
text:
```scala
abstract class AbstractPatchTestBuilder<T> extends ModificationBuilderTarget<T> {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
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

p@@ackage org.jboss.as.patching.tests;

import static org.jboss.as.patching.runner.TestUtils.randomString;

import java.io.File;
import java.io.IOException;

import java.util.Arrays;

import org.jboss.as.patching.metadata.ContentModification;
import org.jboss.as.patching.metadata.ModificationBuilderTarget;
import org.jboss.as.patching.runner.ContentModificationUtils;
import org.jboss.as.patching.runner.TestUtils;

/**
 * @author Emanuel Muckenhuber
 */
public abstract class AbstractPatchTestBuilder<T> extends ModificationBuilderTarget<T> {

    protected abstract String getPatchId();
    protected abstract File getPatchDir();
    protected abstract T returnThis();

    public T addFile(byte[] resultingHash, final String content, String... path) throws IOException {
        final ContentModification modification = ContentModificationUtils.addMisc(getPatchDir(), getPatchId(), content, path);
        return addContentModification(modification, resultingHash);
    }

    public T addFileWithRandomContent(byte[] resultingHash, String... path) throws IOException {
        return addFile(resultingHash, randomString(), path);
    }

    public T updateFileWithRandomContent(byte[] existingHash, byte[] resultingHash, String... path) throws IOException {
        final ContentModification modification = ContentModificationUtils.modifyMisc(getPatchDir(), getPatchId(), randomString(), Arrays.copyOf(existingHash, existingHash.length), path);
        return addContentModification(modification, resultingHash);
    }

    public T removeFile(byte[] existingHash, String... path) {
        final String name = path[path.length - 1];
        removeFile(name, Arrays.asList(Arrays.copyOf(path, path.length - 1)), existingHash, false);
        return returnThis();
    }

    public T addModuleWithContent(final String moduleName, byte[] resultingHash, final String... resourceContents) throws IOException {
        final ContentModification modification = ContentModificationUtils.addModule(getPatchDir(), getPatchId(), moduleName, resourceContents);
        return addContentModification(modification, resultingHash);
    }

    public T addModuleWithRandomContent(final String moduleName, byte[] resultingHash) throws IOException {
        final ContentModification modification = ContentModificationUtils.addModule(getPatchDir(), getPatchId(), moduleName, randomString());
        return addContentModification(modification, resultingHash);
    }

    public T updateModuleWithRandomContent(final String moduleName, byte[] existingHash, byte[] resultingHash) throws IOException {
        final ContentModification modification = ContentModificationUtils.modifyModule(getPatchDir(), getPatchId(), moduleName, existingHash, randomString());
        return addContentModification(modification, resultingHash);
    }

    public T updateModule(final String moduleName, byte[] existingHash, byte[] resultingHash, final TestUtils.ContentTask task) throws IOException {
        final ContentModification modification = ContentModificationUtils.modifyModule(getPatchDir(), getPatchId(), moduleName, existingHash, task);
        return addContentModification(modification, resultingHash);
    }

    protected T addContentModification(final ContentModification modification, byte[] resultingHash) {
        addContentModification(modification);
        contentHash(modification, resultingHash);
        return returnThis();
    }

    static void contentHash(final ContentModification modification, byte[] resultingHash) {
        if (resultingHash != null) {
            final byte[] contentHash = modification.getItem().getContentHash();
            System.arraycopy(contentHash, 0, resultingHash, 0, contentHash.length);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5979.java