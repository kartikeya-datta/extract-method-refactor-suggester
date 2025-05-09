error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3022.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3022.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3022.java
text:
```scala
C@@ontentModification moduleUpdated = new ContentModification(new ModuleItem(newModule.getName(), newModule.getSlot(), newHash), existingHash, MODIFY);

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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

package org.jboss.as.test.patching;

import org.jboss.as.patching.metadata.BundleItem;
import org.jboss.as.patching.metadata.ContentModification;
import org.jboss.as.patching.metadata.MiscContentItem;
import org.jboss.as.patching.metadata.ModuleItem;
import org.jboss.as.test.patching.util.module.Module;

import java.io.File;
import java.io.IOException;

import static org.jboss.as.patching.Constants.BUNDLES;
import static org.jboss.as.patching.Constants.MISC;
import static org.jboss.as.patching.Constants.MODULES;
import static org.jboss.as.patching.HashUtils.hashFile;
import static org.jboss.as.patching.IoUtils.NO_CONTENT;
import static org.jboss.as.patching.IoUtils.newFile;
import static org.jboss.as.patching.metadata.ModificationType.ADD;
import static org.jboss.as.patching.metadata.ModificationType.MODIFY;
import static org.jboss.as.patching.metadata.ModificationType.REMOVE;
import static org.jboss.as.test.patching.PatchingTestUtil.createBundle0;
import static org.jboss.as.test.patching.PatchingTestUtil.createModule0;
import static org.jboss.as.test.patching.PatchingTestUtil.dump;
import static org.jboss.as.test.patching.PatchingTestUtil.randomString;
import static org.jboss.as.test.patching.PatchingTestUtil.touch;

/**
 * @author <a href="http://jmesnil.net/">Jeff Mesnil</a> (c) 2013 Red Hat inc.
 */
public class ContentModificationUtils {

    @Deprecated
    public static ContentModification addModule(File patchDir, String patchElementID, String moduleName, ResourceItem... resourceItems) throws IOException {
        File modulesDir = newFile(patchDir, patchElementID, MODULES);
        File moduleDir = createModule0(modulesDir, moduleName, resourceItems);
        byte[] newHash = hashFile(moduleDir);
        ContentModification moduleAdded = new ContentModification(new ModuleItem(moduleName, ModuleItem.MAIN_SLOT, newHash), NO_CONTENT, ADD);
        return moduleAdded;
    }

    @Deprecated
    public static ContentModification addModule(File patchDir, String patchElementID, String moduleName) throws IOException {
        File modulesDir = newFile(patchDir, patchElementID, MODULES);
        File moduleDir = createModule0(modulesDir, moduleName);
        byte[] newHash = hashFile(moduleDir);
        ContentModification moduleAdded = new ContentModification(new ModuleItem(moduleName, ModuleItem.MAIN_SLOT, newHash), NO_CONTENT, ADD);
        return moduleAdded;
    }

    public static  ContentModification addModule(File patchDir, String patchElementID, Module newModule) throws IOException {
        File baseDir = newFile(patchDir, patchElementID, MODULES);
        File mainDir = newModule.writeToDisk(baseDir);
        byte[] newHash = hashFile(mainDir);
        ContentModification moduleAdded = new ContentModification(new ModuleItem(newModule.getName(), ModuleItem.MAIN_SLOT, newHash), NO_CONTENT, ADD);
        return moduleAdded;
    }

    public static ContentModification removeModule(String moduleName, File existingModule) throws IOException {
        byte[] existingHash = hashFile(existingModule);
        return new ContentModification(new ModuleItem(moduleName, ModuleItem.MAIN_SLOT, NO_CONTENT), existingHash, REMOVE);
    }

    @Deprecated
    public static ContentModification modifyModule(File patchDir, String patchElementID, File existingModule, ResourceItem resourceItem) throws IOException {
        byte[] existingHash = hashFile(existingModule);
        return modifyModule(patchDir, patchElementID, existingModule.getName(), existingHash, resourceItem);
    }

    @Deprecated
    public static ContentModification modifyModule(File patchDir, String patchElementID, String moduleName, byte[] existingHash, ResourceItem resourceItem) throws IOException {
        File modulesDir = newFile(patchDir, patchElementID, MODULES);
        File modifiedModule = createModule0(modulesDir, moduleName, resourceItem);
        byte[] updatedHash = hashFile(modifiedModule);
        ContentModification moduleUpdated = new ContentModification(new ModuleItem(moduleName, ModuleItem.MAIN_SLOT, updatedHash), existingHash, MODIFY);
        return moduleUpdated;
    }

    public static ContentModification modifyModule(File patchDir, String patchElementID, byte[] existingHash, Module newModule) throws IOException {
        File baseDir = newFile(patchDir, patchElementID, MODULES);
        File mainDir = newModule.writeToDisk(baseDir);
        byte[] newHash = hashFile(mainDir);
        ContentModification moduleUpdated = new ContentModification(new ModuleItem(newModule.getName(), ModuleItem.MAIN_SLOT, newHash), existingHash, MODIFY);
        return moduleUpdated;
    }

    public static ContentModification addBundle(File patchDir, String patchElementID, String bundleName) throws IOException {
        File bundlesDir = newFile(patchDir, patchElementID, BUNDLES);
        File bundleDir = createBundle0(bundlesDir, bundleName, randomString());
        byte[] newHash = hashFile(bundleDir);
        ContentModification bundleAdded = new ContentModification(new BundleItem(bundleName, null, newHash), NO_CONTENT, ADD);
        return bundleAdded;
    }

    public static ContentModification removeBundle(File existingBundle) throws IOException {
        byte[] existingHash = hashFile(existingBundle);
        return new ContentModification(new BundleItem(existingBundle.getName(), null, NO_CONTENT), existingHash, REMOVE);
    }

    public static ContentModification modifyBundle(File patchDir, String patchElementID, File existingBundle, String newContent) throws IOException {
        File bundlesDir = newFile(patchDir, patchElementID, BUNDLES);
        File modifiedBundle = createBundle0(bundlesDir, existingBundle.getName(), newContent);
        byte[] existingHash = hashFile(existingBundle);
        byte[] updatedHash = hashFile(modifiedBundle);
        ContentModification moduleUpdated = new ContentModification(new BundleItem(existingBundle.getName(), null, updatedHash), existingHash, MODIFY);
        return moduleUpdated;
    }

    public static ContentModification addMisc(File patchDir, String patchElementID, String content, String... fileSegments) throws IOException {
        File miscDir = newFile(patchDir, patchElementID, MISC);
        File addedFile = touch(miscDir, fileSegments);
        dump(addedFile, content);
        byte[] newHash = hashFile(addedFile);
        String[] subdir = new String[fileSegments.length -1];
        System.arraycopy(fileSegments, 0, subdir, 0, fileSegments.length - 1);
        ContentModification fileAdded = new ContentModification(new MiscContentItem(addedFile.getName(), subdir, newHash), NO_CONTENT, ADD);
        return fileAdded;
    }

    public static ContentModification removeMisc(File existingFile, String... fileSegments) throws IOException {
        byte[] existingHash = hashFile(existingFile);
        String[] subdir = new String[0];
        if (fileSegments.length > 0) {
            subdir = new String[fileSegments.length -1];
            System.arraycopy(fileSegments, 0, subdir, 0, fileSegments.length - 1);
        }
        ContentModification fileRemoved = new ContentModification(new MiscContentItem(existingFile.getName(), subdir, NO_CONTENT), existingHash, REMOVE);
        return fileRemoved;
    }

    public static ContentModification modifyMisc(File patchDir, String patchElementID, String modifiedContent, File existingFile, String... fileSegments) throws IOException {
        byte[] existingHash = hashFile(existingFile);
        return modifyMisc(patchDir, patchElementID, modifiedContent, existingHash, fileSegments);
    }

    public static ContentModification modifyMisc(File patchDir, String patchElementID, String modifiedContent, byte[] existingHash, String... fileSegments) throws IOException {
        File miscDir = newFile(patchDir, patchElementID, MISC);
        File modifiedFile = touch(miscDir, fileSegments);
        dump(modifiedFile, modifiedContent);
        byte[] modifiedHash = hashFile(modifiedFile);
        String[] subdir = new String[0];
        if (fileSegments.length > 0) {
            subdir = new String[fileSegments.length -1];
            System.arraycopy(fileSegments, 0, subdir, 0, fileSegments.length - 1);
        }
        ContentModification fileUpdated = new ContentModification(new MiscContentItem(modifiedFile.getName(), subdir, modifiedHash), existingHash, MODIFY);
        return fileUpdated;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3022.java