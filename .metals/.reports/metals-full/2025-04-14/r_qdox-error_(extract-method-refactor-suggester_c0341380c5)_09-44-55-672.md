error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7829.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7829.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[25,1]

error in qdox parser
file content:
```java
offset: 1138
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7829.java
text:
```scala
class TransactionalProtocolClientImpl implements ManagementRequestHandlerFactory, TransactionalProtocolClient {

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

p@@ackage org.jboss.as.controller.remote;

import org.jboss.as.controller.ModelController;
import org.jboss.as.controller.client.MessageSeverity;
import org.jboss.as.controller.client.OperationAttachments;
import org.jboss.as.controller.client.OperationMessageHandler;
import org.jboss.as.controller.client.impl.AbstractDelegatingAsyncFuture;
import org.jboss.as.controller.client.impl.ModelControllerProtocol;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CANCELLED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILURE_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OUTCOME;
import org.jboss.as.protocol.StreamUtils;
import org.jboss.as.protocol.mgmt.AbstractManagementRequest;
import org.jboss.as.protocol.mgmt.ActiveOperation;
import org.jboss.as.protocol.mgmt.FlushableDataOutput;
import org.jboss.as.protocol.mgmt.ManagementChannelAssociation;
import org.jboss.as.protocol.mgmt.ManagementProtocol;
import org.jboss.as.protocol.mgmt.ManagementRequestContext;
import org.jboss.as.protocol.mgmt.ManagementRequestHandler;
import org.jboss.as.protocol.mgmt.ManagementRequestHandlerFactory;
import org.jboss.as.protocol.mgmt.ManagementRequestHeader;
import org.jboss.as.protocol.mgmt.ManagementResponseHeader;
import static org.jboss.as.protocol.mgmt.ProtocolUtils.expectHeader;
import org.jboss.dmr.ModelNode;
import org.jboss.threads.AsyncFuture;
import org.jboss.threads.AsyncFutureTask;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Base implementation for the transactional protocol.
 *
 * @author Emanuel Muckenhuber
 */
public class TransactionalProtocolClientImpl implements ManagementRequestHandlerFactory, TransactionalProtocolClient {

    private final ManagementChannelAssociation channelAssociation;
    public TransactionalProtocolClientImpl(final ManagementChannelAssociation channelAssociation) {
        assert channelAssociation != null;
        this.channelAssociation = channelAssociation;
    }

    /** {@inheritDoc} */
    @Override
    public ManagementRequestHandler<?, ?> resolveHandler(RequestHandlerChain handlers, ManagementRequestHeader header) {
        final byte operationType = header.getOperationId();
        if (operationType == ModelControllerProtocol.HANDLE_REPORT_REQUEST) {
            return new HandleReportRequestHandler();
        } else if (operationType == ModelControllerProtocol.GET_INPUTSTREAM_REQUEST) {
            return ReadAttachmentInputStreamRequestHandler.INSTANCE;
        }
        return handlers.resolveNext();
    }

    @Override
    public AsyncFuture<ModelNode> execute(TransactionalOperationListener<Operation> listener, ModelNode operation, OperationMessageHandler messageHandler, OperationAttachments attachments) throws IOException {
        final Operation wrapper = new TransactionalOperationImpl(operation, messageHandler, attachments);
        return execute(listener, wrapper);
    }

