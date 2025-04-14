error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13453.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13453.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13453.java
text:
```scala
final S@@tring bindingName = bindingConfiguration.getName().startsWith("java:comp") ? bindingConfiguration.getName() : "java:comp/env/" + bindingConfiguration.getName();

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

package org.jboss.as.ee.component;

import org.jboss.as.ee.naming.RootContextService;
import org.jboss.as.naming.ManagedReferenceFactory;
import org.jboss.as.naming.NamingStore;
import org.jboss.as.naming.deployment.ContextNames;
import org.jboss.as.naming.service.BinderService;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.logging.Logger;
import org.jboss.modules.Module;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.jboss.as.ee.component.Attachments.EE_MODULE_CONFIGURATION;
import static org.jboss.as.server.deployment.Attachments.MODULE;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class ComponentInstallProcessor implements DeploymentUnitProcessor {


    private static final Logger logger = Logger.getLogger(ComponentInstallProcessor.class);

    public void deploy(final DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final Module module = deploymentUnit.getAttachment(MODULE);
        if (module == null) {
            // Nothing to do
            return;
        }
        final EEModuleConfiguration moduleDescription = deploymentUnit.getAttachment(EE_MODULE_CONFIGURATION);
        // Iterate through each component, installing it into the container
        for (ComponentConfiguration configuration : moduleDescription.getComponentConfigurations()) {
            try {
                logger.tracef("Installing component %s", configuration.getComponentClass().getName());
                deployComponent(phaseContext, configuration);
            } catch (RuntimeException e) {
                throw new DeploymentUnitProcessingException("Failed to install component " + configuration, e);
            }
        }
    }

    protected void deployComponent(final DeploymentPhaseContext phaseContext, final ComponentConfiguration configuration) throws DeploymentUnitProcessingException {

        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final ServiceTarget serviceTarget = phaseContext.getServiceTarget();

        final String applicationName = configuration.getApplicationName();
        final String moduleName = configuration.getModuleName();
        final String componentName = configuration.getComponentName();
        final ServiceName baseName = configuration.getComponentDescription().getServiceName();

        //create additional injectors
        final ServiceName createServiceName = baseName.append("CREATE");
        final ServiceName startServiceName = baseName.append("START");
        final BasicComponentCreateService createService = configuration.getComponentCreateServiceFactory().constructService(configuration);
        final ServiceBuilder<Component> createBuilder = serviceTarget.addService(createServiceName, createService);
        // inject the DU
        createBuilder.addDependency(deploymentUnit.getServiceName(), DeploymentUnit.class, createService.getDeploymentUnitInjector());

        final ComponentStartService startService = new ComponentStartService();
        final ServiceBuilder<Component> startBuilder = serviceTarget.addService(startServiceName, startService);
        final EEModuleConfiguration moduleConfiguration = deploymentUnit.getAttachment(Attachments.EE_MODULE_CONFIGURATION);

        if (moduleConfiguration == null) {
            return;
        }
        // Add all service dependencies
        for (DependencyConfigurator configurator : configuration.getCreateDependencies()) {
            configurator.configureDependency(createBuilder);
        }
        for (DependencyConfigurator configurator : configuration.getStartDependencies()) {
            configurator.configureDependency(startBuilder);
        }

        // START depends on CREATE
        startBuilder.addDependency(createServiceName, BasicComponent.class, startService.getComponentInjector());
        final ServiceName contextServiceName;
        //set up the naming context if nessesary
        if (configuration.getComponentDescription().getNamingMode() == ComponentNamingMode.CREATE) {
            final RootContextService contextService = new RootContextService();
            contextServiceName = ContextNames.contextServiceNameOfComponent(configuration.getApplicationName(), configuration.getModuleName(), configuration.getComponentName());
            serviceTarget.addService(contextServiceName, contextService).install();
        } else {
            contextServiceName = ContextNames.contextServiceNameOfModule(configuration.getApplicationName(), configuration.getModuleName());
        }

        final InjectionSource.ResolutionContext resolutionContext = new InjectionSource.ResolutionContext(
                configuration.getComponentDescription().getNamingMode() == ComponentNamingMode.USE_MODULE,
                configuration.getComponentName(),
                configuration.getModuleName(),
                configuration.getApplicationName()
        );

        // Iterate through each view, creating the services for each
        for (ViewConfiguration viewConfiguration : configuration.getViews()) {
            final ServiceName serviceName = viewConfiguration.getViewServiceName();
            final ViewService viewService = new ViewService(viewConfiguration);
            serviceTarget.addService(serviceName, viewService)
                    .addDependency(createServiceName, Component.class, viewService.getComponentInjector())
                    .install();

            // The bindings for the view
            for (BindingConfiguration bindingConfiguration : viewConfiguration.getBindingConfigurations()) {
                final String bindingName = bindingConfiguration.getName();
                final BinderService service = new BinderService(bindingName);
                //these bindings should never be merged, if a view binding is duplicated it is an error
                ServiceBuilder<ManagedReferenceFactory> serviceBuilder = serviceTarget.addService(ContextNames.serviceNameOfContext(applicationName, moduleName, componentName, bindingName), service);
                bindingConfiguration.getSource().getResourceValue(resolutionContext, serviceBuilder, phaseContext, service.getManagedObjectInjector());
                serviceBuilder.addDependency(ContextNames.serviceNameOfNamingStore(applicationName, moduleName, componentName, bindingName), NamingStore.class, service.getNamingStoreInjector());
                serviceBuilder.install();
            }
        }

        if (configuration.getComponentDescription().getNamingMode() == ComponentNamingMode.CREATE) {

            final Map<ServiceName, BindingConfiguration> existingBindings = new HashMap<ServiceName, BindingConfiguration>();

            final Map<ServiceName, BindingConfiguration> deploymentDescriptorBindings = new HashMap<ServiceName, BindingConfiguration>();

            // The bindings for the component
            processBindings(phaseContext, configuration, serviceTarget, contextServiceName, resolutionContext, configuration.getComponentDescription().getBindingConfigurations(), existingBindings, deploymentDescriptorBindings);
            //component level bindings only come from the DD
            //and override all other bindings
            deploymentDescriptorBindings.putAll(existingBindings);

            // The bindings for the component class
            new ClassDescriptionTraversal(configuration.getModuleClassConfiguration(), moduleConfiguration) {
                @Override
                protected void handle(final EEModuleClassConfiguration classConfiguration, final EEModuleClassDescription classDescription) throws DeploymentUnitProcessingException {
                    processBindings(phaseContext, configuration, serviceTarget, contextServiceName, resolutionContext, classConfiguration.getBindingConfigurations(), existingBindings, deploymentDescriptorBindings);
                }
            }.run();


            for (InterceptorDescription interceptor : configuration.getComponentDescription().getAllInterceptors()) {
                final EEModuleClassConfiguration interceptorClass = moduleConfiguration.getClassConfiguration(interceptor.getInterceptorClassName());

                if (interceptorClass != null) {
                    new ClassDescriptionTraversal(interceptorClass, moduleConfiguration) {
                        @Override
                        protected void handle(final EEModuleClassConfiguration classConfiguration, final EEModuleClassDescription classDescription) throws DeploymentUnitProcessingException {
                            processBindings(phaseContext, configuration, serviceTarget, contextServiceName, resolutionContext, classConfiguration.getBindingConfigurations(), existingBindings, deploymentDescriptorBindings);
                        }
                    }.run();
                }
            }
        }

        createBuilder.install();
        startBuilder.install();
    }

    private void processBindings(DeploymentPhaseContext phaseContext, ComponentConfiguration configuration, ServiceTarget serviceTarget, ServiceName contextServiceName, InjectionSource.ResolutionContext resolutionContext, List<BindingConfiguration> bindings, final Map<ServiceName, BindingConfiguration> existingBindings, final Map<ServiceName, BindingConfiguration> deploymentDescriptorBindings) throws DeploymentUnitProcessingException {

        //we only handle java:comp bindings for components that have their own namespace here, the rest are processed by ModuleJndiBindingProcessor
        // TODO: Should the view configuration just return a Set instead of a List? Or is there a better way to
        // handle these duplicates?
        final Set<BindingConfiguration> uniqueBindings = new HashSet(bindings);
        for (BindingConfiguration bindingConfiguration : uniqueBindings) {
            if (bindingConfiguration.getName().startsWith("java:comp") || !bindingConfiguration.getName().startsWith("java:")) {
                final String bindingName = bindingConfiguration.getName();
                final BinderService service = new BinderService(bindingName);
                final ServiceName serviceName = ContextNames.serviceNameOfEnvEntry(configuration.getApplicationName(), configuration.getModuleName(), configuration.getComponentName(), configuration.getComponentDescription().getNamingMode() == ComponentNamingMode.CREATE, bindingName);

                if (existingBindings.containsKey(serviceName)) {
                    if(deploymentDescriptorBindings.containsKey(serviceName)) {
                        continue;
                    }
                    final BindingConfiguration existing = existingBindings.get(serviceName);
                    if (existing.equalTo(bindingConfiguration, phaseContext)) {
                        continue;
                    } else {
                        throw new DeploymentUnitProcessingException("Bindings with the same name at " + bindingConfiguration.getName() + " " + bindingConfiguration + " and " + existing);
                    }
                }
                existingBindings.put(serviceName, bindingConfiguration);

                logger.tracef("Binding %s for %s using service name %s", bindingName, configuration.getComponentClass(), serviceName);
                ServiceBuilder<ManagedReferenceFactory> serviceBuilder = serviceTarget.addService(serviceName, service);
                bindingConfiguration.getSource().getResourceValue(resolutionContext, serviceBuilder, phaseContext, service.getManagedObjectInjector());
                serviceBuilder.addDependency(contextServiceName, NamingStore.class, service.getNamingStoreInjector());
                serviceBuilder.install();
            }
        }
    }

    public void undeploy(final DeploymentUnit context) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13453.java