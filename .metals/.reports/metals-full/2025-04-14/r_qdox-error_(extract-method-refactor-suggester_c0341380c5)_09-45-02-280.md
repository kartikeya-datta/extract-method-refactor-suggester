error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8712.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8712.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8712.java
text:
```scala
final C@@hannel mgmtChannel = context.getChannel();

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.jboss.as.host.controller.mgmt;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILURE_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.HOST;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.process.protocol.ProtocolUtils.expectHeader;

import java.io.DataInput;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.jboss.as.controller.HashUtil;
import org.jboss.as.controller.ModelController;
import org.jboss.as.controller.ModelController.OperationTransactionControl;
import org.jboss.as.controller.client.OperationMessageHandler;
import org.jboss.as.controller.remote.ModelControllerClientOperationHandler;
import org.jboss.as.domain.controller.DomainController;
import org.jboss.as.domain.controller.FileRepository;
import org.jboss.as.domain.controller.UnregisteredHostChannelRegistry;
import org.jboss.as.domain.controller.UnregisteredHostChannelRegistry.ProxyCreatedCallback;
import org.jboss.as.domain.controller.operations.ReadMasterDomainModelHandler;
import org.jboss.as.domain.controller.operations.SlaveRegistrationError;
import org.jboss.as.protocol.StreamUtils;
import org.jboss.as.protocol.mgmt.ActiveOperation;
import org.jboss.as.protocol.mgmt.FlushableDataOutput;
import org.jboss.as.protocol.mgmt.ManagementChannel;
import org.jboss.as.protocol.mgmt.ManagementMessageHandler;
import org.jboss.as.protocol.mgmt.ManagementChannelReceiver;
import org.jboss.as.protocol.mgmt.ManagementProtocol;
import org.jboss.as.protocol.mgmt.ManagementProtocolHeader;
import org.jboss.as.protocol.mgmt.ManagementRequestContext;
import org.jboss.as.protocol.mgmt.ManagementRequestHandler;
import org.jboss.as.protocol.mgmt.ManagementRequestHeader;
import org.jboss.as.protocol.mgmt.ManagementResponseHeader;
import org.jboss.as.protocol.mgmt.RequestProcessingException;
import org.jboss.dmr.ModelNode;
import org.jboss.remoting3.Channel;
import org.jboss.remoting3.CloseHandler;

/**
 * Handles for requests from slave DC to master DC on the 'domain' channel.
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class MasterDomainControllerOperationHandlerImpl extends ManagementChannelReceiver {

    private final LocalOperationHandler clientHandler;
    private final ModelController controller;
    private final DomainController domainController;
    private final UnregisteredHostChannelRegistry registry;

    private volatile ManagementMessageHandler proxyHandler;

    public MasterDomainControllerOperationHandlerImpl(final ExecutorService executorService, final ModelController controller,
                                                      final UnregisteredHostChannelRegistry registry, final DomainController domainController) {
        this.domainController = domainController;
        this.controller = controller;
        this.registry = registry;
        this.clientHandler = new LocalOperationHandler(controller, executorService);
    }

    @Override
    public void handleMessage(final Channel channel, final DataInput input, final ManagementProtocolHeader header) throws IOException {
        final byte type = header.getType();
        if(type == ManagementProtocol.TYPE_REQUEST) {
            final ManagementRequestHeader request = (ManagementRequestHeader) header;
            final byte id = request.getOperationId();

            ManagementRequestHandler<ModelNode, Void> handler = clientHandler.getRequestHandler(id);
            if (handler != null) {
                clientHandler.handleMessage(channel, input, header);
            }
            switch(id) {
                case DomainControllerProtocol.REGISTER_HOST_CONTROLLER_REQUEST:
                    handler = new RegisterOperation();
                    break;
                case DomainControllerProtocol.UNREGISTER_HOST_CONTROLLER_REQUEST:
                    handler = new UnregisterOperation();
                    break;
                case DomainControllerProtocol.GET_FILE_REQUEST:
                    handler =  new GetFileOperation();
                    break;
            }
            if(handler != null) {
                clientHandler.runLocalRequestHandler(channel, input, request, handler);
            } else if (proxyHandler != null) {
                // Delegate to the proxy
                proxyHandler.handleMessage(channel, input, header);
            }

        }
    }

    private class RegisterOperation extends AbstractHostRequestHandler {
        String error;

        @Override
        void handleRequest(final String hostId, final DataInput input, final ManagementRequestContext<Void> context) throws IOException {
            context.executeAsync(new ManagementRequestContext.AsyncTask<Void>() {
                @Override
                public void execute(final ManagementRequestContext<Void> context) throws Exception {
                    try {
                        final ManagementChannel mgmtChannel = new ManagementChannel(hostId, context.getChannel());
                        registry.registerChannel(hostId, mgmtChannel, new ProxyCreatedCallback() {
                            @Override
                            public void proxyCreated(final ManagementMessageHandler handler) {
                                proxyHandler = handler;
                                mgmtChannel.addCloseHandler(new CloseHandler<Channel>() {
                                    @Override
                                    public void handleClose(Channel closed, IOException exception) {
                                        handler.shutdownNow();
                                    }
                                });
                            }
                        });


                        final ModelNode op = new ModelNode();
                        op.get(OP).set(ReadMasterDomainModelHandler.OPERATION_NAME);
                        op.get(OP_ADDR).setEmptyList();
                        op.get(HOST).set(hostId);
                        final ModelNode result = MasterDomainControllerOperationHandlerImpl.this.controller.execute(op, OperationMessageHandler.logging, OperationTransactionControl.COMMIT, null);
                        if (result.hasDefined(FAILURE_DESCRIPTION)) {
                            error = result.get(FAILURE_DESCRIPTION).asString();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        error = SlaveRegistrationError.formatHostAlreadyExists(e.getMessage());
                    }
                    final FlushableDataOutput output = writeGenericResponseHeader(context);
                    try {
                        if (error != null) {
                            output.write(DomainControllerProtocol.PARAM_ERROR);
                            output.writeUTF(SlaveRegistrationError.parse(error).toString());
                        } else {
                            output.write(DomainControllerProtocol.PARAM_OK);
                        }
                        output.close();
                    } finally {
                        StreamUtils.safeClose(output);
                    }
                }
            });
        }

    }

    private class UnregisterOperation extends AbstractHostRequestHandler {

        @Override
        void handleRequest(String hostId, DataInput input, ManagementRequestContext<Void> context) throws IOException {
            domainController.unregisterRemoteHost(hostId);
            final FlushableDataOutput os = writeGenericResponseHeader(context);
            try {
                os.write(ManagementProtocol.RESPONSE_END);
                os.close();
            } finally {
                StreamUtils.safeClose(os);
            }
        }

    }

    private class GetFileOperation extends AbstractHostRequestHandler {

        @Override
        void handleRequest(String hostId, DataInput input, ManagementRequestContext<Void> context) throws IOException {
            expectHeader(input, DomainControllerProtocol.PARAM_ROOT_ID);
            final byte rootId = input.readByte();
            expectHeader(input, DomainControllerProtocol.PARAM_FILE_PATH);
            final String filePath = input.readUTF();
            context.executeAsync(new ManagementRequestContext.AsyncTask<Void>() {
                @Override
                public void execute(ManagementRequestContext<Void> context) throws Exception {
                    final File localPath = processRequest(rootId, filePath);
                    final FlushableDataOutput output = writeGenericResponseHeader(context);
                    try {
                        writeResponse(localPath, output);
                        output.close();
                    } finally {
                        StreamUtils.safeClose(output);
                    }
                }
            });
        }

        protected File processRequest(final byte rootId, final String filePath) throws RequestProcessingException {
            final FileRepository localFileRepository = domainController.getLocalFileRepository();

            switch (rootId) {
                case DomainControllerProtocol.PARAM_ROOT_ID_FILE: {
                    return localFileRepository.getFile(filePath);
                }
                case DomainControllerProtocol.PARAM_ROOT_ID_CONFIGURATION: {
                    return localFileRepository.getConfigurationFile(filePath);
                }
                case DomainControllerProtocol.PARAM_ROOT_ID_DEPLOYMENT: {
                    byte[] hash = HashUtil.hexStringToByteArray(filePath);
                    return localFileRepository.getDeploymentRoot(hash);
                }
                default: {
                    throw new RequestProcessingException(String.format("Invalid root id [%d]", rootId));
                }
            }
        }

        protected void writeResponse(final File localPath, final FlushableDataOutput output) throws IOException {
            output.writeByte(DomainControllerProtocol.PARAM_NUM_FILES);
            if (localPath == null || !localPath.exists()) {
                output.writeInt(-1);
            } else if (localPath.isFile()) {
                output.writeInt(1);
                writeFile(localPath, localPath, output);
            } else {
                final List<File> childFiles = getChildFiles(localPath);
                output.writeInt(childFiles.size());
                for (File child : childFiles) {
                    writeFile(localPath, child, output);
                }
            }
        }

        private List<File> getChildFiles(final File base) {
            final List<File> childFiles = new ArrayList<File>();
            getChildFiles(base, childFiles);
            return childFiles;
        }

        private void getChildFiles(final File base, final List<File> childFiles) {
            for (File child : base.listFiles()) {
                if (child.isFile()) {
                    childFiles.add(child);
                } else {
                    getChildFiles(child, childFiles);
                }
            }
        }

        private String getRelativePath(final File parent, final File child) {
            return child.getAbsolutePath().substring(parent.getAbsolutePath().length());
        }

        private void writeFile(final File localPath, final File file, final FlushableDataOutput output) throws IOException {
            output.writeByte(DomainControllerProtocol.FILE_START);
            output.writeByte(DomainControllerProtocol.PARAM_FILE_PATH);
            output.writeUTF(getRelativePath(localPath, file));
            output.writeByte(DomainControllerProtocol.PARAM_FILE_SIZE);
            output.writeLong(file.length());
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
                byte[] buffer = new byte[8192];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, len);
                }
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException ignored) {
                    }
                }
            }
            output.writeByte(DomainControllerProtocol.FILE_END);
            output.flush();
        }
    }

    abstract static class AbstractHostRequestHandler implements ManagementRequestHandler<ModelNode, Void> {

        abstract void handleRequest(final String hostId, DataInput input, ManagementRequestContext<Void> context) throws IOException;

        @Override
        public void handleRequest(DataInput input, ActiveOperation.ResultHandler<ModelNode> resultHandler, ManagementRequestContext<Void> context) throws IOException {
            expectHeader(input, DomainControllerProtocol.PARAM_HOST_ID);
            final String hostId = input.readUTF();
            handleRequest(hostId, input, context);
            resultHandler.done(null);
        }

        protected FlushableDataOutput writeGenericResponseHeader(final ManagementRequestContext<Void> context) throws IOException {
            final ManagementResponseHeader header = ManagementResponseHeader.create(context.getRequestHeader());
            return context.writeMessage(header);
        }

    }

    static class LocalOperationHandler extends ModelControllerClientOperationHandler {

        LocalOperationHandler(ModelController controller, ExecutorService executorService) {
            super(controller, executorService);
        }

        protected ManagementRequestHandler<ModelNode, Void> getRequestHandler(byte operationType) {
            return super.getRequestHandler(operationType);
        }

        @Override
        protected ManagementRequestHandler<ModelNode, Void> getFallbackHandler() {
            // Return null here
            return null;
        }

       void runLocalRequestHandler(final Channel channel, final DataInput input, final ManagementRequestHeader header, final ManagementRequestHandler<ModelNode, Void> handler) {
           final ActiveOperation<ModelNode, Void> support = super.registerActiveOperation(header.getBatchId(), null);
           super.handleMessage(channel, input, header, support, handler);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8712.java