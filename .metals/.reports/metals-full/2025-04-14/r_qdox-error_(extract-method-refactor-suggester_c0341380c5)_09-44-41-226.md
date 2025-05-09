error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9613.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9613.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9613.java
text:
```scala
c@@ase STOPPING_to_DOWN: {

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

package org.jboss.as.connector.subsystems.datasources;

import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationContext.Stage;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.naming.ManagedReferenceFactory;
import org.jboss.as.naming.ServiceBasedNamingStore;
import org.jboss.as.naming.deployment.ContextNames;
import org.jboss.as.naming.service.BinderService;
import org.jboss.as.security.service.SecurityDomainService;
import org.jboss.dmr.ModelNode;
import org.jboss.jca.common.api.metadata.ds.DsSecurity;
import org.jboss.jca.common.api.validator.ValidateException;
import org.jboss.msc.service.AbstractServiceListener;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistry;
import org.jboss.msc.service.ServiceTarget;

import java.util.ArrayList;
import java.util.List;

import static org.jboss.as.connector.logging.ConnectorLogger.SUBSYSTEM_DATASOURCES_LOGGER;
import static org.jboss.as.connector.logging.ConnectorMessages.MESSAGES;
import static org.jboss.as.connector.subsystems.datasources.Constants.JNDI_NAME;
import static org.jboss.as.connector.subsystems.datasources.DataSourceModelNodeUtil.from;
import static org.jboss.as.connector.subsystems.datasources.DataSourceModelNodeUtil.xaFrom;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ENABLED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.PERSISTENT;

/**
 * Operation handler responsible for enabling an existing data-source.
 *
 * @author John Bailey
 */
public class DataSourceEnable implements OperationStepHandler {
    static final DataSourceEnable LOCAL_INSTANCE = new DataSourceEnable(false);
    static final DataSourceEnable XA_INSTANCE = new DataSourceEnable(true);

    private final boolean xa;

    public DataSourceEnable(boolean xa) {
        super();
        this.xa = xa;
    }

    public void execute(OperationContext context, ModelNode operation) {

        final ModelNode model = context.readResourceForUpdate(PathAddress.EMPTY_ADDRESS).getModel();

        // On boot we invoke this op but we may not want to store a default value the model
        boolean persist = operation.get(PERSISTENT).asBoolean(true);
        if (persist) {
            model.get(ENABLED).set(true);
        } else if (model.hasDefined(ENABLED) && !model.get(ENABLED).asBoolean()) {
            // Just clear the "false" value that gets stored by default
            model.get(ENABLED).set(new ModelNode());
        }

        if (context.isNormalServer()) {

            context.addStep(new OperationStepHandler() {
                public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
                    ServiceVerificationHandler verificationHandler = new ServiceVerificationHandler();
                    final List<ServiceController<?>> controllers = new ArrayList<ServiceController<?>>();
                    addServices(context, operation, verificationHandler, model, isXa(), controllers);
                    context.addStep(verificationHandler, Stage.VERIFY);
                    context.completeStep(new OperationContext.RollbackHandler() {
                                            @Override
                                            public void handleRollback(OperationContext context, ModelNode operation) {
                                                rollbackRuntime(context, operation, model, controllers);
                                            }
                                        });
                }
            }, OperationContext.Stage.RUNTIME);
        }
        context.stepCompleted();
    }

    static void addServices(OperationContext context, ModelNode operation, ServiceVerificationHandler verificationHandler, ModelNode model, boolean isXa, final List<ServiceController<?>> controllers) throws OperationFailedException {
        final ServiceTarget serviceTarget = context.getServiceTarget();

        final ModelNode address = operation.require(OP_ADDR);
        final String dsName = PathAddress.pathAddress(address).getLastElement().getValue();
        final String jndiName = model.get(JNDI_NAME.getName()).asString();
        final ServiceRegistry registry = context.getServiceRegistry(true);
        final List<ServiceName> serviceNames = registry.getServiceNames();


        if (isXa) {
            final ModifiableXaDataSource dataSourceConfig;
            try {
                dataSourceConfig = xaFrom(context, model, dsName);
            } catch (ValidateException e) {
                throw new OperationFailedException(e, new ModelNode().set(MESSAGES.failedToCreate("XaDataSource", operation, e.getLocalizedMessage())));
            }
            final ServiceName xaDataSourceConfigServiceName = XADataSourceConfigService.SERVICE_NAME_BASE.append(dsName);
            final XADataSourceConfigService xaDataSourceConfigService = new XADataSourceConfigService(dataSourceConfig);

            final ServiceBuilder<?> builder = serviceTarget.addService(xaDataSourceConfigServiceName, xaDataSourceConfigService);
            if (verificationHandler != null) {
                builder.addListener(verificationHandler);
            }
            // add dependency on security domain service if applicable
            final DsSecurity dsSecurityConfig = dataSourceConfig.getSecurity();
            if (dsSecurityConfig != null) {
                final String securityDomainName = dsSecurityConfig.getSecurityDomain();
                if (securityDomainName != null) {
                    builder.addDependency(SecurityDomainService.SERVICE_NAME.append(securityDomainName));
                }
            }
            int propertiesCount = 0;
            for (ServiceName name : serviceNames) {
                if (xaDataSourceConfigServiceName.append("xa-datasource-properties").isParentOf(name)) {
                    final ServiceController<?> xaConfigPropertyController = registry.getService(name);
                    XaDataSourcePropertiesService xaPropService = (XaDataSourcePropertiesService) xaConfigPropertyController.getService();

                    if (!ServiceController.State.UP.equals(xaConfigPropertyController.getState())) {
                        propertiesCount++;
                        xaConfigPropertyController.setMode(ServiceController.Mode.ACTIVE);
                        builder.addDependency(name, String.class, xaDataSourceConfigService.getXaDataSourcePropertyInjector(xaPropService.getName()));

                    } else {
                        throw new OperationFailedException(new ModelNode().set(MESSAGES.serviceAlreadyStarted("Data-source.xa-config-property", name)));
                    }
                }
            }
            if (propertiesCount == 0) {
                throw MESSAGES.xaDataSourcePropertiesNotPresent();
            }
            controllers.add(builder.install());

        } else {

            final ModifiableDataSource dataSourceConfig;
            try {
                dataSourceConfig = from(context, model,dsName);
            } catch (ValidateException e) {
                throw new OperationFailedException(e, new ModelNode().set(MESSAGES.failedToCreate("DataSource", operation, e.getLocalizedMessage())));
            }
            final ServiceName dataSourceCongServiceName = DataSourceConfigService.SERVICE_NAME_BASE.append(dsName);
            final DataSourceConfigService configService = new DataSourceConfigService(dataSourceConfig);

            final ServiceBuilder<?> builder = serviceTarget.addService(dataSourceCongServiceName, configService);
            if (verificationHandler != null) {
                builder.addListener(verificationHandler);
            }
            // add dependency on security domain service if applicable
            final DsSecurity dsSecurityConfig = dataSourceConfig.getSecurity();
            if (dsSecurityConfig != null) {
                final String securityDomainName = dsSecurityConfig.getSecurityDomain();
                if (securityDomainName != null) {
                    builder.addDependency(SecurityDomainService.SERVICE_NAME.append(securityDomainName));
                }
            }
            for (ServiceName name : serviceNames) {
                if (dataSourceCongServiceName.append("connection-properties").isParentOf(name)) {
                    final ServiceController<?> dataSourceController = registry.getService(name);
                    ConnectionPropertiesService connPropService = (ConnectionPropertiesService) dataSourceController.getService();

                    if (!ServiceController.State.UP.equals(dataSourceController.getState())) {
                        dataSourceController.setMode(ServiceController.Mode.ACTIVE);
                        builder.addDependency(name, String.class, configService.getConnectionPropertyInjector(connPropService.getName()));

                    } else {
                        throw new OperationFailedException(new ModelNode().set(MESSAGES.serviceAlreadyStarted("Data-source.connectionProperty", name)));
                    }
                }
            }
            controllers.add(builder.install());


        }

        final ServiceName dataSourceServiceName = AbstractDataSourceService.SERVICE_NAME_BASE.append(jndiName);


        final ServiceController<?> dataSourceController = registry.getService(dataSourceServiceName);

        if (dataSourceController != null) {
            if (!ServiceController.State.UP.equals(dataSourceController.getState())) {
                dataSourceController.setMode(ServiceController.Mode.ACTIVE);
            } else {
                throw new OperationFailedException(new ModelNode().set(MESSAGES.serviceAlreadyStarted("Data-source", dsName)));
            }
        } else {
            throw new OperationFailedException(new ModelNode().set(MESSAGES.serviceNotAvailable("Data-source", dsName)));
        }

        final DataSourceReferenceFactoryService referenceFactoryService = new DataSourceReferenceFactoryService();
        final ServiceName referenceFactoryServiceName = DataSourceReferenceFactoryService.SERVICE_NAME_BASE
                .append(dsName);
        final ServiceBuilder<?> referenceBuilder = serviceTarget.addService(referenceFactoryServiceName,
                referenceFactoryService).addDependency(dataSourceServiceName, javax.sql.DataSource.class,
                referenceFactoryService.getDataSourceInjector());
        if (verificationHandler != null) {
            referenceBuilder.addListener(verificationHandler);
        }

        controllers.add(referenceBuilder.install());

        final ContextNames.BindInfo bindInfo = ContextNames.bindInfoFor(jndiName);
        final BinderService binderService = new BinderService(bindInfo.getBindName());
        final ServiceBuilder<?> binderBuilder = serviceTarget
                .addService(bindInfo.getBinderServiceName(), binderService)
                .addDependency(referenceFactoryServiceName, ManagedReferenceFactory.class, binderService.getManagedObjectInjector())
                .addDependency(bindInfo.getParentContextServiceName(), ServiceBasedNamingStore.class, binderService.getNamingStoreInjector()).addListener(new AbstractServiceListener<Object>() {
                    public void transition(final ServiceController<? extends Object> controller, final ServiceController.Transition transition) {
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
        binderBuilder.setInitialMode(ServiceController.Mode.ACTIVE);
        if (verificationHandler != null) {
            binderBuilder.addListener(verificationHandler);
        }
        controllers.add(binderBuilder.install());

    }

    public static DataSourceEnable getLocalInstance() {
        return LOCAL_INSTANCE;
    }

    public boolean isXa() {
        return xa;
    }


    protected void rollbackRuntime(OperationContext context, final ModelNode operation, final ModelNode model, List<ServiceController<?>> controllers) {
        for (ServiceController<?> controller : controllers) {
            context.removeService(controller.getName());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9613.java