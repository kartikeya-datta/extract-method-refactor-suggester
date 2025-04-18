error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5758.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5758.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5758.java
text:
```scala
.@@setDefaultValue(new ModelNode().set(600000L))

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

package org.jboss.as.clustering.infinispan.subsystem;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE;

import java.util.Map;

import org.jboss.as.clustering.infinispan.InfinispanLogger;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.ModelVersion;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ReloadRequiredWriteAttributeHandler;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.client.helpers.MeasurementUnit;
import org.jboss.as.controller.operations.validation.IntRangeValidator;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.services.path.ResolvePathHandler;
import org.jboss.as.controller.transform.TransformationContext;
import org.jboss.as.controller.transform.description.AttributeConverter;
import org.jboss.as.controller.transform.description.DefaultCheckersAndConverter;
import org.jboss.as.controller.transform.description.DiscardAttributeChecker;
import org.jboss.as.controller.transform.description.RejectAttributeChecker;
import org.jboss.as.controller.transform.description.ResourceTransformationDescriptionBuilder;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * Resource description for the addressable resource /subsystem=infinispan/cache-container=X/distributed-cache=*
 *
 * @author Richard Achmatowicz (c) 2011 Red Hat Inc.
 * @author Radoslav Husar
 */
public class DistributedCacheResourceDefinition extends SharedCacheResourceDefinition {

    static final PathElement WILDCARD_PATH = pathElement(PathElement.WILDCARD_VALUE);

    static PathElement pathElement(String name) {
        return PathElement.pathElement(ModelKeys.DISTRIBUTED_CACHE, name);
    }

    // attributes
    static final SimpleAttributeDefinition L1_LIFESPAN = new SimpleAttributeDefinitionBuilder(ModelKeys.L1_LIFESPAN, ModelType.LONG, true)
            .setXmlName(Attribute.L1_LIFESPAN.getLocalName())
            .setMeasurementUnit(MeasurementUnit.MILLISECONDS)
            .setAllowExpression(true)
            .setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES)
            .setDefaultValue(new ModelNode().set(600000))
            .build();

    static final SimpleAttributeDefinition OWNERS = new SimpleAttributeDefinitionBuilder(ModelKeys.OWNERS, ModelType.INT, true)
            .setXmlName(Attribute.OWNERS.getLocalName())
            .setAllowExpression(true)
            .setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES)
            .setDefaultValue(new ModelNode().set(2))
            .setValidator(new IntRangeValidator(1, true, true))
            .build();

    @Deprecated
    static final SimpleAttributeDefinition VIRTUAL_NODES = new SimpleAttributeDefinitionBuilder(ModelKeys.VIRTUAL_NODES, ModelType.INT, true)
            .setXmlName(Attribute.VIRTUAL_NODES.getLocalName())
            .setAllowExpression(false)
            .setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES)
            .setDefaultValue(new ModelNode().set(1))
            .setDeprecated(InfinispanModel.VERSION_1_4_0.getVersion())
            .setAlternatives(ModelKeys.SEGMENTS)
            .build();

    @SuppressWarnings("deprecation")
    static final SimpleAttributeDefinition SEGMENTS = new SimpleAttributeDefinitionBuilder(ModelKeys.SEGMENTS, ModelType.INT, true)
            .setXmlName(Attribute.SEGMENTS.getLocalName())
            .setAllowExpression(true)
            .setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES)
            .setDefaultValue(new ModelNode().set(80)) // Recommended value is 10 * max_cluster_size.
            .setValidator(new IntRangeValidator(1, true, true))
            .setAlternatives(ModelKeys.VIRTUAL_NODES)
            .build();

    static final AttributeDefinition[] ATTRIBUTES = new AttributeDefinition[] { OWNERS, SEGMENTS, L1_LIFESPAN };

    static void buildTransformation(ModelVersion version, ResourceTransformationDescriptionBuilder parent) {
        ResourceTransformationDescriptionBuilder builder = parent.addChildResource(WILDCARD_PATH);

        if (InfinispanModel.VERSION_1_4_0.requiresTransformation(version)) {
            // Convert segments to virtual-nodes if it is set
            AttributeConverter converter = new AttributeConverter.DefaultAttributeConverter() {
                @Override
                protected void convertAttribute(PathAddress address, String attributeName, ModelNode attributeValue, TransformationContext context) {
                    if (attributeValue.isDefined()) {
                        attributeValue.set(SegmentsAndVirtualNodeConverter.segmentsToVirtualNodes(attributeValue.asString()));
                    }
                }
            };
            builder.getAttributeBuilder()
                .setDiscard(DiscardAttributeChecker.UNDEFINED, SEGMENTS)
                .setValueConverter(converter, SEGMENTS)
                .addRename(SEGMENTS, VIRTUAL_NODES.getName())
                .addRejectCheck(RejectAttributeChecker.SIMPLE_EXPRESSIONS, L1_LIFESPAN, OWNERS, VIRTUAL_NODES, SEGMENTS);

        } else if (InfinispanModel.VERSION_1_4_1.requiresTransformation(version)) {
            DiscardAttributeChecker checker = new DiscardAttributeChecker.DefaultDiscardAttributeChecker(false, true) {
                @Override
                protected boolean isValueDiscardable(PathAddress address, String attributeName, ModelNode attributeValue, TransformationContext context) {
                    return (attributeValue.isDefined() && attributeValue.equals(new ModelNode(1)));
                }
            };
            DefaultCheckersAndConverter checkersAndConverter = new DefaultCheckersAndConverter() {
                @Override
                public String getRejectionLogMessage(Map<String, ModelNode> attributes) {
                    return InfinispanLogger.ROOT_LOGGER.segmentsDoesNotSupportExpressions();
                }

                @Override
                protected boolean rejectAttribute(PathAddress address, String attributeName, ModelNode attributeValue, TransformationContext context) {
                    if (checkForExpression(attributeValue)) {
                        return true;
                    }
                    return false;
                }

                @Override
                protected void convertAttribute(PathAddress address, String attributeName, ModelNode attributeValue, TransformationContext context) {
                    if (attributeValue.isDefined()) {
                        attributeValue.set(SegmentsAndVirtualNodeConverter.virtualNodesToSegments(attributeValue));
                    }
                }

                @Override
                protected boolean isValueDiscardable(PathAddress address, String attributeName, ModelNode attributeValue, TransformationContext context) {
                    // not used for discard - there is a separate transformer for this
                    return false;
                }
            };
            //Convert virtual-nodes to segments if it is set
            // this is required to address WFLY-2598
            builder.getAttributeBuilder()
                    .setDiscard(checker, VIRTUAL_NODES)
                    .addRejectCheck(checkersAndConverter, VIRTUAL_NODES)
                    .setValueConverter(checkersAndConverter, VIRTUAL_NODES)
                    .addRename(VIRTUAL_NODES, SEGMENTS.getName());
        }

        SharedCacheResourceDefinition.buildTransformation(version, builder);
    }

    DistributedCacheResourceDefinition(ResolvePathHandler resolvePathHandler, boolean allowRuntimeOnlyRegistration) {
        super(ModelKeys.DISTRIBUTED_CACHE, DistributedCacheAddHandler.INSTANCE, CacheRemoveHandler.INSTANCE, resolvePathHandler, allowRuntimeOnlyRegistration);
    }

    @Override
    public void registerAttributes(ManagementResourceRegistration registration) {
        super.registerAttributes(registration);

        // check that we don't need a special handler here?
        final OperationStepHandler writeHandler = new ReloadRequiredWriteAttributeHandler(ATTRIBUTES);
        for (AttributeDefinition attr : ATTRIBUTES) {
            registration.registerReadWriteAttribute(attr, null, writeHandler);
        }

        // Attribute virtual-nodes has been deprecated, convert to the corresponding segments value if not the default.
        final OperationStepHandler virtualNodesWriteHandler = new OperationStepHandler() {
            @Override
            public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
                if (operation.hasDefined(VALUE) && operation.get(VALUE).asInt() != 1) {

                    // log a WARN
                    InfinispanLogger.ROOT_LOGGER.virtualNodesAttributeDeprecated();

                    // convert the virtual nodes value to a segments value and write
                    ModelNode convertedValue = SegmentsAndVirtualNodeConverter.virtualNodesToSegments(operation.get(VALUE));
                    final ModelNode submodel = context.readResourceForUpdate(PathAddress.EMPTY_ADDRESS).getModel();
                    final ModelNode syntheticOp = new ModelNode();
                    syntheticOp.get(ModelKeys.SEGMENTS).set(convertedValue);
                    SEGMENTS.validateAndSet(syntheticOp, submodel);

                    // since we modified the model, set reload required
                    if (requiresRuntime(context)) {
                        context.addStep(new OperationStepHandler() {
                            @Override
                            public void execute(OperationContext context, ModelNode operation) {
                                context.reloadRequired();
                                context.completeStep(OperationContext.RollbackHandler.REVERT_RELOAD_REQUIRED_ROLLBACK_HANDLER);
                            }
                        }, OperationContext.Stage.RUNTIME);
                    }
                }
                context.stepCompleted();
            }

            protected boolean requiresRuntime(OperationContext context) {
                return context.getProcessType().isServer() && !context.isBooting();
            }
        };

        // Legacy attributes
        registration.registerReadWriteAttribute(VIRTUAL_NODES, null, virtualNodesWriteHandler);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5758.java