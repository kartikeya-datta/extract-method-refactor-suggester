error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14760.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14760.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14760.java
text:
```scala
final i@@nt confirmationWindowSize = CommonAttributes.BRIDGE_CONFIRMATION_WINDOW_SIZE.resolveModelAttribute(context, model).asInt();

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

package org.jboss.as.messaging;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.messaging.MessagingMessages.MESSAGES;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.api.core.management.HornetQServerControl;
import org.hornetq.core.config.BridgeConfiguration;
import org.hornetq.core.config.Configuration;
import org.hornetq.core.server.HornetQServer;
import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistry;

/**
 * Handler for adding a bridge.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class BridgeAdd extends AbstractAddStepHandler implements DescriptionProvider {

    /**
     * Create an "add" operation using the existing model
     */
    public static ModelNode getAddOperation(final ModelNode address, ModelNode subModel) {

        final ModelNode operation = org.jboss.as.controller.operations.common.Util.getOperation(ADD, address, subModel);

        return operation;
    }

    public static final BridgeAdd INSTANCE = new BridgeAdd();

    private BridgeAdd() {}

    @Override
    protected void populateModel(ModelNode operation, ModelNode model) throws OperationFailedException {

        model.setEmptyObject();

        boolean hasStatic = operation.hasDefined(ConnectorRefsAttribute.BRIDGE_CONNECTORS.getName());
        boolean hasDiscGroup = operation.hasDefined(CommonAttributes.DISCOVERY_GROUP_NAME.getName());
        if (!hasStatic && !hasDiscGroup) {
            throw new OperationFailedException(new ModelNode().set(MESSAGES.invalidOperationParameters(
                    ConnectorRefsAttribute.BRIDGE_CONNECTORS.getName(), CommonAttributes.DISCOVERY_GROUP_NAME.getName())));
        } else if (hasStatic && hasDiscGroup) {
            throw new OperationFailedException(new ModelNode().set(MESSAGES.cannotIncludeOperationParameters(
                    ConnectorRefsAttribute.BRIDGE_CONNECTORS.getName(), CommonAttributes.DISCOVERY_GROUP_NAME.getName())));
        }

        for (final AttributeDefinition attributeDefinition : CommonAttributes.BRIDGE_ATTRIBUTES) {
            if (hasDiscGroup && attributeDefinition == ConnectorRefsAttribute.BRIDGE_CONNECTORS) {
                continue;
            } else if (hasStatic && attributeDefinition == CommonAttributes.DISCOVERY_GROUP_NAME) {
                continue;
            }
            attributeDefinition.validateAndSet(operation, model);
        }
    }

    @Override
    protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model, ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers) throws OperationFailedException {

        ServiceRegistry registry = context.getServiceRegistry(true);
        final ServiceName hqServiceName = MessagingServices.getHornetQServiceName(PathAddress.pathAddress(operation.get(ModelDescriptionConstants.OP_ADDR)));
        ServiceController<?> hqService = registry.getService(hqServiceName);
        if (hqService != null) {

            // The original subsystem initialization is complete; use the control object to create the divert
            if (hqService.getState() != ServiceController.State.UP) {
                throw MESSAGES.invalidServiceState(hqServiceName, ServiceController.State.UP, hqService.getState());
            }

            final String name = PathAddress.pathAddress(operation.require(ModelDescriptionConstants.OP_ADDR)).getLastElement().getValue();

            BridgeConfiguration bridgeConfig = createBridgeConfiguration(context, name, model);

            HornetQServerControl serverControl = HornetQServer.class.cast(hqService.getValue()).getHornetQServerControl();
            createBridge(name, bridgeConfig, serverControl);

        }
        // else the initial subsystem install is not complete; MessagingSubsystemAdd will add a
        // handler that calls addBridgeConfigs
    }

    @Override
    public ModelNode getModelDescription(Locale locale) {
        return MessagingDescriptions.getBridgeAdd(locale);
    }

    static void addBridgeConfigs(final OperationContext context, final Configuration configuration, final ModelNode model)  throws OperationFailedException {
        if (model.hasDefined(CommonAttributes.BRIDGE)) {
            final List<BridgeConfiguration> configs = configuration.getBridgeConfigurations();
            for (Property prop : model.get(CommonAttributes.BRIDGE).asPropertyList()) {
                configs.add(createBridgeConfiguration(context, prop.getName(), prop.getValue()));

            }
        }
    }

    static BridgeConfiguration createBridgeConfiguration(final OperationContext context, final String name, final ModelNode model) throws OperationFailedException {

        final String queueName = CommonAttributes.QUEUE_NAME.resolveModelAttribute(context, model).asString();
        final ModelNode forwardingNode = CommonAttributes.BRIDGE_FORWARDING_ADDRESS.resolveModelAttribute(context, model);
        final String forwardingAddress = forwardingNode.isDefined() ? forwardingNode.asString() : null;
        final ModelNode filterNode = CommonAttributes.FILTER.resolveModelAttribute(context, model);
        final String filterString = filterNode.isDefined() ? filterNode.asString() : null;
        final ModelNode transformerNode = CommonAttributes.TRANSFORMER_CLASS_NAME.resolveModelAttribute(context, model);
        final String transformerClassName = transformerNode.isDefined() ? transformerNode.asString() : null;
        final long retryInterval = CommonAttributes.RETRY_INTERVAL.resolveModelAttribute(context, model).asLong();
        final double retryIntervalMultiplier = CommonAttributes.RETRY_INTERVAL_MULTIPLIER.resolveModelAttribute(context, model).asDouble();
        final int reconnectAttempts = CommonAttributes.BRIDGE_RECONNECT_ATTEMPTS.resolveModelAttribute(context, model).asInt();
        final boolean useDuplicateDetection = CommonAttributes.BRIDGE_USE_DUPLICATE_DETECTION.resolveModelAttribute(context, model).asBoolean();
        final int confirmationWindowSize = CommonAttributes.CONFIRMATION_WINDOW_SIZE.resolveModelAttribute(context, model).asInt();
        final long clientFailureCheckPeriod = HornetQClient.DEFAULT_CLIENT_FAILURE_CHECK_PERIOD;
        final ModelNode discoveryNode = CommonAttributes.DISCOVERY_GROUP_NAME.resolveModelAttribute(context, model);
        final String discoveryGroupName = discoveryNode.isDefined() ? discoveryNode.asString() : null;
        List<String> staticConnectors = discoveryGroupName == null ? getStaticConnectors(model) : null;
        final boolean ha = CommonAttributes.HA.resolveModelAttribute(context, model).asBoolean();
        final String user = CommonAttributes.USER.resolveModelAttribute(context, model).asString();
        final String password = CommonAttributes.PASSWORD.resolveModelAttribute(context, model).asString();

        if (discoveryGroupName != null) {
            return new BridgeConfiguration(name, queueName, forwardingAddress, filterString, transformerClassName,
                              retryInterval, retryIntervalMultiplier, reconnectAttempts, useDuplicateDetection,
                              confirmationWindowSize, clientFailureCheckPeriod, discoveryGroupName, ha,
                              user, password);
        } else {
            return new BridgeConfiguration(name, queueName, forwardingAddress, filterString, transformerClassName,
                              retryInterval, retryIntervalMultiplier, reconnectAttempts, useDuplicateDetection,
                              confirmationWindowSize, clientFailureCheckPeriod, staticConnectors, ha,
                              user, password);
        }
    }

    private static List<String> getStaticConnectors(ModelNode model) {
        List<String> result = new ArrayList<String>();
        for (ModelNode connector : model.require(CommonAttributes.STATIC_CONNECTORS).asList()) {
            result.add(connector.asString());
        }
        return result;
    }

    static void createBridge(String name, BridgeConfiguration bridgeConfig, HornetQServerControl serverControl) {
        try {
            if (bridgeConfig.getDiscoveryGroupName() != null) {
                serverControl.createBridge(name, bridgeConfig.getQueueName(), bridgeConfig.getForwardingAddress(),
                        bridgeConfig.getFilterString(), bridgeConfig.getTransformerClassName(), bridgeConfig.getRetryInterval(),
                        bridgeConfig.getRetryIntervalMultiplier(), bridgeConfig.getReconnectAttempts(), bridgeConfig.isUseDuplicateDetection(),
                        bridgeConfig.getConfirmationWindowSize(), HornetQClient.DEFAULT_CLIENT_FAILURE_CHECK_PERIOD,
                        bridgeConfig.getDiscoveryGroupName(), true, bridgeConfig.isHA(), bridgeConfig.getUser(),
                        bridgeConfig.getPassword());
            } else {
                boolean first = true;
                String connectors = "";
                for (String connector : bridgeConfig.getStaticConnectors()) {
                    if (!first) {
                        connectors += ",";
                    } else {
                        first = false;
                    }
                    connectors += connector;
                }
                serverControl.createBridge(name, bridgeConfig.getQueueName(), bridgeConfig.getForwardingAddress(),
                        bridgeConfig.getFilterString(), bridgeConfig.getTransformerClassName(), bridgeConfig.getRetryInterval(),
                        bridgeConfig.getRetryIntervalMultiplier(), bridgeConfig.getReconnectAttempts(), bridgeConfig.isUseDuplicateDetection(),
                        bridgeConfig.getConfirmationWindowSize(), HornetQClient.DEFAULT_CLIENT_FAILURE_CHECK_PERIOD,
                        connectors, false, bridgeConfig.isHA(), bridgeConfig.getUser(),
                        bridgeConfig.getPassword());
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            // TODO should this be an OFE instead?
            throw new RuntimeException(e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14760.java