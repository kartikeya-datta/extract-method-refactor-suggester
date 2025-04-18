error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1645.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1645.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1645.java
text:
```scala
m@@oduleSpecification.addSystemDependency(new ModuleDependency(moduleLoader, moduleIdentifier, false, false, true, false));

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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

package org.jboss.as.jpa.processor;

import static org.jboss.as.jpa.messages.JpaLogger.JPA_LOGGER;
import static org.jboss.as.jpa.messages.JpaLogger.ROOT_LOGGER;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarFile;

import org.jboss.as.ee.component.ComponentDescription;
import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.jpa.config.Configuration;
import org.jboss.as.jpa.config.PersistenceUnitMetadataHolder;
import org.jboss.as.jpa.config.PersistenceUnitsInApplication;
import org.jboss.as.jpa.messages.JpaLogger;
import org.jboss.as.jpa.service.PersistenceUnitServiceImpl;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.DeploymentUtils;
import org.jboss.as.server.deployment.JPADeploymentMarker;
import org.jboss.as.server.deployment.module.ModuleDependency;
import org.jboss.as.server.deployment.module.ModuleSpecification;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;
import org.jboss.modules.ModuleLoader;
import org.jboss.modules.ResourceLoaderSpec;
import org.jboss.modules.ResourceLoaders;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceName;
import org.jipijapa.plugin.spi.PersistenceUnitMetadata;

/**
 * Deployment processor which adds a module dependencies for modules needed for JPA deployments.
 *
 * @author Scott Marlow (copied from WeldDepedencyProcessor)
 */
public class JPADependencyProcessor implements DeploymentUnitProcessor {

    private static final ModuleIdentifier JAVAX_PERSISTENCE_API_ID = ModuleIdentifier.create("javax.persistence.api");
    private static final ModuleIdentifier JBOSS_AS_JPA_ID = ModuleIdentifier.create("org.jboss.as.jpa");
    private static final ModuleIdentifier JBOSS_AS_JPA_SPI_ID = ModuleIdentifier.create("org.jboss.as.jpa.spi");
    private static final ModuleIdentifier JAVASSIST_ID = ModuleIdentifier.create("org.javassist");

    private static final ModuleIdentifier HIBERNATE_3_PROVIDER = ModuleIdentifier.create("org.jboss.as.jpa.hibernate", "3");
    private static final String HIBERNATE3_PROVIDER_ADAPTOR = "org.jboss.as.jpa.hibernate3.HibernatePersistenceProviderAdaptor";
    // module dependencies for hibernate3
    private static final ModuleIdentifier JBOSS_AS_NAMING_ID = ModuleIdentifier.create("org.jboss.as.naming");
    private static final ModuleIdentifier JBOSS_JANDEX_ID = ModuleIdentifier.create("org.jboss.jandex");

    /**
     * Add dependencies for modules required for JPA deployments
     */
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final ModuleSpecification moduleSpecification = deploymentUnit.getAttachment(Attachments.MODULE_SPECIFICATION);
        final ModuleLoader moduleLoader = Module.getBootModuleLoader();

        // all applications get the javax.persistence module added to their deplyoment by default
        addDependency(moduleSpecification, moduleLoader, deploymentUnit, JAVAX_PERSISTENCE_API_ID);

        if (!JPADeploymentMarker.isJPADeployment(deploymentUnit)) {
            return; // Skip if there are no persistence use in the deployment
        }
        addDependency(moduleSpecification, moduleLoader, deploymentUnit, JBOSS_AS_JPA_ID, JBOSS_AS_JPA_SPI_ID, JAVASSIST_ID);
        addPersistenceProviderModuleDependencies(phaseContext, moduleSpecification, moduleLoader);
    }


    private void addDependency(ModuleSpecification moduleSpecification, ModuleLoader moduleLoader,
                               DeploymentUnit deploymentUnit, ModuleIdentifier... moduleIdentifiers) {
        for ( ModuleIdentifier moduleIdentifier : moduleIdentifiers) {
            moduleSpecification.addSystemDependency(new ModuleDependency(moduleLoader, moduleIdentifier, false, false, false, false));
            ROOT_LOGGER.debugf("added %s dependency to %s", moduleIdentifier, deploymentUnit.getName());
        }
    }

    private void addOptionalDependency(ModuleSpecification moduleSpecification, ModuleLoader moduleLoader,
                               DeploymentUnit deploymentUnit, ModuleIdentifier... moduleIdentifiers) {
        for ( ModuleIdentifier moduleIdentifier : moduleIdentifiers) {
            moduleSpecification.addSystemDependency(new ModuleDependency(moduleLoader, moduleIdentifier, true, false, false, false));
            ROOT_LOGGER.debugf("added %s dependency to %s", moduleIdentifier, deploymentUnit.getName());
        }
    }

    @Override
    public void undeploy(DeploymentUnit context) {

    }

    private void addPersistenceProviderModuleDependencies(DeploymentPhaseContext phaseContext, ModuleSpecification moduleSpecification, ModuleLoader moduleLoader) throws
        DeploymentUnitProcessingException {

        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();

        int defaultProviderCount = 0;
        Set<String> moduleDependencies = new HashSet<String>();

        // get the number of persistence units that use the default persistence provider module.
        // Dependencies for other persistence provider will be added to the passed
        // 'moduleDependencies' collection.  Each persistence provider module that is found, will be injected into the
        // passed moduleSpecification (for the current deployment unit).
        PersistenceUnitsInApplication persistenceUnitsInApplication = DeploymentUtils.getTopDeploymentUnit(deploymentUnit).getAttachment(PersistenceUnitsInApplication.PERSISTENCE_UNITS_IN_APPLICATION);
        for (PersistenceUnitMetadataHolder holder: persistenceUnitsInApplication.getPersistenceUnitHolders()) {
            defaultProviderCount += loadPersistenceUnits(moduleSpecification, moduleLoader, deploymentUnit, moduleDependencies, holder);
        }

        // add dependencies for the default persistence provider module
        if (defaultProviderCount > 0) {
            moduleDependencies.add(Configuration.getDefaultProviderModuleName());
            ROOT_LOGGER.debugf("added (default provider) %s dependency to %s (since %d PU(s) didn't specify %s",
                Configuration.getDefaultProviderModuleName(), deploymentUnit.getName(),defaultProviderCount, Configuration.PROVIDER_MODULE + ")");
        }

        // add persistence provider dependency
        for (String dependency : moduleDependencies) {
            addDependency(moduleSpecification, moduleLoader, deploymentUnit, ModuleIdentifier.fromString(dependency));
        }

        // add the PU service as a dependency to all EE components in this scope
        final EEModuleDescription eeModuleDescription = deploymentUnit.getAttachment(org.jboss.as.ee.component.Attachments.EE_MODULE_DESCRIPTION);
        final Collection<ComponentDescription> components = eeModuleDescription.getComponentDescriptions();
        for (PersistenceUnitMetadataHolder holder: persistenceUnitsInApplication.getPersistenceUnitHolders()) {
            addPUServiceDependencyToComponents(components, holder);
        }

    }

    /**
     * Add the <code>puServiceName</code> as a dependency on each of the passed <code>components</code>
     *
     * @param components    The components to which the PU service is added as a dependency
     * @param holder        The persistence units
     */
    private static void addPUServiceDependencyToComponents(final Collection<ComponentDescription> components, final PersistenceUnitMetadataHolder holder) {

        if (components == null || components.isEmpty() || holder == null) {
            return;
        }

        for (PersistenceUnitMetadata pu : holder.getPersistenceUnits()) {
            String jpaContainerManaged = pu.getProperties().getProperty(Configuration.JPA_CONTAINER_MANAGED);
            boolean deployPU = (jpaContainerManaged == null? true : Boolean.parseBoolean(jpaContainerManaged));
            if (deployPU) {
                final ServiceName puServiceName = PersistenceUnitServiceImpl.getPUServiceName(pu);
                for (final ComponentDescription component : components) {
                    JPA_LOGGER.debugf("Adding dependency on PU service %s for component %s", puServiceName, component.getComponentClassName());
                    component.addDependency(puServiceName, ServiceBuilder.DependencyType.REQUIRED);
                }
            }
        }
    }

    private int loadPersistenceUnits(final ModuleSpecification moduleSpecification, final ModuleLoader moduleLoader, final DeploymentUnit deploymentUnit, final Set<String> moduleDependencies, final PersistenceUnitMetadataHolder holder) throws
        DeploymentUnitProcessingException {
        int defaultProviderCount = 0;
        if (holder != null) {
            for (PersistenceUnitMetadata pu : holder.getPersistenceUnits()) {
                String providerModule = pu.getProperties().getProperty(Configuration.PROVIDER_MODULE);
                String adapterModule = pu.getProperties().getProperty(Configuration.ADAPTER_MODULE);
                String adapterClass = pu.getProperties().getProperty(Configuration.ADAPTER_CLASS);
                if (providerModule != null) {
                    if (providerModule.equals(Configuration.PROVIDER_MODULE_HIBERNATE3_BUNDLED)) {
                        //in this case we add the persistence provider to the deployment as a resource root
                        adapterClass = HIBERNATE3_PROVIDER_ADAPTOR;
                        pu.getProperties().put(Configuration.ADAPTER_CLASS, adapterClass);
                        pu.getProperties().put(Configuration.PROVIDER_MODULE, Configuration.PROVIDER_MODULE_APPLICATION_SUPPLIED);
                        pu.getProperties().remove(Configuration.ADAPTER_MODULE);

                        //for this special case we need to make a copy of the hibernate 3 adaptor inside the deployment
                        addHibernate3AdaptorToDeployment(moduleLoader, deploymentUnit);
                    } else if (providerModule.equals(Configuration.PROVIDER_MODULE_HIBERNATE3)) {
                        // if they are using hibernate 3, default the adapter module setting for them.
                        if (adapterModule == null) {
                            adapterModule = Configuration.ADAPTER_MODULE_HIBERNATE3;
                            pu.getProperties().put(Configuration.ADAPTER_MODULE, adapterModule);
                        }
                    }
                }

                if (adapterModule != null) {
                    ROOT_LOGGER.debugf("%s is configured to use adapter module '%s'", pu.getPersistenceUnitName(), adapterModule);
                    moduleDependencies.add(adapterModule);
                }
                deploymentUnit.putAttachment(JpaAttachments.ADAPTOR_CLASS_NAME, adapterClass);

                String provider = pu.getProperties().getProperty(Configuration.PROVIDER_MODULE);
                if (provider != null) {
                    if (provider.equals(Configuration.PROVIDER_MODULE_APPLICATION_SUPPLIED)) {
                        ROOT_LOGGER.debugf("%s is configured to use application supplied persistence provider", pu.getPersistenceUnitName());
                    } else {
                        moduleDependencies.add(provider);
                        ROOT_LOGGER.debugf("%s is configured to use provider module '%s'", pu.getPersistenceUnitName(), provider);
                    }
                } else if (Configuration.PROVIDER_CLASS_DEFAULT.equals(pu.getPersistenceProviderClassName())) {
                    defaultProviderCount++;  // track number of references to default provider module
                } else {
                    // inject other provider modules into application
                    // in case its not obvious, everything but hibernate3 can end up here.  For Hibernate3, the Configuration.PROVIDER_MODULE
                    // should of been specified.
                    //
                    // since we don't know (until after PersistenceProviderProcessor runs in a later phase) if the provider
                    // is packaged with the app or will be accessed as a module, make the module dependency optional (in case it
                    // doesn't exist).
                    String providerModuleName = Configuration.getProviderModuleNameFromProviderClassName(pu.getPersistenceProviderClassName());
                    if (providerModuleName != null) {
                        addOptionalDependency(moduleSpecification, moduleLoader, deploymentUnit, ModuleIdentifier.fromString(providerModuleName));
                        ROOT_LOGGER.debugf("%s is configured to use persistence provider '%s', adding an optional dependency on module '%s'",
                                pu.getPersistenceUnitName(), pu.getPersistenceProviderClassName(), providerModuleName);
                    }
                }
            }
        }
        return defaultProviderCount;
    }

    private void addHibernate3AdaptorToDeployment(final ModuleLoader moduleLoader, final DeploymentUnit deploymentUnit) {
        final ModuleSpecification moduleSpecification = deploymentUnit.getAttachment(Attachments.MODULE_SPECIFICATION);
        try {
            final Module module = moduleLoader.loadModule(HIBERNATE_3_PROVIDER);
            //use a trick to get to the root of the class loader
            final URL url = module.getClassLoader().getResource(HIBERNATE3_PROVIDER_ADAPTOR.replace('.', '/') + ".class");

            final URLConnection connection = url.openConnection();
            if (!(connection instanceof JarURLConnection)) {
                throw JpaLogger.ROOT_LOGGER.invalidUrlConnection("hibernate 3", connection);
            }

            final JarFile jarFile = ((JarURLConnection) connection).getJarFile();

            moduleSpecification.addResourceLoader(ResourceLoaderSpec.createResourceLoaderSpec(ResourceLoaders.createJarResourceLoader("hibernate3integration", jarFile)));

            // hack in the dependencies which are part of hibernate3integration
            // TODO:  do this automatically (adding dependencies found in HIBERNATE_3_PROVIDER).
            addDependency(moduleSpecification, moduleLoader, deploymentUnit, JBOSS_AS_NAMING_ID, JBOSS_JANDEX_ID);
        } catch (ModuleLoadException e) {
            throw JpaLogger.ROOT_LOGGER.cannotLoadModule(e, HIBERNATE_3_PROVIDER, "hibernate 3");
        } catch (MalformedURLException e) {
            throw JpaLogger.ROOT_LOGGER.cannotAddIntegration(e, "hibernate 3");
        } catch (IOException e) {
            throw new RuntimeException(e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1645.java