error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6554.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6554.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6554.java
text:
```scala
i@@f (child.getLowerCaseName().toLowerCase().endsWith(JAR_EXTENSION)) {

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

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
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
import org.jboss.metadata.ear.jboss.JBossAppMetaData;
import org.jboss.metadata.ear.spec.Ear5xMetaData;
import org.jboss.metadata.ear.spec.EarMetaData;
import org.jboss.metadata.ear.spec.ModuleMetaData;
import org.jboss.metadata.ear.spec.ModuleMetaData.ModuleType;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VFSUtils;
import org.jboss.vfs.VirtualFile;
import org.jboss.vfs.VisitorAttributes;
import org.jboss.vfs.util.SuffixMatchFilter;

/**
 * Deployment processor responsible for detecting EAR deployments and putting setting up the basic structure.
 *
 * @author John Bailey
 * @author Stuart Douglas
 */
public class EarStructureProcessor implements DeploymentUnitProcessor {

    private static final String JAR_EXTENSION = ".jar";
    private static final String WAR_EXTENSION = ".war";
    private static final List<String> CHILD_ARCHIVE_EXTENSIONS = new ArrayList<String>();

    static {
        CHILD_ARCHIVE_EXTENSIONS.add(JAR_EXTENSION);
        CHILD_ARCHIVE_EXTENSIONS.add(WAR_EXTENSION);
    }

    private static final SuffixMatchFilter CHILD_ARCHIVE_FILTER = new SuffixMatchFilter(CHILD_ARCHIVE_EXTENSIONS, new VisitorAttributes() {

        public boolean isLeavesOnly() {
            return false;
        }
    });

    private static final String DEFAULT_LIB_DIR = "lib";


    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        if (!DeploymentTypeMarker.isType(DeploymentType.EAR, deploymentUnit)) {
            return;
        }

        final ResourceRoot resourceRoot = phaseContext.getDeploymentUnit().getAttachment(Attachments.DEPLOYMENT_ROOT);
        final VirtualFile virtualFile = resourceRoot.getRoot();

        //  Make sure we don't index or add this as a module root
        resourceRoot.putAttachment(Attachments.INDEX_RESOURCE_ROOT, false);
        ModuleRootMarker.mark(resourceRoot, false);

        String libDirName = DEFAULT_LIB_DIR;

        final JBossAppMetaData appMetaData = deploymentUnit.getAttachment(org.jboss.as.ee.structure.Attachments.JBOSS_APP_METADATA);
        final EarMetaData earMetaData = deploymentUnit.getAttachment(org.jboss.as.ee.structure.Attachments.EAR_METADATA);
        if (appMetaData != null) {
            final String xmlLibDirName = appMetaData.getLibraryDirectory();
            if (xmlLibDirName != null) {
                libDirName = xmlLibDirName;
            }
        } else {
            if (earMetaData != null) {
                if (earMetaData instanceof Ear5xMetaData) {
                    final String xmlLibDirName = Ear5xMetaData.class.cast(earMetaData).getLibraryDirectory();
                    if (xmlLibDirName != null) {
                        libDirName = xmlLibDirName;
                    }
                }
            }
        }

        // Process all the children
        try {
            final VirtualFile libDir;
            // process the lib directory
            if (!libDirName.isEmpty()) {
                libDir = virtualFile.getChild(libDirName);
                if (libDir.exists()) {
                    List<VirtualFile> libArchives = libDir.getChildren(CHILD_ARCHIVE_FILTER);
                    for (final VirtualFile child : libArchives) {
                        final Closeable closable = child.isFile() ? VFS.mountZip(child, child, TempFileProviderService
                                .provider()) : null;
                        final MountHandle mountHandle = new MountHandle(closable);
                        final ResourceRoot childResource = new ResourceRoot(child, mountHandle);
                        if (child.getName().toLowerCase().endsWith(JAR_EXTENSION)) {
                            ModuleRootMarker.mark(childResource);
                            deploymentUnit.addToAttachmentList(Attachments.RESOURCE_ROOTS, childResource);
                        }
                    }
                }
            } else {
                libDir = null;
            }
            // scan the ear looking for wars and jars
            final List<VirtualFile> childArchives = new ArrayList<VirtualFile>(virtualFile.getChildren(new SuffixMatchFilter(
                    CHILD_ARCHIVE_EXTENSIONS, new VisitorAttributes() {
                        @Override
                        public boolean isLeavesOnly() {
                            return false;
                        }

                        @Override
                        public boolean isRecurse(VirtualFile file) {
                            // don't recurse into /lib
                            if (file.equals(libDir)) {
                                return false;
                            }
                            for (String suffix : CHILD_ARCHIVE_EXTENSIONS) {
                                if (file.getName().endsWith(suffix)) {
                                    // don't recurse into sub deployments
                                    return false;
                                }
                            }
                            return true;
                        }
                    })));

            // if there is no application.xml then look in the ear root for modules
            if (earMetaData == null) {
                for (final VirtualFile child : childArchives) {
                    final Closeable closable = child.isFile() ? VFS.mountZip(child, child, TempFileProviderService.provider())
                            : null;
                    final MountHandle mountHandle = new MountHandle(closable);
                    final ResourceRoot childResource = new ResourceRoot(child, mountHandle);
                    deploymentUnit.addToAttachmentList(Attachments.RESOURCE_ROOTS, childResource);
                    if (child.getName().toLowerCase().endsWith(WAR_EXTENSION)) {
                        SubDeploymentMarker.mark(childResource);
                        childResource.putAttachment(Attachments.INDEX_RESOURCE_ROOT, false);
                    }
                }
            } else {
                Set<VirtualFile> subDeploymentFiles = new HashSet<VirtualFile>();
                // otherwise read from application.xml
                for (ModuleMetaData module : earMetaData.getModules()) {

                    VirtualFile moduleFile = virtualFile.getChild(module.getFileName());
                    if (!moduleFile.exists()) {
                        throw new DeploymentUnitProcessingException("Unable to process modules in application.xml for EAR ["
                                + virtualFile + "], module file " + module.getFileName() + " not found");
                    }
                    final Closeable closable = moduleFile.isFile() ? VFS.mountZip(moduleFile, moduleFile,
                            TempFileProviderService.provider()) : null;
                    final MountHandle mountHandle = new MountHandle(closable);
                    final ResourceRoot childResource = new ResourceRoot(moduleFile, mountHandle);
                    deploymentUnit.addToAttachmentList(Attachments.RESOURCE_ROOTS, childResource);
                    childResource.putAttachment(org.jboss.as.ee.structure.Attachments.MODULE_META_DATA, module);
                    subDeploymentFiles.add(moduleFile);
                    SubDeploymentMarker.mark(childResource);
                    if (module.getType() == ModuleType.Web) {
                        childResource.putAttachment(Attachments.INDEX_RESOURCE_ROOT, false);
                    }
                }
                // now check the rest of the archive for any other jar files
                for (final VirtualFile child : childArchives) {
                    if (subDeploymentFiles.contains(child)) {
                        continue;
                    }
                    if (child.getName().toLowerCase().endsWith(JAR_EXTENSION)) {
                        final Closeable closable = child.isFile() ? VFS.mountZip(child, child, TempFileProviderService
                                .provider()) : null;
                        final MountHandle mountHandle = new MountHandle(closable);
                        final ResourceRoot childResource = new ResourceRoot(child, mountHandle);
                        deploymentUnit.addToAttachmentList(Attachments.RESOURCE_ROOTS, childResource);
                    }
                }
            }

        } catch (IOException e) {
            throw new DeploymentUnitProcessingException("Failed to process children for EAR [" + virtualFile + "]", e);
        }
    }

    public void undeploy(DeploymentUnit context) {
        final List<ResourceRoot> children = context.removeAttachment(Attachments.RESOURCE_ROOTS);
        if (children != null) {
            for (ResourceRoot childRoot : children) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6554.java