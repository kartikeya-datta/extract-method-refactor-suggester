error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4635.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4635.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4635.java
text:
```scala
c@@opySubject.getPrincipals().add(new AccessMechanismPrincipal(AccessMechanism.NATIVE));

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
package org.jboss.as.controller.remote;

import static org.jboss.as.controller.ControllerLogger.ROOT_LOGGER;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CALLER_TYPE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.COMPOSITE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DOMAIN_UUID;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.EXECUTE_FOR_COORDINATOR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILURE_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.HOST;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OPERATION_HEADERS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OUTCOME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESULT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUCCESS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.USER;

import java.io.DataInput;
import java.io.IOException;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Collection;

import javax.security.auth.Subject;

import org.jboss.as.controller.ModelController;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.client.impl.ModelControllerProtocol;
import org.jboss.as.controller.security.AccessMechanismPrincipal;
import org.jboss.as.core.security.AccessMechanism;
import org.jboss.as.protocol.StreamUtils;
import org.jboss.as.protocol.mgmt.ActiveOperation;
import org.jboss.as.protocol.mgmt.FlushableDataOutput;
import org.jboss.as.protocol.mgmt.ManagementChannelAssociation;
import org.jboss.as.protocol.mgmt.ManagementProtocol;
import org.jboss.as.protocol.mgmt.ManagementRequestContext;
import org.jboss.as.protocol.mgmt.ManagementRequestHandler;
import org.jboss.as.protocol.mgmt.ManagementRequestHandlerFactory;
import org.jboss.as.protocol.mgmt.ManagementRequestHeader;
import org.jboss.as.protocol.mgmt.ManagementResponseHeader;
import org.jboss.as.protocol.mgmt.ProtocolUtils;
import org.jboss.dmr.ModelNode;
import org.jboss.remoting3.security.InetAddressPrincipal;
import org.wildfly.security.manager.WildFlySecurityManager;

/**
 * Operation handlers for the remote implementation of {@link org.jboss.as.controller.client.ModelControllerClient}
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @author Emanuel Muckenhuber
 */
public class ModelControllerClientOperationHandler implements ManagementRequestHandlerFactory {

    private final ModelController controller;

    private final ManagementChannelAssociation channelAssociation;
    private final Subject subject;

    public ModelControllerClientOperationHandler(final ModelController controller,
                                                 final ManagementChannelAssociation channelAssociation) {
        this(controller, channelAssociation, null);
    }

    public ModelControllerClientOperationHandler(final ModelController controller,
                                                 final ManagementChannelAssociation channelAssociation, final Subject subject) {
        this.controller = controller;
        this.channelAssociation = channelAssociation;
        this.subject = subject;
    }

    @Override
    public ManagementRequestHandler<?, ?> resolveHandler(final RequestHandlerChain handlers, final ManagementRequestHeader header) {
        switch (header.getOperationId()) {
            case ModelControllerProtocol.EXECUTE_ASYNC_CLIENT_REQUEST:
            case ModelControllerProtocol.EXECUTE_CLIENT_REQUEST:
                // initialize the operation ctx before executing the request handler
                handlers.registerActiveOperation(header.getBatchId(), null);
                return new ExecuteRequestHandler();
            case ModelControllerProtocol.CANCEL_ASYNC_REQUEST:
                return new CancelAsyncRequestHandler();
        }
        return handlers.resolveNext();
    }

    class ExecuteRequestHandler implements ManagementRequestHandler<ModelNode, Void> {

        @Override
        public void handleRequest(final DataInput input, final ActiveOperation.ResultHandler<ModelNode> resultHandler, final ManagementRequestContext<Void> context) throws IOException {
            final ModelNode operation = new ModelNode();
            ProtocolUtils.expectHeader(input, ModelControllerProtocol.PARAM_OPERATION);
            operation.readExternal(input);

            ProtocolUtils.expectHeader(input, ModelControllerProtocol.PARAM_INPUTSTREAMS_LENGTH);
            final int attachmentsLength = input.readInt();
            context.executeAsync(new ManagementRequestContext.AsyncTask<Void>() {
                @Override
                public void execute(final ManagementRequestContext<Void> context) throws Exception {
                    final ManagementResponseHeader response = ManagementResponseHeader.create(context.getRequestHeader());
                    Subject useSubject = subject;
                    if (subject != null) {
                        //TODO find a better place for this https://issues.jboss.org/browse/WFLY-1852
                        PrivilegedAction<Subject> copyAction = new PrivilegedAction<Subject>() {
                            @Override
                            public Subject run() {
                                final Subject subject = ModelControllerClientOperationHandler.this.subject;
                                final Subject copySubject = new Subject();
                                copySubject.getPrincipals().addAll(subject.getPrincipals());
                                copySubject.getPrivateCredentials().addAll(subject.getPrivateCredentials());
                                copySubject.getPublicCredentials().addAll(subject.getPublicCredentials());
                                //Add the remote address and the access mechanism
                                Collection<Principal> principals = context.getChannel().getConnection().getPrincipals();
                                for (Principal principal : principals) {
                                    if (principal instanceof InetAddressPrincipal) {
                                        //TODO decide if we should use the remoting principal or not
                                        copySubject.getPrincipals().add(new org.jboss.as.controller.security.InetAddressPrincipal(((InetAddressPrincipal)principal).getInetAddress()));
                                        break;
                                    }
                                }
                                copySubject.getPrincipals().add(new AccessMechanismPrincipal(AccessMechanism.JMX));
                                copySubject.setReadOnly();
                                return copySubject;                            }
                        };

                        useSubject = WildFlySecurityManager.isChecking() ? AccessController.doPrivileged(copyAction) : copyAction.run();
                    }

                    try {
                        Subject.doAs(useSubject, new PrivilegedExceptionAction<Void>() {
                            @Override
                            public Void run() throws Exception {
                                final CompletedCallback callback = new CompletedCallback(response, context, resultHandler);
                                doExecute(operation, attachmentsLength, context, callback);
                                return null;
                            }
                        });
                    } catch (PrivilegedActionException e) {
                        throw e.getException();
                    }
                }
            });
        }

        private void doExecute(final ModelNode operation, final int attachmentsLength, final ManagementRequestContext<Void> context, final CompletedCallback callback) {

            // Header manipulation
            final ModelNode headers = operation.get(OPERATION_HEADERS);
            //Add a header to show that this operation comes from a user. If this is a host controller and the operation needs propagating to the
            //servers it will be removed by the domain ops responsible for propagation to the servers.
            headers.get(CALLER_TYPE).set(USER);
            // Don't allow a domain-uuid operation header from a user call
            if (headers.hasDefined(DOMAIN_UUID)) {
                headers.remove(DOMAIN_UUID);
            }
            // Don't allow a execute-for-coordinator operation header from a user call
            if (headers.hasDefined(EXECUTE_FOR_COORDINATOR)) {
                headers.remove(EXECUTE_FOR_COORDINATOR);
            }

            final ManagementRequestHeader header = ManagementRequestHeader.class.cast(context.getRequestHeader());
            final int batchId = header.getBatchId();
            final ModelNode result = new ModelNode();

            // Send the prepared response for :reload operations
            final boolean sendPreparedOperation = sendPreparedResponse(operation);
            final ModelController.OperationTransactionControl transactionControl = sendPreparedOperation ? new ModelController.OperationTransactionControl() {
                @Override
                public void operationPrepared(ModelController.OperationTransaction transaction, ModelNode result) {
                    transaction.commit();
                    // Fix prepared result
                    result.get(OUTCOME).set(SUCCESS);
                    result.get(RESULT);
                    callback.sendResponse(result);
                }
            } : ModelController.OperationTransactionControl.COMMIT;

            final OperationMessageHandlerProxy messageHandlerProxy = new OperationMessageHandlerProxy(channelAssociation, batchId);
            final OperationAttachmentsProxy attachmentsProxy = OperationAttachmentsProxy.create(channelAssociation, batchId, attachmentsLength);
            try {
                ROOT_LOGGER.tracef("Executing client request %d(%d)", batchId, header.getRequestId());
                result.set(controller.execute(
                        operation,
                        messageHandlerProxy,
                        transactionControl,
                        attachmentsProxy));
            } catch (Exception e) {
                final ModelNode failure = new ModelNode();
                failure.get(OUTCOME).set(FAILED);
                failure.get(FAILURE_DESCRIPTION).set(e.getClass().getName() + ":" + e.getMessage());
                result.set(failure);
                attachmentsProxy.shutdown(e);
            } finally {
                ROOT_LOGGER.tracef("Executed client request %d", batchId);
            }
            // Send the result
            callback.sendResponse(result);
        }

    }

    /**
     * Determine whether the prepared response should be sent, before the operation completed. This is needed in order
     * that operations like :reload() can be executed without causing communication failures.
     *
     * @param operation the operation to be executed
     * @return {@code true} if the prepared result should be sent, {@code false} otherwise
     */
    private boolean sendPreparedResponse(final ModelNode operation) {
        final PathAddress address = PathAddress.pathAddress(operation.get(OP_ADDR));
        final String op = operation.get(OP).asString();
        final int size = address.size();
        if (size == 0) {
            if (op.equals("reload")) {
                return true;
            } else if (op.equals(COMPOSITE)) {
                // TODO
                return false;
            } else {
                return false;
            }
        } else if (size == 1) {
            if (address.getLastElement().getKey().equals(HOST)) {
                return op.equals("reload");
            }
        }
        return false;
    }

    private static class CompletedCallback {

        private volatile boolean completed;
        private final ManagementResponseHeader response;
        private final ManagementRequestContext<Void> responseContext;
        private final ActiveOperation.ResultHandler<ModelNode> resultHandler;

        private CompletedCallback(final ManagementResponseHeader response, final ManagementRequestContext<Void> responseContext,
                                  final ActiveOperation.ResultHandler<ModelNode> resultHandler) {
            this.response = response;
            this.responseContext = responseContext;
            this.resultHandler = resultHandler;
        }

        synchronized void sendResponse(final ModelNode result) {
            // only send a single response
            if (completed) {
                return;
            }
            completed = true;
            try {
                final FlushableDataOutput output = responseContext.writeMessage(response);
                try {
                    output.write(ModelControllerProtocol.PARAM_RESPONSE);
                    result.writeExternal(output);
                    output.writeByte(ManagementProtocol.RESPONSE_END);
                    output.close();
                } finally {
                    StreamUtils.safeClose(output);
                }
                resultHandler.done(result);
            } catch (IOException e) {
                resultHandler.failed(e);
            }
        }

    }

    private static class CancelAsyncRequestHandler implements ManagementRequestHandler<ModelNode, Void> {

        @Override
        public void handleRequest(final DataInput input, final ActiveOperation.ResultHandler<ModelNode> resultHandler, final ManagementRequestContext<Void> context) throws IOException {
            context.executeAsync(new ManagementRequestContext.AsyncTask<Void>() {
                @Override
                public void execute(ManagementRequestContext<Void> context) throws Exception {
                    final ManagementResponseHeader response = ManagementResponseHeader.create(context.getRequestHeader());
                    final FlushableDataOutput output = context.writeMessage(response);
                    try {
                        output.writeByte(ManagementProtocol.RESPONSE_END);
                        output.close();
                    } finally {
                        StreamUtils.safeClose(output);
                    }
                }
            });
            resultHandler.cancel();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4635.java