error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7346.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7346.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7346.java
text:
```scala
final S@@et<Resource.ResourceEntry> deploymentResource = context.getOperationContext().readResourceFromRoot(PathAddress.EMPTY_ADDRESS).getChildren(DEPLOYMENT);

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

package org.jboss.as.ee.subsystem;

import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.ee.logging.EeLogger;
import org.jboss.as.ee.component.ComponentDescription;
import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.ee.structure.DeploymentType;
import org.jboss.as.ee.structure.DeploymentTypeMarker;
import org.jboss.as.naming.NamingContext;
import org.jboss.as.naming.NamingStore;
import org.jboss.as.naming.deployment.ContextNames;
import org.jboss.as.naming.management.JndiViewExtension;
import org.jboss.as.naming.management.JndiViewExtensionContext;
import org.jboss.as.naming.management.JndiViewExtensionRegistry;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.Services;
import org.jboss.as.server.deployment.SubDeploymentMarker;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistry;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;

import javax.naming.NamingException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DEPLOYMENT;

/**
 * @author John Bailey
 */
public class EEJndiViewExtension implements JndiViewExtension, Service<Void> {
    static final ServiceName SERVICE_NAME = ServiceName.JBOSS.append("jndi-view", "extension", "ee");

    private final InjectedValue<JndiViewExtensionRegistry> registry = new InjectedValue<JndiViewExtensionRegistry>();

    public synchronized void start(StartContext startContext) throws StartException {
        registry.getValue().addExtension(this);
    }

    public synchronized void stop(StopContext stopContext) {
        registry.getValue().removeExtension(this);
    }

    public Void getValue() throws IllegalStateException, IllegalArgumentException {
        return null;
    }

    public void execute(final JndiViewExtensionContext context) throws OperationFailedException {

        final ModelNode applicationsNode = context.getResult().get("applications");

        final ServiceRegistry serviceRegistry = context.getOperationContext().getServiceRegistry(false);

        final Set<Resource.ResourceEntry> deploymentResource = context.getOperationContext().readResourceFromRoot(PathAddress.EMPTY_ADDRESS, false).getChildren(DEPLOYMENT);
        for (final Resource.ResourceEntry entry : deploymentResource) {
            final ServiceController<?> deploymentUnitServiceController = serviceRegistry.getService(ServiceName.JBOSS.append("deployment", "unit", entry.getName()));
            if (deploymentUnitServiceController != null) {
                final ModelNode deploymentNode = applicationsNode.get(entry.getName());
                final DeploymentUnit deploymentUnit = DeploymentUnit.class.cast(deploymentUnitServiceController.getValue());

                final String appName = cleanName(deploymentUnit.getName());

                final ServiceName appContextName = ContextNames.contextServiceNameOfApplication(appName);
                final ServiceController<?> appContextController = serviceRegistry.getService(appContextName);
                if (appContextController != null) {
                    final NamingStore appStore = NamingStore.class.cast(appContextController.getValue());
                    try {
                        context.addEntries(deploymentNode.get("java:app"), new NamingContext(appStore, null));
                    } catch (NamingException e) {
                        throw new OperationFailedException(e, new ModelNode().set(EeLogger.ROOT_LOGGER.failedToRead("java:app", appName)));
                    }
                }

                if (DeploymentTypeMarker.isType(DeploymentType.EAR, deploymentUnit)) {
                    final List<ResourceRoot> roots = deploymentUnit.getAttachmentList(org.jboss.as.server.deployment.Attachments.RESOURCE_ROOTS);
                    if(roots != null) for(ResourceRoot root : roots) {
                        if(SubDeploymentMarker.isSubDeployment(root)) {
                            final ResourceRoot parentRoot = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.DEPLOYMENT_ROOT);
                            final String relativePath = root.getRoot().getPathNameRelativeTo(parentRoot.getRoot());
                            final ServiceName subDeploymentServiceName = Services.deploymentUnitName(deploymentUnit.getName(), relativePath);
                            final ServiceController<?> subDeploymentController = serviceRegistry.getService(subDeploymentServiceName);
                            if(subDeploymentController != null) {
                                final DeploymentUnit subDeploymentUnit = DeploymentUnit.class.cast(subDeploymentController.getValue());
                                handleModule(context, subDeploymentUnit, deploymentNode.get("modules"), serviceRegistry);
                            }
                        }
                    }
                } else {
                    handleModule(context, deploymentUnit, deploymentNode.get("modules"), serviceRegistry);
                }
            }
        }
    }

    private void handleModule(final JndiViewExtensionContext context, final DeploymentUnit deploymentUnit, final ModelNode modulesNode, final ServiceRegistry serviceRegistry) throws OperationFailedException {
        final EEModuleDescription moduleDescription = deploymentUnit.getAttachment(org.jboss.as.ee.component.Attachments.EE_MODULE_DESCRIPTION);
        // If it isn't an EE module, just return
        if (moduleDescription == null) {
            return;
        }
        final String appName = moduleDescription.getApplicationName();
        final String moduleName = moduleDescription.getModuleName();

        final ModelNode moduleNode = modulesNode.get(moduleDescription.getModuleName());

        final ServiceName moduleContextName = ContextNames.contextServiceNameOfModule(appName, moduleName);
        final ServiceController<?> moduleContextController = serviceRegistry.getService(moduleContextName);
        if (moduleContextController != null) {
            final NamingStore moduleStore = NamingStore.class.cast(moduleContextController.getValue());
            try {
                context.addEntries(moduleNode.get("java:module"), new NamingContext(moduleStore, null));
            } catch (NamingException e) {
                throw new OperationFailedException(e, new ModelNode().set(EeLogger.ROOT_LOGGER.failedToRead("java:module", appName, moduleName)));
            }

            final Collection<ComponentDescription> componentDescriptions = moduleDescription.getComponentDescriptions();
            for (ComponentDescription componentDescription : componentDescriptions) {
                final String componentName = componentDescription.getComponentName();
                final ServiceName compContextServiceName = ContextNames.contextServiceNameOfComponent(appName, moduleName, componentName);
                final ServiceController<?> compContextController = serviceRegistry.getService(compContextServiceName);
                if (compContextController != null) {
                    final ModelNode componentNode = moduleNode.get("components").get(componentName);
                    final NamingStore compStore = NamingStore.class.cast(compContextController.getValue());
                    try {
                        context.addEntries(componentNode.get("java:comp"), new NamingContext(compStore, null));
                    } catch (NamingException e) {
                        throw new OperationFailedException(e, new ModelNode().set(EeLogger.ROOT_LOGGER.failedToRead("java:comp", appName, moduleName, componentName)));
                    }
                }
            }
        }
    }

    private String cleanName(final String name) {
        final String cleaned;
        if (name.endsWith(".war") || name.endsWith(".jar") || name.endsWith(".ear") || name.endsWith(".rar")) {
            cleaned = name.substring(0, name.length() - 4);
        } else {
            cleaned = name;
        }
        return cleaned;
    }

    public Injector<JndiViewExtensionRegistry> getRegistryInjector() {
        return registry;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7346.java