error id: <WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6520.java
<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6520.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6520.java
text:
```scala
d@@riverName.replaceAll("\\.", "_"));

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

import static org.jboss.as.connector.subsystems.datasources.Constants.DATASOURCE_DRIVER;
import static org.jboss.as.connector.subsystems.datasources.Constants.ENABLED;
import static org.jboss.as.connector.subsystems.datasources.Constants.JNDINAME;
import static org.jboss.as.connector.subsystems.datasources.Constants.USE_JAVA_CONTEXT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;

import java.sql.Driver;

import javax.sql.DataSource;

import org.jboss.as.connector.ConnectorServices;
import org.jboss.as.connector.registry.DriverRegistry;
import org.jboss.as.controller.BasicOperationResult;
import org.jboss.as.controller.ModelAddOperationHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationResult;
import org.jboss.as.controller.ResultHandler;
import org.jboss.as.controller.RuntimeTask;
import org.jboss.as.controller.RuntimeTaskContext;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.naming.ManagedReferenceFactory;
import org.jboss.as.naming.NamingStore;
import org.jboss.as.naming.deployment.ContextNames;
import org.jboss.as.naming.service.BinderService;
import org.jboss.as.naming.service.NamingService;
import org.jboss.as.security.service.SubjectFactoryService;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.jca.core.api.management.ManagementRepository;
import org.jboss.jca.core.spi.transaction.TransactionIntegration;
import org.jboss.logging.Logger;
import org.jboss.msc.service.AbstractServiceListener;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.security.SubjectFactory;

/**
 * Abstract operation handler responsible for adding a DataSource.
 * @author John Bailey
 */
public abstract class AbstractDataSourceAdd implements ModelAddOperationHandler {

    public static final Logger log = Logger.getLogger("org.jboss.as.connector.subsystems.datasources");

    @Override
    public OperationResult execute(final OperationContext context, final ModelNode operation, final ResultHandler resultHandler)
            throws OperationFailedException {
        final ModelNode subModel = context.getSubModel();

        populateModel(operation, subModel);

        // Compensating is remove
        final ModelNode address = operation.require(OP_ADDR);
        final ModelNode compensating = Util.getResourceRemoveOperation(address);
        final String rawJndiName = operation.require(JNDINAME).asString();
        final String jndiName;
        if (!rawJndiName.startsWith("java:/") && operation.hasDefined(USE_JAVA_CONTEXT)
                && operation.get(USE_JAVA_CONTEXT).asBoolean()) {
            jndiName = "java:/" + rawJndiName;
        } else {
            jndiName = rawJndiName;
        }

        if (context.getRuntimeContext() != null) {
            context.getRuntimeContext().setRuntimeTask(new RuntimeTask() {
                @Override
                public void execute(RuntimeTaskContext context) throws OperationFailedException {
                    final ServiceTarget serviceTarget = context.getServiceTarget();

                    boolean enabled = !operation.hasDefined(ENABLED) || operation.get(ENABLED).asBoolean();

                    AbstractDataSourceService dataSourceService = createDataSourceService(jndiName);

                    final ServiceName dataSourceServiceName = AbstractDataSourceService.SERVICE_NAME_BASE.append(jndiName);

                    final ServiceBuilder<?> dataSourceServiceBuilder = serviceTarget
                            .addService(dataSourceServiceName, dataSourceService)
                            .addDependency(ConnectorServices.TRANSACTION_INTEGRATION_SERVICE, TransactionIntegration.class,
                                    dataSourceService.getTransactionIntegrationInjector())
                            .addDependency(ConnectorServices.MANAGEMENT_REPOSISTORY_SERVICE, ManagementRepository.class,
                                    dataSourceService.getmanagementRepositoryInjector())
                            .addDependency(SubjectFactoryService.SERVICE_NAME, SubjectFactory.class,
                                    dataSourceService.getSubjectFactoryInjector())
                            .addDependency(ConnectorServices.JDBC_DRIVER_REGISTRY_SERVICE, DriverRegistry.class,
                                    dataSourceService.getDriverRegistryInjector()).addDependency(NamingService.SERVICE_NAME);

                    startConfigAndAddDependency(dataSourceServiceBuilder, dataSourceService, jndiName, serviceTarget, operation);

                     ModelNode node = operation.require(DATASOURCE_DRIVER);
                    final String driverName = node.asString();
                    final ServiceName driverServiceName = ServiceName.JBOSS.append("jdbc-driver",
                            driverName.replaceAll(".", "_"));
                    if (driverServiceName != null) {
                        dataSourceServiceBuilder.addDependency(driverServiceName, Driver.class,
                                dataSourceService.getDriverInjector());
                    }

                    final DataSourceReferenceFactoryService referenceFactoryService = new DataSourceReferenceFactoryService();
                    final ServiceName referenceFactoryServiceName = DataSourceReferenceFactoryService.SERVICE_NAME_BASE
                            .append(jndiName);
                    final ServiceBuilder<?> referenceBuilder = serviceTarget.addService(referenceFactoryServiceName,
                            referenceFactoryService).addDependency(dataSourceServiceName, DataSource.class,
                            referenceFactoryService.getDataSourceInjector());

                    String bindName = jndiName;
                    if (jndiName.startsWith("java:/")) {
                        bindName = jndiName.substring(6);
                    }
                    final BinderService binderService = new BinderService(bindName);
                    final ServiceName binderServiceName = ContextNames.JAVA_CONTEXT_SERVICE_NAME.append(jndiName);
                    final ServiceBuilder<?> binderBuilder = serviceTarget
                            .addService(binderServiceName, binderService)
                            .addDependency(referenceFactoryServiceName, ManagedReferenceFactory.class,
                                    binderService.getManagedObjectInjector())
                            .addDependency(ContextNames.JAVA_CONTEXT_SERVICE_NAME, NamingStore.class,
                                    binderService.getNamingStoreInjector()).addListener(new AbstractServiceListener<Object>() {
                                @Override
                                public void serviceStarted(ServiceController<?> controller) {
                                    log.infof("Bound data source [%s]", jndiName);
                                }

                                @Override
                                public void serviceStopped(ServiceController<?> serviceController) {
                                    log.infof("Unbound data source [%s]", jndiName);
                                }

                                @Override
                                public void serviceRemoved(ServiceController<?> serviceController) {
                                    log.debugf("Removed JDBC Data-source [%s]", jndiName);
                                    serviceController.removeListener(this);
                                }
                            });

                    if (enabled) {
                        dataSourceServiceBuilder.setInitialMode(ServiceController.Mode.ACTIVE).install();
                        referenceBuilder.setInitialMode(ServiceController.Mode.ACTIVE).install();
                        binderBuilder.setInitialMode(ServiceController.Mode.ACTIVE).install();
                    } else {
                        dataSourceServiceBuilder.setInitialMode(ServiceController.Mode.NEVER).install();
                        referenceBuilder.setInitialMode(ServiceController.Mode.NEVER).install();
                        binderBuilder.setInitialMode(ServiceController.Mode.NEVER).install();
                    }
                    resultHandler.handleResultComplete();
                }
            });
        } else {
            resultHandler.handleResultComplete();
        }
        return new BasicOperationResult(compensating);
    }

    protected abstract void startConfigAndAddDependency(ServiceBuilder<?> dataSourceServiceBuilder,
            AbstractDataSourceService dataSourceService, String jndiName, ServiceTarget serviceTarget, final ModelNode operation)
            throws OperationFailedException;

    protected abstract void populateModel(final ModelNode operation, final ModelNode model);

    protected abstract AbstractDataSourceService createDataSourceService(final String jndiName) throws OperationFailedException;

    static void populateAddModel(final ModelNode existingModel, final ModelNode newModel,
            final String connectionPropertiesProp, final AttributeDefinition[] attributes) {
        if (existingModel.hasDefined(connectionPropertiesProp)) {
            for (Property property : existingModel.get(connectionPropertiesProp).asPropertyList()) {
                newModel.get(connectionPropertiesProp, property.getName()).set(property.getValue().asString());
            }
        }
        for (final AttributeDefinition attribute : attributes) {
            if (existingModel.hasDefined(attribute.getName())) {
                newModel.get(attribute.getName()).set(existingModel.get(attribute.getName()));
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
	scala.meta.internal.mtags.MtagsIndexer.index(MtagsIndexer.scala:21)
	scala.meta.internal.mtags.MtagsIndexer.index$(MtagsIndexer.scala:20)
	scala.meta.internal.mtags.JavaMtags.index(JavaMtags.scala:38)
	scala.meta.internal.tvp.IndexedSymbols.javaSymbols(IndexedSymbols.scala:111)
	scala.meta.internal.tvp.IndexedSymbols.workspaceSymbolsFromPath(IndexedSymbols.scala:120)
	scala.meta.internal.tvp.IndexedSymbols.$anonfun$workspaceSymbols$2(IndexedSymbols.scala:146)
	scala.collection.concurrent.TrieMap.getOrElseUpdate(TrieMap.scala:960)
	scala.meta.internal.tvp.IndexedSymbols.$anonfun$workspaceSymbols$1(IndexedSymbols.scala:146)
	scala.meta.internal.tvp.IndexedSymbols.withTimer(IndexedSymbols.scala:71)
	scala.meta.internal.tvp.IndexedSymbols.workspaceSymbols(IndexedSymbols.scala:143)
	scala.meta.internal.tvp.FolderTreeViewProvider.$anonfun$projects$9(MetalsTreeViewProvider.scala:306)
	scala.collection.Iterator$$anon$9.next(Iterator.scala:584)
	scala.collection.Iterator$$anon$10.nextCur(Iterator.scala:594)
	scala.collection.Iterator$$anon$10.hasNext(Iterator.scala:608)
	scala.collection.Iterator$$anon$6.hasNext(Iterator.scala:477)
	scala.collection.Iterator$$anon$10.hasNext(Iterator.scala:601)
	scala.collection.Iterator$$anon$8.hasNext(Iterator.scala:562)
	scala.collection.immutable.List.prependedAll(List.scala:155)
	scala.collection.immutable.List$.from(List.scala:685)
	scala.collection.immutable.List$.from(List.scala:682)
	scala.collection.SeqFactory$Delegate.from(Factory.scala:306)
	scala.collection.immutable.Seq$.from(Seq.scala:42)
	scala.collection.IterableOnceOps.toSeq(IterableOnce.scala:1473)
	scala.collection.IterableOnceOps.toSeq$(IterableOnce.scala:1473)
	scala.collection.AbstractIterator.toSeq(Iterator.scala:1306)
	scala.meta.internal.tvp.ClasspathTreeView.children(ClasspathTreeView.scala:62)
	scala.meta.internal.tvp.FolderTreeViewProvider.getProjectRoot(MetalsTreeViewProvider.scala:390)
	scala.meta.internal.tvp.MetalsTreeViewProvider.$anonfun$children$1(MetalsTreeViewProvider.scala:84)
	scala.collection.immutable.List.map(List.scala:247)
	scala.meta.internal.tvp.MetalsTreeViewProvider.children(MetalsTreeViewProvider.scala:84)
	scala.meta.internal.metals.WorkspaceLspService.$anonfun$treeViewChildren$1(WorkspaceLspService.scala:705)
	scala.concurrent.Future$.$anonfun$apply$1(Future.scala:687)
	scala.concurrent.impl.Promise$Transformation.run(Promise.scala:467)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
	java.base/java.lang.Thread.run(Thread.java:840)
```
#### Short summary: 

QDox parse error in <WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6520.java