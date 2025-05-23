error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9415.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9415.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9415.java
text:
```scala
i@@f (!JdkORBDeploymentMarker.isJdkORBDeployment(deploymentUnit)) {

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


import static org.jboss.as.ejb3.EjbMessages.MESSAGES;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.TransactionManagementType;

import org.jboss.as.ee.component.Attachments;
import org.jboss.as.ee.component.ComponentConfiguration;
import org.jboss.as.ee.component.ComponentDescription;
import org.jboss.as.ee.component.ComponentView;
import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.ee.component.ViewConfiguration;
import org.jboss.as.ee.component.ViewConfigurator;
import org.jboss.as.ee.component.ViewDescription;
import org.jboss.as.ee.component.interceptors.InterceptorOrder;
import org.jboss.as.ejb3.logging.EjbLogger;
import org.jboss.as.ejb3.component.EJBComponent;
import org.jboss.as.ejb3.component.EJBComponentDescription;
import org.jboss.as.ejb3.component.EJBViewDescription;
import org.jboss.as.ejb3.deployment.EjbDeploymentAttachmentKeys;
import org.jboss.as.ejb3.iiop.EjbIIOPService;
import org.jboss.as.ejb3.iiop.EjbIIOPTransactionInterceptor;
import org.jboss.as.ejb3.iiop.POARegistry;
import org.jboss.as.ejb3.subsystem.IIOPSettingsService;
import org.jboss.as.jdkorb.deployment.JdkORBDeploymentMarker;
import org.jboss.as.jdkorb.rmi.AttributeAnalysis;
import org.jboss.as.jdkorb.rmi.InterfaceAnalysis;
import org.jboss.as.jdkorb.rmi.OperationAnalysis;
import org.jboss.as.jdkorb.rmi.RMIIIOPViolationException;
import org.jboss.as.jdkorb.rmi.marshal.strategy.SkeletonStrategy;
import org.jboss.as.jdkorb.service.CorbaNamingService;
import org.jboss.as.jdkorb.service.CorbaORBService;
import org.jboss.as.jdkorb.service.CorbaPOAService;
import org.jboss.as.server.Services;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.EjbDeploymentMarker;
import org.jboss.as.server.deployment.reflect.ClassIndex;
import org.jboss.as.server.deployment.reflect.DeploymentClassIndex;
import org.jboss.as.server.deployment.reflect.DeploymentReflectionIndex;
import org.jboss.as.server.moduleservice.ServiceModuleLoader;
import org.jboss.as.txn.service.TxnServices;
import org.jboss.metadata.ejb.jboss.IIOPMetaData;
import org.jboss.metadata.ejb.jboss.IORSecurityConfigMetaData;
import org.jboss.metadata.ejb.spec.EjbJarMetaData;
import org.jboss.modules.Module;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceTarget;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.PortableServer.POA;

/**
 * This is the DUP that sets up IIOP for EJB's
 */
public class EjbIIOPDeploymentUnitProcessor implements DeploymentUnitProcessor {

    private final IIOPSettingsService settingsService;

    public EjbIIOPDeploymentUnitProcessor(final IIOPSettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @Override
    public void deploy(final DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {


        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        if (!JdkORBDeploymentMarker.isJacORBDeployment(deploymentUnit)) {
            return;
        }

        final EEModuleDescription moduleDescription = deploymentUnit.getAttachment(Attachments.EE_MODULE_DESCRIPTION);
        if (!EjbDeploymentMarker.isEjbDeployment(deploymentUnit)) {
            return;
        }


        // a bean-name -> IIOPMetaData map, reflecting the assembly descriptor IIOP configuration.
        Map<String, IIOPMetaData> iiopMetaDataMap = new HashMap<String, IIOPMetaData>();
        final EjbJarMetaData ejbMetaData = deploymentUnit.getAttachment(EjbDeploymentAttachmentKeys.EJB_JAR_METADATA);
        if (ejbMetaData != null && ejbMetaData.getAssemblyDescriptor() != null) {
            List<IIOPMetaData> iiopMetaDatas = ejbMetaData.getAssemblyDescriptor().getAny(IIOPMetaData.class);
            if (iiopMetaDatas != null && iiopMetaDatas.size() > 0) {
                for (IIOPMetaData metaData : iiopMetaDatas) {
                    iiopMetaDataMap.put(metaData.getEjbName(), metaData);
                }
            }
        }

        final DeploymentClassIndex classIndex = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.CLASS_INDEX);
        final DeploymentReflectionIndex deploymentReflectionIndex = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.REFLECTION_INDEX);
        final Module module = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.MODULE);
        if (moduleDescription != null) {
            for (final ComponentDescription componentDescription : moduleDescription.getComponentDescriptions()) {
                if (componentDescription instanceof EJBComponentDescription) {
                    final EJBComponentDescription ejbComponentDescription = (EJBComponentDescription) componentDescription;
                    if (ejbComponentDescription.getEjbRemoteView() != null && ejbComponentDescription.getEjbHomeView() != null) {
                        // check if there is IIOP metadata for the bean - first using the bean name, then the wildcard "*" if needed.
                        IIOPMetaData iiopMetaData = iiopMetaDataMap.get(ejbComponentDescription.getEJBName());
                        if (iiopMetaData == null) {
                            iiopMetaData = iiopMetaDataMap.get(IIOPMetaData.WILDCARD_BEAN_NAME);
                        }
                        // the bean will be exposed via IIOP if it has IIOP metadata that applies to it or if IIOP access
                        // has been enabled by default in the EJB3 subsystem.
                        if (iiopMetaData != null || settingsService.isEnabledByDefault()) {
                            processEjb(ejbComponentDescription, classIndex, deploymentReflectionIndex, module,
                                    phaseContext.getServiceTarget(), iiopMetaData);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void undeploy(final DeploymentUnit context) {

    }

    private void processEjb(final EJBComponentDescription componentDescription, final DeploymentClassIndex classIndex,
                            final DeploymentReflectionIndex deploymentReflectionIndex, final Module module,
                            final ServiceTarget serviceTarget, final IIOPMetaData iiopMetaData) {
        componentDescription.setExposedViaIiop(true);


        // Create bean method mappings for container invoker


        final EJBViewDescription remoteView = componentDescription.getEjbRemoteView();
        final ClassIndex remoteClass;
        try {
            remoteClass = classIndex.classIndex(remoteView.getViewClassName());
        } catch (ClassNotFoundException e) {
            throw EjbLogger.ROOT_LOGGER.failedToLoadViewClassForComponent(e, componentDescription.getEJBClassName());
        }
        final EJBViewDescription homeView = componentDescription.getEjbHomeView();
        final ClassIndex homeClass;
        try {
            homeClass = classIndex.classIndex(homeView.getViewClassName());
        } catch (ClassNotFoundException e) {
            throw EjbLogger.ROOT_LOGGER.failedToLoadViewClassForComponent(e, componentDescription.getEJBClassName());
        }


        componentDescription.getEjbHomeView().getConfigurators().add(new IIOPInterceptorViewConfigurator());
        componentDescription.getEjbRemoteView().getConfigurators().add(new IIOPInterceptorViewConfigurator());


        final InterfaceAnalysis remoteInterfaceAnalysis;
        try {
            //TODO: change all this to use the deployment reflection index
            remoteInterfaceAnalysis = InterfaceAnalysis.getInterfaceAnalysis(remoteClass.getModuleClass());
        } catch (RMIIIOPViolationException e) {
            throw EjbLogger.ROOT_LOGGER.failedToAnalyzeRemoteInterface(e, componentDescription.getComponentName());
        }

        final Map<String, SkeletonStrategy> beanMethodMap = new HashMap<String, SkeletonStrategy>();

        final AttributeAnalysis[] remoteAttrs = remoteInterfaceAnalysis.getAttributes();
        for (int i = 0; i < remoteAttrs.length; i++) {
            final OperationAnalysis op = remoteAttrs[i].getAccessorAnalysis();
            if (op != null) {
                EjbLogger.ROOT_LOGGER.debugf("    %s\n                %s", op.getJavaName(), op.getIDLName());
                //translate to the deployment reflection index method
                //TODO: this needs to be fixed so it just returns the correct method
                final Method method = translateMethod(deploymentReflectionIndex, op);

                beanMethodMap.put(op.getIDLName(), new SkeletonStrategy(method));
                final OperationAnalysis setop = remoteAttrs[i].getMutatorAnalysis();
                if (setop != null) {
                    EjbLogger.ROOT_LOGGER.debugf("    %s\n                %s", setop.getJavaName(), setop.getIDLName());
                    //translate to the deployment reflection index method
                    //TODO: this needs to be fixed so it just returns the correct method
                    final Method realSetmethod = translateMethod(deploymentReflectionIndex, setop);
                    beanMethodMap.put(setop.getIDLName(), new SkeletonStrategy(realSetmethod));
                }
            }
        }

        final OperationAnalysis[] ops = remoteInterfaceAnalysis.getOperations();
        for (int i = 0; i < ops.length; i++) {
            EjbLogger.ROOT_LOGGER.debugf("    %s\n                %s", ops[i].getJavaName(), ops[i].getIDLName());
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
            throw EjbLogger.ROOT_LOGGER.failedToAnalyzeRemoteInterface(e, componentDescription.getComponentName());
        }

        final Map<String, SkeletonStrategy> homeMethodMap = new HashMap<String, SkeletonStrategy>();

        final AttributeAnalysis[] attrs = homeInterfaceAnalysis.getAttributes();
        for (int i = 0; i < attrs.length; i++) {
            final OperationAnalysis op = attrs[i].getAccessorAnalysis();
            if (op != null) {
                EjbLogger.ROOT_LOGGER.debugf("    %s\n                %s", op.getJavaName(), op.getIDLName());
                homeMethodMap.put(op.getIDLName(), new SkeletonStrategy(translateMethod(deploymentReflectionIndex, op)));
                final OperationAnalysis setop = attrs[i].getMutatorAnalysis();
                if (setop != null) {
                    EjbLogger.ROOT_LOGGER.debugf("    %s\n                %s", setop.getJavaName(), setop.getIDLName());
                    homeMethodMap.put(setop.getIDLName(), new SkeletonStrategy(translateMethod(deploymentReflectionIndex, setop)));
                }
            }
        }

        final OperationAnalysis[] homeops = homeInterfaceAnalysis.getOperations();
        for (int i = 0; i < homeops.length; i++) {
            EjbLogger.ROOT_LOGGER.debugf("    %s\n                %s", homeops[i].getJavaName(), homeops[i].getIDLName());
            homeMethodMap.put(homeops[i].getIDLName(), new SkeletonStrategy(translateMethod(deploymentReflectionIndex, homeops[i])));
        }

        // Initialize repository ids of home interface
        final String[] homeRepositoryIds = homeInterfaceAnalysis.getAllTypeIds();


        final EjbIIOPService service = new EjbIIOPService(beanMethodMap, beanRepositoryIds, homeMethodMap, homeRepositoryIds,
                settingsService.isUseQualifiedName(), iiopMetaData, module);
        final ServiceBuilder<EjbIIOPService> builder = serviceTarget.addService(componentDescription.getServiceName().append(EjbIIOPService.SERVICE_NAME), service);
        builder.addDependency(componentDescription.getCreateServiceName(), EJBComponent.class, service.getEjbComponentInjectedValue());
        builder.addDependency(homeView.getServiceName(), ComponentView.class, service.getHomeView());
        builder.addDependency(remoteView.getServiceName(), ComponentView.class, service.getRemoteView());
        builder.addDependency(CorbaORBService.SERVICE_NAME, ORB.class, service.getOrb());
        builder.addDependency(POARegistry.SERVICE_NAME, POARegistry.class, service.getPoaRegistry());
        builder.addDependency(CorbaPOAService.INTERFACE_REPOSITORY_SERVICE_NAME, POA.class, service.getIrPoa());
        builder.addDependency(CorbaNamingService.SERVICE_NAME, NamingContextExt.class, service.getCorbaNamingContext());
        builder.addDependency(IORSecConfigMetaDataService.SERVICE_NAME, IORSecurityConfigMetaData.class, service.getIORSecConfigMetaDataInjectedValue());
        builder.addDependency(Services.JBOSS_SERVICE_MODULE_LOADER, ServiceModuleLoader.class, service.getServiceModuleLoaderInjectedValue());

        //we need the arjunta transaction manager to be up, as it performs some initialization that is required by the orb interceptors
        builder.addDependency(TxnServices.JBOSS_TXN_ARJUNA_TRANSACTION_MANAGER);
        builder.install();

    }

    private Method translateMethod(final DeploymentReflectionIndex deploymentReflectionIndex, final OperationAnalysis op) {
        final Method nonMethod = op.getMethod();
        return deploymentReflectionIndex.getClassIndex(nonMethod.getDeclaringClass()).getMethod(nonMethod);
    }

    private static class IIOPInterceptorViewConfigurator implements ViewConfigurator {
        @Override
        public void configure(final DeploymentPhaseContext context, final ComponentConfiguration componentConfiguration, final ViewDescription description, final ViewConfiguration configuration) throws DeploymentUnitProcessingException {
            final EJBComponentDescription ejbComponentDescription = (EJBComponentDescription) componentConfiguration.getComponentDescription();
            if (ejbComponentDescription.getTransactionManagementType() == TransactionManagementType.CONTAINER) {
                configuration.addViewInterceptor(EjbIIOPTransactionInterceptor.FACTORY, InterceptorOrder.View.EJB_IIOP_TRANSACTION);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9415.java