error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/527.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/527.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,10]

error in qdox parser
file content:
```java
offset: 10
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/527.java
text:
```scala
abstract v@@oid transformResource(Resource resource, PathAddress address, ResourceContext context) throws OperationFailedException;

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

import java.util.ArrayList;
import java.util.List;

import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ProcessType;
import org.jboss.as.controller.RunningMode;
import org.jboss.as.controller.registry.ImmutableManagementResourceRegistration;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.controller.transform.OperationResultTransformer;
import org.jboss.as.controller.transform.OperationTransformer;
import org.jboss.as.controller.transform.ResourceTransformationContext;
import org.jboss.as.controller.transform.TransformationContext;
import org.jboss.as.controller.transform.TransformationTarget;
import org.jboss.dmr.ModelNode;

/**
 *
 * @author Emanuel Muckenhuber
 */
abstract class TransformationRule {

    abstract void transformOperation(ModelNode operation, PathAddress address, OperationContext context) throws OperationFailedException;
    abstract void tranformResource(Resource resource, PathAddress address, ResourceContext context) throws OperationFailedException;

    abstract static class AbstractTransformationContext {

        private final TransformationContext context;
        protected AbstractTransformationContext(final TransformationContext context) {
            this.context = new TransformationContextWrapper(context);
        }

        protected TransformationContext getContext() {
            return context;
        }

    }

    abstract static class OperationContext extends AbstractTransformationContext {

        private final List<OperationTransformer.TransformedOperation> transformed = new ArrayList<OperationTransformer.TransformedOperation>();
        private ModelNode lastOperation;
        protected OperationContext(TransformationContext context) {
            super(context);
        }

        protected void recordTransformedOperation(OperationTransformer.TransformedOperation operation) {
            lastOperation = operation.getTransformedOperation();
            transformed.add(operation);
        }

        void invokeNext(ModelNode operation) throws OperationFailedException {
            invokeNext(new OperationTransformer.TransformedOperation(operation, OperationResultTransformer.ORIGINAL_RESULT));
        }

        abstract void invokeNext(OperationTransformer.TransformedOperation transformedOperation) throws OperationFailedException;

        protected OperationTransformer.TransformedOperation createOp() {
            if(transformed.size() == 1) {
                return transformed.get(0);
            }
            return new ChainedTransformedOperation(lastOperation, transformed);
        }

    }

    abstract static class ResourceContext extends AbstractTransformationContext {

        protected ResourceContext(ResourceTransformationContext context) {
            super(context);
        }

        protected TransformationContext getContext() {
            return super.getContext();
        }

        abstract void invokeNext(Resource resource) throws OperationFailedException;

    }

    private static class TransformationContextWrapper implements TransformationContext {

        private final TransformationContext delegate;
        private TransformationContextWrapper(TransformationContext delegate) {
            this.delegate = delegate;
        }

        @Override
        public TransformationTarget getTarget() {
            return delegate.getTarget();
        }

        @Override
        public ProcessType getProcessType() {
            return delegate.getProcessType();
        }

        @Override
        public RunningMode getRunningMode() {
            return delegate.getRunningMode();
        }

        @Override
        public ImmutableManagementResourceRegistration getResourceRegistration(PathAddress address) {
            return delegate.getResourceRegistration(address);
        }

        @Override
        public ImmutableManagementResourceRegistration getResourceRegistrationFromRoot(PathAddress address) {
            return delegate.getResourceRegistrationFromRoot(address);
        }

        @Override
        public Resource readResource(PathAddress address) {
            return delegate.readResource(address);
        }

        @Override
        public Resource readResourceFromRoot(PathAddress address) {
            return delegate.readResourceFromRoot(address);
        }

        @Override
        @Deprecated
        public ModelNode resolveExpressions(ModelNode node) throws OperationFailedException {
            return delegate.resolveExpressions(node);
        }
    }

    private static class ChainedTransformedOperation extends OperationTransformer.TransformedOperation {

        private final List<OperationTransformer.TransformedOperation> delegates;
        private volatile String failure;

        public ChainedTransformedOperation(final ModelNode transformedOperation, final List<OperationTransformer.TransformedOperation> delegates) {
            super(transformedOperation, null);
            this.delegates = delegates;
        }

        @Override
        public ModelNode getTransformedOperation() {
            return super.getTransformedOperation();
        }

        @Override
        public OperationResultTransformer getResultTransformer() {
            return this;
        }

        @Override
        public boolean rejectOperation(ModelNode preparedResult) {
            for (OperationTransformer.TransformedOperation delegate : delegates) {
                if (delegate.rejectOperation(preparedResult)) {
                    failure = delegate.getFailureDescription();
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getFailureDescription() {
            return failure;
        }

        @Override
        public ModelNode transformResult(ModelNode result) {
            ModelNode currentResult = result;
            final int size = delegates.size();
            for (int i = size - 1 ; i >= 0 ; --i) {
                currentResult = delegates.get(i).transformResult(currentResult);
            }
            return currentResult;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/527.java