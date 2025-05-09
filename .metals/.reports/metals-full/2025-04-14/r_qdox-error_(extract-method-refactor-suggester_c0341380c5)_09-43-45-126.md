error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6930.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6930.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6930.java
text:
```scala
Q@@ueueControlHandler.INSTANCE.registerOperations(registry, getResourceDescriptionResolver());

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

package org.jboss.as.messaging;

import static org.jboss.as.controller.SimpleAttributeDefinitionBuilder.create;
import static org.jboss.as.messaging.CommonAttributes.QUEUE;
import static org.jboss.as.messaging.CommonAttributes.RUNTIME_QUEUE;
import static org.jboss.dmr.ModelType.LONG;

import java.util.Collections;
import java.util.List;

import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.access.constraint.ApplicationTypeConfig;
import org.jboss.as.controller.access.management.AccessConstraintDefinition;
import org.jboss.as.controller.access.management.ApplicationTypeAccessConstraintDefinition;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.Resource;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;


/**
 * Queue resource definition
 *
 * @author <a href="http://jmesnil.net">Jeff Mesnil</a> (c) 2012 Red Hat Inc.
 */
public class QueueDefinition extends SimpleResourceDefinition {

    public static final PathElement PATH = PathElement.pathElement(CommonAttributes.QUEUE);

    public static final SimpleAttributeDefinition ADDRESS = create("queue-address", ModelType.STRING)
            .setXmlName(CommonAttributes.ADDRESS)
            .setAllowExpression(true)
            .setRestartAllServices()
            .build();

    static final SimpleAttributeDefinition[] ATTRIBUTES = { ADDRESS, CommonAttributes.FILTER, CommonAttributes.DURABLE };

    public static final SimpleAttributeDefinition EXPIRY_ADDRESS = create(CommonAttributes.EXPIRY_ADDRESS)
            .setStorageRuntime()
            .build();

    public static final SimpleAttributeDefinition DEAD_LETTER_ADDRESS = create(CommonAttributes.DEAD_LETTER_ADDRESS)
            .setStorageRuntime()
            .build();

    static final AttributeDefinition ID= create("id", LONG)
            .setStorageRuntime()
            .build();

    static final AttributeDefinition[] READONLY_ATTRIBUTES = { CommonAttributes.PAUSED, CommonAttributes.TEMPORARY, ID, DEAD_LETTER_ADDRESS, EXPIRY_ADDRESS };

    static final AttributeDefinition[] METRICS = { CommonAttributes.MESSAGE_COUNT, CommonAttributes.DELIVERING_COUNT, CommonAttributes.MESSAGES_ADDED,
            CommonAttributes.SCHEDULED_COUNT, CommonAttributes.CONSUMER_COUNT
            };

    public static QueueDefinition newRuntimeQueueDefinition(final boolean registerRuntimeOnly) {
        return new QueueDefinition(registerRuntimeOnly, true, RUNTIME_QUEUE, null, null);
    }

    public static QueueDefinition newQueueDefinition(final boolean registerRuntimeOnly) {
        return new QueueDefinition(registerRuntimeOnly, false, QUEUE, QueueAdd.INSTANCE, QueueRemove.INSTANCE);
    }

    private final boolean registerRuntimeOnly;
    private final boolean runtimeOnly;

    private final List<AccessConstraintDefinition> accessConstraints;

    private QueueDefinition(final boolean registerRuntimeOnly, final boolean runtimeOnly,
            final String path,
            final AbstractAddStepHandler addHandler,
            final OperationStepHandler removeHandler) {
        super(PathElement.pathElement(path),
                MessagingExtension.getResourceDescriptionResolver(CommonAttributes.QUEUE),
                addHandler,
                removeHandler);
        this.registerRuntimeOnly = registerRuntimeOnly;
        this.runtimeOnly = runtimeOnly;
        if (!runtimeOnly) {
            ApplicationTypeConfig atc = new ApplicationTypeConfig(MessagingExtension.SUBSYSTEM_NAME, path);
            accessConstraints = new ApplicationTypeAccessConstraintDefinition(atc).wrapAsList();
        } else {
            accessConstraints = Collections.emptyList();
        }
    }

    @Override
    public void registerAttributes(ManagementResourceRegistration registry) {
        super.registerAttributes(registry);

        for (SimpleAttributeDefinition attr : ATTRIBUTES) {
            if (registerRuntimeOnly || !attr.getFlags().contains(AttributeAccess.Flag.STORAGE_RUNTIME)) {
                if (runtimeOnly) {
                    AttributeDefinition readOnlyRuntimeAttr = create(attr)
                            .setStorageRuntime()
                            .build();
                    registry.registerReadOnlyAttribute(readOnlyRuntimeAttr, QueueReadAttributeHandler.RUNTIME_INSTANCE);
                } else {
                    registry.registerReadOnlyAttribute(attr, null);
                }
            }
        }

        if (registerRuntimeOnly) {
            for (AttributeDefinition attr : READONLY_ATTRIBUTES) {
                registry.registerReadOnlyAttribute(attr, QueueReadAttributeHandler.INSTANCE);
            }

            for (AttributeDefinition metric : METRICS) {
                registry.registerMetric(metric, QueueReadAttributeHandler.INSTANCE);
            }
        }
    }

    @Override
    public void registerOperations(ManagementResourceRegistration registry) {
        super.registerOperations(registry);

        if (registerRuntimeOnly) {
            QueueControlHandler.INSTANCE.registerOperations(registry);
        }
    }

    @Override
    public List<AccessConstraintDefinition> getAccessConstraints() {
        return accessConstraints;
    }

    /**
     * [AS7-5850] Core queues created with HornetQ API does not create AS7 resources
     *
     * For backwards compatibility if an operation is invoked on a queue that has no corresponding resources,
     * we forward the operation to the corresponding runtime-queue resource (which *does* exist).
     *
     * @return true if the operation is forwarded to the corresponding runtime-queue resource, false else.
     */
    static boolean forwardToRuntimeQueue(OperationContext context, ModelNode operation, OperationStepHandler handler) {
        PathAddress address = PathAddress.pathAddress(operation.require(ModelDescriptionConstants.OP_ADDR));

        // do not forward if the current operation is for a runtime-queue already:
        if (RUNTIME_QUEUE.equals(address.getLastElement().getKey())) {
            return false;
        }

        String queueName = address.getLastElement().getValue();

        PathAddress hornetQPathAddress = MessagingServices.getHornetQServerPathAddress(address);
        Resource hornetqServerResource = context.readResourceFromRoot(hornetQPathAddress);
        boolean hasChild = hornetqServerResource.hasChild(address.getLastElement());
        if (hasChild) {
            return false;
        } else {
            // there is no registered queue resource, forward to the runtime-queue address instead
            ModelNode forwardOperation = operation.clone();
            forwardOperation.get(ModelDescriptionConstants.OP_ADDR).set(hornetQPathAddress.append(RUNTIME_QUEUE, queueName).toModelNode());
            context.addStep(forwardOperation, handler, OperationContext.Stage.RUNTIME, true);
            return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6930.java