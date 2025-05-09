error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10264.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10264.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10264.java
text:
```scala
i@@f (overrideRegistration == null || overrideRegistration.isAllowsOverride()) {

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

package org.jboss.as.connector.deployers.ds.processors;

import static org.jboss.as.connector.logging.ConnectorLogger.SUBSYSTEM_DATASOURCES_LOGGER;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.as.connector.logging.ConnectorLogger;
import org.jboss.as.connector.logging.ConnectorMessages;
import org.jboss.as.connector.services.driver.registry.DriverRegistry;
import org.jboss.as.connector.subsystems.datasources.AbstractDataSourceService;
import org.jboss.as.connector.subsystems.datasources.DataSourceReferenceFactoryService;
import org.jboss.as.connector.subsystems.datasources.DataSourceStatisticsListener;
import org.jboss.as.connector.subsystems.datasources.DataSourcesExtension;
import org.jboss.as.connector.subsystems.datasources.DataSourcesSubsystemProviders;
import org.jboss.as.connector.subsystems.datasources.LocalDataSourceService;
import org.jboss.as.connector.subsystems.datasources.ModifiableDataSource;
import org.jboss.as.connector.subsystems.datasources.ModifiableXaDataSource;
import org.jboss.as.connector.subsystems.datasources.Util;
import org.jboss.as.connector.subsystems.datasources.XMLDataSourceRuntimeHandler;
import org.jboss.as.connector.subsystems.datasources.XMLXaDataSourceRuntimeHandler;
import org.jboss.as.connector.subsystems.datasources.XaDataSourceService;
import org.jboss.as.connector.util.ConnectorServices;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.naming.ManagedReferenceFactory;
import org.jboss.as.naming.ServiceBasedNamingStore;
import org.jboss.as.naming.deployment.ContextNames;
import org.jboss.as.naming.service.BinderService;
import org.jboss.as.naming.service.NamingService;
import org.jboss.as.security.service.SubjectFactoryService;
import org.jboss.as.server.Services;
import org.jboss.as.server.deployment.DeploymentModelUtils;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.jca.common.api.metadata.Defaults;
import org.jboss.jca.common.api.metadata.ds.DataSources;
import org.jboss.jca.common.api.metadata.ds.v12.DataSource;
import org.jboss.jca.common.api.metadata.ds.v12.DsXaPool;
import org.jboss.jca.common.api.metadata.ds.v12.XaDataSource;
import org.jboss.jca.common.metadata.ds.v12.DsXaPoolImpl;
import org.jboss.jca.core.api.connectionmanager.ccm.CachedConnectionManager;
import org.jboss.jca.core.api.management.ManagementRepository;
import org.jboss.jca.core.spi.transaction.TransactionIntegration;
import org.jboss.msc.service.AbstractServiceListener;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.security.SubjectFactory;

/**
 * Picks up -ds.xml deployments
 *
 * @author <a href="mailto:jesper.pedersen@jboss.org">Jesper Pedersen</a>
 */
public class DsXmlDeploymentInstallProcessor implements DeploymentUnitProcessor {

    private static final String DATA_SOURCE = "data-source";
    private static final String XA_DATA_SOURCE = "xa-data-source";
    private static final String CONNECTION_PROPERTIES = "connection-properties";
    private static final String XA_CONNECTION_PROPERTIES = "xa-datasource-properties";

    private static final PathAddress SUBSYSTEM_ADDRESS = PathAddress.pathAddress(PathElement.pathElement(ModelDescriptionConstants.SUBSYSTEM, DataSourcesExtension.SUBSYSTEM_NAME));
    private static final PathAddress DATASOURCE_ADDRESS;
    private static final PathAddress XA_DATASOURCE_ADDRESS;

    static {
        XA_DATASOURCE_ADDRESS = SUBSYSTEM_ADDRESS.append(PathElement.pathElement(XA_DATA_SOURCE));
        DATASOURCE_ADDRESS = SUBSYSTEM_ADDRESS.append(PathElement.pathElement(DATA_SOURCE));
    }

    /**
     * Process a deployment for standard ra deployment files. Will parse the xml
     * file and attach an configuration discovered during processing.
     *
     * @param phaseContext the deployment unit context
     * @throws org.jboss.as.server.deployment.DeploymentUnitProcessingException
     *
     */
    @Override
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();

        final List<DataSources> dataSourcesList = deploymentUnit.getAttachmentList(DsXmlDeploymentParsingProcessor.DATA_SOURCES_ATTACHMENT_KEY);


        for(DataSources dataSources : dataSourcesList) {
            if (dataSources.getDrivers() != null && dataSources.getDrivers().size() > 0) {
                ConnectorLogger.DS_DEPLOYER_LOGGER.driversElementNotSupported(deploymentUnit.getName());
            }

            ServiceTarget serviceTarget = phaseContext.getServiceTarget();
            ServiceVerificationHandler verificationHandler = new ServiceVerificationHandler();

            if (dataSources.getDataSource() != null && dataSources.getDataSource().size() > 0) {
                for (int i = 0; i < dataSources.getDataSource().size(); i++) {
                    DataSource ds = (DataSource)dataSources.getDataSource().get(i);
                    if (ds.isEnabled() && ds.getDriver() != null) {
                        try {
                            final String jndiName = Util.cleanJndiName(ds.getJndiName(), ds.isUseJavaContext());
                            LocalDataSourceService lds = new LocalDataSourceService(jndiName);
                            lds.getDataSourceConfigInjector().inject(buildDataSource(ds));
                            final String dsName = ds.getJndiName();
                            final PathAddress addr = getDataSourceAddress(dsName, deploymentUnit, false);
                            installManagementModel(ds, deploymentUnit, addr);
                            startDataSource(lds, jndiName, ds.getDriver(), serviceTarget, verificationHandler,
                                    getRegistration(false, deploymentUnit), getResource(dsName, false, deploymentUnit), dsName);
                        } catch (Exception e) {
                            throw ConnectorMessages.MESSAGES.exceptionDeployingDatasource(e, ds.getJndiName());
                        }
                    } else {
                        ConnectorLogger.DS_DEPLOYER_LOGGER.debugf("Ignoring: %s", ds.getJndiName());
                    }
                }
            }

            if (dataSources.getXaDataSource() != null && dataSources.getXaDataSource().size() > 0) {
               for (int i = 0; i < dataSources.getXaDataSource().size(); i++) {
                    XaDataSource xads = (XaDataSource)dataSources.getXaDataSource().get(i);
                    if (xads.isEnabled() && xads.getDriver() != null) {
                        try {
                            String jndiName = Util.cleanJndiName(xads.getJndiName(), xads.isUseJavaContext());
                            XaDataSourceService xds = new XaDataSourceService(jndiName);
                            xds.getDataSourceConfigInjector().inject(buildXaDataSource(xads));
                            final String dsName = xads.getJndiName();
                            final PathAddress addr = getDataSourceAddress(dsName, deploymentUnit, true);
                            installManagementModel(xads, deploymentUnit, addr);
                            startDataSource(xds, jndiName, xads.getDriver(), serviceTarget, verificationHandler,
                                    getRegistration(true, deploymentUnit), getResource(dsName, true, deploymentUnit), dsName);

                        } catch (Exception e) {
                            throw ConnectorMessages.MESSAGES.exceptionDeployingDatasource(e, xads.getJndiName());
                        }
                    } else {
                        ConnectorLogger.DS_DEPLOYER_LOGGER.debugf("Ignoring %s", xads.getJndiName());
                    }
                }
            }
        }

    }

    private void installManagementModel(final DataSource ds, final DeploymentUnit deploymentUnit, final PathAddress addr) {
        XMLDataSourceRuntimeHandler.INSTANCE.registerDataSource(addr, ds);
        deploymentUnit.createDeploymentSubModel(DataSourcesExtension.SUBSYSTEM_NAME, addr.getLastElement());
        if (ds.getConnectionProperties() != null) {
            for (final Map.Entry<String, String> prop : ds.getConnectionProperties().entrySet()) {
                PathAddress registration = PathAddress.pathAddress(addr.getLastElement(), PathElement.pathElement(CONNECTION_PROPERTIES, prop.getKey()));
                createDeploymentSubModel(registration, deploymentUnit);
            }
        }
    }


    private void installManagementModel(final XaDataSource ds, final DeploymentUnit deploymentUnit, final PathAddress addr) {
        XMLXaDataSourceRuntimeHandler.INSTANCE.registerDataSource(addr, ds);
        deploymentUnit.createDeploymentSubModel(DataSourcesExtension.SUBSYSTEM_NAME, addr.getLastElement());
        if (ds.getXaDataSourceProperty() != null) {
            for (final Map.Entry<String, String> prop : ds.getXaDataSourceProperty().entrySet()) {
                PathAddress registration = PathAddress.pathAddress(addr.getLastElement(), PathElement.pathElement(XA_CONNECTION_PROPERTIES, prop.getKey()));
                createDeploymentSubModel(registration, deploymentUnit);
            }
        }
    }

    private void undeployDataSource(final DataSource ds, final DeploymentUnit deploymentUnit) {
        final PathAddress addr = getDataSourceAddress(ds.getJndiName(), deploymentUnit, false);
        XMLDataSourceRuntimeHandler.INSTANCE.unregisterDataSource(addr);
    }

    private void undeployXaDataSource(final XaDataSource ds, final DeploymentUnit deploymentUnit) {
        final PathAddress addr = getDataSourceAddress(ds.getJndiName(), deploymentUnit, true);
        XMLXaDataSourceRuntimeHandler.INSTANCE.unregisterDataSource(addr);
    }

    public void undeploy(final DeploymentUnit context) {
        final List<DataSources> dataSourcesList = context.getAttachmentList(DsXmlDeploymentParsingProcessor.DATA_SOURCES_ATTACHMENT_KEY);

        for (final DataSources dataSources : dataSourcesList) {
            if (dataSources.getDataSource() != null) {
                for (int i = 0; i < dataSources.getDataSource().size(); i++) {
                    DataSource ds = (DataSource)dataSources.getDataSource().get(i);
                    undeployDataSource(ds, context);
                }
            }
            if (dataSources.getXaDataSource() != null) {
               for (int i = 0; i < dataSources.getXaDataSource().size(); i++) {
                    XaDataSource xads = (XaDataSource)dataSources.getXaDataSource().get(i);
                    undeployXaDataSource(xads, context);
                }
            }
        }
    }


    private ModifiableDataSource buildDataSource(DataSource ds) throws org.jboss.jca.common.api.validator.ValidateException {
        return new ModifiableDataSource(ds.getConnectionUrl(),
                ds.getDriverClass(), ds.getDataSourceClass(), ds.getDriver(),
                ds.getTransactionIsolation(), ds.getConnectionProperties(), ds.getTimeOut(),
                ds.getSecurity(), ds.getStatement(), ds.getValidation(),
                ds.getUrlDelimiter(), ds.getUrlSelectorStrategyClassName(), ds.getNewConnectionSql(),
                ds.isUseJavaContext(), ds.getPoolName(), ds.isEnabled(), ds.getJndiName(),
                ds.isSpy(), ds.isUseCcm(), ds.isJTA(), ds.getPool());
    }

    private ModifiableXaDataSource buildXaDataSource(XaDataSource xads) throws org.jboss.jca.common.api.validator.ValidateException {
        final DsXaPool xaPool;
        if (xads.getXaPool() == null) {
            xaPool = new DsXaPoolImpl(Defaults.MIN_POOL_SIZE, Defaults.INITIAL_POOL_SIZE, Defaults.MAX_POOL_SIZE, Defaults.PREFILL, Defaults.USE_STRICT_MIN, Defaults.FLUSH_STRATEGY,
                                      Defaults.IS_SAME_RM_OVERRIDE, Defaults.INTERLEAVING, Defaults.PAD_XID, Defaults.WRAP_XA_RESOURCE, Defaults.NO_TX_SEPARATE_POOL, Defaults.ALLOW_MULTIPLE_USERS, null, null);
        } else {
            final DsXaPool p = xads.getXaPool();
            xaPool = new DsXaPoolImpl(getDef(p.getMinPoolSize(), Defaults.MIN_POOL_SIZE), getDef(p.getInitialPoolSize(), Defaults.INITIAL_POOL_SIZE), getDef(p.getMaxPoolSize(), Defaults.MAX_POOL_SIZE), getDef(p.isPrefill(), Defaults.PREFILL),
                    getDef(p.isUseStrictMin(), Defaults.USE_STRICT_MIN), getDef(p.getFlushStrategy(), Defaults.FLUSH_STRATEGY), getDef(p.isSameRmOverride(),
                    Defaults.IS_SAME_RM_OVERRIDE), getDef(p.isInterleaving(), Defaults.INTERLEAVING), getDef(p.isPadXid(), Defaults.PAD_XID)
                    , getDef(p.isWrapXaResource(), Defaults.WRAP_XA_RESOURCE), getDef(p.isNoTxSeparatePool(), Defaults.NO_TX_SEPARATE_POOL), getDef(p.isAllowMultipleUsers(), Defaults.ALLOW_MULTIPLE_USERS),
                    p.getCapacity(), p.getConnectionListener());
        }


        return new ModifiableXaDataSource(xads.getTransactionIsolation(),
                xads.getTimeOut(), xads.getSecurity(),
                xads.getStatement(), xads.getValidation(),
                xads.getUrlDelimiter(), xads.getUrlProperty(), xads.getUrlSelectorStrategyClassName(),
                xads.isUseJavaContext(), xads.getPoolName(), xads.isEnabled(), xads.getJndiName(),
                xads.isSpy(), xads.isUseCcm(),
                xads.getXaDataSourceProperty(), xads.getXaDataSourceClass(), xads.getDriver(),
                xads.getNewConnectionSql(), xaPool, xads.getRecovery());
    }

    private <T> T getDef(T value, T def) {
        return value != null ? value : def;
    }


    private void startDataSource(final AbstractDataSourceService dataSourceService,
                                 final String jndiName,
                                 final String driverName,
                                 final ServiceTarget serviceTarget,
                                 final ServiceVerificationHandler verificationHandler,
                                 final ManagementResourceRegistration registration,
                                 final Resource resource,
                                 final String managementName) {


        final ServiceName dataSourceServiceName = AbstractDataSourceService.SERVICE_NAME_BASE.append(jndiName);
        final ServiceBuilder<?> dataSourceServiceBuilder =
                Services.addServerExecutorDependency(
                        serviceTarget.addService(dataSourceServiceName, dataSourceService),
                        dataSourceService.getExecutorServiceInjector(), false)
                .addDependency(ConnectorServices.TRANSACTION_INTEGRATION_SERVICE, TransactionIntegration.class,
                        dataSourceService.getTransactionIntegrationInjector())
                .addDependency(ConnectorServices.MANAGEMENT_REPOSITORY_SERVICE, ManagementRepository.class,
                        dataSourceService.getManagementRepositoryInjector())
                .addDependency(SubjectFactoryService.SERVICE_NAME, SubjectFactory.class,
                        dataSourceService.getSubjectFactoryInjector())
                .addDependency(ConnectorServices.CCM_SERVICE, CachedConnectionManager.class, dataSourceService.getCcmInjector())
                .addDependency(ConnectorServices.JDBC_DRIVER_REGISTRY_SERVICE, DriverRegistry.class,
                        dataSourceService.getDriverRegistryInjector()).addDependency(NamingService.SERVICE_NAME);

        //Register an empty override model regardless of we're enabled or not - the statistics listener will add the relevant childresources
        if (registration.isAllowsOverride()) {
            ManagementResourceRegistration overrideRegistration = registration.getOverrideModel(managementName);
            if (overrideRegistration == null) {
                overrideRegistration = registration.registerOverrideModel(managementName, DataSourcesSubsystemProviders.OVERRIDE_DS_DESC);
            }
            dataSourceServiceBuilder.addListener(new DataSourceStatisticsListener(overrideRegistration, resource, managementName));
        } // else should probably throw an ISE or something

        final ServiceName driverServiceName = ServiceName.JBOSS.append("jdbc-driver", driverName.replaceAll("\\.", "_"));
        if (driverServiceName != null) {
            dataSourceServiceBuilder.addDependency(driverServiceName, Driver.class,
                    dataSourceService.getDriverInjector());
        }

        final DataSourceReferenceFactoryService referenceFactoryService = new DataSourceReferenceFactoryService();
        final ServiceName referenceFactoryServiceName = DataSourceReferenceFactoryService.SERVICE_NAME_BASE
                .append(jndiName);
        final ServiceBuilder<?> referenceBuilder = serviceTarget.addService(referenceFactoryServiceName,
                referenceFactoryService).addDependency(dataSourceServiceName, javax.sql.DataSource.class,
                referenceFactoryService.getDataSourceInjector());

        final ContextNames.BindInfo bindInfo = ContextNames.bindInfoFor(jndiName);
        final BinderService binderService = new BinderService(bindInfo.getBindName());
        final ServiceBuilder<?> binderBuilder = serviceTarget
                .addService(bindInfo.getBinderServiceName(), binderService)
                .addDependency(referenceFactoryServiceName, ManagedReferenceFactory.class, binderService.getManagedObjectInjector())
                .addDependency(bindInfo.getParentContextServiceName(), ServiceBasedNamingStore.class, binderService.getNamingStoreInjector()).addListener(new AbstractServiceListener<Object>() {
                    public void transition(final ServiceController<?> controller, final ServiceController.Transition transition) {
                        switch (transition) {
                            case STARTING_to_UP: {
                                SUBSYSTEM_DATASOURCES_LOGGER.boundDataSource(jndiName);
                                break;
                            }
                            case START_REQUESTED_to_DOWN: {
                                SUBSYSTEM_DATASOURCES_LOGGER.unboundDataSource(jndiName);
                                break;
                            }
                            case REMOVING_to_REMOVED: {
                                SUBSYSTEM_DATASOURCES_LOGGER.debugf("Removed JDBC Data-source [%s]", jndiName);
                                break;
                            }
                        }
                    }
                });

        dataSourceServiceBuilder.setInitialMode(ServiceController.Mode.ACTIVE).addListener(verificationHandler).install();
        referenceBuilder.setInitialMode(ServiceController.Mode.ACTIVE).addListener(verificationHandler).install();
        binderBuilder.setInitialMode(ServiceController.Mode.ACTIVE).addListener(verificationHandler).install();
    }

    private static PathAddress getDataSourceAddress(final String jndiName, DeploymentUnit deploymentUnit, boolean xa) {
        List<PathElement> elements = new ArrayList<PathElement>();
        if (deploymentUnit.getParent() == null) {
            elements.add(PathElement.pathElement(ModelDescriptionConstants.DEPLOYMENT, deploymentUnit.getName()));
        } else {
            elements.add(PathElement.pathElement(ModelDescriptionConstants.DEPLOYMENT, deploymentUnit.getParent().getName()));
            elements.add(PathElement.pathElement(ModelDescriptionConstants.SUBDEPLOYMENT, deploymentUnit.getName()));
        }
        elements.add(PathElement.pathElement(ModelDescriptionConstants.SUBSYSTEM, DataSourcesExtension.SUBSYSTEM_NAME));
        if (xa) {
            elements.add(PathElement.pathElement(XA_DATA_SOURCE, jndiName));
        } else {
            elements.add(PathElement.pathElement(DATA_SOURCE, jndiName));
        }
        return PathAddress.pathAddress(elements);
    }


    static ManagementResourceRegistration createDeploymentSubModel(final PathAddress address, final DeploymentUnit unit) {
        final Resource root = unit.getAttachment(DeploymentModelUtils.DEPLOYMENT_RESOURCE);
        synchronized (root) {
            final ManagementResourceRegistration registration = unit.getAttachment(DeploymentModelUtils.MUTABLE_REGISTRATION_ATTACHMENT);
            final PathAddress subsystemAddress = PathAddress.pathAddress(PathElement.pathElement(ModelDescriptionConstants.SUBSYSTEM, DataSourcesExtension.SUBSYSTEM_NAME));
            final Resource subsystem = getOrCreate(root, subsystemAddress);

            final ManagementResourceRegistration subModel = registration.getSubModel(subsystemAddress.append(address));
            if (subModel == null) {
                throw new IllegalStateException(address.toString());
            }
            getOrCreate(subsystem, address);
            return subModel;
        }
    }

    static Resource getOrCreate(final Resource parent, final PathAddress address) {
        Resource current = parent;
        for (final PathElement element : address) {
            synchronized (current) {
                if (current.hasChild(element)) {
                    current = current.requireChild(element);
                } else {
                    final Resource resource = Resource.Factory.create();
                    current.registerChild(element, resource);
                    current = resource;
                }
            }
        }
        return current;
    }

    private Resource getResource(final String dsName, final boolean xa, final DeploymentUnit unit) {
        final Resource root = unit.getAttachment(DeploymentModelUtils.DEPLOYMENT_RESOURCE);
        final String key = xa ? XA_DATA_SOURCE : DATA_SOURCE;
        final PathAddress address = PathAddress.pathAddress(PathElement.pathElement(key, dsName));
        synchronized (root) {
            final Resource subsystem = getOrCreate(root, SUBSYSTEM_ADDRESS);
            return getOrCreate(subsystem, address);
        }
    }

    private ManagementResourceRegistration getRegistration(final boolean xa, final DeploymentUnit unit) {
        final Resource root = unit.getAttachment(DeploymentModelUtils.DEPLOYMENT_RESOURCE);
        synchronized (root) {
            ManagementResourceRegistration registration = unit.getAttachment(DeploymentModelUtils.MUTABLE_REGISTRATION_ATTACHMENT);
            final PathAddress address = xa ? XA_DATASOURCE_ADDRESS : DATASOURCE_ADDRESS;
            ManagementResourceRegistration subModel = registration.getSubModel(address);
            if (subModel == null) {
                throw new IllegalStateException(address.toString());
            }
            return subModel;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10264.java