error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7941.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7941.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7941.java
text:
```scala
final R@@esourceRoot mainRoot = phaseContext.getDeploymentUnit().getAttachment(Attachments.DEPLOYMENT_ROOT);

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

import java.io.IOException;

import java.util.List;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.modules.DependencySpec;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;
import org.jboss.modules.ModuleSpec;
import org.jboss.modules.MultiplePathFilterBuilder;
import org.jboss.modules.PathFilter;
import org.jboss.modules.PathFilters;

/**
 * Processor responsible for creating a module for the deployment and attach it to the deployment.
 *
 * @author John E. Bailey
 * @author Jason T. Greene
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public class ModuleDeploymentProcessor implements DeploymentUnitProcessor {

    /**
     * Create a  module from the attached module config and attache the built module..
     *
     * @param phaseContext the deployment unit context
     * @throws DeploymentUnitProcessingException
     */
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {

        final DeploymentModuleLoader deploymentModuleLoader  = phaseContext.getDeploymentUnit().getAttachment(Attachments.DEPLOYMENT_MODULE_LOADER);
        if(deploymentModuleLoader == null) {
            return;
        }

        final ResourceRoot mainRoot = phaseContext.getAttachment(Attachments.DEPLOYMENT_ROOT);

        if (mainRoot == null) {
            return;
        }

        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();

        final List<ResourceRoot> additionalRoots = phaseContext.getAttachment(Attachments.RESOURCE_ROOTS);
        final List<ModuleDependency> dependencies = phaseContext.getAttachment(Attachments.MODULE_DEPENDENCIES);

        // TODO: account for nested DUs here
        final ModuleIdentifier moduleIdentifier = ModuleIdentifier.create("deployment." + deploymentUnit.getName());

        final ModuleSpec.Builder specBuilder = ModuleSpec.build(moduleIdentifier);

        // Add internal resource roots
        addResourceRoot(specBuilder, mainRoot);
        for (ResourceRoot additionalRoot : additionalRoots) {
            addResourceRoot(specBuilder, additionalRoot);
        }

        // Add external resource roots
        // TODO: Class-Path items
        // TODO: Extension-List items

        // Add all module dependencies
        // TODO: read this from config
        final boolean childFirst = false;
        if (childFirst) {
            specBuilder.addDependency(DependencySpec.createLocalDependencySpec());
        }
        for (ModuleDependency dependency : dependencies) {
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
        }
        if (! childFirst) {
            specBuilder.addDependency(DependencySpec.createLocalDependencySpec());
        }

        final ModuleSpec moduleSpec = specBuilder.create();

        deploymentModuleLoader.addModuleSpec(moduleSpec);

        try {
            final Module module = deploymentModuleLoader.loadModule(moduleIdentifier);
            phaseContext.getDeploymentUnit().putAttachment(Attachments.MODULE, module);
        } catch (ModuleLoadException e) {
            throw new DeploymentUnitProcessingException("Failed to load module: " + moduleIdentifier, e);
        }
    }

    private static void addResourceRoot(final ModuleSpec.Builder specBuilder, final ResourceRoot resource) throws DeploymentUnitProcessingException {
        try {
            specBuilder.addResourceRoot(new VFSResourceLoader(resource.getRootName(), resource.getRoot()));
        } catch(IOException e) {
            throw new DeploymentUnitProcessingException("Failed to create VFSResourceLoader for root [" + resource.getRootName()+ "]", e);
        }
    }

    public void undeploy(DeploymentUnit context) {
        final DeploymentModuleLoader deploymentModuleLoader = context.getAttachment(Attachments.DEPLOYMENT_MODULE_LOADER);
        if(deploymentModuleLoader == null) {
            return;
        }
        final Module module = context.getAttachment(Attachments.MODULE);
        if (module != null) {
            deploymentModuleLoader.removeModuleSpec(module.getIdentifier());
            deploymentModuleLoader.removeModule(module);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7941.java