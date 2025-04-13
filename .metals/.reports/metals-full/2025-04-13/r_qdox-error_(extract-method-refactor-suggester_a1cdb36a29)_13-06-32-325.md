error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3116.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3116.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3116.java
text:
```scala
final M@@anagementResponseProtocolHeader responseHeader = new ManagementResponseProtocolHeader(workingVersion, requestHeader.getRequestId());

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

package org.jboss.as.server.manager.management;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import org.jboss.as.communication.InitialSocketRequestException;
import org.jboss.as.communication.SocketConnection;
import org.jboss.as.communication.SocketListener;
import org.jboss.as.communication.SocketListener.SocketHandler;
import org.jboss.as.services.net.NetworkInterfaceBinding;
import org.jboss.logging.Logger;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.SimpleDataInput;
import org.jboss.marshalling.SimpleDataOutput;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;

/**
 * Service responsible for accepting remote communication to server manager processes.  This will wait on a {@link java.net.ServerSocket}
 * for requests and will and the requesting socket over to a {@link org.jboss.as.server.manager.management.ManagementOperationHandler} to
 * process the request.
 *
 * @author John E. Bailey
 */
public class ManagementCommunicationService implements Service<ManagementCommunicationService> {
    public static final ServiceName SERVICE_NAME = ServiceName.JBOSS.append("server", "manager", "management", "communication");
    private static final Logger log = Logger.getLogger("org.jboss.as.domain.controller");
    private final InjectedValue<NetworkInterfaceBinding> interfaceBindingValue = new InjectedValue<NetworkInterfaceBinding>();
    private final InjectedValue<Integer> portValue = new InjectedValue<Integer>();
    private final InjectedValue<ExecutorService> executorServiceValue = new InjectedValue<ExecutorService>();
    private final ConcurrentMap<Byte, ManagementOperationHandler> handlers = new ConcurrentHashMap<Byte, ManagementOperationHandler>();
    private SocketListener socketListener;


    /**
     * Starts the service.  Will start a socket listener to listen for management operation requests.
     *
     * @param context The start context
     * @throws StartException If any errors occur
     */
    public synchronized void start(StartContext context) throws StartException {
        final ExecutorService executorService = executorServiceValue.getValue();
        final NetworkInterfaceBinding interfaceBinding = interfaceBindingValue.getValue();
        final Integer port = portValue.getValue();
        try {
            socketListener = SocketListener.createSocketListener("SM-Management", new DomainControllerSocketHandler(executorService), interfaceBinding.getAddress(), port, 20);
            socketListener.start();
        } catch (Exception e) {
            throw new StartException("Failed to start server socket", e);
        }
    }

    /**
     * Stops the service.  Will shutdown the socket listener and will no longer accept requests.
     *
     * @param context
     */
    public synchronized void stop(StopContext context) {
        if (socketListener != null) {
            socketListener.shutdown();
        }
    }

    /** {@inheritDoc} */
    public ManagementCommunicationService getValue() throws IllegalStateException {
        return this;
    }

    /**
     * Get the interface binding injector.
     *
     * @return The injector
     */
    public Injector<NetworkInterfaceBinding> getInterfaceInjector() {
        return interfaceBindingValue;
    }

    /**
     * Get the executor serice injector.
     *
     * @return The injector
     */
    public Injector<ExecutorService> getExecutorServiceInjector() {
        return executorServiceValue;
    }

    /**
     * Get the management port injector.
     *
     * @return The injector
     */
    public Injector<Integer> getPortInjector() {
        return portValue;
    }

    void addHandler(ManagementOperationHandler handler) {
        if (handlers.putIfAbsent(handler.getIdentifier(), handler) != null) {
            // TODO: Handle
        }
    }

    void removeHandler(ManagementOperationHandler handler) {
        if (!handlers.remove(handler.getIdentifier(), handler)) {
            // TODO: Handle
        }
    }

    private class DomainControllerSocketHandler implements SocketHandler {
        private final ExecutorService executorService;

        private DomainControllerSocketHandler(final ExecutorService executorService) {
            this.executorService = executorService;
        }

        @Override
        public void initializeConnection(final Socket socket) throws IOException, InitialSocketRequestException {
            executorService.execute(new RequestTask(SocketConnection.accepted(socket)));
        }
    }

    private class RequestTask implements Runnable {
        private final SocketConnection socketConnection;

        private RequestTask(final SocketConnection socketConnection) {
            this.socketConnection = socketConnection;
        }

        public void run() {
            final InputStream socketIn = socketConnection.getInputStream();
            final OutputStream socketOut = socketConnection.getOutputStream();
            try {
                final SimpleDataInput input = new SimpleDataInput(Marshalling.createByteInput(socketIn));
                final SimpleDataOutput output =  new SimpleDataOutput(Marshalling.createByteOutput(socketOut));

                // Start by reading the request header
                final ManagementRequestProtocolHeader requestHeader = new ManagementRequestProtocolHeader(input);

                // Work with the lowest protocol version
                int workingVersion = Math.min(ManagementProtocol.VERSION, requestHeader.getVersion());

                // Now write the response header
                final ManagementResponseProtocolHeader responseHeader = new ManagementResponseProtocolHeader(workingVersion);
                responseHeader.write(output);
                output.flush();

                byte handlerId = requestHeader.getOperationHandlerId();
                if (handlerId == -1) {
                    throw new ManagementOperationException("Management request failed.  Invalid handler id");
                }
                final ManagementOperationHandler handler = handlers.get(handlerId);
                if (handler == null) {
                    throw new ManagementOperationException("Management request failed.  NO handler found for id" + handlerId);
                }
                handler.handleRequest(workingVersion, input, output);
            } catch (Exception e) {
                log.error("Failed to process management request", e);
            } finally {
                socketConnection.close();
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3116.java