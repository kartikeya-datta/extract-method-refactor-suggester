error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14666.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14666.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14666.java
text:
```scala
i@@f (PatchXml.PATCH_XML.equals(name) || PatchXml.ROLLBACK_XML.equals(name)) {

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

package org.jboss.as.patching.management;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Map;

import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.patching.installation.InstallationManager;
import org.jboss.as.patching.installation.InstallationManagerService;
import org.jboss.as.patching.installation.InstalledImage;
import org.jboss.as.patching.installation.Layer;
import org.jboss.as.patching.installation.PatchableTarget;
import org.jboss.as.patching.metadata.PatchXml;
import org.jboss.as.patching.tool.PatchingHistory;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceController;

/**
 * This handler removes the part of the history which is inactive.
 *
 * @author Alexey Loubyansky
 */
public class LocalAgeoutHistoryHandler implements OperationStepHandler {

    public static final LocalAgeoutHistoryHandler INSTANCE = new LocalAgeoutHistoryHandler();

    static final FilenameFilter ALL = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            return true;
        }
    };

    static final FilenameFilter HISTORY_FILTER = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            if (PatchXml.PATCH_XML.equals(name) || "rollback.xml".equals(name)) {
                return false;
            }
            return true;
        }
    };

    @Override
    public void execute(final OperationContext context, final ModelNode operation) throws OperationFailedException {

        context.acquireControllerLock();
        final ServiceController<?> mgrService = context.getServiceRegistry(true).getRequiredService(InstallationManagerService.NAME);
        final InstallationManager mgr = (InstallationManager) mgrService.getValue();
        InstalledImage installedImage = mgr.getInstalledImage();
        final PatchableTarget.TargetInfo info;
        try {
            info = mgr.getIdentity().loadTargetInfo();
        } catch (IOException e) {
            throw new OperationFailedException(PatchManagementMessages.MESSAGES.failedToLoadIdentity(), e);
        }

        final PatchingHistory.Iterator i = PatchingHistory.Factory.iterator(mgr, info);
        if(i.hasNextCP()) {
            i.nextCP();
            // everything else down to the base is inactive
            while(i.hasNext()) {
                final PatchingHistory.Entry entry = i.next();
                final Map<String, String> layerPatches = entry.getLayerPatches();
                if(!layerPatches.isEmpty()) {
                    for(String layerName : layerPatches.keySet()) {
                        final Layer layer = mgr.getLayer(layerName);
                        if(layer == null) {
                            throw new OperationFailedException(PatchManagementMessages.MESSAGES.layerNotFound(layerName));
                        }
                        final File patchDir = layer.getDirectoryStructure().getModulePatchDirectory(layerPatches.get(layerName));
                        if(patchDir.exists()) {
                            recursiveDelete(patchDir);
                        }
                    }
                }
                final File patchHistoryDir = installedImage.getPatchHistoryDir(entry.getPatchId());
                if(patchHistoryDir.exists()) {
                    recursiveDelete(patchHistoryDir, HISTORY_FILTER);
                }
            }
        }
        context.completeStep(OperationContext.RollbackHandler.NOOP_ROLLBACK_HANDLER);
    }

    static boolean recursiveDelete(final File root) {
        return recursiveDelete(root, ALL);
    }

    static boolean recursiveDelete(File root, FilenameFilter filter) {
        boolean ok = true;
        if (root.isDirectory()) {
            final File[] files = root.listFiles(filter);
            for (File file : files) {
                ok &= recursiveDelete(file, filter);
            }
            return ok && (root.delete() || !root.exists());
        } else {
            ok &= root.delete() || !root.exists();
        }
        return ok;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14666.java