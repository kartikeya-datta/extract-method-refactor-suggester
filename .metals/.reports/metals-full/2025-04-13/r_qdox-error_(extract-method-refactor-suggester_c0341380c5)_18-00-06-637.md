error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5062.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5062.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5062.java
text:
```scala
t@@hrow SarMessages.MESSAGES.failedToProcessSarChild(e, deploymentRoot);

package org.jboss.as.service;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.MountedDeploymentOverlay;
import org.jboss.as.server.deployment.module.ModuleRootMarker;
import org.jboss.as.server.deployment.module.MountHandle;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.jboss.as.server.deployment.module.TempFileProviderService;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VFSUtils;
import org.jboss.vfs.VirtualFile;
import org.jboss.vfs.VisitorAttributes;
import org.jboss.vfs.util.SuffixMatchFilter;

/**
 * @author Tomasz Adamski
 */

public class SarStructureProcessor implements DeploymentUnitProcessor {

    private static final String SAR_EXTENSION = ".sar";
    private static final String JAR_EXTENSION = ".jar";

    private static final SuffixMatchFilter CHILD_ARCHIVE_FILTER = new SuffixMatchFilter(JAR_EXTENSION,
            VisitorAttributes.RECURSE_LEAVES_ONLY);

    private static Closeable NO_OP_CLOSEABLE = new Closeable() {
        public void close() throws IOException {
            // NO-OP
        }
    };

    @Override
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final ResourceRoot resourceRoot = deploymentUnit.getAttachment(Attachments.DEPLOYMENT_ROOT);
        if (resourceRoot == null) {
            return;
        }
        final VirtualFile deploymentRoot = resourceRoot.getRoot();
        if (deploymentRoot == null || !deploymentRoot.exists()) {
            return;
        }

        final String deploymentRootName = deploymentRoot.getName().toLowerCase(Locale.ENGLISH);
        if (!deploymentRootName.endsWith(SAR_EXTENSION)) {
            return;
        }

        ModuleRootMarker.mark(resourceRoot, true);

        Map<String, MountedDeploymentOverlay> overlays = deploymentUnit.getAttachment(Attachments.DEPLOYMENT_OVERLAY_LOCATIONS);
        try {
            final List<VirtualFile> childArchives = deploymentRoot.getChildren(CHILD_ARCHIVE_FILTER);

            for (final VirtualFile child : childArchives) {
                String relativeName = child.getPathNameRelativeTo(deploymentRoot);
                MountedDeploymentOverlay overlay = overlays.get(relativeName);
                Closeable closable = NO_OP_CLOSEABLE;
                if(overlay != null) {
                    overlay.remountAsZip(false);
                } else if(child.isFile()) {
                    closable = VFS.mountZip(child, child, TempFileProviderService.provider());
                }
                final MountHandle mountHandle = new MountHandle(closable);
                final ResourceRoot childResource = new ResourceRoot(child, mountHandle);
                ModuleRootMarker.mark(childResource);
                deploymentUnit.addToAttachmentList(Attachments.RESOURCE_ROOTS, childResource);
                resourceRoot.addToAttachmentList(Attachments.INDEX_IGNORE_PATHS, child.getPathNameRelativeTo(deploymentRoot));
            }
        } catch (IOException e) {
            SarMessages.MESSAGES.failedToProcessSarChild(e, deploymentRoot);
        }

    }

    @Override
    public void undeploy(DeploymentUnit context) {
        final List<ResourceRoot> childRoots = context.removeAttachment(Attachments.RESOURCE_ROOTS);
        if (childRoots != null) {
            for (ResourceRoot childRoot : childRoots) {
                VFSUtils.safeClose(childRoot.getMountHandle());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5062.java