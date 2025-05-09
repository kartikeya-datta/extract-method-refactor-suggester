error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/165.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/165.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/165.java
text:
```scala
final E@@jbIIOPService service = new EjbIIOPService(beanMethodMap, beanRepositoryIds, homeMethodMap, homeRepositoryIds, settingsService.isUseQualifiedName(), module);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.ejb3.deployment.processors;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.jboss.as.ee.component.Attachments;
import org.jboss.as.ee.component.ComponentDescription;
import org.jboss.as.ee.component.ComponentView;
import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.ejb3.component.EJBComponent;
import org.jboss.as.ejb3.component.EJBComponentDescription;
import org.jboss.as.ejb3.component.EJBViewDescription;
import org.jboss.as.ejb3.deployment.DeploymentRepository;
import org.jboss.as.ejb3.deployment.EjbDeploymentMarker;
import org.jboss.as.ejb3.iiop.EjbIIOPService;
import org.jboss.as.ejb3.iiop.POARegistry;
import org.jboss.as.ejb3.subsystem.IIOPSettingsService;
import org.jboss.as.jacorb.deployment.JacORBDeploymentMarker;
import org.jboss.as.jacorb.rmi.AttributeAnalysis;
import org.jboss.as.jacorb.rmi.InterfaceAnalysis;
import org.jboss.as.jacorb.rmi.OperationAnalysis;
import org.jboss.as.jacorb.rmi.RMIIIOPViolationException;
import org.jboss.as.jacorb.rmi.marshal.strategy.SkeletonStrategy;
import org.jboss.as.jacorb.service.CorbaNamingService;
import org.jboss.as.jacorb.service.CorbaORBService;
import org.jboss.as.jacorb.service.CorbaPOAService;
import org.jboss.as.server.Services;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.reflect.ClassIndex;
import org.jboss.as.server.deployment.reflect.DeploymentClassIndex;
import org.jboss.as.server.deployment.reflect.DeploymentReflectionIndex;
import org.jboss.as.server.moduleservice.ServiceModuleLoader;
import org.jboss.as.txn.service.TxnServices;
import org.jboss.logging.Logger;
import org.jboss.modules.Module;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceTarget;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.PortableServer.POA;

/**
 * This is the DUP that sets up IIOP for EJB's
 *
 */
public class EjbIIOPDeploymentUnitProcessor implements DeploymentUnitProcessor {

    private static final Logger logger = Logger.getLogger(EjbIIOPDeploymentUnitProcessor.class);

    private final IIOPSettingsService settingsService;

    public EjbIIOPDeploymentUnitProcessor(final IIOPSettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @Override
    public void deploy(final DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {

        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        if(!JacORBDeploymentMarker.isJacORBDeployment(deploymentUnit)) {
            return;
        }
        //TODO: we need some way to enable this by deployment descriptor
        if(!settingsService.isEnabledByDefault()) {
            return;
        }

        final EEModuleDescription moduleDescription = deploymentUnit.getAttachment(Attachments.EE_MODULE_DESCRIPTION);
        if (!EjbDeploymentMarker.isEjbDeployment(deploymentUnit)) {
            return;
        }
        final DeploymentClassIndex classIndex = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.CLASS_INDEX);
        final DeploymentReflectionIndex deploymentReflectionIndex = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.REFLECTION_INDEX);
        final Module module = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.MODULE);
        if (moduleDescription != null) {
            for (final ComponentDescription componentDescription : moduleDescription.getComponentDescriptions()) {
                if (componentDescription instanceof EJBComponentDescription) {
                    final EJBComponentDescription ejbComponentDescription = (EJBComponentDescription) componentDescription;
                    if (ejbComponentDescription.getEjbRemoteView() != null && ejbComponentDescription.getEjbHomeView() != null) {
                        processEjb(ejbComponentDescription, classIndex, deploymentReflectionIndex, module, phaseContext.getServiceTarget());
                    }
                }
            }
        }
    }

    @Override
    public void undeploy(final DeploymentUnit context) {

    }

    private void processEjb(final EJBComponentDescription componentDescription, final DeploymentClassIndex classIndex, final DeploymentReflectionIndex deploymentReflectionIndex, final Module module, final ServiceTarget serviceTarget) {
        componentDescription.setExposedViaIiop(true);

        // Create bean method mappings for container invoker

        final EJBViewDescription remoteView = componentDescription.getEjbRemoteView();
        final ClassIndex remoteClass;
        try {
            remoteClass = classIndex.classIndex(remoteView.getViewClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not load remote interface for " + componentDescription.getEJBClassName(), e);
        }
        final EJBViewDescription homeView = componentDescription.getEjbHomeView();
        final ClassIndex homeClass;
        try {
            homeClass = classIndex.classIndex(homeView.getViewClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not load home interface for " + componentDescription.getEJBClassName(), e);
        }


        final InterfaceAnalysis remoteInterfaceAnalysis;
        try {
            //TODO: change all this to use the deployment reflection index
            remoteInterfaceAnalysis = InterfaceAnalysis.getInterfaceAnalysis(remoteClass.getModuleClass());
        } catch (RMIIIOPViolationException e) {
            throw new RuntimeException("Could not analyze remote interface for " + componentDescription.getEJBClassName(), e);
        }

        final Map<String, SkeletonStrategy> beanMethodMap = new HashMap<String, SkeletonStrategy>();

        final AttributeAnalysis[] remoteAttrs = remoteInterfaceAnalysis.getAttributes();
        for (int i = 0; i < remoteAttrs.length; i++) {
            final OperationAnalysis op = remoteAttrs[i].getAccessorAnalysis();

            logger.debug("    " + op.getJavaName() + "\n                " + op.getIDLName());
            //translate to the deployment reflection index method
            //TODO: this needs to be fixed so it just returns the correct method
            final Method method = translateMethod(deploymentReflectionIndex, op);

            beanMethodMap.put(op.getIDLName(), new SkeletonStrategy(method));
            final OperationAnalysis setop = remoteAttrs[i].getMutatorAnalysis();
            if (setop != null) {
                logger.debug("    " + op.getJavaName() + "\n                " + op.getIDLName());
                //translate to the deployment reflection index method
                //TODO: this needs to be fixed so it just returns the correct method
                final Method realSetmethod = translateMethod(deploymentReflectionIndex, setop);
                beanMethodMap.put(op.getIDLName(), new SkeletonStrategy(realSetmethod));
            }
        }

        final OperationAnalysis[] ops = remoteInterfaceAnalysis.getOperations();
        for (int i = 0; i < ops.length; i++) {
            logger.debug("    " + ops[i].getJavaName() + "\n                " + ops[i].getIDLName());
            beanMethodMap.put(ops[i].getIDLName(), new SkeletonStrategy(translateMethod(deploymentReflectionIndex, ops[i])));
        }

        // Initialize repository ids of remote interface
        final String[] beanRepositoryIds = remoteInterfaceAnalysis.getAllTypeIds();

        // Create home method mappings for container invoker
        final InterfaceAnalysis homeInterfaceAnalysis;
        try {
            //TODO: change all this to use the deployment reflection index
            homeInterfaceAnalysis = InterfaceAnalysis.getInterfaceAnalysis(homeClass.getModuleClass());
        } catch (RMIIIOPViolationException e) {
            throw new RuntimeException("Could not analyze remote interface for " + componentDescription.getEJBClassName(), e);
        }

        final Map<String, SkeletonStrategy> homeMethodMap = new HashMap<String, SkeletonStrategy>();

        final AttributeAnalysis[] attrs = homeInterfaceAnalysis.getAttributes();
        for (int i = 0; i < attrs.length; i++) {
            final OperationAnalysis op = attrs[i].getAccessorAnalysis();
            logger.debug("    " + op.getJavaName() + "\n                " + op.getIDLName());
            homeMethodMap.put(op.getIDLName(), new SkeletonStrategy(translateMethod(deploymentReflectionIndex, op)));
            final OperationAnalysis setop = attrs[i].getMutatorAnalysis();
            if (setop != null) {
                logger.debug("    " + setop.getJavaName() + "\n                " + setop.getIDLName());
                homeMethodMap.put(setop.getIDLName(), new SkeletonStrategy(translateMethod(deploymentReflectionIndex, setop)));
            }
        }

        final OperationAnalysis[] homeops = homeInterfaceAnalysis.getOperations();
        for (int i = 0; i < homeops.length; i++) {
            logger.debug("    " + homeops[i].getJavaName() + "\n                " + homeops[i].getIDLName());
            homeMethodMap.put(homeops[i].getIDLName(), new SkeletonStrategy(translateMethod(deploymentReflectionIndex, homeops[i])));
        }

        // Initialize repository ids of home interface
        final String[] homeRepositoryIds = homeInterfaceAnalysis.getAllTypeIds();

        final EjbIIOPService service = new EjbIIOPService(beanMethodMap, beanRepositoryIds, homeMethodMap, homeRepositoryIds, settingsService.isUseQualifiedName());
        final ServiceBuilder<EjbIIOPService> builder = serviceTarget.addService(componentDescription.getServiceName().append(EjbIIOPService.SERVICE_NAME), service);
        builder.addDependency(componentDescription.getCreateServiceName(), EJBComponent.class, service.getEjbComponentInjectedValue());
        builder.addDependency(homeView.getServiceName(), ComponentView.class, service.getHomeView());
        builder.addDependency(remoteView.getServiceName(), ComponentView.class, service.getRemoteView());
        builder.addDependency(CorbaORBService.SERVICE_NAME, ORB.class, service.getOrb());
        builder.addDependency(POARegistry.SERVICE_NAME, POARegistry.class, service.getPoaRegistry());
        builder.addDependency(CorbaPOAService.INTERFACE_REPOSITORY_SERVICE_NAME, POA.class, service.getIrPoa());
        builder.addDependency(CorbaNamingService.SERVICE_NAME, NamingContextExt.class, service.getCorbaNamingContext());
        builder.addDependency(DeploymentRepository.SERVICE_NAME, DeploymentRepository.class, service.getDeploymentRepository());
        builder.addDependency(Services.JBOSS_SERVICE_MODULE_LOADER, ServiceModuleLoader.class, service.getServiceModuleLoaderInjectedValue());

        //we need the arjunta transaction manager to be up, as it performs some initialization that is required by the orb interceptors
        builder.addDependency(TxnServices.JBOSS_TXN_ARJUNA_TRANSACTION_MANAGER);
        builder.install();

    }

    private Method translateMethod(final DeploymentReflectionIndex deploymentReflectionIndex, final OperationAnalysis op) {
        final Method nonMethod = op.getMethod();
        return deploymentReflectionIndex.getClassIndex(nonMethod.getDeclaringClass()).getMethod(nonMethod);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/165.java