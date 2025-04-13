error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15013.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15013.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15013.java
text:
```scala
final S@@erviceName clientMappingRegistryServiceName = ServiceName.JBOSS.append("ejb").append("remoting").append("cluster-registry-service");

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
package org.jboss.as.ejb3.subsystem;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;
import static org.jboss.as.ejb3.subsystem.EJB3SubsystemModel.*;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.jboss.as.clustering.GroupMembershipNotifierRegistry;
import org.jboss.as.clustering.infinispan.subsystem.EmbeddedCacheManagerService;
import org.jboss.as.clustering.registry.Registry;
import org.jboss.as.clustering.registry.RegistryService;
import org.jboss.as.controller.AbstractBoottimeAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.ejb3.cache.impl.backing.clustering.GroupMembershipNotifierRegistryService;
import org.jboss.as.ejb3.deployment.DeploymentRepository;
import org.jboss.as.ejb3.remote.EJBRemoteConnectorService;
import org.jboss.as.ejb3.remote.EJBRemoteTransactionsRepository;
import org.jboss.as.ejb3.remote.EJBRemotingConnectorClientMappingService;
import org.jboss.as.network.ClientMapping;
import org.jboss.as.remoting.AbstractStreamServerService;
import org.jboss.as.remoting.RemotingServices;
import org.jboss.as.txn.service.TransactionManagerService;
import org.jboss.as.txn.service.UserTransactionService;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.remoting3.Endpoint;

/**
 * A {@link AbstractBoottimeAddStepHandler} to handle the add operation for the EJB
 * remote service, in the EJB subsystem
 *
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
public class EJB3RemoteServiceAdd extends AbstractBoottimeAddStepHandler {
    static final EJB3RemoteServiceAdd INSTANCE = new EJB3RemoteServiceAdd();

    static final String DEFAULT_CLIENT_MAPPINGS_CACHE_CONTAINER_REF = "sfsb";
    static final String DEFAULT_CLIENT_MAPPINGS_CACHE_REF = "ejb-remote-connector-client-mappings";

    private EJB3RemoteServiceAdd() {
    }

    static ModelNode create(final String connectorName, final String threadPoolName, final String clientMappingCacheContainerRef,
                            final String clientMappingCacheRef) {
        // set the address for this operation
        final ModelNode address = new ModelNode();
        address.add(SUBSYSTEM, EJB3Extension.SUBSYSTEM_NAME);
        address.add(SERVICE, REMOTE);

        ModelNode operation = new ModelNode();
        operation.get(OP).set(ADD);
        operation.get(OP_ADDR).set(address);

        operation.get(CONNECTOR_REF).set(connectorName);
        operation.get(THREAD_POOL_NAME).set(threadPoolName);
        operation.get(CLIENT_MAPPINGS_CACHE_CONTAINER_REF).set(clientMappingCacheContainerRef);
        operation.get(CLIENT_MAPPINGS_CACHE_REF).set(clientMappingCacheRef);

        return operation;
    }

    // TODO why is this a boottime-only handler?
    @Override
    protected void performBoottime(OperationContext context, ModelNode operation, ModelNode model, ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers) throws OperationFailedException {
        newControllers.addAll(installRuntimeServices(context, model, verificationHandler));
        // add ejb remote transactions repository service
        final EJBRemoteTransactionsRepository transactionsRepository = new EJBRemoteTransactionsRepository();
        final ServiceTarget serviceTarget = context.getServiceTarget();
        final ServiceController transactionRepositoryServiceController = serviceTarget.addService(EJBRemoteTransactionsRepository.SERVICE_NAME, transactionsRepository)
                .addDependency(TransactionManagerService.SERVICE_NAME, TransactionManager.class, transactionsRepository.getTransactionManagerInjector())
                .addDependency(UserTransactionService.SERVICE_NAME, UserTransaction.class, transactionsRepository.getUserTransactionInjector())
                .setInitialMode(ServiceController.Mode.ACTIVE)
                .install();
        newControllers.add(transactionRepositoryServiceController);
    }

    Collection<ServiceController<?>> installRuntimeServices(final OperationContext context, final ModelNode model, final ServiceVerificationHandler verificationHandler) {
        final String connectorName = model.require(CONNECTOR_REF).asString();
        final String threadPoolName = model.require(THREAD_POOL_NAME).asString();
        String clientMappingCacheContainerRef = DEFAULT_CLIENT_MAPPINGS_CACHE_CONTAINER_REF;
        if (model.hasDefined(CLIENT_MAPPINGS_CACHE_CONTAINER_REF)) {
            clientMappingCacheContainerRef = model.get(CLIENT_MAPPINGS_CACHE_CONTAINER_REF).asString();
        }
        String clientMappingCacheRef = DEFAULT_CLIENT_MAPPINGS_CACHE_REF;
        if (model.hasDefined(CLIENT_MAPPINGS_CACHE_REF)) {
            clientMappingCacheRef = model.get(CLIENT_MAPPINGS_CACHE_REF).asString();
        }
        final ServiceName remotingServerServiceName = RemotingServices.serverServiceName(connectorName);

        final List<ServiceController<?>> services = new ArrayList<ServiceController<?>>();
        final ServiceTarget serviceTarget = context.getServiceTarget();

        // Install the client-mapping service for the remoting connector
        final EJBRemotingConnectorClientMappingService clientMappingEntryProviderService = new EJBRemotingConnectorClientMappingService();
        final ServiceBuilder clusterMappingServiceServiceBuilder = serviceTarget.addService(EJBRemotingConnectorClientMappingService.SERVICE_NAME, clientMappingEntryProviderService)
                .addDependency(remotingServerServiceName, AbstractStreamServerService.class, clientMappingEntryProviderService.getRemotingServerInjector());
        if (verificationHandler != null) {
            clusterMappingServiceServiceBuilder.addListener(verificationHandler);
        }

        // Install the clustered registry service backed by the client-mapping registry entry provider
        final RegistryService<String, List<ClientMapping>> clientMappingRegistryService = new RegistryService<String, List<ClientMapping>>(clientMappingEntryProviderService.getRegistryEntryProvider());
        final ServiceName clientMappingRegistryServiceName = ServiceName.JBOSS.of("ejb").append("remoting").append("cluster-registry-service");
        // Form the client-mapping cache's ServiceName
        final ServiceName clientMappingCacheContainerServiceName = EmbeddedCacheManagerService.getServiceName(clientMappingCacheContainerRef);
        final ServiceName clientMappingCacheServiceName = clientMappingCacheContainerServiceName.append(clientMappingCacheRef);
        final ServiceBuilder registryServiceBuilder = clientMappingRegistryService.build(serviceTarget, clientMappingRegistryServiceName, clientMappingCacheServiceName);
        if (verificationHandler != null) {
            registryServiceBuilder.addListener(verificationHandler);
        }

        // Install the EJB remoting connector service which will listen for client connections on the remoting channel
        // TODO: Externalize (expose via management API if needed) the version and the marshalling strategy
        final EJBRemoteConnectorService service = new EJBRemoteConnectorService((byte) 0x01, new String[]{"river"});
        final ServiceBuilder<EJBRemoteConnectorService> ejbRemoteConnectorServiceBuilder = serviceTarget.addService(EJBRemoteConnectorService.SERVICE_NAME, service);
        // add dependency on the Remoting subsytem endpoint
        ejbRemoteConnectorServiceBuilder.addDependency(RemotingServices.SUBSYSTEM_ENDPOINT, Endpoint.class, service.getEndpointInjector());
        // add dependency on the remoting server (which allows remoting connector to connect to it)
        ejbRemoteConnectorServiceBuilder.addDependency(RemotingServices.serverServiceName(connectorName));
        // add rest of the dependencies
        ejbRemoteConnectorServiceBuilder.addDependency(EJB3ThreadPoolAdd.BASE_SERVICE_NAME.append(threadPoolName), ExecutorService.class, service.getExecutorService())
                .addDependency(DeploymentRepository.SERVICE_NAME, DeploymentRepository.class, service.getDeploymentRepositoryInjector())
                .addDependency(EJBRemoteTransactionsRepository.SERVICE_NAME, EJBRemoteTransactionsRepository.class, service.getEJBRemoteTransactionsRepositoryInjector())
                .addDependency(GroupMembershipNotifierRegistryService.SERVICE_NAME, GroupMembershipNotifierRegistry.class, service.getClusterRegistryInjector())
                .addDependency(clientMappingRegistryServiceName, Registry.class, service.getClientMappingRegistryServiceInjector())
                .setInitialMode(ServiceController.Mode.ACTIVE);
        if (verificationHandler != null) {
            ejbRemoteConnectorServiceBuilder.addListener(verificationHandler);
        }
        final ServiceController ejbRemotingConnectorServiceController = ejbRemoteConnectorServiceBuilder.install();
        services.add(ejbRemotingConnectorServiceController);

        return services;
    }

    @Override
    protected void populateModel(ModelNode operation, ModelNode model) throws OperationFailedException {
        model.get(CONNECTOR_REF).set(operation.require(CONNECTOR_REF).asString());
        model.get(THREAD_POOL_NAME).set(operation.require(THREAD_POOL_NAME).asString());
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15013.java