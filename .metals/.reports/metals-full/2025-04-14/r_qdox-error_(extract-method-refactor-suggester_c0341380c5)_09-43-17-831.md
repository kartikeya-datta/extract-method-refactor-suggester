error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5990.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5990.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5990.java
text:
```scala
b@@uilder.addListener(new AbstractResourceAdapterDeploymentServiceListener(registration, deploymentUnit.getName(), deploymentResource, bootstrapCtx, deploymentUnit.getName(), false) {

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

package org.jboss.as.connector.deployers.ra.processors;

import static org.jboss.as.connector.logging.ConnectorLogger.DEPLOYMENT_CONNECTOR_LOGGER;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import org.jboss.as.connector.annotations.repository.jandex.JandexAnnotationRepositoryImpl;
import org.jboss.as.connector.logging.ConnectorLogger;
import org.jboss.as.connector.metadata.xmldescriptors.ConnectorXmlDescriptor;
import org.jboss.as.connector.metadata.xmldescriptors.IronJacamarXmlDescriptor;
import org.jboss.as.connector.services.mdr.AS7MetadataRepository;
import org.jboss.as.connector.services.resourceadapters.deployment.ResourceAdapterDeploymentService;
import org.jboss.as.connector.services.resourceadapters.deployment.registry.ResourceAdapterDeploymentRegistry;
import org.jboss.as.connector.subsystems.jca.JcaSubsystemConfiguration;
import org.jboss.as.connector.subsystems.resourceadapters.IronJacamarResourceCreator;
import org.jboss.as.connector.subsystems.resourceadapters.IronJacamarResourceDefinition;
import org.jboss.as.connector.util.ConnectorServices;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.descriptions.OverrideDescriptionProvider;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.naming.service.NamingService;
import org.jboss.as.security.service.SubjectFactoryService;
import org.jboss.as.server.Services;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentModelUtils;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.annotation.AnnotationIndexUtils;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.jboss.dmr.ModelNode;
import org.jboss.jandex.Index;
import org.jboss.jca.common.annotations.Annotations;
import org.jboss.jca.common.api.metadata.resourceadapter.Activation;
import org.jboss.jca.common.api.metadata.spec.Connector;
import org.jboss.jca.common.metadata.merge.Merger;
import org.jboss.jca.common.spi.annotations.repository.AnnotationRepository;
import org.jboss.jca.core.api.connectionmanager.ccm.CachedConnectionManager;
import org.jboss.jca.core.api.management.ManagementRepository;
import org.jboss.jca.core.spi.rar.ResourceAdapterRepository;
import org.jboss.jca.core.spi.transaction.TransactionIntegration;
import org.jboss.jca.deployers.common.CommonDeployment;
import org.jboss.modules.Module;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.security.SubjectFactory;

/**
 * DeploymentUnitProcessor responsible for using IronJacamar metadata and create
 * service for ResourceAdapter.
 *
 * @author <a href="mailto:stefano.maestri@redhat.com">Stefano Maestri</a>
 * @author <a href="jesper.pedersen@jboss.org">Jesper Pedersen</a>
 */
public class ParsedRaDeploymentProcessor implements DeploymentUnitProcessor {

    public ParsedRaDeploymentProcessor() {
    }

    /**
     * Process a deployment for a Connector. Will install a {@Code
     * JBossService} for this ResourceAdapter.
     *
     * @param phaseContext the deployment unit context
     * @throws DeploymentUnitProcessingException
     *
     */
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final ConnectorXmlDescriptor connectorXmlDescriptor = phaseContext.getDeploymentUnit().getAttachment(ConnectorXmlDescriptor.ATTACHMENT_KEY);
        final ManagementResourceRegistration registration;
        final ManagementResourceRegistration baseRegistration = phaseContext.getDeploymentUnit().getAttachment(DeploymentModelUtils.MUTABLE_REGISTRATION_ATTACHMENT);
        final Resource deploymentResource = phaseContext.getDeploymentUnit().getAttachment(DeploymentModelUtils.DEPLOYMENT_RESOURCE);

        if (connectorXmlDescriptor == null) {
            return;
        }
        final ServiceTarget serviceTarget = phaseContext.getServiceTarget();

        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        if (deploymentUnit.getParent() != null) {
            registration = baseRegistration.getSubModel(PathAddress.pathAddress(PathElement.pathElement("subdeployment")));
        } else {
            registration = baseRegistration;
        }
        final IronJacamarXmlDescriptor ironJacamarXmlDescriptor = deploymentUnit
                .getAttachment(IronJacamarXmlDescriptor.ATTACHMENT_KEY);

        final Module module = deploymentUnit.getAttachment(Attachments.MODULE);
        if (module == null)
            throw ConnectorLogger.ROOT_LOGGER.failedToGetModuleAttachment(phaseContext.getDeploymentUnit());

        DEPLOYMENT_CONNECTOR_LOGGER.debugf("ParsedRaDeploymentProcessor: Processing=%s", deploymentUnit);

        final ClassLoader classLoader = module.getClassLoader();

        Map<ResourceRoot, Index> annotationIndexes = AnnotationIndexUtils.getAnnotationIndexes(deploymentUnit);


        ServiceBuilder builder = process(connectorXmlDescriptor, ironJacamarXmlDescriptor, classLoader, serviceTarget, annotationIndexes, deploymentUnit.getServiceName());
        if (builder != null) {
            String bootstrapCtx = null;

            if (ironJacamarXmlDescriptor != null && ironJacamarXmlDescriptor.getIronJacamar() != null && ironJacamarXmlDescriptor.getIronJacamar().getBootstrapContext() != null)
                bootstrapCtx = ironJacamarXmlDescriptor.getIronJacamar().getBootstrapContext();

            if (bootstrapCtx == null)
                bootstrapCtx = "default";
            //Register an empty override model regardless of we're enabled or not - the statistics listener will add the relevant childresources
            if (registration.isAllowsOverride() && registration.getOverrideModel(deploymentUnit.getName()) == null) {
                registration.registerOverrideModel(deploymentUnit.getName(), new OverrideDescriptionProvider() {
                    @Override
                    public Map<String, ModelNode> getAttributeOverrideDescriptions(Locale locale) {
                        return Collections.emptyMap();
                    }

                    @Override
                    public Map<String, ModelNode> getChildTypeOverrideDescriptions(Locale locale) {
                        return Collections.emptyMap();
                    }
                });

            }
            builder.addListener(new AbstractResourceAdapterDeploymentServiceListener(registration, deploymentUnit.getName(), deploymentResource, bootstrapCtx, deploymentUnit.getName()) {

                @Override
                protected void registerIronjacamar(final ServiceController<? extends Object> controller, final ManagementResourceRegistration subRegistration, final Resource subsystemResource) {
                    //register ironJacamar
                    try {
                        subRegistration.registerSubModel(new IronJacamarResourceDefinition());
                    } catch (IllegalArgumentException iae) {
                        //ignore it: submodel already registered
                    }
                    AS7MetadataRepository mdr = ((ResourceAdapterDeploymentService) controller.getService()).getMdr();
                    IronJacamarResourceCreator.INSTANCE.execute(subsystemResource, mdr);
                }

                @Override
                protected CommonDeployment getDeploymentMetadata(final ServiceController<? extends Object> controller) {
                    return ((ResourceAdapterDeploymentService) controller.getService()).getRaDeployment();
                }
            });


            builder.setInitialMode(Mode.ACTIVE).install();
        }
    }


    public static ServiceBuilder process(final ConnectorXmlDescriptor connectorXmlDescriptor, final IronJacamarXmlDescriptor ironJacamarXmlDescriptor, final ClassLoader classLoader, final ServiceTarget serviceTarget, final Map<ResourceRoot, Index> annotationIndexes, final ServiceName duServiceName) throws DeploymentUnitProcessingException {

        Connector cmd = connectorXmlDescriptor != null ? connectorXmlDescriptor.getConnector() : null;
        final Activation activation = ironJacamarXmlDescriptor != null ? ironJacamarXmlDescriptor.getIronJacamar() : null;

        try {
            // Annotation merging
            Annotations annotator = new Annotations();

            if (annotationIndexes != null && annotationIndexes.size() > 0) {
                DEPLOYMENT_CONNECTOR_LOGGER.debugf("ParsedRaDeploymentProcessor: Found %d annotationIndexes", annotationIndexes.size());
                for (Index index : annotationIndexes.values()) {
                    // Don't apply any empty indexes, as IronJacamar doesn't like that atm.
                    if (index.getKnownClasses() != null && index.getKnownClasses().size() > 0) {
                        AnnotationRepository repository = new JandexAnnotationRepositoryImpl(index, classLoader);
                        cmd = annotator.merge(cmd, repository, classLoader);
                        DEPLOYMENT_CONNECTOR_LOGGER.debugf("ParsedRaDeploymentProcessor: CMD=%s", cmd);
                    }
                }
            }
            if (annotationIndexes == null || annotationIndexes.size() == 0)
                DEPLOYMENT_CONNECTOR_LOGGER.debugf("ParsedRaDeploymentProcessor: Found 0 annotationIndexes");

            // FIXME: when the connector is null the Iron Jacamar data is ignored
            if (cmd != null) {
                // Validate metadata
                cmd.validate();

                // Merge metadata
                cmd = (new Merger()).mergeConnectorWithCommonIronJacamar(activation, cmd);
            }

            final ServiceName deployerServiceName = ConnectorServices.RESOURCE_ADAPTER_DEPLOYER_SERVICE_PREFIX.append(connectorXmlDescriptor.getDeploymentName());
            final ResourceAdapterDeploymentService raDeploymentService = new ResourceAdapterDeploymentService(connectorXmlDescriptor, cmd, activation, classLoader, deployerServiceName, duServiceName);


            // Create the service
            ServiceBuilder builder =
                    Services.addServerExecutorDependency(
                        serviceTarget.addService(deployerServiceName, raDeploymentService),
                        raDeploymentService.getExecutorServiceInjector(), false)
                    .addDependency(ConnectorServices.IRONJACAMAR_MDR, AS7MetadataRepository.class, raDeploymentService.getMdrInjector())
                    .addDependency(ConnectorServices.RA_REPOSITORY_SERVICE, ResourceAdapterRepository.class, raDeploymentService.getRaRepositoryInjector())
                    .addDependency(ConnectorServices.MANAGEMENT_REPOSITORY_SERVICE, ManagementRepository.class, raDeploymentService.getManagementRepositoryInjector())
                    .addDependency(ConnectorServices.RESOURCE_ADAPTER_REGISTRY_SERVICE, ResourceAdapterDeploymentRegistry.class, raDeploymentService.getRegistryInjector())
                    .addDependency(ConnectorServices.TRANSACTION_INTEGRATION_SERVICE, TransactionIntegration.class, raDeploymentService.getTxIntegrationInjector())
                    .addDependency(ConnectorServices.CONNECTOR_CONFIG_SERVICE, JcaSubsystemConfiguration.class, raDeploymentService.getConfigInjector())
                    .addDependency(SubjectFactoryService.SERVICE_NAME, SubjectFactory.class, raDeploymentService.getSubjectFactoryInjector())
                    .addDependency(ConnectorServices.CCM_SERVICE, CachedConnectionManager.class, raDeploymentService.getCcmInjector())
                    .addDependency(ConnectorServices.IDLE_REMOVER_SERVICE)
                    .addDependency(ConnectorServices.CONNECTION_VALIDATOR_SERVICE)
                    .addDependency(NamingService.SERVICE_NAME);

            return builder;
        } catch (Throwable t) {
            throw new DeploymentUnitProcessingException(t);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5990.java