error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7232.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7232.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7232.java
text:
```scala
c@@ontext.completeStep(OperationContext.RollbackHandler.NOOP_ROLLBACK_HANDLER);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.as.messaging.jms.bridge;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.server.Services.addServerExecutorDependency;

import java.util.List;
import java.util.Properties;

import org.hornetq.jms.bridge.ConnectionFactoryFactory;
import org.hornetq.jms.bridge.DestinationFactory;
import org.hornetq.jms.bridge.JMSBridge;
import org.hornetq.jms.bridge.QualityOfServiceMode;
import org.hornetq.jms.bridge.impl.JMSBridgeImpl;
import org.hornetq.jms.bridge.impl.JNDIConnectionFactoryFactory;
import org.hornetq.jms.bridge.impl.JNDIDestinationFactory;
import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.messaging.CommonAttributes;
import org.jboss.as.messaging.MessagingServices;
import org.jboss.as.naming.deployment.ContextNames;
import org.jboss.as.txn.service.TxnServices;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.ServiceName;

/**
 * JMS Bridge add update.
 *
 * @author Jeff Mesnil (c) 2012 Red Hat Inc.
 */
public class JMSBridgeAdd extends AbstractAddStepHandler {
    public static final JMSBridgeAdd INSTANCE = new JMSBridgeAdd();

    private JMSBridgeAdd() {
    }

    @Override
    protected void populateModel(final ModelNode operation, final ModelNode model) throws OperationFailedException {
        for (final AttributeDefinition attributeDefinition : JMSBridgeDefinition.JMS_BRIDGE_ATTRIBUTES) {
            attributeDefinition.validateAndSet(operation, model);
        }
        for (final AttributeDefinition attributeDefinition : JMSBridgeDefinition.JMS_TARGET_ATTRIBUTES) {
            attributeDefinition.validateAndSet(operation, model);
        }
        for (final AttributeDefinition attributeDefinition : JMSBridgeDefinition.JMS_SOURCE_ATTRIBUTES) {
            attributeDefinition.validateAndSet(operation, model);
        }
    }

    @Override
    protected void performRuntime(final OperationContext context, final ModelNode operation, final ModelNode model,
            final ServiceVerificationHandler verificationHandler, final List<ServiceController<?>> newControllers)
                    throws OperationFailedException {
        context.addStep(new OperationStepHandler() {

            @Override
            public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
                final PathAddress address = PathAddress.pathAddress(operation.get(OP_ADDR));

                String moduleName = resolveAttribute(JMSBridgeDefinition.MODULE, context, model);
                final JMSBridge bridge = createJMSBridge(context, model);

                final String bridgeName = address.getLastElement().getValue();
                final JMSBridgeService bridgeService = new JMSBridgeService(moduleName, bridgeName, bridge);
                final ServiceName bridgeServiceName = MessagingServices.getJMSBridgeServiceName(bridgeName);

                final ServiceBuilder<JMSBridge> jmsBridgeServiceBuilder = context.getServiceTarget().addService(bridgeServiceName, bridgeService)
                        .addListener(verificationHandler)
                        .addDependency(TxnServices.JBOSS_TXN_TRANSACTION_MANAGER)
                        .setInitialMode(Mode.ACTIVE);
                addServerExecutorDependency(jmsBridgeServiceBuilder, bridgeService.getExecutorInjector(), false);
                if (dependsOnLocalResources(model, JMSBridgeDefinition.SOURCE_CONTEXT)) {
                    addDependencyForJNDIResource(jmsBridgeServiceBuilder, model, context, JMSBridgeDefinition.SOURCE_CONNECTION_FACTORY);
                    addDependencyForJNDIResource(jmsBridgeServiceBuilder, model, context, JMSBridgeDefinition.SOURCE_DESTINATION);
                }
                if (dependsOnLocalResources(model, JMSBridgeDefinition.TARGET_CONTEXT)) {
                    addDependencyForJNDIResource(jmsBridgeServiceBuilder, model, context, JMSBridgeDefinition.TARGET_CONNECTION_FACTORY);
                    addDependencyForJNDIResource(jmsBridgeServiceBuilder, model, context, JMSBridgeDefinition.TARGET_DESTINATION);
                }

                newControllers.add(jmsBridgeServiceBuilder.install());

                context.completeStep();
            }

        }, OperationContext.Stage.RUNTIME);
    }

    private boolean dependsOnLocalResources(ModelNode model, AttributeDefinition attr) throws OperationFailedException {
        // if either the source or target context attribute is not defined, this means that the JMS resources will be looked up
        // from the local HornetQ server.
        return !(model.hasDefined(attr.getName()));
    }

    private void addDependencyForJNDIResource(final ServiceBuilder<JMSBridge> builder, final ModelNode model, final OperationContext context,
            final AttributeDefinition attribute) throws OperationFailedException {
        String jndiName = attribute.resolveModelAttribute(context, model).asString();
        builder.addDependency(ContextNames.bindInfoFor(jndiName).getBinderServiceName());
    }

    private JMSBridge createJMSBridge(OperationContext context, ModelNode model) throws OperationFailedException {
        final Properties sourceContextProperties = resolveContextProperties(JMSBridgeDefinition.SOURCE_CONTEXT, context, model);
        final String sourceConnectionFactoryName = JMSBridgeDefinition.SOURCE_CONNECTION_FACTORY.resolveModelAttribute(context, model).asString();
        final ConnectionFactoryFactory sourceCff = new JNDIConnectionFactoryFactory(sourceContextProperties , sourceConnectionFactoryName);
        final String sourceDestinationName = JMSBridgeDefinition.SOURCE_DESTINATION.resolveModelAttribute(context, model).asString();
        final DestinationFactory sourceDestinationFactory = new JNDIDestinationFactory(sourceContextProperties, sourceDestinationName);

        final Properties targetContextProperties = resolveContextProperties(JMSBridgeDefinition.TARGET_CONTEXT, context, model);
        final String targetConnectionFactoryName = JMSBridgeDefinition.TARGET_CONNECTION_FACTORY.resolveModelAttribute(context, model).asString();
        final ConnectionFactoryFactory targetCff = new JNDIConnectionFactoryFactory(targetContextProperties, targetConnectionFactoryName);
        final String targetDestinationName = JMSBridgeDefinition.TARGET_DESTINATION.resolveModelAttribute(context, model).asString();
        final DestinationFactory targetDestinationFactory = new JNDIDestinationFactory(targetContextProperties, targetDestinationName);

        final String sourceUsername = resolveAttribute(JMSBridgeDefinition.SOURCE_USER, context, model);
        final String sourcePassword = resolveAttribute(JMSBridgeDefinition.SOURCE_PASSWORD, context, model);
        final String targetUsername = resolveAttribute(JMSBridgeDefinition.TARGET_USER, context, model);
        final String targetPassword = resolveAttribute(JMSBridgeDefinition.TARGET_PASSWORD, context, model);

        final String selector = resolveAttribute(CommonAttributes.SELECTOR, context, model);
        final long failureRetryInterval = JMSBridgeDefinition.FAILURE_RETRY_INTERVAL.resolveModelAttribute(context, model).asLong();
        final int maxRetries = JMSBridgeDefinition.MAX_RETRIES.resolveModelAttribute(context, model).asInt();
        final QualityOfServiceMode qosMode = QualityOfServiceMode.valueOf( JMSBridgeDefinition.QUALITY_OF_SERVICE.resolveModelAttribute(context, model).asString());
        final int maxBatchSize = JMSBridgeDefinition.MAX_BATCH_SIZE.resolveModelAttribute(context, model).asInt();
        final long maxBatchTime = JMSBridgeDefinition.MAX_BATCH_TIME.resolveModelAttribute(context, model).asLong();
        final String subName =  resolveAttribute(JMSBridgeDefinition.SUBSCRIPTION_NAME, context, model);
        final String clientID = resolveAttribute(JMSBridgeDefinition.CLIENT_ID, context, model);
        final boolean addMessageIDInHeader = JMSBridgeDefinition.ADD_MESSAGE_ID_IN_HEADER.resolveModelAttribute(context, model).asBoolean();

        return new JMSBridgeImpl(sourceCff,
                targetCff,
                sourceDestinationFactory,
                targetDestinationFactory,
                sourceUsername,
                sourcePassword,
                targetUsername,
                targetPassword,
                selector,
                failureRetryInterval,
                maxRetries,
                qosMode,
                maxBatchSize,
                maxBatchTime,
                subName,
                clientID,
                addMessageIDInHeader);
    }

    private Properties resolveContextProperties(AttributeDefinition attribute, OperationContext context, ModelNode model) throws OperationFailedException {
        final ModelNode contextModel = attribute.resolveModelAttribute(context, model);
        if (!contextModel.isDefined()) {
            return null;
        }

        final Properties contextProperties = new Properties();
        for (Property property : contextModel.asPropertyList()) {
            contextProperties.put(property.getName(), property.getValue().asString());
        }
        return contextProperties;
    }

    /**
     * Return null if the resolved attribute is not defined
     */
    private String resolveAttribute(SimpleAttributeDefinition attr, OperationContext context, ModelNode model) throws OperationFailedException {
        final ModelNode node = attr.resolveModelAttribute(context, model);
        return node.isDefined() ? node.asString() : null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7232.java