error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15253.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15253.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15253.java
text:
```scala
u@@nregister();

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

package org.jboss.as.host.controller;

import static org.jboss.as.protocol.old.ProtocolUtils.expectHeader;

import java.io.BufferedOutputStream;
import java.io.DataInput;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jboss.as.controller.HashUtil;
import org.jboss.as.controller.NewModelController;
import org.jboss.as.controller.client.NewModelControllerClient;
import org.jboss.as.controller.client.NewOperation;
import org.jboss.as.controller.client.OperationMessageHandler;
import org.jboss.as.controller.remote.NewTransactionalModelControllerOperationHandler;
import org.jboss.as.domain.controller.FileRepository;
import org.jboss.as.domain.controller.NewMasterDomainControllerClient;
import org.jboss.as.host.controller.mgmt.NewDomainControllerProtocol;
import org.jboss.as.protocol.ProtocolChannelClient;
import org.jboss.as.protocol.mgmt.FlushableDataOutput;
import org.jboss.as.protocol.mgmt.ManagementChannel;
import org.jboss.as.protocol.mgmt.ManagementChannelFactory;
import org.jboss.as.protocol.mgmt.ManagementClientChannelStrategy;
import org.jboss.as.protocol.mgmt.ManagementRequest;
import org.jboss.as.protocol.old.Connection.ClosedCallback;
import org.jboss.as.remoting.RemotingServices;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.jboss.remoting3.Channel;
import org.jboss.remoting3.CloseHandler;
import org.jboss.remoting3.Endpoint;
import org.jboss.threads.AsyncFuture;
import org.jboss.threads.AsyncFutureTask;

/**
 * Establishes the connection from a slave {@link org.jboss.as.domain.controller.NewDomainController} to the master
 * {@link org.jboss.as.domain.controller.NewDomainController}
 *
 * @author Kabir Khan
 */
public class NewRemoteDomainConnectionService implements NewMasterDomainControllerClient, Service<NewMasterDomainControllerClient>, ClosedCallback {

    private static final Logger log = Logger.getLogger("org.jboss.as.domain.controller");
    private final NewModelController controller;
    private final InetAddress host;
    private final int port;
    private final String name;
    private final RemoteFileRepository remoteFileRepository;

    private volatile ProtocolChannelClient<ManagementChannel> channelClient;
    /** Used to invoke ModelController ops on the master */
    private volatile NewModelControllerClient masterProxy;
    /** Handler for transactional operations */
    private volatile NewTransactionalModelControllerOperationHandler txOperationHandler;
    private final AtomicBoolean shutdown = new AtomicBoolean();
    private volatile ManagementChannel channel;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final AtomicBoolean connected = new AtomicBoolean(false);
    private final FutureClient futureClient = new FutureClient();
    private final InjectedValue<Endpoint> endpointInjector = new InjectedValue<Endpoint>();

    private NewRemoteDomainConnectionService(final NewModelController controller, final String name, final InetAddress host, final int port, final RemoteFileRepository remoteFileRepository){
        this.controller = controller;
        this.name = name;
        this.host = host;
        this.port = port;
        this.remoteFileRepository = remoteFileRepository;
        remoteFileRepository.setRemoteFileRepositoryExecutor(remoteFileRepositoryExecutor);
    }

    public static Future<NewMasterDomainControllerClient> install(final ServiceTarget serviceTarget, final NewModelController controller, final String localHostName, final String remoteDcHost, final int remoteDcPort, final RemoteFileRepository remoteFileRepository) {
        NewRemoteDomainConnectionService service;
        try {
            service = new NewRemoteDomainConnectionService(
                    controller,
                    localHostName,
                    InetAddress.getByName(remoteDcHost),
                    remoteDcPort,
                    remoteFileRepository);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        serviceTarget.addService(NewMasterDomainControllerClient.SERVICE_NAME, service)
                .addDependency(RemotingServices.ENDPOINT, Endpoint.class, service.endpointInjector)
                .setInitialMode(ServiceController.Mode.ACTIVE)
                .install();
        return service.futureClient;
    }

    /** {@inheritDoc} */
    public void register() {
        // TODO egregious hack. Fix properly as part of AS7-794
        IllegalStateException ise = null;
        boolean connected = false;
        long timeout = System.currentTimeMillis() + 5000;
        while (!connected && System.currentTimeMillis() < timeout) {
            try {
               connect();
               connected = true;
            }
            catch (IllegalStateException e) {
                ise = e;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException inter) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("Interrupted while trying to connect to master", inter);
                }
            }
        }

        if (!connected) {
            throw (ise != null) ? ise : new IllegalStateException("Could not connect to master within 5000 ms");
        }

        this.connected.set(true);
    }

    private synchronized void connect() {
        txOperationHandler = new NewTransactionalModelControllerOperationHandler(executor, controller);
        ProtocolChannelClient<ManagementChannel> client;
        try {
            ProtocolChannelClient.Configuration<ManagementChannel> configuration = new ProtocolChannelClient.Configuration<ManagementChannel>();
            configuration.setEndpoint(endpointInjector.getValue());
            configuration.setUri(new URI("remote://" + host.getHostAddress() + ":" + port));
            configuration.setChannelFactory(new ManagementChannelFactory(txOperationHandler));
            client = ProtocolChannelClient.create(configuration);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            client.connect();
            this.channelClient = client;

            if (connected.get()) {
                unregister();
            }

            ManagementChannel channel = client.openChannel(RemotingServices.DOMAIN_CHANNEL);
            this.channel = channel;

            channel.addCloseHandler(new CloseHandler<Channel>() {
                public void handleClose(Channel closed) {
                    connectionClosed();
                }
            });

            channel.startReceiving();

            masterProxy = NewModelControllerClient.Factory.create(channel);
        } catch (IOException e) {
            log.warnf("Could not connect to remote domain controller %s:%d", host.getHostAddress(), port);
            //TODO remove this line
            e.printStackTrace();
            throw new IllegalStateException(e);
        }

        try {
            final String error = new RegisterModelControllerRequest().executeForResult(executor, ManagementClientChannelStrategy.create(channel));
            if (error != null) {
                throw new Exception(error);
            }
        } catch (Exception e) {
            log.warnf("Error retrieving domain model from remote domain controller %s:%d: %s", host.getHostAddress(), port, e.getMessage());
            throw new IllegalStateException(e);
        }
    }

    /** {@inheritDoc} */
    public synchronized void unregister() {
        try {
            new UnregisterModelControllerRequest().executeForResult(executor, ManagementClientChannelStrategy.create(channel));
        } catch (Exception e) {
            log.errorf(e, "Error unregistering from master");
        }
        finally {
            channel.stopReceiving();

            channelClient.close();
        }
    }

    /** {@inheritDoc} */
    public synchronized FileRepository getRemoteFileRepository() {
        return remoteFileRepository;
    }

    @Override
    public ModelNode execute(ModelNode operation) throws IOException {
        return execute(operation, OperationMessageHandler.logging);
    }

    @Override
    public ModelNode execute(NewOperation operation) throws IOException {
        return masterProxy.execute(operation, OperationMessageHandler.logging);
    }

    @Override
    public ModelNode execute(ModelNode operation, OperationMessageHandler messageHandler) throws IOException {
        return masterProxy.execute(operation, messageHandler);
    }

    @Override
    public ModelNode execute(NewOperation operation, OperationMessageHandler messageHandler) throws IOException {
        return masterProxy.execute(operation, messageHandler);
    }

    @Override
    public AsyncFuture<ModelNode> executeAsync(ModelNode operation, OperationMessageHandler messageHandler) {
        return masterProxy.executeAsync(operation, messageHandler);
    }

    @Override
    public AsyncFuture<ModelNode> executeAsync(NewOperation operation, OperationMessageHandler messageHandler) {
        return masterProxy.executeAsync(operation, messageHandler);
    }

    @Override
    public void close() throws IOException {
        throw new UnsupportedOperationException("Close should be managed by the service");
    }


    /** {@inheritDoc} */
    @Override
    public synchronized void start(StartContext context) throws StartException {
        futureClient.setClient(this);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void stop(StopContext context) {
        shutdown.set(true);
        if (channelClient != null) {
            channelClient.close();
        }
    }

    @Override
    public void connectionClosed() {

        if (!connected.get()) {
            log.error("Null reconnect info, cannot try to reconnect");
            return;
        }

        if (!shutdown.get()) {
            //The remote host went down, try reconnecting
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                    }

                    while (!shutdown.get()) {
                        log.debug("Attempting reconnection to master...");
                        try {
                            connect();
                            log.info("Connected to master");
                            break;
                        } catch (Exception e) {
                        }
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }).start();
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized NewMasterDomainControllerClient getValue() throws IllegalStateException, IllegalArgumentException {
        return this;
    }

    private abstract class RegistryRequest<T> extends ManagementRequest<T>{

        protected T readResponse(DataInput input) throws IOException {
            return null;
        }
    }

    private class RegisterModelControllerRequest extends RegistryRequest<String> {

        RegisterModelControllerRequest() {
        }

        @Override
        protected byte getRequestCode() {
            return NewDomainControllerProtocol.REGISTER_HOST_CONTROLLER_REQUEST;
        }

        /** {@inheritDoc} */
        @Override
        protected void writeRequest(final int protocolVersion, final FlushableDataOutput output) throws IOException {
            output.write(NewDomainControllerProtocol.PARAM_HOST_ID);
            output.writeUTF(name);
        }

        /** {@inheritDoc} */
        @Override
        protected String readResponse(DataInput input) throws IOException {
            byte status = input.readByte();
            if (status == NewDomainControllerProtocol.PARAM_OK) {
                return null;
            } else {
                return input.readUTF();
            }
        }
    }

    private class UnregisterModelControllerRequest extends RegistryRequest<Void> {
        @Override
        protected byte getRequestCode() {
            return NewDomainControllerProtocol.UNREGISTER_HOST_CONTROLLER_REQUEST;
        }

        /** {@inheritDoc} */
        @Override
        protected void writeRequest(final int protocolVersion, final FlushableDataOutput output) throws IOException {
            output.write(NewDomainControllerProtocol.PARAM_HOST_ID);
            output.writeUTF(name);
        }
    }

    private class GetFileRequest extends RegistryRequest<File> {
        private final byte rootId;
        private final String filePath;
        private final FileRepository localFileRepository;

        private GetFileRequest(final byte rootId, final String filePath, final FileRepository localFileRepository) {
            this.rootId = rootId;
            this.filePath = filePath;
            this.localFileRepository = localFileRepository;
        }

        @Override
        public final byte getRequestCode() {
            return NewDomainControllerProtocol.GET_FILE_REQUEST;
        }

        @Override
        protected final void writeRequest(final int protocolVersion, final FlushableDataOutput output) throws IOException {
            super.writeRequest(protocolVersion, output);
            log.debugf("Requesting files for path %s", filePath);
            output.writeByte(NewDomainControllerProtocol.PARAM_ROOT_ID);
            output.writeByte(rootId);
            output.writeByte(NewDomainControllerProtocol.PARAM_FILE_PATH);
            output.writeUTF(filePath);
        }

        @Override
        protected final File readResponse(final DataInput input) throws IOException {
            final File localPath;
            switch (rootId) {
                case NewDomainControllerProtocol.PARAM_ROOT_ID_FILE: {
                    localPath = localFileRepository.getFile(filePath);
                    break;
                }
                case NewDomainControllerProtocol.PARAM_ROOT_ID_CONFIGURATION: {
                    localPath = localFileRepository.getConfigurationFile(filePath);
                    break;
                }
                case NewDomainControllerProtocol.PARAM_ROOT_ID_DEPLOYMENT: {
                    byte[] hash = HashUtil.hexStringToByteArray(filePath);
                    localPath = localFileRepository.getDeploymentRoot(hash);
                    break;
                }
                default: {
                    localPath = null;
                }
            }
            expectHeader(input, NewDomainControllerProtocol.PARAM_NUM_FILES);
            int numFiles = input.readInt();
            log.debugf("Received %d files for %s", numFiles, localPath);
            switch (numFiles) {
                case -1: { // Not found on DC
                    break;
                }
                case 0: { // Found on DC, but was an empty dir
                    if (!localPath.mkdirs()) {
                        throw new IOException("Unable to create local directory: " + localPath);
                    }
                    break;
                }
                default: { // Found on DC
                    for (int i = 0; i < numFiles; i++) {
                        expectHeader(input, NewDomainControllerProtocol.FILE_START);
                        expectHeader(input, NewDomainControllerProtocol.PARAM_FILE_PATH);
                        final String path = input.readUTF();
                        expectHeader(input, NewDomainControllerProtocol.PARAM_FILE_SIZE);
                        final long length = input.readLong();
                        log.debugf("Received file [%s] of length %d", path, length);
                        final File file = new File(localPath, path);
                        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                            throw new IOException("Unable to create local directory " + localPath.getParent());
                        }
                        long totalRead = 0;
                        OutputStream fileOut = null;
                        try {
                            fileOut = new BufferedOutputStream(new FileOutputStream(file));
                            final byte[] buffer = new byte[8192];
                            while (totalRead < length) {
                                int len = Math.min((int) (length - totalRead), buffer.length);
                                input.readFully(buffer, 0, len);
                                fileOut.write(buffer, 0, len);
                                totalRead += len;
                            }
                        } finally {
                            if (fileOut != null) {
                                fileOut.close();
                            }
                        }
                        if (totalRead != length) {
                            throw new IOException("Did not read the entire file. Missing: " + (length - totalRead));
                        }

                        expectHeader(input, NewDomainControllerProtocol.FILE_END);
                    }
                }
            }
            return localPath;
        }
    }

    static class RemoteFileRepository implements FileRepository {
        private final FileRepository localFileRepository;
        private volatile RemoteFileRepositoryExecutor remoteFileRepositoryExecutor;

        RemoteFileRepository(final FileRepository localFileRepository) {
            this.localFileRepository = localFileRepository;
        }

        @Override
        public final File getFile(String relativePath) {
            return getFile(relativePath, NewDomainControllerProtocol.PARAM_ROOT_ID_FILE);
        }

        @Override
        public final File getConfigurationFile(String relativePath) {
            return getFile(relativePath, NewDomainControllerProtocol.PARAM_ROOT_ID_CONFIGURATION);
        }

        @Override
        public final File[] getDeploymentFiles(byte[] deploymentHash) {
            String hex = deploymentHash == null ? "" : HashUtil.bytesToHexString(deploymentHash);
            return getFile(hex, NewDomainControllerProtocol.PARAM_ROOT_ID_DEPLOYMENT).listFiles();
        }

        @Override
        public File getDeploymentRoot(byte[] deploymentHash) {
            String hex = deploymentHash == null ? "" : HashUtil.bytesToHexString(deploymentHash);
            return getFile(hex, NewDomainControllerProtocol.PARAM_ROOT_ID_DEPLOYMENT);
        }

        private File getFile(final String relativePath, final byte repoId) {
            return remoteFileRepositoryExecutor.getFile(relativePath, repoId, localFileRepository);
        }

        private void setRemoteFileRepositoryExecutor(RemoteFileRepositoryExecutor remoteFileRepositoryExecutor) {
            this.remoteFileRepositoryExecutor = remoteFileRepositoryExecutor;
        }
    }

    private static interface RemoteFileRepositoryExecutor {
        File getFile(final String relativePath, final byte repoId, FileRepository localFileRepository);
    }

    private RemoteFileRepositoryExecutor remoteFileRepositoryExecutor = new RemoteFileRepositoryExecutor() {
        public File getFile(final String relativePath, final byte repoId, FileRepository localFileRepository) {
            try {
                return new GetFileRequest(repoId, relativePath, localFileRepository).executeForResult(executor, ManagementClientChannelStrategy.create(channel));
            } catch (Exception e) {
                throw new RuntimeException("Failed to get file from remote repository", e);
            }
        }
    };

    private class FutureClient extends AsyncFutureTask<NewMasterDomainControllerClient>{

        protected FutureClient() {
            super(null);
        }

        private void setClient(NewMasterDomainControllerClient client) {
            super.setResult(client);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15253.java