error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11173.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11173.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11173.java
text:
```scala
i@@f(operation.get(attribute).isDefined()) {

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

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOVE;
import static org.jboss.as.messaging.jms.CommonAttributes.*;

import java.util.HashSet;
import java.util.Set;


import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.jms.server.JMSServerManager;
import org.hornetq.jms.server.config.ConnectionFactoryConfiguration;
import org.hornetq.jms.server.config.impl.ConnectionFactoryConfigurationImpl;
import org.jboss.as.controller.Cancellable;
import org.jboss.as.controller.ModelAddOperationHandler;
import org.jboss.as.controller.NewOperationContext;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ResultHandler;
import org.jboss.as.server.NewRuntimeOperationContext;
import org.jboss.as.server.RuntimeOperationHandler;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceController.Mode;

/**
 * @author Emanuel Muckenhuber
 */
class NewConnectionFactoryAdd implements ModelAddOperationHandler, RuntimeOperationHandler {

    static final NewConnectionFactoryAdd INSTANCE = new NewConnectionFactoryAdd();
    private static final String[] NO_BINDINGS = new String[0];

    /** {@inheritDoc} */
    public Cancellable execute(final NewOperationContext context, final ModelNode operation, final ResultHandler resultHandler) {

        final PathAddress address = PathAddress.pathAddress(operation.require(OP_ADDR));
        final String name = address.getLastElement().getValue();

        final ModelNode compensatingOperation = new ModelNode();
        compensatingOperation.get(OP).set(REMOVE);
        compensatingOperation.get(OP_ADDR).set(operation.require(OP_ADDR));

        if(context instanceof NewRuntimeOperationContext) {
            final NewRuntimeOperationContext runtimeContext = (NewRuntimeOperationContext) context;

            final ConnectionFactoryConfiguration configuration = createConfiguration(name, operation);
            final ConnectionFactoryService service = new ConnectionFactoryService(configuration);
            final ServiceName serviceName = JMSServices.JMS_CF_BASE.append(name);
            runtimeContext.getServiceTarget().addService(serviceName, service)
                    .addDependency(JMSServices.JMS_MANAGER, JMSServerManager.class, service.getJmsServer())
                    .setInitialMode(Mode.ACTIVE)
                    .install();
        }

        final ModelNode subModel = context.getSubModel();
        for(final String attribute : JMSServices.CF_ATTRIBUTES) {
            if(operation.has(attribute)) {
                subModel.get(attribute).set(operation.get(attribute));
            }
        }

        resultHandler.handleResultComplete(compensatingOperation);

        return Cancellable.NULL;
    }

    static ConnectionFactoryConfiguration createConfiguration(final String name, final ModelNode operation) {
        final ConnectionFactoryConfiguration config = new ConnectionFactoryConfigurationImpl(name, jndiBindings(operation));

        config.setAutoGroup(operation.get(AUTO_GROUP).asBoolean(HornetQClient.DEFAULT_AUTO_GROUP));
        config.setBlockOnAcknowledge(operation.get(BLOCK_ON_ACK).asBoolean(HornetQClient.DEFAULT_BLOCK_ON_ACKNOWLEDGE));
        config.setBlockOnDurableSend(operation.get(BLOCK_ON_DURABLE_SEND).asBoolean(HornetQClient.DEFAULT_BLOCK_ON_DURABLE_SEND));
        config.setBlockOnNonDurableSend(operation.get(BLOCK_ON_NON_DURABLE_SEND).asBoolean(HornetQClient.DEFAULT_BLOCK_ON_NON_DURABLE_SEND));
        config.setCacheLargeMessagesClient(operation.get(CACHE_LARGE_MESSAGE_CLIENT).asBoolean(HornetQClient.DEFAULT_CACHE_LARGE_MESSAGE_CLIENT));
        config.setCallTimeout(operation.get(CALL_TIMEOUT).asLong(HornetQClient.DEFAULT_CALL_TIMEOUT));
        config.setClientFailureCheckPeriod(operation.get(CLIENT_FAILURE_CHECK_PERIOD).asInt((int) HornetQClient.DEFAULT_CLIENT_FAILURE_CHECK_PERIOD));
        if(operation.has(CLIENT_ID)) {
            config.setClientID(operation.get(CLIENT_ID).asString());
        }
        config.setConfirmationWindowSize(operation.get(CONFIRMATION_WINDOW_SIZE).asInt(HornetQClient.DEFAULT_CONFIRMATION_WINDOW_SIZE));
        config.setConnectionTTL(operation.get(CONNECTION_TTL).asLong(HornetQClient.DEFAULT_CONNECTION_TTL));
        //config.setConnectorConfigs(connectorConfigs)
        // config.setConnectorNames(connectors);
        config.setConsumerMaxRate(operation.get(CONSUMER_MAX_RATE).asInt(HornetQClient.DEFAULT_CONSUMER_MAX_RATE));
        config.setConsumerWindowSize(operation.get(CONSUMER_WINDOW_SIZE).asInt(HornetQClient.DEFAULT_CONSUMER_WINDOW_SIZE));
        // config.setDiscoveryAddress(discoveryAddress)
        if(operation.has(DISCOVERY_GROUP_NAME)) {
            config.setDiscoveryGroupName(operation.get(DISCOVERY_GROUP_NAME).asString());
        }
        // config.setDiscoveryPort(discoveryPort)
        config.setDupsOKBatchSize(operation.get(DUPS_OK_BATCH_SIZE).asInt(HornetQClient.DEFAULT_ACK_BATCH_SIZE));
        config.setFailoverOnInitialConnection(operation.get(FAILOVER_ON_INITIAL_CONNECTION).asBoolean(HornetQClient.DEFAULT_FAILOVER_ON_INITIAL_CONNECTION));
        config.setFailoverOnServerShutdown(operation.get(FAILOVER_ON_SERVER_SHUTDOWN).asBoolean(HornetQClient.DEFAULT_FAILOVER_ON_SERVER_SHUTDOWN));
        if(operation.has(GROUP_ID)) {
            config.setGroupID(operation.get(GROUP_ID).asString());
        }
        // config.setInitialWaitTimeout(operation.get(INI));
        // config.setLoadBalancingPolicyClassName(loadBalancingPolicyClassName)
        // config.setLocalBindAddress(localBindAddress)
        config.setMaxRetryInterval(operation.get(MAX_RETRY_INTERVAL).asLong(HornetQClient.DEFAULT_MAX_RETRY_INTERVAL));
        config.setMinLargeMessageSize(operation.get(MIN_LARGE_MESSAGE_SIZE).asInt(HornetQClient.DEFAULT_MIN_LARGE_MESSAGE_SIZE));
        config.setPreAcknowledge(operation.get(PRE_ACK).asBoolean(HornetQClient.DEFAULT_PRE_ACKNOWLEDGE));
        config.setProducerMaxRate(operation.get(PRODUCER_MAX_RATE).asInt(HornetQClient.DEFAULT_PRODUCER_MAX_RATE));
        config.setProducerWindowSize(operation.get(PRODUCER_WINDOW_SIZE).asInt(HornetQClient.DEFAULT_PRODUCER_WINDOW_SIZE));
        config.setReconnectAttempts(operation.get(RECONNECT_ATTEMPTS).asInt(HornetQClient.DEFAULT_RECONNECT_ATTEMPTS));
        config.setRetryInterval(operation.get(RETRY_INTERVAL).asLong(HornetQClient.DEFAULT_RETRY_INTERVAL));
        config.setRetryIntervalMultiplier(operation.get(RETRY_INTERVAL_MULTIPLIER).asDouble(HornetQClient.DEFAULT_RETRY_INTERVAL_MULTIPLIER));
        config.setScheduledThreadPoolMaxSize(operation.get(SCHEDULED_THREAD_POOL_MAX_SIZE).asInt(HornetQClient.DEFAULT_SCHEDULED_THREAD_POOL_MAX_SIZE));
        config.setThreadPoolMaxSize(operation.get(THREAD_POOL_MAX_SIZE).asInt(HornetQClient.DEFAULT_THREAD_POOL_MAX_SIZE));
        config.setTransactionBatchSize(operation.get(TRANSACTION_BATH_SIZE).asInt(HornetQClient.DEFAULT_ACK_BATCH_SIZE));
        config.setUseGlobalPools(operation.get(USE_GLOBAL_POOLS).asBoolean(HornetQClient.DEFAULT_USE_GLOBAL_POOLS));

        return config;
    }

    static String[] jndiBindings(final ModelNode node) {
        if(node.has(ENTRIES)) {
            final Set<String> bindings = new HashSet<String>();
            for(final ModelNode entry : node.get(ENTRIES).asList()) {
                bindings.add(entry.asString());
            }
            return bindings.toArray(new String[bindings.size()]);
        }
        return NO_BINDINGS;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11173.java