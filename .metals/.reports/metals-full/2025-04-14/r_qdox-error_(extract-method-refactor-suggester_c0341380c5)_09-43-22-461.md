error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13274.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13274.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13274.java
text:
```scala
i@@f (item.isEmpty() || item.equals(".")) { //a class path of . causes problems and is unnecessary, see WFLY-2725

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

package org.jboss.as.server.deployment.module;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.jboss.as.server.ServerLogger;
import org.jboss.as.server.deployment.Attachable;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.DeploymentUtils;
import org.jboss.as.server.deployment.SubDeploymentMarker;
import org.jboss.as.server.deployment.annotation.ResourceRootIndexer;
import org.jboss.as.server.moduleservice.ExternalModuleService;
import org.jboss.as.server.moduleservice.ServiceModuleLoader;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VFSUtils;
import org.jboss.vfs.VirtualFile;

/**
 * A processor which adds class path entries for each manifest entry.
 * <p/>
 * <p/>
 * <p/>
 * </li>
 * <li>
 * If the Class-Path entry is external to the deployment then it is handled by the external jar service.</li>
 * <li>
 * If the entry refers to a sibling deployment then a dependency is added on that deployment. If this deployment is
 * not present then this deployment will block until it is.</li>
 * <li>
 * If the Class-Path entry points to a jar inside the ear that is not a deployment and not a /lib jar then a reference is added
 * to this jars {@link AdditionalModuleSpecification}</li>
 * </ul>
 *
 * @author Stuart Douglas
 * @author Ales Justin
 */
public final class ManifestClassPathProcessor implements DeploymentUnitProcessor {

    private static final String[] EMPTY_STRING_ARRAY = {};

    /**
     * We only allow a single deployment at a time to be run through the class path processor.
     * <p/>
     * This is because if multiple sibling deployments reference the same item we need to make sure that they end up
     * with the same external module, and do not both create an external module with the same name.
     */
    public synchronized void deploy(final DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();


        final DeploymentUnit parent = deploymentUnit.getParent();
        final DeploymentUnit topLevelDeployment = parent == null ? deploymentUnit : parent;
        final VirtualFile topLevelRoot = topLevelDeployment.getAttachment(Attachments.DEPLOYMENT_ROOT).getRoot();
        final ExternalModuleService externalModuleService = topLevelDeployment.getAttachment(Attachments.EXTERNAL_MODULE_SERVICE);
        final ResourceRoot deploymentRoot = deploymentUnit.getAttachment(Attachments.DEPLOYMENT_ROOT);

        //These are resource roots that are already accessible by default
        //such as ear/lib jars an web-inf/lib jars
        final Set<VirtualFile> existingAccessibleRoots = new HashSet<VirtualFile>();

        final Map<VirtualFile, ResourceRoot> subDeployments = new HashMap<VirtualFile, ResourceRoot>();
        for (ResourceRoot root : DeploymentUtils.allResourceRoots(topLevelDeployment)) {
            if (SubDeploymentMarker.isSubDeployment(root)) {
                subDeployments.put(root.getRoot(), root);
            } else if (ModuleRootMarker.isModuleRoot(root)) {
                //top level module roots are already accessible, as they are either
                //ear/lib jars, or jars that are already part of the deployment
                existingAccessibleRoots.add(root.getRoot());
            }
        }

        final ArrayDeque<RootEntry> resourceRoots = new ArrayDeque<RootEntry>();
        if (deploymentUnit.getParent() != null) {
            //top level deployments already had their exiting roots processed above
            for (ResourceRoot root : DeploymentUtils.allResourceRoots(deploymentUnit)) {

                if (ModuleRootMarker.isModuleRoot(root)) {
                    //if this is a sub deployment of an ear we need to make sure we don't
                    //re-add existing module roots as class path entries
                    //this will mainly be WEB-INF/(lib|classes) entries
                    existingAccessibleRoots.add(root.getRoot());
                }
            }
        }

        for (ResourceRoot root : DeploymentUtils.allResourceRoots(deploymentUnit)) {
            //add this to the list of roots to be processed
            resourceRoots.add(new RootEntry(deploymentUnit, root));
        }

        // build a map of the additional module locations
        // note that if a resource root has been added to two different additional modules
        // and is then referenced via a Class-Path entry the behaviour is undefined
        final Map<VirtualFile, AdditionalModuleSpecification> additionalModules = new HashMap<VirtualFile, AdditionalModuleSpecification>();
        for (AdditionalModuleSpecification module : topLevelDeployment.getAttachmentList(Attachments.ADDITIONAL_MODULES)) {
            for (ResourceRoot additionalModuleResourceRoot : module.getResourceRoots()) {
                additionalModules.put(additionalModuleResourceRoot.getRoot(), module);
            }
        }

        //additional resource roots may be added as
        while (!resourceRoots.isEmpty()) {
            final RootEntry entry = resourceRoots.pop();
            final ResourceRoot resourceRoot = entry.resourceRoot;
            final Attachable target = entry.target;

            //if this is a top level deployment we do not want to process sub deployments
            if (SubDeploymentMarker.isSubDeployment(resourceRoot) && resourceRoot != deploymentRoot) {
                continue;
            }

            final String[] items = getClassPathEntries(resourceRoot);
            for (final String item : items) {
                if (item.isEmpty()) {
                    continue;
                }
                //first try and resolve relative to the manifest resource root
                final VirtualFile classPathFile = resourceRoot.getRoot().getParent().getChild(item);
                //then resolve relative to the deployment root
                final VirtualFile topLevelClassPathFile = deploymentRoot.getRoot().getParent().getChild(item);
                if (item.startsWith("/")) {
                    if (externalModuleService.isValid(item)) {
                        final ModuleIdentifier moduleIdentifier = externalModuleService.addExternalModule(item);
                        target.addToAttachmentList(Attachments.CLASS_PATH_ENTRIES, moduleIdentifier);
                        ServerLogger.DEPLOYMENT_LOGGER.debugf("Resource %s added as external jar %s", classPathFile, resourceRoot.getRoot());
                    } else {
                        ServerLogger.DEPLOYMENT_LOGGER.classPathEntryNotValid(item, resourceRoot.getRoot().getPathName());
                    }
                } else {
                    if (classPathFile.exists()) {
                        //we need to check that this class path item actually lies within the deployment
                        boolean found = false;
                        VirtualFile file = classPathFile.getParent();
                        while (file != null) {
                            if (file.equals(topLevelRoot)) {
                                found = true;
                            }
                            file = file.getParent();
                        }
                        if (!found) {
                            ServerLogger.DEPLOYMENT_LOGGER.classPathEntryNotValid(item, resourceRoot.getRoot().getPathName());
                        } else {
                            handlingExistingClassPathEntry(resourceRoots, topLevelDeployment, topLevelRoot, subDeployments, additionalModules, existingAccessibleRoots, resourceRoot, target, classPathFile);
                        }
                    } else if (topLevelClassPathFile.exists()) {
                        boolean found = false;
                        VirtualFile file = topLevelClassPathFile.getParent();
                        while (file != null) {
                            if (file.equals(topLevelRoot)) {
                                found = true;
                            }
                            file = file.getParent();
                        }
                        if (!found) {
                            ServerLogger.DEPLOYMENT_LOGGER.classPathEntryNotValid(item, resourceRoot.getRoot().getPathName());
                        } else {
                            handlingExistingClassPathEntry(resourceRoots, topLevelDeployment, topLevelRoot, subDeployments, additionalModules, existingAccessibleRoots, resourceRoot, target, topLevelClassPathFile);
                        }
                    } else {
                        ServerLogger.DEPLOYMENT_LOGGER.classPathEntryNotValid(item, resourceRoot.getRoot().getPathName());
                    }
                }
            }
        }
    }

    private void handlingExistingClassPathEntry(final ArrayDeque<RootEntry> resourceRoots, final DeploymentUnit topLevelDeployment, final VirtualFile topLevelRoot, final Map<VirtualFile, ResourceRoot> subDeployments, final Map<VirtualFile, AdditionalModuleSpecification> additionalModules, final Set<VirtualFile> existingAccessibleRoots, final ResourceRoot resourceRoot, final Attachable target, final VirtualFile classPathFile) throws DeploymentUnitProcessingException {
        if (existingAccessibleRoots.contains(classPathFile)) {
            ServerLogger.DEPLOYMENT_LOGGER.debugf("Class-Path entry %s in %s ignored, as target is already accessible", classPathFile, resourceRoot.getRoot());
        } else if (additionalModules.containsKey(classPathFile)) {
            final AdditionalModuleSpecification moduleSpecification = additionalModules.get(classPathFile);
            //as class path entries are exported, transitive dependencies will also be available
            target.addToAttachmentList(Attachments.CLASS_PATH_ENTRIES, moduleSpecification.getModuleIdentifier());
        } else if (subDeployments.containsKey(classPathFile)) {
            //now we need to calculate the sub deployment module identifier
            //unfortunately the sub deployment has not been setup yet, so we cannot just
            //get it from the sub deployment directly
            final ResourceRoot otherRoot = subDeployments.get(classPathFile);
            target.addToAttachmentList(Attachments.CLASS_PATH_ENTRIES, ModuleIdentifierProcessor.createModuleIdentifier(otherRoot.getRootName(), otherRoot, topLevelDeployment, topLevelRoot, false));
        } else {
            ModuleIdentifier identifier = createAdditionalModule(resourceRoot, topLevelDeployment, topLevelRoot, additionalModules, classPathFile, resourceRoots);
            target.addToAttachmentList(Attachments.CLASS_PATH_ENTRIES, identifier);
        }
    }

    private ModuleIdentifier createAdditionalModule(final ResourceRoot resourceRoot, final DeploymentUnit topLevelDeployment, final VirtualFile topLevelRoot, final Map<VirtualFile, AdditionalModuleSpecification> additionalModules, final VirtualFile classPathFile, final ArrayDeque<RootEntry> resourceRoots) throws DeploymentUnitProcessingException {
        final ResourceRoot root = createResourceRoot(classPathFile);
        final String pathName = root.getRoot().getPathNameRelativeTo(topLevelRoot);
        ModuleIdentifier identifier = ModuleIdentifier.create(ServiceModuleLoader.MODULE_PREFIX + topLevelDeployment.getName() + "." + pathName);
        AdditionalModuleSpecification module = new AdditionalModuleSpecification(identifier, root);
        topLevelDeployment.addToAttachmentList(Attachments.ADDITIONAL_MODULES, module);
        additionalModules.put(classPathFile, module);
        resourceRoot.addToAttachmentList(Attachments.CLASS_PATH_RESOURCE_ROOTS, root);

        //add this to the list of roots to be processed, so transitive class path entries will be respected
        resourceRoots.add(new RootEntry(module, root));
        return identifier;

    }

    private static String[] getClassPathEntries(final ResourceRoot resourceRoot) {

        final Manifest manifest;
        try {
            manifest = VFSUtils.getManifest(resourceRoot.getRoot());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (manifest == null) {
            // no class path to process!
            return EMPTY_STRING_ARRAY;
        }
        final String classPathString = manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH);
        if (classPathString == null) {
            // no entry
            return EMPTY_STRING_ARRAY;
        }
        return classPathString.split("\\s+");
    }

    /**
     * Creates a {@link ResourceRoot} for the passed {@link VirtualFile file} and adds it to the list of {@link ResourceRoot}s
     * in the {@link DeploymentUnit deploymentUnit}
     *
     *
     * @param file           The file for which the resource root will be created
     * @return Returns the created {@link ResourceRoot}
     * @throws java.io.IOException
     */
    private synchronized ResourceRoot createResourceRoot(final VirtualFile file) throws DeploymentUnitProcessingException {
        try {
            final Closeable closable = file.isFile() ? VFS.mountZip(file, file, TempFileProviderService.provider()) : null;
            final MountHandle mountHandle = new MountHandle(closable);
            final ResourceRoot resourceRoot = new ResourceRoot(file, mountHandle);
            ModuleRootMarker.mark(resourceRoot);
            ResourceRootIndexer.indexResourceRoot(resourceRoot);
            return resourceRoot;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void undeploy(final DeploymentUnit context) {
    }


    private class RootEntry {
        private final ResourceRoot resourceRoot;
        private final Attachable target;


        private RootEntry(final Attachable target, final ResourceRoot resourceRoot) {
            this.target = target;
            this.resourceRoot = resourceRoot;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13274.java