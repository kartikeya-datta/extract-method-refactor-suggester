error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10387.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10387.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[25,1]

error in qdox parser
file content:
```java
offset: 1079
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10387.java
text:
```scala
public abstract class ModificationBuilderTarget<T> {

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

p@@ackage org.jboss.as.patching.metadata;

import static org.jboss.as.patching.IoUtils.NO_CONTENT;

import java.util.List;

/**
 * @author Emanuel Muckenhuber
 */
public abstract class AbstractModificationBuilderTarget<T> {

    /**
     * Add a content modification.
     *
     * @param modification the content modification
     */
    public abstract T addContentModification(final ContentModification modification);

    /**
     * @return this
     */
    protected abstract T returnThis();

    /**
     * Add a bundle.
     *
     * @param moduleName the module name
     * @param slot the module slot
     * @param newHash the new hash of the added content
     * @return the builder
     */
    public T addBundle(final String moduleName, final String slot, final byte[] newHash) {
        final ContentItem item = createBundleItem(moduleName, slot, newHash);
        addContentModification(createContentModification(item, ModificationType.ADD, NO_CONTENT));
        return returnThis();
    }

    /**
     * Modify a bundle.
     *
     * @param moduleName the module name
     * @param slot the module slot
     * @param existingHash the existing hash
     * @param newHash the new hash of the modified content
     * @return the builder
     */
    public T modifyBundle(final String moduleName, final String slot, final byte[] existingHash, final byte[] newHash) {
        final ContentItem item = createBundleItem(moduleName, slot, newHash);
        addContentModification(createContentModification(item, ModificationType.MODIFY, existingHash));
        return returnThis();
    }

    /**
     * Remove a bundle.
     *
     * @param moduleName the module name
     * @param slot the module slot
     * @param existingHash the existing hash
     * @return the builder
     */
    public T removeBundle(final String moduleName, final String slot, final byte[] existingHash) {
        final ContentItem item = createBundleItem(moduleName, slot, NO_CONTENT);
        addContentModification(createContentModification(item, ModificationType.REMOVE, existingHash));
        return returnThis();
    }

    /**
     * Add a misc file.
     *
     * @param name the file name
     * @param path the relative path
     * @param newHash the new hash of the added content
     * @param isDirectory whether the file is a directory or not
     * @return the builder
     */
    public T addFile(final String name, final List<String> path, final byte[] newHash, final boolean isDirectory) {
        final ContentItem item = createMiscItem(name, path, newHash, isDirectory);
        addContentModification(createContentModification(item, ModificationType.ADD, NO_CONTENT));
        return returnThis();
    }

    /**
     * Modify a misc file.
     *
     * @param name the file name
     * @param path the relative path
     * @param existingHash the existing hash
     * @param newHash the new hash of the modified content
     * @param isDirectory whether the file is a directory or not
     * @return the builder
     */
    public T modifyFile(final String name, final List<String> path, final byte[] existingHash, final byte[] newHash, final boolean isDirectory) {
        final ContentItem item = createMiscItem(name, path, newHash, isDirectory);
        addContentModification(createContentModification(item, ModificationType.MODIFY, existingHash));
        return returnThis();
    }

    /**
     * Remove a misc file.
     *
     * @param name the file name
     * @param path the relative path
     * @param existingHash the existing hash
     * @param isDirectory whether the file is a directory or not
     * @return the builder
     */
    public T removeFile(final String name, final List<String> path, final byte[] existingHash, final boolean isDirectory) {
        final ContentItem item = createMiscItem(name, path, NO_CONTENT, isDirectory);
        addContentModification(createContentModification(item, ModificationType.REMOVE, existingHash));
        return returnThis();
    }


    /**
     * Add a module.
     *
     * @param moduleName the module name
     * @param slot the module slot
     * @param newHash the new hash of the added content
     * @return the builder
     */
    public T addModule(final String moduleName, final String slot, final byte[] newHash) {
        final ContentItem item = createModuleItem(moduleName, slot, newHash);
        addContentModification(createContentModification(item, ModificationType.ADD, NO_CONTENT));
        return returnThis();
    }

    /**
     * Modify a module.
     *
     * @param moduleName the module name
     * @param slot the module slot
     * @param existingHash the existing hash
     * @param newHash the new hash of the modified content
     * @return the builder
     */
    public T modifyModule(final String moduleName, final String slot, final byte[] existingHash, final byte[] newHash) {
        final ContentItem item = createModuleItem(moduleName, slot, newHash);
        addContentModification(createContentModification(item, ModificationType.MODIFY, existingHash));
        return returnThis();
    }

    /**
     * Remove a module.
     *
     * @param moduleName the module name
     * @param slot the module slot
     * @param existingHash the existing hash
     * @return the builder
     */
    public T removeModule(final String moduleName, final String slot, final byte[] existingHash) {
        final ContentItem item = createModuleItem(moduleName, slot, NO_CONTENT);
        addContentModification(createContentModification(item, ModificationType.REMOVE, existingHash));
        return returnThis();
    }

    protected ContentModification createContentModification(final ContentItem item, final ModificationType type, final byte[] existingHash) {
        return new ContentModification(item, existingHash, type);
    }

    protected MiscContentItem createMiscItem(final String name, final List<String> path, final byte[] newHash, final boolean isDirectory) {
        return new MiscContentItem(name, path, newHash, isDirectory);
    }

    protected ModuleItem createBundleItem(final String moduleName, final String slot, final byte[] hash) {
        return new BundleItem(moduleName, slot, hash);
    }

    protected ModuleItem createModuleItem(final String moduleName, final String slot, final byte[] hash) {
        return new ModuleItem(moduleName, slot, hash);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10387.java