    @Override
    public <T extends Operation> AsyncFuture<ModelNode> execute(TransactionalOperationListener<T> listener, T operation) throws IOException {
        final ExecuteRequestContext context = new ExecuteRequestContext(new OperationWrapper<T>(listener, operation));
        final ActiveOperation<ModelNode, ExecuteRequestContext> op = channelAssociation.initializeOperation(context, context);
        final AsyncFuture<ModelNode> result = new AbstractDelegatingAsyncFuture<ModelNode>(op.getResult()) {
            @Override
            public void asyncCancel(boolean interruptionDesired) {
                try {
                    // Execute
                    channelAssociation.executeRequest(op, new CompleteTxRequest(ModelControllerProtocol.PARAM_ROLLBACK));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
        context.initialize(result);
        channelAssociation.executeRequest(op, new ExecuteRequest());
        return result;
    }

    /**
     * Request for the the remote {@link TransactionalProtocolOperationHandler.ExecuteRequestHandler}.
     *
     * The required response is either a:
     *  - {@link org.jboss.as.controller.client.impl.ModelControllerProtocol#PARAM_OPERATION_FAILED}, which will complete the operation right away
     *  - or {@link org.jboss.as.controller.client.impl.ModelControllerProtocol#PARAM_OPERATION_PREPARED}
     */
    private class ExecuteRequest extends AbstractManagementRequest<ModelNode, ExecuteRequestContext> {

        @Override
        public byte getOperationType() {
            return ModelControllerProtocol.EXECUTE_TX_REQUEST;
        }

        @Override
        protected void sendRequest(final ActiveOperation.ResultHandler<ModelNode> resultHandler,
                                   final ManagementRequestContext<ExecuteRequestContext> context,
                                   final FlushableDataOutput output) throws IOException {
            // Write the operation
            final ExecuteRequestContext executionContext = context.getAttachment();
            final List<InputStream> streams = executionContext.getInputStreams();
            final ModelNode operation = executionContext.getOperation();
            int inputStreamLength = 0;
            if (streams != null) {
                inputStreamLength = streams.size();
            }
            output.write(ModelControllerProtocol.PARAM_OPERATION);
            operation.writeExternal(output);
            output.write(ModelControllerProtocol.PARAM_INPUTSTREAMS_LENGTH);
            output.writeInt(inputStreamLength);
        }

        @Override
        public void handleRequest(final DataInput input, final ActiveOperation.ResultHandler<ModelNode> resultHandler, final ManagementRequestContext<ExecuteRequestContext> context) throws IOException {
            final byte responseType = input.readByte();
            final ModelNode response = new ModelNode();
            response.readExternal(input);
            // If not prepared the operation failed
            final boolean prepared = responseType == ModelControllerProtocol.PARAM_OPERATION_PREPARED;
            final ExecuteRequestContext executeRequestContext = context.getAttachment();
            if(prepared) {
                executeRequestContext.operationPrepared(new ModelController.OperationTransaction() {

                    @Override
                    public void rollback() {
                        done(false);
                    }

                    @Override
                    public void commit() {
                        done(true);
                    }

                    private void done(boolean commit) {
                        final byte status = commit ? ModelControllerProtocol.PARAM_COMMIT : ModelControllerProtocol.PARAM_ROLLBACK;
                        try {
                            // Send the CompleteTxRequest
                            channelAssociation.executeRequest(context.getOperationId(), new CompleteTxRequest(status));
                        } catch (Exception e) {
                            resultHandler.failed(e);
                        }
                    }
                }, response);
            } else {
                // Failed
                executeRequestContext.operationFailed(response);
                resultHandler.done(response);
            }
        }
    }

    /**
     * Signal the remote controller to either commit or rollback. The response has to be a
     * {@link org.jboss.as.controller.client.impl.ModelControllerProtocol#PARAM_OPERATION_COMPLETED}.
     */
    private static class CompleteTxRequest extends AbstractManagementRequest<ModelNode, ExecuteRequestContext> {

        private final byte status;

        private CompleteTxRequest(byte status) {
            this.status = status;
        }

        @Override
        public byte getOperationType() {
            return ModelControllerProtocol. COMPLETE_TX_REQUEST;
        }

        @Override
        protected void sendRequest(final ActiveOperation.ResultHandler<ModelNode> resultHandler, final ManagementRequestContext<ExecuteRequestContext> context, final FlushableDataOutput output) throws IOException {
            output.write(status);
        }

        @Override
        public void handleRequest(final DataInput input, final ActiveOperation.ResultHandler<ModelNode> resultHandler, final ManagementRequestContext<ExecuteRequestContext> context) throws IOException {
            // We only accept operationCompleted responses
            expectHeader(input, ModelControllerProtocol.PARAM_OPERATION_COMPLETED);
            final ModelNode response = new ModelNode();
            response.readExternal(input);
            // Complete the operation
            resultHandler.done(response);
        }
    }

    /**
     * Handles {@link org.jboss.as.controller.client.OperationMessageHandler#handleReport(org.jboss.as.controller.client.MessageSeverity, String)} calls
     * done in the remote target controller
     */
    private static class HandleReportRequestHandler implements ManagementRequestHandler<ModelNode, ExecuteRequestContext> {

        @Override
        public void handleRequest(final DataInput input, final ActiveOperation.ResultHandler<ModelNode> resultHandler, final ManagementRequestContext<ExecuteRequestContext> context) throws IOException {
            expectHeader(input, ModelControllerProtocol.PARAM_MESSAGE_SEVERITY);
            final MessageSeverity severity = Enum.valueOf(MessageSeverity.class, input.readUTF());
            expectHeader(input, ModelControllerProtocol.PARAM_MESSAGE);
            final String message = input.readUTF();
            expectHeader(input, ManagementProtocol.REQUEST_END);

            final ExecuteRequestContext requestContext = context.getAttachment();
            // perhaps execute async
            final OperationMessageHandler handler = requestContext.getMessageHandler();
            handler.handleReport(severity, message);
        }

    }

    /**
     * Handles reads on the inputstreams returned by {@link org.jboss.as.controller.client.OperationAttachments#getInputStreams()}
     * done in the remote target controller
     */
    private static class ReadAttachmentInputStreamRequestHandler implements ManagementRequestHandler<ModelNode, ExecuteRequestContext> {

        static final ReadAttachmentInputStreamRequestHandler INSTANCE = new ReadAttachmentInputStreamRequestHandler();

        @Override
        public void handleRequest(final DataInput input, final ActiveOperation.ResultHandler<ModelNode> resultHandler,
                              final ManagementRequestContext<ExecuteRequestContext> context) throws IOException {
            // Read the inputStream index
            expectHeader(input, ModelControllerProtocol.PARAM_INPUTSTREAM_INDEX);
            final int index = input.readInt();

            context.executeAsync(new ManagementRequestContext.AsyncTask<ExecuteRequestContext>() {
                @Override
                public void execute(final ManagementRequestContext<ExecuteRequestContext> context) throws Exception {
                    final ExecuteRequestContext exec = context.getAttachment();
                    final ManagementRequestHeader header = ManagementRequestHeader.class.cast(context.getRequestHeader());
                    final ManagementResponseHeader response = new ManagementResponseHeader(header.getVersion(), header.getRequestId(), null);
                    final InputStream is = exec.getAttachments().getInputStreams().get(index);
                    try {
                        final ByteArrayOutputStream bout = copyStream(is);
                        final FlushableDataOutput output = context.writeMessage(response);
                        try {
                            output.writeByte(ModelControllerProtocol.PARAM_INPUTSTREAM_LENGTH);
                            output.writeInt(bout.size());
                            output.writeByte(ModelControllerProtocol.PARAM_INPUTSTREAM_CONTENTS);
                            output.write(bout.toByteArray());
                            output.writeByte(ManagementProtocol.RESPONSE_END);
                            output.close();
                        } finally {
                            StreamUtils.safeClose(output);
                        }
                    } finally {
                        // the caller is responsible for closing the input streams
                        // StreamUtils.safeClose(is);
                    }
                }
            });
        }

        protected ByteArrayOutputStream copyStream(final InputStream is) throws IOException {
            final ByteArrayOutputStream bout = new ByteArrayOutputStream();
            if(is != null) {
                StreamUtils.copyStream(is, bout);
            }
            return bout;
        }

    }

    static class ExecuteRequestContext implements ActiveOperation.CompletedCallback<ModelNode> {
        final OperationWrapper<?> wrapper;
        final AtomicBoolean completed = new AtomicBoolean(false);

        ExecuteRequestContext(OperationWrapper<?> operationWrapper) {
            this.wrapper = operationWrapper;
        }

        void initialize(final AsyncFuture<ModelNode> result) {
            wrapper.future = result;
        }

        OperationMessageHandler getMessageHandler() {
            return wrapper.getMessageHandler();
        }

        ModelNode getOperation() {
            return wrapper.getOperation();
        }

        OperationAttachments getAttachments() {
            return wrapper.getAttachments();
        }

        List<InputStream> getInputStreams() {
            final OperationAttachments attachments = getAttachments();
            if(attachments == null) {
                return Collections.emptyList();
            }
            return attachments.getInputStreams();
        }

        @Override
        public synchronized void completed(final ModelNode result) {
            if(completed.compareAndSet(false, true)) {
                wrapper.completed(result);
            }
        }

        @Override
        public void failed(Exception e) {
            operationFailed(getFailureResponse(FAILED, e.getMessage()));
        }

        @Override
        public void cancelled() {
            operationFailed(getResponse(CANCELLED));
        }

        synchronized void operationFailed(final ModelNode response) {
            if(completed.compareAndSet(false, true)) {
                wrapper.failed(response);
            }
        }

        synchronized void operationPrepared(final ModelController.OperationTransaction transaction, final ModelNode result) {
            wrapper.prepared(transaction, result);
        }

    }

    private static class OperationWrapper<T extends Operation> {

        private final T operation;
        private final TransactionalOperationListener<T> listener;
        private AsyncFuture<ModelNode> future;

        OperationWrapper(TransactionalOperationListener<T> listener, T operation) {
            this.listener = listener;
            this.operation = operation;
        }

        OperationMessageHandler getMessageHandler() {
            return operation.getMessageHandler();
        }

        ModelNode getOperation() {
            return operation.getOperation();
        }

        OperationAttachments getAttachments() {
            return operation.getAttachments();
        }

        void prepared(final ModelController.OperationTransaction transaction, final ModelNode result) {
            final PreparedOperation<T> preparedOperation = new PreparedOperationImpl<T>(operation, result, future, transaction);
            listener.operationPrepared(preparedOperation);
        }

        void completed(final ModelNode response) {
            listener.operationComplete(operation, response);
        }

        void failed(final ModelNode response) {
            listener.operationFailed(operation, response);
        }

    }

    static class PreparedOperationImpl<T extends Operation> implements PreparedOperation<T> {

        private final T operation;
        private final ModelNode preparedResult;
        private final AsyncFuture<ModelNode> finalResult;
        private final ModelController.OperationTransaction transaction;

        protected PreparedOperationImpl(T operation, ModelNode preparedResult, AsyncFuture<ModelNode> finalResult, ModelController.OperationTransaction transaction) {
            assert finalResult != null : "null result";
            this.operation = operation;
            this.preparedResult = preparedResult;
            this.finalResult = finalResult;
            this.transaction = transaction;
        }

        @Override
        public T getOperation() {
            return operation;
        }

        @Override
        public ModelNode getPreparedResult() {
            return preparedResult;
        }

        @Override
        public boolean isFailed() {
            return false;
        }

        @Override
        public boolean isDone() {
            return finalResult.isDone();
        }

        @Override
        public AsyncFuture<ModelNode> getFinalResult() {
            return finalResult;
        }

        @Override
        public void commit() {
            transaction.commit();
        }

        @Override
        public void rollback() {
            transaction.rollback();
        }

    }

    static ModelNode getFailureResponse(final String outcome, final String message) {
        final ModelNode response = new ModelNode();
        response.get(OUTCOME).set(outcome);
        if(message != null) response.get(FAILURE_DESCRIPTION).set(message);
        return response;
    }

    static ModelNode getResponse(final String outcome) {
        return getFailureResponse(outcome, null);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7829.java