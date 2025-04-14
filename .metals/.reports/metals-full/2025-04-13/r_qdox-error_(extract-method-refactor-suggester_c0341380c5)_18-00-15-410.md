error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5724.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5724.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5724.java
text:
```scala
final R@@esourceRoot childResource = new ResourceRoot(child, mountHandle);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

package org.jboss.as.ee.structure;

import java.util.ArrayList;
import org.jboss.as.ee.config.EarConfig;
import static org.jboss.as.ee.structure.EarDeploymentMarker.isEarDeployment;
import java.io.Closeable;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.SubDeploymentMarker;
import org.jboss.as.server.deployment.module.ModuleRootMarker;
import org.jboss.as.server.deployment.module.MountHandle;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.jboss.as.server.deployment.module.TempFileProviderService;
import org.jboss.metadata.ear.spec.Ear6xMetaData;
import org.jboss.metadata.ear.spec.EarMetaData;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VFSUtils;
import org.jboss.vfs.VirtualFile;
import org.jboss.vfs.VisitorAttributes;
import org.jboss.vfs.util.SuffixMatchFilter;

/**
 * Deployment processor responsible for detecting EAR deployments and putting setting up the basic structure.
 *
 * @author John Bailey
 */
public class EarStructureProcessor implements DeploymentUnitProcessor {
    private static final String JAR_EXTENSION = ".jar";
    private static final String WAR_EXTENSION = ".war";
    private static final Set<String> CHILD_ARCHIVE_EXTENSIONS = new HashSet<String>();
    static {
        CHILD_ARCHIVE_EXTENSIONS.add(JAR_EXTENSION);
        CHILD_ARCHIVE_EXTENSIONS.add(WAR_EXTENSION);
    }

    private static final SuffixMatchFilter CHILD_ARCHIVE_FILTER = new SuffixMatchFilter(CHILD_ARCHIVE_EXTENSIONS,  new VisitorAttributes() {
        public boolean isLeavesOnly() {
            return false;
        }
    });

    private static final String DEFAULT_LIB_DIR = "lib";

    private static Closeable NO_OP_CLOSEABLE = new Closeable() {
        public void close() throws IOException {
            // NO-OP
        }
    };

    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        if(!isEarDeployment(deploymentUnit)) {
            return;
        }

        final ResourceRoot resourceRoot = phaseContext.getDeploymentUnit().getAttachment(Attachments.DEPLOYMENT_ROOT);
        final VirtualFile virtualFile = resourceRoot.getRoot();

        //  Make sure we don't index or add this as a module root
        resourceRoot.putAttachment(Attachments.INDEX_RESOURCE_ROOT, false);
        ModuleRootMarker.markRoot(resourceRoot, false);
        // Make sure any annotation deployers run against the EAR deployment.
        deploymentUnit.putAttachment(Attachments.PROCESS_CHILD_ANNOTATION_INDEX, false);
        deploymentUnit.putAttachment(Attachments.COMPUTE_COMPOSITE_ANNOTATION_INDEX, false);

        String libDirName = DEFAULT_LIB_DIR;

        final EarConfig earConfig = deploymentUnit.getAttachment(EarConfig.ATTACHMENT_KEY);
        if(earConfig != null) {
            final EarMetaData earMetaData = earConfig.getEarMetaData();
            if(earMetaData instanceof Ear6xMetaData) {
                final String xmlLibDirName = Ear6xMetaData.class.cast(earMetaData).getLibraryDirectory();
                if(xmlLibDirName != null) {
                    libDirName = xmlLibDirName;
                }
            }
        }

        // Process all the children
        try {
            final List<VirtualFile> childArchives = new ArrayList<VirtualFile>(virtualFile.getChildren(CHILD_ARCHIVE_FILTER));
            if(!libDirName.isEmpty()) {
                final VirtualFile libDir = virtualFile.getChild(libDirName);
                if(libDir.exists()) {
                    childArchives.addAll(libDir.getChildren(CHILD_ARCHIVE_FILTER));
                }
            }

            for (final VirtualFile child : childArchives) {
                final Closeable closable = child.isFile() ? VFS.mountZip(child, child, TempFileProviderService.provider()) : NO_OP_CLOSEABLE;
                final MountHandle mountHandle = new MountHandle(closable);
                final ResourceRoot childResource = new ResourceRoot(child, mountHandle, false);
                if(child.getName().toLowerCase().endsWith(JAR_EXTENSION)) {
                    ModuleRootMarker.markRoot(childResource);
                } else {
                    childResource.putAttachment(Attachments.INDEX_RESOURCE_ROOT, false);
                    SubDeploymentMarker.markRoot(childResource);
                }
                deploymentUnit.addToAttachmentList(Attachments.RESOURCE_ROOTS, childResource);
            }
        } catch (IOException e) {
            throw new DeploymentUnitProcessingException("Failed to process children for EAR [" + virtualFile + "]", e);
        }
    }

    public void undeploy(DeploymentUnit context) {
        final List<ResourceRoot> childRoots = context.removeAttachment(Attachments.RESOURCE_ROOTS);
        if(childRoots != null) {
            for(ResourceRoot childRoot : childRoots) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5724.java