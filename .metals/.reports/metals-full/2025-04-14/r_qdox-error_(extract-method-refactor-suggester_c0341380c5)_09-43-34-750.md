error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7022.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7022.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7022.java
text:
```scala
C@@ommonDeployment deploymentMD = ((ResourceAdapterXmlDeploymentService) controller.getService()).getRaxmlDeployment();

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

package org.jboss.as.connector.deployers.processors;

import static org.jboss.as.connector.ConnectorLogger.ROOT_LOGGER;
import static org.jboss.as.connector.ConnectorMessages.MESSAGES;

import org.jboss.as.connector.ConnectorServices;
import org.jboss.as.connector.StatisticsDescriptionProvider;
import org.jboss.as.connector.metadata.deployment.ResourceAdapterDeploymentService;
import org.jboss.as.connector.metadata.deployment.ResourceAdapterXmlDeploymentService;
import org.jboss.as.connector.metadata.xmldescriptors.ConnectorXmlDescriptor;
import org.jboss.as.connector.pool.PoolMetrics;
import org.jboss.as.connector.registry.ResourceAdapterDeploymentRegistry;
import org.jboss.as.connector.subsystems.ClearStatisticsHandler;
import org.jboss.as.connector.subsystems.jca.JcaSubsystemConfiguration;
import org.jboss.as.connector.subsystems.resourceadapters.ResourceAdaptersExtension;
import org.jboss.as.connector.subsystems.resourceadapters.ResourceAdaptersSubsystemProviders;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.naming.service.NamingService;
import org.jboss.as.security.service.SubjectFactoryService;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentModelUtils;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.jca.common.api.metadata.resourceadapter.ResourceAdapters;
import org.jboss.jca.core.api.connectionmanager.ccm.CachedConnectionManager;
import org.jboss.jca.core.api.management.ManagementRepository;
import org.jboss.jca.core.spi.mdr.MetadataRepository;
import org.jboss.jca.core.spi.rar.ResourceAdapterRepository;
import org.jboss.jca.core.spi.statistics.StatisticsPlugin;
import org.jboss.jca.core.spi.transaction.TransactionIntegration;
import org.jboss.jca.deployers.common.CommonDeployment;
import org.jboss.modules.Module;
import org.jboss.msc.service.AbstractServiceListener;
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
 * @author <a href="mailto:stefano.maestri@redhat.comdhat.com">Stefano
 *         Maestri</a>
 * @author <a href="jesper.pedersen@jboss.org">Jesper Pedersen</a>
 */
public class RaXmlDeploymentProcessor implements DeploymentUnitProcessor {

    private final MetadataRepository mdr;

    public RaXmlDeploymentProcessor(final MetadataRepository mdr) {
        this.mdr = mdr;
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
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final ManagementResourceRegistration registration = deploymentUnit.getAttachment(DeploymentModelUtils.MUTABLE_REGISTRATION_ATTACHMENT);

        final ConnectorXmlDescriptor connectorXmlDescriptor = deploymentUnit
                .getAttachment(ConnectorXmlDescriptor.ATTACHMENT_KEY);
        if (connectorXmlDescriptor == null) {
            return; // Skip non ra deployments
        }

        ResourceAdapters raxmls = null;
        final ServiceController<?> raService = phaseContext.getServiceRegistry().getService(
                ConnectorServices.RESOURCEADAPTERS_SERVICE);
        if (raService != null)
            raxmls = ((ResourceAdapters) raService.getValue());
        if (raxmls == null)
            return;

        ROOT_LOGGER.tracef("processing Raxml");
        Module module = deploymentUnit.getAttachment(Attachments.MODULE);

        if (module == null)
            throw MESSAGES.failedToGetModuleAttachment(deploymentUnit);

        try {
            final ServiceTarget serviceTarget = phaseContext.getServiceTarget();

            for (org.jboss.jca.common.api.metadata.resourceadapter.ResourceAdapter raxml : raxmls.getResourceAdapters()) {
                String deploymentUnitPrefix = "";

                if (deploymentUnit.getParent() != null) {
                    deploymentUnitPrefix = deploymentUnit.getParent().getName() + "#";
                }

                String deploymentUnitName = deploymentUnitPrefix + deploymentUnit.getName();

                if (deploymentUnitName.equals(raxml.getArchive())) {
                    final String deployment;
                    if (deploymentUnitName.lastIndexOf('.') == -1) {
                        deployment = deploymentUnitName;
                    } else {
                        deployment = deploymentUnitName.substring(0, deploymentUnitName.lastIndexOf('.'));
                    }

                    // Create the service
                    String raName = connectorXmlDescriptor.getDeploymentName();
                    ServiceName serviceName = ConnectorServices.registerDeployment(raName);

                    ResourceAdapterXmlDeploymentService service = new ResourceAdapterXmlDeploymentService(connectorXmlDescriptor,
                        raxml, module, deployment, serviceName);

                    ServiceBuilder builder = serviceTarget
                            .addService(serviceName, service)
                            .addDependency(ConnectorServices.IRONJACAMAR_MDR, MetadataRepository.class, service.getMdrInjector())
                            .addDependency(ConnectorServices.RA_REPOSISTORY_SERVICE, ResourceAdapterRepository.class,
                                    service.getRaRepositoryInjector())
                            .addDependency(ConnectorServices.MANAGEMENT_REPOSISTORY_SERVICE, ManagementRepository.class,
                                    service.getManagementRepositoryInjector())
                            .addDependency(ConnectorServices.RESOURCE_ADAPTER_REGISTRY_SERVICE,
                                    ResourceAdapterDeploymentRegistry.class, service.getRegistryInjector())
                            .addDependency(ConnectorServices.TRANSACTION_INTEGRATION_SERVICE, TransactionIntegration.class,
                                    service.getTxIntegrationInjector())
                            .addDependency(ConnectorServices.CONNECTOR_CONFIG_SERVICE, JcaSubsystemConfiguration.class,
                                    service.getConfigInjector())
                            .addDependency(SubjectFactoryService.SERVICE_NAME, SubjectFactory.class,
                                    service.getSubjectFactoryInjector())
                            .addDependency(ConnectorServices.CCM_SERVICE, CachedConnectionManager.class, service.getCcmInjector())
                            .addDependency(ConnectorServices.IDLE_REMOVER_SERVICE)
                            .addDependency(ConnectorServices.CONNECTION_VALIDATOR_SERVICE)
                            .addDependency(NamingService.SERVICE_NAME)
                            .addDependency(ConnectorServices.RESOURCE_ADAPTER_DEPLOYER_SERVICE_PREFIX.append(connectorXmlDescriptor.getDeploymentName()));
                            builder.addListener(new AbstractServiceListener<Object>() {
                public void transition(final ServiceController<? extends Object> controller,
                                       final ServiceController.Transition transition) {
                    switch (transition) {
                        case STARTING_to_UP: {

                            CommonDeployment deploymentMD = ((ResourceAdapterDeploymentService) controller.getService()).getRaDeployment();

                            if (deploymentMD.getConnectionManagers() != null && deploymentMD.getConnectionManagers()[0].getPool() != null) {
                                StatisticsPlugin poolStats = deploymentMD.getConnectionManagers()[0].getPool().getStatistics();
                                if (poolStats.getNames().size() != 0) {
                                    DescriptionProvider statsResourceDescriptionProvider = new StatisticsDescriptionProvider(ResourceAdaptersSubsystemProviders.RESOURCE_NAME, "statistics", poolStats);
                                    PathElement pe = PathElement.pathElement(ModelDescriptionConstants.SUBSYSTEM, ResourceAdaptersExtension.SUBSYSTEM_NAME);
                                    ManagementResourceRegistration subRegistration = registration.registerSubModel(pe, statsResourceDescriptionProvider);
                                    for (String statName : poolStats.getNames()) {
                                        subRegistration.registerMetric(statName, new PoolMetrics.ParametrizedPoolMetricsHandler(poolStats));
                                    }
                                    subRegistration.registerOperationHandler("clear-statistics", new ClearStatisticsHandler(poolStats), ResourceAdaptersSubsystemProviders.CLEAR_STATISTICS_DESC, false);
                                }
                            }
                            break;

                        }
                        case UP_to_STOP_REQUESTED: {

                            PathElement pe = PathElement.pathElement(ModelDescriptionConstants.SUBSYSTEM, ResourceAdaptersExtension.SUBSYSTEM_NAME);
                            if (registration.getSubModel(PathAddress.pathAddress(pe)) != null) {
                                registration.unregisterSubModel(pe);
                            }
                            break;

                        }

                    }
                }
            });


            builder.setInitialMode(Mode.ACTIVE).install();
                }
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7022.java