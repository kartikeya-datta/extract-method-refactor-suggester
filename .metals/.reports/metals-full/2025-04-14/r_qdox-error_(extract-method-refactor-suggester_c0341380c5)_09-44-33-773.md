error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7759.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7759.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7759.java
text:
```scala
S@@tring name = icap.getName();

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
package org.jboss.as.osgi.service;

import static org.jboss.as.osgi.OSGiLogger.LOGGER;
import static org.jboss.as.server.Services.JBOSS_SERVICE_MODULE_LOADER;
import static org.jboss.as.server.moduleservice.ServiceModuleLoader.MODULE_PREFIX;
import static org.jboss.osgi.framework.spi.IntegrationConstants.MODULE_IDENTIFIER_KEY;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.as.osgi.deployment.BundleDeploymentProcessor;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.module.FilterSpecification;
import org.jboss.as.server.deployment.module.ModuleDependency;
import org.jboss.as.server.deployment.module.ModuleSpecification;
import org.jboss.as.server.moduleservice.ModuleDefinition;
import org.jboss.as.server.moduleservice.ModuleLoadService;
import org.jboss.as.server.moduleservice.ServiceModuleLoader;
import org.jboss.modules.DependencySpec;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;
import org.jboss.modules.ModuleLoader;
import org.jboss.modules.ModuleSpec;
import org.jboss.modules.ModuleSpec.Builder;
import org.jboss.modules.filter.MultiplePathFilterBuilder;
import org.jboss.modules.filter.PathFilter;
import org.jboss.modules.filter.PathFilters;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.ValueService;
import org.jboss.msc.value.ImmediateValue;
import org.jboss.msc.value.InjectedValue;
import org.jboss.osgi.deployment.deployer.Deployment;
import org.jboss.osgi.framework.spi.FrameworkModuleLoader;
import org.jboss.osgi.framework.spi.FrameworkModuleLoaderPlugin;
import org.jboss.osgi.resolver.XBundle;
import org.jboss.osgi.resolver.XBundleRevision;
import org.jboss.osgi.resolver.XIdentityCapability;
import org.osgi.framework.wiring.BundleRevisions;
import org.osgi.framework.wiring.BundleWire;

/**
 * This is the single {@link ModuleLoader} that the OSGi layer uses for the modules that are associated with the bundles that
 * are registered with the {@link org.jboss.osgi.framework.spi.BundleManager}.
 *
 * @author thomas.diesler@jboss.com
 * @since 20-Apr-2011
 */
final class ModuleLoaderIntegration extends FrameworkModuleLoaderPlugin {

    private final InjectedValue<ServiceModuleLoader> injectedModuleLoader = new InjectedValue<ServiceModuleLoader>();
    private ServiceContainer serviceContainer;
    private ServiceTarget serviceTarget;

    @Override
    protected void addServiceDependencies(ServiceBuilder<FrameworkModuleLoader> builder) {
        super.addServiceDependencies(builder);
        builder.addDependency(JBOSS_SERVICE_MODULE_LOADER, ServiceModuleLoader.class, injectedModuleLoader);
    }

    @Override
    public void start(StartContext context) throws StartException {
        serviceContainer = context.getController().getServiceContainer();
        serviceTarget = context.getChildTarget();
        super.start(context);
    }

    @Override
    protected FrameworkModuleLoader createServiceValue(StartContext startContext) {
        return new FrameworkModuleLoaderImpl();
    }

    class FrameworkModuleLoaderImpl implements FrameworkModuleLoader {

        @Override
        public ModuleLoader getModuleLoader() {
            class DelegatingModuleLoader extends ModuleLoader {

                @Override
                protected ModuleSpec findModule(ModuleIdentifier identifier) throws ModuleLoadException {
                    ModuleSpec moduleSpec = injectedModuleLoader.getValue().findModule(identifier);
                    if (moduleSpec == null)
                        LOGGER.debugf("Cannot obtain module spec for: %s", identifier);
                    return moduleSpec;
                }

                @Override
                protected Module preloadModule(ModuleIdentifier identifier) throws ModuleLoadException {
                    Module module = ModuleLoader.preloadModule(identifier, injectedModuleLoader.getValue());
                    if (module == null)
                        LOGGER.debugf("Cannot obtain module for: %s", identifier);
                    return module;
                }

                @Override
                public void setAndRelinkDependencies(Module module, List<DependencySpec> dependencies) throws ModuleLoadException {
                    throw new UnsupportedOperationException();
                }

                @Override
                public String toString() {
                    return ModuleLoaderIntegration.class.getSimpleName() + "." + getClass().getSimpleName();
                }
            }
            return new DelegatingModuleLoader();
        }

        /**
         * Get the module identifier for the given {@link XBundleRevision}. The returned identifier must be such that it can be used
         * by the {@link ServiceModuleLoader}
         */
        @Override
        public ModuleIdentifier getModuleIdentifier(XBundleRevision brev) {
            XBundle bundle = brev.getBundle();
            Deployment deployment = bundle.adapt(Deployment.class);
            ModuleIdentifier identifier = deployment.getAttachment(MODULE_IDENTIFIER_KEY);
            if (identifier == null) {
                XIdentityCapability icap = brev.getIdentityCapability();
                BundleRevisions brevs = bundle.adapt(BundleRevisions.class);
                int revsize = brevs.getRevisions().size();
                String name = icap.getSymbolicName();
                if (revsize > 1) {
                    name += "-rev" + (revsize - 1);
                }
                identifier = ModuleIdentifier.create(MODULE_PREFIX + name, "" + icap.getVersion());
            }
            return identifier;
        }

        @Override
        public void addIntegrationDependencies(ModuleSpecBuilderContext context) {
            Builder builder = context.getModuleSpecBuilder();
            XBundleRevision brev = context.getBundleRevision();
            Map<ModuleIdentifier, DependencySpec> moduleDependencies = context.getModuleDependencies();
            Deployment deployment = brev.getBundle().adapt(Deployment.class);
            ModuleSpecification moduleSpecification = deployment.getAttachment(BundleDeploymentProcessor.MODULE_SPECIFICATION_KEY);
            if (moduleSpecification != null) {
                List<ModuleDependency> dependencies = moduleSpecification.getAllDependencies();
                LOGGER.debugf("Adding integration dependencies: %d", dependencies.size());
                for (ModuleDependency moduleDep : dependencies) {
                    ModuleIdentifier moduleId = moduleDep.getIdentifier();
                    if (moduleDependencies.get(moduleId) != null) {
                        LOGGER.debugf("  -dependency on %s (skipped)", moduleId);
                        continue;
                    }
                    // Build import filter
                    MultiplePathFilterBuilder importBuilder = PathFilters.multiplePathFilterBuilder(true);
                    for (FilterSpecification filter : moduleDep.getImportFilters()) {
                        importBuilder.addFilter(filter.getPathFilter(), filter.isInclude());
                    }
                    PathFilter importFilter = importBuilder.create();
                    // Build export filter
                    MultiplePathFilterBuilder exportBuilder = PathFilters.multiplePathFilterBuilder(true);
                    for (FilterSpecification filter : moduleDep.getExportFilters()) {
                        importBuilder.addFilter(filter.getPathFilter(), filter.isInclude());
                    }
                    PathFilter exportFilter = exportBuilder.create();
                    ModuleLoader moduleLoader = moduleDep.getModuleLoader();
                    boolean optional = moduleDep.isOptional();
                    DependencySpec depSpec = DependencySpec.createModuleDependencySpec(importFilter, exportFilter, moduleLoader, moduleId, optional);
                    LOGGER.debugf("  +%s", depSpec);
                    builder.addDependency(depSpec);
                }
            }
        }

        /**
         * Add a {@link ModuleSpec} for and OSGi module as a service that can later be looked up by the {@link ServiceModuleLoader}
         */
        @Override
        public void addModuleSpec(XBundleRevision brev, final ModuleSpec moduleSpec) {
            ModuleIdentifier identifier = moduleSpec.getModuleIdentifier();
            LOGGER.tracef("Add module spec to loader: %s", identifier);
            ServiceName moduleSpecName = ServiceModuleLoader.moduleSpecServiceName(identifier);
            ImmediateValue<ModuleDefinition> value = new ImmediateValue<>(new ModuleDefinition(identifier, Collections.<ModuleDependency>emptySet(), moduleSpec));
            serviceTarget.addService(moduleSpecName, new ValueService<>(value)).install();

            ServiceModuleLoader.installModuleResolvedService(serviceTarget, identifier);
        }

        /**
         * Add an already loaded {@link Module} to the OSGi {@link ModuleLoader}. This happens when AS registers an existing
         * {@link Module} with the {@link org.jboss.osgi.framework.spi.BundleManager}.
         * <p/>
         * The {@link Module} may not necessarily result from a user deployment. We use the same {@link ServiceName} convention as
         * in {@link ServiceModuleLoader#moduleServiceName(ModuleIdentifier)}
         * <p/>
         * The {@link ServiceModuleLoader} cannot load these modules.
         */
        @Override
        public void addModule(XBundleRevision brev, final Module module) {
            ServiceName moduleServiceName = getModuleServiceName(module.getIdentifier());
            if (serviceContainer.getService(moduleServiceName) == null) {
                LOGGER.debugf("Add module to loader: %s", module.getIdentifier());
                serviceTarget.addService(moduleServiceName, new ValueService<Module>(new ImmediateValue<Module>(module))).install();
            }
        }

        @Override
        public ServiceName createModuleService(XBundleRevision brev, List<BundleWire> wires) {
            Deployment deployment = brev.getBundle().adapt(Deployment.class);
            DeploymentUnit depUnit = deployment.getAttachment(BundleDeploymentProcessor.DEPLOYMENT_UNIT_KEY);

            // Add a dependency on the parent module if we have one
            List<ModuleDependency> dependencies = new ArrayList<ModuleDependency>();
            if (depUnit != null && depUnit.getParent() != null) {
                String parentName = depUnit.getParent().getName();
                ModuleIdentifier depId = ModuleIdentifier.create(MODULE_PREFIX + parentName);
                dependencies.add(new ModuleDependency(null, depId, false, false, false, false));
            }

            // Add dependencies on all modules this brev has a wire to
            for (BundleWire wire : wires) {
                XBundleRevision provider = (XBundleRevision) wire.getProvider();
                ModuleIdentifier providerid = provider.getModuleIdentifier();
                dependencies.add(new ModuleDependency(null, providerid, false, false, false, false));
            }

            ModuleIdentifier identifier = brev.getModuleIdentifier();
            return ModuleLoadService.install(serviceTarget, identifier, dependencies);
        }

        /**
         * Remove the {@link Module} and {@link ModuleSpec} services associated with the given identifier.
         */
        @Override
        public void removeModule(XBundleRevision brev) {
            Set<ServiceName> serviceNames = new HashSet<ServiceName>();
            ModuleIdentifier identifier = brev.getModuleIdentifier();
            serviceNames.add(getModuleSpecServiceName(identifier));
            serviceNames.add(getModuleServiceName(identifier));
            serviceNames.add(ServiceModuleLoader.moduleResolvedServiceName(identifier));
            for (ServiceName serviceName : serviceNames) {
                ServiceController<?> controller = serviceContainer.getService(serviceName);
                if (controller != null) {
                    LOGGER.debugf("Remove from loader: %s", serviceName);
                    controller.setMode(Mode.REMOVE);
                }
            }
        }

        @Override
        public ServiceName getModuleServiceName(ModuleIdentifier identifier) {
            return ServiceModuleLoader.moduleServiceName(identifier);
        }

        private ServiceName getModuleSpecServiceName(ModuleIdentifier identifier) {
            return ServiceModuleLoader.moduleSpecServiceName(identifier);
        }

        @Override
        public String toString() {
            return ModuleLoaderIntegration.class.getSimpleName();
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7759.java