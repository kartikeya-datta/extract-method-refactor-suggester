error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/268.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/268.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/268.java
text:
```scala
public v@@oid execute(OperationContext context, ModelNode operation) throws OperationFailedException {

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

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILURE_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESPONSE_HEADERS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESULT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SERVER_GROUPS;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.jboss.as.controller.client.MessageSeverity;
import org.jboss.as.controller.client.OperationAttachments;
import org.jboss.as.controller.client.OperationMessageHandler;
import org.jboss.as.controller.transform.OperationResultTransformer;
import org.jboss.as.controller.transform.OperationTransformer;
import org.jboss.dmr.ModelNode;

/**
 * Step handler that uses a proxied {@link ModelController} to execute the step.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class ProxyStepHandler implements OperationStepHandler {

    private final ProxyController proxyController;

    public ProxyStepHandler(final ProxyController proxyController) {
        this.proxyController = proxyController;
    }

    @Override
    public void execute(OperationContext context, ModelNode operation) {
        OperationMessageHandler messageHandler = new DelegatingMessageHandler(context);

        final AtomicReference<ModelController.OperationTransaction> txRef = new AtomicReference<ModelController.OperationTransaction>();
        final AtomicReference<ModelNode> preparedResultRef = new AtomicReference<ModelNode>();
        final AtomicReference<ModelNode> finalResultRef = new AtomicReference<ModelNode>();
        final ProxyController.ProxyOperationControl proxyControl = new ProxyController.ProxyOperationControl() {

            @Override
            public void operationPrepared(ModelController.OperationTransaction transaction, ModelNode result) {
                txRef.set(transaction);
                preparedResultRef.set(result);
            }

            @Override
            public void operationFailed(ModelNode response) {
                finalResultRef.set(response);
            }

            @Override
            public void operationCompleted(ModelNode response) {
                finalResultRef.set(response);
            }
        };
        // Transform the operation if needed
        if(proxyController instanceof TransformingProxyController) {
            final TransformingProxyController transformingProxyController = (TransformingProxyController) proxyController;
            final OperationTransformer.TransformedOperation result = transformingProxyController.transformOperation(context, operation);
            final ModelNode transformedOperation = result.getTransformedOperation();
            final OperationResultTransformer resultTransformer = result.getResultTransformer();
            if(transformedOperation != null) {
                final ProxyController.ProxyOperationControl transformingProxyControl = new ProxyController.ProxyOperationControl() {
                    @Override
                    public void operationFailed(ModelNode response) {
                        final ModelNode result = resultTransformer.transformResult(response);
                        proxyControl.operationFailed(result);
                    }

                    @Override
                    public void operationCompleted(ModelNode response) {
                        final ModelNode result = resultTransformer.transformResult(response);
                        proxyControl.operationCompleted(result);
                    }

                    @Override
                    public void operationPrepared(ModelController.OperationTransaction transaction, ModelNode response) {
                        // final ModelNode result = resultTransformer.transformResult(response);
                        proxyControl.operationPrepared(transaction, response);
                    }
                };
                proxyController.execute(transformedOperation, messageHandler, transformingProxyControl, new DelegatingOperationAttachments(context));
            } else {
                // discard the operation
                final ModelNode transformedResult = resultTransformer.transformResult(new ModelNode());
                if(transformedResult != null) {
                    context.getResult().set(transformedResult);
                }
                context.completeStep(OperationContext.RollbackHandler.NOOP_ROLLBACK_HANDLER);
                return;
            }
        } else {
            proxyController.execute(operation, messageHandler, proxyControl, new DelegatingOperationAttachments(context));
        }
        ModelNode finalResult = finalResultRef.get();
        if (finalResult != null) {
            // operation failed before it could commit
            context.getResult().set(finalResult.get(RESULT));
            context.getFailureDescription().set(finalResult.get(FAILURE_DESCRIPTION));
            if (finalResult.hasDefined(RESPONSE_HEADERS)) {
                context.getResponseHeaders().set(finalResult.get(RESPONSE_HEADERS));
            }
            context.completeStep();
        } else {

            completeRemoteTransaction(context, operation, txRef, preparedResultRef, finalResultRef);

        }
    }

    private void completeRemoteTransaction(OperationContext context, ModelNode operation, AtomicReference<ModelController.OperationTransaction> txRef, AtomicReference<ModelNode> preparedResultRef, AtomicReference<ModelNode> finalResultRef) {

        boolean txCompleted = false;
        try {

            ModelNode preparedResponse = preparedResultRef.get();
            ModelNode preparedResult =  preparedResponse.get(RESULT);
            if (preparedResponse.hasDefined(FAILURE_DESCRIPTION)) {
                context.getFailureDescription().set(preparedResponse.get(FAILURE_DESCRIPTION));
                if (preparedResult.isDefined()) {
                    context.getResult().set(preparedResult);
                }
            }
            else {
                context.getResult().set(preparedResult);
            }

            OperationContext.ResultAction resultAction = context.completeStep();
            ModelController.OperationTransaction tx = txRef.get();
            try {
                if (resultAction == OperationContext.ResultAction.KEEP) {
                    tx.commit();
                } else {
                    tx.rollback();
                }
            } finally {
                txCompleted = true;
            }

            // Get the final result from the proxy and use it to update our response.
            // Per the ProxyOperationControl contract, this will have been provided via operationCompleted
            // by the time the call to OperationTransaction.commit/rollback returns

            ModelNode finalResponse = finalResultRef.get();
            if (finalResponse != null) {
                ModelNode finalResult =  finalResponse.get(RESULT);
                if (finalResponse.hasDefined(FAILURE_DESCRIPTION)) {
                    context.getFailureDescription().set(finalResponse.get(FAILURE_DESCRIPTION));
                    if (finalResult.isDefined()) {
                        context.getResult().set(finalResult);
                    }
                } else {
                    context.getResult().set(finalResult);
                }
                if (context.getProcessType() == ProcessType.HOST_CONTROLLER && finalResponse.has(SERVER_GROUPS)) {
                    context.getServerResults().set(finalResponse.get(SERVER_GROUPS));
                }
                if (finalResponse.hasDefined(RESPONSE_HEADERS)) {
                    context.getResponseHeaders().set(finalResponse.get(RESPONSE_HEADERS));
                }
            } else {
                // This is an error condition
                ControllerLogger.SERVER_MANAGEMENT_LOGGER.noFinalProxyOutcomeReceived(operation.get(OP),
                        operation.get(OP_ADDR), proxyController.getProxyNodeAddress().toModelNode());
            }
        } finally {
            // Ensure the remote side gets a transaction outcome if we can't commit/rollback above
            if (!txCompleted && txRef.get() != null) {
                txRef.get().rollback();
            }
        }
    }


    private static class DelegatingMessageHandler implements OperationMessageHandler {

        private final OperationContext context;

        DelegatingMessageHandler(final OperationContext context) {
            this.context = context;
        }

        @Override
        public void handleReport(MessageSeverity severity, String message) {
            context.report(severity, message);
        }
    }

    private static class DelegatingOperationAttachments implements OperationAttachments {

        private final OperationContext context;
        private DelegatingOperationAttachments(final OperationContext context) {
            this.context = context;
        }

        @Override
        public boolean isAutoCloseStreams() {
            return false;
        }

        @Override
        public List<InputStream> getInputStreams() {
            int count = context.getAttachmentStreamCount();
            List<InputStream> result = new ArrayList<InputStream>(count);
            for (int i = 0; i < count; i++) {
                result.add(context.getAttachmentStream(i));
            }
            return result;
        }

        @Override
        public void close() throws IOException {
            //
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/268.java