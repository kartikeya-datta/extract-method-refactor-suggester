error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10917.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10917.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10917.java
text:
```scala
final F@@ileRepository localFileRepository = domainController.getLocalFileRepository();

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
import org.jboss.as.controller.remote.AbstractModelControllerOperationHandler;
import org.jboss.as.controller.remote.ModelControllerClientOperationHandler;
import org.jboss.as.domain.controller.DomainController;
import org.jboss.as.domain.controller.FileRepository;
import org.jboss.as.domain.controller.UnregisteredHostChannelRegistry;
import org.jboss.as.domain.controller.UnregisteredHostChannelRegistry.ProxyCreatedCallback;
import org.jboss.as.domain.controller.operations.ReadMasterDomainModelHandler;
import org.jboss.as.domain.controller.operations.SlaveRegistrationError;
import org.jboss.as.protocol.mgmt.FlushableDataOutput;
import org.jboss.as.protocol.mgmt.ManagementOperationHandler;
import org.jboss.as.protocol.mgmt.ManagementRequestHandler;
import org.jboss.as.protocol.mgmt.RequestProcessingException;
import org.jboss.dmr.ModelNode;

/**
 * Handles for requests from slave DC to master DC on the 'domain' channel.
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class MasterDomainControllerOperationHandlerImpl extends AbstractModelControllerOperationHandler {

    private final ModelControllerClientOperationHandler clientHandler;
    private volatile ManagementOperationHandler proxyHandler;

    private final DomainController domainController;
    private final UnregisteredHostChannelRegistry registry;

    public MasterDomainControllerOperationHandlerImpl(final ExecutorService executorService, final ModelController controller, final UnregisteredHostChannelRegistry registry, final DomainController domainController) {
        super(executorService, controller);
        this.domainController = domainController;
        this.registry = registry;
        this.clientHandler = new ModelControllerClientOperationHandler(executorService, controller);
    }

    @Override
    public ManagementRequestHandler getRequestHandler(byte id) {
        ManagementRequestHandler handler = clientHandler.getRequestHandler(id);
        if (handler != null) {
            return handler;
        }
        if (proxyHandler != null) {
            handler = proxyHandler.getRequestHandler(id);
            if (handler != null) {
                return handler;
            }
        }
        switch (id) {
        case DomainControllerProtocol.REGISTER_HOST_CONTROLLER_REQUEST:
            return new RegisterOperation();
        case DomainControllerProtocol.UNREGISTER_HOST_CONTROLLER_REQUEST:
            return new UnregisterOperation();
        case DomainControllerProtocol.GET_FILE_REQUEST:
            return new GetFileOperation();
        }
        return null;
    }

    private abstract class RegistryOperation extends ManagementRequestHandler {
        String hostId;

        RegistryOperation() {
        }

        @Override
        protected void readRequest(final DataInput input) throws IOException {
            expectHeader(input, DomainControllerProtocol.PARAM_HOST_ID);
            hostId = input.readUTF();
        }
    }

    private class RegisterOperation extends RegistryOperation {
        String error;
        @Override
        protected void readRequest(final DataInput input) throws IOException {
            expectHeader(input, DomainControllerProtocol.PARAM_HOST_ID);
            hostId = input.readUTF();
        }


        @Override
        protected void processRequest() throws RequestProcessingException {
            try {
                registry.registerChannel(hostId, getChannel(), new ProxyCreatedCallback() {
                    @Override
                    public void proxyCreated(ManagementOperationHandler handler) {
                        proxyHandler = handler;
                    }
                });

                ModelNode op = new ModelNode();
                op.get(OP).set(ReadMasterDomainModelHandler.OPERATION_NAME);
                op.get(OP_ADDR).setEmptyList();
                op.get(HOST).set(hostId);
                ModelNode result = controller.execute(op, OperationMessageHandler.logging, OperationTransactionControl.COMMIT, null);
                if (result.hasDefined(FAILURE_DESCRIPTION)) {
                    error = result.get(FAILURE_DESCRIPTION).asString();
                }
            } catch (Exception e) {
                e.printStackTrace();
                error = e.getMessage();
            }
        }


        @Override
        protected void writeResponse(final FlushableDataOutput output) throws IOException {
            if (error != null) {
                output.write(DomainControllerProtocol.PARAM_ERROR);
                output.writeUTF(SlaveRegistrationError.parse(error).toString());
            } else {
                output.write(DomainControllerProtocol.PARAM_OK);
            }
        }
    }

    private class UnregisterOperation extends RegistryOperation {
        @Override
        protected void processRequest() throws RequestProcessingException {
            domainController.unregisterRemoteHost(hostId);
        }
    }

    private class GetFileOperation extends RegistryOperation {
        private File localPath;
        private byte rootId;
        private String filePath;

        @Override
        protected void readRequest(final DataInput input) throws IOException {
            expectHeader(input, DomainControllerProtocol.PARAM_ROOT_ID);
            rootId = input.readByte();
            expectHeader(input, DomainControllerProtocol.PARAM_FILE_PATH);
            filePath = input.readUTF();
        }

        @Override
        protected void processRequest() throws RequestProcessingException {
            final FileRepository localFileRepository = domainController.getFileRepository();

            switch (rootId) {
                case DomainControllerProtocol.PARAM_ROOT_ID_FILE: {
                    localPath = localFileRepository.getFile(filePath);
                    break;
                }
                case DomainControllerProtocol.PARAM_ROOT_ID_CONFIGURATION: {
                    localPath = localFileRepository.getConfigurationFile(filePath);
                    break;
                }
                case DomainControllerProtocol.PARAM_ROOT_ID_DEPLOYMENT: {
                    byte[] hash = HashUtil.hexStringToByteArray(filePath);
                    localPath = localFileRepository.getDeploymentRoot(hash);
                    break;
                }
                default: {
                    throw new RequestProcessingException(String.format("Invalid root id [%d]", rootId));
                }
            }
        }

        @Override
        protected void writeResponse(final FlushableDataOutput output) throws IOException {
            output.writeByte(DomainControllerProtocol.PARAM_NUM_FILES);
            if (localPath == null || !localPath.exists()) {
                output.writeInt(-1);
            } else if (localPath.isFile()) {
                output.writeInt(1);
                writeFile(localPath, output);
            } else {
                final List<File> childFiles = getChildFiles(localPath);
                output.writeInt(childFiles.size());
                for (File child : childFiles) {
                    writeFile(child, output);
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

        private void writeFile(final File file, final FlushableDataOutput output) throws IOException {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10917.java