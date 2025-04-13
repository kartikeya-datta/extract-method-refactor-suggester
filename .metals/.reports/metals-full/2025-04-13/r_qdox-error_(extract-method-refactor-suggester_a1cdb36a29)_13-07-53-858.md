error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10770.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10770.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10770.java
text:
```scala
T@@ransformationContext opCtx = ResourceTransformationContextImpl.wrapForOperation(context, operation);

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

package org.jboss.as.controller.transform;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;

import java.util.Iterator;
import java.util.List;

import org.jboss.as.controller.ControllerLogger;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.registry.Resource;
import org.jboss.dmr.ModelNode;

/**
 * @author Emanuel Muckenhuber
 * @author Tomaz Cerar
 */
public class TransformersImpl implements Transformers {
    private final TransformationTarget target;

    TransformersImpl(TransformationTarget target) {
        assert target != null;
        this.target = target;
    }

    @Override
    public TransformationTarget getTarget() {
        return target;
    }

    @Override
    public OperationTransformer.TransformedOperation transformOperation(final TransformationContext context, final ModelNode operation) throws OperationFailedException {

        final PathAddress address = PathAddress.pathAddress(operation.require(OP_ADDR));
        final String operationName = operation.require(OP).asString();

        final OperationTransformer transformer = target.resolveTransformer(address, operationName);
        if (transformer == null) {
            ControllerLogger.ROOT_LOGGER.tracef("operation %s does not need transformation", operation);
            return new OperationTransformer.TransformedOperation(operation, OperationResultTransformer.ORIGINAL_RESULT);
        }
        // Transform the path address
        final PathAddress transformed = transformAddress(address, target);
        // Update the operation using the new path address
        operation.get(OP_ADDR).set(transformed.toModelNode()); // TODO should this happen by default?

        ResourceTransformationContext opCtx = ResourceTransformationContextImpl.wrapForOperation(context, operation);
        OperationTransformer.TransformedOperation res = transformer.transformOperation(opCtx, transformed, operation);
        context.getLogger().flushLogQueue();
        return res;
    }

    @Override
    public OperationTransformer.TransformedOperation transformOperation(final OperationContext operationContext, final ModelNode operation) throws OperationFailedException {

        final PathAddress original = PathAddress.pathAddress(operation.require(OP_ADDR));
        final String operationName = operation.require(OP).asString();

        final OperationTransformer transformer = target.resolveTransformer(original, operationName);
        if (transformer == null) {
            ControllerLogger.ROOT_LOGGER.tracef("operation %s does not need transformation", operation);
            return new OperationTransformer.TransformedOperation(operation, OperationResultTransformer.ORIGINAL_RESULT);
        }
        // Transform the path address
        final PathAddress transformed = transformAddress(original, target);
        // Update the operation using the new path address
        operation.get(OP_ADDR).set(transformed.toModelNode()); // TODO should this happen by default?

        final TransformationContext context = ResourceTransformationContextImpl.create(operationContext, target, transformed, original);
        final OperationTransformer.TransformedOperation op = transformer.transformOperation(context, transformed, operation);
        context.getLogger().flushLogQueue();
        return op;
    }

    @Override
    public Resource transformRootResource(OperationContext operationContext, Resource resource) throws OperationFailedException {
        return transformResource(operationContext, PathAddress.EMPTY_ADDRESS, resource);
    }

    public Resource transformResource(final OperationContext operationContext, PathAddress original, Resource resource) throws OperationFailedException {
        final ResourceTransformer transformer = target.resolveTransformer(original);
        if(transformer == null) {
            ControllerLogger.ROOT_LOGGER.tracef("resource %s does not need transformation", resource);
            return resource;
        }
        // Transform the path address
        final PathAddress transformed = transformAddress(original, target);
        final ResourceTransformationContext context = ResourceTransformationContextImpl.create(operationContext, target, transformed, original);
        transformer.transformResource(context, transformed, resource);
        context.getLogger().flushLogQueue();
        return context.getTransformedRoot();
    }

    @Override
    public Resource transformResource(final ResourceTransformationContext context, Resource resource) throws OperationFailedException {

        final ResourceTransformer transformer = target.resolveTransformer(PathAddress.EMPTY_ADDRESS);
        if (transformer == null) {
            ControllerLogger.ROOT_LOGGER.tracef("resource %s does not need transformation", resource);
            return resource;
        }
        transformer.transformResource(context, PathAddress.EMPTY_ADDRESS, resource);
        context.getLogger().flushLogQueue();
        return context.getTransformedRoot();
    }

    /**
     * Transform a path address.
     *
     * @param original the path address to be transformed
     * @param target the transformation target
     * @return the transformed path address
     */
    protected static PathAddress transformAddress(final PathAddress original, final TransformationTarget target) {
        final List<PathAddressTransformer> transformers = target.getPathTransformation(original);
        final Iterator<PathAddressTransformer> transformations = transformers.iterator();
        final PathAddressTransformer.BuilderImpl builder = new PathAddressTransformer.BuilderImpl(transformations, original);
        return builder.start();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10770.java