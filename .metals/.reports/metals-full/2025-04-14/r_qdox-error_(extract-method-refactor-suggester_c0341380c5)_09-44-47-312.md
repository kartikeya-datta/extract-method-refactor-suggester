error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1280.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1280.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1280.java
text:
```scala
r@@ecordTransformedOperation(transformer.transformOperation(ctx, address, transformedOperation.getTransformedOperation()));

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
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

package org.jboss.as.controller.transform.description;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;

import java.util.Collections;
import java.util.Iterator;

import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.transform.OperationTransformer;
import org.jboss.as.controller.transform.TransformationContext;
import org.jboss.dmr.ModelNode;

/**
 * @author Emanuel Muckenhuber
 */
class OperationTransformationOverrideBuilderImpl extends AttributeTransformationDescriptionBuilderImpl<OperationTransformationOverrideBuilder> implements OperationTransformationOverrideBuilder {

    private boolean inherit = false;
    private DiscardPolicy discardPolicy = DiscardPolicy.NEVER;
    private OperationTransformer transformer = OperationTransformer.DEFAULT;
    private String newName;

    protected OperationTransformationOverrideBuilderImpl(ResourceTransformationDescriptionBuilder builder) {
        super(builder, new AttributeTransformationDescriptionBuilderImpl.AttributeTransformationDescriptionBuilderRegistry());
    }

    @Override
    public OperationTransformationOverrideBuilder inheritResourceAttributeDefinitions() {
        this.inherit = true;
        return this;
    }

    public OperationTransformationOverrideBuilder setCustomOperationTransformer(OperationTransformer transformer) {
        this.transformer = transformer;
        return this;
    }

    @Override
    public OperationTransformationOverrideBuilder rename(String newName) {
        this.newName = newName;
        return this;
    }

    protected OperationTransformer createTransformer(final AttributeTransformationDescriptionBuilderRegistry resourceRegistry) {
        final AttributeTransformationDescriptionBuilderRegistry registry = resultingRegistry(resourceRegistry);
        final AttributeTransformationRule first = new AttributeTransformationRule(registry.buildAttributes());
        return new OperationTransformer() {
            @Override
            public TransformedOperation transformOperation(final TransformationContext ctx, final PathAddress address, final ModelNode operation) throws OperationFailedException {
                if(discardPolicy.discard(operation, address, ctx)) {
                    return OperationTransformer.DISCARD.transformOperation(ctx, address, operation);
                }
                final Iterator<TransformationRule> iterator = Collections.<TransformationRule>emptyList().iterator();
                final ModelNode originalModel = TransformationRule.cloneAndProtect(operation);
                final TransformationRule.ChainedOperationContext context = new TransformationRule.ChainedOperationContext(ctx) {

                    @Override
                    void invokeNext(OperationTransformer.TransformedOperation transformedOperation) throws OperationFailedException {
                        recordTransformedOperation(transformedOperation);
                        if(iterator.hasNext()) {
                            final TransformationRule next = iterator.next();
                            // TODO hmm, do we need to change the address?
                            next.transformOperation(transformedOperation.getTransformedOperation(), address, this);
                        } else {
                            if (newName != null) {
                                transformedOperation.getTransformedOperation().get(OP).set(newName);
                            }
                            final TransformationContext ctx = getContext();
                            transformer.transformOperation(ctx, address, transformedOperation.getTransformedOperation());
                        }
                    }
                };
                operation.get(ModelDescriptionConstants.OP_ADDR).set(address.toModelNode());
                // Kick off the chain
                first.transformOperation(operation, address, context);
                // Create the composite operation result
                return context.createOp();
            }
        };
    }

    protected AttributeTransformationDescriptionBuilderRegistry resultingRegistry(final AttributeTransformationDescriptionBuilderRegistry resourceRegistry) {
        final AttributeTransformationDescriptionBuilderRegistry local = getLocalRegistry();
        if(inherit) {
            return AttributeTransformationDescriptionBuilderImpl.mergeRegistries(resourceRegistry, local);
        }
        return local;
    }

    @Override
    protected OperationTransformationOverrideBuilder thisBuilder() {
        return this;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1280.java