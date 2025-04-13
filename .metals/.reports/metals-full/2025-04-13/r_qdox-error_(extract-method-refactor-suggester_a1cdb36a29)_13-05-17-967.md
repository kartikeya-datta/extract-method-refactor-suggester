error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13069.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13069.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13069.java
text:
```scala
public static v@@oid assertFileDoesNotExist(File rootFile, String... segments) {

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

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.jboss.as.patching.HashUtils.bytesToHexString;
import static org.jboss.as.patching.HashUtils.hashFile;
import static org.jboss.as.patching.metadata.Patch.PatchType.CUMULATIVE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import junit.framework.Assert;
import org.jboss.as.patching.DirectoryStructure;
import org.jboss.as.patching.PatchInfo;
import org.jboss.as.patching.installation.PatchableTarget;
import org.jboss.as.patching.metadata.ContentItem;
import org.jboss.as.patching.metadata.Patch;

/**
 * @author <a href="http://jmesnil.net/">Jeff Mesnil</a> (c) 2012, Red Hat Inc
 */
public class PatchingAssert {

    public static void assertContains(File f, File... files) {
        List<File> set = Arrays.asList(files);
        assertTrue(f + " not found in " + set, set.contains(f));
    }

    public static File assertDirExists(File rootDir, String... segments) {
        return assertFileExists(true, rootDir, segments);
    }

    public static void assertDirDoesNotExist(File rootDir, String... segments) {
        assertFileDoesNotExist(rootDir, segments);
    }

    public static File assertFileExists(File rootDir, String... segments) {
        return assertFileExists(false, rootDir, segments);
    }

    private static File assertFileExists(boolean isDir, File rootFile, String... segments) {
        assertTrue(rootFile + " does not exist", rootFile.exists());
        File f = rootFile;
        for (String segment : segments) {
            f = new File(f, segment);
            assertTrue(f + " does not exist", f.exists());
        }
        assertEquals(f + " is " + (isDir? "not":"") + " a directory", isDir, f.isDirectory());
        return f;
    }

    static void assertFileDoesNotExist(File rootFile, String... segments) {
        if (segments.length == 0) {
            assertFalse(rootFile + " exists", rootFile.exists());
            return;
        }

        File f = rootFile;
        for (int i = 0; i < segments.length - 1; i++) {
            String segment = segments[i];
            f = new File(f, segment);
            assertTrue(f + " does not exist", f.exists());
        }
        f = new File(f, segments[segments.length -1]);
    }

    static void assertFileContent(byte[] expected, File f) throws Exception {
        assertFileContent(null, expected, f);
    }

    static void assertFileContent(String message, byte[] expected, File f) throws Exception {
        assertEquals(message, bytesToHexString(expected), bytesToHexString(hashFile(f)));
    }

    public static void assertDefinedModule(File[] modulesPath, String moduleName, byte[] expectedHash) throws Exception {
        for (File path : modulesPath) {
            final File modulePath = PatchContentLoader.getModulePath(path, moduleName, "main");
            final File moduleXml = new File(modulePath, "module.xml");
            if (moduleXml.exists()) {
                assertDefinedModuleWithRootElement(moduleXml, moduleName, "<module");
                if (expectedHash != null) {
                    byte[] actualHash = hashFile(modulePath);
                    assertTrue("content of module differs", Arrays.equals(expectedHash, actualHash));
                }
                return;
            }
        }
        fail("count not find module for " + moduleName + " in " + asList(modulesPath));
    }

    public static void assertDefinedModule(File moduleRoot, String moduleName, byte[] expectedHash) throws Exception {
        final File modulePath = PatchContentLoader.getModulePath(moduleRoot, moduleName, "main");
        final File moduleXml = new File(modulePath, "module.xml");
        if (moduleXml.exists()) {
            assertDefinedModuleWithRootElement(moduleXml, moduleName, "<module");
            if (expectedHash != null) {
                byte[] actualHash = hashFile(modulePath);
                assertTrue("content of module differs", Arrays.equals(expectedHash, actualHash));
            }
            return;
        }
        fail("count not find module for " + moduleName + " in " + moduleRoot);
    }

    static void assertDefinedBundle(File[] bundlesPath, String moduleName, byte[] expectedHash) throws Exception {
        for (File path : bundlesPath) {
            final File bundlePath = PatchContentLoader.getModulePath(path, moduleName, "main");
            if(bundlePath.exists()) {
                if(expectedHash != null) {
                    byte[] actualHash = hashFile(bundlePath);
                    assertTrue("content of bundle differs", Arrays.equals(expectedHash, actualHash));
                }
                return;
            }
        }
        fail("content not found bundle for " + moduleName + " in " + asList(bundlesPath));
    }

    static void assertDefinedAbsentBundle(File[] bundlesPath, String moduleName) throws Exception {
        for (File path : bundlesPath) {
            final File bundlePath = PatchContentLoader.getModulePath(path, moduleName, "main");
            if(bundlePath.exists()) {
                final File[] children = bundlePath.listFiles();
                if(children.length == 0) {
                    return;
                }
            }
        }
        fail("content not found bundle for " + moduleName + " in " + asList(bundlesPath));
    }

    static void assertDefinedAbsentModule(File[] modulesPath, String moduleName) throws Exception {
        for (File path : modulesPath) {
            final File modulePath = PatchContentLoader.getModulePath(path, moduleName, "main");
            final File moduleXml = new File(modulePath, "module.xml");
            if (moduleXml.exists()) {
                assertDefinedModuleWithRootElement(moduleXml, moduleName, "<module-absent ");
                return;
            }
        }
        fail("count not found module for " + moduleName + " in " + asList(modulesPath));
    }

    private static void assertDefinedModuleWithRootElement(File moduleXMLFile, String moduleName, String rootElement) throws Exception {
        assertFileExists(moduleXMLFile);
        assertFileContains(moduleXMLFile,rootElement);
        assertFileContains(moduleXMLFile, format("name=\"%s\"", moduleName));
    }

    private static void assertFileContains(File f, String string) throws Exception {
        String content = new Scanner(f, "UTF-8").useDelimiter("\\Z").next();
        assertTrue(string + " not found in " + f + " with content=" + content, content.contains(string));
    }

    public static void assertPatchHasBeenApplied(PatchingResult result, Patch patch) {
        if (CUMULATIVE == patch.getPatchType()) {
            assertEquals(patch.getPatchId(), result.getPatchInfo().getCumulativeID());
            assertEquals(patch.getResultingVersion(), result.getPatchInfo().getVersion());
        } else {
            assertTrue(result.getPatchInfo().getPatchIDs().contains(patch.getPatchId()));
            // applied one-off patch is at the top of the patchIDs
            assertEquals(patch.getPatchId(), result.getPatchInfo().getPatchIDs().get(0));
        }
    }

    static void assertPatchHasNotBeenApplied(PatchingException result, Patch patch, ContentItem problematicItem, DirectoryStructure structure) {
        assertFalse("patch should have failed", result.getConflicts().isEmpty());
        assertTrue(problematicItem + " is not reported in the problems " + result.getConflicts(), result.getConflicts().contains(problematicItem));

        assertDirDoesNotExist(structure.getInstalledImage().getPatchHistoryDir(patch.getPatchId()));
    }

    public static void assertPatchHasBeenRolledBack(PatchingResult result, Patch patch, PatchInfo expectedPatchInfo) {
        assertEquals(expectedPatchInfo.getVersion(), result.getPatchInfo().getVersion());
        assertEquals(expectedPatchInfo.getCumulativeID(), result.getPatchInfo().getCumulativeID());
        assertEquals(expectedPatchInfo.getPatchIDs(), result.getPatchInfo().getPatchIDs());

        // assertNoResourcesForPatch(result.getPatchInfo(), patch);
    }

    static void assertNoResourcesForPatch(DirectoryStructure structure, Patch patch) {
        assertDirDoesNotExist(structure.getModulePatchDirectory(patch.getPatchId()));
        assertDirDoesNotExist(structure.getBundlesPatchDirectory(patch.getPatchId()));
        assertDirDoesNotExist(structure.getInstalledImage().getPatchHistoryDir(patch.getPatchId()));
    }

    public static void assertInstallationIsPatched(Patch patch, PatchableTarget.TargetInfo targetInfo) {
        if (CUMULATIVE == patch.getPatchType()) {
            assertEquals(patch.getPatchId(), targetInfo.getCumulativeID());
        } else {
            Assert.assertTrue(targetInfo.getPatchIDs().contains(patch.getPatchId()));
            // applied one-off patch is at the top of the patchIDs
            assertEquals(patch.getPatchId(), targetInfo.getPatchIDs().get(0));
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13069.java