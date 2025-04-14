error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6067.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6067.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6067.java
text:
```scala
r@@egistry.registerReadWriteAttribute(CommonAttributes.LIVE_CONNECTOR_REF, null, DeprecatedAttributeWriteHandler.INSTANCE);

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

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE;
import static org.jboss.as.messaging.CommonAttributes.CLUSTERED;
import static org.jboss.as.messaging.HornetQActivationService.isHornetQServerActive;
import static org.jboss.as.messaging.MessagingMessages.MESSAGES;

import java.util.Set;

import org.hornetq.api.core.management.HornetQServerControl;
import org.hornetq.core.server.HornetQServer;
import org.jboss.as.controller.AbstractWriteAttributeHandler;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistry;

/**
 * Write attribute handler for attributes that update HornetQServerControl.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class HornetQServerControlWriteHandler extends AbstractWriteAttributeHandler<Void> {

    public static final HornetQServerControlWriteHandler INSTANCE = new HornetQServerControlWriteHandler();

    private HornetQServerControlWriteHandler() {
        super(CommonAttributes.SIMPLE_ROOT_RESOURCE_ATTRIBUTES);
    }

    public void registerAttributes(final ManagementResourceRegistration registry, boolean registerRuntimeOnly) {
        for (AttributeDefinition attr : CommonAttributes.SIMPLE_ROOT_RESOURCE_ATTRIBUTES) {
            if (registerRuntimeOnly || !attr.getFlags().contains(AttributeAccess.Flag.STORAGE_RUNTIME)) {
                if (attr.getName().equals(CLUSTERED.getName())) {
                    registry.registerReadWriteAttribute(CLUSTERED,
                            ClusteredAttributeHandlers.READ_HANDLER,
                            ClusteredAttributeHandlers.WRITE_HANDLER);
                } else {
                    registry.registerReadWriteAttribute(attr, null, this);
                }
            }
        }

        // handle deprecate attributes
        registry.registerReadWriteAttribute(CommonAttributes.LIVE_CONNECTOR_REF, null, new DeprecatedAttributeWriteHandler(CommonAttributes.LIVE_CONNECTOR_REF.getName()));
    }

    @Override
    protected boolean applyUpdateToRuntime(final OperationContext context, final ModelNode operation, final String attributeName,
                                           final ModelNode newValue, final ModelNode currentValue,
                                           final HandbackHolder<Void> handbackHolder) throws OperationFailedException {
        AttributeDefinition attr = getAttributeDefinition(attributeName);
        if (attr.getFlags().contains(AttributeAccess.Flag.RESTART_ALL_SERVICES)) {
            // Restart required
            return true;
        } else {

            ServiceRegistry registry = context.getServiceRegistry(true);
            final ServiceName hqServiceName = MessagingServices.getHornetQServiceName(PathAddress.pathAddress(operation.get(OP_ADDR)));
            ServiceController<?> hqService = registry.getService(hqServiceName);
            if (hqService == null) {
                // The service isn't installed, so the work done in the Stage.MODEL part is all there is to it
                return false;
            } else if (hqService.getState() != ServiceController.State.UP) {
                // Service is installed but not up?
                //throw new IllegalStateException(String.format("Cannot apply attribute %s to runtime; service %s is not in state %s, it is in state %s",
                //            attributeName, MessagingServices.JBOSS_MESSAGING, ServiceController.State.UP, hqService.getState()));
                // No, don't barf; just let the update apply to the model and put the server in a reload-required state
                return true;
            } else {
                if (!isHornetQServerActive(context, operation)) {
                    return false;
                }
                applyOperationToHornetQService(context, operation, attributeName, hqService);
                return false;
            }
        }
    }

    @Override
    protected void revertUpdateToRuntime(final OperationContext context, final ModelNode operation,
                                         final String attributeName, final ModelNode valueToRestore,
                                         final ModelNode valueToRevert,
                                         final Void handback) throws OperationFailedException {

        AttributeDefinition attr = getAttributeDefinition(attributeName);
        if (!attr.getFlags().contains(AttributeAccess.Flag.RESTART_ALL_SERVICES)) {
            ServiceRegistry registry = context.getServiceRegistry(true);
            final ServiceName hqServiceName = MessagingServices.getHornetQServiceName(PathAddress.pathAddress(operation.get(OP_ADDR)));
            ServiceController<?> hqService = registry.getService(hqServiceName);
            if (hqService != null && hqService.getState() == ServiceController.State.UP) {
                // Create and execute a write-attribute operation that uses the valueToRestore
                ModelNode revertOp = operation.clone();
                revertOp.get(attributeName).set(valueToRestore);
                applyOperationToHornetQService(context, revertOp, attributeName, hqService);
            }
        }
    }

    private void applyOperationToHornetQService(final OperationContext context, ModelNode operation, String attributeName, ServiceController<?> hqService) {
        HornetQServerControl serverControl = HornetQServer.class.cast(hqService.getValue()).getHornetQServerControl();
        try {
            if (attributeName.equals(CommonAttributes.FAILOVER_ON_SHUTDOWN.getName()))  {
                serverControl.setFailoverOnServerShutdown(CommonAttributes.FAILOVER_ON_SHUTDOWN.resolveModelAttribute(context, operation).asBoolean());
            } else if (attributeName.equals(CommonAttributes.MESSAGE_COUNTER_SAMPLE_PERIOD.getName())) {
                serverControl.setMessageCounterSamplePeriod(CommonAttributes.MESSAGE_COUNTER_SAMPLE_PERIOD.resolveModelAttribute(context, operation).asLong());
            } else if (attributeName.equals(CommonAttributes.MESSAGE_COUNTER_MAX_DAY_HISTORY.getName())) {
                serverControl.setMessageCounterMaxDayCount(CommonAttributes.MESSAGE_COUNTER_MAX_DAY_HISTORY.resolveModelAttribute(context, operation).asInt());
            } else if (attributeName.equals(CommonAttributes.MESSAGE_COUNTER_ENABLED.getName())) {
                boolean enabled = CommonAttributes.MESSAGE_COUNTER_ENABLED.resolveModelAttribute(context, operation).asBoolean();
                if (enabled) {
                    serverControl.enableMessageCounters();
                } else {
                    serverControl.disableMessageCounters();
                }
            } else {
                // Bug! Someone added the attribute to the set but did not implement
                throw MESSAGES.unsupportedRuntimeAttribute(attributeName);
            }

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * The clustered configuration parameter no longer exists for HornetQ configuration (a hornetq server is automatically clustered if it has cluster-connections)
     * but we continue to support it for legacy versions.
     *
     * For AS7 new versions, we compute its value based on the presence of cluster-connection children and ignore any write-attribute operation on it.
     * We only warn the user if he wants to disable the clustered state of the server by setting it to false.
     */
    private static final class ClusteredAttributeHandlers {
        static final OperationStepHandler READ_HANDLER = new OperationStepHandler() {
            @Override
            public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
                boolean clustered = isClustered(context);
                context.getResult().set(clustered);
                context.stepCompleted();
            }
        };

        static final OperationStepHandler WRITE_HANDLER = new OperationStepHandler() {
            @Override
            public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
                // the real clustered HornetQ state
                boolean clustered = isClustered(context);
                // whether the user wants the server to be clustered
                ModelNode mock = new ModelNode();
                mock.get(CLUSTERED.getName()).set(operation.get(VALUE));
                boolean wantsClustered = CLUSTERED.resolveModelAttribute(context, mock).asBoolean();
                if (clustered && !wantsClustered) {
                    PathAddress serverAddress = PathAddress.pathAddress(operation.get(OP_ADDR));
                    MessagingLogger.MESSAGING_LOGGER.canNotChangeClusteredAttribute(serverAddress);
                }
                // ignore the operation
                context.stepCompleted();
            }
        };

        private static boolean isClustered(OperationContext context) {
            Set<String> clusterConnectionNames = context.readResource(PathAddress.EMPTY_ADDRESS).getChildrenNames(ClusterConnectionDefinition.PATH.getKey());
            return !clusterConnectionNames.isEmpty();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6067.java