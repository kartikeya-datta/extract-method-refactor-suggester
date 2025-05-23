error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8704.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8704.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8704.java
text:
```scala
A@@lternativeAttributeCheckHandler.checkAlternatives(operation, Common.CONNECTOR.getName(), Common.DISCOVERY_GROUP_NAME.getName(), false);

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

package org.jboss.as.messaging.jms;


import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.messaging.CommonAttributes.CALL_TIMEOUT;
import static org.jboss.as.messaging.CommonAttributes.CLIENT_ID;
import static org.jboss.as.messaging.CommonAttributes.CONNECTOR;
import static org.jboss.as.messaging.CommonAttributes.CONSUMER_MAX_RATE;
import static org.jboss.as.messaging.CommonAttributes.HA;

import java.util.ArrayList;
import java.util.List;

import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.api.jms.JMSFactoryType;
import org.hornetq.jms.server.JMSServerManager;
import org.hornetq.jms.server.config.ConnectionFactoryConfiguration;
import org.hornetq.jms.server.config.impl.ConnectionFactoryConfigurationImpl;
import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.messaging.AlternativeAttributeCheckHandler;
import org.jboss.as.messaging.MessagingServices;
import org.jboss.as.messaging.jms.ConnectionFactoryAttributes.Common;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.ServiceName;

/**
 * Update adding a connection factory to the subsystem. The
 * runtime action will create the {@link ConnectionFactoryService}.
 *
 * @author Emanuel Muckenhuber
 * @author <a href="mailto:andy.taylor@jboss.com">Andy Taylor</a>
 */
public class ConnectionFactoryAdd extends AbstractAddStepHandler {

    public static final ConnectionFactoryAdd INSTANCE = new ConnectionFactoryAdd();

    protected void populateModel(ModelNode operation, ModelNode model) throws OperationFailedException {

        AlternativeAttributeCheckHandler.checkAlternatives(operation, Common.CONNECTOR.getName(), Common.DISCOVERY_GROUP_NAME.getName());

        for (final AttributeDefinition attribute : ConnectionFactoryDefinition.ATTRIBUTES) {
            attribute.validateAndSet(operation, model);
        }
    }

    protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model, ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers) throws OperationFailedException {
        final PathAddress address = PathAddress.pathAddress(operation.get(OP_ADDR));
        final String name = address.getLastElement().getValue();
        final ServiceName hqServiceName = MessagingServices.getHornetQServiceName(PathAddress.pathAddress(operation.get(ModelDescriptionConstants.OP_ADDR)));

        final ConnectionFactoryConfiguration configuration = createConfiguration(context, name, model);
        final ConnectionFactoryService service = new ConnectionFactoryService(configuration);
        final ServiceName serviceName = JMSServices.getConnectionFactoryBaseServiceName(hqServiceName).append(name);
        ServiceBuilder<?> serviceBuilder = context.getServiceTarget().addService(serviceName, service)
                .addDependency(JMSServices.getJmsManagerBaseServiceName(hqServiceName), JMSServerManager.class, service.getJmsServer())
                .addListener(verificationHandler)
                .setInitialMode(Mode.ACTIVE);
        org.jboss.as.server.Services.addServerExecutorDependency(serviceBuilder, service.getExecutorInjector(), false);
        newControllers.add(serviceBuilder.install());
    }

    static ConnectionFactoryConfiguration createConfiguration(final OperationContext context, final String name, final ModelNode model) throws OperationFailedException {

        final ModelNode entries = Common.ENTRIES.resolveModelAttribute(context, model);
        final String[] jndiBindings = JMSServices.getJndiBindings(entries);

        final ConnectionFactoryConfiguration config = new ConnectionFactoryConfigurationImpl(name, HornetQClient.DEFAULT_HA, jndiBindings);

        config.setHA(HA.resolveModelAttribute(context, model).asBoolean());
        config.setAutoGroup(Common.AUTO_GROUP.resolveModelAttribute(context, model).asBoolean());
        config.setBlockOnAcknowledge(Common.BLOCK_ON_ACKNOWLEDGE.resolveModelAttribute(context, model).asBoolean());
        config.setBlockOnDurableSend(Common.BLOCK_ON_DURABLE_SEND.resolveModelAttribute(context, model).asBoolean());
        config.setBlockOnNonDurableSend(Common.BLOCK_ON_NON_DURABLE_SEND.resolveModelAttribute(context, model).asBoolean());
        config.setCacheLargeMessagesClient(Common.CACHE_LARGE_MESSAGE_CLIENT.resolveModelAttribute(context, model).asBoolean());
        config.setCallTimeout(CALL_TIMEOUT.resolveModelAttribute(context, model).asLong());
        config.setClientFailureCheckPeriod(Common.CLIENT_FAILURE_CHECK_PERIOD.resolveModelAttribute(context, model).asInt());
        final ModelNode clientId = CLIENT_ID.resolveModelAttribute(context, model);
        if (clientId.isDefined()) {
            config.setClientID(clientId.asString());
        }
        config.setCompressLargeMessages(Common.COMPRESS_LARGE_MESSAGES.resolveModelAttribute(context, model).asBoolean());
        config.setConfirmationWindowSize(Common.CONFIRMATION_WINDOW_SIZE.resolveModelAttribute(context, model).asInt());
        config.setConnectionTTL(Common.CONNECTION_TTL.resolveModelAttribute(context, model).asLong());
        if (model.hasDefined(CONNECTOR)) {
            ModelNode connectorRefs = model.get(CONNECTOR);
            List<String> connectorNames = new ArrayList<String>(connectorRefs.keys());
            config.setConnectorNames(connectorNames);
        }
        config.setConsumerMaxRate(CONSUMER_MAX_RATE.resolveModelAttribute(context, model).asInt());
        config.setConsumerWindowSize(Common.CONSUMER_WINDOW_SIZE.resolveModelAttribute(context, model).asInt());
        final ModelNode discoveryGroupName = Common.DISCOVERY_GROUP_NAME.resolveModelAttribute(context, model);
        if (discoveryGroupName.isDefined()) {
            config.setDiscoveryGroupName(discoveryGroupName.asString());
        }

        config.setDupsOKBatchSize(Common.DUPS_OK_BATCH_SIZE.resolveModelAttribute(context, model).asInt());
        config.setFailoverOnInitialConnection(Common.FAILOVER_ON_INITIAL_CONNECTION.resolveModelAttribute(context, model).asBoolean());

        final ModelNode groupId = Common.GROUP_ID.resolveModelAttribute(context, model);
        if (groupId.isDefined()) {
            config.setGroupID(groupId.asString());
        }

        final ModelNode lbcn = Common.CONNECTION_LOAD_BALANCING_CLASS_NAME.resolveModelAttribute(context, model);
        if (lbcn.isDefined()) {
            config.setLoadBalancingPolicyClassName(lbcn.asString());
        }
        config.setMaxRetryInterval(Common.MAX_RETRY_INTERVAL.resolveModelAttribute(context, model).asLong());
        config.setMinLargeMessageSize(Common.MIN_LARGE_MESSAGE_SIZE.resolveModelAttribute(context, model).asInt());
        config.setPreAcknowledge(Common.PRE_ACKNOWLEDGE.resolveModelAttribute(context, model).asBoolean());
        config.setProducerMaxRate(Common.PRODUCER_MAX_RATE.resolveModelAttribute(context, model).asInt());
        config.setProducerWindowSize(Common.PRODUCER_WINDOW_SIZE.resolveModelAttribute(context, model).asInt());
        config.setReconnectAttempts(Common.RECONNECT_ATTEMPTS.resolveModelAttribute(context, model).asInt());
        config.setRetryInterval(Common.RETRY_INTERVAL.resolveModelAttribute(context, model).asLong());
        config.setRetryIntervalMultiplier(Common.RETRY_INTERVAL_MULTIPLIER.resolveModelAttribute(context, model).asDouble());
        config.setScheduledThreadPoolMaxSize(Common.SCHEDULED_THREAD_POOL_MAX_SIZE.resolveModelAttribute(context, model).asInt());
        config.setThreadPoolMaxSize(Common.THREAD_POOL_MAX_SIZE.resolveModelAttribute(context, model).asInt());
        config.setTransactionBatchSize(Common.TRANSACTION_BATCH_SIZE.resolveModelAttribute(context, model).asInt());
        config.setUseGlobalPools(Common.USE_GLOBAL_POOLS.resolveModelAttribute(context, model).asBoolean());
        config.setLoadBalancingPolicyClassName(Common.CONNECTION_LOAD_BALANCING_CLASS_NAME.resolveModelAttribute(context, model).asString());
        JMSFactoryType jmsFactoryType = ConnectionFactoryType.valueOf(ConnectionFactoryAttributes.Regular.FACTORY_TYPE.resolveModelAttribute(context, model).asString()).getType();
        config.setFactoryType(jmsFactoryType);
        return config;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8704.java