error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11079.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11079.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11079.java
text:
```scala
i@@f(context.isNormalServer()) {

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

import static org.jboss.as.messaging.CommonAttributes.ACCEPTOR;
import static org.jboss.as.messaging.CommonAttributes.CONNECTOR;
import static org.jboss.as.messaging.CommonAttributes.FACTORY_CLASS;
import static org.jboss.as.messaging.CommonAttributes.IN_VM_ACCEPTOR;
import static org.jboss.as.messaging.CommonAttributes.IN_VM_CONNECTOR;
import static org.jboss.as.messaging.CommonAttributes.PARAM;
import static org.jboss.as.messaging.CommonAttributes.REMOTE_ACCEPTOR;
import static org.jboss.as.messaging.CommonAttributes.REMOTE_CONNECTOR;
import static org.jboss.as.messaging.CommonAttributes.SERVER_ID;
import static org.jboss.as.messaging.CommonAttributes.SOCKET_BINDING;
import static org.jboss.as.messaging.CommonAttributes.VALUE;
import static org.jboss.as.messaging.MessagingMessages.MESSAGES;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.core.config.Configuration;
import org.hornetq.core.remoting.impl.invm.InVMAcceptorFactory;
import org.hornetq.core.remoting.impl.invm.InVMConnectorFactory;
import org.hornetq.core.remoting.impl.netty.NettyAcceptorFactory;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ReloadRequiredWriteAttributeHandler;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.Resource;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;

/**
 * Basic {@link TransportConfiguration} (Acceptor/Connector) related operations.
 *
 * @author Emanuel Muckenhuber
 */
class TransportConfigOperationHandlers {

    static AttributeDefinition[] GENERIC = new AttributeDefinition[] { CommonAttributes.FACTORY_CLASS, CommonAttributes.SOCKET_BINDING_OPTIONAL };
    static AttributeDefinition[] REMOTE = new AttributeDefinition[] { CommonAttributes.SOCKET_BINDING };
    static AttributeDefinition[] IN_VM = new AttributeDefinition[] { CommonAttributes.SERVER_ID };

    /** The generic transport-config add operation handler. */
    static final OperationStepHandler GENERIC_ADD = new BasicTransportConfigAdd(GENERIC);
    static final SelfRegisteringAttributeHandler GENERIC_ATTR = new AttributeWriteHandler(GENERIC);

    /** The remote transport-config add operation handler. */
    static final OperationStepHandler REMOTE_ADD = new BasicTransportConfigAdd(REMOTE);
    static final SelfRegisteringAttributeHandler REMOTE_ATTR = new AttributeWriteHandler(REMOTE);

    /** The in-vm transport-config add operation handler. */
    static final OperationStepHandler IN_VM_ADD = new BasicTransportConfigAdd(IN_VM);
    static final SelfRegisteringAttributeHandler IN_VM_ATTR = new AttributeWriteHandler(IN_VM);

    /** The transport-config remove operation handler. */
    static final OperationStepHandler REMOVE = new OperationStepHandler() {

        @Override
        public void execute(final OperationContext context, final ModelNode operation) throws OperationFailedException {
            final Resource resource = context.removeResource(PathAddress.EMPTY_ADDRESS);
            reloadRequiredStep(context);
            context.completeStep();
        }
    };

    /** The transport-config param add operation handler. */
    static final OperationStepHandler PARAM_ADD = new OperationStepHandler() {

        @Override
        public void execute(final OperationContext context, final ModelNode operation) throws OperationFailedException {
            final Resource resource = context.createResource(PathAddress.EMPTY_ADDRESS);
            VALUE.validateAndSet(operation, resource.getModel());
            reloadRequiredStep(context);
            context.completeStep();
        }
    };

    static final OperationStepHandler PARAM_ATTR = new OperationStepHandler() {

        @Override
        public void execute(final OperationContext context, final ModelNode operation) throws OperationFailedException {
            final Resource resource = context.readResourceForUpdate(PathAddress.EMPTY_ADDRESS);
            VALUE.validateAndSet(operation, resource.getModel());
            reloadRequiredStep(context);
            context.completeStep();
        }
    };

    /**
     * Create the connector/acceptor add operation.
     *
     * @param address the address
     * @param node the subModel
     * @return the add operation
     */
    static ModelNode createAddOperation(final ModelNode address, final ModelNode node) {
        final ModelNode operation = new ModelNode();
        operation.get(ModelDescriptionConstants.OP).set(ModelDescriptionConstants.ADD);
        operation.get(ModelDescriptionConstants.OP_ADDR).set(address);
        if(node.hasDefined(CommonAttributes.SOCKET_BINDING.getName())) {
            operation.get(CommonAttributes.SOCKET_BINDING.getName()).set(node.get(CommonAttributes.SOCKET_BINDING.getName()));
        }
        if(node.hasDefined(CommonAttributes.SERVER_ID.getName())) {
            operation.get(CommonAttributes.SERVER_ID.getName()).set(node.get(CommonAttributes.SERVER_ID.getName()));
        }
        if(node.hasDefined(CommonAttributes.PARAM)) {
            for(final Property param : node.get(CommonAttributes.PARAM).asPropertyList()) {
                operation.get(CommonAttributes.PARAM, param.getName()).set(param.getValue().get(ModelDescriptionConstants.VALUE));
            }
        }
        return operation;
    }

    /**
     * Add a step triggering the {@linkplain org.jboss.as.controller.OperationContext#reloadRequired()} in case the
     * the messaging service is installed, since the transport-config operations need a reload/restart and can't be
     * applied to the runtime directly.
     *
     * @param context the operation context
     */
    static void reloadRequiredStep(final OperationContext context) {
        if(context.getType() == OperationContext.Type.SERVER) {
            context.addStep(new OperationStepHandler() {
                @Override
                public void execute(final OperationContext context, final ModelNode operation) throws OperationFailedException {
                    final ServiceName hqServiceName = MessagingServices.getHornetQServiceName(PathAddress.pathAddress(operation.get(ModelDescriptionConstants.OP_ADDR)));
                    final ServiceController<?> controller = context.getServiceRegistry(false).getService(hqServiceName);
                    if(controller != null) {
                        context.reloadRequired();
                    }
                    context.completeStep();
                }
            }, OperationContext.Stage.RUNTIME);
        }
    }

    /**
     * Process the acceptor information.
     *
     * @param configuration the hornetQ configuration
     * @param params        the detyped operation parameters
     * @param bindings      the referenced socket bindings
     */
    static void processAcceptors(final Configuration configuration, final ModelNode params, final Set<String> bindings) {
        final Map<String, TransportConfiguration> acceptors = new HashMap<String, TransportConfiguration>();
        if(params.hasDefined(ACCEPTOR)) {
            for (final Property property : params.get(ACCEPTOR).asPropertyList()) {
                final String acceptorName = property.getName();
                final ModelNode config = property.getValue();
                final Map<String, Object> parameters = getParameters(config);
                final String clazz = config.get(FACTORY_CLASS.getName()).asString();
                acceptors.put(acceptorName, new TransportConfiguration(clazz, parameters, acceptorName));
            }
        }
        if(params.hasDefined(REMOTE_ACCEPTOR)) {
            for (final Property property : params.get(REMOTE_ACCEPTOR).asPropertyList()) {
                final String acceptorName = property.getName();
                final ModelNode config = property.getValue();
                final Map<String, Object> parameters = getParameters(config);
                final String binding = config.get(SOCKET_BINDING.getName()).asString();
                parameters.put(SOCKET_BINDING.getName(), binding);
                bindings.add(binding);
                acceptors.put(acceptorName, new TransportConfiguration(NettyAcceptorFactory.class.getName(), parameters, acceptorName));
            }
        }
        if(params.hasDefined(IN_VM_ACCEPTOR)) {
            for (final Property property : params.get(IN_VM_ACCEPTOR).asPropertyList()) {
                final String acceptorName = property.getName();
                final ModelNode config = property.getValue();
                final Map<String, Object> parameters = getParameters(config);
                parameters.put(SERVER_ID.getName(), config.get(SERVER_ID.getName()).asInt());
                acceptors.put(acceptorName, new TransportConfiguration(InVMAcceptorFactory.class.getName(), parameters, acceptorName));
            }
        }
        configuration.setAcceptorConfigurations(new HashSet<TransportConfiguration>(acceptors.values()));
    }

    /**
     * Get the parameters.
     *
     * @param config the transport configuration
     * @return the extracted parameters
     */
    static Map<String, Object> getParameters(final ModelNode config) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        if (config.get(PARAM).isDefined()) {
            for (final Property parameter : config.get(PARAM).asPropertyList()) {
                parameters.put(parameter.getName(), parameter.getValue().get(ModelDescriptionConstants.VALUE).asString());
            }
        }
        return parameters;
    }

    /**
     * Process the connector information.
     *
     * @param configuration the hornetQ configuration
     * @param params        the detyped operation parameters
     * @param bindings      the referenced socket bindings
     */
    static void processConnectors(final Configuration configuration, final ModelNode params, final Set<String> bindings) {
        final Map<String, TransportConfiguration> connectors = new HashMap<String, TransportConfiguration>();
        if (params.hasDefined(CONNECTOR)) {
            for (final Property property : params.get(CONNECTOR).asPropertyList()) {
                final String connectorName = property.getName();
                final ModelNode config = property.getValue();
                final Map<String, Object> parameters = getParameters(config);
                final String clazz = config.get(FACTORY_CLASS.getName()).asString();
                connectors.put(connectorName, new TransportConfiguration(clazz, parameters, connectorName));
            }
        }
        if (params.hasDefined(REMOTE_CONNECTOR)) {
            for (final Property property : params.get(REMOTE_CONNECTOR).asPropertyList()) {
                final String connectorName = property.getName();
                final ModelNode config = property.getValue();
                final Map<String, Object> parameters = getParameters(config);
                final String binding = config.get(SOCKET_BINDING.getName()).asString();
                parameters.put(SOCKET_BINDING.getName(), binding);
                bindings.add(binding);
                connectors.put(connectorName, new TransportConfiguration(NettyConnectorFactory.class.getName(), parameters, connectorName));
            }
        }
        if(params.hasDefined(IN_VM_CONNECTOR)) {
            for (final Property property : params.get(IN_VM_CONNECTOR).asPropertyList()) {
                final String connectorName = property.getName();
                final ModelNode config = property.getValue();
                final Map<String, Object> parameters = getParameters(config);
                parameters.put(SERVER_ID.getName(), config.get(SERVER_ID.getName()).asInt());
                connectors.put(connectorName, new TransportConfiguration(InVMConnectorFactory.class.getName(), parameters, connectorName));
            }
        }
        configuration.setConnectorConfigurations(connectors);
    }

    private static class BasicTransportConfigAdd implements OperationStepHandler {

        private final AttributeDefinition[] attributes;

        BasicTransportConfigAdd(final AttributeDefinition[] attributes) {
            this.attributes = attributes;
        }

        @Override
        public void execute(final OperationContext context, final ModelNode operation) throws OperationFailedException {
            final Resource resource = context.createResource(PathAddress.EMPTY_ADDRESS);
            final ModelNode subModel = resource.getModel();
            // Process attributes
            for(final AttributeDefinition attribute : attributes) {
                attribute.validateAndSet(operation, subModel);
            }
            // Process acceptor/connector type specific properties
            process(subModel, operation);
            // The transport-config parameters
            if(operation.hasDefined(CommonAttributes.PARAMS)) {
                for(Property property : operation.get(CommonAttributes.PARAMS).asPropertyList()) {
                    final Resource param = context.createResource(PathAddress.pathAddress(PathElement.pathElement(CommonAttributes.PARAM, property.getName())));
                    final ModelNode value = property.getValue();
                    if(! value.isDefined()) {
                        throw new OperationFailedException(new ModelNode().set(MESSAGES.parameterNotDefined(property.getName())));
                    }
                    param.getModel().get(ModelDescriptionConstants.VALUE).set(value);
                }
            }
            // This needs a reload
            reloadRequiredStep(context);
            context.completeStep();
        }

        void process(ModelNode subModel, ModelNode operation) {
            //
        };
    }

    interface SelfRegisteringAttributeHandler extends OperationStepHandler {
        void registerAttributes(final ManagementResourceRegistration registry, boolean registerRuntimeOnly);
    }

    static class AttributeWriteHandler extends ReloadRequiredWriteAttributeHandler implements SelfRegisteringAttributeHandler {
        final AttributeDefinition[] attributes;

        private AttributeWriteHandler(AttributeDefinition[] attributes) {
            super(attributes);
            this.attributes = attributes;
        }

        public void registerAttributes(final ManagementResourceRegistration registry, boolean registerRuntimeOnly) {
            final EnumSet<AttributeAccess.Flag> flags = EnumSet.of(AttributeAccess.Flag.RESTART_ALL_SERVICES);
            for (AttributeDefinition attr : attributes) {
                if (registerRuntimeOnly || !attr.getFlags().contains(AttributeAccess.Flag.STORAGE_RUNTIME)) {
                    registry.registerReadWriteAttribute(attr.getName(), null, this, flags);
                }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11079.java