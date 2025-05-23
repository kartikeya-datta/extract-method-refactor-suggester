error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14599.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14599.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14599.java
text:
```scala
i@@f (!WeldDeploymentMarker.isPartOfWeldDeployment(deploymentUnit)) {

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.as.weld.deployment.processors;

import org.jboss.as.ee.beanvalidation.BeanValidationAttachments;
import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.PrivateSubDeploymentMarker;
import org.jboss.as.txn.TransactionManagerService;
import org.jboss.as.txn.UserTransactionService;
import org.jboss.as.weld.WeldContainer;
import org.jboss.as.weld.WeldDeploymentMarker;
import org.jboss.as.weld.deployment.BeanDeploymentArchiveImpl;
import org.jboss.as.weld.deployment.BeanDeploymentModule;
import org.jboss.as.weld.deployment.WeldAttachments;
import org.jboss.as.weld.deployment.WeldDeployment;
import org.jboss.as.weld.services.TCCLSingletonService;
import org.jboss.as.weld.services.WeldService;
import org.jboss.as.weld.services.bootstrap.WeldEjbInjectionServices;
import org.jboss.as.weld.services.bootstrap.WeldEjbServices;
import org.jboss.as.weld.services.bootstrap.WeldJpaInjectionServices;
import org.jboss.as.weld.services.bootstrap.WeldResourceInjectionServices;
import org.jboss.as.weld.services.bootstrap.WeldSecurityServices;
import org.jboss.as.weld.services.bootstrap.WeldTransactionServices;
import org.jboss.as.weld.services.bootstrap.WeldValidationServices;
import org.jboss.logging.Logger;
import org.jboss.modules.Module;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.weld.bootstrap.api.Environments;
import org.jboss.weld.bootstrap.spi.Metadata;
import org.jboss.weld.ejb.spi.EjbServices;
import org.jboss.weld.injection.spi.EjbInjectionServices;
import org.jboss.weld.injection.spi.JpaInjectionServices;
import org.jboss.weld.validation.spi.ValidationServices;

import javax.enterprise.inject.spi.Extension;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import javax.validation.ValidatorFactory;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Deployment processor that installs the weld services and all other required services
 *
 * @author Stuart Douglas
 *
 */
public class WeldDeploymentProcessor implements DeploymentUnitProcessor {

    private static final Logger log = Logger.getLogger("org.jboss.weld");

    @Override
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final ServiceTarget serviceTarget = phaseContext.getServiceTarget();

        if (!WeldDeploymentMarker.isWeldDeployment(deploymentUnit)) {
            return;
        }
        // we only start weld on top level deployments
        if (deploymentUnit.getParent() != null) {
            return;
        }

        log.info("Starting Services for CDI deployment: " + phaseContext.getDeploymentUnit().getName());

        final Module module = deploymentUnit.getAttachment(Attachments.MODULE);

        final Set<BeanDeploymentModule> beanDeploymentModules = new HashSet<BeanDeploymentModule>();
        final Set<BeanDeploymentModule> globalBeanDeploymentModules = new HashSet<BeanDeploymentModule>();
        final Set<BeanDeploymentArchiveImpl> beanDeploymentArchives = new HashSet<BeanDeploymentArchiveImpl>();

        // the root module only has access to itself. For most deployments this will be the only module
        // for ear deployments this represents the ear/lib directory.
        // ejb jar sub deployments have access to the root module and other ejb jars.
        // war deployments have access to the root level and all ejb jars
        final BeanDeploymentModule rootBeanDeploymentModule = deploymentUnit.getAttachment(WeldAttachments.BEAN_DEPLOYMENT_MODULE);

        final EEModuleDescription eeModuleDescription = deploymentUnit.getAttachment(org.jboss.as.ee.component.Attachments.EE_MODULE_DESCRIPTION);

        globalBeanDeploymentModules.add(rootBeanDeploymentModule);
        beanDeploymentArchives.addAll(rootBeanDeploymentModule.getBeanDeploymentArchives());
        final List<DeploymentUnit> subDeployments = deploymentUnit.getAttachmentList(Attachments.SUB_DEPLOYMENTS);

        final Set<ClassLoader> subDeploymentLoaders = new HashSet<ClassLoader>();

        for (DeploymentUnit subDeployment : subDeployments) {
            final Module subDeploymentModule = subDeployment.getAttachment(Attachments.MODULE);
            if(module != null) {
                subDeploymentLoaders.add(subDeploymentModule.getClassLoader());
            }

            final BeanDeploymentModule bdm = subDeployment.getAttachment(WeldAttachments.BEAN_DEPLOYMENT_MODULE);
            if(bdm == null) {
                continue;
            }
            // add the modules bdas to the global set of bdas
            beanDeploymentArchives.addAll(bdm.getBeanDeploymentArchives());
            if (bdm != null) {
                beanDeploymentModules.add(bdm);
                if (!PrivateSubDeploymentMarker.isPrivate(subDeployment)) {
                    globalBeanDeploymentModules.add(bdm);
                }
            }
        }

        for (BeanDeploymentModule bdm : beanDeploymentModules) {
            if (bdm == rootBeanDeploymentModule) {
                continue; // the root module only has access to itself
            }
            // otherwise add all globally accessible modules to the module
            // this will include the root module and ejb jars
            bdm.addBeanDeploymentModules(globalBeanDeploymentModules);
        }

        final List<Metadata<Extension>> extensions = deploymentUnit.getAttachmentList(WeldAttachments.PORTABLE_EXTENSIONS);

        final WeldDeployment deployment = new WeldDeployment(beanDeploymentArchives, extensions, module, subDeploymentLoaders);

        final WeldContainer weldContainer = new WeldContainer(deployment, Environments.EE_INJECT);
        //hook up validation service
        //TODO: we need to change weld so this is a per-BDA service
        final ValidatorFactory factory = deploymentUnit.getAttachment(BeanValidationAttachments.VALIDATOR_FACTORY);
        weldContainer.addWeldService(ValidationServices.class,new WeldValidationServices(factory));

        final EjbInjectionServices ejbInjectionServices = new WeldEjbInjectionServices(deploymentUnit.getServiceRegistry(),eeModuleDescription);
        weldContainer.addWeldService(EjbInjectionServices.class,ejbInjectionServices);

        weldContainer.addWeldService(EjbServices.class,new WeldEjbServices(deploymentUnit.getServiceRegistry()));


        final JpaInjectionServices rootJpaInjectionServices = new WeldJpaInjectionServices(deploymentUnit,deploymentUnit.getServiceRegistry());
        weldContainer.addWeldService(JpaInjectionServices.class,rootJpaInjectionServices);

        final WeldService weldService = new WeldService(weldContainer);
        final ServiceName weldServiceName = deploymentUnit.getServiceName().append(WeldService.SERVICE_NAME);
        // add the weld service
        final ServiceBuilder<WeldContainer> weldServiceBuilder = serviceTarget.addService(weldServiceName, weldService);

        weldServiceBuilder.addDependencies(TCCLSingletonService.SERVICE_NAME);

        installResourceInjectionService(serviceTarget, deploymentUnit, weldService, weldServiceBuilder);
        installSecurityService(serviceTarget, deploymentUnit, weldService, weldServiceBuilder);
        installTransactionService(serviceTarget, deploymentUnit, weldService, weldServiceBuilder);

        weldServiceBuilder.install();

    }


    private ServiceName installSecurityService(ServiceTarget serviceTarget, DeploymentUnit deploymentUnit,
            WeldService weldService, ServiceBuilder<WeldContainer> weldServiceBuilder) {
        final WeldSecurityServices service = new WeldSecurityServices();

        final ServiceName serviceName = deploymentUnit.getServiceName().append(WeldSecurityServices.SERVICE_NAME);

        serviceTarget.addService(serviceName, service).install();

        weldServiceBuilder.addDependency(serviceName, WeldSecurityServices.class, weldService.getSecurityServices());

        return serviceName;
    }

    private ServiceName installResourceInjectionService(ServiceTarget serviceTarget, DeploymentUnit deploymentUnit,
            WeldService weldService, ServiceBuilder<WeldContainer> weldServiceBuilder) {
        final WeldResourceInjectionServices service = new WeldResourceInjectionServices();

        final ServiceName serviceName = deploymentUnit.getServiceName().append(WeldResourceInjectionServices.SERVICE_NAME);

        serviceTarget.addService(serviceName, service).install();

        weldServiceBuilder.addDependency(serviceName, WeldResourceInjectionServices.class, weldService
                .getResourceInjectionServices());

        return serviceName;
    }

    private ServiceName installTransactionService(final ServiceTarget serviceTarget, final DeploymentUnit deploymentUnit,
            WeldService weldService, ServiceBuilder<WeldContainer> weldServiceBuilder) {
        final WeldTransactionServices weldTransactionServices = new WeldTransactionServices();

        final ServiceName weldTransactionServiceName = deploymentUnit.getServiceName().append(
                WeldTransactionServices.SERVICE_NAME);

        serviceTarget.addService(weldTransactionServiceName, weldTransactionServices).addDependency(
                TransactionManagerService.SERVICE_NAME, TransactionManager.class,
                weldTransactionServices.getInjectedTransactionManager()).addDependency(UserTransactionService.SERVICE_NAME,
                UserTransaction.class, weldTransactionServices.getInjectedTransaction()).install();

        weldServiceBuilder.addDependency(weldTransactionServiceName, WeldTransactionServices.class, weldService
                .getWeldTransactionServices());

        return weldTransactionServiceName;
    }

    @Override
    public void undeploy(DeploymentUnit context) {
        final ServiceName weldTransactionServiceName = context.getServiceName().append(WeldTransactionServices.SERVICE_NAME);
        final ServiceController<?> serviceController = context.getServiceRegistry().getService(weldTransactionServiceName);
        if (serviceController != null) {
            serviceController.setMode(ServiceController.Mode.REMOVE);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14599.java