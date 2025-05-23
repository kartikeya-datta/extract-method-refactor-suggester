error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7476.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7476.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7476.java
text:
```scala
s@@ubsystem.registerXMLElementWriter(MessagingXMLWriter.INSTANCE);

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

package org.jboss.as.messaging;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DESCRIBE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILURE_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.IGNORED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OUTCOME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.PATH;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.WRITE_ATTRIBUTE_OPERATION;
import static org.jboss.as.controller.transform.OperationResultTransformer.ORIGINAL_RESULT;
import static org.jboss.as.messaging.CommonAttributes.CONNECTION_FACTORY;
import static org.jboss.as.messaging.CommonAttributes.HA;
import static org.jboss.as.messaging.CommonAttributes.HORNETQ_SERVER;
import static org.jboss.as.messaging.CommonAttributes.ID_CACHE_SIZE;
import static org.jboss.as.messaging.CommonAttributes.PARAM;
import static org.jboss.as.messaging.CommonAttributes.POOLED_CONNECTION_FACTORY;
import static org.jboss.as.messaging.Namespace.MESSAGING_1_0;
import static org.jboss.as.messaging.Namespace.MESSAGING_1_1;
import static org.jboss.as.messaging.Namespace.MESSAGING_1_2;
import static org.jboss.as.messaging.Namespace.MESSAGING_1_3;
import static org.jboss.as.messaging.jms.ConnectionFactoryAttributes.Regular.FACTORY_TYPE;

import java.util.ArrayList;
import java.util.List;

import org.jboss.as.controller.Extension;
import org.jboss.as.controller.ExtensionContext;
import org.jboss.as.controller.ModelVersion;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ResourceDefinition;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.SubsystemRegistration;
import org.jboss.as.controller.descriptions.ResourceDescriptionResolver;
import org.jboss.as.controller.descriptions.StandardResourceDescriptionResolver;
import org.jboss.as.controller.operations.common.GenericSubsystemDescribeHandler;
import org.jboss.as.controller.parsing.ExtensionParsingContext;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.OperationEntry;
import org.jboss.as.controller.transform.AbstractSubsystemTransformer;
import org.jboss.as.controller.transform.OperationResultTransformer;
import org.jboss.as.controller.transform.OperationTransformer;
import org.jboss.as.controller.transform.RejectExpressionValuesTransformer;
import org.jboss.as.controller.transform.TransformationContext;
import org.jboss.as.controller.transform.TransformersSubRegistration;
import org.jboss.as.messaging.jms.ConnectionFactoryAttributes;
import org.jboss.as.messaging.jms.ConnectionFactoryDefinition;
import org.jboss.as.messaging.jms.JMSQueueDefinition;
import org.jboss.as.messaging.jms.JMSTopicDefinition;
import org.jboss.as.messaging.jms.PooledConnectionFactoryDefinition;
import org.jboss.as.messaging.jms.bridge.JMSBridgeDefinition;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;

/**
 * Domain extension that integrates HornetQ.
 *
 * <dl>
 *   <dt>AS 7.2.0</dt>
 *   <dd>
 *     <ul>
 *       <li>XML namespace: urn:jboss:domain:messaging:1.3
 *       <li>Management model: 1.2.0
 *     </ul>
 *   </dd>
 *   <dt>AS 7.1.2<dt>
 *   <dd>
 *     <ul>
 *       <li>XML namespace: urn:jboss:domain:messaging:1.2
 *       <li>Management model: 1.1.0
 *     </ul>
 *   </dd>
 * </dl>
 *
 * @author Emanuel Muckenhuber
 * @author <a href="mailto:andy.taylor@jboss.com">Andy Taylor</a>
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class MessagingExtension implements Extension {

    public static final String SUBSYSTEM_NAME = "messaging";

    static final PathElement SUBSYSTEM_PATH  = PathElement.pathElement(SUBSYSTEM, SUBSYSTEM_NAME);

    static final String RESOURCE_NAME = MessagingExtension.class.getPackage().getName() + ".LocalDescriptions";

    private static final int MANAGEMENT_API_MAJOR_VERSION = 1;
    private static final int MANAGEMENT_API_MINOR_VERSION = 2;
    private static final int MANAGEMENT_API_MICRO_VERSION = 0;

    public static ResourceDescriptionResolver getResourceDescriptionResolver(final String... keyPrefix) {
        return getResourceDescriptionResolver(true, keyPrefix);
    }

    public static ResourceDescriptionResolver getResourceDescriptionResolver(final boolean useUnprefixedChildTypes, final String... keyPrefix) {
        StringBuilder prefix = new StringBuilder();
        for (String kp : keyPrefix) {
            if (prefix.length() > 0){
                prefix.append('.');
            }
            prefix.append(kp);
        }
        return new StandardResourceDescriptionResolver(prefix.toString(), RESOURCE_NAME, MessagingExtension.class.getClassLoader(), true, useUnprefixedChildTypes);
    }

    public void initialize(ExtensionContext context) {
        final SubsystemRegistration subsystem = context.registerSubsystem(SUBSYSTEM_NAME,
                MANAGEMENT_API_MAJOR_VERSION,
                MANAGEMENT_API_MINOR_VERSION,
                MANAGEMENT_API_MICRO_VERSION);
        subsystem.registerXMLElementWriter(Messaging13SubsystemParser.getInstance());

        boolean registerRuntimeOnly = context.isRuntimeOnlyRegistrationValid();

        // Root resource
        final ManagementResourceRegistration rootRegistration = subsystem.registerSubsystemModel(MessagingSubsystemRootResourceDefinition.INSTANCE);
        rootRegistration.registerOperationHandler(DESCRIBE, GenericSubsystemDescribeHandler.INSTANCE, GenericSubsystemDescribeHandler.INSTANCE, false, OperationEntry.EntryType.PRIVATE);

        // HQ servers
        final ManagementResourceRegistration serverRegistration = rootRegistration.registerSubModel(new HornetQServerResourceDefinition(registerRuntimeOnly));

        // Runtime addresses
        if (registerRuntimeOnly) {
            final ManagementResourceRegistration coreAddress = serverRegistration.registerSubModel(new CoreAddressDefinition());
            coreAddress.setRuntimeOnly(true);
        }

        // Address settings
        serverRegistration.registerSubModel(new AddressSettingDefinition(registerRuntimeOnly));

        // Broadcast groups
        serverRegistration.registerSubModel(new BroadcastGroupDefinition(registerRuntimeOnly));
        // getConnectorPairs, -- no, this is just the same as attribute connector-refs

        // Discovery groups
        serverRegistration.registerSubModel(new DiscoveryGroupDefinition(registerRuntimeOnly));

        // Diverts
        serverRegistration.registerSubModel(new DivertDefinition(registerRuntimeOnly));

        // Core queues
        serverRegistration.registerSubModel(new QueueDefinition(registerRuntimeOnly));
        // getExpiryAddress, setExpiryAddress, getDeadLetterAddress, setDeadLetterAddress  -- no -- just toggle the 'queue-address', make this a mutable attr of address-setting

        // Acceptors
        serverRegistration.registerSubModel(GenericTransportDefinition.createAcceptorDefinition(registerRuntimeOnly));
        serverRegistration.registerSubModel(RemoteTransportDefinition.createAcceptorDefinition(registerRuntimeOnly));
        serverRegistration.registerSubModel(InVMTransportDefinition.createAcceptorDefinition(registerRuntimeOnly));

        // Connectors
        serverRegistration.registerSubModel(GenericTransportDefinition.createConnectorDefinition(registerRuntimeOnly));
        serverRegistration.registerSubModel(RemoteTransportDefinition.createConnectorDefinition(registerRuntimeOnly));
        serverRegistration.registerSubModel(InVMTransportDefinition.createConnectorDefinition(registerRuntimeOnly));

        // Bridges
        serverRegistration.registerSubModel(new BridgeDefinition(registerRuntimeOnly));

        // Cluster connections
        serverRegistration.registerSubModel(new ClusterConnectionDefinition(registerRuntimeOnly));

        // Grouping Handler
        serverRegistration.registerSubModel(new GroupingHandlerDefinition(registerRuntimeOnly));

        // Connector services
        serverRegistration.registerSubModel(new ConnectorServiceDefinition(registerRuntimeOnly));

        // Messaging paths
        //todo, shouldn't we leverage Path service from AS? see: package org.jboss.as.controller.services.path
        for (final String path : MessagingPathHandlers.PATHS) {
            ManagementResourceRegistration bindings = serverRegistration.registerSubModel(PathElement.pathElement(PATH, path),
                    new MessagingSubsystemProviders.PathProvider(path));
            MessagingPathHandlers.register(bindings);
        }

        // Connection factories
        serverRegistration.registerSubModel(new ConnectionFactoryDefinition(registerRuntimeOnly));
        // getJNDIBindings (no -- same as "entries")

        // Resource Adapter Pooled connection factories
        serverRegistration.registerSubModel(new PooledConnectionFactoryDefinition(registerRuntimeOnly));
        // TODO how do ConnectionFactoryControl things relate?

        // JMS Queues
        serverRegistration.registerSubModel(new JMSQueueDefinition(registerRuntimeOnly));
        // setExpiryAddress, setDeadLetterAddress  -- no -- just toggle the 'queue-address', make this a mutable attr of address-setting
        // getJNDIBindings (no -- same as "entries")

        // JMS Topics
        serverRegistration.registerSubModel(new JMSTopicDefinition(registerRuntimeOnly));
        // getJNDIBindings (no -- same as "entries")

        serverRegistration.registerSubModel(new SecuritySettingDefinition(registerRuntimeOnly));

        if (registerRuntimeOnly) {

            ResourceDefinition deploymentsDef = new SimpleResourceDefinition(SUBSYSTEM_PATH, getResourceDescriptionResolver("deployed"));
            final ManagementResourceRegistration deploymentsRegistration = subsystem.registerDeploymentModel(deploymentsDef);
            final ManagementResourceRegistration serverModel = deploymentsRegistration.registerSubModel(new HornetQServerResourceDefinition(true));

            serverModel.registerSubModel(JMSQueueDefinition.newDeployedJMSQueueDefinition());
            serverModel.registerSubModel(JMSTopicDefinition.newDeployedJMSTopicDefinition());
        }

        // JMS Bridges
        rootRegistration.registerSubModel(new JMSBridgeDefinition());

        registerTransformers_1_1_0(subsystem);
    }

    public void initializeParsers(ExtensionParsingContext context) {
        context.setSubsystemXmlMapping(SUBSYSTEM_NAME, MESSAGING_1_0.getUriString(), MessagingSubsystemParser.getInstance());
        context.setSubsystemXmlMapping(SUBSYSTEM_NAME, MESSAGING_1_1.getUriString(), MessagingSubsystemParser.getInstance());
        context.setSubsystemXmlMapping(SUBSYSTEM_NAME, MESSAGING_1_2.getUriString(), Messaging12SubsystemParser.getInstance());
        context.setSubsystemXmlMapping(SUBSYSTEM_NAME, MESSAGING_1_3.getUriString(), Messaging13SubsystemParser.getInstance());
    }

    private static void registerTransformers_1_1_0(final SubsystemRegistration subsystem) {
        final ModelVersion version_1_1_0 = ModelVersion.create(1, 1, 0);
        final TransformersSubRegistration transformers = subsystem.registerModelTransformers(version_1_1_0, new AbstractSubsystemTransformer(SUBSYSTEM_NAME) {

            @Override
            public ModelNode transformModel(final TransformationContext context, final ModelNode model) {
                ModelNode oldModel = model.clone();
                if (oldModel.hasDefined(HORNETQ_SERVER)) {
                    for (Property server : oldModel.get(HORNETQ_SERVER).asPropertyList()) {
                        if (server.getValue().hasDefined(POOLED_CONNECTION_FACTORY)) {
                            for (Property pooledConnectionFactory : server.getValue().get(POOLED_CONNECTION_FACTORY).asPropertyList()) {
                                oldModel.get(HORNETQ_SERVER, server.getName(), POOLED_CONNECTION_FACTORY, pooledConnectionFactory.getName()).remove(ConnectionFactoryAttributes.Pooled.USE_AUTO_RECOVERY.getName());
                                oldModel.get(HORNETQ_SERVER, server.getName(), POOLED_CONNECTION_FACTORY, pooledConnectionFactory.getName()).remove(ConnectionFactoryAttributes.Common.COMPRESS_LARGE_MESSAGES.getName());
                            }
                        }
                        if (server.getValue().hasDefined(CONNECTION_FACTORY)) {
                            for (Property connectionFactory : server.getValue().get(CONNECTION_FACTORY).asPropertyList()) {
                                if (!connectionFactory.getValue().hasDefined(HA.getName())) {
                                    oldModel.get(HORNETQ_SERVER, server.getName(), CONNECTION_FACTORY, connectionFactory.getName()).get(HA.getName()).set(HA.getDefaultValue());
                                }
                                if (connectionFactory.getValue().hasDefined(FACTORY_TYPE.getName()) && (connectionFactory.getValue().get(FACTORY_TYPE.getName()).equals(FACTORY_TYPE.getDefaultValue()))) {
                                    oldModel.get(HORNETQ_SERVER, server.getName(), CONNECTION_FACTORY, connectionFactory.getName()).get(FACTORY_TYPE.getName()).set(new ModelNode());
                                }
                            }
                        }
                    }
                }
                return oldModel;
            }
        });

        TransformersSubRegistration server = transformers.registerSubResource(PathElement.pathElement(CommonAttributes.HORNETQ_SERVER));
        server.registerOperationTransformer(ADD, new OperationTransformer() {
            @Override
            public TransformedOperation transformOperation(TransformationContext context, PathAddress address, ModelNode operation)
                    throws OperationFailedException {
                if (!operation.hasDefined(ID_CACHE_SIZE.getName())) {
                    operation.get(ID_CACHE_SIZE.getName()).set(ID_CACHE_SIZE.getDefaultValue());
                }
                return new TransformedOperation(operation, ORIGINAL_RESULT);
            }
        });

        RejectExpressionValuesTransformer rejectTransportParamExpressionTransformer = new RejectExpressionValuesTransformer(VALUE);
        final String[] transports = { CommonAttributes.ACCEPTOR, CommonAttributes.REMOTE_ACCEPTOR, CommonAttributes.IN_VM_ACCEPTOR,
                CommonAttributes.CONNECTOR, CommonAttributes.REMOTE_CONNECTOR, CommonAttributes.IN_VM_CONNECTOR };
        for (String transport : transports) {
            TransformersSubRegistration remoteConnector = server.registerSubResource(PathElement.pathElement(transport));
            TransformersSubRegistration transportParam = remoteConnector.registerSubResource(PathElement.pathElement(PARAM));
            transportParam.registerOperationTransformer(ADD, rejectTransportParamExpressionTransformer);
            transportParam.registerOperationTransformer(WRITE_ATTRIBUTE_OPERATION, rejectTransportParamExpressionTransformer.getWriteAttributeTransformer());
        }

        RejectExpressionValuesTransformer rejectExpressionTransformer = new RejectExpressionValuesTransformer(PATH);
        for (final String path : MessagingPathHandlers.PATHS) {
            TransformersSubRegistration pathRegistration = server.registerSubResource(PathElement.pathElement(PATH, path), rejectExpressionTransformer, rejectExpressionTransformer);
            pathRegistration.registerOperationTransformer(ADD, rejectExpressionTransformer);
            pathRegistration.registerOperationTransformer(WRITE_ATTRIBUTE_OPERATION, rejectExpressionTransformer.getWriteAttributeTransformer());
        }
        TransformersSubRegistration pooledConnectionFactory = server.registerSubResource(PooledConnectionFactoryDefinition.PATH);
        pooledConnectionFactory.registerOperationTransformer(ADD, new OperationTransformer() {
            @Override
            public TransformedOperation transformOperation(final TransformationContext context, final PathAddress address, final ModelNode operation)
                    throws OperationFailedException {
                final ModelNode transformedOperation = operation.clone();
                transformedOperation.remove(ConnectionFactoryAttributes.Pooled.USE_AUTO_RECOVERY.getName());
                transformedOperation.remove(ConnectionFactoryAttributes.Common.COMPRESS_LARGE_MESSAGES.getName());
                return new TransformedOperation(transformedOperation, ORIGINAL_RESULT);
            }
        });
        pooledConnectionFactory.registerOperationTransformer(WRITE_ATTRIBUTE_OPERATION, new OperationTransformer() {
            @Override
            public TransformedOperation transformOperation(final TransformationContext context, final PathAddress address, final ModelNode operation)
                    throws OperationFailedException {

                OperationResultTransformer resultTransformer = ORIGINAL_RESULT;
                final List<String> found = new ArrayList<String>();

                String[] unsupportedAttributes = {ConnectionFactoryAttributes.Pooled.USE_AUTO_RECOVERY.getName(), ConnectionFactoryAttributes.Common.COMPRESS_LARGE_MESSAGES.getName()};
                for (String attrName : unsupportedAttributes) {
                    if (operation.require(NAME).asString().equals(attrName)) {
                        if (found.size() == 0) {
                            // Transform the result into a failure if the op wasn't ignored
                            resultTransformer = new OperationResultTransformer() {
                                @Override
                                public ModelNode transformResult(ModelNode result) {
                                    ModelNode transformed = result;
                                    if (!IGNORED.equals(result.get(OUTCOME).asString())) {
                                        transformed = new ModelNode();
                                        transformed.get(OUTCOME).set(FAILED);
                                        transformed.get(FAILURE_DESCRIPTION).set(MessagingMessages.MESSAGES.unsupportedAttributeInVersion(found.toString(), version_1_1_0));
                                    }
                                    return transformed;
                                }
                            };
                        }
                        found.add(attrName);
                    }
                }

                return new TransformedOperation(operation, resultTransformer);
            }
        });
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7476.java