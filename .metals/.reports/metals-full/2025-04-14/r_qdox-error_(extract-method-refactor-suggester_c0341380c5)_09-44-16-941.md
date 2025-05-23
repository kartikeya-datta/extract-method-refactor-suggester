error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9405.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9405.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9405.java
text:
```scala
p@@haseContext.addToAttachmentList(Attachments.NEXT_PHASE_DEPS, Services.deploymentUnitName(depDeploymentName, Phase.CONFIGURE_MODULE));

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

import static org.jboss.as.server.deployment.module.ModuleRootMarker.isModuleRoot;

import java.io.IOException;
import java.util.List;

import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.Phase;
import org.jboss.as.server.deployment.Services;
import org.jboss.modules.DependencySpec;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleSpec;
import org.jboss.modules.ResourceLoaderSpec;
import org.jboss.modules.filter.MultiplePathFilterBuilder;
import org.jboss.modules.filter.PathFilter;
import org.jboss.modules.filter.PathFilters;

/**
 * Processor responsible for installing the module spec for this deployment into the deployment module loader.
 *
 * @author John Bailey
 */
public class ModuleSpecProcessor implements DeploymentUnitProcessor {

    private static final String DEPLOYMENT_MODULE_PREFIX = "deployment.";

    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final DeploymentModuleLoader deploymentModuleLoader = deploymentUnit.getAttachment(Attachments.DEPLOYMENT_MODULE_LOADER);
        if (deploymentModuleLoader == null) {
            return;
        }

        // Don't create a ModuleSpec for OSGi deployments
        if (deploymentUnit.hasAttachment(Attachments.OSGI_MANIFEST)) {
            return;
        }

        final ResourceRoot mainRoot = deploymentUnit.getAttachment(Attachments.DEPLOYMENT_ROOT);
        if (mainRoot == null) {
            return;
        }

        final List<ResourceRoot> additionalRoots = deploymentUnit.getAttachment(Attachments.RESOURCE_ROOTS);
        final List<ModuleDependency> dependencies = deploymentUnit.getAttachment(Attachments.MODULE_DEPENDENCIES);

        // TODO: account for nested DUs here
        final ModuleIdentifier moduleIdentifier = ModuleIdentifier.create("deployment." + deploymentUnit.getName());
        deploymentUnit.putAttachment(Attachments.MODULE_IDENTIFIER, moduleIdentifier);

        final ModuleSpec.Builder specBuilder = ModuleSpec.build(moduleIdentifier);

        // Add internal resource roots
        if(isModuleRoot(mainRoot)) {
            addResourceRoot(specBuilder, mainRoot);
        }
        if (additionalRoots != null) for (ResourceRoot additionalRoot : additionalRoots) {
            if(isModuleRoot(additionalRoot)) {
                addResourceRoot(specBuilder, additionalRoot);
            }
        }

        final boolean childFirst = false;
        if (childFirst) {
            specBuilder.addDependency(DependencySpec.createLocalDependencySpec());
        }
        if(dependencies != null) for (ModuleDependency dependency : dependencies) {
            final List<FilterSpecification> importFilters = dependency.getImportFilters();
            final List<FilterSpecification> exportFilters = dependency.getExportFilters();
            final PathFilter importFilter;
            final PathFilter exportFilter;
            final MultiplePathFilterBuilder importBuilder = PathFilters.multiplePathFilterBuilder(true);
            for (FilterSpecification filter : importFilters) {
                importBuilder.addFilter(filter.getPathFilter(), filter.isInclude());
            }
            if (dependency.isImportServices()) {
                importBuilder.addFilter(PathFilters.getMetaInfServicesFilter(), true);
            }
            importBuilder.addFilter(PathFilters.getMetaInfSubdirectoriesFilter(), false);
            importBuilder.addFilter(PathFilters.getMetaInfFilter(), false);
            importFilter = importBuilder.create();
            if (exportFilters.isEmpty()) {
                exportFilter = PathFilters.acceptAll();
            } else {
                final MultiplePathFilterBuilder exportBuilder = PathFilters.multiplePathFilterBuilder(dependency.isExport());
                for (FilterSpecification filter : exportFilters) {
                    exportBuilder.addFilter(filter.getPathFilter(), filter.isInclude());
                }
                exportFilter = exportBuilder.create();
            }
            DependencySpec depSpec = DependencySpec.createModuleDependencySpec(importFilter, exportFilter, dependency.getModuleLoader(), dependency.getIdentifier(), dependency.isOptional());
            specBuilder.addDependency(depSpec);

            final String depName = dependency.getIdentifier().getName();
            if(depName.startsWith(DEPLOYMENT_MODULE_PREFIX)) {
                final String depDeploymentName = depName.substring(DEPLOYMENT_MODULE_PREFIX.length());
                phaseContext.addToAttachmentList(Attachments.NEXT_PHASE_DEPS, Services.JBOSS_DEPLOYMENT_UNIT.append(depDeploymentName).append(Phase.CONFIGURE_MODULE.name()));
            }
        }
        if (!childFirst) {
            specBuilder.addDependency(DependencySpec.createLocalDependencySpec());
        }
        final ModuleSpec moduleSpec = specBuilder.create();
        deploymentModuleLoader.addModuleSpec(moduleSpec);
    }

    private static void addResourceRoot(final ModuleSpec.Builder specBuilder, final ResourceRoot resource) throws DeploymentUnitProcessingException {
        try {
            specBuilder.addResourceRoot(ResourceLoaderSpec.createResourceLoaderSpec(new VFSResourceLoader(resource.getRootName(), resource.getRoot())));
        } catch (IOException e) {
            throw new DeploymentUnitProcessingException("Failed to create VFSResourceLoader for root [" + resource.getRootName() + "]", e);
        }
    }

    public void undeploy(DeploymentUnit context) {
        final DeploymentModuleLoader deploymentModuleLoader = context.getAttachment(Attachments.DEPLOYMENT_MODULE_LOADER);
        if (deploymentModuleLoader == null) {
            return;
        }
        final ModuleIdentifier identifier = context.getAttachment(Attachments.MODULE_IDENTIFIER);
        if (identifier != null) {
            deploymentModuleLoader.removeModuleSpec(identifier);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9405.java