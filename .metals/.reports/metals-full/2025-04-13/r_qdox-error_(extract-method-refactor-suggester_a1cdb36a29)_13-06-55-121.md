error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11207.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11207.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11207.java
text:
```scala
final P@@athAddress address = PathAddress.pathAddress(executionContext.getOperation().get(OP_ADDR));

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

package org.jboss.as.controller;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;

import org.jboss.as.controller.client.ExecutionContext;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.persistence.ConfigurationPersister;
import org.jboss.as.controller.persistence.ConfigurationPersisterProvider;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;

/**
 * A basic transactional model controller.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class BasicTransactionalModelController extends BasicModelController implements TransactionalModelController {

    private static final Logger log = Logger.getLogger("org.jboss.as.controller");

    /**
     * Construct a new instance.
     *
     * @param configurationPersister the configuration persister to use to store changes
     */
    protected BasicTransactionalModelController(final ConfigurationPersister configurationPersister) {
        this(new ModelNode().setEmptyObject(), configurationPersister, null);
    }

    /**
     * Construct a new instance.
     *
     * @param configurationPersister the configuration persister to use to store changes
     * @param rootDescriptionProvider the description provider of the root element
     */
    protected BasicTransactionalModelController(final ConfigurationPersister configurationPersister, final DescriptionProvider rootDescriptionProvider) {
        this(new ModelNode().setEmptyObject(), configurationPersister, rootDescriptionProvider);
    }

    /**
     * Construct a new instance.
     *
     * @param model the model
     * @param configurationPersister the configuration persister to use to store changes
     * @param rootDescriptionProvider the description provider of the root element
     */
    protected BasicTransactionalModelController(final ModelNode model, final ConfigurationPersister configurationPersister, DescriptionProvider rootDescriptionProvider) {
        super(model, configurationPersister, rootDescriptionProvider);
    }

    @Override
    public ModelNode execute(final ExecutionContext executionContext, final ControllerTransactionContext transaction) {
        return super.execute(executionContext, transaction);
    }

    /** {@inheritDoc} */
    @Override
    public OperationResult execute(final ExecutionContext executionContext, final ResultHandler handler) {
        ControllerTransaction transaction = null;
        try {
            final PathAddress address = PathAddress.pathAddress(executionContext.getOperation().require(ModelDescriptionConstants.OP_ADDR));

            final ProxyController proxyExecutor = getRegistry().getProxyController(address);
            if (proxyExecutor != null) {
                ExecutionContext newContext = executionContext.clone();
                newContext.getOperation().get(OP_ADDR).set(address.subAddress(proxyExecutor.getProxyNodeAddress().size()).toModelNode());
                return proxyExecutor.execute(newContext, handler);
            }
            transaction = new ControllerTransaction();
            return execute(executionContext, handler, transaction);

        } catch (final Throwable t) {
            log.errorf(t, "operation (%s) failed - address: (%s)", executionContext.getOperation().get(OP), executionContext.getOperation().get(OP_ADDR));
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            handler.handleFailed(getFailureResult(t));
            return new BasicOperationResult();
        }
        finally {
            if (transaction != null) {
                transaction.commit();
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public OperationResult execute(final ExecutionContext executionContext, final ResultHandler handler, final ControllerTransactionContext transaction) {

        return execute(executionContext, handler, getModelProvider(), getOperationContextFactory(), getConfigurationPersisterProvider(), transaction);
    }

    protected OperationResult execute(ExecutionContext executionContext, ResultHandler handler, ModelProvider modelSource,
            OperationContextFactory contextFactory, ConfigurationPersisterProvider configurationPersisterProvider,
            ControllerTransactionContext transaction) {

        try {
            final PathAddress address = PathAddress.pathAddress(executionContext.getOperation().get(ModelDescriptionConstants.OP_ADDR));

            final ProxyController proxyExecutor = getRegistry().getProxyController(address);
            if (proxyExecutor != null) {
                ExecutionContext newContext = executionContext.clone();
                newContext.getOperation().get(OP_ADDR).set(address.subAddress(proxyExecutor.getProxyNodeAddress().size()).toModelNode());
                return proxyExecutor.execute(newContext, handler);
            }

            if (isMultiStepOperation(executionContext, address)) {
                MultiStepOperationController multistepController = getMultiStepOperationController(executionContext, handler, modelSource, configurationPersisterProvider, transaction);
                return multistepController.execute(handler);
            }

            final String operationName = executionContext.getOperation().require(ModelDescriptionConstants.OP).asString();
            final OperationHandler operationHandler = getRegistry().getOperationHandler(address, operationName);
            if (operationHandler == null) {
                throw new IllegalStateException("No handler for " + operationName + " at address " + address);
            }

            final OperationContext context = contextFactory.getOperationContext(modelSource, address, operationHandler, executionContext);
            try {
                final OperationResult result = operationHandler.execute(context, executionContext.getOperation(), handler);
                ControllerResource txResource = getControllerResource(context, executionContext.getOperation(), operationHandler, handler, address, modelSource, configurationPersisterProvider);
                if (txResource != null) {
                    transaction.registerResource(txResource);
                }
                return result;
            } catch (OperationFailedException e) {
                transaction.setRollbackOnly();
                handler.handleFailed(e.getFailureDescription());
                return new BasicOperationResult();
            }
        } catch (final Throwable t) {
            transaction.setRollbackOnly();
            log.errorf(t, "operation (%s) failed - address: (%s)", executionContext.getOperation().get(OP), executionContext.getOperation().get(OP_ADDR));
            handler.handleFailed(getFailureResult(t));
            return new BasicOperationResult();
        }
    }

    protected MultiStepOperationController getMultiStepOperationController(ExecutionContext executionContext, ResultHandler handler,
            ModelProvider modelSource, final ConfigurationPersisterProvider persisterProvider, ControllerTransactionContext transaction) throws OperationFailedException {
        return new TransactionalMultiStepOperationController(executionContext, handler, modelSource, persisterProvider, transaction);
    }

    protected ControllerResource getControllerResource(final OperationContext context, final ModelNode operation, final OperationHandler operationHandler,
            final ResultHandler resultHandler, final PathAddress address, final ModelProvider modelProvider, final ConfigurationPersisterProvider persisterProvider) {
        ControllerResource resource = null;

        if (operationHandler instanceof ModelUpdateOperationHandler) {
            resource = new UpdateModelControllerResource(operationHandler, address, context.getSubModel(), modelProvider, persisterProvider);
        }

        return resource;
    }

    protected class UpdateModelControllerResource implements ControllerResource {
        private final PathAddress address;
        private final ModelNode subModel;
        private final boolean isRemove;
        private final ModelProvider modelProvider;
        private final ConfigurationPersisterProvider persisterProvider;

        public UpdateModelControllerResource(final OperationHandler handler, final PathAddress address, final ModelNode subModel, final ModelProvider modelProvider, final ConfigurationPersisterProvider persisterProvider) {
            if (handler instanceof ModelUpdateOperationHandler) {
                this.address = address;
                this.subModel = subModel;
                this.isRemove = (handler instanceof ModelRemoveOperationHandler);
                this.modelProvider = modelProvider;
                this.persisterProvider = persisterProvider;
            }
            else {
                this.address = null;
                this.subModel = null;
                this.isRemove = false;
                this.modelProvider = null;
                this.persisterProvider = null;
            }
        }

        @Override
        public void commit() {
            if (address != null) {
                final ModelNode model = modelProvider.getModel();
                synchronized (model) {
                    if (isRemove) {
                        address.remove(model);
                    } else {
                        address.navigate(model, true).set(subModel);
                    }
                    persistConfiguration(model, persisterProvider);
                }
            }
        }

        @Override
        public void rollback() {
            // no-op
        }

    }

    protected class TransactionalMultiStepOperationController extends MultiStepOperationController {

        protected final ControllerTransactionContext transaction;

        protected TransactionalMultiStepOperationController(final ExecutionContext executionContext, final ResultHandler resultHandler,
                final ModelProvider modelSource, final ConfigurationPersisterProvider persisterProvider,
                final ControllerTransactionContext transaction) throws OperationFailedException {
            super(executionContext, resultHandler, modelSource, persisterProvider);
            this.transaction = transaction;
        }

        /** Instead of updating and persisting, we register a resource that does it at commit */
        @Override
        protected void updateModelAndPersist() {
            ControllerResource resource = new ControllerResource() {

                @Override
                public void commit() {
                    TransactionalMultiStepOperationController.this.commit();
                }

                @Override
                public void rollback() {
                    // no-op
                }

            };
            transaction.registerResource(resource);
        }

        private void commit() {
            super.updateModelAndPersist();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11207.java